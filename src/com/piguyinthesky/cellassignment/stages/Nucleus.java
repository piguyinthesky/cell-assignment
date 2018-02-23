package com.piguyinthesky.cellassignment.stages;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;

import com.piguyinthesky.cellassignment.main.CellGame;

public class Nucleus extends Stage {

	public Nucleus(StageManager sm) {
		super(sm);
		bounds = new Area[1];
		bounds[BACK] = new Area(new Rectangle(0, 0, 64, 64));
	}

	@Override
	public void update() {
		handleInput();
	}

	@Override
	public void draw(Graphics2D g) {
		GradientPaint gp = new GradientPaint(0, 0, Color.BLUE, CellGame.WIDTH, CellGame.HEIGHT, Color.RED);
		g.setPaint(gp);
		g.fill(new Rectangle(0, 0, CellGame.WIDTH, CellGame.HEIGHT));
		g.drawImage(CellGame.images[CellGame.ARROW], 0, 0, 64, 64, null);
	}

	@Override
	public void handleInput() {
		super.handleInput();
		if (clickedIn(bounds[BACK])) {
			sm.setStage(StageManager.MAINMENU);
		}
	}

}
