package net.romvoid95.curseforge.core;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;

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
		commandClient = Reflect.registerAllCommands(commandClient);
	}
	
	public CommandClient getCommandClient() {
		return this.commandClient.build();
	}
}
