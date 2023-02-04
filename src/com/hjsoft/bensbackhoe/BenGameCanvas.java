package com.hjsoft.bensbackhoe;

import java.util.Random;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;

public class BenGameCanvas extends GameCanvas 
		implements Runnable, CommandListener {
	private static final int DELAY = 50;
	private static final int MIN_JEWEL = 3;
	static final int SPEED = 2;
	private static final int NUM_CLOUDS = 3;
	private static final int MIN_TREE = 0;
	static final int TREE_SPACING = 20;
	private boolean running = false;

	private Truck truck;
	
	private final int height;
	final int width;
	private boolean[][] holes; 
	private boolean[] jewels;
	private int jewelCount;
	
	
	int[] trees;
	private int treeCount;
	private final Random r = new Random(System.currentTimeMillis());
	private final MIDlet midlet;
	private final Command exit;
	private int score = 0;
	private String scoreString = "0";
	private int[][] clouds;
	
	public BenGameCanvas(final MIDlet midlet) {
		super(true);
		this.midlet = midlet;
		height = getHeight();
		width = getWidth();

		truck = new Truck(4, height / 2, 0, width);
		
		initHoles();
		initJewels();
		initClouds();
		initTrees();
		
		exit = new Command("Exit", Command.BACK, 1);
		addCommand(exit);
		setCommandListener(this);
	}
	
	private void initTrees() {
		trees = new int[width / TREE_SPACING];
		for (int i = MIN_TREE; i < trees.length; i ++) {
			// near truck
			final int truckLeft = (truck.getX())/ TREE_SPACING;
			final int truckRight = (truck.getX() + 20) / TREE_SPACING;

			if (i >= truckLeft && i <= truckRight) {
				continue;
			}
			
			if (r.nextInt() % 3 == 0) {
				trees[i] = 2;
				treeCount ++;
			}
		}
	}

	private void initClouds() {
		clouds = new int[NUM_CLOUDS][4];
		for (int i = 0; i < NUM_CLOUDS; i ++) {
			final int[] cloud = clouds[i];
			cloud[0] = (int) (Math.abs(r.nextInt()) * (long) width
					/ Integer.MAX_VALUE);
			cloud[1] = (int) (Math.abs(r.nextInt()) * (long) height / 3
					/ Integer.MAX_VALUE);
			cloud[2] = (int) (Math.abs(r.nextInt()) * (long) width / 4 
					/ Integer.MAX_VALUE) + width / 4;
			cloud[3] = (int) (Math.abs(r.nextInt()) * (long) height / 12
					/ Integer.MAX_VALUE) + height / 12;
		}
	}

	private void initHoles() {
		holes = new boolean[width / 5][2];
	}
	
	private void initJewels() {
		jewels = new boolean[width / 5 ];
		for (int i = MIN_JEWEL; i < jewels.length; i ++) {
			if (r.nextInt() % 3 == 0) {
				jewels[i] = true;
				jewelCount ++;
			}
		}
	}

	public void stop() {
		running = false;
	}

	public void run() {
		final Graphics g = getGraphics();
		while (running) {
			final long start = System.currentTimeMillis();
			moveTruck();
			
			if (System.currentTimeMillis() - start > DELAY) {
				continue;
			}
			
			render(g);
			flushGraphics();

			sleep(start);
		}
	}
	
	private void sleep(final long start) {
		final long passed = System.currentTimeMillis() - start;
		
		if (passed >= DELAY) {
			return;
		}
		try {
			Thread.sleep(DELAY - passed);
		} catch (InterruptedException iEx) {
		}
	}

	private void moveTruck() {
		final int ks = getKeyStates(); 
		if ((ks & LEFT_PRESSED) != 0) {
			moveLeft();
		}
		if ((ks & RIGHT_PRESSED) != 0) {
			moveRight();
		}
		
		if ((ks & FIRE_PRESSED) != 0) {
			truck.startScoop();
		}
		if ((ks & DOWN_PRESSED) != 0) {
			truck.startScoop();
		}

		if (truck.hasDug()) {
			dig();
		}

		truck.moveScoop();
	}

	private void dig() {
		digHole(truck.getX());
		digHole(truck.getX() + 5);
	}

	private void moveLeft() {
		truck.moveLeft(SPEED);
		
		final int treeIndex = truck.getX() / TREE_SPACING;
		if (treeIndex < 0) {
			return;
		}
		
		if (hitTree(treeIndex)) {
			truck.moveRight(SPEED * 2);
		}
	}
	
	boolean hitTree(final int treeIndex) {
		if (trees[treeIndex] == 0) {
			return false;
		}
		
		trees[treeIndex] --;
		if (trees[treeIndex] == 0) {
			treeCount --;
			score += 5;
		}
		
		if (fieldCleared()) {
			resetField();
		}
		
		return treeCount > 0;
	}
	
	private void render(final Graphics g) {
		drawBackground(g);
		drawClouds(g);
		drawScore(g);
		drawTrees(g);
		drawHoles(g);
		drawJewels(g);
		truck.draw(g);
	}

	private void drawTrees(final Graphics g) {
		for (int i = MIN_TREE; i < trees.length; i ++) {
			switch (trees[i]) {
			case 2:
				g.setColor(0x888800);
				g.fillRect(i * 20 + 8, height / 2, 4, 11);
				g.setColor(0x00FF00);
				g.fillArc(i * 20 + 3, height / 2 - 13, 14, 14, 0, 360);
				break;
			case 1:
				g.setColor(0x888800);
				g.fillRect(i * 20 + 8, height / 2, 4, 11);
				break;
			}
		}
	}

	private void drawScore(final Graphics g) {
		g.drawString(scoreString, 0, 0, Graphics.TOP | Graphics.LEFT);
	}

	private void drawClouds(final Graphics g) {
		for (int i = 0; i < NUM_CLOUDS; i ++) {
			g.setColor(0xEEEEEE - 0x111111 * i);
			g.fillArc(clouds[i][0], clouds[i][1], clouds[i][2], clouds[i][3],
					0, 360);
		}
	}

	private void drawJewels(final Graphics g) {
		g.setColor(0xFF0000);
		final int baseY = height / 2 + 20;
		for (int i = MIN_JEWEL; i < jewels.length; i ++) {
			if (jewels[i]) {
				g.fillTriangle(i * 5 + 2, baseY,
						i * 5, baseY + 5, 
						i * 5 + 5, baseY + 5);
				g.fillTriangle(i * 5 + 3, baseY + 10,
						i * 5, baseY + 5,
						i * 5 + 5, baseY + 5);
			}
		}
	}

	private void drawHoles(final Graphics g) {
		g.setColor(0x000000);
		for (int x = 0; x < holes.length; x ++) {
			for (int y = 0; y < holes[x].length; y ++) {
				if (holes[x][y]) {
					g.fillRect(x * 5, y * 10 + height / 2 + 10, 5, 10);
				}
			}
		}
	}
	
	private void drawBackground(final Graphics g) {
		g.setColor(0x0000FF);
		g.fillRect(0, 0, width - 1, height - 1);
		
		g.setColor(0x22FF00);
		g.fillRect(0, height / 2 + 10, width - 1, height / 2 + 10);
	}

	private void digHole(final int holeX) {
		Display.getDisplay(midlet).vibrate(50);
		final int x = (holeX + 15) > width ? width / 5 : (holeX + 15) / 5;
		final int y = holes[x][0] ? 1 : 0;
		holes[x > 0 ? x : 0][y] = true;
		
		if (y == 1) {
			checkJewels(x);
		}
		
		if (fieldCleared()) {
			resetField();
		}
	}

	private boolean fieldCleared() {
		return jewelCount <= 0 && treeCount <= 0;
	}

	private void resetField() {
		initJewels();
		initHoles();
		initClouds();
		initTrees();
	}
	
	private void checkJewels(final int x) {
		if (jewels[x]) {
			jewels[x] = false;
			jewelCount --;
			score += 10;
			scoreString = String.valueOf(score);
		}
	}
	
	public void start() {
		running = true;
		final Thread t = new Thread(this);
		t.start();
	}

	public void commandAction(final Command command, final Displayable d) {
		if (command == exit) {
			midlet.notifyDestroyed();
		}
	}

	void moveRight() {
		truck.moveRight(SPEED);
		
		final int treeIndex = (truck.getX() + 20) / TREE_SPACING;
		if (treeIndex >= trees.length) {
			return;
		}
		
		if (hitTree(treeIndex)) {
			truck.moveLeft(SPEED * 2);
		}
	}
}