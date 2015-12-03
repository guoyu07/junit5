/*
 * Copyright 2015 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.junit.gen5.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code @AfterEach} is used to signal that the annotated method should be
 * executed <em>after</em> <strong>each</strong> {@code @Test} method in
 * the current test class or test class hierarchy.
 *
 * <p>{@code @AfterEach} methods must not be {@code private} or {@code static}.
 *
 * <p>{@code @AfterEach} methods may optionally declare parameters to be
 * resolved by {@link org.junit.gen5.api.extension.MethodParameterResolver
 * MethodParameterResolvers}.
 *
 * @since 5.0
 * @see BeforeEach
 * @see BeforeAll
 * @see AfterAll
 * @see Test
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AfterEach {
}
