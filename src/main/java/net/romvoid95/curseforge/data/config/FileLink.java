package net.romvoid95.curseforge.data.config;


public enum FileLink {
	
	DEFAULT("default"),
	DIRECT("direct"),
	CURSEFORGE("curseforge");
	
	private String name;
	
	FileLink(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
