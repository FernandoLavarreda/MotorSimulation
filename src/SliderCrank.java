

public class SliderCrank implements ISolve{
	private Link[] links;
	private double displacement, rotation;
	private double[] lengths;
	private Vector absoluteCoords;
	private boolean inversion;
	
	public SliderCrank(Link[] links, double displacement, double rotation, Vector absoluteCoords) throws IllegalArgumentException{
		if (links.length != 3) throw new IllegalArgumentException("Only three links can be part of a SliderCrank");
		this.rotation = rotation;
		this.links = new Link[3];
		this.lengths = new double[2];
		this.displacement = displacement;
		double a1 = Vector.angle(links[0].getConnection(0), links[0].getConnection(1));
		double a2 = Vector.angle(links[1].getConnection(0), links[1].getConnection(1));
		
		this.links[0] = links[0].translate(links[0].getConnection(0).reflect()).rotate(-a1+rotation);
		this.links[1] = links[1].translate(links[2].getConnection(1).reflect()).rotate(rotation+Math.PI-a2);
		this.links[2] = links[2].translate(links[2].getConnection(0).reflect()).rotate(-rotation);
		this.lengths[0] = Vector.distance(links[0].getConnection(0), links[0].getConnection(1));
		this.lengths[1] = Vector.distance(links[1].getConnection(0), links[1].getConnection(1));
		this.absoluteCoords = absoluteCoords;
		this.inversion = true;
	}
	
	
	public SliderCrank(Link[] links, double displacement, double rotation, Vector absoluteCoords, boolean inversion){
		this(links, displacement, rotation, absoluteCoords);
		this.inversion = inversion;
	}
	
	
	public Vector getAbsoluteCoords(){
		return absoluteCoords;
	}
	
	
	public Link[] solve(double rad){
		double angle_coupler, dx, dy;
		Link[] lnks = new Link[3];
		
		if (inversion){
			angle_coupler = Math.asin((lengths[0]*Math.sin(rad)-displacement)/lengths[1]);
		}else{
			angle_coupler = Math.asin(-(lengths[0]*Math.sin(rad)-displacement)/lengths[1])+Math.PI;
		}
		
		lnks[0] = links[0].rotate(rad).translate(absoluteCoords);
		lnks[1] = links[1].rotate(angle_coupler);
		dx = lnks[0].getConnection(1).getX()-lnks[1].getConnection(0).getX();
		dy = lnks[0].getConnection(1).getY()-lnks[1].getConnection(0).getY();
		lnks[1] = lnks[1].translate(dx, dy);
		dx = lnks[1].getConnection(1).getX()-links[2].getConnection(0).getX();
		dy = lnks[1].getConnection(1).getY()-links[2].getConnection(0).getY();
		lnks[2] = links[2].translate(dx, dy);
		return lnks;
	}
	
	
	public double height(double rad){
		double angle_coupler, dx, dy, distance;
		Link[] lnks = new Link[3];
		if (inversion){
			angle_coupler = Math.asin((lengths[0]*Math.sin(rad)-displacement)/lengths[1]);
		}else{
			angle_coupler = Math.asin(-(lengths[0]*Math.sin(rad)-displacement)/lengths[1])+Math.PI;
		}
		lnks[0] = links[0].rotate(rad).translate(absoluteCoords);
		lnks[1] = links[1].rotate(angle_coupler);
		dx = lnks[0].getConnection(1).getX()-lnks[1].getConnection(0).getX();
		dy = lnks[0].getConnection(1).getY()-lnks[1].getConnection(0).getY();
		lnks[1] = lnks[1].translate(dx, dy);
		dx = lnks[1].getConnection(1).getX()-links[2].getConnection(0).getX();
		dy = lnks[1].getConnection(1).getY()-links[2].getConnection(0).getY();
		lnks[2] = links[2].translate(dx, dy);
		distance = Vector.distance(lnks[0].getConnection(0), lnks[2].getConnection(0));
		return distance/(lengths[0]+lengths[1])*21+35;
	}
	
	
	public SliderCrank scale(double factor){
		Link[] lnks = new Link[3];
		lnks[0] = links[0].scale(factor);
		lnks[1] = links[1].scale(factor).rotate(-rotation-Math.PI);
		lnks[2] = links[2].scale(factor).rotate(+rotation);
		return new SliderCrank(lnks, displacement, rotation, absoluteCoords.copy(), inversion);
	}
	
	public SliderCrank copy(){
		return copy(absoluteCoords.copy());
	}
	
	
	public SliderCrank copy(Vector absoluteCoords){
		Link[] lnks = new Link[3];
		int counter = 0;
		for(Link t: links){
			lnks[counter++] = t.copy();
		}
		lnks[2] = lnks[2].rotate(rotation);
		return new SliderCrank(lnks, displacement, rotation, absoluteCoords, inversion);
	}
	
}

