//Fernando Lavarreda

public class Curve{
	private Vector[] vectors;
	
	public Curve(Vector[] vectors){
		this.vectors = vectors;
	}
	
	
	public Vector[] getVectors(){
		return vectors;
	}
	
	
	public Curve translate(Vector move){
		Vector[] nvectors = new Vector[vectors.length];
		int counter = 0;
		for(Vector t: vectors){
			nvectors[counter++] = t.translate(move);
		}
		return new Curve(nvectors);
	}
	
	
	public Curve translate(double dx, double dy){
		Vector[] nvectors = new Vector[vectors.length];
		int counter = 0;
		for(Vector t: vectors){
			nvectors[counter++] = t.translate(dx, dy);
		}
		return new Curve(nvectors);
	}
	
	
	//Rotate respect to origin
	public Curve rotate(double rad){
		Vector[] nvectors = new Vector[vectors.length];
		int counter = 0;
		for(Vector t: vectors){
			nvectors[counter++] = t.rotate(rad);
		}
		return new Curve(nvectors);
	}
	
	
	public Curve rotate(double rad, Vector pivot){
		Vector[] nvectors = new Vector[vectors.length];
		int counter = 0;
		for(Vector t: vectors){
			nvectors[counter++] = t.rotate(rad, pivot);
		}
		return new Curve(nvectors);
	}
	
	
	public Curve scale(double factor){
		Vector[] nvectors = new Vector[vectors.length];
		int counter = 0;
		for(Vector t: vectors){
			nvectors[counter++] = t.scale(factor);
		}
		return new Curve(nvectors);
	}
	
	
	public Curve copy(){
		Vector[] nvectors = new Vector[vectors.length];
		int counter = 0;
		for(Vector t: vectors){
			nvectors[counter++] = t.copy();
		}
		return new Curve(nvectors);
	}
	
	
	public String toString(){
		StringBuilder str = new StringBuilder(200);
		for(Vector t: vectors){
			str.append("\nVector:\n");
			str.append(t.toString());
		}
		return  str.toString();
	}
	
}