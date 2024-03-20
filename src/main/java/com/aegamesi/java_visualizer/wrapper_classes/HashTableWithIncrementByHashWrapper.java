package wrapper_classes;

import aed.colecoes.iteraveis.associativas.estruturas.TabelaHashComIncrementoPorHash;
import ui.MyButton;
import ui.Resources;

import javax.swing.*;

public class HashTableWithIncrementByHashWrapper<K extends WrapperWithValue, V extends WrapperWithValue> extends HashTableWrapper<TabelaHashComIncrementoPorHash<K, V>, K, V> {
    private static final long serialVersionUID = 1L;


    public HashTableWithIncrementByHashWrapper(Class<TabelaHashComIncrementoPorHash<K, V>> valueClass, Class<V> componentClass, String componentTypeName, Class<K> keyClass, HashTableKeyWrapper<V> hashTableKeyWrapper, int size) {
        this(valueClass, componentClass, componentTypeName, keyClass, hashTableKeyWrapper, size, new TabelaHashComIncrementoPorHash<>(size));
    }
    public HashTableWithIncrementByHashWrapper(Class<TabelaHashComIncrementoPorHash<K, V>> valueClass, Class<V> componentClass, String componentTypeName, Class<K> keyClass, HashTableKeyWrapper<V> hashTableKeyWrapper, int size, TabelaHashComIncrementoPorHash<K, V> value) {
        super(valueClass, componentClass, componentTypeName, keyClass, hashTableKeyWrapper);
        this.value = value;
    }

    public static JButton getButton() {
        JButton button = new MyButton();
        button.setToolTipText("Tabela Hash com Incremento por Hash");
        button.setIcon(new ImageIcon(Resources.INSTANCE.hashTableByHashMenuIcon));
        button.addActionListener(e -> HashTableWrapper.create(TabelaHashComIncrementoPorHash.class, HashTableWithIncrementByHashWrapper.class));
        return button;
    }

}
