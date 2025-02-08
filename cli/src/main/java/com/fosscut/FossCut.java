package com.fosscut;

import java.io.File;

import com.fosscut.subcommand.CG;
import com.fosscut.subcommand.CutGen;
import com.fosscut.subcommand.FFD;
import com.fosscut.subcommand.Greedy;
import com.fosscut.subcommand.MWE;
import com.fosscut.subcommand.Validate;
import com.fosscut.util.PropertiesVersionProvider;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(name = "fosscut",
    versionProvider = PropertiesVersionProvider.class,
    subcommands = {
        CG.class,
        CutGen.class,
        FFD.class,
        Greedy.class,
        Validate.class,
        MWE.class
    }
)
public class FossCut implements Runnable {

    @Option(names = {"-h", "--help"}, usageHelp = true,
        scope = CommandLine.ScopeType.INHERIT,
        description = "Show this help message and exit.")
    private boolean usageHelpRequested;

    @Option(names = {"-v", "--version"}, versionHelp = true,
        scope = CommandLine.ScopeType.INHERIT,
        description = "Print version information and exit.")
    private boolean versionInfoRequested;

    @Option(names = {"-q", "--quiet", "-s", "--silent"},
        scope = CommandLine.ScopeType.INHERIT,
        description = "Quiet mode. Inhibits the usual output."
    )
    private boolean quietModeRequested;

    @Option(names = {"--redis-connection-secrets"},
        scope = CommandLine.ScopeType.INHERIT,
        description = "Allows configuration of a redis connection to download"
            + " the order file."
    )
    private File redisConnectionSecrets;

    @Spec
    CommandSpec spec;

    public boolean getQuietModeRequested() {
        return this.quietModeRequested;
    }

    public File getRedisConnectionSecrets() {
        return this.redisConnectionSecrets;
    }

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
