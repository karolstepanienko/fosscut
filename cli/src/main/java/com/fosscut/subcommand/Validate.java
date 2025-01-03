package com.fosscut.subcommand;

import com.fosscut.FossCut;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.util.PropertiesVersionProvider;
import com.fosscut.util.Validator;
import com.fosscut.util.load.OrderLoader;
import com.fosscut.util.load.YamlLoader;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

@Command(name = "validate", versionProvider = PropertiesVersionProvider.class)
public class Validate implements Runnable {

    @Parameters(paramLabel = "<order-path>", arity = "1",
        description = "Path or a redis URL to a YAML file containing an order.")
    String orderPath;

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
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Validate()).execute(args);
        System.exit(exitCode);
    }
}
