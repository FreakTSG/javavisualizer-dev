package com.aegamesi.java_visualizer.ui.operators;


import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;

abstract public class Operator implements MouseInputListener {
    protected static Canvas canvas;

    protected HashMap<Integer, State> states;
    protected State currentState;

    public Operator(Canvas canvas) {
        this.canvas = canvas;
        states = new HashMap<>();
    }

    public static Canvas getCanvas() {
        return canvas;
    }

    public void setCurrentState(int stateKey) {
//        System.out.println("MUDEI DE ESTADO = " + stateKey);
        if (currentState != null) {
            currentState.dispose();
        }
        currentState = states.get(stateKey);
        currentState.init();
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
        currentState.init();
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
