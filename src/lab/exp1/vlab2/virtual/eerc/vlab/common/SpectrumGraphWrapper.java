package eerc.vlab.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/** Draw on a JPanel rather than on JApplet's Panel. **/
//public class GraphPlotter extends JPanel 
public class SpectrumGraphWrapper extends JPanel 
{
	private SpectrumGraph m_graph;
	private JSlider m_slider;	
	
	
	public SpectrumGraphWrapper (SpectrumGraph graph,int max,int mag) {
		   //setBackground(Color.black);
		  //setPreferredSize(new Dimension(w,h));
		  this.setLayout(new java.awt.BorderLayout());
		  this.setBackground(Color.GRAY);
		  m_graph = graph;
		  //m_scalefactor = sf;
		  
	      m_slider  = new JSlider(JSlider.HORIZONTAL,1, max*mag, max);
	      m_slider.setPreferredSize(new Dimension(5,20));
	      m_slider.setBackground(Color.GRAY);

	      m_slider.addChangeListener(new ChangeListener() {
              public void stateChanged(ChangeEvent e) {
	              int val = ((JSlider) e.getSource()).getValue();
            	  m_graph.setScale(val);
            	  
              }
            });
	       
           add(m_slider, BorderLayout.SOUTH);
           add(m_graph, BorderLayout.CENTER);
		   
	  } 
	
	private void setSliderVisible(boolean flag){
		m_slider.setVisible(flag);
	}
	
   		     
	
}
