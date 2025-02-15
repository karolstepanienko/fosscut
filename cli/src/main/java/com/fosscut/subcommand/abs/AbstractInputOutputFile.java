package com.fosscut.subcommand.abs;

import java.io.File;

import com.fosscut.FossCut;
import com.fosscut.type.OutputFormat;

import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

public abstract class AbstractInputOutputFile extends AbstractRunnable {

    @ParentCommand
    protected FossCut fossCut;

    @Parameters(paramLabel = "<order-path>", arity = "1",
        description = "Path or a redis URL to a YAML file containing an order.")
    protected String orderPath;

    @Option(names = { "-f", "--format" },
        defaultValue = "yaml",
        description = "Output format. One of: (${COMPLETION-CANDIDATES}).")
    protected OutputFormat outputFormat;

    @Option(names = { "-o", "--output" },
        description = "Path to the file where the cutting plan will be saved.")
    protected File outputFile;

}
