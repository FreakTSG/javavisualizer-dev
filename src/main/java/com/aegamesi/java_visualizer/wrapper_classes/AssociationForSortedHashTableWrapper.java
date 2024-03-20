package wrapper_classes;

public class AssociationForSortedHashTableWrapper<T> extends GeneralAssociationWrapper<T> implements NonReferenceable {
    private static final long serialVersionUID = 1L;


    public AssociationForSortedHashTableWrapper(Class<T> valueClass, String valueTypeName, T value) {
        super(valueClass, valueTypeName, value);
    }
}
