package icu.puqns67.skintypefix.util;

import net.minecraft.client.resources.PlayerSkin;

import java.util.UUID;

public class Utils {
	private static final UUID INVALID_UUID = new UUID(0, 0);

	public static PlayerSkin.Model reverseModelType(PlayerSkin.Model type) {
		return switch (type) {
			case SLIM -> PlayerSkin.Model.WIDE;
			case WIDE -> PlayerSkin.Model.SLIM;
		};
	}

	public static boolean isInvalidUUID(UUID uuid) {
		return INVALID_UUID.equals(uuid);
	}
}
