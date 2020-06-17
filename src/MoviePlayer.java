import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class MoviePlayer{
	public static void main(String[] args){
		JFrame frame = new JFrame();
		frame.setSize(1920, 1080);
		frame.setLocation(0, 0);
		frame.setVisible(true);
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		frame.add(panel);
		JPanel screen = new JPanel();
		screen.setBackground(new Color(255, 255, 255));
		panel.add(screen);
		screen.setSize(new Dimension(1080, 1080));
		screen.setPreferredSize(new Dimension(1080, 1080));
		screen.setMaximumSize(new Dimension(1080, 1080));
		screen.setMinimumSize(new Dimension(1080, 1080));
		JSlider readSpeed = new JSlider();
		readSpeed.setMaximum(10);
		readSpeed.setMinimum(-10);
		readSpeed.setMajorTickSpacing(2);
		readSpeed.setMinorTickSpacing(1);
		readSpeed.setPaintLabels(true);
		readSpeed.setPaintTicks(true);
		readSpeed.setValue(0);
		panel.add(readSpeed);
		panel.updateUI();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		double number = 0;
		while(true){
			try{
				int num = (int) number;
				BufferedImage paint = null;
				paint = ImageIO.read(new File("movie/gravityArt" + num + ".png"));
				screen.getGraphics().drawImage(paint, 0, 0, null);
				number += readSpeed.getValue()/1.0;
			}catch(Exception e){
				if(number > 0){
					System.out.println("We are currently at " + ((int) (number + 1)) + " frames.");
					break;
				}else{
					number = 0;
				}
				
			}
		}
		frame.setVisible(false);
		frame.setEnabled(false);
		frame = null;
	}
}