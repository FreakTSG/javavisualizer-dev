package com.aegamesi.java_visualizer.ui.graphics.aggregations;


import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;

public abstract class TextElement extends AggregateRectangularGraphicElement implements AggregateRectangularGraphicElementWithToolTip {
    private static final long serialVersionUID = 1L;

    public static final FontRenderContext FRC = new FontRenderContext(null, false, false);
    //    private static final Font FONT = new Font("Lucida Sans", Font.BOLD, 14);
    private static final int PADDING = 4;
    protected final ToolTipManager toolTipManager;
    protected String text;
    protected Font FONT;

    public TextElement(String text, int fontSize) {
        this(new Point(), new Dimension(30, 30), text, fontSize);
    }

    public TextElement(Point position, Dimension dimension, String text, int fontSize) {
        this(position, dimension, text, fontSize, new Color(102, 215, 241));
    }

    public TextElement(Point position, Dimension dimension, String text, int fontSize, Color backgroundColor) {
        super(position, dimension, Color.BLACK, backgroundColor);
//        this.text = Utils.toUTF8(text);
//        FONT = new Font("Monospaced", Font.BOLD, fontSize);
        FONT = new Font("Monospaced", Font.PLAIN, fontSize);
//        dimension.width = getTextWidth();
        setText(text);
        toolTipManager = new ToolTipManager();
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        dimension.height = getTextHeight();
        dimension.width = getTextWidth() + PADDING;
    }

    public void setShowToolTipText(boolean showToolTipText) {
        toolTipManager.setShowToolTipText(showToolTipText);
    }

    @Override
    public void paint(Graphics g) {
        Font oldFont = g.getFont();
        g.setFont(FONT);
        super.paint(g);

        FontMetrics fontMetrics = g.getFontMetrics(FONT);
        int descent = fontMetrics.getDescent();
        Color oldColor = g.getColor();
        g.setColor(Color.BLACK);
        g.drawString(text, position.x + PADDING - 1, position.y + dimension.height - descent);
        g.setFont(oldFont);
        g.setColor(oldColor);
    }

    private int getTextHeight() {
        JComponent jComponent = new JPanel();
        FontMetrics fontMetrics = jComponent.getFontMetrics(FONT);
        return fontMetrics.getHeight();
    }

    private int getTextWidth() {
        return (int) FONT.getStringBounds(text, FRC).getWidth();
    }

}
