package cs4303.p2.util.collisions;

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

	@Override
	default boolean intersects(Circle circle) {
		return Collidable.circleIntersectsRect(
			circle.centreX(), circle.centreY(), circle.radius(),
			this.minX(), this.minY(), this.width(), this.height()
		);
	}

	@Override
	default boolean intersects(Rectangle other) {
		return Collidable.rectIntersectsRect(
			this.minX(), this.minY(), this.width(), this.height(),
			other.minX(), other.minY(), other.width(), other.height()
		);
	}

	@Override
	default boolean intersects(VerticalLine verticalLine) {
		return verticalLine.intersects(this);
	}

	@Override
	default boolean intersects(HorizontalLine horizontalLine) {
		return horizontalLine.intersects(this);
	}

	@Override
	default boolean containsPoint(float x, float y) {
		return x >= this.minX() && y >= this.minY() && x <= this.maxX() && y <= this.maxY();
	}

	default Rectangle intersection(Rectangle rectangle) {
		if(!this.intersects(rectangle)) {
			return null;
		}
		return null; //TODO implement this
	}

	@Override
	default PVector closestPoint(float x, float y) {
		if (this.containsPoint(x, y)) {
			return new PVector(x, y);
		}
		// Find the nearest point on the
		// rectangle to the centre of
		// the circle
		float Xn = Math.max(this.minX(), Math.min(x, this.maxX()));
		float Yn = Math.max(this.minY(), Math.min(y, this.maxY()));
		return new PVector(Xn, Yn);
	}
}