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
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.file.CurseFile;
import com.therandomlabs.curseapi.file.CurseFiles;
import com.therandomlabs.curseapi.project.CurseProject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Utils {
	
	final static DateTimeFormatter dateOnly = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

	public static final Function<Guild, MessageEmbed> guildsFunc = new Function<Guild, MessageEmbed>() {

		@Override
		public MessageEmbed apply(Guild t) {

			return new EmbedBuilder().setTitle(t.getName(), t.getIconUrl())
					.setDescription(formatLine("Owner", t.getOwner().getUser().getAsTag()))
					.appendDescription(formatLine("Members", t.getMemberCount()))
					.appendDescription(formatLine("Created", t.getTimeCreated().format(dateOnly)))
					.setThumbnail(t.getIconUrl()).build();
		}

	};
	
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
							"**For Version(s)**: " + formatVersions(file.gameVersionStrings()) + "\n" +
							"**Type**: `" + DiscordUtils.capitalize(release) + "`\n" + 
							"**Link**: [" + file.displayName() + "](" + url(file) + ")",
							false)
					.setThumbnail(t.logo().thumbnailURL().toString()).build();
		}

	};
	
	private static String formatVersions(Set<String> set) {
		StringBuilder builder = new StringBuilder();
		for(String v : set) {
			builder.append("`" + v + "` ");
		}
		return builder.toString();
	}
	
	private static String formatTime(ZonedDateTime time) {
		return time.format(dateOnly) + " *(" + Utils.last(time.toLocalDate()) + " days ago)*";
	}
	
	private static String formatLine(String title, String rest) {
		return "**" + title + "**: \n" + rest + "\n\n";
	}
	
	private static String formatLine(String title, int rest) {
		return "**" + title + "**: \n" + rest + "\n\n";
	}

	public static Predicate<String> check(String string) {
		return new Predicate<String>() {

			@Override
			public boolean test(String input) {
				return input.equalsIgnoreCase(string);
			}
			
		};
	}

	private static CurseFile getFirst(CurseProject p) {
		try {
			CurseFiles<CurseFile> files = p.files();
			return files.first();
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
}
