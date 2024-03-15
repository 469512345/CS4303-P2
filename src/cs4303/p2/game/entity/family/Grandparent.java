package cs4303.p2.game.entity.family;

import cs4303.p2.game.GameScreen;
import processing.core.PVector;

import java.awt.Color;

/**
 * Grandparent in the last remaining family
 */
public class Grandparent extends Family {
	/**
	 * Construct a grandparent
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public Grandparent(GameScreen game, PVector position) {
		super(game, position);
	}

	@Override
	public int pointsForRescuing() {
		return 150;
	}

	@Override
	public void draw() {
		this.drawBase();
	}

	@Override
	public Color baseColor() {
		return Color.DARK_GRAY;
	}

	@Override
	public float eyeRadius() {
		return 2;
	}

	@Override
	public float velocityMagnitude() {
		return 0.4f;
	}

	@Override
	public float radius() {
		return 10f;
	}
}
