package net.romvoid95.curseforge.util;

import java.util.HashSet;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.google.common.collect.Sets;
import com.jagrosh.jdautilities.command.CommandClientBuilder;

import lombok.extern.slf4j.Slf4j;
import net.romvoid95.curseforge.command.base.BaseCommand;
import net.romvoid95.curseforge.command.base.annotation.CurseCommand;

@Slf4j
public class Reflect {

	private static HashSet<Class<?>> getAllCommands() {
		ConfigurationBuilder configBuilder = new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage("net.romvoid95.curseforge.command"))
				.setScanners(new TypeAnnotationsScanner(), new SubTypesScanner());
		Reflections reflections = new Reflections(configBuilder);
		return Sets.newHashSet(reflections.getTypesAnnotatedWith(CurseCommand.class));
	}
	
	public static CommandClientBuilder registerAllCommands(CommandClientBuilder builer) {
		CommandClientBuilder b = builer;
		for(Class<?> clazz : getAllCommands()) {
			try {
				log.info("Registered Command: " + clazz.getSimpleName().replace("Command", ""));
				b.addCommand((BaseCommand) clazz.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return b;
	}
}
