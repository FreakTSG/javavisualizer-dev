package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.ContainerWithoutInConnectors;


import java.awt.*;

abstract public class RepresentationWithoutInConnectors<T> extends Representation<T> {
    private static final long serialVersionUID = 1L;


    public RepresentationWithoutInConnectors(Point position, T owner, MyCanvas canvas) {
        super(position, owner, canvas);
        container = new ContainerWithoutInConnectors(position, new Dimension(0, 0), true);
        init();
    }

    @Override
    public ContainerWithoutInConnectors getContainer() {
        return ((ContainerWithoutInConnectors) container);
    }

}
