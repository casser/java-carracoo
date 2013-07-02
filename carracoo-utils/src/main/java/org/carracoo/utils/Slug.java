package org.carracoo.utils;

import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey
 * Date: 5/6/13
 * Time: 6:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class Slug {

	private static final Pattern CLEAN_DIVS     = Pattern.compile("[-\\/]");
	private static final Pattern CLEAN_PUNCT    = Pattern.compile("[^\\w\\s]+");
	private static final Pattern TRIM           = Pattern.compile("^\\s+|\\s+$");
	private static final Pattern REPLACE_SPACE  = Pattern.compile("\\s+");


	public static String toSlug(String input) {
		String result = input.toString();
		result = CLEAN_DIVS.matcher(result).replaceAll(" ");
		result = CLEAN_PUNCT.matcher(result).replaceAll("");
		result = TRIM.matcher(result).replaceAll("");
		result = REPLACE_SPACE.matcher(result).replaceAll("-");
		return result.toLowerCase();
    }

}
