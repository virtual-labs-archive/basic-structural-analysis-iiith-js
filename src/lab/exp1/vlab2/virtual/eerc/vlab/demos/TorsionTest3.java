package eerc.vlab.demos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Group;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
//import javax.media.j3d.Transform3D.*;
import javax.media.j3d.TransformGroup;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import eerc.vlab.common.FullViewGraph;
import eerc.vlab.common.HelpWindow;
import eerc.vlab.common.HorizontalGraph;
import eerc.vlab.common.PointLineGraph;
import eerc.vlab.common.J3DShape;
import eerc.vlab.common.Resources;
import eerc.vlab.common.ScatterGraph;
import java.util.Vector;
import javax.vecmath.Matrix3d;
import javax.vecmath.Quat4d;
	/**
	 * Simple Java 3D program that can be run as an application or as an applet.
	 */
	@SuppressWarnings({ "serial", "unused" })
        public class TorsionTest3 extends javax.swing.JPanel {
		//  Variables declaration - do not modify                     
		//////////////////////////GUI componenet ///////////////////////////
		private javax.swing.JPanel topPanel;
		private javax.swing.JPanel simulationPanel;
		private javax.swing.JPanel bottomPanel;
		private javax.swing.JPanel rightPanel;
		  
		private javax.swing.JPanel in1;			// Input panel 1
		private javax.swing.JPanel in2;			// Input panel 2
//		private javax.swing.JPanel in3;			// Input panel 3
		
		private javax.swing.JButton startButton=null;
		private javax.swing.JButton reStartButton=null;
		private javax.swing.JButton nextButton=null;

		

		//private GraphPlotter         graphPlotter;
		////////////////////////////Java3D componenet ///////////////////////////

		private SimpleUniverse      univ = null;                  // Simple Universe Java3D
		private BranchGroup         scene = null;                 // BranchGroup Scene graph
	
		


		
		private HorizontalGraph		outputGraph =null;
           //     private ScatterGraph		outputGraph1 =null;
                private PointLineGraph		outputGraph1 =null;
		private HorizontalGraph		inputGraph =null;
	//	private FullViewGraph  		fullViewGraph = new FullViewGraph();
		

		@SuppressWarnings("unchecked")
		private HashMap 			hm = new HashMap();
		private J3DShape 			m_j3d	= new J3DShape();

		private float[] fields;
		private JLabel outlbl_val[]=new JLabel[2];
		private JLabel iLabel[];
		private JSlider m_Slider[] = new JSlider[2];
		private JLabel m_Objective= new JLabel("Objective:");
		
		private Timer timer=null;
		private Timer m_cameraTimer=null; 
		private float m_cameraViews[];
		private int m_cameraEye;
		// Timer for simulation    
		
		private int stage = 0;
               
		double constant;
		private boolean startStop = false;
		private boolean valChange = true;
		private String[] units ={" (mm) "," (mm) "};
		
	
		Vector<TransformGroup> Tg = new Vector<TransformGroup>();
                    Transform3D t1 = new Transform3D();
                
		int Shaft_length=20,Shaft_dia=10;
		double Torque=0.0,Twist=0.0;
		int index = 0;//works like timer

		
	
		
		public BranchGroup createSceneGraph() 
		{
			// Create the root of the branch graph
			BranchGroup objRoot = new BranchGroup();
			objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND );
			objRoot.setCapability(Group.ALLOW_CHILDREN_READ);
			objRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
			objRoot.setCapability( BranchGroup.ALLOW_DETACH );
			
			

			objRoot.addChild(createVirtualLab());
		
			

			objRoot.addChild(m_j3d.createBox(new Vector3d(0.0,-0.45, -0.5),new Vector3d(3,.01,3),new Vector3d(90,0,0),new Color3f(0f, 1f, 0f),"resources/images/table.jpg"));
			objRoot.addChild(m_j3d.createBox(new Vector3d(4.0,0.4, -.6),new Vector3d(3,4.9,.1),new Vector3d(0, 90,0), new Color3f(0.5f,0.6f,0.72f)));
			
			
			float rad = (float)Math.PI/180;
			Transform3D t = new Transform3D();
			t.setScale(0.4);
			
			TransformGroup tg = new TransformGroup();
			t = new Transform3D();
			t.rotX(rad*10);
			t.setScale(new Vector3d(.5f,.05f,.5f));        
			t.setTranslation(new Vector3d(.3,.3,0));
			tg.setTransform(t);
		    
			return objRoot;
		}

	    private Canvas3D createUniverse(Container container) {
	        GraphicsDevice graphicsDevice;
	        if (container.getGraphicsConfiguration() != null) {
	            graphicsDevice = container.getGraphicsConfiguration().getDevice();
	        } else {
	            graphicsDevice =
	                    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	        }
	        GraphicsConfigTemplate3D template = new GraphicsConfigTemplate3D();
	        GraphicsConfiguration config = graphicsDevice.getBestConfiguration(template);

	        Canvas3D c = new Canvas3D(config);

	        univ = new SimpleUniverse(c);

	        // This will move the ViewPlatform back a bit so the
	        // objects in the scene can be viewed.
	        
	        
			ViewingPlatform viewingPlatform = univ.getViewingPlatform();
	        setLight();
	        
	        univ.getViewingPlatform().setNominalViewingTransform();
	              
	        // Ensure at least 5 msec per frame (i.e., < 200Hz)
	        univ.getViewer().getView().setMinimumFrameCycleTime(5);

	        return c;
	    }
	    
	    private void setLight() {
	            BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
	            PlatformGeometry pg = new PlatformGeometry();


	            Color3f ambientColor = new Color3f(0.1f, 0.1f, 0.1f);
	            AmbientLight ambientLightNode = new AmbientLight(ambientColor);
	            ambientLightNode.setInfluencingBounds(bounds);
	            pg.addChild(ambientLightNode);


	            Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
	            Vector3f light1Direction  = new Vector3f(1.0f, 1.0f, 1.0f);
	            Color3f light2Color = new Color3f(1.0f, 1.0f, 1.0f);
	            Vector3f light2Direction  = new Vector3f(-1.0f, -1.0f, -1.0f);

	            DirectionalLight light1
	                    = new DirectionalLight(light1Color, light1Direction);
	            light1.setInfluencingBounds(bounds);
	            pg.addChild(light1);

	            DirectionalLight light2
	                    = new DirectionalLight(light2Color, light2Direction);
	            light2.setInfluencingBounds(bounds);
	            pg.addChild(light2);

	            ViewingPlatform viewingPlatform = univ.getViewingPlatform();
	            viewingPlatform.setPlatformGeometry( pg );


	    }

	    
	    private void destroy() {
	        univ.cleanup();
	    }

	    
	    
	        
		@SuppressWarnings("unchecked")
		private Group createVirtualLab() {

			
		    Transform3D t = new Transform3D();
	        //t.setTranslation(new Vector3d(0,.1,.0));
	                    
		    TransformGroup objtrans = new TransformGroup(t);
		    objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		    
		                        

		                        
		                /*        Color3f red = new Color3f(1.0f,0f,0f);
		                        LineAttributes la = new LineAttributes();
		                        la.setLineWidth(2.0f);
		                        ColoringAttributes ca = new ColoringAttributes(red, ColoringAttributes.SHADE_FLAT);
		                        Appearance app = new Appearance();
		                        app.setColoringAttributes(ca);
		                        app.setLineAttributes(la);
		                        Point3f[] coords = new Point3f[2];
		                        coords[0] = new Point3f(.5f,1f,-0.03f);
		                        coords[1] = new Point3f(2f,1f,-0.03f);
		                        LineArray line = new LineArray(2, LineArray.COORDINATES);
		                        line.setCoordinates(0, coords);
		                        Shape3D myShape = new Shape3D(line,app);
		                        
		                        objtrans.addChild(myShape); 
		                        
		                        */
		    
		                        setLook(-2.1f, 0.1f, 0.1f);		                        
                    Transform3D t2 = new Transform3D();
                    //Transform3D rot = new Transform3D();
		    
                    t2.setTranslation(new Vector3d(0.0,-0.16,-0.1));

                    
//                    rot.rotZ(-Math.toRadians(25));
//                    t1.rotX(-Math.toRadians(65));
//                    t1.mul(rot);
                    t1.setTranslation(new Vector3d(0.0,-0.21,0.1));
                    

		    TransformGroup cylinder1 = new TransformGroup(t1);Tg.add(cylinder1);
                    TransformGroup cylinder2 = new TransformGroup(t1);Tg.add(cylinder2);
                    TransformGroup cylinder3 = new TransformGroup(t1);Tg.add(cylinder3);
                    TransformGroup cylinder4 = new TransformGroup(t1);Tg.add(cylinder4);
                    TransformGroup cylinder5 = new TransformGroup(t1);Tg.add(cylinder5);
                    TransformGroup cylinder6 = new TransformGroup(t1);Tg.add(cylinder6);
                    TransformGroup cylinder7 = new TransformGroup(t1);Tg.add(cylinder7);
                    TransformGroup cylinder8 = new TransformGroup(t1);Tg.add(cylinder8);
                    TransformGroup cylinder9 = new TransformGroup(t1);Tg.add(cylinder9);
                    TransformGroup cylinder10 = new TransformGroup(t1);Tg.add(cylinder10);
                    TransformGroup cylinder11 = new TransformGroup(t1);Tg.add(cylinder11);
                    TransformGroup cylinder12 = new TransformGroup(t1);Tg.add(cylinder12);
                    TransformGroup cylinder13 = new TransformGroup(t1);Tg.add(cylinder13);
                    TransformGroup cylinder14 = new TransformGroup(t1);Tg.add(cylinder14);
                    TransformGroup cylinder15 = new TransformGroup(t1);Tg.add(cylinder15);
                    TransformGroup cylinder16 = new TransformGroup(t1);Tg.add(cylinder16);
                    TransformGroup cylinder17 = new TransformGroup(t1);Tg.add(cylinder17);
                    TransformGroup cylinder18 = new TransformGroup(t1);Tg.add(cylinder18);
                    TransformGroup cylinder19 = new TransformGroup(t1);Tg.add(cylinder19);
                    TransformGroup cylinder20 = new TransformGroup(t1);Tg.add(cylinder20);

                    
                    
                    
                    
                    for (int i = 0;i<Tg.size();i++){
                        //System.out.println(Tg.size());
                        Tg.get(i).setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                        Tg.get(i).setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
                        Tg.get(i).addChild(m_j3d.createTextureCylinder( new Vector3d(0,0.08f+i*.02,0), new Vector3d(0.4,0.2,0.4),new Vector3d(0,0,0),new Color3f(255.0f/255.0f,255.0f/255.0f,255.0f/255.0f),"resources/images/strips2.jpg","base1",hm));
                        
                        
                        
                        
                        
                        hm.put("cylinder"+i,Tg.get(i));
                        objtrans.addChild(Tg.get(i));
                    }
                    //System.out.println(Tg.get(19).getName());

                    TransformGroup support = new TransformGroup(t1);
                    support.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                    support.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
                    support.addChild(m_j3d.createTextureBox(new Vector3d(-0.03,0.48f,-0.03),new Vector3d(0.15,0.01,0.15),new Vector3d(0,0,0),new Color3f(255.0f/255.0f, 255.0f/255.0f, 255.0f/255.0f),"resources/images/wood.jpg","base1",hm));
                    hm.put("support",support);
                    objtrans.addChild(support);

                    TransformGroup handle = new TransformGroup(t1);
                    handle.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                    handle.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
                    handle.addChild(m_j3d.createTextureBox(new Vector3d(0.0,0.065f,0.07),new Vector3d(0.15,0.015,0.03),new Vector3d(0,90,0),new Color3f(255.0f/255.0f, 255.0f/255.0f, 255.0f/255.0f),"resources/images/bluegem.jpg","base1",hm));
                    hm.put("handle",handle);
                    objtrans.addChild(handle);
		    //objtrans.addChild(m_j3d.createTextureBox(new Vector3d(0,0.15f,0.0),new Vector3d(.5,.001,0.23),new Vector3d(0,0,0),new Color3f(250.0f/255.0f, 247.0f/255.0f, 11.0f/255.0f),"resources/images/tile1.jpg","base1",hm));
		    //objtrans.addChild(m_j3d.createTextureBox(new Vector3d(0,0.25f,0.0),new Vector3d(.5,.001,0.23),new Vector3d(0,0,0),new Color3f(250.0f/255.0f, 246.0f/255.0f, 10.0f/255.0f),"resources/images/tile1.jpg","base2",hm));
		    hm.put("base",objtrans);
		    
		    return objtrans;
		}
	    
	     
	     
	   
	     
	     
	    
	    /**
	     * Creates new form FreeVibration
	     */
	    public TorsionTest3(Container container) {
	        // Initialize the GUI components
	        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
	        initComponents();

	        centerPanel(container);
	        // Create Canvas3D and SimpleUniverse; add canvas to drawing panel
	        
//	        scene.addChild(bgleg);
	    }

	    
	    // ----------------------------------------------------------------
	    
	    // Applet framework

	    public static class MyApplet extends JApplet {
	        TorsionTest3 mainPanel;

	        public void init() {
	            setLayout(new BorderLayout());
	            mainPanel = new TorsionTest3(this);
	            add(mainPanel, BorderLayout.CENTER);
	                        
	        }
	        

	        public void destroy() {
	            mainPanel.destroy();
	        }
	    }

	    // Application framework

	    private static class MyFrame extends JFrame {
	        MyFrame() {
	            setLayout(new BorderLayout());
	            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	            setTitle("TorsionTest Body Applet");
	            getContentPane().add(new TorsionTest3(this), BorderLayout.CENTER);
	            pack();
	        }
	    }

	    // Create a form with the specified labels, tooltips, and sizes.
	    /**
	     * @param args the command line arguments
	     */
	    public static void main(String args[]) {
	        java.awt.EventQueue.invokeLater(new Runnable() {
	            public void run() {
	                new MyFrame().setVisible(true);
	            }
	        });
	    }
	    

	    private void initComponents() {
	        
	        

	        setLayout(new java.awt.BorderLayout());
	        
	        bottomPanel = new javax.swing.JPanel(); 	// input from user at bottom
	        simulationPanel = new javax.swing.JPanel(); // 3D rendering at center
	        topPanel= new javax.swing.JPanel();    		// Pause, resume, Next
	        rightPanel = new javax.swing.JPanel();    	// Graph and Input and Output Parameter
	                
	         
	        topPanel();                 
	        bottomPanel();        
	        rightPanel();
	        
//	      Set Alignment
	        //add(guiPanel, java.awt.BorderLayout.NORTH);
	        add(topPanel, java.awt.BorderLayout.NORTH);
	        add(simulationPanel, java.awt.BorderLayout.CENTER);
	        add(bottomPanel, java.awt.BorderLayout.SOUTH);
	        add(rightPanel, java.awt.BorderLayout.EAST); 
	        
	        startStop = false;
	    	valChange = true;
	    	stage =0;
	        
	        timer = new Timer(50,new ActionListener() {
	            public void actionPerformed(ActionEvent evt) {
	                //...Perform a task...
	            	timerActionPerformed(evt);
	            }
	        });
	        
	                            

	        
	    }// </editor-fold>                        

	    private void topPanel() {
	            
	        java.awt.GridBagConstraints gridBagConstraints;
	            
	        javax.swing.JPanel guiPanel = new javax.swing.JPanel(); // Pause, resume at top
	        guiPanel.setLayout(new java.awt.GridBagLayout());
	        gridBagConstraints = new java.awt.GridBagConstraints();
	        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);  
	                
