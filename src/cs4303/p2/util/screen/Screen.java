package cs4303.p2.util.screen;

import cs4303.p2.Main;
import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public interface Screen {

	Main main();

	void draw();

	default void keyPressed(KeyEvent event) {

	}

	default void keyTyped(KeyEvent event) {

	}

	default void keyReleased(KeyEvent event) {

	}

	default void mousePressed(MouseEvent event) {

	}

	default void mouseReleased(MouseEvent event) {

	}

	default void mouseClicked(MouseEvent event) {

	}

	default void mouseDragged(MouseEvent event) {

	}

	default void mouseMoved(MouseEvent event) {

	}

	default void mouseEntered(MouseEvent event) {

	}

	default void mouseExited(MouseEvent event) {

	}

	default void mouseWheel(MouseEvent event) {

	}

	default float scale() {
		return 1;
	}

	default float offsetX() {
		return 0;
	}

	default float offsetY() {
		return 0;
	}

}
