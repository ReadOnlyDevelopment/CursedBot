package net.romvoid95.curseforge.command.base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.romvoid95.curseforge.command.base.args.Argument;
import net.romvoid95.curseforge.command.base.args.ArgumentIndex;

public abstract class BaseCommand extends Command {
	
	protected static Category UPDATES = new Category("Updates");
	private ArgumentIndex argumentIndex;
	private CommandEvent event;
	private Message returnMsg;

	protected BaseCommand(String name, Category category, String arguments) {
		this.name = name;
		this.category = category;
		this.arguments = arguments;
	}
	
	@Override
	public void execute(CommandEvent event) {
		this.argumentIndex = new ArgumentIndex(event);
		this.event = event;
	}
	
	protected void aliases(String... aliases) {
		this.aliases = aliases;
	}
	
	protected void help(String string) {
		this.help = string;
	}
	
	protected void guildOnly() {
		this.guildOnly = true;
	}
	
	protected void ownerOnly() {
		this.ownerCommand = true;
	}

	protected void requiredRoles(Role... roles) {
		List<String> roleNames = new ArrayList<>();
		for(Role role : roles) {
			roleNames.add(role.getName());
		}
		this.requiredRoles.addAll(roleNames);
	}
	
	public void temporaryReply(ResultLevel level, String message, int time, TimeUnit unit) {
		MessageEmbed embed = new MessageEmbed(null, null, message, null, null, level.getColorInt(), null, null, null, null, null, null, null);
		event.reply(embed, success -> {
			success.delete().queueAfter(time, unit);
		});
	}
	
	public Message sendMessage(String msg) {
		event.reply(msg, m -> this.returnMsg = m);
		return returnMsg;
	}
	
	protected int getArgCount() {
		return argumentIndex.count();
	}
	
	protected String getArgValue(int index) {
		return argumentIndex.getArg(index).val();
	}
	
	protected Argument getArg(int index) {
		return argumentIndex.getArg(index);
	}
	
	protected boolean noArgs() {
		return argumentIndex.isEmpty();
	}
	
	protected String getArgsAsString() {
		StringBuilder b = new StringBuilder();
		b.append("Args: ");
		argumentIndex.list().forEach(s -> b.append("[" + s.val() + "]"));
		return b.toString();
	}
}
