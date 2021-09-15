package net.romvoid95.curseforge.command.base.args;

import com.google.common.base.Predicate;

public class Argument implements IArgument<String> {

	private String argument;

	public Argument(String arg) {
		this.argument = arg;
	}
	
	@Override
	public String val() {
		return argument;
	}
	
	public boolean validate(Predicate<String> predicate) {
		return predicate.test(argument);
	}
}
