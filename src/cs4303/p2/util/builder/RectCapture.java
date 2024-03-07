package cs4303.p2.util.builder;

import cs4303.p2.util.collisions.Rectangle;

/**
 * Immutable capture of a rectangle
 */
public class RectCapture implements Rectangle {

	/**
	 * Min x coord
	 */
	private final float minX;
	/**
	 * Min y coord
	 */
	private final float minY;
	/**
	 * Width of rectangle
	 */
	private final float width;
	/**
	 * Height of rectangle
	 */
	private final float height;

	/**
	 * Capture a rectangle
	 *
	 * @param minX   min x
	 * @param minY   min y
	 * @param width  width
	 * @param height height
	 */
	RectCapture(float minX, float minY, float width, float height) {
		this.minX = minX;
		this.minY = minY;
		this.width = width;
		this.height = height;
	}

	@Override
	public float minX() {
		return this.minX;
	}

	@Override
	public float minY() {
		return this.minY;
	}

	@Override
	public float width() {
		return this.width;
	}

	@Override
	public float height() {
		return this.height;
	}
}
