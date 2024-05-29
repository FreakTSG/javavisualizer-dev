package com.aegamesi.java_visualizer.ui;

import com.aegamesi.java_visualizer.aed.Comparacao;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaDuplaNaoOrdenada;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaSimplesNaoOrdenada;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ordenadas.estruturas.ListaDuplaOrdenada;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ordenadas.estruturas.ListaSimplesOrdenada;
import com.aegamesi.java_visualizer.model.HeapEntity;
import com.aegamesi.java_visualizer.model.HeapObject;

import java.util.Map;

public class HeapObjectConversionManager {
    private Map<Long, HeapEntity> heapMap;
    private MyCanvas canvas;
    private Comparacao<Object> comparator;

    public HeapObjectConversionManager(Map<Long, HeapEntity> heapMap, MyCanvas canvas, Comparacao<Object> comparator) {
        this.heapMap = heapMap;
        this.canvas = canvas;
        this.comparator = comparator;
    }

    public ListaSimplesNaoOrdenada<Object> convertHeapObjectToListOfLists(HeapObject heapObject) {
        return new ListOfListsConverter(heapMap, canvas, comparator).convert(heapObject);
    }

    public ListaDuplaOrdenada<Object> convertHeapObjectToSortedDoubleList(HeapObject heapObject) {
        return new SortedDoubleListConverter(heapMap, comparator).convert(heapObject);
    }

    public ListaDuplaNaoOrdenada<Object> convertHeapObjectToDoubleList(HeapObject heapObject) {
        return new DoubleListConverter(heapMap).convert(heapObject);
    }

    public ListaSimplesNaoOrdenada<Object> convertHeapObjectToSimpleList(HeapObject heapObject) {
        return new SimpleListConverter(heapMap).convert(heapObject);
    }

    public ListaSimplesOrdenada<Object> convertHeapObjectToSortedSimpleList(HeapObject heapObject) {
        return new SortedSimpleListConverter(heapMap,comparator).convert(heapObject);
    }
}
