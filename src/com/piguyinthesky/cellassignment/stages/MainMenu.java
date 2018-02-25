package com.piguyinthesky.cellassignment.stages;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;

import com.piguyinthesky.cellassignment.handlers.InputHandler;
import com.piguyinthesky.cellassignment.main.CellGame;

public class MainMenu extends Stage {

	private Area[] bounds;
	
	public MainMenu(StageManager sm) {
		super(sm);
		bounds = new Area[14];
		initBounds();
	}

	public void initBounds() {
		bounds[StageManager.NUCLEUS] = new Area(new Rectangle(400, 20, 190, 130));
		bounds[StageManager.RIBOSOME] = new Area(new Rectangle(610, 125, 10, 10));
//		bounds[StageManager.VESICLE] = new Area(new Rectangle(400, 20, 190, 130));
//		bounds[StageManager.ROUGHER] = new Area(new Rectangle(400, 20, 190, 130));
		bounds[StageManager.GOLGI] = new Area(new Rectangle(420, 240, 550, 360));
//		bounds[StageManager.CYTOSKELETON] = new Area(new Rectangle(400, 20, 190, 130));
//		bounds[StageManager.SMOOTHER] = new Area(new Rectangle(400, 20, 190, 130));
//		bounds[StageManager.MITOCHONDRION] = new Area(new Rectangle(400, 20, 190, 130));
//		bounds[StageManager.VACUOLE] = new Area(new Rectangle(400, 20, 190, 130));
//		bounds[StageManager.CYTOSOL] = new Area(new Rectangle(400, 20, 190, 130));
//		bounds[StageManager.LYSOSOME] = new Area(new Rectangle(400, 20, 190, 130));
//		bounds[StageManager.CENTROSOME] = new Area(new Rectangle(400, 20, 190, 130));
//		bounds[StageManager.MEMBRANE] = new Area(new Rectangle(400, 20, 190, 130));
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		GradientPaint gp = new GradientPaint(0, 0, Color.BLUE, CellGame.WIDTH, CellGame.HEIGHT, Color.RED);
		g.setPaint(gp);
		g.fill(new Rectangle(0, 0, CellGame.WIDTH, CellGame.HEIGHT));
		g.drawImage(CellGame.images[CellGame.ANIMALCELL], 0, 0, CellGame.WIDTH, CellGame.HEIGHT, null);
		
		Font titleFont = new Font("Impact", Font.BOLD, 64);
		g.setColor(Color.WHITE);
		g.setFont(titleFont);
		drawCenteredText(g, "The Cell: Games by Alex C");
	}

	public void drawCenteredText(Graphics2D g, String txt) {
		FontMetrics fm = g.getFontMetrics();
		int x = (CellGame.WIDTH - fm.stringWidth(txt)) / 2;
		int y = CellGame.HEIGHT - fm.getHeight() + fm.getAscent();
		g.drawString(txt, x, y);
	}

	public void handleInput() {
		super.handleInput();
		
		if (InputHandler.pressed)
			System.out.println(InputHandler.currPos.toString());
		
		if (clickedIn(bounds[StageManager.NUCLEUS])) {
			sm.setStage(StageManager.NUCLEUS);
		} else if (clickedIn(bounds[StageManager.RIBOSOME])) {
			sm.setStage(StageManager.RIBOSOME);
		} else if (clickedIn(bounds[StageManager.GOLGI])) {
			sm.setStage(StageManager.GOLGI);
		}
	}

}
