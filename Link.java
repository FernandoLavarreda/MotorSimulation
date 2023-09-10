//Fernando Lavarreda
import java.util.Scanner;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;


public class Link{
	private Curve[] curves;
	private Vector[] connections;
	
	public Link(Curve[] curves, Vector[] connections){
		this.curves = curves;
		this.connections = connections;
	}
	
	
	public static Link buildIO(InputStream input) throws IOException{
		int line = 1;
		int x, y;
		String[] xy;
		Scanner sc = new Scanner(input);
		sc.nextLine(); //header
		String token = "";
		ArrayList<Vector> connections = new ArrayList<Vector>(10);
		ArrayList<Curve> curves = new ArrayList<Curve>(10);
		Vector[] vs;
		while(sc.hasNextLine()){
			if(!(token.contains("Point")||token.contains("New"))){
				token = sc.nextLine();
				line++;
			}
			switch(token.split(" ")[0]){
				case "Point":
					token = sc.nextLine();
					line++;
					xy = token.split(",");
					try{
						x = (int) Double.parseDouble(xy[0]);
						y = (int) Double.parseDouble(xy[1]);
						connections.add(new Vector(x, y));
					}catch (NumberFormatException e){
						throw new IOException("Could not read number line: "+line);
					}
					break;
				case "New":
					ArrayList<Vector> temp_vectors = new ArrayList<Vector>(200);
					while(sc.hasNextLine()){
						token = sc.nextLine();
						line++;
						xy = token.split(",");
						if(token.contains("New")||token.contains("Point")){
							break;
						}
						try{
							x = (int) Double.parseDouble(xy[0]);
							y = (int) Double.parseDouble(xy[1]);
							temp_vectors.add(new Vector(x, y));
						}catch (NumberFormatException e){
							throw new IOException("Could not read number line: "+line);
						}
					}
					vs = new Vector[temp_vectors.size()];
					vs = temp_vectors.toArray(vs);
					curves.add(new Curve(vs));
					break;
			}
		}
		Curve[] cs = new Curve[curves.size()];
		cs = curves.toArray(cs);
		Vector[] cn = new Vector[connections.size()];
		cn = connections.toArray(cn);
		return new Link(cs, cn);
	}
	
	
	public Vector getConnection(int index) throws IllegalArgumentException{
		if (index<0||index>connections.length-1) throw new IllegalArgumentException("Out of bounds");
		return connections[index];
	}
	
	
	public void setConnections(Vector[] connections) throws IllegalArgumentException{
		if (connections.length<2) throw new IllegalArgumentException("Required at least two connections");
		this.connections = new Vector[connections.length];
		int counter = 0;
		for(Vector v: connections){
			this.connections[counter++] = v;
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
		
		return new Link(ncurves, nvectors);
	}
	
	
	public Curve[] getCurves(){
		return curves;
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
		
		return new Link(ncurves, nvectors);
	}
	
	
	//Rotate respect to origin
	public Link rotate(double rad){
		Curve[] ncurves = new Curve[curves.length];
		Vector[] nvectors = new Vector[connections.length];
		int counter = 0;
		for(Curve t: curves){
			ncurves[counter++] = t.rotate(rad);
		}
		counter = 0;
		for(Vector t: connections){
			nvectors[counter++] = t.rotate(rad);
		}
		
		return new Link(ncurves, nvectors);
	}
	
	
	public Link rotate(double rad, Vector pivot){
		Curve[] ncurves = new Curve[curves.length];
		Vector[] nvectors = new Vector[connections.length];
		int counter = 0;
		for(Curve t: curves){
			ncurves[counter++] = t.rotate(rad, pivot);
		}
		
		counter = 0;
		for(Vector t: connections){
			nvectors[counter++] = t.rotate(rad, pivot);
		}
		return new Link(ncurves, nvectors);
	}
	
	
	public Link scale(double factor){
		Curve[] ncurves = new Curve[curves.length];
		Vector[] nvectors = new Vector[connections.length];
		int counter = 0;
		for(Curve t: curves){
			ncurves[counter++] = t.scale(factor);
		}
		
		counter = 0;
		for(Vector t: connections){
			nvectors[counter++] = t.scale(factor);
		}
		
		return new Link(ncurves, nvectors);
	}
	
	
	public String toString(){
		StringBuilder str = new StringBuilder(400);
		for(Curve t: curves){
			str.append("\nCurve:\n");
			str.append(t.toString());
		}
		for(Vector t: connections){
			str.append("\nConnection:\n");
			str.append(t.toString());
		}
		
		return str.toString();
	}


}