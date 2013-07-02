/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carracoo.seeds.lang;

import org.carracoo.seeds.SeedView;
import org.carracoo.seeds.exceptions.ValidationException;
import org.carracoo.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Sergey
 */
public abstract class Grain<P extends Corn,V> implements Iterable<V> {
	
	// Property specific settings
	private int			index;
	public  int			index(){
		return index;
	}	
	private String		name;
	public  String		name(){
		return name;
	}
	private Class<?>	type;
	public  Class<?>	type(){
		return type;		
	}
	private             P parent;
	public              P parent(){
		return this.parent;
	}
	private		List<V>	value;
	protected	List<V>	value(){
		if(value==null){
			value = new ArrayList<V>(limit<Integer.MAX_VALUE?limit:4);
		}
		return this.value;
	}
	abstract protected V create();
	// public interface 
	public  Boolean isNull(){
		return value().isEmpty();
	}
	
	public V get(){
		return get(0);
	}
	public V get(int index){
		if(value().size()>index){
			return value().get(index);
		}else{
			return null;
		}
	}
	public List<V> all(){
		return value();
	}
	public P set(V value){
		if(multiple()){
			value().add(value);
		}else{
			value().add(0, value);
		}
		return parent();
	}
	
	// view specific methods 
	public Object get(SeedView view){
		return get();
	}
	public Object set(SeedView view, Object value){
		return set((V)value);
	}
	
	public boolean available(SeedView view) {
		return get()!=null;
	}
	//
	
	@Override
	public Iterator<V> iterator() {
		return value().iterator();
	}
	
	//changes detection
	
	private V old;
	public  V old(){
		return old;
	}
	public  P old(V value){
		this.old = value;
		return parent();
	}
	
	private Boolean changed;
	public  Boolean changed(){
		return changed;
	}
	public  void change(V value){
		if(!this.changed){
			this.old = value;
			this.changed = true;
		}
	}
	
	//commit rollback mechanism
	public P normalize(){
		return parent();
	}
	
	public P validate() throws ValidationException {
		normalize();
		if(!isNull() && Corn.class.isAssignableFrom(get().getClass())){
			((Corn)get()).validate();
		}
		if(required() && isNull()){
			throw new ValidationException("property '"+name()+"' required");
		}
		return parent();
	}
	
	public P commit(){
		validate();
		this.changed	= false;
		this.old		= null;
		if(!isNull() && Corn.class.isAssignableFrom(get().getClass())){
			((Corn)get()).commit();
		}
		return parent();
	}
	
	public P rollback(){
		return commit();
	}
	
	
	
	private	int				limit;
	private boolean			required;
	final protected int		limit(){
		return limit;
	}
	final public	boolean multiple(){ 
		return limit()>1;
	}
	final public	boolean required(){ 
		return required;			
	}
	final protected Grain<P,V>	limit(int value){
		limit = value;return this;
	}
	final protected	Grain<P,V>	multiple(int value) { 
		return limit(value);
	}
	final protected	Grain<P,V>	multiple(boolean value){ 
		return limit(value?Integer.MAX_VALUE:1);
	}
	final protected	Grain<P,V>   required(boolean value){ 
		required=value;return this;
	}
	
	public Grain(){
		try {
			Field p = this.getClass().getDeclaredField("this$0");
			if(Corn.class.isAssignableFrom(p.getType())){
				p.setAccessible(true);
				parent = (P) p.get(this);
				if(!this.getClass().getEnclosingClass().equals(parent.clazz())){
					throw new RuntimeException("Grain definition should be in parent class <"+this.getClass().getEnclosingClass()+","+ parent.clazz()+">");
				}	
				type = ReflectUtils.getTypeArguments(Grain.class, (Class<Grain>) getClass()).get(1);
				Corn.FieldInfo f = parent.field();
				if(f!=null){
					this.name   = f.field.getName();
					this.index  = f.index;
				}else{
					System.out.println(this.getClass());
				}
				if(type==null){
					throw new RuntimeException("Property type not detected "+getClass().getName());
				}
			}else{
				throw new RuntimeException("Grain should be defined in <Corn> class");
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		changed = false;
		required(false).multiple(false);
	}
		
	@Override
	public String toString() {
		return (changed()?old()+" < ":"")+get();
	}
	
	protected boolean isDifferent(Object a,Object b){
		return (
			(a==null && b!=null) ||
			(a!=null && b==null) ||
			(a!=null && b!=null && !a.equals(b))
		);
	}
	
	
	
}
