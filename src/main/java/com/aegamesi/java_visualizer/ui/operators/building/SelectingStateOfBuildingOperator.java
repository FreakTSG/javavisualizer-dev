package ui.operators.building;

import ui.graphics.representations.Representation;

import java.awt.*;
import java.awt.event.MouseEvent;

public class SelectingStateOfBuildingOperator extends AppearingButtonBarStateOfBuildingOperator {

    public SelectingStateOfBuildingOperator(BuildingOperator operator) {
        super(operator);
    }

    @Override
    public void init() {
        super.init();
//        operator.setButtonBar(operator.getSourceRepresentation().getButtonBar());
        canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        if (operator.getSourceRepresentation() == null) {
            operator.setCurrentState(BuildingOperator.SEARCHING);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        Representation representation = operator.getSourceRepresentation();
        if (e.getButton() == MouseEvent.BUTTON1) {
            operator.setOriginalPosition(new Point(representation.getPosition()));
            operator.setDeltaFromClick(new Point(e.getX() - representation.getPosition().x, e.getY() - representation.getPosition().y));
            operator.setCurrentState(BuildingOperator.DRAGGING);
        } else {
            contextMenuThread.cancel();
            representation.edit(e.getPoint());
            operator.setCurrentState(BuildingOperator.SEARCHING);

        }
    }

}

