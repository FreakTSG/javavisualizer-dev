package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists;


import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ColecaoIteravelLinear;

import java.util.LinkedList;

public interface DoubleLinkedListRepresentation<TLinkedList extends LinkedList & ColecaoIteravelLinear>
        extends SimpleOrDoubleLinkedListRepresentation<TLinkedList>, DoubleLinkedListOrSortedHashTableRepresentation<TLinkedList> {
}