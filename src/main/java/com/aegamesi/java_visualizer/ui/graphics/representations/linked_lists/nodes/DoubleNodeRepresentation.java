package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.nodes;

import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.ContainerWithInConnectors;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.DoubleNodeContainer;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.FieldReference;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.utils.Utils;

import javax.swing.*;
import java.awt.*;

public class DoubleNodeRepresentation <TNode> extends  NodeRepresentation {
    private static final long serialVersionUID = 1L;

    private FieldReference previousFieldReference;
    private FieldReference elementFieldReference;
    private FieldReference nextFieldReference;

    public DoubleNodeRepresentation(Point position, TNode owner, MyCanvas myCanvas) {
        super(position, owner, myCanvas );
        container = new DoubleNodeContainer(position, new Dimension(0, 0), owner.getClass(), true);
        container.setCellSpacing(2);
        init();
    }

    public void init() {
        try {
            ContainerWithInConnectors containerWithInConnectors = ((ContainerWithInConnectors) this.container);
            int size = 10;
            previousFieldReference = new FieldReference(new Dimension(size, size), owner, Utils.getField(owner, ConstantsIDS.PREVIOUS), Location.CENTER, false);
            elementFieldReference = new FieldReference(new Dimension(size, 2 * size), owner, Utils.getField(owner, ConstantsIDS.ELEMENT), Location.CENTER, false);
            nextFieldReference = new FieldReference(new Dimension(size, size), owner, Utils.getField(owner, ConstantsIDS.NEXT), Location.CENTER, false);

            containerWithInConnectors.setBottomCellSpacing(containerWithInConnectors.getBottomCellSpacing() + 1);
            containerWithInConnectors.setTopCellSpacing(containerWithInConnectors.getTopCellSpacing() + 1);
            containerWithInConnectors.add(previousFieldReference, Location.BOTTOM);
            containerWithInConnectors.add(elementFieldReference, Location.CENTER);
            containerWithInConnectors.add(nextFieldReference, Location.TOP);

            containerWithInConnectors.computeInConnectors();

        } catch (Exception e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(null, "Erro ao Criar Representação de Nó Duplo.");

        }
    }

    public FieldReference getPreviousFieldReference() {
        return previousFieldReference;
    }

    public FieldReference getElementFieldReference() {
        return elementFieldReference;
    }

    public FieldReference getNextFieldReference() {
        return nextFieldReference;
    }
}
