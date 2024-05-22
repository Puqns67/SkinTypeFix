package icu.puqns67.skintypefix;

import icu.puqns67.skintypefix.config.Config;
import icu.puqns67.skintypefix.config.ConfigLoader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Environment(EnvType.CLIENT)
public class SkinTypeFix implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("SkinTypeFix");
	public static final Config CONFIG = ConfigLoader.load();

	@Override
	public void onInitializeClient() {
		LOGGER.info("Loaded!");
	}
}
