/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carracoo.seeds.lang.grains;

import org.carracoo.seeds.lang.Corn;
import org.carracoo.seeds.lang.Grain;

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
