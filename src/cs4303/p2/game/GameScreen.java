package cs4303.p2.game;

import cs4303.p2.Main;
import cs4303.p2.game.entity.family.Child;
import cs4303.p2.game.entity.family.Grandparent;
import cs4303.p2.game.entity.family.Parent;
import cs4303.p2.game.entity.robot.FamilySeekingRobot;
import cs4303.p2.game.entity.robot.MutatingRobot;
import cs4303.p2.game.entity.robot.PlayerSeekingRobot;
import cs4303.p2.game.entity.robot.Robot;
import cs4303.p2.game.level.Level;
import cs4303.p2.game.level.LevelInfo;
import cs4303.p2.game.subscreens.DiedScreen;
import cs4303.p2.game.subscreens.GameOverScreen;
import cs4303.p2.game.subscreens.PauseScreen;
import cs4303.p2.game.subscreens.WaveCompleteScreen;
import cs4303.p2.util.annotation.NotNull;
import cs4303.p2.util.builder.TextBuilder;
import cs4303.p2.util.screen.DeferredDrawTarget;
import cs4303.p2.util.screen.DrawTarget;
import cs4303.p2.util.screen.Screen;
import processing.core.PConstants;
import processing.core.PVector;
import processing.event.Event;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.LinkedList;

/**
 * Screen shown to the user when playing the game
 */
public class GameScreen implements Screen, DeferredDrawTarget {

	/**
	 * Main instance
	 */
	public final Main main;
	/**
	 * Current wave number
	 */
	public final int wave;
	/**
	 * Player instance
	 */
	@NotNull
	public final Player player;
	/**
	 * The current level
	 */
	public Level level;
	/**
	 * The current zoom level
	 */
	private float scale;

	/**
	 * Current score
	 */
	public int score;
	/**
	 * Current lives
	 */
	public int lives;
	/**
	 * Time that the last frame was drawn, according to {@link System#currentTimeMillis()}
	 */
	public long lastFrameMillis;
	/**
	 * Time since the last frame, difference between {@link #lastFrameMillis} for successive frames
	 */
	public long deltaTime;
	/**
	 * Milliseconds since the start of the game
	 */
	public long gameTimeMillis;

	/**
	 * List storing any robots which are created during the update cycle. Adding these during the cycle would cause a
	 * {@link java.util.ConcurrentModificationException}, so instead we add them to this list, then purge the list after
	 * the update cycle.
	 */
	private final LinkedList<Robot> newRobots = new LinkedList<>();

	/**
	 * Create a game instance with existing score, lives etc
	 *
	 * @param main  main instance
	 * @param wave  wave number
	 * @param score current score
	 * @param lives current number of lives remaining
	 */
	public GameScreen(Main main, int wave, int score, int lives) {
		this.main = main;
		this.wave = wave;
		this.score = score;
		this.lives = lives;

		this.level = new Level(this, this.generateLevelInfo());

		this.player = new Player(this, this.level.startingRoom.centre());

		this.scale = this.main.INITIAL_ZOOM;
	}

	/**
	 * Create a new game for wave 1 with no score
	 *
	 * @param main Main instance
	 */
	public GameScreen(@NotNull Main main) {
		this(main, 1, 0, main.STARTING_LIVES);
	}

	@Override
	public void draw() {
		this.update();
		this.justDraw();
	}

	/**
	 * Just draw the game, without performing any update
	 */
	public void justDraw() {
		long nowMillis = System.currentTimeMillis();
		if (this.lastFrameMillis != 0) {
			this.deltaTime = nowMillis - this.lastFrameMillis;
		}
		this.lastFrameMillis = nowMillis;
		this.main.background(this.main.GAME_BACKGROUND_COLOR.getRGB());
		this.level.draw();
		this.player.draw();

		this.drawHUD();
	}

