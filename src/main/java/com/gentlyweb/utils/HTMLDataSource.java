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

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import javax.activation.DataSource;

public class HTMLDataSource implements DataSource
{

    private String html;         

    public HTMLDataSource (String html) 
    {

	this.html = html;
    
    }

    public InputStream getInputStream () 
                                       throws IOException 
    {

	if (this.html == null) 
	{

	    throw new IOException ("No HTML provided");

	}
	
	return new BufferedInputStream (new ByteArrayInputStream (this.html.getBytes()));
    
    }

    public OutputStream getOutputStream () 
                                         throws IOException 
    {

	throw new IOException ("Output not supported");

    }

    public String getContentType() 
    {

	return "text/html; charset=\"iso-8859-1\"";
        
    }
    
    public String getName() 
    {
    
        return "HTML DataSource for sending only";
        
    }

}
