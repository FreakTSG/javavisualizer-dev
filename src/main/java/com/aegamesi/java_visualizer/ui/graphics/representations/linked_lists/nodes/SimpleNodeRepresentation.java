package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.nodes;

import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.ContainerWithInConnectors;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.FieldReference;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.SimpleNodeContainer;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.utils.Utils;

import javax.swing.*;
import java.awt.*;

public class SimpleNodeRepresentation<TNode> extends NodeRepresentation {
    private static final long serialVersionUID = 1L;


    private FieldReference elementFieldReference;
    private FieldReference nextFieldReference;

    public SimpleNodeRepresentation(Point position, TNode owner, MyCanvas myCanvas) {
        super(position, owner, myCanvas);
        container = new SimpleNodeContainer(position, new Dimension(0, 0), owner.getClass(), true);
        container.setCellSpacing(2);
//        container.setBorderShown(false);
        init();
    }

    public void init() {
        try {
            ContainerWithInConnectors containerWithInConnectors = ((ContainerWithInConnectors) this.container);
            int size = 10;
            elementFieldReference = new FieldReference(new Dimension(size, size), owner, Utils.getField(owner, ConstantsIDS.ELEMENT), Location.CENTER, false);
//            elementFieldReference.setBorderShown(false);
            nextFieldReference = new FieldReference(new Dimension(size, size), owner, Utils.getField(owner, ConstantsIDS.NEXT), Location.CENTER, false);

            containerWithInConnectors.add(elementFieldReference, Location.CENTER);
            containerWithInConnectors.add(nextFieldReference, Location.CENTER);

            containerWithInConnectors.computeInConnectors();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao Criar Representação de Nó Simples.");
        }

    }

    public FieldReference getElementFieldReference() {
        return elementFieldReference;
    }

    public FieldReference getNextFieldReference() {
        return nextFieldReference;
    }
}
