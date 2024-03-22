package com.aegamesi.java_visualizer.ui.graphics.representations;

import java.lang.reflect.Field;

public class SortedFieldItem extends FieldItem {
    private static final long serialVersionUID = 1L;


    protected boolean ascending;

    public SortedFieldItem() {
        this(null, null, null);
    }

    public SortedFieldItem(Class clazz, String prefix, Field field) {
        super(clazz, prefix, field);
        this.ascending = true;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void reverseOrder() {
        ascending = !ascending;
    }

}

