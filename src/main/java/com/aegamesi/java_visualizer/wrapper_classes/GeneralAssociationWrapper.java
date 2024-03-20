package wrapper_classes;

public class GeneralAssociationWrapper<T> extends WrapperWithValue<T> {
    private static final long serialVersionUID = 1L;

    public GeneralAssociationWrapper(Class<T> valueClass, String valueTypeName, T value) {
        super(valueClass, valueTypeName, value);
    }
}
