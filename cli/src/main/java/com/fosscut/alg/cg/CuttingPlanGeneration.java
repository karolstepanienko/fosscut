package com.fosscut.alg.cg;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Double;

import com.fosscut.exception.LPUnfeasibleException;
import com.fosscut.exception.NotIntegerLPTaskException;
import com.fosscut.exception.NullCostException;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.type.IntegerSolvers;
import com.fosscut.type.LinearSolvers;
import com.fosscut.type.OptimizationCriterion;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPSolver.ResultStatus;
import com.google.ortools.linearsolver.MPVariable;

class CuttingPlanGeneration extends ColumnGenerationLPTask {

    private static final Logger logger = LoggerFactory.getLogger(CuttingPlanGeneration.class);

    private Parameters params;
    private boolean integer;
    private OptimizationCriterion optimizationCriterion;
    private LinearSolvers linearSolver;
    private IntegerSolvers integerSolver;

    private List<List<MPVariable>> patternsPerInputVariables;
    private List<MPConstraint> demandConstraints;

    public CuttingPlanGeneration(Order order, Parameters params,
        boolean integer,
        OptimizationCriterion optimizationCriterion,
        LinearSolvers linearSolver,
        IntegerSolvers integerSolver
    ) {
        setOrder(order);
        this.params = params;
        this.integer = integer;
        this.optimizationCriterion = optimizationCriterion;
        this.linearSolver = linearSolver;
        this.integerSolver = integerSolver;
    }

    public void solve() throws LPUnfeasibleException, NullCostException {
        logger.info("");
        logger.info("Starting cutting plan generation...");

        setSolver(defineSolver());
        this.patternsPerInputVariables = defineVariables();
        this.demandConstraints = defineDemandConstraints();
        defineInputCountConstraints();
        setObjective(defineObjective(this.optimizationCriterion));
        final ResultStatus resultStatus = getSolver().solve();

        printSolution(resultStatus);
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
            dualValues.add(Double.valueOf(demandConstraints.get(o).dualValue()));
        }
        return dualValues;
    }

    private MPSolver defineSolver() {
        MPSolver solver;
        if (integer) solver = MPSolver.createSolver(integerSolver.toString());
        else solver = MPSolver.createSolver(linearSolver.toString());
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

    private List<MPConstraint> defineDemandConstraints() {
        List<MPConstraint> demandConstraints = new ArrayList<>();
        for (int o = 0; o < getOrder().getOutputs().size(); o++) {
            MPConstraint constraint = getSolver().makeConstraint(
                getOrder().getOutputs().get(o).getCount(), Double.POSITIVE_INFINITY, "Demand_o_" + o);
            for (int i = 0; i < getOrder().getInputs().size(); i++) {
                for (int p = 0; p < params.getNPatternMax(); p++) {
                    constraint.setCoefficient(
                        patternsPerInputVariables.get(i).get(p),
                        params.getNipo().get(i).get(p).get(o)
                    );
                }
            }
            demandConstraints.add(constraint);
        }
        return demandConstraints;
    }

    private void defineInputCountConstraints() {
        for (int i = 0; i < getOrder().getInputs().size(); i++) {
            Integer inputCount = getOrder().getInputs().get(i).getCount();
            if (inputCount != null && inputCount >= 0) {
                MPConstraint constraint = getSolver().makeConstraint(
                    0, inputCount, "InputCount_i_" + i);
                for (int p = 0; p < params.getNPatternMax(); p++) {
                    constraint.setCoefficient(
                        patternsPerInputVariables.get(i).get(p), 1);
                }
            }
        }
    }

    private MPObjective defineObjective(OptimizationCriterion criterion) throws NullCostException {
        MPObjective objective = getSolver().objective();
        for (int i = 0; i < getOrder().getInputs().size(); i++) {
            for (int p = 0; p < params.getNPatternMax(); p++) {
                if (criterion == OptimizationCriterion.MIN_WASTE)
                    objective.setCoefficient(
                        patternsPerInputVariables.get(i).get(p),
                        getOrder().getInputs().get(i).getLength()
                    );
                else if (getOrder().getInputs().get(i).getCost() != null)
                    objective.setCoefficient(
                        patternsPerInputVariables.get(i).get(p),
                        getOrder().getInputs().get(i).getCost()
                    );
                else throw new NullCostException();
            }
        }
        objective.setMinimization();
        return objective;
    }

}
