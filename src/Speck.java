public class Speck extends Particle{
	private double velocityI, velocityJ, velocityK;
	public Speck(double x, double y, double z) {
		super(x, y, z);
		velocityI = 0;
		velocityJ = 0;
		velocityK = 0;
	}
	public void move(){
		incrementTails(true);
		Particle memory = new Particle(getX(), getY(), getZ());
		for(Perspective v: getScreenCoordinates()){
			Perspective copy = new Perspective(v.getName(), v.getiOrientation(), v.getjOrientation(), v.getkOrientation(), memory);
			memory.addView(copy);
			copy.setScreenX(v.getScreenX());
			copy.setScreenY(v.getScreenY());
		}
		setX(getX() + velocityI * Environment.getTimeIncrements());
		setY(getY() + velocityJ * Environment.getTimeIncrements());
		setZ(getZ() + velocityK * Environment.getTimeIncrements());
		for(Perspective v: getScreenCoordinates()){
			v.calculateScreenCoordinates();
		}
		memory.setNext(getNext());
		setNext(memory);
	}
	public double getVelocityI() {
		return velocityI;
	}
	public void setVelocityI(double velocityI) {
		this.velocityI = velocityI;
	}
	public double getVelocityJ() {
		return velocityJ;
	}
	public void setVelocityJ(double velocityJ) {
		this.velocityJ = velocityJ;
	}
	public double getVelocityK() {
		return velocityK;
	}
	public void setVelocityK(double velocityK) {
		this.velocityK = velocityK;
	}
}