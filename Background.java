import java.awt.*;
import javax.swing.*;
import java.util.Arrays;


public class Background extends JPanel{
	private Link[] links;
	
	public Background(Link[] links){
		super();
		this.links = links;
		//setBackground(new Color(0,0,0,65));
		setPreferredSize(new Dimension(600, 900));
		setBounds(0, 0, 600, 900);
	}
	
	
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		for(Link t: links){
			for(Curve u: t.getCurves()){
				int[] xs = Arrays.stream(u.getVectors()).mapToInt((Vector v)->(int)v.getX()).toArray();
				int[] ys = Arrays.stream(u.getVectors()).mapToInt((Vector v)->(int)v.getY()).toArray();
				g2d.drawPolyline(xs, ys, xs.length);
			}
		}
	}
	


}


