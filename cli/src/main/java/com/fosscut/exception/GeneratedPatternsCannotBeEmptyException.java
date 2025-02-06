package com.fosscut.exception;

public class GeneratedPatternsCannotBeEmptyException extends FosscutException {

    private static String staticMessage =
        "Algorithm was not able to generate new patterns."
        + " Specified numer of input elements is not enough to generate a"
        + " cutting plan. Please increase the 'count' field value of input"
        + " elements.";

    public GeneratedPatternsCannotBeEmptyException(String errorMessage) {
        super(staticMessage + errorMessage);
    }

}
