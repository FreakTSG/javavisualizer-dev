package com.aegamesi.java_visualizer.ui.graphics.aggregations;

import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.graphics.RectangularGraphicElement;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class AggregateRectangularGraphicElement extends RectangularGraphicElement {
    private static final long serialVersionUID = 1L;

    //    public static final Color TOOLTIP_BG_COLOR = new Color(223, 223, 223, 173);
    public static final Color TOOLTIP_BG_COLOR = TRANSPARENT_COLOR;
    protected AggregateRectangularGraphicElement parent;

    public AggregateRectangularGraphicElement(Point position, Dimension dimension) {
        this(position, dimension, Color.BLACK, TRANSPARENT_COLOR);
    }

    public AggregateRectangularGraphicElement(Point position, Dimension dimension, Color borderColor, Color backgroundColor) {
        super(position, dimension, borderColor, backgroundColor);
    }

    public AggregateRectangularGraphicElement getParent() {
        return parent;
    }

    public void setParent(AggregateRectangularGraphicElement parent) {
        this.parent = parent;
    }

    public boolean contains(AggregateRectangularGraphicElement aggregateRectangularGraphicElement) {
        while (aggregateRectangularGraphicElement != null) {
            if (aggregateRectangularGraphicElement == this) {
                return true;
            }
            aggregateRectangularGraphicElement = aggregateRectangularGraphicElement.getParent();
        }
        return false;
    }

    protected void drawToolTipText(String text, Graphics g) {
        Dimension parentDimension = parent.getDimension();
        int x = position.x + parentDimension.width;
        int y = position.y + ConstantsIDS.FONT_SIZE_TEXT;

        Color oldColor = g.getColor();

        FontMetrics fm = g.getFontMetrics();
        Rectangle2D rect = fm.getStringBounds(text, g);

        g.setColor(TOOLTIP_BG_COLOR);
        g.fillRect(x,
                y - fm.getAscent(),
                (int) rect.getWidth(),
                (int) rect.getHeight());

        g.setColor(Color.BLACK);
        g.drawString(text, x, y);
        g.setColor(oldColor);
    }

}
