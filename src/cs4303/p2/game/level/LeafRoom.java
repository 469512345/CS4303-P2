package cs4303.p2.game.level;

import cs4303.p2.Main;

import java.awt.Color;
import java.util.Collection;

public final class LeafRoom extends AbstractRoom {

	public LeafRoom(
		Main main,
		ContainerRoom parent,
		LevelInfo levelInfo,
		float minX,
		float minY,
		float maxX,
		float maxY
	) {
		super(main, parent, levelInfo, minX, minY, maxX, maxY);
	}

	@Override
	public void draw() {
		this.main.rect().
			noStroke()
			.fill(Color.WHITE)
			.copy(this)
			.draw();
	}

	@Override
	public LeafRoom findRoom(Axis axis, AxisDirection direction) {
		return this;
	}

	@Override
	public void appendRooms(Collection<LeafRoom> result) {
		result.add(this);
	}
}