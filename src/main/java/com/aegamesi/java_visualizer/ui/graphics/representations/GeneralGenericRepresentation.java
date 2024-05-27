package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.utils.Utils;

import java.awt.*;
import java.lang.reflect.Field;

abstract public class GeneralGenericRepresentation<T extends Object> extends GeneralRepresentation<T> {
    private static final long serialVersionUID = 1L;


    public GeneralGenericRepresentation(Point position, T owner, MyCanvas myCanvas) {
        this(position, owner, myCanvas, false);
    }

    public GeneralGenericRepresentation(Point position, T owner, MyCanvas myCanvas, boolean horizontal) {
        super(position, owner, myCanvas, horizontal);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Point position = new Point(getPosition());
        position.translate(0, -3);
        Color oldColor = g.getColor();
        g.setColor(Color.BLACK);
        // Acessar o campo `label` do owner
        String label = getLabelFromOwner(owner);

        // Desenhar a label em vez do nome da classe
        if (label == "Unknown"){
            g.drawString(Utils.getClassSimpleName(owner.getClass().getName()), position.x, position.y);
        }else {
            g.drawString(label, position.x, position.y);
        }

        g.setColor(oldColor);
    }

    // Método utilitário para obter a label do owner
    private String getLabelFromOwner(Object owner) {
        try {
            // Primeiro, tentamos obter o campo 'label' da classe do objeto
            Field labelField = owner.getClass().getDeclaredField("label");
            labelField.setAccessible(true);
            return (String) labelField.get(owner);
        } catch (NoSuchFieldException e) {
            // Se o campo 'label' não for encontrado, tentamos obter da superclasse
            try {
                Field labelField = owner.getClass().getSuperclass().getDeclaredField("label");
                labelField.setAccessible(true);
                return (String) labelField.get(owner);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                return "Unknown";
            }
        } catch (IllegalAccessException e) {
            return "Unknown";
        }
    }

}
