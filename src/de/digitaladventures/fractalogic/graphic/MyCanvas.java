package de.digitaladventures.fractalogic.graphic;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JComponent;

import de.digitaladventures.fractalogic.logic.Point;
import de.digitaladventures.fractalogic.logic.Shape;

public class MyCanvas extends JComponent implements MouseListener, MouseMotionListener {

	private final Vector<Shape> shapes;
	private boolean firstTime;
	private final Vector<Point> polyline;
	private boolean polylining;
	private Point pointer;
	private BufferedImage fractals;
	public MyCanvas() {
		shapes = new Vector<Shape>();
		firstTime = true;
		polyline = new Vector<Point>();
		addMouseMotionListener(this);
		addMouseListener(this);
	}
	public void addShape(Shape shape) {
		if(fractals == null)
			fractals = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		shapes.add(shape);
		Graphics g = fractals.createGraphics();
		shape.paint(g);
	}
	@Override
	public void paint(Graphics g) {
		update(g);
	}
	public void clear() {
		fractals = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		shapes.clear();
		update(getGraphics());
	}
	public void updateFractals() {
		if(fractals == null)
			fractals = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g2 = fractals.createGraphics();
		for(Shape shape : shapes) {
			shape.doFractal();
			shape.paint(g2);
		}
		update(getGraphics());
	}
	@Override
	public void update(Graphics g) {
		Color bakup = g.getColor();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(fractals, 0, 0, this);
		if(polylining) {
			g.setColor(Color.WHITE);
			int[] xPoints = new int[polyline.size() + 1];
			int[] yPoints = new int[polyline.size() + 1];
			for(int i = 0; i < polyline.size(); i++) {
				xPoints[i] = (int)polyline.get(i).X;
				yPoints[i] = (int)polyline.get(i).Y;
			}
			xPoints[xPoints.length - 1] = (int)pointer.X;
			yPoints[xPoints.length - 1] = (int)pointer.Y;
			g.drawPolyline(xPoints, yPoints, xPoints.length);
		}
		g.setColor(bakup);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			polylining = true;
			polyline.add(new Point(e.getX(), e.getY()));
		}
		else {
			polylining = false;
			Shape shape = new Shape(Color.BLUE, new Vector<Point>(polyline));
			addShape(shape);
			polyline.clear();
			shape.paint(getGraphics());
			
		}
		update(getGraphics());
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		pointer = new Point(e.getX(), e.getY());
		if(polylining) {
			update(getGraphics());
		}
	}
}
