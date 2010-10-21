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
import java.util.Iterator;

import java.io.File;
import java.io.FileFilter;

import com.gentlyweb.utils.FileChangeEvent;
import com.gentlyweb.utils.FileChangeListener;
import com.gentlyweb.utils.FileDetails;

public class FileWatcher 
{

    private Map files = new HashMap ();
    private Worker w = null;

    public FileWatcher ()
    {

	this.w = new Worker (this);

    }

    /**
     * Add a file to be watched.  If the file maps to a directory
     * then we do nothing.  Note:  if you have already started the 
     * thread that contains this FileWatcher then the file will only
     * be watched on the next "watch cycle".  That is it may be longer
     * than "sleepTime" before any changes to the file are noticed and
     * reported.  If we already have this file then we do nothing, in that
     * case changes may be reported before "sleepTime" has elapsed.
     *
     * @param f The file to add.
     */
    public void addFile (File f)
    {

	if (!this.files.containsKey (f))
	{

	    this.files.put (f,
			    new FileDetails (f));

	}

    }

    /**
     * Stop a file being watched.  Note: if you have already started the
     * thread that contains this FileWatcher then any existing changes to the
     * file may still be reported since the watcher could be currently processing
     * (or be about to process) the file.
     *
     * @param f The file to remove.
     */
    public void removeFile (File f)
    {

	this.files.remove (f);

    }

    /**
     * Stop ALL files from being watched.  Note: if you have already started the
     * thread that contains this FileWatcher then any existing changes to the files
     * may still be reported since the watcher could be currently processing 
     * one or more of the files.  After the watchers current watch cycle then the
     * files are guaranteed to no longer be watched.
     */
    public void clearAll ()
    {

	this.files.clear ();

    }

    /**
     * Add all the files in the specified directory.  Use the FileFilter to provide
     * filtering of files that we don't want to add.  If the filter is null then
     * we just add all the files.  If <b>dir</b> points to a file then we do nothing.
     *
     * @param dir The directory.
     * @param ff The FileFilter to use to determine which files to accept/reject.
     */
    public void addAll (File       dir,
			FileFilter ff)
    {

	// See if the dir is really a directory.
	if (!dir.isDirectory ())
	{

	    return;

	}

	if (!dir.canRead ())
	{
	    
	    return;

	}

	File files[] = dir.listFiles ();

	for (int i = 0; i < files.length; i++)
	{

	    if (this.files.containsKey (files[i]))
	    {

		// Goto the next...
		continue;

	    }
	    if (ff == null)
	    {

		this.files.put (files[i],
				new FileDetails (files[i]));

	    } else {

		if (ff.accept (files[i]))
		{
		    
		    this.files.put (files[i],
				    new FileDetails (files[i]));
		    
		}

	    }

	}

    }

    /**
     * Remove all the files that we are watching that have the specified directory
     * as given by the passed in File.  If the passed in File is not a directory then
     * we do nothing.  To perform the check we call: File.getParentFile () on each file
     * and then do <b>dir</b>.equals (parentFile) to see if they are equal, if they are
     * then we remove the file.
     *
     * @param dir The parent directory of files that we want to remove.
     */
    public synchronized void removeAll (File dir)
    {

	if (!dir.isDirectory ())
	{

	    return;

	}

	if (!dir.canRead ())
	{

	    return;

	}

	Map newFs = new HashMap ();

	Iterator iter = this.files.keySet ().iterator ();

	while (iter.hasNext ())
	{

	    File f = (File) iter.next ();

	    File pFile = f.getParentFile ();

	    if (!pFile.equals (dir))
	    {

		newFs.put (f,
			   this.files.get (f));

	    }

	}

    }

    /**
     * Remove all the files from our list of watched files according to the 
     * FileFilter passed in.  This method is synchronized to prevent concurrent 
     * modification issues.
     * 
     * @param ff The FileFilter to determine what should be removed.
     */
    public synchronized void removeAll (FileFilter ff)
    {

	Map newFs = new HashMap ();

	Iterator iter = this.files.keySet ().iterator ();

	while (iter.hasNext ())
	{

	    File f = (File) iter.next ();

	    // If the file filter rejects the file then we should keep it!
	    if (!ff.accept (f))
	    {

		newFs.put (f,
			   this.files.get (f));

	    }

	}

	this.files = newFs;

    }

    protected synchronized Map getFiles ()
    {

	Map fs = new HashMap ();

	fs.putAll (this.files);

	return fs;

    }

    protected synchronized void setFileDetails (File        f,
						FileDetails fd)
    {

	this.files.put (f,
			fd);

    }

    public void setCheckRepeatTime (long millis)
    {

	this.w.setCheckRepeatTime (millis);

    }

    public void start ()
    {

	this.w.start ();

    }

    public void stop ()
    {

	this.w.stop ();

    }

    protected void fireFileChange (File            file,
				   FileChangeEvent event,
				   int             types)
    {

	this.w.fireFileChange (file,
			       event,
			       types);

    }

    public void removeFileChangeListener (FileChangeListener f)
    {

	this.w.removeFileChangeListener (f);

    }

    public void addFileChangeListener (FileChangeListener f,
				       int                changeTypes)
    {

	this.w.addFileChangeListener (f,
				      changeTypes);

    }

    public void addFileChangeListener (FileChangeListener f)
    {
	
	
	this.w.addFileChangeListener (f);

    }

