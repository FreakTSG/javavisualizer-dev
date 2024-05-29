package com.aegamesi.java_visualizer.ui;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaDuplaNaoOrdenada;
import com.aegamesi.java_visualizer.model.HeapEntity;
import com.aegamesi.java_visualizer.model.HeapObject;
import com.aegamesi.java_visualizer.model.Value;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DoubleListConverter extends HeapObjectConverter<ListaDuplaNaoOrdenada<Object>>{
    public DoubleListConverter(Map<Long, HeapEntity> heapMap) {
        super(heapMap);
    }

    @Override
    public ListaDuplaNaoOrdenada<Object> convert(HeapObject heapObject) {
        ListaDuplaNaoOrdenada<Object> doubleList = new ListaDuplaNaoOrdenada<>();
        Value headValue = heapObject.fields.get("base");
        Set<Long> visitedNodeIds = new HashSet<>();

        headValue = ((HeapObject) heapMap.get(headValue.reference)).fields.get("seguinte");

        while (headValue != null && headValue.type == Value.Type.REFERENCE && headValue.reference != 0) {
            if (visitedNodeIds.contains(headValue.reference)) {
                System.out.println("Cycle detected or reached base node again. Terminating.");
                break;
            }
            visitedNodeIds.add(headValue.reference);

            HeapEntity entity = heapMap.get(headValue.reference);
            if (!(entity instanceof HeapObject)) {
                break;
            }
            HeapObject currentNode = (HeapObject) entity;

            if (currentNode.fields.get("elemento") != null) {
                Value dataValue = currentNode.fields.get("elemento");
                if (dataValue != null) {
                    Object actualData = dataValue.getActualValue();
                    if (actualData != null) {
                        doubleList.inserir(actualData);
                        System.out.println("Inserted: " + actualData);
                    } else {
                        System.out.println("Actual Data is null for node with ID: " + currentNode.id);
                    }
                } else {
                    System.out.println("Data Value is null for node with ID: " + currentNode.id);
                }
            }

            headValue = currentNode.fields.get("seguinte");
        }
        return doubleList;
    }
}
