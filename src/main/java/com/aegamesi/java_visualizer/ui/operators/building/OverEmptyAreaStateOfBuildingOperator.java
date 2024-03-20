package ui.operators.building;

import java.awt.*;

public class OverEmptyAreaStateOfBuildingOperator extends OverStateOfBuildingOperator {

    public OverEmptyAreaStateOfBuildingOperator(BuildingOperator operator) {
        super(operator);
    }

    @Override
    public void init() {
        super.init();
        canvas.setCursor(Cursor.getDefaultCursor());

    }

}

