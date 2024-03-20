package ui.operators.building;

import java.awt.*;
import java.awt.event.MouseEvent;

public class StartStateOfBuildingOperator extends StateOfBuildingOperator {

    public StartStateOfBuildingOperator(BuildingOperator operator) {
        super(operator);
    }

    @Override
    public void init() {
        super.init();
        canvas.setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);

        if (!canvas.isVisible()) {
            return;
        }

        if (operator.getCanvas().getVisibleRect().contains(e.getPoint())) {
            operator.setCurrentState(BuildingOperator.SEARCHING);
        } else {
            operator.setCurrentState(BuildingOperator.EXITING_CANVAS);
        }
    }

}

