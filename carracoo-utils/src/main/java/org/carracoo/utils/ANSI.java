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

	public static String format(String text, Color foreground) {
		StringBuilder buff = new StringBuilder();
		append(buff,text,foreground);
		return buff.toString();
	}


	public static void append(Appendable buff, String text,Color foreground) {
		try{
			String str = "\u001b["+(30 + foreground.ordinal())+"m"+text+"\u001b[0m";
			buff.append(str);
		}catch(Exception ex){
			System.err.print(ex);
		}
	}
}
