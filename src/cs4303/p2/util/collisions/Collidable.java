package cs4303.p2.util.collisions;

import processing.core.PVector;

/**
 * Interface for something that can be collided into.
 * <p>
 * Current implementations of this are {@link Rectangle}, {@link Circle}, {@link HorizontalLine}, {@link VerticalLine},
 * and {@link Line}.
 * <p>
 * Some of the implementations for {@link Line} have been written with assistance from CHAT-GPT. Such implementations
 * acknowledge where this is the case.
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
	 * Whether the collidable object intersects a vertical line
	 *
	 * @param verticalLine line to test
	 *
	 * @return true if objects intersect, false otherwise
	 */
	boolean intersects(VerticalLine verticalLine);

	/**
	 * Whether the collidable object intersects a horizontal line
	 *
	 * @param horizontalLine line to test
	 *
	 * @return true if objects intersect, false otherwise
	 */
	boolean intersects(HorizontalLine horizontalLine);

	/**
	 * Whether the collidable object intersects a line
	 *
	 * @param line line to test
	 *
	 * @return true if objects intersect, false otherwise
	 */
	boolean intersects(Line line);

	/**
	 * Whether the collidable object intersects another collidable object. This method will inspect the collidable
	 * object to find the appropriate implementation, and error if it is not one of {@link Circle}, {@link Rectangle},
	 * {@link VerticalLine}, {@link HorizontalLine}.
	 *
	 * @param collidable object to test
	 *
	 * @return true if objects intersect, false otherwise
	 */
	default boolean intersects(Collidable collidable) {
		if (collidable instanceof Circle circle) {
			return this.intersects(circle);
		} else if (collidable instanceof Rectangle rectangle) {
			return this.intersects(rectangle);
		} else if (collidable instanceof VerticalLine verticalLine) {
			return this.intersects(verticalLine);
		} else if (collidable instanceof HorizontalLine horizontalLine) {
			return this.intersects(horizontalLine);
		} else if (collidable instanceof Line line) {
			return this.intersects(line);
		} else {
			throw new ClassCastException("Unable to determine the collidable type of " + collidable.getClass());
		}
	}

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
	 * Whether this object contains a point
	 *
	 * @param point point to test
	 *
	 * @return true if this object contains the point inside, or on the line
	 */
	default boolean containsPoint(PVector point) {
		return this.containsPoint(point.x, point.y);
	}

	/**
	 * Calculate the closest point on this shape to a given coordinate
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 *
	 * @return position vector of closest point on shape
	 */
	PVector closestPoint(float x, float y);

	/**
	 * Calculate the closest point on this shape to a given coordinate
	 *
	 * @param point point to test
	 *
	 * @return position vector of closest point on shape
	 */
	default PVector closestPoint(PVector point) {
		return this.closestPoint(point.x, point.y);
	}

	/**
	 * Calculate the distance from the closest point on this shape to a given point
	 *
	 * @param x x coordinate of point
	 * @param y y coordinate of point
	 *
	 * @return distance between the given point, and the closest point on this shape
	 */
	default float closestDistanceFrom(float x, float y) {
		return this.closestPoint(x, y)
			.sub(x, y)
			.mag();
	}

	/**
	 * Calculate the distance from the closest point on this shape to a given point
	 *
	 * @param point point
	 *
	 * @return distance between the given point, and the closest point on this shape
	 */
	default float closestDistanceFrom(PVector point) {
		return this.closestPoint(point)
			.sub(point)
			.mag();
	}

	/**
	 * Calculate the distance squared from the closest point on this shape to a given point
	 *
	 * @param x x coordinate of point
	 * @param y y coordinate of point
	 *
	 * @return distance squared between the given point, and the closest point on this shape
	 */
	default float closestDistanceSqFrom(float x, float y) {
		return this.closestPoint(x, y)
			.sub(x, y)
			.magSq();
	}

	/**
	 * Calculate the distance squared from the closest point on this shape to a given point
	 *
	 * @param point point
	 *
	 * @return distance squared the given point, and the closest point on this shape
	 */
	default float closestDistanceSqFrom(PVector point) {
		return this.closestPoint(point)
			.sub(point)
			.magSq();
	}

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
	 * @param r2Height height of rectangle 2
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
		PVector closestPoint = rectClosestPoint(rMinX, rMinY, rWidth, rHeight, cX, cY);
		return circleContainsPoint(cX, cY, cR, closestPoint.x, closestPoint.y);
	}

	/**
	 * Check if a rectangle intersects with a horizontal line
	 *
	 * @param rMinX   min x coordinate of rectangle
	 * @param rMinY   min y coordinate of rectangle
	 * @param rWidth  width of rectangle
	 * @param rHeight height of rectangle
	 * @param hlMinX  minimum x coordinate of line
	 * @param hlMaxX  maximum x coordinate of line
	 * @param hlY     y coordinate of line
	 *
	 * @return true if the line intersects the rectangle, false otherwise
	 */
	static boolean rectIntersectsHorizontalLine(
		float rMinX, float rMinY, float rWidth, float rHeight,
		float hlMinX, float hlMaxX, float hlY
	) {
		float rMaxX = rMinX + rWidth;
		float rMaxY = rMinY + rHeight;
		return hlY > rMinY &&
			hlY < rMaxY &&
			hlMaxX > rMinX &&
			hlMinX < rMaxX;
	}

	/**
	 * Check if a rectangle intersects a vertical line
	 *
	 * @param rMinX   min x coordinate of rectangle
	 * @param rMinY   min y coordinate of rectangle
	 * @param rWidth  width of rectangle
	 * @param rHeight height of rectangle
	 * @param vlX     x coordinate of vertical line
	 * @param vlMinY  minimum y coordinate of vertical line
	 * @param vlMaxY  maximum y coordinate of vertical line
	 *
	 * @return true if the line intersects the rectangle, false otherwise
	 */
	static boolean rectIntersectsVerticalLine(
		float rMinX, float rMinY, float rWidth, float rHeight,
		float vlX, float vlMinY, float vlMaxY
	) {
		float rMaxX = rMinX + rWidth;
		float rMaxY = rMinY + rHeight;
		return vlX > rMinX &&
			vlX < rMaxX &&
			vlMaxY > rMinY &&
			vlMinY < rMaxY;
	}

	/**
	 * Check if a circle intersects a horizontal line
	 *
	 * @param cX     x coordinate of centre of circle
	 * @param cY     y coordinate of centre of circle
	 * @param cR     radius of circle
	 * @param hlMinX minimum x coordinate of horizontal line
	 * @param hlMaxX maximum x coordinate of horizontal line
	 * @param hlY    y coordinate of horizontal line
	 *
	 * @return true if the line intersects the circle, false otherwise
	 */
	static boolean circleIntersectsHorizontalLine(
		float cX, float cY, float cR,
		float hlMinX, float hlMaxX, float hlY
	) {
		PVector closestPoint = horizontalLineClosestPoint(hlMinX, hlMaxX, hlY, cX, cY);
		return circleContainsPoint(cX, cY, cR, closestPoint.x, closestPoint.y);
	}

	/**
	 * Check if a circle intersects a vertical line
	 *
	 * @param cX     x coordinate of centre of circle
	 * @param cY     y coordinate of centre of circle
	 * @param cR     radius of circle
	 * @param vlX    x coordinate of vertical line
	 * @param vlMinY minimum y coordinate of vertical line
	 * @param vlMaxY maximum y coordinate of vertical line
	 *
	 * @return true if the line intersects the circle, false otherwise
	 */
	static boolean circleIntersectsVerticalLine(
		float cX, float cY, float cR,
		float vlX, float vlMinY, float vlMaxY
	) {
		PVector closestPoint = verticalLineClosestPoint(vlX, vlMinY, vlMaxY, cX, cY);
		return circleContainsPoint(cX, cY, cR, closestPoint.x, closestPoint.y);
	}

	/**
	 * Calculate the closest point on/in a circle to a given point
	 *
	 * @param cX x coordinate of centre of circle
	 * @param cY y coordinate of centre
	 * @param cR radius of circle
	 * @param x  x coordinate of point
	 * @param y  y coordinate of point
	 *
	 * @return position vector of closest point in or on the circle to the provided point
	 */
	static PVector circleClosestPoint(
		float cX, float cY, float cR,
		float x, float y
	) {
		if (circleContainsPoint(cX, cY, cR, x, y)) {
			return new PVector(x, y);
		}
		//Calculate the vector from the centre of the circle to the point
		PVector vector = new PVector(x - cX, y - cY);
		//Set the magnitude of the vector to the radius of the circle
		vector.setMag(cR);
		//Return the circle's centre, translated by the vector
		return vector.add(cX, cY);
	}

	/**
	 * Check if a circle contains a given point
	 *
	 * @param cX x coordinate of centre of circle
	 * @param cY y coordinate of centre of circle
	 * @param cR radius of circle
	 * @param x  x coordinate of point to check
	 * @param y  y coordinate of point to check
	 *
	 * @return true if the point lies in the circle, or on its circumference, false otherwise
	 */
	static boolean circleContainsPoint(
		float cX, float cY, float cR,
		float x, float y
	) {
		float diffX = cX - x;
		float diffY = cY - y;
		return ((diffX * diffX) + (diffY * diffY)) <= (cR * cR);
	}

	/**
	 * Calculate the closest point on a horizontal line to a given point
	 *
	 * @param hlMinX minimum x coordinate of horizontal line
	 * @param hlMaxX maximum x coordinate of horizontal line
	 * @param hlY    y coordinate of horizontal line
	 * @param x      x coordinate of point
	 * @param y      y coordinate of point
	 *
	 * @return position vector of the closest point on the line to the given point
	 */
	static PVector horizontalLineClosestPoint(
		float hlMinX, float hlMaxX, float hlY,
		float x, float y
	) {
		float closestX;
		if (x < hlMinX) {
			closestX = hlMinX;
		} else if (x > hlMaxX) {
			closestX = hlMaxX;
		} else {
			closestX = x;
		}

		return new PVector(closestX, hlY);
	}

	/**
	 * Calculate the closest point on a vertical line to a given point
	 *
	 * @param vlX    x coordinate of vertical line
	 * @param vlMinY minimum y coordinate of vertical line
	 * @param vlMaxY maximum y coordinate of vertical line
	 * @param x      x coordinate of point
	 * @param y      y coordinate of point
	 *
	 * @return position vector of the closest point on the line to the given point
	 */
	static PVector verticalLineClosestPoint(
		float vlX, float vlMinY, float vlMaxY,
		float x, float y
	) {
		float closestY;
		if (y < vlMinY) {
			closestY = vlMinY;
		} else if (y > vlMaxY) {
			closestY = vlMaxY;
		} else {
			closestY = y;
		}
		return new PVector(vlX, closestY);
	}

	/**
	 * Check if a rectangle contains a point
	 *
	 * @param rMinX   minimum x coordinate of rectangle
	 * @param rMinY   minimum y coordinate of rectangle
	 * @param rWidth  width of rectangle
	 * @param rHeight height of rectangle
	 * @param x       x coordinate of point
	 * @param y       y coordinate of point
	 *
	 * @return true if the rectangle contains the point within, or on the perimeter, false otherwise
	 */
	static boolean rectContainsPoint(
		float rMinX, float rMinY, float rWidth, float rHeight,
		float x, float y
	) {
		float rMaxX = rMinX + rWidth;
		float rMaxY = rMinY + rHeight;
		return x >= rMinX && y >= rMinY && x <= rMaxX && y <= rMaxY;
	}

	/**
	 * Check if a horizontal line intersects with another horizontal line
	 *
	 * @param hl1MinX minimum x coordinate of horizontal line 1
	 * @param hl1MaxX maximum x coordinate of horizontal line 1
	 * @param hl1Y    y coordinate of horizontal line 1
	 * @param hl2MinX minimum x coordinate of horizontal line 2
	 * @param hl2MaxX maximum x coordinate of horizontal line 2
	 * @param hl2Y    y coordinate of horizontal line 2
	 *
	 * @return true if the lines intersect, false otherwise
	 */
	static boolean horizontalLineIntersectsHorizontalLine(
		float hl1MinX, float hl1MaxX, float hl1Y,
		float hl2MinX, float hl2MaxX, float hl2Y
	) {
		return hl1Y == hl2Y &&
			hl1MinX < hl2MaxX &&
			hl1MaxX > hl2MinX;
	}

	/**
	 * Check if a horizontal line intersects with a vertical line
	 *
	 * @param hlMinX minimum x coordinate of horizontal line
	 * @param hlMaxX maximum x coordinate of horizontal line
	 * @param hlY    y coordinate of horizontal line
	 * @param vlX    x coordinate of vertical line
	 * @param vlMinY minimum y coordinate of vertical line
	 * @param vlMaxY maximum y coordinate of vertical line
	 *
	 * @return true if the lines intersect, false otherwise
	 */
	static boolean horizontalLineIntersectsVerticalLine(
		float hlMinX, float hlMaxX, float hlY,
		float vlX, float vlMinY, float vlMaxY
	) {
		return vlX > hlMinX &&
			vlX < hlMaxX &&
			hlY > vlMinY &&
			hlY < vlMaxY;
	}

	/**
	 * Check if a vertical line intersects with another vertical line
	 *
	 * @param vl1X    x coordinate of vertical line 1
	 * @param vl1MinY minimum y coordinate of vertical line 1
	 * @param vl1MaxY maximum y coordinate of vertical line 1
	 * @param vl2X    x coordinate of vertical line 2
	 * @param vl2MinY minimum y coordinate of vertical line 2
	 * @param vl2MaxY maximum y coordinate of vertical line 2
	 *
	 * @return true if the lines intersect, false otherwise
	 */
	static boolean verticalLineIntersectsVerticalLine(
		float vl1X, float vl1MinY, float vl1MaxY,
		float vl2X, float vl2MinY, float vl2MaxY
	) {
		return vl1X == vl2X &&
			vl1MinY < vl2MaxY &&
			vl1MaxY > vl2MinY;
	}

	/**
	 * Calculate the intersection between a horizontal and vertical line
	 *
	 * @param hlMinX minimum x coordinate of horizontal line
	 * @param hlMaxX maximum x coordinate of horizontal line
	 * @param hlY    y coordinate of horizontal line
	 * @param vlX    x coordinate of vertical line
	 * @param vlMinY minimum y coordinate of vertical line
	 * @param vlMaxY maximum y coordinate of vertical line
	 *
	 * @return position vector of intersection between the lines, or null if they do not intersect
	 */
	static PVector horizontalLineIntersectionWithVerticalLine(
		float hlMinX, float hlMaxX, float hlY,
		float vlX, float vlMinY, float vlMaxY
	) {
		if (!horizontalLineIntersectsVerticalLine(hlMinX, hlMaxX, hlY, vlX, vlMinY, vlMaxY)) {
			return null;
		}
		return new PVector(vlX, hlY);
	}

	/**
	 * Calculate the closest point on/in a rectangle to a given point
	 *
	 * @param rMinX   minimum x coordinate of rectangle
	 * @param rMinY   minimum y coordinate of rectangle
	 * @param rWidth  width of rectangle
	 * @param rHeight height of rectangle
	 * @param x       x coordinate of point
	 * @param y       y coordinate of point
	 *
	 * @return position vector of point inside or on the perimeter of rectangle
	 */
	static PVector rectClosestPoint(
		float rMinX, float rMinY, float rWidth, float rHeight,
		float x, float y
	) {
		if (rectContainsPoint(rMinX, rMinY, rWidth, rHeight, x, y)) {
			return new PVector(x, y);
		}

		float rMaxX = rMinX + rWidth;
		float rMaxY = rMinY + rHeight;

		float Xn = Math.max(rMinX, Math.min(x, rMaxX));
		float Yn = Math.max(rMinY, Math.min(y, rMaxY));
		return new PVector(Xn, Yn);
	}

	/**
	 * Check if a horizontal line contains a given point
	 *
	 * @param hlMinX minimum x coordinate of horizontal line
	 * @param hlMaxX maximum x coordinate of horizontal line
	 * @param hlY    y coordinate of horizontal line
	 * @param x      x coordinate of point
	 * @param y      y coordinate of point
	 *
	 * @return true if the line contains the point, false otherwise
	 */
	static boolean horizontalLineContainsPoint(
		float hlMinX, float hlMaxX, float hlY,
		float x, float y
	) {
		return hlY == y &&
			x >= hlMinX &&
			x <= hlMaxX;
	}

	/**
	 * Check if a vertical line contains a given point
	 *
	 * @param vlX    x coordinate of vertical line
	 * @param vlMinY minimum y coordinate of vertical line
	 * @param vlMaxY maximum y coordinate of vertical line
	 * @param x      x coordinate of point
	 * @param y      y coordinate of point
	 *
	 * @return true if the line contains the point, false otherwise
	 */
	static boolean verticalLineContainsPoint(
		float vlX, float vlMinY, float vlMaxY,
		float x, float y
	) {
		return vlX == x &&
			y >= vlMinY &&
			y <= vlMaxY;
	}

	/**
	 * Calculate the intersection between two rectangles
	 *
	 * @param r1MinX   min x coordinate of rectangle 1
	 * @param r1MinY   min y coordinate of rectangle 1
	 * @param r1Width  width of rectangle 1
	 * @param r1Height height of rectangle 1
	 * @param r2MinX   min x coordinate of rectangle 2
	 * @param r2MinY   min y coordinate of rectangle 2
	 * @param r2Width  width of rectangle 2
	 * @param r2Height height of rectangle 2
	 *
	 * @return intersection between the rectangles, or null if they do not intersect
	 */
	static Rectangle rectIntersectionWithRect(
		float r1MinX, float r1MinY, float r1Width, float r1Height,
		float r2MinX, float r2MinY, float r2Width, float r2Height
	) {
		float x1 = Math.max(r1MinX, r2MinX);
		float y1 = Math.max(r1MinY, r2MinY);
		float x2 = Math.min(r1MinX + r1Width, r2MinX + r2Width);
		float y2 = Math.min(r1MinY + r1Height, r2MinY + r2Height);

		if (!(x1 < x2) || !(y1 < y2)) {
			return null;
		}
		return Rectangle.of(x1, y1, x2 - x1, y2 - y1);


	}

	//BEGIN CHAT-GPT GENERATED CODE

	/**
	 * Determines if two line segments intersect.
	 * <p>
	 * Written with assistance from CHAT-GPT.
	 *
	 * @param l1X1 x-coordinate of the first endpoint of the first line
	 * @param l1Y1 y-coordinate of the first endpoint of the first line
	 * @param l1X2 x-coordinate of the second endpoint of the first line
	 * @param l1Y2 y-coordinate of the second endpoint of the first line
	 * @param l2X1 x-coordinate of the first endpoint of the second line
	 * @param l2Y1 y-coordinate of the first endpoint of the second line
	 * @param l2X2 x-coordinate of the second endpoint of the second line
	 * @param l2Y2 y-coordinate of the second endpoint of the second line
	 *
	 * @return true if the two lines intersect, false otherwise
	 */
	static boolean lineIntersectsLine(
		float l1X1, float l1Y1, float l1X2, float l1Y2,
		float l2X1, float l2Y1, float l2X2, float l2Y2
	) {
		float denominator = (l1X1 - l1X2) * (l2Y1 - l2Y2) - (l1Y1 - l1Y2) * (l2X1 - l2X2);

		// If the denominator is 0, the lines are parallel and do not intersect
		if (denominator == 0) {
			return false;
		}

		float numeratorT = (l1X1 - l2X1) * (l2Y1 - l2Y2) - (l1Y1 - l2Y1) * (l2X1 - l2X2);
		float numeratorU = (l1X1 - l1X2) * (l1Y1 - l2Y1) - (l1Y1 - l1Y2) * (l1X1 - l2X1);

		float t = numeratorT / denominator;
		float u = numeratorU / denominator;

		// If t and u are between 0 and 1, the lines intersect
		return t >= 0 && t <= 1 && u >= 0 && u <= 1;
	}

	/**
	 * Determines if a circle intersects with a line segment.
	 * <p>
	 * Written with assistance from CHAT-GPT.
	 *
	 * @param cX  x-coordinate of the center of the circle
	 * @param cY  y-coordinate of the center of the circle
	 * @param cR  radius of the circle
	 * @param lX1 x-coordinate of the first endpoint of the line segment
	 * @param lY1 y-coordinate of the first endpoint of the line segment
	 * @param lX2 x-coordinate of the second endpoint of the line segment
	 * @param lY2 y-coordinate of the second endpoint of the line segment
	 *
	 * @return true if the circle intersects with the line segment, false otherwise
	 */
	static boolean circleIntersectsLine(
		float cX, float cY, float cR,
		float lX1, float lY1, float lX2, float lY2
	) {
		PVector closestPoint = lineClosestPoint(lX1, lY1, lX2, lY2, cX, cY);
		return circleContainsPoint(cX, cY, cR, closestPoint.x, closestPoint.y);
	}

	/**
	 * Determines if a rectangle intersects with a line segment.
	 * <p>
	 * Written with assistance from CHAT-GPT.
	 *
	 * @param rMinX   x-coordinate of the top-left corner of the rectangle
	 * @param rMinY   y-coordinate of the top-left corner of the rectangle
	 * @param rWidth  width of the rectangle
	 * @param rHeight height of the rectangle
	 * @param lX1     x-coordinate of the first endpoint of the line segment
	 * @param lY1     y-coordinate of the first endpoint of the line segment
	 * @param lX2     x-coordinate of the second endpoint of the line segment
	 * @param lY2     y-coordinate of the second endpoint of the line segment
	 *
	 * @return true if the rectangle intersects with the line segment, false otherwise
	 */
	static boolean rectangleIntersectsLine(
		float rMinX, float rMinY, float rWidth, float rHeight,
		float lX1, float lY1, float lX2, float lY2
	) {
		return lineIntersectsLine(rMinX, rMinY, rMinX + rWidth, rMinY, lX1, lY1, lX2, lY2) ||
			lineIntersectsLine(rMinX, rMinY, rMinX, rMinY + rHeight, lX1, lY1, lX2, lY2) ||
			lineIntersectsLine(rMinX + rWidth, rMinY, rMinX + rWidth, rMinY + rHeight, lX1, lY1, lX2, lY2) ||
			lineIntersectsLine(rMinX, rMinY + rHeight, rMinX + rWidth, rMinY + rHeight, lX1, lY1, lX2, lY2) ||
			rectContainsPoint(rMinX, rMinY, rWidth, rHeight, lX1, lY1) ||
			rectContainsPoint(rMinX, rMinY, rWidth, rHeight, lX2, lY2);
	}

	/**
	 * Determines if a vertical line segment intersects with another line segment.
	 * <p>
	 * Written with assistance from CHAT-GPT.
	 *
	 * @param vlX    x-coordinate of the vertical line
	 * @param vlMinY y-coordinate of the top endpoint of the vertical line
	 * @param vlMaxY y-coordinate of the bottom endpoint of the vertical line
	 * @param lX1    x-coordinate of the first endpoint of the line segment
	 * @param lY1    y-coordinate of the first endpoint of the line segment
	 * @param lX2    x-coordinate of the second endpoint of the line segment
	 * @param lY2    y-coordinate of the second endpoint of the line segment
	 *
	 * @return true if the vertical line segment intersects with the other line segment, false otherwise
	 */
	static boolean verticalLineIntersectsLine(
		float vlX, float vlMinY, float vlMaxY,
		float lX1, float lY1, float lX2, float lY2
	) {
		return lineIntersectsLine(vlX, vlMinY, vlX, vlMaxY, lX1, lY1, lX2, lY2);
	}

	/**
	 * Determines if a horizontal line segment intersects with another line segment.
	 * <p>
	 * Written with assistance from CHAT-GPT.
	 *
	 * @param hlMinX x-coordinate of the left endpoint of the horizontal line
	 * @param hlMaxX x-coordinate of the right endpoint of the horizontal line
	 * @param hlY    y-coordinate of the horizontal line
	 * @param lX1    x-coordinate of the first endpoint of the line segment
	 * @param lY1    y-coordinate of the first endpoint of the line segment
	 * @param lX2    x-coordinate of the second endpoint of the line segment
	 * @param lY2    y-coordinate of the second endpoint of the line segment
	 *
	 * @return true if the horizontal line segment intersects with the other line segment, false otherwise
	 */
	static boolean horizontalLineIntersectsLine(
		float hlMinX, float hlMaxX, float hlY,
		float lX1, float lY1, float lX2, float lY2
	) {
		return lineIntersectsLine(hlMinX, hlY, hlMaxX, hlY, lX1, lY1, lX2, lY2);
	}

	/**
	 * Finds the closest point on a line segment to a given point.
	 * <p>
	 * Written with assistance from CHAT-GPT.
	 *
	 * @param lX1 x-coordinate of the first endpoint of the line segment
	 * @param lY1 y-coordinate of the first endpoint of the line segment
	 * @param lX2 x-coordinate of the second endpoint of the line segment
	 * @param lY2 y-coordinate of the second endpoint of the line segment
	 * @param x   x-coordinate of the point
	 * @param y   y-coordinate of the point
	 *
	 * @return closest point on the line segment to the given point
	 */
	static PVector lineClosestPoint(
		float lX1, float lY1, float lX2, float lY2,
		float x, float y
	) {
		float dx = lX2 - lX1;
		float dy = lY2 - lY1;
		float t = ((x - lX1) * dx + (y - lY1) * dy) / (dx * dx + dy * dy);
		t = Math.max(0, Math.min(1, t));
		float closestX = lX1 + t * dx;
		float closestY = lY1 + t * dy;
		return new PVector(closestX, closestY);
	}

	/**
	 * Determines if a line segment contains a given point.
	 * <p>
	 * Written with assistance from CHAT-GPT.
	 *
	 * @param lX1 x-coordinate of the first endpoint of the line segment
	 * @param lY1 y-coordinate of the first endpoint of the line segment
	 * @param lX2 x-coordinate of the second endpoint of the line segment
	 * @param lY2 y-coordinate of the second endpoint of the line segment
	 * @param x   x-coordinate of the point
	 * @param y   y-coordinate of the point
	 *
	 * @return true if the line segment contains the point, false otherwise
	 */
	static boolean lineContainsPoint(
		float lX1, float lY1, float lX2, float lY2,
		float x, float y
	) {
		// Convert the line and point coordinates to vectors
		PVector lineVector = new PVector(lX2 - lX1, lY2 - lY1);
		PVector pointVector = new PVector(x - lX1, y - lY1);

		// Calculate the dot product of the line and point vectors
		float dotProduct = lineVector.dot(pointVector);

		// Calculate the length of the line squared
		float lineLengthSquared = lineVector.magSq();

		// Calculate the projection of the point vector onto the line vector
		float projection = dotProduct / lineLengthSquared;

		// Check if the projection is within the range [0, 1]
		return projection >= 0 && projection <= 1;
	}

	//END CHAT-GPT GENERATED CODE

	/**
	 * Finds the intersection point between two line segments.
	 * <p>
	 * This implementation has been based on the method described on <a
	 * href="https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection#Given_two_points_on_each_line_segment">this wikipedia
	 * page</a> (accessed 13/03/2024)
	 *
	 * @param l1X1 x-coordinate of the first endpoint of the first line segment
	 * @param l1Y1 y-coordinate of the first endpoint of the first line segment
	 * @param l1X2 x-coordinate of the second endpoint of the first line segment
	 * @param l1Y2 y-coordinate of the second endpoint of the first line segment
	 * @param l2X1 x-coordinate of the first endpoint of the second line segment
	 * @param l2Y1 y-coordinate of the first endpoint of the second line segment
	 * @param l2X2 x-coordinate of the second endpoint of the second line segment
	 * @param l2Y2 y-coordinate of the second endpoint of the second line segment
	 *
	 * @return a {@link PVector} representing the intersection point between the two line segments, or {@code null} if
	 * they do not intersect
	 */
	static PVector lineIntersectionWithLine(
		float l1X1, float l1Y1, float l1X2, float l1Y2,
		float l2X1, float l2Y1, float l2X2, float l2Y2
	) {
		float denominator = (l1X1 - l1X2) * (l2Y1 - l2Y2) - (l1Y1 - l1Y2) * (l2X1 - l2X2);

		// If the denominator is 0, the lines are parallel and do not intersect
		if (denominator == 0) {
			return null;
		}

		float numeratorT = (l1X1 - l2X1) * (l2Y1 - l2Y2) - (l1Y1 - l2Y1) * (l2X1 - l2X2);
		float numeratorU = (l1X1 - l1X2) * (l1Y1 - l2Y1) - (l1Y1 - l1Y2) * (l1X1 - l2X1);

		float t = numeratorT / denominator;
		float u = numeratorU / denominator;

		// Check if the intersection point is within the line segments
		if (t < 0 || t > 1 || u < 0 || u > 1) {
			// Intersection point is outside the line segments
			return null;
		}
		// Calculate the intersection point
		float intersectionXT = l1X1 + t * (l1X2 - l1X1);
		float intersectionYT = l1Y1 + t * (l1Y2 - l1Y1);

		float intersectionXU = l2X1 + u * (l2X2 - l2X1);
		float intersectionYU = l2Y1 + u * (l2Y2 - l2Y1);

		System.out.printf(
			"l1x1=%f, l1y1=%f, l1x2=%f, l2x2=%f,%nl2x1=%f, l2y1=%f, l2x2=%f, l2y2=%f,%nt=%f, nT=%f, iXT=%f, iYT=%f,%nu=%f, nU=%f, iXU=%f, iYU=%f, denom=%f%n",
			l1X1, l1Y1, l1X2, l1Y2,
			l2X1, l2Y1, l2X2, l2Y2,
			t, numeratorT, intersectionXT, intersectionYT,
			u, numeratorU, intersectionXU, intersectionYU,
			denominator
		);
		return new PVector(intersectionXU, intersectionYU);
	}

}