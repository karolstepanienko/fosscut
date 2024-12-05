package com.fosscut;

import com.fosscut.subcommand.Cg;
import com.fosscut.subcommand.Mwe;
import com.fosscut.subcommand.Validate;
import com.fosscut.utils.PropertiesVersionProvider;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(name = "fosscut",
    versionProvider = PropertiesVersionProvider.class,
    subcommands = {Cg.class, Validate.class, Mwe.class})
public class FossCut implements Runnable {

    @Option(names = {"-h", "--help"}, usageHelp = true,
        scope = CommandLine.ScopeType.INHERIT,
        description = "Show this help message and exit.")
    boolean usageHelpRequested;

    @Option(names = {"-v", "--version"}, versionHelp = true,
        scope = CommandLine.ScopeType.INHERIT,
        description = "Print version information and exit.")
    boolean versionInfoRequested;

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
