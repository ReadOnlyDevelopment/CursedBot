package net.romvoid95.curseforge.data.override;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.romvoid95.curseforge.CursedBot;
import net.romvoid95.curseforge.data.Data;
import net.romvoid95.curseforge.data.FileLink;
import net.romvoid95.curseforge.util.DiscordUtils;
import net.romvoid95.curseforge.util.builder.DataClass;

public class OverrideList {
	
	private List<ProjectOverride> overrides = new ArrayList<ProjectOverride>();

	public OverrideList() {
	}

	public OverrideList(List<ProjectOverride> overrides) {
		super();
		this.overrides = overrides;
	}

	public List<ProjectOverride> getOverrides() {
		return overrides;
	}

	public void setOverrides(List<ProjectOverride> overrides) {
		this.overrides = overrides;
	}

	public OverrideList withOverrides(List<ProjectOverride> overrides) {
		this.overrides = overrides;
		return this;
	}
	
	public static class ProjectOverride extends DataClass<ProjectOverride> {

		private Integer id;
		private String channel = "default";
		private String role = "default";
		private FileLink fileLink = FileLink.DEFAULT;
		private String description = "default";
		private String discordFormat = "default";
		
		public ProjectOverride() {}
		
		public ProjectOverride(Integer id) {
			super();
			this.id = id;
		}

		public ProjectOverride(String channel, String role, FileLink fileLink, String description, String discordFormat) {
			super();
			this.channel = channel;
			this.role = role;
			this.fileLink = fileLink;
			this.description = description;
			this.discordFormat = discordFormat;
		}
		
		public Integer getId() {
			return id;
		}

		public String getChannel() {
			return channel;
		}

		public String getRole() {
			return role;
		}

		public FileLink getFileLink() {
			return fileLink;
		}

		public String getDescription() {
			return description;
		}

		public String getDiscordFormat() {
			return discordFormat;
		}
		
		@JsonIgnore
		public TextChannel getOverridenChannel() {
			if(getChannel().contentEquals("default")) {
				return DiscordUtils.getChannel(Data.config().get().getDefaultChannel()).get();
			} else {
				return DiscordUtils.getChannel(getChannel()).get();
			}
		}

		public List<String> verifyDiscordEntities() {
			List<String> list = new LinkedList<>();
			try {
				TextChannel channel = CursedBot.instance().getJda().getTextChannelById(this.channel);
				list.add("Channel Valid: TRUE | " + channel.getName());
			} catch (NumberFormatException e) {
				list.add("Channel Valid: FALSE | <>");
			}
			try {
				Role role = CursedBot.instance().getJda().getRoleById(this.role);
				list.add("Role Valid: TRUE | " + role.getName());
			} catch (NumberFormatException e) {
				list.add("Role Valid: FALSE | <>");
			}
			return list;
		}
	}
}
