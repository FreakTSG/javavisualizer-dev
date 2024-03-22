package com.aegamesi.java_visualizer.ui;

import java.util.Stack;

public class StackWithLimitedCapacity<T> extends Stack<T> {
    private static final long serialVersionUID = 1L;
    private final int capacity;

    public StackWithLimitedCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public T push(T elem) {
        super.push(elem);
        if (elementCount > capacity) {
            remove(0);
        }
        return elem;
    }
}
