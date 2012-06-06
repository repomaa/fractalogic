package de.digitaladventures.fractalogic.graphic;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

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
		setVisible(true);
		createBufferStrategy(3);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setJMenuBar(createMenuBar());
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenuItem doFractal = new JMenuItem("Do Fractal");
		doFractal.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.updateFractals();
			}
		});
		JMenuItem clear = new JMenuItem("Clear");
		clear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.clear();
			}
		});
		JMenuItem color = new JMenuItem("Choose Color");
		color.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JColorChooser cc = new JColorChooser(Color.BLUE);
				int i = JOptionPane.showConfirmDialog(MainFrame.this, cc, "Choose Color", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (i == JOptionPane.OK_OPTION)
					canvas.color = cc.getColor();
			}
		});
		menuBar.add(doFractal);
		menuBar.add(clear);
		menuBar.add(color);
		return menuBar;
	}

	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame();
		mainFrame.pack();
//		Shape shape = new Shape(Color.BLUE);
//		shape.addPoint(new Point(100, 200));
//		shape.addPoint(new Point(130, 260));
//		shape.addPoint(new Point(200, 200));
//		mainFrame.canvas.addShape(shape);
//		for(int i = 0; i < 14; i++) {
//			mainFrame.canvas.paint(mainFrame.getGraphics());
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			shape.doFractal();
//		}
	}
}
