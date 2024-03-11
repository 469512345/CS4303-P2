package cs4303.p2.game.level;

/**
 * A wall, which is just a simple 1d line
 *
 * @param p1X x coordinate of point 1
 * @param p1Y y coordinate of point 1
 * @param p2X x coordinate of point 2
 * @param p2Y y coordinate of point 2
 */
public record Wall(
	float p1X,
	float p1Y,
	float p2X,
	float p2Y,
	Axis axis
) {

	public float closestDistanceSqFrom(float x, float y) {
		float minX = Math.min(p1X, p2X);
		float maxX = Math.max(p1X, p2X);
		float minY = Math.min(p1Y, p2Y);
		float maxY = Math.max(p1Y, p2Y);

		float closestX;
		float closestY;

		if (this.axis == Axis.VERTICAL) {
			closestX = minX;
			//
			if (y < minY) {
				closestY = minY;
			} else if (y > maxY) {
				closestY = maxY;
			} else {
				closestY = y;
			}
		} else {
			closestY = minY;
			if (x < minX) {
				closestX = minX;
			} else if (x > maxX) {
				closestX = maxX;
			} else {
				closestX = x;
			}
		}

		float diffX = x - closestX;
		float diffY = y - closestY;

		return diffX * diffX + diffY * diffY;
	}

}
