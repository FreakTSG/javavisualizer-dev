package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.Connection;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.FieldReference;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.NormalTextElement;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.utils.Utils;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public abstract class DefaultRepresentation<T extends Object> extends GeneralGenericWithToolTipRepresentation<T> {
    private static final long serialVersionUID = 1L;

    protected ArrayList<Connection> connections;

    public DefaultRepresentation(Point position, T owner, MyCanvas myCanvas) {
        this(position, owner, myCanvas, false);
    }

    public DefaultRepresentation(Point position, T owner, MyCanvas myCanvas, boolean horizontal) {
        super(position, owner, myCanvas, horizontal);
    }

    public void init() {
        connections = new ArrayList<>();
    }

    public void rebuild() {
        for (Connection connection : connections) {
            myCanvas.remove(connection);
        }
        connections.clear();
        update();
        myCanvas.repaint();
    }

    public void update() {
        container.removeAllGraphicElements();
        final Object ownervalue = owner;
        final Class<?> valueClass = ownervalue.getClass();
       System.out.println(valueClass + " => número atributos: " + Utils.getAllFields(valueClass).size());
        for (Field field : Utils.getAllFields(valueClass)) {
            System.out.println("Quais sao os valores "+field);
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            final Object fieldValue = Utils.getFieldValue(ownervalue, field);
            final String fieldTypeSimpleName = field.getType().getSimpleName();
            if (Utils.isPrimitiveOrPrimitiveWrapperType(fieldTypeSimpleName) || Utils.isACompactReferenceableType(fieldTypeSimpleName) || field.getType().isEnum()) {
                NormalTextElement normalTextElement = new NormalTextElement(String.valueOf(fieldValue), ConstantsIDS.FONT_SIZE_TEXT, field.getName());
                aggregatesRectangularGraphicElementWithToolTip.add(normalTextElement);
                container.add(normalTextElement, Location.LEFT);
            } else {
                FieldReference fieldReference = new FieldReference(owner, field, Location.CENTER, true);
                aggregatesRectangularGraphicElementWithToolTip.add(fieldReference);
                container.add(fieldReference, Location.LEFT);
                if (fieldValue != null) {
                    final RepresentationWithInConnectors representableFieldValue = myCanvas.getRepresentationWithInConnectors(myCanvas.getRepresentationWithInConnectors(fieldValue));
                    createRepresentation(representableFieldValue, fieldReference, representableFieldValue == this);
                }
            }
        }
    }

    public void addNewConnection(Connection connection) {
        connections.add(connection);
        myCanvas.add(connection);
    }

    protected abstract void createRepresentation(RepresentationWithInConnectors representableFieldValue, FieldReference fieldReference, boolean self);

    public String getCreationCode() {
        return Utils.getCreationCode(owner);
    }

}
