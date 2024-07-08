package icu.puqns67.skintypefix.util;

import net.minecraft.Util;
import net.minecraft.client.resources.PlayerSkin;

import java.util.UUID;

public class Utils {
	public static PlayerSkin.Model reverseModelType(PlayerSkin.Model type) {
		return switch (type) {
			case SLIM -> PlayerSkin.Model.WIDE;
			case WIDE -> PlayerSkin.Model.SLIM;
		};
	}

	public static boolean isInvalidUUID(UUID uuid) {
		return Util.NIL_UUID.equals(uuid);
	}
}
