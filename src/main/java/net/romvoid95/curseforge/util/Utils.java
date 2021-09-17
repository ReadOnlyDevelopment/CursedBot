package net.romvoid95.curseforge.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Function;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.google.common.base.Predicate;
import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.file.CurseFile;
import com.therandomlabs.curseapi.project.CurseProject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.romvoid95.curseforge.util.txtmark.HTML;

public class Utils {
	
	final static DateTimeFormatter dateOnly = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

	public static final Function<CurseProject, MessageEmbed> func = new Function<CurseProject, MessageEmbed>() {

		@Override
		public MessageEmbed apply(CurseProject t) {
			CurseFile file = getFirst(t);
			String release = file.releaseType().toString();

			return new EmbedBuilder().setTitle(t.name(), t.url().toString())
					.setDescription(formatLine("Author", t.author().name()))
					.appendDescription(formatLine("Downloads", t.downloadCount()))
					.appendDescription(formatLine("Created", t.creationTime().format(dateOnly)))
					.appendDescription(formatLine("Last Updated", formatTime(t.lastUpdateTime())))
					.appendDescription(formatLine("Summary","`" + t.summary() + "`"))
					.addBlankField(false)
					.addField("Latest File",
							"**Type**: `" + DiscordUtils.capitalize(release) + "`\n" + "**Link**: ["
									+ file.displayName() + "](" + url(file) + ")",
							false)
					.setThumbnail(t.logo().thumbnailURL().toString()).build();
		}

	};
	
	private static String formatTime(ZonedDateTime time) {
		return time.format(dateOnly) + " *(" + Utils.last(time.toLocalDate()) + " days ago)*";
	}
	
	private static String formatLine(String title, String rest) {
		return "**" + title + "**: \n" + rest + "\n\n";
	}
	
	private static String formatLine(String title, int rest) {
		return "**" + title + "**: \n" + rest + "\n\n";
	}

	public static Predicate<String> getAgumentPredicate(String... strings) {
		return new Predicate<String>() {
			@Override
			public boolean apply(@Nullable String input) {
				for (String string : strings) {
					if (!input.equalsIgnoreCase(string))
						return false;
				}
				return true;
			}
		};
	}

	private static CurseFile getFirst(CurseProject p) {
		try {
			return p.files().first();
		} catch (CurseException e) {
			return null;
		}
	}

	private static String url(CurseFile file) {
		try {
			return file.url().toString();
		} catch (CurseException e) {
			return null;
		}
	}

	public static final long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000;

	public static int last(LocalDate date) {
		return Period.between(date, getLocalDate()).getDays();
	}

	public static Date getDateWithoutTimeUsingCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	public static Date getDateWithoutTimeUsingFormat() throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.parse(formatter.format(new Date()));
	}

	public static LocalDate getLocalDate() {
		return LocalDate.now();
	}

	/** Random number generator value. */
	private static int RND = (int) System.nanoTime();

	/**
	 * LCG random number generator.
	 *
	 * @return A pseudo random number between 0 and 1023
	 */
	public final static int rnd() {
		return (RND = RND * 1664525 + 1013904223) >>> 22;
	}

	/**
	 * Skips spaces in the given String.
	 *
	 * @param  in
	 *                   Input String.
	 * @param  start
	 *                   Starting position.
	 * 
	 * @return       The new position or -1 if EOL has been reached.
	 */
	public final static int skipSpaces(final String in, final int start) {
		int pos = start;
		while (pos < in.length() && (in.charAt(pos) == ' ' || in.charAt(pos) == '\n')) {
			pos++;
		}
		return pos < in.length() ? pos : -1;
	}

	/**
	 * Processed the given escape sequence.
	 *
	 * @param  out
	 *                 The StringBuilder to write to.
	 * @param  ch
	 *                 The character.
	 * @param  pos
	 *                 Current parsing position.
	 * 
	 * @return     The new position.
	 */
	public final static int escape(final StringBuilder out, final char ch, final int pos) {
		switch (ch) {
		case '\\':
		case '[':
		case ']':
		case '(':
		case ')':
		case '{':
		case '}':
		case '#':
		case '"':
		case '\'':
		case '.':
		case '>':
		case '<':
		case '*':
		case '+':
		case '-':
		case '_':
		case '!':
		case '`':
		case '^':
			out.append(ch);
			return pos + 1;
		default:
			out.append('\\');
			return pos;
		}
	}

	/**
	 * Reads characters until any 'end' character is encountered.
	 *
	 * @param  out
	 *                   The StringBuilder to write to.
	 * @param  in
	 *                   The Input String.
	 * @param  start
	 *                   Starting position.
	 * @param  end
	 *                   End characters.
	 * 
	 * @return       The new position or -1 if no 'end' char was found.
	 */
	public final static int readUntil(final StringBuilder out, final String in, final int start, final char... end) {
		int pos = start;
		while (pos < in.length()) {
			final char ch = in.charAt(pos);
			if (ch == '\\' && pos + 1 < in.length()) {
				pos = escape(out, in.charAt(pos + 1), pos);
			} else {
				boolean endReached = false;
				for (int n = 0; n < end.length; n++) {
					if (ch == end[n]) {
						endReached = true;
						break;
					}
				}
				if (endReached) {
					break;
				}
				out.append(ch);
			}
			pos++;
		}
		return (pos == in.length()) ? -1 : pos;
	}

	/**
	 * Reads characters until the 'end' character is encountered.
	 *
	 * @param  out
	 *                   The StringBuilder to write to.
	 * @param  in
	 *                   The Input String.
	 * @param  start
	 *                   Starting position.
	 * @param  end
	 *                   End characters.
	 * 
	 * @return       The new position or -1 if no 'end' char was found.
	 */
	public final static int readUntil(final StringBuilder out, final String in, final int start, final char end) {
		int pos = start;
		while (pos < in.length()) {
			final char ch = in.charAt(pos);
			if (ch == '\\' && pos + 1 < in.length()) {
				pos = escape(out, in.charAt(pos + 1), pos);
			} else {
				if (ch == end) {
					break;
				}
				out.append(ch);
			}
			pos++;
		}
		return (pos == in.length()) ? -1 : pos;
	}

	/**
	 * Reads a markdown link.
	 *
	 * @param  out
	 *                   The StringBuilder to write to.
	 * @param  in
	 *                   Input String.
	 * @param  start
	 *                   Starting position.
	 * 
	 * @return       The new position or -1 if this is no valid markdown link.
	 */
	public final static int readMdLink(final StringBuilder out, final String in, final int start) {
		int pos = start;
		int counter = 1;
		while (pos < in.length()) {
			final char ch = in.charAt(pos);
			if (ch == '\\' && pos + 1 < in.length()) {
				pos = escape(out, in.charAt(pos + 1), pos);
			} else {
				boolean endReached = false;
				switch (ch) {
				case '(':
					counter++;
					break;
				case ' ':
					if (counter == 1) {
						endReached = true;
					}
					break;
				case ')':
					counter--;
					if (counter == 0) {
						endReached = true;
					}
					break;
				}
				if (endReached) {
					break;
				}
				out.append(ch);
			}
			pos++;
		}
		return (pos == in.length()) ? -1 : pos;
	}

	/**
	 * Reads a markdown link ID.
	 *
	 * @param  out
	 *                   The StringBuilder to write to.
	 * @param  in
	 *                   Input String.
	 * @param  start
	 *                   Starting position.
	 * 
	 * @return       The new position or -1 if this is no valid markdown link ID.
	 */
	public final static int readMdLinkId(final StringBuilder out, final String in, final int start) {
		int pos = start;
		int counter = 1;
		while (pos < in.length()) {
			final char ch = in.charAt(pos);
			boolean endReached = false;
			switch (ch) {
			case '\n':
				out.append(' ');
				break;
			case '[':
				counter++;
				out.append(ch);
				break;
			case ']':
				counter--;
				if (counter == 0) {
					endReached = true;
				} else {
					out.append(ch);
				}
				break;
			default:
				out.append(ch);
				break;
			}
			if (endReached) {
				break;
			}
			pos++;
		}
		return (pos == in.length()) ? -1 : pos;
	}

	/**
	 * Reads characters until any 'end' character is encountered, ignoring escape sequences.
	 *
	 * @param  out
	 *                   The StringBuilder to write to.
	 * @param  in
	 *                   The Input String.
	 * @param  start
	 *                   Starting position.
	 * @param  end
	 *                   End characters.
	 * 
	 * @return       The new position or -1 if no 'end' char was found.
	 */
	public final static int readRawUntil(final StringBuilder out, final String in, final int start, final char... end) {
		int pos = start;
		while (pos < in.length()) {
			final char ch = in.charAt(pos);
			boolean endReached = false;
			for (int n = 0; n < end.length; n++) {
				if (ch == end[n]) {
					endReached = true;
					break;
				}
			}
			if (endReached) {
				break;
			}
			out.append(ch);
			pos++;
		}
		return (pos == in.length()) ? -1 : pos;
	}

	/**
	 * Reads characters until the end character is encountered, taking care of HTML/XML strings.
	 *
	 * @param  out
	 *                   The StringBuilder to write to.
	 * @param  in
	 *                   The Input String.
	 * @param  start
	 *                   Starting position.
	 * @param  end
	 *                   End characters.
	 * 
	 * @return       The new position or -1 if no 'end' char was found.
	 */
	public final static int readRawUntil(final StringBuilder out, final String in, final int start, final char end) {
		int pos = start;
		while (pos < in.length()) {
			final char ch = in.charAt(pos);
			if (ch == end) {
				break;
			}
			out.append(ch);
			pos++;
		}
		return (pos == in.length()) ? -1 : pos;
	}

	/**
	 * Reads characters until any 'end' character is encountered, ignoring escape sequences.
	 *
	 * @param  out
	 *                   The StringBuilder to write to.
	 * @param  in
	 *                   The Input String.
	 * @param  start
	 *                   Starting position.
	 * @param  end
	 *                   End characters.
	 * 
	 * @return       The new position or -1 if no 'end' char was found.
	 */
	public final static int readXMLUntil(final StringBuilder out, final String in, final int start, final char... end) {
		int pos = start;
		boolean inString = false;
		char stringChar = 0;
		while (pos < in.length()) {
			final char ch = in.charAt(pos);
			if (inString) {
				if (ch == '\\') {
					out.append(ch);
					pos++;
					if (pos < in.length()) {
						out.append(ch);
						pos++;
					}
					continue;
				}
				if (ch == stringChar) {
					inString = false;
					out.append(ch);
					pos++;
					continue;
				}
			}
			switch (ch) {
			case '"':
			case '\'':
				inString = true;
				stringChar = ch;
				break;
			}
			if (!inString) {
				boolean endReached = false;
				for (int n = 0; n < end.length; n++) {
					if (ch == end[n]) {
						endReached = true;
						break;
					}
				}
				if (endReached) {
					break;
				}
			}
			out.append(ch);
			pos++;
		}
		return (pos == in.length()) ? -1 : pos;
	}

	/**
	 * Appends the given string encoding special HTML characters.
	 *
	 * @param out
	 *                  The StringBuilder to write to.
	 * @param in
	 *                  Input String.
	 * @param start
	 *                  Input String starting position.
	 * @param end
	 *                  Input String end position.
	 */
	public final static void appendCode(final StringBuilder out, final String in, final int start, final int end) {
		for (int i = start; i < end; i++) {
			final char c;
			switch (c = in.charAt(i)) {
			case '&':
				out.append("&amp;");
				break;
			case '<':
				out.append("&lt;");
				break;
			case '>':
				out.append("&gt;");
				break;
			default:
				out.append(c);
				break;
			}
		}
	}

	/**
	 * Appends the given string encoding special HTML characters (used in HTML attribute values).
	 *
	 * @param out
	 *                  The StringBuilder to write to.
	 * @param in
	 *                  Input String.
	 * @param start
	 *                  Input String starting position.
	 * @param end
	 *                  Input String end position.
	 */
	public final static void appendValue(final StringBuilder out, final String in, final int start, final int end) {
		for (int i = start; i < end; i++) {
			final char c;
			switch (c = in.charAt(i)) {
			case '&':
				out.append("&amp;");
				break;
			case '<':
				out.append("&lt;");
				break;
			case '>':
				out.append("&gt;");
				break;
			case '"':
				out.append("&quot;");
				break;
			case '\'':
				out.append("&apos;");
				break;
			default:
				out.append(c);
				break;
			}
		}
	}

	/**
	 * Append the given char as a decimal HTML entity.
	 *
	 * @param out
	 *                  The StringBuilder to write to.
	 * @param value
	 *                  The character.
	 */
	public final static void appendDecEntity(final StringBuilder out, final char value) {
		out.append("&#");
		out.append((int) value);
		out.append(';');
	}

	/**
	 * Append the given char as a hexadecimal HTML entity.
	 *
	 * @param out
	 *                  The StringBuilder to write to.
	 * @param value
	 *                  The character.
	 */
	public final static void appendHexEntity(final StringBuilder out, final char value) {
		out.append("&#x");
		out.append(Integer.toHexString(value));
		out.append(';');
	}

	/**
	 * Appends the given mailto link using obfuscation.
	 *
	 * @param out
	 *                  The StringBuilder to write to.
	 * @param in
	 *                  Input String.
	 * @param start
	 *                  Input String starting position.
	 * @param end
	 *                  Input String end position.
	 */
	public final static void appendMailto(final StringBuilder out, final String in, final int start, final int end) {
		for (int i = start; i < end; i++) {
			final char c;
			final int r = rnd();
			switch (c = in.charAt(i)) {
			case '&':
			case '<':
			case '>':
			case '"':
			case '\'':
			case '@':
				if (r < 512) {
					appendDecEntity(out, c);
				} else {
					appendHexEntity(out, c);
				}
				break;
			default:
				if (r < 32) {
					out.append(c);
				} else if (r < 520) {
					appendDecEntity(out, c);
				} else {
					appendHexEntity(out, c);
				}
				break;
			}
		}
	}

	/**
	 * Extracts the tag from an XML element.
	 *
	 * @param out
	 *                The StringBuilder to write to.
	 * @param in
	 *                Input StringBuilder.
	 */
	public final static void getXMLTag(final StringBuilder out, final StringBuilder in) {
		int pos = 1;
		if (in.charAt(1) == '/') {
			pos++;
		}
		while (Character.isLetterOrDigit(in.charAt(pos))) {
			out.append(in.charAt(pos++));
		}
	}

	/**
	 * Extracts the tag from an XML element.
	 *
	 * @param out
	 *                The StringBuilder to write to.
	 * @param in
	 *                Input String.
	 */
	public final static void getXMLTag(final StringBuilder out, final String in) {
		int pos = 1;
		if (in.charAt(1) == '/') {
			pos++;
		}
		while (Character.isLetterOrDigit(in.charAt(pos))) {
			out.append(in.charAt(pos++));
		}
	}

	/**
	 * Reads an XML element.
	 *
	 * @param  out
	 *                      The StringBuilder to write to.
	 * @param  in
	 *                      Input String.
	 * @param  start
	 *                      Starting position.
	 * @param  safeMode
	 *                      Whether to escape unsafe HTML tags or not
	 * 
	 * @return          The new position or -1 if this is no valid XML element.
	 */
	public final static int readXML(final StringBuilder out, final String in, final int start, final boolean safeMode) {
		int pos;
		final boolean isCloseTag;
		try {
			if (in.charAt(start + 1) == '/') {
				isCloseTag = true;
				pos = start + 2;
			} else if (in.charAt(start + 1) == '!') {
				out.append("<!");
				return start + 1;
			} else {
				isCloseTag = false;
				pos = start + 1;
			}
			if (safeMode) {
				final StringBuilder temp = new StringBuilder();
				pos = readXMLUntil(temp, in, pos, ' ', '/', '>');
				if (pos == -1) {
					return -1;
				}
				final String tag = temp.toString().trim().toLowerCase();
				if (HTML.isUnsafeHtmlElement(tag)) {
					out.append("&lt;");
				} else {
					out.append("<");
				}
				if (isCloseTag) {
					out.append('/');
				}
				out.append(temp);
			} else {
				out.append('<');
				if (isCloseTag) {
					out.append('/');
				}
				pos = readXMLUntil(out, in, pos, ' ', '/', '>');
			}
			if (pos == -1) {
				return -1;
			}
			pos = readXMLUntil(out, in, pos, '/', '>');
			if (in.charAt(pos) == '/') {
				out.append(" /");
				pos = readXMLUntil(out, in, pos + 1, '>');
				if (pos == -1) {
					return -1;
				}
			}
			if (in.charAt(pos) == '>') {
				out.append('>');
				return pos;
			}
		} catch (final StringIndexOutOfBoundsException e) {
			return -1;
		}
		return -1;
	}

	/**
	 * Appends the given string to the given StringBuilder, replacing '&amp;', '&lt;' and '&gt;' by their respective HTML entities.
	 *
	 * @param out
	 *                   The StringBuilder to append to.
	 * @param value
	 *                   The string to append.
	 * @param offset
	 *                   The character offset into value from where to start
	 */
	public final static void codeEncode(final StringBuilder out, final String value, final int offset) {
		for (int i = offset; i < value.length(); i++) {
			final char c = value.charAt(i);
			switch (c) {
			case '&':
				out.append("&amp;");
				break;
			case '<':
				out.append("&lt;");
				break;
			case '>':
				out.append("&gt;");
				break;
			default:
				out.append(c);
			}
		}
	}

	/**
	 * Removes trailing <code>`</code> or <code>~</code> and trims spaces.
	 *
	 * @param fenceLine
	 *                      Fenced code block starting line
	 */
	public final static String getMetaFromFence(final String fenceLine) {
		for (int i = 0; i < fenceLine.length(); i++) {
			final char c = fenceLine.charAt(i);
			if (!Character.isWhitespace(c) && c != '`' && c != '~') {
				return fenceLine.substring(i).trim();
			}
		}
		return "";
	}
}
