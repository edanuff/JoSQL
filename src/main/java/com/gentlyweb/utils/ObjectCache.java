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

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Random;

import java.lang.reflect.InvocationTargetException;

/**
 * The ObjectCache is it's own manager which means that classes that extend this one can
 * do so quickly and without having to implement the {@link ObjectCacheManager} interface
 * they only have to provide their own functionality whilst client classes can rely on the
 * interface.  All operations defined with {@link ObjectCacheManager} are supported here.
 */
public class ObjectCache implements ObjectCacheManager
{

    public static final int OLDEST = 1;
    public static final int YOUNGEST = 3;
    public static final int RANDOM = 5;

    private TreeMap keys = new TreeMap ();
    private Map data = new HashMap ();
    private int type = ObjectCache.RANDOM;
    private int maxSize = -1;
    private Random random = null;

    public ObjectCache (int    policy)
                        throws IllegalArgumentException
    {

	if ((type != ObjectCache.OLDEST)
	    &&
	    (type != ObjectCache.YOUNGEST)
	    &&
	    (type != ObjectCache.RANDOM)
	   )
	{

	    throw new IllegalArgumentException ("Incorrect policy: " + type);

	}

	if (type == ObjectCache.RANDOM)
	{

	    this.random = new Random ();

	}

	this.type = type;

    }

    public void setPolicy (int    type)
	                   throws IllegalArgumentException
    {

	if ((type != ObjectCache.OLDEST)
	    &&
	    (type != ObjectCache.YOUNGEST)
	    &&
	    (type != ObjectCache.RANDOM)
	   )
	{

	    throw new IllegalArgumentException ("Incorrect policy: " + type);

	}

	this.type = type;

    }

    public int getPolicy ()
    {

	return this.type;

    }

    public Iterator iterator ()
    {

	return new ObjectCacheIterator (this);

    }

    /**
     * Return a List of all the keys in the cache.
     *
     * @return The List (ArrayList) of all the keys in the cache.
     */
    public synchronized List keys ()
    {

	List l = new ArrayList (this.keys.size ());
	
	l.addAll (this.keys.keySet ());

	return l;


    }

    /**
     * Return a List of all the values in the cache.
     *
     * @return The List (ArrayList) of all the values in the cache.
     */
    public synchronized List values ()
    {

	List l = new ArrayList (this.data.size ());

	Iterator iter = this.data.keySet ().iterator ();

	while (iter.hasNext ())
	{

	    l.add (this.data.get (iter.next ()));

	}

	return l;

    }

    /**
     * Return a List of keys in the cache that match the conditions imposed
     * by the {@link GeneralFilter} AND are applied to the values NOT the keys.
     *
     * @param f The filter to use.
     * @return A List of the keys that map to the matched values.
     * @throws IllegalAccessException Thrown by the {@link GeneralFilter.accept(Object)}
     *                                method if we can't get access to a value defined in the
     *                                GeneralFilter for the value class.
     * @throws InvocationTargetException Thrown by the {@link GeneralFilter.accept(Object)}
     *                                   method if (as a result of accessing a field) an exception
     *                                   is thrown by a value object.
     * @throws FilterException Thrown by the {@link GeneralFilter.accept(Object)}
     *                         method if (as a result of accessing a field) the type of
     *                         object returned by the field access is not of the expected type.    
     */
    public synchronized List keysForFilteredValues (GeneralFilter f)
	                                            throws        IllegalAccessException,
	                                                          InvocationTargetException,
	                                                          FilterException
    {

	List ks = this.keys ();
	List retData = new ArrayList ();

	Class c = f.getFilterClass ();

	for (int i = 0; i < ks.size (); i++)
	{

	    Object k = ks.get (i);
	    Object v = this.get (k);

	    if (c.isAssignableFrom (v.getClass ()))
	    {

		if (f.accept (v))
		{

		    retData.add (k);

		}

	    }

	}

	return retData;		

    }

    /**
     * Return a List of all the values in the cache that match 
     * the conditions imposed by the {@link GeneralFilter} passed in.
     * We first gain all the values in the cache and then pass them
     * through the filter returning the values that match.  Because a GeneralFilter
     * can only filter on a single class type (but the values may not be of a single type)
     * we ignore any values that are not of the type specified for the GeneralFilter.
     *
     * @return A List (ArrayList) of all the values in the cache.
     * @throws IllegalAccessException Thrown by the {@link GeneralFilter.accept(Object)}
     *                                method if we can't get access to a value defined in the
     *                                GeneralFilter for the value class.
     * @throws InvocationTargetException Thrown by the {@link GeneralFilter.accept(Object)}
     *                                   method if (as a result of accessing a field) an exception
     *                                   is thrown by a value object.
     * @throws FilterException Thrown by the {@link GeneralFilter.accept(Object)}
     *                         method if (as a result of accessing a field) the type of
     *                         object returned by the field access is not of the expected type.    
     */
    public synchronized List values (GeneralFilter f)
	                             throws        IllegalAccessException,
	                                           InvocationTargetException,
	                                           FilterException
    {

	List vs = this.values ();
	List retData = new ArrayList ();

	Class c = f.getFilterClass ();

	for (int i = 0; i < vs.size (); i++)
	{

	    Object v = vs.get (i);

	    if (c.isAssignableFrom (v.getClass ()))
	    {

		if (f.accept (v))
		{

		    retData.add (v);

		}

	    }

	}

	return retData;	

    }

