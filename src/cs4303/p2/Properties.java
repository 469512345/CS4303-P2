package cs4303.p2;

import cs4303.p2.util.annotation.NotNull;
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
	@NotNull
	public Keybind KEYBIND_MOVE_UP = new KeyKeybind(87, 0); // w
	/**
	 * Keybind for move down
	 */
	@NotNull
	public Keybind KEYBIND_MOVE_DOWN = new KeyKeybind(83, 0); // s
	/**
	 * Keybind for move left
	 */
	@NotNull
	public Keybind KEYBIND_MOVE_LEFT = new KeyKeybind(65, 0); // a
	/**
	 * Keybind for move right
	 */
	@NotNull
	public Keybind KEYBIND_MOVE_RIGHT = new KeyKeybind(68, 0); // d
	/**
	 * Keybind for fire
	 */
	@NotNull
	public Keybind KEYBIND_FIRE = new MouseKeybind(PConstants.LEFT, 0); // left click
	/**
	 * Keybind for zoom in
	 */
	@NotNull
	public Keybind KEYBIND_ZOOM_IN = new KeyKeybind(61, KeyEvent.CTRL); // CTRL + +
	/**
	 * Keybind for zoom out
	 */
	@NotNull
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
	 * How many ticks between recalculating the AI for an entity
	 */
	public final int CALCULATE_AI_EVERY_TICKS = 30;
	/**
	 * Width of powerup displays on the HUD
	 */
	public final float POWERUP_HUD_WIDTH = 100;
	/**
	 * Height of powerup displays on the HUD
	 */
	public final float POWERUP_HUD_HEIGHT = 80;
	/**
	 * Y coordinate of the powerup hud
	 */
	public final float POWERUP_HUD_Y = 200;
	/**
	 * Width of powerup in world
	 */
	public final float POWERUP_WIDTH = 20;
	/**
	 * Height of powerup in world
	 */
	public final float POWERUP_HEIGHT = 20;
	/**
	 * Powerup text size
	 */
	public final float POWERUP_TEXT_SIZE = 5;
	/**
	 * Powerup text size in the HUD
	 */
	public final float POWERUP_HUD_TEXT_SIZE = 30;
	/**
	 * Padding from top of box for powerup text
	 */
	public final float POWERUP_HUD_TEXT_PADDING_TOP = 10;
	/**
	 * Padding from top of box for powerup timer text
	 */
	public final float POWERUP_HUD_TIMER_PADDING_TOP = 50;
	/**
	 * Powerup text colour
	 */
	public final Color POWERUP_TEXT_COLOR = Color.BLACK;
	/**
	 * Separation between the tops of powerups on the HUD
	 */
	public final float POWERUP_HUD_SEPARATION = 100;

	//OPTIONS
	/**
	 * Whether the player can kill their own family, or themself
	 */
	public boolean FRIENDLY_FIRE = false;
	/**
	 * Whether to show lines representing paths of entities
	 */
	public boolean SHOW_PATHFINDING_INFO = false;
}
