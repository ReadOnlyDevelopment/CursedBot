package net.romvoid95.curseforge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.therandomlabs.curseapi.CurseAPI;
import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.project.CurseProject;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.rom.utility.data.JsonDataManager;
import net.romvoid95.curseforge.data.Data;
import net.romvoid95.curseforge.data.Patterns;
import net.romvoid95.curseforge.data.cache.Cache;
import net.romvoid95.curseforge.data.cache.ProjectData;
import net.romvoid95.curseforge.data.override.ProjectOverride;

public class DataInterface {

	private static final Logger LOG = LoggerFactory.getLogger(DataInterface.class);
	private static File lockFile = new File("cache/cacheLock");
	private static JsonDataManager<Cache> cache = Data.cache();
	private static List<Integer> projectList;
	
    public static List<Integer> getProjectList() {
		return projectList;
	}

	public static void setProjectList(List<Integer> projectList) {
		DataInterface.projectList = projectList;
	}
	
	public static void updateFileId(ProjectData projectData) {
		List<ProjectData> list = cache.get().getCache();
		for(ProjectData d : list) {
			if(projectData.getProjectId().equals(d.getProjectId())) {
	    		try {
	    			d.setFileId(getCurseProject(projectData.getProjectId()).files().first().id());
				} catch (CurseException e) {
					e.printStackTrace();
				}
			}
		}
		cache.save();
		LOG.info("Sucessfully updated FileID for Project: [" + projectData.getProjectId() + "]");
	}
	
	public static void setCaches() {
    	List<ProjectData> data = cache.get().getCache();
    	List<Integer> alreadyGenerated = new ArrayList<Integer>();
    	for(ProjectData d : data) {
    		alreadyGenerated.add(d.getProjectId());
    	}
    	projectList.forEach(id -> {
    		if(alreadyGenerated.contains(id)) {
    			for(ProjectData d : data) {
    				if(id.equals(d.getProjectId())) {
    		    		try {
    		    			d.setFileId(getCurseProject(id).files().first().id());
    					} catch (CurseException e) {
    						e.printStackTrace();
    					}
    				}
    			}
    		} else {
				try {
					data.add(new ProjectData(id, getCurseProject(id).files().first().id()));
				} catch (CurseException e) {
					e.printStackTrace();
				}
			}
    	});
    	cache.get().setProjectData(data);
    	cache.save();
    	LOG.info("Sucessfully set current Cache data");
    }

