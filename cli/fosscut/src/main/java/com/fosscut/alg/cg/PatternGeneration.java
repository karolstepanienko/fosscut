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
    private boolean enableRelaxation;
    List<Double> cuttingPlanDualValues;

    private List<List<MPVariable>> usageVariables;
    private List<List<MPVariable>> relaxVariables;
    private double relaxCost;

    public PatternGeneration(Order order, List<Double> cuttingPlanDualValues, boolean enableRelaxation) {
        setOrder(order);
        this.cuttingPlanDualValues = cuttingPlanDualValues;
        this.enableRelaxation = enableRelaxation;
    }

    public List<List<MPVariable>> getUsageVariables() {
        return usageVariables;
    }

    public List<List<MPVariable>> getRelaxVariables() {
        return relaxVariables;
    }

    public void solve() {
        System.out.println("");
        System.out.println("Starting pattern generation...");

        setSolver(MPSolver.createSolver(Defaults.INTEGER_SOLVER));
        this.usageVariables = defineVariables("usage");
        this.relaxVariables = defineVariables("relax");
        initConstraints();
        relaxCost = defineRelaxCost();
        setObjective(defineObjective());

        System.out.println("Solving with " + getSolver().solverVersion());
        final MPSolver.ResultStatus resultStatus = getSolver().solve();
        printSolution(resultStatus);
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
            MPConstraint inputLengthConstraint = getSolver().makeConstraint(
                -Double.POSITIVE_INFINITY, getOrder().getInputs().get(i).getLength(), "Length_i_" + i);
            for (int o = 0; o < getOrder().getOutputs().size(); o++) {
                inputLengthConstraint.setCoefficient(usageVariables.get(i).get(o), getOrder().getOutputs().get(o).getLength());
                inputLengthConstraint.setCoefficient(relaxVariables.get(i).get(o), -1);

                MPConstraint relaxConstraint = getSolver().makeConstraint(0, Double.POSITIVE_INFINITY, "Relax_i_" + i + "_o_" + o);
                relaxConstraint.setCoefficient(usageVariables.get(i).get(o), getOrder().getOutputs().get(o).getMaxRelax());
                relaxConstraint.setCoefficient(relaxVariables.get(i).get(o), -1);
            }
        }
    }

    private double defineRelaxCost() {
        double relaxCost;
        if(enableRelaxation) relaxCost = Defaults.ENABLED_RELAXATION_COST;
        else relaxCost = Defaults.DISABLED_RELAXATION_COST;
        return relaxCost;
    }

    private MPObjective defineObjective() {
        MPObjective objective = getSolver().objective();
        for (int i = 0; i < getOrder().getInputs().size(); i++) {
            for (int o = 0; o < getOrder().getOutputs().size(); o++) {
                objective.setCoefficient(usageVariables.get(i).get(o), cuttingPlanDualValues.get(o));
                objective.setCoefficient(relaxVariables.get(i).get(o), -relaxCost);
            }
        }
        objective.setOffset(-getOrder().getInputsSumLength());
        objective.setMaximization();
        return objective;
    }
}
