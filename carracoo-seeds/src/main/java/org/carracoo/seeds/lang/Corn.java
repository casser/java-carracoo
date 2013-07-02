/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carracoo.seeds.lang;

import org.carracoo.seeds.SeedView;
import org.carracoo.seeds.exceptions.ValidationException;
import org.carracoo.utils.ANSI;
import org.carracoo.utils.Printer;
import org.carracoo.seeds.exceptions.ValidationException;
import org.carracoo.utils.ANSI;

import java.io.IOException;
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
	{Printer.add(new GrainPrinter());}
}

class GrainPrinter extends Printer.ObjectPrinter{

	@Override
	public boolean support(Object val) {
		return val!=null && Corn.class.isAssignableFrom(val.getClass());
	}

	@Override
	public void print(Printer.Cursor cursor, Appendable buf, Object val) throws IOException {
		Corn map = (Corn)val;
		append(buf,map.type().getSimpleName(), ANSI.Color.MAGENTA);
		for (Grain property:map.properties()){
			cursor.enter(property.name());
			nl(buf);
			ident(buf,cursor.level());
			append(buf, property.name(), ANSI.Color.MAGENTA);
			append(buf, " : ");
			if(property.multiple()){
				Integer index = 0;
				append(buf,"ARRAY", ANSI.Color.MAGENTA);
				for(Object item:property){
					cursor.enter(index++);
					nl(buf);
					ident(buf,cursor.level());
					append(buf, index.toString(), ANSI.Color.MAGENTA);
					append(buf, " : ");
					Printer.print(cursor,buf,property.get());
					cursor.exit();
				}
			} else {
				Printer.print(cursor,buf,property.get());
			}
			cursor.exit();
		}

	}
}