package com.aegamesi.java_visualizer.ui.graphics.aggregations;

import java.awt.*;

public class NormalTextElement extends TextElement {
    private static final long serialVersionUID = 1L;


    private String toolTipText;

    public NormalTextElement(String text, int fontSize) {
        this(new Point(), new Dimension(30, 30), text, fontSize, "");
    }

    public NormalTextElement(String text, int fontSize, Color backgroundColor) {
        this(new Point(), new Dimension(30, 30), text, fontSize, backgroundColor);
    }

    public NormalTextElement(String text, int fontSize, String toolTipText) {
        this(new Point(), new Dimension(30, 30), text, fontSize, toolTipText);
    }

    public NormalTextElement(Point position, Dimension dimension, String text, int fontSize) {
        this(position, dimension, text, fontSize, "");
    }

    public NormalTextElement(Point position, Dimension dimension, String text, int fontSize, String toolTipText) {
        super(position, dimension, text, fontSize);
        this.toolTipText = toolTipText;
    }

    public NormalTextElement(Point position, Dimension dimension, String text, int fontSize, Color backgroundColor) {
        super(position, dimension, text, fontSize, backgroundColor);
    }

    public NormalTextElement(String text, int fontSize, String toolTipText, Color backgroundColor) {
        super(new Point(), new Dimension(30, 30), text, fontSize, backgroundColor);
        this.toolTipText = toolTipText;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (toolTipManager.isShowToolTipText()) {
            drawToolTipText(toolTipText, g);
//            g.drawString(toolTipText, position.x + dimension.width + 10, position.y + Constants.FONT_SIZE_TEXT);
        }
    }
}
