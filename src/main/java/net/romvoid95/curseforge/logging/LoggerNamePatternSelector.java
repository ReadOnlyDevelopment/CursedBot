package net.romvoid95.curseforge.logging;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.layout.PatternMatch;
import org.apache.logging.log4j.core.layout.PatternSelector;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.util.PerformanceSensitive;

import java.util.ArrayList;
import java.util.List;

@Plugin(name = "LoggerNamePatternSelector", category = Node.CATEGORY, elementType = PatternSelector.ELEMENT_TYPE)
@PerformanceSensitive("allocation")
public class LoggerNamePatternSelector implements PatternSelector {
	private static class LoggerNameSelector {
		private final String name;
		private final boolean isPackage;
		private final PatternFormatter[] formatters;

		LoggerNameSelector(String name, PatternFormatter[] formatters){
			this.name = name;
			this.isPackage = name.endsWith(".");
			this.formatters = formatters;
		}

		PatternFormatter[] get() {
			return this.formatters;
		}

		boolean test(String s) {
			return this.isPackage ? s.startsWith(this.name) : s.equals(this.name);
		}

	}

	private final PatternFormatter[] defaultFormatters;
	private final List<LoggerNameSelector> formatters = new ArrayList<>();

	/**
	 * Constructs a new {@link LoggerNamePatternSelector}.
	 *
	 * @param defaultPattern
	 *                                  The default pattern to use if no logger name matches
	 * @param properties
	 *                                  The pattern match rules to use
	 * @param alwaysWriteExceptions
	 *                                  Write exceptions even if pattern does not include exception conversion
	 * @param disableAnsi
	 *                                  If true, disable all ANSI escape codes
	 * @param noConsoleNoAnsi
	 *                                  If true and {@link System#console()} is null, disable ANSI escape codes
	 * @param config
	 *                                  The configuration
	 */
	protected LoggerNamePatternSelector(String defaultPattern, PatternMatch[] properties,
			boolean alwaysWriteExceptions, boolean disableAnsi, boolean noConsoleNoAnsi, Configuration config){
		PatternParser parser = PatternLayout.createPatternParser(config);
		this.defaultFormatters = toArray(parser.parse(defaultPattern, alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi));
		for (PatternMatch property : properties) {
			PatternFormatter[] formatters = toArray(parser.parse(property.getPattern(), alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi));
			for (String name : property.getKey().split(",")) {
				this.formatters.add(new LoggerNameSelector(name, formatters));
			}
		}
	}

	private static PatternFormatter[] toArray(List<PatternFormatter> formatters) {
		return formatters.toArray(new PatternFormatter[formatters.size()]);
	}

	@Override
	public PatternFormatter[] getFormatters(LogEvent event) {
		final String loggerName = event.getLoggerName();
		if (loggerName != null) {
			// noinspection ForLoopReplaceableByForEach
			for (int i = 0; i < this.formatters.size(); i++) {
				LoggerNameSelector selector = this.formatters.get(i);
				if (selector.test(loggerName)) {
					return selector.get();
				}
			}
		}
		return this.defaultFormatters;
	}

	/**
	 * Creates a new {@link LoggerNamePatternSelector}.
	 *
	 * @param  defaultPattern
	 *                                   The default pattern to use if no logger name matches
	 * @param  properties
	 *                                   The pattern match rules to use
	 * @param  alwaysWriteExceptions
	 *                                   Write exceptions even if pattern does not include exception conversion
	 * @param  disableAnsi
	 *                                   If true, disable all ANSI escape codes
	 * @param  noConsoleNoAnsi
	 *                                   If true and {@link System#console()} is null, disable ANSI escape codes
	 * @param  config
	 *                                   The configuration
	 * 
	 * @return                       The new pattern selector
	 */
	@PluginFactory
	public static LoggerNamePatternSelector createSelector(
			@Required(message = "Default pattern is required") @PluginAttribute(value = "defaultPattern") String defaultPattern,
			@PluginElement("PatternMatch") PatternMatch[] properties,
			@PluginAttribute(value = "alwaysWriteExceptions", defaultBoolean = true) boolean alwaysWriteExceptions,
			@PluginAttribute("disableAnsi") boolean disableAnsi,
			@PluginAttribute("noConsoleNoAnsi") boolean noConsoleNoAnsi,
			@PluginConfiguration Configuration config) {
		return new LoggerNamePatternSelector(defaultPattern, properties, alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi, config);
	}

}
