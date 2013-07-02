/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carracoo.seeds.lang.grains;

import org.carracoo.seeds.lang.Corn;

/**
 *
 * @author Sergey
 */
public abstract class BOOLEAN<P extends Corn> extends GRAIN<P, Boolean> {

	@Override
	public final Class<?> type() {
		return Boolean.class;
	}
	
}
