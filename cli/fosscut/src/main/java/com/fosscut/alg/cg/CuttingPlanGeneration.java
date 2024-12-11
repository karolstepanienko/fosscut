package com.fosscut.alg.cg;

import java.util.ArrayList;
import java.util.List;
import java.lang.Double;

import com.fosscut.exception.NotIntegerLPTaskException;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.util.Defaults;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPSolver.ResultStatus;
import com.google.ortools.linearsolver.MPVariable;

class CuttingPlanGeneration extends LPTask {
    private Parameters params;
    private boolean integer;
    private boolean quietModeRequested;

    private List<List<MPVariable>> patternsPerInputVariables;
    private List<MPConstraint> fillConstraints;

    public CuttingPlanGeneration(Order order, Parameters params, boolean integer, boolean quietModeRequested) {
        setOrder(order);
        this.params = params;
        this.integer = integer;
        this.quietModeRequested = quietModeRequested;
    }

    public void solve() {
        if (!quietModeRequested) printIntro();

        setSolver(defineSolver());
        this.patternsPerInputVariables = defineVariables();
        this.fillConstraints = defineConstraints();
        setObjective(defineObjective());
        final ResultStatus resultStatus = getSolver().solve();

        if (!quietModeRequested) printSolution(resultStatus);
    }

    public List<List<Integer>> getInputPatternUsage() throws NotIntegerLPTaskException {
        if (!integer) {
            throw new NotIntegerLPTaskException("getInputPatternUsage()");
        }

        List<List<Integer>> inputPatternUsage = new ArrayList<>();
        for (int i = 0; i < getOrder().getInputs().size(); i++) {
            List<Integer> patternUsage = new ArrayList<>();
            for (int p = 0; p < params.getNPatternMax(); p++) {
                patternUsage.add(Double.valueOf(this.patternsPerInputVariables.get(i).get(p).solutionValue()).intValue());
            }
            inputPatternUsage.add(patternUsage);
        }
        return inputPatternUsage;
    }

    public List<Double> getDualValues() {
        List<Double> dualValues = new ArrayList<>();
        for (int o = 0; o < getOrder().getOutputs().size(); o++) {
            dualValues.add(Double.valueOf(fillConstraints.get(o).dualValue()));
        }
        return dualValues;
    }

    private void printIntro() {
        System.out.println("");
        System.out.println("Starting cutting plan generation...");
    }

    private MPSolver defineSolver() {
        MPSolver solver;
        if (integer) solver = MPSolver.createSolver(Defaults.INTEGER_SOLVER);
        else solver = MPSolver.createSolver(Defaults.LINEAR_SOLVER);
        return solver;
    }

    private List<List<MPVariable>> defineVariables() {
        List<List<MPVariable>> patternsPerInputVariables = new ArrayList<>();
        for (int i = 0; i < getOrder().getInputs().size(); i++) {
            List<MPVariable> patterns = new ArrayList<>();
            for (int p = 0; p < params.getNPatternMax(); p++) {
                String varId = "i_" + i + "_p_" + p;
                if (integer) patterns.add(getSolver().makeIntVar(0.0, Double.POSITIVE_INFINITY, varId));
                else patterns.add(getSolver().makeNumVar(0.0, Double.POSITIVE_INFINITY, varId));
            }
            patternsPerInputVariables.add(patterns);
        }
        return patternsPerInputVariables;
    }

    private List<MPConstraint> defineConstraints() {
        List<MPConstraint> fillConstraints = new ArrayList<>();
        for (int o = 0; o < getOrder().getOutputs().size(); o++) {
            MPConstraint constraint = getSolver().makeConstraint(
                getOrder().getOutputs().get(o).getNumber(), Double.POSITIVE_INFINITY, "Fill_o_" + o);
            for (int i = 0; i < getOrder().getInputs().size(); i++) {
                for (int p = 0; p < params.getNPatternMax(); p++) {
                    constraint.setCoefficient(
                        patternsPerInputVariables.get(i).get(p),
                        params.getNipo().get(i).get(p).get(o)
                    );
                }
            }
            fillConstraints.add(constraint);
        }
        return fillConstraints;
    }

    private MPObjective defineObjective() {
        MPObjective objective = getSolver().objective();
        for (int i = 0; i < getOrder().getInputs().size(); i++) {
            for (int p = 0; p < params.getNPatternMax(); p++) {
                objective.setCoefficient(patternsPerInputVariables.get(i).get(p), getOrder().getInputs().get(i).getLength());
            }
        }
        objective.setMinimization();
        return objective;
    }
}
