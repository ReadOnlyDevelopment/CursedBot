package net.romvoid95.curseforge.async;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.romvoid95.curseforge.data.cache.Cache;
import net.romvoid95.curseforge.data.cache.Cache.ProjectData;

public class CurrentThreads {

	private Logger LOG = LoggerFactory.getLogger(CurrentThreads.class);
	private Map<ProjectData, UpdateThread> currentThreads;
	private int count = 0;
	private int delay = 10;
	
	public CurrentThreads(Cache cache) {
		currentThreads = new LinkedHashMap<ProjectData, UpdateThread>();

		cache.getCache().forEach(projData -> {
			this.buildUpdateThread(projData);
		});
	}
	
	private void buildUpdateThread(ProjectData data) {
		LOG.info(data.toString());
		UpdateThread thread = new UpdateThread(data, count++, delay++);
		currentThreads.put(data, thread);
	}
	
	public void resetThreads() {
		LOG.info("CurrentThreads: " + currentThreads.size());
		currentThreads.entrySet().forEach(set -> {
			set.getValue().shutdown();
		});
		try {
			Thread.sleep(TimeUnit.SECONDS.toMillis(5));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.currentThreads = null;
		LOG.info("CurrentThreads is null?: " + currentThreads != null ? "TRUE" : "FALSE");
	}
	
	public CurrentThreads runThreads() {
		currentThreads.values().forEach(UpdateThread::run);
		return this;
	}
	
	public Map<ProjectData, UpdateThread> getThreadsMap() {
		return currentThreads;
	}
}
