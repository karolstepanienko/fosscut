package com.fosscut.subcommand;

import java.io.IOException;

import com.fosscut.shared.exception.FosscutException;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.util.Validator;
import com.fosscut.shared.util.load.YamlLoader;
import com.fosscut.subcommand.abs.AbstractInputFile;
import com.fosscut.util.LogFormatter;
import com.fosscut.util.PropertiesVersionProvider;
import com.fosscut.util.load.OrderLoader;

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
