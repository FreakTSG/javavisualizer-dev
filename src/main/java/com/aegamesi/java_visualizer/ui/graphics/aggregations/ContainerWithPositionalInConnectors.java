package com.aegamesi.java_visualizer.ui.graphics.aggregations;

import java.awt.*;

public abstract class ContainerWithPositionalInConnectors extends ContainerWithInConnectors {
    private static final long serialVersionUID = 1L;


    public ContainerWithPositionalInConnectors(Point position, Dimension dimension, Class type, boolean horizontal) {
        super(position, dimension, type, horizontal);
    }

    public ContainerWithPositionalInConnectors(Point position, Dimension dimension, Class type) {
        this(position, dimension, type, false);
    }
}
