package com.fosscut.compare.gen;

import java.io.IOException;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import com.fosscut.AbstractTest;
import com.fosscut.plot.PlotData;
import com.fosscut.plot.XYPlot;
import com.fosscut.utils.PerformanceDefaults;

public class GenComparePlot extends AbstractTest {

    protected static String planCommand = "cg --linear-solver GLOP --integer-solver SCIP -ln 1 -in 1 --timeout-amount 5 --timeout-unit MINUTES";

    protected static LinkedList<Integer> seeds = LinkedList_of(3, 4, 9, 31, 58, 96, 121, 239, 373, 435);

    protected static int N_RUNS_INIT = 100; // larger than range to accommodate for future increases
    protected static int N_RUNS_WITH_IDENTICAL_SEED_START = 1;
    protected static int N_RUNS_WITH_IDENTICAL_SEED_END = 5;

    private static String testName = "genCompare";

    @Test public void optimalgenComparePlot() throws IOException {
        PlotData cutgenPlotData = new PlotData("cutgenCompare");
        PlotData optimalgenPlotData = new PlotData("optimalgenCompare");

        LinkedList<String> xtickLabels = new LinkedList<String>();
        for (Integer seed : seeds) {
            xtickLabels.add(seed.toString());
        }

        new XYPlot(testName + "Time.tex",
                cutgenPlotData.getXAxisLabels(),
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

}
