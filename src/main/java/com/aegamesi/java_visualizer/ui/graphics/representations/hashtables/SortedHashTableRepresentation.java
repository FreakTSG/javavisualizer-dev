package com.aegamesi.java_visualizer.ui.graphics.representations.hashtables;

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

    public SortedHashTableRepresentation(Point position, TabelaHashOrdenada sortedHashTableWrapper, MyCanvas myCanvas) {
        super(position, sortedHashTableWrapper, myCanvas, false);
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

        //TabelaHashOrdenada ownerValue = (TabelaHashOrdenada) owner.getClass();

        topContainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), true);
        topContainer.setCellSpacing(0);
        topContainer.setInnerCellSpacing(20);
        topContainer.setBorderShown(false);

        ContainerWithoutInConnectors topLeftContainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), false);
        topLeftContainer.setCellSpacing(0);
        topLeftContainer.setBorderShown(false);
        topContainer.add(topLeftContainer);

        int dim = 14;
        //baseFieldReference = new FieldReference(new Dimension(dim, dim), owner, Utils.getField(ownerValue, Constants.BASE), Location.CENTER, false);
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
//        listContainer.setBottomCellSpacing(20);
        listContainer.setTopCellSpacing(0);
        listContainer.setLeftCellSpacing(0);
        listContainer.setRightCellSpacing(0);
        listContainer.setBorderShown(false);
        listAndAssociationsContainer.add(listContainer);
        topContainer.add(listAndAssociationsContainer);

        container.add(topContainer, Location.LEFT);

        //comparatorFieldReference = new FieldReference(new Dimension(dim, dim), owner, Utils.getField(ownerValue, Constants.COMPARATOR_BY_ORDER), Location.CENTER, false);
        topLeftContainer.add(comparatorFieldReference, Location.LEFT);
        //hashTableFieldReference = new FieldReference(new Dimension(dim, dim), owner, Utils.getField(ownerValue, Constants.NO_POR_CHAVE), Location.CENTER, false);
        topLeftContainer.add(hashTableFieldReference, Location.LEFT);

        actualIndex = -1;
        createNode(baseFieldReference);

        //connection from base node to tail node
        TabelaHashOrdenada.No baseNode = (TabelaHashOrdenada.No) baseFieldReference.getFieldValue();
        FieldReference tailNodeReference = nodeRepresentationByOwner.get(baseNode).getPreviousFieldReference();
        Object tailNode = tailNodeReference.getFieldValue();
        addNewConnection(new CConnection(tailNodeReference.getOutConnector(), nodeRepresentationByOwner.get(tailNode), Color.RED, 14, 21, tailNode == baseNode));


        //RepresentationWithInConnectors representationWithInConnectors = myCanvas.getRepresentationWithInConnectors(((ComparatorWrapper) ownerValue.getComparador()));
        //addNewConnection(new StraightConnection(comparatorFieldReference.getOutConnector(), representationWithInConnectors, Color.RED));

        //final TabelaHashComIncrementoPorHash noPorChave = ((TabelaHashComIncrementoPorHash) Utils.getFieldValue(ownerValue, Constants.NO_POR_CHAVE));
        //SortedHashTableKeyWrapper sortedHashTableKeyWrapper = (SortedHashTableKeyWrapper) owner.getGeneralHashTableKeyWrapper();
        //HashTableKeyWrapper hashTableKeyWrapper = new HashTableKeyWrapper(sortedHashTableKeyWrapper.getValueClass(), sortedHashTableKeyWrapper.getValueTypeName(),
        //        null);

        //HashTableKeyRepresentation hashTableKeyRepresentation = new HashTableKeyRepresentation(new Point(), hashTableKeyWrapper, myCanvas);
        //hashTableKeyWrapper.setGeneralHashTableKeyRepresentation(hashTableKeyRepresentation);
        //SortedHashTableKeyRepresentation sortedHashTableKeyRepresentation = (SortedHashTableKeyRepresentation) sortedHashTableKeyWrapper.getGeneralHashTableKeyRepresentation();
        //for (SortedFieldItem inSortedFieldItem : sortedHashTableKeyRepresentation.getInSortedFieldItems()) {
        //    hashTableKeyRepresentation.getInUnsortedFieldItems().add(new UnsortedFieldItem(inSortedFieldItem.getClazz(), inSortedFieldItem.getPrefix(), inSortedFieldItem.getField()));
        //}
        //hashTableKeyRepresentation.update();

        //HashTableWithIncrementByHashWrapper hashTableWithIncrementByHashWrapper = new HashTableWithIncrementByHashWrapper(
        //        TabelaHashComIncrementoPorHash.class, owner.getValueClass(), owner.getComponentTypeName(),
        //        owner.getKeyClass(), hashTableKeyWrapper, 0, noPorChave);
        //sortedInnerHashTableRepresentation = new SortedInnerHashTableRepresentation(new Point(), hashTableWithIncrementByHashWrapper, this, myCanvas);

        addNewConnection(new StraightConnection(hashTableFieldReference.getOutConnector(), sortedInnerHashTableRepresentation, Color.RED));


        container.add(sortedInnerHashTableRepresentation.getContainer(), Location.LEFT);
        container.update();
        myCanvas.repaint();
    }


    private void createNode(FieldReference nodeFieldReference) {
        TabelaHashOrdenada.No node = (TabelaHashOrdenada.No) nodeFieldReference.getFieldValue();

        //final SortedHashTableNode sortedHashTableNode = new SortedHashTableNode(node.getClass(), node.getClass().getName(), node, owner, actualIndex);
        //DoubleNodeRepresentation<TabelaHashOrdenada.No, TabelaHashOrdenada> doubleNodeRepresentation =
           //    new DoubleNodeRepresentation<>(new Point(), TabelaHashOrdenada, myCanvas, this);
        ContainerWithoutInConnectors nodeAndIndexContainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), false);
        //nodeAndIndexContainer.add(doubleNodeRepresentation.getContainer());
        nodeAndIndexContainer.setInnerCellSpacing(0);
        NormalTextElement indexNormalTextElement = new NormalTextElement(actualIndex == -1 ? getBaseNodeIndexText() : String.valueOf(actualIndex), ConstantsIDS.FONT_SIZE_INDEX);
        //doubleNodeRepresentation.setIndex(actualIndex);
        actualIndex++;
        indexNormalTextElement.setBackgroundColor(GraphicElement.TRANSPARENT_COLOR);
        indexNormalTextElement.setBorderShown(false);
        nodeAndIndexContainer.add(indexNormalTextElement);
        nodeAndIndexContainer.setBorderShown(false);
        listContainer.add(nodeAndIndexContainer);

        //nodeRepresentationByOwner.put(node, doubleNodeRepresentation);
        //addNewConnection(new StraightConnection(nodeFieldReference.getOutConnector(), doubleNodeRepresentation, Color.RED));

        //connect the node to its element
        //FieldReference elementFieldReference = doubleNodeRepresentation.getElementFieldReference();
        //Associacao associacao = (Associacao) elementFieldReference.getFieldValue();
        //if (associacao != null) {
