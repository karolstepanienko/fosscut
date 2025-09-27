package com.fosscut.util;

public class AlgTimer {

    private long startTime;
    private long elapsedTime;

    public AlgTimer() {}

    public long getElapsedTimeMillis() {
        return elapsedTime / 1_000_000;
    }

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        elapsedTime = System.nanoTime() - startTime;
    }

}
