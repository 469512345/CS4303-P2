package cs4303.p2.game;

import processing.core.PVector;

import java.awt.Color;

public class Player {

	public static final int STEP_INDEX = 50;

	private final GameScreen game;
	public final PVector position;

	public Player(GameScreen game, PVector position) {
		this.game = game;
		this.position = position;
	}

	public void draw() {
		this.game.main()
			.ellipse()
			.at(position)
			.radius(8)
			.fill(Color.MAGENTA)
			.draw();
	}

	public void up() {
		this.position.sub(0, STEP_INDEX);
	}

	public void down() {
		this.position.add(0, STEP_INDEX);
	}

	public void left() {
		this.position.sub(STEP_INDEX, 0);
	}

	public void right() {
		this.position.add(STEP_INDEX, 0);
	}
}
