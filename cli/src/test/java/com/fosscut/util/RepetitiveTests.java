package com.fosscut.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RepetitiveTests {
    public static void testHelp(Command command) {
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().contains("Usage"));
    }

    public static void testHelpWithOrderPath(Command command) {
        command.run();
        assert(command.getError().contains("Usage"));
        assert(command.getError().contains("Missing required parameter: '<order-path>'"));
        if (Utils.isLinux()) assertEquals(2, command.getExitCode());
        if (Utils.isWindows()) assertEquals(1, command.getExitCode());
    }

    public static void testVersion(Command command) {
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().contains(TestDefaults.VERSION_STRING));
        assert(command.getOutput().contains("Built: "));
        String dateString = extractAfter(command.getOutput(), "Built: ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateTime = null;
        dateTime = LocalDateTime.parse(dateString, formatter);
        assertNotNull(dateTime);
        assertEquals(0, command.getExitCode());
    }

    public static void testTimeout(Command command, String timeoutMessage) {
        command.run();
        assert(command.getOutput().contains(timeoutMessage));
        assertEquals(1, command.getExitCode());
    }

    private static String extractAfter(String input, String key) {
        int index = input.indexOf(key);
        if (index != -1) {
            // Return the substring after the key
            return input.substring(index + key.length()).trim();
        } else {
            return "";
        }
    }
}
