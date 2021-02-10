package net.romvoid95.curseforge.async;

import java.util.Iterator;
import java.util.stream.Stream;

import com.github.rjeschke.txtmark.Processor;
import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.file.CurseFile;
import com.therandomlabs.curseapi.project.CurseProject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.TextChannel;

public class Embed {

	public static EmbedBuilder noLinkEmbed(CurseProject proj, CurseFile file, TextChannel channel, String desc, String syntax) throws CurseException {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(proj.name(), proj.url().toString());
        embed.setThumbnail(proj.logo().thumbnailURL().toString());
        embed.setDescription(desc);
        embed.addField(EmbedBuilder.ZERO_WIDTH_SPACE,
                "**Release Type**: `" + file.releaseType().name() + "`" + "\n **File Name**: `" + file.displayName()
                        + "`" + "\n **Category**: `" + proj.categorySection().name() + "`" + "\n **GameVersion**: `"
                        + getGameVersions(proj) + "`", false);
        embed.addField(EmbedBuilder.ZERO_WIDTH_SPACE,
                "**Changelog:** \n```" + syntax + "\n" + formatChangelog(file) + "\n```",
                false);
        return embed;
	}
	
	public static EmbedBuilder curseLinkEmbed(CurseProject proj, CurseFile file, TextChannel channel, String desc, String syntax) throws CurseException {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(proj.name(), proj.url().toString());
        //embed.setThumbnail(proj.logo().thumbnailURL().toString());
        embed.setDescription(desc);
        embed.addField(getFieldCurseLink(proj, file));
        embed.addField(EmbedBuilder.ZERO_WIDTH_SPACE,
                "**Changelog:** \n```" + syntax + "\n" + formatChangelog(file) + "\n```",
                false);
        return embed;
	}
	
	private static Field getFieldCurseLink(CurseProject project, CurseFile file) throws CurseException {
		Field newField;
		if(project.categorySection().name().equalsIgnoreCase("modpacks") && file.hasAlternateFile()) {
			newField = new Field(EmbedBuilder.ZERO_WIDTH_SPACE,
					"**Release Type**: `" + file.releaseType().name() + "`" + "\n **File Name**: `" + file.displayName()
                    + "`" + "\n **Category**: `" + project.categorySection().name() + "`" + "\n **GameVersion**: `"
                    + getGameVersions(project) + "`" + "\n **Website Link**: " + "[CurseForge](" + getUrl(project) + ")"
                    + "\n **Server Files Download Link**: " + "[Download](" + getAltUrl(project)+ ")", false);
		} else {
			newField = new Field(EmbedBuilder.ZERO_WIDTH_SPACE,
	                "**Release Type**: `" + file.releaseType().name() + "`" + "\n **File Name**: `" + file.displayName()
                    + "`" + "\n **Category**: `" + project.categorySection().name() + "`" + "\n **GameVersion**: `"
                    + getGameVersions(project) + "`" + "\n **Website Link**: " + "[CurseForge](" + getUrl(project) + ")",
            false);
		}
		return newField;
	}
	
	public static EmbedBuilder directLinkEmbed(CurseProject proj, CurseFile file, TextChannel channel, String desc, String syntax) throws CurseException {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(proj.name(), proj.url().toString());
        embed.setThumbnail(proj.logo().thumbnailURL().toString());
        embed.setDescription(desc);
        embed.addField(getFieldDirectLink(proj, file));
        embed.addField(EmbedBuilder.ZERO_WIDTH_SPACE,
                "**Changelog:** \n```" + syntax + "\n" + formatChangelog(file) + "\n```",
                false);
        return embed;
	}
	
	private static Field getFieldDirectLink(CurseProject project, CurseFile file) throws CurseException {
		Field newField;
		if(project.categorySection().name().equalsIgnoreCase("modpacks") && file.hasAlternateFile()) {
			newField = new Field(EmbedBuilder.ZERO_WIDTH_SPACE,
                "**Release Type**: `" + file.releaseType().name() + "`" + "\n **File Name**: `" + file.displayName()
                        + "`" + "\n **Category**: `" + project.categorySection().name() + "`" + "\n **GameVersion**: `"
                        + getGameVersions(project) + "`" + "\n **Download Link**: " + "[Download](" + file.downloadURL()+ ")"
                        + "\n **Server Files Download Link**: " + "[Download](" + file.alternateFile().downloadURL()+ ")", false);
		} else {
			newField = new Field(EmbedBuilder.ZERO_WIDTH_SPACE,
                "**Release Type**: `" + file.releaseType().name() + "`" + "\n **File Name**: `" + file.displayName()
                        + "`" + "\n **Category**: `" + project.categorySection().name() + "`" + "\n **GameVersion**: `"
                        + getGameVersions(project) + "`" + "\n **Download Link**: " + "[Download](" + file.downloadURL()
                        + ")", false);
		}
		return newField;
	}
	
    /**
     * Return the newest file curseforge page url to embed into message.
     *
     * @param proj the proj
     * @return url link to file page
     * @throws CurseException the curse exception
     */
    private static String getUrl(final CurseProject proj) throws CurseException {
        String urlPre = proj.url().toString();
        int id = proj.files().first().id();
        return urlPre + "/files/" + id;
    }
    
    private static String getAltUrl(final CurseProject proj) throws CurseException {
        String urlPre = proj.url().toString();
        int id = proj.files().first().alternateFileID();
        return urlPre + "/files/" + id;
    }
    
    private static String formatChangelog(final CurseFile file) throws CurseException {
        String string = Processor.process(file.changelog().html()).replace("<br>", "\n").replace("&nbsp;", " ").replace("&lt;", "<").replace("&gt;",
                ">").replaceAll("(?s)<[^>]*>(<[^>]*>)*", "");
        string = string.replaceAll("https.*?\\s", "");
        String out = "";
        int additionalLines = 0;
        for (final String st : string.split("\n")) {
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
     * @param proj the proj
     * @return the game versions
     * @throws CurseException the curse exception
     */
    private static String getGameVersions(final CurseProject proj) throws CurseException {
        if (proj.files().first().gameVersionStrings().isEmpty())
            return "UNKNOWN";
        String out = "";
        final Stream<String> stream = proj.files().first().gameVersionStrings().stream().sorted();
        for (Iterator<String> it = stream.iterator(); it.hasNext(); ) {
            final String s = it.next();
            out = out + s + (it.hasNext() ? ", " : "");
        }
        return out;
    }
}
