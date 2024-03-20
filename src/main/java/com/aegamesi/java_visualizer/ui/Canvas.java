package com.aegamesi.java_visualizer.ui;


import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.nimbus.State;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class Canvas  extends JPanel implements MouseInputListener {
    protected static Canvas canvas;
    private static Canvas instance;
    private Operator currentOperator;
    private float zoom;
    private Point mousePosition = new Point(0, 0);

    protected HashMap<Integer, State> states;
    protected State currentState;

    public void Operator(Canvas canvas) {
        this.canvas = canvas;
        states = new HashMap<>();
    }
    private Canvas() {
            // Initialize the canvas
        }
        public static Canvas getCanvas() {
                if (instance == null) {
                    instance = new Canvas();
                }
                return instance;
            }


    public void setCurrentState(int stateKey) {
}
    private MouseEvent convertCoordinateToZoom(MouseEvent e) {
        int zoomX = (int) (e.getX() / zoom);
        int zoomY = (int) (e.getY() / zoom);
        e.translatePoint(-e.getX(), -e.getY());
        e.translatePoint(zoomX, zoomY);
        return e;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        currentOperator.mouseClicked(convertCoordinateToZoom(e));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentOperator.mousePressed(convertCoordinateToZoom(e));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currentOperator.mouseReleased(convertCoordinateToZoom(e));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        currentOperator.mouseEntered(convertCoordinateToZoom(e));
        currentOperator.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        currentOperator.mouseExited(convertCoordinateToZoom(e));
        currentOperator.mouseExited(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentOperator.mouseDragged(convertCoordinateToZoom(e));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        currentOperator.mouseMoved(convertCoordinateToZoom(e));
        updateMousePosition(e);
    }

    public void updateMousePosition(MouseEvent e) {
        mousePosition.setLocation(e.getX(), e.getY());
        repaint();
    }
}
