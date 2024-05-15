package com.aegamesi.java_visualizer.ui;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.ColecaoIteravel;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaDuplaNaoOrdenada;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaSimplesNaoOrdenada;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ordenadas.estruturas.ListaDuplaOrdenada;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ordenadas.estruturas.ListaSimplesOrdenada;
import com.aegamesi.java_visualizer.model.*;

import com.aegamesi.java_visualizer.ui.graphics.Connection;
import com.aegamesi.java_visualizer.ui.graphics.OutConnector;
import com.aegamesi.java_visualizer.ui.graphics.PositionalGraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.StraightConnection;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.ArrayReference;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.Reference;
import com.aegamesi.java_visualizer.ui.graphics.representations.*;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.*;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.nodes.DoubleNodeRepresentation;

import com.aegamesi.java_visualizer.utils.Vetor2D;
import com.aegamesi.java_visualizer.aed.Comparacao;
import ui.graphics.representations.linked_lists.SortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.lang.reflect.Array;
import java.util.*;


import static com.aegamesi.java_visualizer.model.HeapObject.*;
import static com.aegamesi.java_visualizer.model.Value.Type.LONG;
import static com.aegamesi.java_visualizer.model.Value.Type.STRING;
import static com.aegamesi.java_visualizer.ui.IDSToolWindow.myCanvas;

public class MyCanvas extends JPanel implements MouseListener, MouseMotionListener {
    private RepresentationWithInConnectors draggedRepresentation = null;
    private Point dragOffset = null;


    public static IDSToolWindow IDSToolWindow;
    private static final int i = 0;
    private final LinkedList<PositionalGraphicElement> positionalGraphicElements;
    //    private HashMap<Wrapper, RepresentationWithInConnectors> representationWithInConnectorsByOwner;
    private final HashMap<Object, RepresentationWithInConnectors> representationWithInConnectorsByOwner;
    //    private final HashMap<Long, Wrapper> wrapperById;
    private final HashMap<OutConnector, Connection> connectionByOutConnector;
    private final boolean compactMode;
    private final float zoom;
    private Point mousePosition = new Point(0, 0);
    private static final int START_X = 100;
    private static final int START_Y = 100;
    private static final int HORIZONTAL_SPACING = 50;
    private static final int VERTICAL_SPACING = 50;
    private Map<Object, RepresentationWithInConnectors> existingRepresentations = new HashMap<>();


    public MyCanvas(IDSToolWindow IDSToolWindow) {
        MyCanvas.IDSToolWindow = IDSToolWindow;
        compactMode = false;

        positionalGraphicElements = new LinkedList<>();
        representationWithInConnectorsByOwner = new HashMap<>();
//        wrapperById = new HashMap<>();
        connectionByOutConnector = new HashMap<>();
        addMouseListener(this);
        addMouseMotionListener(this);

        setFocusable(true);

        zoom = 1;
        setDoubleBuffered(true);
        repaint();

    }


    public void rebuildRepresentations() {
        for (RepresentationWithInConnectors representation : representationWithInConnectorsByOwner.values()) {
            if (representation instanceof DefaultRepresentation) {
                ((DefaultRepresentation<?>) representation).rebuild();
            }
        }
    }

    public void add(Connection connection) {
        final Connection oldConnection = connectionByOutConnector.get(connection.getSource());
        if (oldConnection != null) {
            oldConnection.getTarget().decrementReferenceCount();
        }
        connectionByOutConnector.put(connection.getSource(), connection);
    }

    public void updateRepresentations(ExecutionTrace trace) {
        createVisualRepresentations(trace, this); // You need to have this method defined based on your mock test
        revalidate();
        repaint();
    }


