package com.fosscut.relax;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import com.fosscut.compare.alg.CompareAlgOptimalgen10itPlot;
import com.fosscut.plot.PlotData;
import com.fosscut.plot.XYPlot;
import com.fosscut.utils.PerformanceDefaults;

// extending to use the same orders and orderCommand as in CompareAlg tests
public class RelaxOptimalgenPlot extends CompareAlgOptimalgen10itPlot {

    @Test public void ffdRelaxOptimalgenLengthPercentagePlot() throws IOException {
        // DONE
        String testName = "ffdRelaxOptimalgenLengthPercentagePlot";
        PlotData ffdNoRelax = new PlotData("ffdCompareAlgOptimalgen10itTest"); // no relaxation
        PlotData ffd50p5lp = new PlotData("ffdRelaxOptimalgen50p5lp");
        PlotData ffd50p10lp = new PlotData("ffdRelaxOptimalgen50p10lp");
        PlotData ffd50p15lp = new PlotData("ffdRelaxOptimalgen50p15lp");
        // PlotData ffd50p20lp = new PlotData("ffdRelaxOptimalgen50p20lp"); // allowing for 20% of length to be relaxed seems to be unreasonable

        new XYPlot(testName + "Time.tex",
                getCombinedXAxisLabelsList(
                    ffdNoRelax.getXAxisLabels(),
                    ffd50p5lp.getXAxisLabels(),
                    ffd50p10lp.getXAxisLabels(),
                    ffd50p15lp.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    ffdNoRelax.getAverageElapsedTimeSeconds(),
                    ffd50p5lp.getAverageElapsedTimeSeconds(),
                    ffd50p10lp.getAverageElapsedTimeSeconds(),
                    ffd50p15lp.getAverageElapsedTimeSeconds()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_TIME,
                null, null, "0", "1",
                new LinkedList<String>() {{
                    add("0\\%");
                    add("5\\%");
                    add("10\\%");
                    add("15\\%");
                }},
                ffdNoRelax.getXAxisLabels()
        ).generatePlot();

        new XYPlot(testName + "MemoryUsagePeak.tex",
                getCombinedXAxisLabelsList(
                    ffdNoRelax.getXAxisLabels(),
                    ffd50p5lp.getXAxisLabels(),
                    ffd50p10lp.getXAxisLabels(),
                    ffd50p15lp.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    ffdNoRelax.getAverageMemoryUsagePeakGibiBytes(),
                    ffd50p5lp.getAverageMemoryUsagePeakGibiBytes(),
                    ffd50p10lp.getAverageMemoryUsagePeakGibiBytes(),
                    ffd50p15lp.getAverageMemoryUsagePeakGibiBytes()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_MEMORY_USAGE_GIBI_BYTES,
                null, null, "0", "0.04",
                new LinkedList<String>() {{
                    add("0\\%");
                    add("5\\%");
                    add("10\\%");
                    add("15\\%");
                }},
                ffdNoRelax.getXAxisLabels()
        ).generatePlot();

        new XYPlot(testName + "WastePercentage.tex",
                getCombinedXAxisLabelsList(
                    ffdNoRelax.getXAxisLabels(),
                    ffd50p5lp.getXAxisLabels(),
                    ffd50p10lp.getXAxisLabels(),
                    ffd50p15lp.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    ffdNoRelax.getAveragePercentageTrueWasteAboveOptimal(),
                    ffd50p5lp.getAveragePercentageTrueWasteAboveOptimal(),
                    ffd50p10lp.getAveragePercentageTrueWasteAboveOptimal(),
                    ffd50p15lp.getAveragePercentageTrueWasteAboveOptimal()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
                "10cm", null, null, "-8", "4",
                new LinkedList<String>() {{
                    add("0\\%");
                    add("5\\%");
                    add("10\\%");
                    add("15\\%");
                }},
                ffdNoRelax.getXAxisLabels()
        ).generatePlot();
    }

    @Test public void greedyRelaxOptimalgenLengthPercentagePlot() throws IOException {
        // DONE
        String testName = "greedyRelaxOptimalgenLengthPercentagePlot";
        PlotData greedyNoRelax = new PlotData("greedyCompareAlgSCIPOptimalgen10itTest"); // no relaxation
        PlotData greedy50p5lp = new PlotData("greedyRelaxOptimalgenIntSCIP50p5lp0c");
        PlotData greedy50p10lp = new PlotData("greedyRelaxOptimalgenIntSCIP50p10lp0c");
        PlotData greedy50p15lp = new PlotData("greedyRelaxOptimalgenIntSCIP50p15lp0c");

        new XYPlot(testName + "Time.tex",
                getCombinedXAxisLabelsList(
                    greedyNoRelax.getXAxisLabels(),
                    greedy50p5lp.getXAxisLabels(),
                    greedy50p10lp.getXAxisLabels(),
                    greedy50p15lp.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    greedyNoRelax.getAverageElapsedTimeSeconds(),
                    greedy50p5lp.getAverageElapsedTimeSeconds(),
                    greedy50p10lp.getAverageElapsedTimeSeconds(),
                    greedy50p15lp.getAverageElapsedTimeSeconds()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_TIME,
                null, null, "0", "80",
                new LinkedList<String>() {{
                    add("0\\%");
                    add("5\\%");
                    add("10\\%");
                    add("15\\%");
                }},
                greedyNoRelax.getXAxisLabels()
        ).generatePlot();

        new XYPlot(testName + "MemoryUsagePeak.tex",
                getCombinedXAxisLabelsList(
                    greedyNoRelax.getXAxisLabels(),
                    greedy50p5lp.getXAxisLabels(),
                    greedy50p10lp.getXAxisLabels(),
                    greedy50p15lp.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    greedyNoRelax.getAverageMemoryUsagePeakGibiBytes(),
                    greedy50p5lp.getAverageMemoryUsagePeakGibiBytes(),
                    greedy50p10lp.getAverageMemoryUsagePeakGibiBytes(),
                    greedy50p15lp.getAverageMemoryUsagePeakGibiBytes()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_MEMORY_USAGE_GIBI_BYTES,
                null, null, "0", "12",
                new LinkedList<String>() {{
                    add("0\\%");
                    add("5\\%");
                    add("10\\%");
                    add("15\\%");
                }},
                greedyNoRelax.getXAxisLabels()
        ).generatePlot();

        new XYPlot(testName + "WastePercentage.tex",
                getCombinedXAxisLabelsList(
                    greedyNoRelax.getXAxisLabels(),
                    greedy50p5lp.getXAxisLabels(),
                    greedy50p10lp.getXAxisLabels(),
                    greedy50p15lp.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    greedyNoRelax.getAveragePercentageTrueWasteAboveOptimal(),
                    greedy50p5lp.getAveragePercentageTrueWasteAboveOptimal(),
                    greedy50p10lp.getAveragePercentageTrueWasteAboveOptimal(),
                    greedy50p15lp.getAveragePercentageTrueWasteAboveOptimal()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
                "10cm", null, null, "-8", "4",
                new LinkedList<String>() {{
                    add("0\\%");
                    add("5\\%");
                    add("10\\%");
                    add("15\\%");
                }},
                greedyNoRelax.getXAxisLabels()
        ).generatePlot();
    }

    @Test public void cgRelaxOptimalgenLengthPercentagePlot() throws IOException {
        // DONE
        String testName = "cgRelaxOptimalgenLengthPercentagePlot";
        PlotData cgNoRelax = new PlotData("cgCompareAlgSCIPOptimalgen10itTest"); // no relaxation
        PlotData cg50p5lp = new PlotData("cgRelaxOptimalgenLinCLPIntSCIP50p5lp0c");
        PlotData cg50p10lp = new PlotData("cgRelaxOptimalgenLinCLPIntSCIP50p10lp0c");
        PlotData cg50p15lp = new PlotData("cgRelaxOptimalgenLinCLPIntSCIP50p15lp0c");

        new XYPlot(testName + "Time.tex",
                getCombinedXAxisLabelsList(
                    cgNoRelax.getXAxisLabels(),
                    cg50p5lp.getXAxisLabels(),
                    cg50p10lp.getXAxisLabels(),
                    cg50p15lp.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    cgNoRelax.getAverageElapsedTimeSeconds(),
                    cg50p5lp.getAverageElapsedTimeSeconds(),
                    cg50p10lp.getAverageElapsedTimeSeconds(),
                    cg50p15lp.getAverageElapsedTimeSeconds()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_TIME,
                null, null, "0", "150",
                new LinkedList<String>() {{
                    add("0\\%");
                    add("5\\%");
                    add("10\\%");
                    add("15\\%");
                }},
                cgNoRelax.getXAxisLabels()
        ).generatePlot();

        new XYPlot(testName + "MemoryUsagePeak.tex",
                getCombinedXAxisLabelsList(
                    cgNoRelax.getXAxisLabels(),
                    cg50p5lp.getXAxisLabels(),
                    cg50p10lp.getXAxisLabels(),
                    cg50p15lp.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    cgNoRelax.getAverageMemoryUsagePeakGibiBytes(),
                    cg50p5lp.getAverageMemoryUsagePeakGibiBytes(),
                    cg50p10lp.getAverageMemoryUsagePeakGibiBytes(),
                    cg50p15lp.getAverageMemoryUsagePeakGibiBytes()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_MEMORY_USAGE_GIBI_BYTES,
                null, null, "0", "8",
                new LinkedList<String>() {{
                    add("0\\%");
                    add("5\\%");
                    add("10\\%");
                    add("15\\%");
                }},
                cgNoRelax.getXAxisLabels()
        ).generatePlot();

        new XYPlot(testName + "WastePercentage.tex",
                getCombinedXAxisLabelsList(
                    cgNoRelax.getXAxisLabels(),
                    cg50p5lp.getXAxisLabels(),
                    cg50p10lp.getXAxisLabels(),
                    cg50p15lp.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    cgNoRelax.getAveragePercentageTrueWasteAboveOptimal(),
                    cg50p5lp.getAveragePercentageTrueWasteAboveOptimal(),
                    cg50p10lp.getAveragePercentageTrueWasteAboveOptimal(),
                    cg50p15lp.getAveragePercentageTrueWasteAboveOptimal()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
                "10cm", null, null, "-8", "4",
                new LinkedList<String>() {{
                    add("0\\%");
                    add("5\\%");
                    add("10\\%");
                    add("15\\%");
                }},
                cgNoRelax.getXAxisLabels()
        ).generatePlot();
    }

    @Test public void compareAlgRelaxOptimalgenLengthPercentagePlot() throws IOException {
        // DONE
        String testName = "compareAlgRelaxOptimalgenLengthPercentagePlot";
        PlotData ffd50p5lp = new PlotData("ffdRelaxOptimalgen50p5lp"); // ffd does not have relax cost so all are compared with c = 0
        PlotData greedy50p5lp = new PlotData("greedyRelaxOptimalgenIntSCIP50p5lp0c");
        PlotData cg50p5lp = new PlotData("cgRelaxOptimalgenLinCLPIntSCIP50p5lp0c");

        new XYPlot(testName + "5lpWastePercentage.tex",
                getCombinedXAxisLabelsList(
                    ffd50p5lp.getXAxisLabels(),
                    greedy50p5lp.getXAxisLabels(),
                    cg50p5lp.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    ffd50p5lp.getAveragePercentageTrueWasteAboveOptimal(),
                    greedy50p5lp.getAveragePercentageTrueWasteAboveOptimal(),
                    cg50p5lp.getAveragePercentageTrueWasteAboveOptimal()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
                null, null, "-3", "1",
                new LinkedList<String>() {{
                    add("FFD");
                    add("Greedy");
                    add("CG");
                }},
                ffd50p5lp.getXAxisLabels()
        ).generatePlot();

        PlotData ffd50p10lp = new PlotData("ffdRelaxOptimalgen50p10lp"); // ffd does not have relax cost so all are compared with c = 0
        PlotData greedy50p10lp = new PlotData("greedyRelaxOptimalgenIntSCIP50p10lp0c");
        PlotData cg50p10lp = new PlotData("cgRelaxOptimalgenLinCLPIntSCIP50p10lp0c");

        new XYPlot(testName + "10lpWastePercentage.tex",
                getCombinedXAxisLabelsList(
                    ffd50p10lp.getXAxisLabels(),
                    greedy50p10lp.getXAxisLabels(),
                    cg50p10lp.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    ffd50p10lp.getAveragePercentageTrueWasteAboveOptimal(),
                    greedy50p10lp.getAveragePercentageTrueWasteAboveOptimal(),
                    cg50p10lp.getAveragePercentageTrueWasteAboveOptimal()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
                "10cm", null, null, "-6", "0",
                new LinkedList<String>() {{
                    add("FFD");
                    add("Greedy");
                    add("CG");
                }},
                ffd50p10lp.getXAxisLabels()
        ).generatePlot();

        PlotData ffd50p15lp = new PlotData("ffdRelaxOptimalgen50p15lp"); // ffd does not have relax cost so all are compared with c = 0
        PlotData greedy50p15lp = new PlotData("greedyRelaxOptimalgenIntSCIP50p15lp0c");
        PlotData cg50p15lp = new PlotData("cgRelaxOptimalgenLinCLPIntSCIP50p15lp0c");

        new XYPlot(testName + "15lpWastePercentage.tex",
                getCombinedXAxisLabelsList(
                    ffd50p15lp.getXAxisLabels(),
                    greedy50p15lp.getXAxisLabels(),
                    cg50p15lp.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    ffd50p15lp.getAveragePercentageTrueWasteAboveOptimal(),
                    greedy50p15lp.getAveragePercentageTrueWasteAboveOptimal(),
                    cg50p15lp.getAveragePercentageTrueWasteAboveOptimal()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
                "10cm", null, null, "-8", "0",
                new LinkedList<String>() {{
                    add("FFD");
                    add("Greedy");
                    add("CG");
                }},
                ffd50p15lp.getXAxisLabels()
        ).generatePlot();
    }

    @Test public void greedyRelaxOptimalgenRelaxCostPlot() throws IOException {
        // DONE
        String testName = "greedyRelaxOptimalgenRelaxCostPlot";
        PlotData greedyNoRelax = new PlotData("greedyCompareAlgSCIPOptimalgen10itTest"); // no relaxation
        PlotData greedy50p10lp0c = new PlotData("greedyRelaxOptimalgenIntSCIP50p10lp0c");
        PlotData greedy50p10lp01c = new PlotData("greedyRelaxOptimalgenIntSCIP50p10lp01c");
        PlotData greedy50p10lp1c = new PlotData("greedyRelaxOptimalgenIntSCIP50p10lp1c");
        PlotData greedy50p10lp2c = new PlotData("greedyRelaxOptimalgenIntSCIP50p10lp2c");
        // PlotData greedy50p10lp5c = new PlotData("greedyRelaxOptimalgenIntSCIP50p10lp5c"); // incomplete results for c = 5, and it's very similar to c = 2, so not really worth plotting

        new XYPlot(testName + "Time.tex",
                getCombinedXAxisLabelsList(
                    greedyNoRelax.getXAxisLabels(),
                    greedy50p10lp0c.getXAxisLabels(),
                    greedy50p10lp01c.getXAxisLabels(),
                    greedy50p10lp1c.getXAxisLabels(),
                    greedy50p10lp2c.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    greedyNoRelax.getAverageElapsedTimeSeconds(),
                    greedy50p10lp0c.getAverageElapsedTimeSeconds(),
                    greedy50p10lp01c.getAverageElapsedTimeSeconds(),
                    greedy50p10lp1c.getAverageElapsedTimeSeconds(),
                    greedy50p10lp2c.getAverageElapsedTimeSeconds()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_TIME,
                null, null, "0", "200",
                new LinkedList<String>() {{
                    add("no relaxation");
                    add("c = 0");
                    add("c = 0.1");
                    add("c = 1");
                    add("c = 2");
                }},
                greedyNoRelax.getXAxisLabels()
        ).generatePlot();

        new XYPlot(testName + "MemoryUsagePeak.tex",
                getCombinedXAxisLabelsList(
                    greedyNoRelax.getXAxisLabels(),
                    greedy50p10lp0c.getXAxisLabels(),
                    greedy50p10lp01c.getXAxisLabels(),
                    greedy50p10lp1c.getXAxisLabels(),
                    greedy50p10lp2c.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    greedyNoRelax.getAverageMemoryUsagePeakGibiBytes(),
                    greedy50p10lp0c.getAverageMemoryUsagePeakGibiBytes(),
                    greedy50p10lp01c.getAverageMemoryUsagePeakGibiBytes(),
                    greedy50p10lp1c.getAverageMemoryUsagePeakGibiBytes(),
                    greedy50p10lp2c.getAverageMemoryUsagePeakGibiBytes()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_MEMORY_USAGE_GIBI_BYTES,
                null, null, "0", "20",
                new LinkedList<String>() {{
                    add("no relaxation");
                    add("c = 0");
                    add("c = 0.1");
                    add("c = 1");
                    add("c = 2");
                }},
                greedyNoRelax.getXAxisLabels()
        ).generatePlot();

        new XYPlot(testName + "WastePercentage.tex",
                getCombinedXAxisLabelsList(
                    greedyNoRelax.getXAxisLabels(),
                    greedy50p10lp0c.getXAxisLabels(),
                    greedy50p10lp01c.getXAxisLabels(),
                    greedy50p10lp1c.getXAxisLabels(),
                    greedy50p10lp2c.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    greedyNoRelax.getAveragePercentageTrueWasteAboveOptimal(),
                    greedy50p10lp0c.getAveragePercentageTrueWasteAboveOptimal(),
                    greedy50p10lp01c.getAveragePercentageTrueWasteAboveOptimal(),
                    greedy50p10lp1c.getAveragePercentageTrueWasteAboveOptimal(),
                    greedy50p10lp2c.getAveragePercentageTrueWasteAboveOptimal()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
                "10cm", null, null, "-6", "4",
                new LinkedList<String>() {{
                    add("no relaxation");
                    add("c = 0");
                    add("c = 0.1");
                    add("c = 1");
                    add("c = 2");
                }},
                greedyNoRelax.getXAxisLabels()
        ).generatePlot();
    }

    @Test public void cgRelaxOptimalgenRelaxCostPlot() throws IOException {
        // DONE
        String testName = "cgRelaxOptimalgenRelaxCostPlot";
        PlotData cgNoRelax = new PlotData("cgCompareAlgSCIPOptimalgen10itTest"); // no relaxation
        PlotData cg50p10lp0c = new PlotData("cgRelaxOptimalgenLinCLPIntSCIP50p10lp0c");
        // PlotData cg50p10lp01c = new PlotData("cgRelaxOptimalgenLinCLPIntSCIP50p10lp01c"); // c = 0.1 is almost the same as c = 0, so not really worth plotting
        PlotData cg50p10lp1c = new PlotData("cgRelaxOptimalgenLinCLPIntSCIP50p10lp1c");
        PlotData cg50p10lp2c = new PlotData("cgRelaxOptimalgenLinCLPIntSCIP50p10lp2c");
        PlotData cg50p10lp5c = new PlotData("cgRelaxOptimalgenLinCLPIntSCIP50p10lp5c");
        // PlotData cg50p10lp10c = new PlotData("cgRelaxOptimalgenLinCLPIntSCIP50p10lp10c"); // c = 10 just results in relaxation being off, so not really worth plotting

        new XYPlot(testName + "Time.tex",
                getCombinedXAxisLabelsList(
                    cgNoRelax.getXAxisLabels(),
                    cg50p10lp0c.getXAxisLabels(),
                    cg50p10lp1c.getXAxisLabels(),
                    cg50p10lp2c.getXAxisLabels(),
                    cg50p10lp5c.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    cgNoRelax.getAverageElapsedTimeSeconds(),
                    cg50p10lp0c.getAverageElapsedTimeSeconds(),
                    cg50p10lp1c.getAverageElapsedTimeSeconds(),
                    cg50p10lp2c.getAverageElapsedTimeSeconds(),
                    cg50p10lp5c.getAverageElapsedTimeSeconds()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_TIME,
                null, null, "0", "150",
                new LinkedList<String>() {{
                    add("no relaxation");
                    add("c = 0");
                    add("c = 1");
                    add("c = 2");
                    add("c = 5");
                }},
                cgNoRelax.getXAxisLabels()
        ).generatePlot();

        new XYPlot(testName + "MemoryUsagePeak.tex",
                getCombinedXAxisLabelsList(
                    cgNoRelax.getXAxisLabels(),
                    cg50p10lp0c.getXAxisLabels(),
                    cg50p10lp1c.getXAxisLabels(),
                    cg50p10lp2c.getXAxisLabels(),
                    cg50p10lp5c.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    cgNoRelax.getAverageMemoryUsagePeakGibiBytes(),
                    cg50p10lp0c.getAverageMemoryUsagePeakGibiBytes(),
                    cg50p10lp1c.getAverageMemoryUsagePeakGibiBytes(),
                    cg50p10lp2c.getAverageMemoryUsagePeakGibiBytes(),
                    cg50p10lp5c.getAverageMemoryUsagePeakGibiBytes()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_MEMORY_USAGE_GIBI_BYTES,
                null, null, "0", "8",
                new LinkedList<String>() {{
                    add("no relaxation");
                    add("c = 0");
                    add("c = 1");
                    add("c = 2");
                    add("c = 5");
                }},
                cgNoRelax.getXAxisLabels()
        ).generatePlot();

        new XYPlot(testName + "WastePercentage.tex",
                getCombinedXAxisLabelsList(
                    cgNoRelax.getXAxisLabels(),
                    cg50p10lp0c.getXAxisLabels(),
                    cg50p10lp1c.getXAxisLabels(),
                    cg50p10lp2c.getXAxisLabels(),
                    cg50p10lp5c.getXAxisLabels()
                ),
                getCombinedDataSeries(
                    cgNoRelax.getAveragePercentageTrueWasteAboveOptimal(),
                    cg50p10lp0c.getAveragePercentageTrueWasteAboveOptimal(),
                    cg50p10lp1c.getAveragePercentageTrueWasteAboveOptimal(),
                    cg50p10lp2c.getAveragePercentageTrueWasteAboveOptimal(),
                    cg50p10lp5c.getAveragePercentageTrueWasteAboveOptimal()
                ),
                PerformanceDefaults.GRAPH_X_LABEL_OUTPUT_TYPES,
                PerformanceDefaults.GRAPH_Y_LABEL_CPU_WASTE,
                "10cm", null, null, "-6", "4",
                new LinkedList<String>() {{
                    add("no relaxation");
                    add("c = 0");
                    add("c = 1");
                    add("c = 2");
                    add("c = 5");
                }},
                cgNoRelax.getXAxisLabels()
        ).generatePlot();
    }

    // ALL BELOW COPIED FROM PARENT CLASS to limit the number of orders/seeds to 30
    // DONE:       first 10
    // INPROGRESS: 10-30

    protected static LinkedList<Integer> x10seedsLinkedList = LinkedList_of(
        2, 10, 20, 22, 23, 24, 26, 27, 31, 38,  // 10 seeds
        41, 42, 43, 44, 46, 83, 85, 86, 88, 96,           // 10 seeds
        97, 98, 103, 105, 106, 125, 126, 127, 136, 137   // 10 seeds
        // 141, 142, 143, 166, 176, 178, 183, 189, 193, 194, // 10 seeds
        // 195, 196, 198, 199, 203, 206, 231, 259, 268, 273  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x10seedsLinkedHashMap
        = LinkedHashMapFromList_of(x10seedsLinkedList);

    protected static LinkedList<Integer> x20seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32 // 10 seeds
        // 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, // 10 seeds
        // 43, 44, 45, 46, 47, 48, 49, 50, 51, 52  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x20seedsLinkedHashMap
        = LinkedHashMapFromList_of(x20seedsLinkedList);

    protected static LinkedList<Integer> x30seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32 // 10 seeds
        // 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, // 10 seeds
        // 43, 44, 45, 46, 47, 48, 49, 50, 51, 52  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x30seedsLinkedHashMap
        = LinkedHashMapFromList_of(x30seedsLinkedList);

    protected static LinkedList<Integer> x40seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32 // 10 seeds
        // 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, // 10 seeds
        // 43, 44, 45, 46, 47, 48, 49, 50, 51, 52  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x40seedsLinkedHashMap
        = LinkedHashMapFromList_of(x40seedsLinkedList);

    protected static LinkedList<Integer> x50seedsLinkedList = LinkedList_of(
        1, 57, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 33, 31, 32 // 10 seeds
        // 34, 35, 36, 37, 38, 39, 40, 54, 42, 43, // 10 seeds
        // 44, 45, 46, 47, 48, 49, 50, 55, 52, 53  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x50seedsLinkedHashMap
        = LinkedHashMapFromList_of(x50seedsLinkedList);

    protected static LinkedList<Integer> x60seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 33, 20, 22, // 10 seeds
        23, 24, 34, 26, 27, 28, 29, 30, 31, 32 // 10 seeds
        // 35, 36, 55, 38, 39, 58, 41, 42, 43, 44, // 10 seeds
        // 45, 46, 47, 48, 49, 50, 51, 52, 53, 54  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x60seedsLinkedHashMap
        = LinkedHashMapFromList_of(x60seedsLinkedList);

    protected static LinkedList<Integer> x70seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 33, 26, 27, 28, 29, 30, 31, 32 // 10 seeds
        // 35, 36, 37, 55, 39, 40, 41, 42, 43, 44, // 10 seeds
        // 45, 46, 47, 48, 49, 50, 51, 52, 53, 57  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x70seedsLinkedHashMap
        = LinkedHashMapFromList_of(x70seedsLinkedList);

    protected static LinkedList<Integer> x80seedsLinkedList = LinkedList_of(
        1, 2, 4, 33, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 34, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32 // 10 seeds
        // 35, 36, 37, 38, 39, 40, 41, 42, 43, 55, // 10 seeds
        // 45, 46, 47, 48, 49, 50, 57, 52, 53, 54  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x80seedsLinkedHashMap
        = LinkedHashMapFromList_of(x80seedsLinkedList);

    protected static LinkedList<Integer> x90seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 34, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 33, 28, 29, 30, 31, 32 // 10 seeds
        // 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, // 10 seeds
        // 45, 46, 47, 48, 49, 50, 55, 52, 53, 54  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x90seedsLinkedHashMap
        = LinkedHashMapFromList_of(x90seedsLinkedList);

    protected static LinkedList<Integer> x100seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 35, 33, 9, 10, 11, // 10 seeds
        36, 13, 14, 15, 16, 37, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 38, 34, 31, 32 // 10 seeds
        // 39, 40, 41, 42, 43, 44, 45, 46, 47, 59, // 10 seeds
        // 49, 50, 60, 52, 53, 61, 62, 63, 57, 58  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x100seedsLinkedHashMap
        = LinkedHashMapFromList_of(x100seedsLinkedList);

    protected static LinkedList<Integer> x110seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 33, // 10 seeds
        12, 37, 34, 15, 16, 35, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32 // 10 seeds
        // 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, // 10 seeds
        // 48, 49, 50, 58, 52, 53, 54, 55, 59, 57  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x110seedsLinkedHashMap
        = LinkedHashMapFromList_of(x110seedsLinkedList);

    protected static LinkedList<Integer> x120seedsLinkedList = LinkedList_of(
        1, 2, 4, 33, 6, 7, 8, 9, 10, 34, // 10 seeds
        40, 13, 47, 15, 16, 37, 18, 19, 38, 22, // 10 seeds
        23, 24, 25, 26, 27, 42, 43, 39, 46, 32 // 10 seeds
        // 48, 49, 50, 73, 52, 53, 69, 55, 70, 57, // 10 seeds
        // 58, 74, 72, 61, 62, 63, 64, 65, 66, 67  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x120seedsLinkedHashMap
        = LinkedHashMapFromList_of(x120seedsLinkedList);

    protected static LinkedList<Integer> x130seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 46, 7, 8, 34, 35, 48, // 10 seeds
        37, 38, 39, 15, 16, 40, 18, 19, 41, 22, // 10 seeds
        23, 24, 42, 26, 47, 44, 45, 30, 31, 32 // 10 seeds
        // 49, 50, 69, 52, 70, 81, 55, 75, 57, 58, // 10 seeds
        // 59, 60, 61, 62, 63, 73, 65, 66, 67, 68  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x130seedsLinkedHashMap
        = LinkedHashMapFromList_of(x130seedsLinkedList);

    protected static LinkedList<Integer> x140seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 33, 34, 35, 9, 47, 37, // 10 seeds
        12, 13, 38, 15, 39, 86, 18, 19, 40, 22, // 10 seeds
        23, 24, 41, 26, 27, 42, 43, 46, 31, 32 // 10 seeds
        // 68, 49, 50, 69, 52, 79, 71, 55, 72, 73, // 10 seeds
        // 74, 75, 60, 76, 82, 81, 64, 65, 85, 67  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x140seedsLinkedHashMap
        = LinkedHashMapFromList_of(x140seedsLinkedList);


    protected static LinkedList<Integer> x150seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 33, 49, 34, 9, 35, 55, // 10 seeds
        45, 13, 37, 46, 16, 38, 18, 39, 40, 22, // 10 seeds
        23, 24, 47, 26, 41, 28, 53, 42, 31, 32 // 10 seeds
        // 86, 57, 82, 87, 60, 81, 62, 63, 85, 65, // 10 seeds
        // 66, 67, 68, 69, 70, 79, 72, 73, 74, 75  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x150seedsLinkedHashMap
        = LinkedHashMapFromList_of(x150seedsLinkedList);

    protected static LinkedList<Integer> x160seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 54, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32 // 10 seeds
        // 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, // 10 seeds
        // 43, 44, 45, 46, 48, 49, 50, 51, 52, 53  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x160seedsLinkedHashMap
        = LinkedHashMapFromList_of(x160seedsLinkedList);

    protected static LinkedList<Integer> x170seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 55, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32 // 10 seeds
        // 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, // 10 seeds
        // 43, 44, 45, 46, 48, 49, 50, 51, 52, 53  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x170seedsLinkedHashMap
        = LinkedHashMapFromList_of(x170seedsLinkedList);

    protected static LinkedList<Integer> x180seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32 // 10 seeds
        // 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, // 10 seeds
        // 43, 44, 45, 46, 48, 49, 50, 51, 52, 53  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x180seedsLinkedHashMap
        = LinkedHashMapFromList_of(x180seedsLinkedList);

    protected static LinkedList<Integer> x190seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 54, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32 // 10 seeds
        // 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, // 10 seeds
        // 43, 44, 45, 46, 48, 49, 50, 51, 52, 53  // 10 seeds
    );
    protected static LinkedHashMap<Integer, Integer> x190seedsLinkedHashMap
        = LinkedHashMapFromList_of(x190seedsLinkedList);

    protected static LinkedList<Integer> x200seedsLinkedList = LinkedList_of(
        1, 2, 4, 5, 6, 7, 8, 9, 10, 11, // 10 seeds
        12, 13, 14, 15, 16, 17, 18, 19, 20, 22, // 10 seeds
        23, 24, 25, 26, 27, 28, 29, 30, 31, 32 // 10 seeds
        // 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, // 10 seeds
        // 43, 44, 45, 46, 48, 49, 50, 51, 52, 53  // 10 seeds
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

}
