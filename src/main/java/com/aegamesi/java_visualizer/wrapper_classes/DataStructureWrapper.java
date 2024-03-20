package wrapper_classes;

import utils.Utils;

public abstract class DataStructureWrapper<T> extends ReferenceableWrapperWithValue<T> {
    private static final long serialVersionUID = 1L;


    private final String componentTypeName;

    public DataStructureWrapper(Class<T> valueClass, String valueTypeName, String componentTypeName) {
        super(valueClass, valueTypeName);
        this.componentTypeName = componentTypeName;
    }

    public String getComponentTypeName() {
        return componentTypeName;
    }

    public boolean isComponentValid(WrapperWithValueClass wrapper) {
        if (componentTypeName.contains("<") || componentTypeName.contains("[")) {
            return componentTypeName.equals(wrapper.getValueTypeName());
        }

        return Utils.isAssignable(componentTypeName, wrapper.getValueTypeName());
    }
}
