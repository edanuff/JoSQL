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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.activation.DataSource;

/**
 * A Class for handling a gzipped file as a DataSource.
 * <br /><br />
 * <h4>Description Contents</h4>
 * <ul>
 *   <li><a href="#1">Outline</a></li>
 *   <li><a href="#2">Zip file extension</a></li>
 *   <li><a href="#3">Content Types</a><br />
 *     <ul>
 *       <li><a href="#3.1">Default content type</a></li>
 *     </ul>
 *   </li>
 *   <li><a href="#4">Stream Buffering</a></li>
 *   <li><a href="#5">Thread safety and reuse</a></li>
 * </ul>
 * <h4><a name="1">Outline</a></h4>
 * This class providers a wrapper around a file such that GZIPed files will 
 * be unzipped when the {@link #getInputStream()} method is called.  Or 
 * that the file will be zipped with the named zip file extension when
 * the {@link #getOutputStream()} method is called.
 * <br /><br />
 * <h4><a name="2">Zip file extension</a></h4>
 * <p>
 * When used as an <b>output</b> this class expects your file to be gzipped 
 * and be able to be unzipped using a GZIPInputStream.
 * <br /><br />
 * When used an an <b>input</b> we use a GZIPOutputStream to write the file 
 * in a gzipped form.
 * <br /><br />
 * For the content type we only support a number of different content types
 * and this is based upon the file extension <b>without any zip file extension</b>.
 * What this means is that if you have a File called:
 * </p>
 * <pre>
 *   myFile.txt.gz
 * </pre>
 * <p>
 * And you call the {@link #getName()} method then the name:
 * </p>
 * <pre>
 *   myFile.txt
 * </pre>
 * <p>
 * Will be returned and the content type would be: <b>text/plain</b>.
 * <br /><br />
 * If you don't specify a <b>zipExtension</b> then a default of <b>.gz</b> is
 * used.
 * </p>
 * <h4><a name="3">Content Types</a></h4>
 * <p>
 * The file extensions we support and their content types are listed below 
 * (in other words does the file name end with the value specified):
 * </p>
 * <table border="2" cellpadding="2">
 *   <tr>
 *     <th>File Extension</th><th>Content Type Returned</th>
 *   </tr>
 *   <tr>
 *     <td align="center">.doc</td>
 *     <td align="center">application/msword</td>
 *   </tr>
 *   <tr>
 *     <td align="center">.sdw</td>
 *     <td align="center">application/x-swriter</td>
 *   </tr>
 *   <tr>
 *     <td align="center">.rtf</td>
 *     <td align="center">text/rtf</td>
 *   </tr>
 *   <tr>
 *     <td align="center">.html</td>
 *     <td align="center">text/html</td>
 *   </tr>
 *   <tr>
 *     <td align="center">.txt</td>
 *     <td align="center">text/plain</td>
 *   </tr>
 * </table>
 * <p>
 * It should be noted that file extensions are case-insensitive, this is because
 * on Windows platforms <b>.doc</b> and <b>.DOC</b> are treated the same.
 * <br /><br />
 * If you know of any other content types/file extensions that <b>MUST</b>
 * be supported directly by this class then please contact <b>code-monkey&#x40;gentlyweb.com</b>
 * with details.
 * </p>
 * <h5><a name="3.1">Default content type</a></h5>
 * <p>
 * If the file extension does not match any of the "pre-defined" file extensions
 * then a content type of <b>application/octet-stream</b> is returned.
 * </p>
 * <h4><a name="4">Stream Buffering</a></h4>
 * <p>
 * Both the input and output streams that you gain from this class are
 * buffered.
 * </p>
 * <h4><a name="5">Thread safety and reuse</a></h4>
 * <p>
 * It is possible to reuse this class by setting a new File via the
 * {@link #setFile(File)} method and potentially a different zip extension
 * via the {@link #setZipExtension(String)}.  
 * <br /><br />
 * This class is NOT Thread safe and you should synchronize externally if
 * you plan to use multiple Threads with it.
 * </p>
 */
