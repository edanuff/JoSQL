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

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.Iterator;

public class Timings
{

    public static final int TIME_ORDERED = 0;
    public static final int RANDOM = 1;
    public static final int KEY_ORDERED = 2;

    private Map ts = null;

    public Timings (int type)
    {

	if (type == Timings.TIME_ORDERED)
	{

	    this.ts = new LinkedHashMap ();
	    return;

	}

	if (type == Timings.KEY_ORDERED)
	{

	    this.ts = new TreeMap ();
	    return;

	}

	this.ts = new HashMap ();

    }

    public Iterator iterator ()
    {

	if (this.ts instanceof TreeMap)
	{

	    Map m = new TreeMap (this.ts);

	    return m.keySet ().iterator ();

	}

	if (this.ts instanceof LinkedHashMap)
	{

	    Map m = new LinkedHashMap (this.ts);

	    return m.keySet ().iterator ();

	}

	Map m = new HashMap (this.ts);

	return m.keySet ().iterator ();

    }

    public void stop (Object k,
		      Date   d)
    {

	Timing t = (Timing) this.ts.get (k);

	if (t != null)
	{

	    t.stop (d);

	}	

    }

    public void stop (Object k,
		      long   d)
    {

	Timing t = (Timing) this.ts.get (k);

	if (t != null)
	{

	    t.stop (d);

	}	

    }

    public void stop (Object k)
    {

	Timing t = (Timing) this.ts.get (k);

	if (t != null)
	{

	    t.stop ();

	}	

    }

    public void start (Object k)
    {

	Timing t = (Timing) this.ts.get (k);

	if (t == null)
	{

	    t = new Timing ();
	    this.ts.put (k,
			 t);

	}

    }

    public void start (Object k,
		       long   s)
    {

	Timing t = (Timing) this.ts.get (k);

	if (t == null)
	{

	    t = new Timing (s);
	    this.ts.put (k,
			 t);

	}

    }

    public Timing remove (Object k)
    {

	return (Timing) this.ts.remove (k);

    }

    public Timing get (Object k)
    {

	return (Timing) this.ts.get (k);

    }

    public TimeDuration getDuration (Object k)
    {

	Timing t = this.get (k);

	if (t == null)
	{

	    return null;

	}

	return t.getDuration ();

    }

    public long getDurationAsLong (Object k)
    {

	Timing t = this.get (k);

	if (t == null)
	{

	    return 0;

	}

	return t.getDuration ().rollUpToMillis ();

    }

    public void start (Object k,
		       Date   s)
    {

	Timing t = (Timing) this.ts.get (k);

	if (t == null)
	{

	    t = new Timing (s);
	    this.ts.put (k,
			 t);

	}

    }

}
