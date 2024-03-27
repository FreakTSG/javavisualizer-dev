package com.aegamesi.java_visualizer;

import com.aegamesi.java_visualizer.aed.Comparacao;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.MyHashTable;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.TabelaHash;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaDuplaNaoOrdenada;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaSimplesNaoOrdenada;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ordenadas.estruturas.ListaDuplaOrdenada;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ordenadas.estruturas.ListaSimplesOrdenada;
import com.aegamesi.java_visualizer.model.*;
import com.aegamesi.java_visualizer.ui.ClassCreation;
import com.aegamesi.java_visualizer.ui.IDSToolWindow;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.GraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.RectangularGraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.representations.BaseRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.PrimitiveOrEnumRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.TextualRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.hashtables.HashTableRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.SortedCircularSimpleLinkedListWithBaseMaxOrderRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.UnsortedCircularDoubleLinkedListWithBaseRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.UnsortedCircularSimpleLinkedListWithBaseRepresentation;
import ui.graphics.representations.linked_lists.SortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation;


import javax.swing.*;
import java.awt.*;

import static com.aegamesi.java_visualizer.ui.MyCanvas.IDSToolWindow;


public class MainTestes {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Testes com representações");
        jFrame.setLayout(new BorderLayout());
        IDSToolWindow idsToolWindow = new IDSToolWindow();
        jFrame.add(idsToolWindow.getComponent(), BorderLayout.CENTER);
        MyCanvas canvas = IDSToolWindow.myCanvas;

        // Mock the creation of an ExecutionTrace and heap structures as if we were using Tracer
        ExecutionTrace mockTrace = createMockedExecutionTrace();

        // Use the mocked data to create visual representations
        createVisualRepresentations(mockTrace, canvas);

        jFrame.setMinimumSize(new Dimension(1000, 600));
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private static ExecutionTrace createMockedExecutionTrace() {
        // Instantiate and populate your ExecutionTrace and HeapList here...
        // For example:
        ExecutionTrace trace = new ExecutionTrace();
        HeapList heapList = new HeapList(); // Your actual HeapList implementation

        // Create a few nodes with sample values
        Value firstNodeValue = new Value();
        firstNodeValue.type = Value.Type.LONG;
        firstNodeValue.longValue = 5;

        Value secondNodeValue = new Value();
        secondNodeValue.type = Value.Type.LONG;
        secondNodeValue.longValue = -3;

        // If your HeapList holds values directly
        heapList.items.add(firstNodeValue);
        heapList.items.add(secondNodeValue);

        // If your HeapList holds HeapEntity objects that represent list nodes, create those here
        // For example:
        HeapObject firstNode = new HeapObject();
        firstNode.fields.put("value", firstNodeValue);
        // Assuming 'next' is the field name for the next node reference in your list node class
        firstNode.fields.put("next", secondNodeValue);  // This is highly simplified; you'll need a way to represent the node connections

        // Add similar logic for secondNode and more nodes if necessary

        // Add the list to the trace's heap collection
        trace.heap.put(1L, heapList); // The key should be a unique identifier

        return trace;
    }

    // This method will depend heavily on the internal structure of your HeapList and ListaSimplesNaoOrdenada
    private static ListaSimplesNaoOrdenada<Integer> convertHeapListToLinkedList(HeapList heapList) {
        ListaSimplesNaoOrdenada<Integer> lista = new ListaSimplesNaoOrdenada<>();

        // This assumes that HeapList holds values as Value objects representing integers
        for (Value item : heapList.items) {
            if (item.type == Value.Type.LONG) {
                // Cast the longValue to Integer and add to the list
                // This is because Java does not allow casting from long to Integer directly
                // You may need to handle this differently if your list expects a different type
                lista.inserir((int) item.longValue);
            }
            // If your list expects other types, you can add additional checks here
        }

        return lista;
    }

    private static void createVisualRepresentations(ExecutionTrace trace, MyCanvas canvas) {
        for (HeapEntity entity : trace.heap.values()) {
            // Check if the entity is a linked list and create the corresponding representation
            if (entity instanceof HeapList) {
                HeapList heapList = (HeapList) entity;
                ListaSimplesNaoOrdenada<?> lista = convertHeapListToLinkedList(heapList);
                // The below assumes you have a constructor for UnsortedCircularSimpleLinkedListWithBaseRepresentation
                // that takes these parameters.
                UnsortedCircularSimpleLinkedListWithBaseRepresentation listRepresentation =
                        new UnsortedCircularSimpleLinkedListWithBaseRepresentation(
                                new Point(30, 30), // Adjust position as needed
                                lista, // You might need to cast or transform this
                                canvas
                        );

                // Update the representation with any specific details, such as the iterator's position
                // Assuming you have such a method on your representation class
                listRepresentation.updateIteratorPosition(-1); // Set iterator position if needed

                canvas.add(heapList, listRepresentation);
            }
            // Add similar logic for other types of HeapEntities (if any)
        }

        // Refresh canvas to display the new visual elements
        canvas.revalidate();
        canvas.repaint();
    }
}