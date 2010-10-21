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

public class NumberUtils
{

    /**
     * Get the passed in double to the required precision.
     * In essence if you have a number: <b>11.77987</b> and you would like it
     * to 2 digits decimal digit precision then call this method with <code>digits</code>
     * set to 2, in which case: <b>11.78</b> will be returned.  This method
     * is useful when you want to round a number after a multiple
     * decimal place division.
     * <p>
     * In essence it merely rounds the decimal part to the required number of
     * digits using Math.round.
     * <p>
     * This method is most useful when provided in a suitable wrapper, for instance:
     * <code>
     *   public float getAsCurrency (float v)
     *   {
     *
     *       return (float) GeneralUtils.toPrecision (v, 2);
     *
     *   }
     * </code>
     * <p>
     * Note, it is safe to pass 0 as either of the parameters, it should be noted
     * that passing 0 as <code>digits</code> has the same effect as calling: Math.round (v),
     * which makes sense since 0 digit precision of 11.77987 should be 12.
     *
     * @param v The value to round.
     * @param digits The number of decimal digits to round to.
     * @return The rounded value.
     */
    public static double toPrecision (double v,
				      int    digits)
    {

	if (v == 0)
	{

	    return v;

	}

	double mult = Math.pow (10, 
				digits);

	double r = Math.floor (v);

	return r + ((Math.round ((v - r) * mult)) / mult);

    }

    /**
     * Given an int value treat it as a number of days and return 
     * the number of milliseconds for that number of days.
     *
     * @param days Number of days.
     * @return A long giving the number of milliseconds.
     */
    public static long getDaysAsMillis (int days)
    {

	return (long) days * 86400000;

    }

    /**
     * Convert a number of milliseconds into seconds, we format to 2 decimal
     * places, i.e. we return a String of the form a.xy.
     *
     * @param millis The milliseconds to format.
     * @return A String formatted as a.xy.
     */
    public static String getMillisAsFormattedSeconds (long millis)
    {

	long secs = millis / 1000;
	
	long tenths = (millis - (secs * 1000)) / 100;

	long hundredths = (millis - (secs * 1000) - (tenths * 100)) / 10;

	return secs + "." + tenths + hundredths;

    }

}
