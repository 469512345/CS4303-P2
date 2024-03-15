package cs4303.p2.game;

import cs4303.p2.game.entity.Entity;
import cs4303.p2.game.entity.EntityType;
import cs4303.p2.game.powerup.ActivePowerup;
import cs4303.p2.game.powerup.Powerup;
import cs4303.p2.game.powerup.PowerupType;
import cs4303.p2.util.builder.RectBuilder;
import cs4303.p2.util.builder.TextBuilder;
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
		this.drawPowerupsToHUD();
	}

	/**
	 * Draw the powerups to the HUD
	 */
	private void drawPowerupsToHUD() {
		RectBuilder rect = this.game.rect()
			.asHUD()
			.at(this.game.main.width - this.game.main.POWERUP_HUD_WIDTH, this.game.main.POWERUP_HUD_Y)
			.size(this.game.main.POWERUP_HUD_WIDTH, this.game.main.POWERUP_HUD_HEIGHT)
			.strokeWeight(1);
		TextBuilder text = this.game.text(null)
			.asHUD();
		for (ActivePowerup activePowerup : this.activePowerups) {
			PowerupType powerup = activePowerup.powerup;
			rect.fill(powerup.color)
				.stroke(powerup.strokeColor)
				.draw();
			text.text(powerup.text)
				.size(this.game.main.POWERUP_HUD_TEXT_SIZE)
				.fill(this.game.main.POWERUP_TEXT_COLOR)
				.centredHorizontally(
					rect.minX(),
					rect.minY() + this.game.main.POWERUP_HUD_TEXT_PADDING_TOP,
					rect.width()
				)
				.draw();
			text.text(millisToTimeString(activePowerup.remainingMillis))
				.centredHorizontally(
					rect.minX(),
					rect.minY() + this.game.main.POWERUP_HUD_TIMER_PADDING_TOP,
					rect.width()
				)
				.draw();
			rect.translate(0, this.game.main.POWERUP_HUD_SEPARATION);
		}
	}

	private static String millisToTimeString(long millis) {
		long seconds = millis / 1000;
		long minutes = seconds / 60;
		seconds = seconds % 60;
		return zeroPad(minutes) + ":" + zeroPad(seconds);
	}

	private static String zeroPad(long number) {
		return leftPad(String.valueOf(number), "0", 2);
	}

	private static String leftPad(String source, String padding, int minimumLength) {
		StringBuilder sourceBuilder = new StringBuilder(source);
		while (sourceBuilder.length() < minimumLength) {
			sourceBuilder.insert(0, padding);
		}
		source = sourceBuilder.toString();
		return source;
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
			.setMag(this.velocityMagnitude());

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
		if (this.hasPowerUp(PowerupType.SPEED_BOOST)) {
			return this.game.main.PLAYER_MOVEMENT_VELOCITY * 2;
		}

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

	/**
	 * Get whether the player has a powerup of a given type
	 *
	 * @param type type to test
	 *
	 * @return true if the player has a powerup of the given type, false otherwise
	 */
	private boolean hasPowerUp(PowerupType type) {
		return this.activePowerups.stream()
			.anyMatch(p -> p.powerup == type);
	}

	@Override
	public boolean canBeSeenThroughWallsByHumans() {
		return this.hasPowerUp(PowerupType.FAMILY_BEACON);
	}

	/**
	 * Whether the player has the XRay powerup
	 *
	 * @return true if the player has the XRay powerup, false otherwise
	 */
	private boolean hasXRay() {
		return this.hasPowerUp(PowerupType.XRAY);
	}

	@Override
	protected boolean canSeeHumansThroughWalls() {
		return this.hasXRay();
	}

	@Override
	protected boolean canSeeRobotsThroughWalls() {
		return this.hasXRay();
	}
}