//	        javax.swing.JButton pauseButton = new javax.swing.JButton();  
//	        javax.swing.JButton startButton = new javax.swing.JButton(); 
	        reStartButton = new javax.swing.JButton("Re-Start");
	        ImageIcon icon = m_j3d.createImageIcon("resources/icons/restart.png"); 
	        reStartButton.setIcon(icon);
	        startButton = new javax.swing.JButton("Start");
	        icon = m_j3d.createImageIcon("resources/icons/start.png"); 
	        startButton.setIcon(icon);
	        nextButton = new javax.swing.JButton("Next");
	        icon = m_j3d.createImageIcon("resources/icons/next.png");        
	        nextButton.setIcon(icon);
//	        ImageIcon icon = m_j3d.createImageIcon("resources/images/show_graph.png");        
//	        startButton.setIcon(icon);
	        //startButton.setPreferredSize(new Dimension(100,30));
	        
	             
	        //reStartButton.setText("Re-Start");  
	        reStartButton.setEnabled(false);
	        nextButton.setEnabled(false);
	        
	        
	        
	        guiPanel.setBackground(new Color(67,143,205));//Color.BLACK
	        topPanel.setLayout(new java.awt.BorderLayout());
	        topPanel.add(guiPanel, java.awt.BorderLayout.NORTH);
	        
	        
	        startButton.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	// Toggle
	            	startStop = !startStop;
	            	
	            	if(startStop)  startSimulation(evt); 
	            	else pauseSimulation();
	            	univ.getCanvas().repaint();
	            }
	          });
	        
	       
	        
	        reStartButton.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	reStartButton.setEnabled(false);
	                //startButton.setEnabled(true);
	                startButton.setText("Start");
	                startStop = !startStop;
	               // startStop = false;
	                
	                outputGraph.clearGraphValue();
	     //           inputGraph.clearGraphValue();
	                
	                valChange = true;
	                startSimulation(evt);
	                univ.getCanvas().repaint();
	               
	                

	                
	            }
	          });
	        
	        nextButton.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {                
	            	stage++;
	            	nextButton.setEnabled(false);            	
	            	onNextStage();
	            	univ.getCanvas().repaint();
	            }
	          });
	        
	       
