public class Toolkit{
	public static double findDistance(double xOne, double yOne, double zOne, double xTwo, double yTwo, double zTwo){
		return Math.pow(Math.pow(xOne - xTwo, 2) + Math.pow(yOne - yTwo, 2) + Math.pow(zOne - zTwo, 2), 0.5);
	}
	public static double[] findUnitVector(Object one, Object two){
		return findUnitVector(two.getX() - one.getX(), two.getY() - one.getY(), two.getZ() - one.getZ());
	}
	public static double[] findUnitVector(double amountI, double amountJ, double amountK){
		double magnitude = Math.pow(Math.pow(amountI, 2) + Math.pow(amountJ, 2) + Math.pow(amountK, 2), 0.5);
		return new double[]{amountI/magnitude, amountJ/magnitude, amountK/magnitude};
	}
	public static double findDistance(Object one, Object two){
		return findDistance(one.getX(), one.getY(), one.getZ(), two.getX(), two.getY(), two.getZ());
	}
	public static void sleep(long amount){
		try {
			Thread.sleep(amount);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static double[] findUVComponents(double UI, double UJ, double UK, double VI, double VJ, double VK, 
			double pX, double pY, double pZ, double oX, double oY, double oZ){
		//okay, I'm not even going to try and comment this one step by step. Just no. See Animation Basics. Space if
		//you want to find something better
		if(UI != 0 && (VI / UI * UJ) != VJ){
			double dX = pX - oX;
			double brX = VI * -1;
			double dXdivided = dX / UI;
			double bRXdivided = brX / UI;
			double dY = pY - oY;
			double dXdividedTimesUJ = dXdivided * UJ;
			double bRXdividedTimesUJ = bRXdivided * UJ;
			double rightSideTwo = dY - dXdividedTimesUJ;
			double totalB = VJ + bRXdividedTimesUJ;
			double B = rightSideTwo / totalB;
			double BVI = B * VI;
			double rightSideOne = dX - BVI;
			double A = rightSideOne / UI;
			return new double[]{A, B};
		}
		if(UJ != 0 && (VJ / UJ * UK) != VK){
			double dY = pY - oY;
			double brY = VJ * -1;
			double dYdivided = dY / UJ;
			double bRYdivided = brY / UJ;
			double dZ = pZ - oZ;
			double dYdividedTimesUK = dYdivided * UK;
			double bRYdividedTimesUK = bRYdivided * UK;
			double rightSideTwo = dZ - dYdividedTimesUK;
			double totalB = VK + bRYdividedTimesUK;
			double B = rightSideTwo / totalB;
			double BVJ = B * VJ;
			double rightSideOne = dY - BVJ;
			double A = rightSideOne / UJ;
			return new double[]{A, B};
		}
		if(UK != 0 && (VK / UK * UI) != VI){
			double dZ = pZ - oZ;
			double brZ = VK * -1;
			double dZdivided = dZ / UK;
			double bRZdivided = brZ / UK;
			double dX = pX - oX;
			double dZdividedTimesUI = dZdivided * UI;
			double bRZdividedTimesUI = bRZdivided * UI;
			double rightSideTwo = dX - dZdividedTimesUI;
			double totalB = VI + bRZdividedTimesUI;
			double B = rightSideTwo / totalB;
			double BVK = B * VK;
			double rightSideOne = dZ - BVK;
			double A = rightSideOne / UK;
			return new double[]{A, B};
		}
		//if we start getting funny results, try switching A and B for the below three blocks.
		if(VI != 0 && (UI / VI * VJ) != UJ){
			double dX = pX - oX;
			double brX = UI * -1;
			double dXdivided = dX / VI;
			double bRXdivided = brX / VI;
			double dY = pY - oY;
			double dXdividedTimesVJ = dXdivided * VJ;
			double bRXdividedTimesVJ = bRXdivided * VJ;
			double rightSideTwo = dY - dXdividedTimesVJ;
			double totalB = UJ + bRXdividedTimesVJ;
			double B = rightSideTwo / totalB;
			double BUI = B * UI;
			double rightSideOne = dX - BUI;
			double A = rightSideOne / VI;
			return new double[]{A, B};
		}
		if(VJ != 0 && (UJ / VJ * VK) != UK){
			double dY = pY - oY;
			double brY = UJ * -1;
			double dYdivided = dY / VJ;
			double bRYdivided = brY / VJ;
			double dZ = pZ - oZ;
			double dYdividedTimesVK = dYdivided * VK;
			double bRYdividedTimesVK = bRYdivided * VK;
			double rightSideTwo = dZ - dYdividedTimesVK;
			double totalB = UK + bRYdividedTimesVK;
			double B = rightSideTwo / totalB;
			double BUJ = B * UJ;
			double rightSideOne = dY - BUJ;
			double A = rightSideOne / VJ;
			return new double[]{A, B};
		}
		if(VK != 0 && (UK / VK * VI) != UI){
			double dZ = pZ - oZ;
			double brZ = UK * -1;
			double dZdivided = dZ / VK;
			double bRZdivided = brZ / VK;
			double dX = pX - oX;
			double dZdividedTimesVI = dZdivided * VI;
			double bRZdividedTimesVI = bRZdivided * VI;
			double rightSideTwo = dX - dZdividedTimesVI;
			double totalB = UI + bRZdividedTimesVI;
			double B = rightSideTwo / totalB;
			double BUK = B * UK;
			double rightSideOne = dZ - BUK;
			double A = rightSideOne / VK;
			return new double[]{A, B};
		}
		System.out.println("FIND UV COMPONENTS HAS FAILED ME!!!!");
		return new double[]{0, 0};
	}
	public static double[] findIntersect(double xnot, double ynot, double znot, double xone, double yone, double zone, double a, double b, double c, double d){
		//xnot, ynot, znot are the coordinates for one point on a line.
		//xone, yone, zone are the coordinates for another point on the line.
		//a, b, c, d are the parts of the equation for a plane: Ax + By + Cz = D.
		//returns the point at which the line and the plane intersect, if such a point exists
		double at = a * (xone - xnot);
		double an = a * xnot;
		double bt = b * (yone - ynot);
		double bn = b * ynot;
		double ct = c * (zone - znot);
		double cn = c * znot;
		//step one: get the ts and the regular numbers of the parametric equation for the line
		double allT = at + bt + ct;
		double allN = an + bn + cn;
		double addAllN = d - allN;
		double T = addAllN/allT;
		//step two: solve for T
		double x = xnot + T * (xone - xnot);
		double y = ynot + T * (yone - ynot);
		double z = znot + T * (zone - znot);
		//step three: substitute T into the parametric equation to find the point of convergence.
		return new double[]{x, y, z};
	}
}