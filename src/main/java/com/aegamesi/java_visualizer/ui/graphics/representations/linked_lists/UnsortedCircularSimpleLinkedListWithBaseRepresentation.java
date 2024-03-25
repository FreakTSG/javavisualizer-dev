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

import static java.time.zone.ZoneRulesProvider.refresh;

public class UnsortedCircularSimpleLinkedListWithBaseRepresentation extends
        CircularSimpleLinkedListWithBaseRepresentation<ListaSimplesNaoOrdenada, ListaSimplesNaoOrdenada.No> implements UnsortedListRepresentation {
    private static final long serialVersionUID = 1L;
    private int iteratorPosition = -1;
    private Point startPosition = new Point(0, 57); // adjust these values as needed
    private int nodeDistance = 50; // adjust this value as needed


    private FieldReference tailFieldReference;

    public UnsortedCircularSimpleLinkedListWithBaseRepresentation(Point position, ListaSimplesNaoOrdenada owner, MyCanvas myCanvas) {
        super(position, owner, myCanvas);
    }

    @Override
    protected void createTailOrComparatorReferences() {
        tailFieldReference = new FieldReference(new Dimension(14, 14), owner, Utils.getField(owner, ConstantsIDS.TAIL_NODE), Location.CENTER, false);
        leftContainer.add(tailFieldReference, Location.RIGHT);
    }


    public void updateIteratorPosition(int position) {
        // Add a method to update the iterator's position
        this.iteratorPosition = position;
        // Invoke a repaint to update the visualization
        myCanvas.repaint();
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g); // Paint the list

        // Additional logic to draw the iterator indicator
        if(iteratorPosition >= 0) {
            // Calculate the graphical position of the node at iteratorPosition
            Point nodePos = calculateNodePosition(iteratorPosition);
            // Now pass the calculated Point to drawIteratorIndicator
            drawIteratorIndicator(g, nodePos);
        }
    }

    private void drawIteratorIndicator(Graphics g, Point pos) {
        // Example: Draw a small circle next to the current node
        g.setColor(Color.RED); // Iterator indicator color
        int radius = 10; // Small circle radius
        g.fillOval(pos.x + radius * 2, pos.y - radius / 2, radius, radius);
    }

    // Assuming this method calculates the graphical position of a node by its index
    private Point calculateNodePosition(int index) {
        int x = startPosition.x + index * nodeDistance; // startPosition is where the first node is drawn, nodeDistance is the space between nodes
        int y = startPosition.y; // Assuming all nodes are drawn at the same vertical position
        return new Point(x, y);
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
