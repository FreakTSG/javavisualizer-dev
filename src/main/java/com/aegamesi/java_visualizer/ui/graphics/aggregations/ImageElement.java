package com.aegamesi.java_visualizer.ui.graphics.aggregations;

import javax.swing.*;
import java.awt.*;

public class ImageElement extends AggregateRectangularGraphicElement {
    private static final long serialVersionUID = 1L;

    private final ImageIcon imageIcon;

    public ImageElement(Point position, Dimension dimension, Image image) {
        super(position, dimension);
        this.imageIcon = new ImageIcon(image);
        setBorderColor(TRANSPARENT_COLOR);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(imageIcon.getImage(), position.x, position.y, null);
    }


}
