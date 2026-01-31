package com.fosscut.compare.gen;

import java.io.IOException;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.fosscut.AbstractTest;
import com.fosscut.plot.PlotData;
import com.fosscut.plot.XYPlot;
import com.fosscut.utils.PerformanceDefaults;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GenComparePlot extends AbstractTest {

    private static String testName = "genCompare";
    protected static String planCommand = "cg --linear-solver GLOP --integer-solver SCIP -ln 1 -in 1 --timeout-amount 5 --timeout-unit MINUTES";

    protected static LinkedList<Integer> seeds = LinkedList_of(3, 4, 9, 31, 58, 96, 121, 239, 373, 435);

    protected static int N_RUNS_INIT = 100; // larger than range to accommodate for future increases
    protected static int N_RUNS_WITH_IDENTICAL_SEED_START = 1;
    protected static int N_RUNS_WITH_IDENTICAL_SEED_END = 5;

    private PlotData cutgenPlotData;
    private PlotData optimalgenPlotData;
    private LinkedList<String> xtickLabels;

    @Test public void genCompareTimePlot() {
        new XYPlot(testName + "Time.tex",
                getCombinedXAxisLabelsList(
                    cutgenPlotData.getXAxisLabels(),
                    optimalgenPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    cutgenPlotData.getAverageElapsedTimeSeconds(),
                    optimalgenPlotData.getAverageElapsedTimeSeconds()
                ),
                "Ziarno generatora liczb pseudolosowych",
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_TIME,
                null, null, "0", "300",
                new LinkedList<String>() {{
                    add("CUTGEN1");
                    add("OPTIMALGEN");
                }},
                xtickLabels
        ).generatePlot();
    }

    @Test public void genCompareMemoryUsagePlot() {
        new XYPlot(testName + "MemoryUsagePeak.tex",
                getCombinedXAxisLabelsList(
                    cutgenPlotData.getXAxisLabels(),
                    optimalgenPlotData.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    cutgenPlotData.getAverageMemoryUsagePeakMebiBytes(),
                    optimalgenPlotData.getAverageMemoryUsagePeakMebiBytes()
                ),
                "Ziarno generatora liczb pseudolosowych",
                PerformanceDefaults.GRAPH_Y_LABEL_MEMORY_USAGE_MEBI_BYTES,
                null, null, "200", "2500",
                new LinkedList<String>() {{
                    add("CUTGEN1");
                    add("OPTIMALGEN");
                }},
                xtickLabels
        ).generatePlot();
    }

    @BeforeAll
    void setUp() throws IOException {
        cutgenPlotData = new PlotData("cutgenCompare");
        optimalgenPlotData = new PlotData("optimalgenCompare");

        xtickLabels = new LinkedList<String>();
        for (Integer seed : seeds) {
            xtickLabels.add(seed.toString());
        }
    }

}
