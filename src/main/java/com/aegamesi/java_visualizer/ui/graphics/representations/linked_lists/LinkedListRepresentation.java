package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ColecaoIteravelLinear;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.ContainerWithoutInConnectors;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.NormalTextElement;
import com.aegamesi.java_visualizer.ui.graphics.representations.CollectionRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.nodes.NodeRepresentation;

import java.awt.*;
import java.util.HashMap;

public abstract class LinkedListRepresentation<TLinkedList extends ColecaoIteravelLinear, TNodeRepresentation extends NodeRepresentation>
        extends CollectionRepresentation<TLinkedList> {
    private static final long serialVersionUID = 1L;

    protected ContainerWithoutInConnectors leftContainer;
    protected int actualIndex;
    protected NormalTextElement numberOfElementsReference;
    protected HashMap<Object, TNodeRepresentation> nodeRepresentationByOwner;

    public LinkedListRepresentation(Point position, TLinkedList owner, MyCanvas myCanvas) {
        this(position, owner, myCanvas, true);
    }

    public LinkedListRepresentation(Point position, TLinkedList owner, MyCanvas myCanvas, boolean horizontal) {
        super(position, owner, myCanvas, horizontal);
    }

    @Override
    public void init() {
        super.init();
        nodeRepresentationByOwner = new HashMap<>();
//        container.setCellSpacing(0);
        leftContainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), false);
    }

    @Override
    protected void update() {
        super.update();
        nodeRepresentationByOwner.clear();
        numberOfElementsReference.setText(String.valueOf(owner.getNumeroElementos()));
    }

    @Override
    public void rebuild() {
        container.removeAllButFirstGraphicElements();
        super.rebuild();
    }

}