package net.romvoid95.curseforge.data.override;

import net.romvoid95.curseforge.data.config.FileLink;

public class ProjectOverride {

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

	@Override
	public String toString() {
		return String.format(
				"ProjectOverride [id=%s, channel=%s, role=%s, fileLink=%s, description=%s, discordFormat=%s]", id,
				channel, role, fileLink, description, discordFormat);
	}
}
