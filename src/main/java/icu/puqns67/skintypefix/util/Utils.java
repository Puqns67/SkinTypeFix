package icu.puqns67.skintypefix.util;

import com.mojang.blaze3d.platform.NativeImage;
import icu.puqns67.skintypefix.SkinTypeFix;
import icu.puqns67.skintypefix.util.image.Places;
import icu.puqns67.skintypefix.util.image.Point;
import net.minecraft.Util;
import net.minecraft.client.resources.PlayerSkin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Utils {
	private static final ArrayList<Point> PLAYER_SKIN_SLIM = Places.PLAYER_SKIN_SLIM.points();
	private static final ArrayList<Point> PLAYER_SKIN_DIFF_SLIM_TO_WILD = Places.PLAYER_SKIN_DIFF_SLIM_TO_WILD.points();

	public static boolean isInvalidUUID(UUID uuid) {
		return Util.NIL_UUID.equals(uuid);
	}

	@Nullable
	public static PlayerSkin.Model checkSkinModelType(NativeImage image) {
		SkinTypeFix.LOGGER.debug("Checking: {}", image);

		var timesForDiff = new HashMap<Integer, Integer>();
		var timesForSlim = new HashMap<Integer, Integer>();
		PlayerSkin.Model result = null;

		// The result of NativeImage.getPixelRGBA() is ARGB, not RGBA, like 0xAARRGGBB
		PLAYER_SKIN_DIFF_SLIM_TO_WILD.forEach(p -> timesForDiff.merge(image.getPixelRGBA(p.x(), p.y()), 1, Integer::sum));
		PLAYER_SKIN_SLIM.forEach(p -> timesForSlim.merge(image.getPixelRGBA(p.x(), p.y()), 1, Integer::sum));

		var blackTimesForDiff = timesForDiff.getOrDefault(0xff000000, 0);
		var blackTimesForSlim = timesForSlim.getOrDefault(0xff000000, 0);

		// Rules for check skin module type
		if (blackTimesForDiff == 64 && blackTimesForSlim < 256) {
			result = PlayerSkin.Model.SLIM;
		} else if (blackTimesForDiff == 0 || timesForDiff.size() >= 4) {
			result = PlayerSkin.Model.WIDE;
		}

		return result;
	}
}
