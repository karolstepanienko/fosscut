package com.fosscut.subcommand;

import java.io.File;

import com.fosscut.FossCut;
import com.fosscut.alg.ffd.FirstFitDecreasing;
import com.fosscut.exception.NotIntegerLPTaskException;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.util.LogFormatter;
import com.fosscut.util.PropertiesVersionProvider;
import com.fosscut.util.Validator;
import com.fosscut.util.load.OrderLoader;
import com.fosscut.util.load.YamlLoader;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

@Command(name = "ffd", versionProvider = PropertiesVersionProvider.class)
public class Ffd implements Runnable {
    @Parameters(paramLabel = "<order-path>", arity = "1",
        description = "Path or a redis URL to a YAML file containing an order.")
    String orderPath;

    @ParentCommand
    private FossCut fossCut;

    @Override
    public void run() {
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

        FirstFitDecreasing firstFitDecreasing = new FirstFitDecreasing(order);
        firstFitDecreasing.run();
    }

    public static void main(String[] args) throws NotIntegerLPTaskException {
        int exitCode = new CommandLine(new Ffd()).execute(args);
        System.exit(exitCode);
    }
}
