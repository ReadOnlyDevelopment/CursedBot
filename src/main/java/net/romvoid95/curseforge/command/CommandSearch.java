package net.romvoid95.curseforge.command;

import java.awt.Color;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.argument.OptionalArgument;
import com.jagrosh.jdautilities.command.argument.RequiredArgument;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.doc.standard.Error;
import com.jagrosh.jdautilities.doc.standard.RequiredPermissions;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.jagrosh.jdautilities.menu.EmbedPaginator;
import com.therandomlabs.curseapi.CurseAPI;
import com.therandomlabs.curseapi.CurseException;
import com.therandomlabs.curseapi.project.CurseProject;
import com.therandomlabs.curseapi.project.CurseSearchQuery;
import com.therandomlabs.curseapi.project.CurseSearchSort;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.romvoid95.curseforge.CurseForgeBot;
import net.romvoid95.curseforge.command.base.BaseCommand;
import net.romvoid95.curseforge.command.base.CommandCategory;
import net.romvoid95.curseforge.command.base.ResultLevel;
import net.romvoid95.curseforge.command.base.annotation.CurseCommand;
import net.romvoid95.curseforge.util.Utils;

@CommandInfo(
	name = "Search",
	description = "Search for Mods or Modpacks on Curseforge given their name"
)
@Error(
	value = "if arguments [mod] or [modpack] are not provided, but a name and Game Version is",
	response = "Must specify either [mod] or [modpack] before the name"
)
@RequiredPermissions({Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_MANAGE})
@Author("ROMVoid95")
@CurseCommand
public class CommandSearch extends BaseCommand {

	private final EmbedPaginator.Builder pbuilder;
	private String srchMsgId;
	
	private final RequiredArgument seachType = RequiredArgument.of("mod | modpack", "The type to search for");
	private final RequiredArgument name = RequiredArgument.of("name", "The name to search for");
	private final OptionalArgument gameVersion = OptionalArgument.of("version", "The Game Version to filter the search for");
	
	public CommandSearch() {
		super("find", CommandCategory.SERVER_MEMBER);
		this.help("Search for Mods or Modpacks on Curseforge given their name");
		this.isGuildOnly();
		this.addAguments(seachType, name, gameVersion);
        pbuilder = new EmbedPaginator.Builder()
        		.setEventWaiter(CurseForgeBot.eventWaiter)
        		.setTimeout(5, TimeUnit.MINUTES)
        		.wrapPageEnds(true);
	}
	
	@Override
	public void onExecute(CommandEvent event) {
		String searchType = null;
		String gameVersion = "";
		int id = -1;
		String searchName = null;
		boolean runAndDisplay = false;
		event.getMessage().delete().queue();
		if(noArgs()) {
			temporaryReply(ResultLevel.ERROR, "Command Useage: " + event.getPrefix() + this.getName() + " " + this.getArguments(), 30, TimeUnit.SECONDS);
			return;
		} else {
			if(getArgCount() < 2) {
				if(!getArg(0).validate(Utils.check("mod")) && !getArg(0).validate(Utils.check("modpack"))) {
					temporaryReply(ResultLevel.ERROR, "You must specify either 'mod' or 'modpack'", 30, TimeUnit.SECONDS);
					return;
				} else {
					temporaryReply(ResultLevel.ERROR, "No " + getArgValue(0) + " name specified!", 30, TimeUnit.SECONDS);
					return;
				}
			} else if(getArgCount() == 2) {
				if(!getArg(0).validate(Utils.check("mod")) && !getArg(0).validate(Utils.check("modpack"))) {
					temporaryReply(ResultLevel.ERROR, "You must specify either 'mod' or 'modpack'", 30, TimeUnit.SECONDS);
					return;
				}
				searchType = getArgValue(0);
				if(searchType.equalsIgnoreCase("modpack")) {
					id = 4475;
				} else {
					id = 426;
				}
				searchName = getArgValue(1);
				runAndDisplay = true;
			} else if(getArgCount() == 3){
				if(!getArg(0).validate(Utils.check("mod")) && !getArg(0).validate(Utils.check("modpack"))) {
					temporaryReply(ResultLevel.ERROR, "You must specify either 'mod' or 'modpack'", 30, TimeUnit.SECONDS);
					return;
				}
				searchType = getArgValue(0);
				if(searchType.equalsIgnoreCase("modpack")) {
					id = 4475;
				} else {
					id = 426;
				}
				searchName = getArgValue(1);
				gameVersion = getArgValue(2);
				runAndDisplay = true;
			}
			CurseSearchQuery query = null;
			if(searchName != null && id != -1) {
				query = createQuery(searchName, id, gameVersion);
			}
			if(runAndDisplay == true) {
				runSearch(event, query, searchName, searchType, gameVersion);
			} else {
				event.reply(new EmbedBuilder().setDescription("There was an error while trying to run the command").setColor(Color.RED).build(), sent -> sent.delete().queueAfter(30, TimeUnit.SECONDS));
				return;
			}
		}
	}
	
	private CurseSearchQuery createQuery(String search, int id, String gameVersion) {
		CurseSearchQuery query = new CurseSearchQuery();
		query.gameID(432);
		query.gameVersionString(gameVersion);
		query.categoryID(id);
		query.pageIndex(0);
		query.pageSize(50);
		query.searchFilter(search);
		query.categorySectionID(0);
		query.sortingMethod(CurseSearchSort.FEATURED);
		return query;
	}
	
	private void runSearch(CommandEvent event, CurseSearchQuery query, String searchName, String searchType, String gameVersion) {
		event.getChannel().sendMessage("**Searching . . .**").queue(m -> srchMsgId = m.getId());
		List<MessageEmbed> embeds = Lists.newArrayList();
		try {
			Optional<List<CurseProject>> result = CurseAPI.searchProjects(query);
			
			if(result.isPresent()) {
				pbuilder.clearItems();
				embeds = result.get().stream()
				.map(p -> Utils.func.apply(p))
				.collect(Collectors.toList());
			}
		} catch (CurseException e) {
			e.printStackTrace();
			return;
		}
		
		String text;
		if(gameVersion.length() == 0) {
			text = "Search Results for `" + searchName + "`\n**Results Found:** " + embeds.size() + " " + searchType + "s";
		} else {
			text = "Search Results for `" + searchName + "` on `" + gameVersion + "`\n**Results Found:** " + embeds.size() + " " + searchType + "s";
		}
		
		EmbedPaginator p = pbuilder.setText(text)
				 .setUsers(event.getAuthor())
				 .addItems(embeds)
	             .build();
		p.display(event.getChannel().retrieveMessageById(srchMsgId).complete());
	}
}
