package com.fosscut.compare.solver.greedy;

import java.io.IOException;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import com.fosscut.AbstractTest;
import com.fosscut.plot.PlotData;
import com.fosscut.plot.XYPlot;
import com.fosscut.utils.PerformanceDefaults;

public class GreedyCompareSolverOptimalgen3itPlot extends AbstractTest {

    private static String testName = "greedyCompareSolverOptimalgen3it";
    protected static String orderCommand = "optimalgen -iu 1000 -il 800 -it 3 -ol 0.4 -ou 0.8 -oc 10000 --timeout-amount 10 --timeout-unit SECONDS";

    // 10 orders
    // each order is ran one time since solvers running in single-threaded mode
    // always produce the same result for identical seed
    protected static LinkedList<Integer> seeds = LinkedList_of(1, 2, 11, 12, 13, 6, 14, 15, 17, 10);

    @Test public void greedyCompareSolverOptimalgen3itPlot() throws IOException {
        PlotData cbcPlotData = new PlotData("greedyCompareSolverCBCOptimalgen3it");
        PlotData scipPlotData = new PlotData("greedyCompareSolverSCIPOptimalgen3it");
        PlotData scip2ThreadsPlotData = new PlotData("greedyCompareSolverSCIPOptimalgen3it2in");

        new XYPlot(testName + "Time.tex",
                getCombinedXAxisLabelsList(
                    cbcPlotData.getXAxisLabels(),
                    scipPlotData.getXAxisLabels(),
                    scip2ThreadsPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    cbcPlotData.getAverageElapsedTimeSeconds(),
                    scipPlotData.getAverageElapsedTimeSeconds(),
                    scip2ThreadsPlotData.getAverageElapsedTimeSeconds()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_TIME,
                null, null, "0", "100",
                new LinkedList<String>() {{
                    add("CBC");
                    add("SCIP");
                    add("SCIP - 2 wątki");
                }},
                cbcPlotData.getXAxisLabels()
        ).generatePlot();

        new XYPlot(testName + "MemoryUsagePeak.tex",
                getCombinedXAxisLabelsList(
                    cbcPlotData.getXAxisLabels(),
                    scipPlotData.getXAxisLabels(),
                    scip2ThreadsPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    cbcPlotData.getAverageMemoryUsagePeakGibiBytes(),
                    scipPlotData.getAverageMemoryUsagePeakGibiBytes(),
                    scip2ThreadsPlotData.getAverageMemoryUsagePeakGibiBytes()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_MEMORY_USAGE_GIBI_BYTES,
                null, null, "0", "5",
                new LinkedList<String>() {{
                    add("CBC");
                    add("SCIP");
                    add("SCIP - 2 wątki");
                }},
                cbcPlotData.getXAxisLabels()
        ).generatePlot();

        new XYPlot(testName + "WastePercentage.tex",
                getCombinedXAxisLabelsList(
                    cbcPlotData.getXAxisLabels(),
                    scipPlotData.getXAxisLabels(),
                    scip2ThreadsPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    cbcPlotData.getAveragePercentageTrueWasteAboveOptimal(),
                    scipPlotData.getAveragePercentageTrueWasteAboveOptimal(),
                    // only SCIP 2 threads uses best per seed average
                    scip2ThreadsPlotData.getAverageBestPerSeedPercentageTrueWasteAboveOptimal(10)
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
                null, null, "3", "5",
                new LinkedList<String>() {{
                    add("CBC");
                    add("SCIP");
                    add("SCIP - 2 wątki");
                }},
                cbcPlotData.getXAxisLabels()
        ).generatePlot();
    }

}
