package net.romvoid95.curseforge.command;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.argument.RequiredArgument;
import com.therandomlabs.curseapi.CurseAPI;
import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.project.CurseProject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.romvoid95.curseforge.DataInterface;
import net.romvoid95.curseforge.command.base.BaseCommand;
import net.romvoid95.curseforge.command.base.CommandCategory;
import net.romvoid95.curseforge.command.base.ResultLevel;
import net.romvoid95.curseforge.command.base.annotation.CurseCommand;
import net.romvoid95.curseforge.util.Utils;

@CurseCommand
public class CommandProject extends BaseCommand {

	private final RequiredArgument ADD_REMOVE = RequiredArgument.of("add | remove | list", "Add or Remove a project for Update Checks");

	public CommandProject(){
		super("project", CommandCategory.UPDATES);
		this.addAguments(ADD_REMOVE, RequiredArgument.of("project_id", "The ID of the project to add"));
		this.allowDms();
		this.help("Adds a new project to check and notify on each update");
		this.requiredRoles("Cursed");
	}

	@Override
	public void onExecute(CommandEvent event) {
		String action;
		if (noArgs()) {
			event.replyError("A valid Project ID must be provided!");
			return;
		} else {
			if(getArgCount() < 2) {
				if(!getArg(0).validate(Utils.check("add")) && !getArg(0).validate(Utils.check("remove"))) {
					temporaryReply(ResultLevel.ERROR, "You must specify either 'add', 'remove', or 'list'", 30, TimeUnit.SECONDS);
					return;
				}
			}
			if(getArgCount() == 2) {
				if(!getArg(0).validate(Utils.check("add")) && !getArg(0).validate(Utils.check("remove"))) {
					temporaryReply(ResultLevel.ERROR, "You must specify either 'add', 'remove', or 'list'", 30, TimeUnit.SECONDS);
					return;
				}
				try {
					action = getArgValue(0);
					int projectId = Integer.parseInt(getArgValue(1));
					try {
						CurseProject verifiedProject = CurseAPI.project(projectId).get();
						event.getMessage().delete().queue();
						if(action.equalsIgnoreCase("add")) {
							EmbedBuilder embed = new EmbedBuilder().setTitle("Add " + verifiedProject.name() + " (" + verifiedProject.id() + ")");
							embed = DataInterface.instance().addProject(embed, verifiedProject);
							event.reply(embed.build(), sent -> sent.delete().queueAfter(30, TimeUnit.SECONDS));
							return;
						}
						if(action.equalsIgnoreCase("remove")) {
							EmbedBuilder embed = new EmbedBuilder().setTitle("Add " + verifiedProject.name() + " (" + verifiedProject.id() + ")");
							embed = DataInterface.instance().removeProject(embed, verifiedProject);
							event.reply(embed.build(), sent -> sent.delete().queueAfter(30, TimeUnit.SECONDS));
							return;
						}
						
					} catch (CurseException e) {
						event.reply(new EmbedBuilder().setDescription("There was an error retriving CurseProject from the API").setColor(Color.RED).build(), sent -> sent.delete().queueAfter(30, TimeUnit.SECONDS));
						return;
					}
				} catch (NumberFormatException e) {
					event.reply(new EmbedBuilder().setDescription(event.getClient().getError() + " `" + event.getArgs() + "` is not a valid integer!").setColor(Color.RED).build(),
							sent -> sent.delete().queueAfter(30, TimeUnit.SECONDS));
					return;
				}
			}
		}
		event.getMessage().delete().queue();
	}
}
