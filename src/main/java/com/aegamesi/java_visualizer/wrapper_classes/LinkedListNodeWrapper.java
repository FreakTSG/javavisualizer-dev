package wrapper_classes;

public class LinkedListNodeWrapper<T> extends NodeWrapper<T, LinkedListWrapper> {
    private static final long serialVersionUID = 1L;


    public LinkedListNodeWrapper(Class<T> valueClass, String valueTypeName, T value, LinkedListWrapper linkedListWrapper, int index) {
        super(valueClass, valueTypeName, value, linkedListWrapper, index);
    }
}
