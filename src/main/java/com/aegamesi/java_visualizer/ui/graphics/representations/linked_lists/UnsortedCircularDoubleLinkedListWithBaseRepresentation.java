package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaDuplaNaoOrdenada;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.FieldReference;
import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;

import java.awt.*;

public class UnsortedCircularDoubleLinkedListWithBaseRepresentation extends
        CircularDoubleLinkedListWithBaseRepresentation<ListaDuplaNaoOrdenada, ListaDuplaNaoOrdenada.No> implements UnsortedListRepresentation {
    private static final long serialVersionUID = 1L;

    private int iteratorPosition = -1;

    private FieldReference tailFieldReference;

    public UnsortedCircularDoubleLinkedListWithBaseRepresentation(Point position, ListaDuplaNaoOrdenada owner, MyCanvas myCanvas) {
        super(position, owner, myCanvas);
    }

    public void updateIteratorPosition(int position) {
        // Add a method to update the iterator's position
        this.iteratorPosition = position;
        // Invoke a repaint to update the visualization
        myCanvas.repaint();
    }

    @Override
    public void add(RepresentationWithInConnectors representationWithInConnectors) {
        super.add(representationWithInConnectors);
    }

}
