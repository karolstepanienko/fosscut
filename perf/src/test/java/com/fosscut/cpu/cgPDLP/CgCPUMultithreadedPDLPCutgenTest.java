package com.fosscut.cpu.cgPDLP;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

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

// NOT USED in fosscut-doc:
// since time results graphs is VERY similar (almost the same shape)
// like in optimalgen + cgPDLPMultithreaded tests
// It does not make sense to duplicate them in the documentation
@Execution(ExecutionMode.CONCURRENT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CgCPUMultithreadedPDLPCutgenTest extends AbstractTest {

    private static String testName = "cgCPUMultithreadedPDLPCutgen";
    private static String orderCommand = "cutgen -iu 1000 -il 100 -it 5 -ol 0.4 -ou 0.8 -oc 1000 -ot 40 --timeout-amount 10 --timeout-unit SECONDS";
    private static String planCommand = "cg --linear-solver PDLP --integer-solver SCIP --timeout-amount 5 --timeout-unit MINUTES -in 1";
    private static String memory = "5Gi";

    // 100 orders
    // (key, seed, /**/ key, seed)
    private static LinkedHashMap<Integer, Integer> seeds = LinkedHashMap_of(
    1001, 2,   /**/ 1002, 3,   /**/ 1003, 11,  /**/ 1004, 30,
        1005, 32,  /**/ 1006, 649, /**/ 1007, 38,  /**/ 1008, 40,
        1009, 42,  /**/ 1010, 49,  /**/ 1011, 50,  /**/ 1012, 57,
        1013, 69,  /**/ 1014, 73,  /**/ 1015, 77,  /**/ 1016, 81,
        1017, 83,  /**/ 1018, 657, /**/ 1019, 92,  /**/ 1020, 103,
        1021, 106, /**/ 1022, 123, /**/ 1023, 127, /**/ 1024, 134,
        1025, 141, /**/ 1026, 151, /**/ 1027, 154, /**/ 1028, 164,
        1029, 165, /**/ 1030, 178, /**/ 1031, 188, /**/ 1032, 189,
        1033, 202, /**/ 1034, 209, /**/ 1035, 212, /**/ 1036, 223,
        1037, 226, /**/ 1038, 228, /**/ 1039, 244, /**/ 1040, 246,
        1041, 248, /**/ 1042, 250, /**/ 1043, 271, /**/ 1044, 661,
        1045, 278, /**/ 1046, 281, /**/ 1047, 303, /**/ 1048, 312,
        1049, 315, /**/ 1050, 319, /**/ 1051, 328, /**/ 1052, 330,
        1053, 343, /**/ 1054, 344, /**/ 1055, 354, /**/ 1056, 357,
        1057, 358, /**/ 1058, 365, /**/ 1059, 371, /**/ 1060, 372,
        1061, 373, /**/ 1062, 650, /**/ 1063, 381, /**/ 1064, 390,
        1065, 396, /**/ 1066, 409, /**/ 1067, 411, /**/ 1068, 432,
        1069, 446, /**/ 1070, 455, /**/ 1071, 469, /**/ 1072, 473,
        1073, 474, /**/ 1074, 488, /**/ 1075, 489, /**/ 1076, 496,
        1077, 504, /**/ 1078, 508, /**/ 1079, 516, /**/ 1080, 520,
        1081, 522, /**/ 1082, 526, /**/ 1083, 534, /**/ 1084, 540,
        1085, 546, /**/ 1086, 547, /**/ 1087, 550, /**/ 1088, 565,
        1089, 568, /**/ 1090, 572, /**/ 1091, 574, /**/ 1092, 584,
        1093, 591, /**/ 1094, 595, /**/ 1095, 613, /**/ 1096, 655,
        1097, 629, /**/ 1098, 633, /**/ 1099, 635, /**/ 1100, 648
    );

    /***************************** Results Report *****************************/

    @Test @Order(2) public void cgCPUMultithreadedPDLPResultsReport() {
        ResultsReport report = new ResultsReport(testName,
            new ArrayList<>(), seeds);
        report.generateReport();
    }

    @Test @Order(2) public void cgCPUMultithreadedPDLPPlot() throws IOException {
        PlotData plotData = new PlotData(testName);

        new XYPlot(testName + "Time.tex",
            plotData.getXAxisLabels(),
            plotData.getAverageElapsedTimeSeconds(),
            PerformanceDefaults.GRAPH_X_LABEL_CPU,
            PerformanceDefaults.GRAPH_Y_LABEL_CPU_TIME,
            null, null, null, "100"
        ).generatePlot();

        // not used in fosscut-doc
        // new XYPlot(testName + "TotalNeededInputLength.tex",
        //     plotData.getXAxisLabels(),
        //     plotData.getAverageTotalNeededInputLength(),
        //     PerformanceDefaults.GRAPH_X_LABEL_CPU,
        //     "Średnia całkowita długość zamówienia"
        // ).generatePlot();

        // no waste plot because cutgen creates orders without known optimal solution
    }

    /********************************* Tests **********************************/

    @Test @Order(1) public void cgCPUMultithreadedPDLPx05() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, "x0.5", orderCommand,
            planCommand + " -ln " + "1", "0.5", memory, false);
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx1() throws InterruptedException {
        String numThreads = "1";
        CloudCommand cmd = new CloudCommand(testName, "x1", orderCommand,
            planCommand + " -ln " + numThreads, numThreads, memory, false);
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx2() throws InterruptedException {
        String numThreads = "2";
        CloudCommand cmd = new CloudCommand(testName, "x2", orderCommand,
            planCommand + " -ln " + numThreads, numThreads, memory, false);
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx3() throws InterruptedException {
        String numThreads = "3";
        CloudCommand cmd = new CloudCommand(testName, "x3", orderCommand,
            planCommand + " -ln " + numThreads, numThreads, memory, false);
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx4() throws InterruptedException {
        String numThreads = "4";
        CloudCommand cmd = new CloudCommand(testName, "x4", orderCommand,
            planCommand + " -ln " + numThreads, numThreads, memory, false);
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx5() throws InterruptedException {
        String numThreads = "5";
        CloudCommand cmd = new CloudCommand(testName, "x5", orderCommand,
            planCommand + " -ln " + numThreads, numThreads, memory, false);
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx6() throws InterruptedException {
        String numThreads = "6";
        CloudCommand cmd = new CloudCommand(testName, "x6", orderCommand,
            planCommand + " -ln " + numThreads, numThreads, memory, false);
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx7() throws InterruptedException {
        String numThreads = "7";
        CloudCommand cmd = new CloudCommand(testName, "x7", orderCommand,
            planCommand + " -ln " + numThreads, numThreads, memory, false);
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void cgCPUMultithreadedSCIPx8() throws InterruptedException {
        String numThreads = "8";
        CloudCommand cmd = new CloudCommand(testName, "x8", orderCommand,
            planCommand + " -ln " + numThreads, numThreads, memory, false);
        assertTrue(cmd.run(seeds));
    }

}
