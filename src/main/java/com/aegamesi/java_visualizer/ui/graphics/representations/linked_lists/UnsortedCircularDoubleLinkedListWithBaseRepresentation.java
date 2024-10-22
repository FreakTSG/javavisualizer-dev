package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaDuplaNaoOrdenada;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaSimplesNaoOrdenada;
import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.ZConnection;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.FieldReference;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;
import com.aegamesi.java_visualizer.utils.Utils;

import java.awt.*;

public class UnsortedCircularDoubleLinkedListWithBaseRepresentation extends
        CircularDoubleLinkedListWithBaseRepresentation<ListaDuplaNaoOrdenada, ListaDuplaNaoOrdenada.No> implements UnsortedListRepresentation {
    private static final long serialVersionUID = 1L;

    private FieldReference tailFieldReference;

    public UnsortedCircularDoubleLinkedListWithBaseRepresentation(Point position, ListaDuplaNaoOrdenada owner, MyCanvas myCanvas) {
        super(position, owner, myCanvas);
    }

    @Override
    public void init() {
        super.init();
        container.setTopCellSpacing(15);
    }


    @Override
    public void add(RepresentationWithInConnectors representationWithInConnectors) {
        super.add(representationWithInConnectors);
    }

}

