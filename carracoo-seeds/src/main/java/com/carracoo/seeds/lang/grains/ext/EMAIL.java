/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carracoo.seeds.lang.grains.ext;

import com.carracoo.seeds.lang.Corn;
import com.carracoo.seeds.lang.grains.STRING;

import java.util.regex.Pattern;

/**
 *
 * @author Sergey
 */
 public class EMAIL<P extends Corn>	 extends STRING<P> {
	private final static Pattern EMAIL_REGEXP = Pattern.compile("^[-!#$%&'*+/0-9=?A-Z^_a-z{|}~](\\.?[-!#$%&'*+/0-9=?A-Z^_a-z{|}~])*@[a-zA-Z](-?[a-zA-Z0-9])*(\\.[a-zA-Z](-?[a-zA-Z0-9])*)+$");
	public EMAIL() {
		super();
		format(EMAIL_REGEXP);
	}		
}

