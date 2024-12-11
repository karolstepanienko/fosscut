package com.fosscut.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Command {
    private String command;
    private String args;
    private long timeout;

    private String output;
    private String error;
    private boolean exited;
    private int exitCode;

    public Command(String args) {
        this.command = Defaults.getFosscutBinaryExecutable();
        this.args = args;
        this.timeout = Defaults.DEFAULT_COMMAND_TIMEOUT;
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
            processBuilder.directory(Defaults.getFosscutBinaryFolderPath());

            String fullCommand = this.command + " " + this.args;
            if (isLinux())
                processBuilder.command(Defaults.LINUX_SHELL, Defaults.LINUX_CMD_PASS_OPTION, fullCommand);
            else if (isWindows())
                processBuilder.command(Defaults.WINDOWS_SHELL, fullCommand);
            else {
                System.err.println("Only linux and windows are supported.");
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
            System.err.println("Command run failed. Exception:");
            e.printStackTrace();
        }
    }

    private boolean isLinux() {
        return System.getProperty("os.name").equals("Linux");
    }

    private boolean isWindows() {
        return System.getProperty("os.name").contains("Windows");
    }
}
