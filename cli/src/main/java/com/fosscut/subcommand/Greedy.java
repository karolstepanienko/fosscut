package com.fosscut.subcommand;

import java.io.File;
import java.io.IOException;

import com.fosscut.alg.greedy.GreedyAlg;
import com.fosscut.shared.exception.FosscutException;
import com.fosscut.shared.type.IntegerSolver;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.util.Validator;
import com.fosscut.shared.util.load.YamlLoader;
import com.fosscut.shared.util.save.YamlDumper;
import com.fosscut.subcommand.abs.AbstractAlg;
import com.fosscut.type.OutputFormat;
import com.fosscut.util.Cleaner;
import com.fosscut.util.Defaults;
import com.fosscut.util.LogFormatter;
import com.fosscut.util.PrintResult;
import com.fosscut.util.PropertiesVersionProvider;
import com.fosscut.util.load.OrderLoader;
import com.fosscut.util.save.Save;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;

@Command(name = "greedy", versionProvider = PropertiesVersionProvider.class)
public class Greedy extends AbstractAlg {

    private Double relaxCost;

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

    @Option(names = { "--integer-solver" },
        defaultValue = Defaults.DEFAULT_PARAM_INTEGER_SOLVER,
        description = "One of: (${COMPLETION-CANDIDATES}).")
    private IntegerSolver integerSolver;

    @Spec
    private CommandSpec spec;

    @Override
    protected void runWithExceptions() throws FosscutException, IOException {
        boolean quietModeRequested = fossCut.getQuietModeRequested();
        File redisConnectionSecrets = fossCut.getRedisConnectionSecrets();

        LogFormatter logFormatter = new LogFormatter(quietModeRequested);
        logFormatter.configure();

        OrderLoader orderLoader = new OrderLoader(redisConnectionSecrets);
        String orderString = orderLoader.load(orderPath);

        YamlLoader yamlLoader = new YamlLoader();
        Order order = yamlLoader.loadOrder(orderString);

        Validator validator = new Validator(optimizationCriterion);
        validator.validateOrder(order);

        Cleaner cleaner = new Cleaner();
        cleaner.cleanOrder(order);

        GreedyAlg greedy = new GreedyAlg(order, relaxCost, relaxEnabled,
            optimizationCriterion, integerSolver, forceIntegerRelax);
        greedy.run();

        String cuttingPlan = null;
        if (outputFormat == OutputFormat.yaml) {
            YamlDumper yamlDumper = new YamlDumper();
            cuttingPlan = yamlDumper.dump(greedy.getCuttingPlan());
        }

        Save save = new Save(cuttingPlan, orderLoader.getOrderUri(orderPath),
            redisConnectionSecrets);
        save.save(outputFile);

        PrintResult printResult = new PrintResult("cutting plan", outputFile);
        printResult.print(cuttingPlan);
    }

}
