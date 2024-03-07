package cs4303.p2.game.level;

import cs4303.p2.Main;
import cs4303.p2.util.builder.LineBuilder;

import java.awt.Color;

public class Corridor {

	private final Main main;
	private final LeafRoom room1;
	private final LeafRoom room2;
	private final Axis axis;
	private final float width;

	public Corridor(Main main, LeafRoom room1, LeafRoom room2, Axis axis, float width) {
		this.main = main;
		this.room1 = room1;
		this.room2 = room2;
		this.axis = axis;
		this.width = width;
	}

	void draw() {
		float halfStrokeWeight = (float) Math.ceil(this.width / 2f);

		LineBuilder line = main.line()
			.stroke(Color.WHITE)
			.strokeWeight(this.width);

		if (this.axis == Axis.VERTICAL) {
			float centreX = (room1.maxX + room2.minX) / 2;
			float child1CentreY = room1.centreY();
			float child2CentreY = room2.centreY();

			line
				.from(room1.maxX, child1CentreY)
				.to(centreX + halfStrokeWeight, child1CentreY)
				.draw();

			line
				.from(centreX, child1CentreY)
				.to(centreX, child2CentreY)
				.draw();

			line
				.from(room2.minX, child2CentreY)
				.to(centreX - halfStrokeWeight, child2CentreY)
				.draw();

		} else {
			float centreY = (room1.maxY + room2.minY) / 2;
			float child1CentreX = room1.centreX();
			float child2CentreX = room2.centreX();

			line
				.from(child1CentreX, room1.maxY)
				.to(child1CentreX, centreY + halfStrokeWeight)
				.draw();

			line
				.from(child1CentreX, centreY)
				.to(child2CentreX, centreY)
				.draw();

			line
				.from(child2CentreX, room2.minY)
				.to(child2CentreX, centreY - halfStrokeWeight)
				.draw();
		}
	}
}
