package com.fosscut.test.subcommand;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fosscut.util.Command;
import com.fosscut.util.Defaults;
import com.fosscut.util.Messages;
import com.fosscut.util.RepetitiveTests;
import com.fosscut.util.Utils;

public class Validate {
    @Test public void validateCommand() {
        RepetitiveTests.testHelpWithOrderPath(new Command("validate"));
    }

    @Test public void shortHelp() {
        RepetitiveTests.testHelp(new Command("validate -h"));
    }

    @Test public void longHelp() {
        RepetitiveTests.testHelp(new Command("validate --help"));
    }

    @Test public void shortVersion() {
        RepetitiveTests.testVersion(new Command("validate -v"));
    }

    @Test public void longVersion() {
        RepetitiveTests.testVersion(new Command("validate --version"));
    }

    @Test public void nonPositiveInput() {
        Command command = new Command("validate " + Utils.getAbsolutePath(Defaults.FAIL_VALIDATION_NON_POSITIVE_INPUT));
        command.run();
        assert(command.getError().contains(Messages.NON_POSITIVE_INPUT_LENGTH_ERROR));
        assertEquals(1, command.getExitCode());
    }

    @Test public void nonPositiveInputQuiet() {
        Command command = new Command("validate -q " + Utils.getAbsolutePath(Defaults.FAIL_VALIDATION_NON_POSITIVE_INPUT));
        command.run();
        assert(command.getError().equals(Messages.NON_POSITIVE_INPUT_LENGTH_ERROR));
        assertEquals(1, command.getExitCode());
    }

    @Test public void nonPositiveOutput() {
        Command command = new Command("validate " + Utils.getAbsolutePath(Defaults.FAIL_VALIDATION_NON_POSITIVE_OUTPUT));
        command.run();
        assert(command.getError().contains(Messages.NON_POSITIVE_OUTPUT_LENGTH_ERROR));
        assertEquals(1, command.getExitCode());
    }

    @Test public void nonPositiveOutputQuiet() {
        Command command = new Command("validate -q " + Utils.getAbsolutePath(Defaults.FAIL_VALIDATION_NON_POSITIVE_OUTPUT));
        command.run();
        assert(command.getError().equals(Messages.NON_POSITIVE_OUTPUT_LENGTH_ERROR));
        assertEquals(1, command.getExitCode());
    }

    @Test public void outputLongerThanInput() {
        Command command = new Command("validate " + Utils.getAbsolutePath(Defaults.FAIL_VALIDATION_OUTPUT_LONGER_THAN_INPUT));
        command.run();
        assert(command.getError().contains(Messages.OUTPUT_LONGER_THAN_INPUT_ERROR));
        assertEquals(1, command.getExitCode());
    }

    @Test public void outputLongerThanInputQuiet() {
        Command command = new Command("validate -q " + Utils.getAbsolutePath(Defaults.FAIL_VALIDATION_OUTPUT_LONGER_THAN_INPUT));
        command.run();
        assert(command.getError().equals(Messages.OUTPUT_LONGER_THAN_INPUT_ERROR));
        assertEquals(1, command.getExitCode());
    }
}
