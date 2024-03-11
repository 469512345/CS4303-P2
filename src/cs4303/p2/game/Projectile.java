package cs4303.p2.game;

import cs4303.p2.util.collisions.Circle;
import processing.core.PVector;

public class Projectile implements Circle {

	private final GameScreen main;
	private final PVector position;
	private final PVector velocity;

	public Projectile(GameScreen main, PVector position, PVector velocity) {
		this.main = main;
		this.position = position;
		this.velocity = velocity;
	}

	@Override
	public float centreX() {
		return 0;
	}

	@Override
	public float centreY() {
		return 0;
	}

	@Override
	public float radius() {
		return 0;
	}
}
