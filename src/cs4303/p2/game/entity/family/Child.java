package cs4303.p2.game.entity.family;

import cs4303.p2.game.GameScreen;
import processing.core.PVector;

import java.awt.Color;

/**
 * Child in the last remaining family
 */
public class Child extends Family {

	/**
	 * Construct a child
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public Child(GameScreen game, PVector position) {
		super(game, position);
	}

	@Override
	public int pointsForRescuing() {
		return 50;
	}

	@Override
	public void draw() {
		this.drawBase();
	}

	@Override
	public void update() {

	}

	@Override
	public Color baseColor() {
		return Color.PINK;
	}

	@Override
	public float eyeRadius() {
		return 1;
	}

	@Override
	public float velocityMagnitude() {
		return 1.2f;
	}

	@Override
	public float radius() {
		return 4;
	}
}
