package cs4303.p2.game;

import cs4303.p2.game.entity.Entity;
import cs4303.p2.game.entity.EntityType;
import cs4303.p2.game.powerup.ActivePowerup;
import cs4303.p2.game.powerup.Powerup;
import processing.core.PVector;

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Player instance
 */
public final class Player extends Entity implements ProjectileSource {

	/**
	 * Position history
	 */
	private final Queue<PVector> positionHistory = new LinkedList<>();
	/**
	 * Active powerups on the player
	 */
	public final LinkedList<ActivePowerup> activePowerups = new LinkedList<>();
	/**
	 * Whether the player should move left on next update
	 */
	public boolean left = false;
	/**
	 * Whether the player should move right on next update
	 */
	public boolean right = false;
	/**
	 * Whether the player should move up on next update
	 */
	public boolean up = false;
	/**
	 * Whether the player should move down on next update
	 */
	public boolean down = false;

	/**
	 * Construct a player
	 *
	 * @param game     game instance
	 * @param position initial position
	 */
	public Player(GameScreen game, PVector position) {
		super(game, position);
	}

	@Override
	public void draw() {
		this.drawBase();
	}

	@Override
	public void update() {
		if (this.positionHistory.size() > this.game.main.CAMERA_LAG_FRAMES) {
			this.positionHistory.poll();
		}
		//Initial velocity of 0
		float velocityX = 0;
		float velocityY = 0;

		//Each key down will add a component to velocity
		if (this.left) {
			velocityX = -1;
		}
		if (this.right) {
			velocityX = 1;
		}
		if (this.up) {
			velocityY = -1;
		}
		if (this.down) {
			velocityY = 1;
		}

		//Set the velocity
		this.velocity.set(velocityX, velocityY)
			//Set the magnitude if non-zero. stops diagonal movement getting sqrt(2) * speed
			.setMag(this.game.main.PLAYER_MOVEMENT_VELOCITY);

		this.move();

		//Add this new position to the history
		this.positionHistory.add(this.position.copy());
		this.updatePowerups();
	}

	/**
	 * Update the powerups on the player
	 */
	private void updatePowerups() {
		Iterator<ActivePowerup> iterator = this.activePowerups.iterator();
		while (iterator.hasNext()) {
			ActivePowerup powerup = iterator.next();
			powerup.update(this.game.deltaTime);
			if (powerup.finished()) {
				iterator.remove();
			}
		}
	}

	/**
	 * Get the lagged position of the player, for the camera to display.
	 *
	 * @return lagged position
	 */
	public PVector laggedPosition() {
		return this.positionHistory.peek();
	}

	/**
	 * Respawn the player in a position
	 *
	 * @param position position to respawn
	 */
	public void respawn(PVector position) {
		//Move the player, and clear the position history
		this.position.set(position);
		this.positionHistory.clear();
		this.positionHistory.add(position);
		//Ignore any movement that was present when the player died
		this.up = false;
		this.down = false;
		this.left = false;
		this.right = false;
		//Clear any active powerups
		this.activePowerups.clear();
	}

	/**
	 * Add a powerup to the player
	 *
	 * @param powerup powerup to apply to the player
	 */
	public void applyPowerup(Powerup powerup) {
		this.activePowerups.add(new ActivePowerup(powerup.type));
		powerup.remove();
	}

	@Override
	public float radius() {
		return this.game.main.PLAYER_RADIUS;
	}

	@Override
	public Color baseColor() {
		return this.game.main.PLAYER_COLOR;
	}

	@Override
	protected Color eyeColor() {
		return this.game.main.PLAYER_EYE_COLOR;
	}

	@Override
	public float eyeRadius() {
		return this.game.main.PLAYER_EYE_RADIUS;
	}

	@Override
	public float velocityMagnitude() {
		return this.game.main.PLAYER_MOVEMENT_VELOCITY;
	}

	@Override
	protected EntityType type() {
		return EntityType.HUMAN;
	}

	@Override
	public void kill() {
		//Do nothing
	}

	@Override
	public void remove() {
		//Do nothing
	}

	@Override
	public boolean isActive() {
		return true;
	}
}
