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
	private Area[] shapes = new Area[6];

	private final int SQUARE = 0;
	private final int TRIANGLE = 1;
	private final int CIRCLE = 2;
	private final int TRAPEZOID = 3;
	private final int SMALLSQUARE = 4;
	private Area[] basicShapes = new Area[6];

	private final int numPairs = 8;
	private final int BASEH = CellGame.HEIGHT / 4;
	private final int LENGTH = CellGame.WIDTH / (2 * numPairs + 1);
	private final int BOTTOMBASEH = BASEH + LENGTH * 5;

	private AminoAcid[] aacids = new AminoAcid[numPairs];
	private AminoAcid[] bases = new AminoAcid[numPairs * 2 + 1];
	private AminoAcid[] pieces = new AminoAcid[aacids.length + bases.length];

	private Random random;

	public Ribosome(StageManager sm) {
		super(sm);

		generateBasicShapes();
		generateShapes();
		random = new Random();

		for (int i = 0; i < numPairs; i++) {
			aacids[i] = new AminoAcid(random.nextInt(4), LENGTH * (i * 2 + 1), BASEH);
		}

		for (int i = 0; i < bases.length; i++) {
			if (i % 2 == 0) {
				bases[i] = new AminoAcid(PHOSPHATE, LENGTH * i, BASEH);
			} else {
				bases[i] = new AminoAcid(GLUCOSE, LENGTH * i, BASEH);
			}
		}

		generatePieces();
	}

	private void generateBasicShapes() {
		basicShapes[SQUARE] = new Area(new Rectangle(0, 0, LENGTH, LENGTH));
		int[] trixpoints = { 0, LENGTH, LENGTH / 2 };
		int[] triypoints = { 0, 0, LENGTH / 2 };
		basicShapes[TRIANGLE] = new Area(new Polygon(trixpoints, triypoints, 3));
		basicShapes[CIRCLE] = new Area(new Ellipse2D.Double(0, 0, LENGTH, LENGTH));

		basicShapes[SMALLSQUARE] = new Area(new Rectangle(0, 0, LENGTH / 3, LENGTH / 3));

		int[] trapxpoints1 = { 0, LENGTH / 3, LENGTH / 3 };
		int[] trapxpoints2 = { LENGTH, LENGTH * 2 / 3, LENGTH * 2 / 3 };
		int[] trapypoints = { 0, 0, LENGTH / 3 };

		basicShapes[TRAPEZOID] = new Area(new Polygon(trapxpoints1, trapypoints, 3));
		basicShapes[TRAPEZOID].add(new Area(new Polygon(trapxpoints2, trapypoints, 3)));
		basicShapes[TRAPEZOID].add(shift(basicShapes[SMALLSQUARE], LENGTH / 3, 0));
	}

	private void generateShapes() {
		shapes[ADENINE] = getShape(SQUARE);
		shapes[ADENINE].subtract(getShape(TRIANGLE));
		shapes[ADENINE].add(shift(getShape(CIRCLE), 0, LENGTH / 2));

		shapes[THYMINE] = getShape(SQUARE);
		shapes[THYMINE].subtract(getShape(TRIANGLE));
		shapes[THYMINE].add(shift(getShape(SQUARE), 0, LENGTH));
		shapes[THYMINE].add(shift(getShape(CIRCLE), 0, LENGTH * 3 / 2));

		shapes[CYTOSINE] = getShape(SQUARE);
		shapes[CYTOSINE].subtract(getShape(TRIANGLE));
		shapes[CYTOSINE].add(shift(getShape(TRAPEZOID), 0, LENGTH));

		shapes[GUANINE] = getShape(SQUARE);
		shapes[GUANINE].subtract(getShape(TRIANGLE));
		shapes[GUANINE].add(shift(getShape(SQUARE), 0, LENGTH));
		shapes[GUANINE].add(shift(getShape(TRAPEZOID), 0, LENGTH * 2));

		shapes[PHOSPHATE] = getShape(SQUARE);
		shapes[PHOSPHATE].subtract(shift(getShape(SMALLSQUARE), 0, LENGTH / 3));
		shapes[PHOSPHATE].subtract(shift(getShape(SMALLSQUARE), LENGTH * 2 / 3, LENGTH / 3));

		shapes[GLUCOSE] = getShape(SQUARE);
		shapes[GLUCOSE].add(shift(getShape(TRIANGLE), 0, LENGTH));
		shapes[GLUCOSE].add(shift(getShape(SMALLSQUARE), -LENGTH / 3, LENGTH / 3));
		shapes[GLUCOSE].add(shift(getShape(SMALLSQUARE), LENGTH, LENGTH / 3));
	}

	private void generatePieces() {
		AffineTransform upsidedown = AffineTransform.getRotateInstance(Math.PI);
		for (int i = 0; i < bases.length; i++) {
			pieces[i] = new AminoAcid(bases[i].id, i * LENGTH, BOTTOMBASEH - LENGTH);
		}
		
		for (int i = 0; i < aacids.length; i++) {
			int y = random.nextInt(CellGame.HEIGHT - BOTTOMBASEH) + BOTTOMBASEH;
			int x = random.nextInt(CellGame.WIDTH);
			pieces[i + bases.length] = new AminoAcid(aacids[i].id, x, y);
//			pieces[i + bases.length].bound = new Area(upsidedown.createTransformedShape(pieces[i + bases.length].bound));
		}
	}

	private Area shift(Area shape, int x, int y) {
		AffineTransform down = AffineTransform.getTranslateInstance(x, y);
		return new Area(down.createTransformedShape(shape));
	}

	private Area getShape(int shape) {
		return (Area) basicShapes[shape].clone();
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);

		g.setColor(Color.WHITE);
		g.drawLine(0, BOTTOMBASEH, CellGame.WIDTH, BOTTOMBASEH);
		
		for (int i = 0; i < numPairs; i++) {
			g.setColor(aacids[i].color);
			g.fill(aacids[i].bound);
		}

		for (int i = 0; i < bases.length; i++) {
			g.setColor(bases[i].color);
			g.fill(bases[i].bound);
		}

		for (int i = 0; i < pieces.length; i++) {
			g.setColor(pieces[i].color);
			g.fill(pieces[i].bound);
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

		public AminoAcid(int id, int x, int y) {
			this.id = id;
			AffineTransform at = AffineTransform.getTranslateInstance(x, y);

			switch (id) {
			case ADENINE:
				this.color = Color.BLUE;
				this.bound = new Area(at.createTransformedShape(shapes[ADENINE]));
				break;
			case THYMINE:
				this.color = Color.YELLOW;
				this.bound = new Area(at.createTransformedShape(shapes[THYMINE]));
				break;
			case CYTOSINE:
				this.color = Color.RED;
				this.bound = new Area(at.createTransformedShape(shapes[CYTOSINE]));
				break;
			case GUANINE:
				this.color = Color.GREEN;
				this.bound = new Area(at.createTransformedShape(shapes[GUANINE]));
				break;
			case PHOSPHATE:
				this.color = Color.GRAY;
				at.translate(0, -LENGTH);
				this.bound = new Area(at.createTransformedShape(shapes[PHOSPHATE]));
				break;
			case GLUCOSE:
				this.color = Color.WHITE;
				at.translate(0, -LENGTH);
				this.bound = new Area(at.createTransformedShape(shapes[GLUCOSE]));
				break;
			}
		}

	}

}
