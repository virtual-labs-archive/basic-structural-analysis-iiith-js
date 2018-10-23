package structuralanalysis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Formatter;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Group;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Switch;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
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

import eerc.vlab.common.FullViewGraph;
import eerc.vlab.common.HelpWindow;
import eerc.vlab.common.HorizontalGraph;
import eerc.vlab.common.HorizontalGraphWrapper;
import eerc.vlab.common.ImagePanel;
import eerc.vlab.common.J3DShape;
import eerc.vlab.common.Resources;

import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;


public class DefBeams extends javax.swing.JPanel {
	//  Variables declaration - do not modify//GEN-BEGIN:variables
	//////////////////////////GUI componenet ///////////////////////////
	private javax.swing.JPanel topPanel;
	private javax.swing.JPanel simulationPanel;
	private javax.swing.JPanel bottomPanel;
	private javax.swing.JPanel rightPanel;
	  
	private javax.swing.JPanel in1;			// Input panel 1
	private javax.swing.JPanel in2;			// Input panel 2
	private javax.swing.JPanel in3;			// Input panel 3
	
	
//	private javax.swing.JPanel rp1;			// Right Input panel 1
//	private javax.swing.JPanel rp2;			// Right Input panel 2
//	private javax.swing.JPanel ImagePanel;
	
	private javax.swing.JButton startButton=null;
	private javax.swing.JButton reStartButton=null;
	private javax.swing.JButton nextButton=null;
	
	

	private JSlider m_Slider[] = new JSlider[3];
	
	private JComboBox type_of_support;
	JComboBox cementGradeList;
	String obj_f;
	double constant;                   // for have to get from E and I values 
	int val=20;
	double load,defelection;
	String objImg;
	private javax.swing.JButton rightIcon=null;

	//private GraphPlotter         graphPlotter;
	////////////////////////////Java3D componenet ///////////////////////////

	private SimpleUniverse      univ = null;                  // Simple Universe Java3D
	private BranchGroup         scene = null;                 // BranchGroup Scene graph
//	private Switch 				switchGroup=null;			 //
	



	private HorizontalGraph		outputGraph =null;



	@SuppressWarnings("unchecked")
	private HashMap 			hm = new HashMap();
	private J3DShape 			m_j3d	= new J3DShape();

	private float fields[];
	private JLabel outlbl_val[];
	private JLabel iLabel[];
	private JLabel m_Objective= new JLabel("Objective:");
	
	private Timer timer=null;
	private Timer m_cameraTimer=null; 
	private float m_cameraViews[];
	private int m_cameraEye;
	// Timer for simulation    
	
	private int stage = 0;	
	
	private boolean startStop = false;
	private boolean valChange = true;
	
				
	
	public BranchGroup createSceneGraph() 
	{
		// Create the root of the branch graph
		BranchGroup objRoot = new BranchGroup();
		objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND );
		objRoot.setCapability(Group.ALLOW_CHILDREN_READ);
		objRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
		objRoot.setCapability( BranchGroup.ALLOW_DETACH );
		
		
		//btg = (TransformGroup)createVirtualLab();
		objRoot.addChild(createVirtualLab());
		//objRoot.addChild(createBuilding());
		//objRoot.addChild(createLeg());
		//objRoot.addChild(createAllText());
		//objRoot.addChild(createIonsSwitchGroup());
		
//		 Ground
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,-0.25, -.1),new Vector3d(3,.01,1),new Vector3d(0,0,0),new Color3f(0f, 1f, 0f),"resources/images/table.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.4, -.6),new Vector3d(3,.9,.1),new Vector3d(0f, 0f,0f), new Color3f(0.5f,0.6f,0.72f)));
		//objRoot.addChild(m_j3d.createTextureBox("resources/images/table.jpg",new Vector3d(0,-0.2, 0.2),new Vector3d(2,.03,.01), new Vector3d(0,0,0),new Color3f(0f, 1f, 0f)));
		
		//objRoot.addChild(createTextureBox("resources/images/bg.jpg",new Vector3d(0,0.4, -.2),new Vector3d(1.2,.6,.1), new Color3f(1f, 1f, 1f)));
		
		//objRoot.addChild(createTextureBox("resources/images/brick.jpg",new Vector3d(0,0.1, 0),new Vector3d(0.4,.29,.01), new Color3f(1f, 1f, 1f)));
		
		float rad = (float)Math.PI/180;
		Transform3D t = new Transform3D();
		t.setScale(0.4);
		
		TransformGroup tg = new TransformGroup();
		t = new Transform3D();
		t.rotX(rad*10);
		t.setScale(new Vector3d(.5f,.05f,.5f));        
		t.setTranslation(new Vector3d(.3,.3,0));
		tg.setTransform(t);
	//	freeBody = new DefBeamsBody();    
