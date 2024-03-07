package cs4303.p2.util.builder;

import cs4303.p2.Main;
import cs4303.p2.util.collisions.Rectangle;
import processing.core.PConstants;
import processing.core.PVector;
import processing.event.MouseEvent;

/**
 * A rectangular button
 */
class Button implements Rectangle {

	/**
	 * PApplet instance
	 */
	private final Main app;
	/**
	 * Text on button
	 */
	private final String text;
	/**
	 * Min X of rectangle
	 */
	private float minX;
	/**
	 * Min Y of rectangle
	 */
	private float minY;
	/**
	 * Width of rectangle
	 */
	private float width;
	/**
	 * Height of rectangle
	 */
	private float height;

	/**
	 * Create a button
	 *
	 * @param app    PApplet instance
	 * @param text   text on button
	 * @param minX   min x of rectangle
	 * @param minY   min y of rectangle
	 * @param width  width of rectangle
	 * @param height height of rectangle
	 */
	Button(Main app, String text, float minX, float minY, float width, float height) {
		this.app = app;
		this.text = text;
		this.minX = minX;
		this.minY = minY;
		this.width = width;
		this.height = height;
	}

	/**
	 * Create a button
	 *
	 * @param app       PApplet instance
	 * @param text      text on button
	 * @param rectangle rectangle definition
	 */
	Button(Main app, String text, Rectangle rectangle) {
		this(app, text, rectangle.minX(), rectangle.minY(), rectangle.width(), rectangle.height());
	}

	/**
	 * Move the button to a different location
	 *
	 * @param minX new min x coordinate
	 * @param minY new min y coordinate
	 *
	 * @return this
	 */
	Button at(float minX, float minY) {
		this.minX = minX;
		this.minY = minY;
		return this;
	}

	/**
	 * Move the button to a different location
	 *
	 * @param position position vector of new min (x, y) coordinate
	 *
	 * @return this
	 */
	Button at(PVector position) {
		return this.at(position.x, position.y);
	}

	/**
	 * Resize button
	 *
	 * @param width  new width
	 * @param height new height
	 *
	 * @return this
	 */
	Button size(float width, float height) {
		this.width = width;
		this.height = height;
		return this;
	}

	/**
	 * Move and resize button
	 *
	 * @param rectangle new rectangle definition
	 *
	 * @return this
	 */
	Button at(Rectangle rectangle) {
		return this.at(rectangle.minX(), rectangle.minY())
			.size(rectangle.width(), rectangle.height());
	}

	/**
	 * Draw the button
	 */
	void draw() {
		RectBuilder rect = this.app.rect()
			.at(this.minX, this.minY)
			.size(this.width, this.height)
			.fill(this.app.BUTTON_COLOR)
			.cornerRadius(5);
		TextBuilder text = this.app.text(this.text)
			.fill(this.app.BUTTON_TEXT_COLOR)
			.size(this.app.BUTTON_TEXT_SIZE)
			.centredInRect(rect);
		if (this.containsPoint(this.app.mouseX, this.app.mouseY)) {
			rect.strokeWeight(2);
			rect.stroke(this.app.BUTTON_TEXT_COLOR);
			this.app.cursor(PConstants.HAND);
		} else {
			rect.noStroke();
		}
		rect.draw();
		text.draw();
	}

	/**
	 * Test if a mouse event occurs within the button
	 *
	 * @param event mouse event
	 *
	 * @return true if mouse event occurs within the button
	 */
	boolean clicked(MouseEvent event) {
		return this.containsPoint(event.getX(), event.getY());
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