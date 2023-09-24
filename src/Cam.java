//Fernando Lavarreda
import java.util.Scanner;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;


public class Cam extends Link{
	private Double[] angles, radiuses;
	
	public Cam(Curve[] curves, Vector[] connections, Double[] angles, Double[] radiuses){
		super(curves, connections);
		this.angles = angles;
		this.radiuses = radiuses;
	}
	
	
	public static Cam buildIO(InputStream input) throws IOException{
		Scanner sc = new Scanner(input).useDelimiter("\t");
		sc.nextLine(); //header
		String token = "";
		ArrayList<Vector> vectors = new ArrayList<Vector>(722);
		ArrayList<Double> radiuses = new ArrayList<Double>(722);
		ArrayList<Double> degrees = new ArrayList<Double>(722);
		double degree, radius;
		double radian;
		int x, y;
		int line = 1;
		Curve[] curves = new Curve[1];
		Vector[] vs;
		Double[] angs, radii;
		while(sc.hasNextLine()){
			try{
				degree = sc.nextDouble();
				radian = degree*Math.PI/180;
				for(int i=0;i<11;i++){
					sc.next();
				}
				radius = sc.nextDouble();
				x = (int) (-Math.cos(radian)*radius);
				y = (int) (-Math.sin(radian)*radius);
				vectors.add(new Vector(x, y));
				radiuses.add(radius);
				degrees.add(radian);
			}catch (NumberFormatException | InputMismatchException e){
				throw new IOException("Could not read number line: "+ line);
			}
			sc.nextLine();
			line++;
			
		}
		vs = new Vector[vectors.size()];
		angs = new Double[degrees.size()];
		radii = new Double[radiuses.size()];
		vs = vectors.toArray(vs);
		angs = degrees.toArray(angs);
		radii = radiuses.toArray(radii);
		curves[0] = new Curve(vs);
		Vector[] connections = {new Vector(0, 0)};
		return new Cam(curves, connections, angs, radii);
	}
	
	
	public double getLift(double radian){
		int found;
		double rad;
		if(radian<-Math.PI*2||radian>Math.PI*2){
			rad = Math.abs(radian%(Math.PI*2));
		}
		else{
			rad = Math.abs(radian);
		}
		int location = Arrays.binarySearch(angles, rad);
		if (location==-1){
			return (radiuses[1]-radiuses[0])/(angles[1]-angles[0])*(rad-angles[0])+radiuses[0];
		}else if (location == -radiuses.length-1){
			return (radiuses[radiuses.length-1]-radiuses[radiuses.length-2])/(angles[radiuses.length-1]-angles[radiuses.length-2])*(rad-angles[radiuses.length-1])+radiuses[radiuses.length-1];
		}else if(location<0){
			found = Math.abs(location);
			return (radiuses[found-1]-radiuses[found-2])/(angles[found-1]-angles[found-2])*(rad-angles[found-2])+radiuses[found-2];
		}else{
			return radiuses[location];
		}	
	}
	
	
	public Link translate(Vector move){
		Curve[] ncurves = new Curve[curves.length];
		Vector[] nvectors = new Vector[connections.length];
		int counter = 0;
		for(Curve t: curves){
			ncurves[counter++] = t.translate(move);
		}
		
		counter = 0;
		for(Vector t: connections){
			nvectors[counter++] = t.translate(move);
		}
		
		return new Cam(ncurves, nvectors, angles, radiuses);
	}
	
	
	public Link translate(double dx, double dy){
		Curve[] ncurves = new Curve[curves.length];
		Vector[] nvectors = new Vector[connections.length];
		int counter = 0;
		for(Curve t: curves){
			ncurves[counter++] = t.translate(dx, dy);
		}
		
		counter = 0;
		for(Vector t: connections){
			nvectors[counter++] = t.translate(dx, dy);
		}
		
		return new Cam(ncurves, nvectors, angles, radiuses);
	}
	
	
	public Cam scale(double factor){
		Curve[] ncurves = new Curve[curves.length];
		Vector[] nvectors = new Vector[connections.length];
		Double[] rads = new Double[radiuses.length];
		int counter = 0;
		for(Curve t: curves){
			ncurves[counter++] = t.scale(factor);
		}
		
		counter = 0;
		for(Vector t: connections){
			nvectors[counter++] = t.scale(factor);
		}
		counter=0;
		for(Double radius: radiuses){
			rads[counter++] = radius*factor;
		}
		
		return new Cam(ncurves, nvectors, angles, rads);
	}
	
	public Cam copy(){
		Curve[] crvs = new Curve[curves.length];
		Vector[] cons = new Vector[connections.length];
		Double[] angs = new Double[angles.length];
		Double[] rads = new Double[radiuses.length];
		int counter = 0;
		for(Curve c: curves){
			crvs[counter++] = c.copy();
		}
		counter = 0;
		for(Vector v: connections){
			cons[counter++] = v.copy();
		}
		for(counter=0;counter<angs.length;counter++){
			angs[counter] = angles[counter];
			rads[counter] = radiuses[counter];
		}
		
		return new Cam(crvs, cons, angs, rads);
	}

}