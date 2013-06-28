/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carracoo.seeds;

import com.carracoo.seeds.lang.Grain;
import java.util.Stack;

/**
 *
 * @author Sergey
 */
public class SeedView {
	
	public static class Path extends Stack<String>{
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
	
	private final String name;
	public String name(){
		return name;
	}
	
	private final Path path;
	public Path path(){
		return path;
	}
	
	public SeedView(String name){
		this(name,new Path());
	}
	
	public SeedView(String name,Path path){
		this.name = name;
		this.path = path;
	}
	
	public boolean is(String view) {
		return this.name.equals(view);
	}
	public boolean isRoot() {
		return path.size()==1;
	}
	public void enter(Grain property) {
		enter(property.parrent().getClass().getSimpleName()+":"+property.type().getSimpleName()+":"+property.name());
	}
	public void enter(int i) {
		enter("#"+i);
	}
	public void enter(String path) {
		path().push(path);
	}
	
	public void exit() {
		path().pop();
	}
	
}
