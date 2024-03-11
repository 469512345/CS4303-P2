package cs4303.p2.game.level;

/**
 * Enum representing the two axes for level generation
 */
public enum Axis {
	/**
	 * Horizontal - the x axis
	 */
	HORIZONTAL,
	/**
	 * Vertical - the y axis
	 */
	VERTICAL;

	/**
	 * Switch this axis to the other axis
	 *
	 * @return the other axis
	 */
	public Axis other() {
		return this == HORIZONTAL ? VERTICAL : HORIZONTAL;
	}
}
