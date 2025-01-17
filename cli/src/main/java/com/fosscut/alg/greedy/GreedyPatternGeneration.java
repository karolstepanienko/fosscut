package com.fosscut.alg.greedy;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.type.cutting.order.OrderInput;
import com.fosscut.type.cutting.order.OrderOutput;
import com.fosscut.util.Defaults;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPSolver.ResultStatus;
import com.google.ortools.linearsolver.MPVariable;

/*
 * Generates a single cutting pattern for a given input element type.
 */
public class GreedyPatternGeneration extends GreedyLPTask {

    private OrderInput input;

    private List<MPVariable> usageVariables;

    public GreedyPatternGeneration(List<OrderOutput> outputs, OrderInput input) {
        setOutputs(outputs);
        this.input = input;
    }

    public List<MPVariable> getUsageVariables() {
        return usageVariables;
    }

    public void setUsageVariables(List<MPVariable> usageVariables) {
        this.usageVariables = usageVariables;
    }

    public void solve() {
        setSolver(MPSolver.createSolver(Defaults.INTEGER_SOLVER));
        initModel();
        final ResultStatus resultStatus = getSolver().solve();

        printSolution(resultStatus);
    }

    private void initModel() {
        setUsageVariables(defineVariables("usage", true));
        initInputLengthConstraint();
        initOutputCountConstraints();
        initObjective();
    }

    private List<MPVariable> defineVariables(String varName, boolean integerVariables) {
        List<MPVariable> variables = new ArrayList<>();
            for (int o = 0; o < getOutputs().size(); o++) {
                if (integerVariables) variables.add(getSolver().makeIntVar(0, Double.POSITIVE_INFINITY, varName + "_o_" + o));
                else variables.add(getSolver().makeNumVar(0.0, Double.POSITIVE_INFINITY, varName + "_o_" + o));
            }
        return variables;
    }

    private void initInputLengthConstraint() {
        MPConstraint inputLengthConstraint = getSolver().makeConstraint(
            -Double.POSITIVE_INFINITY, input.getLength(), "Length_input");
        for (int o = 0; o < getOutputs().size(); o++) {
            inputLengthConstraint.setCoefficient(
                usageVariables.get(o),
                getOutputs().get(o).getLength()
            );
        }
    }

    private void initOutputCountConstraints() {
        for (int o = 0; o < getOutputs().size(); o++) {
            MPConstraint outputCountConstraint = getSolver().makeConstraint(
                -Double.POSITIVE_INFINITY, getOutputs().get(o).getCount(), "Count_output");
            outputCountConstraint.setCoefficient(usageVariables.get(o), 1);
        }
    }

    private void initObjective() {
        MPObjective objective = getSolver().objective();
        for (int o = 0; o < getOutputs().size(); o++) {
            objective.setCoefficient(
                usageVariables.get(o),
                getOutputs().get(o).getLength()
            );
        }
        objective.setMaximization();
        setObjective(objective);
    }

}
