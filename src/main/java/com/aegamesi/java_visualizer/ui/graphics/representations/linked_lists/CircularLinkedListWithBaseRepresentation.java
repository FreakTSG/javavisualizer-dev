package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ColecaoIteravelLinear;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.FieldReference;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.nodes.NodeRepresentation;

import java.awt.*;

public class CircularLinkedListWithBaseRepresentation<TLinkedList extends ColecaoIteravelLinear, TNodeRepresentation extends NodeRepresentation>
        extends LinkedListRepresentation<TLinkedList, TNodeRepresentation> {
    private static final long serialVersionUID = 1L;


    protected FieldReference baseFieldReference;

    public CircularLinkedListWithBaseRepresentation(Point position, TLinkedList owner, MyCanvas myCanvas, boolean horizontal) {
        super(position, owner, myCanvas, horizontal);
    }


    protected String getBaseNodeIndexText() {
        return "-";
    }
}


