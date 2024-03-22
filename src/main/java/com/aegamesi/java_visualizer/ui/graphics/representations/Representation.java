package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.PositionalGraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.Container;

import java.awt.*;
import java.io.Serializable;

abstract public class Representation<T extends Object> implements BaseRepresentation, Serializable {
    private static final long serialVersionUID = 1L;

    protected T owner;
    protected transient MyCanvas myCanvas;

    protected Container container;

    public void setCanvas(MyCanvas myCanvas) {
        this.myCanvas = myCanvas;
    }

    public Representation(Point position, T owner, MyCanvas myCanvas) {
        this.owner = owner;
        this.myCanvas = myCanvas;
    }

    abstract public void init();

    public void paint(Graphics g) {
        container.paint(g);
    }

    //    @Override
    public PositionalGraphicElement getPositionalGraphicElement(Point pos) {
        return container.getPositionalGraphicElement(pos);
    }

    public boolean contains(Point position) {
        return container.contains(position);
    }

    public Point getPosition() {
        return container.getPosition();
    }

    public void setPosition(Point position) {
        container.setPosition(position);
    }

    public T getOwner() {
        return owner;
    }

    public void setOwner(T owner) {
        this.owner = owner;
    }

    public MyCanvas getCanvas() {
        return myCanvas;
    }


    public Container getContainer() {
        return container;
    }

    public Point getCenterPosition() {
        return container.getCenterPosition();
    }

    public boolean intersects(Representation representationWithInConnectors) {
        return container.intersects(representationWithInConnectors.getContainer());
    }

    public void edit(Point position) {

    }


}
