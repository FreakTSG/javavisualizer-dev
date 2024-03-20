package wrapper_classes;

public class HashTableEntryWrapper<T> extends ReferenceableWrapperWithValue<T> {
    private static final long serialVersionUID = 1L;


    public HashTableEntryWrapper(Class<T> valueClass, String valueTypeName, T value) {
        super(valueClass, valueTypeName, value);
    }
}
