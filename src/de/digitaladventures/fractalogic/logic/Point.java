package de.digitaladventures.fractalogic.logic;

import java.math.BigDecimal;

public class Point {
	public double X;
	public double Y;
	public Point(double x, double y) {
		X = x;
		Y = y;
	}
	public double getDistance(Point otherPoint) {
		return Math.sqrt(Math.pow(X - otherPoint.X, 2) + Math.pow(Y - otherPoint.Y, 2));
	}
	public void rotate(Point point, double rotAngle) {
		double newX = point.X + Math.cos(rotAngle) * (X - point.X) - Math.sin(rotAngle) * (Y - point.Y);
		double newY = point.Y + Math.sin(rotAngle) * (X - point.X) + Math.cos(rotAngle) * (Y - point.Y);
		X = newX;
		Y = newY;
	}
}
