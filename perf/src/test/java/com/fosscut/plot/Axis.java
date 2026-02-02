package com.fosscut.plot;

import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class Axis {

    private LinkedList<LinkedList<String>> xAxisLabelsList;
    private LinkedList<Map<String, Double>> dataSeries;
    private String height;
    private String xMin;
    private String xMax;
    private String yMin;
    private String yMax;
    private String xLabel;
    private String yLabel;
    private LinkedList<String> legendEntries;
    private LinkedList<String> xtickLabels;

    private static final LinkedList<String> LINE_SPEC = new LinkedList<>() {{
        add("color=black, line width=1.5pt");
        add("color=black, dashed, line width=1.5pt");
        add("color=black, densely dotted, line width=1.5pt");
    }};

    private static final LinkedList<String> AT_STRINGS = new LinkedList<>() {{
        add("at={(0,0)},\n");
        add("at={(0,-6.5cm)},\n");
        add("at={(0,-13cm)},\n");
    }};

    public Axis(
        LinkedList<LinkedList<String>> xAxisLabelsList,
        Map<String, Double> dataSeries,
        String xLabel,
        String yLabel
    ) {
        this.xAxisLabelsList = xAxisLabelsList;
        this.dataSeries = new LinkedList<Map<String, Double>>() {{
            add(dataSeries);
        }};
        this.xLabel = xLabel;
        this.yLabel = yLabel;
    }

    public Axis(
        LinkedList<LinkedList<String>> xAxisLabelsList,
        Map<String, Double> dataSeries,
        String xLabel,
        String yLabel,
        String xMin,
        String xMax,
        String yMin,
        String yMax
    ) {
        this.xAxisLabelsList = xAxisLabelsList;
        this.dataSeries = new LinkedList<Map<String, Double>>() {{
            add(dataSeries);
        }};
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public Axis(
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
        this.xAxisLabelsList = xAxisLabelsList;
        this.dataSeries = new LinkedList<Map<String, Double>>() {{
            add(dataSeries);
        }};
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.height = height;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public Axis(
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
        this.xAxisLabelsList = xAxisLabelsList;
        this.dataSeries = dataSeries;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.legendEntries = legendEntries;
        this.xtickLabels = xtickLabels;
    }

    public Axis(
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
        this.xAxisLabelsList = xAxisLabelsList;
        this.dataSeries = dataSeries;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.height = height;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.legendEntries = legendEntries;
        this.xtickLabels = xtickLabels;
    }

    public String generateAxis(Integer axisId, boolean includeAt, boolean includeXLabel) {
        StringBuilder tikzContent = new StringBuilder();
        tikzContent.append(startAxis());
        tikzContent.append(getAxisOptions(axisId, includeAt, includeXLabel));
        tikzContent.append(getPlots());
        tikzContent.append(endAxis());
        return tikzContent.toString();
    }

    private String startAxis() {
        return "\\begin{axis}\n";
    }

    private String getAxisOptions(Integer axisId, boolean includeAt, boolean includeXLabel) {
        StringBuilder options = new StringBuilder();
        options.append("[%\n");
        options.append(includeAt ? AT_STRINGS.get(axisId) : "");
        options.append("width=0.98\\textwidth,\n");
        options.append("height=").append(getHeight()).append(",\n");
        options.append("grid=both,\n");
        options.append("xtick={").append(calculateXTicks()).append("},\n");
        options.append(getXtickLabelsString());
        options.append("ymin=").append(calculateYMin()).append(",\n");
        options.append("ymax=").append(calculateYMax()).append(",\n");
        options.append("xlabel style={font=\\color{white!15!black}},\n");
        if (includeXLabel) options.append("xlabel={").append(xLabel).append("},\n");
        options.append("ylabel style={font=\\color{white!15!black}},\n");
        options.append("ylabel={").append(yLabel).append("},\n");
        options.append(getLegendPosString());
        options.append("ticklabel style={font=\\small},x label style={font=\\small},y label style={font=\\small}\n");
        options.append("]\n");
        return options.toString();
    }

    private String getHeight() {
        if (height == null) {
            return "7cm";
        }
        return height;
    }

    private String calculateXTicks() {
        StringBuilder xTicks = new StringBuilder();
        for (String label : getFilteredXAxisLabels(getLongestXAxisLabels())) {
            xTicks.append(label).append(", ");
        }
        // Remove last comma and space
        if (xTicks.length() > 2) {
            xTicks.setLength(xTicks.length() - 2);
        }
        return xTicks.toString();
    }

    private LinkedList<String> getLongestXAxisLabels() {
        LinkedList<String> longestXAxisLabels = new LinkedList<>();

        for (LinkedList<String> labels : xAxisLabelsList) {
            if (labels.size() > longestXAxisLabels.size()) {
                longestXAxisLabels = labels;
            }
        }

        return longestXAxisLabels;
    }

    private LinkedList<String> getFilteredXAxisLabels(LinkedList<String> xAxisLabels) {
        if (xMin == null && xMax == null) {
            return xAxisLabels;
        } else if (xMin != null && xMax == null) {
            return new LinkedList<>(xAxisLabels.stream()
                .filter(label -> Double.parseDouble(label) >= Double.parseDouble(xMin))
                .toList());
        } else if (xMin == null) { // xMax != null
            return new LinkedList<>(xAxisLabels.stream()
                .filter(label -> Double.parseDouble(label) <= Double.parseDouble(xMax))
                .toList());
        } else {
            return new LinkedList<>(xAxisLabels.stream()
                .filter(label -> Double.parseDouble(label) >= Double.parseDouble(xMin))
                .filter(label -> Double.parseDouble(label) <= Double.parseDouble(xMax))
                .toList());
        }
    }

    private String getXtickLabelsString() {
        if (xtickLabels == null) {
            return "";
        }
        StringBuilder labels = new StringBuilder();
        labels.append("xticklabels={");
        for (String label : xtickLabels) {
            labels.append(label).append(", ");
        }
        // Remove last comma and space
        if (labels.length() > 2) {
            labels.setLength(labels.length() - 2);
        }
        labels.append("},\n");
        return labels.toString();
    }

    private String getLegendPosString() {
        if (legendEntries == null) {
            return "";
        }
        StringBuilder legend = new StringBuilder();
        legend.append("legend pos=north west,\n");
        return legend.toString();
    }

    private Double calculateMinValue() {
        Double minValue = Double.MAX_VALUE;
        for (Map<String, Double> series : dataSeries) {
            for (Map.Entry<String, Double> entry : series.entrySet()) {
                if (this.xMin == null || (this.xMin != null && Double.parseDouble(entry.getKey()) >= Double.parseDouble(this.xMin))) {
                    if (entry.getValue() < minValue) {
                        minValue = entry.getValue();
                    }
                }
            }
        }
        return minValue;
    }

    private Double calculateMaxValue() {
        Double maxValue = Double.MIN_VALUE;
        for (Map<String, Double> series : dataSeries) {
            for (Map.Entry<String, Double> entry : series.entrySet()) {
                if (this.xMin == null || (this.xMin != null && Double.parseDouble(entry.getKey()) >= Double.parseDouble(this.xMin))) {
                    if (entry.getValue() > maxValue) {
                        maxValue = entry.getValue();
                    }
                }
            }
        }
        return maxValue;
    }

    private String calculateYMin() {
        if (yMin != null) {
            return yMin;
        }
        Double range = calculateMaxValue() - calculateMinValue();
        int newYMin = (int) Math.floor(calculateMinValue() - 0.1 * range);
        if (isOdd(newYMin)) newYMin--;
        return String.valueOf(newYMin);
    }

    private String calculateYMax() {
        if (yMax != null) {
            return yMax;
        }
        Double range = calculateMaxValue() - calculateMinValue();
        int newYMax = (int) Math.floor(calculateMaxValue() + 0.1 * range);
        if (isOdd(newYMax)) newYMax++;
        return String.valueOf(newYMax);
    }

    private boolean isOdd(int n) {
        return n % 2 == 1;
    }

    private double calculateStandardDeviation(LinkedList<String> xAxisLabels, Map<String, Double> dataSeries) {
        DescriptiveStatistics stats = new DescriptiveStatistics();

        for (String xAxisLabel : xAxisLabels) {
            Double value = dataSeries.get(xAxisLabel);
            if (value != null) {
                stats.addValue(value);
            }
        }

        return stats.getStandardDeviation();
    }

    private String getPlots() {
        String plot = "";
        for (int i = 0; i < dataSeries.size(); i++) {
            plot += "\\addplot";
            plot += "[" + LINE_SPEC.get(i % LINE_SPEC.size()) + "] table[row sep=crcr]\n";
            plot += getPlotData(xAxisLabelsList.get(i), dataSeries.get(i));
            plot += "% Odchylenie standardowe, Standard deviation: ";
            plot += calculateStandardDeviation(xAxisLabelsList.get(i), dataSeries.get(i)) + "\n";
            if (legendEntries != null && legendEntries.size() > i) {
                plot += "\\addlegendentry{" + legendEntries.get(i) + "}\n";
            }
        }
        return plot;
    }

    private String getPlotData(LinkedList<String> xAxisLabels, Map<String, Double> dataSeries) {
        StringBuilder plotData = new StringBuilder();

        for (String label : getFilteredXAxisLabels(xAxisLabels)) {
            Double value = dataSeries.get(label);
            plotData.append(label).append(" ").append(value).append(" \\\\\n");
        }

        return "{%\n" +
            plotData.toString() +
        "};\n";
    }

    private String endAxis() {
        return "\\end{axis}\n";
    }

}
