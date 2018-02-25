package com.piguyinthesky.cellassignment.stages;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import com.piguyinthesky.cellassignment.handlers.InputHandler;
import com.piguyinthesky.cellassignment.main.CellGame;

public class Ribosome extends Stage {

	private final int KEY = 2;

	private final int ADENINE = 0;
	private final int CYTOSINE = 1;
	private final int GUANINE = 2;
	private final int THYMINE = 3;
	private final int PHOSPHATE = 4;
	private final int GLUCOSE = 5;
	private Area[] shapes = new Area[6];

	private final int SQUARE = 0;
	private final int TRIANGLE = 1;
	private final int CIRCLE = 2;
	private final int TRAPEZOID = 3;
	private final int SMALLSQUARE = 4;
	private Area[] basicShapes = new Area[5];

	private final int numPairs = 8;
	private final int BASEH = CellGame.HEIGHT / 4;
	private final int LENGTH = CellGame.WIDTH / (2 * numPairs + 1);
	private final int BOTTOMBASEH = BASEH + LENGTH * 4;

	private AminoAcid[] templates = new AminoAcid[6];
	private AminoAcid[] aacids = new AminoAcid[numPairs];
	private AminoAcid[] bases = new AminoAcid[numPairs * 2 + 1];
	private AminoAcid[] pieces = new AminoAcid[aacids.length + bases.length];

	private int pickedupind;
	private int xdiff;
	private int ydiff;

	private Random random;

	public Ribosome(StageManager sm) {
		super(sm, 1);

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

		for (int i = 0; i < templates.length; i++) {
			templates[i] = new AminoAcid(i);
		}
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
		shapes[THYMINE].subtract(shift(getShape(CIRCLE), 0, LENGTH * 3 / 2));

		shapes[CYTOSINE] = getShape(SQUARE);
		shapes[CYTOSINE].subtract(getShape(TRIANGLE));
		shapes[CYTOSINE].add(shift(getShape(TRAPEZOID), 0, LENGTH));

		shapes[GUANINE] = getShape(SQUARE);
		shapes[GUANINE].subtract(getShape(TRIANGLE));
		shapes[GUANINE].add(shift(getShape(SQUARE), 0, LENGTH));
		shapes[GUANINE].subtract(shift(flip(getShape(TRAPEZOID)), 0, LENGTH * 5 / 3));

		shapes[PHOSPHATE] = getShape(SQUARE);
		shapes[PHOSPHATE].subtract(shift(getShape(SMALLSQUARE), 0, LENGTH / 3));
		shapes[PHOSPHATE].subtract(shift(getShape(SMALLSQUARE), LENGTH * 2 / 3, LENGTH / 3));

		shapes[GLUCOSE] = getShape(SQUARE);
		shapes[GLUCOSE].add(shift(getShape(TRIANGLE), 0, LENGTH));
		shapes[GLUCOSE].add(shift(getShape(SMALLSQUARE), -LENGTH / 3, LENGTH / 3));
		shapes[GLUCOSE].add(shift(getShape(SMALLSQUARE), LENGTH, LENGTH / 3));
	}

	private void generatePieces() {
		for (int i = 0; i < bases.length; i++) {
			pieces[i] = new AminoAcid(bases[i].id);

			Area newBounds = flip(pieces[i].bounds);
			int x = random.nextInt(CellGame.WIDTH - newBounds.getBounds().width);
			int y = random.nextInt(CellGame.HEIGHT - BOTTOMBASEH - newBounds.getBounds().height) + BOTTOMBASEH;

			AffineTransform translate = AffineTransform.getTranslateInstance(x, y);
			pieces[i].setBounds(new Area(translate.createTransformedShape(newBounds)));
		}

		for (int i = 0; i < aacids.length; i++) {
			pieces[i + bases.length] = new AminoAcid(3 - aacids[i].id);

			Area newBounds = flip(pieces[i + bases.length].bounds);
			int width = newBounds.getBounds().width;
			int height = newBounds.getBounds().height;
			int x = random.nextInt(CellGame.WIDTH - width);
			int y = random.nextInt(CellGame.HEIGHT - BOTTOMBASEH - height) + BOTTOMBASEH;

			AffineTransform translate = AffineTransform.getTranslateInstance(x, y);
			pieces[i + bases.length].setBounds(new Area(translate.createTransformedShape(newBounds)));
		}
	}

	private Area shift(Area shape, int x, int y) {
		AffineTransform down = AffineTransform.getTranslateInstance(x, y);
		return new Area(down.createTransformedShape(shape));
	}

	private Area flip(Area shape) {
		int w = shape.getBounds().width;
		int h = shape.getBounds().height;

		AffineTransform at = new AffineTransform();

		at.setToRotation(Math.PI);
		Shape flipped = at.createTransformedShape(shape);
		at.setToRotation(0);

		at.setToTranslation(w, h);
		flipped = at.createTransformedShape(flipped);

		return new Area(flipped);
	}

	private Area getShape(int shape) {
		return (Area) basicShapes[shape].clone();
	}

	private void drawKeyText(Graphics2D g) {
		g.setFont(new Font("Impact", Font.BOLD, 24));
		FontMetrics fm = g.getFontMetrics();
		int x = CellGame.WIDTH - fm.stringWidth("Key") - CellGame.MARGIN;
		g.drawString("Key", x, CellGame.MARGIN + fm.getHeight());
		if (bounds[KEY] == null)
			bounds[KEY] = new Area(new Rectangle(x, CellGame.MARGIN, fm.stringWidth("Key"), fm.getHeight()));
	}

