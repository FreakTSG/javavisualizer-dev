package ui.operators.building;

import ui.Canvas;
import ui.graphics.representations.RecordRepresentation;
import ui.graphics.representations.Representation;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DraggingOverValidTargetRecordStateOfDraggingOperator extends StateOfBuildingOperator {
    public DraggingOverValidTargetRecordStateOfDraggingOperator(BuildingOperator operator) {
        super(operator);
    }

    @Override
    public void init() {
        operator.getCanvas().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        Point deltaFromClick = operator.getDeltaFromClick();
        operator.getSourceRepresentation().setPosition(new Point(e.getX() - deltaFromClick.x, e.getY() - deltaFromClick.y));
        final Canvas canvas = operator.getCanvas();
        canvas.repaint();
        final Representation targetRepresentation = canvas.getMovableRepresentationExcept(operator.getSourceRepresentation(), e.getX(), e.getY());
        if (targetRepresentation == null) {
            operator.setCurrentState(BuildingOperator.DRAGGING);
            return;
        }
        operator.setTargetPositionalGraphicElement(targetRepresentation.getPositionalGraphicElement(e.getPoint()));

        if (!(targetRepresentation instanceof RecordRepresentation)) {
            operator.setCurrentState(BuildingOperator.DRAGGING);
//        } else {
//            operator.setTargetRepresentation(targetRepresentation);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);

        operator.repositioningRepresentation();
        ((RecordRepresentation) operator.getTargetRepresentation()).add(operator.getSourceRepresentation());

//        operator.repositioningRepresentation();
        operator.setCurrentState(BuildingOperator.SEARCHING);
    }
}
