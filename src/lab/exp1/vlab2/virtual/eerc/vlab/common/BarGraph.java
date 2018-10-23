package eerc.vlab.common;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
/** Draw on a JPanel rather than on JApplet's Panel. **/
//public class GraphPlotter extends JPanel 
public class BarGraph extends JPanel implements MouseListener, MouseMotionListener 
{
	private Image mImage;
	int 	counter=0;
	int 	state=0;
	int 	width, height;
	
	int 	x_offset=30;
	int 	y_offset=30;
	float 	cur_x=0;
	float 	cur_y=0;
	double 	abs_max_y=0;
	double 	max_y=-999999;
	double 	abs_min_y=999999;
	double 	min_y=999999;
	float 	scale_y=1;
	float  barWidth=60,barGap=30;
	int 	mx, my;  // the mouse coordinates
	boolean fit_xWindow;
	boolean fit_yWindow;
	private HashMap<String, Color> hm=new HashMap();
	private HashMap<String,String> hn=new HashMap();
	String heading="Bar Graph";	
	String x_str="";
	String y_str="";
	String t_interval="";	
	String x_unit="";
	String y_unit="";
	
	Color y_color = Color.BLUE;
	Color m_Bkg_color = new Color(0.96f,0.96f,0.96f);//Color.GRAY;
    ArrayList<Point> m_vrtx = new ArrayList<Point>();
	
	public BarGraph (int w,int h,String str_x,String str_y) {
		   //setBackground(Color.black);
		   setPreferredSize(new Dimension(w,h));
		   
		   width = w;
		   height = h;
		   x_str=str_x;
		   y_str=str_y;
		   
		   y_offset = (int)(height*0.85);
		   counter=0;
		   state =0;
		   scale_y=1;
		   mx=0;
		   my=0;
		   fit_xWindow = true;
		   fit_yWindow = false;
		   
		  addMouseListener( this );
		  addMouseMotionListener( this );
		  
		   
	 } 
	
	public BarGraph (BarGraph graph,int w,int h) {
		   //setBackground(Color.black);
		   //setBorder(BorderFactory.createLineBorder(Color.black,20));
		   //System.out.println("w" + w + " h" + h);
		   width = w;//1032;
		   height = h;
		   mx=0; // mouse
		   my=0; // mouse
		   x_str=graph.getXString();
		   y_str=graph.getYString();
		   x_unit=graph.getXUnit();
		   y_unit=graph.getYUnit();
		   y_color = graph.getYAxisColor();
		   
		   y_offset = (int)(height*0.8);
		   counter=graph.getCounter();
		   state =graph.getState();
		   scale_y=graph.getYScale();
		   fit_xWindow = graph.getFitXWindow();
		   fit_yWindow = graph.getFitYWindow();
		   abs_max_y = graph.getAbsMaximumY();
		   max_y = graph.getMaximumY();
		   			
		//   setGraphValue(graph.getAllGraphValue());
		  //m_Bkg_color = Color.WHITE;
		   addMouseListener( this );
		   addMouseMotionListener( this );
		  
		   
	 } 
		 		 		  
	  /** Display a Graph. **/
	  public void drawGraph () {
	    // First paint background
		//Dimension d = getSize();
	    checkOffscreenImage();
	    //Graphics offG = mImage.getGraphics();
	    //offG.setColor(getBackground());
	    //offG.fillRect(0, 0, width, height);
	    Graphics g1 = getGraphics();
	    if(state==1)
	    	createGraph(mImage.getGraphics()); 
	    else if(state==0)
	    	pauseTimeGraph(mImage.getGraphics()); 
	       
		 
		 //Graphics2D g = (Graphics2D) g1;
		    // Use anti-aliasing to avoid "jaggies" in the lines
	    // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		       // RenderingHints.VALUE_ANTIALIAS_ON);
	     
	     g1.drawImage(mImage, 0, 0, null);
	     
	  } //
	  
	  public void drawPauseGraph () {
		    // First paint background
		  
		   //Dimension d = getSize();
		    checkOffscreenImage();
		    //Graphics offG = mImage.getGraphics();
		    //offG.setColor(getBackground());
		    //offG.fillRect(0, 0, width, height);
		    Graphics g1 = getGraphics();
		    if(state==1)
		    	createGraph(mImage.getGraphics()); 
		    else if(state==0)
		    	pauseTimeGraph(mImage.getGraphics());
		    
		    g1.drawImage(mImage, 0, 0, null);
			    
			
			 //Graphics2D g = (Graphics2D) g1;
			    // Use anti-aliasing to avoid "jaggies" in the lines
		    // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			       // RenderingHints.VALUE_ANTIALIAS_ON);
		     //createGraph(g1); 
		    
		     
			// Graphics g1 = getGraphics();
			 //Graphics2D g = (Graphics2D) g1;
			    // Use anti-aliasing to avoid "jaggies" in the lines
		    // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			       // RenderingHints.VALUE_ANTIALIAS_ON);
			 //pauseTimeGraph(g); 
		     
	} //
	   
	 

	
	private void checkOffscreenImage() {
		    Dimension d = getSize();
		    if (mImage == null || mImage.getWidth(null) != width || mImage.getHeight(null) != height) 
			    mImage = createImage(width, height);
	} 
	 
