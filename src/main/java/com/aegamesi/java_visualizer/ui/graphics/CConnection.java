package com.aegamesi.java_visualizer.ui.graphics;

import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;

import java.awt.*;

public class CConnection<TOutConnector extends OutConnector, TRepresentationObject extends RepresentationWithInConnectors> extends OrthogonalConnection<TOutConnector, TRepresentationObject> {
    private static final long serialVersionUID = 1L;


    public CConnection(TOutConnector source, TRepresentationObject target, Color borderColor, int firstDistance, int secondDistance, boolean connectionToHimself) {
        super(source, target, borderColor, -firstDistance, secondDistance, connectionToHimself);
    }
}
