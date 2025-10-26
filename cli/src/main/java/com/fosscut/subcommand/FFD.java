package com.fosscut.subcommand;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import com.fosscut.alg.ffd.FirstFitDecreasing;
import com.fosscut.shared.exception.FosscutException;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.type.cutting.plan.Plan;
import com.fosscut.subcommand.abs.AbstractAlg;
import com.fosscut.util.AlgTimer;
import com.fosscut.util.PropertiesVersionProvider;

import picocli.CommandLine.Command;

@Command(name = "ffd", versionProvider = PropertiesVersionProvider.class)
public class FFD extends AbstractAlg {

    @Override
    protected void runWithExceptions()
    throws FosscutException, IOException, TimeoutException {
        Order order = prepareOrder();
        CompletableFuture<Plan> future = generatePlanFuture(order);
        handlePlanFuture(future);
    }

    @Override
    protected Plan generatePlan(Order order) throws FosscutException {
        AlgTimer timer = new AlgTimer();
        Long algElapsedTime = null;
        if (!disableTimeMeasurementMetadata) timer.start();

        FirstFitDecreasing firstFitDecreasing = new FirstFitDecreasing(
            order,
            relaxEnabled,
            optimizationCriterion,
            relaxationSpreadStrategy
        );
        firstFitDecreasing.run();

        if (!disableTimeMeasurementMetadata) {
            timer.stop();
            algElapsedTime = timer.getElapsedTimeMillis();
        }

        return firstFitDecreasing.getCuttingPlan(algElapsedTime);
    }

}
