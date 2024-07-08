package icu.puqns67.skintypefix.util.image;

import com.mojang.blaze3d.platform.NativeImage;

import java.util.ArrayList;

public class Places {
	public static final Places DIFF_PLAYER_SKIN = Places.diffPlayerSkin();
	private final ArrayList<Square> squares = new ArrayList<>();

	public Places() {
	}

	public static Places diffPlayerSkin() {
		var result = new Places();
		result.add(50, 16, 51, 19);
		result.add(50, 16, 51, 19);
		result.add(54, 20, 55, 31);
		result.add(42, 48, 43, 51);
		result.add(46, 52, 47, 63);
		return result;
	}

	public void add(Square square) {
		this.squares.add(square);
	}

	public void add(int x1, int y1, int x2, int y2) {
		this.add(new Square(x1, y1, x2, y2));
	}

	public boolean hasTransparent(NativeImage image) {
		for (var square : this.squares) {
			if (!square.hasTransparent(image)) {
				return false;
			}
		}
		return true;
	}
}
