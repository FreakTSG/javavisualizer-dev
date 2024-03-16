package com.aegamesi.java_visualizer.model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HeapLinkedList extends HeapList {
    // You can add linked list specific properties here, if necessary.
    // For example, if you need to represent the pointers between the nodes:
    public List<Value> pointers = new ArrayList<>();

    @Override
    public boolean hasSameStructure(HeapEntity other) {
        if (other instanceof HeapLinkedList) {
            HeapLinkedList otherList = (HeapLinkedList) other;
            return super.hasSameStructure(other) && pointers.size() == otherList.pointers.size();
        }
        return false;
    }

    @Override
    JSONObject toJson() {
        JSONObject o = super.toJson();
        // Include additional properties in the JSON if needed
        // o.put("pointers", pointers.stream().map(Value::toJson).toArray());
        return o;
    }

    // Assuming we have a method to create Value instances from JSON
    // This method needs to be implemented or adjusted to fit your actual JSON structure
    static HeapLinkedList fromJson(JSONObject o) {
        HeapLinkedList e = new HeapLinkedList();
        // Populate the 'items' list from the base class
        e.items.addAll(HeapList.fromJson(o).items);
        // Populate additional properties if needed
        // JSONArray jsonPointers = o.optJSONArray("pointers");
        // if (jsonPointers != null) {
        //     for (Object pointer : jsonPointers) {
        //         e.pointers.add(Value.fromJson((JSONObject) pointer));
        //     }
        // }
        return e;
    }
}
