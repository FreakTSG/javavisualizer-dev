package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists;


import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ColecaoIteravelLinear;
import com.aegamesi.java_visualizer.ui.graphics.representations.BaseRepresentation;

public interface SortedListRepresentation <T extends ColecaoIteravelLinear> extends BaseRepresentation {
    T getOwner();

    /*SortedLinkedList<?, ?> getOwner();

    MyCanvas getCanvas();


    default String getCreationCode() {
        String valueTypeName = getOwner().getValueTypeName();
        getCanvas().getIDSToolWindow().addImportToEditorPaneSourceCode(valueTypeName);
        StringBuilder sb = new StringBuilder("new ");
        final String aComparatorVariableName = getCanvas().getFirstReferenceTo(((Wrapper) getOwner().getValue().getComparador()));
        sb.append(Utils.getClassSimpleName(Utils.getValueTypeNameWithoutParametrizedTypes(valueTypeName))).append("(").append(aComparatorVariableName).append(")");
        return sb.toString();
    }*/
}
