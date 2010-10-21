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
import java.util.Map;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import java.lang.reflect.InvocationTargetException;

public class GeneralCollector
{

    public static final String KEYS = "KEYS";
    public static final String VALUES = "VALUES";

    private Class clazz = null;
    private String field = null;
    private List accessorChain = null;

    public GeneralCollector (Class c)
    {

	this.clazz = c;

    }

    public GeneralCollector (Class  c,
			     String field)
    {

	this (c);

	this.setField (field);

    }

    public void collect (Map    objects,
			 String type,
			 List   retData)
	                 throws IllegalArgumentException,
                                IllegalAccessException,
                                InvocationTargetException
    {

	Iterator iter = objects.entrySet ().iterator ();

	while (iter.hasNext ())
	{

	    Map.Entry obj = (Map.Entry) iter.next ();
	    Object key = obj.getKey ();
	    Object value = obj.getValue ();

	    if (type.equals (GeneralCollector.KEYS))
	    {

		if (!key.getClass ().isAssignableFrom (this.clazz))
		{
		    
		    throw new IllegalArgumentException ("Expected key object to be of type: " +
							this.clazz.getName () +
							", got: " +
							key.getClass ().getName ());
		    
		}

		retData.add (Accessor.getValueFromAccessorChain (key,
								 this.accessorChain));

	    } else {

		if (!value.getClass ().isAssignableFrom (this.clazz))
		{
		    
		    throw new IllegalArgumentException ("Expected value object to be of type: " +
							this.clazz.getName () +
							", got: " +
							value.getClass ().getName ());
		    
		}

		retData.add (Accessor.getValueFromAccessorChain (value,
								 this.accessorChain));

	    }

	}

    }

    public void collect (Map        objects,
			 String     type,
			 Collection retData)
	                 throws     IllegalArgumentException,
                                    IllegalAccessException,
                                    InvocationTargetException
    {

	Iterator iter = objects.entrySet ().iterator ();

	while (iter.hasNext ())
	{

	    Map.Entry obj = (Map.Entry) iter.next ();
	    Object key = obj.getKey ();
	    Object value = obj.getValue ();

	    if (type.equals (GeneralCollector.KEYS))
	    {

		if (!key.getClass ().isAssignableFrom (this.clazz))
		{
		    
		    throw new IllegalArgumentException ("Expected key object to be of type: " +
							this.clazz.getName () +
							", got: " +
							key.getClass ().getName ());
		    
		}

		retData.add (Accessor.getValueFromAccessorChain (key,
								 this.accessorChain));

	    } else {

		if (!value.getClass ().isAssignableFrom (this.clazz))
		{
		    
		    throw new IllegalArgumentException ("Expected value object to be of type: " +
							this.clazz.getName () +
							", got: " +
							value.getClass ().getName ());
		    
		}

		retData.add (Accessor.getValueFromAccessorChain (value,
								 this.accessorChain));

	    }

	}

    }

    public void collect (Map    objects,
			 String type,
			 Map    retData)
	                 throws IllegalArgumentException,
                                IllegalAccessException,
                                InvocationTargetException
    {

	Iterator iter = objects.entrySet ().iterator ();

	while (iter.hasNext ())
	{

	    Map.Entry obj = (Map.Entry) iter.next ();
	    Object key = obj.getKey ();
	    Object value = obj.getValue ();

	    if (type.equals (GeneralCollector.KEYS))
	    {

		if (!key.getClass ().isAssignableFrom (this.clazz))
		{
		    
		    throw new IllegalArgumentException ("Expected key object to be of type: " +
							this.clazz.getName () +
							", got: " +
							key.getClass ().getName ());
		    
		}

		retData.put (Accessor.getValueFromAccessorChain (key,
								 this.accessorChain),
			     value);

	    } else {

		if (!value.getClass ().isAssignableFrom (this.clazz))
		{
		    
		    throw new IllegalArgumentException ("Expected value object to be of type: " +
							this.clazz.getName () +
							", got: " +
							value.getClass ().getName ());
		    
		}

		retData.put (key,
			     Accessor.getValueFromAccessorChain (value,
								 this.accessorChain));

	    }

	}

    }

    public void collect (Collection objects,
			 Collection retData)
	                 throws     IllegalArgumentException,
                                    IllegalAccessException,
                                    InvocationTargetException
    {

	Iterator iter = objects.iterator ();

	while (iter.hasNext ())
	{

	    Object o = iter.next ();

	    if (!o.getClass ().isAssignableFrom (this.clazz))
	    {

		throw new IllegalArgumentException ("Expected object to be of type: " +
						    this.clazz.getName () +
						    ", got: " +
						    o.getClass ().getName ());

	    }

	    // Apply the fields...
	    retData.add (Accessor.getValueFromAccessorChain (o,
							     this.accessorChain));

	}

    }

    public void collect (Collection objects,
			 List       retData)
	                 throws     IllegalArgumentException,
                                    IllegalAccessException,
                                    InvocationTargetException
    {

	Iterator iter = objects.iterator ();

	while (iter.hasNext ())
	{

	    Object o = iter.next ();

	    if (!o.getClass ().isAssignableFrom (this.clazz))
	    {

		throw new IllegalArgumentException ("Expected object to be of type: " +
						    this.clazz.getName () +
						    ", got: " +
						    o.getClass ().getName ());

	    }

	    // Apply the fields...
	    retData.add (Accessor.getValueFromAccessorChain (o,
							     this.accessorChain));

	}

    }

    public void collect (List   objects,
			 List   retData)
	                 throws IllegalArgumentException,
                                IllegalAccessException,
                                InvocationTargetException
    {

	for (int i = 0; i < objects.size (); i++)
	{

	    Object o = objects.get (i);

	    if (!o.getClass ().isAssignableFrom (this.clazz))
	    {

		throw new IllegalArgumentException ("Expected object at index: " +
						    i +
						    " to be of type: " +
						    this.clazz.getName () +
						    ", got: " +
						    o.getClass ().getName ());

	    }

	    // Apply the fields...
	    retData.add (Accessor.getValueFromAccessorChain (o,
							     this.accessorChain));

	}

    }

    public void collect (List       objects,
			 Collection retData)
	                 throws     IllegalArgumentException,
	                            IllegalAccessException,
                                    InvocationTargetException
    {

	for (int i = 0; i < objects.size (); i++)
	{

	    Object o = objects.get (i);

	    if (!o.getClass ().isAssignableFrom (this.clazz))
	    {

		throw new IllegalArgumentException ("Expected object at index: " +
						    i +
						    " to be of type: " +
						    this.clazz.getName () +
						    ", got: " +
						    o.getClass ().getName ());

	    }

	    // Apply the fields...
	    retData.add (Accessor.getValueFromAccessorChain (o,
							     this.accessorChain));

	}

    }

    /**
     * Set the field that we collect if you readd the same field then
     * the type is just updated.  
     *
     * @param field The field to sort on.
     * @throws IllegalArgumentException If we can't find the field in the
     *                                  class/class chain passed into the constructor.
     */
    public void setField (String field)
                          throws IllegalArgumentException
    {

	// Find the field...
	this.field = field;

	this.accessorChain = Accessor.getAccessorChain (field,
							this.clazz);

    }

    /**
     * Get a field given a field name.
     *
     * @return The field or null if the field hasn't been set yet.
     */
    public String getField ()
    {

	return this.field;

    }

}
