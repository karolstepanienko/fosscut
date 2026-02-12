package com.fosscut.exception;

import com.fosscut.shared.exception.FosscutException;
import com.fosscut.util.Messages;

public class NoRelaxationAppliedException extends FosscutException {

    public NoRelaxationAppliedException(String errorMessage) {
        super(Messages.RELAX_APPLY_NO_RELAX_APPLIED + errorMessage);
    }

}
