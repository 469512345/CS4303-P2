package cs4303.p2.util.collisions;

import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.annotation.Nullable;
import processing.core.PVector;

/**
 * This interface represents an arbitrary line between two points
 */
public interface Line extends Collidable {
	/**
	 * X coordinate of point 1
	 *
	 * @return x coordinate of point 1
	 */
	float x1();

	/**
	 * Y coordinate of point 1
	 *
	 * @return y coordinate of point1
	 */
	float y1();

	/**
	 * X coordinate of point 2
	 *
	 * @return x coordinate of point2
	 */
	float x2();

	/**
	 * Y coordinate of point 2
	 *
	 * @return y coordinate of point 2
	 */
	float y2();

	/**
	 * Determines if this line intersects with another line.
	 *
	 * @param other the other line to check for intersection
	 *
	 * @return true if the lines intersect, false otherwise
	 */
	@Override
	default boolean intersects(@NotNull Line other) {
		return Collidable.lineIntersectsLine(
			this.x1(), this.y1(), this.x2(), this.y2(),
			other.x1(), other.y1(), other.x2(), other.y2()
		);
	}

	@Override
	default boolean intersects(@NotNull Circle circle) {
		return Collidable.circleIntersectsLine(
			circle.centreX(), circle.centreY(), circle.radius(),
			this.x1(), this.y1(), this.x2(), this.y2()
		);
	}

	@Override
	default boolean intersects(@NotNull Rectangle rectangle) {
		return Collidable.rectangleIntersectsLine(
			rectangle.minX(), rectangle.minY(), rectangle.width(), rectangle.height(),
			this.x1(), this.y1(), this.x2(), this.y2()
		);
	}

	@Override
	default boolean intersects(@NotNull VerticalLine verticalLine) {
		return Collidable.verticalLineIntersectsLine(
			verticalLine.x(), verticalLine.minY(), verticalLine.maxY(),
			this.x1(), this.y1(), this.x2(), this.y2()
		);
	}

	@Override
	default boolean intersects(@NotNull HorizontalLine horizontalLine) {
		return Collidable.horizontalLineIntersectsLine(
			horizontalLine.minX(), horizontalLine.maxX(), horizontalLine.y(),
			this.x1(), this.y1(), this.x2(), this.y2()
		);
	}

	@Override
	default boolean containsPoint(float x, float y) {
		return Collidable.lineContainsPoint(
			this.x1(), this.y1(), this.x2(), this.y2(),
			x, y
		);
	}

	@NotNull
	@Override
	default PVector closestPoint(float x, float y) {
		return Collidable.lineClosestPoint(
			this.x1(), this.y1(), this.x2(), this.y2(),
			x, y
		);
	}

	/**
	 * Calculate the intersection between this line and another line.
	 *
	 * @param line line to calculate intersection with
	 *
	 * @return point of intersection between this line and the other line, or null if none exists
	 */
	@Nullable
	default PVector intersection(@NotNull Line line) {
		return Collidable.lineIntersectionWithLine(
			this.x1(), this.y1(), this.x2(), this.y2(),
			line.x1(), line.y1(), line.x2(), line.y2()
		);
	}

	/**
	 * Calculate the intersection between this line and a horizontal line
	 *
	 * @param horizontalLine horizontal line to calculate intersection with
	 *
	 * @return point of intersection between this line and the horizontal line, or null if none exists
	 */
	@Nullable
	default PVector intersection(@NotNull HorizontalLine horizontalLine) {
		return Collidable.lineIntersectionWithLine(
			this.x1(), this.y1(), this.x2(), this.y2(),
			horizontalLine.minX(), horizontalLine.y(), horizontalLine.maxX(), horizontalLine.y()
		);
	}

	/**
	 * Calculate the intersection between this line and a vertical line
	 *
	 * @param verticalLine vertical line to calculate intersection with
	 *
	 * @return point of intersection between this line and the vertical line, or null if none exists
	 */
	@Nullable
	default PVector intersection(@NotNull VerticalLine verticalLine) {
		return Collidable.lineIntersectionWithLine(
			this.x1(), this.y1(), this.x2(), this.y2(),
			verticalLine.x(), verticalLine.minY(), verticalLine.x(), verticalLine.maxY()
		);
	}

	/**
	 * Create a new line between two points
	 *
	 * @param x1 x coordinate of point 1
	 * @param y1 y coordinate of point 1
	 * @param x2 x coordinate of point 2
	 * @param y2 y coordinate of point 2
	 *
	 * @return new line instance between the two points
	 */
	@NotNull
	static Line of(float x1, float y1, float x2, float y2) {
		return new LineImpl(x1, y1, x2, y2);
	}

	/**
	 * Create a new line between two points
	 *
	 * @param p1 position vector of point 1
	 * @param p2 position vector of point 2
	 *
	 * @return new line instances between the two points
	 */
	@NotNull
	static Line of(@NotNull PVector p1, @NotNull PVector p2) {
		return new LineImpl(p1.x, p1.y, p2.x, p2.y);
	}

	/**
	 * Record implementation of line
	 *
	 * @param x1 x coordinate of point 1
	 * @param y1 y coordinate of point 1
	 * @param x2 x coordinate of point 2
	 * @param y2 y coordinate of point 2
	 */
	record LineImpl(
		float x1,
		float y1,
		float x2,
		float y2
	) implements Line {
	}
}
