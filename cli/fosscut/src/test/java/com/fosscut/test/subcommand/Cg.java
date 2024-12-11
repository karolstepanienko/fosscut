package com.fosscut.test.subcommand;

import org.junit.Test;

import com.fosscut.util.Command;
import com.fosscut.util.RepetitiveTests;

public class Cg {
    @Test public void cgCommand() {
        RepetitiveTests.testHelpWithOrderPath(new Command("cg"));
    }

    @Test public void shortHelp() {
        RepetitiveTests.testHelp(new Command("cg -h"));
    }

    @Test public void longHelp() {
        RepetitiveTests.testHelp(new Command("cg --help"));
    }

    @Test public void shortVersion() {
        RepetitiveTests.testVersion(new Command("cg -v"));
    }

    @Test public void longVersion() {
        RepetitiveTests.testVersion(new Command("cg --version"));
    }
}
