package cs4303.p2;

import cs4303.p2.menu.MenuScreen;
import cs4303.p2.util.builder.EllipseBuilder;
import cs4303.p2.util.builder.LineBuilder;
import cs4303.p2.util.builder.RectBuilder;
import cs4303.p2.util.builder.TextBuilder;
import cs4303.p2.util.collisions.Rectangle;
import cs4303.p2.util.screen.Screen;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.Random;

/**
 * Main class. This extends the Properties class, so it can inherit all of the definitions in this file, without
 * resulting in cluttering the main file.
 */
public class Main extends Properties implements Rectangle {

	/**
	 * Random instance
	 */
	public final Random random = new Random();

	/**
	 * Current screen
	 */
	private Screen screen;
	/**
	 * TextBuilder instance, cached to avoid multiple short-lived instances every frame
	 */
	private TextBuilder textBuilder;
	/**
	 * EllipseBuilder instance, cached to avoid multiple short-lived instances every frame
	 */
	private EllipseBuilder ellipseBuilder;
	/**
	 * RectBuilder instance, cached to avoid multiple short-lived instances every frame
	 */
	private RectBuilder rectBuilder;
	/**
	 * RectBuilder instance, cached to avoid multiple short-lived instances every frame
	 */
	private LineBuilder lineBuilder;

	/**
	 * Change to a new screen
	 *
	 * @param screen screen to change to
	 */
	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	/**
	 * Apply a transformation to render the viewport based on a scale and offset
	 */
	public void applyViewport() {
		if (this.screen != null) {
			this.scale(this.screen.scale());
			this.translate(this.screen.offsetX(), this.screen.offsetY());
		}
	}

	@Override
	public void settings() {
		this.fullScreen();
		//Use anti-aliasing
		this.smooth(8);
	}

	@Override
	public void setup() {
		//Instantiate builders
		this.textBuilder = new TextBuilder(this);
		this.rectBuilder = new RectBuilder(this);
		this.ellipseBuilder = new EllipseBuilder(this);
		this.lineBuilder = new LineBuilder(this);
		this.screen = new MenuScreen(this);
		//Create menu
		this.screen = new MenuScreen(this);
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
			this.screen.mouseReleased(event);
		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (this.screen != null) {
			this.screen.mouseClicked(event);
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		if (this.screen != null) {
			this.screen.mouseDragged(event);
		}
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		if (this.screen != null) {
			this.screen.mouseMoved(event);
		}
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		if (this.screen != null) {
			this.screen.mouseEntered(event);
		}
	}

	@Override
	public void mouseExited(MouseEvent event) {
		if (this.screen != null) {
			this.screen.mouseExited(event);
		}
	}

	@Override
	public void mouseWheel(MouseEvent event) {
		if (this.screen != null) {
			this.screen.mouseWheel(event);
		}
	}

	/**
	 * Get a textBuilder to display text
	 *
	 * @param text text to display
	 *
	 * @return textBuilder builder instance
	 */
	public TextBuilder text(String text) {
		return this.textBuilder.clear()
			.text(text);
	}

	/**
	 * Get a ellipseBuilder to draw a circle
	 *
	 * @return ellipseBuilder instance
	 */
	public EllipseBuilder ellipse() {
		return this.ellipseBuilder.clear();
	}

	/**
	 * Get a rectBuilder to draw a rectangle
	 *
	 * @return rectBuilder instance
	 */
	public RectBuilder rect() {
		return this.rectBuilder.clear();
	}

	/**
	 * Get a lineBuilder to draw a line
	 *
	 * @return lineBuilder instance
	 */
	public LineBuilder line() {
		return this.lineBuilder.clear();
	}

	@Override
	public float minX() {
		return 0;
	}

	@Override
	public float minY() {
		return 0;
	}

	@Override
	public float width() {
		return this.width;
	}

	@Override
	public float height() {
		return this.height;
	}

	/**
	 * Entry point for the program
	 *
	 * @param args cli args
	 */
	public static void main(String[] args) {
		PApplet.main(Main.class, args);
	}

}
