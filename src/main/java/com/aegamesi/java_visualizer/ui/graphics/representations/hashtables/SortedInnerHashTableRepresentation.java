package com.aegamesi.java_visualizer.ui.graphics.representations.hashtables;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.Associacao;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.TabelaHash;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.TabelaHashComIncrementoPorHash;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.TabelaHashOrdenada;
import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.Connection;
import com.aegamesi.java_visualizer.ui.graphics.GraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.StraightConnection;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.*;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.Container;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.ui.graphics.representations.CollectionRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.GeneralRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.nodes.DoubleNodeRepresentation;
import com.aegamesi.java_visualizer.utils.Utils;
import com.sun.nio.sctp.Association;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.LinkedList;

public class SortedInnerHashTableRepresentation extends CollectionRepresentation<TabelaHash> {
    private static final long serialVersionUID = 1L;

    private final SortedHashTableRepresentation sortedHashTableRepresentation;
    private int numberOfElements;
    private FieldReference baseFieldReference;
    private ContainerWithoutInConnectors topContainer;
    private LinkedList<EntryRepresentation> entryRepresentations;
    private boolean canInit = false;

    public SortedInnerHashTableRepresentation(Point position, TabelaHash hashTable, SortedHashTableRepresentation sortedHashTableRepresentation, MyCanvas myCanvas) {
        super(position, hashTable, myCanvas, false);
        this.sortedHashTableRepresentation = sortedHashTableRepresentation;
        canInit = true;
        init();
    }

    @Override
    public void init() {
        if (canInit) {
            super.init();
            update();
        }
    }

