package wrapper_classes;

public class AssociationWrapper<T, TGeneralHashTableWrapper extends GeneralHashTableWrapper>
        extends GeneralAssociationWrapper<T>
        implements NonReferenceable {
    private static final long serialVersionUID = 1L;


    private final TGeneralHashTableWrapper generalHashTableWrapper;

    public AssociationWrapper(Class<T> valueClass, String valueTypeName, T value, TGeneralHashTableWrapper generalHashTableWrapper) {
        super(valueClass, valueTypeName, value);
        this.generalHashTableWrapper = generalHashTableWrapper;
    }

    public TGeneralHashTableWrapper getGeneralHashTableWrapper() {
        return generalHashTableWrapper;
    }
}
