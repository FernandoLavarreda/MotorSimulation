
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;


public class Animation extends JPanel implements ActionListener{
	private Timer timer;
	private JSlider speed;
	private Link[] snapshot;
	private SliderCrank[] pistons;
	private CamFollower[] camfollowers;
	private double instantRotationPistons;
	private double instantRotationFollowers;
	private double[] anglePistons, angleCams;
	
	
	public Animation(SliderCrank mechanism, CamFollower camfollowerIn, CamFollower camfollowerOut){
		super();
		int separation = 300;
		pistons = new SliderCrank[] {
									 mechanism, 
									 mechanism.copy(mechanism.getAbsoluteCoords().translate(separation, 0)),
									 mechanism.copy(mechanism.getAbsoluteCoords().translate(separation*2, 0)),
									 mechanism.copy(mechanism.getAbsoluteCoords().translate(separation*3, 0)),
									};
		
		camfollowers = new CamFollower[] {
										  camfollowerIn,
										  camfollowerIn.copy(camfollowerIn.getAbsoluteCoords().translate(separation, 0)),
										  camfollowerIn.copy(camfollowerIn.getAbsoluteCoords().translate(separation*2, 0)),
										  camfollowerIn.copy(camfollowerIn.getAbsoluteCoords().translate(separation*3, 0)),
										  camfollowerOut,
										  camfollowerOut.copy(camfollowerOut.getAbsoluteCoords().translate(separation, 0)),
										  camfollowerOut.copy(camfollowerOut.getAbsoluteCoords().translate(separation*2, 0)),
										  camfollowerOut.copy(camfollowerOut.getAbsoluteCoords().translate(separation*3, 0))
										  };
		timer = new Timer(1, this);
		snapshot = new Link[28];
		anglePistons = new double[] {0, Math.PI, Math.PI, 0};
		angleCams = new double[] {
								  -Math.PI/180*59, -Math.PI/180*59, -Math.PI/180*59, -Math.PI/180*59,
								  0, 0, Math.PI/180*109, 0
								 };
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
		instantRotationPistons = -speed.getValue()*Math.PI/300;
		instantRotationFollowers = -speed.getValue()*Math.PI/600;
		
		for(int counter = 0; counter<anglePistons.length;counter++){
			anglePistons[counter]+=instantRotationPistons;
			angleCams[counter]+=instantRotationFollowers;
			angleCams[counter+4]+=instantRotationFollowers;
		}
		System.arraycopy(pistons[0].solve(anglePistons[0], true), 0, snapshot, 0, 3);
		System.arraycopy(pistons[1].solve(anglePistons[1], true), 0, snapshot, 3, 3);
		System.arraycopy(pistons[2].solve(anglePistons[2], true), 0, snapshot, 6, 3);
		System.arraycopy(pistons[3].solve(anglePistons[3], true), 0, snapshot, 9, 3);
		System.arraycopy(camfollowers[0].solve(angleCams[0]), 0, snapshot, 12, 2);
		System.arraycopy(camfollowers[1].solve(angleCams[1]), 0, snapshot, 14, 2);
		System.arraycopy(camfollowers[2].solve(angleCams[2]), 0, snapshot, 16, 2);
		System.arraycopy(camfollowers[3].solve(angleCams[3]), 0, snapshot, 18, 2);
		
		System.arraycopy(camfollowers[4].solve(angleCams[4]), 0, snapshot, 20, 2);
		System.arraycopy(camfollowers[5].solve(angleCams[5]), 0, snapshot, 22, 2);
		System.arraycopy(camfollowers[6].solve(angleCams[6]), 0, snapshot, 24, 2);
		System.arraycopy(camfollowers[7].solve(angleCams[7]), 0, snapshot, 26, 2);
		
	}
	
	
	public void actionPerformed(ActionEvent e){
		updateSnapshot();
		repaint();
	}
	
	
	/*private class Chamber implements Runnable{
		
	}*/
}



