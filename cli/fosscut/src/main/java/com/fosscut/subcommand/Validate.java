package com.fosscut.subcommand;

import com.fosscut.utils.PropertiesVersionProvider;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "validate",
    versionProvider = PropertiesVersionProvider.class,
    mixinStandardHelpOptions = true)
public class Validate implements Runnable {

    @Override
    public void run() {
        System.out.println("Hello from validate");
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Validate()).execute(args);
        System.exit(exitCode);
    }
}
