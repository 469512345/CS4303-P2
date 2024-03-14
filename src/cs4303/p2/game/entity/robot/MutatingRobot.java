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
	public int pointsForKilling() {
		return 100;
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
		return Color.BLACK;
	}

	@Override
	public float eyeRadius() {
		return 1;
	}

	@Override
	public float velocityMagnitude() {
		return 1.0f;
	}

	@Override
	public float radius() {
		return 4;
	}
}
