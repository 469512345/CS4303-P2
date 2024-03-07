package cs4303.p2.util.collisions;

/**
 * Interface for something that can be collided into.
 * <p>
 * Current implementations of this are {@link Rectangle} and {@link Circle}.
 */
public interface Collidable {

	/**
	 * Whether the collidable object intersects a circle
	 *
	 * @param circle circle to test
	 *
	 * @return true if the objects intersect, false otherwise
	 */
	boolean intersects(Circle circle);

	/**
	 * Whether the collidable object intersects a rectangle
	 *
	 * @param rectangle rectangle to test
	 *
	 * @return true if the objects intersect, false otherwise
	 */
	boolean intersects(Rectangle rectangle);

	/**
	 * Whether this object contains a point
	 *
	 * @param x x coordinate of point
	 * @param y y coordinate of point
	 *
	 * @return true if this object contains the point inside, or on the line
	 */
	boolean containsPoint(float x, float y);

	/**
	 * Check if two rectangles intersect
	 *
	 * @param r1MinX   min x coordinate of rectangle 1
	 * @param r1MinY   min y coordinate of rectangle 1
	 * @param r1Width  width of rectangle 1
	 * @param r1Height height of rectangle 1
	 * @param r2MinX   min x coordinate of rectangle 2
	 * @param r2MinY   min y coordinate of rectangle 2
	 * @param r2Width  width of rectangle 2
	 * @param r2Height height of rectangle true
	 *
	 * @return true if the rectangles intersect, false otherwise
	 */
	static boolean rectIntersectsRect(
		float r1MinX, float r1MinY, float r1Width, float r1Height,
		float r2MinX, float r2MinY, float r2Width, float r2Height
	) {
		if (r1Width == 0 || r1Height == 0) {
			return false;
		}
		float r1MaxX = r1MinX + r1Width;
		float r1MaxY = r1MinY + r1Height;
		float r2MaxX = r2MinX + r2Width;
		float r2MaxY = r2MinY + r2Height;
		if (r1MinX > r2MaxX || r1MaxX < r2MinX) {
			return false;
		}
		return !(r1MinY > r2MaxY) && !(r1MaxY < r2MinY);
	}

	/**
	 * Check if two circles intersect
	 *
	 * @param c1X x coordinate of centre of circle 1
	 * @param c1Y y coordinate of centre of circle 1
	 * @param c1R radius of circle 1
	 * @param c2X x coordinate of centre of circle 2
	 * @param c2Y y coordinate of centre of circle 2
	 * @param c2R radius of circle 2
	 *
	 * @return true if the circles intersect, false otherwise
	 */
	static boolean circleIntersectsCircle(
		float c1X, float c1Y, float c1R,
		float c2X, float c2Y, float c2R
	) {
		float diffX = c1X - c2X;
		float diffY = c1Y - c2Y;
		return Math.sqrt((diffX * diffX) + (diffY * diffY)) <= (c1R + c2R);
	}

	/**
	 * Check if a circle intersects with a rectangle
	 *
	 * @param cX      x coordinate of centre of circle
	 * @param cY      y coordinate of centre of circle
	 * @param cR      radius of circle
	 * @param rMinX   min x coordinate of rectangle
	 * @param rMinY   min y coordinate of rectangle
	 * @param rWidth  width of rectangle
	 * @param rHeight height of rectangle
	 *
	 * @return true if the circle intersects the rectangle, false otherwise
	 */
	//https://www.geeksforgeeks.org/check-if-any-point-overlaps-the-given-circle-and-rectangle/
	static boolean circleIntersectsRect(
		float cX, float cY, float cR,
		float rMinX, float rMinY, float rWidth, float rHeight
	) {
		float rMaxX = rMinX + rWidth;
		float rMaxY = rMinY + rHeight;
		// Find the nearest point on the
		// rectangle to the centre of
		// the circle
		float Xn = Math.max(rMinX, Math.min(cX, rMaxX));
		float Yn = Math.max(rMinY, Math.min(cY, rMaxY));

		// Find the distance between the
		// nearest point and the centre
		// of the circle
		// Distance between 2 points,
		// (x1, y1) & (x2, y2) in
		// 2D Euclidean space is
		// ((x1-x2)**2 + (y1-y2)**2)**0.5
		float Dx = Xn - cX;
		float Dy = Yn - cY;
		return (Dx * Dx + Dy * Dy) <= cR * cR;
	}

}