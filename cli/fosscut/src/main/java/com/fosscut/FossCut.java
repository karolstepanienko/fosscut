package com.fosscut;

import com.fosscut.subcommand.Cg;
import com.fosscut.subcommand.Mwe;
import com.fosscut.subcommand.Validate;
import com.fosscut.utils.PropertiesVersionProvider;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

@Command(name = "fosscut",
    versionProvider = PropertiesVersionProvider.class,
    subcommands = {Cg.class, Validate.class, Mwe.class},
    mixinStandardHelpOptions = true)
public class FossCut implements Runnable {

    @Spec
    CommandSpec spec;

    @Override
    public void run() {
        // print the help menu if no subcommand is passed
        spec.commandLine().usage(System.err);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new FossCut()).execute(args);
        System.exit(exitCode);
    }
}
