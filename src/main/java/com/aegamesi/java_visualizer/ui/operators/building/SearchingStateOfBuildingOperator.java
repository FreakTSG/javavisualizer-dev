package ui.operators.building;

import ui.IDSToolWindow;
import ui.graphics.representations.GeneralGenericWithToolTipRepresentation;
import ui.graphics.representations.Representation;

import java.awt.*;
import java.awt.event.MouseEvent;

public class SearchingStateOfBuildingOperator extends AppearingButtonBarStateOfBuildingOperator {

    GeneralGenericWithToolTipRepresentation lastGeneralGenericWithToolTipRepresentation;
    public SearchingStateOfBuildingOperator(BuildingOperator operator) {
        super(operator);
    }

    @Override
    public void init() {
        super.init();
        operator.setButtonBar(IDSToolWindow.getButtonBar(IDSToolWindow.EMPTY_AREA_BUTTON_BAR));
        canvas.setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        Representation sourceRepresentation = operator.getSourceRepresentation();
        if (sourceRepresentation != null) {
            operator.setCurrentState(BuildingOperator.SELECTING);
            if (sourceRepresentation instanceof GeneralGenericWithToolTipRepresentation) {
                lastGeneralGenericWithToolTipRepresentation = (GeneralGenericWithToolTipRepresentation) sourceRepresentation;
                lastGeneralGenericWithToolTipRepresentation.setShowToolTip(true);
            }
        } else {
            if (lastGeneralGenericWithToolTipRepresentation != null) {
                lastGeneralGenericWithToolTipRepresentation.setShowToolTip(false);
                lastGeneralGenericWithToolTipRepresentation = null;
            }
        }
    }
}

