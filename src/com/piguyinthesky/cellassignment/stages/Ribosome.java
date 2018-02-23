package com.piguyinthesky.cellassignment.stages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import com.piguyinthesky.cellassignment.main.CellGame;

public class Ribosome extends Stage {

	private final int ADENINE = 0;
	private final int THYMINE = 1;
	private final int CYTOSINE = 2;
	private final int GUANINE = 3;
	private final int PHOSPHATE = 4;
	private final int GLUCOSE = 5;
	
	private final int SQUARE = 0;
	private final int TRIANGLE = 1;
	private final int CIRCLE = 2;
	private final int TRAPEZOID = 3;
	private final int SMALLSQUARE = 4;
	private Area[] shapes = new Area[6];

	private final int BASEH = CellGame.HEIGHT / 4;

	private final int numPairs = 8;
	private AminoAcid[] aacids = new AminoAcid[numPairs];
	private final int LENGTH = CellGame.WIDTH / (2 * numPairs + 1);
	private AminoAcid[] bases = new AminoAcid[numPairs * 2 + 1];

	private Random random;

	public Ribosome(StageManager sm) {
		super(sm);
		
		createShapes();
		random = new Random();

		for (int i = 0; i < numPairs; i++) {
			aacids[i] = new AminoAcid(random.nextInt(4), LENGTH * (i * 2 + 1));
		}
		
		for (int i = 0; i < bases.length; i++) {
			if (i % 2 == 0) {
				bases[i] = new AminoAcid(PHOSPHATE, LENGTH * i);
			} else {
				bases[i] = new AminoAcid(GLUCOSE, LENGTH * i);
			}
		}
	}

	private void createShapes() {
		shapes[SQUARE] = new Area(new Rectangle(0, 0, LENGTH, LENGTH));
		int[] trixpoints = { 0, LENGTH, LENGTH / 2 };
		int[] triypoints = { 0, 0, LENGTH / 2 };
		shapes[TRIANGLE] = new Area(new Polygon(trixpoints, triypoints, 3));
		shapes[CIRCLE] = new Area(new Ellipse2D.Double(0, 0, LENGTH, LENGTH));
		
		shapes[SMALLSQUARE] = new Area(new Rectangle(0, 0, LENGTH / 3, LENGTH / 3));

		int[] trapxpoints1 = { 0, LENGTH / 3, LENGTH / 3 };
		int[] trapxpoints2 = { LENGTH, LENGTH * 2 / 3, LENGTH * 2 / 3 };
		int[] trapypoints = { 0, 0, LENGTH / 3 };
		
		shapes[TRAPEZOID] = new Area(new Polygon(trapxpoints1, trapypoints, 3));
		shapes[TRAPEZOID].add(new Area(new Polygon(trapxpoints2, trapypoints, 3)));
		shapes[TRAPEZOID].add(shift(shapes[SMALLSQUARE], LENGTH / 3, 0));
	}
	
	private Area shift(Area shape, int x, int y) {
		AffineTransform down = AffineTransform.getTranslateInstance(x, y);
		return new Area(down.createTransformedShape(shape));
	}
	
	private Area getShape(int shape) {
		return (Area) shapes[shape].clone();
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);

		for (int i = 0; i < numPairs; i++) {
			g.setColor(aacids[i].color);
			g.fill(aacids[i].bound);
		}
		
		for (int i = 0; i < bases.length; i++) {
			g.setColor(bases[i].color);
			g.fill(bases[i].bound);
		}
	}

	@Override
	public void handleInput() {
		super.handleInput();
	}

	private class AminoAcid {

		private int id;
		public Color color;

		public Area bound;

		public AminoAcid(int id, int x) {
			this.id = id;
			AffineTransform at = AffineTransform.getTranslateInstance(x, BASEH);

			switch (id) {
			case ADENINE:
				this.color = Color.BLUE;

				Area adenine = getShape(SQUARE);
				adenine.subtract(getShape(TRIANGLE));
				adenine.add(shift(getShape(CIRCLE), 0, LENGTH / 2));
				this.bound = new Area(at.createTransformedShape(adenine));
				break;

			case THYMINE:
				this.color = Color.YELLOW;

				Area thymine = getShape(SQUARE);
				thymine.subtract(getShape(TRIANGLE));
				thymine.add(shift(getShape(SQUARE), 0, LENGTH));
				thymine.add(shift(getShape(CIRCLE), 0, LENGTH * 3 / 2));
				this.bound = new Area(at.createTransformedShape(thymine));
				break;

			case CYTOSINE:
				this.color = Color.RED;

				Area cytosine = getShape(SQUARE);
				cytosine.subtract(getShape(TRIANGLE));
				cytosine.add(shift(getShape(TRAPEZOID), 0, LENGTH));
				this.bound = new Area(at.createTransformedShape(cytosine));
				break;

			case GUANINE:
				this.color = Color.GREEN;

				Area guanine = getShape(SQUARE);
				guanine.subtract(getShape(TRIANGLE));
				guanine.add(shift(getShape(SQUARE), 0, LENGTH));
				guanine.add(shift(getShape(TRAPEZOID), 0, LENGTH * 2));
				this.bound = new Area(at.createTransformedShape(guanine));
				break;
				
			case PHOSPHATE:
				this.color = Color.GRAY;
				
				Area phosphate = getShape(SQUARE);
				phosphate.subtract(getShape(SMALLSQUARE));

				at.translate(0, -LENGTH);
				this.bound = new Area(at.createTransformedShape(phosphate));
				break;
				
			case GLUCOSE:
				this.color = Color.WHITE;
				
				Area glucose = getShape(SQUARE);
				glucose.add(getShape(TRIANGLE));
				this.bound = shift(glucose, 0, -LENGTH);
				break;
			}
		}

	}

}
