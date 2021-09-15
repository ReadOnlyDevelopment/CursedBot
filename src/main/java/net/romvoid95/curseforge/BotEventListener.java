package net.romvoid95.curseforge;

import java.awt.Color;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.restaction.RoleAction;

public class BotEventListener implements EventListener {

	@Override
	public void onEvent(GenericEvent event) {
		if(event instanceof GuildJoinEvent) {
			
			Guild guild = ((GuildJoinEvent) event).getGuild();
			RoleAction createRole = guild.createRole();
			createRole.setName("Cursed");
			createRole.setColor(new Color(241, 100, 54));
			createRole.queue();
		}
	}

}
