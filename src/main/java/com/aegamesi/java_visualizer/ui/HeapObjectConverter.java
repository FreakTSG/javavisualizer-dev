package com.aegamesi.java_visualizer.ui;

import com.aegamesi.java_visualizer.model.HeapEntity;
import com.aegamesi.java_visualizer.model.HeapObject;

import java.util.Map;

public abstract class HeapObjectConverter<T> {
    protected Map<Long, HeapEntity> heapMap;

    public HeapObjectConverter(Map<Long, HeapEntity> heapMap) {
        this.heapMap = heapMap;
    }

    public abstract T convert(HeapObject heapObject);
}
