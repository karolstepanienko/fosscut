package com.fosscut.subcommand;

import com.fosscut.mwe.OrTools;
import com.fosscut.util.PropertiesVersionProvider;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "mwe", versionProvider = PropertiesVersionProvider.class)
public class Mwe {

    @Command(name = "ortools")
    public void status() {
        OrTools.main();
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Validate()).execute(args);
        System.exit(exitCode);
    }

}
