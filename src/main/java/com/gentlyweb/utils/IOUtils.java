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
import java.io.OutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import java.util.zip.GZIPOutputStream;

public class IOUtils
{

    /**
     * Copy a file from one location to a new location.
     *
     * @param oldFile The old file name.
     * @param newFile The new file location.
     * @param bufSize The buffer size to use when performing the copy.
     * @throws IOException If we can't perform the copy.
     */
    public static void copyFile (File   oldFile,
				 File   newFile,
				 int    bufSize)
                                 throws IOException
    {

	BufferedInputStream bin = null;
	BufferedOutputStream bout = null;
	
	try
	{
	    
	    bin = new BufferedInputStream (new FileInputStream (oldFile));
	    bout = new BufferedOutputStream (new FileOutputStream (newFile));

	    IOUtils.streamTo (bin,
			      bout,
			      bufSize);

	    /*
	    byte buf[] = new byte[bufSize];

	    int bRead = -1;

	    while ((bRead = bin.read (buf)) != -1)
	    {

		bout.write (buf,
			    0,
			    bRead);

	    }
	    */

	} finally {

	    if (bin != null)
	    {

		bin.close ();

	    }

	    if (bout != null)
	    {

		bout.flush ();
		bout.close ();

	    }

	}

    }

    /**
     * GZIP a file, this will move the file from the
     * given name to the new location.  This leaves the
     * old file in place!
     *
     * @param file The existing file name of the file.
     * @param newFile The new file location for the file.
     * @throws ChainException If we can't perform the transfer, the inner exception will
     *                        contain an IOException that is the "real" exception.
     */
    public static void gzipFile (File   file,
				 File   newFile)
                                 throws ChainException
    {

	// GZIP the file...
	try
	{
		
	    BufferedInputStream oldFileRead = new BufferedInputStream (new FileInputStream (file));
		
	    // Get a GZIP output stream to the new CV file...
	    GZIPOutputStream gzout = new GZIPOutputStream (new BufferedOutputStream (new FileOutputStream (newFile)));
		
	    try
	    {
		    
		// Read the bytes from the old and write to the new.
		byte[] buf = new byte[4096];

		IOUtils.streamTo (oldFileRead,
				  gzout,
				  4096);
		
		/*
		int bytesRead = 0;
		
		while ((bytesRead = oldFileRead.read (buf, 0, buf.length)) != -1)
		{
		    
		    gzout.write (buf, 
				 0,
				 bytesRead);
		    
		}
		*/		

	    } catch (IOException e) {

		throw new ChainException ("Unable to gzip file: " + file.getPath () + " to: " + newFile.getPath (),
					  e);
		    
	    } finally {
		    
		if (oldFileRead != null)
		{
		    
		    oldFileRead.close ();
		    
		}
		
		if (gzout != null)
		{
		    
		    gzout.flush ();
		    gzout.close ();
		    
		} 
		
	    }
	    
	} catch (IOException e) {
	    
	    throw new ChainException ("Unable to gzip file: " + file.getPath () + " to: " + newFile.getPath (),
				      e);

	}

    }

    /**
     * Get a file length as kilobytes in the form x.y.
     *
     * @param length The length of the file.
     * @return A String formatted as x.y.
     */
    public static String getFileLengthAsFormattedKilobytes (long length)
    {

	long kilos = length / 1024;
	
	long tenths = (length - (kilos * 1024)) / 100;

	return kilos + "." + tenths;

    }

    /**
     * Get a file length as formatted string.
     * <ul>
     *   <li>If the long is > 1024 * 1024 then we return the size as X.YY MB</li>
     *   <li>If the long is > 1024 then we return the size as X.YY KB.</li>
     *   <li>If the long is < 1024 then we return the size as XXXX B.</li>
     * </ul>
     * 
     * @param length The length of the file.
     * @return A String formatted as indicated above.
     */
    public static String getFormattedFileLength (long length)
    {

	long meg = 1024 * 1024;

	String val = "B";

	String num = "";

	if (length >= meg)
	{

	    val = "MB";

	}

	if ((length >= 1024)
	    &&
	    (length <= meg)
	   )
	{

	    val = "KB";

	}

	if (length >= meg)
	{

	    length = length / 1024;

	}

	if (length >= 1024)
	{

	    long m = length / 1024;
	    
	    long n = (length - (m * 1024)) / 100;
	    
	    while (String.valueOf (n).length () > 2)
	    {

		n = n / 100;

	    }

	    return m + "." + n + " " + val;

	} else {

	    return length + " " + val;

	}

    }

    /**
     * This method acts as a "join" between an input and an output stream, all it does is
     * take the input and keep reading it in and sending it directly to the output using the
     * specified buffer size.  In this way you can join any input and output streams to pass
     * the data between them.  Note: this method does NOT flush or close either stream (this is
     * to allow the "channel" to remain open and re-used.
     *
     * @param in The input stream.
     * @param out The output stream.
     * @param bufSize The buffer size.
     * @throws IOException If an IO exception occurs.
     */
    public static void streamTo (InputStream  in,
				 OutputStream out,
				 int          bufSize)
				 throws       IOException
    {

	byte buf[] = new byte[bufSize];
	
	int bRead = -1;
	
	while ((bRead = in.read (buf)) != -1)
	{

	    out.write (buf,
		       0,
		       bRead);

	}	

    }

