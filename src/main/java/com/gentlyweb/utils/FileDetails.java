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
import java.io.IOException;

public class FileDetails
{
    
    private boolean exists = false;
    private boolean readable = false;
    private boolean writeable = false;
    private long length = 0;
    private boolean hidden = false;
    private boolean file = false;
    private long lastModified = 0;
    private String name = null;
    private String path = null;
    private File f = null;
    
    public FileDetails (File   file)
    {
	
	if (file.exists ())
	{
	    
	    this.exists = true;
	    
	    this.length = file.length ();
	    this.lastModified = file.lastModified ();
	    
	    this.name = file.getName ();
	    this.path = file.getPath ();
	    
	    if (file.isFile ())
	    {
		
		this.file = true;
		
	    }
	    
	    if (file.isHidden ())
	    {
		
		this.hidden = true;
		
	    }
	    
	    if (file.canWrite ())
	    {
		
		this.writeable = true;
		
	    }
	    
	    if (file.canRead ())
	    {
		
		this.readable = true;
		
	    }
	    
	}
	
    }

    public File getFile ()
    {

	return this.f;

    }

    public boolean isDirectory ()
    {

	return !this.file;

    }

    public boolean isFile ()
    {

	return this.file;

    }

    public boolean canWrite ()
    {

	return this.writeable;	

    }

    public boolean canRead ()
    {

	return this.readable;

    }

    public long getLength ()
    {

	return this.length;

    }

    public boolean isHidden ()
    {

	return this.hidden;

    }

    public long lastModified ()
    {

	return this.lastModified;

    }

    public boolean exists ()
    {

	return this.exists;

    }
    
}
