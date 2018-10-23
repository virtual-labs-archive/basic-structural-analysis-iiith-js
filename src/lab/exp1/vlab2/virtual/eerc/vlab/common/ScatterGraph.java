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
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
/** Draw on a JPanel rather than on JApplet's Panel. **/
//public class GraphPlotter extends JPanel 
public class ScatterGraph extends JPanel 
{
	private Image mImage;
	int state=0;
	int noGraph,m_width, m_height;
	int MIN=0;
	int x_offset=30;
	int y_offset=110;
	
	int cur_Xval=0;
	int cur_Yval=0;
	
	boolean  hide = false;
	
	String heading="Heading";	
	String x_str="";
	String y_str="";
	String t_interval="";	
	String x_unit="";
	String y_unit="";
	
	private HashMap hashColor=new HashMap();
	private HashMap hashCapt=new HashMap();
	private HashMap hashList=new HashMap();
	Color y_color = Color.BLUE;
	Color m_Bkg_color = new Color(0.96f,0.96f,0.96f);
	ArrayList<Point> line=new ArrayList<Point>();
  //  ArrayList<Point> m_vrtx = new ArrayList<Point>();
	
	public ScatterGraph (int n,int w,int h,String str_x,String str_y) {
		   //setBackground(Color.black);
		   setPreferredSize(new Dimension(w,h));
		   
		   noGraph=n;
		   m_width = w;
		   m_height = h;
		   x_str=str_x;
		   y_str=str_y;
		   y_offset = h - 40-n*12;
		   
		   state =0;
		   for(int i=0;i<noGraph;i++)
		   {
			   ArrayList<Point> m_vrtx = new ArrayList<Point>();
			   hashList.put(i+1,m_vrtx);
		   }
	  } 
		 		 		  
	  /** Display a Graph. **/
	  public void drawGraph () {
	    // First paint background
		  checkOffscreenImage();
		  Graphics g1 = getGraphics();
		  pauseTimeGraph(mImage.getGraphics()); 
		  
		  g1.drawImage(mImage, 0, 0, null);
	     
	  } //
	  
	  public void drawPauseGraph () {
		    // First paint background
		  checkOffscreenImage();
		  Graphics g1 = getGraphics();
		  pauseTimeGraph(mImage.getGraphics()); 
		  
		  g1.drawImage(mImage, 0, 0, null);
		     
	 } //
	   
	 
	 private void checkOffscreenImage() {
		    if (mImage == null) 
			    mImage = createImage(m_width, m_height);
	} 
	
	
	public void paint( Graphics g1 ) {
	     //Graphics2D g2d = (Graphics2D)g;
		 checkOffscreenImage();
		   
		 pauseTimeGraph(mImage.getGraphics()); 

		 g1.drawImage(mImage, 0, 0, null);
		    
		
	    //labelTheAxis(g);
	}
   		     
	
	
	
	public void pauseTimeGraph(Graphics g)
	{
		
		Color c=getBackground();
	    g.setColor(c);
	    g.setColor(m_Bkg_color);
	    //g.draw3DRect(5, 5, width-10, height-10, true);
	    g.fillRect(0, 0, m_width+10, m_height);
	    
		showScale(g);
	    // Draw x and y axis
	    g.setColor(Color.RED);
	    g.drawLine(x_offset, y_offset, m_width - x_offset-20, y_offset);
	    g.drawLine(x_offset,  20, x_offset, y_offset );

	    
	    Font prev_font = g.getFont();
	    Font cur_Font = new Font("Book Antiqua", Font.BOLD, 10);
		g.setFont(cur_Font);
		for(int j=0;j< noGraph;j++)
		{
			ArrayList<Point> m_vrtx=(ArrayList<Point>)hashList.get(j+1);
			for(int i=0; i<m_vrtx.size() ; i++)
			{	
				g.setColor((Color)hashColor.get(j+1));
  				g.drawString("x",x_offset + m_vrtx.get(i).x, y_offset - m_vrtx.get(i).y);
  			}
		}
		for(int j=0;j<line.size();j++)
		{
			g.drawLine(line.get(j).x, y_offset,line.get(j).x ,20);
		}
	    drawAllText(g);
	    drawKeys(g);
	    
	}
	
		
	private void drawAllText(Graphics g){
		g.setColor(Color.BLACK);
		Font prev_font = g.getFont();;
		Font cur_Font = new Font("Arial", Font.BOLD, 10); //Book Antiqua
		g.setFont(cur_Font);
		int i=0;
		
		// X Axis
		int x;
		for(x=(int)(m_width*0.8);i<x_str.length();x+=7,i++)
			g.drawString(x_str.substring(i,i+1), x, y_offset );
		
		if(x_unit.trim().length()>0){
			g.setColor(Color.BLUE);
			g.drawString("(" + x_unit +")" , x+5,y_offset + 15);
			g.setColor(Color.BLACK);
		}
		
		
		
		i=0;
		//int y =(int)(m_width*0.5) - y_str.length()*2 -;
		int y=y_offset/2+10-y_str.length()*4;;
		for(; i<y_str.length();y+=10,i++)
			g.drawString(y_str.substring(i,i+1), 10,y);
		
		if(y_unit.trim().length()>0){
			g.setColor(Color.BLUE);
			g.drawString("(" + y_unit + ")", 5, y +5 );
			g.setColor(Color.BLACK);
		}
		
		// Heading
		g.setColor(Color.black);
		cur_Font = new Font("Arial", Font.BOLD, 14);
		g.setFont(cur_Font);
		g.drawString(heading, (int)(m_width*0.25),15);
		
		g.setColor(Color.BLACK);
		cur_Font = new Font("Arial", Font.BOLD, 10);
		g.setFont(cur_Font);
		
		int max=0,disp=0,MIN=1000000;
		//g.drawString(str, x, y)
		for(i=0;i< noGraph;i++)
		{
			ArrayList<Point> m_vrtx=(ArrayList<Point>)hashList.get(i+1);
			
			disp= x_offset+7*hashCapt.get(i+1).toString().length();
			if(m_vrtx.size()!=0)
			{
				String value=String.valueOf(": "+ (int)m_vrtx.get(m_vrtx.size()-1).x);
				
				g.drawString(hashCapt.get(i+1).toString() + " : " + value + " " +y_unit,x_offset,y_offset + 10*(i+1));
				
				if(max< m_vrtx.get(m_vrtx.size()-1).x)
					max=m_vrtx.get(m_vrtx.size()-1).x;
			
			}else
				g.drawString(hashCapt.get(i+1).toString() + " : " + y_unit,x_offset,y_offset + 10*(i+1));
		}
		
		g.drawString(x_str+ ": " + max + " " + x_unit,x_offset, y_offset+ 10*(noGraph+1));
//		disp=x_offset + 7*y_str.length();
//		g.drawString(String.valueOf(max),x_offset+ disp,y_offset + 10*(noGraph+1));
//		disp+= 7* String.valueOf(max).length()+5;
//		g.drawString(x_unit,x_offset+ disp,y_offset + 10*(noGraph+1));
				
	}
	