//            ContainerWithoutInConnectors associationContainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), false);

            //AssociationForSortedHashTableWrapper<Associacao> associationForSortedHashTableWrapper =
            //        new AssociationForSortedHashTableWrapper<>(Associacao.class, Associacao.class.getName(), associacao);
            //if (!myCanvas.isCompactMode()) {
                //AssociationRepresentation associationRepresentation =
                //        new AssociationRepresentation(new Point(), associationForSortedHashTableWrapper, myCanvas);
                //associationsContainer.add(associationRepresentation.getContainer());
//                canvas.add(associationForSortedHashTableWrapper, associationRepresentation);
                //addNewConnection(
                //        new StraightConnection(elementFieldReference.getOutConnector(), associationRepresentation,
                 //               Constants.LINKED_LIST_ELEMENTS_CONNECTIONS_COLOR));
//                addNewConnection(
//                        new StraightConnection(elementFieldReference.getOutConnector(), canvas.getRepresentationWithInConnectors(associationForSortedHashTableWrapper),
//                                Constants.LINKED_LIST_ELEMENTS_CONNECTIONS_COLOR));
            //} else {
                Field value = null;
                //try {
                    //value = associacao.getClass().getDeclaredField(Constants.VALUE);
                    //addNewConnection(new StraightConnection<>(elementFieldReference.getOutConnector(), myCanvas.getRepresentationWithInConnectors((ProjectEntityOrPrimitiveOrEnumWrapper) Utils.getFieldValue(associacao, value)), Constants.HASH_TABLE_ELEMENTS_CONNECTIONS_COLOR));
                //} catch (NoSuchFieldException e) {
                //    e.printStackTrace();
                //    JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao obter valor da Associação", JOptionPane.ERROR_MESSAGE);
                //}
            }
       // }

        //then deal the next node
        //FieldReference nextFieldReference = doubleNodeRepresentation.getNextFieldReference();

        //TabelaHashOrdenada.No nextNode = (TabelaHashOrdenada.No) nextFieldReference.getFieldValue();

        //create connection from last to first(base)
        //if (baseFieldReference.getFieldValue() == nextNode) {
        //    addNewConnection(new CConnection(nextFieldReference.getOutConnector(), nodeRepresentationByOwner.get(nextNode), Color.RED, 14, 21, nextNode == node));
        //} else {
        //    createNode(nextFieldReference);
         //   DoubleNodeRepresentation nextNodeRepresentation = nodeRepresentationByOwner.get(nextNode);
         //   addNewConnection(new StraightConnection(nextNodeRepresentation.getPreviousFieldReference().getOutConnector(), doubleNodeRepresentation, Color.RED));
        //}
