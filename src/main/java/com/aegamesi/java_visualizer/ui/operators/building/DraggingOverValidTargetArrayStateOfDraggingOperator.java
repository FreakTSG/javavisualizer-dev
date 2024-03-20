package ui.operators.building;

import ui.Canvas;
import ui.graphics.OutConnector;
import ui.graphics.StraightConnection;
import ui.graphics.aggregations.ArrayReference;
import ui.graphics.representations.ArrayRepresentation;
import ui.graphics.representations.GeneralGenericWithToolTipRepresentation;
import ui.graphics.representations.Representation;
import ui.graphics.representations.RepresentationWithInConnectors;
import utils.Utils;
import wrapper_classes.ArrayWrapper;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DraggingOverValidTargetArrayStateOfDraggingOperator extends StateOfBuildingOperator {
    public DraggingOverValidTargetArrayStateOfDraggingOperator(BuildingOperator operator) {
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
        operator.setTargetPositionalGraphicElement(targetRepresentation.getPositionalGraphicElement(e.getPoint()));
        if (!(targetRepresentation instanceof ArrayRepresentation &&
                operator.isValid(operator.getTargetPositionalGraphicElement(), ((ArrayRepresentation) targetRepresentation)))) {
            operator.setCurrentState(BuildingOperator.DRAGGING);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);

        final OutConnector outConnector = operator.getTargetPositionalGraphicElement() instanceof ArrayReference ?
                ((ArrayReference) operator.getTargetPositionalGraphicElement()).getOutConnector() :
                ((OutConnector) operator.getTargetPositionalGraphicElement());

        ArrayReference arrayReference = ((ArrayReference) outConnector.getReference());

        //code generation - warning => must be before the creation of the new connection
        ArrayWrapper fieldOwnerObjectOfWrapper = arrayReference.getArrayWrapper();
        RepresentationWithInConnectors ownerRepresentation = canvas.getRepresentationWithInConnectors(fieldOwnerObjectOfWrapper);
        String ownerVariableName = canvas.getFirstReferenceTo(ownerRepresentation);
        String targetVariableName = canvas.getFirstReferenceTo(operator.getSourceRepresentation());
        String code = ownerVariableName + "[" + arrayReference.getIndex() + "] = " + targetVariableName;
        canvas.getIDSToolWindow().addCodeToEditorPaneSourceCode(code + ";\n");


        operator.getCanvas().add(new StraightConnection<>(outConnector, ((RepresentationWithInConnectors) operator.getSourceRepresentation())));
        arrayReference.setFieldValue(operator.getSourceRepresentation().getOwner());
        operator.repositioningRepresentation();
        operator.setCurrentState(BuildingOperator.SEARCHING);
        canvas.addOperation(Utils.getUndoRedoSetReferenceMessage(code));
    }
}
