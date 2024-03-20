package wrapper_classes;

import aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaDuplaNaoOrdenada;
import ui.MyButton;
import ui.Resources;

import javax.swing.*;

public class UnsortedCircularDoubleLinkedListWithBaseWrapper<TComponent extends WrapperWithValue>
        extends UnsortedLinkedListWrapper<ListaDuplaNaoOrdenada<TComponent>, TComponent> {
    private static final long serialVersionUID = 1L;



    public UnsortedCircularDoubleLinkedListWithBaseWrapper(Class<ListaDuplaNaoOrdenada<TComponent>> valueClass, Class<TComponent> componentClass, String componentTypeName) {
        super(valueClass, componentClass, componentTypeName);
        value = new ListaDuplaNaoOrdenada<>();
    }

    public static JButton getButton() {
        JButton button = new MyButton();
        button.setToolTipText("Lista Dupla Não Ordenada");
        button.setIcon(new ImageIcon(Resources.INSTANCE.unsortedCircularDoubleLinkedListWithBaseMenuIcon));
        button.addActionListener(e -> UnsortedLinkedListWrapper.create(ListaDuplaNaoOrdenada.class, UnsortedCircularDoubleLinkedListWithBaseWrapper.class));
        return button;
    }
}
