package cs4303.p2;

public class Properties {

	private final Main main;

	public Properties(Main main) {
		this.main = main;
	}

	public final float roomMarginMax = 80;
	public final float roomMarginMin = 5;

	public float roomMinHeight() {
		return this.main.height / 6f;
	}

	public float roomMinWidth() {
		return this.main.width / 10f;
	}
}
