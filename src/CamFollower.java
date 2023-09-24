


public class CamFollower implements ISolve{
	private Cam cam;
	private Link follower;
	private double rotation;
	private Vector camCenter, absoluteCoords;
	private double offset = 0.0;
	
	public CamFollower(Cam cam, Link follower, Vector camCenter, double rotation, Vector absoluteCoords){
		double af = Vector.angle(follower.getConnection(0), follower.getConnection(1));
		
		this.cam = (Cam) cam.translate(camCenter.reflect());
		this.follower = follower.translate(follower.getConnection(0).reflect()).rotate(-af+rotation);
		this.rotation = rotation;
		this.camCenter = camCenter;
		this.absoluteCoords = absoluteCoords;
	}
	
	
	public Link[] solve(double rad){
		Link[] lnks = new Link[2];
		lnks[0] = cam.rotate(rad+Math.PI).translate(absoluteCoords);
		double radius = cam.getLift(rad-rotation);
		double dx = radius*Math.cos(rotation)+absoluteCoords.getX();
		double dy = radius*Math.sin(rotation)+absoluteCoords.getY();
		lnks[1] = follower.translate(dx, dy);
		return lnks;
	}
	
	public void setOffset(double offset){
		this.offset = offset;
	}
	
	public double height(double rad){
		return cam.getLift(rad-rotation)+offset;
	}
	
	
	public CamFollower scale(double factor){
		Cam cm = cam.scale(factor);
		Link fw = follower.scale(factor);
		return new CamFollower(cm, fw.rotate(-rotation), camCenter.scale(factor), rotation, absoluteCoords.copy());
	}
	
	public CamFollower copy(){
		return new CamFollower(cam.copy(), follower.copy().rotate(-rotation), camCenter, rotation, absoluteCoords);
	}
	
	
	public CamFollower copy(Vector absoluteCoords){
		return new CamFollower(cam.copy(), follower.copy().rotate(-rotation), camCenter, rotation, absoluteCoords);
	}
	
	public Vector getAbsoluteCoords(){
		return absoluteCoords;
	}

}

