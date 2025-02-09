package com.fosscut.util.load;

import java.io.IOException;

import com.fosscut.exception.OrderFileDoesNotExistException;
import com.fosscut.exception.OrderFileIsADirectoryException;
import com.fosscut.exception.RedisOrderPathException;

public abstract interface Loader {
    public abstract void validate(String orderPath)
        throws
            OrderFileIsADirectoryException,
            OrderFileDoesNotExistException,
            RedisOrderPathException;
    public abstract String load(String orderPath) throws IOException;
}
