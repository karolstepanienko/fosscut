package com.fosscut.exception;

import com.fosscut.shared.exception.FosscutException;

public class RedisOrderPathException extends FosscutException {

    public RedisOrderPathException(String errorMessage) {
        super(errorMessage);
    }

}
