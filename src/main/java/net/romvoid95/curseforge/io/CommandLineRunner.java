package net.romvoid95.curseforge.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandLineRunner {
	
	private static final Runtime cmd = Runtime.getRuntime();
	
	public static void run(String command, File file, boolean append, String... args) {
		List<String> argList = new ArrayList<>();
		for(String arg : args) {
			if(append) {
				argList.add("/" + arg);
			} else {
				argList.add(arg);
			}
		}
		try {
			cmd.exec(buildCommand(command, argList), new String[0], file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String buildCommand(String command, List<String> argList) {
		StringBuilder builder = new StringBuilder();
		builder.append(command);
		for(String s : argList) {
			builder.append(" ");
			builder.append(s);
		}
		builder.append(" ");
		return builder.toString();
	}
}