    /**
     * Return a List of all the keys in the cache that match 
     * the conditions imposed by the {@link GeneralFilter} passed in.
     * We first gain all the keys in the cache and then pass them
     * through the filter returning the keys that match.  Because a GeneralFilter
     * can only filter on a single class type (but the keys may not be of a single type)
     * we ignore any keys that are not of the type specified for the GeneralFilter.
     *
     * @return A List (ArrayList) of all the keys in the cache.
     * @throws IllegalAccessException Thrown by the {@link GeneralFilter.accept(Object)}
     *                                method if we can't get access to a value defined in the
     *                                GeneralFilter for the key class.
     * @throws InvocationTargetException Thrown by the {@link GeneralFilter.accept(Object)}
     *                                   method if (as a result of accessing a field) an exception
     *                                   is thrown by a key object.
     * @throws FilterException Thrown by the {@link GeneralFilter.accept(Object)}
     *                         method if (as a result of accessing a field) the type of
     *                         object returned by the field access is not of the expected type.    
     */
    public synchronized List keys (GeneralFilter f)
	                           throws        IllegalAccessException,
	                                         InvocationTargetException,
	                                         FilterException
    {

	List ks = this.keys ();
	List retData = new ArrayList ();

	Class c = f.getFilterClass ();

	for (int i = 0; i < ks.size (); i++)
	{

	    Object k = ks.get (i);

	    if (c.isAssignableFrom (k.getClass ()))
	    {

		if (f.accept (k))
		{

		    retData.add (k);

		}

	    }

	}

	return retData;

    }

    public synchronized void valuesToList (List list)
    {

	Iterator iter = this.keys.keySet ().iterator ();

	while (iter.hasNext ())
	{

	    Object key = iter.next ();
	    Key d = (Key) this.keys.get (key);
	    
	    Object value = this.data.get (d);

	    list.add (value);

	}

    }

    public synchronized void keysToList (List list)
    {

	Iterator iter = this.keys.keySet ().iterator ();

	while (iter.hasNext ())
	{

	    Object key = iter.next ();

	    list.add (key);

	}

    }

    public synchronized void toMap (Map map)
    {

	Iterator iter = this.keys.keySet ().iterator ();

	while (iter.hasNext ())
	{

	    Object key = iter.next ();
	    Key d = (Key) this.keys.get (key);

	    Object value = this.data.get (d);

	    map.put (key,
		     value);

	}

    }

    public synchronized void putAll (Map map)
    {

	Iterator iter = map.keySet ().iterator ();

	while (iter.hasNext ())
	{

	    Object key = iter.next ();

	    Object value = map.get (key);

	    Key d = new Key ();
	    d.key = key;

	    this.data.put (d,
			   value);
	    this.keys.put (key,
			   d);

	}

    }

    public boolean containsKey (Object key)
    {

	return this.keys.containsKey (key);

    }

    public synchronized void merge (ObjectCache cache)
    {

	Iterator iter = cache.iterator ();

	while (iter.hasNext ())
	{

	    Object key = iter.next ();

	    Date date = cache.getLastAccessTime (key);

	    Key d = new Key ();
	    d.key = key;
	    d.date.setTime (date.getTime ());

	    Object value = cache.get (key);

	    this.data.put (d,
			   value);
	    this.keys.put (key,
			   d);

	}

    }

    private boolean between (Date d,
			     Date from,
			     Date to)
    {

	boolean yes = false;

	if ((d.equals (from))
	    ||
	    (d.after (from))
	   )
	{

	    yes = true;

	}

	if (yes)
	{

	    yes = false;

	    if ((d.equals (to))
		||
		(d.before (to))
	       )
	    {

		yes = true;

	    } 

	}

	return yes;

    }

    Iterator keysIterator ()
    {

	return this.keys.keySet ().iterator ();

    }

    public synchronized ObjectCache cacheSliceTo (Date to)
    {

	return this.cacheSlice (new Date (0),
				to);

    }

    public synchronized ObjectCache cacheSliceFrom (Date from)
    {

	return this.cacheSlice (from,
				new Date (Long.MAX_VALUE));

    }