    @Override
    protected void update() {
        entryRepresentations = new LinkedList<>();
        super.update();
        TabelaHash ownerValue = owner;

        numberOfElements = ownerValue.getNumeroElementos();

        topContainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), true);
        topContainer.setCellSpacing(0);
        topContainer.setInnerCellSpacing(20);
        topContainer.setBorderShown(false);

        ContainerWithoutInConnectors topLeftContainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), false);
        topLeftContainer.setCellSpacing(0);
        topLeftContainer.setBorderShown(false);
        topContainer.add(topLeftContainer);
        int dim = 14;
        try {
            Field baseField = Utils.getField(ownerValue, ConstantsIDS.BASE);
            if (baseField == null) {
                System.out.println("Base field not found in ownerValue.");
                return;
            }
            baseFieldReference = new FieldReference(new Dimension(dim, dim), owner, baseField, Location.CENTER, false);
            topLeftContainer.add(baseFieldReference, Location.LEFT);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving base field: " + e.getMessage());
            return;
        }

        TabelaHash<Object, TabelaHashOrdenada<Object, Object>.No>.Entrada<Object, TabelaHashOrdenada<Object, Object>.No>[] table = ownerValue.tabela;

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                entryRepresentations.add(new EntryRepresentation(new Point(), table[i], myCanvas));
            }
        }

        for (EntryRepresentation entryRepresentation : entryRepresentations) {
            container.add(entryRepresentation.getContainer(), Location.CENTER);
        }

        container.add(new NormalTextElement(String.valueOf(numberOfElements), ConstantsIDS.FONT_SIZE_TEXT), Location.CENTER);
        container.add(new NormalTextElement(String.valueOf(ownerValue.getNumeroElementosInativos()), ConstantsIDS.FONT_SIZE_TEXT), Location.CENTER);
    }

    @Override
    public void add(RepresentationWithInConnectors representationWithInConnectors) {
        try {

            Object value = representationWithInConnectors.getOwner();
            Object key = Utils.getFieldValue(value, ConstantsIDS.KEY);

             owner.inserir(key, value);
            super.add(representationWithInConnectors);
            //myCanvas.addOperation(Utils.getUndoRedoAddElementMessage(code));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro Tabela Hash", JOptionPane.ERROR_MESSAGE);
        }
    }



    @Override
    public void rebuild() {
        container.removeAllGraphicElements();
        super.rebuild();
    }





    public void clearConnections() {
        for (Connection connection : connections) {
            myCanvas.remove(connection);
        }
        connections.clear();
    }

    public class EntryRepresentation extends GeneralRepresentation<TabelaHash.Entrada> {
        private static final long serialVersionUID = 1L;

        private FieldReference valueFieldReference;


        public EntryRepresentation(Point position, TabelaHash.Entrada entryHashTableEntry, MyCanvas myCanvas) {
            super(position, entryHashTableEntry, myCanvas, true);
        }

        @Override
        public void init() {
            try {
                final TabelaHash.Entrada ownerValue = owner.getClass().newInstance();
                container.setBackgroundColor((boolean) Utils.getFieldValue(ownerValue, ConstantsIDS.ACTIVE) ? Color.GREEN : Color.YELLOW);
                final Associacao association = (Associacao) Utils.getFieldValue(ownerValue, ConstantsIDS.ASSOCIATION);
                Field key = association.getClass().getDeclaredField(ConstantsIDS.KEY);
                Field value = association.getClass().getDeclaredField(ConstantsIDS.VALUE);

                int dim = 12;
                container.setCellSpacing(0);
                Object keyValue = Utils.getFieldValue(association, key);

                if (Utils.isPrimitiveOrPrimitiveWrapperType(keyValue.getClass().getSimpleName()) || keyValue.getClass().isEnum() || keyValue instanceof String) {
                    final NormalTextElement keyNormalTextElement = new NormalTextElement(keyValue.toString(), ConstantsIDS.FONT_SIZE_TEXT - 2);
                    keyNormalTextElement.setBorderShown(false);
                    container.add(keyNormalTextElement);
                } else {
                    FieldReference keyFieldReference = new FieldReference(new Dimension(dim, dim), owner, key, Location.CENTER, false);
                    container.add(keyFieldReference);
                    addNewConnection(new StraightConnection<>(keyFieldReference.getOutConnector(), myCanvas.getRepresentationWithInConnectors(keyValue)));
                }

                valueFieldReference = new FieldReference(new Dimension(dim, dim), new Object(), value, Location.CENTER, false);

                container.add(valueFieldReference);
                TabelaHashOrdenada.No valueValue = (TabelaHashOrdenada.No) Utils.getFieldValue(association, value);
                DoubleNodeRepresentation nodeRepresentation = SortedInnerHashTableRepresentation.this.sortedHashTableRepresentation.getNodeRepresentation(
                        valueValue);
                if (nodeRepresentation != null) {
                    addNewConnection(
                            new StraightConnection(valueFieldReference.getOutConnector(),
                                    nodeRepresentation,
                                    ConstantsIDS.HASH_TABLE_ELEMENTS_CONNECTIONS_COLOR));
                }


            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao Criar Representação da Entrada");
            }
        }



       public FieldReference getValueFieldReference() {
           return valueFieldReference;
       }


    }

    private class EntryTableRepresentation extends GeneralRepresentation<TabelaHash.Entrada[]> {
        private static final long serialVersionUID = 1L;



        public EntryTableRepresentation(Point position, TabelaHash.Entrada[] owner, MyCanvas canvas) {
            super(position, owner, canvas, false);
        }

        @Override
        public void init() {
            container.setTopCellSpacing(0);
            container.setBottomCellSpacing(0);
            container.setLeftCellSpacing(0);
            container.setInnerCellSpacing(0);
            container.setBorderShown(false);

//            final HashTableEntryWrapper[] ownerValue = (HashTableEntryWrapper[]) owner.getValue();
            //final WrapperWithValue[] ownerValue = owner.getValue();
            for (int i = 0; i < owner.length; i++) {
                //criar contentores horizontais com indice referencia e entrada/associação e respetiva ligação
                ContainerWithoutInConnectors indexReferenceEntrycontainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), true);
               indexReferenceEntrycontainer.setBackgroundColor(Color.MAGENTA);
                indexReferenceEntrycontainer.setBorderShown(false);
                indexReferenceEntrycontainer.setInnerCellSpacing(20);
                container.add(indexReferenceEntrycontainer, Location.LEFT);
                indexReferenceEntrycontainer.setTopCellSpacing(0);
                indexReferenceEntrycontainer.setBottomCellSpacing(0);
                indexReferenceEntrycontainer.setLeftCellSpacing(0);
                indexReferenceEntrycontainer.setRightCellSpacing(0);

                Reference indexReference = new ArrayReference(owner, i, Location.CENTER, false);
                indexReferenceEntrycontainer.add(createAggregateGraphicElementWithIndex(indexReference, i));
                // if the array element at the position i is null do nothing
                if (Array.get(owner, i) == null) {
                    continue;
                }

                final EntryRepresentation entryRepresentation = new EntryRepresentation(new Point(), owner[i], myCanvas);
                entryRepresentations.add(entryRepresentation);
                indexReferenceEntrycontainer.add(entryRepresentation.getContainer(), Location.CENTER);
                addNewConnection(new StraightConnection(indexReference.getOutConnector(), entryRepresentation, Color.RED));
            }

        }

        private Container createAggregateGraphicElementWithIndex(AggregateRectangularGraphicElement aggregateGraphicElement, int index) {
            ContainerWithoutInConnectors containerWithoutInConnectors = new ContainerWithoutInConnectors(new Point(), new Dimension(), true);
            containerWithoutInConnectors.setInnerCellSpacing(0);
            containerWithoutInConnectors.setTopCellSpacing(0);
            containerWithoutInConnectors.setBottomCellSpacing(0);
            containerWithoutInConnectors.setRightCellSpacing(0);
            containerWithoutInConnectors.setLeftCellSpacing(0);
            containerWithoutInConnectors.setBorderShown(false);
           containerWithoutInConnectors.setBackgroundColor(Color.YELLOW);
            NormalTextElement indexNormalTextElement = new NormalTextElement((/*owner.getValue().length > 10 &&*/ index < 10 ? " " : "") + index, ConstantsIDS.FONT_SIZE_INDEX);
            indexNormalTextElement.setBackgroundColor(GraphicElement.TRANSPARENT_COLOR);
            indexNormalTextElement.setBorderColor(GraphicElement.TRANSPARENT_COLOR);
            containerWithoutInConnectors.add(indexNormalTextElement);
            containerWithoutInConnectors.add(aggregateGraphicElement);
            return containerWithoutInConnectors;
        }
    }
}
