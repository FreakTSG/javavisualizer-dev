package wrapper_classes;

public class RecordComponentWrapper<T extends WrapperWithValueClass> extends WrapperWithValueClass<T> implements NonReferenceable {
    private static final long serialVersionUID = 1L;


    private String name;

    public RecordComponentWrapper(Class<T> valueClass, String valueTypeName, String name) {
        super(valueClass, valueTypeName);
        this.name = name;
    }

//    public RecordComponentWrapper(RecordComponentWrapper recordComponentWrapper) {
//        this(recordComponentWrapper.clazz, recordComponentWrapper.genericTypeName, recordComponentWrapper.name);
//    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\tprivate ").append(valueTypeName).append(" ").append(name).append(";\n");
        return sb.toString();
    }

}
