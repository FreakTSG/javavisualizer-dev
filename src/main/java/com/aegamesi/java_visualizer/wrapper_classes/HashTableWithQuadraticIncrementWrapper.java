package wrapper_classes;

import aed.colecoes.iteraveis.associativas.estruturas.TabelaHashComIncrementoQuadratico;
import ui.MyButton;
import ui.Resources;

import javax.swing.*;

public class HashTableWithQuadraticIncrementWrapper<K extends WrapperWithValue, V extends WrapperWithValue> extends HashTableWrapper<TabelaHashComIncrementoQuadratico<K, V>, K, V> {
    private static final long serialVersionUID = 1L;


    public HashTableWithQuadraticIncrementWrapper(Class<TabelaHashComIncrementoQuadratico<K, V>> valueClass, Class<V> componentClass, String componentTypeName, Class<K> keyClass, HashTableKeyWrapper<V> hashTableKeyWrapper, int size) {
        super(valueClass, componentClass, componentTypeName, keyClass, hashTableKeyWrapper);
        value = new TabelaHashComIncrementoQuadratico<>(size);  //todo receber o tamanho da tabela
    }

    public static JButton getButton() {
        JButton button = new MyButton();
        button.setToolTipText("Tabela Hash com Incremento Quadrático");
        button.setIcon(new ImageIcon(Resources.INSTANCE.hashTableQuadraticMenuIcon));
        button.addActionListener(e -> HashTableWrapper.create(TabelaHashComIncrementoQuadratico.class, HashTableWithQuadraticIncrementWrapper.class));
        return button;
    }

}
