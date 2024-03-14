package cs4303.p2.game.entity.family;

import cs4303.p2.game.GameScreen;
import cs4303.p2.game.entity.AIEntity;
import processing.core.PVector;

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
	}

	/**
	 * Whether this family member has been rescued
	 *
	 * @return true if this family member has been rescued
	 */
	public boolean rescued() {
		return this.rescued;
	}
}
