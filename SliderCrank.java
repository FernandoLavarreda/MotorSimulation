

public class SliderCrank{
	private Link[] links;
	private double displacement, rotation;
	private double[] lengths;
	private Vector absoluteCoords;
	
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
	}
	
	
	public Link[] solve(double rad, boolean inversion){
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
	
}

