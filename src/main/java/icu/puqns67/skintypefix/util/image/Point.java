package icu.puqns67.skintypefix.util.image;

public record Point(int x, int y) {
	public Point {
		if (x < 0 || y < 0) {
			throw new IllegalArgumentException(String.format("Invalid position: %d, %d", x, y));
		}
	}
}
