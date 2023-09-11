
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;


public class Animation extends JPanel implements ActionListener{
	private SliderCrank piston, piston2, piston3, piston4;
	private Timer timer;
	private double angle, angle2, angle3, angle4, anglecam;
	private Link[] snapshot;
	private double instantRotation = 0.1;	//0.1 Math.PI/3;10_000rpm
	private double instantRotationFollowers = 0.1; //0.1	//Math.PI/3;10_000rpm
	private JSlider speed;
	private CamFollower camfollower;
	
	public Animation(SliderCrank mechanism, CamFollower camfollower){
		super();
		piston = mechanism;
		this.camfollower = camfollower;
		//*
		piston2 = mechanism.copy(piston.getAbsoluteCoords().translate(82, 0));
		piston3 = mechanism.copy(piston.getAbsoluteCoords().translate(164, 0));
		piston4 = mechanism.copy(piston.getAbsoluteCoords().translate(248, 0));
		//*/
		timer = new Timer(1, this);
		snapshot = new Link[14];
		angle = 0;
		angle2 = Math.PI;
		angle3 = Math.PI;
		angle4 = 0;
		anglecam = Math.PI/180*108;
		speed = new JSlider();
		add(speed);
		updateSnapshot();
		timer.start();
	}
	
	
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		for(Link t: snapshot){
			for(Curve u: t.getCurves()){
				int[] xs = Arrays.stream(u.getVectors()).mapToInt((Vector v)->(int)v.getX()).toArray();
				int[] ys = Arrays.stream(u.getVectors()).mapToInt((Vector v)->(int)v.getY()).toArray();
				g2d.drawPolyline(xs, ys, xs.length);
			}
		}
	}
	
	
	private void updateSnapshot(){
		instantRotation = -speed.getValue()*Math.PI/300;
		instantRotationFollowers = -speed.getValue()*Math.PI/600;
		angle+=instantRotation;
		angle2+=instantRotation;
		angle3+=instantRotation;
		angle4+=instantRotation;
		anglecam+=instantRotationFollowers;
		System.arraycopy(piston.solve(angle, true), 0, snapshot, 0, 3);
		System.arraycopy(piston2.solve(angle2, true), 0, snapshot, 3, 3);
		System.arraycopy(piston3.solve(angle3, true), 0, snapshot, 6, 3);
		System.arraycopy(piston4.solve(angle4, true), 0, snapshot, 9, 3);
		System.arraycopy(camfollower.solve(anglecam), 0, snapshot, 12, 2);
	}
	
	
	public void actionPerformed(ActionEvent e){
		updateSnapshot();
		repaint();
	}
	
}



