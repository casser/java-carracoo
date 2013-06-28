/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carracoo.seeds.lang.grains;

import com.carracoo.seeds.lang.Corn;
import com.carracoo.seeds.lang.Grain;

/**
 *
 * @author Sergey
 */
public abstract class GRAIN<P extends Corn, V> extends Grain<P, V> {
	@Override
	protected V create() {
		return null;
	}
}
