package net.romvoid95.curseforge.data.cache;

public class ProjectData {
	
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
