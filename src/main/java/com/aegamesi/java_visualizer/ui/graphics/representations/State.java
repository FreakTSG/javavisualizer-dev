package com.aegamesi.java_visualizer.ui.graphics.representations;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

abstract public class State<TOperator extends Operator> implements MouseInputListener {
    protected static Robot robot;
    protected TOperator operator;

    public State(TOperator operator) {
        this.operator = operator;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
//        init();
    }

    public void init() {

    }

    public void dispose() {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        operator.getCanvas().updateMousePosition(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
