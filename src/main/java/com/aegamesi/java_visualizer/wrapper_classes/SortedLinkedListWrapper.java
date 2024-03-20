package wrapper_classes;

import aed.colecoes.iteraveis.lineares.ordenadas.ColecaoIteravelLinearOrdenada;
import ui.Canvas;
import ui.dialogs.ComparatorRepresentationDialog;
import ui.dialogs.CreationLinkedListRepresentationDialog;
import ui.graphics.representations.Representation;
import ui.graphics.representations.VariableRepresentation;
import ui.graphics.representations.linked_lists.LinkedListRepresentation;
import ui.graphics.representations.linked_lists.SortedListRepresentation;
import ui.operators.Operator;
import ui.operators.building.BuildingOperator;
import utils.Utils;

import java.awt.*;
import java.lang.reflect.Constructor;

public abstract class SortedLinkedListWrapper<T extends ColecaoIteravelLinearOrdenada<TComponent>, TComponent extends WrapperWithValue>
        extends LinkedListWrapper<T, TComponent> {
    private static final long serialVersionUID = 1L;


    protected ComparatorWrapper<TComponent> comparatorWrapper;

    public SortedLinkedListWrapper(Class<T> valueClass, Class<TComponent> componentClass, String componentTypeName, ComparatorWrapper<TComponent> comparatorWrapper) {
        super(valueClass, componentClass, componentTypeName);
        this.comparatorWrapper = comparatorWrapper;
    }

    public static <TSortedLinkedListWrapper extends SortedLinkedListWrapper> void create(Class sortedLinkedListClass, Class<TSortedLinkedListWrapper> sortedLinkedListWrapperClass) {
        final Representation sourceRepresentation = BuildingOperator.getSourceRepresentation();
        final Point dataStructureRepresentationPosition = new Point(sourceRepresentation.getPosition());
        dataStructureRepresentationPosition.translate(100, 100);


        final Constructor[] constructors = sortedLinkedListWrapperClass.getConstructors();
        SortedLinkedListWrapper newSortedLinkedListWrapper;
        try {
            Class componentClass = ((ComparatorWrapper) sourceRepresentation.getOwner()).getValueClass();
            newSortedLinkedListWrapper = (SortedLinkedListWrapper) constructors[0].newInstance(sortedLinkedListClass,
                    componentClass, componentClass.getName(), sourceRepresentation.getOwner());

            final Class<?> representationClass = Utils.getRepresentationClass(sortedLinkedListWrapperClass);
            final Constructor[] representationConstructors = representationClass.getConstructors();

            final Canvas canvas = Operator.getCanvas();

            final Point variableRepresentationPosition = new Point(dataStructureRepresentationPosition);
            variableRepresentationPosition.translate(-50, -50);
            final VariableRepresentation variableRepresentation = new VariableRepresentation(variableRepresentationPosition, new VariableWrapper(sortedLinkedListWrapperClass, newSortedLinkedListWrapper.getValueTypeName(), null), canvas);

            final LinkedListRepresentation linkedListRepresentation = (LinkedListRepresentation) representationConstructors[0].newInstance(dataStructureRepresentationPosition, sortedLinkedListWrapperClass.cast(newSortedLinkedListWrapper), canvas);
            CreationLinkedListRepresentationDialog creationLinkedListRepresentationDialog = new CreationLinkedListRepresentationDialog(variableRepresentation, linkedListRepresentation, "Criar " + Utils.getClassSimpleName(newSortedLinkedListWrapper.getValueTypeName()));
            if (creationLinkedListRepresentationDialog.getResult() == ComparatorRepresentationDialog.RESULT_OK) {
                canvas.add(variableRepresentation);
                canvas.add(newSortedLinkedListWrapper, linkedListRepresentation);
                canvas.getIDSToolWindow().addCodeToEditorPaneSourceCode(variableRepresentation.getDeclarationCode(((SortedListRepresentation) linkedListRepresentation).getCreationCode()));
                canvas.repaint();
                canvas.addOperation(Utils.getUndoRedoCreationObjectMessage(variableRepresentation.getOwner(), Utils.getClassSimpleName(newSortedLinkedListWrapper.getValueTypeName())));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void add(TComponent element) {
        value.inserir(element);
    }

    public boolean isValid(TComponent elem) {
        return comparatorWrapper.isValid(elem);
    }
}
