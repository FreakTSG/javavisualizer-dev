package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ColecaoIteravelLinear;
import com.aegamesi.java_visualizer.model.HeapObject;
import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.CConnection;
import com.aegamesi.java_visualizer.ui.graphics.GraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.StraightConnection;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.ContainerWithoutInConnectors;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.FieldReference;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.NormalTextElement;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.nodes.SimpleNodeRepresentation;
import com.aegamesi.java_visualizer.utils.Utils;
import com.github.weisj.jsvg.S;

import javax.swing.*;
import java.awt.*;

public abstract class CircularSimpleLinkedListWithBaseRepresentation<TLinkedList extends
        ColecaoIteravelLinear, TNode> extends CircularLinkedListWithBaseRepresentation<TLinkedList, SimpleNodeRepresentation> implements
        SimpleLinkedListRepresentation<TLinkedList> {
    private static final long serialVersionUID = 1L;


    public CircularSimpleLinkedListWithBaseRepresentation(Point position, TLinkedList owner, MyCanvas myCanvas) {
        super(position, owner, myCanvas, true);
    }

    public void init() {
        super.init();
        container.setInnerCellSpacing(8);
//        container.setLeftCellSpacing(10);
        container.setRightCellSpacing(8);
//        container.setTopCellSpacing(17);
//        container.setBottomCellSpacing(10);
        try {

            baseFieldReference = new FieldReference(owner, Utils.getField(owner, ConstantsIDS.BASE), Location.CENTER, false);
            leftContainer.add(baseFieldReference, Location.RIGHT);
            createTailOrComparatorReferences();
            numberOfElementsReference = new NormalTextElement(String.valueOf(Utils.getFieldValue(owner, ConstantsIDS.NUMBER_OF_ELEMENTS)), ConstantsIDS.FONT_SIZE_TEXT);
            leftContainer.add(numberOfElementsReference, Location.CENTER);
            leftContainer.setBorderShown(false);
            container.add(leftContainer);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao Criar Representação de Lista Simples.");
        }
        update();
    }

    protected abstract void createTailOrComparatorReferences();


    public void update() {
        super.update();
        actualIndex = -1;
        createNode(baseFieldReference);
    }


    private void createNode(FieldReference nodeFieldReference) {
        TNode node = (TNode) nodeFieldReference.getFieldValue();
        SimpleNodeRepresentation<TNode> simpleNodeRepresentation = new SimpleNodeRepresentation<>(new Point(), node, myCanvas);
        ContainerWithoutInConnectors nodeAndIndexContainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), false);
        nodeAndIndexContainer.add(simpleNodeRepresentation.getContainer());
        nodeAndIndexContainer.setInnerCellSpacing(0);
        NormalTextElement indexNormalTextElement = new NormalTextElement(actualIndex == -1 ? getBaseNodeIndexText() : String.valueOf(actualIndex), ConstantsIDS.FONT_SIZE_INDEX);
        simpleNodeRepresentation.setIndex(actualIndex);
        actualIndex++;
        indexNormalTextElement.setBackgroundColor(GraphicElement.TRANSPARENT_COLOR);
        indexNormalTextElement.setBorderShown(false);
        nodeAndIndexContainer.add(indexNormalTextElement);
        nodeAndIndexContainer.setBorderShown(false);
        container.add(nodeAndIndexContainer);

        nodeRepresentationByOwner.put(node, simpleNodeRepresentation);
        addNewConnection(new StraightConnection(nodeFieldReference.getOutConnector(), simpleNodeRepresentation, Color.RED));

        // Connect the node to its element
        FieldReference elementFieldReference = simpleNodeRepresentation.getElementFieldReference();
        System.out.println("Element field reference: " + elementFieldReference.getPosition());
        Object fieldObject = elementFieldReference.getFieldValue();
        System.out.println("Field object: " + fieldObject);
        if (fieldObject != null) {
            RepresentationWithInConnectors var = myCanvas.getRepresentationWithInConnectors(fieldObject);
            System.out.println("Var: " + var);

            if (var != null) {
                addNewConnection(new StraightConnection(elementFieldReference.getOutConnector(), var, ConstantsIDS.LINKED_LIST_ELEMENTS_CONNECTIONS_COLOR));
            } else {
                System.out.println("Target representation is null for field object: " + fieldObject);
            }
        } else {
            System.out.println("Field object is null for element field reference.");
        }

        // Then deal with the next node
        FieldReference nextFieldReference = simpleNodeRepresentation.getNextFieldReference();
        TNode nextNode = (TNode) nextFieldReference.getFieldValue();

        // Create connection from last to first (base)
        if (baseFieldReference.getFieldValue() == nextNode) {
            addNewConnection(new CConnection(nextFieldReference.getOutConnector(), nodeRepresentationByOwner.get(nextNode), Color.RED, 14, 21, nextNode == node));
        } else {
            createNode(nextFieldReference);
        }
    }
}
