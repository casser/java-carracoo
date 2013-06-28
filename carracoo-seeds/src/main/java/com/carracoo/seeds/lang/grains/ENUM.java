/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carracoo.seeds.lang.grains;

import com.carracoo.seeds.SeedView;
import com.carracoo.seeds.lang.Corn;

/**
 *
 * @author Sergey
 */
public abstract class ENUM<P extends Corn,V extends Enum> extends GRAIN<P, V>{
	public static <T extends Enum<T>> T valueOfIgnoreCase(Class<T> enumeration, String name) {
		for(Enum enumValue : enumeration.getEnumConstants()) {
			if(enumValue.name().equalsIgnoreCase(name)) {
				return (T) enumValue;
			}
		}
		throw new IllegalArgumentException("There is no value with name '" + name + " in Enum " + enumeration.getClass().getName());        
	}
	
	public P set(String value) {
		return super.set((V)valueOfIgnoreCase((Class<Enum>) type(), value));
	}

	@Override
	public Object get(SeedView view) {
		return get().name().toLowerCase();
	}
	
}
