package com.aegamesi.java_visualizer.ui.graphics;

import java.awt.*;

public class InConnector extends Connector {
    private static final long serialVersionUID = 1L;

    public static final int WIDTH = 1;
    public static final int HEIGHT = 1;

    public InConnector(Point position, Class type) {
        super(position, new Dimension(WIDTH, HEIGHT), type);
    }

}
