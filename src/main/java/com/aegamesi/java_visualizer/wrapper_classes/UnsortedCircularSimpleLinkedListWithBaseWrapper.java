package wrapper_classes;

import aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaSimplesNaoOrdenada;
import ui.MyButton;
import ui.Resources;

import javax.swing.*;

public class UnsortedCircularSimpleLinkedListWithBaseWrapper<TComponent extends WrapperWithValue>
        extends UnsortedLinkedListWrapper<ListaSimplesNaoOrdenada<TComponent>, TComponent> {
    private static final long serialVersionUID = 1L;



    public UnsortedCircularSimpleLinkedListWithBaseWrapper(Class<ListaSimplesNaoOrdenada<TComponent>> valueClass, Class<TComponent> componentClass, String componentTypeName) {
        super(valueClass, componentClass, componentTypeName);
        value = new ListaSimplesNaoOrdenada<>();
    }

    public static JButton getButton() {
        JButton button = new MyButton();
        button.setToolTipText("Lista Simples Não Ordenada");
        button.setIcon(new ImageIcon(Resources.INSTANCE.unsortedCircularsimpleLinkedListWithBaseMenuIcon));
        button.addActionListener(e -> UnsortedLinkedListWrapper.create(ListaSimplesNaoOrdenada.class, UnsortedCircularSimpleLinkedListWithBaseWrapper.class));
        return button;
    }
}
