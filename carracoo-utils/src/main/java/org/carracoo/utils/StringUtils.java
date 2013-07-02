package org.carracoo.utils;

public class StringUtils {
	public static final String EMPTY = "";

	public static String repeat(String str, Integer count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append(str);
		}
		return sb.toString();
	}

	public static String capitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuilder(strLen)
			.append(Character.toTitleCase(str.charAt(0)))
			.append(str.substring(1))
		.toString();
	}

	public static String capitalizeSentance(String str) {
		String[] words = str.split(" ");
		StringBuilder sb = new StringBuilder();
		for (String w : words) {
			sb.append(" ").append(capitalize(w));
		}
		return sb.toString().substring(1);
	}

	public static String uncapitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuilder(strLen)
			.append(Character.toLowerCase(str.charAt(0)))
			.append(str.substring(1))
		.toString();
	}

	public static String toClassNameNotation(String str) {
		return capitalize(toVariableNotation(str));
	}

	public static String toVariableNotation(String str) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (ch == '_') {
				ch = str.charAt(++i);
				result.append(Character.toUpperCase(ch));
			} else {
				result.append(Character.toLowerCase(ch));
			}
		}
		return result.toString();
	}

	public static String toUnderscoredNotation(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		StringBuilder result = new StringBuilder();
		result.append(Character.toLowerCase(str.charAt(0)));
		for (int i = 1; i < str.length(); i++) {
			char ch = str.charAt(i);
			switch (Character.getType(ch)) {
				case Character.DECIMAL_DIGIT_NUMBER:
				case Character.UPPERCASE_LETTER:
					if (str.charAt(i - 1) != '_') {
						result.append('_');
					}
					result.append(Character.toLowerCase(ch));
					break;
				default:
					result.append(Character.toLowerCase(ch));
			}
		}
		return result.toString();
	}


	public static String join(Object[] array, char separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}


	public static String join(Object[] array, char separator, int startIndex, int endIndex) {
		if (array == null) {
			return null;
		}
		int noOfItems = endIndex - startIndex;
		if (noOfItems <= 0) {
			return EMPTY;
		}

		StringBuilder buf = new StringBuilder(noOfItems * 16);

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}

	public static String trim(String str) {
		return str.replaceAll("\\s+", " ").trim();
	}
	
	public static String slug(String str) {
		return Slug.toSlug(str);
	}

}
