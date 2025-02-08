package com.fosscut.subcommand;

import java.io.File;
import java.io.IOException;

import com.fosscut.alg.ffd.FirstFitDecreasing;
import com.fosscut.exception.FosscutException;
import com.fosscut.subcommand.abs.AbstractAlg;
import com.fosscut.type.OutputFormats;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.util.Cleaner;
import com.fosscut.util.LogFormatter;
import com.fosscut.util.PrintResult;
import com.fosscut.util.PropertiesVersionProvider;
import com.fosscut.util.Validator;
import com.fosscut.util.load.OrderLoader;
import com.fosscut.util.load.YamlLoader;
import com.fosscut.util.save.Save;
import com.fosscut.util.save.YamlDumper;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "ffd", versionProvider = PropertiesVersionProvider.class)
public class FFD extends AbstractAlg {

    @Option(names = { "-r", "--relaxation-enabled" },
        defaultValue = "false",
        description = "Enables relaxation mechanism.")
    private boolean relaxEnabled;

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

        Validator validator = new Validator();
        validator.validateOrder(order);

        Cleaner cleaner = new Cleaner();
        cleaner.cleanOrder(order);

        FirstFitDecreasing firstFitDecreasing = new FirstFitDecreasing(
            order,
            relaxEnabled,
            forceIntegerRelax
        );
        firstFitDecreasing.run();

        String cuttingPlan = null;
        if (outputFormat == OutputFormats.yaml) {
            YamlDumper yamlDumper = new YamlDumper();
            cuttingPlan = yamlDumper.dump(firstFitDecreasing);
        }

        Save save = new Save(cuttingPlan, orderLoader.getOrderUri(orderPath),
            redisConnectionSecrets);
        save.save(outputFile);

        PrintResult printResult = new PrintResult("cutting plan", outputFile);
        printResult.print(cuttingPlan);
    }

}
