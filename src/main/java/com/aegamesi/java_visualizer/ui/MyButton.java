package com.aegamesi.java_visualizer.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class MyButton extends JButton {

    public static final Font FONT = new Font("SansSerif", Font.BOLD, 12);
    private final int xPadding = 10;


    public MyButton() {
        super();
        this.init();
    }

    public MyButton(String text) {
        super(text);
        this.init();
    }

    private void init() {
//        this.setFont(new Font("Arial", Font.PLAIN, 16));
        this.setForeground(Color.BLACK);

        this.setContentAreaFilled(false);
        this.setBorderPainted(false);

        this.setBackground(Color.WHITE);
        this.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2d.setRenderingHints(hints);
        g2d.setColor(getBackground());
        g2d.setFont(FONT);
        g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
        g2d.setColor(getForeground());
        super.paintComponent(g2d);
        g2d.dispose();
    }

}