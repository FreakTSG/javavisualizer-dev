package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ColecaoIteravelLinear;
import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.FieldReference;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
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

    /*@Override
    public void update() {
        super.update();
        StraightConnection comparatorConnection = new StraightConnection(comparatorFieldReference.getOutConnector(), myCanvas.getRepresentationWithInConnectors( owner.getValue().getComparador()), Color.RED);
        connections.add(comparatorConnection);
        myCanvas.add(comparatorConnection);
    }*/




}
