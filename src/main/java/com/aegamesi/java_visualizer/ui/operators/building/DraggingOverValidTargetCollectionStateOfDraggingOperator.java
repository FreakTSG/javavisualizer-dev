package ui.operators.building;

import ui.Canvas;
import ui.graphics.representations.CollectionRepresentation;
import ui.graphics.representations.GeneralGenericWithToolTipRepresentation;
import ui.graphics.representations.Representation;
import ui.graphics.representations.RepresentationWithInConnectors;
import wrapper_classes.ArrayWrapper;
import wrapper_classes.DataStructureWrapper;
import wrapper_classes.WrapperWithValueClass;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DraggingOverValidTargetCollectionStateOfDraggingOperator extends StateOfBuildingOperator {
    public DraggingOverValidTargetCollectionStateOfDraggingOperator(BuildingOperator operator) {
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
        if (!(targetRepresentation instanceof CollectionRepresentation &&
                ((DataStructureWrapper) targetRepresentation.getOwner()).
                        isComponentValid((WrapperWithValueClass) operator.getSourceRepresentation().getOwner()))) {
            operator.setCurrentState(BuildingOperator.DRAGGING);
//        } else {
//            operator.setTargetRepresentation(targetRepresentation);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);

        operator.repositioningRepresentation();
        ((CollectionRepresentation) operator.getTargetRepresentation()).add((RepresentationWithInConnectors) operator.getSourceRepresentation());
//        operator.repositioningRepresentation();
        operator.setCurrentState(BuildingOperator.SEARCHING);
    }
}
