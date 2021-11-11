package net.romvoid95.curseforge.core;

import java.util.Objects;

import io.github.romvoid95.command.Command;
import io.github.romvoid95.command.Command.Category;
import io.github.romvoid95.command.CommandClient;
import io.github.romvoid95.command.CommandClientBuilder;
import io.github.romvoid95.command.argument.CommandArgument;
import io.github.romvoid95.command.argument.OptionalArgument;
import io.github.romvoid95.command.argument.RequiredArgument;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.romvoid95.curseforge.data.Data;
import net.romvoid95.curseforge.data.config.Config;
import net.romvoid95.curseforge.util.Reflect;

public final class ClientBuilder {
	
	private static ClientBuilder _instance;

	public static ClientBuilder instance() {
		if (_instance == null) {
			_instance = new ClientBuilder();
		}
		return _instance;
	}
	
	private final Config CONFIG = Data.config().get();
	private CommandClientBuilder commandClient;
	
	private ClientBuilder() {
		this.commandClient = new CommandClientBuilder();
		commandClient.setOwnerId(CONFIG.getOwner());
		commandClient.setPrefixes(CONFIG.getPrefixes());
		Reflect.getCommandList().forEach(c -> commandClient.addCommand(c));
		commandClient.setHelpConsumer((event) -> {
                StringBuilder builder = new StringBuilder("**"+event.getSelfUser().getName()+"** commands:\n\n");
                Category category = null;
                builder.append("[arg]   = Required Argument\n");
                builder.append("<args>  = Optional Argument\n");
                for(Command command : Reflect.getCommandList())
                {
                    if(!command.isHidden() && (!command.isOwnerCommand() || event.isOwner()))
                    {
                        if(!Objects.equals(category, command.getCategory()))
                        {
                            category = command.getCategory();
                            builder.append("\n  __").append(category==null ? "No Category" : category.getName()).append("__:\n");
                        }
                        builder.append("\n`").append(CONFIG.getPrefixes()[0]).append(command.getName());
                        		for(CommandArgument<?> arg : command.getArguments()) {
                        			if(arg instanceof RequiredArgument) {
                        				RequiredArgument a = (RequiredArgument) arg;
                        				builder.append(" " + a.getArgumentForHelp());
                        			} else if(arg instanceof OptionalArgument) {
                        				OptionalArgument a = (OptionalArgument) arg;
                        				builder.append(" " + a.getArgumentForHelp());
									}
                        		}
                        		builder.append("`");
                        		builder.append(" - ").append(command.getHelp());
                    }
                }
                User owner = event.getJDA().getUserById(CONFIG.getOwner());
                if(owner!=null)
                {
                    builder.append("\n\nFor additional help, contact **").append(owner.getName()).append("**#").append(owner.getDiscriminator());
                }
                event.replyInDm(builder.toString(), unused ->
                {
                    if(event.isFromType(ChannelType.TEXT))
                        event.reactSuccess();
                }, t -> event.replyWarning("Help cannot be sent because you are blocking Direct Messages."));
        });
	}
	
	public CommandClient getCommandClient() {
		return this.commandClient.build();
	}
}
