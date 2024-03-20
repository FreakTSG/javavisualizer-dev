package wrapper_classes;

public abstract class NodeWrapper<T, TLinkedListOrSortedHashTableWrapper extends LinkedListOrSortedHashTableWrapper> extends WrapperWithValue<T> implements NonReferenceable {
    private static final long serialVersionUID = 1L;

    private TLinkedListOrSortedHashTableWrapper linkedListOrSortedHashTableWrapper;
    private int index;

    public NodeWrapper(Class<T> valueClass, String valueTypeName, T value, TLinkedListOrSortedHashTableWrapper linkedListOrSortedHashTableWrapper, int index) {
        super(valueClass, valueTypeName, value);
        this.linkedListOrSortedHashTableWrapper = linkedListOrSortedHashTableWrapper;
        this.index = index;
    }

    public TLinkedListOrSortedHashTableWrapper getLinkedListOrSortedHashTableWrapper() {
        return linkedListOrSortedHashTableWrapper;
    }

    public int getIndex() {
        return index;
    }
}
