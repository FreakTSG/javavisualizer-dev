package wrapper_classes;

import ui.Canvas;
import ui.*;
import ui.dialogs.CreationRecordDialog;
import ui.dialogs.DialogErrorException;
import ui.graphics.PositionalGraphicElement;
import ui.graphics.aggregations.RecordComponent;
import ui.graphics.representations.RecordRepresentation;
import ui.operators.Operator;
import ui.operators.building.OverStateOfBuildingOperator;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;

public class RecordWrapper extends Wrapper implements NonReferenceable {
    public static final String GENERATE_RECORD = "GENERATE_RECORD";
    public static final String DELETE_RECORD = "DELETE_RECORD";
    private static final long serialVersionUID = 1L;
    private static HashMap<String, Boolean> existingRecords = new HashMap<>();
    private LinkedList<wrapper_classes.RecordComponentWrapper> components;

    public RecordWrapper() {
        super("");
        components = new LinkedList<>();
    }

    public static void resetNames() {
        existingRecords.clear();
    }

    public static HashMap<String, Boolean> getExistingRecords() {
        return existingRecords;
    }

    public static void setExistingRecords(HashMap<String, Boolean> existingRecords) {
        RecordWrapper.existingRecords = existingRecords;
    }

    public static boolean containsRecord(String recordName) {
        return existingRecords.containsKey(recordName);
    }

    public static JButton getButton() {
        JButton button = new MyButton();
        button.setToolTipText("Classe");
        button.setIcon(new ImageIcon(Resources.INSTANCE.recordMenuIcon));
        button.addActionListener(e -> {
            final Point defaultRepresentationPosition = OverStateOfBuildingOperator.getOverPosition();
            final Point variableRepresentationPosition = new Point(defaultRepresentationPosition);
            variableRepresentationPosition.translate(-50, -50);
            final RecordWrapper recordWrapper = new RecordWrapper();
            final Canvas canvas = Operator.getCanvas();
            final RecordRepresentation recordRepresentation = new RecordRepresentation(defaultRepresentationPosition, recordWrapper, canvas);

            CreationRecordDialog creationRecordDialog = new CreationRecordDialog(recordRepresentation, "Criar Classe");
            if (creationRecordDialog.getResult() == creationRecordDialog.RESULT_OK) {
                recordWrapper.setValueTypeName(creationRecordDialog.getName());
                canvas.add(recordRepresentation.getOwner(), recordRepresentation);
                canvas.repaint();
                canvas.addOperation(Utils.getUndoRedoCreationRecordMessage(creationRecordDialog.getName()));
            }

        });
        return button;
    }

    @Override
    public String getValueTypeName() {
        return super.getValueTypeName() + " Class";
    }

    public void setValueTypeName(String valueTypeName) {
        existingRecords.put(valueTypeName, true);
        this.valueTypeName = valueTypeName;
    }

    public void add(wrapper_classes.RecordComponentWrapper component) {
        components.add(component);
    }

    public LinkedList<wrapper_classes.RecordComponentWrapper> getComponents() {
        return components;
    }

    public void remove(wrapper_classes.RecordComponentWrapper component) {
        components.remove(component);
    }

    public void checkValid(String recordName) {
        if (valueTypeName.equals(recordName) || !Utils.isValidTypeIdentifier(recordName)) {
            throw new DialogErrorException(Constants.INVALID_RECORD_NAME_ERROR);
        }
        if (Utils.isDuplicateTypeIdentifierInPackage(recordName) || existingRecords.containsKey(recordName)) {
            throw new DialogErrorException(Constants.DUPLICATE_RECORD_NAME_ERROR);
        }
    }

    public boolean isComponentValid(String componentName) {
        if (!Utils.isValidIdentifier(componentName)) {
            return false;
        }
        for (wrapper_classes.RecordComponentWrapper component : components) {
            if (component.getName().equals(componentName)) {
                return false;
            }
        }
        return true;
    }


    public ButtonBar getButtonBar(RecordRepresentation recordRepresentation, PositionalGraphicElement positionalGraphicElement) {
        return positionalGraphicElement instanceof RecordComponent ?
                ((RecordComponent<?>) positionalGraphicElement).getRecordComponentButtonBar(recordRepresentation) :
                getRecordButtonBar();

    }

    private ButtonBar getRecordButtonBar() {
        final ButtonBar buttonBar = components.size() > 0 ?
                IDSToolWindow.getButtonBar(IDSToolWindow.RECORD_BUTTON_BAR) :
                IDSToolWindow.getButtonBar(IDSToolWindow.EMPTY_RECORD_BUTTON_BAR);
        JButton button;
        if (components.size() > 0) {
            button = buttonBar.getButton(GENERATE_RECORD);
            generate(button);
        }

        //delete record component
        button = buttonBar.getButton(DELETE_RECORD);
        for (ActionListener actionListener : button.getActionListeners()) {
            button.removeActionListener(actionListener);
        }
        button.addActionListener(e -> {
            //delete record
            Canvas canvas = Operator.getCanvas();
            canvas.remove(this);
            canvas.repaint();
            canvas.addOperation(Utils.getUndoRedoDeleteRecordMessage(valueTypeName));
        });


        return buttonBar;
    }


    public void generate(JButton button) {
        for (ActionListener actionListener : button.getActionListeners()) {
            button.removeActionListener(actionListener);
        }
        button.addActionListener(e -> {
            //criar e compilar classe nova
            final Canvas canvas = Operator.getCanvas();
            ClassCreation newClass = new ClassCreation(canvas.getIDSToolWindow().getProject().getBasePath() + "/src/" + canvas.getModelPackage().replace(".", "/") + "/" + valueTypeName, false, true);
            //fields
            for (RecordComponentWrapper component : components) {
                newClass.addAttribute(component.getValueTypeName(), component.getName());
            }
            newClass.writeGeneratedClass();

//            StringBuilder sb = new StringBuilder();
//            sb.append("package " + Operator.getCanvas().getModelPackage() + ";\n");
//            sb.append("import java.io.Serializable;\n");
//            sb.append("public class ").append(valueTypeName).append(" implements Serializable {\n");
//            //fields
//            for (RecordComponentWrapper component : components) {
//                sb.append(component);
//            }
//            sb.append("}");
            remove();
            canvas.repaint();
            canvas.addOperation(Utils.getUndoRedoGenerateRecordMessage(valueTypeName));

//            Utils.createAndCompileClass(canvas.getIDSToolWindow().getProject().getBasePath() + "/src/" + Operator.getCanvas().getModelPackage().replace(".", "/"), valueTypeName, sb.toString());
            canvas.getIDSToolWindow().processProjectEntities(false);

        });
    }

    public void remove() {
        Operator.getCanvas().removeGraphicElement(this);
        existingRecords.remove(valueTypeName);
    }

}
