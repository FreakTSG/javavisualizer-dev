package com.aegamesi.java_visualizer.ui.graphics.aggregations;

import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.utils.Utils;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;

public class FieldReference extends Reference implements AggregateRectangularGraphicElementWithToolTip {
    private static final long serialVersionUID = 1L;


    private final ToolTipManager toolTipManager;
    protected Object fieldOwnerObject;
    protected transient Field field;

    public FieldReference(Object fieldOwnerObject, Field field, Location outConnectorLocation, boolean manuallyAssigned) {
        this(new Point(), new Dimension(SIZE, SIZE), fieldOwnerObject, field, outConnectorLocation, manuallyAssigned, false);
    }

    public FieldReference(Object fieldOwnerObject, Field field, Location outConnectorLocation, boolean manuallyAssigned, boolean showToolTipText) {
        this(new Point(), new Dimension(SIZE, SIZE), fieldOwnerObject, field, outConnectorLocation, manuallyAssigned, showToolTipText);
    }

    public FieldReference(Dimension dimension, Object fieldOwnerObject, Field field, Location outConnectorLocation, boolean manuallyAssigned) {
        this(new Point(), dimension, fieldOwnerObject, field, outConnectorLocation, manuallyAssigned, false);
    }

    public FieldReference(Dimension dimension, Object fieldOwnerObject, Field field, Location outConnectorLocation, boolean manuallyAssigned, boolean showToolTipText) {
        this(new Point(), dimension, fieldOwnerObject, field, outConnectorLocation, manuallyAssigned, showToolTipText);
    }

    public FieldReference(Point position, Dimension dimension, Object fieldOwnerObject, Field field, Location outConnectorLocation, boolean manuallyAssigned) {
        this(position, dimension, fieldOwnerObject, field, outConnectorLocation, manuallyAssigned, false);
    }

    public FieldReference(Point position, Dimension dimension, Object fieldOwnerObject, Field field, Location outConnectorLocation, boolean manuallyAssigned, boolean showToolTipText) {
        super(position, dimension, field != null ? field.getType() : Object.class, outConnectorLocation, manuallyAssigned);
        this.fieldOwnerObject = fieldOwnerObject;
        this.field = field;
        toolTipManager = new ToolTipManager();
        if (field == null) {
            System.out.println("FieldReference initialized with null field.");
        } else {
            System.out.println("FieldReference initialized with field: " + field.getName());
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(field.getName());
    }

    public Field getField() {
        return field;
    }

    @Override
    public Object getFieldValue() {
        return field != null ? Utils.getFieldValue(fieldOwnerObject, field) : null;
    }

    @Override
    public void setFieldValue(Object fieldValue) {
        if (field != null) {
            Utils.setFieldValue(fieldOwnerObject, field, fieldValue);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (toolTipManager.isShowToolTipText()) {
            drawToolTipText(field.getName(), g);
        }
    }

    public void setShowToolTipText(boolean showToolTipText) {
        toolTipManager.setShowToolTipText(showToolTipText);
    }

    @Override
    protected String getUnsetReferenceExpression() {
        return "";
    }

    @Override
    public String getCompleteTypeName() {
        return field != null ? field.getGenericType().toString() : "Unknown";
    }

    public Object getFieldOwnerObject() {
        return fieldOwnerObject;
    }


}
