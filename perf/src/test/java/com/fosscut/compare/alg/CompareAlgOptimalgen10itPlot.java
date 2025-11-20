package com.fosscut.compare.alg;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import com.fosscut.compare.solver.cg.CgCompareSolverOptimalgen10it2inPlot;
import com.fosscut.plot.PlotData;
import com.fosscut.plot.XYPlot;
import com.fosscut.utils.PerformanceDefaults;

public class CompareAlgOptimalgen10itPlot extends CgCompareSolverOptimalgen10it2inPlot {

    // each order is ran one time since solvers running in single-threaded mode
    // always produce the same result for identical seed
    // 50 orders since for 10 graph was quite jittery

    // x10 - x150 seeds are inherited from CgCompareSolverOptimalgen10it2inPlot

    protected static LinkedList<Integer> x160seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32, // 10 seeds
        33, 34, 35, 36, 37, 38, 39, 40, 41, 42, // 10 seeds
        43, 44, 45, 46, 47, 48, 49, 50, 51, 52  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x160seedsLinkedHashMap
        = LinkedHashMapFromList_of(x160seedsLinkedList);

    protected static LinkedList<Integer> x170seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32, // 10 seeds
        33, 34, 35, 36, 37, 38, 39, 40, 41, 42, // 10 seeds
        43, 44, 45, 46, 47, 48, 49, 50, 51, 52  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x170seedsLinkedHashMap
        = LinkedHashMapFromList_of(x170seedsLinkedList);

    protected static LinkedList<Integer> x180seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32, // 10 seeds
        33, 34, 35, 36, 37, 38, 39, 40, 41, 42, // 10 seeds
        43, 44, 45, 46, 47, 48, 49, 50, 51, 52  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x180seedsLinkedHashMap
        = LinkedHashMapFromList_of(x180seedsLinkedList);

    protected static LinkedList<Integer> x190seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32, // 10 seeds
        33, 34, 35, 36, 37, 38, 39, 40, 41, 42, // 10 seeds
        43, 44, 45, 46, 47, 48, 49, 50, 51, 52  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x190seedsLinkedHashMap
        = LinkedHashMapFromList_of(x190seedsLinkedList);

    protected static LinkedList<Integer> x200seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32, // 10 seeds
        33, 34, 35, 36, 37, 38, 39, 40, 41, 42, // 10 seeds
        43, 44, 45, 46, 47, 48, 49, 50, 51, 52  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x200seedsLinkedHashMap
        = LinkedHashMapFromList_of(x200seedsLinkedList);

    protected static LinkedHashMap<String, LinkedHashMap<Integer, Integer>> getXAxisLabelSeedsMap() {
        LinkedHashMap<String, LinkedHashMap<Integer, Integer>> xAxisLabelSeedsMap = new LinkedHashMap<>();
        xAxisLabelSeedsMap.put("10", detectDuplicates(x10seedsLinkedHashMap));
        xAxisLabelSeedsMap.put("20", detectDuplicates(x20seedsLinkedHashMap));
        xAxisLabelSeedsMap.put("30", detectDuplicates(x30seedsLinkedHashMap));
        xAxisLabelSeedsMap.put("40", detectDuplicates(x40seedsLinkedHashMap));
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

    @Test public void compareAlgOptimalgen10itPlot() throws IOException {
        String testName = "compareAlgOptimalgen10itPlot";
        PlotData ffdPlotData = new PlotData("ffdCompareAlgOptimalgen10itTest");
        PlotData greedyPlotData = new PlotData("greedyCompareAlgOptimalgen10itTest");
        PlotData cgPlotData = new PlotData("cgCompareAlgOptimalgen10itTest");

        // ffd solved all orders
        // greedy solved all orders until x130 where a lot of timeouts and OOMs started happening
        // cg solved all orders until x170 where a timeouts and OOMs started happening

        new XYPlot(testName + "Time.tex",
                getCombinedXAxisLabelsList(
                    ffdPlotData.getXAxisLabels(),
                    greedyPlotData.getXAxisLabels(),
                    cgPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    ffdPlotData.getAverageElapsedTimeSeconds(),
                    greedyPlotData.getAverageElapsedTimeSeconds(),
                    cgPlotData.getAverageElapsedTimeSeconds()
                ),
                "Liczba typów elementów wyjściowych",
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_TIME,
                null, null, "0", "50",
                new LinkedList<String>() {{
                    add("FFD");
                    add("Greedy");
                    add("CG");
                }},
                ffdPlotData.getXAxisLabels()
        ).generatePlot();

        new XYPlot(testName + "WastePercentage.tex",
                getCombinedXAxisLabelsList(
                    ffdPlotData.getXAxisLabels(),
                    greedyPlotData.getXAxisLabels(),
                    cgPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    // since FFD is only single-threaded, all tests were run
                    // with solvers using a single thread
                    ffdPlotData.getAveragePercentageTrueWasteAboveOptimal(),
                    greedyPlotData.getAveragePercentageTrueWasteAboveOptimal(),
                    cgPlotData.getAveragePercentageTrueWasteAboveOptimal()
                ),
                "Liczba typów elementów wyjściowych",
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
                null, null, "0", "1",
                new LinkedList<String>() {{
                    add("FFD");
                    add("Greedy");
                    add("CG");
                }},
                ffdPlotData.getXAxisLabels()
        ).generatePlot();
    }

}
