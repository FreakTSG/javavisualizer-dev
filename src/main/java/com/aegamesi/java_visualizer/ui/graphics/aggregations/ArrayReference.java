package com.aegamesi.java_visualizer.ui.graphics.aggregations;

import com.aegamesi.java_visualizer.ui.graphics.OutConnector;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import java.awt.*;
import java.lang.reflect.Array;

public class ArrayReference extends Reference {
    private static final long serialVersionUID = 1L;
    protected Object arrayWrapper;
    protected int index;

    public ArrayReference(Object arrayWrapper, int index, Location outConnectorLocation, boolean manuallyAssigned) {
        this(new Point(), new Dimension(SIZE, SIZE), arrayWrapper, index, outConnectorLocation, manuallyAssigned);
    }

    public ArrayReference(Dimension dimension, Object arrayWrapper, int index, Location outConnectorLocation, boolean manuallyAssigned) {
        this(new Point(), dimension, arrayWrapper, index, outConnectorLocation, manuallyAssigned);
    }

    public ArrayReference(Point position, Dimension dimension, Object arrayWrapper, int index, Location outConnectorLocation, boolean manuallyAssigned) {
        super(position, dimension, arrayWrapper.getClass(), outConnectorLocation, manuallyAssigned);
        this.arrayWrapper = arrayWrapper;
        this.index = index;
        this.outConnector = new OutConnector(position, this, arrayWrapper.getClass().getComponentType());
        System.out.println("Created ArrayReference for index " + index + " pointing to " + getFieldValue());
    }

    @Override
    public Object getFieldValue() {
        return Array.get(arrayWrapper, index);
    }

    @Override
    public void setFieldValue(Object fieldValue) {
        Array.set(arrayWrapper, index, fieldValue);
    }

    @Override
    protected String getUnsetReferenceExpression() {
        return arrayWrapper.toString() + "[" + index + "] = null";
    }

    @Override
    public String getCompleteTypeName() {
        return arrayWrapper.getClass().getComponentType().getName();
    }

    public Object getArrayWrapper() {
        return arrayWrapper;
    }

    public int getIndex() {
        return index;
    }
}
