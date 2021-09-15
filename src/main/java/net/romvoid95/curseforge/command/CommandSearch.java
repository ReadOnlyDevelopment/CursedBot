package net.romvoid95.curseforge.command;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.doc.standard.Error;
import com.jagrosh.jdautilities.doc.standard.RequiredPermissions;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.jagrosh.jdautilities.menu.EmbedPaginator;
import com.therandomlabs.curseapi.CurseAPI;
import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.file.CurseFile;
import com.therandomlabs.curseapi.project.CurseProject;
import com.therandomlabs.curseapi.project.CurseSearchQuery;
import com.therandomlabs.curseapi.project.CurseSearchSort;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.romvoid95.curseforge.command.base.BaseCommand;
import net.romvoid95.curseforge.command.base.ResultLevel;
import net.romvoid95.curseforge.util.DiscordUtils;
import net.romvoid95.curseforge.util.Utils;

@CommandInfo(
	    name = "Search",
	    description = "Search for Mods or Modpacks on Curseforge given their name",
	    requirements = {
	        "The bot has all necessary permissions.",
	        "The user is the bot's owner."
	    }
	)
	@Error(
	    value = "if arguments [mod] or [modpack] are not provided, but a name is",
	    response = "Must specify either [mod] or [modpack] before the name"
	)
	@RequiredPermissions({Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ADD_REACTION})
	@Author("ROMVoid95")
@Slf4j
public class CommandSearch extends BaseCommand {

	private final EmbedPaginator.Builder pbuilder;
	private final Predicate<String> testArg = new Predicate<String>() {
		@Override
		public boolean apply(@Nullable String input) {
			return input.equalsIgnoreCase("mod") || input.equalsIgnoreCase("modpack");
		}
	};
	private final Function<CurseProject, MessageEmbed> func = new Function<CurseProject, MessageEmbed>() {
		DateTimeFormatter dateOnly = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
		@Override
		public MessageEmbed apply(CurseProject t) {
			CurseFile file = getFirst(t);
			String release = file.releaseType().toString();
			int timeSince = Utils.last(t.lastUpdateTime().toLocalDate());
			
			return new EmbedBuilder()
					.setTitle(t.name(), t.url().toString())
					.setDescription("**Author**: \n"+t.author().name()+"\n\n")
					.appendDescription("**Downloads**: \n" + t.downloadCount() + "\n\n")
					.appendDescription("**Created**: \n" + t.creationTime().format(dateOnly) + "\n\n")
					.appendDescription("**Last Updated**: \n" 
							+ t.lastUpdateTime().format(dateOnly) + " *(" + timeSince + " days ago)*\n\n")
					.appendDescription("**Summary:** \n`" + t.summary() + "`")
					.addBlankField(false)
					.addField("Latest File", "**Type**: `" + DiscordUtils.capitalize(release) + "`\n" + 
							"**Link**: [" + file.displayName() + "](" + url(file) + ")", false)
					.setThumbnail(t.logo().thumbnailURL().toString())
					.build();
		}
		
	};
	
	private String srchMsgId;
	
	public CommandSearch(EventWaiter waiter) {
		super("find", new Category("Utilities"), "[mod|modpack] '[name]' [version]");
		this.help("Search for Mods or Modpacks on Curseforge given their name");
		this.isGuildOnly();
        pbuilder = new EmbedPaginator.Builder()
        		.setEventWaiter(waiter)
        		.setTimeout(5, TimeUnit.MINUTES)
        		.wrapPageEnds(true);
	}
	
	@Override
	public void execute(CommandEvent event) {
		super.execute(event);
		String searchType;
		event.getMessage().delete().queue();
		if(noArgs()) {
			temporaryReply(ResultLevel.ERROR, "Command Useage: " + event.getPrefix() + this.getName() + " " + this.getArguments(), 30, TimeUnit.SECONDS);
			return;
		} else {
			if(getArgCount() < 2) {
				if(!getArg(0).validate(testArg)) {
					temporaryReply(ResultLevel.ERROR, "You must specify either 'mod' or 'modpack'", 30, TimeUnit.SECONDS);
					return;
				} else {
					temporaryReply(ResultLevel.ERROR, "No " + getArgValue(0) + " name specified!", 30, TimeUnit.SECONDS);
					return;
				}
			} else if(getArgCount() == 3){
				if(!getArg(0).validate(testArg)) {
					temporaryReply(ResultLevel.ERROR, "You must specify either 'mod' or 'modpack'", 30, TimeUnit.SECONDS);
					return;
				} else {
					event.getChannel().sendMessage("**Searching . . .**").queue(m -> srchMsgId = m.getId());
					searchType = getArgValue(0);
					int id;
					if(searchType.equalsIgnoreCase("modpack")) {
						id = 4475;
					} else {
						id = 426;
					}
					String searchName = getArgValue(1);
					CurseSearchQuery query = new CurseSearchQuery()
							.gameID(432)
							.categoryID(id)
							.gameVersionString(getArgValue(2))
							.searchFilter(searchName)
							.categorySectionID(0)
							.sortingMethod(CurseSearchSort.POPULARITY);
					List<MessageEmbed> embeds = Lists.newArrayList();
					try {
						Optional<List<CurseProject>> result = CurseAPI.searchProjects(query);
						
						if(result.isPresent()) {
							log.info("Results = " + result.get().size());
							log.info("search result is present");
							pbuilder.clearItems();
							embeds = result.get().stream()
							.map(p -> func.apply(p))
							.collect(Collectors.toList());
						}
					} catch (CurseException e) {
						e.printStackTrace();
					}
					EmbedPaginator p = pbuilder.setText("Search Results for `" + searchName + "` on `" + getArgValue(2) 
					+ "`\n**Results Found:** " + embeds.size() + " " + searchType + "s")
							 .setUsers(event.getAuthor())
							 .addItems(embeds)
				             .build();
					p.display(event.getChannel().retrieveMessageById(srchMsgId).complete());
				}
			}
		}
	}
	
	private CurseFile getFirst(CurseProject p) {
		try {
			return p.files().first();
		} catch (CurseException e) {
			return null;
		}
	}
	
	private String url(CurseFile file) {
		try {
			return file.url().toString();
		} catch (CurseException e) {
			return null;
		}
	}
}
