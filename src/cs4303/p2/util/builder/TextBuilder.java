package cs4303.p2.util.builder;

import cs4303.p2.Main;
import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.annotation.Nullable;
import cs4303.p2.util.collisions.Rectangle;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.Color;

/**
 * Utility builder class for rendering text on the screen
 */
public final class TextBuilder {

	/**
	 * GameScreen instance
	 */
	private final Main app;

	/**
	 * TextBuilder to show
	 */
	@Nullable
	private String text;

	/**
	 * X coordinate - top left hand corner of the text (min X)
	 */
	private float positionX;
	/**
	 * Y coordinate - top left hand corner of the text (min Y).
	 * <p>
	 * Note this is different to {@link PApplet#text(String, float, float)}, which uses bottom left hand corner (max
	 * Y).
	 */
	private float positionY;
	/**
	 * If positive, the text will be centred horizontally within a box from {@link #positionX} of this width
	 */
	private float centringWidth = -1;
	/**
	 * If positive, the text will be centred vertically within a box from {@link #positionY} of this height
	 */
	private float centringHeight = -1;
	/**
	 * Size of the text.
	 *
	 * @see PApplet#textSize(float)
	 */
	private float size;
	/**
	 * Fill color (or null for no fill)
	 */
	@Nullable
	private Integer fillColor;
	/**
	 * Whether this ellipse is hud, and won't be affected by any transformation matrices on the screen
	 */
	private boolean hud;

	/**
	 * Create a new TextBuilder bound to a game
	 *
	 * @param app game instance
	 */
	public TextBuilder(Main app) {
		this.app = app;
	}

	/**
	 * Clear the current state of this text builder
	 *
	 * @return this
	 */
	@NotNull
	public TextBuilder clear() {
		this.text = null;
		this.positionX = 0;
		this.positionY = 0;
		this.centringWidth = -1;
		this.centringHeight = -1;
		this.fillColor = null;
		this.size = 0;
		this.hud = false;
		return this;
	}

	/**
	 * Set the text to be displayed
	 *
	 * @param text text to be displayed
	 *
	 * @return this
	 */
	@NotNull
	public TextBuilder text(String text) {
		this.text = text;
		return this;
	}

	/**
	 * Set the fill colour of the text
	 *
	 * @param fillColor fill color, in rgb
	 *
	 * @return this
	 */
	@NotNull
	public TextBuilder fill(@NotNull Color fillColor) {
		return this.fill(fillColor.getRGB());
	}

	/**
	 * Set the fill colour of the text
	 *
	 * @param fillColor fill color, in rgb int encoding
	 *
	 * @return this
	 */
	@NotNull
	public TextBuilder fill(int fillColor) {
		this.fillColor = fillColor;
		return this;
	}

	/**
	 * Set the text to have no fill color
	 *
	 * @return this
	 */
	@NotNull
	public TextBuilder noFill() {
		this.fillColor = null;
		return this;
	}

	/**
	 * Position the text at a point in the screen
	 *
	 * @param position position vector of top left hand corner of text
	 *
	 * @return this
	 */
	@NotNull
	public TextBuilder at(@NotNull PVector position) {
		return this.at(position.x, position.y);
	}

	/**
	 * Position the text at a point in the screen
	 *
	 * @param x x coordinate of top left hand corner of text
	 * @param y y coordinate of top left hand corner of text
	 *
	 * @return this
	 */
	@NotNull
	public TextBuilder at(float x, float y) {
		this.positionX = x;
		this.positionY = y;
		this.centringWidth = 1;
		this.centringHeight = -1;
		return this;
	}

	/**
	 * Translate the text in the x and y directions
	 *
	 * @param x change in x
	 * @param y change in y
	 *
	 * @return this
	 */
	@NotNull
	public TextBuilder translate(float x, float y) {
		this.positionX += x;
		this.positionY += y;
		return this;
	}

	/**
	 * Translate the text in the x and y directions
	 *
	 * @param translation translation vector
	 *
	 * @return this
	 */
	@NotNull
	public TextBuilder translate(@NotNull PVector translation) {
		return this.translate(translation.x, translation.y);
	}

