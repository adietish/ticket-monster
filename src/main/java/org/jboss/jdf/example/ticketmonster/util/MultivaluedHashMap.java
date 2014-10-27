package org.jboss.jdf.example.ticketmonster.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

public class MultivaluedHashMap<K, V> extends ForwardingMap<K, List<V>> implements MultivaluedMap<K, V> {

    public static MultivaluedMap<?, ?> EMPTY = new MultivaluedHashMap<Object, Object>();

    public static <K, V> MultivaluedHashMap<K, V> empty() {
        return (MultivaluedHashMap<K, V>) EMPTY;
    }

    private Map<K, List<V>> map = new HashMap<K, List<V>>();

    @Override
    protected Map<K, List<V>> delegate() {
        return map;
    }

    @Override
    public void putSingle(K key, V value) {
        List<V> l = new ArrayList<V>(1);
        l.add(value);
        put(key, l);
    }

    @Override
    public void add(K key, V value) {
        List<V> l = get(key);
        if (l == null) {
            l = new ArrayList<V>(1);
            put(key, l);
        }
        l.add(value);
    }

    @Override
    public V getFirst(K key) {
        List<V> l = get(key);
        return l == null ? null : l.get(0);
    }

	@Override
	public void addAll(K key, V... newValues) {
		if(newValues == null) {
			throw new NullPointerException();
		}
		if(newValues.length == 0)
			return;
		List<V> values = getValues(key);
		for(V value: newValues) {
			values.add(value);
		}
	}

	@Override
	public void addAll(K key, List<V> valueList) {
		if(valueList == null) {
			throw new NullPointerException();
		}
		if(valueList.isEmpty())
			return;
		List<V> values = getValues(key);
		for(V value: valueList) {
			values.add(value);
		}
	}

	@Override
	public void addFirst(K key, V value) {
        List<V> values = getValues(key);
        values.add(0, value);
	}
	
	protected final List<V> getValues(K key) {
        List<V> l = delegate().get(key);
        if (l == null) {
            l = new LinkedList<V>();
            delegate().put(key, l);
        }
        return l;
    }

	@Override
	public boolean equalsIgnoreValueOrder(MultivaluedMap<K, V> otherMap) {
		if (this == otherMap) {
            return true;
        }
        if (!keySet().equals(otherMap.keySet())) {
            return false;
        }
        for (Entry<K, List<V>> e : entrySet()) {
            List<V> olist = otherMap.get(e.getKey());
            if (e.getValue().size() != olist.size()) {
                return false;
            }
            for (V v : e.getValue()) {
                if (!olist.contains(v)) {
                    return false;
                }
            }
        }
        return true;
	}

}
