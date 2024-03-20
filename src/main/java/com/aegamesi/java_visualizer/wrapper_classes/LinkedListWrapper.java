package wrapper_classes;

import aed.colecoes.iteraveis.lineares.ColecaoIteravelLinear;

public abstract class LinkedListWrapper<T extends ColecaoIteravelLinear<TComponent>, TComponent extends WrapperWithValue>
        extends DataStructureWithComponentClassWrapper<T, TComponent> implements LinkedListOrSortedHashTableWrapper {
    private static final long serialVersionUID = 1L;



    public LinkedListWrapper(Class<T> valueClass, Class<TComponent> componentClass, String componentTypeName) {
        super(valueClass, valueClass.getName() + "<" + componentTypeName + ">", componentClass, componentTypeName);
//        value = new LinkedList<>();
    }


    @Override
    public void remove(int index) {
        value.removerPorIndice(index);
    }

}
