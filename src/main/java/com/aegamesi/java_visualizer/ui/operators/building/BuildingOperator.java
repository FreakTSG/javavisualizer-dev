package ui.operators.building;

import ui.ButtonBar;
import ui.Canvas;
import ui.graphics.GraphicElement;
import ui.graphics.OutConnector;
import ui.graphics.PositionalGraphicElement;
import ui.graphics.aggregations.ArrayReference;
import ui.graphics.aggregations.FieldReference;
import ui.graphics.aggregations.Reference;
import ui.graphics.representations.ArrayRepresentation;
import ui.graphics.representations.GeneralGenericWithToolTipRepresentation;
import ui.graphics.representations.Representation;
import ui.operators.Operator;
import wrapper_classes.ManuallyAssignableReferenceContainer;
import wrapper_classes.WrapperWithValueClass;

import java.awt.*;
import java.awt.event.MouseEvent;

public class BuildingOperator extends Operator {
    //ESTADOS
    static int i = 0;
    public static final int STARTING = i++;
    public static final int SEARCHING = i++;
    public static final int SELECTING = i++;
    public static final int EXITING_CANVAS = i++;
    public static final int OVER_EMPTY_AREA = i++;
    public static final int OVER_NON_EMPTY_AREA = i++;
    public static final int DRAGGING = i++;
    public static final int DRAGGING_OVER_VALID_TARGET_COLLECTION = i++;
    public static final int DRAGGING_OVER_VALID_TARGET_VARIABLE = i++;
    public static final int DRAGGING_OVER_VALID_TARGET_ARRAY = i++;
    public static final int DRAGGING_OVER_VALID_TARGET_PROJECT_ENTITY = i++;
    public static final int DRAGGING_OVER_VALID_TARGET_RECORD = i++;

    private static Representation sourceRepresentation;
    private static ButtonBar buttonBar;
    private Point deltaFromClick;
    private Point originalPosition;
    private PositionalGraphicElement targetPositionalGraphicElement;
    private Representation targetRepresentation;

    public BuildingOperator(Canvas canvas) {
        super(canvas);

        init();
    }

    public static ButtonBar getButtonBar() {
        return buttonBar == null ? new ButtonBar() : buttonBar;
    }

    public void setButtonBar(ButtonBar buttonBar) {
        this.buttonBar = buttonBar;
        buttonBar.reset();

    }

    public static Representation getSourceRepresentation() {
        return sourceRepresentation;
    }

    public void setSourceRepresentation(Representation sourceRepresentation) {
        this.sourceRepresentation = sourceRepresentation;
    }

    private void init() {
        states.put(STARTING, new StartStateOfBuildingOperator(this));
        states.put(SEARCHING, new SearchingStateOfBuildingOperator(this));
        states.put(SELECTING, new SelectingStateOfBuildingOperator(this));
        states.put(EXITING_CANVAS, new ExitCanvasStateOfBuildingOperator(this));
        states.put(OVER_EMPTY_AREA, new OverEmptyAreaStateOfBuildingOperator(this));
        states.put(OVER_NON_EMPTY_AREA, new OverNonEmptyAreaStateOfBuildingOperator(this));
        states.put(DRAGGING, new DraggingStateOfBuildingOperator(this));
        states.put(DRAGGING_OVER_VALID_TARGET_ARRAY, new DraggingOverValidTargetArrayStateOfDraggingOperator(this));
        states.put(DRAGGING_OVER_VALID_TARGET_COLLECTION, new DraggingOverValidTargetCollectionStateOfDraggingOperator(this));
        states.put(DRAGGING_OVER_VALID_TARGET_PROJECT_ENTITY, new DraggingOverValidTargetProjectEntityStateOfDraggingOperator(this));
        states.put(DRAGGING_OVER_VALID_TARGET_VARIABLE, new DraggingOverValidTargetVariableStateOfDraggingOperator(this));
        states.put(DRAGGING_OVER_VALID_TARGET_RECORD, new DraggingOverValidTargetRecordStateOfDraggingOperator(this));

        setCurrentState(STARTING);

    }

    //for project entities and variables
    public boolean isValid(GraphicElement graphicElement) {
        if (graphicElement instanceof OutConnector) {
            final Reference outConnectorReference = ((OutConnector) graphicElement).getReference();

            if (!(outConnectorReference instanceof FieldReference)) {
                return false;
            }
            return ((ManuallyAssignableReferenceContainer) ((FieldReference) outConnectorReference).getFieldOwnerObjectOfWrapper()).isValid(sourceRepresentation.getOwnerTypeName(), ((FieldReference) outConnectorReference).getField());
        }
        if (!(graphicElement instanceof FieldReference)) {
            return false;
        }
        return ((ManuallyAssignableReferenceContainer) ((FieldReference) graphicElement).getFieldOwnerObjectOfWrapper()).isValid(sourceRepresentation.getOwnerTypeName(), ((FieldReference) graphicElement).getField());
    }

    //for arrays
    public boolean isValid(GraphicElement graphicElement, ArrayRepresentation arrayRepresentation) {
        if (graphicElement instanceof OutConnector) {
            final Reference outConnectorReference = ((OutConnector) graphicElement).getReference();

            if (!(outConnectorReference instanceof ArrayReference)) {
                return false;
            }
            return ((ArrayReference) outConnectorReference).getArrayWrapper().isComponentValid((WrapperWithValueClass) sourceRepresentation.getOwner());
        }
        if (!(graphicElement instanceof ArrayReference)) {
            return false;
        }
        return ((ArrayReference) graphicElement).getArrayWrapper().isComponentValid((WrapperWithValueClass) sourceRepresentation.getOwner());

    }

    public Point getOriginalPosition() {
        return originalPosition;
    }

    public void setOriginalPosition(Point originalPosition) {
        this.originalPosition = originalPosition;
    }

    public PositionalGraphicElement getTargetPositionalGraphicElement() {
        return targetPositionalGraphicElement;
    }

    public void setTargetPositionalGraphicElement(PositionalGraphicElement targetPositionalGraphicElement) {
        this.targetPositionalGraphicElement = targetPositionalGraphicElement;
    }

    public Representation getTargetRepresentation() {
        return targetRepresentation;
    }

    public void setTargetRepresentation(Representation targetRepresentation) {
        this.targetRepresentation = targetRepresentation;
    }

    public Point getDeltaFromClick() {
        return deltaFromClick;
    }

    public void setDeltaFromClick(Point deltaFromClick) {
        this.deltaFromClick = deltaFromClick;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentState.mousePressed(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentState.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        currentState.mouseMoved(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currentState.mouseReleased(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        currentState.mouseExited(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        currentState.mouseEntered(e);
    }

    public void repositioningRepresentation() {
        sourceRepresentation.setPosition(originalPosition);
//        if (sourceRepresentation instanceof GeneralGenericWithToolTipRepresentation) {
//            ((GeneralGenericWithToolTipRepresentation<?>) sourceRepresentation).setShowToolTip(false);
//        }
        canvas.repaint();
    }


}
