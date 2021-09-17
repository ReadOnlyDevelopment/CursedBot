package net.romvoid95.curseforge.command;

import java.awt.Color;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.argument.RequiredArgument;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.doc.standard.RequiredPermissions;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.therandomlabs.curseapi.CurseAPI;
import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.project.CurseProject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.romvoid95.curseforge.DataInterface;
import net.romvoid95.curseforge.command.base.BaseCommand;
import net.romvoid95.curseforge.command.base.CommandCategory;
import net.romvoid95.curseforge.command.base.annotation.CurseCommand;

@CommandInfo(
	name = {"add", "addproject"},
	description = "Adds a new project to check and notify on each update"
)
@RequiredPermissions({Permission.MESSAGE_EMBED_LINKS})
@Author("ROMVoid95")
@CurseCommand
public class CommandAdd extends BaseCommand {

	public CommandAdd() {
		super("add", CommandCategory.UPDATES);
		this.aliases("addproject");
		this.guildOnly();
		this.help("Adds a new project to check and notify on each update");
		this.addAgument(RequiredArgument.of("project_id", "The ID of the project to add"));
	}

	@Override
	public void execute(CommandEvent event) {
		super.execute(event);
		if (noArgs()) {
			event.replyError("A valid Project ID must be provided!");
			return;
		} else {
			try {
				int projectId = Integer.parseInt(getArgValue(0));
				try {
					Optional<CurseProject> project = CurseAPI.project(projectId);
					if (project.isPresent()) {
						event.getMessage().delete().queue();
						
						CurseProject verifiedProject = project.get();
						EmbedBuilder embed = new EmbedBuilder().setTitle("Add " + verifiedProject.name() + " (" + verifiedProject.id() + ")");
						embed = DataInterface.instance().addProject(embed, project.get());
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
