package wrapper_classes;

public class SortedHashTableNodeWrapper<T> extends NodeWrapper<T, SortedHashTableWrapper> {
    private static final long serialVersionUID = 1L;


    public SortedHashTableNodeWrapper(Class<T> valueClass, String valueTypeName, T value, SortedHashTableWrapper sortedHashTableWrapper, int index) {
        super(valueClass, valueTypeName, value, sortedHashTableWrapper, index);
    }
}
