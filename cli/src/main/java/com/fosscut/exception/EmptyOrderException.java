package com.fosscut.exception;

import com.fosscut.shared.exception.FosscutException;

public class EmptyOrderException extends FosscutException {

    private static String staticMessage =
        "Loaded order file is empty";

    public EmptyOrderException(String errorMessage) {
        super(staticMessage + prepareMessage(errorMessage));
    }

    private static String prepareMessage(String errorMessage) {
        if (errorMessage == null || errorMessage.isEmpty()) {
            errorMessage = ".";
        } else {
            errorMessage = " for order path: " + errorMessage;
        }
        return errorMessage;
    }

}
