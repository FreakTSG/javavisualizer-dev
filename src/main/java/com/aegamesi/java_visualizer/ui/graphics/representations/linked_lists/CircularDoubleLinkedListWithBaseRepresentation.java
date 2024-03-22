package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ColecaoIteravelLinear;
import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.CConnection;
import com.aegamesi.java_visualizer.ui.graphics.StraightConnection;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.ContainerWithoutInConnectors;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.FieldReference;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.NormalTextElement;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.nodes.DoubleNodeRepresentation;
import com.aegamesi.java_visualizer.utils.Utils;

import javax.swing.*;
import java.awt.*;

public abstract class CircularDoubleLinkedListWithBaseRepresentation<TLinkedList extends
        ColecaoIteravelLinear, TNode> extends CircularLinkedListWithBaseRepresentation<TLinkedList, DoubleNodeRepresentation> implements
        DoubleLinkedListOrSortedHashTableRepresentation<TLinkedList> {

    private static final long serialVersionUID = 1L;


    public CircularDoubleLinkedListWithBaseRepresentation(Point position, TLinkedList owner, MyCanvas myCanvas) {
        super(position, owner, myCanvas, true);
    }

    public void init() {
        super.init();
        container.setInnerCellSpacing(8);
        container.setRightCellSpacing(8);
        try {

            baseFieldReference = new FieldReference(owner, Utils.getField(owner, ConstantsIDS.BASE), Location.CENTER, false);
            leftContainer.add(baseFieldReference, Location.RIGHT);
            createComparatorReference();

            numberOfElementsReference = new NormalTextElement(String.valueOf(Utils.getFieldValue(owner, ConstantsIDS.NUMBER_OF_ELEMENTS)), ConstantsIDS.FONT_SIZE_TEXT);
            leftContainer.add(numberOfElementsReference, Location.CENTER);
            leftContainer.setBorderShown(false);
            container.add(leftContainer);

//            update();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao Criar Representação de Lista Dupla.");
        }
    }

    protected void createComparatorReference(){

    };



    public void update() {
        super.update();
        actualIndex = -1;
        createNode(baseFieldReference);

        //connection from base node to tail node
        TNode baseNode = (TNode) baseFieldReference.getFieldValue();
        FieldReference tailNodeReference = nodeRepresentationByOwner.get(baseNode).getPreviousFieldReference();
        Object tailNode = tailNodeReference.getFieldValue();
        addNewConnection(new CConnection(tailNodeReference.getOutConnector(), nodeRepresentationByOwner.get(tailNode), Color.RED, 14, 21, tailNode == baseNode));
    }


    private void createNode(FieldReference nodeFieldReference) {
        TNode node = (TNode) nodeFieldReference.getFieldValue();

       // final LinkedListNodeWrapper nodeWrapper = new LinkedListNodeWrapper(node.getClass(), node.getClass().getName(), node, owner, actualIndex);
        DoubleNodeRepresentation doubleNodeRepresentation = new DoubleNodeRepresentation<>(new Point(), node, myCanvas);
        ContainerWithoutInConnectors nodeAndIndexContainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), false);
        nodeAndIndexContainer.add(doubleNodeRepresentation.getContainer());
        nodeAndIndexContainer.setInnerCellSpacing(0);
        nodeAndIndexContainer.setBackgroundColor(new Color(98, 178, 14));

        NormalTextElement indexNormalTextElement = new NormalTextElement(actualIndex == -1 ? getBaseNodeIndexText() : String.valueOf(actualIndex), ConstantsIDS.FONT_SIZE_INDEX);
        doubleNodeRepresentation.setIndex(actualIndex);
        actualIndex++;
//        indexNormalTextElement.setBackgroundColor(GraphicElement.TRANSPARENT_COLOR);
        indexNormalTextElement.setBackgroundColor(new Color(196, 196, 244));
        indexNormalTextElement.setBorderShown(false);
        nodeAndIndexContainer.add(indexNormalTextElement);
        nodeAndIndexContainer.setBorderShown(false);
        container.add(nodeAndIndexContainer);

        nodeRepresentationByOwner.put(node, doubleNodeRepresentation);
        addNewConnection(new StraightConnection(nodeFieldReference.getOutConnector(), doubleNodeRepresentation, Color.RED));

        // Connect the node to its element
        FieldReference elementFieldReference = doubleNodeRepresentation.getElementFieldReference();
        Object fieldObject = elementFieldReference.getFieldValue();
        if (fieldObject != null) {
            addNewConnection(new StraightConnection(elementFieldReference.getOutConnector(), myCanvas.getRepresentationWithInConnectors(fieldObject), ConstantsIDS.LINKED_LIST_ELEMENTS_CONNECTIONS_COLOR));
        }

        // Then deal with the next node
        FieldReference nextFieldReference = doubleNodeRepresentation.getNextFieldReference();
        TNode nextNode = (TNode) nextFieldReference.getFieldValue();


        //create connection from last to first(base)
        if (baseFieldReference.getFieldValue() == nextNode) {
            addNewConnection(new CConnection(nextFieldReference.getOutConnector(), nodeRepresentationByOwner.get(nextNode), Color.RED, 14, 21, nextNode == node));
        } else {
            createNode(nextFieldReference);
            DoubleNodeRepresentation nextNodeRepresentation = nodeRepresentationByOwner.get(nextNode);
            addNewConnection(new StraightConnection(nextNodeRepresentation.getPreviousFieldReference().getOutConnector(), doubleNodeRepresentation, Color.RED));
        }
    }

}
