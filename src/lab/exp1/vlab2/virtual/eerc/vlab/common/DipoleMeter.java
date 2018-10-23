package eerc.vlab.common;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;
/** Draw on a JPanel rather than on JApplet's Panel. **/
//public class GraphPlotter extends JPanel 
public class DipoleMeter extends JPanel 
{
	int counter=0;
	int state=0;
	int width, height;
	
	int x_offset=30;
	int y_offset=100;
	
	
	private float f_l=0.3f;
	private float v_l=0.3f;
	private float time=0;
	boolean match=false;
	

	String heading="Displacment Vs Time Trend";	
	String x_str="";
	String y_str="";
	String t_interval="";	
	String x_unit="";
	String y_unit="";
	
	Color y_color = Color.BLUE;
    ArrayList<Integer> m_vrtx = new ArrayList<Integer>();
    ArrayList<Integer> fs = new ArrayList<Integer>();
    ArrayList<Integer> fc = new ArrayList<Integer>();
	
	public DipoleMeter (int w,int h,String str_x,String str_y) {
		   //setBackground(Color.black);
		   setPreferredSize(new Dimension(w,h));
		   
		   width = w;
		   height = h;
		   x_str=str_x;
		   y_str=str_y;
		   
		   counter=0;
		   state =0;
		   time=0;
		   
	  } 
		 		 		  
	  /** Display a Graph. **/
	  public void drawGraph (int chk) {
	    // First paint background
		 Graphics g1 = getGraphics();
		 Graphics2D g = (Graphics2D) g1;
		    // Use anti-aliasing to avoid "jaggies" in the lines
	    // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		       // RenderingHints.VALUE_ANTIALIAS_ON);
	     createGraph(g,chk); 
	     
	  } //
	  
	  public void drawPauseGraph (int chk) {
		    // First paint background
			 Graphics g1 = getGraphics();
			 Graphics2D g = (Graphics2D) g1;
			    // Use anti-aliasing to avoid "jaggies" in the lines
		    // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			       // RenderingHints.VALUE_ANTIALIAS_ON);
			 pauseTimeGraph(g,chk); 
		     
		  } //
	   
	 

	 public void init() 	{
		width = 300;
		height = getSize().height;
		System.out.println("height" + height);
		height = 500;
		counter=0;
	 }

	
	
	public void paint( Graphics g1 ) {
	     //Graphics2D g2d = (Graphics2D)g;
		Graphics2D g = (Graphics2D) g1;
	    // Use anti-aliasing to avoid "jaggies" in the lines
	   // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	       // RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(Color.GRAY);
	    //g.draw3DRect(5, 5, width-10, height-10, true);
	    g.fillRect(0, 0, width-1, height);
	    
	    if(state==1)
	    	createGraph(g,1);
	    else if(state==0)
	    	pauseTimeGraph(g,1);
	    //labelTheAxis(g);
	}
   		     
