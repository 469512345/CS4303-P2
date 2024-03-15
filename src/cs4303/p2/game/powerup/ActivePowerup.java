package cs4303.p2.game.powerup;

import cs4303.p2.util.annotation.NotNull;

/**
 * An object storing an active powerup on the player, and how long is left on it
 */
public final class ActivePowerup {
	/**
	 * The powerup being applied
	 */
	public final PowerupType powerup;
	/**
	 * How long is left for the powerup
	 */
	public long remainingMillis;

	/**
	 * Create an active powerup record
	 *
	 * @param powerup powerup applied
	 */
	public ActivePowerup(@NotNull PowerupType powerup) {
		this.powerup = powerup;
		this.remainingMillis = powerup.durationMillis;
	}

	/**
	 * Update this powerup by decreasing the time remaining
	 *
	 * @param deltaTime time change since last update
	 */
	public void update(long deltaTime) {
		this.remainingMillis -= deltaTime;
	}

	/**
	 * Whether this powerup is finished (time remaining <= 0 millis)
	 *
	 * @return true if this powerup is finished, false otherwise
	 */
	public boolean finished() {
		return this.remainingMillis <= 0;
	}

}
