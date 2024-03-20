package wrapper_classes;

public abstract class DataStructureWithComponentClassWrapper<T, TComponent extends WrapperWithValue>
        extends DataStructureWrapper<T> {
    private static final long serialVersionUID = 1L;


    protected Class<TComponent> componentClass;

    public DataStructureWithComponentClassWrapper(Class<T> valueClass, String valueTypeName, Class<TComponent> componentClass, String componentTypeName) {
        super(valueClass, valueTypeName, componentTypeName);
        this.componentClass = componentClass;
        String name = componentClass.getName();
        TComponent a;

//        value = new LinkedList<>();
    }


//    public void add(TComponent element) {
//        value.add(element);
//    }


//    public boolean isComponentValid(String typeName) {
//        String componentTypeName = getComponentTypeName();
//        isComponentValid()
//        if (componentTypeName valueTypeName.contains("<") || valueTypeName.contains("[")) {
//            return valueTypeName.equals(typeName);
//        }
//
//        return Utils.getClassForName(valueTypeName).isAssignableFrom(Utils.getClassForName(typeName));
//    }
}
