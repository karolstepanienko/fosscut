package com.fosscut.subcommand;

import java.io.File;

import com.fosscut.util.OutputFormats;

import picocli.CommandLine.Option;

public abstract class AbstractFile {

    @Option(names = { "-f", "--format" },
        defaultValue = "yaml",
        description = "Output format. One of: (${COMPLETION-CANDIDATES}).")
    protected OutputFormats outputFormat;

    @Option(names = { "-o", "--output" },
        description = "Path to the file where the cutting plan will be saved.")
    protected File outputFile;

}
