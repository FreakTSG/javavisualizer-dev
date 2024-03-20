package wrapper_classes;

import ui.Canvas;
import ui.ClassCreation;
import ui.graphics.representations.UnsortedFieldItem;
import ui.graphics.representations.hash_tables.HashTableKeyRepresentation;
import ui.operators.Operator;
import utils.Utils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class HashTableKeyWrapper<K> extends GeneralHashTableKeyWrapper<K, HashTableKeyRepresentation> {
    private static final long serialVersionUID = 1L;


    //                          chave              chave                          chave
    public HashTableKeyWrapper(Class<K> valueClass, String valueTypeName, HashTableKeyRepresentation hashTableKeyRepresentation) {
        super(valueClass, valueTypeName, hashTableKeyRepresentation);
    }

    public Class getKeyClassOrNull() {
        final ArrayList<UnsortedFieldItem> inUnsortedFieldItems = generalHashTableKeyRepresentation.getInUnsortedFieldItems();

        if (inUnsortedFieldItems.size() == 0) {
            return null;
        }

        if (inUnsortedFieldItems.size() == 1) {
            return Utils.getWrapperClassOfPrimitiveType(inUnsortedFieldItems.get(0).getField().getType());
        }
        String prefix = inUnsortedFieldItems.get(0).getPrefix();
        boolean allWithSamePrefixIn = true;
        for (UnsortedFieldItem inSortedFieldItem : inUnsortedFieldItems) {
            if (!inSortedFieldItem.getPrefix().equals(prefix)) {
                allWithSamePrefixIn = false;
                break;
            }
        }
        boolean noneWithPrefixOut = true;
        final ArrayList<UnsortedFieldItem> outUnsortedFieldItems = generalHashTableKeyRepresentation.getOutUnsortedFieldItems();
        for (UnsortedFieldItem outUnsortedFieldItem : outUnsortedFieldItems) {
            if (outUnsortedFieldItem.getPrefix().equals(prefix)) {
                noneWithPrefixOut = false;
                break;
            }
        }
        //can use an existing class (prefix)
        return allWithSamePrefixIn && noneWithPrefixOut ? inUnsortedFieldItems.get(0).getClazz() : null;
    }

    public Class getKeyClass(String keyClassName, DefaultListModel<UnsortedFieldItem> targetListModel) {
        Class keyClass = getKeyClassOrNull();
        if (keyClass != null) {
            return keyClass;
        }
        //criar e compilar classe nova com os campos do in
        final Canvas canvas = Operator.getCanvas();
        ClassCreation keyClassCreation = new ClassCreation(canvas.getIDSToolWindow().getProject().getBasePath() + "/src/" + canvas.getModelPackage().replace(".", "/") + "/" + keyClassName, false, true);

        //fields
        Enumeration<UnsortedFieldItem> elements = targetListModel.elements();
        while (elements.hasMoreElements()) {
            UnsortedFieldItem unsortedFieldItem = elements.nextElement();
            keyClassCreation.addAttribute(unsortedFieldItem.getField().getType().getName(), unsortedFieldItem.getField().getName());
        }

        canvas.getIDSToolWindow().deleteGeneratedClass();

        keyClassCreation.writeGeneratedClass();
        canvas.getIDSToolWindow().processProjectEntities(false);

        return Utils.getClassForName(Operator.getCanvas().getModelPackage() + "." + keyClassName);
    }
}
