package org.carracoo.json;

import org.carracoo.utils.ObjectUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey
 * Date: 5/30/13
 * Time: 3:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class JsonEncoder {
	
	private static class Buffer {
		private final StringBuilder out  = new StringBuilder();	
		private final Boolean formatted;

		public Buffer() {
			this(true);
		}

		public Buffer(Boolean formatted) {
			this.formatted = formatted;
		}
		public Buffer append(Integer str){
			return append(str.toString());
		}
		public Buffer append(CharSequence str){
			out.append(str);return this;
		}
		
		public Buffer space(){
			return space(1);
		}
		
		public Buffer space(int count){
			return repeat(count," ");
		}
		
		public Buffer ident(){
			return ident(1);
		}
		
		public Buffer ident(int count){
			return repeat(count,"  ");
		}
		
		public Buffer repeat(int count, String ch){
			for(int i=0;i<count;i++){
				fappend(ch);
			}
			return this;
		}
		
		public Buffer nl(){
			return fappend("\n");
		}
		
		public Buffer fappend(Integer ch){
			return fappend(ch.toString());
		}
		public Buffer fappend(CharSequence ch){
			if(formatted){
				append(ch);
			}
			return this;
		}
		public int length(){
			return out.length();
		}
		
		@Override
		public String toString() {
			return out.toString();
		}
		
		public String result(int mis) {
			String str = out.toString();
			if(formatted){
				Pattern patt = Pattern.compile("\\t(\\d+):");
				Matcher m = patt.matcher(str);
				StringBuffer sb = new StringBuffer(str.length());
				while(m.find()){
					Integer cnt = mis-Integer.parseInt(m.group(1));
					m.appendReplacement(sb, new Buffer().space(cnt)+": ");
				}
				m.appendTail(sb);
				str = sb.toString();
			}
			return str;
		}
		
	}
	
	private int		mis = 0;
	private boolean formatted;
	
	public JsonEncoder() {
		this(true);
	}
	
	public JsonEncoder(boolean formatted) {
		this.formatted = formatted;
	}
	
	public byte[] encode(Object value) {
		mis=0;
		Buffer out  = new Buffer(this.formatted);
		encodeValue(out,0,value);		
		return out.result(mis).getBytes();
	}

	private void encodeValue(Buffer out, int level, Object value){
		if(value==null){
			out.append("null"); return;
		}
		ObjectUtils.ObjectInfo info = ObjectUtils.info(value);
		if(info.isArray()){
			encodeArray(out,level,value);
		}else
		if(info.isMap()){
			encodeObject(out,level,value);
		}else
		if(info.isSimple()){
			if(
				value instanceof Number  ||
				value instanceof Boolean
			){
				out.append(value.toString());
			}else{
				out.append(escape(value.toString()));
			}
		}else{
			throw new RuntimeException("Invalid Structure");
		}
	}

	private void encodeObject(Buffer out, int level, Object object) {
		boolean isFirst = true;
		out.append("{").nl();
		for(Map.Entry<Object,Object> item:((Map<Object,Object>)object).entrySet()){
			if(!isFirst){
				out.append(",").nl();
			}
			int sl = out.length();
			out.ident(level+1);
			out.append(escape(item.getKey().toString()));
			out.fappend("\t").fappend(out.length()-sl);
			out.append(":");
			mis = Math.max(mis,out.length()-sl);
			
			encodeValue(out,level+1,item.getValue());
			isFirst=false;
		}
		out.nl();
		out.ident(level);
		out.append("}");
	}

	private void encodeArray(Buffer out, int level, Object array) {
		boolean isFirst = true;
		out.append("[");
		for(Object item:ObjectUtils.iterable(array)){
			if(!isFirst){
				out.append(",");
			}
			encodeValue(out,level,item);
			isFirst=false;
		}
		out.append("]");
	}

	private String escape(String str){
		return "\""+str.replaceAll("\\\"", "\\\\\"")+"\"";
	}
	
}
