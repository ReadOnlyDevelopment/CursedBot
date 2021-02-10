package net.romvoid95.curseforge.manager;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.rom.utility.io.FileIO;

public class JsonDataManager<T> implements DataManager<T> {

	private static final Logger LOG = LoggerFactory.getLogger(JsonDataManager.class);

	private static final ObjectMapper mapper = new ObjectMapper()
			.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true) // Allow newlines.
			.configure(JsonReadFeature.ALLOW_MISSING_VALUES.mappedFeature(), true)
			.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
	private final Path configPath;
	private final T data;

	public JsonDataManager(Class<T> clazz, String file, Supplier<T> constructor) {
		this.configPath = Paths.get(file);
		if (!configPath.toFile().exists()) {
			try {
				if (configPath.toFile().createNewFile()) {
					FileIO.write(configPath.toFile(), toString(constructor.get()));
				} else {
					LOG.error(String.format("Could not create config file at %s", file));
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			if (file.equalsIgnoreCase("config.json"))
				LOG.info("Configuration file generated, Please set Required values and restart");
				System.exit(0);
		}
		try {
			this.data = fromJson(FileIO.read(configPath.toFile()), clazz);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public T get() {
		return data;
	}

	@Override
	public void save() {
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(configPath.toFile(), data);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private static String toString(Object data) throws JsonProcessingException {
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
	}

	public static <T> String toJson(T object) throws JsonProcessingException {
		return mapper.writeValueAsString(object);
	}

	public static <T> T fromJson(String json, Class<T> clazz) throws IOException {
		return mapper.readValue(json, clazz);
	}

	public static <T> T fromJson(String json, TypeReference<T> type) throws IOException {
		return mapper.readValue(json, type);
	}

	public static <T> T fromJson(String json, JavaType type) throws IOException {
		return mapper.readValue(json, type);
	}

}
