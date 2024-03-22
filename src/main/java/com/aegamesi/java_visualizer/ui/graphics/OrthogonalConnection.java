package com.aegamesi.java_visualizer.ui.graphics;

import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;
import com.aegamesi.java_visualizer.utils.Utils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public abstract class OrthogonalConnection<TOutConnector extends OutConnector, TRepresentationObject extends RepresentationWithInConnectors> extends Connection<TOutConnector, TRepresentationObject> {
    private static final long serialVersionUID = 1L;

    protected int firstDistance;
    protected int secondDistance;
    protected boolean connectionToHimself;

    public OrthogonalConnection(TOutConnector source, TRepresentationObject target, Color borderColor, int firstDistance, int secondDistance, boolean connectionToHimself) {
        super(source, target, borderColor);
        this.source = source;
        this.target = target;
        this.firstDistance = firstDistance;
        this.secondDistance = secondDistance;
        this.connectionToHimself = connectionToHimself;
    }

    @Override
    protected void draw(Graphics g, Point centerPosition, Point targetPosition) {
        if (connectionToHimself) {

//            final Point pointRotatedOverAnotherAndDownScaling = Utils.getPointRotatedOverAnotherAndDownScaling(0, 0, 10, 0, Math.PI);

            Point targetPositionAfterRotating = Utils.getPointRotatedOverAnotherAndDownScaling(targetPosition.x, targetPosition.y, centerPosition.x, centerPosition.y, Math.PI);
//            if (Point.distance(targetPositionAfterRotating.x, targetPositionAfterRotating.y, centerPosition.x, centerPosition.y) < 1000) {
//                targetPositionAfterRotating = Utils.getPointRotatedOverAnother(centerPosition.x, centerPosition.y, targetPositionAfterRotating.x, targetPositionAfterRotating.y, Math.PI);
//            }
            targetPosition = targetPositionAfterRotating;
        }
        Point pointRotated90Degrees = Utils.getPointRotatedOverAnother(centerPosition.x, centerPosition.y, targetPosition.x, targetPosition.y, -Math.PI / 2);
        Point newTargetPosition = getTargetPosition(pointRotated90Degrees);
        Graphics2D g1 = (Graphics2D) g.create();

        Point projectedPoint = Utils.getProjectionPoint(centerPosition.x, centerPosition.y, targetPosition.x, targetPosition.y, newTargetPosition.x, newTargetPosition.y);

        AffineTransform affineTransform = new AffineTransform(((Graphics2D) g).getTransform());

        final int distance = (int) Point2D.distance(centerPosition.x, centerPosition.y, projectedPoint.x, projectedPoint.y);

        Line2D line0 = new Line2D.Float(0, 0, firstDistance, 0);
        Line2D line1 = new Line2D.Float(firstDistance, 0, firstDistance, secondDistance);
        Line2D line2 = new Line2D.Float(firstDistance, secondDistance, distance, secondDistance);
        Line2D line3 = new Line2D.Float(distance, secondDistance, distance, (float) projectedPoint.distance(newTargetPosition.x, newTargetPosition.y));

        double angle = Math.atan2(projectedPoint.y - centerPosition.y, projectedPoint.x - centerPosition.x);// + (connectionToHimself ? Math.PI : 0);
        affineTransform.translate(centerPosition.x, centerPosition.y);
        affineTransform.rotate(angle);

        g1.setTransform(affineTransform);
        drawArrow(g1, distance, secondDistance, distance, (int) projectedPoint.distance(newTargetPosition.x, newTargetPosition.y));
        g1.draw(line0);
        g1.draw(line1);
        g1.draw(line2);
        g1.draw(line3);
        g1.dispose();

    }


}
