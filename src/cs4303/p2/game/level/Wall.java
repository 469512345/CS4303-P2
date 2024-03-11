package cs4303.p2.game.level;

/**
 * A wall, which is just a simple 1d line. For ease, this will normally be aligned to the x or y axis.
 *
 * @param minX x coordinate of point 1
 * @param minY y coordinate of point 1
 * @param maxX x coordinate of point 2
 * @param maxY y coordinate of point 2
 */
public record Wall(
	float minX,
	float minY,
	float maxX,
	float maxY,
	Axis axis
) {

	/**
	 * Calculate the closest distance squared between a point and a wall
	 *
	 * @param x x coordinate of point
	 * @param y y coordinate of point
	 *
	 * @return distance squared from the point to the closest point on the line
	 */
	public float closestDistanceSqFrom(float x, float y) {
		float closestX;
		float closestY;

		if (this.axis == Axis.VERTICAL) {
			closestX = this.minX;
			//
			if (y < this.minY) {
				closestY = this.minY;
			} else if (y > this.maxY) {
				closestY = this.maxY;
			} else {
				closestY = y;
			}
		} else {
			closestY = this.minY;
			if (x < this.minX) {
				closestX = this.minX;
			} else if (x > this.maxX) {
				closestX = this.maxX;
			} else {
				closestX = x;
			}
		}

		float diffX = x - closestX;
		float diffY = y - closestY;

		return diffX * diffX + diffY * diffY;
	}

}
