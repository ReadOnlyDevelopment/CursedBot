package net.romvoid95.curseforge.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.file.CurseFile;
import com.therandomlabs.curseapi.project.CurseProject;

import io.github.furstenheim.CopyDown;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;

public class Embed {

	public static EmbedBuilder noLinkEmbed(CurseProject proj, CurseFile file, TextChannel channel, String desc, String syntax) throws CurseException {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(proj.name(), proj.url().toString());
		embed.setDescription(desc + " " + proj.name());
		embed.setThumbnail(proj.logo().thumbnailURL().toString());
		embed.addField(EmbedBuilder.ZERO_WIDTH_SPACE, "**Release Type**: `" + file.releaseType().name() + "`" + "\n **File Name**: `" + file.displayName() + "`" + "\n **Category**: `" + proj.categorySection().name() + "`" + "\n **GameVersion**: `" + getGameVersions(file) + "`",
				false);
		embed.addField(EmbedBuilder.ZERO_WIDTH_SPACE, "**Changelog:** \n```" + syntax + "\n" + formatChangelog(file) + "\n```", false);
		return embed;
	}

	public static EmbedBuilder curseLinkEmbed(CurseProject proj, CurseFile file, TextChannel channel, String desc, String syntax) throws CurseException {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(proj.name(), proj.url().toString());
		embed.setDescription(desc + " " + proj.name());
		embed.setThumbnail(proj.logo().thumbnailURL().toString());
		embed.addField(getField(proj, file));
		embed.addField(EmbedBuilder.ZERO_WIDTH_SPACE, "**Changelog:** \n```" + syntax + "\n" + formatChangelog(file) + "\n```", false);
		return embed;
	}

	public static EmbedBuilder directLinkEmbed(CurseProject proj, CurseFile file, TextChannel channel, String desc, String syntax) throws CurseException {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(proj.name(), proj.url().toString());
		embed.setDescription(desc + " " + proj.name());
		embed.setThumbnail(proj.logo().thumbnailURL().toString());
		embed.addField(getField(proj, file));
		embed.addField(EmbedBuilder.ZERO_WIDTH_SPACE, "**Changelog:** \n```" + syntax + "\n" + formatChangelog(file) + "\n```", false);
		return embed;
	}

	public static List<Component> getCursePageButtons(CurseProject proj) throws CurseException {
		List<Component> components = new ArrayList<>();
		if (proj.categorySection().name().equalsIgnoreCase("modpacks")) {
			components.add(Button.link(getUrl(proj), "Webpage Link"));
			if (proj.files().first().hasAlternateFile()) {
				components.add(Button.link(proj.files().first().alternateFile().downloadURL().toString(), "Download Server File"));
			}
		} else {
			components.add(Button.link(getUrl(proj), "Download"));
		}
		return components;
	}

	public static List<Component> getDirectDownloadButtons(CurseProject proj, CurseFile file) throws CurseException {
		List<Component> components = new ArrayList<>();
		if (proj.categorySection().name().equalsIgnoreCase("modpacks")) {
			components.add(Button.link(file.downloadURL().toString(), "Download Pack"));
			if (file.hasAlternateFile()) {
				components.add(Button.link(file.alternateFile().downloadURL().toString(), "Download Server File"));
			}
		} else {
			components.add(Button.link(file.downloadURL().toString(), "Download"));
		}
		return components;
	}

	private static Field getField(CurseProject project, CurseFile file) throws CurseException {
		Field newField;
		if (project.categorySection().name().equalsIgnoreCase("modpacks") && file.hasAlternateFile()) {
			newField = new Field(EmbedBuilder.ZERO_WIDTH_SPACE,
					"**Release Type**: `" + file.releaseType().name() + "`" + "\n **File Name**: `" + file.displayName() + "`" + "\n **Category**: `" + project.categorySection().name() + "`" + "\n **GameVersion**: `" + getGameVersions(file) + "`", false);
		} else {
			newField = new Field(EmbedBuilder.ZERO_WIDTH_SPACE,
					"**Release Type**: `" + file.releaseType().name() + "`" + "\n **File Name**: `" + file.displayName() + "`" + "\n **Category**: `" + project.categorySection().name() + "`" + "\n **GameVersion**: `" + getGameVersions(file) + "`", false);
		}
		return newField;
	}

	/**
	 * Return the newest file curseforge page url to embed into message.
	 *
	 * @param  proj
	 *                            the proj
	 * 
	 * @return                url link to file page
	 * 
	 * @throws CurseException
	 *                            the curse exception
	 */
	private static String getUrl(final CurseProject proj) throws CurseException {
		String urlPre = proj.url().toString();
		int id = proj.files().first().id();
		return urlPre + "/files/" + id;
	}

    /**
     * Format changelog.
     *
     * @param s the s
     * @return the string
     * @throws CurseException 
     */
    private static String formatChangelog(CurseFile file) throws CurseException {
    	getGameVersions(file);
    	String html = file.changelog().html();
        String string = getChangelog(html).replace("\\", "");
        String out = "";
        int additionalLines = 0;
        for (String st : string.split("\n")) {
    		if ((out + st.trim() + "\n").length() > 950) {
                additionalLines++;
            } else // noinspection StringConcatenationInLoop
                out = out + st.trim() + "\n";
        }
        return out + (additionalLines > 0 ? ("... And " + additionalLines + " more lines") : "");
    }

	/**
	 * Gets the game versions.
	 *
	 * @param  proj
	 *                            the proj
	 * 
	 * @return                the game versions
	 * 
	 * @throws CurseException
	 *                            the curse exception
	 */
	private static String getGameVersions(final CurseFile file) throws CurseException {
		if (file.gameVersionStrings().isEmpty())
			return "UNKNOWN";
		String out = "";
		final Stream<String> stream = file.gameVersionStrings().stream().sorted();
		for (Iterator<String> it = stream.iterator(); it.hasNext();) {
			final String s = it.next();
			out = out + s + (it.hasNext() ? ", " : "");
		}
		return out;
	}

	static String getChangelog(String html) {
		return new CopyDown().convert(html);
	}
}
