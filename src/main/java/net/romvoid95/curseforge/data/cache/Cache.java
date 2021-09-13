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
	
	public static class ProjectData {
		
		private Integer projectId;
		private Integer fileId;
		
		public ProjectData() {}
		
		public ProjectData(Integer projectId, Integer fileId) {
			super();
			this.projectId = projectId;
			this.fileId = fileId;
		}
		
		public Integer getProjectId() {
			return projectId;
		}

		public void setProjectId(Integer projectId) {
			this.projectId = projectId;
		}

		public ProjectData withProjectId(Integer projectId) {
			this.projectId = projectId;
			return this;
		}

		public Integer getFileId() {
			return fileId;
		}

		public void setFileId(Integer fileId) {
			this.fileId = fileId;
		}

		public ProjectData withFileId(Integer fileId) {
			this.fileId = fileId;
			return this;
		}
	}
}
