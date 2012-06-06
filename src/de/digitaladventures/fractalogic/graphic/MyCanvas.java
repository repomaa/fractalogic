package de.digitaladventures.fractalogic.graphic;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JComponent;

import de.digitaladventures.fractalogic.logic.Shape;

public class MyCanvas extends JComponent {

	private final Vector<Shape> shapes;
	public MyCanvas() {
		shapes = new Vector<Shape>();
	}
	public void addShape(Shape shape) {
		shapes.add(shape);
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		for(Shape shape : shapes)
			shape.paint(g);
	}
}
