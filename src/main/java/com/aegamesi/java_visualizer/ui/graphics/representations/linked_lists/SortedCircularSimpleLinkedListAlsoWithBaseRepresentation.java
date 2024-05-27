package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ColecaoIteravelLinear;
import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.Connection;
import com.aegamesi.java_visualizer.ui.graphics.InConnector;
import com.aegamesi.java_visualizer.ui.graphics.OutConnector;
import com.aegamesi.java_visualizer.ui.graphics.StraightConnection;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.FieldReference;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;
import com.aegamesi.java_visualizer.utils.Utils;

import javax.swing.*;
import java.awt.*;

public abstract class SortedCircularSimpleLinkedListAlsoWithBaseRepresentation<TSortedLinkedListClass extends ColecaoIteravelLinear,TNode> extends
        CircularSimpleLinkedListWithBaseRepresentation<TSortedLinkedListClass, TNode> implements SortedListRepresentation {
    private static final long serialVersionUID = 1L;


    protected FieldReference comparatorFieldReference;

    public SortedCircularSimpleLinkedListAlsoWithBaseRepresentation(Point position, TSortedLinkedListClass owner, MyCanvas canvas) {
        super(position, owner, canvas);
    }

    @Override
    protected void createTailOrComparatorReferences() {
        comparatorFieldReference = new FieldReference(new Dimension(14, 14), owner, Utils.getField(owner, ConstantsIDS.COMPARATOR_BY_ORDER), Location.CENTER, false);
        leftContainer.add(comparatorFieldReference, Location.RIGHT);
    }

    @Override
    public void update() {
        super.update();

        // Ensure that the target representation exists
        RepresentationWithInConnectors<?> targetRepresentation = myCanvas.getRepresentationWithInConnectors(owner.getComparador());
        if (targetRepresentation == null) {
            System.err.println("Target representation for comparator is null.");
            return;
        }

        // Create and add the connection
        try {
            OutConnector outConnector = comparatorFieldReference.getOutConnector();
            if (outConnector == null) {
                System.err.println("OutConnector for comparatorFieldReference is null.");
                return;
            }

            // We will connect to the first InConnector of the target representation
            InConnector inConnector = targetRepresentation.getFirstInConnector();
            if (inConnector == null) {
                System.err.println("No InConnector found for target representation.");
                return;
            }

            // Create a StraightConnection using OutConnector and InConnector
            Connection comparatorConnection = new StraightConnection(outConnector, targetRepresentation, Color.RED);
            connections.add(comparatorConnection);
            myCanvas.add(comparatorConnection);
        } catch (Exception e) {
            System.err.println("Error creating connection: " + e.getMessage());
        }
    }




}
