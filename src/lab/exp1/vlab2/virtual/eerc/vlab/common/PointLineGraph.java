package eerc.vlab.common;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;

import javax.swing.JPanel;
/** Draw on a JPanel rather than on JApplet's Panel. **/
//public class GraphPlotter extends JPanel 
public class PointLineGraph extends JPanel 
{
	private Image mImage;
	int state=0;
	int width, height;
	
	int x_offset=30;
	int y_offset=100;
	
	float 	cur_x=0;
	float 	cur_y=0;
	float 	scale_y=1;
	double 	abs_max_y=0;
	
	boolean  hide = false;
	boolean  m_Vdisplay;
	
	private String heading="";	
	private String x_str="";
	private String y_str="";
	private String x_unit="";
	private String y_unit="";
		
	private Color y_color = Color.BLUE;
	Color m_Bkg_color = Color.WHITE;//Color.GRAY;
	private int m_line[];
	private ArrayList<Point> m_vrtx = new ArrayList<Point>();
	private ArrayList<Point> m1_vrtx = new ArrayList<Point>();
	private ArrayList<Point> m2_vrtx = new ArrayList<Point>();
	
	
	public PointLineGraph (int w,int h,String str_x,String str_y) {
		   //setBackground(Color.black);
		   setPreferredSize(new Dimension(w,h));
		   
		   width = w;
		   height = h;
		   x_str=str_x;
		   y_str=str_y;
		   y_offset = h - 40;
		   
		   state =0;
		   m_Vdisplay = false;
		   m_line = new int [5]; // can be parameterized
		   clearLineFlag();
		   
	  } 
		 		 		  
	  /** Display a Graph. **/
	  public void drawGraph () {
	    // First paint background
		 if(hide) return;
		 checkOffscreenImage();
		    //Graphics offG = mImage.getGraphics();
		    //offG.setColor(getBackground());
		    //offG.fillRect(0, 0, width, height);
	     Graphics g1 = getGraphics();
	     if(state==1)
	    	runTimeGraph(mImage.getGraphics()); 
	     else if(state==0)
	    	pauseTimeGraph(mImage.getGraphics()); 
	    
	     g1.drawImage(mImage, 0, 0, null);
		    
		
	     
	  } //
	  
	  public void drawPauseGraph () {
		    // First paint background
		 if(hide) return;
		 checkOffscreenImage();
		    //Graphics offG = mImage.getGraphics();
		    //offG.setColor(getBackground());
		    //offG.fillRect(0, 0, width, height);
	    Graphics g1 = getGraphics();
	    if(state==0)
	    	pauseTimeGraph(mImage.getGraphics());
	  
	    g1.drawImage(mImage, 0, 0, null);
		     
	 } //
	   
	 private void checkOffscreenImage() {
		    Dimension d = getSize();
		    if (mImage == null || mImage.getWidth(null) != width || mImage.getHeight(null) != height) 
			    mImage = createImage(width, height);
	 }
	 
	 public void paint( Graphics g1 ) {
	     //Graphics2D g2d = (Graphics2D)g;
		
		checkOffscreenImage();
		 
		Graphics2D g = (Graphics2D) g1;
	    // Use anti-aliasing to avoid "jaggies" in the lines
	   // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	       // RenderingHints.VALUE_ANTIALIAS_ON);
	   
	    if(hide) return;
	    
	    if(state==1)
	    	runTimeGraph(mImage.getGraphics()); 
	    else if(state==0)
	    	pauseTimeGraph(mImage.getGraphics());
	   
	    g1.drawImage(mImage, 0, 0, null);
	        
	    
	    //labelTheAxis(g);
	 }
  		     
	public void runTimeGraph(Graphics g)
	{
		

	    drawAllText(g);
	    
	}
	
	/**
	 * @param g
	 */
	public void pauseTimeGraph(Graphics g)
	{
		
		g.setColor(m_Bkg_color);
	    //g.draw3DRect(5, 5, width-10, height-10, true);
	    g.fillRect(0, 0, width-1, height);
	    
	    showScale(g);
	    // Draw scaling
	    g.setColor(Color.BLUE);
	    g.drawLine(x_offset, y_offset, width - x_offset, y_offset);
	    g.drawLine(x_offset, y_offset - (int)(height*0.6), x_offset, y_offset );
	    
	    //Font prev_font = g.getFont();
	    Font cur_Font = new Font("Book Antiqua", Font.BOLD, 10);
		g.setFont(cur_Font);
		int step = (int)((width - 2*x_offset)/35);
	    
//		 Draw Scaled Graph
		g.setColor(Color.BLACK);
	    for(int i=0;i<m_vrtx.size();i++)
	    {
	    	g.drawString("x",x_offset+m_vrtx.get(i).x,y_offset-m_vrtx.get(i).y);
	    //	System.out.println("graph"+ (x_offset+m_vrtx.get(i).x ) +' '+(y_offset+m_vrtx.get(i).y));
	    }
	    for(int i=0;i< m1_vrtx.size();i++)
	    {
	    	g.drawLine(x_offset+m1_vrtx.get(i).x,y_offset+m1_vrtx.get(i).y ,x_offset+m2_vrtx.get(i).x,y_offset+m2_vrtx.get(i).y);
	    	//System.out.println("line "+(x_offset+m1_vrtx.get(i).x)+' '+(y_offset+m1_vrtx.get(i).y)+' '+' '+(x_offset+m2_vrtx.get(i).x)+' '+(y_offset+m2_vrtx.get(i).y));
	    }
	    
	    drawAllText(g);
	    
	}
	
		
		
