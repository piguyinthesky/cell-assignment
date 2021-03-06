package com.piguyinthesky.cellassignment.stages;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;

import com.piguyinthesky.cellassignment.handlers.InputHandler;
import com.piguyinthesky.cellassignment.main.CellGame;

public abstract class Stage {

	protected StageManager sm;
	protected String name;
	protected String description;

	protected Area[] bounds;
	protected final int BACK = 0;
	protected final int NAME = 1;

	protected final int TL = 0;
	protected final int TC = 1;
	protected final int TR = 2;
	protected final int ML = 3;
	protected final int C = 4;
	protected final int MR = 5;
	protected final int BL = 6;
	protected final int BC = 7;
	protected final int BR = 8;

	protected boolean showdesc = false;

	public Stage(StageManager sm) {
		this(sm, 0);
	}

	public Stage(StageManager sm, int numBounds) {
		this.sm = sm;
		this.name = this.getClass().getSimpleName();
		if (numBounds < 1)
			numBounds = 1;
		bounds = new Area[2 + numBounds];
		bounds[BACK] = new Area(new Rectangle(0, 0, 64, 64));
	}

	public void drawCenteredString(String text, Rectangle r, Graphics2D g, int position) {
		FontMetrics fm = g.getFontMetrics();
		int x = (r.width - fm.stringWidth(text)) / 2;
		int y = (r.height - fm.getHeight()) / 2 + fm.getAscent();
		g.drawString(text, x, y);
	}

	protected void update() {
		handleInput();
	};

	protected void showDescription(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, CellGame.WIDTH, CellGame.HEIGHT);
	}

	protected void draw(Graphics2D g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, CellGame.WIDTH, CellGame.HEIGHT);

		g.drawImage(CellGame.images[CellGame.ARROW], 0, 0, 64, 64, null);

		g.setFont(new Font("Impact", Font.BOLD, 32));
		FontMetrics fm = g.getFontMetrics();
		int x = (CellGame.WIDTH - fm.stringWidth(name)) / 2;
		int y = CellGame.MARGIN + fm.getHeight();

		if (bounds[NAME] == null) {
			bounds[NAME] = new Area(
					new Rectangle(x, y - fm.getHeight() + CellGame.MARGIN, fm.stringWidth(name), fm.getAscent()));
		}

		g.setColor(Color.WHITE);
		g.drawString(name, x, y);
	};

	protected void handleInput() {
		if (clickedIn(bounds[BACK])) {
			sm.setStage(StageManager.MAINMENU);
		}
		if (bounds[NAME] != null) {
			if (bounds[NAME].contains(InputHandler.currPos))
				showdesc = true;
			else
				showdesc = false;
		}
	};

	protected boolean clickedIn(Area a) {
		if (a.contains(InputHandler.startPos) && a.contains(InputHandler.releasePos))
			return true;
		return false;
	}

}
