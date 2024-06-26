package cs4303.p2.util.keybind;

import cs4303.p2.util.annotation.NotNull;
import processing.event.Event;
import processing.event.KeyEvent;

/**
 * A keyboard keybinding
 */
public final class KeyKeybind implements Keybind {

	/**
	 * keyCode, according to {@link KeyEvent#getKeyCode()}.
	 */
	private final int keyCode;

	/**
	 * Modifiers, according to {@link KeyEvent#getModifiers()}.
	 */
	private final int modifiers;

	/**
	 * @param keyCode   keyCode of the event
	 * @param modifiers modifiers of the event
	 */
	public KeyKeybind(int keyCode, int modifiers) {
		this.keyCode = keyCode;
		this.modifiers = modifiers;
	}

	/**
	 * Create a keybinding to mirror a key event
	 *
	 * @param event event to mirror
	 */
	public KeyKeybind(@NotNull KeyEvent event) {
		this(event.getKeyCode(), event.getModifiers());
	}

	@Override
	public boolean test(Event event) {
		if (event instanceof KeyEvent keyEvent) {
			//Processing doesn't seem to allow Java 16+ pattern matching with instanceof, even though it runs java 17...
			return keyEvent.getKeyCode() == this.keyCode && keyEvent.getModifiers() == this.modifiers;
		}
		return false;
	}

}
