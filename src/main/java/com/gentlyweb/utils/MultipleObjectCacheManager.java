/*
 * Copyright 2006 - Gary Bentley
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gentlyweb.utils;

import java.util.Map;

/**
 * This interface should be implemented by classes that contain X number of publicly available
 * ObjectCaches, it provides methods that allow applications to control the caches without
 * understanding what is inside the cache.
 * <br /><br />
 * All of the methods in this interface are optional and if not supported then
 * implementing classes should throw <code>UnsupportedOperationException</code>.
 * <br /><br />
 * This interface mirrors {@link ObjectCacheManager} but each method (where appropriate)
 * takes a key object to tell the implementing class which cache it wants the operation
 * to occur on.
 */
public interface MultipleObjectCacheManager
{

    /**
     * Let the object cache be flushed.
     * 
     * @param key The key to identify the particular cache.
     */
    public void flush (Object key);

    /**
     * Set the maximum size of the cache.
     *
     * @param key The key to identify the particular cache.
     * @param size The maximum size.
     */
    public void setMaxSize (Object key,
			    int    size);

    /**
     * Resize the cache to a particular size, if the size is actually bigger than the 
     * current size then this operation should not touch the cached objects, if the size is
     * less then the cache should be reduced in size using the current policy until the
     * size is reached.  Either way the maximum size should be set to this value.
     *
     * @param key The key to identify the particular cache.
     * @param size The new size.
     */
    public void resize (Object key,
			int    size);

    /**
     * Return the current capacity of the cache, it should basically be (max size - current size).
     *
     * @param key The key to identify the particular cache.
     * @return The current number of items that can be added until the cache reaches it's maximum size.
     */
    public int capacity (Object key);

    /**
     * Return whether the cache is empty or not.
     *
     * @param key The key to identify the particular cache.
     * @return <code>true</code> if the cache is empty, <code>false</code> if it has entries.
     */
    public boolean isEmpty (Object key);

    /**
     * Get all the entries in the cache as a Map of key to value.
     * 
     * @param key The key to identify the particular cache.
     * @param map The Map that should be populated with the key/values in the cache.
     */
    public void toMap (Object object,
		       Map    map);

    /**
     * Merge the current cache with another.
     *
     * @param key The key to identify the particular cache.
     * @param cache The cache to merge.
     */
    public void merge (Object      key,
		       ObjectCache cache);

    /**
     * Add all the entries in the Map to cache.
     *
     * @param key The key to identify the particular cache.
     * @param map The Map to get key/values from.
     */
    public void putAll (Object key,
			Map    map);

    /**
     * Set the policy for managing the cache, should be one of:
     * {@link ObjectCache.OLDEST}, {@link ObjectCache.YOUNGEST}, {@link ObjectCache.RANDOM}.
     *
     * @param key The key to identify the particular cache.
     * @param policy The policy.
     */
    public void setPolicy (Object key,
			   int    policy);

}
