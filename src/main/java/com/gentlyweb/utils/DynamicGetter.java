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

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * This class is used to perform access into a Java object using a 
 * String value with a specific notation.  This class differs from the {@link Getter}
 * class in that instead of creating the chain of methods when the getter is
 * instantiated it will instead get the <b>actual</b> method from the object passed in.
 */
public class DynamicGetter
{

    private List chain = new ArrayList ();

    private Class clazz = null;
    private Object origObj = null;
    private Object origObjRes = null;
    private int cs = 0;

    /**
     * Get the getter associated with the named reference.  Return
     * null if there isn't one, or if we can't access it.
     *
     * @param ref The reference for the getter.
     * @param obj The Object to build up the getter from.
     */
    public DynamicGetter (String ref,
			  Object obj)
	                  throws IllegalArgumentException,
                                 IllegalAccessException,
				 InvocationTargetException
    {

	this.clazz = obj.getClass ();

	this.origObj = obj;

	StringTokenizer t = new StringTokenizer (ref,
						 ".");

	Class c = obj.getClass ();

	while (t.hasMoreTokens ())
	{

	    String tok = t.nextToken ();

	    // Create a Getter for this.
	    Getter g = new Getter (tok,
				   c);

	    this.chain.add (g);

	    c = g.getType ();

	}

	this.cs = this.chain.size ();

    }

    public Class getBaseClass ()
    {

	return this.clazz;

    }

    /**
     * Get the class of the type of object we would return from the {@link #getValue(Object)}
     * method.
     *
     * @return The class.
     */
    public Class getType ()
    {

	return ((Getter) this.chain.get (this.cs - 1)).getType ();

    }

    public Object getValue (Object obj)
	                    throws IllegalAccessException,
                                   InvocationTargetException
    {
	
	// If the object is null then return null.
	if (obj == null)
	{

	    return null;

	}

	if (this.origObj == obj)
	{

	    return this.origObjRes;

	}

	// For our accessor chain, use the Field and Methods
	// to get the actual value.
	Object retdata = obj;
	
	for (int i = 0; i < this.cs; i++)
	{

	    Getter g = (Getter) this.chain.get (i);

	    retdata = g.getValue (retdata);

	}
	
	return retdata;
	
    }

}
