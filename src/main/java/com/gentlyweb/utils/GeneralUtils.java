package com.gentlyweb.utils;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import java.util.StringTokenizer;

import java.lang.reflect.Method;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * A Class to hold some general purpose Utilities that don't have a home 
 * anywhere else (at the moment).
 * <br /><br />
 * You should <b>NOT</b> rely on these methods staying in this class.
 * <br /><br />
 * All methods in this class are static.
 */
public class GeneralUtils
{

    public static String GENTLYWEB_EMAIL = "code-monkey@gentlyweb.com";

    /**
     * This method is here for historical reasons and is just a wrapper around
     * {@link StringUtils.replaceString(java.lang.String,java.lang.String,java.lang.String)}
     * and will be removed soon!  It should <b>NOT</b> be used!
     *
     * @param text The text to perform the replacement on.
     * @param str The string to find in the text.
     * @param replace The string to replace "str" with.
     * @return A new String representing the replaced text (if any 
     *         were made).  
     * @throws NullPointerException If any of the arguments are null indicating which one is at fault.
     */
    /*
    public static String replaceString (String text,
					String str,
					String replace)
	                                throws NullPointerException
    {

	return StringUtils.replaceString (text,
					  str,
					  replace);

    }
    */

    /**
     * Get an exception (and if it's an instance of  {@link ChainException}) the chain of
     * exceptions (if present) as a String.
     *
     * @param e The exception
     * @return A String of the exception chain.
     * @throws IOException Should never happen since we are using a StringWriter.
     */
    public static String getExceptionTraceAsString (Exception e)
                                                    throws    IOException
    {

	StringWriter sout = new StringWriter ();
	PrintWriter pout = new PrintWriter (sout);
	e.printStackTrace (pout);

	pout.println ();

	if (e instanceof ChainException)
	{
	    
	    ChainException ee = (ChainException) e;
	    
	    ee.printInnerExceptionChain (pout);
		
	}

	return sout.toString ();

    }

    /**
     *
     * Validate an IPv4 address (string) passed in, it must conform to the following rules:
     * <ul>
     *   <li>Must have 4 parts.</li>
     *   <li>Parts must be separated by .</li>
     *   <li>The first octet must be in the range 0-223.</li>
     *   <li>The second and third octets must be in the range 0-255</li>
     *   <li>4th octet can be either a number in the range 1-254 (0 is the name of the network, 255 is the
     *       broadcast address).</li>
     *   <li>All parts must be numbers...</li>
     * </ul>
     * 
     * @param ipaddress The ip address to validate.
     * @throws Exception If one of the rules is broken.
    */
    public static void validateIPv4Address (String ipaddress)
					    throws Exception
    {

	// Rules here are:
	//
	//    Must have 4 parts.
	//    Parts must be separated by .
	//    The first octet must be in the range 0-223.
	//    The second and third octets must be in the range 0-255
	//    4th octet can be either a number in the range 1-254 (0 is the name of the network, 255 is the
	//    broadcast address...)
	//    All parts must be numbers...

	StringTokenizer t = new StringTokenizer (ipaddress,
						 ".");
	
	if (t.countTokens () != 4)
	{

	    throw new Exception ("IP address: " +
				 ipaddress +
				 ", does not consist of 4 parts");

	}

	String octet1 = t.nextToken ();
	String octet2 = t.nextToken ();
	String octet3 = t.nextToken ();
	String lastpart = t.nextToken ();

	try
	{

	    int intoctet1 = Integer.parseInt (octet1);
	    
	    if ((intoctet1 > 223)
		||
		(intoctet1 < 0)
	       )
	    {

		throw new Exception ("First octet of IP address: " +
				     ipaddress +
				     " must be in the range 0-223");

	    }

	} catch (NumberFormatException nfe) {

	    throw new Exception ("First octet of IP address: " +
				 ipaddress +
				 ", is not a number");

	}

	try
	{

	    int intoctet2 = Integer.parseInt (octet2);

	    if ((intoctet2 > 255)
		||
		(intoctet2 < 0)
	       )
	    {

		throw new Exception ("Second octet of IP address: " +
				     ipaddress +
				     " must be in the range 0-255");

	    }

	} catch (NumberFormatException nfe) {

	    throw new Exception ("Second octet of IP address: " +
				 ipaddress +
				 ", is not a number");

	}	

	try
	{

	    int intoctet3 = Integer.parseInt (octet3);
	    
	    if ((intoctet3 > 255)
		||
		(intoctet3 < 0)
	       )
	    {

		throw new Exception ("Third octet of IP address: " +
				     ipaddress +
				     " must be in the range 0-255");

	    }

	} catch (NumberFormatException nfe) {

	    throw new Exception ("Third octet of IP address: " +
				 ipaddress +
				 ", is not a number");

	}

	// Now handle the fourth octet.
	// This is just a number...or supposed to be...
	try
	{
	    
	    int intlastpart = Integer.parseInt (lastpart);
	    
	    if ((intlastpart > 254)
		||
		(intlastpart < 1)
	       )
	    {

		throw new Exception ("Final octet of IP address: " +
				     ipaddress +
				     " must be in the range 1-254");

	    }

	} catch (NumberFormatException nfe) {
		
	    throw new Exception ("Final octet of IP address: " +
				 ipaddress +
				 ", is not a number");
		
	}

    }

    public static void getMethods (Class  c,
				   String name,
				   int    mods,
				   List   ms)
    {

	if (c == null)
	{

	    return;

	}

	Method[] meths = c.getDeclaredMethods ();

	for (int i = 0; i < meths.length; i++)
	{

	    Method m = meths[i];

	    if ((m.getName ().equals (name))
		&&
		((m.getModifiers () & mods) == mods)
	       )
	    {

		if (!ms.contains (m))
		{

		    // This is one.
		    ms.add (m);

		}

	    }

	}	

	// Now get all the super-classes.
	Class sup = c.getSuperclass ();

	if (sup != null)
	{

	    GeneralUtils.getMethods (sup,
				     name,
				     mods,
				     ms);

	}

	// Now work up through the super-classes/interfaces.
	Class[] ints = c.getInterfaces ();

	for (int i = 0; i < ints.length; i++)
	{

	    Class in = ints[i];

	    GeneralUtils.getMethods (in,
				     name,
				     mods,
				     ms);

	}

    }

}
