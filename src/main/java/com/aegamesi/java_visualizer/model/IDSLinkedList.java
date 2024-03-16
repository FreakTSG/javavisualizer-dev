package com.aegamesi.java_visualizer.model;

import java.util.LinkedList;
import wrapper_classes.WrapperWithValue;

public class IDSLinkedList<T extends WrapperWithValue> extends HeapList {

    // Constructor that adapts an IDS linked list into the Java Visualizer's HeapList structure
    public IDSLinkedList(LinkedList<T> idsLinkedList) {
        super();

        // Convert the IDS linked list structure into the HeapList structure
        for (T item : idsLinkedList) {
            // Convert each item into a Value
            this.items.add(convertToValue(item.getValue()));
        }
    }

    // Helper method to create a Value from an Object based on your Value class definition
    private Value convertToValue(Object value) {
        Value v = new Value();
        // Determine the type of the value and set the corresponding Value properties
        if (value instanceof Integer || value instanceof Long) {
            v.type = Value.Type.LONG;
            v.longValue = ((Number) value).longValue(); // Auto-unboxing handles Integer to Long conversion
        } else if (value instanceof Double || value instanceof Float) {
            v.type = Value.Type.DOUBLE;
            v.doubleValue = ((Number) value).doubleValue(); // Auto-unboxing handles Float to Double conversion
        } else if (value instanceof Boolean) {
            v.type = Value.Type.BOOLEAN;
            v.booleanValue = (Boolean) value;
        } else if (value instanceof String) {
            v.type = Value.Type.STRING;
            v.stringValue = (String) value;
        } else if (value instanceof Character) {
            v.type = Value.Type.CHAR;
            v.charValue = (Character) value;
        } else if (value == null) {
            v.type = Value.Type.NULL;
        } else {
            // Handle other potential types or throw an error
            throw new IllegalArgumentException("Unsupported type for Value class");
        }
        return v;
    }
}
