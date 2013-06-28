/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carracoo.seeds.lang.grains;

import com.carracoo.seeds.lang.Corn;
import java.util.Date;

/**
 *
 * @author Sergey
 */
public abstract class DATE<P extends Corn> extends GRAIN<P, Date> {

	@Override
	public final Class<?> type() {
		return String.class;
	}
	
}
