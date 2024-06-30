package com.aegamesi.java_visualizer.ui.graphics.representations.hashtables;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.Associacao;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.MyHashTable;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.TabelaHash;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.TabelaHashComIncrementoPorHash;
import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.IDSToolWindow;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.GraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.StraightConnection;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.*;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.Container;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.ui.graphics.representations.*;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.ArrayRepresentation;
import com.aegamesi.java_visualizer.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.Map;

import static com.aegamesi.java_visualizer.utils.Utils.invokeMethod;


import com.aegamesi.java_visualizer.ui.graphics.aggregations.ContainerWithoutInConnectors;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.ui.graphics.representations.CollectionRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.GeneralRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;

public class HashTableRepresentation extends CollectionRepresentation<TabelaHash> {
    private static final long serialVersionUID = 1L;

    private int numberOfElements;
    private LinkedList<EntryRepresentation> entryRepresentations;

    public HashTableRepresentation(Point position, TabelaHash hashTable, MyCanvas canvas) {
        super(position, hashTable, canvas, false);
    }

    @Override
    public void init() {
        super.init();
        update();
    }

    @Override
    protected void update() {
        // Clear the container before updating to avoid duplication
        container.removeAllGraphicElements();

        entryRepresentations = new LinkedList<>();
        super.update();


        numberOfElements = owner.getNumeroElementos();
        System.out.println("Number of Elements: " + numberOfElements);

        container.add(new NormalTextElement(String.valueOf(numberOfElements), ConstantsIDS.FONT_SIZE_TEXT), Location.CENTER);

        int numeroElementosInativos = owner.numeroElementosInativos;
        container.add(new NormalTextElement(String.valueOf(numeroElementosInativos), ConstantsIDS.FONT_SIZE_TEXT), Location.CENTER);

        TabelaHash<?, ?>.Entrada<?, ?>[] table = owner.tabela;

        EntryTableRepresentation entryTableRepresentation = new EntryTableRepresentation(new Point(), table, myCanvas);
        container.add(entryTableRepresentation.getContainer(), Location.CENTER);
    }

