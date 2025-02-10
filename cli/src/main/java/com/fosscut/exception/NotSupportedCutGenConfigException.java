package com.fosscut.exception;

import com.fosscut.shared.exception.FosscutException;

public class NotSupportedCutGenConfigException extends FosscutException {

    private static String staticMessage =
        "Unsupported parameter configuration for CUTGEN generator detected.";

    public NotSupportedCutGenConfigException(String errorMessage) {
        super(staticMessage + errorMessage);
    }

}
