package eerc.vlab.common;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/** Draw on a JPanel rather than on JApplet's Panel. **/
//public class GraphPlotter extends JPanel 
public class HelpWindow extends JPanel 
{
	JTextArea m_textArea;
	String m_vlab;
	
	public HelpWindow (String vlab) {
		  //setBackground(Color.black);
		  //setPreferredSize(new Dimension(w,h));
		 
		  this.setLayout(new java.awt.BorderLayout());
		  m_vlab = vlab;
		  // this.setBackground(bkg);
		  m_textArea = new JTextArea(50, 80);
		  		  
		  javax.swing.JPanel rightPanel = new javax.swing.JPanel();
		  rightPanel.setLayout(new java.awt.GridLayout(5,1,10,0));
		  
		  javax.swing.JButton button= new javax.swing.JButton("Manual");
		  rightPanel.add(button);
		  
		  button.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	String data = loadManual("resources/Help/"+ m_vlab + "/manual.txt");
	            	m_textArea.setText(data);
	            }
	       });
		  
		  button= new javax.swing.JButton("Introduction");
		  rightPanel.add(button);
		  button.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	String data = loadManual("resources/Help/"+ m_vlab + "/intro.txt");
	            	m_textArea.setText(data);
	            	
	            }
	      });
		  
		  button= new javax.swing.JButton("Theory");
		  rightPanel.add(button);
		  button.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	String data = loadManual("resources/Help/"+ m_vlab + "/theory.txt");
	            	m_textArea.setText(data);
	            	
	            }
	      });
		  
		  button= new javax.swing.JButton("Further Reading");
		  rightPanel.add(button);
		  button.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	// Toggle
	            	String data = loadManual("resources/Help/"+ m_vlab + "/ext_link.txt");
	            	m_textArea.setText(data);
	            }
	      });
		  
		  button= new javax.swing.JButton("About Us");
		  rightPanel.add(button);
		  button.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	// Toggle
	            	String data = loadManual("resources/Help/"+ m_vlab + "/about_us.txt");
	            	m_textArea.setText(data);
	            }
	      });
		  
		  this.add(rightPanel, BorderLayout.WEST);
		  		  
		  String data = loadManual("resources/Help/"+ m_vlab + "/manual.txt");
		  m_textArea.setText(data);
	      JScrollPane scrollPane = new JScrollPane(m_textArea);
	      
	      this.add(scrollPane, BorderLayout.CENTER);
		 
	      
	     
	        
	       
	        
	       
		   
  } 
	
	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public static void createAndShowGUI(String vlab) {
        //Create and set up the window.
        JFrame frame = new JFrame("Help");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add contents to the window.       
        frame.setExtendedState(frame.getExtendedState() | frame.MAXIMIZED_BOTH);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
        frame.add(new HelpWindow(vlab));
    }
    
    public String loadManual(String fileName){
    	String data="";
    	String line="";
    	StringBuffer strBuff;
    	URL url = Resources.getResource(fileName);
    	try{
    		InputStream in = url.openStream();
    		BufferedReader bf = new BufferedReader(new InputStreamReader(in));
    		strBuff = new StringBuffer();
    		while((line = bf.readLine()) != null){
    			//strBuff.append(line + "\n");
    			data += "\n" + line;
    			//System.out.println(line);
    			//System.out.println(temp[i].trim());
    		}
    	}catch(IOException e){
    			e.printStackTrace();
    	}
    	
    	return data;
    }
	
}
