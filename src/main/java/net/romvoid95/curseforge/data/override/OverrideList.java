package net.romvoid95.curseforge.data.override;

import java.util.ArrayList;
import java.util.List;

public class OverrideList {
	
	private List<ProjectOverride> overrides = new ArrayList<ProjectOverride>();

	public OverrideList() {
	}

	public OverrideList(List<ProjectOverride> overrides) {
		super();
		this.overrides = overrides;
	}

	public List<ProjectOverride> getOverrides() {
		return overrides;
	}

	public void setOverrides(List<ProjectOverride> overrides) {
		this.overrides = overrides;
	}

	public OverrideList withOverrides(List<ProjectOverride> overrides) {
		this.overrides = overrides;
		return this;
	}
}
