package net.romvoid95.curseforge.data.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.ListUtils;

import lombok.Getter;
import net.romvoid95.curseforge.data.FileLink;

@Getter
public class Config {

	private boolean developmentMode = true;
	private String devChannel = "908273329549479936";
	private String token = "INSERT-TOKEN";
	private String owner = "owner";
	private String[] prefixes = new String[] {">>"}; 
	private boolean debug = false;
	private String defaultChannel = "SET ID";
	private String defulatRole = "none";
	private FileLink updateFileLink = FileLink.DEFAULT;
	private String defaultDescription = "New File Detected For CurseForge Project";
	private String discordFormat = "css";
	private List<Integer> projects = new ArrayList<>();

	public Config() {}
	
	/**
	 * @param token
	 * @param defaultChannel
	 * @param defulatRole
	 * @param updateFileLink
	 * @param defaultDescription
	 * @param discordFormat
	 * @param projects
	 */
	public Config(Boolean developmentMode, String token, String owner, String[] prefixes, Boolean debug, String defaultChannel, String defulatRole, FileLink updateFileLink, String defaultDescription, String discordFormat, List<Integer> projects) {
		super();
		this.developmentMode = developmentMode;
		this.token = token;
		this.owner = owner;
		this.prefixes = prefixes;
		this.debug = debug;
		this.defaultChannel = defaultChannel;
		this.defulatRole = defulatRole;
		this.updateFileLink = updateFileLink;
		this.defaultDescription = defaultDescription;
		this.discordFormat = discordFormat;
		this.projects = projects;
	}
	
	
	
	public boolean addProject(Integer project) {
		return this.projects.add(project);
	}
	
	public boolean removeProject(Integer project) {
		return this.projects.remove(project);
	}

	public boolean equals(Config obj) {
		Config other = obj;
		return Objects.equals(debug, other.debug) && Objects.equals(defaultChannel, other.defaultChannel)
				&& Objects.equals(defaultDescription, other.defaultDescription)
				&& Objects.equals(defulatRole, other.defulatRole) && Objects.equals(discordFormat, other.discordFormat)
				&& ListUtils.isEqualList(projects, other.projects) && Objects.equals(token, other.token)
				&& Arrays.equals(prefixes, other.prefixes) && updateFileLink == other.updateFileLink;
	}

	@Override
	public String toString() {
		return String.format("Config [token=%s, owner=%s, prefixes=%s, debug=%s, defaultChannel=%s, defulatRole=%s, updateFileLink=%s, defaultDescription=%s, discordFormat=%s, projects=%s]", token, owner, Arrays.toString(prefixes), debug, defaultChannel, defulatRole,
				updateFileLink, defaultDescription, discordFormat, projects);
	}
	
	
}
