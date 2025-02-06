package com.fosscut.subcommand;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.alg.greedy.Greedy;
import com.fosscut.exception.FosscutException;
import com.fosscut.type.IntegerSolvers;
import com.fosscut.type.OutputFormats;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.util.Defaults;
import com.fosscut.util.LogFormatter;
import com.fosscut.util.PrintResult;
import com.fosscut.util.PropertiesVersionProvider;
import com.fosscut.util.Validator;
import com.fosscut.util.load.OrderLoader;
import com.fosscut.util.load.YamlLoader;
import com.fosscut.util.save.Save;
import com.fosscut.util.save.YamlDumper;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;

@Command(name = "greedy", versionProvider = PropertiesVersionProvider.class)
public class GreedySubcommand extends AbstractAlg implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GreedySubcommand.class);

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
    private IntegerSolvers integerSolver;

    @Spec
    private CommandSpec spec;

    @Override
    public void run() {
        try {
            runWithExceptions();
        } catch (FosscutException e) {
            logger.error(e.getMessage());
            System.exit(1);
        }
    }

    private void runWithExceptions() throws FosscutException {
        boolean quietModeRequested = fossCut.getQuietModeRequested();
        File redisConnectionSecrets = fossCut.getRedisConnectionSecrets();

        LogFormatter logFormatter = new LogFormatter(quietModeRequested);
        logFormatter.configure();

        OrderLoader orderLoader = new OrderLoader(redisConnectionSecrets);
        String orderString = orderLoader.load(orderPath);

        YamlLoader yamlLoader = new YamlLoader();
        Order order = yamlLoader.loadOrder(orderString);

        Validator validator = new Validator();
        validator.validateOrder(order);

        Greedy greedy = new Greedy(order, relaxCost, forceIntegerRelax,
            integerSolver);
        greedy.run();

        String cuttingPlan = null;
        if (outputFormat == OutputFormats.yaml) {
            YamlDumper yamlDumper = new YamlDumper();
            cuttingPlan = yamlDumper.dump(greedy);
        }

        Save save = new Save(cuttingPlan, orderLoader.getOrderUri(orderPath),
            redisConnectionSecrets);
        save.save(outputFile);

        PrintResult printResult = new PrintResult("cutting plan", outputFile);
        printResult.print(cuttingPlan);
    }

}
