package com.fosscut.cpu.cgSCIP;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.fosscut.AbstractTest;
import com.fosscut.plot.PlotData;
import com.fosscut.plot.XYPlot;
import com.fosscut.utils.CloudCommand;
import com.fosscut.utils.PerformanceDefaults;
import com.fosscut.utils.ResultsReport;

// DONE retest with collecting memory usage data
@Execution(ExecutionMode.CONCURRENT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CgCPUMultithreadedSCIPTest extends AbstractTest {

    private static String testName = "cgCPUMultithreadedSCIP";
    private static String orderCommand = "optimalgen -iu 1000 -il 100 -it 5 -ol 0.4 -ou 0.8 -oc 1000 -ot 30 --timeout-amount 10 --timeout-unit SECONDS";
    private static String planCommand = "cg --linear-solver GLOP --integer-solver SCIP -ln 1 --timeout-amount 2 --timeout-unit MINUTES";
    private static String memory = "10Gi";
    // five orders
    private static LinkedList<Integer> seeds = LinkedList_of(5, 7, 9, 11, 19);
    // each order is ran this amount of times
    private static int N_RUNS_INIT = 1000; // larger than range to accommodate for future increases
    private static int N_RUNS_WITH_IDENTICAL_SEED_START = 1;
    private static int N_RUNS_WITH_IDENTICAL_SEED_END = 100;

    /***************************** Results Report *****************************/

    @Test @Order(2) public void cgCPUMultithreadedSCIPResultsReport() {
        ResultsReport report = new ResultsReport(testName,
            new ArrayList<>(List.of("0.5", "1")),
            seeds, N_RUNS_INIT,
            N_RUNS_WITH_IDENTICAL_SEED_START,
            N_RUNS_WITH_IDENTICAL_SEED_END
        );
        report.generateReport();
    }

    @Test @Order(2) public void cgCPUMultithreadedSCIPTimePlot() throws IOException {
        PlotData plotData = new PlotData(testName, new LinkedList<String>() {{ add("1"); }});

        new XYPlot(testName + "Time.tex",
            plotData.getXAxisLabelsList(),
            plotData.getAverageElapsedTimeSeconds(),
            PerformanceDefaults.GRAPH_X_LABEL_CPU,
            PerformanceDefaults.GRAPH_Y_LABEL_CPU_TIME,
            "5cm", "1", null, "4", "8"
        ).generatePlot();
    }

    @Test @Order(2) public void cgCPUMultithreadedSCIPWastePercentagePlot() throws IOException {
        PlotData plotData = new PlotData(testName, new LinkedList<String>() {{ add("1"); }});

        new XYPlot(testName + "WastePercentage.tex",
            plotData.getXAxisLabelsList(),
            plotData.getAveragePercentageTrueWasteAboveOptimal(),
            PerformanceDefaults.GRAPH_X_LABEL_CPU,
            PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
            "5cm", "1", null, null, "0.008"
        ).generatePlot();
    }

    @Test @Order(2) public void cgCPUMultithreadedSCIPMemoryUsagePlot() throws IOException {
        PlotData plotData = new PlotData(testName, new LinkedList<String>() {{ add("1"); }});

        new XYPlot(testName + "MemoryUsagePeak.tex",
            plotData.getXAxisLabelsList(),
            plotData.getAverageMemoryUsagePeakGibiBytes(),
            PerformanceDefaults.GRAPH_X_LABEL_CPU,
            PerformanceDefaults.GRAPH_Y_LABEL_MEMORY_USAGE_GIBI_BYTES,
            "5cm", "1", null, "0", "3"
        ).generatePlot();
    }

    /********************************* Tests **********************************/

    @Test @Order(1) public void cgCPUMultithreadedSCIPx05() throws InterruptedException {
        // SCIP in one thread is deterministic so running only one run
        CloudCommand cmd = new CloudCommand(testName, "x0.5",
            orderCommand, planCommand + " -in " + "1", "0.5", memory,
            false
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx1() throws InterruptedException {
        String numThreads = "1";
        // SCIP in one thread is deterministic so running only one run
        CloudCommand cmd = new CloudCommand(testName, "x1",
            orderCommand, planCommand + " -in " + numThreads, numThreads, memory,
            false
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx2() throws InterruptedException {
        String numThreads = "2";
        CloudCommand cmd = new CloudCommand(testName, "x2",
            orderCommand, planCommand + " -in " + numThreads, numThreads, memory,
            false
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx3() throws InterruptedException {
        String numThreads = "3";
        CloudCommand cmd = new CloudCommand(testName, "x3",
            orderCommand, planCommand + " -in " + numThreads, numThreads, memory,
            false
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx4() throws InterruptedException {
        String numThreads = "4";
        CloudCommand cmd = new CloudCommand(testName, "x4",
            orderCommand, planCommand + " -in " + numThreads, numThreads, memory,
            false
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx5() throws InterruptedException {
        String numThreads = "5";
        CloudCommand cmd = new CloudCommand(testName, "x5",
            orderCommand, planCommand + " -in " + numThreads, numThreads, "16Gi",
            false
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx6() throws InterruptedException {
        String numThreads = "6";
        CloudCommand cmd = new CloudCommand(testName, "x6",
            orderCommand, planCommand + " -in " + numThreads, numThreads, "16Gi",
            false
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx7() throws InterruptedException {
        String numThreads = "7";
        CloudCommand cmd = new CloudCommand(testName, "x7",
            orderCommand, planCommand + " -in " + numThreads, numThreads, "20Gi",
            false
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx8() throws InterruptedException {
        String numThreads = "8";
        CloudCommand cmd = new CloudCommand(testName, "x8",
            orderCommand, planCommand + " -in " + numThreads, numThreads, "20Gi",
            false
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

}
