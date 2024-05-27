package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.Resources;
import com.aegamesi.java_visualizer.ui.graphics.Connection;
import com.aegamesi.java_visualizer.ui.graphics.InConnector;
import com.aegamesi.java_visualizer.ui.graphics.OutConnector;
import com.aegamesi.java_visualizer.ui.graphics.StraightConnection;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.ContainerWithoutInConnectors;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.FieldReference;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.ImageElement;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.NormalTextElement;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.utils.Utils;

import java.awt.*;
import java.util.ArrayList;

public class ComparatorRepresentation<TProjectEntityOrPrimitive extends Object> extends GeneralGenericRepresentation<TProjectEntityOrPrimitive> {
    private static final long serialVersionUID = 1L;

    protected ArrayList<SortedFieldItem> outSortedFieldItems;
    protected ArrayList<SortedFieldItem> inSortedFieldComponentItems;
    protected int level;

    protected ContainerWithoutInConnectors leftContainer;
    protected ArrayList<Connection> connections;

    public ComparatorRepresentation(Point position, TProjectEntityOrPrimitive owner, MyCanvas myCanvas) {
        this(position, owner, myCanvas, false);
    }

    public ComparatorRepresentation(Point position,TProjectEntityOrPrimitive owner, MyCanvas myCanvas, boolean horizontal) {
        super(position, owner, myCanvas, horizontal);
    }

    public void init() {
        level = 1;
        container.setHorizontal(false);

        outSortedFieldItems = new ArrayList<>();
        inSortedFieldComponentItems = new ArrayList<>();
        Utils.createFieldItem(() -> new SortedFieldItem(), outSortedFieldItems, "", owner.getClass(), 0, level);
//        update();
    }

    public void update() {
        container.removeAllGraphicElements();

        for (SortedFieldItem inSortedFieldItem : inSortedFieldComponentItems) {
            final ContainerWithoutInConnectors orderAndFieldContainer = new ContainerWithoutInConnectors(new Point(), new Dimension(), true);
            orderAndFieldContainer.setOutterCellSpacing(0);
            orderAndFieldContainer.setBorderShown(false);
            orderAndFieldContainer.add(new ImageElement(new Point(), new Dimension(14, 14), inSortedFieldItem.isAscending() ? Resources.INSTANCE.ascendingIcon : Resources.INSTANCE.descendingIcon));
            final String fieldName = inSortedFieldItem.getPrefix() + inSortedFieldItem.getField().getName();
            orderAndFieldContainer.add(new NormalTextElement(fieldName, ConstantsIDS.FONT_SIZE_TEXT, inSortedFieldItem.getField().getType().getSimpleName(), new Color(153, 13, 75, 173)));
            container.add(orderAndFieldContainer, Location.LEFT);
        }

        container.update();
        myCanvas.repaint();
    }

    public ArrayList<SortedFieldItem> getOutSortedFieldItems() {
        return outSortedFieldItems;
    }

    public ArrayList<SortedFieldItem> getInSortedFieldItems() {
        return inSortedFieldComponentItems;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }





}


