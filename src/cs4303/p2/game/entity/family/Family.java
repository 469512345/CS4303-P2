package cs4303.p2.game.entity.family;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.entity.AIEntity;
import cs4303.p2.game.entity.EntityType;
import cs4303.p2.game.entity.robot.Robot;
import cs4303.p2.util.annotation.NotNull;
import processing.core.PVector;

import java.awt.Color;

/**
 * A member of the last family
 */
public abstract class Family extends AIEntity {

	/**
	 * Construct a family member
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public Family(GameScreen game, PVector position) {
		super(game, position);
	}

	@NotNull
	@Override
	protected EntityType type() {
		return EntityType.HUMAN;
	}

	/**
	 * How many points the player should earn for rescuing this family member
	 *
	 * @return points earned for rescuing this family member
	 */
	public abstract int pointsForRescuing();

	/**
	 * Mark this family member as rescued
	 */
	public void rescue() {
		this.active = false;
		this.game.addScore(this.pointsForRescuing());
	}

	/**
	 * Mark this family member as killed
	 */
	@Override
	public void kill() {
		this.active = false;
	}

	@Override
	public void remove() {
		this.active = false;
	}

	@Override
	protected Color eyeColor() {
		return Color.GREEN;
	}

	@Override
	public void recalculateGoal() {
		Robot nearestRobot = this.nearestInLineOfSight(this.game.level.robots);
		boolean canSeePlayer = this.knowsLocationOf(this.game.player);
		boolean canSeeEnemy = nearestRobot != null;
		if (canSeePlayer) {
			if (canSeeEnemy) {
				float nearestDistance = this.distanceTo(nearestRobot);
				if (nearestDistance < this.distanceTo(this.game.player)) {
					this.fleeFrom(nearestRobot);
				} else {
					this.target(this.game.player);
				}
			} else {
				this.target(this.game.player);
			}
		} else {
			if (canSeeEnemy) {
				this.fleeFrom(nearestRobot);
			} else {
				this.wander();
			}
		}
	}


}
