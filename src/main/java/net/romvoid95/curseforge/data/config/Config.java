package net.romvoid95.curseforge.data.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.ListUtils;

import net.romvoid95.curseforge.data.FileLink;

public class Config {

	private String token = "INSERT-TOKEN";
	private String owner = "owner";
	private String[] prefixes = new String[] {">>"}; 
	private Boolean debug = false;
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
	public Config(String token, String owner, String[] prefixes, Boolean debug, String defaultChannel, String defulatRole, FileLink updateFileLink, String defaultDescription, String discordFormat, List<Integer> projects) {
		super();
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

	public String getToken() {
		return token;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public String[] getPrefixes() {
		return prefixes;
	}
	
	public Boolean getDebug() {
		return debug;
	}

	public String getDefaultChannel() {
		return defaultChannel;
	}

	public String getDefulatRole() {
		return defulatRole;
	}

	public FileLink getUpdateFileLink() {
		return updateFileLink;
	}

	public String getDefaultDescription() {
		return defaultDescription;
	}

	public String getDiscordFormat() {
		return discordFormat;
	}

	public List<Integer> getProjects() {
		return projects;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public void setPrefixes(String[] prefixes) {
		this.prefixes = prefixes;
	}
	
	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public void setDefaultChannel(String defaultChannel) {
		this.defaultChannel = defaultChannel;
	}

	public void setDefulatRole(String defulatRole) {
		this.defulatRole = defulatRole;
	}

	public void setUpdateFileLink(FileLink updateFileLink) {
		this.updateFileLink = updateFileLink;
	}

	public void setDefaultDescription(String defaultDescription) {
		this.defaultDescription = defaultDescription;
	}

	public void setDiscordFormat(String discordFormat) {
		this.discordFormat = discordFormat;
	}

	public void setProjects(List<Integer> projects) {
		this.projects = projects;
	}
	
	public boolean addProject(Integer project) {
		return this.projects.add(project);
	}
	
	public boolean removeProject(Integer project) {
		return this.projects.remove(project);
	}
	
	public Config withToken(String token) {
		this.token = token;
		return this;
	}
	
	public Config withOwner(String owner) {
		this.owner = owner;
		return this;
	}
	
	public Config withPrefixes(String[] prefixes) {
		this.prefixes = prefixes;
		return this;
	}
	
	public Config withDebug(Boolean debug) {
		this.debug = debug;
		return this;
	}

	public Config withDefaultChannel(String defaultChannel) {
		this.defaultChannel = defaultChannel;
		return this;
	}

	public Config withDefulatRole(String defulatRole) {
		this.defulatRole = defulatRole;
		return this;
	}

	public Config withUpdateFileLink(FileLink updateFileLink) {
		this.updateFileLink = updateFileLink;
		return this;
	}

	public Config withDefaultDescription(String defaultDescription) {
		this.defaultDescription = defaultDescription;
		return this;
	}

	public Config withDiscordFormat(String discordFormat) {
		this.discordFormat = discordFormat;
		return this;
	}

	public Config withProjects(List<Integer> projects) {
		this.projects = projects;
		return this;
	}

	public boolean equals(Config obj) {
		Config other = obj;
		return Objects.equals(debug, other.debug) && Objects.equals(defaultChannel, other.defaultChannel)
				&& Objects.equals(defaultDescription, other.defaultDescription)
				&& Objects.equals(defulatRole, other.defulatRole) && Objects.equals(discordFormat, other.discordFormat)
				&& ListUtils.isEqualList(projects, other.projects) && Objects.equals(token, other.token)
				&& Arrays.equals(prefixes, other.prefixes) && updateFileLink == other.updateFileLink;
	}
}
