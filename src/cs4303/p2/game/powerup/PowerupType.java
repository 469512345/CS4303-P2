package cs4303.p2.game.powerup;

import cs4303.p2.game.GameScreen;
import processing.core.PVector;

import java.awt.Color;

/**
 * Types of powerup available
 */
public enum PowerupType {
	/**
	 * Speed boost powerup
	 */
	SPEED_BOOST(
		10_000, //10 seconds
		20,
		20
	) {
		@Override
		public void draw(GameScreen game, PVector position) {
			game.rect()
				.at(position)
				.size(this.width, this.height)
				.fill(Color.RED)
				.draw();
		}
	},
	/**
	 * XRay powerup, allowing the player to see through walls
	 */
	XRAY(
		10_000,
		20,
		20
	) {
		@Override
		public void draw(GameScreen game, PVector position) {

		}
	},
	/**
	 * Beacon powerup, allowing the player to be seen by other family members through walls
	 */
	FAMILY_BEACON(
		10_000,
		20,
		20
	) {
		@Override
		public void draw(GameScreen game, PVector position) {

		}
	};

	/**
	 * Duration in milliseconds
	 */
	public final long durationMillis;
	/**
	 * Width of powerup
	 */
	public final float width;
	/**
	 * Height of powerup
	 */
	public final float height;

	/**
	 * Construct a powerup type
	 *
	 * @param durationMillis duration in milliseconds
	 * @param width          width on screen
	 * @param height         height on screen
	 */
	PowerupType(long durationMillis, float width, float height) {
		this.durationMillis = durationMillis;
		this.width = width;
		this.height = height;
	}

	/**
	 * Draw this powerup on screen in a position
	 *
	 * @param game     game instance to draw to
	 * @param position position of powerup on screen
	 */
	public abstract void draw(GameScreen game, PVector position);
}
