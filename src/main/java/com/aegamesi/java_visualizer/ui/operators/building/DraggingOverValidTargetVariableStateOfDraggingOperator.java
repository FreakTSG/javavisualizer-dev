package ui.operators.building;

import ui.Canvas;
import ui.graphics.OutConnector;
import ui.graphics.StraightConnection;
import ui.graphics.aggregations.FieldReference;
import ui.graphics.representations.Representation;
import ui.graphics.representations.RepresentationWithInConnectors;
import ui.graphics.representations.VariableRepresentation;
import utils.Utils;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DraggingOverValidTargetVariableStateOfDraggingOperator extends StateOfBuildingOperator {

    private Representation movableRepresentation;

    public DraggingOverValidTargetVariableStateOfDraggingOperator(BuildingOperator operator) {
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
        movableRepresentation = canvas.getMovableRepresentationExcept(operator.getSourceRepresentation(), e.getX(), e.getY());
        if (movableRepresentation == null || !operator.isValid(movableRepresentation.getPositionalGraphicElement(e.getPoint()))) {
            operator.setCurrentState(BuildingOperator.DRAGGING);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        Canvas canvas = operator.getCanvas();
        String code = ((VariableRepresentation) movableRepresentation).getAttributionCode(operator.getSourceRepresentation());
        canvas.getIDSToolWindow().addCodeToEditorPaneSourceCode(code);
        final OutConnector outConnector = ((FieldReference) operator.getTargetPositionalGraphicElement()).getOutConnector();
        canvas.add(new StraightConnection<>(outConnector, ((RepresentationWithInConnectors) operator.getSourceRepresentation())));
        outConnector.getReference().setFieldValue(operator.getSourceRepresentation().getOwner());
        operator.repositioningRepresentation();
        operator.setCurrentState(BuildingOperator.SEARCHING);
        canvas.addOperation(Utils.getUndoRedoSetReferenceMessage(code));
    }
}