//	        javax.swing.JButton btn= new javax.swing.JButton("Full View Graph");
//	        guiPanel.add(btn, gridBagConstraints);
//	        icon = m_j3d.createImageIcon("resources/icons/graph_window.png");        
//	        btn.setIcon(icon);
//	        btn.addActionListener(new java.awt.event.ActionListener() {
//	            @SuppressWarnings("static-access")
//				public void actionPerformed(java.awt.event.ActionEvent evt) {                
//	 
//	            	HorizontalGraph graph[] ={outputGraph};
//	             	int max[]={1000,100};
//	             	int magX[]={2,2};
//	             	int magY[]={2,2};
//	            	
//	            	JFrame frame = new JFrame("Full View Graph");
//	            	//GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
//	                //Add contents to the window.
//	            	
//	                //frame.add(p);
//	            	frame.setExtendedState(frame.getExtendedState() | frame.MAXIMIZED_BOTH);
//
//	                
//	                frame.pack();
//	                frame.setVisible(true);
//	              
//	                
//	                fullViewGraph =new FullViewGraph(graph,max,magX,magY,frame.getWidth()-20, frame.getHeight());
//	                 
//	                frame.add(fullViewGraph);
//	                System.out.println("w " + frame.getWidth() + " h " + frame.getHeight());
//	                
//	                
//	            }
//	          });
	        
	      
	      guiPanel.add(reStartButton, gridBagConstraints);
	      guiPanel.add(startButton, gridBagConstraints);
	      guiPanel.add(nextButton, gridBagConstraints);
	        
