package com.fosscut;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class AbstractTest {

    protected static LinkedList<Integer> LinkedList_of(Integer... values) {
        LinkedList<Integer> list = new LinkedList<>();
        for (Integer value : values) {
            list.add(value);
        }
        return list;
    }

    // for creating a LinkedHashMap from a linked (preserves order) list of integers when keys are 0,1,2,...
    protected static LinkedHashMap<Integer, Integer> LinkedHashMap_of(LinkedList<Integer> values) {
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < values.size(); i += 1) {
            map.put(i, values.get(i));
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

}
