package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.ColecaoIteravel;

public interface SimpleOrDoubleLinkedListOrSortedHashTableRepresentation<TLinkedListOrSortedHashTable extends ColecaoIteravel> {
    TLinkedListOrSortedHashTable getOwner();

    void rebuild();
}
