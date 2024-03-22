package com.aegamesi.java_visualizer.ui.graphics.aggregations;

import com.aegamesi.java_visualizer.ui.graphics.InConnector;
import com.aegamesi.java_visualizer.ui.graphics.localizations.HoursLocalization;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;


import java.awt.*;

public abstract class ContainerWithInConnectors extends Container {
    private static final long serialVersionUID = 1L;


    protected Class type;
    protected InConnector[] inConnectors;
    protected boolean connectorsShown;

    public ContainerWithInConnectors(Point position, Dimension dimension, Class type, boolean horizontal) {
        super(position, dimension, horizontal);
        this.type = type;
        inConnectors = new InConnector[12];
        connectorsShown = false;
    }

    public ContainerWithInConnectors(Point position, Dimension dimension, Class type) {
        this(position, dimension, type, false);
    }

    public void computeInConnectors() {
        int dy1 = dimension.height / 4;
        int dy2 = 2 * dy1;
        int dy3 = dy1 + dy2;
        int dx1 = dimension.width / 4;
        int dx2 = 2 * dx1;
        int dx3 = dx1 + dx2;

        Point p = new Point(position);
        p.translate(-InConnector.WIDTH, dy2);
        inConnectors[HoursLocalization.NINE.ordinal()] = new InConnector(p, type);

        p = new Point(position);
        p.translate(-InConnector.WIDTH, dy1);
        inConnectors[HoursLocalization.TEN.ordinal()] = new InConnector(p, type);

        p = new Point(position);
        p.translate(-InConnector.WIDTH, dy3);
        inConnectors[HoursLocalization.EIGHT.ordinal()] = new InConnector(p, type);

        p = new Point(position);
        p.translate(dx2, -InConnector.HEIGHT);
        inConnectors[HoursLocalization.TWELVE.ordinal()] = new InConnector(p, type);

        p = new Point(position);
        p.translate(dx1, -InConnector.HEIGHT);
        inConnectors[HoursLocalization.ELEVEN.ordinal()] = new InConnector(p, type);

        p = new Point(position);
        p.translate(dx3, -InConnector.HEIGHT);
        inConnectors[HoursLocalization.ONE.ordinal()] = new InConnector(p, type);

        p = new Point(position);
        p.translate(dimension.width, dy2);
        inConnectors[HoursLocalization.THREE.ordinal()] = new InConnector(p, type);

        p = new Point(position);
        p.translate(dimension.width, dy1);
        inConnectors[HoursLocalization.TWO.ordinal()] = new InConnector(p, type);

        p = new Point(position);
        p.translate(dimension.width, dy3);
        inConnectors[HoursLocalization.FOUR.ordinal()] = new InConnector(p, type);

        p = new Point(position);
        p.translate(dx2, dimension.height);
        inConnectors[HoursLocalization.SIX.ordinal()] = new InConnector(p, type);

        p = new Point(position);
        p.translate(dx1, dimension.height);
        inConnectors[HoursLocalization.SEVEN.ordinal()] = new InConnector(p, type);

        p = new Point(position);
        p.translate(dx3, dimension.height);
        inConnectors[HoursLocalization.FIVE.ordinal()] = new InConnector(p, type);
    }

    public void showConnectors() {
        connectorsShown = true;
    }

    public void hideConnectors() {
        connectorsShown = false;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (connectorsShown) {
            for (InConnector inConnector : inConnectors) {
                inConnector.paint(g);
            }
        }
    }

    @Override
    public void setPosition(Point position) {
        super.setPosition(position);
        computeInConnectors();
    }


    public InConnector[] getInConnectors() {
        return inConnectors;
    }

    public InConnector[] getLeftInConnectors() {
        InConnector[] leftInConnectors = new InConnector[3];
        leftInConnectors[0] = inConnectors[HoursLocalization.EIGHT.ordinal()];
        leftInConnectors[1] = inConnectors[HoursLocalization.NINE.ordinal()];
        leftInConnectors[2] = inConnectors[HoursLocalization.TEN.ordinal()];
        return leftInConnectors;
    }

    @Override
    public void add(AggregateRectangularGraphicElement aggregateGraphicElement) {
        super.add(aggregateGraphicElement);
        computeInConnectors();
    }

    @Override
    public void add(AggregateRectangularGraphicElement aggregateGraphicElement, Location location) {
        super.add(aggregateGraphicElement, location);
        computeInConnectors();
    }

    @Override
    public void remove(AggregateRectangularGraphicElement aggregateGraphicElement) {
        super.remove(aggregateGraphicElement);
        computeInConnectors();
    }

}
