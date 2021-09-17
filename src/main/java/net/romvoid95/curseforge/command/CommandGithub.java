package net.romvoid95.curseforge.command;

import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.doc.standard.RequiredPermissions;
import com.jagrosh.jdautilities.examples.doc.Author;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.romvoid95.curseforge.command.base.BaseCommand;
import net.romvoid95.curseforge.command.base.CommandCategory;
import net.romvoid95.curseforge.command.base.annotation.CurseCommand;

@CommandInfo(
	name = {"github", "git", "repo"},
	description = "Displays the github repository link for the bot"
)
@RequiredPermissions({Permission.MESSAGE_EMBED_LINKS})
@Author("ROMVoid95")
@CurseCommand
public class CommandGithub extends BaseCommand {

	public CommandGithub() {
		super("github", CommandCategory.SERVER_MEMBER);
		this.aliases("git", "repo");
		this.help("Displays the github repository link for the bot");
		this.botPermissions = new Permission[] { Permission.MESSAGE_EMBED_LINKS };
	}

	@Override
	public void onExecute(CommandEvent event) {
		Field field = new Field("Github Link", "[Github Repository](https://github.com/ReadOnlyDevelopment/CursedBot)", false);
		embed(event, "CursedBot Github","Have an issue? Or want to contribute? Head on over to my Github Repo!", field);
	}

	private void embed(CommandEvent event, String title, String description, Field field) {
		event.getChannel().sendMessage(new MessageBuilder()
				.setEmbeds(new EmbedBuilder().setTitle(title).setDescription(description).addField(field).build())
				.build()).queue(sentMessage -> sentMessage.delete().queueAfter(2, TimeUnit.MINUTES, null, i -> {}));
		event.getMessage().delete().queue();
	}
}
