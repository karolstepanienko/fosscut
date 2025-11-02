package com.fosscut.alg.greedy;

import java.util.ArrayList;
import java.util.List;

import com.fosscut.alg.RelaxationSpread;
import com.fosscut.alg.SingleOutput;
import com.fosscut.exception.LPUnfeasibleException;
import com.fosscut.shared.type.IntegerSolver;
import com.fosscut.shared.type.cutting.order.OrderInput;
import com.fosscut.shared.type.cutting.order.OrderOutput;
import com.fosscut.type.RelaxationSpreadStrategy;
import com.fosscut.type.cutting.CHOutput;
import com.fosscut.type.cutting.CHPattern;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver.ResultStatus;
import com.google.ortools.linearsolver.MPVariable;

/*
 * Generates a single cutting pattern for a given input element type.
 */
public class GreedyPatternGeneration extends GreedyLPTask {

    private Integer orderInputId;
    private OrderInput input;
    private List<Integer> orderDemands;
    private Double relaxCost;
    private boolean relaxEnabled;
    private IntegerSolver integerSolver;
    private int integerNumThreads;

    private RelaxationSpread relaxationSpread;
    private List<MPVariable> usageVariables;
    private List<MPVariable> relaxVariables;

    public GreedyPatternGeneration(
        Integer orderInputId,
        OrderInput input,
        List<OrderOutput> outputs,
        List<Integer> orderDemands,
        Double relaxCost,
        boolean relaxEnabled,
        RelaxationSpreadStrategy relaxationSpreadStrategy,
        IntegerSolver integerSolver,
        int integerNumThreads
    ) {
        setOutputs(outputs);
        this.orderInputId = orderInputId;
        this.input = input;
        this.orderDemands = orderDemands;
        this.relaxCost = relaxCost;
        this.relaxEnabled = relaxEnabled;
        this.integerSolver = integerSolver;
        this.relaxationSpread = new RelaxationSpread(relaxationSpreadStrategy);
        this.integerNumThreads = integerNumThreads;
    }

    public void setUsageVariables(List<MPVariable> usageVariables) {
        this.usageVariables = usageVariables;
    }

    public void setRelaxVariables(List<MPVariable> relaxVariables) {
        this.relaxVariables = relaxVariables;
    }

    public void solve() throws LPUnfeasibleException {
        createSolver(integerSolver.toString(), integerNumThreads);

        if (relaxEnabled) initModelWithRelaxation();
        else initModel();

        final ResultStatus resultStatus = getSolver().solve();
        printSolution("Pattern: ", resultStatus);
    }

    public CHPattern getPattern() {
        CHPattern pattern = new CHPattern();
        pattern.setInputId(orderInputId);
        pattern.setInput(input);
        if (relaxEnabled) {
            pattern.setPatternDefinition(getPatternDefinitionWithRelaxation());
        } else {
            pattern.setPatternDefinition(getPatternDefinition());
        }
        return pattern;
    }

    private List<CHOutput> getPatternDefinition() {
        List<CHOutput> patternDefinition = new ArrayList<CHOutput>();
        for (int o = 0; o < getOutputs().size(); o++) {
            // Only add outputs with a count higher than 0 to pattern definition
            if (usageVariables.get(o).solutionValue() > 0) {
                patternDefinition.add(new CHOutput(
                    o,
                    getOutputs().get(o).getLength(),
                    Double.valueOf(usageVariables.get(o).solutionValue()).intValue(),
                    null
                ));
            }
        }
        return patternDefinition;
    }

    private List<CHOutput> getPatternDefinitionWithRelaxation() {
        List<Integer> outputCounts = new ArrayList<Integer>();
        List<Integer> relaxValues = new ArrayList<Integer>();

        for (int o = 0; o < getOutputs().size(); o++) {
            outputCounts.add(Double.valueOf(usageVariables.get(o).solutionValue()).intValue());
            relaxValues.add(Double.valueOf(relaxVariables.get(o).solutionValue()).intValue());
        }

        List<SingleOutput> singlePatternDefinition =
            relaxationSpread.getSinglePatternDefinition(
                getOutputs(),
                outputCounts,
                relaxValues
            );

        return relaxationSpread.convertSingleToChPatternDefinition(
            singlePatternDefinition
        );
    }

    private void initModel() {
        initVariables();
        initInputLengthConstraint();
        initOutputCountConstraints();
        initObjective();
    }

    private void initModelWithRelaxation() {
        initVariablesWithRelaxation();
        initInputLengthConstraintWithRelaxation();
        initOutputCountConstraints();
        initRelaxConstraints();
        initObjectiveWithRelaxation();
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
            for (int o = 0; o < getOutputs().size(); o++) {
                variables.add(getSolver().makeIntVar(0, Double.POSITIVE_INFINITY, varName + "_o_" + o));
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

    private void initInputLengthConstraintWithRelaxation() {
        MPConstraint inputLengthConstraintWithRelaxation = getSolver().makeConstraint(
            -Double.POSITIVE_INFINITY, input.getLength(), "Length_input");
        for (int o = 0; o < getOutputs().size(); o++) {
            inputLengthConstraintWithRelaxation.setCoefficient(
                usageVariables.get(o),
                getOutputs().get(o).getLength()
            );
            inputLengthConstraintWithRelaxation.setCoefficient(
                relaxVariables.get(o),
                -1
            );
        }
    }

    private void initOutputCountConstraints() {
        for (int o = 0; o < getOutputs().size(); o++) {
            MPConstraint outputCountConstraint = getSolver().makeConstraint(
                -Double.POSITIVE_INFINITY,
                orderDemands.get(o),
                "Count_output_" + o
            );
            outputCountConstraint.setCoefficient(usageVariables.get(o), 1);
        }
    }

    private void initRelaxConstraints() {
        for (int o = 0; o < getOutputs().size(); o++) {
            MPConstraint relaxConstraint = getSolver().makeConstraint(
                0,
                Double.POSITIVE_INFINITY,
                "Relax_output"
            );
            relaxConstraint.setCoefficient(usageVariables.get(o), getOutputs().get(o).getMaxRelax());
            relaxConstraint.setCoefficient(relaxVariables.get(o), -1);
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

    private void initObjectiveWithRelaxation() {
        MPObjective objective = getSolver().objective();
        for (int o = 0; o < getOutputs().size(); o++) {
            objective.setCoefficient(
                usageVariables.get(o),
                getOutputs().get(o).getLength()
            );
            OrderOutput output = getOutputs().get(o);
            Double localOutputRelaxCost = output.getRelaxCost() != null ? output.getRelaxCost() : relaxCost;
            objective.setCoefficient(
                relaxVariables.get(o),
                -localOutputRelaxCost
            );
        }
        objective.setMaximization();
        setObjective(objective);
    }

}