	public void createGraph(Graphics g,int chk)
	{
		
		Color c=getBackground();
	    g.setColor(c);
	    g.setColor(Color.GRAY);
	    //g.draw3DRect(5, 5, width-10, height-10, true);
	    g.fillRect(0, 0, width-1, height);
	   
	    // Draw scaling
	    g.setColor(Color.RED);
	    g.drawLine(x_offset, y_offset, width - x_offset, y_offset);
	    g.drawLine(x_offset, y_offset - (int)(height*0.5), x_offset, y_offset + (int)(height*0.5));
	    
	    
	    int i=counter+3;
	    int x=x_offset;
	    for(; x < width && i<fs.size()-1 ; x++,i++){
	    	g.setColor(y_color);
	       	g.drawLine(x,y_offset , x,y_offset+fs.get(i));
	       	g.setColor(Color.green);
	       	g.drawLine(x,y_offset , x,y_offset+fc.get(i));
	    }
	    // last dot
	    g.setColor(Color.RED);
	    if(i<fs.size()){
	    	g.fillOval(x, y_offset+fs.get(i-1), 5, 5);
	    	g.fillOval(x, y_offset+fc.get(i-1), 5, 5);
	    }

	    
	    if(fs.size()> width*0.75)
	    	counter++;//m_vrtx.remove(0);	    
	   // g.drawString("Interval on X-axis - 0.02", 10, 10);
	    
	    drawAllText(g);
	    
	}
	
	
	public void pauseTimeGraph(Graphics g,int chk)
	{
		
		Color c=getBackground();
	    g.setColor(c);
	    g.setColor(Color.GRAY);
	    //g.draw3DRect(5, 5, width-10, height-10, true);
	    g.fillRect(0, 0, width-1, height);
	   
	    // Draw scaling
	    g.setColor(Color.RED);
	    g.drawLine(x_offset, y_offset, width - x_offset, y_offset);
	    g.drawLine(x_offset, y_offset - (int)(height*0.5), x_offset, y_offset + (int)(height*0.5));
	    
	    g.setColor(y_color);
	    //m_vrtx.size()/150
	    int step = (int)(fs.size()/(width*0.75));
	    if(step==0) step=1;
	    int i=3;
	    int x=x_offset;
	    for(; x < width - x_offset && i<fs.size()-step ; x++,i+=step){
	       	g.setColor(y_color);
	       	g.drawLine(x,y_offset , x,y_offset+fs.get(i+step));
	       	g.setColor(Color.green);
	       	g.drawLine(x,y_offset , x,y_offset+fc.get(i+step));
	    }
	    // last dot
	    g.setColor(Color.RED);
	    if(i<fs.size()-1){
	    	g.fillOval(x, y_offset+ fs.get(i), 5, 5);
	    	g.fillOval(x, y_offset+ fc.get(i), 5, 5);
	    }

	    drawAllText(g);
	    
	}
	
	public void removeGraph(Graphics g)
	{
			     
	}
	
		
	private void drawAllText(Graphics g){
		g.setColor(Color.BLACK);
		Font prev_font = g.getFont();;
		Font cur_Font = new Font("Book Antiqua", Font.BOLD, 10);
		g.setFont(cur_Font);
		int i=0;
		//char[] str = x_str.toCharArray();
		// Axis
		for(int x= (int)(width*0.5) - (int)(x_str.length()); i<x_str.length();x+=10,i++)
			g.drawString(x_str.substring(i,i+1), x, y_offset + (int)(height*0.5));
		i=0;
		for(int y= y_offset - y_str.length()*5; i<y_str.length();y+=20,i++)
			g.drawString(y_str.substring(i,i+1), 10, y_offset - (int)(height*0.5) + y);
		
		// Heading
		g.setColor(Color.orange);		
		cur_Font = new Font("Book Antiqua", Font.BOLD, 16);
		g.setFont(cur_Font);
		g.drawString(heading, (int)(width*0.25),15);
		
		// Current Value
		
		
	}
	
	

	  /** Display an image. **/
	  public void addGraphValue (int val) {
		  
		  m_vrtx.add(new Integer(val));
		 
	  } //
	  
	  public void update (float dt) {
//		  fs.add(10);
//		  fc.add(10);
		  time +=dt;
		  float mass=5;
		  // fixed frequency
	  	  double T = 2*Math.PI*Math.sqrt(f_l/mass);
	  	  double f = (2*Math.PI)/T;
	  		
		  fs.add((int)(45*Math.sin(f*time)));
		
		  // variable frequency
		  T = 2*Math.PI*Math.sqrt(v_l/mass);
  		  f = (2*Math.PI)/T;
	  	 		  
  		  fc.add((int)(-45*Math.sin(f*time)));
  		  
		  //m_vrtx.add(new Integer(val));
		 
	  }
	  
	  public void clearGraphValue () {
		  m_vrtx.clear();
		  
		  fc.clear();
		  fs.clear();
		  time =0;
		  counter =0;
	  } //
	  
	  public void setHeading (String h) {
		  
		  heading = h;
	  } //
	  
	  public void setTimeInterval(String intrval){
		  t_interval = intrval;
	  }
	  
	  
	  public void setAxisUnit(String x_unit,String y_unit){
		  this.x_unit = x_unit;
		  this.y_unit = y_unit;
	  }
	  public void setYAxisColor(Color c){
		  y_color = c;
	  }
	  
	  public boolean isFrequencyMatch() {return match;}
	  public void setFixedCapacitor( float l ) {	f_l =l; if(v_l == f_l) match =false; else match=false;}
	  public void setVariableCapacitor( float l ) { v_l =l; if(v_l == f_l) match =true; else match=false;}
	  
	  public void setState(int s){
		  state =s;
	  }
	  
}
