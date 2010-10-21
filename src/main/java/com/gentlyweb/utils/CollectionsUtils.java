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
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;

import java.lang.reflect.InvocationTargetException;

public class CollectionsUtils
{

    /**
     * Given a Map return the entries in the Map in the passed in
     * List in order.
     *
     * @param map The Map to use.
     * @param list The List to fill up.
     */
    public static void getMapEntriesAsOrderedList (Map  map,
						   List list)
    {

	// Get all the keys from the Map as an Array.
	Object[] keys = map.keySet ().toArray ();

	// Do a natural sort.
	Arrays.sort (keys);

	// Cycle over them and pull out the associated value
	// from the Map placing it into the List.
	for (int i = 0; i < keys.length; i++)
	{

	    list.add (map.get (keys[i]));

	}

    }

    /**
     * Get all the entries in the Map object out and place into
     * the List passed in, just add at the bottom.
     *
     * @param map The Map to get objects out of.
     * @param list The List to add objects to the end of.  We add the
     *             Map.Entry.
     */
    public static void addMapEntriesToList (Map  map,
					    List list)
    {

	Iterator iter = map.entrySet ().iterator ();

	while (iter.hasNext ())
	{

	    list.add (((Map.Entry) iter.next ()).getValue ());

	}

    }

    public static List convertToRows (List objs,
				      int  rowSize)
    {

	List retData = new ArrayList ();

	if ((rowSize == 0)
	    ||
	    (rowSize >= objs.size ())
	   )
	{

	    retData.add (objs);

	    return retData;

	}

	int count = 0;

	List row = new ArrayList ();

	for (int i = 0; i < objs.size (); i++)
	{

	    row.add (objs.get (i));

	    count++;

	    if (count == rowSize)
	    {

		// We need a new row, add this row to our return data.
		retData.add (row);

		row = new ArrayList ();

		count = 0;

	    }

	}

	while (count != rowSize)
	{

	    row.add (null);

	    count++;

	}

	retData.add (row);

	return retData;

    }

    /**
     * Given a List of objects, convert the values it contains to the passed in Map.
     * The <b>keyAccessor</b> and <b>valueAccessor</b> parameters should
     * be {@link Getter accessors} and specify the key and value for the
     * map respectively.  Set <b>valueAccessor</b> to <code>null</code> to mean that
     * the object from the List should be added as the value.  Note: all the objects
     * in the list MUST be of the same type, to determine the class the first object
     * in the list is examined.
     *
     * @param objs The objects to convert.
     * @param map The Map to add the objects to.
     * @param keyAccessor The accessor for the key to set in the map.
     * @param valueAccessor The accessor for the value to set in the map, set to <code>null</code>
     *                      to get the object added.
     */
    public static void convertListToMap (List   objs,
					 Map    map,
					 String keyAccessor,
					 String valueAccessor)
	                                 throws IllegalAccessException,
	                                        InvocationTargetException,
                                                ClassCastException
    {

	if ((objs == null)
            ||
	    (map == null)
	   )
	{

	    return;

	}

	if (objs.size () == 0)
	{

	    return;

	}

	Class c = objs.get (0).getClass ();

	Getter ka = new Getter (keyAccessor,
				c);
	Getter va = null;

	if (valueAccessor != null)
	{

	    va = new Getter (valueAccessor,
			     c);

	}

	for (int i = 0; i < objs.size (); i++)
	{

	    Object o = objs.get (i);

	    Object k = ka.getValue (o);

	    Object v = null;

	    if (va == null)
	    {

		v = o;

	    } else {

		v = va.getValue (o);

	    }

	    map.put (k,
		     v);

	}

    }

    /**
     * Add all the entries in the List as keys in the specified map.
     * Set "keyIsValue" to <code>true</code> to put the entry from
     * the List as the value for the map as well, if it's set to
     * <code>false</code> then we use "".
     *
     * @param list The List to get entries from.
     * @param map The Map to add entries to.
     * @param keyIsValue Indicate whether we should use the List entry
     *                   as the value as well or "".
     */
    public static void addListEntriesToMap (List    list,
					    Map     map,
					    boolean keyIsValue)
    {

	for (int i = 0; i < list.size (); i++)
	{

	    if (keyIsValue)
	    {

		map.put (list.get (i),
			 list.get (i));

	    } else {

		map.put (list.get (i),
			 "");

	    }

	}

    }

}
