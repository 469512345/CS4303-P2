package cs4303.p2.game;

import cs4303.p2.util.collisions.Circle;
import processing.core.PVector;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;

public class Player implements Circle {

	public static final int STEP_INDEX = 50;
	public static final int CAMERA_LAG_FRAMES = 15;
	public static final float MOVEMENT_VELOCITY = 0.8f; // pixels / frame
	public static final float PLAYER_RADIUS = 8f;

	private final GameScreen game;
	private final PVector position;
	private final PVector velocity = new PVector();
	private final Queue<PVector> positionHistory = new LinkedList<>();
	public boolean left = false;
	public boolean right = false;
	public boolean up = false;
	public boolean down = false;


	public Player(GameScreen game, PVector position) {
		this.game = game;
		this.position = position;
	}

	public void draw() {
		this.game.main()
			.ellipse()
			.copy(this)
			.fill(Color.MAGENTA)
			.draw();
	}

	public void update() {
		if (this.positionHistory.size() > CAMERA_LAG_FRAMES) {
			this.positionHistory.poll();
		}
		//Initial velocity of 0
		float velocityX = 0;
		float velocityY = 0;

		//Each key down will add a component to velocity
		if (this.left) {
			velocityX = -STEP_INDEX;
		}
		if (this.right) {
			velocityX = STEP_INDEX;
		}
		if (this.up) {
			velocityY = -STEP_INDEX;
		}
		if (this.down) {
			velocityY = STEP_INDEX;
		}

		//Set the velocity
		this.velocity.set(velocityX, velocityY)
			//Set the magnitude if non-zero. stops left+up moving at sqrt(2) * speed
			.setMag(MOVEMENT_VELOCITY);
		this.position.add(this.velocity);

		//Add this new position to the history
		this.positionHistory.add(this.position.copy());
	}

	public PVector laggedPosition() {
		return positionHistory.peek();
	}

	@Override
	public float centreX() {
		return this.position.x;
	}

	@Override
	public float centreY() {
		return this.position.y;
	}

	@Override
	public float radius() {
		return PLAYER_RADIUS;
	}
}
