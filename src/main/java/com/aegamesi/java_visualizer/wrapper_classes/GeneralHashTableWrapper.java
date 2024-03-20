package wrapper_classes;

import aed.colecoes.iteraveis.associativas.ColecaoIteravelAssociativa;
import ui.Canvas;
import ui.dialogs.CreationHashTableRepresentationDialog;
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
import java.util.ArrayList;


public abstract class GeneralHashTableWrapper<T extends ColecaoIteravelAssociativa<K, V>, K extends WrapperWithValue, V extends WrapperWithValue, TGeneralHashTableKeyWrapper extends GeneralHashTableKeyWrapper<V, ?>>
        extends DataStructureWithComponentClassWrapper<T, V> {
    private static final long serialVersionUID = 1L;


    protected Class<K> keyClass;
    protected TGeneralHashTableKeyWrapper generalHashTableKeyWrapper;

    //                          tabela de hash          classe do valor     tipo do valor               classe da chave     definicao da chave
//    public HashTableWrapper(Class<T> valueClass, Class<V> componentClass, String componentTypeName, Class<K> keyClass, HashTableKeyWrapper<V> generalHashTableKeyWrapper) {
    public GeneralHashTableWrapper(Class<T> hashTableClass, Class<V> valueClass, String valueTypeName, Class<K> keyClass, TGeneralHashTableKeyWrapper generalHashTableKeyWrapper) {
        super(hashTableClass, hashTableClass.getName() + "<" + keyClass.getName() + ", " + valueTypeName + ">", valueClass, valueTypeName);
        this.keyClass = keyClass;
        this.generalHashTableKeyWrapper = generalHashTableKeyWrapper;
    }


    public Class<K> getKeyClass() {
        return keyClass;
    }

    public TGeneralHashTableKeyWrapper getGeneralHashTableKeyWrapper() {
        return generalHashTableKeyWrapper;
    }

    public void remove(K keyValue) {
        value.remover(keyValue);
    }
}
