package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists;



import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.SortedCircularSimpleLinkedListAlsoWithBaseRepresentation;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ordenadas.estruturas.ListaSimplesOrdenada;

import javax.swing.*;
import java.awt.*;

public class SortedCircularSimpleLinkedListWithBaseMaxOrderRepresentation extends
        SortedCircularSimpleLinkedListAlsoWithBaseRepresentation<ListaSimplesOrdenada, ListaSimplesOrdenada.No>{
    private static final long serialVersionUID = 1L;


    public SortedCircularSimpleLinkedListWithBaseMaxOrderRepresentation(Point position, ListaSimplesOrdenada owner, MyCanvas canvas) {
        super(position, owner, canvas);
    }

    @Override
    public void init() {
        super.init();
        container.setInnerCellSpacing(6);
    }

    @Override
    public void add(RepresentationWithInConnectors representationWithInConnectors) {
            super.add(representationWithInConnectors);
    }

    @Override
    protected String getBaseNodeIndexText() {
//        return '\u221e' + "  " + super.getBaseNodeIndexText() + "    ";
//        return "   " + super.getBaseNodeIndexText() + "     ";
        return " " + super.getBaseNodeIndexText() + "   ";
    }

}
