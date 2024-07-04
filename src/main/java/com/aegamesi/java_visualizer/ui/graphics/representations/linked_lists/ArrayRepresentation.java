package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists;

import com.aegamesi.java_visualizer.model.HeapObject;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.*;
import com.aegamesi.java_visualizer.ui.graphics.representations.GeneralRepresentation;

import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.IDSToolWindow;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.*;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.ui.graphics.representations.Representation;
import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;
import com.aegamesi.java_visualizer.utils.Utils;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.Container;
import com.github.weisj.jsvg.S;


import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Map;

public class ArrayRepresentation extends GeneralRepresentation<Object> {
    private static final long serialVersionUID = 1L;
    private String componentTypeName;

    public ArrayRepresentation(Object owner, String componentTypeName, MyCanvas canvas) {
        super(new Point(), owner, canvas, true);
        this.componentTypeName = componentTypeName;

    }

    public ArrayRepresentation(Point position, Object owner, String componentTypeName, MyCanvas canvas) {
        super(position, owner, canvas, true);
        this.componentTypeName = componentTypeName;
    }

    public void init() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                initializeInBackground();
                return null;
            }

            @Override
            protected void done() {
                myCanvas.repaint();
            }
        };
        worker.execute();
    }

    private void initializeInBackground() {
        container.setTopCellSpacing(0);
        container.setLeftCellSpacing(0);
        container.setInnerCellSpacing(0);
        container.setHorizontal(false);
        container.setTopCellSpacing(4);

        System.out.println("owner = " + owner);
        System.out.println("Array.getLength(owner) = " + Array.getLength(owner));

        if (isArrayOfPrimitivesOrEnums(owner)) {
            for (int i = 0; i < Array.getLength(owner); i++) {
                Object element = Array.get(owner, i);
                String elementValue = (element != null) ? element.toString() : "null";
                System.out.println("Adding primitive/enum element at index " + i + ": " + elementValue);
                container.add(createAggregateGraphicElementWithIndex(new NormalTextElement(elementValue, ConstantsIDS.FONT_SIZE_TEXT), i), Location.LEFT);
            }
        } else {
            for (int i = 0; i < Array.getLength(owner); i++) {
                Reference reference = new ArrayReference(owner, i, Location.CENTER, true);
                System.out.println("Adding reference to object at index " + i + ": " + owner);
                container.add(createAggregateGraphicElementWithIndex(reference, i), Location.RIGHT);

                Object targetObject = Array.get(owner, i);

                System.out.println("Iterating over representationWithInConnectorsByOwner:");
                for (Map.Entry<Object, RepresentationWithInConnectors> entry : myCanvas.representationWithInConnectorsByOwner.entrySet()) {
                    System.out.println("Entry: " + entry);
                    Object storedKey = entry.getKey();
                    RepresentationWithInConnectors storedRepresentation = entry.getValue();
                    System.out.println("Stored Key (identityHashCode: " + System.identityHashCode(storedKey) + ") -> Representation: " + storedRepresentation + " for entry: " + entry);
                }

                System.out.println("Target object: " + targetObject);
                System.out.println("RepresentationWithInConnectors for target object: " + myCanvas.getRepresentationForHeapObject((HeapObject) targetObject));
                RepresentationWithInConnectors<?> targetRepresentation = myCanvas.wrapObjectIfNeeded(targetObject);
                if (targetRepresentation != null) {
                    System.out.println("Wrapped target object: " + targetObject);
                    //myCanvas.add(targetObject, targetRepresentation);
                    add(reference.getOutConnector(), myCanvas.getRepresentationForHeapObject((HeapObject) targetObject));
                } else {
                    System.err.println("Could not wrap target object: " + targetObject);
                }
            }
        }
        System.out.println("Initialized ArrayRepresentation with " + container + " elements.");
    }

    private boolean isArrayOfPrimitivesOrEnums(Object array) {
        Class<?> componentType = array.getClass().getComponentType();
        return componentType.isPrimitive() || componentType.isEnum() || isWrapperType(componentType);
    }

    private boolean isWrapperType(Class<?> clazz) {
        return clazz == Boolean.class || clazz == Character.class || clazz == Byte.class ||
                clazz == Short.class || clazz == Integer.class || clazz == Long.class ||
                clazz == Float.class || clazz == Double.class || clazz == Void.class || clazz == String.class;
    }

    public void update() {
        container.removeAllGraphicElements();
        init();
        myCanvas.repaint();
    }

    public String getComponentTypeName() {
        return componentTypeName + "[]";
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Point position = new Point(getPosition());
        position.translate(0, -3);
        Color oldColor = g.getColor();
        g.setColor(Color.BLACK);
        g.drawString(Utils.getClassSimpleName(getComponentTypeName()), position.x, position.y);
        g.setColor(oldColor);
    }

    private Container createAggregateGraphicElementWithIndex(AggregateRectangularGraphicElement aggregateGraphicElement, int index) {
        Container containerWithoutInConnectors = new Container(new Point(), new Dimension(), true);
        containerWithoutInConnectors.setInnerCellSpacing(0);
        containerWithoutInConnectors.setTopCellSpacing(0);
        containerWithoutInConnectors.setBottomCellSpacing(0);
        containerWithoutInConnectors.setBorderShown(false);
        NormalTextElement indexNormalTextElement = new NormalTextElement(String.valueOf(index), ConstantsIDS.FONT_SIZE_INDEX);
        indexNormalTextElement.setBackgroundColor(GraphicElement.TRANSPARENT_COLOR);
        indexNormalTextElement.setBorderColor(GraphicElement.TRANSPARENT_COLOR);
        containerWithoutInConnectors.add(indexNormalTextElement);
        containerWithoutInConnectors.add(aggregateGraphicElement);
        return containerWithoutInConnectors;
    }

    public void add(OutConnector outConnector, RepresentationWithInConnectors<?> targetRepresentation) {
        System.out.println("Entering add method");

        System.out.println("OutConnector: " + outConnector);

        // Use the first InConnector for this example
        InConnector inConnector = targetRepresentation.getFirstInConnector();
        if (inConnector != null) {
            System.out.println("InConnector found: " + inConnector);

            // Add the connection using the correct target type
            myCanvas.add(new StraightConnection<>(outConnector, targetRepresentation));
            outConnector.getReference().setFieldValue(targetRepresentation.getOwner());
            System.out.println("Added connection from " + outConnector + " to " + targetRepresentation);
        } else {
            System.err.println("No InConnector found for targetRepresentation.");
        }

        System.out.println("Exiting add method");
    }
}
