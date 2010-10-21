package com.gentlyweb.utils;

import java.util.Map;

/**
 * This interface should be implemented by classes that contain a publicly available
 * ObjectCache, it provides methods that allow applications to control the cache without
 * understanding what is inside the cache.
 * <br /><br />
 * All of the methods in this interface are optional and if not supported then
 * implementing classes should throw <code>UnsupportedOperationException</code>.
 */
public interface ObjectCacheManager
{

    /**
     * Let the object cache be flushed.
     */
    public void flush ();

    /**
     * Set the maximum size of the cache.
     *
     * @param size The maximum size.
     */
    public void setMaxSize (int size);

    /**
     * Resize the cache to a particular size, if the size is actually bigger than the 
     * current size then this operation should not touch the cached objects, if the size is
     * less then the cache should be reduced in size using the current policy until the
     * size is reached.  Either way the maximum size should be set to this value.
     *
     * @param size The new size.
     */
    public void resize (int size);

    /**
     * Return the current capacity of the cache, it should basically be (max size - current size).
     *
     * @return The current number of items that can be added until the cache reaches it's maximum size.
     */
    public int capacity ();

    /**
     * Return whether the cache is empty or not.
     *
     * @return <code>true</code> if the cache is empty, <code>false</code> if it has entries.
     */
    public boolean isEmpty ();

    /**
     * Get all the entries in the cache as a Map of key to value.
     * 
     * @param map The Map that should be populated with the key/values in the cache.
     */
    public void toMap (Map map);

    /**
     * Merge the current cache with another.
     *
     * @param cache The cache to merge.
     */
    public void merge (ObjectCache cache);

    /**
     * Add all the entries in the Map to cache.
     *
     * @param map The Map to get key/values from.
     */
    public void putAll (Map map);

    /**
     * Set the policy for managing the cache, should be one of:
     * {@link ObjectCache.OLDEST}, {@link ObjectCache.YOUNGEST}, {@link ObjectCache.RANDOM}.
     *
     * @param policy The policy.
     */
    public void setPolicy (int policy);

}
