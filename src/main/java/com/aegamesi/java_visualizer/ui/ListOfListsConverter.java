package com.aegamesi.java_visualizer.ui;

import com.aegamesi.java_visualizer.aed.Comparacao;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaDuplaNaoOrdenada;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaSimplesNaoOrdenada;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ordenadas.estruturas.ListaDuplaOrdenada;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ordenadas.estruturas.ListaSimplesOrdenada;
import com.aegamesi.java_visualizer.model.HeapEntity;
import com.aegamesi.java_visualizer.model.HeapObject;
import com.aegamesi.java_visualizer.model.Value;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ListOfListsConverter extends HeapObjectConverter<ListaSimplesNaoOrdenada<Object>>{
    private MyCanvas canvas;
    private Comparacao<Object> comparator;

    public ListOfListsConverter(Map<Long, HeapEntity> heapMap, MyCanvas canvas, Comparacao<Object> comparator) {
        super(heapMap);
        this.canvas = canvas;
        this.comparator = comparator;
    }

    @Override
    public ListaSimplesNaoOrdenada<Object> convert(HeapObject heapObject) {
        ListaSimplesNaoOrdenada<Object> list = new ListaSimplesNaoOrdenada<>();
        Value headValue = heapObject.fields.get("base");
        headValue = ((HeapObject) heapMap.get(headValue.reference)).fields.get("seguinte");

        Set<Long> visitedNodeIds = new HashSet<>();

        while (headValue != null && headValue.type == Value.Type.REFERENCE && headValue.reference != 0) {
            if (visitedNodeIds.contains(headValue.reference)) {
                break;
            }
            visitedNodeIds.add(headValue.reference);

            HeapObject currentNode = (HeapObject) heapMap.get(headValue.reference);
            Value dataValue = currentNode.fields.get("elemento");
            if (dataValue != null && dataValue.type == Value.Type.REFERENCE) {
                HeapObject innerListObject = (HeapObject) heapMap.get(dataValue.reference);
                if (isSimpleList(innerListObject)) {
                    ListaSimplesNaoOrdenada<?> innerList = new SimpleListConverter(heapMap).convert(innerListObject);
                    list.inserir(innerList);
                } else if (innerListObject.label.contains("ListaDuplaNaoOrdenada")) {
                    ListaDuplaNaoOrdenada<?> innerList = new DoubleListConverter(heapMap).convert(innerListObject);
                    list.inserir(innerList);
                } else if (innerListObject.label.contains("ListaSimplesOrdenada")) {
                    ListaSimplesOrdenada<?> innerList = new SortedSimpleListConverter(heapMap, comparator).convert(innerListObject);
                    list.inserir(innerList);
                } else if (innerListObject.label.contains("ListaDuplaOrdenada")) {
                    ListaDuplaOrdenada<?> innerList = new SortedDoubleListConverter(heapMap, comparator).convert(innerListObject);
                    list.inserir(innerList);
                } else {
                    HeapObject customObject = (HeapObject) heapMap.get(dataValue.reference);
                    if (customObject != null) {
                        list.inserir(customObject);
                    }
                }
            } else if (dataValue != null) {
                Object actualData = dataValue.getActualValue();
                if (actualData != null) {
                    list.inserir(actualData);
                }
            }

            headValue = currentNode.fields.get("seguinte");
        }
        return list;
    }

    private boolean isSimpleList(HeapObject heapObject) {
        // Implementar a lógica para verificar se é uma lista simples
        return heapObject.label.contains("ListaSimplesNaoOrdenada");
    }
}
