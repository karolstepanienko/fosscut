package com.fosscut.subcommand;

import java.io.File;

import com.fosscut.type.Order;
import com.fosscut.utils.PropertiesVersionProvider;
import com.fosscut.utils.Validator;
import com.fosscut.utils.YamlLoader;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "validate", versionProvider = PropertiesVersionProvider.class)
public class Validate implements Runnable {

    @Parameters(paramLabel = "<order-path>", arity = "1",
        description = "Path to a YAML file containing an order")
    File orderFile;

    @Override
    public void run() {
        YamlLoader yamlLoader = new YamlLoader();
        Order order = yamlLoader.loadOrder(orderFile);

        Validator validator = new Validator();
        validator.validateOrder(order);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Validate()).execute(args);
        System.exit(exitCode);
    }
}
