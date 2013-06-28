/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carracoo.seeds.lang.grains;

import com.carracoo.seeds.exceptions.ValidationException;
import com.carracoo.seeds.lang.Corn;

import java.util.regex.Pattern;

/**
 *
 * @author Sergey
 */
public abstract class STRING<P extends Corn> extends GRAIN<P, String> {
	private Pattern format;
	@Override
	public final Class<?> type() {
		return String.class;
	}

	public Pattern format() {
		return format;
	}

	public Pattern format(String format) {
		return format(Pattern.compile(format));
	}

	public Pattern format(Pattern format) {
		return this.format = format;
	}

	@Override
	public P validate() throws ValidationException {
		if (!isNull() && format() != null) {
			if (!format().matcher(get()).matches()) {
				throw new ValidationException("invalid property '" + parrent().getClass().getSimpleName() + ":" + name() + "' /" + value() + "/ should match /" + format().pattern() + "/");
			}
		}
		return (P) super.validate();
	}
	
}