//	      btn= new javax.swing.JButton("Manual"); 
//	      icon = m_j3d.createImageIcon("resources/icons/manual.png");        
//	      btn.setIcon(icon);
//	      //startButton.setPreferredSize(new Dimension(100,30));
//	        guiPanel.add(btn, gridBagConstraints);
//	        
//	        btn.addActionListener(new java.awt.event.ActionListener() {
//	            public void actionPerformed(java.awt.event.ActionEvent evt) {                
//	            	
//	                HelpWindow.createAndShowGUI("forcedVib");
//	            }
//	          });

	        
	        
	        
	    }
	    
	    
	    private void rightPanel() {
	        
	        
	        rightPanel.setLayout(new java.awt.GridLayout(2,1,0,1));
	       
	        outputGraph = new HorizontalGraph(300,300," Torque "," Twist "); 
	        outputGraph.setHeading("Torque Vs Twist Curve ");
	        outputGraph.setAxisUnit(" "," ");
	        outputGraph.setYAxisColor(Color.BLUE);
	        outputGraph.setYScale(1);
	        outputGraph.fitToYwindow(true);        
	   
	        rightPanel.add(outputGraph);
                
                
                outputGraph1 = new PointLineGraph(300,300," "," "); 
	        outputGraph1.setHeading(" ");
	   //   outputGraph1.setAxisUnit("sec","m");
	        outputGraph1.setYAxisColor(Color.RED);
	        outputGraph1.setScale(1);
//	        outputGraph1.fitToYwindow(true);        
	   
	        rightPanel.add(outputGraph1);
	         outputGraph1.setVisible(false);       
	        rightPanel.setVisible(false);

	    }
	    
	    private static void enable(Container root, boolean enable) {
		    Component children[] = root.getComponents();
		    for(int i = 0; i < children.length; i++) 
				    children[i].setEnabled(enable);
	    }
	    
	    private void centerPanel(Container container){
	    	
	    	 simulationPanel.setPreferredSize(new java.awt.Dimension(1024, 600));
	         simulationPanel.setLayout(new java.awt.BorderLayout());
	        
	         javax.swing.JPanel guiPanel = new javax.swing.JPanel();
	         guiPanel.setBackground(new Color(100,100,100));
	         JLabel lbl = new JLabel("Torsion Test", JLabel.CENTER);
	         lbl.setFont(new Font("Arial", Font.BOLD, 18));

	         lbl.setForeground(Color.orange);
	        
	         guiPanel.add(lbl);
	         simulationPanel.add(guiPanel, BorderLayout.NORTH);
	         
	         Canvas3D c = createUniverse(container);
	         simulationPanel.add(c, BorderLayout.CENTER);

	         JPanel btmPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
	         simulationPanel.add(btmPanel, BorderLayout.SOUTH);
	         
	         guiPanel = new javax.swing.JPanel();
	         guiPanel.setBackground(new Color(100,100,100));         
	         simulationPanel.add(guiPanel, BorderLayout.EAST);
	         
	         guiPanel = new javax.swing.JPanel();
	         guiPanel.setBackground(new Color(100,100,100));         
	         simulationPanel.add(guiPanel, BorderLayout.WEST);
	         
	         // Create the content branch and add it to the universe
	         scene = createSceneGraph();
	         univ.addBranchGraph(scene);
	         
	     
	    
	         
	         m_Objective = new JLabel(">:Observe the angle of twist in the speciman.", JLabel.LEFT);
	         m_Objective.setFont(new Font("Arial", Font.BOLD, 13));
	         m_Objective.setForeground(Color.WHITE);
	         guiPanel = new javax.swing.JPanel();
	         guiPanel.setBackground(new Color(100,100,100));        
	         guiPanel.add(m_Objective);
	         btmPanel.add(guiPanel,BorderLayout.NORTH);
	         
	         
	         
	         guiPanel = new javax.swing.JPanel(); //          
	         guiPanel.setBackground(new Color(235,233,215));
	         guiPanel.setLayout(new java.awt.GridBagLayout());
	         java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
	         gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);  

