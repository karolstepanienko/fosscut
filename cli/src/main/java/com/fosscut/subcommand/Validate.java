package com.fosscut.subcommand;

import java.io.IOException;

import com.fosscut.exception.FosscutException;
import com.fosscut.subcommand.abs.AbstractInputFile;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.util.LogFormatter;
import com.fosscut.util.PropertiesVersionProvider;
import com.fosscut.util.Validator;
import com.fosscut.util.load.OrderLoader;
import com.fosscut.util.load.YamlLoader;

import picocli.CommandLine.Command;

@Command(name = "validate", versionProvider = PropertiesVersionProvider.class)
public class Validate extends AbstractInputFile {

    @Override
    protected void runWithExceptions() throws FosscutException, IOException {
        LogFormatter logFormatter = new LogFormatter(fossCut.getQuietModeRequested());
        logFormatter.configure();

        OrderLoader orderLoader = new OrderLoader(fossCut.getRedisConnectionSecrets());
        String orderString = orderLoader.load(orderPath);

        YamlLoader yamlLoader = new YamlLoader();
        Order order = yamlLoader.loadOrder(orderString);

        Validator validator = new Validator();
        validator.validateOrder(order);
    }

}
