package cs4303.p2.game.entity.family;

import cs4303.p2.game.GameScreen;
import processing.core.PVector;

import java.awt.Color;

/**
 * Parent in the last remaining family
 */
public class Parent extends Family {
	/**
	 * Construct a parent
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public Parent(GameScreen game, PVector position) {
		super(game, position);
	}

	@Override
	public void recalculateGoal() {

	}

	@Override
	public int pointsForRescuing() {
		return 100;
	}

	@Override
	public void draw() {
		this.drawBase();
	}

	@Override
	public Color baseColor() {
		return Color.BLUE;
	}

	@Override
	public float eyeRadius() {
		return 2f;
	}

	@Override
	public float velocityMagnitude() {
		return 0.8f;
	}

	@Override
	public float radius() {
		return 8f;
	}
}
