package eerc.vlab.demos;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.border.EtchedBorder;

import eerc.vlab.common.PointLineGraph;


@SuppressWarnings("serial")
public class TestPoint extends javax.swing.JPanel {
	private static PointLineGraph pg=null;
	public static void main(String args[])
	{
		    JFrame aWindow = new JFrame("This is a Border Layout");
		    aWindow.setBounds(30, 30, 300, 300); // Size
		    aWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    BorderLayout border = new BorderLayout(); // Create a layout manager
		    Container content = aWindow.getContentPane(); // Get the content pane
		    content.setLayout(border); // Set the container layout mgr
		    EtchedBorder edge = new EtchedBorder(EtchedBorder.RAISED); 
		    
		    
		    pg =new PointLineGraph(400,400,"gety ","getx");
		    pg.setHeading("test by pradeep ");
		 //   pg.setAxisUnit("sec","m");
		    pg.setYAxisColor(Color.BLUE);
		//    pg.setYScale(1000);
		 //   pg.fitToYwindow(true);    
		    content.add(pg, BorderLayout.CENTER);
		   
	}

}