//	         javax.swing.JButton viewButton= new javax.swing.JButton("Horizontal View");
//	         ImageIcon icon = m_j3d.createImageIcon("resources/icons/h-view.png"); 
//	         viewButton.setIcon(icon);
//	         viewButton.addActionListener(new java.awt.event.ActionListener() {
//	             public void actionPerformed(java.awt.event.ActionEvent evt) {                
//	             	
//	            	 //  0 , 
//	            	 if(m_cameraTimer!=null && m_cameraTimer.isRunning()){ m_cameraTimer.stop();}
//	            	 setCameraViews();
//	            	 m_cameraTimer = new Timer(200,new ActionListener() {
//	                     public void actionPerformed(ActionEvent evt) {
//	                         //...Perform a task...
//	                    	 timerActionHorizontalCameraMotion(evt);
//	                     }
//	                 });
//	            	 m_cameraTimer.start();
//	             }
//	           });
//
//	         
//	         guiPanel.add(viewButton, gridBagConstraints);
	          
//	         viewButton= new javax.swing.JButton("Vertical View");
//	         icon = m_j3d.createImageIcon("resources/icons/v-view.png");
//	         viewButton.setIcon(icon);
//	         viewButton.addActionListener(new java.awt.event.ActionListener() {
//	             public void actionPerformed(java.awt.event.ActionEvent evt) {                
//	             	
//	            	 if(m_cameraTimer!=null && m_cameraTimer.isRunning()){ m_cameraTimer.stop();}
//	            	 setCameraViews();
//	            	 m_cameraTimer = new Timer(200,new ActionListener() {
//	                     public void actionPerformed(ActionEvent evt) {
//	                                	 timerActionVerticalCameraMotion(evt);
//	                     }
//	                 });
//	            	 m_cameraTimer.start();
//	            	 
//	             }
//	           });
	         
