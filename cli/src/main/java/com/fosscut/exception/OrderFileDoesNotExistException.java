package com.fosscut.exception;

import com.fosscut.util.Messages;

public class OrderFileDoesNotExistException extends FosscutException {

    public OrderFileDoesNotExistException() {
        super(Messages.ORDER_FILE_DOES_NOT_EXIST_EXCEPTION);
    }

}
