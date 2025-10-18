package com.fosscut.cg;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fosscut.utils.CloudCommand;

public class CgCPUMultithreadedSCIPTest {

    private static String folderName = "CgCPUMultithreadedSCIPTest";
    private static String orderCommand = "optimalgen -iu 1000 -il 100 -it 5 -ol 0.4 -ou 0.8 -oc 1000 -ot 30";
    private static String planCommand = "cg --linear-solver GLOP --integer-solver SCIP -ln 1 ";
    private static String memory = "5Gi";
    private static int SEED = 5;
    private static int N_RUNS_WITH_IDENTICAL_SEED = 10;

    @Test public void cgCPU1MultithreadedSCIP() throws InterruptedException {
        String numThreads = "1";
        // SCIP in one thread is deterministic so running only one run
        new CloudCommand(folderName, "cgCPU1MultithreadedSCIP",
            orderCommand, planCommand + "-in " + numThreads, numThreads, memory,
            false
        ).run(Map.of(0, SEED));
    }

    @Test public void cgCPU2MultithreadedSCIP() throws InterruptedException {
        String numThreads = "2";
        new CloudCommand(folderName, "cgCPU2MultithreadedSCIP",
            orderCommand, planCommand + "-in " + numThreads, numThreads, memory,
            false
        ).run(Map.of(0, SEED), N_RUNS_WITH_IDENTICAL_SEED);
    }

    @Test public void cgCPU3MultithreadedSCIP() throws InterruptedException {
        String numThreads = "3";
        new CloudCommand(folderName, "cgCPU3MultithreadedSCIP",
            orderCommand, planCommand + "-in " + numThreads, numThreads, memory,
            false
        ).run(Map.of(0, SEED), N_RUNS_WITH_IDENTICAL_SEED);
    }

    @Test public void cgCPU4MultithreadedSCIP() throws InterruptedException {
        String numThreads = "4";
        new CloudCommand(folderName, "cgCPU4MultithreadedSCIP",
            orderCommand, planCommand + "-in " + numThreads, numThreads, memory,
            false
        ).run(Map.of(0, SEED), N_RUNS_WITH_IDENTICAL_SEED);
    }

    @Test public void cgCPU5MultithreadedSCIP() throws InterruptedException {
        String numThreads = "5";
        new CloudCommand(folderName, "cgCPU5MultithreadedSCIP",
            orderCommand, planCommand + "-in " + numThreads, numThreads, memory,
            false
        ).run(Map.of(0, SEED), N_RUNS_WITH_IDENTICAL_SEED);
    }

    @Test public void cgCPU6MultithreadedSCIP() throws InterruptedException {
        String numThreads = "6";
        new CloudCommand(folderName, "cgCPU6MultithreadedSCIP",
            orderCommand, planCommand + "-in " + numThreads, numThreads, memory,
            false
        ).run(Map.of(0, SEED), N_RUNS_WITH_IDENTICAL_SEED);
    }

    @Test public void cgCPU7MultithreadedSCIP() throws InterruptedException {
        String numThreads = "7";
        new CloudCommand(folderName, "cgCPU7MultithreadedSCIP",
            orderCommand, planCommand + "-in " + numThreads, numThreads, memory,
            false
        ).run(Map.of(0, SEED), N_RUNS_WITH_IDENTICAL_SEED);
    }

    @Test public void cgCPU8MultithreadedSCIP() throws InterruptedException {
        String numThreads = "8";
        new CloudCommand(folderName, "cgCPU8MultithreadedSCIP",
            orderCommand, planCommand + "-in " + numThreads, numThreads, memory,
            false
        ).run(Map.of(0, SEED), N_RUNS_WITH_IDENTICAL_SEED);
    }

}
