package wrapper_classes;

import utils.Utils;

import java.lang.reflect.Field;

public class ProjectEntityWrapper<T> extends ProjectEntityOrPrimitiveOrEnumWrapper<T> implements ManuallyAssignableReferenceContainer {
    private static final long serialVersionUID = 1L;


    public ProjectEntityWrapper(Class<T> valueClass, T value) {
        super(valueClass, value);
    }

    @Override
    public boolean isValid(String sourceTypeName, Field targetField) {
        final String targetFieldTypeName = targetField.getGenericType().toString();
        if (targetFieldTypeName.contains("<") || targetFieldTypeName.contains("[")) {
            return targetFieldTypeName.equals(sourceTypeName);
        }
        return Utils.isAssignable((Class) targetField.getGenericType(), sourceTypeName);
    }


}
