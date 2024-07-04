package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.StraightConnection;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.FieldReference;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VariableRepresentation extends RepresentationWithoutInConnectors<Object> {
    private static final long serialVersionUID = 1L;


    private FieldReference fieldReference;


    //se esta é uma representação que pode existir sem owner (numa dada altura)
    //então deve guardar o tipo de objeto que pode referenciar
    public VariableRepresentation(Point position,Object  owner, MyCanvas canvas) {
        super(position, owner, canvas);
    }

    public void init() {
        // Adjust this to correctly reference the field in your object
        fieldReference = new FieldReference(owner, Utils.getField(owner, "value"), Location.CENTER, false, false);
        container.add(fieldReference);
        container.setBorderShown(false);
        container.setOutterCellSpacing(0);
    }



    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Point position = new Point(getPosition());
        position.translate(0, -3);
        g.drawString("ref. " + Utils.getClassSimpleName(owner.getClass().getTypeName()), position.x, position.y);
        position.translate(0, -13);
        g.drawString(owner.getClass().getName(), position.x, position.y);
    }







}

