package icu.puqns67.skintypefix.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import icu.puqns67.skintypefix.SkinTypeFix;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


public class ConfigLoader {
	private static final Path PATH = Path.of("config", "skintypefix.json");
	private static final Gson GSON = new GsonBuilder()
		.setPrettyPrinting()
		.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
		.create();

	public static Config load() {
		if (!Files.exists(PATH)) {
			SkinTypeFix.LOGGER.info("[SkinTypeFix] Config file does not exist, create a new one!");
			var result = new Config();
			save(result);
			return result;
		}
		try (var reader = Files.newBufferedReader(PATH)) {
			return GSON.fromJson(reader, Config.class);
		} catch (Exception e) {
			SkinTypeFix.LOGGER.warn("[SkinTypeFix] Can't read config file, using default config!", e);
		}
		return new Config();
	}

	public static void save(Config config) {
		try (var writer = Files.newBufferedWriter(PATH, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
			GSON.toJson(config, writer);
		} catch (Exception e) {
			SkinTypeFix.LOGGER.warn("[SkinTypeFix] Can't save config file, changes may not be saved!", e);
		}
	}
}
