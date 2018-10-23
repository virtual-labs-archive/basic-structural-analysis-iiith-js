package eerc.vlab.common;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
/** Draw on a JPanel rather than on JApplet's Panel. **/
//public class GraphPlotter extends JPanel 
@SuppressWarnings("unused")
public class Hgraph extends JPanel implements MouseListener, MouseMotionListener 
{
	private Image mImage;
	int 	m_counter=0;
	int 	m_state=0;
	int 	m_width, m_height;
	
	int 	m_x_offset=50;
	int 	m_y_offset=180;
	float 	m_cur_x=0;
	float 	m_cur_y=0;
	double 	m_abs_max_x=0;
	double 	m_abs_min_x=999999;
	double 	m_max_x=-999999;	
	double 	m_min_x=999999;
	float 	m_scale_x=1;
	int 	m_mx, m_my;  // the mouse coordinates
	boolean m_fit_xWindow;
	boolean m_fit_yWindow;
	boolean m_hideCurrentValue=false;
	

	String m_heading="Heading";	
	String m_x_str="";
	String m_y_str="";
	String m_t_interval="";	
	String m_x_unit="";
	String m_y_unit="";
	
	Color m_y_color = Color.BLUE;
	Color m_Bkg_color = new Color(0.96f,0.96f,0.96f);//new Color(0.96f,0.96f,0.96f);//Color.GRAY;
    ArrayList<Float> m_vrtx = new ArrayList<Float>();
	
	public Hgraph (int w,int h,String str_x,String str_y) {
		   //setBackground(Color.black);
		   setPreferredSize(new Dimension(w,h));
		   
		   m_width = w;
		   m_height = h;
		   m_x_str=str_x;
		   m_y_str=str_y;
		   
		   m_x_offset = (int)(m_width*0.5);
		   m_y_offset = m_height-40;
		   m_counter=0;
		   m_state =0;
		   m_scale_x=1;
		   m_mx=0;
		   m_my=0;
		   m_fit_xWindow = true;
		   m_fit_yWindow = false;
		   
		  addMouseListener( this );
		  addMouseMotionListener( this );
		   
	  } 
		 		 		  
	  /** Display a Graph. **/
	  public void drawGraph () {
	    // First paint background
		  checkOffscreenImage();
		    //Graphics offG = mImage.getGraphics();
		    //offG.setColor(getBackground());
		    //offG.fillRect(0, 0, width, height);
		    Graphics g1 = getGraphics();
		    if(m_state==0)
		    	pauseTimeGraph(mImage.getGraphics()); 
		    else if(m_state==1)
		    	createGraph(mImage.getGraphics());
		    else if(m_state==2)
		    	fitPointToWindowGraph(mImage.getGraphics()); 
		       
			 
			 //Graphics2D g = (Graphics2D) g1;
			    // Use anti-aliasing to avoid "jaggies" in the lines
		    // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			       // RenderingHints.VALUE_ANTIALIAS_ON);
		     
		     g1.drawImage(mImage, 0, 0, null);
	     
	  } //
	  
	  public void drawPauseGraph () {
		    // First paint background
		  checkOffscreenImage();
		    //Graphics offG = mImage.getGraphics();
		    //offG.setColor(getBackground());
		    //offG.fillRect(0, 0, width, height);
		    Graphics g1 = getGraphics();
		    if(m_state==0)
		    	pauseTimeGraph(mImage.getGraphics()); 
		    else if(m_state==1)
		    	createGraph(mImage.getGraphics());
		    else if(m_state==2)
		    	fitPointToWindowGraph(mImage.getGraphics());
		  
		    g1.drawImage(mImage, 0, 0, null);
		     
		  } //
	   
	 

	 

	 private void checkOffscreenImage() {
		    if (mImage == null) 
			    mImage = createImage(m_width, m_height);
	 } 
	
	public void paint( Graphics g1 ) {
//		Dimension d = getSize();
	    checkOffscreenImage();
	    //Graphics offG = mImage.getGraphics();
	    //offG.setColor(getBackground());
	    //offG.fillRect(0, 0, width, height);
	    if(m_state==0)
	    	pauseTimeGraph(mImage.getGraphics()); 
	    else if(m_state==1)
	    	createGraph(mImage.getGraphics());
	    else if(m_state==2)
	    	fitPointToWindowGraph(mImage.getGraphics());
	   
	    g1.drawImage(mImage, 0, 0, null);
	    //labelTheAxis(g);
	}
   		     
