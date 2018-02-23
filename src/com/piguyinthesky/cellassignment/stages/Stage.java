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

	protected Area[] bounds;
	protected final int BACK = 0;
	
	public final int margin = CellGame.HEIGHT / 48;

	public Stage(StageManager sm) {
		this.sm = sm;
		this.name = this.getClass().getSimpleName();

		bounds = new Area[1];
		bounds[BACK] = new Area(new Rectangle(0, 0, 64, 64));
	}

	protected void update() {
		handleInput();
	};

	protected void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, CellGame.WIDTH, CellGame.HEIGHT);
		
		g.drawImage(CellGame.images[CellGame.ARROW], 0, 0, 64, 64, null);
		
		g.setFont(new Font("Impact", Font.BOLD, 32));
		FontMetrics fm = g.getFontMetrics();
		int x = (CellGame.WIDTH - fm.stringWidth(name)) / 2;
		
		g.setColor(Color.WHITE);
		g.drawString(name, x, margin + fm.getHeight());
	};

	protected void handleInput() {
		if (clickedIn(bounds[BACK])) {
			sm.setStage(StageManager.MAINMENU);
		}
	};

	protected boolean clickedIn(Area a) {
		if (a.contains(InputHandler.startPos) && a.contains(InputHandler.releasePos))
			return true;
		return false;
	}

}
