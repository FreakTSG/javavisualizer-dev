package ui.operators.building;

import ui.Canvas;
import ui.graphics.representations.*;
import wrapper_classes.DataStructureWrapper;
import wrapper_classes.WrapperWithValueClass;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DraggingStateOfBuildingOperator extends StateOfBuildingOperator {
    public DraggingStateOfBuildingOperator(BuildingOperator operator) {
        super(operator);
    }

    @Override
    public void init() {
        operator.getCanvas().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        Point deltaFromClick = operator.getDeltaFromClick();
        final Representation sourceRepresentation = operator.getSourceRepresentation();
        sourceRepresentation.setPosition(new Point(e.getX() - deltaFromClick.x, e.getY() - deltaFromClick.y));
        final Canvas canvas = operator.getCanvas();
        canvas.repaint();
        //a variable can't be dragged over any representation
        //a record can't be dragged over any representation
        if (sourceRepresentation instanceof VariableRepresentation || sourceRepresentation instanceof RecordRepresentation) {
            return;
        }

        if (sourceRepresentation instanceof GeneralGenericWithToolTipRepresentation) {
            ((GeneralGenericWithToolTipRepresentation<?>) sourceRepresentation).setShowToolTip(false);
        }

        Representation oldTargetRepresentation = operator.getTargetRepresentation();
        if (oldTargetRepresentation instanceof GeneralGenericWithToolTipRepresentation) {
            ((GeneralGenericWithToolTipRepresentation<?>) oldTargetRepresentation).setShowToolTip(false);
        }

        final Representation targetRepresentation = canvas.getMovableRepresentationExcept(sourceRepresentation, e.getX(), e.getY());
        if (targetRepresentation == null) {
            return;
        }

        if (targetRepresentation instanceof GeneralGenericWithToolTipRepresentation) {
            ((GeneralGenericWithToolTipRepresentation<?>) targetRepresentation).setShowToolTip(true);
        }

        operator.setTargetPositionalGraphicElement(targetRepresentation.getPositionalGraphicElement(e.getPoint()));
        operator.setTargetRepresentation(targetRepresentation);
        if (targetRepresentation instanceof CollectionRepresentation) {

            if (((DataStructureWrapper) targetRepresentation.getOwner()).
                    isComponentValid((WrapperWithValueClass) operator.getSourceRepresentation().getOwner())) {
//                operator.setTargetRepresentation(targetRepresentation);
                operator.setCurrentState(BuildingOperator.DRAGGING_OVER_VALID_TARGET_COLLECTION);
            }
        } else if (targetRepresentation instanceof VariableRepresentation) {
            if (operator.isValid(operator.getTargetPositionalGraphicElement())) {
                operator.setCurrentState(BuildingOperator.DRAGGING_OVER_VALID_TARGET_VARIABLE);
            }
        } else if (targetRepresentation instanceof ArrayRepresentation) {
            if (operator.isValid(operator.getTargetPositionalGraphicElement(), ((ArrayRepresentation) targetRepresentation))) {
                operator.setCurrentState(BuildingOperator.DRAGGING_OVER_VALID_TARGET_ARRAY);
            }
        } else if (targetRepresentation instanceof RecordRepresentation) {
            operator.setCurrentState(BuildingOperator.DRAGGING_OVER_VALID_TARGET_RECORD);
        } else {  //project entity
            if (operator.isValid(operator.getTargetPositionalGraphicElement())) {
                operator.setCurrentState(BuildingOperator.DRAGGING_OVER_VALID_TARGET_PROJECT_ENTITY);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        Representation sourceRepresentation = operator.getSourceRepresentation();
        if (operator.getCanvas().intersects(sourceRepresentation)) {
            operator.repositioningRepresentation();
        }
        sourceRepresentation.getContainer().forceNonNegativePosition();
        operator.setCurrentState(BuildingOperator.SEARCHING);
    }
}
