package com.fosscut.subcommand;

import com.fosscut.FossCut;

import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

/*
 * Defines all common parameters and options of algorithm subcommands.
 */
public abstract class AbstractAlg extends AbstractFile {

    @Parameters(paramLabel = "<order-path>", arity = "1",
        description = "Path or a redis URL to a YAML file containing an order.")
    protected String orderPath;

    @Option(names = { "-i", "--integer-relaxation" },
        description = "Enforces integer constraints on relaxation values."
         + " By default relaxation values can be floating point numbers.")
    protected boolean forceIntegerRelax;

    @ParentCommand
    protected FossCut fossCut;

}
