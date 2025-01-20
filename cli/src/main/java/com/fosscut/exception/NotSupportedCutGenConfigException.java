package com.fosscut.exception;

public class NotSupportedCutGenConfigException extends Exception {

    private static String staticMessage =
        "Unsupported parameter configuration for CUTGEN generator detected.";

    public NotSupportedCutGenConfigException(String errorMessage) {
        super(staticMessage + errorMessage);
    }

}
