package wrapper_classes;

import java.io.Serializable;

public abstract class Wrapper implements WrapperInterface, Serializable {
    private static final long serialVersionUID = 1L;

    protected long id;
    private static long nextId = 1;
    protected String valueTypeName;

    public Wrapper(String valueTypeName) {
        this.valueTypeName = valueTypeName;
        id = nextId++;
    }

    public static void setNextId(long nextId) {
        Wrapper.nextId = nextId;
    }

    public long getId() {
        return id;
    }

    public String getValueTypeName() {
        return valueTypeName;
    }

    public void setValueTypeName(String valueTypeName) {
        this.valueTypeName = valueTypeName;
    }
}
