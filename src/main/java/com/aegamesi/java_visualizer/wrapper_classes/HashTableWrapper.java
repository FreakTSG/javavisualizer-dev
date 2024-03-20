package wrapper_classes;

import aed.colecoes.iteraveis.associativas.ColecaoIteravelAssociativa;
import ui.Canvas;
import ui.dialogs.CreationHashTableRepresentationDialog;
import ui.graphics.representations.ProjectEntityRepresentation;
import ui.graphics.representations.Representation;
import ui.graphics.representations.UnsortedFieldItem;
import ui.graphics.representations.VariableRepresentation;
import ui.graphics.representations.hash_tables.HashTableKeyRepresentation;
import ui.graphics.representations.hash_tables.HashTableRepresentation;
import ui.operators.Operator;
import ui.operators.building.BuildingOperator;
import utils.Utils;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


public abstract class HashTableWrapper<T extends ColecaoIteravelAssociativa<K, V>, K extends WrapperWithValue, V extends WrapperWithValue> extends GeneralHashTableWrapper<T, K, V, HashTableKeyWrapper<V>> {
    private static final long serialVersionUID = 1L;


    //                          tabela de hash          classe do valor     tipo do valor               classe da chave     definicao da chave
//    public HashTableWrapper(Class<T> valueClass, Class<V> componentClass, String componentTypeName, Class<K> keyClass, HashTableKeyWrapper<V> hashTableKeyWrapper) {
    public HashTableWrapper(Class<T> hashTableClass, Class<V> valueClass, String valueTypeName, Class<K> keyClass, HashTableKeyWrapper<V> hashTableKeyWrapper) {
        super(hashTableClass, valueClass, valueTypeName, keyClass, hashTableKeyWrapper);
    }

