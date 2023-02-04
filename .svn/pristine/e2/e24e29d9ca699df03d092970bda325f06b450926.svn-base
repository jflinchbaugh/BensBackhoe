package com.hjsoft.bensbackhoe;

import javax.microedition.lcdui.Graphics;

public class Truck {
	private static final int TRUCKWIDTH = 25;
	
	int x;
	int y;
	int scoop;
	int screenWidth;
	
	public Truck(final int x, final int y, final int scoop,
			final int screenWidth) {
		setX(x);
		setY(y);
		setScoop(scoop);
		this.screenWidth = screenWidth;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(final int x) {
		this.x = x;
	}
	
	public void setY(final int y) {
		this.y = y;
	}
	
	public void draw(final Graphics g) {
		g.setColor(0xFFFF00);
		g.fillRect(this.getX(), this.getY(), 10, 10);
		
		switch (scoop) {
		case 0:
			g.drawLine(this.getX() + 10, this.getY() + 10, this.getX() + 20, this.getY());
			g.drawLine(this.getX() + 20, this.getY(), this.getX() + 25, this.getY() + 5);
			g.fillTriangle(this.getX() + 25, this.getY() + 5,
					this.getX() + 28, this.getY() + 5,
					this.getX() + 28, this.getY() + 8);
			break;
		case 1:
		case 2:
		case 3:
			g.drawLine(this.getX() + 10, this.getY() + 10, this.getX() + 20, this.getY() + 10);
			g.drawLine(this.getX() + 20, this.getY() + 10, this.getX() + 25, this.getY() + 20);
			g.fillTriangle(this.getX() + 25, this.getY() + 20,
					this.getX() + 28, this.getY() + 23,
					this.getX() + 25, this.getY() + 25);
			break;
		case 4:
		case 5:
		case 6:
			g.drawLine(this.getX() + 10, this.getY() + 10, this.getX() + 20, this.getY() + 20);
			g.drawLine(this.getX() + 20, this.getY() + 20, this.getX() + 20, this.getY() + 30);
			g.fillTriangle(this.getX() + 20, this.getY() + 30,
					this.getX() + 17, this.getY() + 33,
					this.getX() + 15, this.getY() + 30);
			break;
		}
		
		// bulldozer
		g.drawLine(this.getX(), this.getY() + 8, this.getX() - 3, this.getY() + 8);
		g.setColor(0x888888);
		g.drawLine(this.getX() - 3, this.getY() + 4, this.getX() - 4, this.getY() + 10);
		g.drawLine(this.getX() - 4, this.getY() + 4, this.getX() - 5, this.getY() + 10);
		
		// track
		g.setColor(0x999999);
		g.fillRect(this.getX() - 1, this.getY() + 8, 12, 3);
		
		// windows
		g.setColor(0xEEEEEE);
		g.fillRect(this.getX() + 5, this.getY() + 1, 4, 4);
		g.fillRect(this.getX() + 1, this.getY() + 1, 3, 4);
	}

	public void setScoop(final int scoop) {
		this.scoop = scoop;
	}

	public int getScoop() {
		return scoop;
	}
	
	public void startScoop() {
		if (scoop != 0) {
			return;
		}
		scoop = 1;
	}

	public void moveScoop() {
		if (hasDug()) {
			setScoop(0);
		} else if (getScoop() > 0) {
			setScoop(getScoop() + 1);
		}
	}

	public boolean hasDug() {
		return getScoop() == 6;
	}
	
	public void moveLeft(final int speed) {
		setX(getX() - speed);
		if (getX() < 0) {
			setX(0);
		}
	}
	
	public void moveRight(final int speed) {
		setX(getX() + speed);
		if (getX() > screenWidth - TRUCKWIDTH) {
			setX(screenWidth - TRUCKWIDTH);
		}
	}
}