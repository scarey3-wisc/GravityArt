import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
public class View extends BufferedImage{
	private String name;
	private double zoom;
	private double orientation, inclination;
	public View(int width, int height, double o, double i, String name) {
		super(width, height, BufferedImage.TYPE_INT_RGB);
		orientation = o;
		inclination = i;
		this.name = name;
		zoom = 2;
	}
	public double[] reverseEngineer(int screenX, int screenY){
		double originalY = 1.0 * getHeight() - screenY;
		double xRatio = getWidth()/2140.0 * zoom;
		double yRatio = getHeight()/2140.0 * zoom;
		double originalX = screenX - getWidth()/2;
		originalY = originalY - getHeight()/2;
		originalX /= xRatio;
		originalY /= yRatio;
		double windowX = originalX/1000;
		double windowY = originalY/1000;
		//well, for the equivelant of this method (the caluclateScreenCoordinates in the .Perspective class,
		//I commented extensively. I think I should do the same here: it is a complicated method. Well.
		//First of fall, we are going to generate the equation for the plane, same as before. That is simply
		//Ax + By + Cz = D. We are going to use this perspective's i, j, and k orientations as the a, b, and c.
		double a = getiOrientation();
		double b = getjOrientation();
		double c = getkOrientation();
		double d = 0;
		double i = a;
		double j = b;
		double k = c;
		//Second of all, we are going to use the same block of text in the .Perspective class to generate
		//a u and v coordinate system. As it will be the exact same text, it will be the exact same u-v system.
		double xOrigin = 0;
		double yOrigin = 0;
		double zOrigin = 0;
		double[] topPoints = Toolkit.findIntersect(0, 0, 1, i, j, k + 1, a, b, c, d);
		if(k != 0){
			topPoints = Toolkit.findIntersect(0, 1, 0, i, j + 1, k, a, b, c, d);
		}
		double topPointX = topPoints[0];
		double topPointY = topPoints[1];
		double topPointZ = topPoints[2];
		double nuUpI = topPointX - xOrigin;
		double nuUpJ = topPointY - yOrigin;
		double nuUpK = topPointZ - zOrigin;
		double[] upVector = Toolkit.findUnitVector(nuUpI, nuUpJ, nuUpK);
		double vI = upVector[0];
		double vJ = upVector[1];
		double vK = upVector[2];
		double uI = j * vK - k * vJ;
		double uJ = k * vI - i * vK;
		double uK = i * vJ - j * vI;
		//Now, this next bit is delightfully easy. You see, we already have v and u expressed in terms of 
		//the ijk system. So, we just multiply the window coordinates by the i, j, and k componenets.
		double I = windowY * vI + windowX * uI;
		double J = windowY * vJ + windowX * uJ;
		double K = windowY * vK + windowX * uK;
		return new double[]{I, J, K};
	}
	public int[] calculateScreenCoordinates(double originalX, double originalY){
		double xRatio = getWidth()/2140.0 * zoom;
		double yRatio = getHeight()/2140.0 * zoom;
		originalX *= xRatio;
		originalY *= yRatio;
		originalX += getWidth()/2;
		originalY += getHeight()/2;
		originalY = getHeight() - originalY;
		return new int[]{(int) originalX, (int) originalY};
	}
	public void paintNodes(LinkedList<ForceNode> pushers, ForceNode select){
		Graphics g = getGraphics();
		g.setColor(Color.white);
		for(ForceNode fn: pushers){
			if(fn == select){
				g.setColor(Color.red);
				int[] coordinates = calculateScreenCoordinates(fn.getScreenX(name), fn.getScreenY(name));
				g.fillRect((int) (coordinates[0] - Environment.getNodeSize()/2), (int) (coordinates[1] - Environment.getNodeSize()/2), Environment.getNodeSize(), Environment.getNodeSize());
				g.setColor(Color.white);
			}else{
				int[] coordinates = calculateScreenCoordinates(fn.getScreenX(name), fn.getScreenY(name));
				g.fillRect((int) (coordinates[0] - Environment.getNodeSize()/2), (int) (coordinates[1] - Environment.getNodeSize()/2), Environment.getNodeSize(), Environment.getNodeSize());
			}
		}
	}
	public void paintAll(LinkedList<Speck> heads, LinkedList<ForceNode> pushers, ForceNode selected){
		Graphics g = getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, getWidth(), getHeight());
		paintParticles(heads);
		paintNodes(pushers, selected);
	}
	public void paintParticles(LinkedList<Speck> heads){
		for(Speck head: heads){
			paintParticle(head, head, 0);
		}
	}
	public void paintParticle(Particle head, Speck original, int number){
		int[] coordinates = calculateScreenCoordinates(head.getScreenX(name), head.getScreenY(name));
		if(head.hasNext()){
			if(head.getNext().detonate()){
				head.setNext(null);
			}
		}
		if(head.hasNext()){
			int[] nCoordinates = calculateScreenCoordinates(head.getNext().getScreenX(name), head.getNext().getScreenY(name));
			Color c = head.getColor();
			Graphics g = getGraphics();
			g.setColor(c);
			g.drawLine(coordinates[0], coordinates[1], nCoordinates[0], nCoordinates[1]); 
			//g.fillOval((int) originalX - 4, (int) originalY - 4, 8, 8);
			paintParticle(head.getNext(), original, number + 1);
		}else{
			if(original.getTails() > Environment.getMaximumTails()){
				head.markForDeath();
				original.incrementTails(false);
			}
		}
	}
	public double getZoom() {
		return zoom;
	}
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getiOrientation() {
		double bottomHyp = Math.pow(1 - Math.pow(Math.sin(inclination), 2), 0.5);
		return bottomHyp * Math.cos(orientation);
	}
	public double getkOrientation() {
		return Math.sin(inclination);
	}
	public double getjOrientation() {
		double bottomHyp = Math.pow(1 - Math.pow(Math.sin(inclination), 2), 0.5);
		return bottomHyp * Math.sin(orientation);
	}
	public double getOrientation() {
		return orientation;
	}
	public void setOrientation(double orientation) {
		this.orientation = orientation;
	}
	public double getInclination() {
		return inclination;
	}
	public void setInclination(double inclination) {
		this.inclination = inclination;
	}
	
}