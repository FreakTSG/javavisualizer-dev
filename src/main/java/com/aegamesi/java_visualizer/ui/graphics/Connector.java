package com.aegamesi.java_visualizer.ui.graphics;

import java.awt.*;

abstract public class Connector extends RectangularGraphicElement {
    private static final long serialVersionUID = 1L;


    protected Class type;

    public Connector(Point position, Dimension dimension, Class type) {
        super(position, dimension);
        this.type = type;
    }

    public Class getType() {
        return type;
    }

    public Point getCenterPosition() {
        Point position = new Point(getPosition());
        position.translate(dimension.width / 2, dimension.height / 2);
        return position;
    }
}
