package com.fosscut.subcommand.abs;

import com.fosscut.FossCut;
import com.fosscut.type.OutputFormat;

import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

public abstract class AbstractOutputFile extends AbstractRunnable {

    @ParentCommand
    protected FossCut fossCut;

    @Option(names = { "-f", "--format" },
        defaultValue = "yaml",
        description = "Output format. One of: (${COMPLETION-CANDIDATES}).")
    protected OutputFormat outputFormat;

    @Option(names = { "-o", "--output" },
        description = "Path or a redis URL to a location where the cutting"
        + " order/plan should be saved.")
    protected String outputPath;

}
