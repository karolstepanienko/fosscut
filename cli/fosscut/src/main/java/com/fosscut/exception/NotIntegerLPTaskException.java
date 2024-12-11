package com.fosscut.exception;

public class NotIntegerLPTaskException extends Exception {
    private static String staticMessage =
        "Linear programming task should use only integer variables for this"
            + " method to work. ";

    public NotIntegerLPTaskException(String errorMessage) {
        super(staticMessage + errorMessage);
    }
}