	public void createGraph(Graphics g)
	{
		
		Color c=getBackground();
	    g.setColor(c);
	    g.setColor(m_Bkg_color);
	    //g.draw3DRect(5, 5, width-10, height-10, true);
	    g.fillRect(0, 0, m_width+10, m_height);
	   
	    // Draw scaling
	    showScale(g);
	    g.setColor(Color.RED);
	    g.drawLine(m_x_offset -(int)(m_width*0.5), m_y_offset, m_x_offset +(int)(m_width*0.5), m_y_offset);
	    g.drawLine(m_x_offset, m_y_offset, m_x_offset,  m_y_offset-(int)(m_height*0.7));
	    
	    g.setColor(m_y_color);
	    	
	    int i=m_counter+1;
	    int y=m_y_offset ;
	    for(; y > 20 && i<m_vrtx.size()-1 ; y--,i++){
	       	g.drawLine(m_x_offset +  (int)(m_vrtx.get(i-1)*m_scale_x),y,m_x_offset + (int)(m_vrtx.get(i)*m_scale_x),y );
	    }
	   
	    g.setColor(Color.RED);
	    if(i<m_vrtx.size())
	    	g.fillOval(m_x_offset +  (int)(m_vrtx.get(i-1)*m_scale_x),y, 5, 5);
	    if(m_vrtx.size()> m_height*0.75)
	    	m_counter++;//m_vrtx.remove(0);
	    
	    drawAllText(g);
	}
	
	
	public void pauseTimeGraph(Graphics g)
	{
		
		Color c=getBackground();
	    g.setColor(c);
	    g.setColor(m_Bkg_color);
	    //g.draw3DRect(5, 5, width-10, height-10, true);
	    g.fillRect(0, 0, m_width+10, m_height);
	   
	    showScale(g);
	    // Draw scaling
	    g.setColor(Color.RED);
	    g.drawLine(m_x_offset -(int)(m_width*0.5), m_y_offset, m_x_offset +(int)(m_width*0.5), m_y_offset);
	    g.drawLine(m_x_offset, m_y_offset, m_x_offset,  m_y_offset-(int)(m_height*0.7));
	    
	    g.setColor(m_y_color);
	    
	    	    
	    int step = (int)(m_vrtx.size()/(m_height*0.9));
	    if(step==0) step=1;
	    int i=1;
	    int y=m_y_offset;
	   // System.out.println("abs_max_y" + abs_max_y);
	    for(; y > 20 && i<m_vrtx.size()-step ; y--,i+=step){
	    	if(m_fit_xWindow)
	    		g.drawLine(m_x_offset + (int)((m_vrtx.get(i-1)/(m_abs_max_x))*100),y,m_x_offset+ (int)((m_vrtx.get(i+step)/(m_abs_max_x))*100),y);
	    	else
	    		g.drawLine(m_x_offset +  (int)(m_vrtx.get(i-1)*m_scale_x),y,m_x_offset + (int)(m_vrtx.get(i+step)*m_scale_x),y );
	    	//else
	    		//g.drawLine(x,y_offset + m_vrtx.get(i-1).intValue(), x,y_offset+ m_vrtx.get(i+step).intValue());
	    		
	       	//System.out.println("abs_max_y" + (m_vrtx.get(i-1)/(abs_max_y*scale_y))*50);
	    }
	    
	    
	    
	    drawAllText(g);
	    
	}
	