	public void paint( Graphics g1 ) {
		
		//Dimension d = getSize();
	    checkOffscreenImage();
	    //Graphics offG = mImage.getGraphics();
	    //offG.setColor(getBackground());
	    //offG.fillRect(0, 0, width, height);
	    if(state==1)
	    	createGraph(mImage.getGraphics()); 
	    else if(state==0)
	    	pauseTimeGraph(mImage.getGraphics());
	   
	    g1.drawImage(mImage, 0, 0, null);
	    
//	     //Graphics2D g2d = (Graphics2D)g;
//		Graphics2D g = (Graphics2D) g1;
//	    // Use anti-aliasing to avoid "jaggies" in the lines
//	   // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//	       // RenderingHints.VALUE_ANTIALIAS_ON);
//
//		g.setColor(m_Bkg_color);
//	    //g.draw3DRect(5, 5, width-10, height-10, true);
//	    g.fillRect(0, 0, width-1, height+200);
//	    
//	    if(state==1)
//	    	createGraph(g);
//	    else if(state==0)
//	    	pauseTimeGraph(g);
	    //labelTheAxis(g);
	}
   		     
	public void createGraph(Graphics g)
	{
		
		//Color c=getBackground();
	    //g.setColor(c);
	    g.setColor(m_Bkg_color);
	    //g.draw3DRect(5, 5, width-10, height-10, true);
	    g.fillRect(0, 0, width-1, height+200);
	   
	    // Draw scaling
	    showScale(g);
	    
	    g.setColor(Color.RED);
	    g.drawLine(x_offset, y_offset, width - x_offset, y_offset);
	    g.drawLine(x_offset, y_offset - (int)(height*0.9), x_offset, y_offset);
	    
	    //
	    Font cur_Font = new Font("Arial", Font.BOLD, 10); //Book Antiqua
		g.setFont(cur_Font);
	    g.setColor(y_color);
	    for(int i=0;i<m_vrtx.size();i++)
		   {
			 // if(hm.containsKey(""+m_vrtx.get(i).x))
			 // {
				  g.setColor((hm.get(""+m_vrtx.get(i).x)));
				  
			  //}
			 g.fillRect((int)(x_offset+(m_vrtx.get(i).x)*barGap + (m_vrtx.get(i).x-1)*barWidth), 
					   y_offset-(int)(m_vrtx.get(i).y), (int)barWidth,(int)(m_vrtx.get(i).y) );
			 g.setColor(Color.BLACK);
			 g.drawString(hn.get(""+m_vrtx.get(i).x),(int)(x_offset+(m_vrtx.get(i).x)*barGap + (m_vrtx.get(i).x-1)*barWidth), y_offset+20);
				  
			  
		 }
	  //  int i=counter+1;
	  //  int x=x_offset;
	  /*  for(; x < width && i<m_vrtx.size()-1 ; x++,i++){
	       	//g.drawLine(x,y_offset + (int)(m_vrtx.get(i-1)*scale_y), x,y_offset+(int)(m_vrtx.get(i)*scale_y));
	    	g.drawRect(10+2*(int)barWidth*(int)(m_vrtx.get(i-1).x), (int)(m_vrtx.get(i-1).y), (int)barWidth,height );
	    }
	    // last dot
	    g.setColor(Color.RED);
	   */ //if(i<m_vrtx.size())
	  //  	g.fillOval(x, y_offset+(int)(m_vrtx.get(i-1)*scale_y), 5, 5);

	    
	    if(m_vrtx.size()> width*0.7)
	    	counter++;//m_vrtx.remove(0);	    
	   // g.drawString("Interval on X-axis - 0.02", 10, 10);
	    
	    drawAllText(g);
	    
	}
	
	
	public void pauseTimeGraph(Graphics g)
	{
		
		//Color c=getBackground();
	    //g.setColor(c);
	    g.setColor(m_Bkg_color);
	    //g.draw3DRect(5, 5, width-10, height-10, true);
	    g.fillRect(0, 0, width-1, height);
	   
	    // Draw scaling
	    showScale(g);
	    
	    g.setColor(Color.RED);
	    g.drawLine(x_offset, y_offset, width - x_offset, y_offset);
	    g.drawLine(x_offset, y_offset - (int)(height*0.8), x_offset, y_offset);
	    //g.drawString(">",width-x_offset,y_offset );
	    //g.drawString("^",x_offset,y_offset-(int)(height*0.6) );
	    
	    
	    g.setColor(y_color);
	  //  g.setColor(Color.BLACK);
	    //m_vrtx.size()/150
	    int step = (int)(m_vrtx.size()/(width*0.7));
	    if(step==0) step=1;
	    
	    Font cur_Font = new Font("Arial", Font.BOLD, 10); //Book Antiqua
		g.setFont(cur_Font);
	  //  int x=x_offset;
	    //System.out.println(x_offset+"==="+y_offset);
	     for(int i=0;i<m_vrtx.size();i++)
	   {
		 // if(hm.containsKey(""+m_vrtx.get(i).x))
		 // {
			  g.setColor((hm.get(""+m_vrtx.get(i).x)));
			  
		  //}
		 g.fillRect((int)(x_offset+(m_vrtx.get(i).x)*barGap + (m_vrtx.get(i).x-1)*barWidth), 
				   y_offset-(int)(m_vrtx.get(i).y), (int)barWidth,(int)(m_vrtx.get(i).y) );
		 g.setColor(Color.BLACK);
		 
		 g.drawString(hn.get(""+m_vrtx.get(i).x),(int)(x_offset+(m_vrtx.get(i).x)*barGap + (m_vrtx.get(i).x-1)*barWidth), y_offset+20);
			  
		  
	   }
	    //for(; x < width - x_offset && i<m_vrtx.size()-step ; x++,i+=step){
	    	//g.drawRect(10+2*(int)barWidth*(int)(m_vrtx.get(i-1).x), (int)(m_vrtx.get(i-1).y), (int)barWidth,height );
	    	/*if(fit_yWindow)
	    		g.drawLine(x,y_offset + (int)((m_vrtx.get(i-1)/(abs_max_y))*100), x,y_offset+ (int)((m_vrtx.get(i+step)/(abs_max_y))*100));
	    	else
	    		g.drawLine(x,y_offset + (int)(m_vrtx.get(i-1)*scale_y), x,y_offset+ (int)(m_vrtx.get(i+step)*scale_y));
	    		*/
	       	//System.out.println("abs_max_y" + (m_vrtx.get(i-1)/(abs_max_y*scale_y))*50);
	    
	    // last dot
	   // g.setColor(Color.RED);
	    //if(i<m_vrtx.size()-1)
	    	//g.fillOval(x, y_offset+ m_vrtx.get(i).intValue(), 5, 5);
	    
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
		
		// Current Value
		//g.setColor(Color.BLACK);
		//cur_Font = new Font("Arial", Font.BOLD, 10);
		//g.setFont(cur_Font);
		//g.drawString("Current   " + y_str + "   : " + cur_y  +  " " +  y_unit, 20,height-15);
		
		//g.drawString("Current   " + x_str + "   : " + cur_x  +  " " +  x_unit, 20,height-6);
		
		
	}
	
