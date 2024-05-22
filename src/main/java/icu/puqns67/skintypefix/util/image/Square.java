package icu.puqns67.skintypefix.util.image;

import net.minecraft.client.texture.NativeImage;

import java.util.ArrayList;


public record Square(Point p1, Point p2) {
	public Square {
		// The point close to (0,0) is p1
		if (p2.x() < p1.x() || p1.x() == p2.x() && p2.y() < p1.y()) {
			var tmp = p1;
			p1 = p2;
			p2 = tmp;
		}
	}

	public Square(int x1, int y1, int x2, int y2) {
		this(new Point(x1, y1), new Point(x2, y2));
	}

	public ArrayList<Point> points() {
		var result = new ArrayList<Point>();
		for (var Y = this.p1.y(); Y <= this.p2.y(); Y++) {
			for (var X = this.p1.x(); X <= this.p2.x(); X++) {
				result.add(new Point(X, Y));
			}
		}
		return result;
	}

	public boolean isAllBlack(NativeImage image) {
		for (var point : this.points()) {
			if (!point.isBlack(image)) {
				return false;
			}
		}
		return true;
	}
}
