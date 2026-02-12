package com.fosscut.subcommand.abs;

import java.io.File;

import com.fosscut.shared.util.save.YamlDumper;
import com.fosscut.type.OutputFormat;
import com.fosscut.util.PrintResult;
import com.fosscut.util.RedisUriParser;
import com.fosscut.util.save.Save;
import com.fosscut.util.save.SaveContentType;

import picocli.CommandLine.Option;

/**
 * Abstract class for commands generating cutting orders.
 * It provides common options for specifying input and output element parameters.
 */
public abstract class AbstractGen extends AbstractOutputFile {

    @Option(names = { "--seed" },
        description = "Seed for random number generator."
    )
    protected Long seed;

    @Option(names = { "-otrp", "--output-types-to-relax-percentage" },
        description = "Percentage of output types to relax. null means no relaxation."
    )
    protected Integer outputTypesToRelaxPercentage;

    @Option(names = { "-otlrp", "--output-type-length-relax-percentage" },
        description = "Percentage of output type lengths to relax. null means no relaxation."
    )
    protected Integer outputTypeLengthRelaxPercentage;

    /******************************* Inputs ***********************************/

    @Option(names = { "-i", "--input-length" },
        description = "Input length."
            + "Specify only if exactly one input element type should be used."
    )
    protected Integer inputLength;

    @Option(names = { "-it", "--input-type-count" },
        description = "Number of input element types."
    )
    protected Integer inputTypeCount;

    @Option(names = { "-il", "--min-input-length" })
    protected Integer minInputLength;

    @Option(names = { "-iu", "--max-input-length" })
    protected Integer maxInputLength;

    @Option(names = { "-ai", "--allow-input-type-duplicates" },
        defaultValue = "false",
        description = "Some input types can be generated as duplicates."
        + "By default an error is thrown if it happens."
        + "This flag disables that error."
        + "Number of generated input types might be less than specified.")
    protected boolean allowInputTypeDuplicates;

    /****************************** Outputs ***********************************/

    @Option(names = { "-ot", "--output-type-count" },
        required = true,
        description = "Number of output element types."
    )
    protected int outputTypeCount;

    @Option(names = { "-ol", "--output-length-lower-bound" },
        description = "Allowed values from 0 to 1."
            + " Defines minimal output element length based on input"
            + " element length.",
        required = true)
    protected double outputLengthLowerBound;

    @Option(names = { "-ou", "--output-length-upper-bound" },
        description = "Allowed values from 0 to 1."
            + " Defines maximal output element length based on input"
            + " element length.",
        required = true)
    protected double outputLengthUpperBound;

    @Option(names = { "-oc", "--output-count" },
        description = "Minimal number of outputs in the order."
            + " Actual number might be higher due to patterns"
            + " generating multiple outputs.",
        required = true)
    protected int outputCount;

    /********************** Shared generator methods **************************/

    protected void handleGeneratedOrder(Object object) {
        File redisConnectionSecrets = fossCut.getRedisConnectionSecrets();

        String orderString = null;
        if (outputFormat == OutputFormat.yaml) {
            YamlDumper yamlDumper = new YamlDumper();
            orderString = yamlDumper.dump(object);
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
