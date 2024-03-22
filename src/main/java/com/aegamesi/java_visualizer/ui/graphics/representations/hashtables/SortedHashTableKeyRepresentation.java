package com.aegamesi.java_visualizer.ui.graphics.representations.hashtables;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.TabelaHashOrdenada;
import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.NormalTextElement;
import com.aegamesi.java_visualizer.ui.graphics.representations.SortedFieldItem;
import com.aegamesi.java_visualizer.utils.Utils;

import java.awt.*;
import java.util.ArrayList;

public class SortedHashTableKeyRepresentation extends GeneralHashTableKeyRepresentation<TabelaHashOrdenada> {
    private static final long serialVersionUID = 1L;

    private ArrayList<SortedFieldItem> outSortedFieldItems;
    private ArrayList<SortedFieldItem> inSortedFieldItems;
    private int level;

    public SortedHashTableKeyRepresentation(Point position, TabelaHashOrdenada owner, MyCanvas mycanvas) {
        this(position, owner, mycanvas, false);
    }

    public SortedHashTableKeyRepresentation(Point position, TabelaHashOrdenada owner, MyCanvas mycanvas, boolean horizontal) {
        super(position, owner, mycanvas, horizontal);
    }

//    public <T> ArrayList<T> setList(Class<T> clazz) {
//        return new ArrayList<>();
//    }

    public void init() {
        level = 1;
        container.setHorizontal(false);

        outSortedFieldItems = new ArrayList<>();
        inSortedFieldItems = new ArrayList<>();
//        Utils.createFieldItem(() -> new UnsortedFieldItem(), outUnsortedFieldItems, "", ownerClass, 0, level);
        try {
            Utils.createFieldItem(() -> new SortedFieldItem(), outSortedFieldItems, "", owner.getClass(), 0, level);
        } catch (Exception e) {
            e.printStackTrace();
        }
        update();
    }

    public void update() {
        container.removeAllGraphicElements();

        for (SortedFieldItem inSortedFieldItem : inSortedFieldItems) {
            container.add(new NormalTextElement(inSortedFieldItem.getPrefix() + inSortedFieldItem.getField().getName(), ConstantsIDS.FONT_SIZE_TEXT, new Color(196, 196, 244, 255)));
        }

        container.update();
    }

    public ArrayList<SortedFieldItem> getOutSortedFieldItems() {
        return outSortedFieldItems;
    }

    public ArrayList<SortedFieldItem> getInSortedFieldItems() {
        return inSortedFieldItems;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getFieldName() {
        String prefix = inSortedFieldItems.get(0).getPrefix();
        boolean allWithSamePrefixIn = true;
        for (SortedFieldItem inSortedFieldItem : inSortedFieldItems) {
            if (!inSortedFieldItem.getPrefix().equals(prefix)) {
                allWithSamePrefixIn = false;
                break;
            }
        }
        boolean noneWithPrefixOut = true;
        for (SortedFieldItem outSortedFieldItem : outSortedFieldItems) {
            if (outSortedFieldItem.getPrefix().equals(prefix)) {
                noneWithPrefixOut = false;
                break;
            }
        }

        return allWithSamePrefixIn && noneWithPrefixOut ?
                prefix.substring(0, prefix.length()-1) :
                null;

    }
}


