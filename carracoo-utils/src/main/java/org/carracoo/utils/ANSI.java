package org.carracoo.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey
 * Date: 6/29/13
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ANSI {

	public enum Attribute {

		NORMAL(0),      BRIGHT(1),  DIM(2),
		UNDERLINE(4),   BLINK(5),   REVERSE(7),
		HIDDEN(8);

		private String value;
		private Attribute(int value) {
			this.value = String.valueOf(value);
		}
		public String toString() {
			return "" + value;
		}
	}

	public enum Color {
		BLACK, RED, GREEN, YELLOW,
		BLUE, MAGENTA, CYAN, WHITE
	}

	private static final String PREFIX = "\u001b["; //NOI18N
	private static final String SUFFIX = "m";
	private static final String SEPARATOR = ";";
	private static final String END = PREFIX + SUFFIX;


	public static String format(String text, Color foreground) {
		return format(text, null, foreground, null);
	}

	public static String format(String text, Color foreground, Color background) {
		return format(text, null, foreground, background);
	}

	public static String format(String text, Attribute attr, Color foreground, Color background) {
		StringBuilder buff = new StringBuilder();
		append(buff,text,foreground,background,attr);
		return buff.toString();
	}



	public static void append(Appendable buff, String text, Color foreground) {
		append(buff, text, foreground, null, null);
	}
	public static void append(Appendable buff, String text, Color foreground,Attribute attr) {
		append(buff, text, foreground, null, attr);
	}
	public static void append(Appendable buff, String text, Color foreground, Color background) {
		append(buff, text,  foreground, background, null);
	}
	public static void append(Appendable buff, String text,Color foreground, Color background, Attribute attr) {
		try{
			buff.append(PREFIX);
			if (attr != null) {
				buff.append(attr.toString());
			}
			if (foreground != null) {
				buff.append(SEPARATOR);
				buff.append("" + (30 + foreground.ordinal()));
			}
			if (background != null) {
				buff.append(SEPARATOR);
				buff.append("" + (40 + background.ordinal()));
			}
			buff.append(SUFFIX);
			buff.append(text);
			buff.append(END);
		}catch(Exception ex){
			System.err.print(ex);
		}
	}
}
