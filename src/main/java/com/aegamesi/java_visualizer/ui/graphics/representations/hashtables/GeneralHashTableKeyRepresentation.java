package com.aegamesi.java_visualizer.ui.graphics.representations.hashtables;

import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.representations.GeneralGenericRepresentation;

import java.awt.*;

public abstract class GeneralHashTableKeyRepresentation<TGeneralHashTableKey extends Object> extends GeneralGenericRepresentation<TGeneralHashTableKey> {
    private static final long serialVersionUID = 1L;

    public GeneralHashTableKeyRepresentation(Point position, TGeneralHashTableKey owner, MyCanvas myCanvas) {
        this(position, owner, myCanvas, false);
    }

    public GeneralHashTableKeyRepresentation(Point position, TGeneralHashTableKey owner, MyCanvas myCanvas, boolean horizontal) {
        super(position, owner, myCanvas, horizontal);
    }

}


