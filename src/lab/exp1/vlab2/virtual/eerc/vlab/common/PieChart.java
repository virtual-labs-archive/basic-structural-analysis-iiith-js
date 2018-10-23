package eerc.vlab.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
public class PieChart extends JPanel {
	private Image mImage;
	int state=0;
	int width, height;
	int x_center;
	int y_center;
	int radius;
	float scale=1;
	boolean header_flag=false;
	boolean  hide = false;
	private HashMap<String, Color> Color_hash = new HashMap<String, Color>();
	private HashMap<String, Integer> StartAngle_hash=new HashMap<String, Integer>();
	private HashMap<String, Integer> ArcAngle_hash=new HashMap<String, Integer>();
	String heading="";
	ArrayList<String> Arc_name = new ArrayList<String>();
	Color m_bkg_Color = Color.GRAY;
	
	
	public PieChart (int w,int h,int r,float s) 
	{
		   //setm_bkg_Color(Color.black);
		   setPreferredSize(new Dimension(w,h));
		   
		   width = w;
		   height = h;
		   state =0;
		   x_center=(int)(w*0.5);
		   y_center=(int)(h*0.5);;
		   radius=r;
		   scale=s;
		   
		   
	 }
	
	public void drawPieChart()
	{
		checkOffscreenImage();
		
		if(hide) return;
		Graphics g1 = getGraphics();
		 
		 
		 drawArcs (mImage.getGraphics());
		 g1.drawImage(mImage, 0, 0, null);
		// drawArcs(g);
	}
	public void drawArcs(Graphics g) 
	{
	 Graphics2D g1 = (Graphics2D) g;
	 g1.setColor(m_bkg_Color);
	 g1.fillRect(0, 0, width-1, height);
		
		int i=0;
		for(i=0;i<Arc_name.size();i++)
		{
			Color c=(Color)Color_hash.get(Arc_name.get(i));
			g.setColor(c);
			//need to change blue to c
			int r=(int)scale*radius;
			int TopCornerX=x_center-r;
			int TopCornerY=y_center-r;
			int rect_width=2*r;
			int rect_height=2*r;
			Integer start_angle=(Integer) StartAngle_hash.get(Arc_name.get(i));
			int start=start_angle.intValue();
			Integer arc_angle=(Integer)ArcAngle_hash.get(Arc_name.get(i));
			int arcangle=arc_angle.intValue();
			
			g.fillArc(TopCornerX,TopCornerY,rect_width,rect_height,start,arcangle);
			
			 drawAllText(g);
			
		}
	}
	public void drawAllText(Graphics g)
	{
		g.setColor(Color.BLACK);
		Font cur_Font = new Font("Book Antiqua", Font.BOLD, 18);
		g.setFont(cur_Font);
		if(header_flag)
			g.drawString(heading, (int)(width*0.25),15);
		
		
		
	}
	
	
	public void addArc(String name,int startAngle, int arcAngle,Color c)
	{
		Color_hash.put(name, c);
		Arc_name.add(name);
		StartAngle_hash.put(name, startAngle);
		ArcAngle_hash.put(name, arcAngle);
	}
	
	private void checkOffscreenImage() {
		    Dimension d = getSize();
		    if (mImage == null || mImage.getWidth(null) != width || mImage.getHeight(null) != height) 
			    mImage = createImage(width, height);
	} 
	public void paint( Graphics g1 )
	{checkOffscreenImage();
		
		drawArcs(mImage.getGraphics());
		
		if(hide) return;
		g1.drawImage(mImage, 0, 0, null);
		
		
	}
	 public void setHeading (String h) 
	 {
		 header_flag=true;
		  heading = h;
	 } //
	 public void setHideFlag(boolean f){
		  hide =f;
	  }
	 public void setState(int s){
		  state =s;
	  }
	public void setScale(float s)
	{
		scale=s;
	}
	public void setArcColor(String name,Color c)
	{
		Color_hash.put(name, c);
	}
	public void setArcIntialAngle(String name,int theta)
	{
		StartAngle_hash.put(name, theta);
		
	}
	public void setArcAngle(String name,int theta)
	{
		ArcAngle_hash.put(name, theta);
	}
    public void setm_bkg_Color(Color c)
    {
    
    	m_bkg_Color=c;
    }

}
