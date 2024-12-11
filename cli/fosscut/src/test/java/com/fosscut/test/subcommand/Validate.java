package com.fosscut.test.subcommand;

import org.junit.Test;

import com.fosscut.util.Command;
import com.fosscut.util.RepetitiveTests;

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
}
