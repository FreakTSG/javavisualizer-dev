package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.Resources;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.ContainerWithoutInConnectors;
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
            orderAndFieldContainer.add(new NormalTextElement(fieldName, ConstantsIDS.FONT_SIZE_TEXT, inSortedFieldItem.getField().getType().getSimpleName(), new Color(228, 152, 185, 173)));
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

    //@Override
   // public ButtonBar getButtonBar(Point position) {
    //    return IDSToolWindow.getButtonBar(IDSToolWindow.COMPARATOR_BUTTON_BAR);
    //}

    public String getCreationCode() {
        final StringBuilder sb = new StringBuilder("new ");
//        final String genericTypeName = owner.getValueTypeName();
        final String genericTypeName = owner.getClass().getSimpleName();

        sb.append("\t@Override\n");
        sb.append("\tpublic int comparar(").append(genericTypeName).append(" o1,  ").append(genericTypeName).append(" o2) {\n");

        sb.append("\t\t");
        if (inSortedFieldComponentItems.size() > 1) {
            sb.append("int ");
        }
        for (int i = 0; i < inSortedFieldComponentItems.size() - 1; i++) {
            sb.append("comp = ");
            sb.append(getComparisonCode(i));
            sb.append("\t\tif (comp != 0) {\n");
            sb.append("\t\t\treturn comp;\n");
            sb.append("\t\t}\n");
            sb.append("\t\t");
        }
        sb.append("return ").append(
                owner.getClass().isAssignableFrom(String.class) ?
                        "o1.compareTo(o2);\n" : //special case for String comparison
                        getComparisonCode(inSortedFieldComponentItems.size() - 1));
        sb.append("\t}\n");
        sb.append("}");
        return sb.toString();
    }

    private String getComparisonCode(int i) {
        StringBuilder sb = new StringBuilder();
        final SortedFieldItem sortedFieldItem = inSortedFieldComponentItems.get(i);
        final String className = Utils.getWrapperClassName(sortedFieldItem.getField().getType().getName());
        final String[] parts = sortedFieldItem.getPrefix().split("\\.");
        sb.append(!sortedFieldItem.isAscending() ? "-" : "");
        if (className == null) { //java wrapper
            sb.append("o1");
        } else {    //java primitive
            sb.append(className).append(".compare(o1");
        }

        StringBuilder sbCompare = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                sbCompare.append(Utils.getGetterCall(part));
            }
        }
        sbCompare.append(Utils.getGetterCall(sortedFieldItem.getField()));
        sb.append(sbCompare);

        if (className == null) { //java wrapper
            sb.append(".compareTo(o2").append(sbCompare);
        } else {    //java primitive
            sb.append(", o2").append(sbCompare);
        }
        sb.append(");\n");
        return sb.toString();
    }
}


