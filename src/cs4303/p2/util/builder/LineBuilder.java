package cs4303.p2.util.builder;

import cs4303.p2.Main;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.awt.Color;

/**
 * Utility builder class for rendering a line on the screen
 */
public final class LineBuilder {

	/**
	 * PApplet instance
	 */
	private final Main app;
	/**
	 * Direction of line from ({@link #startX}, {@link #startY}).
	 */
	private final PVector direction = new PVector();
	/**
	 * X coordinate of start
	 */
	private float startX;
	/**
	 * Y coordinate of start
	 */
	private float startY;
	/**
	 * Stroke (outline) colour (or null for no outline)
	 *
	 * @see PApplet#stroke(int)
	 */
	private Integer strokeColor;
	/**
	 * Stroke (outline) weight
	 *
	 * @see PApplet#strokeWeight(float)
	 */
	private float strokeWeight;
	/**
	 * Stroke cap.
	 *
	 * @see PApplet#strokeCap(int)
	 */
	private int strokeCap;

	/**
	 * Create a new TextBuilder bound to a game
	 *
	 * @param app app instance
	 */
	public LineBuilder(Main app) {
		this.app = app;
	}

	/**
	 * Clear the current state of this line builder
	 *
	 * @return this
	 */
	public LineBuilder clear() {
		this.startX = 0;
		this.startY = 0;
		this.direction.set(0, 0);
		this.strokeColor = null;
		this.strokeWeight = 0;
		this.strokeCap = PConstants.SQUARE;
		return this;
	}

	/**
	 * Set the stroke (outline) colour of the line
	 *
	 * @param strokeColor stroke color, in rgb
	 *
	 * @return this
	 */
	public LineBuilder stroke(Color strokeColor) {
		return this.stroke(strokeColor.getRGB());
	}

	/**
	 * Set the stroke (outline) colour of the line
	 *
	 * @param strokeColor stroke color, in rgb int encoding
	 *
	 * @return this
	 *
	 * @see PApplet#stroke(int)
	 */
	public LineBuilder stroke(int strokeColor) {
		this.strokeColor = strokeColor;
		return this;
	}

	/**
	 * Set the line to have no stroke (outline)
	 *
	 * @return this
	 */
	public LineBuilder noStroke() {
		this.strokeColor = null;
		return this;
	}

	/**
	 * Set the stroke (outline) weight of the line
	 *
	 * @param strokeWeight stroke (outline) weight
	 *
	 * @return this
	 */
	public LineBuilder strokeWeight(float strokeWeight) {
		this.strokeWeight = strokeWeight;
		return this;
	}

	/**
	 * Set the stroke cap on the end of the line
	 *
	 * @param strokeCap stroke cap value
	 *
	 * @return this
	 *
	 * @see PApplet#strokeCap(int)
	 */
	public LineBuilder strokeCap(int strokeCap) {
		this.strokeCap = strokeCap;
		return this;
	}

	/**
	 * Use square stroke cap
	 *
	 * @return this
	 *
	 * @see PConstants#SQUARE
	 * @see PApplet#strokeCap(int)
	 */
	public LineBuilder squareCap() {
		return this.strokeCap(PConstants.SQUARE);
	}

	/**
	 * Use round stroke cap
	 *
	 * @return this
	 *
	 * @see PConstants#ROUND
	 * @see PApplet#strokeCap(int)
	 */
	public LineBuilder roundCap() {
		return this.strokeCap(PConstants.ROUND);
	}

	/**
	 * Use project stroke cap
	 *
	 * @return this
	 *
	 * @see PConstants#PROJECT
	 * @see PApplet#strokeCap(int)
	 */
	public LineBuilder projectCap() {
		return this.strokeCap(PConstants.PROJECT);
	}

	/**
	 * Use miter stroke cap
	 *
	 * @return this
	 *
	 * @see PConstants#MITER
	 * @see PApplet#strokeCap(int)
	 */
	public LineBuilder miterCap() {
		return this.strokeCap(PConstants.MITER);
	}

	/**
	 * Use bevel stroke cap
	 *
	 * @return this
	 *
	 * @see PConstants#BEVEL
	 * @see PApplet#strokeCap(int)
	 */
	public LineBuilder bevelCap() {
		return this.strokeCap(PConstants.BEVEL);
	}

	/**
	 * Set the start position of the line
	 *
	 * @param position position vector to start the line from
	 *
	 * @return this
	 */
	public LineBuilder from(PVector position) {
		return this.from(position.x, position.y);
	}

	/**
	 * Position the text at a point in the screen
	 *
	 * @param x x coordinate to start line from
	 * @param y y coordinate to start line from
	 *
	 * @return this
	 */
	public LineBuilder from(float x, float y) {
		this.startX = x;
		this.startY = y;
		return this;
	}

	/**
	 * Translate the line in the x and y directions
	 *
	 * @param x change in x
	 * @param y change in y
	 *
	 * @return this
	 */
	public LineBuilder translate(float x, float y) {
		this.startX += x;
		this.startY += y;
		return this;
	}

	/**
	 * Translate the line in the x and y directions
	 *
	 * @param translation translation vector
	 *
	 * @return this
	 */
	public LineBuilder translate(PVector translation) {
		return this.translate(translation.x, translation.y);
	}

	/**
	 * Set the end position of the line
	 *
	 * @param position position vector to end the line at
	 *
	 * @return this
	 */
	public LineBuilder to(PVector position) {
		return this.to(position.x, position.y);
	}

	/**
	 * Position the text at a point in the screen
	 *
	 * @param x x coordinate to end line at
	 * @param y y coordinate to end line at
	 *
	 * @return this
	 */
	public LineBuilder to(float x, float y) {
		this.direction.set(x - this.startX, y - this.startY);
		return this;
	}

	/**
	 * Set the line to point along a vector from the start position.
	 * <p>
	 * Note that the start location must be set before a call to this method.
	 *
	 * @param vector direction to move along
	 *
	 * @return this
	 */
	public LineBuilder along(PVector vector) {
		this.direction.set(vector);
		return this;
	}

	/**
	 * Limit the length of the line
	 *
	 * @param limit maximum length of line
	 *
	 * @return this
	 */
	public LineBuilder limit(float limit) {
		this.direction.limit(limit);
		return this;
	}

	/**
	 * Draw the line based on the current internal configuration
	 */
	public void draw() {
		this.app.push();
		this.app.pushMatrix();

		this.app.applyViewport();

		if (this.strokeColor != null) {
			this.app.stroke(this.strokeColor);
		} else {
			this.app.noStroke();
		}

		this.app.strokeWeight(this.strokeWeight);
		this.app.strokeCap(this.strokeCap);

		float endX = this.startX + this.direction.x;
		float endY = this.startY + this.direction.y;
		this.app.line(
			this.startX,
			this.startY,
			endX,
			endY
		);

		this.app.popMatrix();
		this.app.pop();
	}

}