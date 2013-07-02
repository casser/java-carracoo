package org.carracoo.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Validators {

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Text {
		String pattern();
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Number {
		int max() default Integer.MAX_VALUE;

		int min() default Integer.MIN_VALUE;
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Array {
		boolean unique() default false;
	}

}
