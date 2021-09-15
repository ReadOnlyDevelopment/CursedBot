package net.romvoid95.curseforge.command;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ApplicationInfo;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.romvoid95.curseforge.command.base.BaseCommand;
import net.romvoid95.curseforge.command.base.ResultLevel;

public class CommandInvite extends BaseCommand {
	
	private String oauthLink;
	private EnumSet<Permission> botPerms = EnumSet.of(
			Permission.VIEW_CHANNEL,
			Permission.MESSAGE_WRITE,
			Permission.MESSAGE_MANAGE,
			Permission.MESSAGE_EMBED_LINKS,
			Permission.MESSAGE_ATTACH_FILES,
			Permission.MESSAGE_READ,
			Permission.MESSAGE_EXT_EMOJI,
			Permission.MESSAGE_ADD_REACTION,
			Permission.CREATE_INSTANT_INVITE);

	public CommandInvite() {
		super("invite", new Category("General"), "[guild_id]");
		this.help("Generates an invite link for the bot");
	}

	@Override
	public void execute(CommandEvent event) {
		super.execute(event);
        if (oauthLink == null) {
            try {
                ApplicationInfo info = event.getJDA().retrieveApplicationInfo().complete();
                oauthLink = info.isBotPublic() ? info.getInviteUrl(0L, botPerms) : "";
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
