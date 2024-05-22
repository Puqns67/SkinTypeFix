package icu.puqns67.skintypefix.util.image;

import net.minecraft.client.texture.NativeImage;


public record Point(int x, int y) {
	public Point {
		if (x < 0 || y < 0) {
			throw new IllegalArgumentException(String.format("Invalid position: %d, %d", x, y));
		}
	}

	public boolean isBlack(NativeImage image) {
		return image.getColor(x, y) == 0xff000000;
	}
}
