/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.api;

import static org.apiguardian.api.API.Status.STABLE;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apiguardian.api.API;

/**
 * {@code @Enabled} is used to signal that the annotated test class or
 * test method is only executed if and only if the script passed via
 * the {@link #value()} property evaluates to {@code true}.
 *
 * <p>When applied at the class level, all test methods within that class
 * are automatically disabled as well.
 *
 * @since 5.1
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = STABLE, since = "5.1")
public @interface Enabled {

	/**
	 * The short name of the ScriptEngine implementation.
	 *
	 * @return script engine name, defaults to {@code "JavaScript"}
	 */
	String engine() default "JavaScript";

	/**
	 * Package names to import.
	 *
	 * <p>{@code JavaImporter} takes a variable number of arguments as Java packages, and
	 * the returned object is used in a {@code with} statement whose scope includes the
	 * specified package imports. The global JavaScript scope is not affected.
	 *
	 * @return names of packages to import
	 */
	String[] imports() default {};

	/**
	 * Script predicate to evaluate.
	 *
	 * @return lines of the script predicate
	 */
	String[] value();

	/**
	 * Reason why the container or test should be enabled.
	 *
	 * @return the reason why the container or test should be enabled
	 */
	String reason() default "";

}
