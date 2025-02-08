package com.fosscut.exception;

import com.fosscut.util.Messages;

public class OrderFileIsADirectoryException extends FosscutException {

    public OrderFileIsADirectoryException() {
        super(Messages.ORDER_FILE_IS_A_DIRECTORY_EXCEPTION);
    }

}
