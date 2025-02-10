package com.fosscut.exception;

import com.fosscut.shared.exception.FosscutException;

public class NotIntegerLPTaskException extends FosscutException {

    private static String staticMessage =
        "Linear programming task should use only integer variables for this"
            + " method to work. ";

    public NotIntegerLPTaskException(String errorMessage) {
        super(staticMessage + errorMessage);
    }

}
