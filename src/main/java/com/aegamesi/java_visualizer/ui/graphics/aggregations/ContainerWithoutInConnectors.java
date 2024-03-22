package com.aegamesi.java_visualizer.ui.graphics.aggregations;

import java.awt.*;

public class ContainerWithoutInConnectors extends Container {
    private static final long serialVersionUID = 1L;


    public ContainerWithoutInConnectors(Point position, Dimension dimension, boolean horizontal) {
        super(position, dimension, horizontal);
    }

    public ContainerWithoutInConnectors(Point position, Dimension dimension, Color borderColor, Color backgroundColor, boolean horizontal) {
        super(position, dimension, borderColor, backgroundColor, horizontal);
    }

}
