package utils;

import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.Nullable;
import ui.Constants;
import ui.IDSToolWindow;
import ui.graphics.representations.FieldItem;
import ui.graphics.representations.hash_tables.HashTableRepresentation;
import ui.graphics.representations.linked_lists.*;
import ui.operators.Operator;
import wrapper_classes.VariableWrapper;
import wrapper_classes.Wrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class Utils {
    private static HashMap<String, Class<?>> representationClassByClassSimpleName = new HashMap<>();
    private static HashMap<String, Object> defaultValueByClassSimpleName = new HashMap<>();
    private static HashMap<String, String> wrapperClassNameByPrimitiveName = new HashMap<>();
//    private static HashMap<Class<?>, Class<?>> classByPrimitiveWrapperClass = new HashMap<>();


    static {
        representationClassByClassSimpleName.put("UnsortedCircularSimpleLinkedListWithBaseWrapper", UnsortedCircularSimpleLinkedListWithBaseRepresentation.class);
        representationClassByClassSimpleName.put("UnsortedCircularDoubleLinkedListWithBaseWrapper", UnsortedCircularDoubleLinkedListWithBaseRepresentation.class);

        representationClassByClassSimpleName.put("SortedCircularSimpleLinkedListWithBaseMaxOrderWrapper", SortedCircularSimpleLinkedListWithBaseMaxOrderRepresentation.class);
        representationClassByClassSimpleName.put("SortedCircularDoubleLinkedListWithBaseMaxOrderWrapper", SortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation.class);
        representationClassByClassSimpleName.put("DistinctSortedCircularDoubleLinkedListWithBaseMaxOrderWrapper", DistinctSortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation.class);

        representationClassByClassSimpleName.put("HashTableWithQuadracticIncrementWrapper", HashTableRepresentation.class);

        defaultValueByClassSimpleName.put(Boolean.class.getSimpleName(), false);
        defaultValueByClassSimpleName.put("boolean", false);
        defaultValueByClassSimpleName.put(Byte.class.getSimpleName(), (byte) 0);
        defaultValueByClassSimpleName.put("byte", (byte) 0);
        defaultValueByClassSimpleName.put(Character.class.getSimpleName(), '\u0000');
        defaultValueByClassSimpleName.put("char", '\u0000');
        defaultValueByClassSimpleName.put(Short.class.getSimpleName(), (short) 0);
        defaultValueByClassSimpleName.put("short", (short) 0);
        defaultValueByClassSimpleName.put(Integer.class.getSimpleName(), 0);
        defaultValueByClassSimpleName.put("int", 0);
        defaultValueByClassSimpleName.put(Long.class.getSimpleName(), 0L);
        defaultValueByClassSimpleName.put("long", 0L);
        defaultValueByClassSimpleName.put(Float.class.getSimpleName(), 0.0f);
        defaultValueByClassSimpleName.put("float", 0.0f);
        defaultValueByClassSimpleName.put(Double.class.getSimpleName(), 0.0d);
        defaultValueByClassSimpleName.put("double", 0.0d);
        defaultValueByClassSimpleName.put(String.class.getSimpleName(), "");

        wrapperClassNameByPrimitiveName.put("boolean", "Boolean");
        wrapperClassNameByPrimitiveName.put("byte", "Byte");
        wrapperClassNameByPrimitiveName.put("char", "Character");
        wrapperClassNameByPrimitiveName.put("short", "Short");
        wrapperClassNameByPrimitiveName.put("int", "Integer");
        wrapperClassNameByPrimitiveName.put("long", "Long");
        wrapperClassNameByPrimitiveName.put("float", "Float");
        wrapperClassNameByPrimitiveName.put("double", "Double");

//        classByPrimitiveWrapperClass.put(BooleanWrapper.class, Boolean.class);
//        classByPrimitiveWrapperClass.put(ByteWrapper.class, Byte.class);
//        classByPrimitiveWrapperClass.put(CharacterWrapper.class, Character.class);
//        classByPrimitiveWrapperClass.put(ShortWrapper.class, Short.class);
//        classByPrimitiveWrapperClass.put(IntegerWrapper.class, Integer.class);
//        classByPrimitiveWrapperClass.put(LongWrapper.class, Long.class);
//        classByPrimitiveWrapperClass.put(FloatWrapper.class, Float.class);
//        classByPrimitiveWrapperClass.put(DoubleWrapper.class, Double.class);
//        classByPrimitiveWrapperClass.put(StringWrapper.class, String.class);
    }

//    public static Class<?> getClass(Class<?> primitiveWrapperClass) {
//        return PrimitiveWrapper.class.isAssignableFrom(primitiveWrapperClass) ? classByPrimitiveWrapperClass.get(primitiveWrapperClass) : primitiveWrapperClass;
//    }

    public static String toUTF8(String texto) {
        return new String(texto.getBytes(), StandardCharsets.UTF_8);
    }

    public static boolean isPrimitiveOrPrimitiveWrapperType(String typeName) {
        switch (typeName) {
            case "int":
            case "double":
            case "float":
            case "char":
            case "byte":
            case "long":
            case "short":
            case "boolean":
            case "Integer":
            case "Double":
            case "Float":
            case "Character":
            case "Byte":
            case "Long":
            case "Short":
            case "Boolean":
                return true;
            default:
                return false;
        }
    }

    public static String getWrapperClassName(String primitiveName) {
        return wrapperClassNameByPrimitiveName.get(primitiveName);
    }

    public static boolean isPrimitiveOrPrimitiveWrapperOrStringType(Field field) {
        return isPrimitiveOrPrimitiveWrapperOrStringType(field.getType().getSimpleName());
    }

    public static boolean isPrimitiveOrPrimitiveWrapperOrStringType(String classSimpleName) {
        return Utils.isPrimitiveOrPrimitiveWrapperType(classSimpleName) || classSimpleName.equals("String");
    }


    public static boolean isACompactReferenceableType(String type) {
        switch (type) {
            case "String":
//            case "Enum":
//            case "Date":
                return true;
            default:
                return false;
        }
    }


    public static Field getFieldOfClass(Class ownerClass, String fieldName) {
        Field field = null;
        for (Field f : getAllFields(ownerClass)) {
            if (f.getName().equals(fieldName)) {
                field = f;
                break;
            }
        }
        field.setAccessible(true);
        return field;
    }

    public static Field getField(Object owner, String fieldName) {
        return getFieldOfClass(owner.getClass(), fieldName);
    }

    public static Object getFieldValue(Object owner, String fieldName) {
        try {
            return getField(owner, fieldName).get(owner);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getFieldValue(Object owner, Field field) {
        try {
            field.setAccessible(true);
            return field.get(owner);
        } catch (IllegalAccessException e) {
//            e.printStackTrace();
            return null;
        }
    }

    public static Object getConvertedValue(Class clazz, String value) {
        switch (clazz.getSimpleName()) {
            case "int":
            case "Integer":
                return Integer.valueOf(value);
            case "double":
            case "Double":
                return Double.valueOf(value);
            case "float":
            case "Float":
                return Float.valueOf(value);
            case "char":
            case "Character":
                if (value.length() == 1) {
                    return Character.valueOf(value.charAt(0));
                }
                if (value.length() == 2 && value.charAt(0) == 0) {
                    return Character.valueOf(value.charAt(1));
                }
                throw new IllegalArgumentException();
            case "byte":
            case "Byte":
                return Byte.valueOf(value);
            case "long":
            case "Long":
                return Long.valueOf(value);
            case "short":
            case "Short":
                return Short.valueOf(value);
            case "boolean":
            case "Boolean":
                return Boolean.valueOf(value);
        }
        return value;
    }

    public static Class<?> getWrapperClassOfPrimitiveType(Class<?> clazz) {
        if (!clazz.isPrimitive()) {
            return clazz;
        }
        if (clazz == Boolean.TYPE) {
            return Boolean.class;
        }
        if (clazz == Byte.TYPE) {
            return Byte.class;
        }
        if (clazz == Character.TYPE) {
            return Character.class;
        }
        if (clazz == Double.TYPE) {
            return Double.class;
        }
        if (clazz == Float.TYPE) {
            return Float.class;
        }
        if (clazz == Integer.TYPE) {
            return Integer.class;
        }
        if (clazz == Long.TYPE) {
            return Long.class;
        }
        if (clazz == Short.TYPE) {
            return Short.class;
        }
        return clazz == Void.TYPE ? Void.class : clazz;

    }

    public static void setFieldValue(Object owner, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(owner, value instanceof String ? getConvertedValue(field.getType(), (String) value) : value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Valor de atributo inválido.");
        } catch (Exception e) {

        }
    }

    public static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> classe = type; classe != null; classe = classe.getSuperclass()) {
            fields.addAll(Arrays.asList(classe.getDeclaredFields()));
        }
        return fields;
    }

    public static Point getProjectionPoint(int x1, int y1, int x2, int y2, int x3, int y3) {
        int diffX = x2 - x1;
        if (diffX == 0) {
            diffX = 1;
        }
        float m = (y2 - y1) / diffX;
        float b = y1 - m * x1;

        float x = (x3 + m * (y3 - b)) / (m * m + 1);
        float y = m * x + b;
        return new Point((int) x, (int) y);
    }

    public static Point getPointRotatedOverAnother(int x1, int y1, int x2, int y2, double angle) {
        /**
         *     (x1, y1)----------------(x2,y2)
         *                               |
         *                     angle     |
         *                               |
         *                          (x1, y1) desired
         */
        AffineTransform affineTransform = new AffineTransform();

        affineTransform.translate(x2, y2);
        affineTransform.rotate(angle);
        affineTransform.translate(-x2, -y2);

        Point p = new Point(x1, y1);
        Point pT = new Point();
        affineTransform.transform(p, pT);

        return new Point(pT.x, pT.y);
    }

    public static Point getPointRotatedOverAnotherAndDownScaling(int x1, int y1, int x2, int y2, double angle) {
        /**
         *     (x1, y1)----------------(x2,y2)
         *                               |
         *                     angle     |
         *                               |
         *                          (x1, y1) desired
         */

        final double distance = Point.distance(x1, y1, x2, y2);
        AffineTransform affineTransform = new AffineTransform();

        affineTransform.translate(x2, y2);
        affineTransform.translate(-distance / 2, 0);
        affineTransform.rotate(angle);
        affineTransform.translate(-x2, -y2);

        Point p = new Point(x1, y1);
        Point pT = new Point();
        affineTransform.transform(p, pT);

        return new Point(pT.x, pT.y);
    }


    public static Class<?> findSubClassParameterType(Object instance, Class<?> classOfInterest, int parameterIndex) {
        Map<Type, Type> typeMap = new HashMap<Type, Type>();
        Class<?> instanceClass = instance.getClass();
        while (classOfInterest != instanceClass.getSuperclass()) {
            extractTypeArguments(typeMap, instanceClass);
            instanceClass = instanceClass.getSuperclass();
            if (instanceClass == null) {
                throw new IllegalArgumentException();
            }
        }

        ParameterizedType parameterizedType = (ParameterizedType) instanceClass.getGenericSuperclass();
        Type actualType = parameterizedType.getActualTypeArguments()[parameterIndex];
        if (typeMap.containsKey(actualType)) {
            actualType = typeMap.get(actualType);
        }

        if (actualType instanceof Class) {
            return (Class<?>) actualType;
        } else if (actualType instanceof TypeVariable) {
            return browseNestedTypes(instance, (TypeVariable<?>) actualType);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static Class<?> browseNestedTypes(Object instance, TypeVariable<?> actualType) {
        Class<?> instanceClass = instance.getClass();
        List<Class<?>> nestedOuterTypes = new LinkedList<Class<?>>();
        for (
                Class<?> enclosingClass = instanceClass.getEnclosingClass();
                enclosingClass != null;
                enclosingClass = enclosingClass.getEnclosingClass()) {
            try {
                Field this$0 = instanceClass.getDeclaredField("this$0");
                Object outerInstance = this$0.get(instance);
                Class<?> outerClass = outerInstance.getClass();
                nestedOuterTypes.add(outerClass);
                Map<Type, Type> outerTypeMap = new HashMap<Type, Type>();
                extractTypeArguments(outerTypeMap, outerClass);
                for (Map.Entry<Type, Type> entry : outerTypeMap.entrySet()) {
                    if (!(entry.getKey() instanceof TypeVariable)) {
                        continue;
                    }
                    TypeVariable<?> foundType = (TypeVariable<?>) entry.getKey();
                    if (foundType.getName().equals(actualType.getName())
                            && isInnerClass(foundType.getGenericDeclaration(), actualType.getGenericDeclaration())) {
                        if (entry.getValue() instanceof Class) {
                            return (Class<?>) entry.getValue();
                        }
                        actualType = (TypeVariable<?>) entry.getValue();
                    }
                }
            } catch (NoSuchFieldException e) { /* this should never happen */ } catch (
                    IllegalAccessException e) { /* this might happen */}

        }
        throw new IllegalArgumentException();
    }

    private static boolean isInnerClass(GenericDeclaration outerDeclaration, GenericDeclaration innerDeclaration) {
        if (!(outerDeclaration instanceof Class) || !(innerDeclaration instanceof Class)) {
            throw new IllegalArgumentException();
        }
        Class<?> outerClass = (Class<?>) outerDeclaration;
        Class<?> innerClass = (Class<?>) innerDeclaration;
        while ((innerClass = innerClass.getEnclosingClass()) != null) {
            if (innerClass == outerClass) {
                return true;
            }
        }
        return false;
    }

    private static void extractTypeArguments(Map<Type, Type> typeMap, Class<?> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType)) {
            return;
        }

        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] typeParameter = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
        Type[] actualTypeArgument = parameterizedType.getActualTypeArguments();
        for (int i = 0; i < typeParameter.length; i++) {
            if (typeMap.containsKey(actualTypeArgument[i])) {
                actualTypeArgument[i] = typeMap.get(actualTypeArgument[i]);
            }
            typeMap.put(typeParameter[i], actualTypeArgument[i]);
        }
    }


    public static Object getDefaultValue(Class<?> clazz) {
        return defaultValueByClassSimpleName.get(clazz.getSimpleName());
    }

    public static Class<?> getRepresentationClass(Class<?> clazz) {
        return representationClassByClassSimpleName.get(clazz.getSimpleName());
    }

    public static int compare(Object o1, Object o2) {
        if (o1.getClass().isEnum()) {
            return ((Enum) o1).compareTo((Enum) o2);
        }
        switch (o1.getClass().getSimpleName()) {
            case "Integer":
                return ((Integer) o1).compareTo((Integer) o2);
            case "Long":
                return ((Long) o1).compareTo((Long) o2);
            case "Double":
                return ((Double) o1).compareTo((Double) o2);
            case "Float":
                return ((Float) o1).compareTo((Float) o2);
            case "Character":
                return ((Character) o1).compareTo((Character) o2);
            case "Byte":
                return ((Byte) o1).compareTo((Byte) o2);
            case "Short":
                return ((Short) o1).compareTo((Short) o2);
            case "Boolean":
                return ((Boolean) o1).compareTo((Boolean) o2);
            case "String":
                return ((String) o1).compareTo((String) o2);
//            case "byte[]": //because of Strings comparison that have byte[] as field
//                return new String(((byte[]) o1)).compareTo(new String((byte[]) o2));
            default:
                return 0;
        }
    }

    public static <T extends FieldItem> void createFieldItem(Supplier<T> supplier, ArrayList<T> fieldItems, String prefix, Class owner, int level, int maxLevel) {
        //because of special case of String class
        if (isPrimitiveOrPrimitiveWrapperOrStringType(owner.getSimpleName())) {
            final T t = supplier.get();
            t.setPrefix(prefix);
            t.setField(getFieldOfClass(owner, "value"));
            t.setClazz(owner);
            fieldItems.add(t);
            return;
        }
        for (Field field : Utils.getAllFields(owner)) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (Utils.isPrimitiveOrPrimitiveWrapperOrStringType(field) || field.getType().isEnum()) {
                final T t = supplier.get();
                t.setPrefix(prefix);
                t.setField(field);
                t.setClazz(owner);
                fieldItems.add(t);
            } else if (level < maxLevel) {
                createFieldItem(supplier, fieldItems, prefix + field.getName() + ".", field.getType(), level + 1, maxLevel);
            }
        }
    }

