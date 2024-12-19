package com.fosscut.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {
    public static File getFosscutBinaryFolderPath() {
        return new File(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH);
    }

    public static File getFosscutBinaryPath() {
        return new File(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + getFosscutBinaryName());
    }

    public static String getFosscutBinaryExecutable() {
        return "." + File.separator + getFosscutBinaryName();
    }

    public static String getAbsolutePath(String relativePath) {
        return new File(relativePath).getAbsolutePath();
    }

    public static String loadFile(String path) throws IOException {
        return Files.readString(Paths.get(TestDefaults.SIMPLE_CG_PLAN));
    }

    public static boolean isLinux() {
        return System.getProperty("os.name").equals("Linux");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").contains("Windows");
    }

    private static String getFosscutBinaryName() {
        String binaryName = TestDefaults.FOSSCUT_LINUX_BINARY_NAME;
        if (isWindows()) binaryName = TestDefaults.FOSSCUT_WINDOWS_BINARY_NAME;
        return binaryName;
    }
}
