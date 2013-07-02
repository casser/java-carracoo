/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carracoo.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.Iterable;
import java.util.Iterator;
/**
 *
 * @author Sergey
 */
public class ObjectUtils {
	
	private static final Map<Class, ObjectInfo> OBJECT_INFO_CACHE = new ConcurrentHashMap<Class, ObjectInfo>();

	
	public static class SimpleIterable<T> implements Iterable<T>{
		private Iterator<T> iterator;
		public SimpleIterable(Iterator iterator){
			this.iterator = iterator;
		}
		@Override
		public Iterator<T> iterator() {
			return this.iterator;
		}
	}
	public static class ArrayIterator<T> implements Iterator<T>{
		
		private Object[] array;
		private int index;
		private ArrayIterator(Object object) {
			array	= (Object[])object;
			index	= 0;
		}

		@Override
		public boolean hasNext() {
			return index<array.length;
		}

		@Override
		public T next() {
			return (T)array[index++];
		}

		@Override
		public void remove() {
			array[index] = null;
		}
		
	}
	public static enum ObjectType {
		ARRAY,MAP,SIMPLE,UNKNOWN
	}
	
	public static class ObjectInfo {		
		private final ObjectType	type;
		private final Class<?>		clazz;
		
		private ObjectInfo(Class<?> clazz) {
			this.clazz = clazz;
			if(
				clazz.isPrimitive()							||
				CharSequence.class.isAssignableFrom(clazz)	||
				Boolean.class.isAssignableFrom(clazz)		||
				Number.class.isAssignableFrom(clazz)
			){
				this.type = ObjectType.SIMPLE;
			}else
			if(clazz.isArray()||Iterable.class.isAssignableFrom(clazz)){
				this.type = ObjectType.ARRAY;
			}else
			if(Map.class.isAssignableFrom(clazz)){
				this.type = ObjectType.MAP;
			}else{
				this.type = ObjectType.UNKNOWN;
			}
		}
		
		public boolean isArray() {
			return type.equals(ObjectType.ARRAY);
		}
		
		public boolean isArray(Class<?> itemType) {
			return type.equals(ObjectType.ARRAY);
		}
		
		public boolean isMap() {
			return type.equals(ObjectType.MAP);
		}
		public boolean isSimple() {
			return type.equals(ObjectType.SIMPLE);
		}
		public boolean isUnknown() {
			return type.equals(ObjectType.UNKNOWN);
		}
		
		public boolean isIterable() {
			return isArray()||isMap();
		}
				
	}
	
	public static ObjectInfo info(Object value){
		Class<?> clazz = (value instanceof Class)?(Class<?>)value:value.getClass();
		if(!OBJECT_INFO_CACHE.containsKey(clazz)){
			OBJECT_INFO_CACHE.put(clazz,new ObjectInfo(clazz));
		}
		return OBJECT_INFO_CACHE.get(clazz);
	}
	
	public static <T> Iterable<T> iterable(Object object) {
		if(object instanceof Iterable){
			return (Iterable<T>) object;
		}else
		if(object instanceof Iterator){
			return new SimpleIterable<T>((Iterator<T>)object);
		}else 
		if(object.getClass().isArray()){
			return new SimpleIterable<T>(new ArrayIterator<T>(object));
		}else{
			throw new RuntimeException("object is not iterable");
		}
	}
		
}
