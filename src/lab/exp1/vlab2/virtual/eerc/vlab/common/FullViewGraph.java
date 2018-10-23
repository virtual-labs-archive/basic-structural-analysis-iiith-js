package eerc.vlab.common;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FullViewGraph extends JPanel{
	private HorizontalGraph m_Graph[];
	private boolean m_visible = false;
	public FullViewGraph(){
		m_visible = false;
	}
    public FullViewGraph(HorizontalGraph graph[],int max[], int magX[],int magY[],int w,int h) {
        super();
        m_visible = true;
        m_Graph = new HorizontalGraph[graph.length];
        this.setLayout(new java.awt.GridLayout(graph.length,1));
		this.setBackground(Color.GRAY);
		h = 600/graph.length;
        for(int i=0;i<graph.length;i++){
        	m_Graph[i] =  new HorizontalGraph(graph[i],w,h);
        	add(new HorizontalGraphWrapper(m_Graph[i],max[i],magY[i],Color.GRAY));
        }
          
        //setPreferredSize(new Dimension(1032,682));
    }
 
          
        

   
    public void updateGraph(float val[]){ 
    	if (!m_visible)
    			return;
    	for(int i=0; i<m_Graph.length;i++)
    		m_Graph[i].addGraphValue(val[i]);
    	
    }
    public void drawGraph(){
    	if (!m_visible)
			return;
    	for(int i=0; i<m_Graph.length;i++)
    		m_Graph[i].drawGraph();
    }

    
}

