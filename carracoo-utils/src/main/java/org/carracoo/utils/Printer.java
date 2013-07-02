package org.carracoo.utils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey
 * Date: 6/29/13
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class Printer {

	public static class Cursor {
		public static class Path extends Stack<String> {
			@Override
			public synchronized String toString() {
				StringBuilder buf = new StringBuilder();
				for(String p:this){
					buf.append(".").append(p);
				}
				return buf.length()>0 ? buf.substring(1) : "";
			}
			public boolean match(String pattern) {
				return toString().matches(pattern);
			}
		}

		private final Path path;
		public Path path(){
			return path;
		}

		public Cursor(){
			this(new Path());
		}

		public Cursor(Path path){
			this.path = path;
		}

		public boolean isRoot() {
			return path.size()==1;
		}

		public void enter(Integer i) {
			enter(i.toString());
		}

		public void enter(String path) {
			path().push(path);
		}

		public void exit() {
			path().pop();
		}
		public int level(){
			return path().size();
		}
	}

	public static class ObjectPrinter {
		
		public boolean support(Object val) {
			return true;
		}
		
		protected void append(Appendable buf, String val) throws IOException {
			append(buf, val, null);
		}
		protected void append(Appendable buf, String val, ANSI.Color color) throws IOException {
			if(color!=null){
				ANSI.append(buf,val,color);
			}else{
				buf.append(val);
			}
		}
		protected void nl(Appendable buf) throws IOException {
			buf.append("\n");
		}
		protected void ident(Appendable buf, int size) throws IOException {
			for(int i=0;i<size;i++){
				append(buf,"  ");
			}
		}
		protected void print(Appendable buf, Object val) throws IOException {
			print(new Cursor(),buf,val);
		}

		protected void print(Cursor cursor, Appendable buf, Object val) throws IOException {
			buf.append(val==null?"null":val.getClass().getSimpleName()+"("+val.toString()+")");
		}
	}
	public static class StringPrinter extends ObjectPrinter {
		@Override
		public boolean support(Object val) {
			return (val instanceof CharSequence || val.getClass().isEnum());
		}

		@Override
		protected void print(Cursor cursor, Appendable buf, Object val) throws IOException {
			if(val instanceof CharSequence){
				append(buf, "'"+val.toString()+"'", ANSI.Color.GREEN);
			}else {
				append(buf, val.toString(), ANSI.Color.GREEN);
			}

		}
	}
	public static class NumberPrinter extends ObjectPrinter {
		@Override
		public boolean support(Object val) {
			return (val instanceof Number);
		}

		@Override
		protected void print(Cursor cursor, Appendable buf, Object val) throws IOException {
			append(buf, val.toString(), ANSI.Color.BLUE);
		}
	}
	public static class BooleanPrinter extends ObjectPrinter {
		@Override
		public boolean support(Object val) {
			return (val instanceof Boolean);
		}

		@Override
		protected void print(Cursor cursor, Appendable buf, Object val) throws IOException {
			append(buf, val.toString(), ANSI.Color.BLUE);
		}
	}
	public static class ArrayPrinter extends ObjectPrinter {
		@Override
		public boolean support(Object val) {
			return val!=null && (
				Iterable.class.isAssignableFrom(val.getClass())||
				val.getClass().isArray()
			);

		}
		public void print(Cursor cursor, Appendable buf, Object val) throws IOException {
			append(buf,"ARRAY", ANSI.Color.MAGENTA);
			if(val instanceof Iterable){
				Iterable it = (Iterable)val;
				Integer index = 0;
				for (Object item:it){
					String key = (index++).toString();
					cursor.enter(key);
					nl(buf);
					ident(buf,cursor.level());
					append(buf, key, ANSI.Color.MAGENTA);
					append(buf, " : ");
					Printer.print(cursor, buf, item);
					cursor.exit();
				}
			}else if(val.getClass().isArray()){
				Object[] it = (Object[])val;
				Integer index = 0;
				for (Object item:it){
					String key = (index++).toString();
					cursor.enter(key);
					nl(buf);
					ident(buf,cursor.level());
					append(buf, key, ANSI.Color.MAGENTA);
					append(buf, " : ");
					Printer.print(cursor, buf, item);
					cursor.exit();
				}
			}
		}
	}

	public static class MapPrinter extends ObjectPrinter {
		@Override
		public boolean support(Object val) {
			return val!=null && Map.class.isAssignableFrom(val.getClass());
		}
		public String getKey(Object key,int index){
			Class<?> cls = key.getClass();
			if(cls.isEnum()||String.class.isAssignableFrom(cls)||Number.class.isAssignableFrom(cls)){
				return key.toString();
			}
			return cls.getSimpleName()+"_"+index;
		}
		public void print(Cursor cursor, Appendable buf, Object val) throws IOException {
			Map<Object,Object> map = (Map<Object,Object>)val;
			int index = 0;
			append(buf,"OBJECT", ANSI.Color.BLUE);
			for (Map.Entry<Object,Object> entry:map.entrySet()){
				String key = getKey(entry.getKey(), index++);
				cursor.enter(key);
				nl(buf);
				ident(buf,cursor.level());
				append(buf, key, ANSI.Color.BLUE);
				append(buf, " : ");
				Printer.print(cursor,buf,entry.getValue());

				cursor.exit();
			}

		}
	}

	private static final List<ObjectPrinter> PRINTERS =
	Collections.synchronizedList(new ArrayList(){{
		add(new BooleanPrinter());
		add(new NumberPrinter());
		add(new StringPrinter());
		add(new MapPrinter());
		add(new ArrayPrinter());
		add(new ObjectPrinter());
	}});

	public static void add(ObjectPrinter printer){
		PRINTERS.add(0, printer);
	}

	public static void print(Object val){
		print(System.out,val);
		System.out.println();
	}

	public static void print(Appendable buf, Object val){
		print(new Cursor(),buf,val);
	}

	public static void print(Cursor cursor, Appendable buf, Object val){
		try{
			if(val==null){
				ANSI.append(buf,"null", ANSI.Color.RED);return;
			}
			for(ObjectPrinter printer:PRINTERS){
				if(printer.support(val)){
					printer.print(cursor, buf, val);
					return;
				}
			}
		}catch (Exception ex){
			ex.printStackTrace(System.err);
		}
	}


}
