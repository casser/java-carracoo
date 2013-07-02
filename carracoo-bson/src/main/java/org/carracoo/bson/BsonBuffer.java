package org.carracoo.bson;



import org.carracoo.utils.ByteArray;
import org.carracoo.utils.ByteUtils;
import org.carracoo.utils.ObjectUtils;
import org.carracoo.utils.ByteArray;
import org.carracoo.utils.ByteUtils;
import org.carracoo.utils.ObjectUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;


public class BsonBuffer extends ByteArray {
	
	public static final Pattern OID_FORMAT = Pattern.compile("[0-9a-f]{24}");
	public static final Pattern MD5_FORMAT = Pattern.compile("[0-9a-f]{32}");
	
	private static final Charset 	UTF8 		= Charset.forName("UTF-8");
	// the BSON types
	public static final byte TERMINATOR  	= (byte)0x00;
	public static final byte DOUBLE  		= (byte)0x01;
	public static final byte STRING  		= (byte)0x02;
	public static final byte DOCUMENT  		= (byte)0x03;
	public static final byte ARRAY  		= (byte)0x04;
	public static final byte BINARY  		= (byte)0x05;
	// 0x06 is deprecated
	public static final byte OBJECTID  		= (byte)0x07;
	public static final byte BOOLEAN  		= (byte)0x08;
	public static final byte UTC  			= (byte)0x09;
	public static final byte NULL  			= (byte)0x0a;
	public static final byte REGEXP 		= (byte)0x0b;
	// 0x0c is deprecated
	public static final byte JS  			= (byte)0x0d;
	public static final byte SYMBOL  		= (byte)0x0e;
	public static final byte SCOPEDJS  		= (byte)0x0f;
	public static final byte INT32 			= (byte)0x10;
	public static final byte TIMESTAMP 		= (byte)0x11;
	public static final byte INT64  		= (byte)0x12;
	public static final byte MAX_KEY  		= (byte)0x7f;
	public static final byte MIN_KEY  		= (byte)0xff;
	
	private final Stack<Integer> pointers	= new Stack<Integer>();
	
	public void startDocument(){
		pointers.push(position());
		writeInt(0);
	}
	
	public void saveDocument(){
		int sPos	= pointers.pop();
		int ePos	= position();
		int dLen	= ePos-sPos;
		position(sPos);
		writeInt(dLen);
		position(ePos);
	}
	
	private byte getType(Object obj){
		
		if(obj==null){
			return NULL;
		}
		Class<?> cls = obj.getClass();
		if( byte[].class.isAssignableFrom(cls)){
			return BINARY;
		}else
		if(CharSequence.class.isAssignableFrom(cls)) {
			CharSequence str = (CharSequence) obj;
			if(str.length()==24 && OID_FORMAT.matcher(str).matches()){
				return OBJECTID;
			}
			return STRING;
		}else
		if( Boolean.class.isAssignableFrom(cls)) {
			return BOOLEAN;
		}else 
		if( Integer.class.isAssignableFrom(cls)) {
			return INT32;
		}else 
		if( Long.class.isAssignableFrom(cls)) {
			return INT64;
		}else 
		if( Number.class.isAssignableFrom(cls)) {
			return DOUBLE;
		}else 
		if( Date.class.isAssignableFrom(cls)) {
			return UTC;
		}
		if( Map.class.isAssignableFrom(cls)){
			return DOCUMENT;
		}
		if( List.class.isAssignableFrom(cls)){
			return ARRAY;
		} else {
			return STRING;
		} 
	}
	
	
	public Object readDocument(){
		int  size = readInt();
		byte type;
		Map<String,Object> object = new LinkedHashMap<String, Object>();
		while(TERMINATOR != (type = readByte())){
			String key = readCString();
			Object val = readValue(type);
			object.put(key, val);
		}
		return object;
	}
	
