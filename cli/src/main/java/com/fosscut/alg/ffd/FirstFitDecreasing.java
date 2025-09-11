package com.fosscut.alg.ffd;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.alg.ch.ConstructiveHeuristic;
import com.fosscut.exception.GeneratedPatternsCannotBeEmptyException;
import com.fosscut.exception.LPUnfeasibleException;
import com.fosscut.shared.type.OptimizationCriterion;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.type.cutting.order.OrderInput;
import com.fosscut.type.RelaxationSpreadStrategy;
import com.fosscut.type.cutting.CHOutput;
import com.fosscut.type.cutting.CHPattern;
import com.fosscut.type.cutting.plan.CuttingPlan;

public class FirstFitDecreasing extends ConstructiveHeuristic {

    private static final Logger logger = LoggerFactory.getLogger(FirstFitDecreasing.class);

    private Order orderSortedOutputs;
    private boolean relaxEnabled;
    private RelaxationSpreadStrategy relaxationSpreadStrategy;

    public FirstFitDecreasing(Order order, boolean relaxEnabled,
        OptimizationCriterion optimizationCriterion,
        RelaxationSpreadStrategy relaxationSpreadStrategy
    ) {
        super(optimizationCriterion);
        this.orderSortedOutputs = order;
        this.relaxEnabled = relaxEnabled;
        this.relaxationSpreadStrategy = relaxationSpreadStrategy;
    }

    public CuttingPlan getCuttingPlan() {
        return getCuttingPlan(orderSortedOutputs);
    }

    public void run() throws GeneratedPatternsCannotBeEmptyException, LPUnfeasibleException {
        logger.info("");
        logger.info("Running cutting plan generation using a first-fit-decreasing algorithm...");

        initInputCounts(orderSortedOutputs);
        orderSortedOutputs.reverseSortOutputs();
        initOrderDemands(orderSortedOutputs);

        setCuttingPlanPatterns(demandLoop());
    }

    @Override
    protected List<CHPattern> generatePatternForEachInput() {
        List<CHPattern> chPatterns = new ArrayList<CHPattern>();
        for (Integer inputId = 0; inputId < orderSortedOutputs.getInputs().size(); ++inputId) {
            Integer inputCount = getInputCounts().get(inputId);
            if (inputCount == null || inputCount > 0) {
                chPatterns.add(getPattern(inputId, orderSortedOutputs.getInputs().get(inputId)));
            }
        }
        return chPatterns;
    }

    private CHPattern getPattern(Integer inputId, OrderInput input) {
        CHPattern chPattern = new CHPattern();
        chPattern.setInputId(inputId);
        chPattern.setInput(new OrderInput(input));
        chPattern.setPatternDefinition(getPatternDefinition(input));
        return chPattern;
    }

    private List<CHOutput> getPatternDefinition(OrderInput input) {
        if (relaxEnabled) {
            FFDPatternGenRelax ffdPatternGenRelax = new FFDPatternGenRelax(
                orderSortedOutputs, getOrderDemands(), relaxationSpreadStrategy);
            return ffdPatternGenRelax.getPatternDefinition(input);
        } else {
            FFDPatternGen ffdPatternGen = new FFDPatternGen(orderSortedOutputs, getOrderDemands());
            return ffdPatternGen.getPatternDefinition(input);
        }
    }

}
