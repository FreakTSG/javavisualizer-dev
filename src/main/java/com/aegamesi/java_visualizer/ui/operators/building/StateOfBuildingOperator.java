package ui.operators.building;

import ui.Canvas;
import ui.operators.State;

import java.awt.event.MouseEvent;

public abstract class StateOfBuildingOperator extends State<BuildingOperator> {

    protected Canvas canvas;

    public StateOfBuildingOperator(BuildingOperator operator) {
        super(operator);
        canvas = operator.getCanvas();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        if (!operator.getCanvas().getVisibleRect().contains(e.getPoint())) {
            operator.getButtonBar().setVisible(false);
            operator.setCurrentState(BuildingOperator.EXITING_CANVAS);
        }
    }

}

