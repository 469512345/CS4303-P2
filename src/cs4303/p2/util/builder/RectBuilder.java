package cs4303.p2.util.builder;

import cs4303.p2.Main;
import cs4303.p2.util.collisions.Rectangle;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.Color;

/**
 * Utility builder class for rendering a rectangle on the screen
 */
public final class RectBuilder implements Rectangle {

	/**
	 * GameScreen instance
	 */
	private final PApplet app;

	/**
	 * X coordinate - top left hand corner of the rectangle (min X)
	 */
	private float positionX;
	/**
	 * Y coordinate - top left hand corner of the rectangle (min Y).
	 */
	private float positionY;
	/**
	 * Width of rectangle
	 */
	private float width;
	/**
	 * Height of rectangle
	 */
	private float height;
	/**
	 * Radius of top left corner
	 */
	private float topLeftRadius;
	/**
	 * Radius of top right corner
	 */
	private float topRightRadius;
	/**
	 * Radius of bottom left corner
	 */
	private float bottomLeftRadius;
	/**
	 * Radius of bottom right corner
	 */
	private float bottomRightRadius;
	/**
	 * Fill color (or null for no fill)
	 *
	 * @see PApplet#fill(float, float, float, float)
	 */
	private Integer fillColor;
	/**
	 * Stroke (outline) colour (or null for no outline)
	 *
	 * @see PApplet#stroke(float, float, float, float)
	 */
	private Integer strokeColor;
	/**
	 * Stroke (outline) weight
	 *
	 * @see PApplet#strokeWeight(float)
	 */
	private float strokeWeight;

	/**
	 * Create a new RectBuilder bound to a game
	 *
	 * @param app game instance
	 */
	public RectBuilder(Main app) {
		this.app = app;
	}

	/**
	 * Set the fill colour of the rectangle
	 *
	 * @param fillColor fill color, in rgb
	 *
	 * @return this
	 */
	RectBuilder fill(Color fillColor) {
		return this.fill(fillColor.getRGB());
	}

	/**
	 * Set the fill colour of the rectangle
	 *
	 * @param fillColor fill color, in rgb int encoding
	 *
	 * @return this
	 *
	 * @see PApplet#fill(int)
	 */
	RectBuilder fill(int fillColor) {
		this.fillColor = fillColor;
		return this;
	}

	/**
	 * Set the rectangle to have no fill color
	 *
	 * @return this
	 */
	RectBuilder noFill() {
		this.fillColor = null;
		return this;
	}

	/**
	 * Set the stroke (outline) colour of the rectangle
	 *
	 * @param strokeColor stroke color, in rgb
	 *
	 * @return this
	 */
	RectBuilder stroke(Color strokeColor) {
		return this.stroke(strokeColor.getRGB());
	}

	/**
	 * Set the stroke (outline) colour of the rectangle
	 *
	 * @param strokeColor stroke color, in rgb int encoding
	 *
	 * @return this
	 *
	 * @see PApplet#stroke(int)
	 */
	RectBuilder stroke(int strokeColor) {
		this.strokeColor = strokeColor;
		return this;
	}

	/**
	 * Set the rectangle to have no stroke (outline)
	 *
	 * @return this
	 */
	RectBuilder noStroke() {
		this.strokeColor = null;
		return this;
	}

	/**
	 * Set the stroke (outline) weight of the rectangle
	 *
	 * @param strokeWeight stroke (outline) weight
	 *
	 * @return this
	 */
	RectBuilder strokeWeight(float strokeWeight) {
		this.strokeWeight = strokeWeight;
		return this;
	}

	/**
	 * Position the rectangle at a point in the screen
	 *
	 * @param position position vector of top left hand corner of rectangle
	 *
	 * @return this
	 */
	RectBuilder at(PVector position) {
		return this.at(position.x, position.y);
	}

	/**
	 * Position the rectangle at a point in the screen
	 *
	 * @param x x coordinate of top left hand corner of rectangle
	 * @param y y coordinate of top left hand corner of rectangle
	 *
	 * @return this
	 */
	RectBuilder at(float x, float y) {
		this.positionX = x;
		this.positionY = y;
		return this;
	}

	/**
	 * Translate the rectangle in the x and y directions
	 *
	 * @param x change in x
	 * @param y change in y
	 *
	 * @return this
	 */
	RectBuilder translate(float x, float y) {
		this.positionX += x;
		this.positionY += y;
		return this;
	}

	/**
	 * Translate the rectangle in the x and y directions
	 *
	 * @param translation translation vector
	 *
	 * @return this
	 */
	RectBuilder translate(PVector translation) {
		return this.translate(translation.x, translation.y);
	}

	/**
	 * Set the dimensions of the rectangle
	 *
	 * @param width  width of rectangle
	 * @param height height of rectangle
	 *
	 * @return this
	 */
	RectBuilder size(float width, float height) {
		this.width = width;
		this.height = height;
		return this;
	}

	/**
	 * Set the radii of all four corner of the rectangle
	 *
	 * @param radius radius to use for all four corners
	 *
	 * @return this
	 */
	RectBuilder cornerRadius(float radius) {
		return this.cornerRadii(radius, radius, radius, radius);
	}

	/**
	 * Set the radius for each of the four corners of the rectangle
	 *
	 * @param topLeftRadius     radius of top left corner
	 * @param topRightRadius    radius of rop right corner
	 * @param bottomRightRadius radius of bottom right corner
	 * @param bottomLeftRadius  radius of bottom left corner
	 *
	 * @return this
	 */
	RectBuilder cornerRadii(
		float topLeftRadius,
		float topRightRadius,
		float bottomRightRadius,
		float bottomLeftRadius
	) {
		this.topLeftRadius = topLeftRadius;
		this.topRightRadius = topRightRadius;
		this.bottomRightRadius = bottomRightRadius;
		this.bottomLeftRadius = bottomLeftRadius;
		return this;
	}

	/**
	 * Draw the rectangle based on the current internal configuration
	 */
	void draw() {
		if (this.fillColor != null) {
			this.app.fill(this.fillColor);
		} else {
			this.app.noFill();
		}
		if (this.strokeColor != null) {
			this.app.stroke(this.strokeColor);
		} else {
			this.app.noStroke();
		}
		this.app.strokeWeight(this.strokeWeight);
		this.app.rect(
			this.positionX,
			this.positionY,
			this.width,
			this.height,
			this.topLeftRadius,
			this.topRightRadius,
			this.bottomRightRadius,
			this.bottomLeftRadius
		);
	}

	@Override
	public float minX() {
		return this.positionX;
	}

	@Override
	public float minY() {
		return this.positionY;
	}

	@Override
	public float width() {
		return this.width;
	}

	@Override
	public float height() {
		return this.height;
	}

	/**
	 * Capture the state of this rect builder as an immutable rectangle
	 *
	 * @return immutable rectangle from current rect builder state
	 */
	Rectangle capture() {
		return new RectCapture(this.positionX, this.positionY, this.width, this.height);
	}

	/**
	 * Immutable capture of a rectangle
	 */
	static class RectCapture implements Rectangle {

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
}