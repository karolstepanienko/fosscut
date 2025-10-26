package com.fosscut.subcommand.abs;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fosscut.shared.exception.FosscutException;

import picocli.CommandLine.Option;

public abstract class AbstractRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRunnable.class);

    protected File redisConnectionSecrets;

    @Option(names = { "--timeout-amount"},
        defaultValue = "5",
        description = "Amount of time after which command run"
        + " will be terminated."
        + " If not set, there is no timeout."
        + " Time unit is set by --timeout-unit."
        + " Default: ${DEFAULT-VALUE} minutes.")
    protected Long timeoutAmount;

    @Option(names = { "--timeout-unit"},
        defaultValue = "MINUTES",
        description = "Time unit for the timeout amount."
        + " One of: (${COMPLETION-CANDIDATES})."
        + " Default: ${DEFAULT-VALUE}.")
    protected TimeUnit timeoutUnit;

    @Override
    public void run() {
        try {
            runWithExceptions();
        } catch (FosscutException | IOException | TimeoutException e) {
            logger.error(e.getMessage());
            System.exit(1);
        }
    }

    protected abstract void runWithExceptions()
        throws FosscutException, IOException, TimeoutException;

}