	private void drawAllText(Graphics g){
		g.setColor(Color.BLACK);
//		Font prev_font = g.getFont();;
		Font cur_Font = new Font("Arial", Font.BOLD, 10); //Book Antiqua
		g.setFont(cur_Font);
		int i=0;
		//char[] str = x_str.toCharArray();
		// Axis
		int x= (int)(width*0.90) ;
		for(; i<x_str.length();x+=10,i++)
			g.drawString(x_str.substring(i,i+1), x, y_offset );
		
		g.drawString(x_unit ,  x+10,y_offset );
		
		i=0;
		int y = y_str.length()*5;
		for(; i<y_str.length();y-=10,i++)
			g.drawString(y_str.substring(i,i+1), 10, (int)(height*.5) - y);
		
		y-=10;
		g.drawString(y_unit , 10, (int)(height*.5) - y );
		
		// Heading
		g.setColor(new Color(100,100,100));//Color.black);//new Color(0.80f,0.36f,0.36f));		
		cur_Font = new Font("Arial", Font.BOLD, 14);
		g.setFont(cur_Font);
		g.drawString(heading, (int)(width*0.25),15);
		
	}
	
	private void showScale(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.lightGray);
	  
	    float[] dash1 = { 2f, 0f, 2f };
	    
	    Stroke strk= g2d.getStroke();
	    
	    g2d.setStroke(new BasicStroke(1, 
	            BasicStroke.CAP_BUTT, 
	            BasicStroke.JOIN_ROUND, 
	            1.0f, 
	            dash1,2f)
	    );
	    
	    Font cur_Font = new Font("Arial", Font.BOLD, 8);
		g.setFont(cur_Font);
		
	    int y_step=20;
	    for(int y= y_offset-y_step ; y>y_offset - (int)(height*0.6) ;y-=y_step)
	    	g2d.drawLine(x_offset,y, width - x_offset,y);
//	    for(int y= y_offset + y_step; y<y_offset + (int)(height*0.5);y+=y_step)
//	    	g2d.drawLine(x_offset,y, width - x_offset,y);
	
	    int i=y_step;
	    for(int y= y_offset-y_step ; y>y_offset - (int)(height*0.6) ;y-=y_step, i+=y_step)
	    	g.drawString(i + "",width - x_offset,y);
	    
	    y_step=20;
	    for(int x= x_offset + y_step ; x<width - x_offset;x+=y_step)
	    	g2d.drawLine(x,y_offset, x,y_offset - (int)(height*0.6));
	    i=1;
	    for(int x= x_offset + y_step ; x<width - x_offset;x+=y_step, i++)
	    	g.drawString((i*4) + "",x,y_offset+10);
	   	
	    g2d.setStroke(strk);
	   
	}

	  /** Display an image. **/
	 public void addPointValue (double valx,double valy) {
		  
		  m_vrtx.add(new Point((int)valx,(int)valy));
				 
	  }
	 public void addLineValue (float x1,float y1,float x2,float y2) {
		  
		  m1_vrtx.add(new Point((int)x1,(int)y1));
		  m2_vrtx.add(new Point((int)x2,(int)y2));
		 
	  } ///
	  /** Display an image. **/
	  
	  public void addCurrentGraphValue (float x,float y) {		  
		  cur_x=x;
		  cur_y=y;		 
	  } //
	  
	  public void clearGraphValue () {
		  m1_vrtx.clear();
		  m2_vrtx.clear();
		  m_vrtx.clear();
		 
		  cur_x=0;
		  cur_y=0;
		  abs_max_y =0;
		  
	  } //
	  
	  public void setHeading (String h) {
		  heading = h;
	  } //
	  
	
	  
	  public void setYAxisColor(Color c){
		  y_color = c;
	  }
	  
	  public void setBackgroundColor(Color bkg)
	  {
			m_Bkg_color = bkg;	
	  }
	  
	  public void setState(int s){
		  state =s;
	  }
	  
	  public void setScale(float s){
		  scale_y = s;
		}
	  
	  public void setHideFlag(boolean f){
		  hide =f;
	  }
	  
	  public void setWindowSize(int w,int h){
		  setPreferredSize(new Dimension(w,h));
		   
		   width = w;
		   height = h;
		   y_offset = h - 40;
	  }
	  
	  public void clearLineFlag(){
		  for(int i=0;i<m_line.length;i++)
			   m_line[i]=-1;
	  }
	  
	  public void setLineFlag(int i,int pos){
		  if(i<m_line.length)
			   m_line[i]=pos-1;
		  
	  }
	  
	  public void setXOffset(int x)
		{
			x_offset=x;		
		}
		public void setYOffset(int y)
		{
			y_offset=y;
		}
		public void setOffset(int x,int y)
		{
			x_offset=x;
			y_offset=y;		
		}
}