/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.engine.extension;

import static org.junit.jupiter.api.extension.ConditionEvaluationResult.disabled;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.enabled;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Enabled;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.platform.commons.util.Preconditions;

/**
 * {@link ExecutionCondition} that supports the {@link Enabled @Enabled} annotation.
 *
 * @since 5.1
 * @see #evaluateExecutionCondition(ExtensionContext)
 */
class EnabledCondition implements ExecutionCondition {

	private static final Logger logger = LoggerFactory.getLogger(EnabledCondition.class);

	@Override
	public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
		Optional<AnnotatedElement> element = context.getElement();
		Optional<Enabled> annotation = findAnnotation(element, Enabled.class);
		if (!annotation.isPresent()) {
			return enabled("@Enabled is not present");
		}

		Optional<Disabled> disabled = findAnnotation(element, Disabled.class);
		if (disabled.isPresent()) {
			logger.warn(() -> "@Enabled and @Disabled found on " + element.get());
			return disabled("@Disabled is also present");
		}

		Enabled enabled = annotation.get();

		ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName(enabled.engine());
		Preconditions.notNull(scriptEngine, "Script engine not found: " + enabled.engine());

		boolean isJavaScript = "javascript".equals(scriptEngine.getFactory().getParameter(ScriptEngine.NAME));
		boolean isJavaImporter = isJavaScript && enabled.imports().length > 0;

		Bindings bindings = scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE);
		bindings.put("context", context);

		List<String> lines = new ArrayList<>();
		if (isJavaImporter) {
			String imports = String.join(", ", enabled.imports());
			lines.add("var javaImporter = new JavaImporter(" + imports + ");");
			lines.add("with (javaImporter) {");
		}
		for (String line : enabled.value()) {
			if (isJavaImporter) {
				line = "  " + line;
			}
			lines.add(line);
		}
		if (isJavaImporter) {
			lines.add("}");
			lines.add("");
		}
		String script = String.join(System.lineSeparator(), lines);
		try {
			Object result = scriptEngine.eval(script);
			boolean ok = Boolean.parseBoolean(String.valueOf(result));
			String reason = enabled.reason();
			if (reason.isEmpty()) {
				reason = String.format("script `%s` evaluated to: %s", script, result);
			}
			return ok ? enabled(reason) : disabled(reason);
		}
		catch (ScriptException e) {
			return disabled(e.getMessage());
		}
	}
}
