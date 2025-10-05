package com.fosscut.alg.cg;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.exception.LPUnfeasibleException;
import com.fosscut.shared.type.IntegerSolver;
import com.fosscut.shared.type.OptimizationCriterion;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.type.cutting.order.OrderOutput;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPSolver.ResultStatus;
import com.google.ortools.linearsolver.MPVariable;

class PatternGeneration extends ColumnGenerationLPTask {

    private static final Logger logger = LoggerFactory.getLogger(PatternGeneration.class);

    private int inputId;
    private List<Double> cuttingPlanDualValues;
    private Double relaxCost;
    private boolean relaxEnabled;
    private IntegerSolver integerSolver;
    private OptimizationCriterion optimizationCriterion;

    private List<MPVariable> usageVariables;
    private List<MPVariable> relaxVariables;

    public PatternGeneration(
        Order order,
        int inputId,
        List<Double> cuttingPlanDualValues,
        Double relaxCost,
        boolean relaxEnabled,
        IntegerSolver integerSolver,
        OptimizationCriterion optimizationCriterion
    ) {
        setOrder(order);
        this.inputId = inputId;
        this.cuttingPlanDualValues = cuttingPlanDualValues;
        this.relaxCost = relaxCost;
        this.relaxEnabled = relaxEnabled;
        this.integerSolver = integerSolver;
        this.optimizationCriterion = optimizationCriterion;
    }

    public List<MPVariable> getUsageVariables() {
        return usageVariables;
    }

    public void setUsageVariables(List<MPVariable> usageVariables) {
        this.usageVariables = usageVariables;
    }

    public List<MPVariable> getRelaxVariables() {
        return relaxVariables;
    }

    public void setRelaxVariables(List<MPVariable> relaxVariables) {
        this.relaxVariables = relaxVariables;
    }

    public void solve() throws LPUnfeasibleException {
        logger.info("");
        logger.info("Starting pattern generation...");

        setSolver(MPSolver.createSolver(integerSolver.toString()));

        if (relaxEnabled) initModelWithRelaxation();
        else initModel();

        final ResultStatus resultStatus = getSolver().solve();
        printSolution(resultStatus);
    }

    private void initModel() {
        initVariables();
        initConstraints();
        if (optimizationCriterion == OptimizationCriterion.MIN_WASTE_EXPERIMENTAL) {
            initExperimentalWasteObjective();
        } else initObjective();
    }

    private void initModelWithRelaxation() {
        initVariablesWithRelaxation();
        initConstraintsWithRelaxation();
        if (optimizationCriterion == OptimizationCriterion.MIN_WASTE_EXPERIMENTAL) {
            initExperimentalWasteObjectiveWithRelaxation();
        } else initObjectiveWithRelaxation();
    }

    private void initVariables() {
        setUsageVariables(defineVariables("usage"));
    }

    private void initVariablesWithRelaxation() {
        initVariables();
        setRelaxVariables(defineVariables("relax"));
    }

    private List<MPVariable> defineVariables(String varName) {
        List<MPVariable> variables = new ArrayList<>();
        for (int o = 0; o < getOrder().getOutputs().size(); o++) {
            variables.add(getSolver().makeIntVar(
                0,
                Double.POSITIVE_INFINITY,
                varName + "_o_" + o
            ));
        }
        return variables;
    }

    private void initConstraints() {
        MPConstraint usageConstraint = getSolver().makeConstraint(
            -Double.POSITIVE_INFINITY, getOrder().getInputs().get(inputId).getLength(), "Length_i_" + inputId);
        for (int o = 0; o < getOrder().getOutputs().size(); o++) {
            usageConstraint.setCoefficient(usageVariables.get(o), getOrder().getOutputs().get(o).getLength());
        }
    }

    private void initConstraintsWithRelaxation() {
        MPConstraint usageConstraint = getSolver().makeConstraint(
            -Double.POSITIVE_INFINITY, getOrder().getInputs().get(inputId).getLength(), "Length_i_" + inputId);
        for (int o = 0; o < getOrder().getOutputs().size(); o++) {
            usageConstraint.setCoefficient(usageVariables.get(o), getOrder().getOutputs().get(o).getLength());
            usageConstraint.setCoefficient(relaxVariables.get(o), -1);

            MPConstraint relaxConstraint = getSolver().makeConstraint(0, Double.POSITIVE_INFINITY, "Relax_o_" + o);
            relaxConstraint.setCoefficient(usageVariables.get(o), getOrder().getOutputs().get(o).getMaxRelax());
            relaxConstraint.setCoefficient(relaxVariables.get(o), -1);
        }
    }

    private void initObjective() {
        MPObjective objective = getSolver().objective();
        for (int o = 0; o < getOrder().getOutputs().size(); o++) {
            objective.setCoefficient(usageVariables.get(o), cuttingPlanDualValues.get(o));
        }
        objective.setMaximization();
        setObjective(objective);
    }

    // Gives worse results in practice
    @Deprecated
    private void initExperimentalWasteObjective() {
        MPObjective objective = getSolver().objective();
        objective.setOffset(getOrder().getInputs().get(inputId).getLength());
        for (int o = 0; o < getOrder().getOutputs().size(); o++) {
            objective.setCoefficient(usageVariables.get(o), -cuttingPlanDualValues.get(o));
            objective.setCoefficient(usageVariables.get(o), -getOrder().getOutputs().get(o).getLength());
        }
        objective.setMinimization();
        setObjective(objective);
    }

    private void initObjectiveWithRelaxation() {
        MPObjective objective = getSolver().objective();
        for (int o = 0; o < getOrder().getOutputs().size(); o++) {
            OrderOutput output = getOrder().getOutputs().get(o);
            Double localOutputRelaxCost = output.getRelaxCost() != null ? output.getRelaxCost() : relaxCost;
            objective.setCoefficient(usageVariables.get(o), cuttingPlanDualValues.get(o));
            objective.setCoefficient(relaxVariables.get(o), -localOutputRelaxCost);
        }
        objective.setMaximization();
        setObjective(objective);
    }

    // Gives similar results in practice
    // but adds unnecessary complexity for the solver
    @Deprecated
    private void initExperimentalWasteObjectiveWithRelaxation() {
        MPObjective objective = getSolver().objective();
        objective.setOffset(getOrder().getInputs().get(inputId).getLength());

        for (int o = 0; o < getOrder().getOutputs().size(); o++) {
            OrderOutput output = getOrder().getOutputs().get(o);
            Double localOutputRelaxCost = output.getRelaxCost() != null ? output.getRelaxCost() : relaxCost;
            objective.setCoefficient(usageVariables.get(o), -cuttingPlanDualValues.get(o) -getOrder().getOutputs().get(o).getLength());
            objective.setCoefficient(relaxVariables.get(o), localOutputRelaxCost + 1);
        }

        objective.setMinimization();
        setObjective(objective);
    }

}
