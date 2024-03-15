package cs4303.p2.game.entity.robot;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.entity.family.Family;
import processing.core.PVector;

import java.awt.Color;

/**
 * A robot who seeks out members of the last remaining family
 */
public class FamilySeekingRobot extends Robot {
	/**
	 * Construct a family seeking robot
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public FamilySeekingRobot(GameScreen game, PVector position) {
		super(game, position);
	}

	@Override
	public void recalculateGoal() {
		Family familyMember = this.nearestKnowsLocation(this.game.level.family);
		if (familyMember != null) {
			this.target(familyMember);
		} else {
			if(this.hasLineOfSight(this.game.player)) {
				this.target(this.game.player);
			} else {
				this.wander();
			}
		}
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
	public Color baseColor() {
		return Color.YELLOW;
	}

	@Override
	public float eyeRadius() {
		return 2f;
	}

	@Override
	public float velocityMagnitude() {
		return 0.9f;
	}

	@Override
	public float radius() {
		return 6f;
	}
}