    public void createVisualRepresentations(ExecutionTrace trace, MyCanvas canvas) {
        System.out.println("Creating visual representations for the trace");
        Map<Long, HeapEntity> heapMap = buildHeapMap(trace);
        canvas.removeAllRepresentations();
        for (HeapEntity entity : trace.heap.values()) {
            System.out.println("Creating visual representation for entity: " + entity);

            // Check if the entity is a list of lists (HeapList) and handle it separately
            if (entity instanceof HeapList) {

                System.out.println("Processing a HeapList which might be a list of lists.");
                HeapList heapList = (HeapList) entity;
                System.out.println("HeapList items: " + heapList.items);
                System.out.println("HeapList label: " + heapList.label);
                System.out.println("HeapList id: " + heapList.id);
                System.out.println("HeapList type: " + heapList.type);
                System.out.println("HeapList Class: " + heapList.getClass());

                if(heapList.label.contains("Integer")){
                    System.out.println("Entrei no Integer");
                    int length = heapList.items.size();
                    Class<?> componentType = Integer.class;  // or dynamically determined
                    Object array = Array.newInstance(componentType, length);

                    for (int i = 0; i < length; i++) {
                        Array.set(array, i, heapList.items.get(i).getLongValue().intValue());
                    }
                    ArrayRepresentation arrayRepresentation = new ArrayRepresentation(new Point(0, 0), array, "Integer", canvas);
                    // Assuming you have a method to add this to the canvas
                    canvas.add(array,arrayRepresentation ); // Method to add this representation to the canvas
                }


                //print whats inside the heaplist
                for (Value value : heapList.items) {
                    System.out.println("Value Type: " + value.type);
                    switch (value.type) {
                        case LONG:
                            System.out.println("Long value: " + value.longValue);
                            break;
                        case STRING:
                            System.out.println("String value: " + value.stringValue);
                            break;
                        case REFERENCE:
                            System.out.println("Reference to entity with ID: " + value.reference);
                            if (heapMap.containsKey(value.reference)) {
                                System.out.println("Referenced Entity: " + heapMap.get(value.reference).getClass().getSimpleName());
                            } else {
                                System.out.println("Reference not found in heapMap.");
                            }
                            break;
                    }
                  // Array items= (Array) heapMap.get(value.reference);
                  // ArrayRepresentation arrayRepresentation = new ArrayRepresentation(new Point(0, 0), items, "int", canvas);

                }



            } else if (entity instanceof HeapObject ) {

                HeapObject HeapObject = (HeapObject) entity;

                Comparacao<Object> comparator = (o1, o2) -> {
                    if (o1.getClass() == o2.getClass() && o1 instanceof Comparable) {
                        return ((Comparable) o1).compareTo(o2);
                    }
                    throw new IllegalArgumentException("Unsupported object types for comparison");
                };
                if (HeapObject.label.contains("org.example.aed.Comparacao")) {
                    System.out.println("Comparator found: " + comparator);
                }


                if(isSimpleList(HeapObject)){
                    System.out.println("Entrei na lista simples nao ordenada");
                    ListaSimplesNaoOrdenada<?> simpleList=convertHeapObjectToListofLists(HeapObject, heapMap,canvas,comparator);
                    System.out.println("Simple list converted "+simpleList);
                    addSimpleListRepresentation(simpleList, canvas);
                }
               if(HeapObject.label.contains("ListaDuplaNaoOrdenada")){
                   System.out.println("Entrei na lista dupla nao ordenada");
                   ListaDuplaNaoOrdenada<?> doubleList=convertHeapObjectToDoubleList(HeapObject, heapMap);
                   System.out.println("Double list converted "+doubleList);
                   addDoubleListRepresentation(doubleList, canvas);
               }
               if(isSortedSimpleList(HeapObject, heapMap)){

                   System.out.println("Entrei na lista Simples ordenada");
                   ListaSimplesOrdenada<Object> simpleSortedList = convertHeapObjectToSortedSimpleList(HeapObject, heapMap, comparator);
                   System.out.println("Sorted SImple list converted "+simpleSortedList);
                   addSortedSimpleListRepresentation(simpleSortedList, canvas);
               }
               if (isSortedDoubleList(HeapObject, heapMap)){
                   System.out.println("Entrei na lista Dupla ordenada");
                   ListaDuplaOrdenada<Object> doubleSortedList = convertHeapObjectToSortedDoubleList(HeapObject, heapMap, comparator);
                   System.out.println("Sorted Double list converted "+doubleSortedList);
                   addSortedDoubleListRepresentation(doubleSortedList, canvas);

               }

                //determineAndRepresentHeapObject(heapObject, canvas,heapMap);
            }
        }

        if (canvas.representationWithInConnectorsByOwner.isEmpty()) {
            System.out.println("No representations lista have been added to the canvas.");
        } else {
            System.out.println("Representations lista added to canvas: " + canvas.representationWithInConnectorsByOwner.size());
        }
        refreshCanvas(canvas);
    }

    public static boolean isSortedSimpleList(HeapObject heapObject, Map<Long, HeapEntity> heapMap) {
        if (heapObject.fields.containsKey("base") && heapObject.fields.containsKey("criterio")) {
            HeapObject baseNode = (HeapObject) heapMap.get(heapObject.fields.get("base").reference);
            // It has 'anterior' field, which might suggest it is a double list (ListaDuplaOrdenada)
            return baseNode == null || !baseNode.fields.containsKey("anterior");
            // If it doesn't have 'anterior', it's a simple sorted list (ListaSimplesOrdenada)
        }
        // If it doesn't have 'base' or 'criterio', it's not a sorted list
        return false;
    }

    public static boolean isSortedDoubleList(HeapObject heapObject, Map<Long, HeapEntity> heapMap) {
        if (heapObject.fields.containsKey("base") && heapObject.fields.containsKey("criterio")) {
            HeapObject baseNode = (HeapObject) heapMap.get(heapObject.fields.get("base").reference);
            // It has 'anterior' field, which might suggest it is a double list (ListaDuplaOrdenada)
            return baseNode != null && baseNode.fields.containsKey("anterior");
            // If it doesn't have 'anterior', it's a simple sorted list (ListaSimplesOrdenada)
        }
        // If it doesn't have 'base' or 'criterio', it's not a sorted list
        return false;
    }
    private static Map<Long, HeapEntity> buildHeapMap(ExecutionTrace trace) {
        Map<Long, HeapEntity> map = new HashMap<>();
        for (HeapEntity heapEntity : trace.heap.values()) {
            map.put(heapEntity.id, heapEntity);  // Correctly using HeapEntity
        }
        return map;
    }
    private static Point calculatePositionForListItem(int index) {
        int x = START_X + index * HORIZONTAL_SPACING;
        int y = START_Y + VERTICAL_SPACING;
        return new Point(x, y);
    }
    private static void refreshCanvas(MyCanvas canvas) {
        canvas.invalidate();
        canvas.revalidate();
        canvas.repaint();
    }

