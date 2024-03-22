package com.aegamesi.java_visualizer.ui.graphics.representations.hashtables;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.Associacao;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.TabelaHash;
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
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.nodes.DoubleNodeRepresentation;
import com.aegamesi.java_visualizer.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.LinkedList;

public class SortedInnerHashTableRepresentation extends CollectionRepresentation<TabelaHash> {
    private static final long serialVersionUID = 1L;

    private final SortedHashTableRepresentation sortedHashTableRepresentation;
    private int numberOfElements;
    private LinkedList<EntryRepresentation> entryRepresentations;
    private boolean canInit = false;

    public SortedInnerHashTableRepresentation(Point position, TabelaHash hashTableWrapper, SortedHashTableRepresentation sortedHashTableRepresentation, MyCanvas myCanvas) {
        super(position, hashTableWrapper, myCanvas, false);
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
        Object ownerValue = owner.getClass();
        numberOfElements = (int) Utils.getFieldValue(ownerValue, ConstantsIDS.NUMBER_OF_ELEMENTS);

        final TabelaHash.Entrada[] table = ((TabelaHash.Entrada[]) Utils.getFieldValue(ownerValue, ConstantsIDS.TABLE));

//        final HashTableEntryTableWrapper hashTableEntryTableWrapper = new HashTableEntryTableWrapper(owner.getValueTypeName(), table.length);
//        EntryTableRepresentation tableRepresentation = new EntryTableRepresentation(new Point(), hashTableEntryTableWrapper, canvas);
        //final ArrayWrapper arrayWrapper = new ArrayWrapper(owner.getValueTypeName(), table.length);
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                Class<TabelaHash.Entrada> entryClass = TabelaHash.Entrada.class;
                //arrayWrapper.add(new HashTableEntryWrapper(entryClass, entryClass.getName(), table[i]), i);
            }
        }
       // EntryTableRepresentation tableRepresentation = new EntryTableRepresentation(new Point(), arrayWrapper, canvas);

//        container.add(owner.getGeneralHashTableKeyWrapper().getGeneralHashTableKeyRepresentation().getContainer());
       // container.add(tableRepresentation.getContainer());
        container.add(new NormalTextElement(String.valueOf(numberOfElements), ConstantsIDS.FONT_SIZE_TEXT), Location.CENTER);
        container.add(new NormalTextElement(String.valueOf(Utils.getFieldValue(ownerValue, ConstantsIDS.NUMBER_OF_INACTIVE_ELEMENTS)), ConstantsIDS.FONT_SIZE_TEXT), Location.CENTER);
//        container.add(new NormalTextElement(connections.size()+"", Constants.FONT_SIZE_TEXT), Location.CENTER);
    }

//    @Override
//    public void add(RepresentationWithInConnectors representationWithInConnectors) {
//        try {
//
//            final WrapperWithValue value = (WrapperWithValue) representationWithInConnectors.getOwner();
//            Object key = owner.getKey(value);
//
//            //code generation - attention => must be before the operation
//            String ownerVariableName = canvas.getFirstReferenceTo(this);
//            String elementVariableName = canvas.getFirstReferenceTo(representationWithInConnectors);
//            String keyCode = getCode(key);
//            String code = ownerVariableName + ".put(" + keyCode + ", " + elementVariableName + ")";
//            canvas.getIDSToolWindow().addCodeToEditorPaneSourceCode(code + ";\n");
//
//            ((TabelaHash) owner.getValue()).inserir(key, value);
//            super.add(representationWithInConnectors);
//            canvas.addOperation(Utils.getUndoRedoAddElementMessage(code));
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro Tabela Hash", JOptionPane.ERROR_MESSAGE);
//        }
//    }

    private String getCode(Object key) {
        String keyExpression = "";
        if (key instanceof String) {
            keyExpression = "\"" + key + "\"";
        } else if (key instanceof Character) {
            keyExpression = "'\\u" + Integer.toHexString(((Character) key).charValue() | 0x10000).substring(1) + "'";
//        } else if (key.getClass().getSimpleName().equals("byte[]")) {
//            keyExpression = "\"" + new String((byte[])key) + "\"";
        } else {
           // keyExpression = key instanceof WrapperWithValue ? ((WrapperWithValue<?>) key).getValue().toString() : key.toString();
        }
        return keyExpression;
    }

    @Override
    public void rebuild() {
        container.removeAllGraphicElements();
        super.rebuild();
    }

    public String getCreationCode() {
        StringBuilder sb = new StringBuilder("new ");
//        String valueTypeName = owner.getValue().getClass().getName();
        String valueTypeName = owner.getClass().getName();
        //Operator.getCanvas().getIDSToolWindow().addImportToEditorPaneSourceCode(valueTypeName);
        sb.append(Utils.getClassSimpleName(valueTypeName)).append("(").append(((TabelaHash.Entrada[]) Utils.getFieldValue(owner.getClass(), ConstantsIDS.TABLE)).length).append(")");
        return sb.toString();
    }

    /*@Override
    public ButtonBar getButtonBar(Point position) {
        for (EntryRepresentation entryRepresentation : entryRepresentations) {
            if (entryRepresentation.contains(position)) {
                return entryRepresentation.getButtonBar();
            }
        }
        return super.getButtonBar(position);
    }*/

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

                //keyValueWrapper = (WrapperWithValue) Utils.getFieldValue(association, key);
                //Object keyValue = keyValueWrapper.getValue();

                /*if (Utils.isPrimitiveOrPrimitiveWrapperType(keyValue.getClass().getSimpleName()) || keyValue.getClass().isEnum() || keyValue instanceof String) {
                    final NormalTextElement keyNormalTextElement = new NormalTextElement(keyValue.toString(), Constants.FONT_SIZE_TEXT - 2);
                    keyNormalTextElement.setBorderShown(false);
                    container.add(keyNormalTextElement);
                } else {
                    FieldReference keyFieldReference = new FieldReference(new Dimension(dim, dim), owner, key, Location.CENTER, false);
                    container.add(keyFieldReference);
                    addNewConnection(new StraightConnection<>(keyFieldReference.getOutConnector(), canvas.getRepresentationWithInConnectors(keyValueWrapper)));
                }

                valueFieldReference = new FieldReference(new Dimension(dim, dim),
                        new AssociationWrapper<>(Associacao.class, Associacao.class.getName(), association, SortedInnerHashTableRepresentation.this.owner),
                        value, Location.CENTER, false);
                */
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

