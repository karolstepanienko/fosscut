package com.fosscut.alg.cg;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.type.Order;
import com.fosscut.utils.Defaults;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class PatternGeneration extends LPTask {
    private Double relaxCost;
    private List<Double> cuttingPlanDualValues;

    private List<List<MPVariable>> usageVariables;
    private List<List<MPVariable>> relaxVariables;

    public PatternGeneration(Order order, List<Double> cuttingPlanDualValues, Double relaxCost) {
        setOrder(order);
        this.cuttingPlanDualValues = cuttingPlanDualValues;
        this.relaxCost = relaxCost;
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
        System.out.println("");
        System.out.println("Starting pattern generation...");

        setSolver(MPSolver.createSolver(Defaults.INTEGER_SOLVER));
        if (relaxCost == null) initModel();
        else initModelWithRelaxation();

        printSolution();
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
        setUsageVariables(defineVariables("usage"));
    }

    private void initVariablesWithRelaxation() {
        setUsageVariables(defineVariables("usage"));
        setRelaxVariables(defineVariables("relax"));
    }

    private List<List<MPVariable>> defineVariables(String varName) {
        List<List<MPVariable>> variables = new ArrayList<>();
        for (int i = 0; i < getOrder().getInputs().size(); i++) {
            List<MPVariable> outputs = new ArrayList<>();
            for (int o = 0; o < getOrder().getOutputs().size(); o++) {
                outputs.add(getSolver().makeIntVar(0, Double.POSITIVE_INFINITY, varName + "_i_" + i + "_o_" + o));
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
