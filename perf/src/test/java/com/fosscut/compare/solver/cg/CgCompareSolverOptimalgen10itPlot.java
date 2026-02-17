package com.fosscut.compare.solver.cg;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import com.fosscut.AbstractTest;
import com.fosscut.plot.Axis;
import com.fosscut.plot.PlotData;
import com.fosscut.plot.XYPlot;
import com.fosscut.utils.PerformanceDefaults;

public class CgCompareSolverOptimalgen10itPlot extends AbstractTest {

    protected static String orderCommand = "optimalgen -iu 1000 -il 500 -it 10 -ol 0.4 -ou 0.8 -oc 10000 --timeout-amount 10 --timeout-unit SECONDS";

    // each order is ran one time since solvers running in single-threaded mode
    // always produce the same result for identical seed
    // 50 orders since for 10 graph was quite jittery

    // PDLP-SCIP x50 had timeouts for seeds:
    // 30, 41, 51
    // PDLP-SCIP x50 had OOMs for seeds:
    protected static LinkedList<Integer> x50seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 33, 31, 32, // 10 seeds
        34, 35, 36, 37, 38, 39, 40, 54, 42, 43, // 10 seeds
        44, 45, 46, 47, 48, 49, 50, 55, 52, 53  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x50seedsLinkedHashMap
        = LinkedHashMapFromList_of(x50seedsLinkedList);


    // PDLP-SCIP x60 had timeouts for seeds:
    // 19, 25, 37, 40
    // PDLP-SCIP x60 had OOMs for seeds:
    protected static LinkedList<Integer> x60seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 33, 20, 22, // 10 seeds
        23, 24, 34, 26, 27, 28, 29, 30, 31, 32, // 10 seeds
        35, 36, 55, 38, 39, 58, 41, 42, 43, 44, // 10 seeds
        45, 46, 47, 48, 49, 50, 51, 52, 53, 54  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x60seedsLinkedHashMap
        = LinkedHashMapFromList_of(x60seedsLinkedList);


    // PDLP-SCIP x70 had timeouts for seeds:
    // 25, 38, 54
    // PDLP-SCIP x70 had OOMs for seeds:
    protected static LinkedList<Integer> x70seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 33, 26, 27, 28, 29, 30, 31, 32, // 10 seeds
        35, 36, 37, 55, 39, 40, 41, 42, 43, 44, // 10 seeds
        45, 46, 47, 48, 49, 50, 51, 52, 53, 57  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x70seedsLinkedHashMap
        = LinkedHashMapFromList_of(x70seedsLinkedList);


