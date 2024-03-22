package com.aegamesi.java_visualizer.ui.graphics;

import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;

import java.awt.*;

public class ThisConnection<TOutConnector extends OutConnector, TRepresentableObject extends RepresentationWithInConnectors> extends Connection<TOutConnector, TRepresentableObject> {
    private static final long serialVersionUID = 1L;


    public ThisConnection(TOutConnector source, TRepresentableObject target) {
        this(source, target, Color.BLUE);
    }

    public ThisConnection(TOutConnector source, TRepresentableObject target, Color borderColor) {
        super(source, target, borderColor);
    }

    @Override
    protected InConnector[] getTargetInConnectors() {
        return target.getLeftInConnectors();
    }

    protected void draw(Graphics g, Point centerPosition, Point targetPosition) {
        int delta = 15;
        g.drawLine(centerPosition.x, centerPosition.y, targetPosition.x - delta, centerPosition.y);
        g.drawLine(targetPosition.x - delta, centerPosition.y, targetPosition.x - delta, targetPosition.y);
        g.drawLine(targetPosition.x - delta, targetPosition.y, targetPosition.x, targetPosition.y);
        drawArrow((Graphics2D) g, targetPosition.x - delta, targetPosition.y, targetPosition.x, targetPosition.y);
    }

}
