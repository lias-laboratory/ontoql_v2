package fr.ensma.lias.mariusql.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simple LRU implementation with hash map.
 * 
 * @author Florian MHUN
 *
 * @see http://stackoverflow.com/questions/221525/how-would-you-implement-an-lru-cache-in-java-6
 *
 * @param <A> Type of object used for map key
 * @param <B> Type of object used for map value
 */
public class LRUHashMap<A, B> extends LinkedHashMap<A, B> {

	private static final long serialVersionUID = 4809464528756120899L;

	private final int maxEntries;

	public LRUHashMap(final int maxEntries) {
		super(maxEntries + 1, 1.0f, true);
		this.maxEntries = maxEntries;
	}

	@Override
	protected boolean removeEldestEntry(final Map.Entry<A, B> eldest) {
		return super.size() > maxEntries;
	}
}
