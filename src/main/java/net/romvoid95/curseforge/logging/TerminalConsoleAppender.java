package net.romvoid95.curseforge.logging;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

@Plugin(name = TerminalConsoleAppender.PLUGIN_NAME, category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class TerminalConsoleAppender extends AbstractAppender
{
    public static final String PLUGIN_NAME = "TerminalConsole";
    public static final String PROPERTY_PREFIX = "terminal";
    public static final String JLINE_OVERRIDE_PROPERTY = PROPERTY_PREFIX + ".jline";
    public static final String ANSI_OVERRIDE_PROPERTY = PROPERTY_PREFIX + ".ansi";
    public static final Boolean ANSI_OVERRIDE = getOptionalBooleanProperty(ANSI_OVERRIDE_PROPERTY);
    private static final PrintStream stdout = System.out;
    private static boolean initialized;
    @Nullable
    private static Terminal terminal;
    @Nullable
    private static LineReader reader;

    /**
     * Returns the {@link Terminal} that is used to print messages to the
     * console. Returns {@code null} in unsupported environments, unless
     * overridden using the {@link TerminalConsoleAppender#JLINE_OVERRIDE_PROPERTY} system
     * property.
     *
     * @return The terminal, or null if not supported
     * @see TerminalConsoleAppender
     */
    @Nullable
    public static Terminal getTerminal()
    {
        return terminal;
    }

    /**
     * Returns the currently configured {@link LineReader} that is used to
     * read input from the console. May be null if no {@link LineReader}
     * was configured by the environment.
     *
     * @return The current line reader, or null if none
     */
    @Nullable
    public static LineReader getReader()
    {
        return reader;
    }

    /**
     * Sets the {@link LineReader} that is used to read input from the console.
     * Setting the {@link LineReader} will allow the appender to automatically
     * redraw the input line when a new log message is added.
     *
     * <p><b>Note:</b> The specified {@link LineReader} must be created with
     * the terminal returned by {@link #getTerminal()}.</p>
     *
     * @param newReader The new line reader
     */
    public static void setReader(@Nullable LineReader newReader)
    {
        if (newReader != null && newReader.getTerminal() != terminal)
        {
            throw new IllegalArgumentException("Reader was not created with TerminalConsoleAppender.getTerminal()");
        }

        reader = newReader;
    }

    /**
     * Returns whether ANSI escapes codes should be written to the console
     * output.
     *
     * <p>The return value is {@code true} by default if the JLine terminal
     * is enabled and {@code false} otherwise. It may be overridden using
     * the {@link TerminalConsoleAppender#ANSI_OVERRIDE_PROPERTY} system property.</p>
     *
     * @return true if ANSI escapes codes should be written to the console
     */
    public static boolean isAnsiSupported()
    {
        return ANSI_OVERRIDE != null ? ANSI_OVERRIDE : terminal != null;
    }

    /**
     * Constructs a new {@link TerminalConsoleAppender}.
     *
     * @param name             The name of the appender
     * @param filter           The filter, can be {@code null}
     * @param layout           The layout to use
     * @param ignoreExceptions If {@code true} exceptions encountered when
     *                         appending events are logged, otherwise they are propagated to the
     *                         caller
     */
    protected TerminalConsoleAppender(String name, @Nullable Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions)
    {
        super(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
        initializeTerminal();
    }

    private static void initializeTerminal()
    {
        if (!initialized)
        {
            initialized = true;

            Boolean jlineOverride = getOptionalBooleanProperty(JLINE_OVERRIDE_PROPERTY);

            boolean dumb = jlineOverride == Boolean.TRUE || System.getProperty("java.class.path").contains("idea_rt.jar");

            if (jlineOverride != Boolean.FALSE)
            {
                try
                {
                    terminal = TerminalBuilder.builder().dumb(dumb).build();
                }
                catch (IllegalStateException e)
                {
                    if (LOGGER.isDebugEnabled())
                    {
                        LOGGER.warn("Disabling terminal, you're running in an unsupported environment.", e);
                    }
                    else
                    {
                        LOGGER.warn("Disabling terminal, you're running in an unsupported environment.");
                    }
                }
                catch (IOException e)
                {
                    LOGGER.error("Failed to initialize terminal. Falling back to standard output", e);
                }
            }
        }
    }

    @Nullable
    private static Boolean getOptionalBooleanProperty(String name)
    {
        String value = PropertiesUtil.getProperties().getStringProperty(name);
        if (value == null)
        {
            return null;
        }

        if (value.equalsIgnoreCase("true"))
        {
            return Boolean.TRUE;
        }
        else if (value.equalsIgnoreCase("false"))
        {
            return Boolean.FALSE;
        }
        else
        {
            LOGGER.warn("Invalid value for boolean input property '{}': {}", name, value);
            return null;
        }
    }

    @Override
    public void append(LogEvent event)
    {
        if (terminal != null)
        {
            if (reader != null)
            {
                reader.callWidget(LineReader.CLEAR);
                terminal.writer().print(getLayout().toSerializable(event));
                reader.callWidget(LineReader.REDRAW_LINE);
                reader.callWidget(LineReader.REDISPLAY);
            }
            else
            {
                terminal.writer().print(getLayout().toSerializable(event));
            }

            terminal.writer().flush();
        }
        else
        {
            stdout.print(getLayout().toSerializable(event));
        }
    }

    public static void close() throws IOException
    {
        if (initialized)
        {
            initialized = false;
            if (terminal != null)
            {
                try
                {
                    terminal.close();
                }
                finally
                {
                    terminal = null;
                }
            }
        }
    }

    @PluginFactory
    public static TerminalConsoleAppender createAppender(
            @Required(message = "No name provided for TerminalConsoleAppender") @PluginAttribute("name") String name,
            @PluginElement("Filter") @Nullable Filter filter,
            @PluginElement("Layout") @Nullable Layout<? extends Serializable> layout,
            @PluginAttribute(value = "ignoreExceptions", defaultBoolean = true) boolean ignoreExceptions)
    {
        if (layout == null)
        {
            layout = PatternLayout.createDefaultLayout();
        }

        return new TerminalConsoleAppender(name, filter, layout, ignoreExceptions);
    }
}
