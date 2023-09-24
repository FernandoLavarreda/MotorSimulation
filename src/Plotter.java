import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class Plotter extends JPanel{
	private int xlim, ylim;
	private double xrlim, yrlim;
	private boolean relative;
	private double[][] datax, datay;
	private int[][] tdatax, tdatay;
	private double xmax, xmin, ymax, ymin;
	private Color[] colors = {new Color(255, 255, 255)};
	private boolean ignorex = false;
	
	public Plotter(int xsize, int ysize, double[][] datax, double[][] datay){
		xlim = xsize;
		ylim = ysize;
		relative = false;
		this.datax = datax;
		this.datay = datay;
		tdatax = new int[datax.length][];
		tdatay = new int[datay.length][];
		int counter = 0;
		for(double[] t : datax){
			tdatax[counter] = new int[t.length];
			tdatay[counter++] = new int[t.length];
		}
	}
	
	
	public void setIgnoreX(boolean ignore){
		this.ignorex = ignore;
	}
	
	public Plotter(double relativex, double relativey, double[][] datax, double[][] datay){
		xrlim = relativex;
		yrlim = relativey;
		relative = true;
		this.datax = datax;
		this.datay = datay;
		tdatax = new int[datax.length][];
		tdatay = new int[datay.length][];
		int counter = 0;
		for(double[] t : datax){
			tdatax[counter] = new int[t.length];
			tdatay[counter++] = new int[t.length];
		}
	}
	
	
	public Plotter(int xsize, int ysize, double[][] datax, double[][] datay, Color[] colors){
		this(xsize, ysize, datax, datay);
		this.colors = colors;
	}
	
	
	public Plotter(double relativex, double relativey, double[][] datax, double[][] datay, Color[] colors){
		this(relativex, relativey, datax, datay);
		this.colors = colors;
	}
	
	public void transform(double xmin, double xmax, double ymin, double ymax){
		double xfactor, yfactor;
		double xdistance = Math.abs(xmax-xmin);
		double ydistance = Math.abs(ymax-ymin);
		int height = getHeight();
		int width = getWidth();
		if(relative){
			xfactor = xrlim*width;
			yfactor = yrlim*height;
			this.xmax = xmax;
			this.xmin = xmin;
			this.ymin = ymin;
			this.ymax = ymax;
		}else{
			xfactor = xlim;
			yfactor = ylim;
			width = xlim;
			height = ylim;
		}
		
		if(ignorex){
			for(int counter=0; counter<datax.length;counter++){
				for(int inner=0; inner<datax[counter].length;inner++){
					tdatay[counter][inner] = (int)((height)-(datay[counter][inner]-ymin)/ydistance*yfactor);
				}
			}
		}else{
			for(int counter=0; counter<datax.length;counter++){
				for(int inner=0; inner<datax[counter].length;inner++){
					tdatax[counter][inner] = (int)((datax[counter][inner]-xmin)/xdistance*xfactor);
					tdatay[counter][inner] = (int)((height)-(datay[counter][inner]-ymin)/ydistance*yfactor);
				}
			}

		}
	
	}
	
	
	public double[][] getDatay(){
		return datay;
	}
	
	
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		if(relative){
			transform(xmin, xmax, ymin, ymax);
		}
		for(int counter=0; counter<tdatax.length;counter++){
			g2.setColor(colors[counter%colors.length]);
			g2.drawPolyline(tdatax[counter], tdatay[counter], tdatax[counter].length);
		}
	}
	
	
}
