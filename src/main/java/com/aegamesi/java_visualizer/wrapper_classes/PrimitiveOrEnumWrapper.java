package wrapper_classes;

import ui.Canvas;
import ui.Constants;
import ui.MyButton;
import ui.dialogs.CreationBooleanPrimitiveOrEnumDialog;
import ui.dialogs.CreationNonBooleanPrimitiveDialog;
import ui.dialogs.DialogErrorException;
import ui.dialogs.PrimitiveOrEnumDialog;
import ui.graphics.representations.PrimitiveOrEnumRepresentation;
import ui.graphics.representations.VariableRepresentation;
import ui.operators.Operator;
import ui.operators.building.OverStateOfBuildingOperator;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class PrimitiveOrEnumWrapper<T> extends ProjectEntityOrPrimitiveOrEnumWrapper<T> {
    private static final long serialVersionUID = 1L;


    public PrimitiveOrEnumWrapper(Class<T> valueClass, T value) {
        super(valueClass, value);
    }

    public static JButton getButton(Class<?> clazz) {
        final String simpleName = clazz.getSimpleName();
//        JButton button = new JButton(simpleName);
        JButton button = new MyButton(simpleName);
        button.setToolTipText(simpleName);
        button.addActionListener(e -> {
            PrimitiveOrEnumWrapper primitiveOrEnumWrapper = new PrimitiveOrEnumWrapper(clazz, Utils.createObject(clazz, false));
            final Point defaultRepresentationPosition = OverStateOfBuildingOperator.getOverPosition();
            final Point variableRepresentationPosition = new Point(defaultRepresentationPosition);
            variableRepresentationPosition.translate(-50, -50);
            final Canvas canvas = Operator.getCanvas();
            final VariableRepresentation variableRepresentation = new VariableRepresentation(variableRepresentationPosition, new VariableWrapper(clazz, clazz.getName(), primitiveOrEnumWrapper), canvas);
            final PrimitiveOrEnumRepresentation primitiveOrEnumRepresentation = new PrimitiveOrEnumRepresentation(defaultRepresentationPosition, primitiveOrEnumWrapper, canvas);
            PrimitiveOrEnumDialog creationPrimitiveDialog;
            String title = "Criar " + simpleName;
            if (clazz.isAssignableFrom(Boolean.class) || clazz.isEnum()) {
                creationPrimitiveDialog = new CreationBooleanPrimitiveOrEnumDialog(variableRepresentation, primitiveOrEnumRepresentation, title);
            } else {
                creationPrimitiveDialog = new CreationNonBooleanPrimitiveDialog(variableRepresentation, primitiveOrEnumRepresentation, title);
            }
            if (creationPrimitiveDialog.getResult() == creationPrimitiveDialog.RESULT_OK) {
                canvas.add(variableRepresentation);
                canvas.add((Wrapper) primitiveOrEnumRepresentation.getOwner(), primitiveOrEnumRepresentation);
                canvas.getIDSToolWindow().addCodeToEditorPaneSourceCode(variableRepresentation.getDeclarationCode(primitiveOrEnumRepresentation.getCreationCode()));
                canvas.repaint();
                canvas.addOperation(Utils.getUndoRedoCreationObjectMessage(variableRepresentation.getOwner(), simpleName));
            }
        });
        return button;
    }

    public void setValueFromString(String value) {
        final Object convertedValue = Utils.getConvertedValue(valueClass, value);

        this.value = (T) convertedValue;
    }

    public void checkValid(String value) {
        try {
            final Object convertedValue = Utils.getConvertedValue(valueClass, value);
            if (!valueClass.getSimpleName().equals(String.class.getSimpleName()) && convertedValue instanceof String) {
                throw new DialogErrorException(Constants.INVALID_VALUE_ERROR);
            }
        } catch (Exception e) {
            throw new DialogErrorException(Constants.INVALID_VALUE_ERROR);
        }
    }
}
