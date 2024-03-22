package com.aegamesi.java_visualizer.ui.graphics.aggregations;

import java.awt.*;

public class ContainerWithAutomaticInConnectors extends ContainerWithInConnectors {
    private static final long serialVersionUID = 1L;


    public ContainerWithAutomaticInConnectors(Point position, Dimension dimension, Class type, boolean horizontal) {
        super(position, dimension, type, horizontal);
        computeInConnectors();
    }

    public ContainerWithAutomaticInConnectors(Point position, Dimension dimension, Class type) {
        this(position, dimension, type, false);
    }

}
