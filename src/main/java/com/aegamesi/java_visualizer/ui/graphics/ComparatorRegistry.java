package com.aegamesi.java_visualizer.ui.graphics;
import com.aegamesi.java_visualizer.aed.Comparacao;

import java.util.HashMap;
import java.util.Map;

public class ComparatorRegistry {
    private static final Map<String, Class<? extends Comparacao<?>>> comparatorMap = new HashMap<>();

    public static void addComparator(String name, Class<? extends Comparacao<?>> comparatorClass) {
        comparatorMap.put(name, comparatorClass);
    }

    public static Class<? extends Comparacao<?>> getComparatorClass(String name) {
        return comparatorMap.get(name);
    }
}
