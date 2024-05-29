package com.aegamesi.java_visualizer.ui;

import com.aegamesi.java_visualizer.aed.Comparacao;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ordenadas.estruturas.ListaDuplaOrdenada;
import com.aegamesi.java_visualizer.model.HeapEntity;
import com.aegamesi.java_visualizer.model.HeapObject;

import java.util.Map;

public class SortedDoubleListConverter extends HeapObjectConverter<ListaDuplaOrdenada<Object>>{
    private Comparacao<Object> comparator;

    public SortedDoubleListConverter(Map<Long, HeapEntity> heapMap, Comparacao<Object> comparator) {
        super(heapMap);
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator must not be null.");
        }
        this.comparator = comparator;
    }

    @Override
    public ListaDuplaOrdenada<Object> convert(HeapObject heapObject) {
        ListaDuplaOrdenada<Object> sortedDoubleList = new ListaDuplaOrdenada<>(comparator);
        Long baseRef = heapObject.fields.get("base").reference;
        HeapObject currentNode = (HeapObject) heapMap.get(baseRef);

        if (currentNode == null) {
            System.out.println("Sentinel node is null.");
            return sortedDoubleList;
        }

        Long currentNodeRef = currentNode.fields.get("seguinte").reference;

        while (currentNodeRef != null && !currentNodeRef.equals(baseRef)) {
            currentNode = (HeapObject) heapMap.get(currentNodeRef);
            if (currentNode == null) {
                break;
            }
            Object element = currentNode.fields.get("elemento").getActualValue();
            sortedDoubleList.inserir(element);
            currentNodeRef = currentNode.fields.get("seguinte").reference;
        }

        return sortedDoubleList;
    }
}
