package com.fosscut.util.load;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fosscut.exception.OrderFileDoesNotExistException;
import com.fosscut.exception.OrderFileIsADirectoryException;

public class OrderFileLoader implements Loader {

    @Override
    public void validate(String orderPath)
        throws OrderFileIsADirectoryException, OrderFileDoesNotExistException
    {
        File orderFile = new File(orderPath);

        if (orderFile.isDirectory()) {
            throw new OrderFileIsADirectoryException();
        }

        if (!orderFile.exists()) {
            throw new OrderFileDoesNotExistException();
        }
    }

    @Override
    public String load(String orderPath) throws IOException {
        return Files.readString(Paths.get(orderPath));
    }

}
