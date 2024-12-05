package com.fosscut.subcommand;

import java.io.File;

import com.fosscut.alg.cg.ColumnGeneration;
import com.fosscut.type.Order;
import com.fosscut.utils.PropertiesVersionProvider;
import com.fosscut.utils.Validator;
import com.fosscut.utils.YamlLoader;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

@Command(name = "cg", versionProvider = PropertiesVersionProvider.class)
public class Cg implements Runnable {

    @Parameters(paramLabel = "<order-path>", arity = "1",
        description = "Path to a YAML file containing an order.")
    File orderFile;

    @Spec
    CommandSpec spec;

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

    @Override
    public void run() {
        YamlLoader yamlLoader = new YamlLoader();
        Order order = yamlLoader.loadOrder(orderFile);

        Validator validator = new Validator();
        validator.validateOrder(order);

        ColumnGeneration columnGeneration = new ColumnGeneration(relaxCost);
        columnGeneration.run(order);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Cg()).execute(args);
        System.exit(exitCode);
    }
}
