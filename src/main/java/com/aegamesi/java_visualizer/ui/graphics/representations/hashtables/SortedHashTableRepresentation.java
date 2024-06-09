package com.aegamesi.java_visualizer.ui.graphics.representations.hashtables;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.Associacao;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.TabelaHashComIncrementoPorHash;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.TabelaHashOrdenada;
import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.CConnection;
import com.aegamesi.java_visualizer.ui.graphics.GraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.StraightConnection;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.ContainerWithoutInConnectors;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.FieldReference;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.NormalTextElement;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.ui.graphics.representations.*;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.DoubleLinkedListOrSortedHashTableRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.nodes.DoubleNodeRepresentation;
import com.aegamesi.java_visualizer.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashMap;


public class SortedHashTableRepresentation
        extends CollectionRepresentation<TabelaHashOrdenada>
        implements DoubleLinkedListOrSortedHashTableRepresentation {

    private static final long serialVersionUID = 1L;

    private SortedInnerHashTableRepresentation sortedInnerHashTableRepresentation;
    private FieldReference baseFieldReference;
    private FieldReference comparatorFieldReference;
    private FieldReference hashTableFieldReference;
    private int actualIndex;
    private HashMap<Object, DoubleNodeRepresentation> nodeRepresentationByOwner;
    private ContainerWithoutInConnectors topContainer;
    private ContainerWithoutInConnectors listContainer;
    private ContainerWithoutInConnectors listAndAssociationsContainer;
    private ContainerWithoutInConnectors associationsContainer;

    public SortedHashTableRepresentation(Point position, TabelaHashOrdenada sortedHashTable, MyCanvas canvas) {
        super(position, sortedHashTable, canvas, false);
    }

    @Override
    public void init() {
        super.init();
        nodeRepresentationByOwner = new HashMap<>();
        update();
    }

    @Override
    protected void update() {
        super.update();
        nodeRepresentationByOwner.clear();
        container.setTopCellSpacing(myCanvas.isCompactMode() ? 12 : 1);
        container.setInnerCellSpacing(0);

        TabelaHashOrdenada ownerValue = owner;

        topContainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), true);
        topContainer.setCellSpacing(0);
        topContainer.setInnerCellSpacing(20);
        topContainer.setBorderShown(false);

        ContainerWithoutInConnectors topLeftContainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), false);
        topLeftContainer.setCellSpacing(0);
        topLeftContainer.setBorderShown(false);
        topContainer.add(topLeftContainer);

        int dim = 14;
        baseFieldReference = new FieldReference(new Dimension(dim, dim), owner, Utils.getField(ownerValue, ConstantsIDS.BASE), Location.CENTER, false);
        topLeftContainer.add(baseFieldReference, Location.LEFT);

        listAndAssociationsContainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), false);
        listAndAssociationsContainer.setInnerCellSpacing(20);
        listAndAssociationsContainer.setTopCellSpacing(0);
        listAndAssociationsContainer.setBottomCellSpacing(0);
        listAndAssociationsContainer.setBorderShown(false);
        if (!myCanvas.isCompactMode()) {
            associationsContainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), true);
            associationsContainer.setBorderShown(false);
            listAndAssociationsContainer.add(associationsContainer);
        }
        listContainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), true);
        listContainer.setTopCellSpacing(0);
        listContainer.setLeftCellSpacing(0);
        listContainer.setRightCellSpacing(0);
        listContainer.setBorderShown(false);
        listAndAssociationsContainer.add(listContainer);
        topContainer.add(listAndAssociationsContainer);

        container.add(topContainer, Location.LEFT);

        comparatorFieldReference = new FieldReference(new Dimension(dim, dim), owner, Utils.getField(ownerValue, ConstantsIDS.COMPARATOR_BY_ORDER), Location.CENTER, false);
        topLeftContainer.add(comparatorFieldReference, Location.LEFT);
        hashTableFieldReference = new FieldReference(new Dimension(dim, dim), owner, Utils.getField(ownerValue, ConstantsIDS.NO_POR_CHAVE), Location.CENTER, false);
        topLeftContainer.add(hashTableFieldReference, Location.LEFT);

        actualIndex = -1;
        createNode(baseFieldReference);

        TabelaHashOrdenada.No baseNode = (TabelaHashOrdenada.No) baseFieldReference.getFieldValue();
        FieldReference tailNodeReference = nodeRepresentationByOwner.get(baseNode).getPreviousFieldReference();
        Object tailNode = tailNodeReference.getFieldValue();
        addNewConnection(new CConnection(tailNodeReference.getOutConnector(), nodeRepresentationByOwner.get(tailNode), Color.RED, 14, 21, tailNode == baseNode));

        RepresentationWithInConnectors representationWithInConnectors = myCanvas.getRepresentationWithInConnectors(ownerValue.getComparador());
        addNewConnection(new StraightConnection(comparatorFieldReference.getOutConnector(), representationWithInConnectors, Color.RED));

        TabelaHashComIncrementoPorHash noPorChave = (TabelaHashComIncrementoPorHash) Utils.getFieldValue(ownerValue, ConstantsIDS.NO_POR_CHAVE);
        sortedInnerHashTableRepresentation = new SortedInnerHashTableRepresentation(new Point(), noPorChave, this, myCanvas);

        // Ensure that the inner hash table representation is added to the top container
        listContainer.add(sortedInnerHashTableRepresentation.getContainer(), Location.LEFT);

        container.update();
        myCanvas.repaint();
    }

    private void createNode(FieldReference nodeFieldReference) {
        TabelaHashOrdenada.No node = (TabelaHashOrdenada.No) nodeFieldReference.getFieldValue();

        DoubleNodeRepresentation<TabelaHashOrdenada.No> doubleNodeRepresentation =
                new DoubleNodeRepresentation<>(new Point(), node, myCanvas);
        ContainerWithoutInConnectors nodeAndIndexContainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), false);
        nodeAndIndexContainer.add(doubleNodeRepresentation.getContainer());
        nodeAndIndexContainer.setInnerCellSpacing(0);
        NormalTextElement indexNormalTextElement = new NormalTextElement(actualIndex == -1 ? getBaseNodeIndexText() : String.valueOf(actualIndex), ConstantsIDS.FONT_SIZE_INDEX);
        doubleNodeRepresentation.setIndex(actualIndex);
        actualIndex++;
        indexNormalTextElement.setBackgroundColor(GraphicElement.TRANSPARENT_COLOR);
        indexNormalTextElement.setBorderShown(false);
        nodeAndIndexContainer.add(indexNormalTextElement);
        nodeAndIndexContainer.setBorderShown(false);
        listContainer.add(nodeAndIndexContainer);

        nodeRepresentationByOwner.put(node, doubleNodeRepresentation);
        addNewConnection(new StraightConnection(nodeFieldReference.getOutConnector(), doubleNodeRepresentation, Color.RED));

        FieldReference elementFieldReference = doubleNodeRepresentation.getElementFieldReference();
        Associacao associacao = (Associacao) elementFieldReference.getFieldValue();
        if (associacao != null) {
            if (!myCanvas.isCompactMode()) {
                AssociationRepresentation associationRepresentation = new AssociationRepresentation(new Point(), associacao, myCanvas);
                associationsContainer.add(associationRepresentation.getContainer());
                addNewConnection(
                        new StraightConnection(elementFieldReference.getOutConnector(), associationRepresentation,
                                ConstantsIDS.LINKED_LIST_ELEMENTS_CONNECTIONS_COLOR));
            } else {
                Field value = null;
                try {
                    value = associacao.getClass().getDeclaredField(ConstantsIDS.VALUE);
                    addNewConnection(new StraightConnection<>(elementFieldReference.getOutConnector(), myCanvas.getRepresentationWithInConnectors(Utils.getFieldValue(associacao, value)), ConstantsIDS.HASH_TABLE_ELEMENTS_CONNECTIONS_COLOR));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao obter valor da Associação", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        FieldReference nextFieldReference = doubleNodeRepresentation.getNextFieldReference();
        TabelaHashOrdenada.No nextNode = (TabelaHashOrdenada.No) nextFieldReference.getFieldValue();

        if (baseFieldReference.getFieldValue() == nextNode) {
            addNewConnection(new CConnection(nextFieldReference.getOutConnector(), nodeRepresentationByOwner.get(nextNode), Color.RED, 14, 21, nextNode == node));
        } else {
            createNode(nextFieldReference);
            DoubleNodeRepresentation nextNodeRepresentation = nodeRepresentationByOwner.get(nextNode);
            addNewConnection(new StraightConnection(nextNodeRepresentation.getPreviousFieldReference().getOutConnector(), doubleNodeRepresentation, Color.RED));
        }
        myCanvas.repaint();
    }

    protected String getBaseNodeIndexText() {
        return "-";
    }

    @Override
    public void add(RepresentationWithInConnectors representationWithInConnectors) {
        try {
            Object value = representationWithInConnectors.getOwner();
            Object key = Utils.getFieldValue(value, ConstantsIDS.KEY);
            if (key == null) {
                JOptionPane.showMessageDialog(null, "Chave nula", "Erro de inserção", JOptionPane.ERROR_MESSAGE);
                return;
            }

            owner.inserir(key, value);
            sortedInnerHashTableRepresentation.clearConnections();
            super.add(representationWithInConnectors);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro Tabela Hash", JOptionPane.ERROR_MESSAGE);
        }
    }



    @Override
    public void rebuild() {
        container.removeAllGraphicElements();
        sortedInnerHashTableRepresentation.clearConnections();
        super.rebuild();
    }



    public DoubleNodeRepresentation getNodeRepresentation(TabelaHashOrdenada.No node) {
        return nodeRepresentationByOwner.get(node);
    }



    class AssociationRepresentation extends GeneralRepresentation<Associacao> {
        private static final long serialVersionUID = 1L;

        private FieldReference valueFieldReference;
        private Object keyValue;

        public AssociationRepresentation(Point position, Associacao association, MyCanvas canvas) {
            super(position, association, canvas, true);
        }

        @Override
        public void init() {
            try {
                Associacao association = owner;
                Field key = association.getClass().getDeclaredField(ConstantsIDS.KEY);
                Field value = association.getClass().getDeclaredField(ConstantsIDS.VALUE);

                int dim = 14;
                container.setCellSpacing(0);

                keyValue = Utils.getFieldValue(association, key);
                Object keyValueValue = keyValue;

                if (Utils.isPrimitiveOrPrimitiveWrapperType(keyValueValue.getClass().getSimpleName()) || keyValueValue.getClass().isEnum() || keyValueValue instanceof String) {
                    final NormalTextElement keyNormalTextElement = new NormalTextElement(keyValueValue.toString(), ConstantsIDS.FONT_SIZE_TEXT - 2);
                    keyNormalTextElement.setBorderShown(true);
                    container.add(keyNormalTextElement);
                } else {
                    FieldReference keyFieldReference = new FieldReference(new Dimension(dim, dim), owner, key, Location.CENTER, false);
                    container.add(keyFieldReference);
                    addNewConnection(new StraightConnection<>(keyFieldReference.getOutConnector(), myCanvas.getRepresentationWithInConnectors(keyValue)));
                }
                valueFieldReference = new FieldReference(new Dimension(dim, dim), association, value, Location.CENTER, false);
                container.add(valueFieldReference);
                addNewConnection(new StraightConnection<>(valueFieldReference.getOutConnector(), myCanvas.getRepresentationWithInConnectors(Utils.getFieldValue(association, value)), ConstantsIDS.HASH_TABLE_ELEMENTS_CONNECTIONS_COLOR));

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao Criar Representação da Associação da Tabela Hash Ordenada");
            }
        }

        public FieldReference getValueFieldReference() {
            return valueFieldReference;
        }
    }
}

