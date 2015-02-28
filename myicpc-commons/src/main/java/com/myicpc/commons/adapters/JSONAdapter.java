package com.myicpc.commons.adapters;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Adapter for {@link com.google.gson.JsonObject} which does values check
 * 
 * @author Roman Smetana
 * 
 */
public class JSONAdapter {
	private JsonObject jsonObject;

	public JSONAdapter(JsonElement element) {
		this.jsonObject = element.getAsJsonObject();
	}

	/**
	 * Returns the member with the specified name.
	 * 
	 * @param key
	 *            name of the member that is being requested.
	 * @return the member matching the name. Null if no such member exists.
	 */
	public JsonElement get(String key) {
		return jsonObject.get(key);
	}

	/**
	 * convenient method to check if a member with the specified name is
	 * present in this object.
	 * 
	 * @param key
	 *            name of the member that is being checked for presence.
	 * @return true if there is a member with the specified name, false
	 *         otherwise.
	 */
	public boolean has(String key) {
		return jsonObject.has(key);
	}

	/**
	 * convenient method to get element as a string value.
	 * 
	 * @param key
	 *            name of the member
	 * @return get this element as a string value, or null if not present
	 */
	public String getString(String key) {
		return getString(key, null);
	}

	/**
	 * convenient method to get element as a string value.
	 * 
	 * @param key
	 *            name of the member
	 * @param defaultValue
	 *            default value if the key is not in the JSON object
	 * @return get this element as a string value.
	 */
	public String getString(String key, String defaultValue) {
		return getString(key, defaultValue, true);
	}

	/**
	 * convenient method to get element as a string value.
	 * 
	 * @param key
	 *            name of the member
	 * @param defaultValue
	 *            default value if the key is not in the JSON object
	 * @param allowEmpty
	 *            if false, the empty JSON value is replaced by default value
	 * @return get this element as a string value.
	 */
	public String getString(String key, String defaultValue, boolean allowEmpty) {
		if (jsonObject.has(key)) {
			if (jsonObject.get(key).isJsonNull()) {
				return defaultValue;
			}
			String value = jsonObject.get(key).getAsString();
			if (!allowEmpty && StringUtils.isEmpty(value)) {
				return defaultValue;
			}
			return value;
		}
		return defaultValue;
	}

	/**
	 * convenient method to get element as a double value.
	 * 
	 * @param key
	 *            name of the member
	 * @return get this element as a double value, or null if not present
	 */
	public Double getDouble(String key) {
		return getDouble(key, null);
	}

	/**
	 * convenient method to get element as a double value.
	 * 
	 * @param key
	 *            name of the member
	 * @param defaultValue
	 *            default value if the key is not in the JSON object
	 * @return get this element as a double value.
	 */
	public Double getDouble(String key, Double defaultValue) {
		if (jsonObject.has(key)) {
			return jsonObject.get(key).getAsDouble();
		}
		return defaultValue;
	}

    /**
     * convenient method to get element as a boolean value.
     *
     * @param key
     *            name of the member
     * @return get this element as a boolean value, or false if not present
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * convenient method to get element as a boolean value.
     *
     * @param key
     *            name of the member
     * @param defaultValue
     *            default value if the key is not in the JSON object
     * @return get this element as a boolean value.
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        if (jsonObject.has(key)) {
            return jsonObject.get(key).getAsBoolean();
        }
        return defaultValue;
    }

	/**
	 * convenient method to get element as a long value.
	 * 
	 * @param key
	 *            name of the member
	 * @return get this element as a long value, or null if not present
	 */
	public Long getLong(String key) {
		return getLong(key, null);
	}

	/**
	 * convenient method to get element as a long value.
	 * 
	 * @param key
	 *            name of the member
	 * @param defaultValue
	 *            default value if the key is not in the JSON object
	 * @return get this element as a long value.
	 */
	public Long getLong(String key, Long defaultValue) {
		if (jsonObject.has(key)) {
			return jsonObject.get(key).getAsLong();
		}
		return defaultValue;
	}

	/**
	 * convenient method to get element as an integer value.
	 * 
	 * @param key
	 *            name of the member
	 * @return get this element as an integer value, or null if not present
	 */
	public Integer getInteger(String key) {
		return getInteger(key, null);
	}

	/**
	 * convenient method to get element as an integer value.
	 * 
	 * @param key
	 *            name of the member
	 * @param defaultValue
	 *            default value if the key is not in the JSON object
	 * @return get this element as an integer value.
	 */
	public Integer getInteger(String key, Integer defaultValue) {
		if (jsonObject.has(key)) {
			return jsonObject.get(key).getAsInt();
		}
		return defaultValue;
	}

	/**
	 * convenient method to get element as a JSON array.
	 * 
	 * @param key
	 *            name of the member
	 * @return JSON array, or empty array, if key is not present
	 */
	public JsonArray getJsonArray(String key) {
		if (jsonObject.has(key)) {
			return jsonObject.getAsJsonArray(key);
		}
		return new JsonArray();
	}

	/**
	 * convenient method to get element as a string value from inner object
	 * 
	 * @param objectKey
	 *            object name of the member
	 * @param key
	 *            name of the member
	 * @return string value of inner object
	 */
	public String getStringFromObject(String objectKey, String key) {
		if (has(objectKey) && get(objectKey).isJsonObject()) {
			return get(objectKey).getAsJsonObject().get(key).getAsString();
		}
		return null;
	}

	/**
	 * convenient method to get element as a string value from inner-inner
	 * object
	 * 
	 * @param objectKey1
	 *            object name of the member
	 * @param objectKey2
	 *            inner object name of the member
	 * @param key
	 *            name of the member
	 * @return string value of inner-inner object
	 */
	public String getStringFromObject(String objectKey1, String objectKey2, String key) {
		if (has(objectKey1) && get(objectKey1).isJsonObject()) {
			JsonObject o = get(objectKey1).getAsJsonObject();

			if (o.has(objectKey2) && o.get(objectKey2).isJsonObject()) {
				return o.get(objectKey2).getAsJsonObject().get(key).getAsString();
			}
		}
		return null;
	}

    /**
     * method to get values of array with primitive types
     *
     * @param key name of the member
     * @return primitive value array
     */
    public String[] getJsonArrayValues(String key) {
        if (jsonObject.has(key)) {
            JsonArray arr = getJsonArray(key);
            String[] values = new String[arr.size()];
            int i = 0;
            for (JsonElement elem : arr) {
                values[i++] = elem.getAsString();
            }
            return values;
        }
        return new String[0];
    }

}
