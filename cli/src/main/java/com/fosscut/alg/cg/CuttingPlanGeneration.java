package com.fosscut.alg.cg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Double;

import com.fosscut.exception.LPUnfeasibleException;
import com.fosscut.exception.NotIntegerLPTaskException;
import com.fosscut.shared.type.IntegerSolver;
import com.fosscut.shared.type.LinearSolver;
import com.fosscut.shared.type.OptimizationCriterion;
import com.fosscut.shared.type.cutting.order.Order;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver.ResultStatus;
import com.google.ortools.linearsolver.MPVariable;

class CuttingPlanGeneration extends ColumnGenerationLPTask {

    private static final Logger logger = LoggerFactory.getLogger(CuttingPlanGeneration.class);

    private Parameters params;
    private boolean integer;
    private OptimizationCriterion optimizationCriterion;
    private LinearSolver linearSolver;
    private IntegerSolver integerSolver;
    private int linearNumThreads;
    private int integerNumThreads;
    private boolean relaxEnabled;

    private List<List<MPVariable>> patternsPerInputVariables;
    private List<MPConstraint> demandConstraints;
    private Map<Integer, MPConstraint> supplyConstraints;

    public CuttingPlanGeneration(Order order, Parameters params,
        boolean integer,
        OptimizationCriterion optimizationCriterion,
        LinearSolver linearSolver,
        IntegerSolver integerSolver,
        int linearNumThreads,
        int integerNumThreads,
        boolean relaxEnabled
    ) {
        setOrder(order);
        this.params = params;
        this.integer = integer;
        this.optimizationCriterion = optimizationCriterion;
        this.linearSolver = linearSolver;
        this.integerSolver = integerSolver;
        this.linearNumThreads = linearNumThreads;
        this.integerNumThreads = integerNumThreads;
        this.relaxEnabled = relaxEnabled;
    }

    public void solve() throws LPUnfeasibleException {
        defineSolver();
        this.patternsPerInputVariables = defineVariables();
        this.demandConstraints = defineDemandConstraints();
        this.supplyConstraints = defineSupplyConstraints();
        setObjective(defineObjective(this.optimizationCriterion));
        final ResultStatus resultStatus = getSolver().solve();

        printSolution("Plan:    ", resultStatus);
    }

    public List<List<Integer>> getInputPatternUsage() throws NotIntegerLPTaskException {
        if (!integer) {
            throw new NotIntegerLPTaskException("getInputPatternUsage()");
        }

        List<List<Integer>> inputPatternUsage = new ArrayList<>();
        for (int i = 0; i < getOrder().getInputs().size(); i++) {
            List<Integer> patternUsage = new ArrayList<>();
            for (int p = 0; p < params.getNumberOfPatternsPerInput(i); p++) {
                patternUsage.add(Double.valueOf(this.patternsPerInputVariables.get(i).get(p).solutionValue()).intValue());
            }
            inputPatternUsage.add(patternUsage);
        }
        return inputPatternUsage;
    }

    public List<Double> getDemandDualValues() {
        List<Double> demandDualValues = new ArrayList<>();
        for (int o = 0; o < getOrder().getOutputs().size(); o++) {
            demandDualValues.add(Double.valueOf(demandConstraints.get(o).dualValue()));
        }
        return demandDualValues;
    }

    public Map<Integer, Double> getSupplyDualValues() {
        Map<Integer, Double> supplyDualValues = new HashMap<>();
        for (Map.Entry<Integer, MPConstraint> entry : supplyConstraints.entrySet()) {
            Integer inputId = entry.getKey();
            MPConstraint constraint = entry.getValue();
            supplyDualValues.put(inputId, Double.valueOf(constraint.dualValue()));
        }
        return supplyDualValues;
    }

    private void defineSolver() {
        if (integer) {
            createSolver(integerSolver.toString(), integerNumThreads);
        } else {
            createSolver(linearSolver.toString(), linearNumThreads);
        }
    }

    private List<List<MPVariable>> defineVariables() {
        List<List<MPVariable>> patternsPerInputVariables = new ArrayList<>();
        for (int i = 0; i < getOrder().getInputs().size(); i++) {
            List<MPVariable> patterns = new ArrayList<>();
            for (int p = 0; p < params.getNumberOfPatternsPerInput(i); p++) {
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
                for (int p = 0; p < params.getNumberOfPatternsPerInput(i); p++) {
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

    private Map<Integer, MPConstraint> defineSupplyConstraints() {
        Map<Integer, MPConstraint> supplyConstraints = new HashMap<>();
        for (int i = 0; i < getOrder().getInputs().size(); i++) {
            Integer inputCount = getOrder().getInputs().get(i).getCount();
            if (inputCount != null && inputCount >= 0) {
                MPConstraint constraint = getSolver().makeConstraint(
                    0, inputCount, "InputCount_i_" + i);
                for (int p = 0; p < params.getNumberOfPatternsPerInput(i); p++) {
                    constraint.setCoefficient(
                        patternsPerInputVariables.get(i).get(p), 1);
                }
                supplyConstraints.put(i, constraint);
            }
        }
        return supplyConstraints;
    }

    private MPObjective defineObjective(OptimizationCriterion criterion) {
        switch (criterion) {
            case MIN_WASTE:
                return defineMinWasteObjective();
            case MIN_COST:
                return defineMinCostObjective();
            case MIN_WASTE_EXPERIMENTAL:
                return defineMinWasteExperimentalObjective();
            default:
                throw new IllegalArgumentException("Unknown optimization criterion: " + criterion);
        }
    }

    private MPObjective defineMinWasteObjective() {
        MPObjective objective = getSolver().objective();
        for (int i = 0; i < getOrder().getInputs().size(); i++) {
            for (int p = 0; p < params.getNumberOfPatternsPerInput(i); p++) {
                objective.setCoefficient(
                    patternsPerInputVariables.get(i).get(p),
                    getOrder().getInputs().get(i).getLength()
                );
            }
        }
        objective.setMinimization();
        return objective;
    }

    private MPObjective defineMinCostObjective() {
        MPObjective objective = getSolver().objective();
        for (int i = 0; i < getOrder().getInputs().size(); i++) {
            for (int p = 0; p < params.getNumberOfPatternsPerInput(i); p++) {
                objective.setCoefficient(
                    patternsPerInputVariables.get(i).get(p),
                    getOrder().getInputs().get(i).getCost()
                );
            }
        }
        objective.setMinimization();
        return objective;
    }

    /*
     * Experimental objective function to minimize waste directly.
     * It does not work well in practice, because it results in a lot worse
     * sub-problem prices, which leads to worse overall results.
     */
    @Deprecated
    private MPObjective defineMinWasteExperimentalObjective() {
        MPObjective objective = getSolver().objective();
        for (int i = 0; i < getOrder().getInputs().size(); i++) {
            for (int p = 0; p < params.getNumberOfPatternsPerInput(i); p++) {
                Integer sum = 0;
                for (int o = 0; o < getOrder().getOutputs().size(); o++) {
                    sum += params.getNipo().get(i).get(p).get(o)
                        * getOrder().getOutputs().get(o).getLength();
                    if (relaxEnabled) sum -= params.getRipo().get(i).get(p).get(o);
                }

                Integer waste = getOrder().getInputs().get(i).getLength() - sum;
                objective.setCoefficient(
                    patternsPerInputVariables.get(i).get(p),
                    waste
                );
            }
        }
        objective.setMinimization();
        return objective;
    }

}
