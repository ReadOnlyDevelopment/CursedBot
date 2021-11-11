package net.romvoid95.curseforge.command.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.romvoid95.command.Command;
import io.github.romvoid95.command.CommandEvent;
import io.github.romvoid95.command.argument.CommandArgument;
import io.github.romvoid95.command.argument.OptionalArgument;
import io.github.romvoid95.command.argument.RequiredArgument;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.romvoid95.curseforge.command.base.args.Argument;
import net.romvoid95.curseforge.command.base.args.ArgumentIndex;
import net.romvoid95.curseforge.util.DiscordUtils;

@Log4j2
public abstract class BaseCommand extends Command {

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
		log.info("(CommandEvent) " + event.getAuthor().getAsTag() + " ran the " + DiscordUtils.capitalize(this.name) + " command");
		if(this.getArgCount() > 0) {
			log.info("(CommandEvent) Invoked: [ " + this.name + " " + this.getArgsAsString() + " ]");
		}
		if(event.getArgs().equalsIgnoreCase("help")) {
			temporaryReply(ResultLevel.SUCCESS, this.getHelpEmbed(), 30, TimeUnit.SECONDS);
			event.getMessage().delete().queue();
		} else {
			onExecute(event);
		}
	}
	
	public abstract void onExecute(CommandEvent event);
	
	protected void aliases(String... aliases) {
		this.aliases = aliases;
	}
	
	protected void help(String string) {
		this.help = string;
	}
	
	protected void allowDms() {
		this.guildOnly = false;
	}
	
	protected void ownerOnly() {
		this.ownerCommand = true;
	}

	protected void requiredRoles(String... roles) {
		Arrays.asList(roles).forEach(r -> this.addRequiredRoles(r));
	}
	
	public void temporaryReply(ResultLevel level, String message, int time, TimeUnit unit) {
		MessageEmbed embed = new MessageEmbed(null, null, message, null, null, level.getColorInt(), null, null, null, null, null, null, null);
		event.reply(embed, success -> {
			success.delete().queueAfter(time, unit);
		});
	}
	
	public void temporaryReply(ResultLevel level, MessageEmbed embed, int time, TimeUnit unit) {
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
		argumentIndex.list().forEach(s -> b.append(s.val() + " "));
		return b.toString();
	}
	
	public MessageEmbed getHelpEmbed() {
		String s = "⁣  ";
		
		List<RequiredArgument> requiredArguments = new ArrayList<>();
		List<OptionalArgument> optionalArguments = new ArrayList<>();
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(DiscordUtils.capitalize(name + "Command"));
		builder.setDescription(this.getHelp());
		StringBuilder b1 = new StringBuilder();
		for(CommandArgument<?> arg : this.getArguments()) {
			if(arg instanceof RequiredArgument) {
				RequiredArgument a = (RequiredArgument) arg;
				requiredArguments.add(a);
				b1.append(" " + a.getArgumentForHelp());
			} else if(arg instanceof OptionalArgument) {
				OptionalArgument a = (OptionalArgument) arg;
				optionalArguments.add(a);
				b1.append(" " + a.getArgumentForHelp());
			}
		}
		b1.append("`");
		builder.appendDescription("\n\n**Usage:** `" + name + b1.toString());
		if(!requiredArguments.isEmpty()) {
			StringBuilder b2 = new StringBuilder();
			for(RequiredArgument r : requiredArguments) {
				b2.append(r.getArgumentForHelp() + "\n");
				b2.append(s + "-").append(" *" + r.getDescription() + "*\n");
			}
			builder.addField("Required Arguments", b2.toString(), false);
		}
		if(!optionalArguments.isEmpty()) {
			StringBuilder b2 = new StringBuilder();
			for(OptionalArgument r : optionalArguments) {
				b2.append(r.getArgumentForHelp() + "\n");
				b2.append(s + "⁣-").append(" *" + r.getDescription() + "*\n");
			}
			builder.addField("Optional Arguments", b2.toString(), false);
		}
		return builder.build();
	}
}
