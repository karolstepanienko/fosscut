package com.fosscut;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class AbstractTest {

    protected static LinkedList<Integer> LinkedList_of(Integer... values) {
        LinkedList<Integer> list = new LinkedList<>();
        for (Integer value : values) {
            list.add(value);
        }
        return list;
    }

    // for creating a LinkedHashMap from a linked (preserves order) list of integers when keys are 1,2,...
    protected static LinkedHashMap<Integer, Integer> LinkedHashMapFromList_of(LinkedList<Integer> values) {
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        for (int i = 1; i <= values.size(); i += 1) {
            map.put(i, values.get(i - 1));
        }
        return map;
    }

    protected static LinkedHashMap<Integer, Integer> LinkedHashMap_of(Integer... values) {
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < values.length; i += 2) {
            map.put(values[i], values[i + 1]);
        }
        return map;
    }

    protected static LinkedHashMap<Integer, Integer> detectDuplicates(LinkedHashMap<Integer, Integer> map) {
        Set<Integer> seen = new HashSet<>();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            Integer value = entry.getValue();
            if (seen.contains(value)) {
                assert false : "Duplicate seed detected: " + value;
            } else {
                seen.add(value);
            }
        }
        return map;
    }

    protected static LinkedList<Integer> detectDuplicates(LinkedList<Integer> list) {
        Set<Integer> seen = new HashSet<>();
        for (Integer value : list) {
            if (seen.contains(value)) {
                assert false : "Duplicate seed detected: " + value;
            } else {
                seen.add(value);
            }
        }
        return list;
    }

    protected static LinkedList<LinkedList<String>> getCombinedXAxisLabelsList(LinkedList<String> xAxisLabelsList1, LinkedList<String> xAxisLabelsList2) {
        LinkedList<LinkedList<String>> combinedXAxisLabelsList = new LinkedList<>();
        combinedXAxisLabelsList.add(xAxisLabelsList1);
        combinedXAxisLabelsList.add(xAxisLabelsList2);
        return combinedXAxisLabelsList;
    }

    protected static LinkedList<LinkedList<String>> getCombinedXAxisLabelsList(LinkedList<String> xAxisLabelsList1, LinkedList<String> xAxisLabelsList2, LinkedList<String> xAxisLabelsList3) {
        LinkedList<LinkedList<String>> combinedXAxisLabelsList = new LinkedList<>();
        combinedXAxisLabelsList.add(xAxisLabelsList1);
        combinedXAxisLabelsList.add(xAxisLabelsList2);
        combinedXAxisLabelsList.add(xAxisLabelsList3);
        return combinedXAxisLabelsList;
    }

    protected static LinkedList<Map<String, Double>> getCombinedDataSeries(Map<String, Double> dataSeries1, Map<String, Double> dataSeries2) {
        LinkedList<Map<String, Double>> combinedDataSeries = new LinkedList<>();
        combinedDataSeries.add(dataSeries1);
        combinedDataSeries.add(dataSeries2);
        return combinedDataSeries;
    }

    protected static LinkedList<Map<String, Double>> getCombinedDataSeries(Map<String, Double> dataSeries1, Map<String, Double> dataSeries2, Map<String, Double> dataSeries3) {
        LinkedList<Map<String, Double>> combinedDataSeries = new LinkedList<>();
        combinedDataSeries.add(dataSeries1);
        combinedDataSeries.add(dataSeries2);
        combinedDataSeries.add(dataSeries3);
        return combinedDataSeries;
    }

}
