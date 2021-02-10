package net.romvoid95.curseforge;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.romvoid95.curseforge.async.UpdateThread;
import net.romvoid95.curseforge.data.Data;

public class CurseForgeBot {

	public static CurseForgeBot INSTANCE;
	private static JDA jda;
	static int delay = 0;
	
	private CurseForgeBot() throws LoginException, InterruptedException {

		DataInterface.initializeCache();
		DataInterface.setProjectList(Arrays.asList(Data.config().get().getProjects()));
		DataInterface.setCaches();
		DataInterface.generateOverrides();
		
		DataInterface.runPreJDAChecks();

		jda = JDABuilder.create(
				Data.config().get().getToken(), 
				Collections.unmodifiableSet(EnumSet.of(GatewayIntent.GUILD_MESSAGES)))
				.disableCache(EnumSet.of(CacheFlag.ACTIVITY, CacheFlag.EMOTE, CacheFlag.CLIENT_STATUS, CacheFlag.VOICE_STATE))
				.setActivity(Activity.playing("CurseForgeBot Init Stage"))
				.build()
				.awaitReady();
	}
	
	public static void main(String... args) {
		try {
			INSTANCE = new CurseForgeBot();

			if(jda.getStatus().isInit()) {
				DataInterface.runPostJDAChecks();
				
				jda.getPresence().setActivity(Activity.playing("Checking for Updates!"));
				
				Data.cache().get().getCache().forEach(pd -> {
					delay = delay + 2;
					UpdateThread thread = new UpdateThread(pd);
					thread.run(delay);
				});
			}
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public JDA getJda() {
		return jda;
	}
}
