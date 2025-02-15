package com.fosscut.subcommand.abs;

import java.io.File;

import com.fosscut.type.OutputFormat;

import picocli.CommandLine.Option;

public abstract class AbstractOutputFile extends AbstractRunnable {

    @Option(names = { "-f", "--format" },
        defaultValue = "yaml",
        description = "Output format. One of: (${COMPLETION-CANDIDATES}).")
    protected OutputFormat outputFormat;

    @Option(names = { "-o", "--output" },
        description = "Path to the file where the cutting plan will be saved.")
    protected File outputFile;

}
