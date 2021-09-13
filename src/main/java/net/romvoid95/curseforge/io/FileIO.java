package net.romvoid95.curseforge.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileIO {
	private static final String IS_NULL = "null!";
	private static final Charset UTF8 = Charset.forName("UTF-8");

	public static String read(File file){
		return read(file.toPath());
	}

	public static String read(Path path) {
		try {
			return new String(Files.readAllBytes(path), UTF8);
		} catch (IOException e) {
			e.printStackTrace();
			return IS_NULL;
		}
	}

	public static void write(File file, String contents) {
		write(file.toPath(), contents);
	}

	public static void write(Path path, String contents) {
		try {
			Files.write(path, contents.getBytes(UTF8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

