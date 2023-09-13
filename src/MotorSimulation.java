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
		
		int separation = 300;
		
		String pistonLoc = args[0]+"\\cabezal.csv";
		String crankLoc = args[0]+"\\ciguenal.csv";
		String couplerLoc = args[0]+"\\biela.csv";
		String followerInLoc = args[0]+"\\followeri.csv";
		String camInLoc = args[0]+"\\cami.csv";
		String followerOutLoc = args[0]+"\\followero.csv";
		String camOutLoc = args[0]+"\\camo.csv";
		String chamber = args[0]+"\\chamber.csv";
		
		
		SliderCrank mech = MotorSimulation.buildPiston(pistonLoc, crankLoc, couplerLoc);
		CamFollower camIn = MotorSimulation.buildCamFollower(followerInLoc, camInLoc, Math.PI/180*70, new Vector(246, 205));
		CamFollower camOut = MotorSimulation.buildCamFollower(followerOutLoc, camOutLoc, Math.PI/180*110, new Vector(359, 210));
		Link backgroundLink = Link.buildIO(new FileInputStream(new File(chamber))).rotate(Math.PI).translate(300, 550);
		Link[] background  = {backgroundLink, backgroundLink.translate(separation, 0), backgroundLink.translate(separation*2, 0), backgroundLink.translate(separation*3, 0)};
		
		
		JFrame frame = new JFrame("MotorSim");
		frame.add(new Animation(mech, camIn, camOut, background, separation));
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
		return new SliderCrank(links, 0, Math.PI/2, new Vector(300, 550));
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