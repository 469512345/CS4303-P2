package cs4303.p2.util.builder;

import cs4303.p2.Main;
import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.annotation.Nullable;
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
	@Nullable
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
	 * Whether this ellipse is hud, and won't be affected by any transformation matrices on the screen
	 */
	private boolean hud;

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
	@NotNull
	public LineBuilder clear() {
		this.startX = 0;
		this.startY = 0;
		this.direction.set(0, 0);
		this.strokeColor = null;
		this.strokeWeight = 1;
		this.strokeCap = PConstants.SQUARE;
		this.hud = false;
		return this;
	}

	/**
	 * Set the stroke (outline) colour of the line
	 *
	 * @param strokeColor stroke color, in rgb
	 *
	 * @return this
	 */
	@NotNull
	public LineBuilder stroke(@NotNull Color strokeColor) {
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
	@NotNull
	public LineBuilder stroke(int strokeColor) {
		this.strokeColor = strokeColor;
		return this;
	}

	/**
	 * Set the line to have no stroke (outline)
	 *
	 * @return this
	 */
	@NotNull
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
	@NotNull
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
	@NotNull
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
	@NotNull
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
	@NotNull
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
	@NotNull
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
	@NotNull
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
	@NotNull
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
	@NotNull
	public LineBuilder from(@NotNull PVector position) {
		return this.from(position.x, position.y);
	}

	/**
	 * Set the start point of the line
	 *
	 * @param x x coordinate to start line from
	 * @param y y coordinate to start line from
	 *
	 * @return this
	 */
	@NotNull
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
	@NotNull
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
	@NotNull
	public LineBuilder translate(@NotNull PVector translation) {
		return this.translate(translation.x, translation.y);
	}

	/**
	 * Set the end position of the line
	 *
	 * @param position position vector to end the line at
	 *
	 * @return this
	 */
	@NotNull
	public LineBuilder to(@NotNull PVector position) {
		return this.to(position.x, position.y);
	}

	/**
	 * Set the end point of the line
	 *
	 * @param x x coordinate to end line at
	 * @param y y coordinate to end line at
	 *
	 * @return this
	 */
	@NotNull
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
	@NotNull
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
	@NotNull
	public LineBuilder limit(float limit) {
		this.direction.limit(limit);
		return this;
	}

	/**
	 * Set the length of the line
	 *
	 * @param length length of the line
	 *
	 * @return this
	 */
	@NotNull
	public LineBuilder length(float length) {
		this.direction.setMag(length);
		return this;
	}


	/**
	 * Mark this line as part of the HUD, so it will be statically positioned on the screen and not affected by any
	 * transformation matrices on the screen.
	 *
	 * @return this
	 */
	@NotNull
	public LineBuilder asHUD() {
		return this.hud(true);
	}

	/**
	 * Set whether this line is part of the HUD. Elements on the HUD will be statically positioned on the screen and not
	 * affected by any transformation matrices on the screen.
	 *
	 * @param hud whether this line is part of the hud
	 *
	 * @return this
	 */
	@NotNull
	public LineBuilder hud(boolean hud) {
		this.hud = hud;
		return this;
	}

	/**
	 * Draw the line based on the current internal configuration
	 */
	public void draw() {
		this.app.push();
		this.app.pushMatrix();

		if (!this.hud) {
			this.app.applyViewport();
		}

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