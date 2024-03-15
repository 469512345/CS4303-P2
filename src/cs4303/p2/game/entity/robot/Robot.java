package cs4303.p2.game.entity.robot;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.entity.AIEntity;
import cs4303.p2.game.entity.EntityType;
import cs4303.p2.game.entity.family.Family;
import cs4303.p2.util.annotation.NotNull;
import processing.core.PVector;

import java.awt.Color;

/**
 * Base class for robots
 */
public abstract class Robot extends AIEntity {

	/**
	 * Construct an enemy
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public Robot(GameScreen game, PVector position) {
		super(game, position);
	}

	@NotNull
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
	@Override
	public void kill() {
		this.game.addScore(this.pointsForKilling());
		this.remove();
	}

	/**
	 * Kill a family member
	 *
	 * @param family family member to kill
	 */
	public void killFamilyMember(@NotNull Family family) {
		family.kill();
	}

	/**
	 * Mark this robot as killed, but don't increment score
	 */
	@Override
	public void remove() {
		this.active = false;
	}

	@Override
	protected Color eyeColor() {
		return Color.RED;
	}

	@Override
	public boolean canSeeHumansThroughWalls() {
		return true;
	}
}
