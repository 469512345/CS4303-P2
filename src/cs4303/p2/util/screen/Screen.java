package cs4303.p2.util.screen;

import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

/**
 * Abstract screen shown to the user. This interface allows the PApplet instance to defer rendering and responding to
 * events to a screen, and switch between screens easily.
 */
public interface Screen {

	/**
	 * Draw the screen
	 */
	void draw();

	/**
	 * Handle a key press event
	 *
	 * @param event key event
	 */
	default void keyPressed(KeyEvent event) {

	}

	/**
	 * Handle a key type event
	 *
	 * @param event key event
	 */
	default void keyTyped(KeyEvent event) {

	}

	/**
	 * Handle a key release event
	 *
	 * @param event key event
	 */
	default void keyReleased(KeyEvent event) {

	}

	/**
	 * Handle a mouse press event
	 *
	 * @param event mouse event
	 */
	default void mousePressed(MouseEvent event) {

	}

	/**
	 * Handle a mouse release event
	 *
	 * @param event mouse event
	 */
	default void mouseReleased(MouseEvent event) {

	}

	/**
	 * Handle a mouse click event
	 *
	 * @param event mouse event
	 */
	default void mouseClicked(MouseEvent event) {

	}

	/**
	 * Handle a mouse drag event
	 *
	 * @param event mouse event
	 */
	default void mouseDragged(MouseEvent event) {

	}

	/**
	 * Handle a mouse move event
	 *
	 * @param event mouse event
	 */
	default void mouseMoved(MouseEvent event) {

	}

	/**
	 * Handle a mouse enter event
	 *
	 * @param event mouse event
	 */
	default void mouseEntered(MouseEvent event) {

	}

	/**
	 * Handle a mouse exit event
	 *
	 * @param event mouse event
	 */
	default void mouseExited(MouseEvent event) {

	}

	/**
	 * Handle a mouse wheel event
	 *
	 * @param event mouse event
	 */
	default void mouseWheel(MouseEvent event) {

	}

	/**
	 * Provide a scale factor to the viewport
	 *
	 * @return scale factor to apply to viewport
	 */
	default float scale() {
		return 1;
	}

	/**
	 * Provide an offset to the viewport in the x-axis
	 *
	 * @return x offset applied to viewport after any {@link #scale()} is applied
	 */
	default float offsetX() {
		return 0;
	}

	/**
	 * Provide an offset to the viewport in the y-axis
	 *
	 * @return y offset applied to viewport after any {@link #scale()} is applied
	 */
	default float offsetY() {
		return 0;
	}

	/**
	 * Convert an x coordinate in world coordinates to screen coordinates
	 *
	 * @param x x coordinate in world coordinates
	 *
	 * @return transformed x coordinate in screen coordinates
	 */
	default float convertX(float x) {
		return this.scale() * (x + this.offsetX());
	}

	/**
	 * Convert a y coordinate in world coordinates to screen coordinates
	 *
	 * @param y y coordinate in world coordinates
	 *
	 * @return transformed y coordinate in screen coordinates
	 */
	default float convertY(float y) {
		return this.scale() * (y + this.offsetY());
	}

	/**
	 * Convert a position vector in world coordinates to screen coordinates. The vector will be written in place.
	 *
	 * @param point position vector in world coordinates
	 */
	default void convert(PVector point) {
		point.set(this.convertX(point.x), this.convertY(point.y));
	}

	/**
	 * Convert an x coordinate in screen coordinates to world coordinates
	 *
	 * @param x x coordinate in screen coordinate
	 *
	 * @return reverse transformed x coordinate in world coordinates
	 */
	default float unconvertX(float x) {
		return x / this.scale() - this.offsetX();
	}

	/**
	 * Convert a y coordinate in screen coordinates to world coordinates
	 *
	 * @param y y coordinate in screen coordinate
	 *
	 * @return reverse transformed y coordinate in world coordinates
	 */
	default float unconvertY(float y) {
		return y / this.scale() - this.offsetY();
	}

	/**
	 * Convert a position vector in screen coordinates to world coordinates. The vector will be written in place
	 *
	 * @param point position vector in screen coordinates
	 */
	default void unconvert(PVector point) {
		point.set(this.unconvertX(point.x), this.unconvertY(point.y));
	}

}
