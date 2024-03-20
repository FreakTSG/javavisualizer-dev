package wrapper_classes;

import aed.colecoes.iteraveis.lineares.naoordenadas.ColecaoIteravelLinearNaoOrdenada;
import ui.Canvas;
import ui.dialogs.ComparatorRepresentationDialog;
import ui.dialogs.CreationLinkedListRepresentationDialog;
import ui.graphics.representations.Representation;
import ui.graphics.representations.VariableRepresentation;
import ui.graphics.representations.linked_lists.LinkedListRepresentation;
import ui.graphics.representations.linked_lists.UnsortedListRepresentation;
import ui.operators.Operator;
import ui.operators.building.BuildingOperator;
import utils.Utils;

import java.awt.*;
import java.lang.reflect.Constructor;

public abstract class UnsortedLinkedListWrapper<T extends ColecaoIteravelLinearNaoOrdenada<TComponent>, TComponent extends WrapperWithValue>
        extends LinkedListWrapper<T, TComponent> {
    private static final long serialVersionUID = 1L;



    public UnsortedLinkedListWrapper(Class<T> valueClass, Class<TComponent> componentClass, String componentTypeName) {
        super(valueClass, componentClass, componentTypeName);
//        value = new LinkedList<>();
    }

    public static <TUnsortedLinkedListWrapper extends UnsortedLinkedListWrapper> void create(Class unsortedLinkedListClass,  Class<TUnsortedLinkedListWrapper> unsortedLinkedListWrapperClass) {
        final Representation sourceRepresentation = BuildingOperator.getSourceRepresentation();
        String sourceRepresentationOwnerTypeName = sourceRepresentation.getOwnerTypeName();
        final Point dataStructureRepresentationPosition = new Point(sourceRepresentation.getPosition());
        dataStructureRepresentationPosition.translate(100, 100);


        final Constructor[] constructors = unsortedLinkedListWrapperClass.getConstructors();
        UnsortedLinkedListWrapper newUnsortedLinkedListWrapper;
        try {
            newUnsortedLinkedListWrapper = (UnsortedLinkedListWrapper) constructors[0].newInstance(unsortedLinkedListClass, sourceRepresentation.getOwner().getClass(), sourceRepresentationOwnerTypeName);

            final Class<?> representationClass = Utils.getRepresentationClass(unsortedLinkedListWrapperClass);
            final Constructor[] representationConstructors = representationClass.getConstructors();

            final Canvas canvas = Operator.getCanvas();

            final Point variableRepresentationPosition = new Point(dataStructureRepresentationPosition);
            variableRepresentationPosition.translate(-50, -50);
            final String newSortedLinkedListWrapperValueTypeName = newUnsortedLinkedListWrapper.getValueTypeName();
            final VariableRepresentation variableRepresentation = new VariableRepresentation(variableRepresentationPosition, new VariableWrapper(unsortedLinkedListWrapperClass, newSortedLinkedListWrapperValueTypeName, null), canvas);

            final LinkedListRepresentation linkedListRepresentation =
                    (LinkedListRepresentation) representationConstructors[0].newInstance(dataStructureRepresentationPosition,
                            unsortedLinkedListWrapperClass.cast(newUnsortedLinkedListWrapper), canvas);
            CreationLinkedListRepresentationDialog creationLinkedListRepresentationDialog =
                    new CreationLinkedListRepresentationDialog(variableRepresentation, linkedListRepresentation, "Criar " + Utils.getClassSimpleName(newSortedLinkedListWrapperValueTypeName));
            if (creationLinkedListRepresentationDialog.getResult() == CreationLinkedListRepresentationDialog.RESULT_OK) {
                canvas.add(variableRepresentation);
                canvas.add(newUnsortedLinkedListWrapper, linkedListRepresentation);
                canvas.getIDSToolWindow().addCodeToEditorPaneSourceCode(variableRepresentation.getDeclarationCode(((UnsortedListRepresentation) linkedListRepresentation).getCreationCode()));
                canvas.repaint();
                canvas.addOperation(Utils.getUndoRedoCreationObjectMessage(variableRepresentation.getOwner(), Utils.getClassSimpleName(newUnsortedLinkedListWrapper.getValueTypeName())));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void addFirst(TComponent element) {
        value.inserirNoInicio(element);
    }

    public void addLast(TComponent element) {
        value.inserirNoFim(element);
    }

    public void add(int index, TComponent element) {
        value.inserirPorIndice(index, element);
    }
}
