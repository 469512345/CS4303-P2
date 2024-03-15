package cs4303.p2.game.powerup;

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
		Color.GREEN,
		Color.LIGHT_GRAY,
		"Speed"
	),
	/**
	 * XRay powerup, allowing the player to see through walls
	 */
	XRAY(
		10_000,
		Color.CYAN,
		Color.BLUE,
		"XRay"
	),
	/**
	 * Beacon powerup, allowing the player to be seen by other family members through walls
	 */
	FAMILY_BEACON(
		10_000,
		Color.YELLOW,
		Color.ORANGE,
		"Beacon"
	);

	/**
	 * Duration in milliseconds
	 */
	public final long durationMillis;
	/**
	 * fill colour
	 */
	public final Color color;
	/**
	 * line colour
	 */
	public final Color strokeColor;
	/**
	 * text in powerup
	 */
	public final String text;

	/**
	 * Construct a powerup type
	 *
	 * @param color          fillColor of powerup
	 * @param strokeColor    line color of powerup
	 * @param durationMillis duration in milliseconds
	 * @param text           text on powerup
	 */

	PowerupType(long durationMillis, Color color, Color strokeColor, String text) {
		this.durationMillis = durationMillis;
		this.color = color;
		this.strokeColor = strokeColor;
		this.text = text;
	}
}
