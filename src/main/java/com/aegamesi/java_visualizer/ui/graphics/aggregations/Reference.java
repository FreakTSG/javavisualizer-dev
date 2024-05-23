package com.aegamesi.java_visualizer.ui.graphics.aggregations;

import com.aegamesi.java_visualizer.ui.graphics.OutConnector;
import com.aegamesi.java_visualizer.ui.graphics.PositionalGraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;

import java.awt.*;

abstract public class Reference extends AggregateRectangularGraphicElement {
    private static final long serialVersionUID = 1L;


    protected OutConnector outConnector;
    protected Location outConnectorLocation;
    protected boolean manuallyAssigned;
    protected static final int SIZE = 14;

    public Reference(Dimension dimension, Class<?> type, Location outConnectorLocation, boolean manuallyAssigned) {
        this(new Point(), dimension, type, outConnectorLocation, manuallyAssigned);
    }

    public Reference(Point position, Dimension dimension, Class<?> type, Location outConnectorLocation, boolean manuallyAssigned) {
        super(position, dimension);
        this.outConnectorLocation = outConnectorLocation;
        outConnector = new OutConnector(computeOutConnectorPosition(), this, type);
        this.manuallyAssigned = manuallyAssigned;
    }

//    public Class<TReferenceable> getType() {
//        return outConnector.getType();
//    }

    public boolean isManuallyAssigned() {
        return manuallyAssigned;
    }

    private Point computeOutConnectorPosition() {
        Point outConnectorPosition = null;
        int dx = (dimension.width - OutConnector.WIDTH) / 2;
        int dy = (dimension.height - OutConnector.HEIGHT) / 2;
        switch (outConnectorLocation) {
            case CENTER:
                outConnectorPosition = new Point(position.x + dx,
                        position.y + dy);
                break;
            case TOP:
                outConnectorPosition = new Point(position.x + dx,
                        position.y);
                break;
            case BOTTOM:
                outConnectorPosition = new Point(position.x + dx,
                        position.y + dimension.height - OutConnector.HEIGHT);
                break;
            case LEFT:
                outConnectorPosition = new Point(position.x,
                        position.y + dy);
                break;
            case RIGHT:
                outConnectorPosition = new Point(position.x + dimension.width - OutConnector.WIDTH,
                        position.y + dy);
        }
        return outConnectorPosition;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
//        outConnector.paint(g);
    }

    @Override
    public void setPosition(Point position) {
        super.setPosition(position);
        outConnector.setPosition(computeOutConnectorPosition());
    }

    @Override

    public PositionalGraphicElement getPositionalGraphicElement(Point position) {
        PositionalGraphicElement positionalGraphicElement = outConnector.getPositionalGraphicElement(position);
        if (positionalGraphicElement != null) {
            return positionalGraphicElement;
        }
        return super.getPositionalGraphicElement(position);
    }

    public OutConnector getOutConnector() {
        return outConnector;
    }

    public abstract String getCompleteTypeName();

    abstract public Object getFieldValue();

    abstract public void setFieldValue(Object fieldValue);


    protected abstract String getUnsetReferenceExpression();


}
