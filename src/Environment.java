import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
public class Environment{
	private static double colorIncrements = 0.0005;
	private static double timeIncrements = 0.015;
	private static double G = 0.01;
	private static int borderWidth = 10;
	private static int maximumTails = 1;
	private static int nodeSize = 10;
	private static double deltaAngle = 0.004;
	public static double getDeltaAngle() {
		return deltaAngle;
	}
	public static void setDeltaAngle(double deltaAngle) {
		Environment.deltaAngle = deltaAngle;
	}
	public static int getNodeSize() {
		return nodeSize;
	}
	public static void setNodeSize(int nodeSize) {
		Environment.nodeSize = nodeSize;
	}
	public static int getMaximumTails() {
		return maximumTails;
	}
	public static void setMaximumTails(int maximumTails) {
		Environment.maximumTails = maximumTails;
	}
	public static int getBorderWidth() {
		return borderWidth;
	}
	public static void setBorderWidth(int borderWidth) {
		Environment.borderWidth = borderWidth;
	}
	public static double getColorIncrements() {
		return colorIncrements;
	}
	public static void setColorIncrements(double colorIncrements) {
		Environment.colorIncrements = colorIncrements;
	}
	public static double getTimeIncrements() {
		return timeIncrements;
	}
	public static void setTimeIncrements(double timeIncrements) {
		Environment.timeIncrements = timeIncrements;
	}
	public static double getG() {
		return G;
	}
	public static void setG(double g) {
		G = g;
	}
	//this static stuff is all constants that I'm not sure as to their exact value, and so I put them here.
	//Originally, they were in Toolkit, but I really think this works better. Now, on to the actual
	//environment stuff. Because it is a class.
	private JFrame frame;
	private JPanel outerPanel;
	private JPanel mainViewPanel;
	private JPanel instrumentPanel;
	private JPanel secondaryViewPanel;
	private LinkedList<View> angles;
	private LinkedList<Speck> particles;
	private LinkedList<ForceNode> pushers;
	private JSlider cIS;
	private JSlider tIS;
	private JSlider gS;
	private JSlider mTS;
	private JSlider sNF;
	private JSlider parameterOne;
	private JSlider parameterTwo;
	private JSlider parameterThr;
	private ForceNode selected;
	private boolean go;
	private int saveNumber;
	public Environment(){
		saveNumber = 0;
		go = true;
		angles = new LinkedList<View>();
		particles = new LinkedList<Speck>();
		pushers = new LinkedList<ForceNode>();
		frame = new JFrame();
		outerPanel = new JPanel();
		mainViewPanel = new JPanel();
		instrumentPanel = new JPanel();
		secondaryViewPanel = new JPanel();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLocation(0, 0);
		frame.setVisible(true);
		frame.add(outerPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit.sleep(100);
		pushers.add(new ForceNode(0, 0, 0, 2));
		outerPanel.add(secondaryViewPanel);
		outerPanel.add(mainViewPanel);
		outerPanel.add(instrumentPanel);
		outerPanel.setBackground(new Color(255, 255, 255));
		cIS = new JSlider();
		cIS.setMinimum(1);
		cIS.setMaximum(9);
		cIS.setValue(5);
		cIS.setMajorTickSpacing(1);
		cIS.setPaintLabels(true);
		cIS.setToolTipText("Color Increments");
		cIS.setVisible(true);
		instrumentPanel.add(cIS);
		tIS = new JSlider();
		tIS.setMinimum(1);
		tIS.setMaximum(99);
		tIS.setValue(15);
		tIS.setMajorTickSpacing(10);
		tIS.setPaintLabels(true);
		tIS.setToolTipText("Time Increments");
		tIS.setVisible(true);
		instrumentPanel.add(tIS);
		gS = new JSlider();
		gS.setMinimum(1);
		gS.setMaximum(99);
		gS.setValue(40);
		gS.setMajorTickSpacing(10);
		gS.setPaintLabels(true);		
		gS.setToolTipText("Gravity's Strength");
		gS.setVisible(true);
		instrumentPanel.add(gS);
		mTS = new JSlider();
		mTS.setMinimum(1);
		mTS.setMaximum(999);
		mTS.setValue(20);
		mTS.setMajorTickSpacing(200);
		mTS.setPaintLabels(true);		
		mTS.setToolTipText("Tail Length");
		mTS.setVisible(true);
		instrumentPanel.add(mTS);
		sNF = new JSlider();
		sNF.setMinimum(-50);
		sNF.setMaximum(50);
		sNF.setValue(10);
		sNF.setMajorTickSpacing(10);
		sNF.setPaintLabels(true);		
		sNF.setToolTipText("Selected Force Node's Power");
		sNF.setVisible(true);
		instrumentPanel.add(sNF);
		JButton pause = new JButton("Pause/Play");
		pause.addActionListener(new pause());
		instrumentPanel.add(pause);
		JButton swap = new JButton("Swap Main View");
		swap.addActionListener(new swapView());
		instrumentPanel.add(swap);
		JButton deleteParticles = new JButton("Delete All Particles");
		deleteParticles.addActionListener(new deleteParticles());
		instrumentPanel.add(deleteParticles);
		JButton reverseParticles = new JButton("Reverse All Velocities");
		reverseParticles.addActionListener(new reverseParticles());
		instrumentPanel.add(reverseParticles);
		JButton summonCircle = new JButton("Create Particle Cloud");
		summonCircle.addActionListener(new SummonCircularCloud());
		instrumentPanel.add(summonCircle);
		summonCircle.setToolTipText("Uses Parameter 1: #arms, Parameter 2: #particles in arms, Parameter 3: length of arms");
		parameterOne = new JSlider();
		parameterOne.setMinimum(0);
		parameterOne.setMaximum(100);
		parameterOne.setValue(0);
		parameterOne.setMajorTickSpacing(10);
		parameterOne.setPaintLabels(true);		
		parameterOne.setToolTipText("Number of arms in the cloud");
		parameterOne.setVisible(true);
		instrumentPanel.add(parameterOne);
		parameterTwo = new JSlider();
		parameterTwo.setMinimum(0);
		parameterTwo.setMaximum(100);
		parameterTwo.setValue(0);
		parameterTwo.setMajorTickSpacing(10);
		parameterTwo.setPaintLabels(true);		
		parameterTwo.setToolTipText("Number of particles in an arm");
		parameterTwo.setVisible(true);
		instrumentPanel.add(parameterTwo);
		parameterThr = new JSlider();
		parameterThr.setMinimum(0);
		parameterThr.setMaximum(100);
		parameterThr.setValue(0);
		parameterThr.setMajorTickSpacing(10);
		parameterThr.setPaintLabels(true);		
		parameterThr.setToolTipText("Length of an arm");
		parameterThr.setVisible(true);
		instrumentPanel.add(parameterThr);
		secondaryViewPanel.setBackground(new Color(255, 255, 255));
		mainViewPanel.setBackground(new Color(200, 255, 200));
		instrumentPanel.setBackground(new Color(255, 255, 255));
		frame.addComponentListener(new setSizeListener());
		rotateViewAndCreateForceNodes rv = new rotateViewAndCreateForceNodes();
		mainViewPanel.addMouseListener(rv);
		mainViewPanel.addMouseMotionListener(rv);
		outerPanel.updateUI();
	}
	public void run(){
		while(true){
			try{
				if(go){
					push();
					moveAllSpecks();
				}
				paintAll();
				updateConstants();
			}catch(Exception e){
				
			}
			
		}
	}
	public void updateConstants(){
		Environment.setColorIncrements(cIS.getValue()/10000.0);
		Environment.setTimeIncrements(tIS.getValue()/1000.0);
		Environment.setG(gS.getValue()/10000.0);
		Environment.setMaximumTails(mTS.getValue());
		if(selected != null){
			selected.setPower(sNF.getValue()/10.0);
		}
	}
	public void addView(double o, double i, String name, int width, int height){
		View nova = new View(width, height, o, i, name);
		for(Speck s: particles){
			s.addView(name, nova.getiOrientation(), nova.getjOrientation(), nova.getkOrientation());
		}
		for(ForceNode s: pushers){
			s.addView(name, nova.getiOrientation(), nova.getjOrientation(), nova.getkOrientation());
		}
		angles.add(nova);
		setSizes();
	}
	public void setSizes(){
		instrumentPanel.setPreferredSize(new Dimension(outerPanel.getWidth()/4 - 10, outerPanel.getHeight() - 10));
		instrumentPanel.setMaximumSize(new Dimension(outerPanel.getWidth()/4 - 10, outerPanel.getHeight() - 10));
		instrumentPanel.setMinimumSize(new Dimension(outerPanel.getWidth()/4 - 10, outerPanel.getHeight() - 10));
		instrumentPanel.setSize(new Dimension(outerPanel.getWidth()/4 - 10, outerPanel.getHeight() - 10));
		mainViewPanel.setPreferredSize(new Dimension(outerPanel.getWidth()/2 - 10, outerPanel.getHeight() - 10));
		mainViewPanel.setMaximumSize(new Dimension(outerPanel.getWidth()/2 - 10, outerPanel.getHeight() - 10));
		mainViewPanel.setMinimumSize(new Dimension(outerPanel.getWidth()/2 - 10, outerPanel.getHeight() - 10));
		mainViewPanel.setSize(new Dimension(outerPanel.getWidth()/2 - 10, outerPanel.getHeight() - 10));
		secondaryViewPanel.setPreferredSize(new Dimension(outerPanel.getWidth()/4 - 10, outerPanel.getHeight() - 10));
		secondaryViewPanel.setMaximumSize(new Dimension(outerPanel.getWidth()/4 - 10, outerPanel.getHeight() - 10));
		secondaryViewPanel.setMinimumSize(new Dimension(outerPanel.getWidth()/4 - 10, outerPanel.getHeight() - 10));
		secondaryViewPanel.setSize(new Dimension(outerPanel.getWidth()/4 - 10, outerPanel.getHeight() - 10));
		if(angles.size() != 0){
			View v = angles.get(0);
			angles.set(0, new View(mainViewPanel.getWidth(), mainViewPanel.getHeight(), v.getOrientation(), v.getInclination(), v.getName()));
		}
		if(angles.size() != 1){
			int panes = angles.size() - 1;
			double maximumWidth = secondaryViewPanel.getWidth();
			double maximumHeight = secondaryViewPanel.getHeight()/panes;
			int maximum = 0;
			if(maximumWidth < maximumHeight){
				maximum = (int) maximumWidth;
			}else{
				maximum = (int) maximumHeight;
			}
			for(int i = 1; i < angles.size(); i++){
				View v = angles.get(i);
				angles.set(i, new View(maximum, maximum, v.getOrientation(), v.getInclination(), v.getName()));
			}
		}
		outerPanel.updateUI();
	}
	public void changeViewAngle(View v, double deltaO, double deltaI){
		v.setOrientation(v.getOrientation() + deltaO);
		v.setInclination(v.getInclination() + deltaI);
		for(Object s: particles){
			s.editView(v.getName(), v.getiOrientation(), v.getjOrientation(), v.getkOrientation());
		}
		for(Object s: pushers){
			s.editView(v.getName(), v.getiOrientation(), v.getjOrientation(), v.getkOrientation());
		}
	}
	public void changeViewAngle(String name, double deltaO, double deltaI){
		for(View v: angles){
			if(v.getName().equals(name)){
				v.setOrientation(v.getOrientation() + deltaO);
				v.setInclination(v.getInclination() + deltaI);
				for(Object s: particles){
					s.editView(name, v.getiOrientation(), v.getjOrientation(), v.getkOrientation());
				}
				for(Object s: pushers){
					s.editView(name, v.getiOrientation(), v.getjOrientation(), v.getkOrientation());
				}
			}
		}
	}
	public void setViewAngle(View v, double newO, double newI){
		v.setOrientation(newO);
		v.setInclination(newI);
		for(Object s: particles){
			s.editView(v.getName(), v.getiOrientation(), v.getjOrientation(), v.getkOrientation());
		}
		for(Object s: pushers){
			s.editView(v.getName(), v.getiOrientation(), v.getjOrientation(), v.getkOrientation());
		}
	}
	public void setViewAngle(String name, double newO, double newI){
		for(View v: angles){
			if(v.getName().equals(name)){
				v.setOrientation(newO);
				v.setInclination(newI);
				for(Object s: particles){
					s.editView(name, v.getiOrientation(), v.getjOrientation(), v.getkOrientation());
				}
				for(Object s: pushers){
					s.editView(name, v.getiOrientation(), v.getjOrientation(), v.getkOrientation());
				}
			}
		}
	}
	public void push(){
		for(ForceNode fn: pushers){
			for(Speck s: particles){
				fn.force(s);
			}
		}
	}
	public void moveAllSpecks(){
		for(Speck s: particles){
			s.move();
		}
	}
	public void paintAll(){
		for(int i = 0; i < angles.size(); i++){
			View v = angles.get(i);
			v.paintAll(particles, pushers, selected);
			if(i == 0){
				mainViewPanel.getGraphics().drawImage(v, 0, 0, null);
				
			}else{
				secondaryViewPanel.getGraphics().drawImage(v, 0, (i - 1) * v.getHeight(), null);
			}
		}
	}
	public void circularCloud(int arms, int armAmount, int armLength){
		for(double theta = 0; theta < arms; theta++){
			double angle = 2 * Math.PI/arms * theta;
			for(double i = 1; i <= armAmount; i++){
				double x = i * Math.cos(angle) / armAmount * (armLength / 100.0) + 0.0001;
				double y = i * Math.sin(angle) / armAmount * (armLength / 100.0) + 0.0001;
				Speck chose = new Speck(0, y, x);
				particles.add(chose);
				chose.setVelocityI(-0.2);
				for(View v: angles){
					chose.addView(v.getName(), v.getiOrientation(), v.getjOrientation(), v.getkOrientation());
				}
			}
		}
	}
	public void createNewForceNode(double x, double y, double z, double power) {
		ForceNode nova = new ForceNode(x, y, z, power);
		pushers.add(nova);		
		for(View v: angles){
			nova.addView(v.getName(), v.getiOrientation(), v.getjOrientation(), v.getkOrientation());
		}
	}
	public class  SummonCircularCloud implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			circularCloud(parameterOne.getValue(), parameterTwo.getValue(), parameterThr.getValue());
		}
	}
	public class reverseParticles implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			boolean former = go;
			go = false;
			Toolkit.sleep(1000);
			for(Speck p: particles){
				p.setVelocityI(p.getVelocityI() * -1);
				p.setVelocityJ(p.getVelocityJ() * -1);
				p.setVelocityK(p.getVelocityK() * -1);
			}
			go = former;
		}
	}
	public class deleteParticles implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			particles = new LinkedList<Speck>();
		}
		
	}
	public class swapView implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(angles.size() != 0){
				angles.addLast(angles.removeFirst());
				setSizes();
			}
		}
		
	}
	public class pause implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			go = !go;
		}
		
	}
	public class rotateViewAndCreateForceNodes implements MouseListener, MouseMotionListener{
		int pressedX;
		int pressedY;
		double pressedO;
		double pressedI;
		double selectX;
		double selectY;
		double selectZ;
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(angles.size() == 0){
				return;
			}
			View main = angles.get(0);
			int x = arg0.getX();
			int y = arg0.getY();
			double[] xyz = main.reverseEngineer(x, y);
			if(arg0.getClickCount() == 1){
				selected = null;
				for(ForceNode fn: pushers){
					int[] coordinates = main.calculateScreenCoordinates(fn.getScreenX(main.getName()), fn.getScreenY(main.getName()));
					if(Math.abs(x - coordinates[0]) < nodeSize && Math.abs(y - coordinates[1]) < nodeSize){
						selected = fn;
						sNF.setValue((int) (selected.getPower() * 10));
					}
				}
			}else if(arg0.getClickCount() == 2 && arg0.getButton() == 3){
				ForceNode itemSelected = null;
				for(ForceNode fn: pushers){
					int[] coordinates = main.calculateScreenCoordinates(fn.getScreenX(main.getName()), fn.getScreenY(main.getName()));
					if(Math.abs(x - coordinates[0]) < nodeSize && Math.abs(y - coordinates[1]) < nodeSize){
						itemSelected = fn;
						sNF.setValue((int) (selected.getPower() * 10));
					}
				}
				if(itemSelected == null){
					ForceNode nova = new ForceNode(xyz[0], xyz[1], xyz[2], 0);
					pushers.add(nova);
					selected = nova;
					sNF.setValue((int) (selected.getPower() * 10));
					for(View v: angles){
						nova.addView(v.getName(), v.getiOrientation(), v.getjOrientation(), v.getkOrientation());
					}
				}else{
					if(selected == itemSelected){
						selected = null;
					}
					pushers.remove(itemSelected);
				}
			}
				
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}

		@Override
		public void mousePressed(MouseEvent arg0) {
			pressedX = arg0.getX();
			pressedY = arg0.getY();
			if(angles.size() == 0){
				return;
			}
			View rotate = angles.get(0);
			pressedO = rotate.getOrientation();
			pressedI = rotate.getInclination();
			ForceNode itemSelected = null;
			for(ForceNode fn: pushers){
				int[] coordinates = rotate.calculateScreenCoordinates(fn.getScreenX(rotate.getName()), fn.getScreenY(rotate.getName()));
				if(Math.abs(arg0.getX() - coordinates[0]) < nodeSize && Math.abs(arg0.getY() - coordinates[1]) < nodeSize){
					itemSelected = fn;
				}
			}
			if(itemSelected == null){
				selected = null;
			}else{
				selectX = itemSelected.getX();
				selectY = itemSelected.getY();
				selectZ = itemSelected.getZ();
			}
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {}
		@Override
		public void mouseDragged(MouseEvent arg0) {
			if(selected == null){
				if(angles.size() == 0){
					return;
				}
				View rotate = angles.get(0);
				int deltaX = arg0.getX() - pressedX;
				int deltaY = arg0.getY() - pressedY;
				setViewAngle(rotate, pressedO + deltaX * Environment.getDeltaAngle(), pressedI + deltaY * Environment.getDeltaAngle());

			}else{
				if(angles.size() == 0){
					return;
				}
				View rotate = angles.get(0);
				double i = rotate.getiOrientation();
				double j = rotate.getjOrientation();
				double k = rotate.getkOrientation();
				double[] shadow = Toolkit.findIntersect(selectX, selectY, selectZ, selectX + i, selectY + j, selectZ + k, i, j, k, 0);
				double offsetX = selectX - shadow[0];
				double offsetY = selectY - shadow[1];
				double offsetZ = selectZ - shadow[2];
				double[] coordinates = rotate.reverseEngineer(arg0.getX(), arg0.getY());
				selected.setX(coordinates[0] + offsetX);
				selected.setY(coordinates[1] + offsetY);
				selected.setZ(coordinates[2] + offsetZ);
				for(View v: angles){
					selected.calculateScreenCoordinates(v.getName());
				}
			}
		}
		@Override
		public void mouseMoved(MouseEvent arg0) {}
	}
	public class setSizeListener implements ComponentListener{

		@Override
		public void componentHidden(ComponentEvent arg0) {}
		@Override
		public void componentMoved(ComponentEvent arg0) {}
		@Override
		public void componentResized(ComponentEvent arg0) {
			setSizes();
		}
		@Override
		public void componentShown(ComponentEvent arg0) {}
	}
}