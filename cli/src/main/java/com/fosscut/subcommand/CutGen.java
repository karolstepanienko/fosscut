package com.fosscut.subcommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.alg.cutgen.CutGenAlg;
import com.fosscut.exception.DuplicatesAreNotAllowedException;
import com.fosscut.exception.NotSupportedCutGenConfigException;
import com.fosscut.type.cutting.order.Order;
import com.fosscut.util.OutputFormats;
import com.fosscut.util.PrintResult;
import com.fosscut.util.PropertiesVersionProvider;
import com.fosscut.util.save.Save;
import com.fosscut.util.save.YamlDumper;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "cutgen", versionProvider = PropertiesVersionProvider.class)
public class CutGen extends AbstractFile implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(CutGen.class);

    @Option(names = { "-ot", "--output-type-count" },
        required = true,
        description = "Number of output element types."
    )
    private int outputTypeCount;

    @Option(names = { "-ol", "--output-length-lower-bound" }, required = true)
    private double outputLengthLowerBound;

    @Option(names = { "-ou", "--output-length-upper-bound" }, required = true)
    private double outputLengthUpperBound;

    @Option(names = { "-d", "--average-output-demand" }, required = true)
    private int averageOutputDemand;

    @Option(names = { "-i", "--input-length" },
        description = "Input length."
            + "Specify only if exactly one input element type should be used."
    )
    private Integer inputLength;

    @Option(names = { "-it", "--input-type-count" },
        description = "Number of input element types."
    )
    private Integer inputTypeCount;

    @Option(names = { "-il", "--input-length-lower-bound" })
    private Integer inputLengthLowerBound;

    @Option(names = { "-iu", "--input-length-upper-bound" })
    private Integer inputLengthUpperBound;

    @Option(names = { "--seed" },
        description = "Seed for the random number generator"
    )
    private Long seed;

    @Option(names = { "-ao", "--allow-output-type-duplicates" },
        defaultValue = "false",
        description = "Some output types can be generated as duplicates."
            + "By default an error is thrown if it happens."
            + "This flag disables that error."
            + "Number of generated output types might be less than specified.")
    private boolean allowOutputTypeDuplicates;

    @Option(names = { "-ai", "--allow-input-type-duplicates" },
        defaultValue = "false",
        description = "Some input types can be generated as duplicates."
        + "By default an error is thrown if it happens."
        + "This flag disables that error."
        + "Number of generated input types might be less than specified.")
    private boolean allowInputTypeDuplicates;

    @Override
    public void run() {
        CutGenAlg cutGenAlg = new CutGenAlg(
            outputTypeCount,
            outputLengthLowerBound,
            outputLengthUpperBound,
            averageOutputDemand,
            inputLength,
            inputTypeCount,
            inputLengthLowerBound,
            inputLengthUpperBound,
            seed,
            allowOutputTypeDuplicates,
            allowInputTypeDuplicates
        );

        Order order = null;
        try {
            order = cutGenAlg.nextOrder();
        } catch (
            NotSupportedCutGenConfigException
            | DuplicatesAreNotAllowedException e
        ) {
            logger.error(e.getMessage());
            System.exit(1);
        }

        String orderString = null;
        if (outputFormat == OutputFormats.yaml) {
            YamlDumper yamlDumper = new YamlDumper();
            orderString = yamlDumper.dump(order);
        }

        Save save = new Save(orderString);
        save.save(outputFile);

        PrintResult printResult = new PrintResult("order", outputFile);
        printResult.print(orderString);
    }

    public static void main(String[] args) throws NotSupportedCutGenConfigException {
        int exitCode = new CommandLine(new CutGen()).execute(args);
        System.exit(exitCode);
    }

}
