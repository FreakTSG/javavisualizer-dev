package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.nodes;

import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;

import java.awt.*;

abstract public class NodeRepresentation extends RepresentationWithInConnectors<Object> {
    private static final long serialVersionUID = 1L;

    private int index;

    public NodeRepresentation(Point position, Object owner, MyCanvas myCanvas) {
        super(position, owner, myCanvas);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