    public static void initializeCache() {
    	if(!lockFile.exists()) {
    		try {
    			lockFile.createNewFile();
		    	Data.cache().get();
		    	Data.cache().get().setProjectData(new ArrayList<ProjectData>());
		    	Data.cache().save();
		    	LOG.info("Sucessfully initialized Cache data");
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    public static void generateOverrides() {
    	Data.overrides().get();
    	List<ProjectOverride> overrideList = Data.overrides().get().getOverrides();
    	List<Integer> alreadyGenerated =  new ArrayList<Integer>();
    	for(ProjectOverride override : overrideList) {
    		alreadyGenerated.add(override.getId());
    	}
    	projectList.forEach(po  -> {
    		if(!alreadyGenerated.contains(po)) {
        		Data.overrides().get().getOverrides().add(new ProjectOverride(po));
    		}
    	});
    	Data.overrides().save();
    	LOG.info("Sucessfully initialized Overrides data");
    }
    
    public static void runPreJDAChecks() {
    	List<ProjectOverride> overrideList = Data.overrides().get().getOverrides();
    	for(ProjectOverride override : overrideList) {
    		if(override.getChannel().equals("default")) {
    			Matcher idMatcher = Patterns.DISCORD_ID.matcher(Data.config().get().getDefaultChannel());
    			if(!idMatcher.find()) {
    				LOG.error("Channel ID for Project " + override.getId() + " was set to Config \"default\" but the Config Default Channel is not a correct Channel ID");
    				LOG.error("Please set a Channel ID in the list.json override or provide a valid Discord ID for the default channel");
    				System.exit(0);
    			}
    		} else {
    			Matcher idMatcher = Patterns.DISCORD_ID.matcher(override.getChannel());
    			if(!idMatcher.find()) {
    				LOG.error("Channel ID for Project Override " + override.getId() + " is not a correct Channel ID");
    				LOG.error("Please set a valid Channel ID in the list.json override");
    				System.exit(0);
    			}
			}
    		if(!override.getRole().equals("none")) {
        		if(override.getRole().equals("default")) {
        			Matcher idMatcher = Patterns.DISCORD_ID.matcher(Data.config().get().getDefulatRole());
        			if(!idMatcher.find()) {
        				LOG.error("Role ID for Project " + override.getId() + " was set to Config \"default\" but the Config Default Role is not a correct Channel ID");
        				LOG.error("Please set a Role ID in the list.json override or provide a valid Discord ID for the default Role");
        				System.exit(0);
        			} 
        		} else {
        			Matcher idMatcher = Patterns.DISCORD_ID.matcher(override.getRole());
        			if(!idMatcher.find()) {
        				LOG.error("Role ID for Project Override " + override.getId() + " is not a correct Role ID");
        				LOG.error("Please set a valid Role ID in the list.json override");
        				System.exit(0);
        			}
    			}
    		} else {
    			LOG.info("Skipping Role Validation Check as value was specified as \"none\"");
			}
    	}
    }
    
    public static void runPostJDAChecks() {
    	List<ProjectOverride> overrideList = Data.overrides().get().getOverrides();
    	JDA jda = CurseForgeBot.INSTANCE.getJda();
    	for(ProjectOverride override : overrideList) {
    		if(override.getChannel().equals("default")) {
    			TextChannel channel = jda.getTextChannelById(Data.config().get().getDefaultChannel());
    			if(channel == null) {
    				LOG.error("Channel ID for Project " + override.getId() + " was not found in any guilds");
    				LOG.error("Please set a valid channel in the config.json file and make sure the bot has the correct permissions for the channel");
    				CurseForgeBot.INSTANCE.getJda().shutdown();
    				System.exit(0);
    			}
    		} else {
    			TextChannel channel = jda.getTextChannelById(override.getChannel());
    			if(channel == null) {
    				LOG.error("Channel ID for Project Override " + override.getId() + " was not found in any guilds");
    				LOG.error("Please set a valid Channel ID in the list.json override and make sure the bot has the correct permissions for the channel");
    				CurseForgeBot.INSTANCE.getJda().shutdown();
    				System.exit(0);
    			}
			}
    		if(!override.getRole().equals("none")) {
        		if(override.getRole().equals("default")) {
        			Role role = jda.getRoleById(Data.config().get().getDefulatRole());
        			if(role == null) {
        				LOG.error("Role for Project " + override.getId() + " was not found in any guilds");
        				LOG.error("Please set a valid role in the config.json file");
        				CurseForgeBot.INSTANCE.getJda().shutdown();
        				System.exit(0);
        			} 
        		} else {
        			Role role = jda.getRoleById(override.getRole());
        			if(role == null) {
        				LOG.error("Role for Project Override " + override.getId() + " was not found in any guilds");
        				LOG.error("Please set a valid Role in the list.json override");
        				CurseForgeBot.INSTANCE.getJda().shutdown();
        				System.exit(0);
        			}
    			}
    		} else {
				LOG.info("Skipping Role Validation Check as value was specified as \"none\"");
			}
    	}
    }
    
	public static ProjectOverride findOverride(Integer id) {
		List<ProjectOverride> overrideList = Data.overrides().get().getOverrides();
		for(ProjectOverride override : overrideList) {
			if(override.getId().equals(id)) {
				return override;
			}
		}
		return null;
	}
    
    public static CurseProject getCurseProject(Integer projectId)  {
		try {
			Optional<CurseProject> optional = CurseAPI.project(projectId);
	        if (!optional.isPresent()) throw new CurseException("Project with ID: ["+projectId+"] not found");
	        return optional.get();
		} catch (CurseException e) {
			e.printStackTrace();
			return null;
		}
    }
}
