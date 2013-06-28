/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carracoo.seeds.lang;

import com.carracoo.seeds.SeedView;
import com.carracoo.seeds.exceptions.ValidationException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 *
 * @author Sergey
 */
public class Corn {
	
	
	
	private static final Map<Class,List<FieldInfo>> FIELDS = new ConcurrentHashMap<Class, List<FieldInfo>>();
	protected final static class FieldInfo {
		public final Field field;
		public final int   index;
		public FieldInfo(int index,Field field){
			this.field = field;
			this.index = index;
		}
	}
	
	protected final Class<? extends Corn> clazz(){
		Class<? extends Corn> cls = this.getClass();
		while(cls.getEnclosingClass()!=null){
			cls = (Class<? extends Corn>) cls.getSuperclass();
		}
		return cls;
	}
	
	protected final List<FieldInfo> fields(){
		Class<?> cls = clazz();
		if(!FIELDS.containsKey(cls)){
			List<FieldInfo> fields = new ArrayList<FieldInfo>();
			int index = 1;
			for(Field field:cls.getDeclaredFields()){
				if(Grain.class.isAssignableFrom(field.getType())){
					fields.add(new FieldInfo(index++, field));
				}
			}
			FIELDS.put(cls, fields);
		}
		return FIELDS.get(cls);
	}
	
	public List<Grain> properties(){
		try{
			List<Grain> properties = new ArrayList<Grain>();
			for(FieldInfo info:fields()){
				properties.add((Grain)info.field.get(this));
			}
			return properties;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private int pindex = 0;
	protected final FieldInfo field() {
		return fields().get(pindex++);
	}
	
	public Grain property(String name) {
		for(Grain property:properties()){
			if(property.name().equals(name)){
				return property;
			}
		}
		return null;
	}
	
	public Set<Grain> changes(){
		Set<Grain> changes = new HashSet<Grain>();
		for(Grain property:properties()){
			if(property.changed()){
				changes.add(property);
			}
		}
		return changes;
	}
	
	public Class<? extends Corn> type() {
		return clazz();
	}
	
	public String name() {
		return clazz().getSimpleName();
	}
	
	public Boolean changed(){
		return changes().size()>0;
	}
	
	public <T extends Corn> T rollback(){
		for(Grain property:changes()){
			property.rollback();
		}
		return (T) this;
	}
	public <T extends Corn> T normalize(){
		return (T) this;
	}
	public <T extends Corn> T commit(){
		validate();
		for(Grain property:changes()){
			property.commit();
		}
		return (T)this;
	}
	
	public <T extends Corn> T validate() throws ValidationException {
		normalize();
		for(Grain property:properties()){
			property.validate();
		}
		return (T)this;
	}
	
	public Object get(SeedView view) {
		return this;
	}
	public Corn set(SeedView view, Object value) {
		return null;
	}
	@Override
	public String toString() {
		return GrainPrinter.string(this);
	}
}


class GrainPrinter {

	static String string(Corn model){
		return new GrainPrinter().convert(model, 0);
	}
	static void print(Corn model){
		System.out.print(string(model));
	}
	
	private String convert(Corn model, int level){
		StringBuilder buf = new StringBuilder();
		//buf.append("\u001B[5;34m");
		buf.append("{").append(model.name()).append("}");
		//buf.append("\u001B[0m");
		buf.append("\n");
		for(Grain property:model.properties()){
			if(!property.isNull()){
				if(property.multiple()){
					buf
						.append(ident(level+1))
						.append(property.name()).append(" : [")
						.append(property.type().getSimpleName())
						.append("]\n")
					;
					int index = 0;
					for(Object item:property){
						buf
							.append(ident(level+2))
							.append(index++).append(" : ")
							.append(stringify(item,level+2))
							.append("\n")
						;
					}	
				}else{
					buf
						.append(ident(level+1))
						.append(property.index()).append(" ")
						.append(property.name()).append(" : ")
						.append(stringify(property.get(),level+1)).append("\n")
					;
				}
			}
		}
		return buf.substring(0,buf.length()-1);
	}
	
	private String ident(int level){
		String str = "";
		for(int i=0;i<level;i++){
			str+="  ";
		}
		return str;
	}
	
	private String stringify(Object vobj, int level){
		return (vobj==null?"null":(vobj instanceof Corn?convert((Corn)vobj,level):vobj.toString()));
	}
}