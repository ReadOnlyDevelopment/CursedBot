package net.romvoid95.curseforge.command;

import java.util.concurrent.TimeUnit;

import io.github.romvoid95.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.romvoid95.curseforge.command.base.BaseCommand;
import net.romvoid95.curseforge.command.base.CommandCategory;
import net.romvoid95.curseforge.command.base.annotation.CurseCommand;

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
