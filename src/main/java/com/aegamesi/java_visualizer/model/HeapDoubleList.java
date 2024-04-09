package com.aegamesi.java_visualizer.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HeapDoubleList extends HeapEntity {
    public List<Value> items = new ArrayList<>();
    public HeapDoubleList prev; // Reference to the previous HeapDoubleList node
    public HeapDoubleList next; // Reference to the next HeapDoubleList node

    @Override
    public boolean hasSameStructure(HeapEntity other) {
        if (other instanceof HeapDoubleList) {
            HeapDoubleList otherList = (HeapDoubleList) other;
            return items.size() == otherList.items.size() &&
                    (prev == null && otherList.prev == null || prev.hasSameStructure(otherList.prev)) &&
                    (next == null && otherList.next == null || next.hasSameStructure(otherList.next));
        }
        return false;
    }

    @Override
    JSONObject toJson() {
        JSONObject o = super.toJson();
        o.put("items", items.stream().map(Value::toJson).toArray());
        return o;
    }

    static HeapDoubleList fromJson(JSONObject o) {
        HeapDoubleList e = new HeapDoubleList();
        for (Object item : o.getJSONArray("items")) {
            e.items.add(Value.fromJson((JSONArray) item));
        }
        return e;
    }
}

