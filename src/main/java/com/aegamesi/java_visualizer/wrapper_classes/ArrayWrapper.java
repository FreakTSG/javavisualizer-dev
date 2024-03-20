package wrapper_classes;

import ui.Canvas;
import ui.MyButton;
import ui.Resources;
import ui.dialogs.ComparatorRepresentationDialog;
import ui.dialogs.CreationArrayRepresentationDialog;
import ui.graphics.representations.ArrayRepresentation;
import ui.graphics.representations.Representation;
import ui.graphics.representations.VariableRepresentation;
import ui.operators.Operator;
import ui.operators.building.BuildingOperator;
import utils.Utils;

import javax.swing.*;
import java.awt.*;

public class ArrayWrapper
        extends DataStructureWrapper<WrapperWithValue[]> {
    private static final long serialVersionUID = 1L;


    public ArrayWrapper(String componentTypeName, int size) {
        super(WrapperWithValue[].class, componentTypeName + "[]", componentTypeName);
        value = new WrapperWithValue[size];
    }

    public static JButton getButton() {
        JButton button = new MyButton();
        button.setToolTipText("Array");
        button.setIcon(new ImageIcon(Resources.INSTANCE.arrayMenuIcon));
        button.addActionListener(e -> {
            final Representation sourceRepresentation = BuildingOperator.getSourceRepresentation();
            final WrapperInterface sourceRepresentationOwner = sourceRepresentation.getOwner();
            final String sourceRepresentationOwnerValueTypeName = sourceRepresentationOwner.getValueTypeName();

            final Point arrayRepresentationPosition = new Point(sourceRepresentation.getPosition());
            arrayRepresentationPosition.translate(0, 100);

            final Canvas canvas = Operator.getCanvas();

            final Point variableRepresentationPosition = new Point(arrayRepresentationPosition);
            variableRepresentationPosition.translate(-50, -50);

            final VariableRepresentation variableRepresentation = new VariableRepresentation(variableRepresentationPosition, new VariableWrapper(wrapper_classes.ArrayWrapper.class, sourceRepresentationOwnerValueTypeName + "[]", null), canvas);
//            CreationArrayRepresentationDialog creationArrayRepresentationDialog = new CreationArrayRepresentationDialog(variableRepresentation, "Creating array of " + Utils.getClassSimpleName(sourceRepresentationOwnerValueTypeName));
            ArrayWrapper arrayWrapper = new ArrayWrapper(sourceRepresentationOwnerValueTypeName, 0);
            ArrayRepresentation arrayRepresentation = new ArrayRepresentation(arrayRepresentationPosition, arrayWrapper, sourceRepresentationOwnerValueTypeName, canvas);
            CreationArrayRepresentationDialog creationArrayRepresentationDialog = new CreationArrayRepresentationDialog(variableRepresentation, arrayRepresentation, "Criar Array de " + Utils.getClassSimpleName(sourceRepresentationOwnerValueTypeName));

            if (creationArrayRepresentationDialog.getResult() == ComparatorRepresentationDialog.RESULT_OK) {
//                final ArrayWrapper arrayWrapper = new ArrayWrapper(sourceRepresentationOwnerValueTypeName, creationArrayRepresentationDialog.getSizeOfArray());
//                ArrayRepresentation arrayRepresentation = new ArrayRepresentation(arrayRepresentationPosition,
//                        arrayWrapper, sourceRepresentationOwnerValueTypeName, canvas);
//                creationArrayRepresentationDialog.setArrayRepresentation(arrayRepresentation);

                canvas.add(variableRepresentation);
                canvas.add(arrayWrapper, arrayRepresentation);
                canvas.getIDSToolWindow().addCodeToEditorPaneSourceCode(variableRepresentation.getDeclarationCode(arrayRepresentation.getCreationCode()));
                canvas.repaint();
                canvas.addOperation(Utils.getUndoRedoCreationObjectMessage(variableRepresentation.getOwner(), Utils.getClassSimpleName(arrayWrapper.valueTypeName)));
            }
        });
        return button;

    }

    public void add(WrapperWithValue element, int index) {
        value[index] = element;
    }

    public WrapperWithValue get(int index) {
        return value[index];
    }

//    public boolean isComponentValid(String sourceTypeName, ArrayReference arrayReference) {
//        final String targetFieldTypeName = arrayReference.getCompleteTypeName();
//        if (targetFieldTypeName.contains("<") || targetFieldTypeName.contains("[")) {
//            return targetFieldTypeName.equals(sourceTypeName);
//        }
//
//        return Utils.getClassForName(targetFieldTypeName).isAssignableFrom(Utils.getClassForName(sourceTypeName));
//    }
}
