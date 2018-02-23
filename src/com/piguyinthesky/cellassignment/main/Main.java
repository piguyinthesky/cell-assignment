package com.piguyinthesky.cellassignment.main;

import javax.swing.JFrame;

public class Main {
	
	public static void main(String[] args) {
		JFrame window = new JFrame("The Cell");
		window.add(new CellGame());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
	
}
