package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaSimplesNaoOrdenada;
import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.ZConnection;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.FieldReference;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;
import com.aegamesi.java_visualizer.utils.Utils;

import java.awt.*;

public class UnsortedCircularSimpleLinkedListWithBaseRepresentation extends
        CircularSimpleLinkedListWithBaseRepresentation<ListaSimplesNaoOrdenada, ListaSimplesNaoOrdenada.No> implements UnsortedListRepresentation {
    private static final long serialVersionUID = 1L;


    private FieldReference tailFieldReference;

    public UnsortedCircularSimpleLinkedListWithBaseRepresentation(Point position, ListaSimplesNaoOrdenada owner, MyCanvas myCanvas) {
        super(position, owner, myCanvas);
    }

    @Override
    protected void createTailOrComparatorReferences() {
        tailFieldReference = new FieldReference(new Dimension(14, 14), owner, Utils.getField(owner, ConstantsIDS.TAIL_NODE), Location.CENTER, false);
        leftContainer.add(tailFieldReference, Location.RIGHT);
    }

    public void update() {
        super.update();
        //create the connection from tail field reference to tail node
        ListaSimplesNaoOrdenada.No node = (ListaSimplesNaoOrdenada.No) tailFieldReference.getFieldValue();
        if (node == null) {
            return;
        }
        addNewConnection(new ZConnection(tailFieldReference.getOutConnector(), nodeRepresentationByOwner.get(node), Color.RED, 20, 14));
    }


    @Override
    public void add(RepresentationWithInConnectors representationWithInConnectors) {
        super.add(representationWithInConnectors);
    }
}
