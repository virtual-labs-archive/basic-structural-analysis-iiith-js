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
public class SpectrumGraph extends JPanel 
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
	
	private String heading="Displacment Vs Time Trend";	
	private String x_str="";
	private String y_str="";
		
	private Color y_color = Color.BLUE;
	Color m_Bkg_color = Color.GRAY;//Color.GRAY;
	private int m_line[];
	private ArrayList<Float> m_vrtx = new ArrayList<Float>();
	private ArrayList<Float> max_vrtx = new ArrayList<Float>();
	
	
	public SpectrumGraph (int w,int h,String str_x,String str_y) {
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
		g.setColor(m_Bkg_color);
	    //g.draw3DRect(5, 5, width-10, height-10, true);
	    g.fillRect(0, 0, width-1, height);
		
	    showScale(g);
	    // Draw scaling
	    g.setColor(Color.BLUE);
	    g.drawLine(x_offset, y_offset, width - x_offset, y_offset);
	    g.drawLine(x_offset, y_offset - (int)(height*0.6), x_offset, y_offset );
	    
	   // Font prev_font = g.getFont();
	    Font cur_Font = new Font("Book Antiqua", Font.BOLD, 10);
		g.setFont(cur_Font);
		int step = (int)((width - 2*x_offset)/35);
		
	  // Draw Graph
		g.setColor(Color.GREEN); 
	    for(int i=1; i< 40 && i<m_vrtx.size()/*m_vrtx.size()*/ ; i++){	    	 			
  			g.drawLine(x_offset + (i-1)*step, y_offset- (int)(m_vrtx.get(i-1)*scale_y),x_offset + i*step, y_offset - (int)(m_vrtx.get(i)*scale_y));
  			
	    }
	    
	    g.setColor(Color.yellow);  	
	    for(int i=1; i< 40 && i<m_vrtx.size()/*m_vrtx.size()*/ ; i++){	    			
  			g.drawLine(x_offset + (i-1)*step, y_offset- (int)(max_vrtx.get(i-1)*scale_y),x_offset + i*step, y_offset - (int)(max_vrtx.get(i)*scale_y));
  			
	    }
	    // Draw Line
//	    g.setColor(Color.RED);
//	    for(int i=0;i<m_line.length;i++){
//	    	if(m_line[i]>-1 && m_line[i] < m_vrtx.size() ){
//	    		
//	    		g.drawLine(x_offset + m_line[i]*step, y_offset,x_offset + m_line[i]*step, y_offset - (int)(max_vrtx.get(m_line[i])*scale_y)); 
//	    	}
//	    }
	    
	    g.setColor(Color.RED);
	    for(int i=0;i<m_line.length;i++){
	    	if(m_line[i]>-1 && m_line[i] < m_vrtx.size() ){
	    		g.fillOval(x_offset + m_line[i]*step, y_offset - (int)(max_vrtx.get(m_line[i])*scale_y), 5, 5);
	    	}
	    }

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
		g.setColor(Color.GREEN);
	    for(int i=1; i< 40 && i<m_vrtx.size() ; i++){	
	    	
  			g.drawLine(x_offset + (i-1)*step, y_offset - (int)((m_vrtx.get(i-1)/(abs_max_y))*100),x_offset + i*step, y_offset - (int)((m_vrtx.get(i)/(abs_max_y))*100));
	    	//g.drawLine(x_offset + i*step, y_offset-2,x_offset + i*step, y_offset - m_vrtx.get(i).intValue());
	    	//System.out.println("mac:" + abs_max_y);
  			
	    }
	    g.setColor(Color.YELLOW);
	    for(int i=1; i< 40 && i<m_vrtx.size() ; i++){	    	
  			g.drawLine(x_offset + (i-1)*step, y_offset - (int)((max_vrtx.get(i-1)/(abs_max_y))*100),x_offset + i*step, y_offset - (int)((max_vrtx.get(i)/(abs_max_y))*100));
	    	//g.drawLine(x_offset + i*step, y_offset-2,x_offset + i*step, y_offset - m_vrtx.get(i).intValue());
	    	//System.out.println("mac:" + abs_max_y);
  			
	    }
	    // Draw Line
//	    g.setColor(Color.RED);
//	    for(int i=0;i<m_line.length;i++){
//	    	if(m_line[i]>-1 && m_line[i] < m_vrtx.size() ){
//	    		g.drawLine(x_offset + m_line[i]*step, y_offset,x_offset + m_line[i]*step, y_offset - (int)(max_vrtx.get(m_line[i])/abs_max_y)*100);
//	    	}
//	    }
	    //step-=5;
	    g.setColor(Color.RED);
	    for(int i=0;i<m_line.length;i++){
	    	if(m_line[i]>-1 && m_line[i] < m_vrtx.size() ){
	    		//System.out.println("m_line[i]" + i +"x" + (x_offset +  m_line[i]*step));
	    		g.fillOval(x_offset + m_line[i]*step, y_offset - (int)((max_vrtx.get(m_line[i])/(abs_max_y))*100), 5, 5);
	    	}
	    }
	    
	    
	    
	    	

	    drawAllText(g);
	    
	}
	
		
		
	private void drawAllText(Graphics g){
		g.setColor(Color.BLACK);
		Font prev_font = g.getFont();;
		Font cur_Font = new Font("Arial", Font.BOLD, 10);
		g.setFont(cur_Font);
		int i=0;
		//char[] str = x_str.toCharArray();
		// Axis
		for(int x= (int)(width*0.5) - (int)(x_str.length()*5); i<x_str.length();x+=10,i++)
			g.drawString(x_str.substring(i,i+1), x, y_offset + (int)(height*0.1));
		
		
		if(m_Vdisplay){
			i=0;
			for(int y= y_offset - y_str.length()*5; i<y_str.length();y+=10,i++)
				g.drawString(y_str.substring(i,i+1), 10, y_offset - height -10 + y);
			
			
		}else{
			g.setColor(Color.BLACK);
			g.drawString(y_str , 10, (int)(height*0.5));
			g.setColor(Color.BLUE);
			//g.drawString(" (" + m_y_unit + ")", 10, y_offset - 10 );
			g.setColor(Color.BLACK);
			
		}
		
		
		
		// Heading
		g.setColor(Color.orange);		
		cur_Font = new Font("Arial", Font.BOLD, 12);
		g.setFont(cur_Font);
		g.drawString(heading, (int)(width*0.25),15);
		
		
		g.setColor(Color.GREEN);
		g.drawLine((int)(width*0.55) -10,30,(int)(width*0.55) -5,30);
		g.setColor(Color.YELLOW);
		g.drawLine((int)(width*0.55) -10,40,(int)(width*0.55) -5,40);
		
		g.setColor(Color.BLACK);		
		cur_Font = new Font("Arial", Font.BOLD, 10);
		g.setFont(cur_Font);		
		
		g.drawString(":" + "Max. Abs. Displacement", (int)(width*0.55),30);
		g.drawString(":" + "Current Abs. Displacement", (int)(width*0.55),40);
		
		// Current Value
		//g.drawString("T:"+  String.valueOf(cur_Xval)+ " (K)",20,height-30);
		//g.drawString("P:" + String.valueOf(cur_Yval)+ " (m^3)",20,height-20);
		
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
	    	g.drawString((i*4)/10 + "",x,y_offset+10);
	   	
	    g2d.setStroke(strk);
	   
	}

	  /** Display an image. **/
	 public void addGraphValue (double val) {
		  
		  m_vrtx.add(new Float(val));
		  //addCurrentGraphValue ((int) x,(int) y);
		  if(Math.abs(val) > abs_max_y)
			  abs_max_y = Math.abs(val);
			  //System.out.println("mac:" + abs_max_y);
		  
		 
	  } //
	  /** Display an image. **/
	  public void addGraphMaxValue (double val) {
		  
		  max_vrtx.add(new Float(val));
			  //System.out.println("mac:" + abs_max_y);
		  
		 
	  } //
	  
	  public void addCurrentGraphValue (float x,float y) {		  
		  cur_x=x;
		  cur_y=y;		 
	  } //
	  
	  public void clearGraphValue () {
		  m_vrtx.clear();
		  max_vrtx.clear();
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
