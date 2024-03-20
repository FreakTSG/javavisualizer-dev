package wrapper_classes;

import java.lang.reflect.Field;

public interface ManuallyAssignableReferenceContainer {

    boolean isValid(String typeName, Field field);
}
