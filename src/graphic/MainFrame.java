package graphic;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.digitaladventures.fractalogic.logic.Point;
import de.digitaladventures.fractalogic.logic.Shape;

public class MainFrame extends JFrame {
	private final MyCanvas canvas;
	public MainFrame() {
		JPanel panel = new JPanel();
		canvas = new MyCanvas();
		canvas.setPreferredSize(new Dimension(600, 600));
		panel.add(canvas);
		setContentPane(panel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame();
		mainFrame.pack();
		mainFrame.setVisible(true);
		Shape shape = new Shape(Color.WHITE);
		shape.addPoint(new Point(100, 100));
		shape.addPoint(new Point(150, 150));
		shape.addPoint(new Point(200, 100));
		mainFrame.canvas.addShape(shape);
		for(int i = 0; i < 10; i++) {
			shape.doFractal();
			mainFrame.canvas.paintComponent(mainFrame.getGraphics());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
