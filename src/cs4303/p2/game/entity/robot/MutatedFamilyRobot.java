package cs4303.p2.game.entity.robot;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.entity.family.Family;
import processing.core.PVector;

import java.awt.Color;

/**
 * A mutated family member
 */
public class MutatedFamilyRobot extends Robot {

	/**
	 * The family member that was mutated into this robot
	 */
	private final Family familyMember;

	/**
	 * Construct a mutated family member
	 *
	 * @param game     game instance
	 * @param position initial position
	 * @param family   the family member that was mutated into this robot
	 */
	public MutatedFamilyRobot(GameScreen game, PVector position, Family family) {
		super(game, position);
		this.familyMember = family;
	}

	@Override
	public int pointsForKilling() {
		return 150;
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
		return this.familyMember.baseColor();
	}

	@Override
	public float eyeRadius() {
		return this.familyMember.eyeRadius();
	}

	@Override
	public float velocityMagnitude() {
		return this.familyMember.velocityMagnitude();
	}

	@Override
	public float radius() {
		return this.familyMember.radius();
	}
}
