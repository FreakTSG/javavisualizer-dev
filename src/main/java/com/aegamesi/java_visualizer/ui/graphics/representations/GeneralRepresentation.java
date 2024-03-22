package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.ContainerWithAutomaticInConnectors;

import java.awt.*;

abstract public class GeneralRepresentation<T extends Object> extends RepresentationWithInConnectors<T> {
    private static final long serialVersionUID = 1L;


    public GeneralRepresentation(Point position, T owner, MyCanvas myCanvas) {
        this(position, owner, myCanvas, false);
    }

    public GeneralRepresentation(Point position, T owner, MyCanvas myCanvas, boolean horizontal) {
        super(position, owner, myCanvas);
        container = new ContainerWithAutomaticInConnectors(position, new Dimension(0, 0), owner.getClass(), horizontal);
        init();
    }

}