	private void drawKeys(Graphics g){
		Font cur_Font = new Font("Arial", Font.BOLD, 10); //Book Antiqua
		g.setFont(cur_Font);
		int y_value=30;
		for(int i=0;i< noGraph; i++)
		{
			 g.setColor((Color)hashColor.get(i+1));
			
			 g.fillRect((int)(m_width-3*x_offset),y_value, 10,10);
			 g.setColor(Color.BLACK);
			 g.drawString(hashCapt.get(i+1).toString(),(int)(m_width-3*x_offset+10),y_value+10);
			 y_value+=12;
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
	    for(int y= y_offset-y_step ; y>=20;y-=y_step)
	    	g2d.drawLine(x_offset,y, m_width - x_offset,y);
	   
	
	    for(int x= x_offset + y_step ; x<m_width - x_offset;x+=y_step)
	    	g2d.drawLine(x,20, x,y_offset );
	    
	    int i=y_step;
	    for(int y= y_offset-y_step ; y>=20;y-=y_step, i+=y_step)
	    	g.drawString(i + "",m_width - x_offset,y);
	   g2d.setStroke(strk);
	   
	}

	  /** Display an image. **/
	public void addLineValue(double x,double y)
	{
		line.add(new Point((int)x+25,0));
	}
	
	 public void addGraphValue (int n,double x,double y) {
		  ArrayList<Point> vrtx=(ArrayList<Point>)hashList.get(n);
		  
		 if(x> m_width - x_offset-20)
		 {
				 if(vrtx.size()!=0 && vrtx.get(0).x < MIN )
					 vrtx.remove(0); 
				
				 vrtx.add(new Point((int)x,(int)y));
				// addCurrentGraphValue ((int) x,(int) y);
				 for(int i=1;i<= noGraph;i++)
				  {
					  ArrayList<Point> m=(ArrayList<Point>)hashList.get(i);
					  for(int j=0;j<m.size();j++)
					  {
						  //m.set(j,new Pointm.get(j).y-MIN);
						  int xx=m.get(j).x,yy=m.get(j).y;
						  //m.remove(j);
						  m.set(j,new Point(xx-MIN,yy));
					  }		
					  if(m.size()!=0 && m.get(0).x < MIN)
						  m.remove(0);
				  }
				 MIN=2;
			//	 System.out.print("-"+MIN+"-");
		 }
		  else
		  {
			  vrtx.add(new Point((int)x,(int)y));
		//  addCurrentGraphValue ((int) x,(int) y);
		  }
		  
	  } 
	  
	  public void addCurrentGraphValue (int x,int y) {		  
		  cur_Xval=x;
		  cur_Yval=y;		 
	  } //
	  
	  public void clearGraphValue () {
		for(int i=0;i<noGraph;i++)
		{
		ArrayList<Point> m_vrtx=(ArrayList<Point>)hashList.get(i+1);
		m_vrtx.clear();
		cur_Xval=0;
		cur_Yval=0;
		}
		//  for(int i=0;i<line.size();i++)
		 line.clear();
	  } //
	 
	  public void setColorCapt(int n,Color c,String capt)
	  {
		  hashColor.put(n,c);
		  hashCapt.put(n,capt);
	  }
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
	  
	  
	  public void setState(int s){
		  state =s;
	  }
	  
	  public void setHideFlag(boolean f){
		  hide =f;
	  }
	  
	  
}