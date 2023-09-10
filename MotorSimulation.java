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
			//UIManager.setLookAndFeel(new FlatIntelliJLaf());
		}catch (Exception e) {
			System.out.println(e);
		}
		
		JFrame frame = new JFrame("MotorSim");
		SliderCrank mech = MotorSimulation.buildMotor();
		frame.add(new Animation(mech));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static SliderCrank buildMotor() throws IOException{
		Link piston = Link.buildIO(new FileInputStream(new File("C:\\Users\\ferna\\Downloads\\cabezal.csv")));
		Link crank = Link.buildIO(new FileInputStream(new File("C:\\Users\\ferna\\Downloads\\ciguenal.csv")));
		Link coupler = Link.buildIO(new FileInputStream(new File("C:\\Users\\ferna\\Downloads\\biela.csv")));
		Link[] links = {crank, coupler, piston};
		Vector[] conCoupler = {new Vector(20, 10.64), new Vector(20, 158)};
		Vector[] conPiston = {new Vector(17, 0), new Vector(17, 0)};
		Vector[] conCrank = {new Vector(0, 0), new Vector(0, 42)};
		crank.setConnections(conCrank);
		coupler.setConnections(conCoupler);
		piston.setConnections(conPiston);
		return new SliderCrank(links, 0, Math.PI/2, new Vector(400, 600));
	}

}