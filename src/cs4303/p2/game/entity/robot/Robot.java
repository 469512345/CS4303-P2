package cs4303.p2.game.entity.robot;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.entity.AIEntity;
import cs4303.p2.game.entity.EntityType;
import processing.core.PVector;

import java.awt.Color;

/**
 * Base class for robots
 */
public abstract class Robot extends AIEntity {

	/**
	 * Whether this robot is dead
	 */
	private boolean dead = false;

	/**
	 * Construct an enemy
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public Robot(GameScreen game, PVector position) {
		super(game, position);
	}

	@Override
	protected EntityType type() {
		return EntityType.ROBOT;
	}

	/**
	 * How many points the player should earn for killing this robot
	 *
	 * @return points earned for killing this robot
	 */
	public abstract int pointsForKilling();

	/**
	 * Mark this robot as killed, and for removal on next update cycle
	 */
	public void kill() {
		this.dead = true;
	}

	/**
	 * Whether this robot is dead, and should be removed from the world
	 * @return true if this robot is dead, false otherwise
	 */
	public boolean dead() {
		return this.dead;
	}

	@Override
	protected Color eyeColor() {
		return Color.RED;
	}
}
