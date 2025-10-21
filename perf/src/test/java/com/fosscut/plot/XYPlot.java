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
        String xMin
    ) {
        this.filePath = PerformanceDefaults.RESULTS_PLOT_PATH + filePath;
        this.xAxisLabels = xAxisLabels;
        this.dataSeries = dataSeries;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.xMin = xMin;
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
        options.append("width=\\textwidth,\n");
        options.append("height=8cm,\n");
        options.append("xmin=").append(calculateXMin()).append(",\n");
        options.append("xmax=").append(calculateXMax()).append(",\n");
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
        } else {
            return xAxisLabels.get(0);
        }
    }

    private String calculateXMax() {
        return xAxisLabels.get(xAxisLabels.size() - 1);
    }

    private Double calculateMinValue() {
        Double minValue = Double.MAX_VALUE;
        for (Double value : dataSeries.values()) {
            if (value < minValue) {
                minValue = value;
            }
        }
        return minValue;
    }

    private Double calculateMaxValue() {
        Double maxValue = Double.MIN_VALUE;
        for (Double value : dataSeries.values()) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    private String calculateYMin() {
        Double range = calculateMaxValue() - calculateMinValue();
        return String.valueOf(Math.floor(calculateMinValue() - 0.1 * range));
    }

    private String calculateYMax() {
        Double range = calculateMaxValue() - calculateMinValue();
        return String.valueOf(Math.ceil(calculateMaxValue() + 0.1 * range));
    }

    private String startPlot() {
        return "\\addplot";
    }

    private String getPlotOptions() {
        return "[color=black, line width=1.5pt] table[row sep=crcr]\n";
    }

    private String getPlotData() {
        StringBuilder plotData = new StringBuilder();

        for (String label : xAxisLabels) {
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
