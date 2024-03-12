package cs4303.p2;

import cs4303.p2.util.keybind.KeyKeybind;
import cs4303.p2.util.keybind.Keybind;
import cs4303.p2.util.keybind.MouseKeybind;
import processing.core.PApplet;
import processing.event.KeyEvent;

import java.awt.Color;
import java.awt.event.MouseEvent;

/**
 * Properties for the application. The Main class extends this class to gain access to these properties without
 * cluttering the Main class file.
 */
public abstract class Properties extends PApplet {

	//BUTTON properties
	/**
	 * Colour of buttons
	 */
	public final Color BUTTON_COLOR = Color.YELLOW;
	/**
	 * Colour of text on buttons
	 */
	public final Color BUTTON_TEXT_COLOR = Color.BLACK;
	/**
	 * Colour of highlight when hovering over button
	 */
	public final Color BUTTON_HIGHLIGHT_COLOR = Color.WHITE;
	/**
	 * Size of text in a button
	 */
	public final int BUTTON_TEXT_SIZE = 50;
	/**
	 * Height of buttons
	 */
	public final int BUTTON_HEIGHT = 50;

	//Wall properties
	/**
	 * Colour of walls
	 */
	public final Color WALL_COLOR = Color.GREEN;
	/**
	 * Stroke width of walls
	 */
	public final float WALL_STROKE_WIDTH = 1;

	//HUD properties
	/**
	 * How far from the top of the screen the HUD text should be
	 */
	public final float HUD_TEXT_HEIGHT = 20;
	/**
	 * Text size of the score text on the HUD
	 */
	public final float HUD_SCORE_TEXT_SIZE = 100;
	/**
	 * Text size of other text on the HUD (lives, wave number)
	 */
	public final float HUD_OTHER_TEXT_SIZE = 50;
	/**
	 * Colour of text on the HUD
	 */
	public final Color HUD_TEXT_COLOR = Color.RED;

	//Viewport settings
	/**
	 * How much to change the zoom by when zooming in or out
	 */
	public final float ZOOM_FACTOR = 1.1f;
	/**
	 * Initial zoom of the viewport
	 */
	public final float INITIAL_ZOOM = 2f;

	//Game settings
	/**
	 * How many lives the player starts with
	 */
	public final int STARTING_LIVES = 3;
	/**
	 * Background color for the game
	 */
	public final Color GAME_BACKGROUND_COLOR = Color.BLACK;

	//Player settings
	/**
	 * Colour of the player on the screen
	 */
	public final Color PLAYER_COLOR = Color.MAGENTA;
	/**
	 * Radius of the player's circle on the screen
	 */
	public final float PLAYER_RADIUS = 8f;
	/**
	 * Radius of the player squared
	 */
	public final float PLAYER_RADIUS_SQUARED = this.PLAYER_RADIUS * this.PLAYER_RADIUS;
	/**
	 * The magnitude of the player's velocity. This is the same velocity in the cardinal directions, and also when moving diagonally.
	 */
	public final float PLAYER_MOVEMENT_VELOCITY = 0.8f; // pixels / frame
	/**
	 * How many frames the camera should lag behind the player's current position
	 */
	public final int CAMERA_LAG_FRAMES = 15;

	//Keybind settings
	/**
	 * Keybind for move up
	 */
	public Keybind KEYBIND_MOVE_UP = new KeyKeybind(87, 0); // w
	/**
	 * Keybind for move down
	 */
	public Keybind KEYBIND_MOVE_DOWN = new KeyKeybind(83, 0); // s
	/**
	 * Keybind for move left
	 */
	public Keybind KEYBIND_MOVE_LEFT = new KeyKeybind(65, 0); // a
	/**
	 * Keybind for move right
	 */
	public Keybind KEYBIND_MOVE_RIGHT = new KeyKeybind(68, 0); // d
	/**
	 * Keybind for fire
	 */
	public Keybind KEYBIND_FIRE = new MouseKeybind(MouseEvent.BUTTON1, 0); // left click
	/**
	 * Keybind for zoom in
	 */
	public Keybind KEYBIND_ZOOM_IN = new KeyKeybind(61, KeyEvent.CTRL); // CTRL + +
	/**
	 * Keybind for zoom out
	 */
	public Keybind KEYBIND_ZOOM_OUT = new KeyKeybind(45, KeyEvent.CTRL); // CTRL + -

	//Menu screen properties
	/**
	 * Colour of the background on the menu screen
	 */
	public final Color MENU_BACKGROUND_COLOR = Color.BLACK;
	/**
	 * Size of the title text on the menu screen
	 */
	public final float MENU_TITLE_TEXT_SIZE = 150;
	/**
	 * Colour of the title text on the menu screen
	 */
	public final Color MENU_TITLE_TEXT_COLOR = Color.RED;
}