//		freeBody = new DefBeamsBody(new Vector3d(.3,.3,0),tg);    
		

		
	    
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

    
    
        
	private Group createVirtualLab() {
	   
	    Transform3D t = new Transform3D();
        //t.setTranslation(new Vector3d(0,.1,.0));
                    
	    TransformGroup objtrans = new TransformGroup(t);
	    objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    //tile1
	    //brick
	    //table
	    //objtrans.addChild(m_j3d.createTextureBox("resources/images/tile1.jpg",new Vector3d(0,0.4, 0.2),new Vector3d(0.5,.04,.01), new Vector3d(0,0,0),new Color3f(1f, 1f, 1f)));
	    //objtrans.addChild(loadObjectFile("resources/geometry/sixDoorFrenchStyle.obj",new Vector3d(0,0.17, 0.3),new Vector3d(.15,.25,.5), new Vector3d(0,0,0),new Color3f(0f, 1f, 0f)));
	    //objtrans.addChild(createTextureBox("resources/images/brick.jpg",new Vector3d(0,0.1, 0),new Vector3d(0.4,.29,.01), new Color3f(1f, 1f, 1f)));
	    
	    
	    //Block Left
	    //objtrans.addChild(m_j3d.createTextureBox("resources/images/tile1.jpg",new Vector3d(-.68,0.17, -.1),new Vector3d(.2,.02,.3),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"roof1",hm));
	    ///objtrans.addChild(m_j3d.createTextureCube(new Vector3d(-.7,-0.05, -.1),new Vector3d(.3,.4,.3), new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/brick2.jpeg","block1",hm));
	    //Block Center
	    objtrans.addChild(m_j3d.createTextureBox(new Vector3d(0,0.17, -.1),new Vector3d(.2,.02,.3),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/tile1.jpg","roof1",hm));
	    objtrans.addChild(m_j3d.createTextureCube(new Vector3d(0,-0.05, -.1),new Vector3d(.3,.4,.3), new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/brick2.jpeg","block1",hm));
	    //Block Right
	    //objtrans.addChild(m_j3d.createTextureBox("resources/images/tile1.jpg",new Vector3d(.68,0.17, -.1),new Vector3d(.2,.02,.3),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"roof3",hm));
	    //objtrans.addChild(m_j3d.createTextureCube(new Vector3d(.7,-0.05, -.1),new Vector3d(.3,.4,.3), new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/brick2.jpeg","block3",hm));
	    	             
	  
	    return objtrans;
	}
    
     
     
   
     
     
    
    /**
     * Creates new form FreeVibration
     */
    public DefBeams(Container container) {
        // Initialize the GUI components
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        initComponents();

        centerPanel(container);
        // Create Canvas3D and SimpleUniverse; add canvas to drawing panel
        
//        scene.addChild(bgleg);
    }

    
    // ----------------------------------------------------------------
    
    // Applet framework

    public static class MyApplet extends JApplet {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		DefBeams mainPanel;

        public void init() {
            setLayout(new BorderLayout());
            mainPanel = new DefBeams(this);
            add(mainPanel, BorderLayout.CENTER);
                        
        }

        public void destroy() {
            mainPanel.destroy();
        }
    }

    // Application framework

    private static class MyFrame extends JFrame {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		MyFrame() {
            setLayout(new BorderLayout());
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setTitle("Defelection of Beams Body Applet");
            getContentPane().add(new DefBeams(this), BorderLayout.CENTER);
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
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        
        
//      new GridLayout(2, 1)
        setLayout(new java.awt.BorderLayout());
        
        bottomPanel = new javax.swing.JPanel(); 	// input from user at bottom
        simulationPanel = new javax.swing.JPanel(); // 3D rendering at center
        topPanel= new javax.swing.JPanel();    		// Pause, resume, Next
        rightPanel = new javax.swing.JPanel();    	// Graph and Input and Output Parameter
                
         
        topPanel();                 
        bottomPanel();        
        rightPanel();
        
//      Set Alignment
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
        
                            

        
    }// </editor-fold>//GEN-END:initComponents

    private void topPanel() {
            
        java.awt.GridBagConstraints gridBagConstraints;
            
        javax.swing.JPanel guiPanel = new javax.swing.JPanel(); // Pause, resume at top
        guiPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);  
 
        reStartButton = new javax.swing.JButton("Re-Start");
        ImageIcon icon = m_j3d.createImageIcon("resources/icons/restart.png"); 
        reStartButton.setIcon(icon);
        startButton = new javax.swing.JButton("Start");
        icon = m_j3d.createImageIcon("resources/icons/start.png"); 
        startButton.setIcon(icon);
        nextButton = new javax.swing.JButton("Next");
        icon = m_j3d.createImageIcon("resources/icons/next.png");        
        nextButton.setIcon(icon);

        
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
            //    inputGraph.clearGraphValue();
                
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
        
//       
//        javax.swing.JButton btn= new javax.swing.JButton("Full View Graph");
//        guiPanel.add(btn, gridBagConstraints);
//        icon = m_j3d.createImageIcon("resources/icons/graph_window.png");        
//        btn.setIcon(icon);
//        btn.addActionListener(new java.awt.event.ActionListener() {
//            @SuppressWarnings("static-access")
//			public void actionPerformed(java.awt.event.ActionEvent evt) {                
// 
//            	HorizontalGraph graph[] ={outputGraph};
//             	int max[]={1000,100};
//             	int magX[]={2,2};
//             	int magY[]={2,2};
//            	
//            	JFrame frame = new JFrame("Full View Graph");
//            	
//            	frame.setExtendedState(frame.getExtendedState() | frame.MAXIMIZED_BOTH);
//
//              
//                frame.pack();
//                frame.setVisible(true);
//       
//                
//                fullViewGraph =new FullViewGraph(graph,max,magX,magY,frame.getWidth()-20, frame.getHeight());
//                frame.add(fullViewGraph);   //Pradeep: added
//           //     System.out.println("w " + frame.getWidth() + " h " + frame.getHeight());
//                
//                
//            }
//          });
//        
      
      guiPanel.add(reStartButton, gridBagConstraints);
      guiPanel.add(startButton, gridBagConstraints);
      guiPanel.add(nextButton, gridBagConstraints);
        
      javax.swing.JButton btn= new javax.swing.JButton("Manual"); 
      icon = m_j3d.createImageIcon("resources/icons/manual.png");        
      btn.setIcon(icon);
      //startButton.setPreferredSize(new Dimension(100,30));
   //     guiPanel.add(btn, gridBagConstraints);
        btn.setVisible(false);                  //Pradeep: they said to remove Manual
        
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {                
            	
                HelpWindow.createAndShowGUI("forcedVib");
            }
          });

        
        
        
    }
    
    
    private void rightPanel() {
        
     
        rightPanel.setLayout(new java.awt.BorderLayout());
  
        JPanel panel = new JPanel();
        panel.setBackground(new Color(140,200,240));
      
        ImageIcon icon = m_j3d.createImageIcon("resources/Data/DefBeams/stepforce1.jpg"); 
      
       
        rightIcon=new javax.swing.JButton(" ");
        rightIcon.setIcon(icon);
        panel.add(rightIcon);
        
        panel.setPreferredSize(new Dimension(300,175));
    //    rightTop.setBackground(new Color(200,200,100));
        rightPanel.add(panel,BorderLayout.NORTH);

        JPanel rightBottom = new JPanel();
        rightBottom.setPreferredSize(new Dimension(300, 295));
  //      rightBottom.setBackground(new Color(100,200,100));
       
        
        
        
        outputGraph = new HorizontalGraph(300,280,"N","mm"); 
        outputGraph.setHeading("load vs Deflection ");
        outputGraph.setAxisUnit("N","mm");
        outputGraph.setYAxisColor(Color.BLUE);
        outputGraph.setYScale(100);
        outputGraph.fitToYwindow(true);        
        HorizontalGraphWrapper wrapper = new HorizontalGraphWrapper(outputGraph,10,2,Color.GRAY);
        
        rightBottom.add(wrapper);     //added wrapper instead of outputGraph
        
        rightPanel.add(rightBottom,BorderLayout.SOUTH);
        
       
        // Can't call draw graph here as Graphics object is not initialize
                
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
         JLabel lbl = new JLabel("DefBeams Responce of SDOF ", JLabel.CENTER);
         lbl.setFont(new Font("Arial", Font.BOLD, 18));

         lbl.setForeground(Color.orange);
         //lbl.setBackground(Color.BLACK);
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
         
     
  
         
         m_Objective = new JLabel(">: Start the experiment and observe the Displacement Response with respect to Time.", JLabel.LEFT);
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

//         javax.swing.JButton viewButton= new javax.swing.JButton("Horizontal View");
//         ImageIcon icon = m_j3d.createImageIcon("resources/icons/h-view.png"); 
//         viewButton.setIcon(icon);
//         viewButton.addActionListener(new java.awt.event.ActionListener() {
//             public void actionPerformed(java.awt.event.ActionEvent evt) {                
//             	
//            	 //  0 , 
//            	 if(m_cameraTimer!=null && m_cameraTimer.isRunning()){ m_cameraTimer.stop();}
//            	 setCameraViews();
//            	 m_cameraTimer = new Timer(200,new ActionListener() {
//                     public void actionPerformed(ActionEvent evt) {
//                         //...Perform a task...
//                    	 timerActionHorizontalCameraMotion(evt);
//                     }
//                 });
//            	 m_cameraTimer.start();
//             }
//           });
//
//         
//         guiPanel.add(viewButton, gridBagConstraints);
//          
//         viewButton= new javax.swing.JButton("Vertical View");
//         icon = m_j3d.createImageIcon("resources/icons/v-view.png");
//         viewButton.setIcon(icon);
//         viewButton.addActionListener(new java.awt.event.ActionListener() {
//             public void actionPerformed(java.awt.event.ActionEvent evt) {                
//             	
//            	 if(m_cameraTimer!=null && m_cameraTimer.isRunning()){ m_cameraTimer.stop();}
//            	 setCameraViews();
//            	 m_cameraTimer = new Timer(200,new ActionListener() {
//                     public void actionPerformed(ActionEvent evt) {
//                         //...Perform a task...
//                    	 timerActionVerticalCameraMotion(evt);
//                     }
//                 });
//            	 m_cameraTimer.start();
//            	 
//             }
//           });
         
        // guiPanel.add(viewButton, gridBagConstraints);
         
         JCheckBox chkbox = new JCheckBox("");
         lbl = new JLabel("Change Input Parameters", JLabel.CENTER);
         //lbl.setFont(new Font("Arial", Font.BOLD, 18));
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
                             // (a) checkbox.isSelected();
                             // (b) ((JCheckBox)event.getSource()).isSelected();
                            
             }
         });

         guiPanel.add(chkbox, gridBagConstraints);
         guiPanel.add(lbl, gridBagConstraints);
         
         chkbox = new JCheckBox("");
         lbl = new JLabel("Show Graphs", JLabel.CENTER);
         //lbl.setFont(new Font("Arial", Font.BOLD, 18));
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
        // guiPanel.add(createInputOutputPanel());
        // btmPanel.add(guiPanel,BorderLayout.SOUTH);
         
        
         

    }
    
    
    private JPanel createInputOutputPanel(){
    	
    	JPanel ioparm = new JPanel(new java.awt.GridLayout(1,0));
//    	//ioparm.setPreferredSize(new java.awt.Dimension(600, 100));
//    	ioparm.setBackground(new Color(67,143,205));
//    	JPanel parm = new JPanel(new java.awt.GridLayout(4,2,0,10)); 
//    	parm.setBackground(new Color(130,169,193));
//    	outlbl_val = new JLabel[4];// new  = 
//    	
//    	int i=0;
//    	JLabel lbl = new JLabel("Input  ", JLabel.RIGHT);   parm.add(lbl); lbl.setForeground(Color.yellow);
//    	lbl = new JLabel("Parameters", JLabel.LEFT);    	parm.add(lbl); lbl.setForeground(Color.yellow);
//    	lbl = new JLabel("Mass", JLabel.LEFT);	    		parm.add(lbl);
//    	outlbl_val[i] = new JLabel(getMass() + " Kg", JLabel.LEFT);   outlbl_val[i].setForeground(Color.white); parm.add(outlbl_val[i++]);
//    	lbl = new JLabel("Stiffness", JLabel.LEFT);	    	parm.add(lbl);
//    	outlbl_val[i] = new JLabel((float)getStiff() + " N/m", JLabel.LEFT);   outlbl_val[i].setForeground(Color.white); parm.add(outlbl_val[i++]);
//    	
//    	ioparm.add(parm);
//    	
//    	
//    	parm = new JPanel(new java.awt.GridLayout(4,2,0,10)); //additionally added
//    	parm.setBackground(new Color(130,169,193));
//    	lbl = new JLabel("            ", JLabel.RIGHT);   parm.add(lbl); lbl.setForeground(Color.yellow);
//    	lbl = new JLabel("            ", JLabel.RIGHT);    	parm.add(lbl); lbl.setForeground(Color.yellow);    
//    	
//    	lbl = new JLabel("            ", JLabel.RIGHT);					parm.add(lbl); 
//    	lbl = new JLabel("            ", JLabel.RIGHT);					parm.add(lbl); 
//    	lbl = new JLabel("            ", JLabel.RIGHT);					parm.add(lbl); 
//    	lbl = new JLabel("            ", JLabel.RIGHT);					parm.add(lbl); 
//    
//    	ioparm.add(parm);
//    	
//    	
//    	
//    	
//    	parm = new JPanel(new java.awt.GridLayout(4,2,20,0)); 
//    	parm.setBackground(new Color(130,169,193));
//    	lbl = new JLabel("Output  ", JLabel.RIGHT);   parm.add(lbl); lbl.setForeground(Color.yellow);
//    	lbl = new JLabel("Parameters", JLabel.LEFT);    	parm.add(lbl); lbl.setForeground(Color.yellow);
//         
//    	
//        
//    	lbl = new JLabel("Time", JLabel.RIGHT);					parm.add(lbl); 
//    	outlbl_val[i] = new JLabel("t (sec)", JLabel.RIGHT);   	outlbl_val[i].setForeground(Color.white);  parm.add(outlbl_val[i++]); // fmt.format("%.15s", outlbl_val[i++]);  parm.add(toString(fmt));
//    	lbl = new JLabel("Displacement", JLabel.RIGHT);			parm.add(lbl);
//    	outlbl_val[i] = new JLabel("d (m)", JLabel.RIGHT);   	outlbl_val[i].setForeground(Color.white); parm.add(outlbl_val[i++]);
//
//    	ioparm.add(parm);
    	
    	return  ioparm;
        
    }


    private void bottomPanel()
    {
    	   initInputControlsField();
    	   outlbl_val=new JLabel[3];
    	   Color bk = new Color(219,226,238);
           bottomPanel.setLayout(new java.awt.GridLayout(1,3));
           bottomPanel.setBackground(Color.black);
           bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(235,233,215),8));
           
           in1 = new JPanel(new java.awt.GridLayout(3,3));
           in1.setBackground(bk);
           bottomPanel.add(in1);

           in2 = new JPanel(new java.awt.GridLayout(3,2)); 
           in2.setBackground(bk);

           bottomPanel.add(in2);

           in3 = new JPanel(new java.awt.GridLayout(3,3)); 
           in3.setBackground(bk);
           bottomPanel.add(in3);
           
           JLabel lab=new JLabel("Breadth of Beam");
           m_Slider[0] = new JSlider(JSlider.HORIZONTAL,1, 10, 3);
           m_Slider[0].addChangeListener(new ChangeListener() {
   		    public void stateChanged(ChangeEvent e) {
               valChange = true;
               int val = ((JSlider) e.getSource()).getValue();
               fields[0]=val*100;
   
               iLabel[0].setText(":: " + fields[0] + " mm");
               repaint();
            }
            });
           m_Slider[0].setBackground(bk);
           in1.add(lab);
           in1.add(m_Slider[0]);
           in1.add(iLabel[0]);
           
           lab = new JLabel("Depth of Beam", JLabel.RIGHT);
           m_Slider[1] = new JSlider(JSlider.HORIZONTAL,1, 10, 4);
           m_Slider[1].addChangeListener(new ChangeListener() {
   		    public void stateChanged(ChangeEvent e) {
               valChange = true;
               int val = ((JSlider) e.getSource()).getValue();
               fields[1]=val*100;
          
               iLabel[1].setText(":: " + fields[1] + " mm");
               repaint();
            }
            });
           m_Slider[1].setBackground(bk);
           in1.add(lab);
           in1.add(m_Slider[1]);
           in1.add(iLabel[1]);
           
           
           lab = new JLabel("Length of Beam", JLabel.RIGHT);
           m_Slider[2] = new JSlider(JSlider.HORIZONTAL,1, 5, 2);
           m_Slider[2].addChangeListener(new ChangeListener() {
   		    public void stateChanged(ChangeEvent e) {
               valChange = true;
               int val = ((JSlider) e.getSource()).getValue();
               fields[2]=val*1000;
       
               iLabel[2].setText(":: " + fields[2] + " mm");
               repaint();
            }
            });
           m_Slider[2].setBackground(bk);
           in1.add(lab);
           in1.add(m_Slider[2]);
           in1.add(iLabel[2]);
        
           
           
           lab = new JLabel("Type of Support", JLabel.RIGHT);
           type_of_support = new JComboBox();
           type_of_support.addItem("Simply Supported");            type_of_support.addItem("Cantilever");
           type_of_support.addItem("Fixed and Contineous");
            type_of_support.addActionListener(new java.awt.event.ActionListener() {
	   			 public void actionPerformed(ActionEvent e) {
	   				 valChange = true;
	   				 JComboBox cb = (JComboBox)e.getSource();
	   				  objImg = (String)cb.getSelectedItem();
	   				 valChange = true;	   
	   				 getImg();
	   				univ.getCanvas().repaint();
	   			//	repaint();
	   			 }
 			 });
           in2.add(lab);
           in2.add(type_of_support);
           
           lab=new JLabel("Column Material",JLabel.CENTER);
           JComboBox Column_material = new JComboBox();
      //     Column_material.addItem("-");
           Column_material.addItem("Concrete");
           Column_material.addItem("Steel");
           Column_material.addActionListener(new java.awt.event.ActionListener() {
	   			 public void actionPerformed(ActionEvent e) {
	   				 valChange = true;
	   				
	   			      
	   				 JComboBox cb = (JComboBox)e.getSource();
	   				 obj_f = (String)cb.getSelectedItem();
	   				 valChange = true;
	   			   	if (obj_f.equalsIgnoreCase("Concrete"))
	 	     		    {
	 	     	  
	 	     	            cementGradeList.addItem("M" + 25); 
	 	     	        	cementGradeList.addItem("M" + 20); 
	 	     	        	cementGradeList.addItem("M" + 30); 
	 	     	  	     	cementGradeList.addItem("M" + 35);  
	 	     	         	cementGradeList.addItem("M" + 40);  
	 	     	  	    int n=cementGradeList.getItemCount();
	 	     	  	    if (n>5)
	 	     	  	    {
	 	     	  	        cementGradeList.removeItem("Fe" + 415); 	
	 	     	  	    }
	     	              
	 	     	     
	 	     	     	cementGradeList.removeItem("Fe" + 415);
	     	    
	 	     	  	repaint();
	 	     		   }
	 	   		     	if (obj_f.equalsIgnoreCase("Steel"))
		     		    {
		     
		     
		     	      	cementGradeList.addItem("Fe" + 415); 
		     	      	cementGradeList.removeItem("M" + 20);
		     	    	cementGradeList.removeItem("M" + 25);
		     	    	cementGradeList.removeItem("M" + 30);
		     		    cementGradeList.removeItem("M" + 35);
		     		    cementGradeList.removeItem("M" + 40);
		     		
		     	      	int n=cementGradeList.getItemCount();
		     	         if(n>1)
		     	         {
		     	        	cementGradeList.removeItem("M" + 20);
			     	    	cementGradeList.removeItem("M" + 25);
			     	    	cementGradeList.removeItem("M" + 30);
			     		    cementGradeList.removeItem("M" + 35);
			     		    cementGradeList.removeItem("M" + 40);		     	        	 
		     	         }
		     	          System.out.println(n);
		     	         repaint();
		     		   }
	 	   		 //   cementGradeList.setEditable(false);
	   				 resetOutputParameters();
	   				repaint();
	   			
	   			 }
 			 });
           
           in2.add(lab);
           in2.add(Column_material);
        
        lab = new JLabel("Material Grade", JLabel.RIGHT);         
        cementGradeList = new JComboBox();  
        cementGradeList.setEditable(false);
             
   //   System.out.println(cementGradeList);
         cementGradeList.addActionListener(new java.awt.event.ActionListener() {
	   			 public void actionPerformed(ActionEvent e) {
	   				 valChange = true;
	   			   				 String obj =(String)((JComboBox)e.getSource()).getSelectedItem();
	   		    	
	   			   			
	   			    System.out.println(obj);
	   			    StringBuffer obj1 = new StringBuffer(obj);
	   			    if(obj_f.equalsIgnoreCase("Concrete")) 
	   				 {
	   			    	
	   				    obj1 = new StringBuffer(obj.substring(1));	   			
	   				 }
	   			    if(obj_f.equalsIgnoreCase("Steel")) 
				    {
	   			
				       obj1 = new StringBuffer(obj.substring(2));	   			
				    }
	   			//    System.out.println(obj1);
	   			    
	   			  //  val=Integer.parseInt(obj);
	   				 val = Integer.parseInt(obj1.toString());	   				   				 
	   				 resetOutputParameters();
	   				repaint();
	   				 // univ.getCanvas().repaint();
	   			 }
			 });
      
         in2.add(lab);
         in2.add(cementGradeList);
         
         
         lab =new JLabel("OUTPUT", JLabel.CENTER);   
         JLabel lab1 =new JLabel("    ", JLabel.CENTER);
         in3.add(lab);
         in3.add(lab1);  
         
         lab =new JLabel("LOAD", JLabel.CENTER); 
         outlbl_val[0]=new JLabel("0", JLabel.CENTER);;
         in3.add(lab);
         in3.add(outlbl_val[0]);
         
         lab=new JLabel("Defelection",JLabel.RIGHT);        
         outlbl_val[1]=new JLabel("0", JLabel.CENTER);
         in3.add(lab);
         in3.add(outlbl_val[1]);
          

	   
	       
	       
         
     
      
	        bottomPanel.setVisible(false);
            /////////// Enable/Disable function for Input parameters
            enable(in1,false);
            enable(in2,false);
            enable(in3,false);
    }
    private String getImg() {
    	if(objImg=="Step Force")
			{
    		    String str="resources/data/DefBeams/stepforce1.jpg";
    		    rightIcon.setIcon(m_j3d.createImageIcon(str)); 
				return str;
				
			}
			else if(objImg=="Ramp Force")
			{
				String str="resources/data/DefBeams/rampforce1.jpg";
				rightIcon.setIcon(m_j3d.createImageIcon(str)); 
				return str;
			}
			else if(objImg=="Ramp Dropped")
			{
				String str="resources/data/DefBeams/Rampdrop1.jpg";
				rightIcon.setIcon(m_j3d.createImageIcon(str)); 
				return str;
				
			}
			else if(objImg=="Ramp With Rise")
			{
				String str="resources/data/DefBeams/ramp_stepforce1.jpg";
				rightIcon.setIcon(m_j3d.createImageIcon(str));
				return str;
				
			}
			else if(objImg=="Rectangular")
			{
				String str="resources/data/DefBeams/rectangular-force1.jpg";
				rightIcon.setIcon(m_j3d.createImageIcon(str));
				return str;
				
			}
			else if (objImg=="Half Sine Pulse")
			{
				String str="resources/data/DefBeams/halfsine-force1.jpg";
				rightIcon.setIcon(m_j3d.createImageIcon(str));
				return str;
				
			}
			else 
				return "resources/data/DefBeams/stepforce1.jpg";
	}
    
  private void initInputControlsField(){
    	
    	iLabel = new JLabel[3];
       	int i=0;
       	iLabel[i] = new JLabel("300 mm", JLabel.LEFT); iLabel[i++].setForeground(Color.blue);
       	iLabel[i] = new JLabel("400 mm", JLabel.LEFT); iLabel[i++].setForeground(Color.blue);
    	iLabel[i] = new JLabel("2000 mm", JLabel.LEFT); iLabel[i++].setForeground(Color.blue);
     
       	i=0;
       	fields = new float[3];
       	fields[0]=3;
       	fields[1]=4;
    	fields[2]=2;
       	
    }
    
 
    
 
    

    private int getTOF()
    {
    	if(objImg=="Step Force")
		{
			return 1;
		}
		else if(objImg=="Ramp Force")
		{
			return 2;
		}
		else if(objImg=="Ramp Dropped")
		{
			return 3;
			
		}
		else if(objImg=="Ramp With Rise")
		{
			return 4;
			
		}
		else if(objImg=="Rectangular")
		{
			return 5;
			
		}
		else if (objImg=="Half Sine Pulse")
		{
			return 6;
		}
		else 
			return 1;
    }
  
   
    
    private void onNextStage()
    {
    	    	
    	valChange = true; // Clear the graph. or Graph will restart on Play    	
    	resetOutputParameters(); // Clear the Output Parameters
    	bottomPanel.setVisible(true);
    	enableStage(stage);
    	setInstructionText();
   		 
    }
    private void enableStage(int s){
    	switch(s){
    	case 0: // Home     		
    		enable(in1,false);	 enable(in2,false);		 enable(in3,false);	
			
    		break;
    	case 1: // Home 
    		
    		enable(in1,true);	 enable(in2,true);		 enable(in3,true);	
			
			
    		break;
    	case 2:

    		enable(in1,true);	 enable(in2,true);		 enable(in3,true);	
    		nextButton.setVisible(false);
    		break;
//    	case 2:
//
//    		enable(in1,false);	 enable(in2,false);		 enable(in3,true);	
//    		break;
//			
//    	case 3:
//    		enable(in1,true);	 enable(in2,true);		 enable(in3,true);	
//		break;
    	
    
    	}
    	
    }
    
    private void setInstructionText()
    {
    	    	
    	valChange = true; // Clear the graph. or Graph will restart on Play    	
    	resetOutputParameters(); // Clear the Output Parameters
    	
    	
    	switch(stage){
    	case 0: // Home 
    		m_Objective.setText(">: Run the experiment with selected 'type of force'.");
    		m_Objective.setForeground(Color.WHITE);
    					break;
    	case 1:
    		m_Objective.setText(">: Observe the effect of time period on maximum response.");
    		m_Objective.setForeground(Color.GREEN);
    		    		break;
//    	case 2:
//    		m_Objective.setText(">: What is resonance and how damping effect the resonance?");
//    		m_Objective.setForeground(Color.WHITE);
//    					break;
//    	case 3:
//    		m_Objective.setText(">: Observe how resonance get effect in different cases of Earthquake data input");
//    		m_Objective.setForeground(Color.GREEN);
//    		  			break;
//    	case 4:
//    		m_Objective.setText(">: Change different parameter and observe the effect in time period.");
//    		m_Objective.setForeground(Color.WHITE);
//    					break;
    	}
    		
   		 
    }
    
    private void resetOutputParameters()
    {
//    	int i=0;
//    	outlbl_val[i++].setText(getMass() + " Kg");
//        outlbl_val[i++].setText(String.valueOf(getStiff()).substring(0,4)+ String.valueOf(getStiff()).substring(String.valueOf(getStiff()).length()-4,String.valueOf(getStiff()).length())+" N/m");
//    	 i=2;
//        outlbl_val[i++].setText(" t sec");
//        outlbl_val[i++].setText(" d m");
//       
        
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
	    //Vector3d s = new Vector3d();
	    Vector3f currPos=new Vector3f();
	    t3d.get(currPos); 
	   
	    
	   // System.out.println("current Pos:" + currPos);
	    float y = (float)(float)Math.sin(Math.toRadians(m_cameraViews[m_cameraEye]));
	    float z = 2.41f - Math.abs(y);//((float)Math.sin(Math.toRadians(m_cameraViews[m_cameraEye])));
	    // default (0, 0, 2.41)
	   // System.out.println("x" + x);
	    t3d.lookAt( new Point3d(0, y,z), new Point3d(0,0,0), new Vector3d(0,1,0));
	    t3d.invert();
	    
	    //t3d.setTranslation(new Vector3d(0,0,8));
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
	    //Vector3d s = new Vector3d();
	    Vector3f currPos=new Vector3f();
	    t3d.get(currPos);
	    
	   // System.out.println("current Pos:" + currPos);
	    float x = (float)(float)Math.sin(Math.toRadians(m_cameraViews[m_cameraEye]));
	    float z = 2.41f - Math.abs(x);
	    //((float)Math.sin(Math.toRadians(m_cameraViews[m_cameraEye])));
	    // default (0, 0, 2.41)
	    // System.out.println("x" + x);
	    t3d.lookAt( new Point3d(x, 0,z), new Point3d(0,0,0), new Vector3d(0,1,0));
	    t3d.invert();
	    
	    //t3d.setTranslation(new Vector3d(0,0,8));
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
    	enableStage(0);   // -1 	
        reStartButton.setEnabled(false);
        nextButton.setEnabled(false);
        outputGraph.setState(1);
 
        
        if(valChange){
        	double len=fields[2];
        	double bre=fields[0];
        	double dep=fields[1];
	        init(len,bre,dep);
	      
        }
        
        timer.start();
        System.out.println("Timer started");     
    }
    
   
    
    private void init(double len, double bre, double dep) {
    	double E;
		double I=bre*Math.pow(dep, 3)/12;
		if (val > 100.0)
      	  E=200000;         // Es = 200000;
        else
        E=5000*Math.sqrt(val);
		constant=E*I;
	}

	// Resume Button Action
    private void timerActionPerformed(java.awt.event.ActionEvent evt)
    {
     
    	int i=0;   

	    
        outlbl_val[i++].setText(String.valueOf(load) + " N");
        
        if(String.valueOf(defelection).length()>7)                                 
        	outlbl_val[i++].setText(String.valueOf(defelection).substring(0,7)+" mm");
      
        outputGraph.addGraphValue(defelection);  
        if(rightPanel.isVisible())
        {
                    outputGraph.drawGraph();
    
        }
   
        	update();
         
        if(isDataCompleted()) {
	        	timer.stop();
	        	System.out.println("timer stopped");
	        	pauseSimulation();
	        	return;
	        }
         return;            
    }
    
    private void update() {
    	if(objImg=="Simply Supported")
		{
		   
		    	defelection=load*Math.pow(fields[2],3)/(48*constant);
		    	load=load+1000;
			
		}
		else if(objImg=="Cantilever")
		{
			
		    	defelection=load*Math.pow(fields[2],3)/(3*constant);
		    	load=load+1000;
		}
		else if(objImg=="Fixed and Contineous")
		{
		//	for(load=0.0;load<1000000.0;load=load+1000.0)
		    	defelection=load*Math.pow(fields[2],3)/(192*constant);			
		    	load=load+1000;
		}
		
		else 
		{
		//	for(load=0.0;load<1000000.0;load=load+1000.0)
		    	defelection=load*Math.pow(fields[2],3)/(48*constant);
		    	load=load+1000;
		}
	}
    private boolean isDataCompleted() {
		if (load>1000000.0)
			return true;
		return false;
	}

	private void updateSimulationBody(double disp){
    	
    	Shape3D shape = (Shape3D)hm.get("block1");
    	shape.setGeometry(m_j3d.createBoxGeom((float)disp*3));
    	
    	TransformGroup tgp = (TransformGroup)hm.get("roof1");
    	Transform3D trans = new Transform3D();
    	tgp.getTransform(trans);
        trans.setTranslation(new Vector3d( disp -0,0.17, -.1));       
        tgp.setTransform(trans);
        

    }
    
    private void pauseSimulation()
    {
    	
		timer.stop();
		ImageIcon icon = m_j3d.createImageIcon("resources/icons/start.png"); 
    	startButton.setIcon(icon);
    	startButton.setText("Start");
    	reStartButton.setEnabled(true);
         // bottomPanel.setVisible(true);
        nextButton.setEnabled(true);
    	
    	rightPanel.setVisible(true);
		enableStage(stage);
		outputGraph.setState(0);
//		inputGraph.setState(0);
        //startButton.setEnabled(true);
		       
        valChange = false;
         
		repaint();
    }
 
    
}

    

   
