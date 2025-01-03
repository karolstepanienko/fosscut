package com.fosscut.alg.cg;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.type.cutting.order.Order;
import com.fosscut.util.Defaults;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPSolver.ResultStatus;
import com.google.ortools.linearsolver.MPVariable;

class PatternGeneration extends LPTask {
    private List<Double> cuttingPlanDualValues;
    private Double relaxCost;
    private boolean integerRelax;
    private boolean quietModeRequested;

    private List<List<MPVariable>> usageVariables;
    private List<List<MPVariable>> relaxVariables;

    public PatternGeneration(Order order, List<Double> cuttingPlanDualValues, Double relaxCost, boolean integerRelax, boolean quietModeRequested) {
        setOrder(order);
        this.cuttingPlanDualValues = cuttingPlanDualValues;
        this.relaxCost = relaxCost;
        this.integerRelax = integerRelax;
        this.quietModeRequested = quietModeRequested;
    }

    public List<List<MPVariable>> getUsageVariables() {
        return usageVariables;
    }

    public void setUsageVariables(List<List<MPVariable>> usageVariables) {
        this.usageVariables = usageVariables;
    }

    public List<List<MPVariable>> getRelaxVariables() {
        return relaxVariables;
    }

    public void setRelaxVariables(List<List<MPVariable>> relaxVariables) {
        this.relaxVariables = relaxVariables;
    }

    public void solve() {
        if (!quietModeRequested) printIntro();

        setSolver(MPSolver.createSolver(Defaults.INTEGER_SOLVER));
        if (relaxCost == null) initModel();
        else initModelWithRelaxation();
        final ResultStatus resultStatus = getSolver().solve();

        if (!quietModeRequested) printSolution(resultStatus);
    }

    private void printIntro() {
        System.out.println("");
        System.out.println("Starting pattern generation...");
    }

    private void initModel() {
        initVariables();
        initConstraints();
        initObjective();
    }

    private void initModelWithRelaxation() {
        initVariablesWithRelaxation();
        initConstraintsWithRelaxation();
        initObjectiveWithRelaxation();
    }

    private void initVariables() {
        setUsageVariables(defineVariables("usage", true));
    }

    private void initVariablesWithRelaxation() {
        setUsageVariables(defineVariables("usage", true));
        setRelaxVariables(defineVariables("relax", integerRelax));
    }

    private List<List<MPVariable>> defineVariables(String varName, boolean integerVariables) {
        List<List<MPVariable>> variables = new ArrayList<>();
        for (int i = 0; i < getOrder().getInputs().size(); i++) {
            List<MPVariable> outputs = new ArrayList<>();
            for (int o = 0; o < getOrder().getOutputs().size(); o++) {
                if (integerVariables) outputs.add(getSolver().makeIntVar(0, Double.POSITIVE_INFINITY, varName + "_i_" + i + "_o_" + o));
                else outputs.add(getSolver().makeNumVar(0.0, Double.POSITIVE_INFINITY, varName + "_i_" + i + "_o_" + o));
            }
            variables.add(outputs);
        }
        return variables;
    }

    private void initConstraints() {
        for (int i = 0; i < getOrder().getInputs().size(); i++) {
            MPConstraint usageConstraint = getSolver().makeConstraint(
                -Double.POSITIVE_INFINITY, getOrder().getInputs().get(i).getLength(), "Length_i_" + i);
            for (int o = 0; o < getOrder().getOutputs().size(); o++) {
                usageConstraint.setCoefficient(usageVariables.get(i).get(o), getOrder().getOutputs().get(o).getLength());
            }
        }
    }

    private void initConstraintsWithRelaxation() {
        for (int i = 0; i < getOrder().getInputs().size(); i++) {
            MPConstraint usageConstraint = getSolver().makeConstraint(
                -Double.POSITIVE_INFINITY, getOrder().getInputs().get(i).getLength(), "Length_i_" + i);
            for (int o = 0; o < getOrder().getOutputs().size(); o++) {
                usageConstraint.setCoefficient(usageVariables.get(i).get(o), getOrder().getOutputs().get(o).getLength());
                usageConstraint.setCoefficient(relaxVariables.get(i).get(o), -1);

                MPConstraint relaxConstraint = getSolver().makeConstraint(0, Double.POSITIVE_INFINITY, "Relax_i_" + i + "_o_" + o);
                relaxConstraint.setCoefficient(usageVariables.get(i).get(o), getOrder().getOutputs().get(o).getMaxRelax());
                relaxConstraint.setCoefficient(relaxVariables.get(i).get(o), -1);
            }
        }
    }

    private void initObjective() {
        MPObjective objective = getSolver().objective();
        for (int i = 0; i < getOrder().getInputs().size(); i++) {
            for (int o = 0; o < getOrder().getOutputs().size(); o++) {
                objective.setCoefficient(usageVariables.get(i).get(o), cuttingPlanDualValues.get(o));
            }
        }
        initGeneralObjective(objective);
        setObjective(objective);
    }

    private void initObjectiveWithRelaxation() {
        MPObjective objective = getSolver().objective();
        for (int i = 0; i < getOrder().getInputs().size(); i++) {
            for (int o = 0; o < getOrder().getOutputs().size(); o++) {
                objective.setCoefficient(usageVariables.get(i).get(o), cuttingPlanDualValues.get(o));
                objective.setCoefficient(relaxVariables.get(i).get(o), -relaxCost);
            }
        }
        initGeneralObjective(objective);
        setObjective(objective);
    }

    private void initGeneralObjective(MPObjective objective) {
        objective.setOffset(-getOrder().getInputsSumLength());
        objective.setMaximization();
    }
}
