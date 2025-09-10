package com.fosscut.exception;

import com.fosscut.shared.exception.FosscutException;

public class PlanValidationException extends FosscutException {

    public PlanValidationException(String errorMessage) {
        super(errorMessage);
    }

}
