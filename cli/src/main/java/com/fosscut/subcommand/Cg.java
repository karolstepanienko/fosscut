package com.fosscut.subcommand;

import java.io.File;

import com.fosscut.FossCut;
import com.fosscut.alg.cg.ColumnGeneration;
import com.fosscut.exception.NotIntegerLPTaskException;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.util.OutputFormats;
import com.fosscut.util.PropertiesVersionProvider;
import com.fosscut.util.Validator;
import com.fosscut.util.load.OrderLoader;
import com.fosscut.util.load.YamlLoader;
import com.fosscut.util.save.Save;
import com.fosscut.util.save.YamlDumper;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;
import picocli.CommandLine.Spec;

@Command(name = "cg", versionProvider = PropertiesVersionProvider.class)
public class Cg implements Runnable {

    @Parameters(paramLabel = "<order-path>", arity = "1",
        description = "Path or a redis URL to a YAML file containing an order.")
    String orderPath;

    @Option(names = { "-f", "--format" },
        defaultValue = "yaml",
        description = "Output format. One of: (${COMPLETION-CANDIDATES}).")
    OutputFormats outputFormat;

    @Option(names = { "-o", "--output" },
        description = "Path to the file where the cutting plan will be saved.")
    File outputFile;

    @Option(names = { "-i", "--integer-relaxation" },
        description = "Enforces integer constraints on relaxation values."
         + " By default relaxation values can be floating point numbers.")
    boolean integerRelax;

    Double relaxCost;

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

    @Spec
    CommandSpec spec;

    @ParentCommand
    private FossCut fossCut;

    @Override
    public void run() {
        boolean quietModeRequested = fossCut.getQuietModeRequested();

        OrderLoader orderLoader = new OrderLoader(fossCut.getRedisConnectionSecrets(), quietModeRequested);
        String orderString = orderLoader.load(orderPath);

        YamlLoader yamlLoader = new YamlLoader(quietModeRequested);
        Order order = yamlLoader.loadOrder(orderString);

        Validator validator = new Validator(quietModeRequested);
        validator.validateOrder(order);

        ColumnGeneration columnGeneration = new ColumnGeneration(
            order, relaxCost, integerRelax, quietModeRequested);
        columnGeneration.run();

        String cuttingPlan = null;
        if (outputFormat == OutputFormats.yaml) {
            YamlDumper yamlDumper = new YamlDumper();
            cuttingPlan = yamlDumper.dump(columnGeneration);
        }

        Save save = new Save(cuttingPlan, quietModeRequested);
        save.save(outputFile);
    }

    public static void main(String[] args) throws NotIntegerLPTaskException {
        int exitCode = new CommandLine(new Cg()).execute(args);
        System.exit(exitCode);
    }
}
