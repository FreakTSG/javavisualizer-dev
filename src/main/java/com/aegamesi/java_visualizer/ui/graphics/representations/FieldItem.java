package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;

public abstract class FieldItem implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Class<?> clazz;
    protected String prefix;
    protected transient Field field;

    public FieldItem(Class<?> clazz, String prefix, Field field) {
        this.clazz = clazz;
        this.prefix = prefix;
        this.field = field;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field fieldComponent) {
        this.field = fieldComponent;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(field.getName());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        String fieldName = (String) in.readObject();
        field = Utils.getFieldOfClass(clazz, fieldName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FieldItem fieldItem = (FieldItem) o;

        if (!Objects.equals(prefix, fieldItem.prefix)) {
            return false;
        }
        return Objects.equals(field, fieldItem.field);
    }

    @Override
    public int hashCode() {
        int result = prefix != null ? prefix.hashCode() : 0;
        result = 31 * result + (field != null ? field.hashCode() : 0);
        return result;
    }
}
