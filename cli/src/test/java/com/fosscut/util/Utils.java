package com.fosscut.util;

import java.io.File;

public class Utils {
    public static File getFosscutBinaryFolderPath() {
        return new File(Defaults.FOSSCUT_BINARY_FOLDER_PATH);
    }

    public static File getFosscutBinaryPath() {
        return new File(Defaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + getFosscutBinaryName());
    }

    public static String getFosscutBinaryExecutable() {
        return "." + File.separator + getFosscutBinaryName();
    }

    public static String getAbsolutePath(String relativePath) {
        return new File(relativePath).getAbsolutePath();
    }

    public static boolean isLinux() {
        return System.getProperty("os.name").equals("Linux");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").contains("Windows");
    }

    private static String getFosscutBinaryName() {
        String binaryName = Defaults.FOSSCUT_LINUX_BINARY_NAME;
        if (isWindows()) binaryName = Defaults.FOSSCUT_WINDOWS_BINARY_NAME;
        return binaryName;
    }
}