	private void showScale(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.lightGray);
	    int y_step=20;
	    float[] dash1 = { 2f, 0f, 2f };
	    
	    Stroke strk= g2d.getStroke();
	    
	    g2d.setStroke(new BasicStroke(1, 
	            BasicStroke.CAP_BUTT, 
	            BasicStroke.JOIN_ROUND, 
	            1.0f, 
	            dash1,2f)
	    );
	    
	    for(int y= y_offset-y_step ; y>y_offset - (int)(height*0.8) ;y-=y_step)
	    	g2d.drawLine(x_offset,y, width - x_offset,y);
	    //for(int y= y_offset + y_step; y<y_offset + (int)(height*0.5);y+=y_step)
	    	//g2d.drawLine(x_offset,y, width - x_offset,y);
	
	    for(int x= x_offset + y_step ; x<width - x_offset;x+=y_step)
	    	g2d.drawLine(x,y_offset - (int)(height*0.8), x,y_offset);
	    
	    int i=y_step;
	    for(int y= y_offset-y_step ; y>y_offset - (int)(height*0.8) ;y-=y_step, i+=y_step)
	    	g.drawString(i + "",width - x_offset,y);
	    //i=-y_step;
	    //for(int y= y_offset + y_step; y<y_offset + (int)(height*0.5);y+=y_step, i-=y_step)
	    	//g.drawString(i + "",width - x_offset,y);
	   
	    	
	    g2d.setStroke(strk);
	   
	}
	
	private void showTooltip(Graphics g)
	{
		int step = (int)(m_vrtx.size()/(width*0.75));
	    if(step==0) step=1;
	    int i=1;
	    int x=x_offset;
	    
	    
	/*    for(; x < width - x_offset && i<m_vrtx.size()-step ; x++,i+=step){
	       	if(fit_yWindow && my ==  y_offset + (int)((m_vrtx.get(i)/abs_max_y)*100))
	       	{
	       		 String str ="(";
			     str = str + Float.toString(x);
			     str=str+",";
			     str = str + Float.toString(-m_vrtx.get(i));
			     str=str+")";
			     setToolTipText(str);
	       	}else if ( my ==  y_offset + (int)(m_vrtx.get(i)*scale_y)){
	       		String str ="(";
			     str = str + Float.toString(x);
			     str=str+",";
			     str = str + Float.toString(-m_vrtx.get(i));
			     str=str+")";
			     setToolTipText(str);
	       	}
	    }
	  */   
	}
	
	public void mouseEntered( MouseEvent e ) {
	//	for(int i=0;i<m_vrtx.size();i++)
	
		
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
	
		mx = e.getX();
		my = e.getY();
		Graphics g = getGraphics();
		//if(state==0)
			showTooltip(g);
		e.consume();
	}
	public void mouseDragged( MouseEvent e ) {  // called during motion with buttons down
	}
	

	  /** Display an image. **/
    public void addGraphValue (int x,double y,Color c,String name) {
		
    	 m_vrtx.add(new Point(x,(int) y));
    	 hm.put(""+x,c);
    	 hn.put(""+x,name);
	 /* m_vrtx.add(new Float(-val));
	  if(Math.abs(val) > abs_max_y) abs_max_y = Math.abs(val);
	  if(Math.abs(val) < abs_min_y) abs_min_y = Math.abs(val);
	  
	  if(val > max_y) max_y = val;
	  else if(val < min_y) min_y = val;*/
	} //
	  
	public void clearGraphValue () {
		  
		  m_vrtx.clear();
		  hm.clear();
		  hn.clear();
		  counter =0;
	} //
	  
	public void setHeading (String h) {
		  
		  heading = h;
	} //
	  
	public void setTimeInterval(String intrval){
		  t_interval = intrval;
	}
	  
	public void setCurrentValue(float x,float y){
		  cur_x = x; this.x_unit = x_unit;
		  cur_y = y; this.y_unit = y_unit;
	}
	
	public void setAxisUnit(String x_unit,String y_unit){
		  this.x_unit = x_unit;
		  this.y_unit = y_unit;
	}
	
	public void setYAxisColor(Color c){
	  y_color = c;
	}
	
	public Color getYAxisColor(){
		  return y_color;
	}
	
	public double getAbsMaximumY(){
	  return abs_max_y;
	}
	
	public double getAbsMinimumY(){
	  return abs_min_y;
	}
	
	public double getMaximumY()
	{
		return max_y;	
	}

