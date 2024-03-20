package wrapper_classes;

public abstract class ProjectEntityOrPrimitiveOrEnumWrapper<T> extends ReferenceableWrapperWithValue<T> {
    private static final long serialVersionUID = 1L;


    public ProjectEntityOrPrimitiveOrEnumWrapper(Class<T> valueClass, T value) {
        super(valueClass, valueClass.getName(), value);
    }
}