	public Object readArray(){
		int  size = readInt();
		byte type;
		List<Object> object = new ArrayList<Object>();
		while(TERMINATOR != (type = readByte())){
			String key = readCString();
			Object val = readValue(type);
			object.add(val);
		}
		return object;
	}
	
	public Object readValue(byte type){
		switch(type){
			case TERMINATOR	: return null;
			case NULL		: return null;
			case STRING		: return readString();
			case BOOLEAN	: return readBoolean();
			case INT32		: return readInt();
			case INT64		: return readLong();
			case DOUBLE		: return readDouble();
			case OBJECTID	: return readObjectId();
			case UTC		: return readDate();
			case BINARY		: return readBinary();
			case ARRAY		: return readArray(); 
			case DOCUMENT	: return readDocument(); 
			default			: throw new RuntimeException("Unsupported object type");
		}
	}
	
	public void writeDocument(Object doc) {
		startDocument();
		for(Map.Entry<Object,Object> item:((Map<Object,Object>)doc).entrySet()){
			writeElement(item.getKey(), item.getValue());
		}
		saveDocument();
	}
	
	public void writeArray(Object doc) {
		startDocument();
		Integer index = 0;
		for(Object item: ObjectUtils.iterable(doc)){
			writeElement((index++).toString(), item);
		}
		saveDocument();
	}

	private void writeElement(Object key, Object val) {
		byte type = getType(val);
		writeByte(type);
		writeCString(key.toString());
		switch(type){
			case NULL     : break;
			case STRING   : writeString(val.toString());break;
			case BOOLEAN  : writeBoolean((Boolean)val);break;
			case INT32    : writeInt((Integer)val);break;
			case INT64    : writeLong((Long)val);break;
			case DOUBLE   : writeDouble((Double)val);break;
			case OBJECTID : writeObjectId(val.toString());break;
			case UTC      : writeDate((Date)val);break;
			case BINARY   : writeBinary((byte[])val); break;
			case ARRAY    : writeArray(val); break;
			case DOCUMENT : writeDocument(val); break;
		}
	}
	
	public void writeTerminator() {
		writeByte(TERMINATOR);
	}	
	public BsonBuffer(){
		super();
	}
	public BsonBuffer(int capasity){
		super(capasity);
	}
	
	public void writeString(Object object) {
		byte[] bytes = object.toString().getBytes(UTF8);
		writeInt(bytes.length+1);
		writeBytes(bytes);
		writeByte(TERMINATOR);
	}
	
	public String readString() {
		int length = readInt();
		byte[] bytes = readBytes(length-1);
		readByte();
		return new String(bytes,UTF8);
	}
	
	public byte[] readBytes() {
		int size = readInt();
		backward(4);
		return readBytes(size);
	}
	
	public byte[] readBinary() {
		int  length 	= readInt();
		byte type		= readByte();
		byte[] bytes 	= new byte[length];
		readBytes(bytes);
		return bytes;
	}
	
	public String readObjectId() {
		return ByteUtils.toHex(readBytes(12));
	}
	public void writeObjectId(String oid) {
		writeBytes(ByteUtils.fromHex(oid));
	}
	public void writeBinary(byte[] bytes) {
		writeInt(bytes.length);
		writeByte((byte)0x80);
		writeBytes(bytes);
	}
	public String readCString() {
		int sPos = position();
		int ePos = sPos;
		while(readByte()!=TERMINATOR){
			ePos++;
		}
		byte[] bytes = new byte[ePos-sPos];
		position(sPos);
		readBytes(bytes);
		readByte();
		return new String(bytes,UTF8);
	}
	
	public void writeCString(String string) {
		byte[] bytes = string.getBytes(UTF8); 
		writeBytes(bytes);
		writeByte((byte)0x00);
	}
	public Date readDate() {
		long time = readLong();
		return new Date(time);
	}
	public void writeDate(Date object) {
		long time = object.getTime();
		writeLong(time);
	}
	
		
}
