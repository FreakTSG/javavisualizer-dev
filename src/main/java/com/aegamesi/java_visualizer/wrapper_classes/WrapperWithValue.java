package wrapper_classes;

public abstract class WrapperWithValue<T> extends WrapperWithValueClass<T> {
    private static final long serialVersionUID = 1L;


    protected T value;

    public WrapperWithValue(Class<T> valueClass, String valueTypeName) {
        this(valueClass, valueTypeName, null);
    }

    public WrapperWithValue(Class<T> valueClass, String valueTypeName, T value) {
        super(valueClass, valueTypeName);
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof WrapperWithValue && value.equals(((WrapperWithValue<?>) obj).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