	private void drawKey(Graphics2D g) {
		Area[] shrunkBounds = new Area[shapes.length];

		int widest = 0;
		int tallest = 0;

		AffineTransform shrink = AffineTransform.getScaleInstance(3.0 / 4, 3.0 / 4);
		for (int shape = 0; shape < shapes.length; shape++) {
			shrunkBounds[shape] = new Area(shrink.createTransformedShape(shapes[shape]));
		}

		for (int i = 0; i < shrunkBounds.length; i++) {
			if (shrunkBounds[i].getBounds().width + CellGame.MARGIN > widest)
				widest = shrunkBounds[i].getBounds().width + CellGame.MARGIN;
			if (shapes[i].getBounds().height + CellGame.MARGIN > tallest)
				tallest = shrunkBounds[i].getBounds().height + CellGame.MARGIN;
		}

		for (int shape = 0; shape < shapes.length; shape++) {
			int x = CellGame.WIDTH - CellGame.MARGIN - widest;
			int y = CellGame.MARGIN + shape * tallest;
			Area rect = new Area(new Rectangle(x, y, widest, tallest));

			AffineTransform translate = AffineTransform.getTranslateInstance(x + CellGame.MARGIN / 2,
					y + CellGame.MARGIN / 2);

			g.setColor(Color.BLACK);
			g.fill(rect);
			g.setColor(Color.WHITE);
			g.draw(rect);

			g.setColor(templates[shape].color);
			g.fill(translate.createTransformedShape(shrunkBounds[shape]));
			g.setFont(new Font("Impact", Font.BOLD, 16));
			g.drawString(templates[shape].name, x + CellGame.MARGIN / 2, y);
		}
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

		drawKeyText(g);

		for (int i = 0; i < numPairs; i++) {
			g.setColor(aacids[i].color);
			g.fill(aacids[i].bounds);
		}

		for (int i = 0; i < bases.length; i++) {
			g.setColor(bases[i].color);
			g.fill(bases[i].bounds);
		}

		for (int i = 0; i < pieces.length; i++) {
			g.setColor(pieces[i].color);
			if (i == pickedupind) {
				AffineTransform inAir = AffineTransform.getTranslateInstance(
						InputHandler.currPos.x - InputHandler.startPos.x,
						InputHandler.currPos.y - InputHandler.startPos.y);
				g.fill(inAir.createTransformedShape(pieces[i].bounds));
			} else {
				g.fill(pieces[i].bounds);
			}
		}

		if (bounds[KEY].contains(InputHandler.currPos)) {
			drawKey(g);
		}
		
		if (showdesc) {
			showDescription(g);
		}
	}

	@Override
	public void handleInput() {
		super.handleInput();
		if (InputHandler.pressed) {
			for (int i = 0; i < pieces.length; i++) {
				if (pieces[i].bounds.contains(InputHandler.startPos)) {
					pickedupind = i;
					xdiff = InputHandler.startPos.x - pieces[i].bounds.getBounds().x;
					ydiff = InputHandler.startPos.y - pieces[i].bounds.getBounds().y;
				}
			}
		} else {
			for (int i = 0; i < pieces.length; i++) {
				if (pickedupind == i) {
					pieces[i].moveTo(InputHandler.currPos.x - xdiff, InputHandler.currPos.y - ydiff);
					pickedupind = -1;
				}
			}
		}
	}

	private class AminoAcid {

		private int id;
		private String name;
		private Color color;

		private Area bounds;

		public AminoAcid(int id) {
			this(id, 0, 0);
		}

		public AminoAcid(int id, int x, int y) {
			this.id = id;
			AffineTransform at = AffineTransform.getTranslateInstance(x, y);

			switch (id) {
			case ADENINE:
				this.color = Color.BLUE;
				this.name = "adenine";
				this.bounds = new Area(at.createTransformedShape(shapes[ADENINE]));
				break;
			case THYMINE:
				this.color = Color.YELLOW;
				this.name = "thymine";
				this.bounds = new Area(at.createTransformedShape(shapes[THYMINE]));
				break;
			case CYTOSINE:
				this.color = Color.RED;
				this.name = "cytosine";
				this.bounds = new Area(at.createTransformedShape(shapes[CYTOSINE]));
				break;
			case GUANINE:
				this.color = Color.GREEN;
				this.name = "guanine";
				this.bounds = new Area(at.createTransformedShape(shapes[GUANINE]));
				break;
			case PHOSPHATE:
				this.color = Color.GRAY;
				this.name = "phosphate";
				at.translate(0, -LENGTH);
				this.bounds = new Area(at.createTransformedShape(shapes[PHOSPHATE]));
				break;
			case GLUCOSE:
				this.color = Color.WHITE;
				this.name = "glucose";
				at.translate(0, -LENGTH);
				this.bounds = new Area(at.createTransformedShape(shapes[GLUCOSE]));
				break;
			}
		}

		private void setBounds(Shape shape) {
			bounds = new Area(shape);
		}

		public void moveTo(int x, int y) {
			AffineTransform translate = AffineTransform.getTranslateInstance(x - bounds.getBounds().x,
					y - bounds.getBounds().y);
			bounds = new Area(translate.createTransformedShape(bounds));
		}

	}

}
