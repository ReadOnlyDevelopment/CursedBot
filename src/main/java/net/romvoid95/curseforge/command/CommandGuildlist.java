package net.romvoid95.curseforge.command;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.jagrosh.jdautilities.command.CommandEvent;

import io.vavr.collection.Stream;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.romvoid95.curseforge.CursedBot;
import net.romvoid95.curseforge.command.base.BaseCommand;
import net.romvoid95.curseforge.command.base.CommandCategory;
import net.romvoid95.curseforge.command.base.annotation.CurseCommand;
import net.romvoid95.curseforge.util.Utils;
import net.romvoid95.curseforge.util.menu.EmbedMessageMenu;

@CurseCommand
public class CommandGuildlist extends BaseCommand {

	private final EmbedMessageMenu.Builder pbuilder;

	public CommandGuildlist(){
		super("guilds", CommandCategory.BOT_OWNER);
		this.help("shows the list of guilds the bot is on");
		this.allowDms();
		this.ownerOnly();
		pbuilder = new EmbedMessageMenu.Builder()
				.setEventWaiter(CursedBot.eventWaiter)
				.setTimeout(1, TimeUnit.MINUTES);
	}

	@Override
	public void onExecute(CommandEvent event) {
		pbuilder.clearItems();
		Stream<Guild> guilds = Stream.of(CursedBot.instance().getJda().getGuilds().toArray(new Guild[0]));
		Map<MessageEmbed, Guild> guildMap = guilds.collect(Collectors.toMap(g -> Utils.guildsFunc.apply(g), g -> g));
		EmbedMessageMenu p = pbuilder.setText("Joined Server List").setUsers(event.getAuthor()).setItems(guildMap).setJda(CursedBot.instance().getJda()).build();
		event.getMessage().delete().queue();
		p.display(event.getChannel());
	}
}