	/**
	 * Set the size of the text
	 *
	 * @param size size of the text
	 *
	 * @return this
	 *
	 * @see PApplet#textSize(float)
	 */
	@NotNull
	public TextBuilder size(float size) {
		this.size = size;
		return this;
	}

	/**
	 * Position the text centred in a rectangle
	 *
	 * @param p1     position vector of top-left hand corner
	 * @param width  width of rectangle
	 * @param height height of rectangle
	 *
	 * @return this
	 */
	@NotNull
	public TextBuilder centredInRect(@NotNull PVector p1, float width, float height) {
		return this.centredInRect(p1.x, p1.y, width, height);
	}

	/**
	 * Position text centred horizontally on a line
	 *
	 * @param p1    position vector of start of line
	 * @param width length of horizontal line
	 *
	 * @return this
	 */
	@NotNull
	public TextBuilder centredHorizontally(@NotNull PVector p1, float width) {
		return this.centredHorizontally(p1.x, p1.y, width);
	}

	/**
	 * Position text centred vertically on a line
	 *
	 * @param p1     position vector of start of line
	 * @param height length of vertical line
	 *
	 * @return this
	 */
	@NotNull
	public TextBuilder centredVertically(@NotNull PVector p1, float height) {
		return this.centredVertically(p1.x, p1.y, height);
	}

	/**
	 * Position text centred horizontally on a line
	 *
	 * @param x     x coordinate of start of line
	 * @param y     y coordinate of line
	 * @param width length of horizontal line
	 *
	 * @return this
	 */
	@NotNull
	public TextBuilder centredHorizontally(float x, float y, float width) {
		return this.centredInRect(x, y, width, -1);
	}

	/**
	 * Position text centred vertically on a line
	 *
	 * @param x      x coordinate of line
	 * @param y      y coordinate of start of line
	 * @param height length of vertical line
	 *
	 * @return this
	 */
	@NotNull
	public TextBuilder centredVertically(float x, float y, float height) {
		return this.centredInRect(x, y, -1, height);
	}

	/**
	 * Position the text centred in a rectangle
	 *
	 * @param x      x coordinate of top left hand corner (min x)
	 * @param y      y coordinate of top left hand corner (min y)
	 * @param width  width of rectangle
	 * @param height height of rectangle
	 *
	 * @return this
	 */
	@NotNull
	public TextBuilder centredInRect(float x, float y, float width, float height) {
		this.positionX = x;
		this.positionY = y;
		this.centringWidth = width;
		this.centringHeight = height;
		return this;
	}

	/**
	 * Position the text centred in a rectangle
	 *
	 * @param rectangle rectangle definition
	 *
	 * @return this
	 */
	@NotNull
	public TextBuilder centredInRect(@NotNull Rectangle rectangle) {
		return this.centredInRect(rectangle.minX(), rectangle.minY(), rectangle.width(), rectangle.height());
	}

	/**
	 * Mark this text as part of the HUD, so it will be statically positioned on the screen and not affected by any
	 * transformation matrices on the screen.
	 *
	 * @return this
	 */
	@NotNull
	public TextBuilder asHUD() {
		return this.hud(true);
	}

	/**
	 * Set whether this text is part of the HUD. Elements on the HUD will be statically positioned on the screen and not
	 * affected by any transformation matrices on the screen.
	 *
	 * @param hud whether this text is part of the hud
	 *
	 * @return this
	 */
	@NotNull
	public TextBuilder hud(boolean hud) {
		this.hud = hud;
		return this;
	}

	/**
	 * Draw the text based on the current internal configuration
	 */
	public void draw() {
		this.app.push();
		this.app.pushMatrix();

		if (!this.hud) {
			this.app.applyViewport();
		}

		if (this.fillColor != null) {
			this.app.fill(this.fillColor);
		} else {
			this.app.noFill();
		}

		this.app.textSize(this.size);

		float x = this.positionX;
		float y = this.positionY;

		if (this.centringWidth > 0) {
			float textWidth = this.app.textWidth(this.text);
			x = this.positionX + ((this.centringWidth - textWidth) / 2);
		}

		float textAscent = this.app.textAscent();
		if (this.centringHeight > 0) {
			y = this.positionY + ((this.centringHeight - textAscent) / 2);
		}

		this.app.text(this.text, x, y + textAscent);

		this.app.popMatrix();
		this.app.pop();
	}

}
