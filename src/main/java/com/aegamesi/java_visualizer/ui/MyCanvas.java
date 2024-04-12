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
        ListaDuplaNaoOrdenada<Long> doubleLinkedList = new ListaDuplaNaoOrdenada<>();
        canvas.removeAllRepresentations();
        for (HeapEntity entity : trace.heap.values()) {
            System.out.println("Creating visual representation for entity: " + entity);

            // Check if the entity is a linked list or an object and create the corresponding representation
            if (entity instanceof HeapList) {
                System.out.println("Creating visual representation for HeapList");
                HeapList heapList = (HeapList) entity;
                ListaSimplesNaoOrdenada<?> linkedList = convertHeapListToLinkedList(heapList);
                int index = 0; // Just an example, adjust according to your position calculation
                for (Object item : linkedList) { // Assuming you can iterate over the list
                    Point position = calculatePositionForListItem(index); // You need to implement this method
                    System.out.println("Posicao do index: " + index);
                    PrimitiveOrEnumRepresentation itemRepresentation =
                            new PrimitiveOrEnumRepresentation(position, item, canvas);
                    canvas.add(item, itemRepresentation);
                    index++;
                }
                UnsortedCircularSimpleLinkedListWithBaseRepresentation linkedListRepresentation =
                        new UnsortedCircularSimpleLinkedListWithBaseRepresentation(
                                new Point(30, 30), // Adjust position as needed
                                linkedList,
                                canvas
                        );
                System.out.println("Lista valores:" + linkedList);
                linkedListRepresentation.updateIteratorPosition(-1);
                canvas.add(heapList, linkedListRepresentation);

            } else if (entity instanceof HeapObject) {
                System.out.println("Creating visual representation for HeapObject");
                HeapObject heapObject = (HeapObject) entity;
                Long newNumber = convertHeapObjectToDoubleLinkedList(heapObject);
                doubleLinkedList.inserir(newNumber);
                // Create the representation for doubleLinkedList outside of the loop
                UnsortedCircularDoubleLinkedListWithBaseRepresentation doubleLinkedListRepresentation =
                        new UnsortedCircularDoubleLinkedListWithBaseRepresentation(
                                new Point(30, 30), // Adjust position as needed
                                doubleLinkedList,
                                canvas
                        );

                System.out.println("No representations lista dupla have been added to the canvas." + doubleLinkedList);
                // Update the iterator position
                doubleLinkedListRepresentation.updateIteratorPosition(-1);

                // Add the doubleLinkedList representation to the canvas
                canvas.add(doubleLinkedList, doubleLinkedListRepresentation);
                doubleLinkedListRepresentation.update();
            }
            // Add similar logic for other types of HeapEntities (if any)
        }

        if (canvas.representationWithInConnectorsByOwner.isEmpty()) {
            System.out.println("No representations lista dupla have been added to the canvas.");
        } else {
            System.out.println("Representations lista dupla added to canvas: " + canvas.representationWithInConnectorsByOwner.size());
        }
        refreshCanvas(canvas);
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

    private static Long convertHeapObjectToDoubleLinkedList(HeapObject heapObject) {
        Long doubleLinkedList = null;

        // Assuming fields in HeapObject contain integer values
        for (Value value : heapObject.fields.values()) {
            if (value.type == Value.Type.LONG) {
                doubleLinkedList = value.longValue;
            }
            // You can add additional checks and handling for other value types if needed
        }

        return doubleLinkedList;
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
