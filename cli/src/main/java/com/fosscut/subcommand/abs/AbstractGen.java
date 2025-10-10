package com.fosscut.subcommand.abs;

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

}
