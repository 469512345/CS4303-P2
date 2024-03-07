package cs4303.p2.util.keybind;

import processing.event.Event;
import processing.event.KeyEvent;

/**
 * A keyboard keybinding
 */
final class KeyKeybind implements Keybind {

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
	KeyKeybind(int keyCode, int modifiers) {
		this.keyCode = keyCode;
		this.modifiers = modifiers;
	}

	/**
	 * Create a keybinding to mirror a key event
	 *
	 * @param event event to mirror
	 */
	KeyKeybind(KeyEvent event) {
		this(event.getKeyCode(), event.getModifiers());
	}

	@Override
	public boolean test(Event event) {
		if (event instanceof KeyEvent) {
			//Processing doesn't seem to allow Java 16+ pattern matching with instanceof, even though it runs java 17...
			KeyEvent keyEvent = (KeyEvent) event;
			return keyEvent.getKeyCode() == this.keyCode && keyEvent.getModifiers() == this.modifiers;
		}
		return false;
	}

}
