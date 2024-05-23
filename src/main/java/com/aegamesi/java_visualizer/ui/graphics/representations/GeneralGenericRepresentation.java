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
        System.out.println("O owner tem" + owner.getClass());
        // Desenhar a label em vez do nome da classe
        if (label != null) {
            // Desenhar a label em vez do nome da classe
            if (label.equals("Unknown")) {
                System.out.println("Estou a passar por aqui Unknown");
                g.drawString(Utils.getClassSimpleName(owner.getClass().getName()), position.x, position.y);
            } else {
                g.drawString(label, position.x, position.y);
            }
        } else {
            // Se o campo `label` não estiver presente, desenhar o nome da classe do owner
            g.drawString(Utils.getClassSimpleName(owner.getClass().getName()), position.x, position.y);
        }

        g.setColor(oldColor);
    }

    // Método utilitário para obter a label do owner
    private String getLabelFromOwner(Object owner) {
        try {
            // Tentamos obter o campo 'label' da classe do objeto
            Field labelField = null;
            Class<?> clazz = owner.getClass();
            while (clazz != null) {
                try {
                    labelField = clazz.getDeclaredField("label");
                    labelField.setAccessible(true);
                    break; // Se encontrarmos o campo, saímos do loop
                } catch (NoSuchFieldException e) {
                    // Se o campo não for encontrado, continuamos procurando na superclasse
                    clazz = clazz.getSuperclass();
                }
            }

            // Se labelField for null, significa que o campo 'label' não foi encontrado em nenhuma classe na hierarquia
            if (labelField == null) {
                System.out.println("Campo 'label' não encontrado na classe do objeto ou em suas superclasses.");
                return "Unknown"; // Retornamos um valor padrão
            }

            // Se chegamos aqui, encontramos o campo 'label' e podemos obtê-lo
            return (String) labelField.get(owner);
        } catch (IllegalAccessException e) {
            // Lidamos com exceções de acesso ilegal aqui
            e.printStackTrace();
            return "Unknown";
        }
    }

}
