package com.fosscut.subcommand;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeoutException;

import com.fosscut.alg.gen.optimal.OptimalGenAlg;
import com.fosscut.shared.exception.FosscutException;
import com.fosscut.shared.type.cutting.plan.Plan;
import com.fosscut.subcommand.abs.AbstractGen;
import com.fosscut.util.Messages;
import com.fosscut.util.PlanValidator;
import com.fosscut.util.PropertiesVersionProvider;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "optimalgen", versionProvider = PropertiesVersionProvider.class)
public class OptimalGen extends AbstractGen {

    @Option(names = { "-oc", "--output-count" },
        description = "Minimal number of outputs in the order."
            + " Actual number might be higher due to patterns"
            + " generating multiple outputs.",
        required = true)
    private int outputCount;

    @Override
    protected void runWithExceptions()
    throws FosscutException, TimeoutException {
        handleOrderFuture(generateOrderFuture());
    }

    private CompletableFuture<Plan> generateOrderFuture() throws FosscutException {
        CompletableFuture<Plan> future = CompletableFuture.supplyAsync(() -> {
            try {
                return generateOrderWithCuttingPlan();
            } catch (FosscutException e) {
                throw new RuntimeException(e);
            }
        });

        return future.orTimeout(timeoutAmount, timeoutUnit);
    }

    Plan generateOrderWithCuttingPlan() throws FosscutException {
        OptimalGenAlg optimalGenAlg = new OptimalGenAlg(
            inputLength,
            inputTypeCount,
            minInputLength,
            maxInputLength,
            allowInputTypeDuplicates,
            outputCount,
            outputTypeCount,
            outputLengthLowerBound,
            outputLengthUpperBound,
            seed);
        return optimalGenAlg.nextOrder();
    }

    private void handleOrderFuture(CompletableFuture<Plan> future)
    throws FosscutException, TimeoutException {
        try {
            Plan orderWithCuttingPlan = future.join();
            handleGeneratedOrder(orderWithCuttingPlan);
            PlanValidator planValidator = new PlanValidator();
            planValidator.validatePlan(orderWithCuttingPlan);
        } catch (CompletionException e) {
            if (e.getCause() instanceof TimeoutException) {
                StringWriter sw = new StringWriter();
                e.getCause().printStackTrace(new PrintWriter(sw));
                throw new TimeoutException(
                    sw.toString()
                    + Messages.ORDER_GENERATION_TIMEOUT
                    + timeoutAmount + " " + timeoutUnit.toString().toLowerCase()
                    + "."
                );
            } else {
                throw e; // rethrow FosscutExceptions and others
            }
        }
    }

}
