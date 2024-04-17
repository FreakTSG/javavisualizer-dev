package com.aegamesi.java_visualizer.ui;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaDuplaNaoOrdenada;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaSimplesNaoOrdenada;
import com.aegamesi.java_visualizer.model.*;
import com.aegamesi.java_visualizer.ui.graphics.Connection;
import com.aegamesi.java_visualizer.ui.graphics.OutConnector;
import com.aegamesi.java_visualizer.ui.graphics.PositionalGraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.representations.DefaultRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.PrimitiveOrEnumRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.Representation;
import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.UnsortedCircularDoubleLinkedListWithBaseRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.UnsortedCircularSimpleLinkedListWithBaseRepresentation;
import com.aegamesi.java_visualizer.utils.Vetor2D;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


import static com.aegamesi.java_visualizer.model.HeapObject.isDoubleList;
import static com.aegamesi.java_visualizer.model.HeapObject.isSimpleList;
import static com.aegamesi.java_visualizer.model.Value.Type.LONG;
import static com.aegamesi.java_visualizer.model.Value.Type.STRING;


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

        //removeAllRepresentations();

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

    /*public static void createVisualRepresentations(ExecutionTrace trace, MyCanvas canvas) {
        for (HeapEntity entity : trace.heap.values()) {
            // Check if the entity is a linked list and create the corresponding representation
            System.out.println("What is going on here: " + entity.getClass().getName() + "\n");
            System.out.println("What is going on here: " + HeapList.class.getName() + "\n");
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

            } else if (entity instanceof HeapObject) {
                HeapObject heapList = (HeapObject) entity;


                ListaDuplaNaoOrdenada<?> lista2 = convertHeapListToDoubleLinkedList(heapList);

                // The below assumes you have a constructor for UnsortedCircularSimpleLinkedListWithBaseRepresentation
                // that takes these parameters.

                UnsortedCircularDoubleLinkedListWithBaseRepresentation listRepresentation2 =
                        new UnsortedCircularDoubleLinkedListWithBaseRepresentation(
                                new Point(30, 30), // Adjust position as needed
                                lista2, // You might need to cast or transform this
                                canvas
                        );

                // Update the representation with any specific details, such as the iterator's position
                // Assuming you have such a method on your representation class

                listRepresentation2.updateIteratorPosition(-1);

                canvas.add(heapList, listRepresentation2);

                System.out.println("What is going on here: " + lista2 + "\n");
            }
            // Add similar logic for other types of HeapEntities (if any)
        }

        if (canvas.representationWithInConnectorsByOwner.isEmpty()) {
            System.out.println("No representations have been added to the canvas.");
        } else {
            System.out.println("Representations added to canvas: " + canvas.representationWithInConnectorsByOwner.size());
        }
        canvas.repaint();


        // Refresh canvas to display the new visual elements
        canvas.revalidate();
        canvas.repaint();
    }*/

    public static void createVisualRepresentations(ExecutionTrace trace, MyCanvas canvas) {
        System.out.println("Creating visual representations for the trace");
        Map<Long, HeapEntity> heapMap = buildHeapMap(trace);
        canvas.removeAllRepresentations();
        for (HeapEntity entity : trace.heap.values()) {
            System.out.println("Creating visual representation for entity: " + entity);

            // Check if the entity is a list of lists (HeapList) and handle it separately
            if (entity instanceof HeapList) {
                System.out.println("Processing a HeapList which might be a list of lists.");
                HeapList heapList = (HeapList) entity;

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
                }


               //if (isListOfLists(heapList, heapMap)) {
               //    System.out.println("The HeapList is a list of lists.");
               //    representListOfLists(heapList, canvas, heapMap);
               //}
                representListOfLists(heapList, canvas, heapMap);


            } else if (entity instanceof HeapObject) {
                System.out.println("Entrei nos heapObjects");
                HeapObject heapObject = (HeapObject) entity;
                determineAndRepresentHeapObject(heapObject, canvas,heapMap);
            }

        }

        if (canvas.representationWithInConnectorsByOwner.isEmpty()) {
            System.out.println("No representations lista have been added to the canvas.");
        } else {
            System.out.println("Representations lista added to canvas: " + canvas.representationWithInConnectorsByOwner.size());
        }
        refreshCanvas(canvas);
    }
    private static Map<Long, HeapEntity> buildHeapMap(ExecutionTrace trace) {
        Map<Long, HeapEntity> map = new HashMap<>();
        for (HeapEntity heapEntity : trace.heap.values()) {
            map.put(heapEntity.id, heapEntity);  // Correctly using HeapEntity
        }
        return map;
    }
    private static Point calculatePositionForListItem(int index) {
        // Implement your logic to calculate the position based on the index
        // For example:

        int x = START_X + index * HORIZONTAL_SPACING;
        int y = START_Y + VERTICAL_SPACING;
        return new Point(x, y);
    }
    private static void refreshCanvas(MyCanvas canvas) {
        canvas.invalidate();
        canvas.revalidate();
        canvas.repaint();
    }

    private static void determineAndRepresentHeapObject(HeapObject heapObject, MyCanvas canvas, Map<Long, HeapEntity> heapMap) {
        if (isSimpleList(heapObject)) {
            ListaSimplesNaoOrdenada<?> simpleList = convertHeapObjectToSimpleList(heapObject, heapMap);
            addSimpleListRepresentation(simpleList, canvas);
            System.out.println("Simple list represented.");
        } else if (isDoubleList(heapObject)) {
            ListaDuplaNaoOrdenada<?> doubleList = convertHeapObjectToDoubleList(heapObject, heapMap);
            addDoubleListRepresentation(doubleList, canvas);
            System.out.println("Double list represented.");
        }




    }

    private static void addSimpleListRepresentation(ListaSimplesNaoOrdenada<?> simpleList, MyCanvas canvas) {
        // Logic to create a visual representation for the simple list and add it to the canvas
        // This might involve creating new GraphicElement objects and adding them to the canvas
        int index = 0;
        for (Object item : simpleList) {
            Point position = calculatePositionForListItem(index);
            PrimitiveOrEnumRepresentation itemRepresentation = new PrimitiveOrEnumRepresentation(position, item, canvas);
            canvas.add(item, itemRepresentation);
            index++;
        }
         UnsortedCircularSimpleLinkedListWithBaseRepresentation representation =
              new UnsortedCircularSimpleLinkedListWithBaseRepresentation(new Point(START_X, START_Y), simpleList, canvas);
         canvas.add(simpleList, representation);
        System.out.println("Lista valores:" + simpleList);

        refreshCanvas(canvas);

    }

    private static void addDoubleListRepresentation(ListaDuplaNaoOrdenada<?> doubleList, MyCanvas canvas) {
        // Logic to create a visual representation for the double list and add it to the canvas
        // This might involve creating new GraphicElement objects and adding them to the canvas

         UnsortedCircularDoubleLinkedListWithBaseRepresentation representation =
              new UnsortedCircularDoubleLinkedListWithBaseRepresentation(new Point(START_X, START_Y), doubleList, canvas);
            canvas.add(doubleList, representation);

    }

    private static ListaDuplaNaoOrdenada<?> convertHeapObjectToDoubleList(HeapObject heapObject,Map<Long, HeapEntity> heapMap) {
        // Conversion logic from HeapObject to ListaDuplaNaoOrdenada
        // This is just placeholder logic; you need to implement the actual conversion based on your application's needs.
        ListaDuplaNaoOrdenada<?> list = new ListaDuplaNaoOrdenada<>();
        // ...populate the list based on the heapObject's data
        return list;
    }

    private static boolean isListOfLists(HeapList heapList, Map<Long, HeapEntity> heapMap) {
        // This method checks if the elements of the HeapList are themselves lists
        for (Value value : heapList.items) {
            if (value.type == Value.Type.REFERENCE) {
                HeapEntity possibleList = heapMap.get(value.reference);
                if (possibleList instanceof HeapList) {
                    return true;
                }
            }
        }
        return false;
    }

    private static ListaSimplesNaoOrdenada<Object> convertHeapObjectToSimpleList(HeapObject heapObject, Map<Long, HeapEntity> heapMap) {
        ListaSimplesNaoOrdenada<Object> list = new ListaSimplesNaoOrdenada<>();
        Value headValue = heapObject.fields.get("head");

        while (headValue != null && headValue.type == Value.Type.REFERENCE && headValue.reference != 0) {
            HeapEntity entity = heapMap.get(headValue.reference); // Retrieve as HeapEntity
            if (!(entity instanceof HeapObject)) {
                break; // Break if it's not a HeapObject
            }
            HeapObject currentNode = (HeapObject) entity;

            Value dataValue = currentNode.fields.get("data");
            if (dataValue != null) {
                Object actualData = dataValue.getActualValue(); // Convert Value to actual data
                list.inserir(actualData);
                System.out.println("Inserted: " + actualData);
            }

            headValue = currentNode.fields.get("next"); // Move to the next node
        }

        return list;
    }
    private static ListaSimplesNaoOrdenada<Object> convertHeapListToSimpleList(HeapList heapList, Map<Long, HeapEntity> heapMap) {
        ListaSimplesNaoOrdenada<Object> list = new ListaSimplesNaoOrdenada<>();
        System.out.println("Converting HeapList with items count: " + heapList.items.size());  // Debugging output

        for (Value value : heapList.items) {
            System.out.println("Processing Value: " + value);  // Debugging output

            if (value.type == Value.Type.REFERENCE) {
                HeapEntity entity = heapMap.get(value.reference);
                System.out.println("Retrieved entity from map: " + entity);  // Debugging output

                if (entity instanceof HeapObject) {
                    // Convert the referenced HeapObject to a SimpleList and add it as an item in the current list
                    ListaSimplesNaoOrdenada<Object> subList = convertHeapObjectToSimpleList((HeapObject) entity, heapMap);
                    System.out.println("Converted subList: " + subList);  // Debugging output

                    list.inserir(subList);
                }
            }
        }

        System.out.println("Final converted list: " + list);  // Debugging output
        return list;
    }
    private static ListaSimplesNaoOrdenada<Integer> convertHeapListToLinkedList(HeapList heapList) {
        ListaSimplesNaoOrdenada<Integer> lista = new ListaSimplesNaoOrdenada<>();

        // This assumes that HeapList holds values as Value objects representing integers
        for (Value item : heapList.items) {
            if (item.type == Value.Type.LONG) {
                lista.inserir((int) item.longValue);
            }
            // If your list expects other types, you can add additional checks here
        }

        return lista;
    }

    private static void representListOfLists(HeapList heapList, MyCanvas canvas, Map<Long, HeapEntity> heapMap) {
        // Convert the entire HeapList to a single ListaSimplesNaoOrdenada
        ListaSimplesNaoOrdenada<Object> simpleList = convertHeapListToSimpleList(heapList, heapMap);
        addSimpleListRepresentation(simpleList, canvas);  // Represent this list as a whole

        // Optionally, iterate over individual lists if needed for further granular representations

        System.out.println("List of lists represented.");
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
