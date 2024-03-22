package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.aed.colecoes.Colecao;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.Connection;

import java.awt.*;
import java.util.ArrayList;

public abstract class CollectionRepresentation<TDataStructureWithComponentClass extends Colecao> extends GeneralGenericRepresentation<TDataStructureWithComponentClass> {
    private static final long serialVersionUID = 1L;


    protected ArrayList<Connection> connections;

    public CollectionRepresentation(Point position, TDataStructureWithComponentClass owner, MyCanvas myCanvas) {
        super(position, owner, myCanvas);
    }

    public CollectionRepresentation(Point position, TDataStructureWithComponentClass owner, MyCanvas myCanvas, boolean horizontal) {
        super(position, owner, myCanvas, horizontal);
    }

    @Override
    public void dispose() {
        super.dispose();
        for (Connection connection : connections) {
            myCanvas.remove(connection);
        }
    }

    public void add(RepresentationWithInConnectors representationWithInConnectors) {
        rebuild();
    }

    @Override
    public void init() {
        connections = new ArrayList<>();
    }

    public void rebuild() {
        for (Connection connection : connections) {
            myCanvas.remove(connection);
        }
//        connections.clear();
        update();
        myCanvas.repaint();
    }

    protected void update() {
        connections.clear();
    }

    public void addNewConnection(Connection connection) {
        connections.add(connection);
        myCanvas.add(connection);
    }


}
