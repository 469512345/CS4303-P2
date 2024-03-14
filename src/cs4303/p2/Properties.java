package cs4303.p2;

import cs4303.p2.util.keybind.KeyKeybind;
import cs4303.p2.util.keybind.Keybind;
import cs4303.p2.util.keybind.MouseKeybind;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.KeyEvent;

import java.awt.Color;

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
	/**
	 * Spacing between buttons
	 */
	public final float BUTTON_SPACING = this.BUTTON_HEIGHT * 1.6f;

	//Room properties
	/**
	 * Colour of a room on the level
	 */
	public final Color ROOM_COLOR = Color.WHITE;

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
	public final float INITIAL_ZOOM = 3f;

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
	 * The magnitude of the player's velocity. This is the same velocity in the cardinal directions, and also when
	 * moving diagonally.
	 */
	public final float PLAYER_MOVEMENT_VELOCITY = 0.8f; // pixels / frame
	/**
	 * How many frames the camera should lag behind the player's current position
	 */
	public final int CAMERA_LAG_FRAMES = 15;

	//Player eye properties
	/**
	 * Distance from the centre of the player to their eye
	 */
	public final float PLAYER_EYE_DISTANCE = 6f;
	/**
	 * Radius of the player's eye
	 */
	public final float PLAYER_EYE_RADIUS = 2f;
	/**
	 * Colour of the player's eye
	 */
	public final Color PLAYER_EYE_COLOR = Color.GREEN;
	/**
	 * Radius by which the player's orientation change each update
	 */
	public final float PLAYER_EYE_TURN_INCREMENT = (float) (Math.PI / 32d);

	//Player projectile settings
	/**
	 * Colour of the player's projectiles
	 */
	public final Color PLAYER_PROJECTILE_COLOR = Color.CYAN;
	/**
	 * Radius of the player's projectiles
	 */
	public final float PLAYER_PROJECTILE_RADIUS = 2f;
	/**
	 * Radius squared of the player's projectiles
	 */
	public final float PLAYER_PROJECTILE_RADIUS_SQUARED = this.PLAYER_PROJECTILE_RADIUS * this.PLAYER_PROJECTILE_RADIUS;
	/**
	 * Magnitude of the player's projectiles' velocity
	 */
	public final float PLAYER_PROJECTILE_MOVEMENT_VELOCITY = 1.6f;
	/**
	 * Maximum number of bounces a player's projectile can have
	 */
	public final int PLAYER_PROJECTILE_MAX_BOUNCES = 1;

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
	public Keybind KEYBIND_FIRE = new MouseKeybind(PConstants.LEFT, 0); // left click
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
	/**
	 * Colour of overlays on pause screens
	 */
	public final Color OVERLAY_COLOR = new Color(100, 100, 100, 100);

	//Obstacle properties
	/**
	 * Colour of obstacles in the map
	 */
	public final Color OBSTACLE_COLOR = Color.ORANGE;
	/**
	 * Maximum number of failed attempts to place an obstacle in a room before it is aborted
	 */
	public final int OBSTACLE_MAX_FAILED_ATTEMPTS = 10;
	/**
	 * Score for exploding an obstacle
	 */
	public final int OBSTACLE_EXPLODE_SCORE = 10;

	/**
	 * Whether the player can kill their own family
	 */
	public boolean PLAYER_CAN_KILL_FAMILY = false;
}
