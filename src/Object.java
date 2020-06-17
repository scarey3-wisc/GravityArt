import java.util.LinkedList;

public class Object{
	private double x, y, z;
	private LinkedList<Perspective> screenCoordinates;
	public Object(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		screenCoordinates = new LinkedList<Perspective>();
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	public LinkedList<Perspective> getScreenCoordinates() {
		return screenCoordinates;
	}
	public void setScreenCoordinates(LinkedList<Perspective> screenCoordinates) {
		this.screenCoordinates = screenCoordinates;
	}
	public void addView(String name, double i, double j, double k){
		Perspective v = new Perspective(name, i, j, k, this);
		addView(v);
		v.calculateScreenCoordinates();
	}
	public void addView(Perspective v){
		screenCoordinates.add(v);
		v.calculateScreenCoordinates();
	}
	public void editView(String name, double newI, double newJ, double newK){
		boolean found = false;
		for(Perspective v: screenCoordinates){
			if(v.getName().equals(name)){
				v.setiOrientation(newI);
				v.setjOrientation(newJ);
				v.setkOrientation(newK);
				v.calculateScreenCoordinates();
				found = true;
			}
		}
		if(!found){
			System.out.println("WE DIDN'T FIND VIEW " + name.toUpperCase() + "!!!!! AH!!! WE ARE ALL GOING TO DIE!!!");
		}
	}
	public void calculateScreenCoordinates(String name){
		boolean found = false;
		for(Perspective v: screenCoordinates){
			if(v.getName().equals(name)){
				v.calculateScreenCoordinates();
				found = true;
			}
		}
		if(!found){
			System.out.println("WE DIDN'T FIND VIEW " + name.toUpperCase() + "!!!!! AH!!! WE ARE ALL GOING TO DIE!!!");
		}
	}
	public double getScreenX(String name){
		return getScreenCoordinates(name)[0];
	}
	public double getScreenY(String name){
		return getScreenCoordinates(name)[1];
	}
	public double[] getScreenCoordinates(String name){
		for(Perspective v: screenCoordinates){
			if(v.getName().equals(name)){
				return new double[]{v.getScreenX(), v.getScreenY()};
			}
		}
		System.out.println("The requested perspective " + name + " is not available for this object");
		return new double[]{0, 0};
	}
}