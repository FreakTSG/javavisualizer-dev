package ui.operators.building;

import ui.graphics.representations.Representation;

import java.awt.*;
import java.awt.event.MouseEvent;

public class OverNonEmptyAreaStateOfBuildingOperator extends OverStateOfBuildingOperator {

    public OverNonEmptyAreaStateOfBuildingOperator(BuildingOperator operator) {
        super(operator);
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
            operator.getButtonBar().setVisible(false);
            representation.edit(e.getPoint());
            operator.setCurrentState(BuildingOperator.SEARCHING);
        }
    }
}
