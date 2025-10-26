package com.fosscut.subcommand;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import com.fosscut.alg.cg.ColumnGeneration;
import com.fosscut.shared.exception.FosscutException;
import com.fosscut.shared.type.IntegerSolver;
import com.fosscut.shared.type.LinearSolver;
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

@Command(name = "cg", versionProvider = PropertiesVersionProvider.class)
public class CG extends AbstractAlg {

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

    @Option(names = { "--linear-solver" },
        defaultValue = Defaults.DEFAULT_PARAM_LINEAR_SOLVER,
        description = "One of: (${COMPLETION-CANDIDATES}).")
    private LinearSolver linearSolver;

    @Option(names = { "--integer-solver" },
        defaultValue = Defaults.DEFAULT_PARAM_INTEGER_SOLVER,
        description = "One of: (${COMPLETION-CANDIDATES}).")
    private IntegerSolver integerSolver;

    @Option(names = { "-ln", "--linear-num-threads" },
        defaultValue = Defaults.DEFAULT_NUM_THREADS,
        description = Messages.LINEAR_NUM_THREADS_DESCRIPTION)
    private int linearNumThreads;

    @Option(names = { "-in", "--integer-num-threads" },
        defaultValue = Defaults.DEFAULT_NUM_THREADS,
        description = Messages.INTEGER_NUM_THREADS_DESCRIPTION)
    private int integerNumThreads;

    @Option(names = { "--force-linear-improvement", "-fli" },
        defaultValue = "false",
        description = "Force only adding patterns that improve the solution"
        + " in linear programming tasks for cutting plan generation that have"
        + " integer constraints disabled."
        + " Not recommended since those patterns might be useful during final"
        + " solving of a linear programming task with integer constraints"
        + " enabled."
        + " Enable only for large orders where cutting plan generation takes"
        + " too long.")
    private boolean forceLinearImprovement;

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

        ColumnGeneration columnGeneration = new ColumnGeneration(
            order, relaxCost, relaxEnabled, optimizationCriterion,
            relaxationSpreadStrategy, forceLinearImprovement,
            linearSolver, integerSolver, linearNumThreads, integerNumThreads);
        columnGeneration.run();

        if (!disableTimeMeasurementMetadata) {
            timer.stop();
            algElapsedTime = timer.getElapsedTimeMillis();
        }

        return columnGeneration.getCuttingPlan(algElapsedTime);
    }

}
