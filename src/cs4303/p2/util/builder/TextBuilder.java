package cs4303.p2.util.builder;

import cs4303.p2.Main;
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
	private final PApplet app;

	/**
	 * TextBuilder to show
	 */
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
	private Integer fillColor;

	/**
	 * Create a new TextBuilder bound to a game
	 *
	 * @param app game instance
	 */
	public TextBuilder(Main app) {
		this.app = app;
	}

	/**
	 * Set the text to be displayed
	 *
	 * @param text text to be displayed
	 *
	 * @return this
	 */
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
	TextBuilder fill(Color fillColor) {
		return this.fill(fillColor.getRGB());
	}

	/**
	 * Set the fill colour of the text
	 *
	 * @param fillColor fill color, in rgb int encoding
	 *
	 * @return this
	 */
	TextBuilder fill(int fillColor) {
		this.fillColor = fillColor;
		return this;
	}

	/**
	 * Set the text to have no fill color
	 *
	 * @return this
	 */
	TextBuilder noFill() {
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
	TextBuilder at(PVector position) {
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
	TextBuilder at(float x, float y) {
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
	TextBuilder translate(float x, float y) {
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
	TextBuilder translate(PVector translation) {
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
	TextBuilder size(float size) {
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
	TextBuilder centredInRect(PVector p1, float width, float height) {
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
	TextBuilder centredHorizontally(PVector p1, float width) {
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
	TextBuilder centredVertically(PVector p1, float height) {
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
	TextBuilder centredHorizontally(float x, float y, float width) {
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
	TextBuilder centredVertically(float x, float y, float height) {
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
	TextBuilder centredInRect(float x, float y, float width, float height) {
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
	TextBuilder centredInRect(Rectangle rectangle) {
		return this.centredInRect(rectangle.minX(), rectangle.minY(), rectangle.width(), rectangle.height());
	}

	/**
	 * Draw the text based on the current internal configuration
	 */
	void draw() {
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
	}

}
