package ui.operators.building;

import ui.ButtonBar;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class OverStateOfBuildingOperator extends StateOfBuildingOperator {

    protected static Point overPosition;

    public OverStateOfBuildingOperator(BuildingOperator operator) {
        super(operator);
    }

    public static Point getOverPosition() {
        return overPosition;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);

        final ButtonBar buttonBar = operator.getButtonBar();
        final Point location = buttonBar.getLocation();
        final Dimension size = buttonBar.getSize();
        final Rectangle rectangle = new Rectangle(location.x, location.y, size.width, size.height);
        rectangle.grow(15, 15);
//        System.out.println("--------");
//        System.out.println(rectangle.x + "->" + (rectangle.x + rectangle.width) + " ---" + MouseInfo.getPointerInfo().getLocation().x);
//        System.out.println(rectangle.y + "->" + (rectangle.y + rectangle.height) + " ---" + MouseInfo.getPointerInfo().getLocation().y);
        if (!rectangle.contains(MouseInfo.getPointerInfo().getLocation())) {
            buttonBar.setVisible(false);
            operator.setCurrentState(BuildingOperator.SEARCHING);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        operator.getButtonBar().setVisible(false);
    }
}

