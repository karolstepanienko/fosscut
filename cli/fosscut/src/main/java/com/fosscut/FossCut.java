package com.fosscut;

import com.fosscut.subcommand.Mwe;
import com.fosscut.subcommand.Validate;
import com.fosscut.utils.PropertiesVersionProvider;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;


@Command(name = "fosscut",
    versionProvider = PropertiesVersionProvider.class,
    subcommands = {Validate.class, Mwe.class},
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