	/**
	 * Draw the HUD
	 */
	private void drawHUD() {
		TextBuilder text = this.text("Score: " + this.score)
			.fill(this.main.HUD_TEXT_COLOR)
			.asHUD();
		text.centredHorizontally(0, this.main.HUD_TEXT_HEIGHT, this.main.width)
			.size(this.main.HUD_SCORE_TEXT_SIZE)
			.draw();
		float halfWidth = this.main.width / 2f;
		text.text("Wave: " + this.wave)
			.centredHorizontally(0, this.main.HUD_TEXT_HEIGHT, halfWidth)
			.size(this.main.HUD_OTHER_TEXT_SIZE)
			.draw();
		text.text("Lives: " + this.lives)
			.centredHorizontally(halfWidth, this.main.HUD_TEXT_HEIGHT, halfWidth)
			.draw();
	}

	/**
	 * Update the physics of all objects
	 */
	public void update() {
		//This should technically be after the draw call, but it's probably fine having it here
		this.gameTimeMillis += this.deltaTime;
		this.player.update();
		this.level.update();
		this.level.robots.addAll(this.newRobots);
		this.newRobots.clear();
	}

	@Override
	public void keyPressed(@NotNull KeyEvent event) {
		if (event.getKeyCode() == PConstants.ESC) {
			//Overwrite key to stop process ending
			this.main.key = 0;
			this.main.keyCode = 0;
			//Show the pause screen
			this.main.setScreen(new PauseScreen(this));
			return;
		}
		this.handleKeyOrMouseDown(event);
	}

	@Override
	public void keyReleased(KeyEvent event) {
		this.handleKeyOrMouseUp(event);
	}

	@Override
	public void mousePressed(MouseEvent event) {
		this.handleKeyOrMouseDown(event);
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		this.handleKeyOrMouseUp(event);
	}

	/**
	 * Handle a key down or mouse down event
	 *
	 * @param event key event or mouse event
	 */
	private void handleKeyOrMouseDown(Event event) {
		if (this.main.KEYBIND_ZOOM_IN.test(event)) {
			this.zoomIn();
		} else if (this.main.KEYBIND_ZOOM_OUT.test(event)) {
			this.zoomOut();
		} else if (this.main.KEYBIND_FIRE.test(event)) {
			this.fire();
		} else {
			this.handleUpOrDownEvent(event, true);
		}
	}

	/**
	 * Handle a key or mouse up event
	 *
	 * @param event key or mouse event
	 */
	private void handleKeyOrMouseUp(Event event) {
		this.handleUpOrDownEvent(event, false);
	}

	/**
	 * Handle a key up or key down event, and update the player's movement accordingly
	 *
	 * @param event event
	 * @param down  true if the key / mouse is being pressed, false otherwise
	 */
	private void handleUpOrDownEvent(Event event, boolean down) {
		if (this.main.KEYBIND_MOVE_UP.test(event)) {
			this.player.up = down;
		} else if (this.main.KEYBIND_MOVE_DOWN.test(event)) {
			this.player.down = down;
		} else if (this.main.KEYBIND_MOVE_LEFT.test(event)) {
			this.player.left = down;
		} else if (this.main.KEYBIND_MOVE_RIGHT.test(event)) {
			this.player.right = down;
		}
	}

	/**
	 * Zoom the camera in
	 */
	private void zoomIn() {
		this.scale *= this.main.ZOOM_FACTOR;
	}

	/**
	 * Zoom the camera out
	 */
	private void zoomOut() {
		this.scale /= this.main.ZOOM_FACTOR;
	}

	/**
	 * Fire a projectile from the player
	 */
	private void fire() {
		PVector position = this.player.copyPosition();
		PVector velocity = new PVector(this.main.mouseX, this.main.mouseY);
		this.unconvert(velocity); // Convert the screen coordinates back to world coordinates
		velocity.sub(position)
			.setMag(this.main.PLAYER_PROJECTILE_MOVEMENT_VELOCITY);
		Projectile projectile = new Projectile(this, position, velocity, this.player);
		this.level.projectiles.add(projectile);
	}

