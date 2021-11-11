package net.romvoid95.curseforge;

import java.awt.Color;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.restaction.RoleAction;

@Log4j2
public class BotEventListener implements EventListener {

	@Override
	public void onEvent(GenericEvent event) {
		if(event instanceof GuildJoinEvent) {
			
			Guild guild = ((GuildJoinEvent) event).getGuild();
			this.createRole(guild);
		}
		if(event instanceof ReadyEvent) {
			JDA jda = ((ReadyEvent) event).getJDA();
			jda.getGuilds().forEach(guild -> {
				List<Role> cursed = guild.getRolesByName("Cursed", false);
				if(cursed.isEmpty()) {
					this.createRole(guild);
				}
			});
		}
	}

	
	private void createRole(Guild guild) {
		RoleAction createRole = guild.createRole();
		createRole.setName("Cursed");
		createRole.setColor(new Color(241, 100, 54));
		createRole.queue(
				s -> log.info("Created \"Cursed\" role in Guild: " + guild.getName()), 
				f -> log.error("Error creating \"Cursed\" role in Guild: " + guild.getName(), f)
		);
	}
}
