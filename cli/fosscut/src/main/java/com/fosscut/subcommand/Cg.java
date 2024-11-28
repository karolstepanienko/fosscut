package com.fosscut.subcommand;

import java.io.File;

import com.fosscut.alg.ColumnGeneration;
import com.fosscut.type.Order;
import com.fosscut.utils.PropertiesVersionProvider;
import com.fosscut.utils.Validator;
import com.fosscut.utils.YamlLoader;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "cg",
    versionProvider = PropertiesVersionProvider.class,
    mixinStandardHelpOptions = true)
public class Cg implements Runnable {
    @Parameters(paramLabel = "<order-path>", arity = "1",
        description = "Path to a YAML file containing an order")
    File orderFile;

    @Override
    public void run() {
        YamlLoader yamlLoader = new YamlLoader();
        Order order = yamlLoader.loadOrder(orderFile);

        Validator validator = new Validator();
        validator.validateOrder(order);

        ColumnGeneration columnGeneration = new ColumnGeneration();
        columnGeneration.run(order);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Cg()).execute(args);
        System.exit(exitCode);
    }
}
