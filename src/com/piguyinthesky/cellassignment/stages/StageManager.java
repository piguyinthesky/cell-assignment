package com.piguyinthesky.cellassignment.stages;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.piguyinthesky.cellassignment.main.CellGame;

public class StageManager {

	private Stage[] stages;
	private int currStage;

	public static final int NUMSTAGES = 15;
	
	public static final int MAINMENU = 0;
//	public static final int NUCLEOLUS = 1;
	public static final int NUCLEUS = 2;
	public static final int RIBOSOME = 3;
	public static final int VESICLE = 4;
	public static final int ROUGHER = 5;
	public static final int GOLGI = 6;
	public static final int CYTOSKELETON = 7;
	public static final int SMOOTHER = 8;
	public static final int MITOCHONDRION = 9;
	public static final int VACUOLE = 10;
//	public static final int CYTOSOL = 11;
	public static final int LYSOSOME = 12;
//	public static final int CENTROSOME = 13;
	public static final int MEMBRANE = 14;

	public StageManager() {
		stages = new Stage[NUMSTAGES];

		currStage = MAINMENU;
		setStage(currStage);
	}

	private void loadStage(int stage) {
		switch (stage) {
		case MAINMENU:
			stages[stage] = new MainMenu(this);
			break;
//		case NUCLEOLUS:
//			stages[stage] = new Nucleolus(this);
//			break;
		case NUCLEUS:
			stages[stage] = new Nucleus(this);
			break;
		case RIBOSOME:
			stages[stage] = new Ribosome(this);
			break;
		case VESICLE:
			stages[stage] = new Vesicle(this);
			break;
		case ROUGHER:
			stages[stage] = new RoughER(this);
			break;
		case GOLGI:
			stages[stage] = new Golgi(this);
			break;
		case CYTOSKELETON:
			stages[stage] = new Cytoskeleton(this);
			break;
		case SMOOTHER:
			stages[stage] = new SmoothER(this);
			break;
		case MITOCHONDRION:
			stages[stage] = new Mitochondrion(this);
			break;
		case VACUOLE:
			stages[stage] = new Vacuole(this);
			break;
//		case CYTOSOL:
//			stages[stage] = new Cytosol(this);
//			break;
		case LYSOSOME:
			stages[stage] = new Lysosome(this);
			break;
//		case CENTROSOME:
//			stages[stage] = new Centrosome(this);
//			break;
		case MEMBRANE:
			stages[stage] = new Membrane(this);
			break;
		default:
			stages[stage] = new MainMenu(this);
			break;
		}
	}

	private void unloadStage(int stage) {
		stages[stage] = null;
	}

	public void setStage(int stage) {
		unloadStage(currStage);
		currStage = stage;
		loadStage(currStage);
	}

	public void update() {
		if (stages[currStage] != null)
			stages[currStage].update();
	}

	public void draw(Graphics2D g) {
		if (stages[currStage] != null) {
			stages[currStage].draw(g);
		} else {
			GradientPaint gp = new GradientPaint(0, 0, Color.RED, CellGame.WIDTH, CellGame.HEIGHT, Color.GREEN);
			g.setPaint(gp);
			g.draw(new Rectangle(0, 0, CellGame.WIDTH, CellGame.HEIGHT));
		}
	}

}
