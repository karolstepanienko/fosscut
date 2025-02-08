package com.fosscut.subcommand.abs;

import com.fosscut.FossCut;

import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

public abstract class AbstractInputFile extends AbstractRunnable {

    @ParentCommand
    protected FossCut fossCut;

    @Parameters(paramLabel = "<order-path>", arity = "1",
        description = "Path or a redis URL to a YAML file containing an order.")
    protected String orderPath;

}
