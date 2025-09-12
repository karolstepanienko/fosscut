package com.fosscut.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fosscut.util.Command;
import com.fosscut.util.RepetitiveTests;

public class FossCut {
    @Test public void fosscutCommand() {
        Command command = new Command("");
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getError().contains("Usage"));
    }

    @Test public void shortHelp() {
        RepetitiveTests.testHelp(new Command("-h"));
    }

    @Test public void longHelp() {
        RepetitiveTests.testHelp(new Command("--help"));
    }

    @Test public void shortVersion() {
        RepetitiveTests.testVersion(new Command("-v"));
    }

    @Test public void longVersion() {
        RepetitiveTests.testVersion(new Command("--version"));
    }
}
