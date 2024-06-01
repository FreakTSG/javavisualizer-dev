package com.aegamesi.java_visualizer.model;

import com.aegamesi.java_visualizer.aed.Comparacao;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

public class HeapObject extends HeapEntity {
	public Map<String, Value> fields = new TreeMap<>();

	public Comparacao<?> getComparadorInstance(Map<Long, HeapEntity> heapMap) {
		try {
			Value criterioValue = fields.get("criterio");
			if (criterioValue == null) {
				System.err.println("Comparator value is null.");
				return null;
			}

			if (criterioValue.type != Value.Type.REFERENCE) {
				System.err.println("Comparator value is not a reference.");
				return null;
			}

			// Get the comparator object from the heap
			HeapObject comparatorObject = (HeapObject) heapMap.get(criterioValue.reference);
			if (comparatorObject == null) {
				System.err.println("Comparator object is null.");
				return null;
			}

			// Ensure that the comparator object has the class name set
			String className = comparatorObject.getClassName();
			if (className == null) {
				System.err.println("Comparator class name is null.");
				return null;
			}

			System.out.println("Comparator class name: " + className);

			Class<?> comparatorClass = Class.forName(className);
			System.out.println("Loaded comparator class: " + comparatorClass.getName());
			return (Comparacao<?>) comparatorClass.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean hasSameStructure(HeapEntity other) {
		if (other instanceof HeapObject) {
			return fields.size() == ((HeapObject) other).fields.size();
		}
		return false;
	}

	@Override
	JSONObject toJson() {
		JSONObject o = super.toJson();
		o.put("keys", fields.keySet());
		o.put("vals", fields.values().stream().map(f -> f.toJson()).toArray());
		return o;
	}

	static HeapObject fromJson(JSONObject o) {
		HeapObject e = new HeapObject();

		JSONArray keys = o.getJSONArray("keys");
		JSONArray vals = o.getJSONArray("vals");
		for (int i = 0; i < keys.length(); i++) {
			e.fields.put(keys.getString(i), Value.fromJson(vals.getJSONArray(i)));
		}
		return e;
	}

	public static boolean isSimpleList(HeapObject heapObject) {
		return heapObject.fields.containsKey("base") && heapObject.fields.containsKey("noFinal");
	}
	public static boolean isDoubleList(HeapObject heapObject) {
		return heapObject.fields.containsKey("base") ;
	}


    public Class<?> getActualClass() {
		if (fields.containsKey("class")) {
			return fields.get("class").getActualClass();
		} else {
			return null;
		}
	}

	public String getClassName() {
		if (fields.containsKey("class")) {
			return fields.get("class").stringValue;
		} else {
			return null;
		}
    }

}
