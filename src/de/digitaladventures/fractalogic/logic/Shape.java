package de.digitaladventures.fractalogic.logic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Vector;

import javax.swing.JComponent;

public class Shape {
	private final Color color;
	private final Vector<Point> points;
	private final Vector<Shape> subShapes;
	public Shape(Color color, Vector<Point> points) {
		this.color = color;
		this.points = points;
		subShapes = new Vector<Shape>();
	}
	public Shape(Color color) {
		this(color, new Vector<Point>());
	}
	private double getAngle(Point first, Point second) {
		return Math.atan(getDeltaY(first, second) / getDeltaX(first, second));
	}
	private double getAngle() {
		return getAngle(points.firstElement(), points.lastElement());
	}
	private double getDeltaX(Point first, Point second) {
		return second.X - first.X;
	}
	private double getDeltaY(Point first, Point second) {
		return second.Y - first.Y;
	}
	public Shape getShape(Point start, Point end) {
		double scale = start.getDistance(end) / points.firstElement().getDistance(points.lastElement());
		double lineAngle = getAngle(start, end);
		double shapeAngle = getAngle();
		double rotAngle = lineAngle - shapeAngle;
		Vector<Point> newPoints = new Vector<Point>();
		newPoints.add(start);
		for(int i = 1; i < points.size(); i++) {
			double deltaX = getDeltaX(points.get(i - 1), points.get(i)) * scale;
			double deltaY = getDeltaY(points.get(i - 1), points.get(i)) * scale;
			Point p = new Point(newPoints.get(i - 1).X + deltaX, newPoints.get(i - 1).Y + deltaY);
			p.rotate(newPoints.get(i - 1), rotAngle);
			newPoints.add(p);
		}
		return new Shape(getNextColor(), newPoints);
	}
	public Color getNextColor() {
		return color;
//		float[] rgbVals = color.getRGBComponents(null);
//		float[] hsbVals = Color.RGBtoHSB((int)rgbVals[0], (int)rgbVals[1], (int)rgbVals[2], null);
//		return Color.getHSBColor(hsbVals[0] + 1, hsbVals[1], hsbVals[2]);
	}
	public void paint(Graphics g) {
		Color bak = g.getColor();
		g.setColor(color);
		for(int i = 1; i < points.size(); i++) {
			Line2D.Double tempLine = new Line2D.Double(points.get(i - 1).X, points.get(i - 1).Y, points.get(i).X, points.get(i).Y);
			((Graphics2D) g).draw(tempLine);
		}
		for(Shape shape : subShapes)
			shape.paint(g);
		g.setColor(bak);
	}
	public void doFractal() {
		if(subShapes.isEmpty())
			for(int i = 1; i < points.size(); i++)
				subShapes.add(getShape(points.get(i - 1), points.get(i)));
		else
			for(Shape shape : subShapes)
				shape.doFractal();
	}
	public void addPoint(Point point) {
		points.add(point);
	}
}
