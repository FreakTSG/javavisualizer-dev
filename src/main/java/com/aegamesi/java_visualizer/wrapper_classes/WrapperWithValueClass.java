package wrapper_classes;

import utils.Utils;

public abstract class WrapperWithValueClass<T> extends Wrapper {
    private static final long serialVersionUID = 1L;

    protected Class<T> valueClass;

    public WrapperWithValueClass(Class<T> valueClass, String valueTypeName) {
        super(valueTypeName);
        this.valueClass = valueClass;
    }

    public Class<T> getValueClass() {
        return valueClass;
    }

    public boolean isValid(WrapperWithValueClass wrapper) {
        return isValid(wrapper.valueTypeName);
    }

    public boolean isValid(String typeName) {
        if (valueTypeName.contains("<") || valueTypeName.contains("[")) {
            return valueTypeName.equals(typeName);
        }

        return Utils.isAssignable(valueTypeName, typeName);
    }
}
