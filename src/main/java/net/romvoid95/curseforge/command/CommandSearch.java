package net.romvoid95.curseforge.command;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.argument.OptionalArgument;
import com.jagrosh.jdautilities.command.argument.RequiredArgument;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
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
	
	private final RequiredArgument seachType = RequiredArgument.of("mod|modpack", "The type to search for");
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
	public void execute(CommandEvent event) {
		super.execute(event);
		String searchType;
		event.getMessage().delete().queue();
		if(noArgs()) {
			temporaryReply(ResultLevel.ERROR, "Command Useage: " + event.getPrefix() + this.getName() + " " + this.getArguments(), 30, TimeUnit.SECONDS);
			return;
		} else {
			if(getArgCount() < 2) {
				if(!getArg(0).validate(Utils.getAgumentPredicate("mod", "modpack"))) {
					temporaryReply(ResultLevel.ERROR, "You must specify either 'mod' or 'modpack'", 30, TimeUnit.SECONDS);
					return;
				} else {
					temporaryReply(ResultLevel.ERROR, "No " + getArgValue(0) + " name specified!", 30, TimeUnit.SECONDS);
					return;
				}
			} else if(getArgCount() == 3){
				if(!getArg(0).validate(Utils.getAgumentPredicate("mod", "modpack"))) {
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
							pbuilder.clearItems();
							embeds = result.get().stream()
							.map(p -> Utils.func.apply(p))
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
}