    public synchronized ObjectCache cacheSlice (Date from,
						Date to)
    {

	ObjectCache c = new ObjectCache (this.getPolicy ());
	c.setMaxSize (this.getMaxSize ());

	Iterator iter = this.keys.keySet ().iterator ();

	while (iter.hasNext ())
	{

	    Object key = iter.next ();

	    Key d = (Key) this.keys.get (key);

	    if (this.between (d.date,
			      from,
			      to))
	    {

		Object value = this.data.get (d);

		c.put (key,
		       value);
		
		c.setLastAccessTime (key,
				     d.date);

	    }

	}

	return c;

    }

    synchronized void setLastAccessTime (Object key,
					 Date   date)
    {

	if (this.keys.containsKey (key))
	{

	    Key d = (Key) this.keys.get (key);

	    d.date.setTime (date.getTime ());

	}

    }

    public synchronized Map sliceFrom (Date from)
    {

	return this.slice (from,
			   new Date (Long.MAX_VALUE));

    }

    public synchronized Map sliceTo (Date to)
    {

	return this.slice (new Date (0),
			   to);

    }

    public synchronized Map slice (Date from,
				   Date to)
    {

	Map map = new HashMap ();

	Iterator iter = this.keys.keySet ().iterator ();

	while (iter.hasNext ())
	{

	    Object key = iter.next ();
	    Key d = (Key) this.keys.get (key);

	    if (this.between (d.date,
			      from,
			      to))
	    {

		Object value = this.data.get (d);
		
		map.put (key,
			 value);

	    }

	}

	return map;

    }

    public Date getLastAccessTime (Object key)
    {

	return ((Key) this.keys.get (key)).date;

    }

    public boolean isEmpty ()
    {

	return this.keys.size () == 0;

    }

    public int capacity ()
    {

	if (this.maxSize == -1)
	{

	    return this.maxSize;

	}

	return this.maxSize - this.keys.size ();

    }

    public int getMaxSize ()
    {

	return this.maxSize;

    }

    public void setMaxSize (int size)
    {

	if (size < 1)
	{

	    return;

	}

	this.maxSize = size;

    }

    public Object get (Object key)
    {

	Key d = (Key) this.keys.get (key);

	if (d == null)
	{

	    return null;

	}

	d.touch ();

	return this.data.get (d);

    }

    public Object firstValue ()
    {

	Key d = (Key) this.keys.get (this.keys.firstKey ());

	d.touch ();

	return this.data.get (d);

    }

    public Object lastValue ()
    {

	Key d = (Key) this.keys.get (this.keys.lastKey ());

	d.touch ();

	return this.data.get (d);

    }

    public Object firstKey ()
    {
    
	return this.keys.firstKey ();

    }

    public int size ()
    {

	return this.keys.size ();

    }

    public synchronized void remove (Object key)
    {

	Key d = null;

	if (this.keys.containsKey (key))
	{

	    d = (Key) this.keys.get (key);

	    this.data.remove (d);

	    this.keys.remove (key);

	} 

    }

    public void resize (int size)
    {

	if (size > 0)
	{

	    this.maxSize = size;

	    this.resize ();

	}

    }

    protected synchronized void resize ()
    {

	if (this.maxSize > 0)
	{

	    while (this.keys.size () > this.maxSize)
	    {

		Object key = null;

		if (this.type == ObjectCache.OLDEST)
		{
		
		    key = this.keys.lastKey ();

		}
		
		if (this.type == ObjectCache.YOUNGEST)
		{

		    key = this.keys.firstKey ();

		}

		if (this.type == ObjectCache.RANDOM)
		{

		    if (this.random == null)
		    {

			this.random = new Random ();

		    }

		    int val = this.random.nextInt (this.keys.size ());

		    Object[] _keys = this.keys.keySet ().toArray ();

		    key = _keys[val];

		}

		Key d = (Key) this.keys.get (key);

		this.data.remove (d);

		this.keys.remove (key);
		
	    }

	}

    }

    public synchronized void put (Object key,
				  Object value)
    {

	Key d = null;

	// Check to see if we will go over our max size.
	this.resize ();

	if (this.keys.containsKey (key))
	{

	    d = (Key) this.keys.get (key);

	    d.touch ();

	} else {

	    d = new Key ();
	    d.key = key;

	    this.keys.put (key,
			   d);

	}

	this.data.put (d,
		       value);

    }

    /**
     * Clear our data structures.
     */
    public synchronized void flush ()
    {

	this.keys.clear ();
	this.data.clear ();

    }

    private class Key implements Comparable
    {

	private Object key = null;
	private Date date = new Date ();

	public int compareTo (Object o)
	{

	    Key k = (Key) o;

	    return this.date.compareTo (k.date);

	}

	public void touch ()
	{

	    this.date.setTime (System.currentTimeMillis ());

	}

	public boolean equals (Object o)
	{

	    Key k = (Key) o;

	    return this.key.equals (k.key)
		   &&
		   this.date.equals (k.date);

	}

	public int hasCode ()
	{

	    return key.hashCode () + date.hashCode ();

	}

    }

}
