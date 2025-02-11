package com.fosscut.exception;

import com.fosscut.shared.exception.FosscutException;
import com.fosscut.util.Messages;

public class NullCostException extends FosscutException {

    public NullCostException() {
        super(Messages.NULL_COST_EXCEPTION);
    }

}
