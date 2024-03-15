package cs4303.p2.util.builder;

import cs4303.p2.Main;
import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.collisions.Rectangle;
import processing.core.PConstants;
import processing.core.PVector;
import processing.event.MouseEvent;

/**
 * A rectangular button
 */
public class Button implements Rectangle {

	/**
	 * PApplet instance
	 */
	private final Main app;
	/**
	 * Text on button
	 */
	private String text;
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
	 * Whether this button is part of the hud. Buttons on the HUD are not affected by any transformation matrices
	 * currently on the screen
	 */
	private boolean hud;

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
	public Button(Main app, String text, float minX, float minY, float width, float height) {
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
	public Button(Main app, String text, @NotNull Rectangle rectangle) {
		this(app, text, rectangle.minX(), rectangle.minY(), rectangle.width(), rectangle.height());
	}

	/**
	 * Set the button's text
	 *
	 * @param text new text for the button
	 *
	 * @return this
	 */
	@NotNull
	public Button text(String text) {
		this.text = text;
		return this;
	}

	/**
	 * Copy this button to a rectangle's position
	 *
	 * @param rectangle rectangle to copy
	 *
	 * @return this
	 */
	public Button copy(@NotNull Rectangle rectangle) {
		return this.at(rectangle.minX(), rectangle.minY())
			.size(rectangle.width(), rectangle.height());
	}

	/**
	 * Move the button to a different location
	 *
	 * @param minX new min x coordinate
	 * @param minY new min y coordinate
	 *
	 * @return this
	 */
	@NotNull
	public Button at(float minX, float minY) {
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
	public Button at(@NotNull PVector position) {
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
	@NotNull
	public Button size(float width, float height) {
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
	public Button at(@NotNull Rectangle rectangle) {
		return this.at(rectangle.minX(), rectangle.minY())
			.size(rectangle.width(), rectangle.height());
	}

	/**
	 * Mark this button as part of the HUD, so it will be statically positioned on the screen and not affected by any
	 * transformation matrices on the screen.
	 *
	 * @return this
	 */
	public Button asHUD() {
		return this.hud(true);
	}

	/**
	 * Set whether this button is part of the HUD. Elements on the HUD will be statically positioned on the screen and
	 * not affected by any transformation matrices on the screen.
	 *
	 * @param hud whether this button is part of the hud
	 *
	 * @return this
	 */
	@NotNull
	public Button hud(boolean hud) {
		this.hud = hud;
		return this;
	}

	/**
	 * Draw the button
	 */
	public void draw() {
		RectBuilder rect = this.app.rect()
			.copy(this)
			.hud(this.hud)
			.fill(this.app.BUTTON_COLOR)
			.cornerRadius(5);
		TextBuilder text = this.app.text(this.text)
			.fill(this.app.BUTTON_TEXT_COLOR)
			.hud(this.hud)
			.size(this.app.BUTTON_TEXT_SIZE)
			.centredInRect(rect);
		if (this.containsPoint(this.app.mouseX, this.app.mouseY)) {
			rect.strokeWeight(2);
			rect.stroke(this.app.BUTTON_HIGHLIGHT_COLOR);
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
	public boolean clicked(@NotNull MouseEvent event) {
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