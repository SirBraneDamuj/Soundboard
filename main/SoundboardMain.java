package main;

import org.eclipse.swt.widgets.Display;

import player.Soundboard;

public class SoundboardMain {

	public static void main(String[] args) {
		
		Display display = new Display();
		Soundboard board = new Soundboard(display);
		board.open();
		while (!board.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}

	}

}
