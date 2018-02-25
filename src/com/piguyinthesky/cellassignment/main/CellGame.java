package com.piguyinthesky.cellassignment.main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.piguyinthesky.cellassignment.handlers.InputHandler;
import com.piguyinthesky.cellassignment.handlers.ResourceHandler;
import com.piguyinthesky.cellassignment.stages.StageManager;

@SuppressWarnings("serial")
public class CellGame extends JPanel implements Runnable {

	public static final int WIDTH = 720;
	public static final int HEIGHT = 480;
	public static final int SCALE = 1;
	public static final int MARGIN = HEIGHT / 48;

	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;

	private Graphics2D g;
	private BufferedImage image;

	private StageManager sm;
	private InputHandler ih;
	private ResourceHandler rh;

	private String[] imgPaths = { "Animal_Cell.jpg", "arrow.jpg" };
	public static Image[] images;
	public static final int ANIMALCELL = 0;
	public static final int ARROW = 1;

	public CellGame() {
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}

	@Override
	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			ih = new InputHandler();
			addMouseListener(ih);
			addMouseMotionListener(ih);
			thread.start();
		}
	}

	public void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();

		sm = new StageManager();
		rh = new ResourceHandler();

		images = rh.loadImages(imgPaths);

		running = true;
	}

	@Override
	public void run() {
		init();

		long start;
		long elapsed;
		long wait;

		while (running) {
			start = System.nanoTime();

			sm.update();
			sm.draw(g);

			Graphics g2 = getGraphics();
			g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
			g2.dispose();

			elapsed = System.nanoTime() - start;

			wait = targetTime - elapsed / 1000000;
			if (wait < 0)
				wait = 5;

			try {
				Thread.sleep(wait);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void createAndShowGUI() {
		JFrame window = new JFrame("The Cell");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		window.setResizable(false);

		window.add(new CellGame());
		
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

}
