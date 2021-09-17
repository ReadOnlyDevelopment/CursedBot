package net.romvoid95.curseforge.command.base;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import ch.qos.logback.classic.Logger;
import groovyjarjarantlr4.v4.misc.Utils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.romvoid95.curseforge.CurseForgeBot;
import net.romvoid95.curseforge.command.base.args.Argument;
import net.romvoid95.curseforge.command.base.args.ArgumentIndex;

public abstract class BaseCommand extends Command {

	private static final Logger log = (Logger) LoggerFactory.getLogger(CurseForgeBot.class);
	
	private ArgumentIndex argumentIndex;
	private CommandEvent event;
	private Message returnMsg;

	protected BaseCommand(String name, Category category) {
		this.name = name;
		this.category = category;
	}
	
	@Override
	public void execute(CommandEvent event) {
		this.argumentIndex = new ArgumentIndex(event);
		this.event = event;
		log.info("(CommandEvent) " + event.getAuthor().getAsTag() + " ran the " + Utils.capitalize(this.name) + " command");
		if(this.getArgCount() > 0) {
			log.info("(CommandEvent) Invoked: [ " + this.name + " " + this.getArgsAsString() + " ]");
		}
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
		Arrays.asList(roles).forEach(r -> this.addRequiredRoles(r.getName()));
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
		argumentIndex.list().forEach(s -> b.append(s.val()));
		return b.toString();
	}
}
