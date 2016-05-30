/*
 * Copyright 2015-2016 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.junit.gen5.engine.support.filter;

import static org.junit.gen5.commons.meta.API.Usage.Internal;

import java.util.Optional;
import java.util.function.BiConsumer;

import org.junit.gen5.commons.meta.API;
import org.junit.gen5.commons.util.Preconditions;
import org.junit.gen5.engine.Filter;
import org.junit.gen5.engine.FilterResult;

/**
 * Decorator for a {@link Filter} that passes the <em>object</em> and the
 * <em>reason</em> to a {@link BiConsumer} in case it is <em>excluded</em>.
 *
 * @since 5.0
 */
@API(Internal)
public class ExclusionReasonConsumingFilter<T> implements Filter<T> {

	private final Filter<T> filter;
	private final BiConsumer<T, Optional<String>> reasonConsumer;

	/**
	 * Create a new {@code ExclusionReasonConsumingFilter} using the supplied
	 * {@code filter} and {@code reasonConsumer}.
	 *
	 * @param filter the filter to decorate; must not be {@code null}
	 * @param reasonConsumer the consumer to call in case of exclusions; must not be {@code null}
	 */
	public ExclusionReasonConsumingFilter(Filter<T> filter, BiConsumer<T, Optional<String>> reasonConsumer) {
		this.filter = Preconditions.notNull(filter, "filter must not be null");
		this.reasonConsumer = Preconditions.notNull(reasonConsumer, "reasonConsumer must not be null");
	}

	@Override
	public FilterResult apply(T object) {
		FilterResult result = this.filter.apply(object);
		if (result.excluded()) {
			this.reasonConsumer.accept(object, result.getReason());
		}
		return result;
	}

}
