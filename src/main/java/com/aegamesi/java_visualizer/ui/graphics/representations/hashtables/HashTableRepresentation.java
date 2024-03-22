package com.aegamesi.java_visualizer.ui.graphics.representations.hashtables;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.Associacao;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.MyHashTable;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.TabelaHash;
import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.GraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.StraightConnection;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.*;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.Container;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.ui.graphics.representations.*;
import com.aegamesi.java_visualizer.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.LinkedList;

import static com.aegamesi.java_visualizer.utils.Utils.invokeMethod;

public class HashTableRepresentation extends CollectionRepresentation<TabelaHash> {
    private static final long serialVersionUID = 1L;

    private int numberOfElements;
    private LinkedList<EntryRepresentation> entryRepresentations;

    public HashTableRepresentation(Point position, TabelaHash hashTable, MyCanvas myCanvas) {
        super(position, hashTable, myCanvas, false);
    }

    @Override
    public void init() {
        super.init();
        update();
    }

    @Override
    protected void update() {
        entryRepresentations = new LinkedList<>();
        super.update();
        MyHashTable MyHashTable = (MyHashTable) owner;
        Object numberOfElements = invokeMethod(MyHashTable, "getNumeroElementos");
        if (numberOfElements == null) {
            System.out.println("Could not retrieve the value of 'numeroElementos'");
            return; // Or handle the case accordingly
        }
        numberOfElements = (int) numberOfElements;
        Object ownerValue = owner.getClass();


        Object tableObject = Utils.getFieldValue(ownerValue, ConstantsIDS.TABLE);
//        final HashTableEntryTableWrapper hashTableEntryTableWrapper = new HashTableEntryTableWrapper(owner.getValueTypeName(), table.length);
//        EntryTableRepresentation tableRepresentation = new EntryTableRepresentation(new Point(), hashTableEntryTableWrapper, canvas);
        if (tableObject instanceof TabelaHash.Entrada[]) {
            final TabelaHash.Entrada[] table = (TabelaHash.Entrada[]) tableObject;
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null) {
                    Class<TabelaHash.Entrada> entryClass = TabelaHash.Entrada.class;
                    TabelaHash.Entrada entry = table[i];
                    EntryRepresentation entryRepresentation = new EntryRepresentation(new Point(), entry, myCanvas);
                    entryRepresentations.add(entryRepresentation);

                }
            }
        } else {
            // The table field was not found or is not an instance of TabelaHash.Entrada[]
            // Handle this case appropriately


        }


        //container.add(owner.getGeneralHashTableKey().getGeneralHashTableKeyRepresentation().getContainer());
        //container.add(tableRepresentation.getContainer());
        container.add(new NormalTextElement(String.valueOf(numberOfElements), ConstantsIDS.FONT_SIZE_TEXT), Location.CENTER);
        container.add(new NormalTextElement(String.valueOf(Utils.getFieldValue(owner, ConstantsIDS.NUMBER_OF_INACTIVE_ELEMENTS)), ConstantsIDS.FONT_SIZE_TEXT), Location.CENTER);
    }

    @Override
    public void add(RepresentationWithInConnectors representationWithInConnectors) {
        try {

            //final WrapperWithValue value = (WrapperWithValue) representationWithInConnectors.getOwner();
            //WrapperWithValue keyWrapper = owner.getKey(value);
            //if (keyWrapper == null) {
              //  JOptionPane.showMessageDialog(null, "Chave nula", "Erro de inserção", JOptionPane.ERROR_MESSAGE);
                //return;
            //}

            //code generation - attention => must be before the operation
            //String ownerVariableName = myCanvas.getFirstReferenceTo(this);
            //String elementVariableName = myCanvas.getFirstReferenceTo(representationWithInConnectors);
            //String firstReferenceToKeyWrapper = myCanvas.getFirstReferenceTo(keyWrapper);
            //String keyCode = firstReferenceToKeyWrapper.equals(Constants.UNKOWN_REF) ?
                    //Utils.getCode(keyWrapper.getValue()) :
                  //  firstReferenceToKeyWrapper;
            //String code = ownerVariableName + ".inserir(" + /*keyCode*/ ", " + elementVariableName + ")";
            //myCanvas.getIDSToolWindow().addCodeToEditorPaneSourceCode(code + ";\n");

            //((TabelaHash) owner.getValue()).inserir(keyWrapper, value);
            super.add(representationWithInConnectors);
           // myCanvas.addOperation(Utils.getUndoRedoAddElementMessage(code));
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

    /*public String getCreationCode() {
        StringBuilder sb = new StringBuilder("new ");
        String valueTypeName = owner.getClass().getName();

        Operator.getCanvas().getIDSToolWindow().addImportToEditorPaneSourceCode(valueTypeName);
        sb.append(Utils.getClassSimpleName(Utils.getValueTypeNameWithoutParametrizedTypes(valueTypeName))).append("(").append(((TabelaHash.Entrada[]) Utils.getFieldValue(owner.getValue(), Constants.TABLE)).length).append(")");
        return sb.toString();
    }*/

   /* @Override
    public ButtonBar getButtonBar(Point position) {
        for (EntryRepresentation entryRepresentation : entryRepresentations) {
            if (entryRepresentation.contains(position)) {
                return entryRepresentation.getButtonBar();
            }
        }
        return super.getButtonBar(position);
    }*/

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
                    addNewConnection(new StraightConnection<>(keyFieldReference.getOutConnector(), myCanvas.getRepresentationWithInConnectors(keyValueWrapper)));
                }*/
                //valueFieldReference = new FieldReference(new Dimension(dim, dim), new AssociationWrapper<>(Associacao.class, Associacao.class.getName(), association, HashTableRepresentation.this.owner), value, Location.CENTER, false);
                container.add(valueFieldReference);
                addNewConnection(new StraightConnection<>(valueFieldReference.getOutConnector(), myCanvas.getRepresentationWithInConnectors( Utils.getFieldValue(association, value)), ConstantsIDS.HASH_TABLE_ELEMENTS_CONNECTIONS_COLOR));

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao Criar Representação da Entrada");
            }
        }

