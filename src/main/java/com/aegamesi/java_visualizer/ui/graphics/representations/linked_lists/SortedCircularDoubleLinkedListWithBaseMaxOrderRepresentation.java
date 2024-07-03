package ui.graphics.representations.linked_lists;



import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ordenadas.estruturas.ListaDuplaOrdenada;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.StraightConnection;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.FieldReference;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.CircularDoubleLinkedListWithBaseRepresentation;
import com.aegamesi.java_visualizer.ui.ConstantsIDS;

import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.SortedListRepresentation;
import com.aegamesi.java_visualizer.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

public class SortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation extends
        CircularDoubleLinkedListWithBaseRepresentation<ListaDuplaOrdenada, ListaDuplaOrdenada.No> implements SortedListRepresentation {
    private static final long serialVersionUID = 1L;


    private FieldReference comparatorFieldReference;

    public SortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation(Point position, ListaDuplaOrdenada owner, MyCanvas canvas) {
        super(position, owner, canvas);
    }

    @Override
    protected void createComparatorReference() {
        ListaDuplaOrdenada<?> ownerValue = owner;
        //JOptionPane.showMessageDialog(null, "Aqui versao 2\n" + ownerValue);
        comparatorFieldReference = new FieldReference(new Dimension(14, 14), owner, Utils.getField(ownerValue, ConstantsIDS.COMPARATOR_BY_ORDER), Location.CENTER, false);
        leftContainer.add(comparatorFieldReference, Location.RIGHT);
//        canvas.add(new StraightConnection(comparatorFieldReference.getOutConnector(), canvas.getRepresentationWithInConnectors(((ComparatorWrapper) ownerValue.getComparatorByOrder())), Color.RED));
    }

    @Override
    public void init() {
        super.init();
        container.setTopCellSpacing(9);
        container.setInnerCellSpacing(8);
    }

    @Override
    public void update() {
        super.update();
        StraightConnection comparatorConnection = new StraightConnection(comparatorFieldReference.getOutConnector(), myCanvas.getRepresentationWithInConnectors((owner.getComparador())), Color.RED);

        connections.add(comparatorConnection);
        myCanvas.add(comparatorConnection);
    }

    @Override
    public void add(RepresentationWithInConnectors representationWithInConnectors) {
            super.add(representationWithInConnectors);
    }

    @Override
    protected String getBaseNodeIndexText() {
//        return '\u221e' + "   " + super.getBaseNodeIndexText() + "     ";
        return " " + super.getBaseNodeIndexText() + "   ";
    }
}

