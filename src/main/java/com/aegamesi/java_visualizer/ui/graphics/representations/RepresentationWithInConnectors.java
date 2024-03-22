package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.InConnector;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.ContainerWithInConnectors;

import java.awt.*;

abstract public class RepresentationWithInConnectors<T extends Object> extends Representation<T> {
    private static final long serialVersionUID = 1L;

    protected int referenceCount;

    public RepresentationWithInConnectors(Point position, T owner, MyCanvas myCanvas) {
        super(position, owner, myCanvas);
        referenceCount = 0;
    }

    public InConnector[] getInConnectors() {
        return ((ContainerWithInConnectors) container).getInConnectors();
    }

    public InConnector[] getLeftInConnectors() {
        return ((ContainerWithInConnectors) container).getLeftInConnectors();
    }

    @Override
    public ContainerWithInConnectors getContainer() {
        return ((ContainerWithInConnectors) container);
    }

    public void incrementReferenceCount() {
        referenceCount++;
//        System.out.println("inc referenceCount " + getClass().getSimpleName() + " = " + referenceCount);
    }

    public void decrementReferenceCount() {
        referenceCount--;
//        System.out.println("dec referenceCount " + getClass().getSimpleName() + " = " + referenceCount);
    }

    public boolean isReferenced() {
        return referenceCount != 0;
    }

    //free any resource (ex: connections) before destroy this object
    public void dispose() {

    }
}
