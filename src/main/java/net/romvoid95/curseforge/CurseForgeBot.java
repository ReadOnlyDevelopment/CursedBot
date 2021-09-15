package net.romvoid95.curseforge;

import java.util.EnumSet;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.romvoid95.curseforge.async.CurrentThreads;
import net.romvoid95.curseforge.command.CommandAddProject;
import net.romvoid95.curseforge.command.CommandSearch;
import net.romvoid95.curseforge.data.Data;

public class CurseForgeBot {

	private static CurseForgeBot _instance;
	private CommandClientBuilder clientBuilder;
	private JDA jda;
	private CurrentThreads currentThreads;
	private EventWaiter eventWaiter;

	public static CurseForgeBot instance() {
		if (_instance != null) {
			return _instance;
		}
		return null;
	}

	private CurseForgeBot() throws LoginException, InterruptedException {

		eventWaiter = new EventWaiter();
		clientBuilder = new CommandClientBuilder();
		clientBuilder.setOwnerId(Data.config().get().getOwner());
		clientBuilder.setPrefixes(Data.config().get().getPrefixes());
		clientBuilder.addCommands(new CommandAddProject(), new CommandSearch(eventWaiter));
		
		EnumSet<GatewayIntent> intents = EnumSet.of(
				GatewayIntent.GUILD_EMOJIS, 
				GatewayIntent.GUILD_MESSAGES, 
				GatewayIntent.GUILD_PRESENCES, 
				GatewayIntent.GUILD_MESSAGE_REACTIONS);
		
		EnumSet<CacheFlag> caches = EnumSet.of(
				CacheFlag.ACTIVITY, 
				CacheFlag.CLIENT_STATUS, 
				CacheFlag.VOICE_STATE);

		jda = JDABuilder
				.create(Data.config().get().getToken(), intents)
				.disableCache(caches)
				.setActivity(Activity.playing("CurseForgeBot Init Stage"))
				.addEventListeners(eventWaiter, clientBuilder.build())
				.build()
				.awaitReady();

	}

	public static void main(String... args) {
		try {
			_instance = new CurseForgeBot();
			
			if (_instance.getJda().getStatus().isInit()) {
				_instance.getJda().getPresence().setActivity(Activity.playing("Checking for Updates!"));
				
				DataInterface.instance().setup();
				
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

	public void setCurrentThreads(CurrentThreads currentThreads) {
		this.currentThreads = currentThreads;
	}

	public CurrentThreads getCurrentThreads() {
		return currentThreads;
	}
}
