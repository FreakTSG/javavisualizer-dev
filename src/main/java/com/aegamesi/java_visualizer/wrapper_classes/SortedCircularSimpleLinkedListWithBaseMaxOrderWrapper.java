package wrapper_classes;

import aed.colecoes.iteraveis.lineares.ordenadas.estruturas.ListaSimplesOrdenada;
import ui.MyButton;
import ui.Resources;

import javax.swing.*;

public class SortedCircularSimpleLinkedListWithBaseMaxOrderWrapper<TComponent extends WrapperWithValue>
        extends SortedLinkedListWrapper<ListaSimplesOrdenada<TComponent>, TComponent> {
    private static final long serialVersionUID = 1L;


    public SortedCircularSimpleLinkedListWithBaseMaxOrderWrapper(Class<ListaSimplesOrdenada<TComponent>> valueClass, Class<TComponent> componentClass,
                                                                 String componentTypeName, ComparatorWrapper<TComponent> comparatorWrapper) {
        super(valueClass, componentClass, componentTypeName, comparatorWrapper);
        value = new ListaSimplesOrdenada<>(comparatorWrapper);
    }

    public static JButton getButton() {
        JButton button = new MyButton();
        button.setToolTipText("Lista Simples Ordenada");
        button.setIcon(new ImageIcon(Resources.INSTANCE.sortedCircularSimpleLinkedListWithMaxOrderMenuIcon));
        button.addActionListener(e -> SortedLinkedListWrapper.create(ListaSimplesOrdenada.class, SortedCircularSimpleLinkedListWithBaseMaxOrderWrapper.class));
        return button;
    }

}
