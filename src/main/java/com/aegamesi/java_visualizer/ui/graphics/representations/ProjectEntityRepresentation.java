package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.model.HeapObject;
import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.PositionalGraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.StraightConnection;
import com.aegamesi.java_visualizer.ui.graphics.ThisConnection;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.*;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.Container;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;
import com.aegamesi.java_visualizer.utils.Utils;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public class ProjectEntityRepresentation<T> extends DefaultRepresentation<T> {
    private static final long serialVersionUID = 1L;
    private static final int MAX_RETRY_ATTEMPTS = 5;
    private static final int RETRY_DELAY_MS = 100;


    public ProjectEntityRepresentation(Point position, T owner, MyCanvas canvas) {
        this(position, owner, canvas, false);
    }

    public ProjectEntityRepresentation(Point position, T owner, MyCanvas canvas, boolean horizontal) {
        super(position, owner, canvas, horizontal);
    }

    @Override
    protected void createRepresentation(RepresentationWithInConnectors representableFieldValue, FieldReference fieldReference, boolean self) {
        if (representableFieldValue != null) {
            addNewConnection(self ? new ThisConnection(fieldReference.getOutConnector(), representableFieldValue) :
                    new StraightConnection(fieldReference.getOutConnector(), representableFieldValue));

        }
    }

    @Override
    public void update() {
        container.removeAllGraphicElements();
        final Object ownervalue = owner;
        final Class<?> valueClass = ownervalue.getClass();
        System.out.println(valueClass + " => número atributos: " + Utils.getAllFields(valueClass).size());

        for (Field field : Utils.getAllFields(valueClass)) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            final Object fieldValue = Utils.getFieldValue(ownervalue, field);
            System.out.println("Field: " + field);
            System.out.println("Field Name: " + field.getName() + ", Field Value: " + fieldValue);


            // Skip id and type fields
            if (field.getName().equals("id") || field.getName().equals("type")) {
                continue;
            }

            System.out.println("Contents of representationWithInConnectorsByOwner:");
            for (Map.Entry<Object, RepresentationWithInConnectors> entry : myCanvas.representationWithInConnectorsByOwner.entrySet()) {
                System.out.println("Key xd: " + entry.getKey() + ", Value: " + entry.getValue());
            }

            // Create FieldReference


            if (field.getName().equals("fields") && fieldValue instanceof Map) {
                Map<?, ?> fieldsMap = (Map<?, ?>) fieldValue;
                for (Map.Entry<?, ?> entry : fieldsMap.entrySet()) {
                    String key = String.valueOf(entry.getKey());
                    Object value = entry.getValue();
                    String displayText = key + ": " + value;
                    System.out.println("Key map: " + key + ", Value map: " + value);
                    System.out.println("Value class: " + (value != null ? value.getClass().getName() : "null"));
                    if (value != null && "REFERENCE".equals(getValueType(value))) {
                        FieldReference fieldReference = new FieldReference(owner, field, Location.CENTER, true);
                        aggregatesRectangularGraphicElementWithToolTip.add(fieldReference);
                        container.add(fieldReference, Location.LEFT);
                        long referenceId = getReferenceId(value);
                        System.out.println("Reference ID extracted: " + referenceId + " for key " + key);
                        HeapObject heapObject = myCanvas.getHeapObjectById(referenceId);
                        System.out.println("Heap Object for reference: " + heapObject + " for id " + referenceId);

                        if (heapObject != null) {
                            System.out.println("Fetching representation with retries for HeapObject: " + heapObject);
                            RepresentationWithInConnectors representableFieldValue = myCanvas.getRepresentationWithInConnectors(heapObject);
                            System.out.println("Representable Field Value for key '" + key + "': " + representableFieldValue);

                            if (representableFieldValue != null) {
                                System.out.println("Creating representation for reference: " + referenceId);
                                createRepresentation(representableFieldValue, fieldReference, representableFieldValue == this);
                            } else {
                                System.out.println("Representation not found for reference: " + referenceId);
                            }
                        } else {
                            System.out.println("HeapObject is null for referenceId: " + referenceId);
                        }
                    } else {
                        System.out.println("Value is not a reference: " + value);
                        NormalTextElement normalTextElement = new NormalTextElement(displayText, ConstantsIDS.FONT_SIZE_TEXT, key);
                        aggregatesRectangularGraphicElementWithToolTip.add(normalTextElement);
                        container.add(normalTextElement, Location.LEFT);
                    }
                }
            } else {
                // Handle non-map fields
                String displayText = String.valueOf(fieldValue);
                NormalTextElement normalTextElement = new NormalTextElement(displayText, ConstantsIDS.FONT_SIZE_TEXT, field.getName());
                aggregatesRectangularGraphicElementWithToolTip.add(normalTextElement);
                container.add(normalTextElement, Location.LEFT);
            }
        }
        System.out.println("Container: " + container);

        // Log positions of the elements for debugging
        for (Container.LocalizableAggregateGraphicElement element : container.getGraphicElements()) {
            AggregateRectangularGraphicElement aggregateElement = element.aggregateGraphicElement;
            System.out.println("Element: " + aggregateElement + " Position: " + aggregateElement.getPosition());
        }
    }

    private String getValueType(Object value) {
        try {
            Field typeField = value.getClass().getDeclaredField("type");
            typeField.setAccessible(true);
            Object type = typeField.get(value);
            return type != null ? type.toString() : null;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private long getReferenceId(Object value) {
        try {
            Field referenceField = value.getClass().getDeclaredField("reference");
            referenceField.setAccessible(true);
            Object reference = referenceField.get(value);
            return reference instanceof Long ? (Long) reference : 0L;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return 0L;
        }
    }




}