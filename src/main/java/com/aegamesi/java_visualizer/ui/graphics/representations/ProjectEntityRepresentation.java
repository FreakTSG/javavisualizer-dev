package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.PositionalGraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.StraightConnection;
import com.aegamesi.java_visualizer.ui.graphics.ThisConnection;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.AggregateRectangularGraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.FieldReference;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.NormalTextElement;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;
import com.aegamesi.java_visualizer.utils.Utils;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.Container;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public class ProjectEntityRepresentation<T> extends DefaultRepresentation<T> {
    private static final long serialVersionUID = 1L;


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
            final String fieldTypeSimpleName = field.getType().getSimpleName();
            System.out.println("Field Name: " + field.getName() + ", Field Value: " + fieldValue);

            // Exclude id and type fields
            if (field.getName().equals("id") || field.getName().equals("type")) {
                continue;
            }

            // Check if the field is the Map "fields"
            if (field.getName().equals("fields") && fieldValue instanceof Map) {
                Map<?, ?> fieldsMap = (Map<?, ?>) fieldValue;
                for (Map.Entry<?, ?> entry : fieldsMap.entrySet()) {
                    String key = String.valueOf(entry.getKey());
                    String value = String.valueOf(entry.getValue());
                    String displayText = key + ": " + value;
                    NormalTextElement normalTextElement = new NormalTextElement(displayText, ConstantsIDS.FONT_SIZE_TEXT, key);
                    aggregatesRectangularGraphicElementWithToolTip.add(normalTextElement);
                    container.add(normalTextElement, Location.LEFT);
                }
            } else {
                // Include the label field
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
}
