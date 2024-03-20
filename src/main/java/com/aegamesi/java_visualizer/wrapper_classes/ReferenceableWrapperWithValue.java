package wrapper_classes;

public abstract class ReferenceableWrapperWithValue<T> extends WrapperWithValue<T> implements Referenceable {
    private static final long serialVersionUID = 1L;


    public ReferenceableWrapperWithValue(Class<T> valueClass, String valueTypeName) {
        super(valueClass, valueTypeName);
    }

    public ReferenceableWrapperWithValue(Class<T> valueClass, String valueTypeName, T value) {
        super(valueClass, valueTypeName, value);
    }
}