    private void addSimpleListRepresentation(ListaSimplesNaoOrdenada<?> simpleList, MyCanvas canvas) {
        int index = 0;
        for (Object item : simpleList) {
            Point position = calculatePositionForListItem(index);
            System.out.println("O que esta a passar"+item);
            System.out.println("O que esta a passar"+(item instanceof ColecaoIteravel<?>));
            if (item instanceof ListaSimplesNaoOrdenada<?>) {
                System.out.println("Estou aqui dentro 1\n\n");

                RepresentationWithInConnectors existingRepresentation = findRepresentationForList((ListaSimplesNaoOrdenada<?>) item);
                if (existingRepresentation != null) {
                    // Connect to existing representation
                    canvas.add(item, existingRepresentation);
                    refreshCanvas(canvas);
                } else {
                    System.out.println("Existing Representation is null\n\n");
                    // Create new representation
                    UnsortedCircularSimpleLinkedListWithBaseRepresentation nestedListRepresentation =
                            new UnsortedCircularSimpleLinkedListWithBaseRepresentation(position, (ListaSimplesNaoOrdenada<?>) item, canvas);
                    //nestedListRepresentation.update();
                    canvas.add(item, nestedListRepresentation);
                    existingRepresentations.put(item, nestedListRepresentation);
                    refreshCanvas(canvas);
                }

            }
            if (item instanceof ListaDuplaNaoOrdenada<?>) {
                System.out.println("Estou aqui dentro 1\n\n");

                RepresentationWithInConnectors existingRepresentation = findRepresentationForDoubleList((ListaDuplaNaoOrdenada<?>) item);
                if (existingRepresentation != null) {
                    // Connect to existing representation
                    canvas.add(item, existingRepresentation);
                    refreshCanvas(canvas);
                } else {
                    System.out.println("Existing Representation is null\n\n");
                    // Create new representation
                    UnsortedCircularDoubleLinkedListWithBaseRepresentation nestedListRepresentation =
                            new UnsortedCircularDoubleLinkedListWithBaseRepresentation(position, (ListaDuplaNaoOrdenada<?>) item, canvas);
                    //nestedListRepresentation.update();
                    canvas.add(item, nestedListRepresentation);
                    existingRepresentations.put(item, nestedListRepresentation);
                    refreshCanvas(canvas);
                }

            }
            if(item instanceof ListaSimplesOrdenada<?>){
                RepresentationWithInConnectors existingRepresentation = findRepresentationForSortedList((ListaSimplesOrdenada<?>) item);
                if (existingRepresentation != null) {
                    // Connect to existing representation
                    canvas.add(item, existingRepresentation);
                    refreshCanvas(canvas);
                } else {
                    System.out.println("Existing Representation is null\n\n");
                    // Create new representation
                    SortedCircularSimpleLinkedListWithBaseMaxOrderRepresentation nestedListRepresentation =
                            new SortedCircularSimpleLinkedListWithBaseMaxOrderRepresentation(position, (ListaSimplesOrdenada<?>) item, canvas);
                    //nestedListRepresentation.update();
                    canvas.add(item, nestedListRepresentation);
                    existingRepresentations.put(item, nestedListRepresentation);
                    refreshCanvas(canvas);
                }
            }
            if(item instanceof ListaDuplaOrdenada<?>){
                RepresentationWithInConnectors existingRepresentation = findRepresentationForSortedDoubleList((ListaDuplaOrdenada<?>) item);
                if (existingRepresentation != null) {
                    // Connect to existing representation
                    canvas.add(item, existingRepresentation);
                    refreshCanvas(canvas);
                } else {
                    System.out.println("Existing Representation is null\n\n");
                    // Create new representation
                    SortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation nestedListRepresentation =
                            new SortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation(position, (ListaDuplaOrdenada<?>) item, canvas);
                    //nestedListRepresentation.update();
                    canvas.add(item, nestedListRepresentation);
                    existingRepresentations.put(item, nestedListRepresentation);
                    refreshCanvas(canvas);
                }
            }else if(!(item instanceof ColecaoIteravel<?>)){
           PrimitiveOrEnumRepresentation itemRepresentation = new PrimitiveOrEnumRepresentation(position, item, canvas);
           canvas.add(item, itemRepresentation);
           refreshCanvas(canvas);
//
           }
            index++;
        }

        System.out.println("Estou aqui dentro 2\n\n");
        UnsortedCircularSimpleLinkedListWithBaseRepresentation representation =
                new UnsortedCircularSimpleLinkedListWithBaseRepresentation(new Point(START_X, START_Y), simpleList, canvas);
        canvas.add(simpleList, representation);
        System.out.println("Lista valores:" + simpleList);
        existingRepresentations.put(simpleList, representation);
        canvas.representationWithInConnectorsByOwner.put(simpleList, representation);
        refreshCanvas(canvas);

    }

