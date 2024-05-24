package icu.puqns67.skintypefix;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = SkinTypeFix.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

	private static final ModConfigSpec.BooleanValue SKIP_FIX_FOR_SLIM_PLAYERS = BUILDER
		.comment("Skip fix for SLIM players, default to true")
		.define("skipFixForSlimPlayers", true);

	static final ModConfigSpec SPEC = BUILDER.build();
	
	public static boolean skipFixForSlimPlayers;

	@SubscribeEvent
	static void onLoad(final ModConfigEvent event) {
		skipFixForSlimPlayers = SKIP_FIX_FOR_SLIM_PLAYERS.get();
	}
}