	public void fitPointToWindowGraph(Graphics g)
	{
		
		Color c=getBackground();
	    g.setColor(c);
	    g.setColor(m_Bkg_color);
	    //g.draw3DRect(5, 5, width-10, height-10, true);
	    g.fillRect(0, 0, m_width+10, m_height);
	   
	    //showScale(g);
	    // Draw scaling
	    g.setColor(Color.RED);
	    g.drawLine(m_x_offset -(int)(m_width*0.5), m_y_offset, m_x_offset +(int)(m_width*0.5), m_y_offset);
	    g.drawLine(m_x_offset, m_y_offset, m_x_offset,  m_y_offset-(int)(m_height*0.7));
	    
	    g.setColor(m_y_color);
	    
	    	    
	    int step = (int)((m_height*0.6)/m_vrtx.size());
	    //System.out.println("step" + m_height + step);
	    int y=m_y_offset;
	   // System.out.println("abs_max_y" + abs_max_y);
	    for(int i=1; y > 20 && i<m_vrtx.size() ; y-=step,i++){
	    		g.drawLine(m_x_offset +  (int)(m_vrtx.get(i-1)*m_scale_x),y,m_x_offset +(int)(m_vrtx.get(i)*m_scale_x),y-step);
	    		if(i!=1)
	    			g.fillOval(m_x_offset +  (int)(m_vrtx.get(i-1)*m_scale_x),y,5,5);  // This is added By Pradeep
	    		g.fillOval(m_x_offset +(int)(m_vrtx.get(i)*m_scale_x),y-step,5,5);  // This is added By Pradeep

	       	//System.out.println("abs_max_y" + (m_vrtx.get(i-1)/(abs_max_y*scale_y))*50);
	    }
	    
	    
	    
	    drawAllText(g);
	    
	}
	
			
	private void drawAllText(Graphics g){
		g.setColor(Color.BLACK);
		Font prev_font = g.getFont();;
		Font cur_Font = new Font("Arial", Font.BOLD, 10); //Book Antiqua
		g.setFont(cur_Font);
		int i=0;
		//char[] str = x_str.toCharArray();
		// Axis
		int x= m_x_offset - (int)(m_x_str.length()*3);
		for( ;i<m_x_str.length();x+=8,i++)
			g.drawString(m_x_str.substring(i,i+1), x, m_y_offset + 15);
		
		if(m_x_unit.trim().length()>0){
			g.setColor(Color.BLUE);
			g.drawString("(" + m_x_unit +")" , x+5,m_y_offset + 15);
			g.setColor(Color.BLACK);
		}
		
		
		
		i=0;
		int y= (int)(m_height*0.5) - m_y_str.length()*5;
		for(; i<m_y_str.length();y+=10,i++)
			g.drawString(m_y_str.substring(i,i+1), 5,  y);
		
		if(m_y_unit.trim().length()>0){
			g.setColor(Color.BLUE);
			g.drawString("(" + m_y_unit + ")", 5, y +5 );
			g.setColor(Color.BLACK);
		}
		
		// Heading
		g.setColor(new Color(100,100,100));		
		cur_Font = new Font("Arial", Font.BOLD, 14);
		g.setFont(cur_Font);
		g.drawString(m_heading, (int)(m_width*0.5) - m_heading.length()*5,15);
		
		// Current Value
		//g.setColor(Color.BLUE);
		if(!m_hideCurrentValue){
			cur_Font = new Font("Arial", Font.ITALIC, 9);
			g.setFont(cur_Font);
			g.drawString(m_x_str + ": " + m_cur_x  +  " " + m_x_unit , 10,m_height-15);
			;
			g.drawString( m_y_str + ": " + m_cur_y  +  " " +  m_y_unit, 10,m_height-6);
			
		}
		
		
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
	    
	    for(int y= m_y_offset-y_step ; y> m_y_offset - (int)(m_height*0.7) ;y-=y_step)
	    	g2d.drawLine(0,y , (int)m_width, y);
//	    for(int y= y_offset + y_step; y<y_offset + (int)(height*0.5);y+=y_step)
//	    	g2d.drawLine(x_offset,y, width - x_offset,y);
////	
	    for(int x= m_x_offset ; x< m_width ;x+=y_step)
	    	g2d.drawLine(x,m_y_offset, x,m_y_offset - (int)(m_height*0.7) );
	    
	    for(int x= m_x_offset ; x> 0 ;x-=y_step)
	    	g2d.drawLine(x,m_y_offset, x,m_y_offset - (int)(m_height*0.7) );
	    
	    Font cur_Font = new Font("Arial", Font.BOLD, 8);
		g.setFont(cur_Font);
		
	    int i=y_step;
	    //g.drawLine(x_offset -(int)(width*0.4), y_offset, x_offset +(int)(width*0.4), y_offset);
	    for(int x= m_x_offset+y_step ; x<m_x_offset + (int)(m_width*0.5) ;x+=y_step, i+=y_step)
	    	g.drawString(i + "",x,m_y_offset - (int)(m_height*0.70));
	    i=-y_step;
	    for(int x= m_x_offset-y_step ; x>m_x_offset - (int)(m_width*0.5) ;x-=y_step, i-=y_step)
	    	g.drawString(i + "",x,m_y_offset - (int)(m_height*0.70));
	   
	    	
	    g2d.setStroke(strk);
	   
	}
	
