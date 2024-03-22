package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.ui.MyButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class ButtonBar extends JDialog {
    public static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
    private final JPanel topPanel;
    private final JPanel bottomPanel;
    private int buttonSize = 50;
    private int tamanho;
    private HashMap<String, JButton> buttonByKey;
    private static final int SPACE_BETWEEN_BUTTONS = 12;

    public ButtonBar() {

        setUndecorated(true);
        setBackground(TRANSPARENT_COLOR);

        final BorderLayout manager = new BorderLayout();
        setLayout(manager);
        manager.setHgap(0);
        manager.setVgap(0);

        topPanel = new JPanel();
        bottomPanel = new JPanel();
        final FlowLayout layout1 = new FlowLayout(FlowLayout.LEFT, 0, 0);
        final FlowLayout layout2 = new FlowLayout(FlowLayout.LEFT, 0, 0);
        layout1.setHgap(0);
        layout2.setHgap(0);
        layout1.setVgap(0);
        layout2.setVgap(0);
        topPanel.setLayout(layout1);
        bottomPanel.setLayout(layout2);
        topPanel.setBackground(TRANSPARENT_COLOR);
        bottomPanel.setBackground(TRANSPARENT_COLOR);


        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);

//        setSize(500, 100);

        buttonByKey = new HashMap<>();

    }

    public JButton getButton(String key) {
        return buttonByKey.get(key);
    }

    //para as project entities
    public void add(int row, String key, String imagePath, String toolTipText, ActionListener actionListener) {
        tamanho++;
        JButton button = null;
        if (imagePath == null) {
            button = new MyButton(toolTipText);
            FontMetrics fontMetrics = button.getFontMetrics(button.getFont());
            Rectangle2D stringBounds = fontMetrics.getStringBounds(toolTipText, getGraphics());
            button.setPreferredSize(new Dimension((int) stringBounds.getWidth() + SPACE_BETWEEN_BUTTONS, (int) stringBounds.getHeight()));
//            button.setMargin(new Insets(0, 0, 0, 0));
//        } else {
//            button = new JButton(new ImageIcon(Resources.INSTANCE.getImage(imagePath)));
//            button.setPreferredSize(new Dimension(30, 30)); //record
        }
        button.setToolTipText(toolTipText);
        button.addActionListener(actionListener);
        prepareButton(row, key, button);
        pack();
    }

    //botões lista, array, th, etc
    public void add(int row, String key, JButton button) {
        add(row, key, button, buttonSize);
//        button.setPreferredSize(new Dimension(buttonSize, buttonSize));
//        prepareButton(row, key, button);
//        pack();
    }

    //record
    public void add(int row, String key, JButton button, int buttonSize) {
        if (buttonSize > 0) {
            button.setPreferredSize(new Dimension(buttonSize, buttonSize));
        }
        FontMetrics fontMetrics = button.getFontMetrics(button.getFont());
        if (!button.getText().equals("")) {
            Rectangle2D stringBounds = fontMetrics.getStringBounds(button.getText(), getGraphics());
            button.setPreferredSize(new Dimension((int) stringBounds.getWidth() + SPACE_BETWEEN_BUTTONS, (int) stringBounds.getHeight()));
        }
        prepareButton(row, key, button);
        pack();
    }

    private void prepareButton(int row, String key, JButton button) {
        button.setFocusable(false);
//        button.setBackground(TRANSPARENT_COLOR);
        button.setOpaque(false);


        button.setBorderPainted(false);
        button.setMargin(new Insets(0, -4, 0, -4));
        button.setBorder(null);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        if (row == 0) {
            topPanel.add(button);
        } else {
            bottomPanel.add(button);
        }
        buttonByKey.put(key, button);
    }

    public JPanel getTopPanel() {
        return topPanel;
    }

    @Override
    public String toString() {
        return "" + tamanho;
    }

    public void reset() {
//        for (JButton jButton : buttonByKey.values()) {
//            jButton.setText(jButton.getText());
//        }
    }
}
