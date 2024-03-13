package cs4303.p2.game.entity.robot;

import cs4303.p2.game.GameScreen;
import processing.core.PVector;

import java.awt.Color;

/**
 * A mutated family member
 */
public class MutatedFamilyRobot extends Robot {
	/**
	 * Construct a mutated family member
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public MutatedFamilyRobot(GameScreen game, PVector position) {
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
