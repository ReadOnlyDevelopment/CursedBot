package net.romvoid95.curseforge.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.google.common.collect.Sets;

import net.romvoid95.curseforge.command.base.BaseCommand;
import net.romvoid95.curseforge.command.base.annotation.CurseCommand;

public class Reflect {

	private static HashSet<Class<?>> getAllCommands() {
		ConfigurationBuilder configBuilder = new ConfigurationBuilder()
				.forPackages("net.romvoid95.curseforge.command")
				.filterInputsBy(new FilterBuilder().includePackage("net.romvoid95.curseforge.command"))
				.setScanners(new TypeAnnotationsScanner(), new SubTypesScanner());
		Reflections reflections = new Reflections(configBuilder);
		return Sets.newHashSet(reflections.getTypesAnnotatedWith(CurseCommand.class));
	}
	
	public static List<BaseCommand> getCommandList() {
		List<BaseCommand> list = new ArrayList<>();
		for(Class<?> clazz : getAllCommands()) {
			try {
				list.add((BaseCommand) clazz.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
