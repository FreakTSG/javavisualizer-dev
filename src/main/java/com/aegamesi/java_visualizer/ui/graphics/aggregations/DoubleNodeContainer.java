package com.aegamesi.java_visualizer.ui.graphics.aggregations;

import com.aegamesi.java_visualizer.ui.graphics.localizations.HoursLocalization;

import java.awt.*;

public class DoubleNodeContainer extends ContainerWithPositionalInConnectors {
    private static final long serialVersionUID = 1L;


    public DoubleNodeContainer(Point position, Dimension dimension, Class type, boolean horizontal) {
        super(position, dimension, type, horizontal);
    }

    public DoubleNodeContainer(Point position, Dimension dimension, Class type) {
        this(position, dimension, type, false);
    }

    @Override
    public void computeInConnectors() {
        if (localizableAggregateGraphicElements.size() < 3) {
            return;
        }
        super.computeInConnectors();

        Point centerPosition = ((Reference) localizableAggregateGraphicElements.get(2).aggregateGraphicElement).outConnector.getCenterPosition();
        inConnectors[HoursLocalization.TEN.ordinal()].setPosition(new Point(inConnectors[HoursLocalization.TEN.ordinal()].getPosition().x, centerPosition.y));

        centerPosition = ((Reference) localizableAggregateGraphicElements.get(0).aggregateGraphicElement).outConnector.getCenterPosition();
        inConnectors[HoursLocalization.TWO.ordinal()].setPosition(new Point(inConnectors[HoursLocalization.TWO.ordinal()].getPosition().x, centerPosition.y));

    }
}
