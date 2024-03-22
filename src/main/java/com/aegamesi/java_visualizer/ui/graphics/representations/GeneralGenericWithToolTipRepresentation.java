package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.AggregateRectangularGraphicElementWithToolTip;

import java.awt.*;
import java.util.ArrayList;

public abstract class GeneralGenericWithToolTipRepresentation<T extends Object> extends GeneralGenericRepresentation<T> {
    private static final long serialVersionUID = 1L;

    //    protected boolean showToolTip;
    protected ArrayList<AggregateRectangularGraphicElementWithToolTip> aggregatesRectangularGraphicElementWithToolTip;

    public GeneralGenericWithToolTipRepresentation(Point position, T owner, MyCanvas myCanvas, boolean horizontal) {
        super(position, owner, myCanvas, horizontal);
//        showToolTip = false;
        aggregatesRectangularGraphicElementWithToolTip = new ArrayList<>();
    }

    public void setShowToolTip(boolean showToolTip) {
//        this.showToolTip = showToolTip;
        for (AggregateRectangularGraphicElementWithToolTip aggregateRectangularGraphicElementWithToolTip : aggregatesRectangularGraphicElementWithToolTip) {
            aggregateRectangularGraphicElementWithToolTip.setShowToolTipText(showToolTip);
        }
    }
}
