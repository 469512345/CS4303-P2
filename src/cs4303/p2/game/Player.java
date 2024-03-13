package cs4303.p2.game;

import cs4303.p2.util.collisions.Circle;
import processing.core.PVector;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Player instance
 */
public class Player implements Circle {

	/**
	 * Game instance
	 */
	private final GameScreen game;
	/**
	 * Player's position
	 */
	private final PVector position;
	/**
	 * Player's velocity
	 */
	private final PVector velocity = new PVector();
	/**
	 * Position history
	 */
	private final Queue<PVector> positionHistory = new LinkedList<>();
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
		this.game = game;
		this.position = position;
	}

	/**
	 * Draw the player
	 */
	public void draw() {
		this.game.main
			.ellipse()
			.copy(this)
			.fill(this.game.main.PLAYER_COLOR)
			.draw();
	}

	/**
	 * Update the player's position based on movement
	 */
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

		this.game.moveNoBounce(this.position, this.velocity, this.game.main.PLAYER_RADIUS_SQUARED);

		//Add this new position to the history
		this.positionHistory.add(this.position.copy());
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
	 * Get a copy of the player's current position
	 *
	 * @return copy of the player's current position
	 */
	public PVector copyPosition() {
		return this.position.copy();
	}

	@Override
	public float centreX() {
		return this.position.x;
	}

	@Override
	public float centreY() {
		return this.position.y;
	}

	@Override
	public float radius() {
		return this.game.main.PLAYER_RADIUS;
	}
}
