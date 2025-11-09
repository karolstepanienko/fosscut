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

// PDLP is deterministic with multithreading
// so the best test would be to just run a set of seeds that produce an order
// that is not solved optimally by a single-threaded PDLP
@Execution(ExecutionMode.CONCURRENT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CgCPUMultithreadedPDLPTest extends AbstractTest {

    private static String testName = "cgCPUMultithreadedPDLP";
    private static String orderCommand = "optimalgen -iu 1000 -il 100 -it 5 -ol 0.4 -ou 0.8 -oc 1000 -ot 30 --timeout-amount 10 --timeout-unit SECONDS";
    private static String planCommand = "cg --linear-solver PDLP --integer-solver SCIP --timeout-amount 2 --timeout-unit MINUTES -in 1";
    private static String memory = "5Gi";

    // 150 orders
    // All seeds have been tested to work with PDLP multithreaded
    // (key, seed, /**/ key, seed)
    private static LinkedHashMap<Integer, Integer> seeds = LinkedHashMap_of(
        1001, 22,  /**/ 1002, 3,   /**/ 1003, 5,   /**/ 1004, 6,
        1005, 7,   /**/ 1006, 9,   /**/ 1007, 12,  /**/ 1008, 18,
        1009, 24,  /**/ 1010, 28,  /**/ 1011, 29,  /**/ 1012, 544,
        1013, 35,  /**/ 1014, 38,  /**/ 1015, 39,  /**/ 1016, 49,
        1017, 58,  /**/ 1018, 60,  /**/ 1019, 69,  /**/ 1020, 72,
        1021, 76,  /**/ 1022, 79,  /**/ 1023, 80,  /**/ 1024, 82,
        1025, 90,  /**/ 1026, 100, /**/ 1027, 102, /**/ 1028, 103,
        1029, 105, /**/ 1030, 106, /**/ 1031, 108, /**/ 1032, 111,
        1033, 115, /**/ 1034, 116, /**/ 1035, 119, /**/ 1036, 128,
        1037, 133, /**/ 1038, 136, /**/ 1039, 137, /**/ 1040, 142,
        1041, 550, /**/ 1042, 153, /**/ 1043, 154, /**/ 1044, 157,
        1045, 558, /**/ 1046, 170, /**/ 1047, 530, /**/ 1048, 172,
        1049, 175, /**/ 1050, 176, /**/ 1051, 180, /**/ 1052, 182,
        1053, 556, /**/ 1054, 185, /**/ 1055, 205, /**/ 1056, 529,
        1057, 218, /**/ 1058, 224, /**/ 1059, 230, /**/ 1060, 231,
        1061, 238, /**/ 1062, 240, /**/ 1063, 246, /**/ 1064, 256,
        1065, 257, /**/ 1066, 259, /**/ 1067, 261, /**/ 1068, 264,
        1069, 265, /**/ 1070, 278, /**/ 1071, 285, /**/ 1072, 288,
        1073, 537, /**/ 1074, 298, /**/ 1075, 299, /**/ 1076, 304,
        1077, 305, /**/ 1078, 515, /**/ 1079, 318, /**/ 1080, 319,
        1081, 323, /**/ 1082, 330, /**/ 1083, 332, /**/ 1084, 333,
        1085, 335, /**/ 1086, 338, /**/ 1087, 343, /**/ 1088, 347,
        1089, 557, /**/ 1090, 358, /**/ 1091, 514, /**/ 1092, 363,
        1093, 365, /**/ 1094, 372, /**/ 1095, 373, /**/ 1096, 379,
        1097, 386, /**/ 1098, 388, /**/ 1099, 391, /**/ 1100, 392,
        1101, 400, /**/ 1102, 401, /**/ 1103, 403, /**/ 1104, 405,
        1105, 373, /**/ 1106, 379, /**/ 1107, 386, /**/ 1108, 388,
        1109, 391, /**/ 1110, 392, /**/ 1111, 400, /**/ 1112, 401,
        1113, 403, /**/ 1114, 405, /**/ 1115, 406, /**/ 1116, 411,
        1117, 414, /**/ 1118, 415, /**/ 1119, 512, /**/ 1120, 417,
        1121, 418, /**/ 1122, 419, /**/ 1123, 420, /**/ 1124, 426,
        1125, 427, /**/ 1126, 431, /**/ 1127, 439, /**/ 1128, 446,
        1129, 449, /**/ 1130, 461, /**/ 1131, 467, /**/ 1132, 532,
        1133, 496, /**/ 1134, 502, /**/ 1135, 503, /**/ 1136, 508,
        1137, 509, /**/ 1138, 562, /**/ 1139, 563, /**/ 1140, 564,
        1141, 565, /**/ 1142, 569, /**/ 1143, 573, /**/ 1144, 574,
        1145, 579, /**/ 1146, 592, /**/ 1147, 593, /**/ 1148, 595,
        1149, 600, /**/ 1150, 607
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
            "1", null, "0", "50"
        ).generatePlot();

        new XYPlot(testName + "WastePercentage.tex",
            plotData.getXAxisLabels(),
            plotData.getAveragePercentageWasteAboveOptimal(),
            PerformanceDefaults.GRAPH_X_LABEL_CPU,
            PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
            "1", null, "0.008", "0.016"
        ).generatePlot();

        // not used in fosscut-doc
        // new XYPlot(testName + "TotalNeededInputLength.tex",
        //     plotData.getXAxisLabels(),
        //     plotData.getAverageTotalNeededInputLength(),
        //     PerformanceDefaults.GRAPH_X_LABEL_CPU,
        //     "Średnia całkowita długość zamówienia"
        // ).generatePlot();
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
