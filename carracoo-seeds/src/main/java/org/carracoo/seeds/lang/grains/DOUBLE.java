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
public abstract class DOUBLE<P extends Corn> extends NUMBER<P, Double> {

	@Override
	public final Class<?> type() {
		return Double.class;
	}
	
}