//	  public float getMinimumY(){
//		  //return abs_min_y;
//	  }
	  
	public void setState(int s){
	  state =s;
	}
	public void setBarWidth(float f)
	{
		barWidth=f;
	}
	public void setBarGap(float f)
	{
		barGap=f;
	}
	public int getState()
	{
		return state;	
	}
  
	public void setScale(float s){
	  scale_y = s;
	}
	public void addToScale(float s){
		  scale_y += s;
	}
	
	public void fitToXwindow(boolean flag)
	{
		fit_xWindow = flag;
	}
	
	public boolean getFitXWindow()
	{
		return fit_xWindow;	
	}
	
	
	public void fitToYwindow(boolean flag)
	{
		fit_yWindow = flag;
	}
	
	public boolean getFitYWindow()
	{
		return fit_yWindow;	
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
	public void setBackgroundColor(Color bkg)
	{
		m_Bkg_color = bkg;	
	}
	
	public String getXString()
	{
		return x_str;	
	}
	
	public String getYString()
	{
		return y_str;	
	}
	public String getXUnit()
	{
		return x_unit;	
	}
	
	public String getYUnit()
	{
		return y_unit;	
	}
			
	public float getYScale()
	{
		return scale_y;	
	}
	
	public int getCounter()
	{
		return counter;	
	}
	
		
	
	public Object[] getAllGraphValue()
	{
		return m_vrtx.toArray();	
	}
	public void setGraphValue(int pos,int val)
	{
		//for(int i=0;i<val.length;i++)
			m_vrtx.get(pos-1).y=val;	
	}
	
	

}