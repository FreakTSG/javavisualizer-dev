package com.aegamesi.java_visualizer.ui.graphics;

import java.awt.*;
import java.io.Serializable;

abstract public class GraphicElement implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
    protected Color borderColor;

    public GraphicElement(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public void paint(Graphics g) {
    }

}
