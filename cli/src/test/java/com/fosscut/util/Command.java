package com.fosscut.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Command {

    private static final Logger logger = LoggerFactory.getLogger(Command.class);

    private String command;
    private String args;
    private long timeout;

    private String output;
    private String error;
    private boolean exited;
    private int exitCode;

    public Command(String args) {
        this.command = Utils.getFosscutBinaryExecutable();
        this.args = args;
        this.timeout = TestDefaults.DEFAULT_COMMAND_TIMEOUT;
    }

    public Command(String command, String args, long timeout) {
        this.command = command;
        this.args = args;
        this.timeout = timeout;
    }

    public String getOutput() {
        return output;
    }

    public String getError() {
        return error;
    }

    public boolean getExited() {
        return exited;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void run() {
        try {
            // Define the command
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(Utils.getFosscutBinaryFolderPath());

            String fullCommand = this.command + " " + this.args;
            if (Utils.isLinux())
                processBuilder.command(TestDefaults.LINUX_SHELL, TestDefaults.LINUX_CMD_PASS_OPTION, fullCommand);
            else if (Utils.isWindows())
                processBuilder.command(TestDefaults.WINDOWS_SHELL, fullCommand);
            else {
                logger.error("Only linux and windows are supported.");
                System.exit(1);
            }

            // Start the process
            Process process = processBuilder.start();

            // Capture the output
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
                 BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()))) {
                this.output = reader.lines().collect(Collectors.joining("\n"));
                this.error = errorReader.lines().collect(Collectors.joining("\n"));
            }

            // Wait for the process to complete and get the exit code
            this.exited = process.waitFor(this.timeout, TimeUnit.SECONDS);
            this.exitCode = process.waitFor();
        } catch (IOException | InterruptedException e) {
            logger.error("Command run failed. Exception:");
            e.printStackTrace();
        }
    }

}
