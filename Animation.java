
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;


public class Animation extends JPanel implements ActionListener{
	private SliderCrank piston;
	private Timer timer;
	private double angle;
	private Link[] snapshot;
	
	
	public Animation(SliderCrank mechanism){
		super();
		piston = mechanism;
		timer = new Timer(1, this);
		snapshot = new Link[3];
		angle = 0;
		snapshot = piston.solve(angle, true);
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
	
	
	public void actionPerformed(ActionEvent e){
		angle+=1;
		snapshot = piston.solve(angle, true);
		repaint();
	}
	
}



