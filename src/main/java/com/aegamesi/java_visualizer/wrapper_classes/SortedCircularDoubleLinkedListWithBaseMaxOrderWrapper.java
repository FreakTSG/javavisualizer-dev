package wrapper_classes;

import aed.colecoes.iteraveis.lineares.ordenadas.estruturas.ListaDuplaOrdenada;
import ui.MyButton;
import ui.Resources;

import javax.swing.*;

public class SortedCircularDoubleLinkedListWithBaseMaxOrderWrapper<TComponent extends WrapperWithValue>
        extends SortedLinkedListWrapper<ListaDuplaOrdenada<TComponent>, TComponent> {
    private static final long serialVersionUID = 1L;


    public SortedCircularDoubleLinkedListWithBaseMaxOrderWrapper(Class<ListaDuplaOrdenada<TComponent>> valueClass, Class<TComponent> componentClass,
                                                                 String componentTypeName, ComparatorWrapper<TComponent> comparatorWrapper) {
        super(valueClass, componentClass, componentTypeName, comparatorWrapper);
        value = new ListaDuplaOrdenada<>(comparatorWrapper);
    }

    public static JButton getButton() {
        JButton button = new MyButton();
        button.setToolTipText("Lista Dupla Ordenada");
        button.setIcon(new ImageIcon(Resources.INSTANCE.sortedCircularDoubleLinkedListMaxOrderMenuIcon));
        button.addActionListener(e -> SortedLinkedListWrapper.create(ListaDuplaOrdenada.class, SortedCircularDoubleLinkedListWithBaseMaxOrderWrapper.class));
        return button;
    }

}
