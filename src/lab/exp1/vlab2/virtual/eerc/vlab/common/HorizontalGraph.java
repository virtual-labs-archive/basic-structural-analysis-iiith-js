package eerc.vlab.common;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
/** Draw on a JPanel rather than on JApplet's Panel. **/
//public class GraphPlotter extends JPanel 
public class HorizontalGraph extends JPanel implements MouseListener, MouseMotionListener 
{
	private Image mImage;
	int 	m_counter=0;
	int 	m_state=0;
	int 	m_width, m_height;
	
	int 	m_x_offset=30;
	int 	m_y_offset=100;
	float 	m_cur_x=0;
	float 	m_cur_y=0;
	double 	m_abs_max_y=0;
	double 	m_max_y=-999999;
	double 	m_abs_min_y=999999;
	double 	m_min_y=999999;
	float 	m_scale_y=1;
	int 	mx, my;  // the mouse coordinates
	boolean m_fit_xWindow;
	boolean m_fit_yWindow;
	boolean m_Vdisplay;
	

	String m_heading="Horizontal Graph";	
	String m_x_str="";
	String m_y_str="";
	String t_interval="";	
	String m_x_unit="";
	String m_y_unit="";
	
	Color m_y_color = Color.BLUE;
	Color m_Bkg_color = new Color(0.96f,0.96f,0.96f);//Color.GRAY;
    ArrayList<Float> m_vrtx = new ArrayList<Float>();
    ArrayList<Float> p_secvetx=new ArrayList<Float>(); //Pradeep
    
	public HorizontalGraph (int w,int h,String str_x,String str_y) {
		   //setBackground(Color.black);
		   setPreferredSize(new Dimension(w,h));
		   
		   m_width = w;
		   m_height = h;
		   m_x_str=str_x;
		   m_y_str=str_y;
		   
		   m_y_offset = (int)(m_height*0.5);
		   m_x_offset = 30;
		   m_counter=0;
		   m_state =0;
		   m_scale_y=1;
		   mx=0;
		   my=0;
		   m_fit_xWindow = true;
		   m_fit_yWindow = false;
		   m_Vdisplay = false;
		   
		  addMouseListener( this );
		  addMouseMotionListener( this );
		  
		   
	 } 
	
	public HorizontalGraph (HorizontalGraph graph,int w,int h) {
		   //setBackground(Color.black);
		   //setBorder(BorderFactory.createLineBorder(Color.black,20));
		   //System.out.println("w" + w + " h" + h);
		   m_width = w;//1032;
		   m_height = h;
		   mx=0; // mouse
		   my=0; // mouse
		   m_x_str=graph.getXString();
		   m_y_str=graph.getYString();
		   m_x_unit=graph.getXUnit();
		   m_y_unit=graph.getYUnit();
		   m_y_color = graph.getYAxisColor();
		   
		   m_y_offset = (int)(m_height*0.5);
		   m_counter=graph.getCounter();
		   m_state =graph.getState();
		   m_scale_y=graph.getYScale();
		   m_fit_xWindow = graph.getFitXWindow();
		   m_fit_yWindow = graph.getFitYWindow();
		   m_abs_max_y = graph.getAbsMaximumY();
		   m_max_y = graph.getMaximumY();
		   
		   m_heading = graph.getHeading();
		   setGraphValue(graph.getAllGraphValue());
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
	    //offG.fillRect(0, 0, m_width, m_height);
	    Graphics g1 = getGraphics();
	    if(m_state==1)
	    	createGraph(mImage.getGraphics()); 
	    else if(m_state==0)
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
		    //offG.fillRect(0, 0, m_width, m_height);
		    Graphics g1 = getGraphics();
		    if(m_state==1)
		    	createGraph(mImage.getGraphics()); 
		    else if(m_state==0)
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
		    if (mImage == null || mImage.getWidth(null) != m_width || mImage.getHeight(null) != m_height) 
			    mImage = createImage(m_width, m_height+100);
	} 
	 
