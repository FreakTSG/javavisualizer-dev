package com.aegamesi.java_visualizer.ui.graphics;

import java.awt.*;

public class LinearGraphicElement extends PositionalGraphicElement {
    private static final long serialVersionUID = 1L;

    protected Point oppositePosition;

    public LinearGraphicElement(Point position, Point oppositePosition) {
        this(position, oppositePosition, Color.BLACK);
    }

    public LinearGraphicElement(Point position, Point oppositePosition, Color borderColor) {
        super(position, borderColor);
        this.oppositePosition = oppositePosition;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        final Color color = g.getColor();
        g.setColor(borderColor);
        g.drawLine(position.x, position.y, oppositePosition.x, oppositePosition.y);
        g.setColor(color);
    }

    public Point getOppositePosition() {
        return oppositePosition;
    }

    public void setOppositePosition(Point oppositePosition) {
        this.oppositePosition = oppositePosition;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

}