//        container.update();
        //myCanvas.repaint();
    //}

    protected String getBaseNodeIndexText() {
        return "-";
    }


    /*@Override
    public void add(RepresentationWithInConnectors representationWithInConnectors) {
        try {

            final WrapperWithValue value = (WrapperWithValue) representationWithInConnectors.getOwner();
//            canvas.getIDSToolWindow().processProjectEntities();
            WrapperWithValue keyWrapper = owner.getKey(value);
            if (keyWrapper == null) {
                JOptionPane.showMessageDialog(null, "Chave nula", "Erro de inserção", JOptionPane.ERROR_MESSAGE);
                return;
            }


            //code generation - attention => must be before the operation
            //String ownerVariableName = myCanvas.getFirstReferenceTo(this);
           // String elementVariableName = myCanvas.getFirstReferenceTo(representationWithInConnectors);
            //String firstReferenceToKeyWrapper = myCanvas.getFirstReferenceTo(keyWrapper);
            //String keyCode = firstReferenceToKeyWrapper.equals(Constants.UNKOWN_REF) ?
                    getCode(keyWrapper.getValue()) :
                    firstReferenceToKeyWrapper;
//            String keyCode = getCode(keyWrapper.getValue());
            String code = ownerVariableName + ".inserir(" + keyCode + ", " + elementVariableName + ")";
            myCanvas.getIDSToolWindow().addCodeToEditorPaneSourceCode(code + ";\n");

            ((TabelaHashOrdenada) owner.getValue()).inserir(keyWrapper, value);
            sortedInnerHashTableRepresentation.clearConnections();
            super.add(representationWithInConnectors);
            myCanvas.addOperation(Utils.getUndoRedoAddElementMessage(code));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro Tabela Hash", JOptionPane.ERROR_MESSAGE);
        }
    }*/

    private String getCode(Object key) {
        String keyExpression = "";
        if (key instanceof String) {
            keyExpression = "\"" + key + "\"";
        } else if (key instanceof Character) {
            keyExpression = "'\\u" + Integer.toHexString(((Character) key).charValue() | 0x10000).substring(1) + "'";
//        } else if (key.getClass().getSimpleName().equals("byte[]")) {
//            keyExpression = "\"" + new String((byte[])key) + "\"";
        } else if (key instanceof Enum) {
            keyExpression = key.getClass().getSimpleName() + "." + key;
        } else {
            keyExpression = key.toString();
        }
        return keyExpression;
    }

    @Override
    public void rebuild() {
        container.removeAllGraphicElements();
        sortedInnerHashTableRepresentation.clearConnections();
        super.rebuild();
    }

    /*public String getCreationCode() {
        //todo
        StringBuilder sb = new StringBuilder("new ");
        String valueTypeName = owner.getClass().getName();
        myCanvas.getIDSToolWindow().addImportToEditorPaneSourceCode(valueTypeName);
        myCanvas.getIDSToolWindow().addImportToEditorPaneSourceCode(owner.getClass().getName());
        sb.append(Utils.getClassSimpleName(Utils.getValueTypeNameWithoutParametrizedTypes(valueTypeName))).append("(");
        sb.append(myCanvas.getFirstReferenceTo(myCanvas.getRepresentationWithInConnectors((ComparatorWrapper) ((TabelaHashOrdenada) owner.getValue()).getComparador())));
        sb.append(", ");
        sb.append(((TabelaHash.Entrada[]) Utils.getFieldValue(sortedInnerHashTableRepresentation.getOwner().getValue(), Constants.TABLE)).length).append(")");
        return sb.toString();
    }*/

    public DoubleNodeRepresentation getNodeRepresentation(TabelaHashOrdenada.No node) {
        return nodeRepresentationByOwner.get(node);
    }

   /* @Override
    public ButtonBar getButtonBar(Point position) {
        return sortedInnerHashTableRepresentation.getButtonBar(position);
//        return super.getButtonBar(position);
    }*/