	public void paint( Graphics g1 ) {
		
		//Dimension d = getSize();
	    checkOffscreenImage();
	    //Graphics offG = mImage.getGraphics();
	    //offG.setColor(getBackground());
	    //offG.fillRect(0, 0, m_width, m_height);
	    if(m_state==1)
	    	createGraph(mImage.getGraphics()); 
	    else if(m_state==0)
	    	pauseTimeGraph(mImage.getGraphics());
	   
	    g1.drawImage(mImage, 0, 0, null);
	    
//	     //Graphics2D g2d = (Graphics2D)g;
//		Graphics2D g = (Graphics2D) g1;
//	    // Use anti-aliasing to avoid "jaggies" in the lines
//	   // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//	       // RenderingHints.VALUE_ANTIALIAS_ON);
//
//		g.setColor(m_Bkg_color);
//	    //g.draw3DRect(5, 5, m_width-10, m_height-10, true);
//	    g.fillRect(0, 0, m_width-1, m_height+200);
//	    
//	    if(m_state==1)
//	    	createGraph(g);
//	    else if(m_state==0)
//	    	pauseTimeGraph(g);
	    //labelTheAxis(g);
	}
   		     
	public void createGraph(Graphics g)
	{
		
		//Color c=getBackground();
	    //g.setColor(c);
	    g.setColor(m_Bkg_color);
	    //g.draw3DRect(5, 5, m_width-10, m_height-10, true);
	    g.fillRect(0, 0, m_width-1, m_height+200);
	   
	    // Draw scaling
	    showScale(g);
	    
	    g.setColor(Color.RED);
	    g.drawLine(m_x_offset, m_y_offset, m_width - m_x_offset, m_y_offset);
	    g.drawLine(m_x_offset, m_y_offset - (int)(m_height*0.5), m_x_offset, m_y_offset + (int)(m_height*0.5));
	    
	    //
	    
	    g.setColor(m_y_color);
	    int i=m_counter+1;
	    int x=m_x_offset;
	    for(; x < m_width && i<m_vrtx.size()-1 ; x++,i++){
	       	g.drawLine(x,m_y_offset + (int)(m_vrtx.get(i-1)*m_scale_y), x,m_y_offset+(int)(m_vrtx.get(i)*m_scale_y));
	    //	g.drawLine(x,m_y_offset + (int)(p_secvetx.get(i-1)*m_scale_y), x,m_y_offset+(int)(p_secvetx.get(i)*m_scale_y));
	    }
	    
	    
//	    for(; x < m_width && i<p_secvetx.size()-1 ; x++,i++){       // Again this is added
//	       	g.drawLine(x,m_y_offset + (int)(p_secvetx.get(i-1)*m_scale_y), x,m_y_offset+(int)(p_secvetx.get(i)*m_scale_y));
//	    }
	    
	    
	  
	    g.setColor(Color.RED);
	    if(i<m_vrtx.size())
	    	g.fillOval(x, m_y_offset+(int)(m_vrtx.get(i-1)*m_scale_y), 5, 5);

	    
	    //m_vrtx.remove(0);	    
	   // g.drawString("Interval on X-axis - 0.02", 10, 10);
	    
	    drawAllText(g);
	    
	    
	    
	}
	
	
	public void pauseTimeGraph(Graphics g)
	{
		
		//Color c=getBackground();
	    //g.setColor(c);
	    g.setColor(m_Bkg_color);
	    //g.draw3DRect(5, 5, m_width-10, m_height-10, true);
	    g.fillRect(0, 0, m_width-1, m_height);
	   
	    // Draw scaling
	    showScale(g);
	    
	    g.setColor(Color.RED);
	    g.drawLine(m_x_offset, m_y_offset, m_width - m_x_offset, m_y_offset);
	    g.drawLine(m_x_offset, m_y_offset - (int)(m_height*0.5), m_x_offset, m_y_offset + (int)(m_height*0.5));
	    
	    g.setColor(m_y_color);
	    //m_vrtx.size()/150
	    int step = (int)(m_vrtx.size()/(m_width*0.7));
	    if(step==0) step=1;
	    int i=0;
	    int x=m_x_offset;
	   // System.out.println("m_abs_max_y" + m_abs_max_y);
	    for(; x < m_width - m_x_offset && i<m_vrtx.size()-step ; x++,i+=step){
	    	if(m_fit_yWindow)
	    		g.drawLine(x,m_y_offset + (int)((m_vrtx.get(i)/(m_abs_max_y))*100), x,m_y_offset+ (int)((m_vrtx.get(i+step)/(m_abs_max_y))*100));
	    	else
	    		g.drawLine(x,m_y_offset + (int)(m_vrtx.get(i)*m_scale_y), x,m_y_offset+ (int)(m_vrtx.get(i+step)*m_scale_y));
	       	//System.out.println("m_abs_max_y" + (m_vrtx.get(i-1)/(m_abs_max_y*m_scale_y))*50);
	    }
	    // last dot
	    // g.setColor(Color.RED);
	    // if(i<m_vrtx.size()-1)
	    // g.fillOval(x, m_y_offset+ m_vrtx.get(i).intValue(), 5, 5);
	    
	    drawAllText(g);
	    
	}
	
		
	private void drawAllText(Graphics g){
		g.setColor(Color.BLACK);
		Font prev_font = g.getFont();;
		Font cur_Font = new Font("Arial", Font.BOLD, 10); //Book Antiqua
		g.setFont(cur_Font);
		int i=0;
		//char[] str = m_x_str.toCharArray();
		// Axis
		int x= (int)(m_width*0.85) ;
		if(m_x_unit.trim().length()>0){
			g.setColor(Color.BLUE);
			g.drawString("(" + m_x_unit +")" , x,m_y_offset +10 );
			g.setColor(Color.BLACK);
		}
		
		
		for(; i<m_x_str.length();x+=10,i++)
			g.drawString(m_x_str.substring(i,i+1), x, m_y_offset );
		
		if(m_Vdisplay){
			i=0;
			int y =m_y_offset - m_y_str.length()*5;
			for(; i<m_y_str.length();y+=10,i++)
				g.drawString(m_y_str.substring(i,i+1), 10, m_y_offset - (int)(m_height*0.5) + y);
			if(m_y_unit.trim().length()>0){
				g.setColor(Color.BLUE);
				g.drawString("(" + m_y_unit + ")", 10, y +10 );
				g.setColor(Color.BLACK);			
			}
			
		}else{
			g.setColor(Color.BLACK);
			g.drawString(m_y_str , 10, m_y_offset -30);
			g.setColor(Color.BLUE);
			g.drawString(" (" + m_y_unit + ")", 10, m_y_offset - 10 );
			g.setColor(Color.BLACK);
			
		}
		
		
		
		// m_heading
		g.setColor(new Color(100,100,100));//Color.black);//new Color(0.80f,0.36f,0.36f));		
		cur_Font = new Font("Arial", Font.BOLD, 14);
		g.setFont(cur_Font);
		g.drawString(m_heading, (int)(m_width*0.25),15);
		
		// Current Value
		g.setColor(Color.BLACK);
		cur_Font = new Font("Arial", Font.BOLD, 10);
		g.setFont(cur_Font);
		g.drawString( m_y_str + " : " + m_cur_y  +  " " +  m_y_unit, 35,m_height-20);
		
		g.drawString( m_x_str + " : " + m_cur_x  +  " " +  m_x_unit, 35,m_height-12);
		
		
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
	    for(int y= m_y_offset-y_step ; y>m_y_offset - (int)(m_height*0.5) ;y-=y_step)
	    	g2d.drawLine(m_x_offset,y, m_width - m_x_offset,y);
	    for(int y= m_y_offset + y_step; y<m_y_offset + (int)(m_height*0.5);y+=y_step)
	    	g2d.drawLine(m_x_offset,y, m_width - m_x_offset,y);
	
	    for(int x= m_x_offset + y_step ; x<m_width - m_x_offset;x+=y_step)
	    	g2d.drawLine(x,m_y_offset - (int)(m_height*0.5), x,m_y_offset + (int)(m_height*0.5));
	    
	    Font cur_Font = new Font("Arial", Font.BOLD, 8);
		g.setFont(cur_Font);
		
	    int i=y_step;
	    for(int y= m_y_offset-y_step ; y>m_y_offset - (int)(m_height*0.5) ;y-=y_step, i+=y_step)
	    	g.drawString(i + "",m_width - m_x_offset,y);
	    i=-y_step;
	    for(int y= m_y_offset + y_step; y<m_y_offset + (int)(m_height*0.5);y+=y_step, i-=y_step)
	    	g.drawString(i + "",m_width - m_x_offset,y);
	   
	    	
	    g2d.setStroke(strk);
	   
	}
	
