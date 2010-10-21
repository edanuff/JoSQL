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

import java.io.File;
import java.util.EventObject;

public class FileChangeEvent extends EventObject
{

    public static final int EXISTS = 1;
    public static final int NOT_EXISTS = 2;
    public static final int MODIFIED = 4;
    public static final int HIDDEN = 8;
    public static final int NOT_HIDDEN = 16;
    public static final int LENGTH_CHANGED = 32;
    public static final int READABLE = 64;
    public static final int NOT_READABLE = 128;
    public static final int WRITEABLE = 256;
    public static final int NOT_WRITEABLE = 512;
    public static final int FILE_TYPE_CHANGE = 1024;

    private FileDetails newD = null;
    private FileDetails oldD = null;
    private int types = -1;

    public FileChangeEvent (File        file,
			    FileDetails newDetails,
			    FileDetails oldDetails,
			    int         eventTypes)
    {

	super (file);

	this.newD = newDetails;
	this.oldD = oldDetails;
	this.types = eventTypes;

    }
    
    public boolean hasEvent (int type)
    {

	if ((this.types & type) > 0)
	{

	    return true;

	}

	return false;

    }

    public int getEventTypes ()
    {

	return this.types;

    }

    public FileDetails getOldFileDetails ()
    {

	return this.oldD;

    }

    public FileDetails getNewFileDetails ()
    {

	return this.newD;

    }

    public File getFile ()
    {

	return (File) this.getSource ();

    }

}
