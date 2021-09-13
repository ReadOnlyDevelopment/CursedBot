package net.romvoid95.curseforge;

import java.util.EnumSet;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.romvoid95.curseforge.async.CurrentThreads;
import net.romvoid95.curseforge.commands.CommandAddProject;
import net.romvoid95.curseforge.data.Data;

public class CurseForgeBot {

	public static CurseForgeBot _instance;
	private DataInterface dataInterface = new DataInterface();
	private CommandClientBuilder clientBuilder;
	private JDA jda;
	private CurrentThreads currentThreads;

	private CurseForgeBot() throws LoginException, InterruptedException {

		clientBuilder = new CommandClientBuilder();
		clientBuilder.setOwnerId(Data.config().get().getOwner());
		clientBuilder.setPrefixes(Data.config().get().getPrefixes());
		clientBuilder.addCommand(new CommandAddProject());
		CommandClient client = clientBuilder.build();
		
		EnumSet<GatewayIntent> intents = EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES);
		EnumSet<CacheFlag> caches = EnumSet.of(CacheFlag.ACTIVITY, CacheFlag.EMOTE, CacheFlag.CLIENT_STATUS, CacheFlag.VOICE_STATE);

		jda = JDABuilder
				.create(Data.config().get().getToken(), intents)
				.disableCache(caches)
				.setActivity(Activity.playing("CurseForgeBot Init Stage"))
				.addEventListeners(client)
				.build()
				.awaitReady();
		
		if (jda.getStatus().isInit()) {
		
		}

	}

	public static void main(String... args) {
		try {
			_instance = new CurseForgeBot();
			
			if (_instance.getJda().getStatus().isInit()) {
				_instance.getJda().getPresence().setActivity(Activity.playing("Checking for Updates!"));
				
				_instance.dataInterface.setup();
				
				_instance.setCurrentThreads(new CurrentThreads(Data.cache().get()));
				_instance.setRuns();
			}
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setRuns() {
		getCurrentThreads().runThreads();
	}

	public JDA getJda() {
		return jda;
	}

	public DataInterface getDataInterface() {
		return dataInterface;
	}
	
	public void setCurrentThreads(CurrentThreads currentThreads) {
		this.currentThreads = currentThreads;
	}

	public CurrentThreads getCurrentThreads() {
		return currentThreads;
	}
}
