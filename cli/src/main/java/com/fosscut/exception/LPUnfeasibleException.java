package com.fosscut.exception;

import com.fosscut.shared.exception.FosscutException;
import com.fosscut.util.Messages;

public class LPUnfeasibleException extends FosscutException {

    public LPUnfeasibleException(String errorMessage) {
        super(Messages.LP_UNFEASIBLE_EXCEPTION + errorMessage);
    }

}
