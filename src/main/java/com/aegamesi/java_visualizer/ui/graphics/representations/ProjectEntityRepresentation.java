package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.StraightConnection;
import com.aegamesi.java_visualizer.ui.graphics.ThisConnection;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.FieldReference;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;

import java.awt.*;
import java.lang.reflect.Field;

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

    //@Override
    //public void update() {
    //    container.removeAllGraphicElements();
    //    Field[] fields = owner.getClass().getDeclaredFields();
    //    for (Field field : fields) {
    //        field.setAccessible(true);
    //        try {
    //            Object value = field.get(owner);
    //            FieldReference fieldReference = new FieldReference(owner, field, Location.LEFT, false);
    //            container.add(fieldReference, Location.LEFT);
    //        } catch (IllegalAccessException e) {
    //            e.printStackTrace();
    //        }
    //    }
    //}



}
