package net.romvoid95.curseforge.async;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.therandomlabs.curseapi.CurseAPI;
import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.file.CurseFile;
import com.therandomlabs.curseapi.project.CurseProject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.rom.utility.async.Async;
import net.romvoid95.curseforge.CurseForgeBot;
import net.romvoid95.curseforge.DataInterface;
import net.romvoid95.curseforge.data.Data;
import net.romvoid95.curseforge.data.cache.ProjectData;
import net.romvoid95.curseforge.data.config.Config;
import net.romvoid95.curseforge.data.config.FileLink;
import net.romvoid95.curseforge.data.override.ProjectOverride;

public class UpdateThread {
	
	private final Logger LOG;
	
	private CurseProject proj;
	private CurseFile newFile;

	private Integer projectId;

	private Integer newFileId;
	private Integer cachedFileId;

	private TextChannel channel;
	private Role role;
	private FileLink filelink;
	private String description;
	private String format;

	public UpdateThread(ProjectData projectData) {
		
		this.projectId = projectData.getProjectId();
		this.cachedFileId = projectData.getFileId();
		LOG = LoggerFactory.getLogger( "UpdateThread-" + getProjectId()); 
		getMessageData(projectId);

		Optional<CurseProject> project;
		try {
			project = CurseAPI.project(projectId);
			if (!project.isPresent())
				throw new CurseException("Project with ID: [" + projectId + "] not found");
			proj = project.get();
		} catch (CurseException e) {
			e.printStackTrace();
		}
	}

	private void getMessageData(Integer projectId) {
		Config config = Data.config().get();
		ProjectOverride override = DataInterface.findOverride(projectId);
		if (override.getChannel().equals("default")) {
			this.channel = CurseForgeBot.INSTANCE.getJda().getTextChannelById(config.getDefaultChannel());
		} else {
			this.channel = CurseForgeBot.INSTANCE.getJda().getTextChannelById(override.getChannel());
		}
		if(!Data.config().get().getDefulatRole().equals("none") && !override.getRole().equals("none")) {
			if (override.getRole().equals("default")) {
				this.role = CurseForgeBot.INSTANCE.getJda().getRoleById(config.getDefulatRole());
			} else {
				this.role = CurseForgeBot.INSTANCE.getJda().getRoleById(override.getRole());
			}
		}
		if (override.getDescription().equals("default")) {
			this.description = config.getDefaultDescription();
		} else {
			this.description = override.getDescription();
		}
		if (override.getFileLink().equals(FileLink.DEFAULT)) {
			this.filelink = config.getUpdateFileLink();
		} else {
			this.filelink = override.getFileLink();
		}
		if (override.getDiscordFormat().equals("default")) {
			this.format = config.getDiscordFormat();
		} else {
			this.format = override.getDiscordFormat();
		}
	}

	private boolean isNewFile() throws CurseException {
		newFileId = proj.files().first().id();
		return cachedFileId < newFileId;
	}

	private synchronized void runCheck() {
		LOG.info("UpdateThread runCheck() Start");
		try {
			if (isNewFile()) {
				LOG.info("New File Found for" + proj.name() + " [New File] = " + proj.files().first().id());
				newFile = proj.files().first();
				ProjectData data = new ProjectData(getProjectId(), newFileId);
				DataInterface.updateFileId(data);
				EmbedBuilder builder;
				switch (this.filelink) {
				case DIRECT:
					builder = Embed.directLinkEmbed(this.proj, this.newFile, this.channel, this.description,
							this.format);
					break;
				case CURSEFORGE:
					builder = Embed.curseLinkEmbed(this.proj, this.newFile, this.channel, this.description,
							this.format);
					break;
				case DEFAULT:
					builder = Embed.noLinkEmbed(this.proj, this.newFile, this.channel, this.description, this.format);
					break;
				default:
					builder = Embed.noLinkEmbed(this.proj, this.newFile, this.channel, this.description, this.format);
				}
				if (this.role != null) {
					this.channel.sendMessage(role.getAsMention()).queue();
					this.channel.sendMessage(builder.build()).queue();
				} else {
					this.channel.sendMessage(builder.build()).queue();
				}
				LOG.info("UpdateThread runCheck() Finished");
				proj.refreshFiles();
			} else {
				LOG.info("UpdateThread runCheck() Finished");
			}
		} catch (CurseException e) {
			e.printStackTrace();
		}
	}

	public void run(int delay) {
		
		Async.task("UpdateThread-" + getProjectId(), () -> {
			runCheck();
		}, Long.valueOf(String.valueOf(delay)), Long.valueOf(String.valueOf(30)), TimeUnit.SECONDS);
	}

	public Integer getProjectId() {
		return projectId;
	}
}