	private void showTooltip(Graphics g)
	{
		
	   
	    int step = (int)(m_vrtx.size()/(m_height*0.6));
	    if(step==0) step=1;
	    int i=0;	   
	    if(m_fit_xWindow){	        
	    	for(; i<m_vrtx.size()-step ; i+=step)
	    		if(m_mx ==  m_x_offset + (int)((m_vrtx.get(i)/(m_abs_max_x))*100))
		       	{
		       		String str ="(";   str = str + Float.toString(m_vrtx.get(i));	     str=str+",";
		       		str = 	str + Float.toString(m_y_offset - m_my);	     str=str+")";
		       		setToolTipText(str);	       		 
				     return;
		       	}
	    	
	    }else{
	    	for(; i<m_vrtx.size()-step ; i+=step)
	    		if(m_mx ==  m_x_offset + (int)(m_vrtx.get(i)*m_scale_x))
		       	{
		       		String str ="(";   str = str + Float.toString(m_vrtx.get(i));	     str=str+",";
		       		str = 	str + Float.toString(m_y_offset - m_my);	     str=str+")";
		       		setToolTipText(str);	       		 
				     return;
		       	}
	    }
	    
	    setToolTipText("");
	     
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
	
		m_mx = e.getX();
		m_my = e.getY();
		Graphics g = getGraphics();
		if(m_state==0)
			showTooltip(g);
		e.consume();
	}
	public void mouseDragged( MouseEvent e ) {  // called during motion with buttons down
	}
	

	  /** Display an image. **/
    public void addGraphValue (double val) {
		  
	  m_vrtx.add(new Float(val));
	  if(Math.abs(val) > m_abs_max_x) m_abs_max_x = Math.abs(val);
	  if(Math.abs(val) < m_abs_min_x) m_abs_min_x = Math.abs(val);
	  
	  if(val > m_max_x) m_max_x = val;
	  else if(val < m_min_x) m_min_x = val;
	} //
	  
	public void clearGraphValue () {
		  
		  m_vrtx.clear();
		  m_counter =0;
		  m_cur_x =0;
		  m_cur_y =0;
	} //
	  
	public void setHeading (String h) {
		  
		m_heading = h;
	} //
	  
	public void setTimeInterval(String intrval){
		m_t_interval = intrval;
	}
	  
	public void setCurrentValue(float x,float y){
		m_cur_x = x; 
		m_cur_y = y; 
	}
	public void setAxisUnit(String x_unit,String y_unit){
		  this.m_x_unit = x_unit;
		  this.m_y_unit = y_unit;
	}
	
	public void setAxisString(String x_unit,String y_str){
		  this.m_x_str = y_str;
		  this.m_y_str = y_str;
	}
	
	public void setYAxisColor(Color c){
		m_y_color = c;
	}
  
	public void setBackgroundColor(Color bkg)
	{
		m_Bkg_color = bkg;	
	}
	public double getAbsMaximumX(){
	  return m_abs_max_x;
	}
	public double getAbsMinimumX(){
	  return m_abs_min_x;
	}
	  
//	  public float getMaximumY(){
//		  //return abs_max_y;
//	  }
//	  public float getMinimumY(){
//		  //return abs_min_y;
//	  }
	  
	public void setState(int s){
		m_state =s;
	}
  
	public void setXScale(float s){
		m_scale_x = s;
	}
	
	public void fitToXwindow(boolean flag)
	{
		m_fit_xWindow = flag;
	}
	
	public void fitToYwindow(boolean flag)
	{
		m_fit_yWindow = flag;
	}
	
	public void setXOffset(int x)
	{
		m_x_offset=x;		
	}
	public void setYOffset(int y)
	{
		m_y_offset=y;
	}
	public void setOffset(int x,int y)
	{
		m_x_offset=x;
		m_y_offset=y;		
	}
	
	public void setWidthHeight(int w,int h)
	{
		m_width = w;
		m_height = h;
		setOffset((int)(m_width*0.5),m_height-40);
	}
	
	public void hideCurrentValue(boolean flag)
	{
		m_hideCurrentValue = flag;
	}
}

