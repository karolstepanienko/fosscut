package com.fosscut.subcommand;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import com.fosscut.alg.greedy.GreedyAlg;
import com.fosscut.shared.exception.FosscutException;
import com.fosscut.shared.type.IntegerSolver;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.type.cutting.plan.Plan;
import com.fosscut.subcommand.abs.AbstractAlg;
import com.fosscut.util.AlgTimer;
import com.fosscut.util.Defaults;
import com.fosscut.util.Messages;
import com.fosscut.util.PropertiesVersionProvider;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;

@Command(name = "greedy", versionProvider = PropertiesVersionProvider.class)
public class Greedy extends AbstractAlg {

    @Option(names = { "-c", "--relaxation-cost" },
    description = "Cost of relaxing the length of an output element by"
    + " a single unit. Relaxation is off if this parameter is not set."
    + " Allowed values: <0, infinity>.")
    public void setRelaxCost(Double relaxCost) {
        if (relaxCost != null && relaxCost < 0) {
            throw new ParameterException(spec.commandLine(),
                "Relaxation cost cannot be negative."
                + " Allowed values: <0, infinity>.");
        }
        this.relaxCost = relaxCost;
    }
    private Double relaxCost;

    @Option(names = { "--integer-solver" },
        defaultValue = Defaults.DEFAULT_PARAM_INTEGER_SOLVER,
        description = "One of: (${COMPLETION-CANDIDATES}).")
    private IntegerSolver integerSolver;

    @Option(names = { "-in", "--integer-num-threads" },
        defaultValue = Defaults.DEFAULT_NUM_THREADS,
        description = Messages.INTEGER_NUM_THREADS_DESCRIPTION)
    private int integerNumThreads;

    @Spec
    private CommandSpec spec;

    @Override
    protected void runWithExceptions()
    throws FosscutException, IOException, TimeoutException {
        Order order = prepareOrder();
        validateRelax(order, relaxCost);
        CompletableFuture<Plan> future = generatePlanFuture(order);
        handlePlanFuture(future);
    }

    @Override
    protected Plan generatePlan(Order order) throws FosscutException {
        AlgTimer timer = new AlgTimer();
        Long algElapsedTime = null;
        if (!disableTimeMeasurementMetadata) timer.start();

        GreedyAlg greedy = new GreedyAlg(order, relaxCost, relaxEnabled,
            relaxationSpreadStrategy, optimizationCriterion, integerSolver,
            integerNumThreads);
        greedy.run();

        if (!disableTimeMeasurementMetadata) {
            timer.stop();
            algElapsedTime = timer.getElapsedTimeMillis();
        }

        return greedy.getCuttingPlan(algElapsedTime);
    }

}
