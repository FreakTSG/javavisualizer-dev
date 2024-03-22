package com.aegamesi.java_visualizer.ui.graphics;

import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

public abstract class Connection<TOutConnector extends OutConnector, TRepresentableObject extends RepresentationWithInConnectors> extends GraphicElement {
    private static final long serialVersionUID = 1L;

    protected TOutConnector source;
    protected TRepresentableObject target;

    public Connection(TOutConnector source, TRepresentableObject target) {
        this(source, target, Color.BLUE);
    }

    public Connection(TOutConnector source, TRepresentableObject target, Color borderColor) {
        super(borderColor);
        this.source = source;
        this.target = target;
        target.incrementReferenceCount();
    }

    protected InConnector[] getTargetInConnectors() {
        return target.getInConnectors();
    }

    protected Point getTargetPosition(Point sourcePosition) {
        double minDistance = Double.MAX_VALUE;
        Point targetPosition = new Point(0, 0);
        for (InConnector inConnector : getTargetInConnectors()) {
            Point inConnectorCenterPosition = inConnector.getCenterPosition();
            double distanceSq = inConnectorCenterPosition.distanceSq(sourcePosition);
            if (distanceSq < minDistance) {
                minDistance = distanceSq;
                targetPosition = inConnectorCenterPosition;
            }
        }
        return targetPosition;
    }

    public TOutConnector getSource() {
        return source;
    }

    public void setSource(TOutConnector source) {
        this.source = source;
    }

    public TRepresentableObject getTarget() {
        return target;
    }

//    public void setTarget(TRepresentableObject target) {
//        this.target = target;
//    }

    @Override
    public void paint(Graphics g) {
        final Color color = g.getColor();
        Stroke stroke = ((Graphics2D) g).getStroke();
        g.setColor(borderColor);
        ((Graphics2D) g).setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));

        Point centerPosition = source.getCenterPosition();
        Point targetPosition = getTargetPosition(centerPosition);

        draw(g, centerPosition, targetPosition);

        ((Graphics2D) g).setStroke(stroke);
        g.setColor(color);
    }

    protected abstract void draw(Graphics g, Point centerPosition, Point targetPosition);

    /////////////////////////////////

    protected void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        // create an AffineTransform
        // and a triangle centered on (0,0) and pointing downward
        // somewhere outside Swing's paint loop
        AffineTransform affineTransform = new AffineTransform(g2d.getTransform());
//        Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);

//        Polygon arrowHead = new Polygon();
//        arrowHead.addPoint(0, 5);
//        arrowHead.addPoint(-5, -5);
//        arrowHead.addPoint(5, -5);

        Line2D line1 = new Line2D.Float(0, 0, -3, -6);
        Line2D line2 = new Line2D.Float(0, 0, 3, -6);


//        affineTransform.setToIdentity();
        double angle = Math.atan2(y2 - y1, x2 - x1);
        affineTransform.translate(x2, y2);
        affineTransform.rotate(angle - Math.PI / 2d);

        Graphics2D g = (Graphics2D) g2d.create();
        g.setTransform(affineTransform);
//        g.fill(arrowHead);
        g.draw(line1);
        g.draw(line2);
        g.dispose();
    }

}
