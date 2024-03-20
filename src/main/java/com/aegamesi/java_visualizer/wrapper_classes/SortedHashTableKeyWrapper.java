package wrapper_classes;

import ui.Canvas;
import ui.ClassCreation;
import ui.graphics.representations.SortedFieldItem;
import ui.graphics.representations.hash_tables.SortedHashTableKeyRepresentation;
import ui.operators.Operator;
import utils.Utils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class SortedHashTableKeyWrapper<K> extends GeneralHashTableKeyWrapper<K, SortedHashTableKeyRepresentation> {
    private static final long serialVersionUID = 1L;


    //                                       chave              chave                          chave
    public SortedHashTableKeyWrapper(Class<K> valueClass, String valueTypeName, SortedHashTableKeyRepresentation sortedHashTableKeyRepresentation) {
        super(valueClass, valueTypeName, sortedHashTableKeyRepresentation);
    }


    public Class getKeyClassOrNull() {
        final ArrayList<SortedFieldItem> inSortedFieldItems = generalHashTableKeyRepresentation.getInSortedFieldItems();

        if (inSortedFieldItems.size() == 0) {
            return null;
        }

        if (inSortedFieldItems.size() == 1) {
            return Utils.getWrapperClassOfPrimitiveType(inSortedFieldItems.get(0).getField().getType());
        }
        String prefix = inSortedFieldItems.get(0).getPrefix();
        boolean allWithSamePrefixIn = true;
        for (SortedFieldItem inSortedFieldItem : inSortedFieldItems) {
            if (!inSortedFieldItem.getPrefix().equals(prefix)) {
                allWithSamePrefixIn = false;
                break;
            }
        }
        boolean noneWithPrefixOut = true;
        final ArrayList<SortedFieldItem> outSortedFieldItems = generalHashTableKeyRepresentation.getOutSortedFieldItems();
        for (SortedFieldItem outSortedFieldItem : outSortedFieldItems) {
            if (outSortedFieldItem.getPrefix().equals(prefix)) {
                noneWithPrefixOut = false;
                break;
            }
        }
        //can use an existing class (prefix)
        return allWithSamePrefixIn && noneWithPrefixOut ? inSortedFieldItems.get(0).getClazz() : null;
    }

    public Class getKeyClass(String keyClassName, DefaultListModel<SortedFieldItem> targetListModel) {
        Class keyClass = getKeyClassOrNull();
        if (keyClass != null) {
            return keyClass;
        }
        //criar e compilar classe nova com os campos do in
        final Canvas canvas = Operator.getCanvas();
        ClassCreation keyClassCreation = new ClassCreation(canvas.getIDSToolWindow().getProject().getBasePath() + "/src/" + canvas.getModelPackage().replace(".", "/") + "/" + keyClassName, false, true);

        //fields
        Enumeration<SortedFieldItem> elements = targetListModel.elements();
        while (elements.hasMoreElements()) {
            SortedFieldItem sortedFieldItem = elements.nextElement();
            keyClassCreation.addAttribute(sortedFieldItem.getField().getType().getName(), sortedFieldItem.getField().getName());
        }

        canvas.getIDSToolWindow().deleteGeneratedClass();

        keyClassCreation.writeGeneratedClass();
        canvas.getIDSToolWindow().processProjectEntities(false);

        return Utils.getClassForName(Operator.getCanvas().getModelPackage() + "." + keyClassName);


//        StringBuilder sb = new StringBuilder();
//        sb.append("package " + Operator.getCanvas().getModelPackage() + ";\n");
//        sb.append("import java.io.Serializable;\n");
//        sb.append("import java.util.Objects;\n");
//        sb.append("public class ").append(keyClassName).append(" implements Serializable {\n");
//        //fields
//        Enumeration<SortedFieldItem> elements = targetListModel.elements();
//        while (elements.hasMoreElements()) {
//            SortedFieldItem sortedFieldItem = elements.nextElement();
//            sb.append("\tprivate ").append(sortedFieldItem.getField().getType().getName()).append(" ").append(sortedFieldItem.getField().getName()).append(";\n");
////            sb.append(elements.nextElement().getField() + ";\n");
//        }
//        //construtor
//        sb.append("\tpublic ").append(keyClassName).append("(");
//        elements = targetListModel.elements();
//        while (elements.hasMoreElements()) {
//            SortedFieldItem sortedFieldItem = elements.nextElement();
//            sb.append(sortedFieldItem.getField().getType().getName()).append(" ").append(sortedFieldItem.getField().getName()).append(",");
//        }
//        sb.deleteCharAt(sb.length() - 1);
//        sb.append(") {\n");
//        elements = targetListModel.elements();
//        while (elements.hasMoreElements()) {
//            String fieldName = elements.nextElement().getField().getName();
//            sb.append("\t\tthis.").append(fieldName).append(" = ").append(fieldName).append(";\n");
//        }
//        sb.append("\t}\n");
//
//        sb.append("\t@Override\n");
//        sb.append("\tpublic boolean equals(Object o) {\n");
//        sb.append("\t\tif (this == o) return true;\n");
//        sb.append("\t\tif (o == null || getClass() != o.getClass()) return false;\n");
//        sb.append("\t\t").append(keyClassName).append(" obj = (").append(keyClassName).append(") o;\n");
//        sb.append("\t\treturn ");
//        elements = targetListModel.elements();
//        while (elements.hasMoreElements()) {
//            String fieldName = elements.nextElement().getField().getName();
//            sb.append("Objects.equals(").append(fieldName).append(", obj.").append(fieldName).append(") && ");
//        }
//        int length = sb.length();
//        sb.replace(length - 4, length, "");
//        sb.append(";\n");
//        sb.append("\t}\n");
//
//        sb.append("\n\t@Override\n");
//        sb.append("\tpublic int hashCode () {\n");
//        sb.append("\t\treturn Objects.hash(");
//        elements = targetListModel.elements();
//        while (elements.hasMoreElements()) {
//            String fieldName = elements.nextElement().getField().getName();
//            sb.append(fieldName).append(", ");
//        }
//        length = sb.length();
//        sb.replace(length - 2, length, "");
//        sb.append(");\n");
//        sb.append("\t}\n");
//
//        sb.append("\n\t@Override\n");
//        sb.append("\tpublic String toString() {\n");
//        elements = targetListModel.elements();
//        sb.append("\t\treturn \" (\" + ");
//        while (elements.hasMoreElements()) {
//            String fieldName = elements.nextElement().getField().getName();
//            sb.append(fieldName).append(" + \", \" + ");
//        }
//        length = sb.length();
//        sb.replace(length - 7, length, "");
//        sb.append("\")\";\n");
//        sb.append("\t}\n");
//
//
//        sb.append("}");
//        final Canvas canvas = Operator.getCanvas();
////        canvas.addOperation(Utils.getUndoRedoGenerateRecordMessage(valueTypeName));
//        canvas.getIDSToolWindow().deleteGeneratedClass();
//        Utils.createAndCompileClass(canvas.getIDSToolWindow().getProject().getBasePath() + "/src/" + Operator.getCanvas().getModelPackage().replace(".", "/"), keyClassName, sb.toString());
////        canvas.getIDSToolWindow().saveState();
////        canvas.getIDSToolWindow().loadState();

    }

}
