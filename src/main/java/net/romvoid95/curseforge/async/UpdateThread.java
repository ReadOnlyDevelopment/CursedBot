package net.romvoid95.curseforge.async;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.therandomlabs.curseapi.CurseAPI;
import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.file.CurseFile;
import com.therandomlabs.curseapi.project.CurseProject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.Component;
import net.rom.utility.async.Async;
import net.romvoid95.curseforge.CurseForgeBot;
import net.romvoid95.curseforge.DataInterface;
import net.romvoid95.curseforge.data.Data;
import net.romvoid95.curseforge.data.FileLink;
import net.romvoid95.curseforge.data.cache.Cache.ProjectData;
import net.romvoid95.curseforge.data.config.Config;
import net.romvoid95.curseforge.data.override.OverrideList.ProjectOverride;

public class UpdateThread {

	private ScheduledExecutorService service;
	private int delay;
	String THREAD_NAME;
	List<Component> components;
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

	private final DataInterface INTERFACE = CurseForgeBot._instance.getDataInterface();
	private final JDA JDA = CurseForgeBot._instance.getJda();

	public UpdateThread(ProjectData projectData, int id, int delay) {
		THREAD_NAME = "UpdateThread-" + id;
		this.delay = delay;
		this.projectId = projectData.getProjectId();
		LOG = (Logger) LoggerFactory.getLogger(THREAD_NAME);
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
		ProjectOverride override = INTERFACE.findOverride(projectId);
		if (override.getChannel().equals("default")) {
			this.channel = JDA.getTextChannelById(config.getDefaultChannel());
		} else {
			this.channel = JDA.getTextChannelById(override.getChannel());
		}
		if (!Data.config().get().getDefulatRole().equals("none") && !override.getRole().equals("none")) {
			if (override.getRole().equals("default")) {
				this.role = JDA.getRoleById(config.getDefulatRole());
			} else {
				this.role = JDA.getRoleById(override.getRole());
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
		Data.cache().get().getCache().forEach(pd -> {
			if (pd.getProjectId() == this.projectId) {
				this.cachedFileId = pd.getFileId();
			}
		});
		return cachedFileId < newFileId;
	}

	private synchronized void runCheck() {
		try {
			if (isNewFile()) {
				LOG.info("New File Found for" + proj.name() + " [New File] = " + proj.files().first().id());
				newFile = proj.files().first();
				ProjectData data = new ProjectData(projectId, newFileId);
				INTERFACE.updateFileId(data);
				EmbedBuilder builder;
				switch (this.filelink) {
				case DIRECT:
					builder = Embed.directLinkEmbed(this.proj, this.newFile, this.channel, this.description,
							this.format);
					components = Embed.getDirectDownloadButtons(this.proj, this.newFile);
					break;
				case CURSEFORGE:
					builder = Embed.curseLinkEmbed(this.proj, this.newFile, this.channel, this.description,
							this.format);
					components = Embed.getCursePageButtons(this.proj);
					break;
				default:
					builder = Embed.noLinkEmbed(this.proj, this.newFile, this.channel, this.description, this.format);
					components = Collections.emptyList();
				}

				if (this.role != null) {
					this.channel.sendMessage(role.getAsMention()).queue();
					this.channel.sendMessageEmbeds(builder.build()).setActionRow(components).queue();

				} else {
					this.channel.sendMessageEmbeds(builder.build()).setActionRow(components).queue();
				}

				proj.refreshFiles();
			}
			LOG.info("runCheck() for " + proj.name() + "(" + projectId + ") completed");
		} catch (CurseException e) {
			LOG.error("runCheck() for " + proj.name() + "(" + projectId + ") failed!", e);
		}
	}

	public void run() {

		this.service = Async.task(THREAD_NAME, () -> {
			runCheck();
		}, delay, 45, TimeUnit.SECONDS);
	}

	public void shutdown() {
		LOG.info("Stopping " + THREAD_NAME);
		this.service.shutdownNow();
	}
}
