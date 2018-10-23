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
public class HorizontalGraphWrapper extends JPanel 
{
	private HorizontalGraph m_graph;
	private JSlider m_slider;	
	
	
	public HorizontalGraphWrapper (HorizontalGraph graph,int max,int mag,Color bkg) {
		   //setBackground(Color.black);
		  //setPreferredSize(new Dimension(w,h));
		  this.setLayout(new java.awt.BorderLayout());
		  this.setBackground(bkg);
		  this.setBorder(BorderFactory.createLineBorder(new Color(235,233,215),8));

		  m_graph = graph;
		  //m_graph.setBackgroundColor(bkg);
		  
	      m_slider  = new JSlider(JSlider.HORIZONTAL,1, max*mag, max);
	     // m_slider.setPreferredSize(new Dimension(5,15));
	      m_slider.setBackground(new Color(0.96f,0.96f,0.96f));

	      m_slider.addChangeListener(new ChangeListener() {
              public void stateChanged(ChangeEvent e) {
	              int val = ((JSlider) e.getSource()).getValue();
            	  m_graph.setYScale(val);
            	  
              }
            });
	       
           add(m_slider, BorderLayout.SOUTH);
           add(m_graph, BorderLayout.CENTER);
		   
	  } 
	
	private void setSliderVisible(boolean flag){
		m_slider.setVisible(flag);
	}
	
   		     
	
}
