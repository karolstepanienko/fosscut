package com.fosscut.util;

import java.io.File;

public class Defaults {
    public static final String FOSSCUT_BINARY_FOLDER_PATH = "./build/native/nativeCompile";
    public static final String FOSSCUT_BINARY_NAME = "fosscut";

    public static File getFosscutBinaryFolderPath() {
        return new File(FOSSCUT_BINARY_FOLDER_PATH);
    }

    public static File getFosscutBinaryPath() {
        return new File(FOSSCUT_BINARY_FOLDER_PATH + File.separator + FOSSCUT_BINARY_NAME);
    }

    public static String getFosscutBinaryExecutable() {
        return "." + File.separator + FOSSCUT_BINARY_NAME;
    }

    public static final String LINUX_SHELL = "bash";
    public static final String LINUX_CMD_PASS_OPTION = "-c";
    public static final String WINDOWS_SHELL = "powershell.exe";

    public static final long DEFAULT_COMMAND_TIMEOUT = 5;

    public static final String VERSION_STRING = "fosscut version \"0.0.1\"";
}
