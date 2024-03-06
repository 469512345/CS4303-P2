package cs4303.p2;

import cs4303.p2.menu.MenuScreen;
import cs4303.p2.util.screen.Screen;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.Random;

public class Main extends PApplet {

	private Screen screen = new MenuScreen(this);
	public Properties properties = new Properties(this);
	public final Random random = new Random();

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	@Override
	public void settings() {
		this.fullScreen();
	}

	@Override
	public void draw() {
		if (this.screen != null) {
			this.screen.draw();
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (this.screen != null) {
			this.screen.keyPressed(event);
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		if (this.screen != null) {
			this.screen.keyReleased(event);
		}
	}

	@Override
	public void keyTyped(KeyEvent event) {
		if (this.screen != null) {
			this.screen.keyTyped(event);
		}
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if (this.screen != null) {
			this.screen.mousePressed(event);
		}
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (this.screen != null) {
			screen.mouseReleased(event);
		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (this.screen != null) {
			screen.mouseClicked(event);
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		if (this.screen != null) {
			screen.mouseDragged(event);
		}
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		if (this.screen != null) {
			screen.mouseMoved(event);
		}
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		if (this.screen != null) {
			screen.mouseEntered(event);
		}
	}

	@Override
	public void mouseExited(MouseEvent event) {
		if (this.screen != null) {
			screen.mouseExited(event);
		}
	}

	@Override
	public void mouseWheel(MouseEvent event) {
		if (this.screen != null) {
			screen.mouseWheel(event);
		}
	}

	public static void main(String[] args) {
		PApplet.main(Main.class, args);
	}

}
