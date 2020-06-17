public class Perspective{
	private String name;
	private double iOrientation, jOrientation, kOrientation;
	private double screenX, screenY;
	private Object refers;
	public Perspective(String name, double iOrientation, double jOrientation, double kOrientation, Object refers){
		this.name = name;
		this.iOrientation = iOrientation;
		this.jOrientation = jOrientation;
		this.kOrientation = kOrientation;
		this.refers = refers;
		screenX = 0;
		screenY = 0;
	}
	public void calculateScreenCoordinates(){
		double a = iOrientation;
		double b = jOrientation;
		double c = kOrientation;
		double d = 0;
		double i = a;
		double j = b;
		double k = c;
		double objectX = refers.getX();
		double objectY = refers.getY();
		double objectZ = refers.getZ();
		//so far, this is pretty simple: we have the object's position, and we have the viewing plane.
		//Because we don't have a perspective point, merely a perspective direction, we are going to 
		//use a simple projection of the points onto the plane. In other words, we are going to take the vector
		//normal to the plane which passes through the object, and find where that vector intersects the plane.
		double normalX = objectX + iOrientation;
		double normalY = objectY + jOrientation;
		double normalZ = objectZ + kOrientation;
		double[] xyz = Toolkit.findIntersect(objectX, objectY, objectZ, normalX, normalY, normalZ, a, b, c, d);
		double planeX = xyz[0];
		double planeY = xyz[1];
		double planeZ = xyz[2];
		//so, now we have got our projection onto the plane. Annoyingly, this next part is the hardest.
		//Here is a quick outline of the next steps.
		//Step one: declare a semi-arbitrary U - V coordinate system on the plane.
		//One-sub-one: Find an origin on the plane, probably just the actual origin: 0, 0, 0.
		//One-sub-two: Find an 'up' vector on the plane, probably based on the maximization of z component.
		//One-sub-three: Use cross product to find left, right, and down vectors on the plane.
		//Step two: convert the xyz coordinates into the U - V coordinate system we have just dreamed up.
		//Step three: adjust the U - V coordinates to fit inside the screen properly. 
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
		//well, there we have it: a u and v vector. V is up, -V is down, U is right, -U is left.
		//to explain the nuUp notation, nu stands for not unit. That is all. Step one victory confirmed!
		//and I think we are going to put step two into a separate method, probably in Toolkit. See you after
		//a ton of work on that method!
		double[] UVCoords = Toolkit.findUVComponents(uI, uJ, uK, vI, vJ, vK, planeX, planeY, planeZ, xOrigin, yOrigin, zOrigin);
		//congratulations!!! Step two, in one method that I have absolutely no idea if it'll work.
		//but, assuming it does, we just multiply both parts by, say, 1000. We'll shift it over once
		//we have access to the size of the window, which we don't right now. Currently, 0, 0 means
		//the CENTER of the screen, not the upper left corner. We'll also handle zoom elsewhere.
		screenX = UVCoords[0] * 1000;
		screenY = UVCoords[1] * 1000;
	}
	public Object getRefers(){
		return refers;
	}
	public void setRefers(Object refers){
		this.refers = refers;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getiOrientation() {
		return iOrientation;
	}
	public void setiOrientation(double iOrientation) {
		this.iOrientation = iOrientation;
	}
	public double getjOrientation() {
		return jOrientation;
	}
	public void setjOrientation(double jOrientation) {
		this.jOrientation = jOrientation;
	}
	public double getkOrientation() {
		return kOrientation;
	}
	public void setkOrientation(double kOrientation) {
		this.kOrientation = kOrientation;
	}
	public double getScreenX() {
		return screenX;
	}
	public void setScreenX(double screenX) {
		this.screenX = screenX;
	}
	public double getScreenY() {
		return screenY;
	}
	public void setScreenY(double screenY) {
		this.screenY = screenY;
	}
}