//        public FieldReference getKeyFieldReference() {
//            return keyFieldReference;
//        }

//        public FieldReference getValueFieldReference() {
//            return valueFieldReference;
//        }

        /*public ButtonBar getButtonBar() {
            if (!(boolean) Utils.getFieldValue(owner.getClass(), Constants.ACTIVE)) {
                return IDSToolWindow.getButtonBar(IDSToolWindow.COLLECTION_BUTTON_BAR);
            }
            final ButtonBar buttonBar = IDSToolWindow.getButtonBar(IDSToolWindow.DELETE_BUTTON_BAR);
            final JButton button = buttonBar.getButton(IDSToolWindow.DELETE_BUTTON);
            for (ActionListener actionListener : button.getActionListeners()) {
                button.removeActionListener(actionListener);
            }
            button.addActionListener(e -> {
                String firstReferenceToKeyWrapper = myCanvas.getFirstReferenceTo(keyValueWrapper);
                String keyCode = firstReferenceToKeyWrapper.equals(Constants.UNKOWN_REF) ?
                        getCode(keyValueWrapper.getValue()) :
                        firstReferenceToKeyWrapper;

                MyCanvas myCanvas = Operator.getCanvas();
                String code = myCanvas.getFirstReferenceTo(SortedInnerHashTableRepresentation.this.sortedHashTableRepresentation.getOwner()) + ".remover(" + keyCode + ")";
                myCanvas.getIDSToolWindow().addCodeToEditorPaneSourceCode(code + ";\n");

                sortedHashTableRepresentation.getOwner().remove(keyValueWrapper);
                sortedHashTableRepresentation.rebuild();
                myCanvas.addOperation(Utils.getUndoRedoRemoveElementMessage(code));
            });
            return buttonBar;
        }*/
    }

    private class EntryTableRepresentation extends GeneralRepresentation<Array> {
        private static final long serialVersionUID = 1L;

//    private class EntryTableRepresentation extends GeneralRepresentation<HashTableEntryTableWrapper> {

        public EntryTableRepresentation(Point position,Array owner, MyCanvas myCanvas) {
            super(position, owner, myCanvas, false);
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
            /*for (int i = 0; i < ownerValue.length; i++) {
                //criar contentores horizontais com indice referencia e entrada/associação e respetiva ligação
                ContainerWithoutInConnectors indexReferenceEntrycontainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), true);
//                indexReferenceEntrycontainer.setBackgroundColor(Color.MAGENTA);
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
                if (Array.get(ownerValue, i) == null) {
                    continue;
                }

                final EntryRepresentation entryRepresentation = new EntryRepresentation(new Point(), (HashTableEntryWrapper<TabelaHash.Entrada>) ownerValue[i], canvas);
                entryRepresentations.add(entryRepresentation);
                indexReferenceEntrycontainer.add(entryRepresentation.getContainer(), Location.CENTER);
                addNewConnection(new StraightConnection(indexReference.getOutConnector(), entryRepresentation, Color.RED));
            }
            */
        }

        private Container createAggregateGraphicElementWithIndex(AggregateRectangularGraphicElement aggregateGraphicElement, int index) {
            ContainerWithoutInConnectors containerWithoutInConnectors = new ContainerWithoutInConnectors(new Point(), new Dimension(), true);
            containerWithoutInConnectors.setInnerCellSpacing(0);
            containerWithoutInConnectors.setTopCellSpacing(0);
            containerWithoutInConnectors.setBottomCellSpacing(0);
            containerWithoutInConnectors.setRightCellSpacing(0);
            containerWithoutInConnectors.setLeftCellSpacing(0);
            containerWithoutInConnectors.setBorderShown(false);
//            containerWithoutInConnectors.setBackgroundColor(Color.YELLOW);
            NormalTextElement indexNormalTextElement = new NormalTextElement((/*owner.getValue().length > 10 &&*/ index < 10 ? " " : "") + index, ConstantsIDS.FONT_SIZE_INDEX);
            indexNormalTextElement.setBackgroundColor(GraphicElement.TRANSPARENT_COLOR);
            indexNormalTextElement.setBorderColor(GraphicElement.TRANSPARENT_COLOR);
            containerWithoutInConnectors.add(indexNormalTextElement);
            containerWithoutInConnectors.add(aggregateGraphicElement);
            return containerWithoutInConnectors;
        }
    }
}
