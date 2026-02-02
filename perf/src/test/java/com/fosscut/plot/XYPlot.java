package com.fosscut.plot;

import java.util.LinkedList;
import java.util.Map;

import com.fosscut.shared.util.save.SaveFile;
import com.fosscut.utils.PerformanceDefaults;

public class XYPlot {

    private String filePath;
    private LinkedList<Axis> axesList;

    public XYPlot(
        String filePath,
        LinkedList<LinkedList<String>> xAxisLabelsList,
        Map<String, Double> dataSeries,
        String xLabel,
        String yLabel
    ) {
        this.filePath = PerformanceDefaults.RESULTS_PLOT_PATH + filePath;
        this.axesList = new LinkedList<>();
        this.axesList.add(new Axis(xAxisLabelsList, dataSeries, xLabel, yLabel));
    }

    public XYPlot(
        String filePath,
        LinkedList<LinkedList<String>> xAxisLabelsList,
        Map<String, Double> dataSeries,
        String xLabel,
        String yLabel,
        String xMin,
        String xMax,
        String yMin,
        String yMax
    ) {
        this.filePath = PerformanceDefaults.RESULTS_PLOT_PATH + filePath;
        this.axesList = new LinkedList<>();
        this.axesList.add(new Axis(
            xAxisLabelsList, dataSeries, xLabel, yLabel,
            xMin, xMax, yMin, yMax
        ));
    }

    public XYPlot(
        String filePath,
        LinkedList<LinkedList<String>> xAxisLabelsList,
        Map<String, Double> dataSeries,
        String xLabel,
        String yLabel,
        String height,
        String xMin,
        String xMax,
        String yMin,
        String yMax
    ) {
        this.filePath = PerformanceDefaults.RESULTS_PLOT_PATH + filePath;
        this.axesList = new LinkedList<>();
        this.axesList.add(new Axis(
            xAxisLabelsList, dataSeries, xLabel, yLabel,
            height, xMin, xMax, yMin, yMax
        ));
    }

    public XYPlot(
        String filePath,
        LinkedList<LinkedList<String>> xAxisLabelsList,
        LinkedList<Map<String, Double>> dataSeries,
        String xLabel,
        String yLabel,
        String xMin,
        String xMax,
        String yMin,
        String yMax,
        LinkedList<String> legendEntries,
        LinkedList<String> xtickLabels
    ) {
        this.filePath = PerformanceDefaults.RESULTS_PLOT_PATH + filePath;
        this.axesList = new LinkedList<>();
        this.axesList.add(new Axis(
            xAxisLabelsList, dataSeries, xLabel, yLabel,
            xMin, xMax, yMin, yMax,
            legendEntries, xtickLabels
        ));
    }

    public XYPlot(
        String filePath,
        LinkedList<LinkedList<String>> xAxisLabelsList,
        LinkedList<Map<String, Double>> dataSeries,
        String xLabel,
        String yLabel,
        String height,
        String xMin,
        String xMax,
        String yMin,
        String yMax,
        LinkedList<String> legendEntries,
        LinkedList<String> xtickLabels
    ) {
        this.filePath = PerformanceDefaults.RESULTS_PLOT_PATH + filePath;
        this.axesList = new LinkedList<>();
        this.axesList.add(new Axis(
            xAxisLabelsList, dataSeries, xLabel, yLabel,
            height, xMin, xMax, yMin, yMax,
            legendEntries, xtickLabels
        ));
    }

    public XYPlot(
        String filePath,
        LinkedList<Axis> axesList
    ) {
        this.filePath = PerformanceDefaults.RESULTS_PLOT_PATH + filePath;
        this.axesList = axesList;
    }

    public void generatePlot() {
        StringBuilder tikzContent = new StringBuilder();
        tikzContent.append(startTikzPicture());
        for (int axisId = 0; axisId < axesList.size(); axisId++) {
            boolean includeAt = axesList.size() > 1; // include at only if more that one axis
            boolean includeXLabel = axisId == axesList.size() - 1; // last axis
            tikzContent.append(axesList.get(axisId).generateAxis(axisId, includeAt, includeXLabel));
        }
        tikzContent.append(endTikzPicture());
        SaveFile.saveContentToFile(tikzContent.toString(), filePath);
    }

    private String startTikzPicture() {
        return "\\begin{tikzpicture}\n";
    }

    private String endTikzPicture() {
        return "\\end{tikzpicture}%";
    }

}
