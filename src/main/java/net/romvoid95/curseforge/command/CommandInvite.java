package net.romvoid95.curseforge.command;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.argument.OptionalArgument;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ApplicationInfo;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.romvoid95.curseforge.command.base.BaseCommand;
import net.romvoid95.curseforge.command.base.CommandCategory;
import net.romvoid95.curseforge.command.base.ResultLevel;
import net.romvoid95.curseforge.command.base.annotation.CurseCommand;
import net.romvoid95.curseforge.data.Data;

@CurseCommand
public class CommandInvite extends BaseCommand {
	
	private String oauthLink;

	public CommandInvite() {
		super("invite", CommandCategory.SERVER_MEMBER);
		this.help("Generates an invite link for the bot");
		this.addAgument(OptionalArgument.of("guild_id", "The guild_id to create a custom invite link"));
	}

	@Override
	public void onExecute(CommandEvent event) {
        if (oauthLink == null) {
            try {
                ApplicationInfo info = event.getJDA().retrieveApplicationInfo().complete();
                oauthLink = info.isBotPublic() ? info.getInviteUrl(0L, Data.getBotPermissions()) : "";
            } catch (Exception e) {
                Logger log = LoggerFactory.getLogger("OAuth2");
                log.error("Could not generate invite link ", e);
                oauthLink = "";
            }
        }
		if (noArgs()) {
			Field field = new Field("Invite Link", "[Invite Me](" + oauthLink + ")", false);
			embed(event, "Invite Me To Your Server", "Want to use this bot in your server also? No problem! heres your invite link", field);
		} else {
			if (getArgCount() > 1) {
				temporaryReply(ResultLevel.ERROR, "You can only define 1 serverId per command!", 20, TimeUnit.SECONDS);
			} else {
				String specialInvite = oauthLink + "&guild_id=" + getArgValue(0);
				Field field = new Field("Here is your direct link to invite the bot", "[Invite Me](" + specialInvite + ")",false);
				embed(event, "Invite Me To Your Server", "Want to use this bot in your server also? No problem! heres your invite link", field);
			}
		}
	}

	private void embed(CommandEvent event, String title, String description, Field field) {
		event.getChannel().sendMessage(new MessageBuilder()
				.setEmbeds(new EmbedBuilder().setTitle(title).setDescription(description).addField(field).build())
				.build()).queue(sentMessage -> sentMessage.delete().queueAfter(2, TimeUnit.MINUTES, null, i -> {}));
		event.getMessage().delete().queue();
	}
}
