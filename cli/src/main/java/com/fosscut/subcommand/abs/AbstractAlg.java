package com.fosscut.subcommand.abs;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeoutException;

import com.fosscut.exception.PlanValidationException;
import com.fosscut.shared.exception.FosscutException;
import com.fosscut.shared.type.OptimizationCriterion;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.type.cutting.plan.Plan;
import com.fosscut.shared.util.OrderValidator;
import com.fosscut.shared.util.RelaxValidator;
import com.fosscut.shared.util.load.YamlLoader;
import com.fosscut.shared.util.save.YamlDumper;
import com.fosscut.type.OutputFormat;
import com.fosscut.type.RelaxationSpreadStrategy;
import com.fosscut.util.Cleaner;
import com.fosscut.util.Defaults;
import com.fosscut.util.LogFormatter;
import com.fosscut.util.Messages;
import com.fosscut.util.PlanValidator;
import com.fosscut.util.PrintResult;
import com.fosscut.util.RedisUriParser;
import com.fosscut.util.load.OrderLoader;
import com.fosscut.util.save.Save;
import com.fosscut.util.save.SaveContentType;

import picocli.CommandLine.Option;

public abstract class AbstractAlg extends AbstractInputOutputFile {

    @Option(names = { "-r", "--relaxation-enabled" },
        defaultValue = "false",
        description = "Enables relaxation mechanism.")
    protected boolean relaxEnabled;

    @Option(names = { "--relaxation-spread-strategy" },
        defaultValue = Defaults.DEFAULT_PARAM_RELAX_SPREAD_STRATEGY,
        description = Messages.RELAXATION_SPREAD_STRAT_DESCRIPTION)
    protected RelaxationSpreadStrategy relaxationSpreadStrategy;

    @Option(names = { "--optimization-criterion"},
        defaultValue = Defaults.DEFAULT_PARAM_OPTIMIZATION_CRITERION,
        description = "One of: (${COMPLETION-CANDIDATES})."
            + " Default: ${DEFAULT-VALUE}.")
    protected OptimizationCriterion optimizationCriterion;

    @Option(names = { "-d", "--disable-time-measurement-metadata" },
        defaultValue = "false",
        description = "Disables adding time measurement metadata to the"
            + " generated cutting plan.")
    protected boolean disableTimeMeasurementMetadata;

    protected Order prepareOrder() throws FosscutException, IOException {
        boolean quietModeRequested = fossCut.getQuietModeRequested();
        redisConnectionSecrets = fossCut.getRedisConnectionSecrets();

        LogFormatter logFormatter = new LogFormatter(quietModeRequested);
        logFormatter.configure();

        OrderLoader orderLoader = new OrderLoader(redisConnectionSecrets);
        String orderString = orderLoader.load(orderPath);

        YamlLoader yamlLoader = new YamlLoader();
        Order order = yamlLoader.loadOrder(orderString);

        OrderValidator validator = new OrderValidator(optimizationCriterion);
        validator.validateOrder(order);

        Cleaner cleaner = new Cleaner();
        cleaner.cleanOrder(order);

        return order;
    }

    protected void validateRelax(Order order, Double relaxCost) throws FosscutException {
        RelaxValidator relaxValidator = new RelaxValidator(relaxEnabled, relaxCost);
        relaxValidator.validate(order);
    }

    protected CompletableFuture<Plan> generatePlanFuture(Order order) {
        CompletableFuture<Plan> future = CompletableFuture.supplyAsync(() -> {
            try {
                return generatePlan(order);
            } catch (FosscutException e) {
                throw new CompletionException(e);
            }
        });

        return future.orTimeout(timeoutAmount, timeoutUnit);
    }

    protected abstract Plan generatePlan(Order order) throws FosscutException;

    protected void handlePlanFuture(CompletableFuture<Plan> future)
    throws FosscutException, TimeoutException {
        try {
            Plan cuttingPlan = future.join();
            handlePlan(cuttingPlan);
            validatePlan(cuttingPlan);
        } catch (CompletionException e) {
            if (e.getCause() instanceof TimeoutException) {
                handlePlan(new Plan()); // print or save info about plan time out
                StringWriter sw = new StringWriter();
                e.getCause().printStackTrace(new PrintWriter(sw));
                throw new TimeoutException(
                    sw.toString()
                    + Messages.PLAN_GENERATION_TIMEOUT
                    + timeoutAmount + " " + timeoutUnit.toString().toLowerCase()
                    + "."
                );
            } else {
                throw e; // rethrow FosscutExceptions and others
            }
        }
    }

    private void handlePlan(Plan cuttingPlan) throws PlanValidationException {
        String cuttingPlanString = null;
        if (outputFormat == OutputFormat.yaml) {
            YamlDumper yamlDumper = new YamlDumper();
            cuttingPlanString = yamlDumper.dump(cuttingPlan);
        }

        Save save = new Save(
            SaveContentType.PLAN,
            cuttingPlanString,
            RedisUriParser.getOrderUri(orderPath),
            redisConnectionSecrets);
        save.save(outputFile);

        PrintResult printResult = new PrintResult("cutting plan", outputFile);
        printResult.print(cuttingPlanString);
    }

    private void validatePlan(Plan cuttingPlan) throws PlanValidationException {
        PlanValidator planValidator = new PlanValidator();
        planValidator.validatePlan(cuttingPlan);
    }

}
