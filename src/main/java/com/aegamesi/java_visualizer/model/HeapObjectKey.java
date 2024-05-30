package com.aegamesi.java_visualizer.model;import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class HeapObjectKey {
    private final String label;
    private final Map<String, Object> fields;

    public HeapObjectKey(HeapObject obj) {
        this.label = obj.label;
        this.fields = generateFieldsMap(obj);
    }

    private Map<String, Object> generateFieldsMap(HeapObject obj) {
        Map<String, Object> fieldsMap = new TreeMap<>();
        for (Map.Entry<String, Value> entry : obj.fields.entrySet()) {
            fieldsMap.put(entry.getKey(), entry.getValue().getActualValue());
        }
        return fieldsMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeapObjectKey that = (HeapObjectKey) o;
        return Objects.equals(label, that.label) && Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, fields);
    }

    @Override
    public String toString() {
        return "HeapObjectKey{" +
                "label='" + label + '\'' +
                ", fields=" + fields +
                '}';
    }
}