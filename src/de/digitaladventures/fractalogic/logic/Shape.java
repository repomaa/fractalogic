package de.digitaladventures.fractalogic.logic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class Shape implements Runnable {
	private final Color color;
	private final Color nextColor;
	private final Vector<Point> points;
	private final Vector<Shape> subShapes;
	private ExecutorService threadPool;
	private boolean ready;
	private BufferedImage image;
	private Rectangle boundingBox;
	private final ImageObserver observer;
	public Shape(Color color, Vector<Point> points, ImageObserver observer) {
		this.color = color;
		this.points = points;
		this.observer = observer;
		subShapes = new Vector<Shape>();
		float[] rgbVals = color.getRGBComponents(null);
		float[] hsbVals = Color.RGBtoHSB((int)(rgbVals[0]*255), (int)(rgbVals[1]*255), (int)(rgbVals[2]*255), null);
		hsbVals[0] = (hsbVals[0] * 360F + 10F) / 360F;
		Color nextColor = Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]);
		ColorSpace sRGB = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		float[] newVals = nextColor.getColorComponents(sRGB, null);
		this.nextColor = new Color(sRGB, newVals, 0.5F);
		boundingBox = getBounds();
		image = new BufferedImage(boundingBox.width, boundingBox.height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = image.createGraphics();
		g.setColor(color);
		for(int i = 1; i < points.size(); i++)
			g.drawLine((int)points.get(i-1).X, (int)points.get(i-1).Y, (int)points.get(i).X, (int)points.get(i).Y);
	}
	public Shape(Color color, ImageObserver observer) {
		this(color, new Vector<Point>(), observer);
	}
	private double getAngle(Point first, Point second) {
		return Math.atan2(getDeltaY(second, first), getDeltaX(second, first));
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
		Shape shape = new Shape(getNextColor(), newPoints, observer);
		shape.threadPool = threadPool;
		return shape;
	}
	public Color getNextColor() {
		return nextColor;		
	}
	public void paint(Graphics g) {
		if(subShapes.isEmpty()) {
			((Graphics2D) g).drawImage(image, boundingBox.x, boundingBox.y, observer);
		}
		for(Shape shape : subShapes)
			shape.paint(g);
	}
	public void doFractal(Graphics g) {
		ready = false;
		threadPool = Executors.newFixedThreadPool(2);
		threadPool.execute(this);			
	}
	public void addPoint(Point point) {
		points.add(point);
		boundingBox = getBounds();
		image = new BufferedImage(boundingBox.width, boundingBox.height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = image.createGraphics();
		g.setColor(color);
		for(int i = 1; i < points.size(); i++)
			g.drawLine((int)points.get(i-1).X, (int)points.get(i-1).Y, (int)points.get(i).X, (int)points.get(i).Y);
	}
	private Rectangle getBounds() {
		double maxX = Integer.MIN_VALUE;
		double minX = Integer.MAX_VALUE;
		double maxY = Integer.MIN_VALUE;
		double minY = Integer.MAX_VALUE;
		for(Point p : points) {
			maxX = Math.max(p.X, maxX);
			minX = Math.min(p.X, minX);
			maxY = Math.max(p.Y, maxY);
			minY = Math.min(p.Y, minY);
		}
		return new Rectangle((int)minX, (int)minY, (int)(maxX - minX), (int)(maxY - minY));
	}
	@Override
	public void run() {
		Graphics g = image.createGraphics();
		g.setColor(color);
		if(subShapes.isEmpty()) {
			for(int i = 1; i < points.size(); i++) {
				Line2D.Double line = new Line2D.Double(points.get(i - 1).X, points.get(i - 1).Y, points.get(i).X, points.get(i).Y);
				((Graphics2D) g).draw(line);
				subShapes.add(getShape(points.get(i - 1), points.get(i)));
			}
			ready = true;
		}
		else
			for(Shape shape : subShapes) {
				threadPool.execute(shape);
			}
	}
	private boolean isReady() {
		if(ready) {
			for(Shape shape : subShapes)
				if(!shape.isReady())
					return false;
			return true;
		}
		return false;
	}
}
