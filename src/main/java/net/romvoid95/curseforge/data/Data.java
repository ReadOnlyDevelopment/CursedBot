package net.romvoid95.curseforge.data;

import java.util.EnumSet;

import net.dv8tion.jda.api.Permission;
import net.romvoid95.curseforge.DataInterface;
import net.romvoid95.curseforge.data.cache.Cache;
import net.romvoid95.curseforge.data.config.Config;
import net.romvoid95.curseforge.data.override.OverrideList;
import net.romvoid95.curseforge.io.JsonDataManager;

public class Data {
	
	private static JsonDataManager<Cache> cache;
	private static JsonDataManager<Config> config;
	
	private static JsonDataManager<OverrideList> overrides;
    
    public static JsonDataManager<OverrideList> overrides() {
        if (overrides == null) {
        	overrides = new JsonDataManager<>(OverrideList.class, DataInterface.data.toString() + "/overrides.json", OverrideList::new);
        }
    	return overrides;
    }
    
    public static JsonDataManager<Config> config() {
        if (config == null) {
        	config = new JsonDataManager<>(Config.class, "config.json", Config::new);
        }
        return config;
    }
    
    public static JsonDataManager<Cache> cache() {
        if (cache == null) {
        	cache = new JsonDataManager<>(Cache.class, DataInterface.data.toString() + "/cache.json", Cache::new);
        }
        return cache;
    }
    
    public static EnumSet<Permission> getBotPermissions() {
    	return 	EnumSet.of(
    			Permission.VIEW_CHANNEL,
    			Permission.MESSAGE_WRITE,
    			Permission.MESSAGE_MANAGE,
    			Permission.MESSAGE_EMBED_LINKS,
    			Permission.MESSAGE_ATTACH_FILES,
    			Permission.MESSAGE_READ,
    			Permission.MESSAGE_EXT_EMOJI,
    			Permission.MESSAGE_ADD_REACTION,
    			Permission.CREATE_INSTANT_INVITE,
    			Permission.MESSAGE_HISTORY,
    			Permission.MANAGE_ROLES);
    }
}
