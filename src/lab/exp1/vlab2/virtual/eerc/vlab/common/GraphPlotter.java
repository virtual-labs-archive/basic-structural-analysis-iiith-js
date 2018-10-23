package eerc.vlab.common;

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.*;
import java.util.ArrayList;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.*;
/** Draw on a JPanel rather than on JApplet's Panel. **/
//public class GraphPlotter extends JPanel 
public class GraphPlotter extends JPanel implements MouseListener, MouseMotionListener 
{
	private Image mImage;
	int width, height;
	int flag;
	int mx, my;  // the mouse coordinates
	boolean isButtonPressed = false;
	public GraphPlotter () {
		   setBackground(Color.white);
		   setPreferredSize(new Dimension(800, 130));
		   flag=0;
		  } 
		 		 		  
		  /** Display a Graph. **/
		  public void drawGraph (int chk) {
		    // First paint background

		     Graphics g1 = getGraphics(); 
		     Graphics2D g = (Graphics2D)g1;
		     createGraph(g,chk); 
		     
		  } //
		  
		  /** Display an image. **/
		  public void addValue (int val) {
			  m_vrtx.add(new Integer(val));
		  } //
		  
		  int     m_timestep;
		  int 	  m_vrtx_size;
		  ArrayList m_vrtx = new ArrayList();



	public void init() 	{
//		setBackground(Color.white);
		width = getSize().width;
		height = 130;//getSize().height;
		//addMouseListener( this );
		//addMouseMotionListener( this );
	}

	public void mouseEntered( MouseEvent e ) {
	}
	public void mouseExited( MouseEvent e ) {
	}
	public void mouseClicked( MouseEvent e ) {
	}
	public void mousePressed( MouseEvent e ) {  // called after a button is pressed down
	}
	public void mouseReleased( MouseEvent e ) {  // called after a button is released
	}
	public void mouseMoved( MouseEvent e ) {  // called during motion when no buttons are down
	
		//mx = e.getX();
		//my = e.getY();
		//e.consume();
	}
	public void mouseDragged( MouseEvent e ) {  // called during motion with buttons down
	}

	public void paint( Graphics g1 ) {
		     Graphics2D g = (Graphics2D)g1;
		     
		     createGraph(g,0);
	}
   		     
	public void createGraph(Graphics2D g,int chk)
	{
		
		 Color c=g.getBackground();
		 g.setColor(Color.red);
		 g.drawString("", 15, 25);
		 g.setColor(Color.blue);
		 int x1 = 500;
		 int y1 = 90;
		 int x2 = 500;
		 int y2 = 90;
	// Graph Box
	//
	     g.draw3DRect(10, 0, 1000, 125, true);
	     // commented     Changed by jeet
        // g.drawLine(10,-100,10,190);             
	     //g.drawLine(10,190,1400,190); 
	     //g.drawLine(10,0,1400,0); 
 
		//Scale  
		  
	     g.drawString("Interval on X-axis - 0.02", 100,25);
	     g.drawString("Interval on Y-axis - 10", 100,40);
	     g.drawString("Y-coordinate in terms of mm ", 100,55);

		  
		// Coordinate Axes		 

         g.drawLine(500,0,500,130); 
         g.drawLine(500,60,1000,60); 
	     g.setColor(Color.black);
	     
	     g.drawLine(600,120,780,120); 
	     g.drawString("Time", 800,125);
	     g.drawLine(850,120,900,120); 
	     g.drawString(">", 900,125);


         g.drawLine(450,70,450,120); 		    
	     g.drawString("Displacement", 400,60);
         g.drawLine(450,10,450,40); 		    
	     g.drawString("^", 448,12);

//	     for (int i= 1; i<m_vrtx.size(); i++) 
//    		     g.drawString("|", i*5 + 500,63);
	     
	     // set tool tip  // commented     Changed by jeet
//	     if(mx > 500 && my > 0 && my < 200)
//	     {
//		     float x=(float)mx;
//		     x=(x-500)/250;
//
//		     float y=(float)my;
//		     y=(50-y)*7;		
//
//		     String str ="(";
//		     str = str + Float.toString(x);
//		     str=str+",";
//		     str = str + Float.toString(y);
//		     str=str+")";
//		     setToolTipText(str);	
//	     }
	     
	     for (int i= 0; i<=13; i++) {
           	    	 g.drawString("--", 493,10*i);
	     }
	    
	    
	     for (int i= 0; i < m_vrtx.size()-1; i++) 
	     {
	    	 g.setColor(Color.RED);
		     y1 = 60 - (Integer)m_vrtx.get(i) ;
		     y2 = 60 - (Integer)m_vrtx.get(i+1) ;
		     x2 = i*5 + 500 ;
		     g.draw(new Line2D.Double(x1,y1,x2,y2));
		     x1=x2;
		     g.setColor(Color.DARK_GRAY);
		     if(y2==60) { g.drawString("|", i*5 + 500,63); g.drawString(String.valueOf(i*5), i*5 + 505,63);}
		     
	     }
	     if(m_vrtx.size() > 110)
	     {
		     removeGraph(g);
/*			     c = new Color((float)238.0/255,(float)238.0/255,(float)238.0/255);
			     g.setColor(c);
			     x1 = 500;
			     x2 = 500;
			     y2 = 90;
			     for (int i=0; i < m_vrtx.size()-1; i++) 
			     {
				     y1 = 95 - (Integer)m_vrtx.get(i) ;
				     y2 = 95 - (Integer)m_vrtx.get(i+1) ;
				     x2 = i*5 + 500 ;
				     g.draw(new Line2D.Double(x1,y1,x2,y2));
				     x1=x2;

			     }
			     for (int i= 1; i<m_vrtx.size(); i++) 
			     {
				     g.drawString("|", i*5 + 500,98);
			     }
			     g.setColor(Color.black);
			     g.drawLine(500,95,1400,95); 

			     m_vrtx.clear();*/
	     }
	     if(chk==1)
		     removeGraph(g);
	}
	public void removeGraph(Graphics2D g)
	{
			     Color c = new Color((float)238.0/255,(float)238.0/255,(float)238.0/255);
			     g.setColor(c);
			     int x1 = 500;
			     int x2 = 500;
			     int y2 = 90;
			     int y1;
			     for (int i=0; i < m_vrtx.size()-1; i++) 
			     {
				     y1 = 60 - (Integer)m_vrtx.get(i) ;
				     y2 = 60 - (Integer)m_vrtx.get(i+1) ;
				     x2 = i*5 + 500 ;
				     g.draw(new Line2D.Double(x1,y1,x2,y2));
				     if(y2==60) { g.drawString("|", i*5 + 500,63); g.drawString(String.valueOf(i*5), i*5 + 505,63);}
				     x1=x2;

			     }
//			     for (int i= 1; i<m_vrtx.size(); i++) 
//			     {
//				     g.drawString("|", i*5 + 500,63);
//			     }
			     
			     // commented     Changed by jeet
			     //g.setColor(Color.black);
			     //g.drawLine(500,65,1400,65); 

			     m_vrtx.clear();
	}
}
