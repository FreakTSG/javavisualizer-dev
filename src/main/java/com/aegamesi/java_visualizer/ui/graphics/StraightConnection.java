package com.aegamesi.java_visualizer.ui.graphics;

import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;

import java.awt.*;

public class StraightConnection<TOutConnector extends OutConnector, TRepresentationObject extends RepresentationWithInConnectors<?>> extends Connection<TOutConnector, TRepresentationObject> {
    private static final long serialVersionUID = 1L;


    public StraightConnection(TOutConnector source, TRepresentationObject target) {
        this(source, target, Color.BLUE);
    }

    public StraightConnection(TOutConnector source, TRepresentationObject target, Color borderColor) {
        super(source, target, borderColor);
    }

    @Override
    protected void draw(Graphics g, Point centerPosition, Point targetPosition) {
        g.drawLine(centerPosition.x, centerPosition.y, targetPosition.x, targetPosition.y);
        drawArrow((Graphics2D) g, centerPosition.x, centerPosition.y, targetPosition.x, targetPosition.y);
    }


}
