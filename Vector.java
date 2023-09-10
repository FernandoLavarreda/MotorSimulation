//Fernando Lavarreda

public class Vector{
	private double x, y;
	
	public Vector(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	
	public double getX(){
		return x;
	}
	
	
	public double getY(){
		return y;
	}
	
	
	public Vector translate(Vector move){
		return new Vector(x+move.x, y+move.y);
	}
	
	
	public Vector translate(double dx, double dy){
		return new Vector(x+dx, y+dy);
	}
	
	
	public Vector reflect(){
		return new Vector(-x, -y);
	}
	
	
	//Rotate respect to origin
	public Vector rotate(double rad){
		double nx = Math.cos(rad)*x-Math.sin(rad)*y;
		double ny = Math.sin(rad)*x+Math.cos(rad)*y;
		return new Vector(nx, ny);
	}
	
	
	public Vector rotate(double rad, Vector pivot){
		double dx = x-pivot.x;
		double dy = y-pivot.y;
		return new Vector(dx, dy).rotate(rad).translate(pivot);
	}
	
	
	public Vector scale(double factor){
		return new Vector(factor*x, factor*y);
	}
	
	public double length(){
		return Math.pow(Math.pow(x, 2)+Math.pow(y, 2), 0.5);
	}
	
	//Get angle between tow vectors
	public static double angle(Vector v1, Vector v2){
		double dx = v2.getX()-v1.getX();
		double dy = v2.getY()-v1.getY();
		return Math.atan2(dy, dx);
	}
	
	
	public static double distance(Vector v1, Vector v2){
		double dx = v2.getX()-v1.getX();
		double dy = v2.getY()-v1.getY();
		return Math.pow(Math.pow(dx, 2)+Math.pow(dy, 2), 0.5);
	}
	
	
	public String toString(){
		return "x: "+x+"\ny: "+y;
	}

}