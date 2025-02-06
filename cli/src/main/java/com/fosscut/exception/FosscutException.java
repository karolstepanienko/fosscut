package com.fosscut.exception;

/*
 * General exception parent class used by all specific exceptions.
 */
public abstract class FosscutException extends Exception {

    public FosscutException(String errorMessage) {
        super(errorMessage);
    }

}
