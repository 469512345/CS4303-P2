package cs4303.p2.game.entity.robot;

import cs4303.p2.game.GameScreen;
import processing.core.PVector;

import java.awt.Color;

/**
 * Robot who seeks the player
 */
public class PlayerSeekingRobot extends Robot {
	/**
	 * Construct a player seeking robot
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public PlayerSeekingRobot(GameScreen game, PVector position) {
		super(game, position);
	}

	@Override
	public int pointsForKilling() {
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
		return Color.MAGENTA;
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
