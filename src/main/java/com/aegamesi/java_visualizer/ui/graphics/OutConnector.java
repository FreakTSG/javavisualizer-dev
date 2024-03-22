package com.aegamesi.java_visualizer.ui.graphics;

import com.aegamesi.java_visualizer.ui.graphics.aggregations.Reference;

import java.awt.*;

public class OutConnector extends Connector {
    private static final long serialVersionUID = 1L;

    public static final int WIDTH = 4;
    public static final int HEIGHT = 4;

    protected Reference reference;

    public OutConnector(Point position, Reference reference, Class type) {
        super(position, new Dimension(WIDTH, HEIGHT), type);
        this.reference = reference;
    }

    public Reference getReference() {
        return reference;
    }

    public String getCompleteTypeName() {
//        return  FieldReference ? ((FieldReference) reference).getFieldCompleteTypeName() : ((ArrayReference) reference).getCompleteTypeName();
        return reference.getCompleteTypeName();
    }
}
