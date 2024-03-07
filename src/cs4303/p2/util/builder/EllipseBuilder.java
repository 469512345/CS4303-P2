package cs4303.p2.util.builder;

import cs4303.p2.Main;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.Color;

/**
 * Utility builder class for rendering an ellipse on the screen
 */
public final class EllipseBuilder {

	/**
	 * GameScreen instance
	 */
	private final Main app;

	/**
	 * X coordinate of ellipse's centre
	 */
	private float positionX;
	/**
	 * Y coordinate of ellipse's centre
	 */
	private float positionY;
	/**
	 * Width of ellipse
	 */
	private float width;
	/**
	 * Height of ellipse
	 */
	private float height;

	/**
	 * Fill color (or null for no fill)
	 */
	private Integer fillColor;
	/**
	 * Stroke (outline) colour (or null for no outline)
	 */
	private Integer strokeColor;
	/**
	 * Stroke (outline) width
	 */
	private float strokeWeight;

	/**
	 * Create a new EllipseBuilder bound to a game
	 *
	 * @param app game instance
	 */
	public EllipseBuilder(Main app) {
		this.app = app;
	}

	/**
	 * Clear the current state of this ellipse builder
	 *
	 * @return this
	 */
	public EllipseBuilder clear() {
		this.positionX = 0;
		this.positionY = 0;
		this.width = 0;
		this.height = 0;
		this.fillColor = null;
		this.strokeColor = null;
		this.strokeWeight = 0;
		return this;
	}

	/**
	 * Set the fill colour of the ellipse
	 *
	 * @param fillColor fill color, in rgb
	 *
	 * @return this
	 */
	public EllipseBuilder fill(Color fillColor) {
		return this.fill(fillColor.getRGB());
	}

	/**
	 * Set the fill colour of the ellipse
	 *
	 * @param fillColor fill color, in rgb int encoding
	 *
	 * @return this
	 *
	 * @see Color#getRGB()
	 * @see Color#Color(int)
	 */
	public EllipseBuilder fill(int fillColor) {
		this.fillColor = fillColor;
		return this;
	}

	/**
	 * Set the ellipse to have no fill color
	 *
	 * @return this
	 */
	public EllipseBuilder noFill() {
		this.fillColor = null;
		return this;
	}

	/**
	 * Set the stroke colour of the ellipse
	 *
	 * @param strokeColor stroke color, in rgb
	 *
	 * @return this
	 *
	 * @see PApplet#stroke(float, float, float)
	 */
	public EllipseBuilder stroke(Color strokeColor) {
		return this.stroke(strokeColor.getRGB());
	}

	/**
	 * Set the stroke colour of the ellipse
	 *
	 * @param strokeColor stroke color, in rgb int encoding
	 *
	 * @return this
	 *
	 * @see PApplet#stroke(int)
	 */
	public EllipseBuilder stroke(int strokeColor) {
		this.strokeColor = strokeColor;
		return this;
	}

	/**
	 * Set the ellipse to have no stroke (outline)
	 *
	 * @return this
	 *
	 * @see PApplet#noStroke()
	 */
	public EllipseBuilder noStroke() {
		this.strokeColor = null;
		return this;
	}

	/**
	 * Set the stroke (outline) weight of the ellipse
	 *
	 * @param strokeWeight stroke (outline) weight
	 *
	 * @return this
	 *
	 * @see PApplet#strokeWeight(float)
	 */
	public EllipseBuilder strokeWeight(float strokeWeight) {
		this.strokeWeight = strokeWeight;
		return this;
	}

	/**
	 * Centre the ellipse around a point on the screen
	 *
	 * @param position position vector of centre of the ellipse
	 *
	 * @return this
	 */
	public EllipseBuilder at(PVector position) {
		return this.at(position.x, position.y);
	}

	/**
	 * Centre the ellipse around a point on the screen
	 *
	 * @param x x coordinate of centre of ellipse
	 * @param y y coordinate of centre of ellipse
	 *
	 * @return this
	 */
	public EllipseBuilder at(float x, float y) {
		this.positionX = x;
		this.positionY = y;
		return this;
	}

	/**
	 * Translate the ellipse in the x and y directions
	 *
	 * @param x change in x
	 * @param y change in y
	 *
	 * @return this
	 */
	public EllipseBuilder translate(float x, float y) {
		this.positionX += x;
		this.positionY += y;
		return this;
	}

	/**
	 * Translate the ellipse in the x and y directions
	 *
	 * @param translation translation vector
	 *
	 * @return this
	 */
	public EllipseBuilder translate(PVector translation) {
		return this.translate(translation.x, translation.y);
	}

	/**
	 * Set the radius, as such drawing a circle
	 *
	 * @param radius radius of the ellipse
	 *
	 * @return this
	 */
	public EllipseBuilder radius(float radius) {
		float diameter = radius * 2;
		return this.size(diameter, diameter);
	}

	/**
	 * Set the width and height of the ellipse
	 *
	 * @param width  width of ellipse
	 * @param height height of ellipse
	 *
	 * @return this
	 */
	public EllipseBuilder size(float width, float height) {
		this.width = width;
		this.height = height;
		return this;
	}

	/**
	 * Set the width and of the ellipse
	 *
	 * @param width width of ellipse
	 *
	 * @return this
	 */
	public EllipseBuilder width(float width) {
		this.width = width;
		return this;
	}

	/**
	 * Set the height of the ellipse
	 *
	 * @param height height of ellipse
	 *
	 * @return this
	 */
	public EllipseBuilder height(float height) {
		this.height = height;
		return this;
	}

	/**
	 * Draw the ellipse based on the current internal configuration
	 */
	public void draw() {
		this.app.push();
		this.app.pushMatrix();

		this.app.applyViewport();

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
		this.app.ellipse(this.positionX, this.positionY, this.width, this.height);

		this.app.popMatrix();
		this.app.pop();
	}

}
