package cs4303.p2.game.entity.robot;

import cs4303.p2.game.GameScreen;
import processing.core.PVector;

import java.awt.Color;

/**
 * A robot who mutates family members into a {@link MutatedFamilyRobot}.
 */
public class MutatingRobot extends Robot {

	/**
	 * Construct a mutating robot
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public MutatingRobot(GameScreen game, PVector position) {
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