    private class FileListener 
    {

	private int types = -1;
	private FileChangeListener listener = null;

	private FileListener (FileChangeListener l,
			      int                types)
	{

	    this.listener = l;
	    this.types = types;

	}

	private boolean hasEvents (int types)
	{

	    if (this.types < 0)
	    {

		return true;

	    }

	    if ((this.types & types) > 0)
	    {

		return true;

	    }

	    return false;

	}

    }

    protected class Worker implements Runnable
    {

	private boolean shouldRun = false;
	private List listeners = new ArrayList ();
	private long sleepTime = 5000;

	private FileWatcher fw = null;

	public Worker (FileWatcher fw)
	{

	    this.fw = fw;

	}

	public void run ()
	{

	    while (this.shouldRun)
	    {
		
		Map files = this.fw.getFiles ();
		
		Iterator iter = files.keySet ().iterator ();
		
		while (iter.hasNext ())
		{
		    
		    File f = (File) iter.next ();
		    FileDetails fd = (FileDetails) files.get (f);

		    this.processFile (f,
				      fd);
		    
		}
		
		if (this.shouldRun)
		{
		    
		    try
		    {
			
			Thread.sleep (this.sleepTime);
			
		    } catch (Exception e) {
			
			// Not sure what we can do here...
		    
		    }

		}
		
	    }
	    
	}
	
	private void processFile (File        f,
				  FileDetails det)
	{
	    
	    int types = 0;
	    
	    // Work out if it's changed...
	    if (f.exists () != det.exists ())
	    {
	    
		if (f.exists ())
		{
		    
		    types += FileChangeEvent.EXISTS;
		    
		} else {
		    
		    types += FileChangeEvent.NOT_EXISTS;
		    
		}
		
	    } else {
		
		if (f.exists ())
		{
		    
		    if (f.lastModified () != det.lastModified ())
		    {
			
			types += FileChangeEvent.MODIFIED;
			
		    }
		    
		    if (f.isHidden () != det.isHidden ())
		    {
			
			if (f.isHidden ())
			{
			    
			    types += FileChangeEvent.HIDDEN;
			    
			} else {
			    
			    types += FileChangeEvent.NOT_HIDDEN;
			    
			}
			
		    }
		    
		    if (f.length () != det.getLength ())
		    {
			
			types += FileChangeEvent.LENGTH_CHANGED;
			
		    }
		    
		    if (f.canRead () != det.canRead ())
		    {
			
			if (f.canRead ())
			{
			    
			    types += FileChangeEvent.READABLE;
			    
			} else {
			    
			    types += FileChangeEvent.NOT_READABLE;
			    
			}
			
		    }
		    
		    if (f.canWrite () != det.canWrite ())
		    {
			
			if (f.canWrite ())
			{
			    
			    types += FileChangeEvent.WRITEABLE;
			    
			} else {
			    
			    types += FileChangeEvent.NOT_WRITEABLE;
			    
			}
			
		    }
		    
		    if (f.isFile () != det.isFile ())
		    {
			
			types += FileChangeEvent.FILE_TYPE_CHANGE;
			
		    }
		    
		}
		
	    }
	    
	    if (types > 0)
	    {
		
		if (this.shouldRun)
		{

		    FileDetails newDetails = new FileDetails (f);
		    
		    FileChangeEvent e = new FileChangeEvent (f,
							     newDetails,
							     det,
							     types);
		    this.fireFileChange (f,
					 e,
					 types);
		    
		    this.fw.setFileDetails (f,
					    newDetails);
		    
		}
		
	    }

	}

	public void setCheckRepeatTime (long millis)
	{

	    this.sleepTime = millis;

	}

	public void start ()
	{
	    
	    if (!this.shouldRun)
	    {

		this.shouldRun = true;

		Thread t = new Thread (this);
		t.setDaemon (true);
		t.start ();

	    }

	}

	public void stop ()
	{
	    
	    this.shouldRun = false;
	    
	}

	protected void fireFileChange (File            file,
				       FileChangeEvent event,
				       int             types)
	{
	    
	    for (int i = 0; i < this.listeners.size (); i++)
	    {
		
		FileListener l = (FileListener) this.listeners.get (i);
		
		if (l.hasEvents (types))
		{
		    
		    l.listener.fileChanged (event,
					    types);
		    
		}

	    }
	    
	}
	
	public void removeFileChangeListener (FileChangeListener f)
	{
	    
	    for (int i = 0; i < this.listeners.size (); i++)
	    {
		
		FileListener fl = (FileListener) this.listeners.get (i);
		
		if (fl.listener == f)
		{

		    this.listeners.remove (i);
		    return;
		    
		}
		
	    }
	    
	}
	
	public void addFileChangeListener (FileChangeListener f,
					   int                changeTypes)
	{
	    
	    // Ensure that we don't already have the listener.
	    for (int i = 0; i < this.listeners.size (); i++)
	    {

		if (this.listeners.get (i) == f)
		{

		    return;

		}

	    }

	    this.listeners.add (new FileListener (f,
						  changeTypes));
	    
	}
	
	public void addFileChangeListener (FileChangeListener f)
	{
	    
	    for (int i = 0; i < this.listeners.size (); i++)
	    {

		if (this.listeners.get (i) == f)
		{

		    return;

		}

	    }
	    
	    this.listeners.add (new FileListener (f,
						  -1));
	    
	}

    }

}
