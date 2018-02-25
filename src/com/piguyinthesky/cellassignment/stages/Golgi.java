package com.piguyinthesky.cellassignment.stages;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Random;

import com.piguyinthesky.cellassignment.handlers.InputHandler;
import com.piguyinthesky.cellassignment.main.CellGame;

public class Golgi extends Stage {

	private int THING = 2;
	private Random random;
	private ArrayList<Point> path;

	public Golgi(StageManager sm) {
		super(sm, 2);

		random = new Random();
		int[] xpoints = new int[8];
		int[] ypoints = new int[xpoints.length];
		for (int i = 0; i < xpoints.length; i++) {
			xpoints[i] = random.nextInt(CellGame.WIDTH);
			ypoints[i] = random.nextInt(CellGame.HEIGHT);
			System.out.println("xpoints[" + i + "]: " + xpoints[i] + ", ypoints[" + i + "]: " + ypoints[i]);
		}
		bounds[THING] = new Area(new Polygon(xpoints, ypoints, xpoints.length));

		path = new ArrayList<Point>();
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(6));
		g.draw(bounds[THING]);
		for (int i = 1; i < path.size(); i++) {
			g.drawLine(path.get(i - 1).x, path.get(i - 1).y, path.get(i).x, path.get(i).y);
		}
	}

	@Override
	public void handleInput() {
		super.handleInput();
		if (InputHandler.pressed) {
			path.add(InputHandler.currPos);
		}
	}

}