    private RepresentationWithInConnectors findRepresentationForList(ListaSimplesNaoOrdenada<?> list) {
        for (Map.Entry<Object, RepresentationWithInConnectors> entry : existingRepresentations.entrySet()) {
            if (entry.getKey() instanceof ListaSimplesNaoOrdenada<?> && list.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private RepresentationWithInConnectors findRepresentationForDoubleList(ListaDuplaNaoOrdenada<?> list) {
        for (Map.Entry<Object, RepresentationWithInConnectors> entry : existingRepresentations.entrySet()) {
            if (entry.getKey() instanceof ListaDuplaNaoOrdenada<?> && list.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private RepresentationWithInConnectors findRepresentationForSortedList(ListaSimplesOrdenada<?> list) {
        for (Map.Entry<Object, RepresentationWithInConnectors> entry : existingRepresentations.entrySet()) {
            if (entry.getKey() instanceof ListaSimplesOrdenada<?> && list.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private RepresentationWithInConnectors findRepresentationForSortedDoubleList(ListaDuplaOrdenada<?> list) {
        for (Map.Entry<Object, RepresentationWithInConnectors> entry : existingRepresentations.entrySet()) {
            if (entry.getKey() instanceof ListaDuplaOrdenada<?> && list.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private RepresentationWithInConnectors findRepresentationForListDoubleUnsorted(ListaDuplaNaoOrdenada<?> list) {
        for (Map.Entry<Object, RepresentationWithInConnectors> entry : existingRepresentations.entrySet()) {
            System.out.println("O if esta a dar:  " + (entry.getKey() instanceof ListaDuplaNaoOrdenada<?> && list.equals(entry.getKey())));
            if (entry.getKey() instanceof ListaDuplaNaoOrdenada<?> && list.equals(entry.getKey())) {
                System.out.println("O resultado da procura: " + entry.getValue());
                return entry.getValue();
            }
        }
        System.out.println("Este resultado é null ");
        return null;
    }


    private void addDoubleListRepresentation(ListaDuplaNaoOrdenada<?> doubleList, MyCanvas canvas) {
        // Logic to create a visual representation for the double list and add it to the canvas
        int index = 0;
        for (Object item : doubleList) {
            Point position = calculatePositionForListItem(index);

            if (item instanceof ListaDuplaNaoOrdenada<?>) {
                RepresentationWithInConnectors existingRepresentation = findRepresentationForListDoubleUnsorted((ListaDuplaNaoOrdenada<?>) item);
                if (existingRepresentation != null) {
                    // Connect to existing representation
                    System.out.println("Encontrei uma representacao\n\n");
                    canvas.add(item, existingRepresentation);
                    refreshCanvas(canvas);
                } else {
                    System.out.println("Existing Representation is null\n\n");
                    // Create new representation
                    UnsortedCircularDoubleLinkedListWithBaseRepresentation nestedListRepresentation =
                            new UnsortedCircularDoubleLinkedListWithBaseRepresentation(position, (ListaDuplaNaoOrdenada<?>) item, canvas);
                    //nestedListRepresentation.update();
                    canvas.add(item, nestedListRepresentation);
                    existingRepresentations.put(item, nestedListRepresentation);
                    refreshCanvas(canvas);
                }
            } else {
                System.out.println("Estou so  a ver aqui ListaDouble\n\n");
                PrimitiveOrEnumRepresentation itemRepresentation = new PrimitiveOrEnumRepresentation(position, item, canvas);
                canvas.add(item, itemRepresentation);
                refreshCanvas(canvas);


            }
            index++;
        }
        UnsortedCircularDoubleLinkedListWithBaseRepresentation representation =
                new UnsortedCircularDoubleLinkedListWithBaseRepresentation(new Point(START_X, START_Y), doubleList, canvas);
        canvas.add(doubleList, representation);
        representation.update();
        existingRepresentations.put(doubleList, representation);
        canvas.representationWithInConnectorsByOwner.put(doubleList, representation);
        refreshCanvas(canvas);

    }


    private void addSortedSimpleListRepresentation(ListaSimplesOrdenada<?> simpleList, MyCanvas canvas) {
        // Logic to create a visual representation for the double list and add it to the canvas
        // Traverse the doubleList and create representations for each element
        int index = 0;
        for (Object item : simpleList) {
            Point position = calculatePositionForListItem(index);
            PrimitiveOrEnumRepresentation itemRepresentation = new PrimitiveOrEnumRepresentation(position, item, canvas);
            canvas.add(item, itemRepresentation);
            index++;

        }
        SortedCircularSimpleLinkedListWithBaseMaxOrderRepresentation sortedSingleList =
                new SortedCircularSimpleLinkedListWithBaseMaxOrderRepresentation(new Point(0, 0), simpleList, canvas); // Adjust the position as needed
        canvas.add(simpleList, sortedSingleList);
        System.out.println("Lista valores:" + simpleList);
        existingRepresentations.put(simpleList, sortedSingleList);
        canvas.representationWithInConnectorsByOwner.put(simpleList, sortedSingleList);
        refreshCanvas(canvas);
    }

    private void addSortedDoubleListRepresentation(ListaDuplaOrdenada<?> doubleList, MyCanvas canvas) {
        // Logic to create a visual representation for the double list and add it to the canvas
        // Traverse the doubleList and create representations for each element
        int index = 1;
        for (Object item : doubleList) {
            Point position = calculatePositionForListItem(index);
            PrimitiveOrEnumRepresentation itemRepresentation = new PrimitiveOrEnumRepresentation(position, item, canvas);
            canvas.add(item, itemRepresentation);
            index++;
        }
        SortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation sortedDoubleList =
                new SortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation(new Point(0, 0), doubleList, canvas); // Adjust the position as needed
        canvas.add(doubleList, sortedDoubleList);
        sortedDoubleList.update();
        System.out.println("Lista valores:" + doubleList);
        existingRepresentations.put(doubleList, sortedDoubleList);
        canvas.representationWithInConnectorsByOwner.put(doubleList, sortedDoubleList);
        refreshCanvas(canvas);
    }
    private ListaSimplesOrdenada<Object> convertHeapObjectToSortedSimpleList(HeapObject heapObject, Map<Long, HeapEntity> heapMap, Comparacao<Object> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator must not be null.");
        }
        ListaSimplesOrdenada<Object> sortedSingleList = new ListaSimplesOrdenada<>(comparator);
        Long baseRef = heapObject.fields.get("base").reference;
        HeapObject currentNode = (HeapObject) heapMap.get(baseRef);

        if (currentNode == null) {
            System.out.println("Sentinel node is null.");
            return sortedSingleList;
        }

        Long currentNodeRef = currentNode.fields.get("seguinte").reference;

        while (currentNodeRef != null && !currentNodeRef.equals(baseRef)) {
            currentNode = (HeapObject) heapMap.get(currentNodeRef);
            if (currentNode == null) {
                break;
            }
            Object element = currentNode.fields.get("elemento").getActualValue();
            sortedSingleList.inserir(element);
            currentNodeRef = currentNode.fields.get("seguinte").reference;
        }
        System.out.println("ISTO ESTA A DAR O QUE?::: " + sortedSingleList);
        return sortedSingleList;
    }

    private ListaDuplaOrdenada<Object> convertHeapObjectToSortedDoubleList(HeapObject heapObject, Map<Long, HeapEntity> heapMap, Comparacao<Object> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator must not be null.");
        }
        ListaDuplaOrdenada<Object> sortedDoubleList = new ListaDuplaOrdenada<>(comparator);
        Long baseRef = heapObject.fields.get("base").reference;
        HeapObject currentNode = (HeapObject) heapMap.get(baseRef);

        if (currentNode == null) {
            System.out.println("Sentinel node is null.");
            return sortedDoubleList;
        }

        Long currentNodeRef = currentNode.fields.get("seguinte").reference;


        while (currentNodeRef != null && !currentNodeRef.equals(baseRef)) {
            currentNode = (HeapObject) heapMap.get(currentNodeRef);
            if (currentNode == null) {
                break;
            }
            Object element = currentNode.fields.get("elemento").getActualValue();
            sortedDoubleList.inserir(element);
            currentNodeRef = currentNode.fields.get("seguinte").reference;
        }

        return sortedDoubleList;
    }



    private ListaDuplaNaoOrdenada<?> convertHeapObjectToDoubleList(HeapObject heapObject, Map<Long, HeapEntity> heapMap) {
        ListaDuplaNaoOrdenada<Object> doubleList = new ListaDuplaNaoOrdenada<>();
        Value headValue = heapObject.fields.get("base"); // Start from the 'base', not 'noFinal'
        Set<Long> visitedNodeIds = new HashSet<>(); // To detect cycles

        // The base node itself should not be added, so move to the first actual element
        headValue = ((HeapObject)heapMap.get(headValue.reference)).fields.get("seguinte");

        while (headValue != null && headValue.type == Value.Type.REFERENCE && headValue.reference != 0) {
            if (visitedNodeIds.contains(headValue.reference)) {
                System.out.println("Cycle detected or reached base node again. Terminating.");
                break; // Detect a cycle or the traversal has reached the base node again
            }
            visitedNodeIds.add(headValue.reference); // Track the visited nodes

            HeapEntity entity = heapMap.get(headValue.reference);
            if (!(entity instanceof HeapObject)) {
                break;
            }
            HeapObject currentNode = (HeapObject) entity;

            if (currentNode.fields.get("elemento")!=null) { // Make sure it's not the base sentinel node
                Value dataValue = currentNode.fields.get("elemento");
                if (dataValue != null) {
                    Object actualData = dataValue.getActualValue();
                    if (actualData != null) {
                        doubleList.inserir(actualData);
                        System.out.println("Inserted: " + actualData);
                    } else {
                        System.out.println("Actual Data is null for node with ID: " + currentNode.id);
                    }
                } else {
                    System.out.println("Data Value is null for node with ID: " + currentNode.id);
                }
            }

            headValue = currentNode.fields.get("seguinte"); // Move to the next node
        }
        return doubleList;
    }



    private ListaSimplesNaoOrdenada<Object> convertHeapObjectToSimpleList(HeapObject heapObject, Map<Long, HeapEntity> heapMap) {
        ListaSimplesNaoOrdenada<Object> simpleList = new ListaSimplesNaoOrdenada<>();
        Value headValue = heapObject.fields.get("base"); // Start from the 'base', not 'noFinal'
        Set<Long> visitedNodeIds = new HashSet<>(); // To detect cycles

        // The base node itself should not be added, so move to the first actual element
        headValue = ((HeapObject)heapMap.get(headValue.reference)).fields.get("seguinte");

        while (headValue != null && headValue.type == Value.Type.REFERENCE && headValue.reference != 0) {
            if (visitedNodeIds.contains(headValue.reference)) {
                System.out.println("Cycle detected or reached base node again. Terminating.");
                break; // Detect a cycle or the traversal has reached the base node again
            }
            visitedNodeIds.add(headValue.reference); // Track the visited nodes

            HeapEntity entity = heapMap.get(headValue.reference);
            if (!(entity instanceof HeapObject)) {
                break;
            }
            HeapObject currentNode = (HeapObject) entity;

            if (currentNode.fields.get("elemento")!=null) { // Make sure it's not the base sentinel node
                Value dataValue = currentNode.fields.get("elemento");
                if (dataValue != null) {
                    Object actualData = dataValue.getActualValue();
                    if (actualData != null) {
                        simpleList.inserir(actualData);
                        System.out.println("Inserted: " + actualData);
                    } else {
                        System.out.println("Actual Data is null for node with ID: " + currentNode.id);
                    }
                } else {
                    System.out.println("Data Value is null for node with ID: " + currentNode.id);
                }
            }

            headValue = currentNode.fields.get("seguinte"); // Move to the next node
        }
        return simpleList;
    }
    private ListaSimplesNaoOrdenada<Object> convertHeapObjectToListofLists(HeapObject heapObject, Map<Long, HeapEntity> heapMap, MyCanvas canvas,Comparacao<Object> comparator) {
        ListaSimplesNaoOrdenada<Object> list = new ListaSimplesNaoOrdenada<>();
        Value headValue = heapObject.fields.get("base"); // Start from the 'base'
        headValue = ((HeapObject)heapMap.get(headValue.reference)).fields.get("seguinte"); // Skip the sentinel node

        Set<Long> visitedNodeIds = new HashSet<>(); // To detect cycles

        while (headValue != null && headValue.type == Value.Type.REFERENCE && headValue.reference != 0) {
            if (visitedNodeIds.contains(headValue.reference)) {
                break; // Detect a cycle or the traversal has reached the base node again
            }
            visitedNodeIds.add(headValue.reference); // Track the visited nodes

            HeapObject currentNode = (HeapObject) heapMap.get(headValue.reference);
            Value dataValue = currentNode.fields.get("elemento");
            // Check if the dataValue is a reference to another list ( another HeapObject)
            if (dataValue != null && dataValue.type == Value.Type.REFERENCE) {
                HeapObject innerListObject = (HeapObject) heapMap.get(dataValue.reference);
                System.out.println("Esta a entrar no primeiro if: " + isSimpleList(innerListObject));
                System.out.println("Esta a entrar no segundo if: "+ innerListObject.label + "||||||||" + innerListObject.label.contains("ListaDuplaNaoOrdenada"));
                if (isSimpleList(innerListObject)) {
                    // It's a simple list, convert it to a simple list and add it to the current list
                    ListaSimplesNaoOrdenada<?> innerList = convertHeapObjectToSimpleList(innerListObject, heapMap);
                    list.inserir(innerList);
                } else if (innerListObject.label.contains("ListaDuplaNaoOrdenada")) {

                    ListaDuplaNaoOrdenada<?> innerList = convertHeapObjectToDoubleList(innerListObject, heapMap);
                    list.inserir(innerList);
                    //addDoubleListRepresentation(innerList, canvas);
                } else if (innerListObject.label.contains("ListaSimplesOrdenada")) {
                    ListaSimplesOrdenada<?> innerList = convertHeapObjectToSortedSimpleList(innerListObject, heapMap,comparator);
                    list.inserir(innerList);
                }else if (innerListObject.label.contains("ListaDuplaOrdenada")) {
                    ListaDuplaOrdenada<?> innerList = convertHeapObjectToSortedDoubleList(innerListObject, heapMap,comparator);
                    list.inserir(innerList);
                }
            } else if (dataValue != null) {
                // It's a direct value, add it to the current list
                Object actualData = dataValue.getActualValue();
                if (actualData != null) {
                    list.inserir(actualData);
                }
            }

            headValue = currentNode.fields.get("seguinte"); // Move to the next node
        }
        return list;
    }



    private void removeAllRepresentations() {
        representationWithInConnectorsByOwner.clear();
        connectionByOutConnector.clear();
        positionalGraphicElements.clear();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform transform = new AffineTransform(g2.getTransform());
        transform.scale(zoom, zoom);
        g2.setTransform(transform);

        for (RepresentationWithInConnectors representationWithInConnectors : representationWithInConnectorsByOwner.values()) {
            representationWithInConnectors.paint(g);
        }
        for (Connection connection : connectionByOutConnector.values()) {
            connection.paint(g);
        }
        for (PositionalGraphicElement positionalGraphicElement : positionalGraphicElements) {
            positionalGraphicElement.paint(g);
        }
//        g.drawString( mousePosition.x + "," + mousePosition.y + "   ZOOM=" + zoom + "   SIZE=" + getSize().width + "x" + getSize().height, 10, 10);
//        g.drawString("ActualFilename=->" + actualFilename + "<-" + mousePosition.x + "," + mousePosition.y + "   ZOOM=" + zoom + "   SIZE=" + getSize().width + "x" + getSize().height, 10, 10);
//        g.drawString("showVar=" + showVar, 10, 10);

    }

    public void remove(Connection connection) {
        final RepresentationWithInConnectors representationWithInConnectors = connection.getTarget();
        connectionByOutConnector.remove(connection.getSource());
        representationWithInConnectors.decrementReferenceCount();
    }

//    public WrapperWithValue getWrapperWithValue(Object value) {
////        for (Wrapper wrapper : representationWithInConnectorsByOwner.keySet()) {
//        for (Wrapper wrapper : wrapperById.values()) {
//            if (wrapper instanceof WrapperWithValue && ((WrapperWithValue<?>) wrapper).getValue() == value) {
//                return ((WrapperWithValue<?>) wrapper);
//            }
//        }
//        return null; //nunca deve acontecer
//    }


    public Object getIDSToolWindow() {
        return IDSToolWindow;
    }

    /*public String getFirstReferenceTo(Representation representation) {
       String firstVariableReference = getFirstVariableReferenceTo(representation);
       if (firstVariableReference != null) {
           return firstVariableReference;
       }
       //identificar todas as conexões cujo destino seja esta representação
       for (Connection connection : connectionByOutConnector.values()) {
           if (connection.getTarget() == representation) {
               //se o ponto de partida for um outConnector de uma ArrayReference
               Reference reference = connection.getSource().getReference();
               if (reference instanceof ArrayReference arrayReference) {
                   //guardar o index
                   int index = arrayReference.getIndex();
                   //chamar este método para a representação do array + [index]
                   return getFirstReferenceTo(getRepresentationWithInConnectors(
                           arrayReference.getArray())) + "[" + index + "]";
               } else {
                   //se o ponto de partida for um outConnector de uma FieldReference
                   FieldReference fieldReference = (FieldReference) reference;
                   //se for um nó de uma lista
                   WrapperWithValue<?> fieldOwnerObjectOfWrapper = fieldReference.getFieldOwnerObjectOfWrapper();
                   if (fieldOwnerObjectOfWrapper instanceof LinkedListNodeWrapper) {
                       LinkedListRepresentation linkedListRepresentation = (LinkedListRepresentation) getRepresentationWithInConnectors(((LinkedListNodeWrapper<?>) fieldOwnerObjectOfWrapper).getLinkedListOrSortedHashTableWrapper());
                       //chamar este método para a representação da lista + .get(i)
                       return getFirstReferenceTo(linkedListRepresentation)
                               + ".consultarPorIndice(" + (fieldOwnerObjectOfWrapper).getIndex() + ")";
                   }

                   Point sourceRepresentationPosition = getRepresentationWithInConnectors(fieldOwnerObjectOfWrapper).getPosition();
                   Representation movableRepresentation = getMovableRepresentation(sourceRepresentationPosition.x, sourceRepresentationPosition.y);

                   if (fieldOwnerObjectOfWrapper instanceof AssociationWrapper) {
                       //se for uma association de uma hashtable
                       //chamar este método para a representação da hashtable + .get(chave)
                       RepresentationWithInConnectors representationWithInConnectors = getRepresentationWithInConnectors(((AssociationWrapper) fieldOwnerObjectOfWrapper).getGeneralHashTableWrapper());
                       if (representationWithInConnectors instanceof HashTableRepresentation hashTableRepresentation) {

                           WrapperWithValue keyWrapper = (WrapperWithValue) ((Associacao) ((AssociationWrapper) fieldOwnerObjectOfWrapper).getValue()).getChave();
                           String firstReferenceToKeyWrapper = getFirstReferenceTo(keyWrapper);
                           String keyCode = firstReferenceToKeyWrapper.equals(Constants.UNKOWN_REF) ?
                                   Utils.getCode(keyWrapper.getValue()) :
                                   firstReferenceToKeyWrapper;

                           return getFirstReferenceTo(hashTableRepresentation)
                                   + ".consultar(" + keyCode + ")";
                       } else {
                           SortedHashTableRepresentation sortedHashTableRepresentation = (SortedHashTableRepresentation) representationWithInConnectors;
                           WrapperWithValue keyWrapper = (WrapperWithValue) ((Associacao) ((AssociationWrapper) fieldOwnerObjectOfWrapper).getValue()).getChave();
                           String firstReferenceToKeyWrapper = getFirstReferenceTo(keyWrapper);
                           String keyCode = firstReferenceToKeyWrapper.equals(Constants.UNKOWN_REF) ?
                                   Utils.getCode(keyWrapper.getValue()) :
                                   firstReferenceToKeyWrapper;

                           return getFirstReferenceTo(sortedHashTableRepresentation)
                                   + ".consultar(" + keyCode + ")";
                       }
                   }
                   //se for de uma ProjectEntity
                  //chamar este método para a representação da projectentity + .getXXXXX()
                        Point sourceRepresentationPosition = getRepresentationWithInConnectors(fieldOwnerObjectOfWrapper).getPosition();
                  Representation movableRepresentation = getRepresentationWithInConnectors(fieldOwnerObjectOfWrapper);
                    Field field = fieldReference.getField();
                    StringBuilder methodName = new StringBuilder(field.getName());
                    String initialCharUpperCase = String.valueOf(methodName.charAt(0)).toUpperCase();
                    methodName.setCharAt(0, initialCharUpperCase.charAt(0));
                   return getFirstReferenceTo(movableRepresentation)
                           + Utils.getGetterCall(fieldReference.getField());

               }
           }
       }
       return "[unkown_ref]";
   }*/

   /*public String getFirstReferenceTo(Wrapper wrapper) {
        return getFirstReferenceTo(representationWithInConnectorsByOwner.get(wrapper));
       RepresentationWithInConnectors representation = representationWithInConnectorsByOwner.get(wrapper.getId());
       return representation == null ? Constants.UNKOWN_REF : getFirstReferenceTo(representation);
   }*/

    public RepresentationWithInConnectors getRepresentationWithInConnectors(Object owner) {
        return representationWithInConnectorsByOwner.get(owner);
    }


    public void add(Object owner, RepresentationWithInConnectors representationWithInConnectors) {
//        representationWithInConnectorsByOwner.put(owner, representationWithInConnectors);
        representationWithInConnectorsByOwner.put(owner, representationWithInConnectors);
//        wrapperById.put(owner.getId(), owner);
        refresh();
    }


    public void refresh() {
        doSpatialDistribution();
        repaint();
    }
    @Override
    public void mousePressed(MouseEvent e) {
        for (RepresentationWithInConnectors rep : representationWithInConnectorsByOwner.values()) {
            if (rep.getContainer().contains(e.getPoint())) {
                draggedRepresentation = rep;
                Point repPosition = rep.getPosition();
                dragOffset = new Point(e.getX() - repPosition.x, e.getY() - repPosition.y);
                break;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (draggedRepresentation != null) {
            draggedRepresentation.setPosition(new Point(e.getX() - dragOffset.x, e.getY() - dragOffset.y));
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        draggedRepresentation = null;
        dragOffset = null;
    }
    public void updateMousePosition(MouseEvent e) {
        mousePosition.setLocation(e.getX(), e.getY());
        repaint();
    }

    public void doSpatialDistribution() {
        ArrayList<Representation> allRepresentations = new ArrayList<>(representationWithInConnectorsByOwner.values());
        boolean separated;
        do {
            separated = true;

            for (Representation representation : allRepresentations) {
                Vetor2D speed = new Vetor2D();
                Point position = representation.getPosition();
                //para não sair do canvas
                forceInsideCanvas(position);
                representation.setPosition(position);
                Point centerPosition = representation.getCenterPosition();
                Vetor2D centerVector = new Vetor2D(centerPosition.x, centerPosition.y);

                for (Representation representation2 : allRepresentations) {
                    if (representation == representation2 ||
                            representation.getContainer().contains(representation2.getContainer()) ||
                            representation2.getContainer().contains(representation.getContainer()) ||
                            !representation.intersects(representation2)) {
                        continue;
                    }

                    Point centerPosition2 = representation2.getCenterPosition();
                    int forcedDiffDelta = centerPosition.getX() == centerPosition2.getX() && centerPosition.getY() == centerPosition2.getY() ? 1 : 0;
                    Vetor2D centerVector2 = new Vetor2D(centerPosition2.x + forcedDiffDelta, centerPosition2.y + forcedDiffDelta);
                    Vetor2D diffVector = new Vetor2D(centerVector.getX() - centerVector2.getX(), centerVector.getY() - centerVector2.getY());
                    double sqDiff = diffVector.lengthSquared();

                    if (sqDiff > 0d) {
                        final double coefficientWeakeningRepulse = 1.0d;
                        double scale = coefficientWeakeningRepulse / sqDiff;
                        diffVector.normalize();
                        diffVector.scale(scale);
                        speed.add(diffVector);
                    }


                }

                if (speed.lengthSquared() > 0d) {
                    separated = false;
                    speed.normalize();
                    speed.scale(30d);

                    position.translate((int) speed.getX(), (int) speed.getY());
                    //para não sair do canvas
                    forceInsideCanvas(position);
                    representation.setPosition(position);
                }
            }
        } while (!separated);
    }

    private void forceInsideCanvas(Point position) {
        //para não sair do canvas
        if (position.x < 0) {
            position.x = 0;
        }
        if (position.y < 0) {
            position.y = 0;
        }
    }

    public boolean isCompactMode() {
        return compactMode;
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}







}
