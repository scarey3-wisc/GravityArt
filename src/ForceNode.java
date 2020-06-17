public class ForceNode extends Object{
	private double power;
	public ForceNode(double x, double y, double z, double power) {
		super(x, y, z);
		this.power = power;
	}
	public double getPower() {
		return power;
	}
	public void setPower(double power) {
		this.power = power;
	}
	public void force(Speck s){
		double force = Environment.getG() * power / Math.pow(Toolkit.findDistance(this, s), 2);
		force *= Environment.getTimeIncrements();
		s.setVelocityI(s.getVelocityI() + force * Toolkit.findUnitVector(s, this)[0]);
		s.setVelocityJ(s.getVelocityJ() + force * Toolkit.findUnitVector(s, this)[1]);
		s.setVelocityK(s.getVelocityK() + force * Toolkit.findUnitVector(s, this)[2]);
	}
}