//        public FieldReference getKeyFieldReference() {
//            return keyFieldReference;
//        }

        public FieldReference getValueFieldReference() {
            return valueFieldReference;
        }

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
                String firstReferenceToKeyWrapper = myCanvas.getFirstReferenceTo(keyValue);
                String keyCode = firstReferenceToKeyWrapper.equals(Constants.UNKOWN_REF) ?
                        Utils.getCode(keyValue.getValue()) :
                        firstReferenceToKeyWrapper;

                MyCanvas myCanvas = Operator.getCanvas();
                String code = myCanvas.getFirstReferenceTo(HashTableRepresentation.this.owner) + ".remover(" + keyCode + ")";
                myCanvas.getIDSToolWindow().addCodeToEditorPaneSourceCode(code + ";\n");

                HashTableRepresentation.this.getOwner().remove((WrapperWithValue) keyValueWrapper);
                rebuild();
                myCanvas.addOperation(Utils.getUndoRedoRemoveElementMessage(code));
            });
            return buttonBar;
        }
    }*/

    private class EntryTableRepresentation extends GeneralRepresentation<Array> {
        private static final long serialVersionUID = 1L;

//    private class EntryTableRepresentation extends GeneralRepresentation<HashTableEntryTableWrapper> {

        public EntryTableRepresentation(Point position,  Array owner, MyCanvas myCanvas) {
            super(position, owner, myCanvas, false);
        }

        @Override
        public void init() {

        }

        }

       /* @Override
        public void init() {
            container.setTopCellSpacing(0);
            container.setBottomCellSpacing(0);
            container.setLeftCellSpacing(0);
            container.setInnerCellSpacing(0);
            container.setBorderShown(false);

//            final HashTableEntryWrapper[] ownerValue = (HashTableEntryWrapper[]) owner.getValue();
            final WrapperWithValue[] ownerValue = owner.getValue();
            for (int i = 0; i < ownerValue.length; i++) {
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
        }*/


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
