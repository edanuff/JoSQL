package org.josql.functions.regexp;

import java.util.Map;
import java.util.HashMap;

import org.josql.QueryExecutionException;

public class RegExpFactory
{

    /**
     * The instance name to use for the Java 1.4 (java.util.regex) regular expression library.
     * Whilst this is the default it will not be available on version of Java < 1.4.
     */
    public static final String JAVA_INST = "java";

    /**
     * The instance name to use for the ORO Apache regular expression library.
     */
    public static final String ORO_INST = "oro";

    /**
     * The instance name to use for the GNU regular expression library.
     */
    public static final String GNU_INST = "gnu";

    /**
     * The instance name to use for the Apache RegExp regular expression library.
     */
    public static final String APACHE_REGEXP_INST = "apache.regexp";

    private static String defInst = RegExpFactory.JAVA_INST;

    private static Map mappings = new HashMap ();
    private static Map versions = new HashMap ();

    static
    {

	StandardJavaRegExpWrapper j = new StandardJavaRegExpWrapper ();

	if (j.isAvailable ())
	{

	    RegExpFactory.mappings.put (RegExpFactory.JAVA_INST,
					StandardJavaRegExpWrapper.class);
	    RegExpFactory.versions.put (RegExpFactory.JAVA_INST,
					j.getSupportedVersion ());

	}

	OroApacheRegExpWrapper o = new OroApacheRegExpWrapper ();

	if (o.isAvailable ())
	{

	    RegExpFactory.mappings.put (RegExpFactory.ORO_INST,
					OroApacheRegExpWrapper.class);
	    RegExpFactory.versions.put (RegExpFactory.ORO_INST,
					o.getSupportedVersion ());

	}

	GNURegExpWrapper g = new GNURegExpWrapper ();

	if (g.isAvailable ())
	{

	    RegExpFactory.mappings.put (RegExpFactory.GNU_INST,
					GNURegExpWrapper.class);
 	    RegExpFactory.versions.put (RegExpFactory.GNU_INST,
					g.getSupportedVersion ());

	}

	ApacheRegExpWrapper a = new ApacheRegExpWrapper ();

	if (a.isAvailable ())
	{

	    RegExpFactory.mappings.put (RegExpFactory.APACHE_REGEXP_INST,
					ApacheRegExpWrapper.class);
 	    RegExpFactory.versions.put (RegExpFactory.APACHE_REGEXP_INST,
					a.getSupportedVersion ());

	}

    }

    private RegExpFactory ()
    {

    }

    public static String getSupportedVersion (String instName)
    {

	return (String) RegExpFactory.versions.get (instName);

    }

    public static String getDefaultInstanceName ()
    {

	return RegExpFactory.defInst;

    }

    public static void addInstance (String  name,
				    RegExp  re,
				    boolean def)
    {

	RegExpFactory.mappings.put (name,
				    re);

	if (def)
	{
	    
	    RegExpFactory.defInst = name;

	}

    }

    public static void setDefaultInstanceName (String n)
    {

	if (!RegExpFactory.mappings.containsKey (n))
	{

	    throw new IllegalArgumentException ("No appropriate wrapper class found for instance name: " +
						n);

	}

	RegExpFactory.defInst = n;

    }

    public static RegExp getDefaultInstance ()
	                                     throws QueryExecutionException
    {

	return RegExpFactory.getInstance (RegExpFactory.defInst);

    }

    public static RegExp getInstance (String type)
	                              throws QueryExecutionException
    {

	Object o = RegExpFactory.mappings.get (type);

	if (o == null)
	{

	    return null;

	}

	if (o instanceof RegExp)
	{

	    // Already inited...
	    return (RegExp) o;

	}

	Class c = (Class) o;

	try
	{

	    RegExp re = (RegExp) c.newInstance ();

	    re.init ();

	    RegExpFactory.mappings.put (type,
					re);

	    return re;

	} catch (Exception e) {

	    throw new QueryExecutionException ("Unable to init RegExp instance: " + 
					       c.getName (),
					       e);

	}

    }

}