    // PDLP-SCIP x80 had timeouts for seeds:
    // 5, 15, 44, 51
    // PDLP-SCIP x80 had OOMs for seeds:
    protected static LinkedList<Integer> x80seedsLinkedList = LinkedList_of(
        1, 2, 4, 33, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 34, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32, // 10 seeds
        35, 36, 37, 38, 39, 40, 41, 42, 43, 55, // 10 seeds
        45, 46, 47, 48, 49, 50, 57, 52, 53, 54  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x80seedsLinkedHashMap
        = LinkedHashMapFromList_of(x80seedsLinkedList);


    // PDLP-SCIP x90 had timeouts for seeds:
    // 11, 27
    // PDLP-SCIP x90 had OOMs for seeds:
    // 51
    protected static LinkedList<Integer> x90seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 34, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 33, 28, 29, 30, 31, 32, // 10 seeds
        35, 36, 37, 38, 39, 40, 41, 42, 43, 44, // 10 seeds
        45, 46, 47, 48, 49, 50, 55, 52, 53, 54  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x90seedsLinkedHashMap
        = LinkedHashMapFromList_of(x90seedsLinkedList);


    // PDLP-SCIP x100 had timeouts for seeds:
    // 7, 8, 12, 30, 48, 55
    // PDLP-SCIP x100 had OOMs for seeds:
    // 17, 29, 51, 54
    protected static LinkedList<Integer> x100seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 35, 33, 9, 10, 11, // 10 seeds
        36, 13, 14, 15, 16, 37, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 38, 34, 31, 32, // 10 seeds
        39, 40, 41, 42, 43, 44, 45, 46, 47, 59, // 10 seeds
        49, 50, 60, 52, 53, 61, 62, 63, 57, 58  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x100seedsLinkedHashMap
        = LinkedHashMapFromList_of(x100seedsLinkedList);


    // PDLP-SCIP x110 had timeouts for seeds:
    // 11, 14, 17, 36, 51
    // PDLP-SCIP x110 had OOMs for seeds:
    // PDLP-SCIP x110 returned UNFEASIBLE for seeds:
    // 13
    protected static LinkedList<Integer> x110seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 33, // 10 seeds
        12, 37, 34, 15, 16, 35, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32, // 10 seeds
        38, 39, 40, 41, 42, 43, 44, 45, 46, 47, // 10 seeds
        48, 49, 50, 58, 52, 53, 54, 55, 59, 57  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x110seedsLinkedHashMap
        = LinkedHashMapFromList_of(x110seedsLinkedList);


    // PDLP-SCIP x120 had timeouts for seeds:
    // 5, 11, 12, 14, 35, 28, 31, 41, 44, 45, 51, 59, 68, 71
    // PDLP-SCIP x120 had OOMs for seeds:
    // 17, 20, 30, 36, 29, 54, 60
    protected static LinkedList<Integer> x120seedsLinkedList = LinkedList_of(
        1, 2, 4, 33, 6, 7, 8, 9, 10, 34, // 10 seeds
        40, 13, 47, 15, 16, 37, 18, 19, 38, 22, // 10 seeds
        23, 24, 25, 26, 27, 42, 43, 39, 46, 32, // 10 seeds
        48, 49, 50, 73, 52, 53, 69, 55, 70, 57, // 10 seeds
        58, 74, 72, 61, 62, 63, 64, 65, 66, 67  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x120seedsLinkedHashMap
        = LinkedHashMapFromList_of(x120seedsLinkedList);


    // PDLP-SCIP x130 had timeouts for seeds:
    // 9, 10, 11, 12, 13, 14, 20, 28, 33, 43, 51, 53, 64, 71, 72, 74, 78, 79
    // PDLP-SCIP x130 had OOMs for seeds:
    // 6, 17, 25, 27, 29, 36, 54
    protected static LinkedList<Integer> x130seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 46, 7, 8, 34, 35, 48, // 10 seeds
        37, 38, 39, 15, 16, 40, 18, 19, 41, 22, // 10 seeds
        23, 24, 42, 26, 47, 44, 45, 30, 31, 32, // 10 seeds
        49, 50, 69, 52, 70, 81, 55, 75, 57, 58, // 10 seeds
        59, 60, 61, 62, 63, 73, 65, 66, 67, 68  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x130seedsLinkedHashMap
        = LinkedHashMapFromList_of(x130seedsLinkedList);


    // PDLP-SCIP x140 had timeouts for seeds:
    // 6, 8, 10, 11, 16, 20, 25, 38, 48, 56, 57, 58, 62, 66, 83
    // PDLP-SCIP x140 had OOMs for seeds:
    // 7, 14, 17, 29, 30, 51, 54, 59, 61, 70, 78, 84
    // UNFEASIBLE for seeds: 63
    protected static LinkedList<Integer> x140seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 33, 34, 35, 9, 47, 37, // 10 seeds
        12, 13, 38, 15, 39, 86, 18, 19, 40, 22, // 10 seeds
        23, 24, 41, 26, 27, 42, 43, 46, 31, 32, // 10 seeds
        68, 49, 50, 69, 52, 79, 71, 55, 72, 73, // 10 seeds
        74, 75, 60, 76, 82, 81, 64, 65, 85, 67  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x140seedsLinkedHashMap
        = LinkedHashMapFromList_of(x140seedsLinkedList);


    // PDLP-SCIP x150 had timeouts for seeds:
    // 6, 8, 10, 12, 14, 17, 19, 20, 27, 30, 7, 11, 36, 25, 56, 59, 64, 71, 76, 78, 84
    // PDLP-SCIP x150 had OOMs for seeds:
    // 15, 29, 44, 48, 54, 58, 61, 83
    protected static LinkedList<Integer> x150seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 33, 49, 34, 9, 35, 55, // 10 seeds
        45, 13, 37, 46, 16, 38, 18, 39, 40, 22, // 10 seeds
        23, 24, 47, 26, 41, 28, 53, 42, 31, 32, // 10 seeds
        86, 57, 82, 87, 60, 81, 62, 63, 85, 65, // 10 seeds
        66, 67, 68, 69, 70, 79, 72, 73, 74, 75  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x150seedsLinkedHashMap
        = LinkedHashMapFromList_of(x150seedsLinkedList);

    // PDLP-SCIP x160 had timeouts for seeds:
    // 7, 23, 27, 44, 59
    // could not generate an order for seeds:
    // 56
    // GLOP-SCIP UNFEASIBLE for seeds:
    // 10
    protected static LinkedList<Integer> x160seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 54, 8, 9, 60, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        55, 24, 25, 26, 58, 28, 29, 30, 31, 32, // 10 seeds
        33, 34, 35, 36, 37, 38, 39, 40, 41, 42, // 10 seeds
        43, 57, 45, 46, 48, 49, 50, 51, 52, 53  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x160seedsLinkedHashMap
        = LinkedHashMapFromList_of(x160seedsLinkedList);

    // PDLP-SCIP x170 had timeouts for seeds:
    // 7, 12, 17, 37, 41, 54, 59, 61
    // could not generate an order for seeds:
    // 56
    // PDLP-SCIP UNFEASIBLE for seeds:
    // 42
    // GLOP-SCIP UNFEASIBLE for seeds:
    // 8
    protected static LinkedList<Integer> x170seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 60, 64, 9, 10, 11, // 10 seeds
        55, 13, 14, 15, 16, 63, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32, // 10 seeds
        33, 34, 35, 36, 57, 38, 39, 40, 58, 62, // 10 seeds
        43, 44, 45, 46, 48, 49, 50, 51, 52, 53  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x170seedsLinkedHashMap
        = LinkedHashMapFromList_of(x170seedsLinkedList);

    // PDLP-SCIP x180 had timeouts for seeds:
    // 8, 13, 20, 35, 38, 44, 51, 58
    // could not generate an order for seeds:
    // 56
    protected static LinkedList<Integer> x180seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 54, 9, 10, 11, // 10 seeds
        12, 55, 14, 15, 16, 17, 18, 19, 61, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32, // 10 seeds
        33, 34, 57, 36, 37, 62, 39, 40, 41, 42, // 10 seeds
        43, 59, 45, 46, 48, 49, 50, 60, 52, 53  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x180seedsLinkedHashMap
        = LinkedHashMapFromList_of(x180seedsLinkedList);

    // PDLP-SCIP x190 had timeouts for seeds:
    // 11, 20, 32, 38, 44, 48
    // GLOP-SCIP x200 had timeouts for seeds:
    // 53
    // could not generate an order for seeds:
    // 56
    protected static LinkedList<Integer> x190seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 54, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 55, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 60, // 10 seeds
        33, 34, 35, 36, 37, 57, 39, 40, 41, 42, // 10 seeds
        43, 58, 45, 46, 59, 49, 50, 51, 52, 61  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x190seedsLinkedHashMap
        = LinkedHashMapFromList_of(x190seedsLinkedList);

    // PDLP-SCIP x200 had timeouts for seeds:
    // 1, 6, 11, 44, 49, 52, 59
    // could not generate an order for seeds:
    // 56
    protected static LinkedList<Integer> x200seedsLinkedList = LinkedList_of(
        54, 2, 4, 5, 55, 7, 8, 9, 10, 60, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32, // 10 seeds
        33, 34, 35, 36, 37, 38, 39, 40, 41, 42, // 10 seeds
        43, 57, 45, 46, 48, 58, 50, 51, 61, 53  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x200seedsLinkedHashMap
        = LinkedHashMapFromList_of(x200seedsLinkedList);

    protected static LinkedHashMap<String, LinkedHashMap<Integer, Integer>> getXAxisLabelSeedsMap() {
        LinkedHashMap<String, LinkedHashMap<Integer, Integer>> xAxisLabelSeedsMap = new LinkedHashMap<>();
        xAxisLabelSeedsMap.put("50", detectDuplicates(x50seedsLinkedHashMap));
        xAxisLabelSeedsMap.put("60", detectDuplicates(x60seedsLinkedHashMap));
        xAxisLabelSeedsMap.put("70", detectDuplicates(x70seedsLinkedHashMap));
        xAxisLabelSeedsMap.put("80", detectDuplicates(x80seedsLinkedHashMap));
        xAxisLabelSeedsMap.put("90", detectDuplicates(x90seedsLinkedHashMap));
        xAxisLabelSeedsMap.put("100", detectDuplicates(x100seedsLinkedHashMap));
        xAxisLabelSeedsMap.put("110", detectDuplicates(x110seedsLinkedHashMap));
        xAxisLabelSeedsMap.put("120", detectDuplicates(x120seedsLinkedHashMap));
        xAxisLabelSeedsMap.put("130", detectDuplicates(x130seedsLinkedHashMap));
        xAxisLabelSeedsMap.put("140", detectDuplicates(x140seedsLinkedHashMap));
        xAxisLabelSeedsMap.put("150", detectDuplicates(x150seedsLinkedHashMap));
        xAxisLabelSeedsMap.put("160", detectDuplicates(x160seedsLinkedHashMap));
        xAxisLabelSeedsMap.put("170", detectDuplicates(x170seedsLinkedHashMap));
        xAxisLabelSeedsMap.put("180", detectDuplicates(x180seedsLinkedHashMap));
        xAxisLabelSeedsMap.put("190", detectDuplicates(x190seedsLinkedHashMap));
        xAxisLabelSeedsMap.put("200", detectDuplicates(x200seedsLinkedHashMap));
        return xAxisLabelSeedsMap;
    }


    @Test public void cgCompareLinSolverCBCOptimalgen10itPlot() throws IOException {
        String testName = "cgCompareLinSolverCBCOptimalgen10itPlot";
        PlotData clpPlotData = new PlotData("cgCompareSolverLinCLPIntCBCOptimalgen10itTest");
        PlotData glopPlotData = new PlotData("cgCompareSolverLinGLOPIntCBCOptimalgen10itTest");
        PlotData pdlpPlotData = new PlotData("cgCompareSolverLinPDLPIntCBCOptimalgen10itTest");

        new XYPlot(testName + "Time.tex",
                getCombinedXAxisLabelsList(
                    clpPlotData.getXAxisLabels(),
                    glopPlotData.getXAxisLabels(),
                    pdlpPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    clpPlotData.getAverageElapsedTimeSeconds(),
                    glopPlotData.getAverageElapsedTimeSeconds(),
                    pdlpPlotData.getAverageElapsedTimeSeconds()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_TIME,
                "9cm", null, null, "0", "140",
                new LinkedList<String>() {{
                    add("CLP");
                    add("GLOP");
                    add("PDLP");
                }},
                clpPlotData.getXAxisLabels()
        ).generatePlot();

        new XYPlot(testName + "MemoryUsagePeak.tex",
                getCombinedXAxisLabelsList(
                    clpPlotData.getXAxisLabels(),
                    glopPlotData.getXAxisLabels(),
                    pdlpPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    clpPlotData.getAverageMemoryUsagePeakMebiBytes(),
                    glopPlotData.getAverageMemoryUsagePeakMebiBytes(),
                    pdlpPlotData.getAverageMemoryUsagePeakMebiBytes()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_MEMORY_USAGE_MEBI_BYTES,
                "9cm", null, null, "50", "450",
                new LinkedList<String>() {{
                    add("CLP");
                    add("GLOP");
                    add("PDLP");
                }},
                clpPlotData.getXAxisLabels()
        ).generatePlot();

        new XYPlot(testName + "WastePercentage.tex",
                getCombinedXAxisLabelsList(
                    clpPlotData.getXAxisLabels(),
                    glopPlotData.getXAxisLabels(),
                    pdlpPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    clpPlotData.getAveragePercentageTrueWasteAboveOptimal(),
                    glopPlotData.getAveragePercentageTrueWasteAboveOptimal(),
                    pdlpPlotData.getAveragePercentageTrueWasteAboveOptimal()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
                "8.6cm", null, null, "0", "0.006",
                new LinkedList<String>() {{
                    add("CLP");
                    add("GLOP");
                    add("PDLP");
                }},
                clpPlotData.getXAxisLabels()
        ).generatePlot();
    }

    @Test public void cgCompareLinSolverSCIPOptimalgen10itPlot() throws IOException {
        String testName = "cgCompareLinSolverSCIPOptimalgen10itPlot";
        PlotData clpPlotData = new PlotData("cgCompareSolverLinCLPIntSCIPOptimalgen10itTest");
        PlotData glopPlotData = new PlotData("cgCompareSolverLinGLOPIntSCIPOptimalgen10itTest");
        PlotData pdlpPlotData = new PlotData("cgCompareSolverLinPDLPIntSCIPOptimalgen10itTest");

        new XYPlot(testName + "Time.tex",
                getCombinedXAxisLabelsList(
                    clpPlotData.getXAxisLabels(),
                    glopPlotData.getXAxisLabels(),
                    pdlpPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    clpPlotData.getAverageElapsedTimeSeconds(),
                    glopPlotData.getAverageElapsedTimeSeconds(),
                    pdlpPlotData.getAverageElapsedTimeSeconds()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_TIME,
                "8.6cm", null, null, "0", "60",
                new LinkedList<String>() {{
                    add("CLP");
                    add("GLOP");
                    add("PDLP");
                }},
                clpPlotData.getXAxisLabels()
        ).generatePlot();

        new XYPlot(testName + "MemoryUsagePeak.tex",
                getCombinedXAxisLabelsList(
                    clpPlotData.getXAxisLabels(),
                    glopPlotData.getXAxisLabels(),
                    pdlpPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    clpPlotData.getAverageMemoryUsagePeakGibiBytes(),
                    glopPlotData.getAverageMemoryUsagePeakGibiBytes(),
                    pdlpPlotData.getAverageMemoryUsagePeakGibiBytes()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_MEMORY_USAGE_GIBI_BYTES,
                "9cm", null, null, null, "5",
                new LinkedList<String>() {{
                    add("CLP");
                    add("GLOP");
                    add("PDLP");
                }},
                clpPlotData.getXAxisLabels()
        ).generatePlot();

        new XYPlot(testName + "WastePercentage.tex",
                getCombinedXAxisLabelsList(
                    clpPlotData.getXAxisLabels(),
                    glopPlotData.getXAxisLabels(),
                    pdlpPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    clpPlotData.getAveragePercentageTrueWasteAboveOptimal(),
                    glopPlotData.getAveragePercentageTrueWasteAboveOptimal(),
                    pdlpPlotData.getAveragePercentageTrueWasteAboveOptimal()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
                "9cm", null, null, "0", "0.008",
                new LinkedList<String>() {{
                    add("CLP");
                    add("GLOP");
                    add("PDLP");
                }},
                clpPlotData.getXAxisLabels()
        ).generatePlot();
    }

    @Test public void cgCompareIntSolverOptimalgen10itPlot() throws IOException {
        String testName = "cgCompareIntSolverOptimalgen10itPlot";

        PlotData clpCbcPlotData = new PlotData("cgCompareSolverLinCLPIntCBCOptimalgen10itTest");
        PlotData glopCbcPlotData = new PlotData("cgCompareSolverLinGLOPIntCBCOptimalgen10itTest");
        PlotData pdlpCbcPlotData = new PlotData("cgCompareSolverLinPDLPIntCBCOptimalgen10itTest");

        PlotData clpScipPlotData = new PlotData("cgCompareSolverLinCLPIntSCIPOptimalgen10itTest");
        PlotData glopScipPlotData = new PlotData("cgCompareSolverLinGLOPIntSCIPOptimalgen10itTest");
        PlotData pdlpScipPlotData = new PlotData("cgCompareSolverLinPDLPIntSCIPOptimalgen10itTest");

        // Time plot
        LinkedList<Axis> timeAxesList = new LinkedList<>();
        // CLP - CBC vs CLP - SCIP
        timeAxesList.add(
            new Axis(
                getCombinedXAxisLabelsList(
                    clpCbcPlotData.getXAxisLabels(),
                    clpScipPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    clpCbcPlotData.getAverageElapsedTimeSeconds(),
                    clpScipPlotData.getAverageElapsedTimeSeconds()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_TIME,
                "5cm", null, null, "0", "60",
                new LinkedList<String>() {{
                    add("CLP - CBC");
                    add("CLP - SCIP");
                }},
                clpCbcPlotData.getXAxisLabels()
            )
        );
        // GLOP - CBC vs GLOP - SCIP
        timeAxesList.add(
            new Axis(
                getCombinedXAxisLabelsList(
                    glopCbcPlotData.getXAxisLabels(),
                    glopScipPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    glopCbcPlotData.getAverageElapsedTimeSeconds(),
                    glopScipPlotData.getAverageElapsedTimeSeconds()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_TIME,
                "5cm", null, null, "0", "60",
                new LinkedList<String>() {{
                    add("GLOP - CBC");
                    add("GLOP - SCIP");
                }},
                glopCbcPlotData.getXAxisLabels()
            )
        );
        // PDLP - CBC vs PDLP - SCIP
        timeAxesList.add(
            new Axis(
                getCombinedXAxisLabelsList(
                    pdlpCbcPlotData.getXAxisLabels(),
                    pdlpScipPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    pdlpCbcPlotData.getAverageElapsedTimeSeconds(),
                    pdlpScipPlotData.getAverageElapsedTimeSeconds()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_TIME,
                "5cm", null, null, "0", "150",
                new LinkedList<String>() {{
                    add("PDLP - CBC");
                    add("PDLP - SCIP");
                }},
                pdlpCbcPlotData.getXAxisLabels()
            )
        );
        new XYPlot(testName + "Time.tex", timeAxesList).generatePlot();

        // Memory usage plot
        LinkedList<Axis> memoryAxesList = new LinkedList<>();
        // CLP - CBC vs CLP - SCIP
        memoryAxesList.add(
            new Axis(
                getCombinedXAxisLabelsList(
                    clpCbcPlotData.getXAxisLabels(),
                    clpScipPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    clpCbcPlotData.getAverageMemoryUsagePeakGibiBytes(),
                    clpScipPlotData.getAverageMemoryUsagePeakGibiBytes()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_MEMORY_USAGE_GIBI_BYTES,
                "5cm", null, null, "0", "6",
                new LinkedList<String>() {{
                    add("CLP - CBC");
                    add("CLP - SCIP");
                }},
                clpCbcPlotData.getXAxisLabels()
            )
        );
        // GLOP - CBC vs GLOP - SCIP
        memoryAxesList.add(
            new Axis(
                getCombinedXAxisLabelsList(
                    glopCbcPlotData.getXAxisLabels(),
                    glopScipPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    glopCbcPlotData.getAverageMemoryUsagePeakGibiBytes(),
                    glopScipPlotData.getAverageMemoryUsagePeakGibiBytes()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_MEMORY_USAGE_GIBI_BYTES,
                "5cm", null, null, "0", "6",
                new LinkedList<String>() {{
                    add("GLOP - CBC");
                    add("GLOP - SCIP");
                }},
                glopCbcPlotData.getXAxisLabels()
            )
        );
        // PDLP - CBC vs PDLP - SCIP
        memoryAxesList.add(
            new Axis(
                getCombinedXAxisLabelsList(
                    pdlpCbcPlotData.getXAxisLabels(),
                    pdlpScipPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    pdlpCbcPlotData.getAverageMemoryUsagePeakGibiBytes(),
                    pdlpScipPlotData.getAverageMemoryUsagePeakGibiBytes()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_MEMORY_USAGE_GIBI_BYTES,
                "5cm", null, null, "0", "6",
                new LinkedList<String>() {{
                    add("PDLP - CBC");
                    add("PDLP - SCIP");
                }},
                pdlpCbcPlotData.getXAxisLabels()
            )
        );
        new XYPlot(testName + "MemoryUsagePeak.tex", memoryAxesList).generatePlot();

        // Waste plot
        LinkedList<Axis> wasteAxesList = new LinkedList<>();
        // CLP - CBC vs CLP - SCIP
        wasteAxesList.add(
            new Axis(
                getCombinedXAxisLabelsList(
                    clpCbcPlotData.getXAxisLabels(),
                    clpScipPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    clpCbcPlotData.getAveragePercentageTrueWasteAboveOptimal(),
                    clpScipPlotData.getAveragePercentageTrueWasteAboveOptimal()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
                "5cm", null, null, "0", "0.008",
                new LinkedList<String>() {{
                    add("CLP - CBC");
                    add("CLP - SCIP");
                }},
                clpCbcPlotData.getXAxisLabels()
            )
        );
        // GLOP - CBC vs GLOP - SCIP
        wasteAxesList.add(
            new Axis(
                getCombinedXAxisLabelsList(
                    glopCbcPlotData.getXAxisLabels(),
                    glopScipPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    glopCbcPlotData.getAveragePercentageTrueWasteAboveOptimal(),
                    glopScipPlotData.getAveragePercentageTrueWasteAboveOptimal()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
                "5cm", null, null, "0", "0.008",
                new LinkedList<String>() {{
                    add("GLOP - CBC");
                    add("GLOP - SCIP");
                }},
                glopCbcPlotData.getXAxisLabels()
            )
        );
        // PDLP - CBC vs PDLP - SCIP
        wasteAxesList.add(
            new Axis(
                getCombinedXAxisLabelsList(
                    pdlpCbcPlotData.getXAxisLabels(),
                    pdlpScipPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    pdlpCbcPlotData.getAveragePercentageTrueWasteAboveOptimal(),
                    pdlpScipPlotData.getAveragePercentageTrueWasteAboveOptimal()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
                "5cm", null, null, "0", "0.008",
                new LinkedList<String>() {{
                    add("PDLP - CBC");
                    add("PDLP - SCIP");
                }},
                pdlpCbcPlotData.getXAxisLabels()
            )
        );
        new XYPlot(testName + "WastePercentage.tex", wasteAxesList).generatePlot();
    }

}