	/**
	 * Trigger a death for the player, and move to the necessary screen
	 */
	public void die() {
		this.lives--;
		if (this.lives <= 0) {
			this.main.setScreen(new GameOverScreen(this));
		} else {
			this.main.setScreen(new DiedScreen(this));
		}
	}

	/**
	 * Respawn the player in the starting room
	 */
	public void respawn() {
		this.player.respawn(this.level.startingRoom.centre());
	}

	/**
	 * Provide an X offset to centre the player on the screen
	 *
	 * @return x offset to centre the player
	 */
	@Override
	public float offsetX() {
		return ((this.main.width / 2f) / this.scale()) - this.player.laggedPosition().x;
	}

	/**
	 * Provide a Y offset to centre the player on the screen
	 *
	 * @return y offset to centre the player
	 */
	@Override
	public float offsetY() {
		return ((this.main.height / 2f) / this.scale()) - this.player.laggedPosition().y;
	}

	/**
	 * Provide a zoom to the view
	 *
	 * @return scale factor for zoom on viewport
	 */
	@Override
	public float scale() {
		return this.scale;
	}

	/**
	 * Generate level parameters for the current wave
	 *
	 * @return level parameters
	 */
	@NotNull
	public LevelInfo generateLevelInfo() {
		float width = this.main.width;
		float height = this.main.height;

		LevelInfo.HumanConstructor[] familyConstructors = {
			Child::new,
			Parent::new,
			Grandparent::new
		};

		LevelInfo.RobotConstructor[] robotConstructors = {
			FamilySeekingRobot::new,
			PlayerSeekingRobot::new,
			MutatingRobot::new
		};

		//Use the same generation as level 2 for level 1
		int effectiveWave = Math.max(this.wave, 2);

		float divisor = (float) (Math.log(effectiveWave) / 4f + 0.2f);

		int maxEntitiesPerRoom = Math.floorDiv(this.wave - 1, 3) + 1;

		return new LevelInfo(
			width * divisor,
			height * divisor,
			192,
			180,
			576,
			540,
			16,
			80,
			0.8f,
			30,
			5,
			25,
			1,
			maxEntitiesPerRoom,
			0,
			1,
			1,
			maxEntitiesPerRoom,
			familyConstructors,
			1,
			maxEntitiesPerRoom,
			robotConstructors,
			0.6f,
			this.wave
		);
	}

	/**
	 * Calculate the score multiplier for the current wave.
	 * <p>
	 * This will be x1 for waves 1 and 2, x2 for 3 and 4, and increase in this way for every 2 levels.
	 *
	 * @return the score multiplier.
	 */
	public int scoreMultiplier() {
		return 1 + Math.floorDiv(this.wave - 1, 2);
	}

	/**
	 * Increase the player's score
	 *
	 * @param score amount to increase score by (before any multiplier)
	 */
	public void addScore(int score) {
		this.score += score * this.scoreMultiplier();
	}

	/**
	 * Create a game instance for the next wave
	 *
	 * @return game instance for the next wave
	 */
	@NotNull
	public GameScreen nextWave() {
		GameScreen next = new GameScreen(this.main, this.wave + 1, this.score, this.lives);
		next.scale = this.scale;
		return next;
	}

	//Defer drawing via builders to the main instance
	@Override
	public DrawTarget deferRenderingTo() {
		return this.main;
	}

	/**
	 * Check if the round is over, and if so move onto the next wave
	 */
	public void checkRoundOver() {
		if (this.level.robots.isEmpty()) {
			this.main.setScreen(new WaveCompleteScreen(this));
		}
	}

	/**
	 * Add a robot to the temporary list, which will be added to the main list after the update cycle has finished
	 *
	 * @param robot robot to add
	 */
	public void addRobot(Robot robot) {
		this.newRobots.add(robot);
	}
}
