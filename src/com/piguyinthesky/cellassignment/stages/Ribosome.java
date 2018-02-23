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
	private Area[] shapes = new Area[6];

	private final int BASEH = CellGame.HEIGHT / 4;

	private final int numPairs = 8;
	private AminoAcid[] aacids = new AminoAcid[numPairs];
	private final int aacidw = CellGame.WIDTH / (2 * numPairs + 1);
	private AminoAcid[] bases = new AminoAcid[numPairs * 2 + 1];

	private Random random;

	public Ribosome(StageManager sm) {
		super(sm);
		
		createShapes();
		random = new Random();

		for (int i = 0; i < numPairs; i++) {
			aacids[i] = new AminoAcid(random.nextInt(4), aacidw * (i * 2 + 1));
		}
		
		for (int i = 0; i < bases.length; i++) {
			if (i % 2 == 0) {
				bases[i] = new AminoAcid(PHOSPHATE, aacidw * i);
			} else {
				bases[i] = new AminoAcid(GLUCOSE, aacidw * i);
			}
		}
		
		for (int i = 0; i < bases.length; i++) {
			System.out.println(bases[i].id);
			System.out.println(bases[i].bound);
		}
	}

	private void createShapes() {
		int[] trixpoints = { 0, aacidw, aacidw / 2 };
		int[] triypoints = { 0, 0, aacidw / 2 };
		shapes[TRIANGLE] = new Area(new Polygon(trixpoints, triypoints, 3));
	}
	
	private Area shiftDown(Area shape) {
		AffineTransform down = AffineTransform.getTranslateInstance(0, aacidw);
		return new Area(down.createTransformedShape(shape));
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

		private Area square = new Area(new Rectangle(0, 0, aacidw, aacidw));

		private Area circle = new Area(new Ellipse2D.Double(0, aacidw / 2, aacidw, aacidw));

		private int[] trapxpoints1 = { 0, aacidw / 3, aacidw / 3 };
		private int[] trapxpoints2 = { aacidw, aacidw * 2 / 3, aacidw * 2 / 3 };
		private int[] trapypoints = { aacidw, aacidw, aacidw * 4 / 3 };
		private Area tri1 = new Area(new Polygon(trapxpoints1, trapypoints, 3));
		private Area tri2 = new Area(new Polygon(trapxpoints2, trapypoints, 3));
		private Area ssquare = new Area(new Rectangle(aacidw / 3, aacidw, aacidw / 3, aacidw / 3));

		AffineTransform h2 = AffineTransform.getTranslateInstance(0, aacidw);
		private Area square2 = new Area(h2.createTransformedShape(square));
		private Area circle2 = new Area(h2.createTransformedShape(circle));
		private Area tri12 = new Area(h2.createTransformedShape(tri1));
		private Area tri22 = new Area(h2.createTransformedShape(tri2));
		private Area ssquare2 = new Area(h2.createTransformedShape(ssquare));

		public AminoAcid(int id, int x) {
			this.id = id;
			AffineTransform at = AffineTransform.getTranslateInstance(x, BASEH);

			switch (id) {
			case ADENINE:
				this.color = Color.BLUE;

				Area adenine = square;
				adenine.subtract(shapes[TRIANGLE]);
				adenine.add(circle);
				this.bound = new Area(at.createTransformedShape(adenine));
				break;

			case THYMINE:
				this.color = Color.YELLOW;

				Area thymine = square;
				thymine.subtract(shapes[TRIANGLE]);
				thymine.add(square2);
				thymine.add(circle2);
				this.bound = new Area(at.createTransformedShape(thymine));
				break;

			case CYTOSINE:
				this.color = Color.RED;

				Area cytosine = square;
				cytosine.subtract(shapes[TRIANGLE]);
				cytosine.add(tri1);
				cytosine.add(tri2);
				cytosine.add(ssquare);
				this.bound = new Area(at.createTransformedShape(cytosine));
				break;

			case GUANINE:
				this.color = Color.GREEN;

				Area guanine = square;
				guanine.subtract(shapes[TRIANGLE]);
				guanine.add(square2);
				guanine.add(tri12);
				guanine.add(tri22);
				guanine.add(ssquare2);
				this.bound = new Area(at.createTransformedShape(guanine));
				break;
				
			case PHOSPHATE:
				this.color = Color.GRAY;
				
				Area phosphate = square;
				phosphate.subtract(ssquare);

				at.translate(0, -aacidw);
				this.bound = new Area(at.createTransformedShape(phosphate));
				break;
				
			case GLUCOSE:
				this.color = Color.WHITE;
				
				Area sugar = square;
				sugar.add(shapes[TRIANGLE]);
				
				at.translate(0, -aacidw);
				this.bound = new Area(at.createTransformedShape(sugar));
			}
		}

	}

}
