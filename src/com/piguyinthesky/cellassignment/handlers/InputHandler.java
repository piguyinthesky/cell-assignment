package com.piguyinthesky.cellassignment.handlers;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputHandler implements MouseListener, MouseMotionListener {
	
	public static Point currPos;
	public static Point prevPos;
	public static Point startPos;
	public static Point releasePos;
	
	public static boolean pressed = false;
	
	public InputHandler() {
		currPos = prevPos = startPos = releasePos = new Point(-1, -1);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		startPos = e.getPoint();
		pressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		releasePos = e.getPoint();
		pressed = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		prevPos = currPos;
		currPos = e.getPoint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		prevPos = currPos;
		currPos = e.getPoint();
	}

}
