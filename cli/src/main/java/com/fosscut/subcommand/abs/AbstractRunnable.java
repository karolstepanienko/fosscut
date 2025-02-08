package com.fosscut.subcommand.abs;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.exception.FosscutException;

public abstract class AbstractRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRunnable.class);

    @Override
    public void run() {
        try {
            runWithExceptions();
        } catch (FosscutException | IOException e) {
            logger.error(e.getMessage());
            System.exit(1);
        }
    }

    protected abstract void runWithExceptions()
        throws FosscutException, IOException;

}
