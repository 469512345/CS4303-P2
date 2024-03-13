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
	public void draw() {

	}

	@Override
	public void update() {

	}

	@Override
	protected Color baseColor() {
		return null;
	}

	@Override
	protected Color eyeColor() {
		return null;
	}

	@Override
	protected float eyeRadius() {
		return 0;
	}

	@Override
	protected float eyeDistance() {
		return 0;
	}

	@Override
	public float radius() {
		return 0;
	}
}
