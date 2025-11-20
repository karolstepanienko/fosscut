package com.fosscut.compare.solver.cg;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import com.fosscut.plot.PlotData;
import com.fosscut.plot.XYPlot;
import com.fosscut.utils.PerformanceDefaults;

public class CgCompareSolverOptimalgen10it2inPlot extends CgCompareSolverOptimalgen10itPlot {

    protected static LinkedList<Integer> x10seedsLinkedList = LinkedList_of(
        2, 10, 20, 22, 23, 24, 26, 27, 31, 38,  // 10 seeds
        41, 42, 43, 44, 46, 83, 85, 86, 88, 96,           // 10 seeds
        97, 98, 103, 105, 106, 125, 126, 127, 136, 137,   // 10 seeds
        141, 142, 143, 166, 176, 178, 183, 189, 193, 194, // 10 seeds
        195, 196, 198, 199, 203, 206, 231, 259, 268, 273  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x10seedsLinkedHashMap
        = LinkedHashMapFromList_of(x10seedsLinkedList);

    protected static LinkedList<Integer> x20seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32, // 10 seeds
        33, 34, 35, 36, 37, 38, 39, 40, 41, 42, // 10 seeds
        43, 44, 45, 46, 47, 48, 49, 50, 51, 52  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x20seedsLinkedHashMap
        = LinkedHashMapFromList_of(x20seedsLinkedList);

    protected static LinkedList<Integer> x30seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32, // 10 seeds
        33, 34, 35, 36, 37, 38, 39, 40, 41, 42, // 10 seeds
        43, 44, 45, 46, 47, 48, 49, 50, 51, 52  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x30seedsLinkedHashMap
        = LinkedHashMapFromList_of(x30seedsLinkedList);

    protected static LinkedList<Integer> x40seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32, // 10 seeds
        33, 34, 35, 36, 37, 38, 39, 40, 41, 42, // 10 seeds
        43, 44, 45, 46, 47, 48, 49, 50, 51, 52  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x40seedsLinkedHashMap
        = LinkedHashMapFromList_of(x40seedsLinkedList);

    // x50 - x150 seeds are inherited from CgCompareSolverOptimalgen10itPlot

    protected static LinkedHashMap<String, LinkedList<Integer>> getXAxisLabelSeedsMapOfLists() {
        LinkedHashMap<String, LinkedList<Integer>> xAxisLabelSeedsMapOfLists = new LinkedHashMap<>();
        xAxisLabelSeedsMapOfLists.put("10", detectDuplicates(x10seedsLinkedList));
        xAxisLabelSeedsMapOfLists.put("20", detectDuplicates(x20seedsLinkedList));
        xAxisLabelSeedsMapOfLists.put("30", detectDuplicates(x30seedsLinkedList));
        xAxisLabelSeedsMapOfLists.put("40", detectDuplicates(x40seedsLinkedList));
        xAxisLabelSeedsMapOfLists.put("50", detectDuplicates(x50seedsLinkedList));
        xAxisLabelSeedsMapOfLists.put("60", detectDuplicates(x60seedsLinkedList));
        xAxisLabelSeedsMapOfLists.put("70", detectDuplicates(x70seedsLinkedList));
        xAxisLabelSeedsMapOfLists.put("80", detectDuplicates(x80seedsLinkedList));
        xAxisLabelSeedsMapOfLists.put("90", detectDuplicates(x90seedsLinkedList));
        xAxisLabelSeedsMapOfLists.put("100", detectDuplicates(x100seedsLinkedList));
        xAxisLabelSeedsMapOfLists.put("110", detectDuplicates(x110seedsLinkedList));
        xAxisLabelSeedsMapOfLists.put("120", detectDuplicates(x120seedsLinkedList));
        xAxisLabelSeedsMapOfLists.put("130", detectDuplicates(x130seedsLinkedList));
        xAxisLabelSeedsMapOfLists.put("140", detectDuplicates(x140seedsLinkedList));
        xAxisLabelSeedsMapOfLists.put("150", detectDuplicates(x150seedsLinkedList));
        return xAxisLabelSeedsMapOfLists;
    }


    @Test public void cgCompareCLPAndGlopWithSCIPOptimalgen10it2inPlot() throws IOException {
        String testName = "cgCompareCLPAndGLOPWithSCIPOptimalgen10it2inPlot";
        PlotData clpPlotData = new PlotData("cgCompareSolverLinCLPIntSCIPOptimalgen10it2inTest");
        PlotData glopPlotData = new PlotData("cgCompareSolverLinGLOPIntSCIPOptimalgen10it2inTest");

        // GLOP had timeouts for 110 output types and above
        // while CLP was able to solve 110 and 120 while timeouts started
        // from 130 output types
        // so CLP is more capable but GLOP could provide better results

        new XYPlot(testName + "Time.tex",
                getCombinedXAxisLabelsList(
                    clpPlotData.getXAxisLabels(),
                    glopPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    clpPlotData.getAverageElapsedTimeSeconds(),
                    glopPlotData.getAverageElapsedTimeSeconds()
                ),
                "Liczba typów elementów wyjściowych",
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_TIME,
                null, null, "0", "30",
                new LinkedList<String>() {{
                    add("CLP");
                    add("GLOP");
                }},
                clpPlotData.getXAxisLabels()
        ).generatePlot();

        // one run does not make sense sense to show because SCIP multithreaded
        // is nondeterministic so it has to be run multiple times to get reliable results
        // CLP will win over GLOP simply because it is able to solve larger instances
        // new XYPlot(testName + "1Run" + "WastePercentage.tex",
        //         xAxisLabels,
        //         getCombinedDataSeries(
        //             clpPlotData.getAverageBestPerSeedPercentageTrueWasteAboveOptimal(1),
        //             glopPlotData.getAverageBestPerSeedPercentageTrueWasteAboveOptimal(1)
        //         ),
        //         "Liczba typów elementów wyjściowych",
        //         PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
        //         null, null, "0", "0.004",
        //         new LinkedList<String>() {{
        //             add("CLP");
        //             add("GLOP");
        //         }},
        //         xAxisLabels
        // ).generatePlot();

        new XYPlot(testName + "10Run" + "WastePercentage.tex",
                getCombinedXAxisLabelsList(
                    clpPlotData.getXAxisLabels(),
                    glopPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    clpPlotData.getAverageBestPerSeedPercentageTrueWasteAboveOptimal(10),
                    glopPlotData.getAverageBestPerSeedPercentageTrueWasteAboveOptimal(10)
                ),
                "Liczba typów elementów wyjściowych",
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
                null, null, "0", "0.001",
                new LinkedList<String>() {{
                    add("CLP");
                    add("GLOP");
                }},
                clpPlotData.getXAxisLabels()
        ).generatePlot();
    }

}
