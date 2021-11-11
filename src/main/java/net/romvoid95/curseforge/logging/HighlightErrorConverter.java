package net.romvoid95.curseforge.logging;

import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "highlightError", category = PatternConverter.CATEGORY)
@ConverterKeys({ "highlightError" })
@PerformanceSensitive("allocation")
public class HighlightErrorConverter extends LogEventPatternConverter {
	private static final String ANSI_RESET = "\u001B[39;0m";
	private static final String ANSI_ERROR = "\u001B[31;1m";
	private static final String ANSI_WARN = "\u001B[33;1m";
	private static final String ANSI_INFO = "\u001B[32;1m";
	private static final String ANSI_DEBUG = "\u001B[34;1m";

	private final List<PatternFormatter> formatters;

	protected HighlightErrorConverter(List<PatternFormatter> formatters){
		super("highlightError", null);
		this.formatters = formatters;
	}

	@Override
	public void format(LogEvent event, StringBuilder toAppendTo) {
		if (TerminalConsoleAppender.isAnsiSupported()) {
			Level level = event.getLevel();
			if (level.isMoreSpecificThan(Level.ERROR)) {
				format(ANSI_ERROR, event, toAppendTo);
				return;
			} else if (level.isMoreSpecificThan(Level.WARN)) {
				format(ANSI_WARN, event, toAppendTo);
				return;
			} else if (level.isMoreSpecificThan(Level.INFO)) {
				format(ANSI_INFO, event, toAppendTo);
				return;
			} else if (level.isMoreSpecificThan(Level.DEBUG)) {
				format(ANSI_DEBUG, event, toAppendTo);
				return;
			}
		}
		for (int i = 0, size = formatters.size(); i < size; i++) {
			formatters.get(i).format(event, toAppendTo);
		}
	}

	private void format(String style, LogEvent event, StringBuilder toAppendTo) {
		int start = toAppendTo.length();
		toAppendTo.append(style);
		int end = toAppendTo.length();
		for (int i = 0, size = formatters.size(); i < size; i++) {
			formatters.get(i).format(event, toAppendTo);
		}
		if (toAppendTo.length() == end) {
			toAppendTo.setLength(start);
		} else {
			toAppendTo.append(ANSI_RESET);
		}
	}

	@Override
	public boolean handlesThrowable() {
		for (final PatternFormatter formatter : formatters) {
			if (formatter.handlesThrowable()) {
				return true;
			}
		}
		return false;
	}

	@Nullable
	public static HighlightErrorConverter newInstance(Configuration config, String[] options) {
		if (options.length != 1) {
			LOGGER.error("Incorrect number of options on highlightError. Expected 1 received " + options.length);
			return null;
		}
		if (options[0] == null) {
			LOGGER.error("No pattern supplied on highlightError");
			return null;
		}
		PatternParser parser = PatternLayout.createPatternParser(config);
		List<PatternFormatter> formatters = parser.parse(options[0]);
		return new HighlightErrorConverter(formatters);
	}
}
