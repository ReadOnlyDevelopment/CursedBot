package net.romvoid95.curseforge.data;

import java.util.regex.Pattern;

public final class Patterns {

	public final static Pattern DISCORD_ID = Pattern.compile("\\d{17,20}"); // ID
	public final static Pattern FULL_USER_REF = Pattern.compile("(\\S.{0,30}\\S)\\s*#(\\d{4})"); // $1 -> username,
																									// $2
																									// ->
																									// discriminator
	public final static Pattern USER_MENTION = Pattern.compile("<@!?(\\d{17,20})>"); // $1 -> ID
	public final static Pattern CHANNEL_MENTION = Pattern.compile("<#(\\d{17,20})>"); // $1 -> ID
	public final static Pattern ROLE_MENTION = Pattern.compile("<@&(\\d{17,20})>"); // $1 -> ID
	public final static Pattern EMOTE_MENTION = Pattern.compile("<:(.{2,32}):(\\d{17,20})>");
}