    /** 
     * Write the given bytes to a file, note that this method will just overwrite
     * any existing file.  
     *
     * @param file The file to write to.
     * @param bytes The byte array.
     * @param bufSize The size of output buffer to use, set to -1 to have the buffer size set to
     *                bytes.length.
     * @throws IOException If the array cannot be written to the file.
     */
    public static void writeBytesToFile (File   file,
					 byte[] bytes,
					 int    bufSize)
					 throws IOException
    {

	if (bufSize <= 0)
	{

	    bufSize = bytes.length;

	}

	BufferedInputStream bin = null;
	BufferedOutputStream bout = null;
	
	try
	{
	    
	    bin = new BufferedInputStream (new ByteArrayInputStream (bytes));
	    bout = new BufferedOutputStream (new FileOutputStream (file));

	    IOUtils.streamTo (bin,
			      bout,
			      bufSize);

	} finally {

	    if (bin != null)
	    {

		bin.close ();

	    }

	    if (bout != null)
	    {

		bout.flush ();
		bout.close ();

	    }

	}

    }

    /** 
     * Write the given bytes to a file, note that this method will just overwrite
     * any existing file.  For more control over the output buffer size use: {@link IOUtils#writeBytesToFile(File,byte[],int)}.
     *
     * @param file The file to write to.
     * @param bytes The byte array.
     * @throws IOException If the array cannot be written to the file.
     */
    public static void writeBytesToFile (File   file,
					 byte[] bytes)
					 throws IOException
    {

	IOUtils.writeBytesToFile (file,
				  bytes,
				  bytes.length);

    }

    /**
     * Write the given String to the File.
     *
     * @param file The file to write to.
     * @param str The value to write.
     * @throws IOException This should never happen because PrintWriter will
     *                     catch it.
     */
    public static void writeStringToFile (File    file,
					  String  str,
					  boolean compress)
	                                  throws  IOException
    {

	OutputStream out = new BufferedOutputStream (new FileOutputStream (file));

	if (compress)
	{

	    out = new GZIPOutputStream (out);

	}


	PrintWriter pout = new PrintWriter (out);

	pout.print (str);
	pout.flush ();
	pout.close ();

    }

    /**
     * Get the contents of a File as a String.  Remember that this method is
     * constrained by the underlying limits of an array size, since that can only
     * be as big as Integer.MAX_VALUE any file bigger than that cannot be returned.
     * Although why you would want to read a file into memory bigger than 2GB is anyone's
     * guess.
     *
     * @param file The file to read in.
     * @return The content of the file as a String.
     * @throws IOException If there is a problem with the read.
     * @throws IllegalArgumentException If the file length is greater than 2<sup>32</sup>-1 since
     *                                  the maximum size of an array is (max)int - 1.
     */
    public static String getFile (File   file)
                                  throws IOException
    {

	return new String (IOUtils.getFileAsArray (file));

    }

    /**
     * Get the contents of a File as a byte array.  We use a BufferedInputStream
     * and read the entire file in one go.
     *
     * @param file The file to read in.
     * @return The content of the file as a byte array.
     * @throws IOException If there is a problem with the read.
     * @throws IllegalArgumentException If the file length is greater than 2<sup>32</sup>-1 since
     *                                  the maximum size of an array is (max)int - 1.
     */
    public static byte[] getFileAsArray (File   file)
                                         throws IOException
    {

	if (file == null)
	{

	    throw new IOException ("No file specified!");

	}

	if (!file.exists ())
	{

	    throw new IOException ("File: " + file + " does not exist.");

	}

	long length = file.length ();

	if (length > Integer.MAX_VALUE)
	{

	    throw new IllegalArgumentException ("File: " +
						file.getPath () +
						" is greater than maximum value: " +
						Integer.MAX_VALUE);

	}

	BufferedInputStream bin = null;

	byte[] chars = new byte[(int) length];

	try
	{

	    bin = new BufferedInputStream (new FileInputStream (file));
	    bin.read (chars, 
		      0,
		      (int) length);
	    
	} finally {

	    if (bin != null)
	    {

		bin.close ();

	    }

	}

	return chars;

    }

    /**
     * Ask a question of the User on stdout and wait for a response.
     * <br /><br />
     * Take a positive as either "", "y|Y" or "yes|YES" or any case value for
     * <b>yes</b> and return <code>true</code>.  Everything else is a <b>no</b> and
     * returns <code>false</code>.
     *
     * @param question The question to ask.
     * @return <code>true</code> if they responded with y|yes etc...return
     *         <code>false</code> otherwise.
     * @throws IOException If there is an io problem.
     */
    public static boolean getYNFromUser (String question)
	                                 throws IOException
    {

	System.out.print (question);
	System.out.println (" [y|n, default y]");

	BufferedReader bread = new BufferedReader (new InputStreamReader (System.in));

	// Read a line...
	String line = bread.readLine ();

	// Check what the line is...
	line = line.trim ();

	line = line.toLowerCase ();

	boolean val = false;

	if ((line.equals (""))
	    ||
	    (line.equals ("y"))
	    ||
	    (line.equals ("yes"))
	   )
	{

	    val = true;

	}

	if (!val)
	{

	    System.out.println ("Taking answer as n");

	}

	System.out.println ();

	return val;

    }

}
