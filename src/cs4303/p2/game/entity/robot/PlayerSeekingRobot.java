package cs4303.p2.game.entity.robot;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.entity.ai.TargetSight;
import cs4303.p2.game.entity.ai.Wander;
import cs4303.p2.game.level.Node;
import processing.core.PVector;

import java.awt.Color;
import java.util.ArrayList;

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
	public void update() {
		if (this.hasLineOfSight(this.game.player) && !(this.goal instanceof TargetSight)) {
			this.setGoal(new TargetSight(this.game.player, this.game.player.copyPosition()));
		} else if (this.goal == null) {
			Node closest = this.game.level.closestNodeTo(this.position.x, this.position.y, true);
			this.setGoal(new Wander(closest, new ArrayList<>()));
		}
		this.reRunAIOrFollowPath();
	}

	@Override
	public void recalculateGoal() {

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
