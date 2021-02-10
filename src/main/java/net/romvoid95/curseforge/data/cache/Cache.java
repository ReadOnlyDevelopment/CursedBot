package net.romvoid95.curseforge.data.cache;

import java.util.ArrayList;
import java.util.List;

public class Cache {
	private List<ProjectData> cache = new ArrayList<ProjectData>();

	public Cache() {
	}

	public Cache(List<ProjectData> cache) {
		super();
		this.cache = cache;
	}

	public List<ProjectData> getCache() {
		return cache;
	}

	public void setProjectData(List<ProjectData> cache) {
		this.cache = cache;
	}

	public Cache withProjectData(List<ProjectData> cache) {
		this.cache = cache;
		return this;
	}
}
