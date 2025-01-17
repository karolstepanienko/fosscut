package com.fosscut.alg.greedy;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.type.cutting.CHOutput;
import com.fosscut.type.cutting.CHPattern;
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

    private List<Integer> orderDemands;
    private OrderInput input;

    private List<MPVariable> usageVariables;

    public GreedyPatternGeneration(
        OrderInput input,
        List<OrderOutput> outputs,
        List<Integer> orderDemands
    ) {
        setOutputs(outputs);
        this.input = input;
        this.orderDemands = orderDemands;
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

    public CHPattern getPattern() {
        CHPattern pattern = new CHPattern();
        pattern.setInput(input);
        List<CHOutput> patternDefinition = new ArrayList<CHOutput>();
        for (int o = 0; o < getOutputs().size(); o++) {
            // Only add outputs with a count higher than 0 to pattern definition
            if (this.usageVariables.get(o).solutionValue() > 0) {
                patternDefinition.add(new CHOutput(
                    o,
                    getOutputs().get(o).getLength(),
                    Double.valueOf(this.usageVariables.get(o).solutionValue()).intValue(),
                    0
                ));
            }
        }
        pattern.setPatternDefinition(patternDefinition);
        return pattern;
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
                -Double.POSITIVE_INFINITY,
                orderDemands.get(o),
                "Count_output");
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
