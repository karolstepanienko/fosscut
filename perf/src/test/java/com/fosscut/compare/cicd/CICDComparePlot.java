package com.fosscut.compare.cicd;

import java.io.IOException;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import com.fosscut.plot.XYPlot;
import com.fosscut.utils.PerformanceDefaults;

public class CICDComparePlot {

    private static LinkedList<String> testNames = new LinkedList<String>() {{
        add("CICDCompareAirflowTest");
        add("CICDCompareJenkinsTest");
        add("CICDCompareTektonTest");
    }};

    private static LinkedList<String> xAxisLabelsList = new LinkedList<String>() {{
        add("50");
        add("100");
        add("150");
        add("200");
        add("250");
    }};

    private static LinkedList<String> runIds = new LinkedList<String>() {{
        add("a");
        add("b");
        add("c");
        add("d");
        add("e");
    }};

    @Test public void generateCICDComparePlot() throws IOException {
        CICDPlotData cicdPlotData = new CICDPlotData(testNames, runIds, xAxisLabelsList);

        new XYPlot(
            "cicdComparePlotTime.tex",
            cicdPlotData.getCombinedXAxisLabelsList(),
            cicdPlotData.getDataSeries(),
            "Liczba podów",
            PerformanceDefaults.GRAPH_Y_LABEL_CPU_TIME,
            null, null, "0", "400",
            new LinkedList<String>() {{
                add("Airflow");
                add("Jenkins");
                add("Tekton");
            }},
            xAxisLabelsList
        ).generatePlot();
    }

}
