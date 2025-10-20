package com.fosscut.cg;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.fosscut.plot.PlotData;
import com.fosscut.utils.CloudCommand;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CgCPUMultithreadedSCIPTest {

    private static String folderName = "CgCPUMultithreadedSCIPTest";
    private static String orderCommand = "optimalgen -iu 1000 -il 100 -it 5 -ol 0.4 -ou 0.8 -oc 1000 -ot 30";
    private static String planCommand = "cg --linear-solver GLOP --integer-solver SCIP -ln 1 ";
    private static String memory = "5Gi";
    private static int SEED = 5;
    private static int N_RUNS_WITH_IDENTICAL_SEED = 10;

    @Test @Order(1) public void cgCPUMultithreadedSCIPx05() throws InterruptedException {
        // SCIP in one thread is deterministic so running only one run
        new CloudCommand(folderName, "cgCPUMultithreadedSCIPx0.5",
            orderCommand, planCommand + "-in " + "1", "0.5", memory,
            false
        ).run(Map.of(0, SEED));
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx1() throws InterruptedException {
        String numThreads = "1";
        // SCIP in one thread is deterministic so running only one run
        new CloudCommand(folderName, "cgCPUMultithreadedSCIPx1",
            orderCommand, planCommand + "-in " + numThreads, numThreads, memory,
            false
        ).run(Map.of(0, SEED));
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx2() throws InterruptedException {
        String numThreads = "2";
        new CloudCommand(folderName, "cgCPUMultithreadedSCIPx2",
            orderCommand, planCommand + "-in " + numThreads, numThreads, memory,
            false
        ).run(Map.of(0, SEED), N_RUNS_WITH_IDENTICAL_SEED);
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx3() throws InterruptedException {
        String numThreads = "3";
        new CloudCommand(folderName, "cgCPUMultithreadedSCIPx3",
            orderCommand, planCommand + "-in " + numThreads, numThreads, memory,
            false
        ).run(Map.of(0, SEED), N_RUNS_WITH_IDENTICAL_SEED);
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx4() throws InterruptedException {
        String numThreads = "4";
        new CloudCommand(folderName, "cgCPUMultithreadedSCIPx4",
            orderCommand, planCommand + "-in " + numThreads, numThreads, memory,
            false
        ).run(Map.of(0, SEED), N_RUNS_WITH_IDENTICAL_SEED);
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx5() throws InterruptedException {
        String numThreads = "5";
        new CloudCommand(folderName, "cgCPUMultithreadedSCIPx5",
            orderCommand, planCommand + "-in " + numThreads, numThreads, memory,
            false
        ).run(Map.of(0, SEED), N_RUNS_WITH_IDENTICAL_SEED);
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx6() throws InterruptedException {
        String numThreads = "6";
        new CloudCommand(folderName, "cgCPUMultithreadedSCIPx6",
            orderCommand, planCommand + "-in " + numThreads, numThreads, memory,
            false
        ).run(Map.of(0, SEED), N_RUNS_WITH_IDENTICAL_SEED);
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx7() throws InterruptedException {
        String numThreads = "7";
        new CloudCommand(folderName, "cgCPUMultithreadedSCIPx7",
            orderCommand, planCommand + "-in " + numThreads, numThreads, memory,
            false
        ).run(Map.of(0, SEED), N_RUNS_WITH_IDENTICAL_SEED);
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx8() throws InterruptedException {
        String numThreads = "8";
        new CloudCommand(folderName, "cgCPUMultithreadedSCIPx8",
            orderCommand, planCommand + "-in " + numThreads, numThreads, memory,
            false
        ).run(Map.of(0, SEED), N_RUNS_WITH_IDENTICAL_SEED);
    }

    @Test @Order(2) public void cgCPUMultithreadedSCIPPlot() throws IOException {
        PlotData plotData = new PlotData(folderName);
        plotData.getAverageElapsedTimeMilliseconds();
    }

}
