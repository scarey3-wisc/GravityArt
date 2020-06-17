import java.awt.Color;

public class Particle extends Object{
	private Particle next;
	private int tails;
	private boolean nullify;
	public Particle(double x, double y, double z) {
		super(x, y, z);
		tails = 0;
		nullify = false;
	}
	public boolean detonate(){
		return nullify;
	}
	public void markForDeath(){
		nullify = true;
	}
	public int getTails() {
		return tails;
	}
	public void incrementTails(boolean positive) {
		if(positive){
			tails++;
		}else{
			tails--;
		}
	}
	public boolean hasNext(){
		return next != null;
	}
	public Particle getNext() {
		return next;
	}
	public void setNext(Particle next) {
		this.next = next;
	}
	public Color getColor(){
		if(hasNext()){
			double distance = Toolkit.findDistance(this, next)/Environment.getTimeIncrements();
			int rgb = (int) Math.abs(distance / Environment.getColorIncrements());
			if(rgb < 256){
				return new Color(0, 0, rgb);
			}else if(rgb < 512){
				return new Color(0, rgb - 256, 511 - rgb);
			}else if(rgb < 768){
				return new Color(rgb - 512, 767 - rgb, 0);
			}else if(rgb < 1024){
				return new Color(255, rgb - 768, 0);
			}else if(rgb < 1280){
				return new Color(255, 255, rgb - 1024);
			}else{
				return new Color(255, 255, 255);
			}
		}else{
			return new Color(0, 0, 0);
		}
		//return color;
	}
	public void editView(String name, double newI, double newJ, double newK){
		super.editView(name, newI, newJ, newK);
		if(hasNext()){
			next.editView(name, newI, newJ, newK);
		}
	}
	public void addView(String name, double i, double j, double k){
		super.addView(name, i, j, k);
		if(hasNext()){
			next.addView(name, i, j, k);
		}
	}
	public void addView(Perspective v){
		super.addView(v);
		if(hasNext()){
			next.addView(v);
		}
	}
}