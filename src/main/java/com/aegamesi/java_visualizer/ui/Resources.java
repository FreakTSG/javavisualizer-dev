package com.aegamesi.java_visualizer.ui;

import java.awt.*;

public enum Resources {
    INSTANCE;

    public Image variableMenuIcon;
    public Image recordMenuIcon;
    public Image comparatorMenuIcon;
    public Image unsortedCircularDoubleLinkedListWithBaseMenuIcon;
    public Image unsortedCircularsimpleLinkedListWithBaseMenuIcon;
    public Image unsortedDoubleLinkedListMenuIcon;
    public Image unsortedSimpleLinkedListMenuIcon;
    public Image sortedCircularSimpleLinkedListWithBaseMenuIcon;
    public Image sortedCircularDoubleLinkedListMaxOrderMenuIcon;
    public Image distinctSortedCircularDoubleLinkedListMaxOrderMenuIcon;
    public Image sortedCircularSimpleLinkedListWithMaxOrderMenuIcon;
    public Image arrayMenuIcon;
    public Image hashTableLinearMenuIcon;
    public Image hashTableQuadraticMenuIcon;
    public Image hashTableByHashMenuIcon;
    public Image sortedHashTableByHashMenuIcon;
    public Image deleteMenuIcon;
    public Image thisMenuIcon;
    public Image generateMenuIcon;

    public Cursor testeMouseCursor;
    public Image ascendingIcon;
    public Image descendingIcon;
    public String ascendingIconFilename;
    public String descendingIconFilename;

    Resources() {
        int width = 48;
        int height = 48;
        variableMenuIcon = getMenuIconImage("Variable.png", width, height);
        recordMenuIcon = getMenuIconImage("Aggregator.png", 28, 28);
        comparatorMenuIcon = getMenuIconImage("Comparator.png", width, height);
        unsortedCircularDoubleLinkedListWithBaseMenuIcon = getMenuIconImage("LDCBNO.png", width, height);
        unsortedCircularsimpleLinkedListWithBaseMenuIcon = getMenuIconImage("LSCBNO.png", width, height);
        unsortedDoubleLinkedListMenuIcon = getMenuIconImage("LDNO.png", width, height);
        unsortedSimpleLinkedListMenuIcon = getMenuIconImage("LSNO.png", width, height);
        sortedCircularSimpleLinkedListWithBaseMenuIcon = getMenuIconImage("LSCBO.png", width, height);
        sortedCircularDoubleLinkedListMaxOrderMenuIcon = getMenuIconImage("LDCBLO.png", width, height);
        distinctSortedCircularDoubleLinkedListMaxOrderMenuIcon = getMenuIconImage("LDCBLOD.png", width, height);
        sortedCircularSimpleLinkedListWithMaxOrderMenuIcon = getMenuIconImage("LSCBLO.png", width, height);
        arrayMenuIcon = getMenuIconImage("Array.png", width, height);
        hashTableLinearMenuIcon = getMenuIconImage("HashTableL.png", width, height);
        hashTableQuadraticMenuIcon = getMenuIconImage("HashTableQ.png", width, height);
        hashTableByHashMenuIcon = getMenuIconImage("HashTableH.png", width, height);
        sortedHashTableByHashMenuIcon = getMenuIconImage("HashTableS.png", width, height);
        deleteMenuIcon = getMenuIconImage("Delete.png", width, height);
        thisMenuIcon = getMenuIconImage("This.png", width, height);
        generateMenuIcon = getMenuIconImage("Generate.png", width, height);

        testeMouseCursor = getMouseCursor("mouse.png");

        ascendingIcon = getComparatorComponentImage("sort-ascending.png", 12, 12);
        descendingIcon = getComparatorComponentImage("sort-descending.png", 12, 12);
        ascendingIconFilename = "/comparator_icons/sort-ascending.png";
        descendingIconFilename = "/comparator_icons/sort-descending.png";
    }

//    public Image getImage(String filename) {
//        try {
//            return ImageIO.read(getClass().getResource(filename));
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public Image getImage(String filename) {
        return Toolkit.getDefaultToolkit().getImage(getClass().getResource(filename));
    }

    public Image getMenuIconImage(String filename, int width, int height) {
        return getImage("/menu_icons/" + filename).getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public Image getComparatorComponentImage(String filename, int width, int height) {
        return getImage("/comparator_icons/" + filename).getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public Cursor getMouseCursor(String filename) {
        return Toolkit.getDefaultToolkit().createCustomCursor(getImage("/mouse_cursors/" + filename), new Point(0, 0), filename);
    }
}
