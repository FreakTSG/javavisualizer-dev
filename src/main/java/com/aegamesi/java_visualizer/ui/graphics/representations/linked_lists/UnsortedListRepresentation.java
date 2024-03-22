package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ColecaoIteravelLinear;
import com.aegamesi.java_visualizer.ui.graphics.representations.BaseRepresentation;

public interface UnsortedListRepresentation<T extends ColecaoIteravelLinear> extends BaseRepresentation {
    T getOwner();


}
