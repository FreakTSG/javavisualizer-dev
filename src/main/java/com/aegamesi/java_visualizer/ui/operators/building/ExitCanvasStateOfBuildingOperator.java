package ui.operators.building;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ExitCanvasStateOfBuildingOperator extends StateOfBuildingOperator {


    public ExitCanvasStateOfBuildingOperator(BuildingOperator operator) {
        super(operator);
    }

    @Override
    public void init() {
        super.init();
        canvas.setCursor(Cursor.getDefaultCursor());
    }


    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        operator.setCurrentState(BuildingOperator.SEARCHING);
    }
}