    public static <THashTableWrapper extends HashTableWrapper> void create(Class hashTableClass, Class<THashTableWrapper> hashTableWrapperClass) {
        final Representation sourceRepresentation = BuildingOperator.getSourceRepresentation();

        final Point hashTableRepresentationPosition = new Point(sourceRepresentation.getPosition());
        hashTableRepresentationPosition.translate(100, 0);

        final Canvas canvas = Operator.getCanvas();

        final Point variableRepresentationPosition = new Point(hashTableRepresentationPosition);
        variableRepresentationPosition.translate(-50, -50);
        final String sourceRepresentationOwnerTypeName = sourceRepresentation.getOwnerTypeName();

        VariableWrapper variableWrapper = new VariableWrapper(hashTableWrapperClass, sourceRepresentationOwnerTypeName, null);
//        VariableWrapper variableWrapper = new VariableWrapper(HashTableWrapper.class, hashTableWrapperClass.getName(), null);
        final VariableRepresentation variableRepresentation = new VariableRepresentation(variableRepresentationPosition, variableWrapper, canvas);

        final Class<? extends WrapperInterface> sourceRepresentationOwnerClass = (Class<? extends WrapperInterface>) ((WrapperWithValue) sourceRepresentation.getOwner()).getValue().getClass();
        HashTableKeyWrapper<? extends WrapperInterface> hashTableKeyWrapperTmp = new HashTableKeyWrapper<>(sourceRepresentationOwnerClass, sourceRepresentationOwnerTypeName, null);
        HashTableKeyRepresentation hashTableKeyRepresentation = new HashTableKeyRepresentation(hashTableRepresentationPosition, hashTableKeyWrapperTmp, canvas);
        hashTableKeyWrapperTmp.setGeneralHashTableKeyRepresentation(hashTableKeyRepresentation);

        CreationHashTableRepresentationDialog creationHashTableRepresentationDialog =
                new CreationHashTableRepresentationDialog(variableRepresentation, hashTableKeyRepresentation, "Tabela Hash de " + Utils.getClassSimpleName(sourceRepresentationOwnerTypeName));
        if (creationHashTableRepresentationDialog.getResult() == CreationHashTableRepresentationDialog.RESULT_OK) {
            final HashTableKeyWrapper hashTableKeyWrapper = hashTableKeyRepresentation.getOwner();


//            final HashTableWithQuadraticIncrementWrapper hashTableWithQuadraticIncrementWrapper =
//            new HashTableWithQuadraticIncrementWrapper<>(classToCompare, genericTypeName, hashTableKeyWrapper.getKeyClass(), hashTableKeyWrapper);
            final Constructor[] constructors = hashTableWrapperClass.getConstructors();
            HashTableWrapper newHashTableWrapper;
            try {
                newHashTableWrapper = (HashTableWrapper) constructors[0].newInstance(
                        hashTableClass, sourceRepresentationOwnerClass, sourceRepresentationOwnerClass.getName(),
                        hashTableKeyWrapper.getValueClass(), hashTableKeyWrapper,
                        creationHashTableRepresentationDialog.getHashTableSize());

                final HashTableRepresentation hashTableRepresentation = new HashTableRepresentation(hashTableRepresentationPosition, newHashTableWrapper, canvas);
//                variableWrapper = new VariableWrapper(hashTableWrapperClass, newHashTableWrapper.getValueTypeName(), newHashTableWrapper);
//                variableRepresentation.setOwner(variableWrapper);
                variableWrapper.setValue(newHashTableWrapper);
                variableWrapper.setValueTypeName(newHashTableWrapper.getValueTypeName());
                variableRepresentation.setVariableReference(hashTableRepresentation);
                canvas.add(variableRepresentation);
                canvas.add(hashTableRepresentation.getOwner(), hashTableRepresentation);
                canvas.getIDSToolWindow().addCodeToEditorPaneSourceCode(variableRepresentation.getDeclarationCode(hashTableRepresentation.getCreationCode()));
                canvas.repaint();
                canvas.addOperation(Utils.getUndoRedoCreationObjectMessage(variableWrapper, Utils.getClassSimpleName(newHashTableWrapper.getValueTypeName())));

                // (no caso de ter criado nova classe para a chave)
                if (creationHashTableRepresentationDialog.isNewClassCreated()) {
                    canvas.getIDSToolWindow().processProjectEntities(false);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public K getKey(V value) {
        ArrayList<UnsortedFieldItem> inUnsortedFieldItems = generalHashTableKeyWrapper.getGeneralHashTableKeyRepresentation().getInUnsortedFieldItems();
        UnsortedFieldItem firstUnsortedFieldItem = inUnsortedFieldItems.get(0);
        Class<?> firstFieldClass = firstUnsortedFieldItem.getClazz();
        Object valueValue = value.getValue();
        if (inUnsortedFieldItems.size() == 1) {
            Object ownerFromFieldItem = Utils.getOwnerFromFieldItem(valueValue, firstUnsortedFieldItem);
            Object fieldValue = Utils.getFieldValue(ownerFromFieldItem, firstUnsortedFieldItem.getField());
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
        Object[] params = new Object[inUnsortedFieldItems.size()];

        for (UnsortedFieldItem inUnsortedFieldItem : inUnsortedFieldItems) {
            Object ownerFromFieldItem = Utils.getOwnerFromFieldItem(valueValue, inUnsortedFieldItem);
            if (ownerFromFieldItem == null) {
                throw new IllegalArgumentException("Chave inexistente para " + value + "!");
            }

            Field inUnsortedFieldItemField = inUnsortedFieldItem.getField();

            int index = 0;
            for (Field declaredField : generalHashTableKeyWrapper.valueClass.getDeclaredFields()) {
                if (declaredField.getName().equals(inUnsortedFieldItemField.getName())) {
                    break;
                }
                index++;
            }
            params[index] = Utils.getFieldValue(ownerFromFieldItem, inUnsortedFieldItemField);
        }
        try {
            ProjectEntityWrapper projectEntityWrapper = new ProjectEntityWrapper(generalHashTableKeyWrapper.valueClass, generalHashTableKeyWrapper.valueClass.getConstructors()[0].newInstance(params));

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
            variableWrapper.setName(VariableWrapper.getNewVariableName());
            VariableRepresentation variableRepresentation = new VariableRepresentation(variablePosition, variableWrapper, canvas);
            variableRepresentation.setVariableReference(projectEntityRepresentation);

            projectEntityRepresentation.update();
            canvas.add(variableRepresentation);
            canvas.add(projectEntityWrapper, projectEntityRepresentation);
            canvas.getIDSToolWindow().addCodeToEditorPaneSourceCode(variableRepresentation.getDeclarationCode(projectEntityRepresentation.getCreationCode()));

            return (K) projectEntityWrapper;


        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


}
