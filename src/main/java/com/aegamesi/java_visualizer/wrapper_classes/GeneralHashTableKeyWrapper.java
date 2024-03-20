package wrapper_classes;

import ui.graphics.representations.hash_tables.GeneralHashTableKeyRepresentation;

public abstract class GeneralHashTableKeyWrapper<K, TGeneralHashTableKeyRepresentation extends GeneralHashTableKeyRepresentation> extends WrapperWithValueClass<K> implements NonReferenceable {
    protected TGeneralHashTableKeyRepresentation generalHashTableKeyRepresentation;
    private static final long serialVersionUID = 1L;


    //                                          chave              chave                         chave
    public GeneralHashTableKeyWrapper(Class<K> valueClass, String valueTypeName, TGeneralHashTableKeyRepresentation generalHashTableKeyRepresentation) {
        super(valueClass, valueTypeName);
        this.generalHashTableKeyRepresentation = generalHashTableKeyRepresentation;
    }

    public TGeneralHashTableKeyRepresentation getGeneralHashTableKeyRepresentation() {
        return generalHashTableKeyRepresentation;
    }

    public void setGeneralHashTableKeyRepresentation(TGeneralHashTableKeyRepresentation generalHashTableKeyRepresentation) {
        this.generalHashTableKeyRepresentation = generalHashTableKeyRepresentation;
    }
}
