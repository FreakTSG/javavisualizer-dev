package com.aegamesi.java_visualizer.ui.graphics.representations;

import java.lang.reflect.Field;

public class UnsortedFieldItem extends FieldItem {
    private static final long serialVersionUID = 1L;


    public UnsortedFieldItem() {
        this(null, null, null);
    }

    public UnsortedFieldItem(Class clazz, String prefix, Field field) {
        super(clazz, prefix, field);
    }
}

