package wrapper_classes;


import aed.Comparacao;
import ui.Canvas;
import ui.Constants;
import ui.MyButton;
import ui.Resources;
import ui.dialogs.ComparatorRepresentationDialog;
import ui.graphics.representations.ComparatorRepresentation;
import ui.graphics.representations.Representation;
import ui.graphics.representations.SortedFieldItem;
import ui.graphics.representations.VariableRepresentation;
import ui.operators.Operator;
import ui.operators.building.BuildingOperator;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

public class ComparatorWrapper<T> extends WrapperWithValueClass<T> implements Comparacao<T>, Referenceable {
    private static final long serialVersionUID = 1L;


    protected ComparatorRepresentation comparatorRepresentation;

    public ComparatorWrapper(Class<T> valueClass, String valueTypeName, ComparatorRepresentation comparatorRepresentation) {
        super(valueClass, valueTypeName);
        this.comparatorRepresentation = comparatorRepresentation;
    }

    public static JButton getButton() {
        JButton button = new MyButton();
        button.setToolTipText("Comparação");
        button.setIcon(new ImageIcon(Resources.INSTANCE.comparatorMenuIcon));
        button.addActionListener(e -> {
            final Representation sourceRepresentation = BuildingOperator.getSourceRepresentation();
//            final Class sourceRepresentationType = sourceRepresentation.getOwnerTypeName();

            final String sourceRepresentationOwnerTypeName = sourceRepresentation.getOwnerTypeName();
            final Point comparatorRepresentationPosition = new Point(sourceRepresentation.getPosition());
            comparatorRepresentationPosition.translate(100, 0);
            final Canvas canvas = Operator.getCanvas();

            final Point variableRepresentationPosition = new Point(comparatorRepresentationPosition);
            variableRepresentationPosition.translate(-50, -50);
            String comparatorTypeName = Constants.COMPARATOR_CLASSNAME + "<" + Utils.getClassSimpleName(sourceRepresentationOwnerTypeName) + ">";
            final VariableRepresentation variableRepresentation = new VariableRepresentation(variableRepresentationPosition, new VariableWrapper(ComparatorWrapper.class, comparatorTypeName, null), canvas);

            WrapperInterface sourceRepresentationOwner = sourceRepresentation.getOwner();
            ComparatorRepresentation<?> comparatorRepresentation = new ComparatorRepresentation(comparatorRepresentationPosition,
                    new ComparatorWrapper(((WrapperWithValue) sourceRepresentationOwner).getValue().getClass(), comparatorTypeName, null), canvas);
            ComparatorRepresentationDialog creationComparatorRepresentationDialog = new ComparatorRepresentationDialog(variableRepresentation, comparatorRepresentation, comparatorTypeName);
            if (creationComparatorRepresentationDialog.getResult() == ComparatorRepresentationDialog.RESULT_OK) {
                variableRepresentation.getOwner().setValue(comparatorRepresentation.getOwner());
                canvas.add(variableRepresentation);
                canvas.add(comparatorRepresentation.getOwner(), comparatorRepresentation);
                canvas.getIDSToolWindow().addCodeToEditorPaneSourceCode(variableRepresentation.getDeclarationCode(comparatorRepresentation.getCreationCode()));
                canvas.repaint();
                canvas.addOperation(Utils.getUndoRedoCreationObjectMessage(variableRepresentation.getOwner(), Utils.getClassSimpleName(comparatorTypeName)));

            }
        });
        return button;
    }


    @Override
    public int comparar(T o1, T o2) {
        //speacial case of String comparison
        Object o1Value, o2Value;
        o1Value = ((WrapperWithValue) o1).value;
        o2Value = ((WrapperWithValue) o2).value;
        if (o1Value instanceof String) {
//            System.out.println(o1Value + " compareTo " + o2Value);
            int compare = ((String) o1Value).compareTo(((String) o2Value));
            return ((ComparatorRepresentation<ProjectEntityOrPrimitiveOrEnumWrapper>) comparatorRepresentation).getInSortedFieldItems().get(0).isAscending() ?
                    compare : -compare;
        }
        int compare = 0;
        for (SortedFieldItem inSortedFieldItem : ((ComparatorRepresentation<ProjectEntityOrPrimitiveOrEnumWrapper>) comparatorRepresentation).getInSortedFieldItems()) {
            Field field = inSortedFieldItem.getField();

            Object ownerFromFieldItem1 = Utils.getOwnerFromFieldItem(((WrapperWithValue) o1).getValue(), inSortedFieldItem);
            if (ownerFromFieldItem1 == null) {
                throw new IllegalArgumentException("Elemento inválido: " + Operator.getCanvas().getFirstVariableReferenceTo((WrapperWithValue) o1));
            }
            Object owner1FieldValue = Utils.getFieldValue(ownerFromFieldItem1, field);

            Object ownerFromFieldItem2 = Utils.getOwnerFromFieldItem(((WrapperWithValue) o2).getValue(), inSortedFieldItem);
            if (ownerFromFieldItem2 == null) {
                throw new IllegalArgumentException("Elemento inválido: " + Operator.getCanvas().getFirstVariableReferenceTo((WrapperWithValue) o2));
            }
            Object owner2FieldValue = Utils.getFieldValue(ownerFromFieldItem2, field);

            compare = Utils.compare(owner1FieldValue, owner2FieldValue);
            if (!inSortedFieldItem.isAscending()) {
                compare = -compare;
            }
            if (compare != 0) {
                return compare;
            }
        }
        return compare;
    }

    public void setComparatorRepresentation(ComparatorRepresentation comparatorRepresentation) {
        this.comparatorRepresentation = comparatorRepresentation;
    }
}
