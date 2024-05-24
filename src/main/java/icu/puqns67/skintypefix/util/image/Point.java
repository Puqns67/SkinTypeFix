package icu.puqns67.skintypefix.util.image;

import com.mojang.blaze3d.platform.NativeImage;

public record Point(int x, int y) {
	public Point {
		if (x < 0 || y < 0) {
			throw new IllegalArgumentException(String.format("Invalid position: %d, %d", x, y));
		}
	}

	public boolean isBlack(NativeImage image) {
		return image.getPixelRGBA(x, y) == 0xff000000;
	}
}
