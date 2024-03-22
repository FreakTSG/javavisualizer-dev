package com.aegamesi.java_visualizer.ui.graphics.aggregations;

import java.awt.*;

public class SimpleNodeContainer extends ContainerWithPositionalInConnectors {
    private static final long serialVersionUID = 1L;


    public SimpleNodeContainer(Point position, Dimension dimension, Class type, boolean horizontal) {
        super(position, dimension, type, horizontal);
    }

    public SimpleNodeContainer(Point position, Dimension dimension, Class type) {
        this(position, dimension, type, false);
    }

    @Override
    public void computeInConnectors() {
        super.computeInConnectors();
    }
}
