package com.fosscut.exception;

import com.fosscut.shared.exception.FosscutException;
import com.fosscut.util.Messages;

public class GeneratedPatternsCannotBeEmptyException extends FosscutException {

    public GeneratedPatternsCannotBeEmptyException(String errorMessage) {
        super(Messages.UNABLE_TO_GENERATE_NEW_PATTERNS + errorMessage);
    }

}
