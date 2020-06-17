public class GravityArt{
	public static void main(String[] args){
		Environment e = new Environment();
		e.addView(0, Math.PI/-2, "Inward", 10, 10);
		e.addView(-Math.PI, 0, "Leftward", 10, 10);
		e.circularCloud(100, 1, 20);
		e.createNewForceNode(0.2, 0.2, 0, -0.1);
		e.run();
	}
}