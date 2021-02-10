package net.romvoid95.curseforge.data;

import net.romvoid95.curseforge.data.cache.Cache;
import net.romvoid95.curseforge.data.config.Config;
import net.romvoid95.curseforge.data.override.OverrideList;
import net.romvoid95.curseforge.manager.JsonDataManager;

public class Data {
	
	private static JsonDataManager<Cache> cache;
	private static JsonDataManager<Config> config;
	
	private static JsonDataManager<OverrideList> overrides;
    
    public static JsonDataManager<OverrideList> overrides() {
        if (overrides == null) {
        	overrides = new JsonDataManager<>(OverrideList.class, "overrides/list.json", OverrideList::new);
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
        	cache = new JsonDataManager<>(Cache.class, "cache/cache.json", Cache::new);
        }
        return cache;
    }
}
