package icu.puqns67.skintypefix.util;

import net.minecraft.client.util.SkinTextures;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


public class Utils {
	private static final UUID INVALID_UUID = new UUID(0, 0);

	public static @NotNull SkinTextures.Model reverseModelType(SkinTextures.Model type) {
		return switch (type) {
			case SLIM -> SkinTextures.Model.WIDE;
			case WIDE -> SkinTextures.Model.SLIM;
		};
	}

	public static boolean isInvalidUUID(UUID uuid) {
		return INVALID_UUID.equals(uuid);
	}
}