public class GZIPFileDataSource implements DataSource
{

    private File file = null;
    private String zipExtension = ".gz";

    /**
     * Create a new data source for the specified file.
     * <p>
     * We expect the file to have the file extension as given by <b>zipExtension</b>.
     * </p>
     *
     * @param f The File.
     * @param zipExtension The file extension for gzipped files.  Set to <code>null</code>
     *                     to use the default of <b>.gz</b>.
     */
    public GZIPFileDataSource (File   f,
			       String zipExtension)
    {

	this.file = f;

	if (zipExtension != null)
	{

	    this.zipExtension = zipExtension.toLowerCase ();

	}

    }

    /**
     * Get the content type for this data source.
     * <p>
     * We base the content type on the file extension of the file minus the
     * <b>zipExtension</b>, so if a file is called <b>myFile.txt.gz</b> and
     * the zip extension is <b>.gz</b> then we trim off the <b>.gz</b> and
     * then look for the "real" file extension, then determine the
     * appropriate content type and return it.
     * <br /><br />
     * You should note that the file <b>DOESN'T</b> have to have the
     * zipExtension for this method to work.
     * <br /><br />
     * If we don't have a specific file extension to use (see the
     * table of <a href="#3">content type to file extension mappings</a> for full details
     * of what this method returns.
     *
     * @return The content type based upon the file extension of the file, or 
     *         <b>application/octet-stream</b> if we don't recognise the file extension.
     */
    public String getContentType ()
    {

	String fName = this.file.getName ().toLowerCase ();

	// Strip off any gz file extension...
	if (fName.indexOf (this.zipExtension) != -1)
	{
	    
	    fName = fName.substring (0,
				     fName.indexOf (this.zipExtension));
	    
	}
	
	if (fName.endsWith (".doc"))
	{
	    
	    return "application/msword";
	    
	}
	
	if (fName.endsWith (".sdw"))
	{

	    return "application/x-swriter";

	}
	
	if (fName.endsWith (".rtf"))
	{
	    
	    return "text/rtf";

	}

	if (fName.endsWith (".html"))
	{
	    
	    return "text/html";

	}

	if (fName.endsWith (".txt"))
	{

	    return "text/plain";

	}	

	return "application/octet-stream";

    }

    /**
     * Get an appropriate InputStream for the data source.
     * <br /><br />
     * Here we just return a buffered GZIPInputStream.
     *
     * @return The InputStream for the data source.
     * @throws IOException If we can't get the stream to the source.
     */
    public InputStream getInputStream ()
                                       throws IOException
    {

	return new GZIPInputStream (new BufferedInputStream (new FileInputStream (this.file)));

    }
    /**
     * Get an appropriate OutputStream for the data source.
     * <br /><br />
     * Here we just return a buffered GZIPOutputStream.
     *
     * @return The OutputStream for the data source.
     * @throws IOException If we can't get the stream to the source.
     */
    public OutputStream getOutputStream ()
                                         throws IOException
    {

	return new GZIPOutputStream (new BufferedOutputStream (new FileOutputStream (this.file)));

    }

    /**
     * Set the zip extension to use.
     *
     * @param ext The zip extension.
     */
    public void setZipExtension (String ext)
    {

	this.zipExtension = ext;

    }

    /**
     * Set the File to use for the data source.
     *
     * @param file The file.
     */
    public void setFile (File file)
    {

	this.file = file;

    }

    /**
     * Get the name of the data source.
     * <br /><br />
     * If the file ends with the <b>zipExtension</b> then we strip that off
     * before returning the name.
     *
     * @return The name of the data source.
     */
    public String getName ()
    {

	String fName = this.file.getName ().toLowerCase ();

	// Strip off any gz file extension...
	if (fName.indexOf (this.zipExtension) != -1)
	{
	    
	    fName = fName.substring (0,
				     fName.indexOf (this.zipExtension));
	    
	}

	return fName;

    }

}
