package com.aegamesi.java_visualizer.ui.graphics.representations.hashtables;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.TabelaHash;
import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.NormalTextElement;
import com.aegamesi.java_visualizer.ui.graphics.representations.UnsortedFieldItem;
import com.aegamesi.java_visualizer.utils.Utils;

import java.awt.*;
import java.util.ArrayList;
public class HashTableKeyRepresentation extends GeneralHashTableKeyRepresentation<TabelaHash> {
    private static final long serialVersionUID = 1L;

    private ArrayList<UnsortedFieldItem> outUnsortedFieldItems;
    private ArrayList<UnsortedFieldItem> inUnsortedFieldItems;
    private int level;

    public HashTableKeyRepresentation(Point position,TabelaHash owner, MyCanvas myCanvas) {
        this(position, owner, myCanvas, false);
    }

    public HashTableKeyRepresentation(Point position, TabelaHash owner, MyCanvas myCanvas, boolean horizontal) {
        super(position, owner, myCanvas, horizontal);
    }

    public <T> ArrayList<T> setList(Class<T> clazz) {
        return new ArrayList<>();
    }

    public void init() {
        level = 1;
        container.setHorizontal(false);

        outUnsortedFieldItems = new ArrayList<>();
        inUnsortedFieldItems = new ArrayList<>();
      //  Utils.createFieldItem(() -> new UnsortedFieldItem(), outUnsortedFieldItems, "", ownerClass, 0, level);
        try {
            Utils.createFieldItem(() -> new UnsortedFieldItem(), outUnsortedFieldItems, "", owner.getClass(), 0, level);
        } catch (Exception e) {
            e.printStackTrace();
        }
        update();
    }

    public void update() {
        container.removeAllGraphicElements();

        for (UnsortedFieldItem inUnsortedFieldItem : inUnsortedFieldItems) {
            container.add(new NormalTextElement(inUnsortedFieldItem.getPrefix() + inUnsortedFieldItem.getField().getName(), ConstantsIDS.FONT_SIZE_TEXT, new Color(196, 196, 244, 255)));
        }

        container.update();
    }

    public ArrayList<UnsortedFieldItem> getOutUnsortedFieldItems() {
        return outUnsortedFieldItems;
    }

    public ArrayList<UnsortedFieldItem> getInUnsortedFieldItems() {
        return inUnsortedFieldItems;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getFieldName() {
        String prefix = inUnsortedFieldItems.get(0).getPrefix();
        boolean allWithSamePrefixIn = true;
        for (UnsortedFieldItem inUnsortedFieldItem : inUnsortedFieldItems) {
            if (!inUnsortedFieldItem.getPrefix().equals(prefix)) {
                allWithSamePrefixIn = false;
                break;
            }
        }
        boolean noneWithPrefixOut = true;
        for (UnsortedFieldItem outUnsortedFieldItem : outUnsortedFieldItems) {
            if (outUnsortedFieldItem.getPrefix().equals(prefix)) {
                noneWithPrefixOut = false;
                break;
            }
        }

        return allWithSamePrefixIn && noneWithPrefixOut ?
                prefix.substring(0, prefix.length() - 1) :
                null;

    }
}



