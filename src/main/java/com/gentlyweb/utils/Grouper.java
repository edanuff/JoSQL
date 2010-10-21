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
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class Grouper
{

    private List getters = new ArrayList ();
    private Class c = null;

    public Grouper (Class c)
    {

	this.c = c;

    }

    public void addGroupBy (Getter get)
	                    throws IllegalArgumentException
    {

	// Get the class that the getter relates to, they MUST be the same class AND the
	// same object... classes loaded via other classloaders are not compatible.
	if (get.getBaseClass ().hashCode () != this.c.hashCode ())
	{

	    throw new IllegalArgumentException ("Class in Getter is: " + 
						get.getBaseClass ().getName () +
						" with hashCode: " +
						get.getBaseClass ().hashCode () +
						" which is incompatible with comparator class: " +
						this.c.getName () + 
						" with hashCode: " +
						this.c.hashCode ());

	}

	this.getters.add (get);

    }

    public void addGroupBy (String on)
    {

	this.getters.add (new Getter (on,
				      this.c));

    }

    public Map group (List   items)
	              throws ChainException
    {

	Map groups = new HashMap ();

	for (int j = 0; j < items.size (); j++)
	{

	    Object o = items.get (j);

	    boolean match = true;

	    List key = new ArrayList ();

	    for (int i = 0; i < this.getters.size (); i++)
	    {

		// Ensure that all getters match (equals = true)

		// Get the value.
		Object val = null;

		try
		{

		    val = ((Getter) this.getters.get (i)).getValue (o);

		} catch (Exception e) {
		    
		    throw new ChainException ("Unable to get value for accessor from class: " +
					      o.getClass ().getName (),
					      e);

		}

		key.add (val);

	    }

	    List g = (List) groups.get (key);

	    if (g == null)
	    {

		g = new ArrayList ();

		groups.put (key,
			    g);

	    } 

	    g.add (o);

	}

	return groups;

    }

    public List groupSortByGroupSize (List   items,
				      String ascDesc)
                                      throws ChainException
    {

	Map m = this.group (items);

	List nSearches = new ArrayList ();
	CollectionsUtils.addMapEntriesToList (m,
					      nSearches);

	GeneralComparator gc = new GeneralComparator (List.class);

	gc.addField ("size",
		     ascDesc);

	Collections.sort (nSearches,
			  gc);	

	return nSearches;

    }

}
