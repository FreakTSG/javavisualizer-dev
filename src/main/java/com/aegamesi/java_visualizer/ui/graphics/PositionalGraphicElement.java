package com.aegamesi.java_visualizer.ui.graphics;

import java.awt.*;

public class PositionalGraphicElement extends GraphicElement {
    private static final long serialVersionUID = 1L;

    protected Point position;

    public PositionalGraphicElement(Point position, Color borderColor) {
        super(borderColor);
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = new Point(position);
    }

    public PositionalGraphicElement getPositionalGraphicElement(Point position) {
        return this.position.equals(position) ? this : null;
    }

    public boolean contains(Point position) {
        return getPositionalGraphicElement(position) != null;
    }
}
