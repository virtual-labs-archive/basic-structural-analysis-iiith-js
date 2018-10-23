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
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Group;
import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
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
	/**
	 * Simple Java 3D program that can be run as an application or as an applet.
	 */
	@SuppressWarnings({ "serial", "unused" })
        public class TorsionTest extends javax.swing.JPanel {
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
		private FullViewGraph  		fullViewGraph = new FullViewGraph();
		

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
               
		
		private boolean startStop = false;
		private boolean valChange = true;
		private String[] units ={" (mm) "," (mm) "};
		
//		private JComboBox ch;
//		private JComboBox che;
//		private JLabel lbl_k;
//	
		
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
		
			

			objRoot.addChild(m_j3d.createBox(new Vector3d(0,-0.45, -.1),new Vector3d(3,.01,1),new Vector3d(0,0,0),new Color3f(0f, 1f, 0f),"resources/images/table.jpg"));
			objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.4, -.6),new Vector3d(3,.9,.1),new Vector3d(0f, 0f,0f), new Color3f(0.5f,0.6f,0.72f)));
			
			
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
		    
		    Transform3D t1 = new Transform3D();
                    Transform3D t2 = new Transform3D();
		    t1.setTranslation(new Vector3d(0.0f,-0.16f,-0.1));
                    //t2.setTranslation(new Vector3d(0.0,0.15,-0.1));
		    TransformGroup cylinder1 = new TransformGroup(t1);
		    TransformGroup cylinder2 = new TransformGroup(t1);
                    TransformGroup cylinder3 = new TransformGroup(t1);
		    TransformGroup cylinder4 = new TransformGroup(t1);
                    TransformGroup cylinder0 = new TransformGroup(t1);
		    TransformGroup cylinder5 = new TransformGroup(t1);

                    TransformGroup BoxTop = new TransformGroup(t1);
                    TransformGroup BoxBottom = new TransformGroup(t1);
                    TransformGroup BoxSide1 = new TransformGroup(t1);
                    TransformGroup BoxSide2 = new TransformGroup(t1);
                    TransformGroup cone1 = new TransformGroup(t1);
                    TransformGroup cone2 = new TransformGroup(t1);

		    cylinder1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    cylinder1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
                    cylinder2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    cylinder2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
                    cylinder3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    cylinder3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
                    cylinder4.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    cylinder4.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

                    cone1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    cone1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
                    cone2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    cone2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
                    BoxTop.addChild(m_j3d.createTextureBox(new Vector3d(0,0.61f,0.35),new Vector3d(0.5,0.01,0.43),new Vector3d(0,0,0),new Color3f(255.0f/255.0f, 255.0f/255.0f, 255.0f/255.0f),"resources/images/aquamarine.jpg","base1",hm));
                    //BoxTop.addChild(m_j3d.createBoxWithMatProp(new Vector3d(0,0.61f,0.35), new Vector3d(0.5,0.01,0.43), new Vector3d(0,0,0), m_j3d.getColor3f(180,191,96),m_j3d.getColor3f(80,191,96),m_j3d.getColor3f(80,191,196),m_j3d.getColor3f(80,191,95), new Color3f(0.5f,0.4f,0.3f),50.0f));
                    BoxBottom.addChild(m_j3d.createTextureBox(new Vector3d(0,-0.1f,0.35),new Vector3d(0.509,0.01,0.43),new Vector3d(0,0,0),new Color3f(250.0f/255.0f, 255.0f/255.0f, 255.0f/255.0f),"resources/images/aquamarine.jpg","base1",hm));
                    //BoxBottom.addChild(m_j3d.createBoxWithMatProp(new Vector3d(0,-0.1f,0.35), new Vector3d(0.509,0.01,0.43), new Vector3d(0,0,0), m_j3d.getColor3f(180,191,96),m_j3d.getColor3f(80,191,96),m_j3d.getColor3f(80,191,196),m_j3d.getColor3f(80,191,95), new Color3f(0.5f,0.4f,0.3f),50.0f));
                    BoxSide1.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0.45,0.26f,0.25), new Vector3d(1.3,7.1,5.9), new Vector3d(0,0,0), m_j3d.getColor3f(67,121,124),m_j3d.getColor3f(67,121,124),m_j3d.getColor3f(67,121,124),m_j3d.getColor3f(67,121,124), new Color3f(67.0f/255.0f,121.0f/255.0f,124.0f/255.0f),40.0f));
                    BoxSide2.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(-0.45,0.26f,0.25), new Vector3d(1.3,7.1,5.9), new Vector3d(0,0,0),m_j3d.getColor3f(67,121,124),m_j3d.getColor3f(67,121,124),m_j3d.getColor3f(67,121,124),m_j3d.getColor3f(67,121,124),  new Color3f(67.0f/255.0f,121.0f/255.0f,124.0f/255.0f),40.0f));


                    cylinder0.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(0,-0.04f,0), new Vector3d(0.8,1.0,0.8),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.2f,0.2f,0.2f),30.0f));
		    hm.put("cylinder1",cylinder1);

		    cylinder1.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(0,0.08f,0), new Vector3d(1.5,1.5,1.5),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.3f,0.3f,0.3f),30.0f));
		    hm.put("cylinder1",cylinder1);
		  
		    cylinder2.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(0.0f,0.33f,0), new Vector3d(0.4,2.01,0.4),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(0,0,0),m_j3d.getColor3f(0,0,0),m_j3d.getColor3f(0,0,0),new Color3f(0.3f,0.3f,0.3f),30.0f));
		    hm.put("cylinder2",cylinder2);

                    cone1.addChild(m_j3d.createConeWithMatProp( new Vector3d(0.0f,0.21f,0), new Vector3d(0.4,0.51,0.4),new Vector3d(180,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(0,0,0),m_j3d.getColor3f(0,0,0),m_j3d.getColor3f(0,0,0),new Color3f(0.3f,0.3f,0.3f),30.0f));
		    hm.put("cone1",cone1);
                    cone2.addChild(m_j3d.createConeWithMatProp( new Vector3d(0.0f,0.25f,0), new Vector3d(0.4,0.51,0.4),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(0,0,0),m_j3d.getColor3f(0,0,0),m_j3d.getColor3f(0,0,0),new Color3f(0.3f,0.3f,0.3f),30.0f));
		    hm.put("cone2",cone2);

                    cylinder4.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(0.0f,0.13f,0), new Vector3d(0.4,2.01,0.4),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(0,0,0),m_j3d.getColor3f(0,0,0),m_j3d.getColor3f(0,0,0),new Color3f(0.3f,0.3f,0.3f),30.0f));
		    hm.put("cylinder4",cylinder4);

                    cylinder3.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(0,0.43f,0), new Vector3d(1.5,1.5,1.5),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.3f,0.3f,0.3f),30.0f));
		    hm.put("cylinder3",cylinder3);

                    cylinder5.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(0,0.55f,0), new Vector3d(0.8,1.0,0.8),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.2f,0.2f,0.2f),30.0f));
		    hm.put("cylinder3",cylinder3);

                    objtrans.addChild(cylinder0);
                    objtrans.addChild(cylinder1);
                    objtrans.addChild(cylinder2);
                    objtrans.addChild(cone1);
                    objtrans.addChild(cone2);
                    objtrans.addChild(cylinder4);
                    objtrans.addChild(cylinder3);
                    objtrans.addChild(cylinder5);

                    objtrans.addChild(BoxTop);
                    objtrans.addChild(BoxBottom);
                    objtrans.addChild(BoxSide1);
                    objtrans.addChild(BoxSide2);

		    //objtrans.addChild(m_j3d.createTextureBox(new Vector3d(0,0.15f,0.0),new Vector3d(.5,.001,0.23),new Vector3d(0,0,0),new Color3f(250.0f/255.0f, 247.0f/255.0f, 11.0f/255.0f),"resources/images/tile1.jpg","base1",hm));
		    //objtrans.addChild(m_j3d.createTextureBox(new Vector3d(0,0.25f,0.0),new Vector3d(.5,.001,0.23),new Vector3d(0,0,0),new Color3f(250.0f/255.0f, 246.0f/255.0f, 10.0f/255.0f),"resources/images/tile1.jpg","base2",hm));
		    hm.put("base",objtrans);
		    
		    return objtrans;
		}
	    
	     
	     
	   
	     
	     
	    
	    /**
	     * Creates new form FreeVibration
	     */
	    public TorsionTest(Container container) {
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
	        TorsionTest mainPanel;

	        public void init() {
	            setLayout(new BorderLayout());
	            mainPanel = new TorsionTest(this);
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
	            getContentPane().add(new TorsionTest(this), BorderLayout.CENTER);
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
	                inputGraph.clearGraphValue();
	                
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
	        
	       
	        javax.swing.JButton btn= new javax.swing.JButton("Full View Graph");
	        guiPanel.add(btn, gridBagConstraints);
	        icon = m_j3d.createImageIcon("resources/icons/graph_window.png");        
	        btn.setIcon(icon);
	        btn.addActionListener(new java.awt.event.ActionListener() {
	            @SuppressWarnings("static-access")
				public void actionPerformed(java.awt.event.ActionEvent evt) {                
	 
	            	HorizontalGraph graph[] ={outputGraph};
	             	int max[]={1000,100};
	             	int magX[]={2,2};
	             	int magY[]={2,2};
	            	
	            	JFrame frame = new JFrame("Full View Graph");
	            	//GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
	                //Add contents to the window.
	            	
	                //frame.add(p);
	            	frame.setExtendedState(frame.getExtendedState() | frame.MAXIMIZED_BOTH);

	                
	                frame.pack();
	                frame.setVisible(true);
	              
	                
	                fullViewGraph =new FullViewGraph(graph,max,magX,magY,frame.getWidth()-20, frame.getHeight());
	                 
	                frame.add(fullViewGraph);
	                System.out.println("w " + frame.getWidth() + " h " + frame.getHeight());
	                
	                
	            }
	          });
	        
	      
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
	       
	        outputGraph = new HorizontalGraph(300,300,"t ","u(t)"); 
	        outputGraph.setHeading("Stress Strain Curve ");
	        outputGraph.setAxisUnit("sec","m");
	        outputGraph.setYAxisColor(Color.BLUE);
	        outputGraph.setYScale(1);
	        outputGraph.fitToYwindow(true);        
	   
	        rightPanel.add(outputGraph);
                
                
                outputGraph1 = new PointLineGraph(300,300,"t ","u(t)"); 
	        outputGraph1.setHeading("Stress Strain Curve ");
	   //   outputGraph1.setAxisUnit("sec","m");
	        outputGraph1.setYAxisColor(Color.RED);
	        outputGraph1.setScale(1);
//	        outputGraph1.fitToYwindow(true);        
	   
	        rightPanel.add(outputGraph1);
	                
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
	         
	     
	    
	         
	         m_Objective = new JLabel(">:Observe the elongation of the speciman.", JLabel.LEFT);
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

	         javax.swing.JButton viewButton= new javax.swing.JButton("Horizontal View");
	         ImageIcon icon = m_j3d.createImageIcon("resources/icons/h-view.png"); 
	         viewButton.setIcon(icon);
	         viewButton.addActionListener(new java.awt.event.ActionListener() {
	             public void actionPerformed(java.awt.event.ActionEvent evt) {                
	             	
	            	 //  0 , 
	            	 if(m_cameraTimer!=null && m_cameraTimer.isRunning()){ m_cameraTimer.stop();}
	            	 setCameraViews();
	            	 m_cameraTimer = new Timer(200,new ActionListener() {
	                     public void actionPerformed(ActionEvent evt) {
	                         //...Perform a task...
	                    	 timerActionHorizontalCameraMotion(evt);
	                     }
	                 });
	            	 m_cameraTimer.start();
	             }
	           });

	         
	         guiPanel.add(viewButton, gridBagConstraints);
	          
	         viewButton= new javax.swing.JButton("Vertical View");
	         icon = m_j3d.createImageIcon("resources/icons/v-view.png");
	         viewButton.setIcon(icon);
	         viewButton.addActionListener(new java.awt.event.ActionListener() {
	             public void actionPerformed(java.awt.event.ActionEvent evt) {                
	             	
	            	 if(m_cameraTimer!=null && m_cameraTimer.isRunning()){ m_cameraTimer.stop();}
	            	 setCameraViews();
	            	 m_cameraTimer = new Timer(200,new ActionListener() {
	                     public void actionPerformed(ActionEvent evt) {
	                                	 timerActionVerticalCameraMotion(evt);
	                     }
	                 });
	            	 m_cameraTimer.start();
	            	 
	             }
	           });
	         
	         guiPanel.add(viewButton, gridBagConstraints);
	         
	         JCheckBox chkbox = new JCheckBox("");
	         lbl = new JLabel("Change Input Parameters", JLabel.CENTER);
	         icon = m_j3d.createImageIcon("resources/icons/tasklist.png");        
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
	           
	           in1 = new JPanel(new java.awt.GridLayout(3,2));
	           in1.setBackground(bk);
	           bottomPanel.add(in1);

	           in2 = new JPanel(new java.awt.GridLayout(3,2)); 
	           in2.setBackground(bk);

	           bottomPanel.add(in2);

	           JLabel lab = new JLabel("INPUTS", JLabel.CENTER);
	           in1.add(lab);
	           lab=new JLabel("-", JLabel.CENTER);
	           in1.add(lab);
	           
	           
	           lab=new JLabel("Length of Shaft", JLabel.LEFT);
	           m_Slider[0] = new JSlider(JSlider.HORIZONTAL,1, 10, 3);
	           m_Slider[0].addChangeListener(new ChangeListener() {
	   		    public void stateChanged(ChangeEvent e) {
	               valChange = true;
	               int val = ((JSlider) e.getSource()).getValue();
	               fields[0]=val*40;
	               iLabel[0].setText(":: " + fields[0]*40 + " m");
	               
	               repaint();            
	            }
	            });
	           m_Slider[0].setBackground(bk);
	           in1.add(lab);
	           in1.add(m_Slider[0]);
	           lab=new JLabel("Length of Shaft", JLabel.LEFT);
	           m_Slider[0] = new JSlider(JSlider.HORIZONTAL,1, 10, 3);
	           m_Slider[0].addChangeListener(new ChangeListener() {
	   		    public void stateChanged(ChangeEvent e) {
	               valChange = true;
	               int val = ((JSlider) e.getSource()).getValue();
	               fields[0]=val*40;
	               iLabel[0].setText(":: " + fields[0]*40 + " m");
	               
	               repaint();            
	            }
	            });
	           m_Slider[0].setBackground(bk);
	           in1.add(lab);
	           in1.add(m_Slider[0]);
	         
	    }
	    
	    private void setField(JLabel lbl, float v, int f){
	    	fields[f]= v;
	    	lbl.setText(" :: " + Float.toString(fields[f]) + units[f]);
	    }
	    
	    
	    private void initInputControlsField(){
	    	iLabel = new JLabel[2];
	    	int i=0;
	    	
	    	iLabel[i++] = new JLabel("3 (m)", JLabel.LEFT); 
	    	iLabel[i-1].setForeground(Color.BLUE);
	    	
	    	
	    	
	
	    
	    	
	    	i=0;
	    	fields = new float[2];
	    	fields[i++] = 3;
	    	fields[i++] = 3;
	   
	        
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

	    	}
	    	
	    }
	    
	    private void setInstructionText()
	    {
	    	    	
	    	valChange = true;   	
	    	resetOutputParameters(); 
	    	
	    	
	    	switch(stage){
	    	case 0: // Home 
	    		m_Objective.setText(">:  Observe the elongation of the speciman.");
	    		m_Objective.setForeground(Color.WHITE);
	    					break;
	    	case 1:
	    		m_Objective.setText(">: Observe the responce of structure with different structural Behaviour.");
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
		   
		
		    float y = (float)(float)Math.sin(Math.toRadians(m_cameraViews[m_cameraEye]));
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
		    
		
		    float x = (float)(float)Math.sin(Math.toRadians(m_cameraViews[m_cameraEye]));
		    float z = 2.41f - Math.abs(x);
		
		    t3d.lookAt( new Point3d(x, 0,z), new Point3d(0,0,0), new Vector3d(0,1,0));
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
	      //  inputGraph.setState(1);
	      

	        
	        if(valChange){

		        init(120,20);
		        outputGraph.clearGraphValue();
		       // inputGraph.clearGraphValue();
	        }
	        
	        timer.start();
	        System.out.println("Timer started");     
	    }
	    
	   
	    
	 
	   

		private void timerActionPerformed(java.awt.event.ActionEvent evt)
	    {
                 int i=0;   

	    
	       
	    	    	
	    
	           
	        /////////// Text
	        
	       
	        

	        ////////////// Graph /////////
	        
	    
	     
	      
	   
	        
	        if(rightPanel.isVisible())
	        {
	        	
                        outputGraph1.drawGraph();
	      
	        }
	                
	        fullViewGraph.updateGraph(new float[]{});
	        fullViewGraph.drawGraph();
	        

	    
	        //updating base movement  
	        TransformGroup tgp = (TransformGroup)hm.get("base");
	        Transform3D trans = new Transform3D();
	    	tgp.getTransform(trans);
	   //     trans.setTranslation(new Vector3d(eqd/100,0,0)); // intensionally removed this line
	        tgp.setTransform(trans);
	        
	        updateSimulationBody(1);
	        System.out.println(1 + "");
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
				
				length=Shaft_length;
				dia=Shaft_dia;
			}
	    private void update() {
	    	  
	    	Torque=100*index;
	    	Twist=Torque*Shaft_length*32/(8000*3.141*Math.pow(Shaft_dia, 4.0))*180/3.141;
	    	index=index+100;
		}

		private boolean isDataCompleted() {
			if (index==100000)
				return true;
			return false;
		}

		private void updateSimulationBody(double disp)
	    {
	    	
	    	  float rad = (float)Math.PI/180;
                  double diff = 0.0;
                  double factor1 = 0.0,factor2 = 0.0;
                 //add factors for new data over here
//                  if(type_of_force.getSelectedItem().equals("ASTM_A53")){
//                        factor1 = 0.012;
//                        factor2 = 0.005;
//                  }
//                  else if(type_of_force.getSelectedItem().equals("Corten_Steel")){
//                        factor1 = 0.025;
//                        factor2 = 0.005;
//                  }
//                  else if(type_of_force.getSelectedItem().equals("Mild_Steel")){
//                        factor1 = 0.030;
//                        factor2 = 0.005;
//                  }
//                   else if(type_of_force.getSelectedItem().equals("Carbon_Steel")){
//                        factor1 = 0.030;
//                        factor2 = 0.005;
//                  }
//                   else if(type_of_force.getSelectedItem().equals("Copper")){
//                        factor1 = 0.030;
//                        factor2 = 0.005;
//                  }
	    	  Transform3D trans1 = new Transform3D();
                  Transform3D trans2 = new Transform3D();
                  Transform3D trans3 = new Transform3D();
                  Transform3D trans4 = new Transform3D();
                  Transform3D trans5 = new Transform3D();
                  Transform3D trans6 = new Transform3D();
                  TransformGroup tgp1 = (TransformGroup)hm.get("cylinder1");
                  TransformGroup tgp2 = (TransformGroup)hm.get("cylinder2");
                  TransformGroup tgp5 = (TransformGroup)hm.get("cone1");
                  TransformGroup tgp6 = (TransformGroup)hm.get("cone2");
                  TransformGroup tgp4 = (TransformGroup)hm.get("cylinder4");
	    	  TransformGroup tgp3 = (TransformGroup)hm.get("cylinder3");
	    	  tgp1.getTransform(trans1);
                  tgp2.getTransform(trans2);
                  tgp5.getTransform(trans5);
                  tgp6.getTransform(trans6);
                  tgp4.getTransform(trans4);
                  tgp3.getTransform(trans3);
	    	  Vector3d s2 = new Vector3d();
                  Vector3d s5 = new Vector3d();
                  Vector3d s6 = new Vector3d();
                  Vector3d s4 = new Vector3d();
                  Vector3d s1 = new Vector3d();
                  //Vector3d m = new Vector3d(1.0f,1.0f,1.0f);
			  trans2.getScale(s2);
                          trans5.getScale(s5);
                          trans6.getScale(s6);
                          trans4.getScale(s4);
                          trans4.get(s1);
                          System.out.println(s1);
                          System.out.println(s4);
                          if(disp<28.0){
                            s2.setX(1.0-disp*.02);
                            s4.setX(1.0-disp*.02);

                            s5.setX(1.0-disp*.02);
                            s6.setX(1.0-disp*.02);
                            s5.setY((1.0+disp*factor1));
                            s6.setY((1.0-disp*factor2));

                            s2.setY((1.0+disp*factor1));
                            s4.setY((1.0-disp*factor2));
                          }
                          diff = ((1.0+disp*007)-s2.getY())*0.0005;
			  float val = (float)disp*200;
			  //trans.rotZ(rad*val);
			  trans2.setScale(s2);
                          trans5.setScale(s5);
                          trans6.setScale(s6);
                          trans4.setScale(s4);
			 // trans.setTranslation(new Vector3d(0.0,0.0,0.0));
                          //System.out.println(diff+"hello");
                          trans1.setTranslation(new Vector3d(0.0,-0.15-diff,-0.1));
                          trans3.setTranslation(new Vector3d(0.0,-0.15+diff,-0.1));
			  tgp1.setTransform(trans1);
                          tgp2.setTransform(trans2);
                          tgp5.setTransform(trans5);
                          tgp6.setTransform(trans6);
                          tgp4.setTransform(trans4);
                          tgp3.setTransform(trans3);
                          //System.out.println(type_of_force.getSelectedItem()+ "HELLO_HELLO");
			  
			  
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
	   

//	
//	class TorsionTestBody  {
//		ArrayList<Double> inp1=new ArrayList<Double>();
//		ArrayList<Double> inp2=new ArrayList<Double>();
//		ArrayList<Double> inp3=new ArrayList<Double>();
//		ArrayList<Double> inp4=new ArrayList<Double>();
//		ArrayList<Double> inp5=new ArrayList<Double>();
//		double[] in1,in2,in3,in4,in5;
//	
//
//	    double data,data1,data2,data3,data4;
//	    int inc;
//	    int length=0;
//	    String df;
//		
//		void init(String datafile)
//		{
//                    inc=0;
//			inp1.clear();
//                        inp2.clear();
//                        inp3.clear();
//                        inp4.clear();
//                        inp5.clear();
//                        
//                        
//                        
//			 df=datafile;
//			 
//			 String data_file = "resources/Data/" + datafile + "/data.txt";
//		   
//		      URL url = Resources.getResource(data_file);
//		      try
//		      {    	
//		    	
//			    	InputStream in = url.openStream();
//					BufferedReader bf = new BufferedReader(new InputStreamReader(in));
//					
//			    	
//					System.out.println("Reading File" + datafile); 
//			      	String str;
//		      	
//					while ((str = bf.readLine()) != null)
//			  	    {
//			  	        String delimit = "\t";
//			  	        String[] tokens = str.split(delimit);
//			  	        if(tokens.length != 0)
//			  	        {
//			  	        	 data = Double.parseDouble(tokens[0].trim());
//			  			     inp1.add(data);
//			  			     System.out.println("data is  :" + data);
//			 				 data1 = Double.parseDouble(tokens[1].trim());	
//			  				 inp2.add(data1);
//			  				 data2 = Double.parseDouble(tokens[2].trim());	
//			  				 inp3.add(data2);
//			  				 data3 = Double.parseDouble(tokens[3].trim());	
//			  				 inp4.add(data3);
//			  				 data4 = Double.parseDouble(tokens[4].trim());	
//			  				 inp5.add(data4);
//			  	        }  	   
//			  	    }
//					length=inp4.size();
//                                        System.out.println("length :" + length);
//			  	}
//			  	catch (Exception e2)
//			  	{
//			  		System.out.println("Some Error in opening file " + e2); 
//			  	}
//			  
//			  	in1=new double[length];
//		  				
//		}
//	
//		
//		public double getStress()
//		{
//                    System.out.println("inp4 =  " + inp4.size());
//                    System.out.println(inp4.get(inc));
//			return inp4.get(inc);
//		}
//		public double getStrain()
//		{
//			return inp3.get(inc);
//		}
//		public double getLoad()
//		{
//			return inp2.get(inc);
//		}
//		public double getElong()
//		{
//			return inp1.get(inc);
//		}
//		public double getYoung()
//		{
//			return inp5.get(inc);
//		}
//		 public void update()
//		    {
//		    	inc++;		    	
//		    }
//		 boolean isDataCompleted()
//			{
//				int index=inc;
//			
//				if(index >=  inp4.size())
//					return true;
//				
//				return false;
//			}
//               
//	}

