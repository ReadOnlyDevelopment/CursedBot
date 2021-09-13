package net.romvoid95.curseforge.commands;

import java.awt.Color;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.therandomlabs.curseapi.CurseAPI;
import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.project.CurseProject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.romvoid95.curseforge.CurseForgeBot;

public class CommandAddProject extends Command {

	public CommandAddProject() {
		this.name = "add";
		this.help = "adds a new project for update checks";
		this.arguments = "[project id]";
		this.ownerCommand = true;
		this.guildOnly = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		if (event.getArgs().isEmpty()) {
			event.replyError("A valid Project ID must be provided!");
			return;
		} else {
			try {
				int projectId = Integer.parseInt(event.getArgs());
				try {
					Optional<CurseProject> project = CurseAPI.project(projectId);
					if (project.isPresent()) {
						event.getMessage().delete().queue();
						
						CurseProject verifiedProject = project.get();
						EmbedBuilder embed = new EmbedBuilder().setTitle("Add " + verifiedProject.name() + " (" + verifiedProject.id() + ")");
						embed = CurseForgeBot._instance.getDataInterface().addProject(embed, project.get());
						event.reply(embed.build(), sent -> sent.delete().queueAfter(30, TimeUnit.SECONDS));
						return;
					}
				} catch (CurseException e) {
					event.reply(new EmbedBuilder().setDescription("There was an error retriving CurseProject from the API").setColor(Color.RED).build()
							, sent -> sent.delete().queueAfter(30, TimeUnit.SECONDS));
					return;
				}
			} catch (NumberFormatException e) {
				event.reply(new EmbedBuilder().setDescription(event.getClient().getError() + " `" + event.getArgs() + "` is not a valid integer!").setColor(Color.RED).build(),
						sent -> sent.delete().queueAfter(30, TimeUnit.SECONDS));
				return;
			}
		}
	}
}
