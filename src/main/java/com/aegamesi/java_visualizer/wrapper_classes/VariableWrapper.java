package wrapper_classes;

import ui.Canvas;
import ui.Constants;
import ui.MyButton;
import ui.Resources;
import ui.dialogs.DialogErrorException;
import ui.dialogs.VariableRepresentationDialog;
import ui.graphics.representations.Representation;
import ui.graphics.representations.VariableRepresentation;
import ui.operators.Operator;
import ui.operators.building.BuildingOperator;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashMap;

public class VariableWrapper<T> extends WrapperWithValue<T> implements ManuallyAssignableReferenceContainer, NonReferenceable {
    private static final long serialVersionUID = 1L;

    private static HashMap<String, Boolean> existingVariables = new HashMap<>();

    private String name;

    public VariableWrapper(Class<T> valueClass, String valueTypeName, T value) {
        super(valueClass, valueTypeName, value);
        name = "";
    }

    public static void resetNames() {
        existingVariables.clear();
    }

    public static HashMap<String, Boolean> getExistingVariables() {
        return existingVariables;
    }

    public static void setExistingVariables(HashMap<String, Boolean> existingVariables) {
        VariableWrapper.existingVariables = existingVariables;
    }

    private static boolean isNewVariableNameValid(String variableName) {
        return Utils.isValidIdentifier(variableName) && !existingVariables.containsKey(variableName);
    }

    public static String getNewVariableName() {
        String newVariableName;
        do {
            newVariableName = "var" + (existingVariables.size() + 1);
        } while (!isNewVariableNameValid(newVariableName));
        return newVariableName;
    }

    public static JButton getButton() {
        JButton button = new MyButton();
        button.setToolTipText("Variável");
        button.setIcon(new ImageIcon(Resources.INSTANCE.variableMenuIcon));
        button.addActionListener(e -> {
            final Representation sourceRepresentation = BuildingOperator.getSourceRepresentation();
            final Point variableRepresentationPosition = new Point(sourceRepresentation.getPosition());
            variableRepresentationPosition.translate(50, -50);

//            final Class sourceRepresentationType = sourceRepresentation.getOwnerTypeName();
//            final Class classToCompare = ClassWrapper.class.isAssignableFrom(sourceRepresentationType) ? sourceRepresentationType.getSuperclass() : sourceRepresentationType;
//            final String genericTypeName = wrapper_classes.Wrapper.class.isAssignableFrom(sourceRepresentationType) ? ((Wrapper) sourceRepresentation.getOwner()).getCompleteTypeName() : sourceRepresentationType.getName();

            final Canvas canvas = Operator.getCanvas();
            final WrapperInterface sourceRepresentationOwner = sourceRepresentation.getOwner();
            final String sourceRepresentationOwnerValueTypeName = sourceRepresentationOwner.getValueTypeName();
            VariableRepresentation variableRepresentation = new VariableRepresentation(variableRepresentationPosition,
                    new VariableWrapper<>(sourceRepresentationOwner.getClass(), sourceRepresentationOwnerValueTypeName, null), canvas);
            VariableRepresentationDialog variableRepresentationDialog = new VariableRepresentationDialog(variableRepresentation, "Criar variável do tipo " + Utils.getClassSimpleName(sourceRepresentationOwnerValueTypeName));
            if (variableRepresentationDialog.getResult() == variableRepresentationDialog.RESULT_OK) {
                canvas.getIDSToolWindow().addCodeToEditorPaneSourceCode(variableRepresentation.getDeclarationCode());
                canvas.add(variableRepresentation);
                canvas.repaint();
                canvas.addOperation(Utils.getUndoRedoCreationVariableMessage(variableRepresentation.getOwner().getName()));
            }
        });
        return button;
    }

    @Override
    public boolean isValid(String sourceTypeName, Field targetField) {
        //targetField is not used here
        return isValid(sourceTypeName);
    }

    public void checkValid(String variableName) {
        if (!Utils.isValidIdentifier(variableName)) {
            throw new DialogErrorException(Constants.INVALID_VARIABLE_NAME_ERROR);
        }
        if (name.equals(variableName)) {
            return;
        }
        if (existingVariables.containsKey(variableName)) {
            throw new DialogErrorException(Constants.DUPLICATE_VARIABLE_NAME_ERROR);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        existingVariables.put(name, true);
    }
}
