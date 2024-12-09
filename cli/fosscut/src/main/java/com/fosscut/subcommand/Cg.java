package com.fosscut.subcommand;

import java.io.File;

import com.fosscut.FossCut;
import com.fosscut.alg.cg.ColumnGeneration;
import com.fosscut.exceptions.NotIntegerLPTaskException;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.utils.OutputFormats;
import com.fosscut.utils.PropertiesVersionProvider;
import com.fosscut.utils.Save;
import com.fosscut.utils.Validator;
import com.fosscut.utils.YamlDumper;
import com.fosscut.utils.YamlLoader;

import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
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
        description = "Path to a YAML file containing an order.")
    File orderFile;

    @Option(names = { "-f", "--format" },
        defaultValue = "yaml",
        description = "Output format. One of: (${COMPLETION-CANDIDATES}).")
    OutputFormats outputFormat;

    @Option(names = { "-o", "--output" },
        description = "Path to the file where the cutting plan will be saved.")
    File outputFile;

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
        YamlLoader yamlLoader = new YamlLoader(quietModeRequested);
        Order order = yamlLoader.loadOrder(orderFile);

        Validator validator = new Validator(quietModeRequested);
        validator.validateOrder(order);

        ColumnGeneration columnGeneration = new ColumnGeneration(order, relaxCost, quietModeRequested);
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
