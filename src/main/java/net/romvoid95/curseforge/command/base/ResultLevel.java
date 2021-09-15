package net.romvoid95.curseforge.command.base;

import java.awt.Color;

import net.romvoid95.curseforge.util.DiscordUtils;

public enum ResultLevel {

	SUCCESS(Color.GREEN),
	WARNING(Color.YELLOW),
	ERROR(Color.RED);

	private Color color;
	
	ResultLevel(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getColorInt() {
		String hex = DiscordUtils.getHexValue(getColor());
		return DiscordUtils.parseColor(hex);
	}
}
