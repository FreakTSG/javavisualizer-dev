package com.aegamesi.java_visualizer.ui;

import com.aegamesi.java_visualizer.ui.graphics.Connection;
import com.aegamesi.java_visualizer.ui.graphics.OutConnector;
import com.aegamesi.java_visualizer.ui.graphics.PositionalGraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.representations.DefaultRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.Representation;
import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;
import com.aegamesi.java_visualizer.utils.Vetor2D;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MyCanvas extends JPanel {

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


    public MyCanvas(IDSToolWindow IDSToolWindow) {
        MyCanvas.IDSToolWindow = IDSToolWindow;
        compactMode = false;

        positionalGraphicElements = new LinkedList<>();
        representationWithInConnectorsByOwner = new HashMap<>();
//        wrapperById = new HashMap<>();
        connectionByOutConnector = new HashMap<>();

        setFocusable(true);

        zoom = 1;
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


}
