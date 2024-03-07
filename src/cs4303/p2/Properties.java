package cs4303.p2;

import processing.core.PApplet;

import java.awt.Color;

public class Properties extends PApplet {

	public final float ROOM_MARGIN_MAX = 80;
	public final float ROOM_MARGIN_MIN = 5;

	public float roomMinHeight() {
		return this.height / 6f;
	}

	public float roomMinWidth() {
		return this.width / 10f;
	}

	/**
	 * Colour of buttons
	 */
	public final Color BUTTON_COLOR = Color.YELLOW;
	/**
	 * Colour of text on buttons
	 */
	public final Color BUTTON_TEXT_COLOR = Color.BLACK;
	/**
	 * Size of text in a button
	 */
	public final int BUTTON_TEXT_SIZE = 50;
	/**
	 * Height of buttons
	 */
	public final int BUTTON_HEIGHT = 50;
}
