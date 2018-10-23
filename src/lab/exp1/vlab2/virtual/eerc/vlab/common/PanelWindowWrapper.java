package eerc.vlab.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/** Draw on a JPanel rather than on JApplet's Panel. **/
//public class GraphPlotter extends JPanel 
public class PanelWindowWrapper extends JPanel 
{
	
	public PanelWindowWrapper (JPanel panel[],Color bkg) {
		   //setBackground(Color.black);
		  //setPreferredSize(new Dimension(w,h));
		  this.setLayout(new java.awt.BorderLayout());
		  this.setBackground(bkg);
		  //this.setBorder(BorderFactory.createLineBorder(new Color(235,233,215),8));
	     
		  for(int i=0;i<panel.length;i++)
			  if(i==0)
				  add(panel[i], BorderLayout.NORTH);
			  else  if(i==1)
				  add(panel[i], BorderLayout.CENTER);
			  else  if(i==2)
				  add(panel[i], BorderLayout.SOUTH);
		   
	  } 
	
		     
	
}
