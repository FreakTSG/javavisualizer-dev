package wrapper_classes;

import aed.colecoes.iteraveis.associativas.estruturas.TabelaHashOrdenada;
import ui.Canvas;
import ui.MyButton;
import ui.Resources;
import ui.dialogs.CreationSortedHashTableRepresentationDialog;
import ui.graphics.representations.*;
import ui.graphics.representations.hash_tables.SortedHashTableKeyRepresentation;
import ui.graphics.representations.hash_tables.SortedHashTableRepresentation;
import ui.operators.Operator;
import ui.operators.building.BuildingOperator;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class SortedHashTableWrapper<K extends WrapperWithValue, V extends WrapperWithValue>
        extends GeneralHashTableWrapper<TabelaHashOrdenada<K, V>, K, V, SortedHashTableKeyWrapper<V>>
        implements LinkedListOrSortedHashTableWrapper {
    private static final long serialVersionUID = 1L;


    public SortedHashTableWrapper(Class<TabelaHashOrdenada<K, V>> valueClass, Class<V> componentClass, String componentTypeName, Class<K> keyClass, SortedHashTableKeyWrapper<V> hashTableKeyWrapper, int size, ComparatorWrapper<K> comparatorWrapper) {
        super(valueClass, componentClass, componentTypeName, keyClass, hashTableKeyWrapper);
        value = new TabelaHashOrdenada<>(comparatorWrapper, size);
    }

    public static JButton getButton() {
        JButton button = new MyButton();
        button.setToolTipText("Tabela Hash Ordenada");
        button.setIcon(new ImageIcon(Resources.INSTANCE.sortedHashTableByHashMenuIcon));
        button.addActionListener(e -> create());
        return button;
    }

    public static void create() {
        final Representation sourceRepresentation = BuildingOperator.getSourceRepresentation();

        final Point sortedHashTableRepresentationPosition = new Point(sourceRepresentation.getPosition());
        sortedHashTableRepresentationPosition.translate(100, 0);

        final Canvas canvas = Operator.getCanvas();

        final Point sortedHashTableVariableRepresentationPosition = new Point(sortedHashTableRepresentationPosition);
        sortedHashTableVariableRepresentationPosition.translate(-50, -50);

        final String sourceRepresentationOwnerTypeName = sourceRepresentation.getOwnerTypeName();

        VariableWrapper sortedHashTableVariableWrapper = new VariableWrapper(SortedHashTableWrapper.class, sourceRepresentationOwnerTypeName, null);
        final VariableRepresentation sortedHashTableVariableRepresentation = new VariableRepresentation(sortedHashTableVariableRepresentationPosition, sortedHashTableVariableWrapper, canvas);

        WrapperInterface sourceRepresentationOwner = sourceRepresentation.getOwner();

        final Class<? extends WrapperInterface> sourceRepresentationOwnerClass = (Class<? extends WrapperInterface>) ((WrapperWithValue) sourceRepresentationOwner).getValue().getClass();
        SortedHashTableKeyWrapper<? extends WrapperInterface> sortedHashTableKeyWrapperTmp =
                new SortedHashTableKeyWrapper<>(sourceRepresentationOwnerClass, sourceRepresentationOwnerTypeName, null);
        SortedHashTableKeyRepresentation sortedHashTableKeyRepresentation =
                new SortedHashTableKeyRepresentation(sortedHashTableRepresentationPosition, sortedHashTableKeyWrapperTmp, canvas);
        sortedHashTableKeyWrapperTmp.setGeneralHashTableKeyRepresentation(sortedHashTableKeyRepresentation);

        CreationSortedHashTableRepresentationDialog creationSortedHashTableRepresentationDialog =
                new CreationSortedHashTableRepresentationDialog(sortedHashTableVariableRepresentation, sortedHashTableKeyRepresentation, "Tabela Hash Ordenada de " + Utils.getClassSimpleName(sourceRepresentationOwnerTypeName));
        if (creationSortedHashTableRepresentationDialog.getResult() == CreationSortedHashTableRepresentationDialog.RESULT_OK) {
            final SortedHashTableKeyWrapper sortedHashTableKeyWrapper = sortedHashTableKeyRepresentation.getOwner();


            final Constructor[] constructors = SortedHashTableWrapper.class.getConstructors();
            SortedHashTableWrapper newSortedHashTableWrapper;
            try {

                newSortedHashTableWrapper = (SortedHashTableWrapper) constructors[0].newInstance(
                        TabelaHashOrdenada.class, sourceRepresentationOwnerClass, sourceRepresentationOwnerClass.getName(),
                        sortedHashTableKeyWrapper.getValueClass(), sortedHashTableKeyWrapper,
                        creationSortedHashTableRepresentationDialog.getHashTableSize(), creationSortedHashTableRepresentationDialog.getComparatorWrapper());
//                newSortedHashTableWrapper = (SortedHashTableWrapper) constructors[0].newInstance(
//                        TabelaHashOrdenada.class, sourceRepresentationOwnerClass, sourceRepresentationOwnerClass.getName(),
//                        ProjectEntityWrapper.class, sortedHashTableKeyWrapper,
//                        creationSortedHashTableRepresentationDialog.getHashTableSize(), creationSortedHashTableRepresentationDialog.getComparatorWrapper());


                final SortedHashTableRepresentation sortedHashTableRepresentation = new SortedHashTableRepresentation(sortedHashTableRepresentationPosition, newSortedHashTableWrapper, canvas);
                sortedHashTableVariableWrapper.setValue(newSortedHashTableWrapper);
                sortedHashTableVariableWrapper.setValueTypeName(newSortedHashTableWrapper.getValueTypeName());
                sortedHashTableVariableRepresentation.setVariableReference(sortedHashTableRepresentation);

                canvas.add(sortedHashTableVariableRepresentation);
                canvas.add(sortedHashTableRepresentation.getOwner(), sortedHashTableRepresentation);
                canvas.getIDSToolWindow().addCodeToEditorPaneSourceCode(sortedHashTableVariableRepresentation.getDeclarationCode(sortedHashTableRepresentation.getCreationCode()));

                canvas.repaint();
                canvas.addOperation(Utils.getUndoRedoCreationObjectMessage(sortedHashTableVariableWrapper, Utils.getClassSimpleName(newSortedHashTableWrapper.getValueTypeName())));

                //necessário para que as classes tenham o mesmo ClassLoader
                // (no caso de ter criado nova classe para a chave)
                if (creationSortedHashTableRepresentationDialog.isNewClassCreated()) {
                    canvas.getIDSToolWindow().processProjectEntities(false);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public K getKey(V value) {
        //special case of String value
//        if (componentClass.isAssignableFrom(String.class)) {
//            return (K) (value.value);
//        }
        ArrayList<SortedFieldItem> inSortedFieldItems = generalHashTableKeyWrapper.getGeneralHashTableKeyRepresentation().getInSortedFieldItems();
        SortedFieldItem firstSortedFieldItem = inSortedFieldItems.get(0);
        Class<?> firstFieldClass = firstSortedFieldItem.getClazz();
        Object valueValue = value.getValue();
        if (inSortedFieldItems.size() == 1) {
            Object ownerFromFieldItem = Utils.getOwnerFromFieldItem(valueValue, firstSortedFieldItem);
            Object fieldValue = Utils.getFieldValue(ownerFromFieldItem, firstSortedFieldItem.getField());
            return (K) new PrimitiveOrEnumWrapper(firstFieldClass, fieldValue);
        }
        String fieldName = generalHashTableKeyWrapper.getGeneralHashTableKeyRepresentation().getFieldName();
        Canvas canvas = Operator.getCanvas();
        if (fieldName != null) {
            //usar atributo com nome fieldName do valor
            Object fieldValue = Utils.getFieldValue(value.value, fieldName);
            return fieldValue == null ? null : (K) canvas.getWrapperWithValue(fieldValue);
        }
        //criar novo objeto
        Object[] params = new Object[inSortedFieldItems.size()];

        for (SortedFieldItem inSortedFieldItem : inSortedFieldItems) {
            Object ownerFromFieldItem = Utils.getOwnerFromFieldItem(valueValue, inSortedFieldItem);
            if (ownerFromFieldItem == null) {
                throw new IllegalArgumentException("Chave inexistente para " + value + "!");
            }

            Field inSortedFieldItemField = inSortedFieldItem.getField();

            int index = 0;
            for (Field declaredField : generalHashTableKeyWrapper.valueClass.getDeclaredFields()) {
                if (declaredField.getName().equals(inSortedFieldItemField.getName())) {
                    break;
                }
                index++;
            }
            params[index] = Utils.getFieldValue(ownerFromFieldItem, inSortedFieldItemField);
        }
        try {
            ProjectEntityWrapper projectEntityWrapper =
                    new ProjectEntityWrapper(generalHashTableKeyWrapper.valueClass, generalHashTableKeyWrapper.valueClass.getConstructors()[0].newInstance(params));

            //se houver objeto igual devolve-o
            WrapperWithValue wrapperInCanvas = canvas.getWrapperWithValueEqualsTo(projectEntityWrapper);
            if (wrapperInCanvas != null) {
                return (K) wrapperInCanvas;
            }

            Point valuePosition = canvas.getRepresentationWithInConnectors(value).getContainer().getPosition();
            Point newProjectEntityPosition = new Point(valuePosition);
            newProjectEntityPosition.translate(-100, -100);
            Point variablePosition = new Point(newProjectEntityPosition);
            variablePosition.translate(-50, -50);

            ProjectEntityRepresentation projectEntityRepresentation = new ProjectEntityRepresentation(valuePosition, projectEntityWrapper, canvas);
            VariableWrapper variableWrapper = new VariableWrapper(ProjectEntityWrapper.class, generalHashTableKeyWrapper.valueClass.getSimpleName(), projectEntityWrapper);
            VariableRepresentation variableRepresentation = new VariableRepresentation(variablePosition, variableWrapper, canvas);
            variableRepresentation.setVariableReference(projectEntityRepresentation);

            variableWrapper.setName(VariableWrapper.getNewVariableName());

            projectEntityRepresentation.update();
            canvas.add(variableRepresentation);
            canvas.add(projectEntityWrapper, projectEntityRepresentation);
            canvas.getIDSToolWindow().addCodeToEditorPaneSourceCode(variableRepresentation.getDeclarationCode(projectEntityRepresentation.getCreationCode()));

            return (K) projectEntityWrapper;



        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void remove(int index) {

    }
}
