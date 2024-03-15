package cs4303.p2.util.collisions;

import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.annotation.Nullable;
import processing.core.PVector;

/**
 * This interface represents a collidable object that can be represented as an axis-aligned rectangle
 */
public interface Rectangle extends Collidable {

	/**
	 * X coordinate of the top left corner of the rectangle (min x)
	 *
	 * @return x coordinate of the top left corner of the rectangle (min x)
	 */
	float minX();

	/**
	 * Y coordinate of the top left corner of the rectangle (min y)
	 *
	 * @return y coordinate of the top left corner of the rectangle (min y)
	 */
	float minY();

	/**
	 * Width of the rectangle
	 *
	 * @return width of the rectangle
	 */
	float width();

	/**
	 * Height of the rectangle
	 *
	 * @return height of the rectangle
	 */
	float height();

	/**
	 * X coordinate of bottom right corner of the rectangle (max x)
	 *
	 * @return x coordinate of bottom right corner of the rectangle (max x)
	 */
	default float maxX() {
		return this.minX() + this.width();
	}

	/**
	 * Y coordinate of bottom right corner of the rectangle (max y)
	 *
	 * @return y coordinate of bottom right corner of the rectangle (max y)
	 */
	default float maxY() {
		return this.minY() + this.height();
	}

	/**
	 * Calculate the x coordinate of the centre of the rectangle
	 *
	 * @return centre x coordinate
	 */
	default float centreX() {
		return this.minX() + (this.width() / 2);
	}

	/**
	 * Calculate the y coordinate of the centre of the rectangle
	 *
	 * @return centre y coordinate
	 */
	default float centreY() {
		return this.minY() + (this.height() / 2);
	}

	/**
	 * Calculate the position vector of the centre of the rectangle
	 *
	 * @return position vector of the centre of the rectangle
	 */
	@NotNull
	default PVector centre() {
		return new PVector(this.centreX(), this.centreY());
	}

	/**
	 * Create a copy of this rectangle, but with a margin added to the shape. A negative margin will make the shape
	 * smaller.
	 *
	 * @param margin margin to extend the shape by
	 *
	 * @return copy of this rectangle with margin applied
	 */
	@NotNull
	default Rectangle withMargin(float margin) {
		return Rectangle.of(
			this.minX() - margin,
			this.minY() - margin,
			this.width() + 2 * margin,
			this.height() + 2 * margin
		);
	}

	@Override
	default boolean intersects(@NotNull Circle circle) {
		return Collidable.circleIntersectsRect(
			circle.centreX(), circle.centreY(), circle.radius(),
			this.minX(), this.minY(), this.width(), this.height()
		);
	}

	@Override
	default boolean intersects(@NotNull Rectangle other) {
		return Collidable.rectIntersectsRect(
			this.minX(), this.minY(), this.width(), this.height(),
			other.minX(), other.minY(), other.width(), other.height()
		);
	}

	@Override
	default boolean intersects(@NotNull VerticalLine verticalLine) {
		return Collidable.rectIntersectsVerticalLine(
			this.minX(), this.minY(), this.width(), this.height(),
			verticalLine.x(), verticalLine.minY(), verticalLine.maxY()
		);
	}

	@Override
	default boolean intersects(@NotNull HorizontalLine horizontalLine) {
		return Collidable.rectIntersectsHorizontalLine(
			this.minX(), this.minY(), this.width(), this.height(),
			horizontalLine.minX(), horizontalLine.maxX(), horizontalLine.y()
		);
	}

	@Override
	default boolean intersects(@NotNull Line line) {
		return Collidable.rectangleIntersectsLine(
			this.minX(), this.minY(), this.width(), this.height(),
			line.x1(), line.y1(), line.x2(), line.y2()
		);
	}

	@Override
	default boolean containsPoint(float x, float y) {
		return Collidable.rectContainsPoint(
			this.minX(), this.minY(), this.width(), this.height(),
			x, y
		);
	}

	@NotNull
	@Override
	default PVector closestPoint(float x, float y) {
		return Collidable.rectClosestPoint(
			this.minX(), this.minY(), this.width(), this.height(),
			x, y
		);
	}

	/**
	 * Calculate the intersection between this rectangle and another
	 *
	 * @param other other rectangle
	 *
	 * @return new rectangle representing the intersection, or null if the rectangles do not intersect
	 */
	@Nullable
	default Rectangle intersection(@NotNull Rectangle other) {
		return Collidable.rectIntersectionWithRect(
			this.minX(), this.minY(), this.width(), this.height(),
			other.minX(), other.minY(), other.width(), other.height()
		);
	}

	/**
	 * Get the top edge of this rectangle as a horizontal line
	 *
	 * @return top edge of this rectangle
	 */
	@NotNull
	default HorizontalLine topEdge() {
		return HorizontalLine.of(this.minX(), this.maxX(), this.minY());
	}

	/**
	 * Get the bottom edge of this rectangle as a horizontal line
	 *
	 * @return bottom edge of this rectangle
	 */
	@NotNull
	default HorizontalLine bottomEdge() {
		return HorizontalLine.of(this.minX(), this.maxX(), this.maxY());
	}

	/**
	 * Get the left edge of this rectangle as a vertical line
	 *
	 * @return left edge of this rectangle
	 */
	@NotNull
	default VerticalLine leftEdge() {
		return VerticalLine.of(this.minX(), this.minY(), this.maxY());
	}

	/**
	 * Get the right edge of this rectangle as a vertical line
	 *
	 * @return right edge of this rectangle
	 */
	@NotNull
	default VerticalLine rightEdge() {
		return VerticalLine.of(this.maxX(), this.minY(), this.maxY());
	}

	/**
	 * Create a rectangle from 2 points. This will determine the minimum x and y coordinate from the two points.
	 *
	 * @param x1 x coordinate of point1
	 * @param y1 y coordinate of point1
	 * @param x2 x coordinate of point2
	 * @param y2 y coordinate of point2
	 *
	 * @return rectangle instance between the two points
	 */
	@NotNull
	static Rectangle fromPoints(float x1, float y1, float x2, float y2) {
		float minX = Math.min(x1, x2);
		float maxX = Math.max(x1, x2);

		float minY = Math.min(y1, y2);
		float maxY = Math.max(y1, y2);

		float width = maxX - minX;
		float height = maxY - minY;

		return new RectangleImpl(minX, minY, width, height);
	}

	/**
	 * Create a rectangle with the given parameters
	 *
	 * @param minX   minimum x coordinate of the rectangle
	 * @param minY   minimum y coordinate of the rectangle
	 * @param width  width of the rectangle
	 * @param height height of the rectangle
	 *
	 * @return rectangle instance with the given parameters
	 */
	@NotNull
	static Rectangle of(float minX, float minY, float width, float height) {
		return new RectangleImpl(minX, minY, width, height);
	}

	/**
	 * Rectangle record implementation
	 *
	 * @param minX   Min x coord
	 * @param minY   Min y coord
	 * @param width  Width of rectangle
	 * @param height Height of rectangle
	 */
	record RectangleImpl(
		float minX,
		float minY,
		float width,
		float height
	) implements Rectangle {


	}
}