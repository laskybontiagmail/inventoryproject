package com.aiur.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.springframework.util.ConcurrentReferenceHashMap;

/**
 * GenericCache<KeyType, ValueType>
 */
@SuppressWarnings("serial")
public class GenericCache<Key, Value> implements Serializable {
	private transient Map<Key, Value> cache = new ConcurrentReferenceHashMap<Key, Value>();

	/**
	 * Constructor.
	 */
	public GenericCache() {
	}

	/**
	 * Set value
	 * @param key - key
	 * @param value - value
	 */
	public void set(Key key, Value value) {
		this.cache.put(key, value);
	}

	/**
	 * Get value
	 * ValueType
	 */
	public Value get(Key key) {
		Value value = this.cache.get(key);
		if (value != null) {
			return value;
		}
		return null;
	}

	/**
	 * Get values
	 * @return List<ValueType>
	 */
	public List<Value> getValues() {
		List<Value> values = new ArrayList<Value>();
		values.addAll(this.cache.values());
		return values;
	}

	/**
	 * Get all the keys of this cache/map
	 * 
	 * @return
	 */
	public List<Key> getKeys() {
		List<Key> keys = new ArrayList<Key>();
		keys.addAll(this.cache.keySet());
		return keys;
	}

	/**
	 * Get all
	 * @return {Map<String, Object>}
	 */
	public Map<Key, Value> getMap() {
		return this.cache;
	}

	public boolean isEmpty() {
		return this.cache.isEmpty();
	}

	public Iterator<Entry<Key, Value>> iterator() {
		return this.cache.entrySet().iterator();
	}
	
	public Set<Entry<Key, Value>> entries() {
		return this.cache.entrySet();
	}

	/**
	 * Remove
	 * @param key - value
	 * 
	 */
	public void delete(Key key) {
		try {
			this.cache.remove(key);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * Clear
	 */
	public void clear() {
		try {
			if (this.cache != null && !cache.isEmpty()) {
				this.cache.clear();
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			this.cache = null;
		}
	}

	/**
	 * Get the size of this map
	 */
	public int getSize() {
		return this.cache.size();
	}
}