//	         guiPanel.add(viewButton, gridBagConstraints);
	         
	         JCheckBox chkbox = new JCheckBox("");
	         lbl = new JLabel("Change Input Parameters", JLabel.CENTER);
	         ImageIcon icon = m_j3d.createImageIcon("resources/icons/tasklist.png");        
	         lbl.setIcon(icon);
	         chkbox.addActionListener(new ActionListener() {
	             public void actionPerformed(ActionEvent event){                               
	                     boolean bChecked =((JCheckBox)event.getSource()).isSelected();
	                     if(bChecked)
	                    	 bottomPanel.setVisible(true);
	                     else
	                    	 bottomPanel.setVisible(false);
	                     univ.getCanvas().repaint();
	                            
	             }
	         });

	         guiPanel.add(chkbox, gridBagConstraints);
	         guiPanel.add(lbl, gridBagConstraints);
	         
	         chkbox = new JCheckBox("");
	         lbl = new JLabel("Show Graphs", JLabel.CENTER);
	        
	         icon = m_j3d.createImageIcon("resources/icons/show_graph.png");        
	         lbl.setIcon(icon);
	         chkbox.addActionListener(new ActionListener() {
	             public void actionPerformed(ActionEvent event){                               
	                     boolean bChecked =((JCheckBox)event.getSource()).isSelected();
	                     if(bChecked)
	                    	 rightPanel.setVisible(true);
	                     else
	                    	 rightPanel.setVisible(false);
	                     univ.getCanvas().repaint();
	                            
	             }
	         });
	         guiPanel.add(chkbox, gridBagConstraints);
	         guiPanel.add(lbl, gridBagConstraints);

	         
	         btmPanel.add(guiPanel,BorderLayout.CENTER);
	                 
	         guiPanel = new javax.swing.JPanel(); // 
	         guiPanel.setBackground(new Color(130,169,193));
	         guiPanel.setBorder(BorderFactory.createLineBorder(new Color(235,233,215),4));
	      //   guiPanel.add(createInputOutputPanel());
	         btmPanel.add(guiPanel,BorderLayout.SOUTH);
	         
	        

	    }
	 
	    
	    

	    private void bottomPanel()
	    {
	    	   initInputControlsField();
	           
	    	   Color bk = new Color(219,226,238);
	           bottomPanel.setLayout(new java.awt.GridLayout(1,2));
	           bottomPanel.setBackground(Color.black);
	           bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(235,233,215),8));
	           
	           in1 = new JPanel(new java.awt.GridLayout(3,3));
	           in1.setBackground(bk);
	           bottomPanel.add(in1);

	           in2 = new JPanel(new java.awt.GridLayout(3,2)); 
	           in2.setBackground(bk);

	           bottomPanel.add(in2);

	           JLabel lab = new JLabel("INPUTS", JLabel.CENTER);
	          
	           JLabel lab2=new JLabel("-", JLabel.CENTER);
                    JLabel lab3=new JLabel("-", JLabel.CENTER);
	           in1.add(lab);
	           in1.add(lab2);
                   in1.add(lab3);
                 
                   
	           
	           
	           lab=new JLabel("Length of Shaft", JLabel.CENTER);
	           m_Slider[0] = new JSlider(JSlider.HORIZONTAL,1, 10, 3);
	           m_Slider[0].addChangeListener(new ChangeListener() {
	   		    public void stateChanged(ChangeEvent e) {
	               valChange = true;
	               int val = ((JSlider) e.getSource()).getValue();
	               fields[0]=val*40;
	               iLabel[0].setText(":: " + fields[0] + " mm");
	               
	               repaint();            
	            }
	            });
	           m_Slider[0].setBackground(bk);
	           in1.add(lab);
	           in1.add(m_Slider[0]);
                   in1.add(iLabel[0]);
	           
	           
	           lab=new JLabel("Diameter of Shaft", JLabel.CENTER);
	           m_Slider[1] = new JSlider(JSlider.HORIZONTAL,5, 10, 5);
	           m_Slider[1].addChangeListener(new ChangeListener() {
	   		    public void stateChanged(ChangeEvent e) {
	               valChange = true;
	               int val = ((JSlider) e.getSource()).getValue();
	               fields[1]=val*2;
	               iLabel[1].setText(":: " + fields[1] + " mm");
	               
	               repaint();            
	            }
	            });
	           m_Slider[1].setBackground(bk);
	           in1.add(lab);
	           in1.add(m_Slider[1]);
                    in1.add(iLabel[1]);
	           
	           lab=new JLabel("----OUTPUT----", JLabel.CENTER);
	            lab2=new JLabel("            ", JLabel.CENTER);
	           in2.add(lab);
	           in2.add(lab2);
	           
	           lab=new JLabel("Torque  = ", JLabel.CENTER);
	           outlbl_val[0] = new JLabel(" 0 N mm", JLabel.LEFT);
	           
                   in2.add(lab);
                   in2.add(outlbl_val[0]);
	           
	           lab=new JLabel("Angle of Twist = ", JLabel.CENTER);
	           outlbl_val[1] = new JLabel(" 0 Degrees", JLabel.LEFT);
	           
                   in2.add(lab);
                   in2.add(outlbl_val[1]);
	           bottomPanel.setVisible(false);
	           
	         
	    }
	    
	    private void setField(JLabel lbl, float v, int f){
	    	fields[f]= v;
	    	lbl.setText(" :: " + Float.toString(fields[f]) + units[f]);
	    }
	    
	    
	    private void initInputControlsField(){
	    	iLabel = new JLabel[4];
	    	int i=0;
	    	
	    	iLabel[0] = new JLabel("120 (mm)", JLabel.LEFT); 
	    	iLabel[0].setForeground(Color.BLUE);
                
                
	    	iLabel[1] = new JLabel("10 (mm)", JLabel.LEFT); 
	    	iLabel[1].setForeground(Color.BLUE);
                
                
                iLabel[2] = new JLabel("0 (N.mm)", JLabel.LEFT); 
	    	iLabel[2].setForeground(Color.BLUE);
                
                
	    	iLabel[3] = new JLabel("0 (deg)", JLabel.LEFT); 
	    	iLabel[3].setForeground(Color.BLUE);
	    	
	    	
	    	
	    	
	
	    
	    	
	    	i=0;
	    	fields = new float[2];
	    	fields[i++] = 120;
	    	fields[i++] = 10;
	   
	        
	    }
	   

	    private void onNextStage()
	    {
	    	    	
	    	valChange = true;     	
	    	resetOutputParameters(); 
	    	bottomPanel.setVisible(true);
	    	enableStage(stage);
	    	setInstructionText();
	   		 
	    }
	    private void enableStage(int s){
	    	switch(s){
	    	case 0: // Home     		
	    		enable(in1,false);	 enable(in2,false);		// enable(in3,false);	
				
	    		break;
	    
	    	case 1:

	    		enable(in1,true);	 enable(in2,true);		// enable(in3,true);	
	    
	    		break;
                case 2:

	    		enable(in1,true);	 enable(in2,true);		// enable(in3,true);	
	                nextButton.setVisible(false);
	    		break;

	    	}
	    	
	    }
	    
	    private void setInstructionText()
	    {
	    	    	
	    	valChange = true;   	
	    	resetOutputParameters(); 
	    	
	    	
	    	switch(stage){
	    	case 0: // Home 
	    		m_Objective.setText(">:  Observe the angle of twist in the speciman.");
	    		m_Objective.setForeground(Color.WHITE);
	    					break;
	    	case 1:
	    		m_Objective.setText(">: Observe the angle of twist in the speciman.");
	    		m_Objective.setForeground(Color.GREEN);
	    		    		break;
	    	

	    	}
	    		
	   		 
	    }
	    
	    private void resetOutputParameters()
	    {
	    	int i=2;
	       
//	        outlbl_val[i++].setText(" 0 sec");
//	        outlbl_val[i++].setText(" 0 (m/s)");
	      

	        
	    }
	    
	    private void  setCameraViews()
	    {
	    	m_cameraViews = new float[360];
	    	int i=0;
	    	for(i=0;i<90;i++)
	    		m_cameraViews[i] = i;
	    	for(int j=0;j<90;j++,i++)
	    		m_cameraViews[i] = (90-j);
	    	for(int j=0;j<90;j++,i++)
	    		m_cameraViews[i] = -j;
	    	for(int j=0;j<90;j++,i++)
	    		m_cameraViews[i] = -(90-j);
	    	
	    	m_cameraEye =0;
	    	
	    }
	    private void  timerActionVerticalCameraMotion(java.awt.event.ActionEvent evt)
	    {
	    	ViewingPlatform vp = univ.getViewingPlatform();
		    TransformGroup steerTG = vp.getViewPlatformTransform();
		    Transform3D t3d = new Transform3D();
		    steerTG.getTransform(t3d);
		  
		    Vector3f currPos=new Vector3f();
		    t3d.get(currPos); 
		   
		
		    float y = (float)Math.sin(Math.toRadians(m_cameraViews[m_cameraEye]));
		    float z = 2.41f - Math.abs(y);
		   
		    t3d.lookAt( new Point3d(0, y,z), new Point3d(0,0,0), new Vector3d(0,1,0));
		    t3d.invert();
		    
		    
		    steerTG.setTransform(t3d);
		    m_cameraEye++;
		    if(m_cameraEye == 180){
		    	m_cameraTimer.stop();
		    	m_cameraEye = 0;
		    }
	    }
	    
	    private void  timerActionHorizontalCameraMotion(java.awt.event.ActionEvent evt)
	    {
	    	ViewingPlatform vp = univ.getViewingPlatform();
		    TransformGroup steerTG = vp.getViewPlatformTransform();
		    Transform3D t3d = new Transform3D();
		    steerTG.getTransform(t3d);
		  
		    Vector3f currPos=new Vector3f();
		    t3d.get(currPos);
		    
		
		    float x = (float)Math.sin(Math.toRadians(m_cameraViews[m_cameraEye]));
		    float z = 2.41f - Math.abs(x);
		
		    t3d.lookAt( new Point3d(x, 0,z), new Point3d(0,0,0), new Vector3d(0,0,1));
		    t3d.invert();
		    
		  
		    steerTG.setTransform(t3d);
		    m_cameraEye++;
		    if(m_cameraEye == 360){
		    	m_cameraTimer.stop();
		    	m_cameraEye = 0;
		    }
	    }
            private void setLook(float x,float y,float z){
                ViewingPlatform vp = univ.getViewingPlatform();
		    TransformGroup steerTG = vp.getViewPlatformTransform();
		    Transform3D t3d = new Transform3D();
		    steerTG.getTransform(t3d);

		    Vector3f currPos=new Vector3f();
		    t3d.get(currPos);

		    t3d.lookAt( new Point3d(x,y,z), new Point3d(0,0,0), new Vector3d(0,0,1));
		    t3d.invert();


		    steerTG.setTransform(t3d);
		    m_cameraEye++;
		    if(m_cameraEye == 360){
		    	m_cameraTimer.stop();
		    	m_cameraEye = 0;
		    }
            }
	    // Resume Button Action
	    private void startSimulation(java.awt.event.ActionEvent evt)
	    {
	    	ImageIcon icon = m_j3d.createImageIcon("resources/icons/stop.png"); 
	    	startButton.setIcon(icon);
	    	startButton.setText("Stop");
	    	enableStage(0);    	
	        reStartButton.setEnabled(false);
	        nextButton.setEnabled(false);
	        outputGraph1.setState(1);
	

	        
	        if(valChange){

		        init((int)fields[0],(int)fields[1]);
		        outputGraph.clearGraphValue();		      
	        }
	        
	        timer.start();
	        System.out.println("Timer started");     
	    }
	    
	   
	    
	 
	   

		private void timerActionPerformed(java.awt.event.ActionEvent evt)
	    {
                 int i=0;   

	            float MyTorque=(float)Torque;
	            float MyTwist=(float)Twist;
                outlbl_val[i++].setText(String.valueOf(Torque) + " N.mm");
     	        outlbl_val[i++].setText(String.valueOf(Twist)+ " Deg");
	    
	        ////////////// Graph /////////
	        
	        outputGraph.addGraphValue(Twist);
	        outputGraph.setCurrentValue(MyTorque,MyTwist);
	        //outputGraph1.addLineValue((float)(Torque/10000.0),(float)Twist);
	      
	   
	        
	        if(rightPanel.isVisible())
	        {
                        outputGraph.drawGraph();
	        	
                        outputGraph1.drawGraph();
	      
	        }
	                
//	        fullViewGraph.updateGraph(new float[]{});
//	        fullViewGraph.drawGraph();
//	        

	    
	        //updating base movement  
	        TransformGroup tgp = (TransformGroup)hm.get("base");
	        Transform3D trans = new Transform3D();
	    	tgp.getTransform(trans);
	   //     trans.setTranslation(new Vector3d(eqd/100,0,0)); // intensionally removed this line
	        tgp.setTransform(trans);

                
	        updateSimulationBody(Twist);
	       // System.out.println(1 + "");
	        update();        
	        //freeBody.updateTransform();
	        if(isDataCompleted()) {
	        	timer.stop();
	        	System.out.println("timer stopped");
	        	pauseSimulation();
	        	return;
	        }
	        
	        
	        
	        
	        return;            
	    }
		 private void init(int length,int dia) {
				index=0;
                                Torque=0.0;
                                Twist=0.0;
				Shaft_length=length;
				Shaft_dia=dia;
                                System.out.println("Shaft_length " + Shaft_length);
                                System.out.println("Shaft_dia " + Shaft_dia);
                                constant=Shaft_length*32/(Math.pow(Shaft_dia,4.0)*3.141*8*10000);
                                System.out.println("Constant = " + constant);
			}
	    private void update() {
	    	  
//	    	Torque=100*index;
	    	Twist=Torque*constant*180/3.141;
                Torque=Torque+1000;
	    	index=index+1000;
                
		}
	    

		private boolean isDataCompleted() {
			if (index>=1000000)
				return true;
			return false;
		}

		private void updateSimulationBody(double disp)
	    {	    	
	    	  float rad = (float)Math.PI/180;
                Vector<Transform3D> vtrans = new Vector<Transform3D>();
                Vector<TransformGroup> vtgp = new Vector<TransformGroup>();
                Transform3D rotx = new Transform3D();
                Transform3D roty = new Transform3D();
                Transform3D rotz = new Transform3D();

	    	  Transform3D trans1 = new Transform3D();vtrans.add(trans1);
                  Transform3D trans2 = new Transform3D();vtrans.add(trans2);
                  Transform3D trans3 = new Transform3D();vtrans.add(trans3);
                  Transform3D trans4 = new Transform3D();vtrans.add(trans4);
                  Transform3D trans5 = new Transform3D();vtrans.add(trans5);
                  Transform3D trans6 = new Transform3D();vtrans.add(trans6);
                  Transform3D trans7 = new Transform3D();vtrans.add(trans7);
                  Transform3D trans8 = new Transform3D();vtrans.add(trans8);
                  Transform3D trans9 = new Transform3D();vtrans.add(trans9);
                  Transform3D trans10 = new Transform3D();vtrans.add(trans10);
                  Transform3D trans11 = new Transform3D();vtrans.add(trans11);
                  Transform3D trans12 = new Transform3D();vtrans.add(trans12);
                  Transform3D trans13 = new Transform3D();vtrans.add(trans13);
                  Transform3D trans14 = new Transform3D();vtrans.add(trans14);
                  Transform3D trans15 = new Transform3D();vtrans.add(trans15);
                  Transform3D trans16 = new Transform3D();vtrans.add(trans16);
                  Transform3D trans17 = new Transform3D();vtrans.add(trans17);
                  Transform3D trans18 = new Transform3D();vtrans.add(trans18);
                  Transform3D trans19 = new Transform3D();vtrans.add(trans19);
                  Transform3D trans20 = new Transform3D();vtrans.add(trans20);
                  Transform3D trans21 = new Transform3D();vtrans.add(trans21);
                  
                  TransformGroup tgp1 = (TransformGroup)hm.get("cylinder0");vtgp.add(tgp1);
                  TransformGroup tgp2 = (TransformGroup)hm.get("cylinder1");vtgp.add(tgp2);
                  TransformGroup tgp3 = (TransformGroup)hm.get("cylinder2");vtgp.add(tgp3);
                  TransformGroup tgp4 = (TransformGroup)hm.get("cylinder3");vtgp.add(tgp4);
                  TransformGroup tgp5 = (TransformGroup)hm.get("cylinder4");vtgp.add(tgp5);
                  TransformGroup tgp6 = (TransformGroup)hm.get("cylinder5");vtgp.add(tgp6);
                  TransformGroup tgp7 = (TransformGroup)hm.get("cylinder6");vtgp.add(tgp7);
                  TransformGroup tgp8 = (TransformGroup)hm.get("cylinder7");vtgp.add(tgp8);
                  TransformGroup tgp9 = (TransformGroup)hm.get("cylinder8");vtgp.add(tgp9);
                  TransformGroup tgp10 = (TransformGroup)hm.get("cylinder9");vtgp.add(tgp10);
                  TransformGroup tgp11 = (TransformGroup)hm.get("cylinder10");vtgp.add(tgp11);
                  TransformGroup tgp12 = (TransformGroup)hm.get("cylinder11");vtgp.add(tgp12);
                  TransformGroup tgp13 = (TransformGroup)hm.get("cylinder12");vtgp.add(tgp13);
                  TransformGroup tgp14 = (TransformGroup)hm.get("cylinder13");vtgp.add(tgp14);
                  TransformGroup tgp15 = (TransformGroup)hm.get("cylinder14");vtgp.add(tgp15);
                  TransformGroup tgp16 = (TransformGroup)hm.get("cylinder15");vtgp.add(tgp16);
                  TransformGroup tgp17 = (TransformGroup)hm.get("cylinder16");vtgp.add(tgp17);
                  TransformGroup tgp18 = (TransformGroup)hm.get("cylinder17");vtgp.add(tgp18);
                  TransformGroup tgp19 = (TransformGroup)hm.get("cylinder18");vtgp.add(tgp19);
                  TransformGroup tgp20 = (TransformGroup)hm.get("cylinder19");vtgp.add(tgp20);
                  TransformGroup tgp21 = (TransformGroup)hm.get("handle");vtgp.add(tgp21);
          
                    float val = (float)disp;

                   //System.out.println(vtgp.get(20));
                                rotz.rotZ(-Math.toRadians(25));
                                rotx.rotX(-Math.toRadians(65));
                                rotx.mul(rotz);
            
//                           }
			  for(int i=0;i<vtrans.size()-2;i++){
                       
                              
                                vtrans.get(i).rotY(-Math.toRadians(val)*(float)(19-i)/10);
                     
                                vtrans.get(i).setTranslation(new Vector3d(0.0,-0.21,0.1));
                        
                                if(i>4 && i<17){
                                    if (i > 7 && i < 14) {
                                        vtrans.get(i).setScale(new Vector3d(1.0f * 1 - val / 10000, 1.0f, 1.0f * 1 - val / 10000));
                                    }
                                    else{
                                        vtrans.get(i).setScale(new Vector3d(1.0f * 1 - val*0.9 / 10000, 1.0f, 1.0f * 1 - val*0.9 / 10000));
                                    }
                                
                                }
                                vtgp.get(i).setTransform(vtrans.get(i));

                          }
                          vtrans.get(20).rotY(-Math.toRadians(val));
                          vtrans.get(20).setTranslation(new Vector3d(0.0,-0.21,0.1));
                          vtgp.get(20).setTransform(vtrans.get(20));
			  
	    }
	    
	    
	    private void pauseSimulation()
	    {
	    	
			timer.stop();
			ImageIcon icon = m_j3d.createImageIcon("resources/icons/start.png"); 
	    	startButton.setIcon(icon);
	    	startButton.setText("Start");
	    	reStartButton.setEnabled(true);
	        nextButton.setEnabled(true);
	    	rightPanel.setVisible(true);
			enableStage(stage);
			outputGraph.setState(0);
                        outputGraph1.setState(0);
		//	inputGraph.setState(0);
	        valChange = false;	         
			repaint();
	    }
	    
	}	    
	   


