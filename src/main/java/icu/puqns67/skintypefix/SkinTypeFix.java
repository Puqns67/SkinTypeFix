package icu.puqns67.skintypefix;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@OnlyIn(Dist.CLIENT)
@Mod(SkinTypeFix.ID)
public class SkinTypeFix {
	public static final String ID = "skintypefix";
	public static final String NAME = "SkinTypeFix";

	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	public SkinTypeFix(IEventBus modEventBus, ModContainer modContainer) {
		modEventBus.addListener(this::setup);
		modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
	}

	private void setup(final FMLClientSetupEvent event) {
		LOGGER.info("Loaded!");
	}
}
