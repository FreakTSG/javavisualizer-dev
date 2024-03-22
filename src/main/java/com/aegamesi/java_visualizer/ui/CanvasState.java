package com.aegamesi.java_visualizer.ui;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class CanvasState implements Serializable {
    private final String description;
    private final ByteArrayOutputStream byteArrayOutputStream;

    public CanvasState(String description, ByteArrayOutputStream byteArrayOutputStream) {
        this.description = description;
        this.byteArrayOutputStream = byteArrayOutputStream;
    }

    public String getDescription() {
        return description;
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }
}