//    public static String getCompleteTypeNameWithoutPackages(String completeTypeName) {
//        final String[] packages = completeTypeName.split("<");
//        StringBuilder completeTypeNameWithoutPackages = new StringBuilder();
//        for (int i = 0; i < packages.length - 1; i++) {
//            final String[] split = packages[i].split("\\.");
//            completeTypeNameWithoutPackages.append(split[split.length - 1]).append('<');
//        }
//        final String[] split = packages[packages.length - 1].split("\\.");
//        completeTypeNameWithoutPackages.append(split[split.length - 1]);
//        return completeTypeNameWithoutPackages.toString();
//    }

    public static Object createObject(Class clazz, boolean referencesAlwaysNull) {
        if (clazz.isEnum()) {
            return clazz.getEnumConstants()[0];
        }
        final Constructor[] constructors = clazz.getConstructors();
//        final Object newObject = createObject(constructors, 0);
//        return newObject != null || constructors.length <= 1 ? newObject : createObject(constructors, 1);
        Object newObject;
        int i = 0;
        do {
            newObject = createObject(constructors, i++, referencesAlwaysNull);
        } while (newObject == null && i < constructors.length);
        return newObject;
    }

    @Nullable
    private static Object createObject(Constructor[] constructors, int constructorIndex, boolean referencesAlwaysNull) {
        final Constructor constructor = constructors[constructorIndex];
        final Class[] parameterTypes = constructor.getParameterTypes();
        Object[] parametersValue = new Object[parameterTypes.length];
        int index = 0;
        for (Class parameterType : parameterTypes) {
            parametersValue[index++] = parameterType.isEnum() ? parameterType.getEnumConstants()[0] : Utils.getDefaultValue(parameterType);
        }
        Object newInstance;
        try {
            newInstance = constructor.newInstance(parametersValue);

            //atribuir os "nossos" valores por omissão aos atributos null (Float, Double, Byte, Enum, etc)
            for (Field field : Utils.getAllFields(newInstance.getClass())) {
                final Object fieldValue = Utils.getFieldValue(newInstance, field);
                final String fieldTypeSimpleName = field.getType().getSimpleName();
                if (Utils.isPrimitiveOrPrimitiveWrapperType(fieldTypeSimpleName) || Utils.isACompactReferenceableType(fieldTypeSimpleName) || field.getType().isEnum()) {
                    if (fieldValue == null) {
                        Utils.setFieldValue(newInstance, field, field.getType().isEnum() ? field.getType().getEnumConstants()[0] : Utils.getDefaultValue(field.getType()));
                    }
                }
                //TESTE WARNING TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                else if (referencesAlwaysNull) {
                    Utils.setFieldValue(newInstance, field, null);
                }
            }
            return newInstance;
        } catch (Exception e) {
            return null;
        }
    }

    public static void addKeyBinding(JComponent component, int keyCode, int modifiers, String id, ActionListener actionListener) {
        component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keyCode, modifiers, false), id);
        component.getActionMap().put(id, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionListener.actionPerformed(e);
            }
        });
    }

    public static String getClassNameWithGenericTypes(Object object) {
        return object instanceof Wrapper ? ((Wrapper) object).getValueTypeName() : object.getClass().getName();
    }

    public static void createAndCompileClass(String classDir, String className, String classSourceCode) {
        try {
            File sourceFile = new File(classDir + "/" + className + ".java");
            sourceFile.getParentFile().mkdirs();
            new FileWriter(sourceFile).append(classSourceCode).close();

//            Operator.getCanvas().getIDSToolWindow().compileSingleClassOfLocalProject(sourceFile);
//            Operator.getCanvas().getIDSToolWindow().updateEmptyAreaButtonBar();

            Operator.getCanvas().getIDSToolWindow().processProjectEntities(false);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getClassSimpleName(String completeClassName) {
//        final String[] withPackages = completeClassName.split("<");
//        for (int i = 0; i < withPackages.length; i++) {
//            int lastDot = withPackages[i].lastIndexOf('.');
//            if (lastDot >= 0) {
//                withPackages[i] = withPackages[i].substring(lastDot + 1);
//            }
//        }
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < withPackages.length; i++) {
//            sb.append(withPackages[i]);
//            if (i < withPackages.length - 1) {
//                sb.append("<");
//            }
//        }
//        return sb.toString();

        return Pattern.compile("([a-zA-Z0-9]+\\.)").matcher(completeClassName).replaceAll("");
    }

    public static String getValueTypeNameWithoutParametrizedTypes(String valueTypeName) {
        //Comparacao<PND>  => Comparacao<>
        return Pattern.compile("(<.*>)").matcher(valueTypeName).replaceAll("<>");
    }

    public static boolean isValidIdentifier(String id) {
//        Pattern p = Pattern.compile("\"^([a-z][a-zA-Z\\\\d]*)$\"");//. represents single character.
//        return Pattern.compile("(?:\\b[_a-z]|\\B\\$)[_$a-zA-Z0-9]*+").matcher(id).matches();
        return Pattern.compile("(?:\\b[a-z]|\\B\\$)[a-zA-Z0-9]*+").matcher(id).matches();

//        return Pattern.compile("\"^([a-z][a-zA-Z\\\\d]*)$\"").matcher(id).matches();
//return true;
    }

    public static boolean isValidTypeIdentifier(String id) {
        return Pattern.compile("(?:\\b[A-Z]|\\B\\$)[a-zA-Z0-9]*+").matcher(id).matches();
    }

    public static boolean isDuplicateTypeIdentifierInPackage(String id) {
        final ArrayList<PsiClass> psiClasses = Operator.getCanvas().getIDSToolWindow().getPsiClasses(Operator.getCanvas().getModelPackage());
        for (PsiClass psiClass : psiClasses) {
            if (psiClass.getName().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public static String getCreationCode(Object owner) {
        IDSToolWindow idsToolWindow = Operator.getCanvas().getIDSToolWindow();
        idsToolWindow.addImportToEditorPaneSourceCode(owner.getClass().getName());
        final StringBuilder sb = new StringBuilder("new ");
        sb.append(owner.getClass().getSimpleName()).append("(");
        for (Field field : Utils.getAllFields(owner.getClass())) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue; //não usa o costrutor para inicializar estáticos
            }
            final Object fieldValue = Utils.getFieldValue(owner, field);
            sb.append(getCode(fieldValue, idsToolWindow));
            sb.append(", ");
        }
        final int length = sb.length();
        sb.replace(length - 2, length - 1, ")");
        return sb.toString();
    }

    public static String getCode(Object fieldValue, IDSToolWindow idsToolWindow) {
        if (fieldValue == null) {
//            JOptionPane.showMessageDialog(null, "fieldValue = null!!! em UTILS => String getCode(Object fieldValue, IDSToolWindow idsToolWindow)");
            return String.valueOf(fieldValue);
        }
        Class<?> fieldType = fieldValue.getClass();
        if (!isPrimitiveOrPrimitiveWrapperOrStringType(fieldType.getSimpleName()) && !fieldType.isEnum()) {
            return getCreationCode(fieldValue);
        }
        if (fieldType.isEnum()) {
            idsToolWindow.addImportToEditorPaneSourceCode(fieldType.getName());
            return fieldType.getSimpleName() + "." + fieldValue;
        }
        if (fieldType.getSimpleName().equals(String.class.getSimpleName())) {
            return "\"" + fieldValue + "\"";
        }
        if (fieldType.getSimpleName().equals(Character.class.getSimpleName()) || fieldType.getSimpleName().equals("char")) {
            return "\'" + fieldValue + "\'";
        }
        return String.valueOf(fieldValue);
    }


    public static Class<?> getClassForName(String valueTypeName) {
        URLClassLoader urlClassLoader = Operator.getCanvas().getIDSToolWindow().getUrlClassLoader();
        try {
            return Class.forName(valueTypeName, true, urlClassLoader);
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
        }
        return null;
    }

    public static boolean isAssignable(String sourceTypeName, String targetTypeName) {
        //sourceTypeName from existing class (FOR SURE)
        return isAssignable(Utils.getClassForName(sourceTypeName), targetTypeName);
    }

    public static boolean isAssignable(Class<?> source, String targetTypeName) {
        Class<?> targetClass = Utils.getClassForName(targetTypeName);
        return targetClass != null && source.isAssignableFrom(targetClass);
    }

    public static Object getOwnerFromFieldItem(Object owner, FieldItem fieldItem) {
        final String prefix = fieldItem.getPrefix();
        final String[] sortedFieldItemPrefixes = prefix.isEmpty() ? new String[0] : prefix.split("\\.");

        for (String sortedFieldItemPrefix : sortedFieldItemPrefixes) {
            owner = Utils.getFieldValue(owner, sortedFieldItemPrefix);
        }
        return owner;
    }

    public static String getGetterCall(String fieldName) {
        StringBuilder sb = new StringBuilder();
        final String c = String.valueOf(fieldName.charAt(0));
        fieldName = fieldName.replaceFirst(c, c.toUpperCase());
        sb.append(".get").append(fieldName).append("()");
        return sb.toString();
    }

    public static String getGetterCall(Field field) {
        if (field.getName().equals("ordinal")) {
            return ".ordinal()";
        }
        StringBuilder sb = new StringBuilder();
        String fieldName = field.getName();
        final String c = String.valueOf(fieldName.charAt(0));
        fieldName = fieldName.replaceFirst(c, c.toUpperCase());
        sb.append(field.getType().isAssignableFrom(boolean.class) || field.getType().isAssignableFrom(Boolean.class) ? ".is" : ".get").append(fieldName).append("()");
        return sb.toString();
    }

    public static String getSetterCall(String fieldName, String value) {
        StringBuilder sb = new StringBuilder();
        final String c = String.valueOf(fieldName.charAt(0));
        fieldName = fieldName.replaceFirst(c, c.toUpperCase());
        sb.append(".set").append(fieldName).append("(" + value + ")");
        return sb.toString();
    }

    public static String getUndoRedoCreationObjectMessage(VariableWrapper variableWrapper, String typeName) {
//        return Constants.NEW + "(" + variableWrapper.getName() + ") " + typeName;
        return Constants.NEW + typeName + " " + variableWrapper.getName();
    }

    public static String getUndoRedoSetReferenceMessage(String code) {
        return Constants.SET + code;
    }

    public static String getUndoRedoUnsetMessage(String code) {
        return Constants.UNSET + code;
    }

    public static String getUndoRedoCreationVariableMessage(String name) {
        return Constants.DECLARE + name;
    }

    public static String getUndoRedoAddRecordComponentMessage(String componentName, String recordName) {
        return Constants.ADD + componentName + Constants.TO + recordName;
    }

    public static String getUndoRedoDeleteRecordComponentMessage(String componentName, String recordName) {
        return Constants.REMOVE + componentName + Constants.FROM + recordName;
    }

    public static String getUndoRedoCreationRecordMessage(String recordName) {
        return Constants.CREATE + "classe " + recordName;
    }

    public static String getUndoRedoDeleteRecordMessage(String recordName) {
        return Constants.DELETE + "classe " + recordName;
    }

    public static String getUndoRedoGenerateRecordMessage(String name) {
        return Constants.GENERATE + "classe " + name;
    }

    public static String getUndoRedoAddElementMessage(String code) {
        return Constants.ADD + " " + code;
    }

    public static String getUndoRedoRemoveElementMessage(String code) {
        return Constants.REMOVE + " " + code;
    }

    public static int getSize(Enumeration<?> enumeration) {
        int size = 0;
        while (enumeration.hasMoreElements()) {
            enumeration.nextElement();
            size++;
        }
        return size;
    }

    public static String getCode(Object key) {
        String keyExpression = "";
        if (key instanceof String) {
            keyExpression = "\"" + key + "\"";
        } else if (key instanceof Character) {
            keyExpression = "'\\u" + Integer.toHexString(((Character) key).charValue() | 0x10000).substring(1) + "'";
//        } else if (key.getClass().getSimpleName().equals("byte[]")) {
//            keyExpression = "\"" + new String((byte[])key) + "\"";
        } else if (key instanceof Enum) {
            keyExpression = key.getClass().getSimpleName() + "." + key;
        } else {
            keyExpression = key.toString();
        }
        return keyExpression;
    }

    public static void log(String s) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(Operator.getCanvas().getIDSToolWindow().getProject().getBasePath() + "/log.txt", true))) {
            bufferedWriter.write(s + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
