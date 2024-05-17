package cs4303.p2.game.entity.robot;

import cs4303.p2.game.entity.family.Family;
import cs4303.p2.util.annotation.NotNull;

import java.awt.Color;

/**
 * A mutated family member
 */
public class MutatedFamilyRobot extends Robot {

	/**
	 * The family member that was mutated into this robot
	 */
	@NotNull
	private final Family familyMember;

	/**
	 * Construct a mutated family member
	 *
	 * @param family the family member that was mutated into this robot
	 */
	public MutatedFamilyRobot(@NotNull Family family) {
		super(family.game, family.position);
		this.familyMember = family;
	}

	@Override
	public int pointsForKilling() {
		return 150;
	}

	@Override
	public void kill() {
		super.kill();
		this.game.level.family.add(this.familyMember);
		this.familyMember.recalculateGoal();
	}

	@Override
	public void draw() {
		this.drawBase();
	}

	@Override
	public Color baseColor() {
		return this.familyMember.baseColor();
	}

	@Override
	public float eyeRadius() {
		return this.familyMember.eyeRadius();
	}

	@Override
	public float velocityMagnitude() {
		return this.familyMember.velocityMagnitude() * 2;
	}

	@Override
	public float radius() {
		return this.familyMember.radius();
	}

	@Override
	public void recalculateGoal() {
		if (this.knowsLocationOf(this.game.player)) {
			this.target(this.game.player);
		} else {
			this.wander();
		}
	}

	@Override
	public boolean canSeeHumansThroughWalls() {
		return false;
	}
}
