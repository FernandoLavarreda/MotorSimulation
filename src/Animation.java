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
	private int cores, separation, start;
	private long time = System.currentTimeMillis();
	private Plotter[] plots;
	private double[][] pistonHeights, camHeights;
	
	public Animation(SliderCrank mechanism, CamFollower camfollowerIn, CamFollower camfollowerOut, Link[] background, int separation, Plotter[] plots){
		super();
		cores = 4; //Should be [1-4] -- Only 4 works with current implementation of plots
		this.separation= separation;
		this.start = (int) mechanism.getAbsoluteCoords().getX();
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
		
		//Offset exhaust valves  so they have same height as intake
		for(int counter=4;counter<camfollowers.length;counter++){
			camfollowers[counter].setOffset(5.0);
		}
		
		pistonHeights = new double[][] {
					plots[0].getDatay()[0], 
					plots[1].getDatay()[0],
					plots[2].getDatay()[0],
					plots[3].getDatay()[0],
		};
		
		camHeights = new double[][] {
					plots[0].getDatay()[1], 
					plots[1].getDatay()[1],
					plots[2].getDatay()[1],
					plots[3].getDatay()[1],
					plots[0].getDatay()[2],
					plots[1].getDatay()[2],
					plots[2].getDatay()[2],
					plots[3].getDatay()[2],
		};
		
		timer = new Timer(10, this);
		snapshot = new Link[28+background.length];
		System.arraycopy(background, 0, snapshot, 28, background.length);
		anglePistons = new double[] {0, -Math.PI, -Math.PI, 0};
		//Max intake cam at 108° afeter TDC
		//Max exhaust cam at 112°C before TDC
		//Firing 1-3-4-2
		angleCams = new double[] {
								  Math.PI/180*115, Math.PI/180*25, -Math.PI/180*145, -Math.PI/180*65,
								  Math.PI/180*35, -Math.PI/180*55, Math.PI/180*125, -Math.PI/180*145
								 };
		speed = new JSlider();
		speed.setValue(0);
		speedMarker = new JLabel();
		add(speed);
		add(speedMarker);
		this.plots = plots;
		updateSnapshot();
		timer.start();
		setPreferredSize(new Dimension(1000, 900));
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
		g2d.setColor(Color.RED);
		if(Math.abs((anglePistons[0]+3*Math.PI)%(4*Math.PI))<0.3){
			g2d.fillOval(start-5, 300, 10, 10);
			return;
		}
		if(Math.abs((anglePistons[1]+3*Math.PI)%(4*Math.PI))<0.3){
			g2d.fillOval(start+separation-5, 300, 10, 10);
			return;
		}
		if(Math.abs((anglePistons[2]+Math.PI)%(4*Math.PI))<0.3){
			g2d.fillOval(start+separation*2-5, 300, 10, 10);
			return;
		}
		if(Math.abs((anglePistons[3]+Math.PI)%(4*Math.PI))<0.3){
			g2d.fillOval(start+separation*3-5, 300, 10, 10);
			return;
		}
	}
	
	
	private void updateSnapshot(){
		long follows = System.currentTimeMillis();
		instantRotationPistons = -speed.getValue()*Math.PI/150;
		instantRotationFollowers = -speed.getValue()*Math.PI/300;
		speedMarker.setText(""+(Math.abs((int)(instantRotationPistons*30_000/Math.PI)/(follows-time)))+" rpm"); //Just visual updates on screen don't allow to properly set correct speed
		time = follows;
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
		
		double[][] heightsPistons, heightsCams;
		int remainderGraphs = plots.length%cores;
		int graphsPerCore = plots.length/cores;
		
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
				//Graphs
				heightsPistons = new double[graphsPerCore][];
				heightsCams = new double[graphsPerCore*2][];
				for(int i = 0; i<graphsPerCore;i++){
					heightsPistons[i] = pistonHeights[counter+i];
					heightsCams[i*2] = camHeights[(counter+i)*2];
					heightsCams[i*2+1] = camHeights[(counter+i)*2+1];
				}
			}else{
				arr = new SliderCrank[pistonsPerCore+remainder];
				carr = new CamFollower[(pistonsPerCore+remainder)*2];
				rotationPistons = new double[pistonsPerCore+remainder];
				rotationCams = new double[(pistonsPerCore+remainder)*2];
				System.arraycopy(pistons, counter*pistonsPerCore, arr, 0, pistonsPerCore+remainder);
				System.arraycopy(camfollowers, counter*pistonsPerCore*2, carr, 0, (pistonsPerCore+remainder)*2);
				System.arraycopy(anglePistons, counter*pistonsPerCore, rotationPistons, 0, pistonsPerCore+remainder);
				System.arraycopy(angleCams, counter*pistonsPerCore*2, rotationCams, 0, (pistonsPerCore+remainder)*2);
				//Graphs
				heightsPistons = new double[remainderGraphs+graphsPerCore][];
				heightsCams = new double[(remainderGraphs+graphsPerCore)*2][];
				for(int i = 0; i<graphsPerCore+remainderGraphs;i++){
					heightsPistons[i] = pistonHeights[counter+i];
					heightsCams[i*2] = camHeights[(counter+i)*2];
					heightsCams[i*2+1] = camHeights[(counter+i)*2+1];
				}
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
			AsyncSolutionHeight<SliderCrank> heightscranks = new AsyncSolutionHeight<SliderCrank>(arr, rotationPistons, heightsPistons);
			thread = new Thread(heightscranks);
			thread.start();
			threadpool.add(thread);
			AsyncSolutionHeight<CamFollower> heightscams = new AsyncSolutionHeight<CamFollower>(carr, rotationCams, heightsCams);
			thread = new Thread(heightscams);
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
		for(Plotter p: plots){
			p.repaint();
		}
		repaint();
	}
	
	
}



