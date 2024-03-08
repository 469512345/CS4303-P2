package cs4303.p2.game.level;

import cs4303.p2.Main;
import cs4303.p2.util.builder.LineBuilder;
import processing.core.PVector;

import java.awt.Color;
import java.util.ArrayList;

public class Corridor {

	private final Main main;
	private final LeafRoom room1;
	private final LeafRoom room2;
	private final Axis axis;
	private final float width;
	private final ArrayList<PVector> points;

	public Corridor(Main main, LeafRoom room1, LeafRoom room2, Axis axis, float width) {
		this.main = main;
		this.room1 = room1;
		this.room2 = room2;
		this.axis = axis;
		this.width = width;
		this.points = new ArrayList<>();

		if (this.axis == Axis.VERTICAL) {
			float centreX = (room1.maxX + room2.minX) / 2;
			float child1CentreY = room1.centreY();
			float child2CentreY = room2.centreY();

			this.points.add(new PVector(room1.maxX, child1CentreY));
			this.points.add(new PVector(centreX, child1CentreY));
			this.points.add(new PVector(centreX, child2CentreY));
			this.points.add(new PVector(room2.minX, child2CentreY));

		} else {
			float centreY = (room1.maxY + room2.minY) / 2;
			float child1CentreX = room1.centreX();
			float child2CentreX = room2.centreX();

			this.points.add(new PVector(child1CentreX, room1.maxY));
			this.points.add(new PVector(child1CentreX, centreY));
			this.points.add(new PVector(child2CentreX, centreY));
			this.points.add(new PVector(child2CentreX, room2.minY));

		}
	}

	void draw() {
		LineBuilder line = main.line()
			.stroke(Color.WHITE)
			.strokeWeight(this.width);

		for (int i = 1; i < this.points.size(); i++) {
			line.from(this.points.get(i - 1))
				.to(this.points.get(i))
				.draw();
		}
	}
}
