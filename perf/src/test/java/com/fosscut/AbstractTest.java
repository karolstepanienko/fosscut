package com.fosscut;

import java.util.LinkedHashMap;

public class AbstractTest {

    protected static LinkedHashMap<Integer, Integer> LinkedHashMap_of(Integer... values) {
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < values.length; i += 2) {
            map.put(values[i], values[i + 1]);
        }
        return map;
    }

}
