package com.aegamesi.java_visualizer.ui.graphics;

import java.awt.*;

public class RectangularGraphicElement extends PositionalGraphicElement {
    private static final long serialVersionUID = 1L;

    protected Dimension dimension;
    protected Color backgroundColor;
    protected boolean borderShown;

    public RectangularGraphicElement(Point position, Dimension dimension) {
        this(position, dimension, Color.BLACK, TRANSPARENT_COLOR);
    }

    public RectangularGraphicElement(Point position, Dimension dimension, Color borderColor, Color backgroundColor) {
        super(position, borderColor);
        this.dimension = dimension;
        this.backgroundColor = backgroundColor;
        borderShown = true;
    }

    public boolean isBorderShown() {
        return borderShown;
    }

    public void setBorderShown(boolean borderShown) {
        this.borderShown = borderShown;
    }

    @Override
    public void paint(Graphics g) {
        if (!borderShown) {
            return;
        }
        super.paint(g);
        final Color color = g.getColor();
        g.setColor(backgroundColor);
        g.fillRect(position.x, position.y, dimension.width, dimension.height);
        g.setColor(borderColor);
        g.drawRect(position.x, position.y, dimension.width, dimension.height);
        g.setColor(color);

    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public PositionalGraphicElement getPositionalGraphicElement(Point position) {
        return new Rectangle(this.position.x, this.position.y, dimension.width, dimension.height).contains(position) ? this : null;
    }

}
