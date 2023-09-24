import java.awt.*;
import java.io.File;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.FileInputStream;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.UIManager.LookAndFeelInfo;


public class MotorSimulation{
	public static void main(String[] args) throws IOException{
		try {
			UIManager.setLookAndFeel(new FlatDarkLaf());
		}catch (Exception e) {
			System.out.println("Did not find UI requested");
			System.out.println(e);
		}
		
		if(args.length<1){
			System.out.println("No file location provided");
			System.exit(1);
		}
		
		
		
		double scale = 1;
		int separation = (int)(250*scale);
		
		
		String pistonLoc = args[0]+"\\cabezal.csv";
		String crankLoc = args[0]+"\\ciguenal.csv";
		String couplerLoc = args[0]+"\\biela.csv";
		String followerInLoc = args[0]+"\\followeri.csv";
		String camInLoc = args[0]+"\\cami.csv";
		String followerOutLoc = args[0]+"\\followero.csv";
		String camOutLoc = args[0]+"\\camo.csv";
		String chamber = args[0]+"\\chamber.csv";
		
		
		SliderCrank mech = MotorSimulation.buildPiston(pistonLoc, crankLoc, couplerLoc).scale(scale);
		CamFollower camIn = MotorSimulation.buildCamFollower(followerInLoc, camInLoc, Math.PI/180*70, new Vector(71, 205+(1-scale)*320)).scale(scale);
		CamFollower camOut = MotorSimulation.buildCamFollower(followerOutLoc, camOutLoc, Math.PI/180*110, new Vector(184, 210+(1-scale)*325)).scale(scale);
		Link backgroundLink = Link.buildIO(new FileInputStream(new File(chamber))).scale(scale).rotate(Math.PI).translate(125, 550);
		Link[] background  = {backgroundLink, backgroundLink.translate(separation, 0), backgroundLink.translate(separation*2, 0), backgroundLink.translate(separation*3, 0)};
		
		
		
		JFrame frame = new JFrame("MotorSim");
		JPanel mainPanel = new JPanel();
		JPanel sidePanel = new JPanel();
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
		sidePanel.add(Box.createVerticalGlue());
		
		
		Plotter[] plots = new Plotter[4];
		int size = 200;
		double[][] xs, ys;
		Color[] colors = {new Color(255, 255, 255), new Color(0, 255, 0), new Color(255, 0, 0)};
		for(int plot=0; plot<plots.length;plot++){
			xs = new double[3][];
			ys = new double[3][];
			for(int i=0; i<3;i++){
				xs[i] = new double[size];
				ys[i] = new double[size];
			}
			
			for(int counter=0;counter<size;counter++){
				xs[0][counter] = (double)counter/size;
				ys[0][counter] = 40;
				ys[1][counter] = 40;
				ys[2][counter] = 40;
			}
			xs[1] = xs[0];
			xs[2] = xs[0];
			plots[plot] = new Plotter(1.0, 1.0, xs, ys, colors);
			plots[plot].transform(0, 1, 39, 60);
			plots[plot].setPreferredSize(new Dimension(500, 100));
			sidePanel.add(plots[plot]);
		}
		
		
		sidePanel.add(Box.createVerticalGlue());
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(new Animation(mech, camIn, camOut, background, separation, plots));
		mainPanel.add(sidePanel);
		mainPanel.add(Box.createHorizontalGlue());
		frame.add(mainPanel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	
	public static SliderCrank buildPiston(String pistonLoc, String crankLoc, String couplerLoc) throws IOException{
		Link piston = Link.buildIO(new FileInputStream(new File(pistonLoc)));
		Link crank = Link.buildIO(new FileInputStream(new File(crankLoc)));
		Link coupler = Link.buildIO(new FileInputStream(new File(couplerLoc)));
		Link[] links = {crank, coupler, piston};
		Vector[] conCoupler = {new Vector(20, 10.64), new Vector(20, 158)};
		Vector[] conPiston = {new Vector(17, 0), new Vector(17, 0)};
		Vector[] conCrank = {new Vector(0, 0), new Vector(0, 42)};
		crank.setConnections(conCrank);
		coupler.setConnections(conCoupler);
		piston.setConnections(conPiston);
		return new SliderCrank(links, 0, Math.PI/2, new Vector(125, 550));
	}
	
	
	public static CamFollower buildCamFollower(String followerLoc, String camLoc, double rotation, Vector place) throws IOException{
		Link follower = Link.buildIO(new FileInputStream(new File(followerLoc)));
		Cam cam = Cam.buildIO(new FileInputStream(new File(camLoc)));
		Vector[] conFollower = {new Vector(0, 0), new Vector(0, 0)};
		Vector[] conCam = {new Vector(0, 0), new Vector(0, 0)};
		follower.setConnections(conFollower);
		cam.setConnections(conCam);
		return new CamFollower(cam, follower, new Vector(0, 0), rotation, place);
	}	
}