    @Override
    public void add(RepresentationWithInConnectors representationWithInConnectors) {
        try {
            final Object value = representationWithInConnectors.getOwner();
            Object key = Utils.getFieldValue(value, ConstantsIDS.KEY);
            if (key == null) {
                JOptionPane.showMessageDialog(null, "Chave nula");
                return;
            }

            owner.inserir(key, value);
            super.add(representationWithInConnectors);
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

    public class EntryRepresentation extends GeneralRepresentation<TabelaHash.Entrada> {
        private static final long serialVersionUID = 1L;

        private FieldReference valueFieldReference;
        private Object keyValue;

        public EntryRepresentation(Point position, TabelaHash.Entrada entry, MyCanvas canvas) {
            super(position, entry, canvas, true);
        }

        @Override
        public void init() {
            try {
                System.out.println("Initializing EntryRepresentation. Current map size: " + myCanvas.representationWithInConnectorsByOwner.size());

                final TabelaHash.Entrada ownerValue = owner;
                container.setBackgroundColor((boolean) Utils.getFieldValue(ownerValue, ConstantsIDS.ACTIVE) ? Color.GREEN : Color.YELLOW);
                final Associacao<?, ?> association = (Associacao<?, ?>) Utils.getFieldValue(ownerValue, ConstantsIDS.ASSOCIATION);
                System.out.println("Association hashCode: " + System.identityHashCode(association));
                Field key = association.getClass().getDeclaredField(ConstantsIDS.KEY);
                Field value = association.getClass().getDeclaredField(ConstantsIDS.VALUE);

                int dim = 12;
                container.setCellSpacing(0);

                keyValue = Utils.getFieldValue(association, key);
                if (keyValue != null) {
                    if (Utils.isPrimitiveOrPrimitiveWrapperType(keyValue.getClass().getSimpleName()) || keyValue.getClass().isEnum() || keyValue instanceof String) {
                        final NormalTextElement keyNormalTextElement = new NormalTextElement(keyValue.toString(), ConstantsIDS.FONT_SIZE_TEXT - 2);
                        keyNormalTextElement.setBorderShown(false);
                        container.add(keyNormalTextElement);
                    } else {
                        FieldReference keyFieldReference = new FieldReference(new Dimension(dim, dim), owner, key, Location.CENTER, false);
                        container.add(keyFieldReference);

                        RepresentationWithInConnectors keyValueRepresentation = myCanvas.getRepresentationWithInConnectors(keyValue);
                        System.out.println("Key Value (identityHashCode: " + System.identityHashCode(keyValue) + ") -> Representation: " + keyValueRepresentation);

                        if (keyValueRepresentation != null) {
                            addNewConnection(new StraightConnection<>(keyFieldReference.getOutConnector(), keyValueRepresentation));
                        } else {
                            System.out.println("Key value representation is null for key: " + keyValue);
                        }
                    }
                } else {
                    System.out.println("Key value is null for entry: " + ownerValue);
                }

                Object valueObject = Utils.getFieldValue(association, value);
                System.out.println("Value Object: " + valueObject + " (identityHashCode: " + System.identityHashCode(valueObject) + ")");
                valueFieldReference = new FieldReference(new Dimension(dim, dim), new Object(), value, Location.CENTER, false);
                container.add(valueFieldReference);

                System.out.println("Iterating over representationWithInConnectorsByOwner:");
                for (Map.Entry<Object, RepresentationWithInConnectors> entry : myCanvas.representationWithInConnectorsByOwner.entrySet()) {
                    System.out.println("Entry: " + entry);
                    Object storedKey = entry.getKey();
                    RepresentationWithInConnectors storedRepresentation = entry.getValue();
                    System.out.println("Stored Key (identityHashCode: " + System.identityHashCode(storedKey) + ") -> Representation: " + storedRepresentation);
                }

                RepresentationWithInConnectors valueRepresentation = myCanvas.getRepresentationWithInConnectors(valueObject);
                System.out.println("Retrieved Value Representation: " + valueRepresentation + " for valueObject: " + valueObject + " (identityHashCode: " + System.identityHashCode(valueObject) + ")");

                if (valueRepresentation != null) {
                    addNewConnection(new StraightConnection<>(valueFieldReference.getOutConnector(), valueRepresentation, ConstantsIDS.HASH_TABLE_ELEMENTS_CONNECTIONS_COLOR));
                } else {
                    System.out.println("Value representation is null for value: " + valueObject + " (identityHashCode: " + System.identityHashCode(valueObject) + ")");
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

            for (int i = 0; i < owner.length; i++) {
                ContainerWithoutInConnectors indexReferenceEntryContainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), true);
                indexReferenceEntryContainer.setBorderShown(false);
                indexReferenceEntryContainer.setInnerCellSpacing(20);
                container.add(indexReferenceEntryContainer, Location.LEFT);

                Reference indexReference = new ArrayReference(owner, i, Location.CENTER, false);
                indexReferenceEntryContainer.add(createAggregateGraphicElementWithIndex(indexReference, i));

                if (Array.get(owner, i) == null) {
                    continue;
                }

                final EntryRepresentation entryRepresentation = new EntryRepresentation(new Point(), owner[i], myCanvas);
                entryRepresentations.add(entryRepresentation);
                indexReferenceEntryContainer.add(entryRepresentation.getContainer(), Location.CENTER);
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
            NormalTextElement indexNormalTextElement = new NormalTextElement((owner.length > 10 && index < 10 ? " " : "") + index, ConstantsIDS.FONT_SIZE_INDEX);
            indexNormalTextElement.setBackgroundColor(GraphicElement.TRANSPARENT_COLOR);
            indexNormalTextElement.setBorderColor(GraphicElement.TRANSPARENT_COLOR);
            containerWithoutInConnectors.add(indexNormalTextElement);
            containerWithoutInConnectors.add(aggregateGraphicElement);
            return containerWithoutInConnectors;
        }
    }
}

