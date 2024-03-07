package cs4303.p2.game.level;

/**
 * Boolean blindness go brr
 */
public enum Axis {
	//Horizontal - the x axis
	HORIZONTAL,
	//Vertical - the y axis
	VERTICAL;

	public Axis other() {
		return this == HORIZONTAL ? VERTICAL : HORIZONTAL;
	}
}
