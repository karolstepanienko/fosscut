package com.fosscut.subcommand;

import java.io.File;

import com.fosscut.alg.gen.cut.CutGenAlg;
import com.fosscut.shared.exception.FosscutException;
import com.fosscut.shared.type.cutting.order.Order;
import com.fosscut.shared.util.save.YamlDumper;
import com.fosscut.subcommand.abs.AbstractGen;
import com.fosscut.type.OutputFormat;
import com.fosscut.util.PrintResult;
import com.fosscut.util.PropertiesVersionProvider;
import com.fosscut.util.RedisUriParser;
import com.fosscut.util.save.Save;
import com.fosscut.util.save.SaveContentType;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "cutgen", versionProvider = PropertiesVersionProvider.class)
public class CutGen extends AbstractGen {

    @Option(names = { "-d", "--average-output-demand" }, required = true)
    private int averageOutputDemand;

    @Option(names = { "-ao", "--allow-output-type-duplicates" },
        defaultValue = "false",
        description = "Some output types can be generated as duplicates."
            + " By default an error is thrown if it happens."
            + " This flag disables that error."
            + " Number of generated output types might be less than specified.")
    private boolean allowOutputTypeDuplicates;

    @Override
    protected void runWithExceptions() throws FosscutException {
        File redisConnectionSecrets = fossCut.getRedisConnectionSecrets();

        CutGenAlg cutGenAlg = new CutGenAlg(
            inputLength,
            inputTypeCount,
            minInputLength,
            maxInputLength,
            allowInputTypeDuplicates,
            averageOutputDemand,
            outputTypeCount,
            outputLengthLowerBound,
            outputLengthUpperBound,
            allowOutputTypeDuplicates,
            seed
        );

        Order order = null;
        order = cutGenAlg.nextOrder();

        String orderString = null;
        if (outputFormat == OutputFormat.yaml) {
            YamlDumper yamlDumper = new YamlDumper();
            orderString = yamlDumper.dump(order);
        }

        Save save = new Save(
            SaveContentType.ORDER,
            orderString,
            RedisUriParser.getOrderUri(outputPath),
            redisConnectionSecrets);
        save.save(outputPath);

        PrintResult printResult = new PrintResult("order", outputPath);
        printResult.print(orderString);
    }

}
