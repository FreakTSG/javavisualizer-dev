package com.aegamesi.java_visualizer.model;

import org.json.JSONArray;

public class Value {
	// primitive or reference
	public Type type;
	public long longValue;
	public double doubleValue;
	public boolean booleanValue;
	public String stringValue;
	public char charValue;
	public long reference;
	public int integerValue;

	@Override
	public String toString() {
		switch (type) {
			case NULL:
				return "null";
			case STRING:
				return "\"" + stringValue + "\"";
			case LONG:
				return Long.toString(longValue);
			case INTEGER:
				return Integer.toString(integerValue);
			case DOUBLE:
				return Double.toString(doubleValue);
			case BOOLEAN:
				return Boolean.toString(booleanValue);
			case CHAR:
				return "'" + charValue + "'";
			case REFERENCE:
				return "*REF*";
			default:
				return "<?>";
		}
	}

	public Object getActualValue() {
		switch (type) {
			case STRING:
				return stringValue;
			case LONG:
				return longValue;
			case INTEGER:
				return integerValue;
			case DOUBLE:
				return doubleValue;
			case BOOLEAN:
				return booleanValue;
			case CHAR:
				return charValue;
			case REFERENCE:
				return reference;
			default:
				return null;
		}
	}



	public Class<?> getActualClass() {
		switch (type) {
			case INTEGER:
				return Integer.class;
			case STRING:
				return String.class;
			case LONG:
				return Long.class;
			case DOUBLE:
				return Double.class;
			case BOOLEAN:
				return Boolean.class;
			case CHAR:
				return Character.class;
			case REFERENCE:
				return Object.class;
			default:
				return null;
		}
	}

	public enum Type {
		NULL, VOID, LONG, DOUBLE, BOOLEAN, STRING, CHAR, REFERENCE,INTEGER;
	}

	JSONArray toJson() {
		JSONArray a = new JSONArray();
		a.put(type.name());
		switch (type) {
			case STRING:
				a.put(stringValue);
				break;
			case LONG:
				a.put(longValue);
				break;
			case DOUBLE:
				a.put(doubleValue);
				break;
			case BOOLEAN:
				a.put(booleanValue);
				break;
			case CHAR:
				a.put(charValue);
				break;
			case REFERENCE:
				a.put(reference);
				break;
			case INTEGER:
				a.put(integerValue);
				break;
		}
		return a;
	}

	static Value fromJson(JSONArray a) {
		Value v = new Value();
		v.type = Type.valueOf(a.getString(0));
		switch (v.type) {
			case STRING:
				v.stringValue = a.getString(1);
				break;
			case LONG:
				v.longValue = a.getLong(1);
				break;
			case DOUBLE:
				v.doubleValue = a.getDouble(1);
				break;
			case BOOLEAN:
				v.booleanValue = a.getBoolean(1);
				break;
			case CHAR:
				v.charValue = (char) a.getInt(1);
				break;
			case REFERENCE:
				v.reference = a.getLong(1);
				break;
			case INTEGER:
				v.integerValue = a.getInt(1);
				break;
		}
		return v;
	}
}
