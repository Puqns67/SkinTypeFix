package icu.puqns67.skintypefix;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
@Mod(SkinTypeFix.MODID)
public class SkinTypeFix {
	public static final String MODID = "skintypefix";
	public static final Logger LOGGER = LogUtils.getLogger();

	public SkinTypeFix(IEventBus modEventBus, ModContainer modContainer) {
		modEventBus.addListener(this::setup);
		modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
	}

	private void setup(final FMLClientSetupEvent event) {
		LOGGER.info("Loaded!");
	}
}
