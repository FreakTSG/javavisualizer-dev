package wrapper_classes;

import aed.colecoes.iteraveis.associativas.estruturas.TabelaHashComIncrementoLinear;
import ui.MyButton;
import ui.Resources;

import javax.swing.*;

public class HashTableWithLinearIncrementWrapper<K extends WrapperWithValue, V extends WrapperWithValue> extends HashTableWrapper<TabelaHashComIncrementoLinear<K, V>, K, V> {
    private static final long serialVersionUID = 1L;


    public HashTableWithLinearIncrementWrapper(Class<TabelaHashComIncrementoLinear<K, V>> valueClass, Class<V> componentClass, String componentTypeName, Class<K> keyClass, HashTableKeyWrapper<V> hashTableKeyWrapper, int size) {
        super(valueClass, componentClass, componentTypeName, keyClass, hashTableKeyWrapper);
        value = new TabelaHashComIncrementoLinear<>(size);  //todo receber o tamanho da tabela
    }

    public static JButton getButton() {
        JButton button = new MyButton();
        button.setToolTipText("Tabela Hash com Incremento Linear");
        button.setIcon(new ImageIcon(Resources.INSTANCE.hashTableLinearMenuIcon));
        button.addActionListener(e -> HashTableWrapper.create(TabelaHashComIncrementoLinear.class, HashTableWithLinearIncrementWrapper.class));
        return button;
    }

}
