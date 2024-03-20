package wrapper_classes;

import aed.colecoes.iteraveis.lineares.ordenadas.estruturas.ListaDuplaOrdenadaOrdemDistinta;
import ui.MyButton;
import ui.Resources;

import javax.swing.*;

public class DistinctSortedCircularDoubleLinkedListWithBaseMaxOrderWrapper<TComponent extends WrapperWithValue>
        extends SortedLinkedListWrapper<ListaDuplaOrdenadaOrdemDistinta<TComponent>, TComponent> {
    private static final long serialVersionUID = 1L;


    public DistinctSortedCircularDoubleLinkedListWithBaseMaxOrderWrapper(Class<ListaDuplaOrdenadaOrdemDistinta<TComponent>> valueClass, Class<TComponent> componentClass,
                                                                         String componentTypeName, ComparatorWrapper<TComponent> comparatorWrapper) {
        super(valueClass, componentClass, componentTypeName, comparatorWrapper);
        value = new ListaDuplaOrdenadaOrdemDistinta<>(comparatorWrapper);
    }

    public static JButton getButton() {
        JButton button = new MyButton();
        button.setToolTipText("Lista Dupla Ordenada Ordem Distinta");
        button.setIcon(new ImageIcon(Resources.INSTANCE.distinctSortedCircularDoubleLinkedListMaxOrderMenuIcon));
        button.addActionListener(e -> SortedLinkedListWrapper.create(ListaDuplaOrdenadaOrdemDistinta.class, DistinctSortedCircularDoubleLinkedListWithBaseMaxOrderWrapper.class));
        return button;
    }

}
