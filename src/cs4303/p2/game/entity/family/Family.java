package cs4303.p2.game.entity.family;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.entity.AIEntity;
import cs4303.p2.game.entity.EntityType;
import cs4303.p2.game.entity.ai.TargetSight;
import cs4303.p2.game.entity.ai.Wander;
import cs4303.p2.game.level.Node;
import processing.core.PVector;

import java.awt.Color;
import java.util.ArrayList;

/**
 * A member of the last family
 */
public abstract class Family extends AIEntity {

	private boolean rescued = false;

	/**
	 * Construct a family member
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public Family(GameScreen game, PVector position) {
		super(game, position);
	}

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
		this.rescued = true;
		this.game.addScore(this.pointsForRescuing());
	}

	/**
	 * Mark this family member as killed
	 */
	public void kill() {
		this.rescued = true;
	}

	/**
	 * Whether this family member has been rescued
	 *
	 * @return true if this family member has been rescued
	 */
	public boolean rescued() {
		return this.rescued;
	}

	@Override
	protected Color eyeColor() {
		return Color.GREEN;
	}

	@Override
	public void recalculateGoal() {
		Node closest = this.game.level.closestNodeTo(this.position.x, this.position.y, true);
		this.setGoal(new Wander(closest, new ArrayList<>()));
	}

	@Override
	public void update() {
//		if(this.canSee(this.game.player)) {
//			this.setGoal(new TargetSight(this.game.player, this.game.player.copyPosition()));
//		}
	}
}
