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

import java.util.Map.Entry;

public class ItemPair implements Entry
{

    private Object key = null;
    private Object value = null;

    public ItemPair (Object key,
		     Object value)
    {

	this.key = key;
	this.value = value;

    }

    /**
     * This method just uses the algorithm defined in the Map.Entry interface.
     *
     * @return The hash code.
     */
    public int hashCode ()
    {

	int k = 0;
	int v = 0;

	if (this.key != null)
	{

	    k = this.value.hashCode ();

	} 

	if (this.value != null)
	{

	    v = this.value.hashCode ();

	}

	return k ^ v;

    }

    public Object getKey ()
    {

	return this.key;

    }

    public Object setValue (Object value)
    {

	Object old = this.value;
	this.value = value;

	return old;

    }

    public Object getValue ()
    {

	return this.value;

    }

    /**
     * This method just uses the algorithm defined in the Map.Entry interface.
     *
     * @return The hash code.
     */
    public boolean equals (Object o)
    {

	if (!(o instanceof ItemPair))
	{

	    return false;

	}

	ItemPair ipo = (ItemPair) o;

	if ((ipo.getKey ().equals (this.key))
	    &&
	    (ipo.getValue ().equals (this.value))
	   )
	{

	    return true;

	}
	    
	return false;

    }

}
