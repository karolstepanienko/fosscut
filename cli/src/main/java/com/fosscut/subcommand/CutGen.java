package com.fosscut.subcommand;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeoutException;

import com.fosscut.alg.gen.cut.CutGenAlg;
import com.fosscut.shared.exception.FosscutException;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.subcommand.abs.AbstractGen;
import com.fosscut.util.Messages;
import com.fosscut.util.PropertiesVersionProvider;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "cutgen", versionProvider = PropertiesVersionProvider.class)
public class CutGen extends AbstractGen {

    @Option(names = { "-d", "--average-output-demand" }, required = true)
    private int averageOutputDemand;

    @Option(names = { "-ao", "--allow-output-type-duplicates" },
        defaultValue = "false",
        description = "Some output types can be generated as duplicates."
            + " By default an error is thrown if it happens."
            + " This flag disables that error."
            + " Number of generated output types might be less than specified.")
    private boolean allowOutputTypeDuplicates;

    @Override
    protected void runWithExceptions()
    throws FosscutException, TimeoutException {
        handleOrderFuture(generateOrderFuture());
    }

    private CompletableFuture<Order> generateOrderFuture() throws FosscutException {
        CompletableFuture<Order> future = CompletableFuture.supplyAsync(() -> {
            try {
                return generateOrder();
            } catch (FosscutException e) {
                throw new CompletionException(e);
            }
        });

        return future.orTimeout(timeoutAmount, timeoutUnit);
    }

    private Order generateOrder() throws FosscutException {
        CutGenAlg cutGenAlg = new CutGenAlg(
            inputLength,
            inputTypeCount,
            minInputLength,
            maxInputLength,
            allowInputTypeDuplicates,
            averageOutputDemand,
            outputTypeCount,
            outputLengthLowerBound,
            outputLengthUpperBound,
            allowOutputTypeDuplicates,
            seed
        );
        return cutGenAlg.nextOrder();
    }

    private void handleOrderFuture(CompletableFuture<Order> future)
    throws FosscutException, TimeoutException {
        try {
            handleGeneratedOrder(future.join());
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
