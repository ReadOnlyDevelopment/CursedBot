package net.romvoid95.curseforge;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.therandomlabs.curseapi.CurseAPI;
import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.file.CurseFile;
import com.therandomlabs.curseapi.project.CurseProject;

import io.github.romvoid95.system.OS;
import io.github.romvoid95.system.SystemPlatform;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.EmbedBuilder;
import net.romvoid95.curseforge.async.CurrentThreads;
import net.romvoid95.curseforge.data.Data;
import net.romvoid95.curseforge.data.cache.Cache.ProjectData;
import net.romvoid95.curseforge.data.config.Config;
import net.romvoid95.curseforge.data.override.OverrideList.ProjectOverride;
import net.romvoid95.curseforge.io.FileIO;
import net.romvoid95.curseforge.io.JsonDataManager;

@Log4j2
public class DataInterface {
	private static DataInterface _instance;
	public static Path data;
	private List<Integer> projectList;

	public static DataInterface instance() {
		if(_instance == null) {
			_instance = new DataInterface();
		}
		return _instance;
	}
	
	public void setup() {
		data = Paths.get(SystemPlatform.CURRENT_OS == OS.WINDOWS ? "data" : ".data");
		try {
			if (!data.toFile().exists())
				Files.createDirectory(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

		initializeCache();
		this.projectList = Data.config().get().getProjects();
		generateOverrides();
		setCaches();
		
		log.info("Total Projects: " + projectList.size());
		projectList.forEach(p ->  {
			CurseProject project = getCurseProject(p);
			log.info("Preparing UpdateThread for " + project.name());
		});
	}

	public void updateFileId(ProjectData projectData) {
		List<ProjectData> list = Data.cache().get().getCache();
		for (ProjectData d : list) {
			if (projectData.getProjectId().equals(d.getProjectId())) {
				try {
					CurseProject project = getCurseProject(d.getProjectId());
					CurseFile file = getCurseProject(projectData.getProjectId()).files().first();
					log.info("[" + project.name() + "]");
					log.info("CacheFile: " + d.getFileId());
					log.info("FirstFile: " + file.id());
					
					d.setFileId(getCurseProject(projectData.getProjectId()).files().first().id());
				} catch (CurseException e) {
					e.printStackTrace();
				}
			}
		}
		Data.cache().save();
		log.info("Sucessfully updated FileID for Project: [" + projectData.getProjectId() + "]");
	}
	
	public EmbedBuilder addProject(EmbedBuilder embed, CurseProject project) throws CurseException {
		JsonDataManager<Config> config = Data.config();
		if(config.get().addProject(project.id())) {
			config.save();
			embed.addField("Add ProjectID", "Sucessfull", false);
			log.info("Sucessfully added project " + project.id());
		} else {
			embed.addField("Add ProjectID to Config", "Failed", false);
			embed.setColor(Color.RED);
			log.error("Failed to add project " + project.id());
		}
		
		ProjectOverride newOverride = new ProjectOverride(project.id());
		if(Data.overrides().get().getOverrides().add(newOverride)) {
			Data.overrides().save();
			embed.addField("Add New ProjectOverride Data", "Sucessfull", false);
			log.info("Sucessfully added Override data for new project " + project.id());
		} else {
			embed.addField("Add New ProjectOverride Data", "Failed", false);
			embed.setColor(Color.RED);
			log.error("Failed to add override data for new project " + project.id());
		}
		
		ProjectData projectData = new ProjectData(project.id(), project.files().first().id());
		if(Data.cache().get().getCache().add(projectData)) {
			Data.cache().save();
			embed.addField("Add New ProjectData To Cache", "Sucessfull", false);
			log.info("Sucessfully added cache data for new project " + project.id());
		} else {
			embed.addField("Add New ProjectData To Cache", "Failed", false);
			embed.setColor(Color.RED);
			log.error("Failed to add cache data for new project " + project.id());
		}
		embed.setColor(Color.GREEN);
		setCaches();
		CursedBot.instance().getCurrentThreads().resetThreads();
		CursedBot.instance().setCurrentThreads(new CurrentThreads(Data.cache().get()));
		CursedBot.instance().setRuns();
		
		return embed;
	}
	
	public EmbedBuilder removeProject(EmbedBuilder embed, CurseProject project) throws CurseException {
		JsonDataManager<Config> config = Data.config();
		if(config.get().removeProject(project.id())) {
			config.save();
			embed.addField("Remove ProjectID", "Sucessfull", false);
			log.info("Sucessfully removed project " + project.id());
		} else {
			embed.addField("Remove ProjectID from Config", "Failed", false);
			embed.setColor(Color.RED);
			log.error("Failed to Remove project " + project.id());
		}
		
		ProjectOverride override = findOverride(project.id());
		if(override != null) {
			if(Data.overrides().get().getOverrides().remove(override)) {
				Data.overrides().save();
				embed.addField("Remove ProjectOverride Data", "Sucessfull", false);
				log.info("Sucessfully Removed Override for project " + project.id());
			} else {
				embed.addField("Remove ProjectOverride Data", "Failed", false);
				embed.setColor(Color.RED);
				log.error("Failed to remove override data for project " + project.id());
			}
		} else {
			embed.addField("Failed to retrieve Override Data", "Failed", false);
			embed.setColor(Color.RED);
			log.error("Failed to retrieve override data for project " + project.id());
		}

		
		ProjectData projectData = findData(project.id());
		if(projectData != null) {
			if(Data.cache().get().getCache().remove(projectData)) {
				Data.cache().save();
				embed.addField("Remove ProjectData From Cache", "Sucessfull", false);
				log.info("Sucessfully removed ProjectData from cache for project " + project.id());
			} else {
				embed.addField("Remove ProjectData From Cache", "Failed", false);
				embed.setColor(Color.RED);
				log.error("Failed to removed ProjectData from cache for project " + project.id());
			}
		} else {
			embed.addField("Failed to retrieve ProjectData", "Failed", false);
			embed.setColor(Color.RED);
			log.error("Failed to retrieve ProjectData for project " + project.id());
		}

		embed.setColor(Color.GREEN);
		setCaches();
		CursedBot.instance().getCurrentThreads().resetThreads();
		CursedBot.instance().setCurrentThreads(new CurrentThreads(Data.cache().get()));
		CursedBot.instance().setRuns();
		
		return embed;
	}

	private void initializeCache() {
		File lockFile = new File(data.toString() + "/lock.file");
		if (!lockFile.exists()) {
			try {
				lockFile.createNewFile();
				if(SystemPlatform.CURRENT_OS == OS.WINDOWS) {
					FileIO.setHidden(lockFile.toPath());
				}
				Data.cache().get();
				Data.cache().get().setProjectData(new ArrayList<ProjectData>());
				Data.cache().save();
				log.info("Sucessfully initialized Cache data");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setCaches() {
    	List<ProjectData> data = Data.cache().get().getCache();
    	List<Integer> alreadyGenerated = new ArrayList<Integer>();
    	for(ProjectData d : data) {
    		alreadyGenerated.add(d.getProjectId());
    	}
    	projectList.forEach(id -> {
    		if(!alreadyGenerated.contains(id)) {
				try {
					data.add(new ProjectData(id, getCurseProject(id).files().first().id()));
				} catch (CurseException e) {
					e.printStackTrace();
				}
    		}
    	});
    	Data.cache().save();
    	log.info("Sucessfully set current Cache data");
    }

	private void generateOverrides() {
		Data.overrides().get();
		List<ProjectOverride> overrideList = Data.overrides().get().getOverrides();
		List<Integer> alreadyGenerated = new ArrayList<Integer>();
		for (ProjectOverride override : overrideList) {
			alreadyGenerated.add(override.getId());
		}
		projectList.forEach(po -> {
			if (!alreadyGenerated.contains(po)) {
				Data.overrides().get().getOverrides().add(new ProjectOverride(po));
			}
		});
		Data.overrides().save();
		log.info("Sucessfully initialized Overrides data");
	}


	public ProjectOverride findOverride(Integer id) {
		List<ProjectOverride> overrideList = Data.overrides().get().getOverrides();
		for (ProjectOverride override : overrideList) {
			if (override.getId().equals(id)) {
				return override;
			}
		}
		return null;
	}
	
	public ProjectData findData(Integer id) {
		List<ProjectData> dataList = Data.cache().get().getCache();
		for (ProjectData data : dataList) {
			if (data.getProjectId().equals(id)) {
				return data;
			}
		}
		return null;
	}

	public CurseProject getCurseProject(Integer projectId) {
		try {
			Optional<CurseProject> optional = CurseAPI.project(projectId);
			if (!optional.isPresent())
				throw new CurseException("Project with ID: [" + projectId + "] not found");
			return optional.get();
		} catch (CurseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
