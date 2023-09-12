import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.ArrayList;


public class Animation extends JPanel implements ActionListener{
	private Timer timer;
	private JSlider speed;
	private JLabel speedMarker;
	private Link[] snapshot, background;
	private SliderCrank[] pistons;
	private CamFollower[] camfollowers;
	private double instantRotationPistons;
	private double instantRotationFollowers;
	private double[] anglePistons, angleCams;
	private int cores;
	private static int c = 1;
	
	public Animation(SliderCrank mechanism, CamFollower camfollowerIn, CamFollower camfollowerOut, Link[] background){
		super();
		cores = 2;
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
		snapshot = new Link[28+background.length];
		System.arraycopy(background, 0, snapshot, 28, background.length);
		anglePistons = new double[] {0, Math.PI, Math.PI, 0};
		//Max intake cam at 108° afeter TDC
		//Max exhaust cam at 112°C before TDC
		//Firing 1-3-4-2
		angleCams = new double[] {
								  Math.PI/180*115, Math.PI/180*25, -Math.PI/180*145, -Math.PI/180*65,
								  Math.PI/180*35, -Math.PI/180*55, Math.PI/180*125, -Math.PI/180*145
								 };
		speed = new JSlider();
		speedMarker = new JLabel();
		add(speed);
		add(speedMarker);
		updateSnapshot();
		timer.start();
		setPreferredSize(new Dimension(600, 900));
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
		instantRotationPistons = -speed.getValue()*Math.PI/60;
		instantRotationFollowers = -speed.getValue()*Math.PI/120;
		speedMarker.setText(""+(speed.getValue()*20)+" rpm"); //Just visual updates on screen don't allow to properly set correct speed
		
		for(int counter = 0; counter<anglePistons.length;counter++){
			anglePistons[counter]+=instantRotationPistons;
			angleCams[counter]+=instantRotationFollowers;
			angleCams[counter+4]+=instantRotationFollowers;
		}
		
		ArrayList<Thread> threadpool = new ArrayList<Thread>(cores);
		ArrayList<AsyncSolution<SliderCrank>> solutions = new ArrayList<AsyncSolution<SliderCrank>>(4);
		ArrayList<AsyncSolution<CamFollower>> solutionsCams = new ArrayList<AsyncSolution<CamFollower>>(4);
		int remainder = pistons.length%cores;
		int pistonsPerCore = pistons.length/cores;
		SliderCrank[] arr;
		CamFollower[] carr;
		double[] rotationPistons, rotationCams;
		
		for(int counter = 0; counter<cores;counter++){
			if(counter!=cores-1){
				arr = new SliderCrank[pistonsPerCore];
				carr = new CamFollower[pistonsPerCore*2];
				rotationPistons = new double[pistonsPerCore];
				rotationCams = new double[pistonsPerCore*2];
				System.arraycopy(pistons, counter*pistonsPerCore, arr, 0, pistonsPerCore);
				System.arraycopy(camfollowers, counter*pistonsPerCore*2, carr, 0, pistonsPerCore*2);
				System.arraycopy(anglePistons, counter*pistonsPerCore, rotationPistons, 0, pistonsPerCore);
				System.arraycopy(angleCams, counter*pistonsPerCore*2, rotationCams, 0, pistonsPerCore*2);
			}else{
				arr = new SliderCrank[pistonsPerCore+remainder];
				carr = new CamFollower[(pistonsPerCore+remainder)*2];
				rotationPistons = new double[pistonsPerCore+remainder];
				rotationCams = new double[(pistonsPerCore+remainder)*2];
				System.arraycopy(pistons, counter*pistonsPerCore, arr, 0, pistonsPerCore+remainder);
				System.arraycopy(camfollowers, counter*pistonsPerCore*2, carr, 0, (pistonsPerCore+remainder)*2);
				System.arraycopy(anglePistons, counter*pistonsPerCore, rotationPistons, 0, pistonsPerCore+remainder);
				System.arraycopy(angleCams, counter*pistonsPerCore*2, rotationCams, 0, (pistonsPerCore+remainder)*2);
			}
			AsyncSolution<SliderCrank> async = new AsyncSolution<SliderCrank>(arr, rotationPistons);
			solutions.add(async);
			Thread thread = new Thread(async);
			threadpool.add(thread);
			thread.start();
			AsyncSolution<CamFollower> asyncCam = new AsyncSolution<CamFollower>(carr, rotationCams);
			solutionsCams.add(asyncCam);
			thread = new Thread(asyncCam);
			thread.start();
			threadpool.add(thread);
		}
		
		for(Thread t: threadpool){
			try{
				t.join();
			}catch(InterruptedException e){
				System.out.println("Could not solve");
			}
		}
		
		Link[] snap;
		int pointer = 0;
		for(AsyncSolution<SliderCrank> t: solutions){
			snap = t.getSolutions();
			System.arraycopy(snap, 0, snapshot, pointer, snap.length);
			pointer+=snap.length;
		}
		for(AsyncSolution<CamFollower> t: solutionsCams){
			snap = t.getSolutions();
			System.arraycopy(snap, 0, snapshot, pointer, snap.length);
			pointer+=snap.length;
		}
		
	}
	
	
	public void actionPerformed(ActionEvent e){
		updateSnapshot();
		repaint();
	}
	
	
}



