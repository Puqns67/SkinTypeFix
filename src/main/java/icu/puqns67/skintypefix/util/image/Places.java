package icu.puqns67.skintypefix.util.image;

import java.util.ArrayList;

public class Places {
	public static final Places PLAYER_SKIN_SLIM = Places.slimPlayerSkin();
	public static final Places PLAYER_SKIN_DIFF_SLIM_TO_WILD = Places.diffPlayerSkinSlimToWild();

	private final ArrayList<Square> squares = new ArrayList<>();

	private static Places slimPlayerSkin() {
		var result = new Places();
		result.add(8, 0, 23, 7);
		result.add(0, 8, 31, 15);
		result.add(4, 16, 11, 19);
		result.add(20, 16, 35, 19);
		result.add(44, 16, 51, 19);
		result.add(0, 20, 55, 31);
		result.add(20, 48, 27, 51);
		result.add(36, 48, 43, 51);
		result.add(16, 52, 47, 63);
		return result;
	}

	private static Places diffPlayerSkinSlimToWild() {
		var result = new Places();
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

	public ArrayList<Point> points() {
		var result = new ArrayList<Point>();
		this.squares.forEach(v -> result.addAll(v.points()));
		return result;
	}
}
