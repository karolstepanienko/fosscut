package com.fosscut.subcommand.abs;

import picocli.CommandLine.Option;

public abstract class AbstractAlg extends AbstractInputOutputFile {

    @Option(names = { "-i", "--integer-relaxation" },
        description = "Enforces integer constraints on relaxation values."
         + " By default relaxation values can be floating point numbers.")
    protected boolean forceIntegerRelax;

}