//        public FieldReference getValueFieldReference() {
//            return valueFieldReference;
//        }


   /* class AssociationRepresentation extends GeneralRepresentation<GeneralAssociationWrapper<Associacao>> {
        private static final long serialVersionUID = 1L;

        private FieldReference valueFieldReference;
        private WrapperWithValue keyValue;

        public AssociationRepresentation(Point position, GeneralAssociationWrapper<Associacao> associationForSortedHashTableWrapper, Canvas canvas) {
            super(position, associationForSortedHashTableWrapper, canvas, true);
        }

        @Override
        public void init() {
            try {
                final Associacao association = owner.getClass();
                Field key = association.getClass().getDeclaredField(Constants.KEY);
                Field value = association.getClass().getDeclaredField(Constants.VALUE);

                int dim = 14;
                container.setCellSpacing(0);

                keyValue = (WrapperWithValue) Utils.getFieldValue(association, key);
                Object keyValueValue = keyValue.getValue();

                if (Utils.isPrimitiveOrPrimitiveWrapperType(keyValueValue.getClass().getSimpleName()) || keyValueValue.getClass().isEnum() || keyValueValue instanceof String) {
                    final NormalTextElement keyNormalTextElement = new NormalTextElement(keyValueValue.toString(), Constants.FONT_SIZE_TEXT - 2);
                    keyNormalTextElement.setBorderShown(true);
                    container.add(keyNormalTextElement);
                } else {
                    FieldReference keyFieldReference = new FieldReference(new Dimension(dim, dim), owner, key, Location.CENTER, false);
                    container.add(keyFieldReference);
                    addNewConnection(new StraightConnection<>(keyFieldReference.getOutConnector(), myCanvas.getRepresentationWithInConnectors(keyValue)));
                }
                valueFieldReference = new FieldReference(new Dimension(dim, dim), new AssociationWrapper(Associacao.class, Associacao.class.getName(), association, SortedHashTableRepresentation.this.owner), value, Location.CENTER, false);
                container.add(valueFieldReference);
                addNewConnection(new StraightConnection<>(valueFieldReference.getOutConnector(), myCanvas.getRepresentationWithInConnectors((ProjectEntityOrPrimitiveOrEnumWrapper) Utils.getFieldValue(association, value)), Constants.HASH_TABLE_ELEMENTS_CONNECTIONS_COLOR));

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao Criar Representação da Associação da Tabela Hash Ordenada");
            }
        }*/

        // FieldReference getValueFieldReference() {
        //    return valueFieldReference;
        //}

    }