	private void showTooltip(Graphics g)
	{
		
		int step = (int)(m_vrtx.size()/(m_width*0.7));
	    if(step==0) step=1;
	    int i=0;
			    
	    for(; i<m_vrtx.size()-step ; i+=step){
	       	if(m_fit_yWindow && my ==  m_y_offset + (int)((m_vrtx.get(i)/(m_abs_max_y))*100))
	       	{
	       		 String str ="(";   str = str + Float.toString(mx);	     str=str+",";
			     str = str + Float.toString(-m_vrtx.get(i));		     str=str+")";
			     setToolTipText(str);
			     return;
	       	}else if ( my ==  m_y_offset + (int)(m_vrtx.get(i)*m_scale_y)){
	       		String str ="(";     str = str + Float.toString(mx);     str=str+",";
	       		str = str + Float.toString(-m_vrtx.get(i));			    str=str+")";
			    setToolTipText(str);
			    return;
	       	}
	    }
	    setToolTipText("");
	     
	}
	
	public void mouseMoved( MouseEvent e ) {  // called during motion when no buttons are down
		
		mx = e.getX();
		my = e.getY();
		Graphics g = getGraphics();
		if(m_state==0)
			showTooltip(g);
		e.consume();
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
	
	public void mouseDragged( MouseEvent e ) {  // called during motion with buttons down
	}
	

	  /** Display an image. **/
    public void addGraphValue (double val) {
		  
	  m_vrtx.add(new Float(-val));
	  if(Math.abs(val) > m_abs_max_y) m_abs_max_y = Math.abs(val);
	  if(Math.abs(val) < m_abs_min_y) m_abs_min_y = Math.abs(val);
	  
	  if(val > m_max_y) m_max_y = val;
	  else if(val < m_min_y) m_min_y = val;
	  
	  if(m_vrtx.size()> m_width*0.7)
	    	m_counter++;
	  
	} //
    public void addGraphValue (double val,double val2) { 
    	// added by pradeep later for further development of adding one more graph on the same graph.
    	// this whole add graph value itself is useless.
		  
  	  m_vrtx.add(new Float(-val));
  	  p_secvetx.add(new Float(-val));
  	  if(Math.abs(val) > m_abs_max_y) m_abs_max_y = Math.abs(val);
  	  if(Math.abs(val) < m_abs_min_y) m_abs_min_y = Math.abs(val);
  	  
  	  if(val > m_max_y) m_max_y = val;
  	  else if(val < m_min_y) m_min_y = val;
  	  
  	  if(m_vrtx.size()> m_width*0.7)
  	    	m_counter++;
  	  
  	} 
	  
	public void clearGraphValue () {
		  
		  m_vrtx.clear();
		  m_counter =0;
		  m_cur_x =0;
		  m_cur_y =0;
	} 
	  
	public void setHeading (String h) {
		  
		  m_heading = h;
	} 
	  
	public String getHeading(){
		return m_heading;
	}
	public void setTimeInterval(String intrval){
		  t_interval = intrval;
	}
	  
	public void setCurrentValue(float x,float y){
		  m_cur_x = x; 
		  m_cur_y = y; 
	}
	
	public void setAxisUnit(String x_unit,String y_unit){
		  m_x_unit = x_unit;
		  m_y_unit = y_unit;
	}
	
	public void setYAxisColor(Color c){
	  m_y_color = c;
	}
	
	public Color getYAxisColor(){
		  return m_y_color;
	}
	
	public double getAbsMaximumY(){
	  return m_abs_max_y;
	}
	
	public double getAbsMinimumY(){
	  return m_abs_min_y;
	}
	
	public double getMaximumY()
	{
		return m_max_y;	
	}

//	  public float getMinimumY(){
//		  //return m_abs_min_y;
//	  }
	  
	public void setState(int s){
		m_state =s;
	}
	
	public int getState()
	{
		return m_state;	
	}
  
	public void setYScale(float s){
	  m_scale_y = s;
	}
	public void addToScale(float s){
		  m_scale_y += s;
	}
	
	public void fitToXwindow(boolean flag)
	{
		m_fit_xWindow = flag;
	}
	
	public boolean getFitXWindow()
	{
		return m_fit_xWindow;	
	}
	
	
	public void fitToYwindow(boolean flag)
	{
		m_fit_yWindow = flag;
	}
	
	public boolean getFitYWindow()
	{
		return m_fit_yWindow;	
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
	public void setBackgroundColor(Color bkg)
	{
		m_Bkg_color = bkg;	
	}
	
	public String getXString()
	{
		return m_x_str;	
	}
	
	public String getYString()
	{
		return m_y_str;	
	}
	public String getXUnit()
	{
		return m_x_unit;	
	}
	
	public String getYUnit()
	{
		return m_y_unit;	
	}
			
	public float getYScale()
	{
		return m_scale_y;	
	}
	
	public int getCounter()
	{
		return m_counter;	
	}
	
		
	
	public Object[] getAllGraphValue()
	{
		return m_vrtx.toArray();	
	}
	
	public void setGraphValue(Object[] val)
	{
		for(int i=0;i<val.length;i++)
			m_vrtx.add((Float) val[i]);	
	}
	
	public void setVerticalLabelDisplay(boolean flag){
		m_Vdisplay = flag;
	}
	

}
