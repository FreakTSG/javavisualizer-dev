package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.NormalTextElement;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;

import java.awt.*;

public class PrimitiveOrEnumRepresentation<TPrimitive extends Object> extends GeneralGenericRepresentation<TPrimitive> {
    private static final long serialVersionUID = 1L;


    public PrimitiveOrEnumRepresentation(Point position, TPrimitive owner, MyCanvas myCanvas) {
        this(position, owner, myCanvas, false);
    }

    public PrimitiveOrEnumRepresentation(Point position, TPrimitive owner, MyCanvas myCanvas, boolean horizontal) {
        super(position, owner, myCanvas, horizontal);
        update();
    }

    @Override
    public void init() {

    }

    public void update() {
        container.removeAllGraphicElements();
        container.add(new NormalTextElement(String.valueOf(owner), ConstantsIDS.FONT_SIZE_TEXT), Location.LEFT);
    }

    @Override
    public void edit(Point position) {
    }


}
