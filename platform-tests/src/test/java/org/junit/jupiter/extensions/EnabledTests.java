/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.extensions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Enabled;
import org.junit.jupiter.api.Test;

/**
 * JavaScript-based execution condition evaluation tests.
 *
 * @since 1.1
 */
@Enabled("true")
class EnabledTests {

	@Test
	@Enabled("true")
	void justTrue() {
	}

	@Test
	@Enabled("false")
	void justFalse() {
		fail("test must not be executed");
	}

	@Test
	@Enabled("1 == 2")
	void oneEqualsTwo() {
		fail("test must not be executed");
	}

	@Test
	@Enabled("java.lang.Boolean.getBoolean('is-not-set')")
	void getBoolean() {
		fail("test must not be executed");
	}

	@Test
	@Enabled(imports = "java.nio.file", value = "Files.exists(Files.createTempFile('temp-', '.txt'))")
	void filesExists() {
	}

	@Test
	@Enabled(value = "context.publishReportEntry('foo', 'bar')", reason = "no result, no execution")
	void publishReportEntry() {
		fail("test must not be executed");
	}

	@Test
	@Enabled("java.lang.System.getProperty('os.name').toLowerCase().contains('win')")
	void win() {
		assertTrue(System.getProperty("os.name").toLowerCase().contains("win"));
	}

	@Test
	@Enabled(engine = "groovy", value = { "System.properties['nine'] = 9", "9 == System.properties['nine']" })
	void groovy() {
		assertEquals(9, System.getProperties().get("nine"));
	}

	@Test
	@Enabled("true")
	@Disabled
	void enabledAndDisabled() {
	}

	@Test
	@Disabled
	@Enabled("true")
	void disabledAndEnabled() {
	}

}
