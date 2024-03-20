package wrapper_classes;

public class HashTableEntryTableWrapper extends DataStructureWrapper<HashTableEntryWrapper[]> {
    private static final long serialVersionUID = 1L;


    public HashTableEntryTableWrapper(String componentTypeName, int size) {
        super(HashTableEntryWrapper[].class, componentTypeName + "[]", componentTypeName);
        value = new HashTableEntryWrapper[size];
    }


    public void add(HashTableEntryWrapper element, int index) {
        value[index] = element;
    }

    public HashTableEntryWrapper get(int index) {
        return value[index];
    }

}
