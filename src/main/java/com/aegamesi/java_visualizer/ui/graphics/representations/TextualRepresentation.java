package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.NormalTextElement;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;

import java.awt.*;

public class TextualRepresentation<T extends Object> extends GeneralRepresentation<T> {
    private static final long serialVersionUID = 1L;


    public TextualRepresentation(Point position, T owner, MyCanvas myCanvas) {
        this(position, owner, myCanvas, false);
    }

    public TextualRepresentation(Point position, T owner, MyCanvas myCanvas, boolean horizontal) {
        super(position, owner, myCanvas, horizontal);
    }

    public void init() {
        container.add(new NormalTextElement(owner.toString(), ConstantsIDS.FONT_SIZE_TEXT), Location.LEFT);
        container.setBorderShown(false);
        container.setOutterCellSpacing(0);
    }


}
