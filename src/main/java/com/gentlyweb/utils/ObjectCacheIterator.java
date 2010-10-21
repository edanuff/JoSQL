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

import java.util.Iterator;

public class ObjectCacheIterator implements Iterator
{

    private Iterator iter = null;

    public ObjectCacheIterator (ObjectCache cache)
    {
	
	this.iter = cache.keysIterator ();

    }

    public void remove ()
    {

	throw new UnsupportedOperationException ("Remove not supported for ObjectCaches.");

    }

    public Object next ()
    {

	return this.iter.next ();

    }

    public boolean hasNext ()
    {

	return this.iter.hasNext ();

    }

}
