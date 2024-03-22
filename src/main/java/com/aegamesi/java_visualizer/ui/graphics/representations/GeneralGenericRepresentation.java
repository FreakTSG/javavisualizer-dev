package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.ui.MyCanvas;

import java.awt.*;

abstract public class GeneralGenericRepresentation<T extends Object> extends GeneralRepresentation<T> {
    private static final long serialVersionUID = 1L;


    public GeneralGenericRepresentation(Point position, T owner, MyCanvas myCanvas) {
        this(position, owner, myCanvas, false);
    }

    public GeneralGenericRepresentation(Point position, T owner, MyCanvas myCanvas, boolean horizontal) {
        super(position, owner, myCanvas, horizontal);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Point position = new Point(getPosition());
        position.translate(0, -3);
        Color oldColor = g.getColor();
        g.setColor(Color.BLACK);
        //TODO linha abaixo sem erros
//        g.drawString(Utils.getClassSimpleName(owner.getValueTypeName()), position.x, position.y);
        g.setColor(oldColor);
    }

}
