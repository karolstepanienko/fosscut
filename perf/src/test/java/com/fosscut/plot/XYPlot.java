package com.fosscut.plot;

import java.util.List;
import java.util.Map;

import com.fosscut.shared.util.save.SaveFile;
import com.fosscut.utils.PerformanceDefaults;

public class XYPlot {

    private String filePath;
    private List<String> xAxisLabels;
    private Map<String, Double> dataSeries;
    private String xMin;
    private String xMax;
    private String yMin;
    private String yMax;
    private String xLabel;
    private String yLabel;

    public XYPlot(
        String filePath,
        List<String> xAxisLabels,
        Map<String, Double> dataSeries,
        String xLabel,
        String yLabel
    ) {
        this.filePath = PerformanceDefaults.RESULTS_PLOT_PATH + filePath;
        this.xAxisLabels = xAxisLabels;
        this.dataSeries = dataSeries;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
    }

    public XYPlot(
        String filePath,
        List<String> xAxisLabels,
        Map<String, Double> dataSeries,
        String xLabel,
        String yLabel,
        String xMin,
        String xMax,
        String yMin,
        String yMax
    ) {
        this.filePath = PerformanceDefaults.RESULTS_PLOT_PATH + filePath;
        this.xAxisLabels = xAxisLabels;
        this.dataSeries = dataSeries;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public void generatePlot() {
        StringBuilder tikzContent = new StringBuilder();
        tikzContent.append(startTikzPicture());
        tikzContent.append(startAxis());
        tikzContent.append(getAxisOptions());
        tikzContent.append(startPlot());
        tikzContent.append(getPlotOptions());
        tikzContent.append(getPlotData());
        tikzContent.append(endAxis());
        tikzContent.append(endTikzPicture());
        SaveFile.saveContentToFile(tikzContent.toString(), filePath);
    }

    private String startTikzPicture() {
        return "\\begin{tikzpicture}\n";
    }

    private String startAxis() {
        return "\\begin{axis}\n";
    }

    private String getAxisOptions() {
        StringBuilder options = new StringBuilder();
        options.append("[%\n");
        options.append("width=0.98\\textwidth,\n");
        options.append("height=7cm,\n");
        options.append("grid=both,\n");
        options.append("xtick={").append(calculateXTicks()).append("},\n");
        options.append("ymin=").append(calculateYMin()).append(",\n");
        options.append("ymax=").append(calculateYMax()).append(",\n");
        options.append("xlabel style={font=\\color{white!15!black}},\n");
        options.append("xlabel={").append(xLabel).append("},\n");
        options.append("ylabel style={font=\\color{white!15!black}},\n");
        options.append("ylabel={").append(yLabel).append("},\n");
        options.append("ticklabel style={font=\\small},x label style={font=\\small},y label style={font=\\small}\n");
        options.append("]\n");
        return options.toString();
    }

    private String calculateXMin() {
        if (xMin != null) {
            return xMin;
        }
        return xAxisLabels.get(0);
    }

    private String calculateXMax() {
        if (xMax != null) {
            return xMax;
        }
        return xAxisLabels.get(xAxisLabels.size() - 1);
    }

    private String calculateXTicks() {
        StringBuilder xTicks = new StringBuilder();
        for (String label : getFilteredXAxisLabels()) {
            xTicks.append(label).append(", ");
        }
        // Remove last comma and space
        if (xTicks.length() > 2) {
            xTicks.setLength(xTicks.length() - 2);
        }
        return xTicks.toString();
    }

    private List<String> getFilteredXAxisLabels() {
        if (xMin == null && xMax == null) {
            return xAxisLabels;
        } else if (xMin != null && xMax == null) {
            return xAxisLabels.stream()
                .filter(label -> Double.parseDouble(label) >= Double.parseDouble(xMin))
                .toList();
        } else if (xMin == null) { // xMax != null
            return xAxisLabels.stream()
                .filter(label -> Double.parseDouble(label) <= Double.parseDouble(xMax))
                .toList();
        } else {
            return xAxisLabels.stream()
                .filter(label -> Double.parseDouble(label) >= Double.parseDouble(xMin))
                .filter(label -> Double.parseDouble(label) <= Double.parseDouble(xMax))
                .toList();
        }
    }

    private Double calculateMinValue() {
        Double minValue = Double.MAX_VALUE;
        for (Map.Entry<String, Double> entry : dataSeries.entrySet()) {
            if (this.xMin == null || (this.xMin != null && Double.parseDouble(entry.getKey()) >= Double.parseDouble(this.xMin))) {
                if (entry.getValue() < minValue) {
                    minValue = entry.getValue();
                }
            }
        }
        return minValue;
    }

    private Double calculateMaxValue() {
        Double maxValue = Double.MIN_VALUE;
        for (Map.Entry<String, Double> entry : dataSeries.entrySet()) {
            if (this.xMin == null || (this.xMin != null && Double.parseDouble(entry.getKey()) >= Double.parseDouble(this.xMin))) {
                if (entry.getValue() > maxValue) {
                    maxValue = entry.getValue();
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

    private String startPlot() {
        return "\\addplot";
    }

    private String getPlotOptions() {
        return "[color=black, line width=1.5pt] table[row sep=crcr]\n";
    }

    private String getPlotData() {
        StringBuilder plotData = new StringBuilder();

        for (String label : getFilteredXAxisLabels()) {
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

    private String endTikzPicture() {
        return "\\end{tikzpicture}%";
    }

}
