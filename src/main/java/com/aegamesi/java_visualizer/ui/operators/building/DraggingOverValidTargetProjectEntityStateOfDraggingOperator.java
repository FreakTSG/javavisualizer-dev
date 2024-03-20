package ui.operators.building;

import ui.graphics.OutConnector;
import ui.graphics.StraightConnection;
import ui.graphics.aggregations.FieldReference;
import ui.graphics.representations.DefaultRepresentation;
import ui.graphics.representations.GeneralGenericWithToolTipRepresentation;
import ui.graphics.representations.Representation;
import ui.graphics.representations.RepresentationWithInConnectors;
import utils.Utils;
import wrapper_classes.WrapperWithValue;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DraggingOverValidTargetProjectEntityStateOfDraggingOperator extends StateOfBuildingOperator {
    public DraggingOverValidTargetProjectEntityStateOfDraggingOperator(BuildingOperator operator) {
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
//        final Canvas canvas = operator.getCanvas();
        canvas.repaint();
        final Representation movableRepresentation = canvas.getMovableRepresentationExcept(operator.getSourceRepresentation(), e.getX(), e.getY());
        if (movableRepresentation == null || !operator.isValid(movableRepresentation.getPositionalGraphicElement(e.getPoint()))) {
            operator.setCurrentState(BuildingOperator.DRAGGING);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);


        final OutConnector outConnector = ((FieldReference) operator.getTargetPositionalGraphicElement()).getOutConnector();
        FieldReference fieldReference = (FieldReference) outConnector.getReference();


        //code generation - attention => must be before the creation of the new connection
        WrapperWithValue fieldOwnerObjectOfWrapper = fieldReference.getFieldOwnerObjectOfWrapper();
        RepresentationWithInConnectors ownerRepresentation = canvas.getRepresentationWithInConnectors(fieldOwnerObjectOfWrapper);
        String ownerVariableName = canvas.getFirstReferenceTo(ownerRepresentation);
        String targetVariableName = canvas.getFirstReferenceTo(operator.getSourceRepresentation());

//        StringBuilder methodName = new StringBuilder("set").append(fieldReference.getField().getName());
//        String initialCharUpperCase = String.valueOf(methodName.charAt(3)).toUpperCase();
//        methodName.setCharAt(3, initialCharUpperCase.charAt(0));
        String code = ownerVariableName + Utils.getSetterCall(fieldReference.getField().getName(), targetVariableName);
        canvas.getIDSToolWindow().addCodeToEditorPaneSourceCode(code + ";\n");

        fieldReference.setFieldValue(operator.getSourceRepresentation().getOwner());
        ((DefaultRepresentation) operator.getTargetRepresentation()).addNewConnection(new StraightConnection<>(outConnector, ((RepresentationWithInConnectors) operator.getSourceRepresentation())));

        operator.repositioningRepresentation();
        operator.setCurrentState(BuildingOperator.SEARCHING);
        canvas.addOperation(Utils.getUndoRedoSetReferenceMessage(code));
    }
}
