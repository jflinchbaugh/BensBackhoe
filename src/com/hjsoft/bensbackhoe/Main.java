package com.hjsoft.bensbackhoe;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class Main extends MIDlet {
	private final BenGameCanvas canvas;
	public Main() {
		canvas = new BenGameCanvas(this);
		Display.getDisplay(this).setCurrent(canvas);
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		canvas.stop();
	}

	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {
		canvas.start();
	}
}