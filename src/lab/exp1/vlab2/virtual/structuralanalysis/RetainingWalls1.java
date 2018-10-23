
package structuralanalysis;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Formatter;
import java.util.HashMap;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Group;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import eerc.vlab.common.J3DShape;
/**
 * Simple Java 3D program that can be run as an application or as an applet.
 */
public class RetainingWalls1 extends javax.swing.JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private javax.swing.JPanel topPanel;
	private javax.swing.JPanel simulationPanel;
	private javax.swing.JPanel bottomPanel;
	private javax.swing.JPanel rightPanel;
	private javax.swing.JPanel createInputOutputPanel;
	  
	private javax.swing.JPanel in1;			
	private javax.swing.JPanel in2;			
	private javax.swing.JPanel in3;			
	
	private javax.swing.JButton startButton=null;
	private javax.swing.JButton reStartButton=null;
	private javax.swing.JButton nextButton=null;

	

	//private GraphPlotter         graphPlotter;
	////////////////////////////Java3D componenet ///////////////////////////

	
	private SimpleUniverse      univ = null;                  // Simple Universe Java3D
	private BranchGroup         scene = null;                 // BranchGroup Scene graph
	private Switch 				switchGroup=null;	
	private Switch 				switchGroup1=null;
	private Switch 				switchGroup2=null;
	private Switch 				switchGroup3=null;
	private Switch 				switchGroup4=null;
	 

	private RetainingWallsBody1     freeBody =null;               // Shape3D
	private HorizontalGraph		outputGraph =null;
	private HorizontalGraph		outputGraph2 =null;
	private HorizontalGraph		inputGraph =null;
	private FullViewGraph  		fullViewGraph = new FullViewGraph();
	
	


	private JSlider m_Slider[] = new JSlider[3];
	private JLabel m_Lbl[] = new JLabel[4];
	private int stagei=1;
	
	private HashMap 			hm = new HashMap();
	private J3DShape 			m_j3d	= new J3DShape();

	private float fields[];
	private JLabel outlbl_val[]=new JLabel[2];
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
	
	private JComboBox soil_wt;
	private JComboBox mas_wt;
	private JComboBox ang_res;
	private JComboBox prs_tp;
	private JComboBox uni_sur;
	private JComboBox s_hl;
	private JComboBox s_vl;
        private String soil_wt1="18",mas_wt1="20",ang_res1="30",uni_sur1="10",s_hl1="1",s_vl1="10";
        private String psr_tp1="Active";
	
	private JLabel lbl_k;
	private javax.swing.JButton rightIcon=null;
	

	
	public BranchGroup createSceneGraph() 
	{
		// Create the root of the branch graph
		BranchGroup objRoot = new BranchGroup();
		objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND );
		objRoot.setCapability(Group.ALLOW_CHILDREN_READ);
		objRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
		objRoot.setCapability( BranchGroup.ALLOW_DETACH );
		
		
		//Point3f p = new Point3f();
//		btg = (TransformGroup)createVirtualLab();
//		objRoot.addChild(btg);
		objRoot.addChild(createVirtualLab());
		//objRoot.addChild(createVirtualLab());
		//objRoot.addChild(createLeg());
		//objRoot.addChild(createAllText());
		//objRoot.addChild(createIonsSwitchGroup());
		
		
//		 Floor
		int i,j;
		for(i=-4;i<=4;i++)
		{
			for(j=-4;j<=4;j++)
			{
				objRoot.addChild(m_j3d.createBox(new Vector3d((float)(i),-0.6,(float)(j)),new Vector3d(0.5,.01,0.5),new Vector3d(0,0,0),new Color3f(.8f, .8f, .8f),"resources/images/tile.jpg"));
			}
		}
	//	objRoot.addChild(m_j3d.createBox(new Vector3d(0,-0.6, -.1),new Vector3d(1.5,.01,1.5),new Vector3d(0,0,0),new Color3f(.8f, .8f, .8f),"resources/images/tile.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.4,-2.5),new Vector3d(10,10,.01),new Vector3d(0f, 0f,0f), new Color3f(0.5f,0.6f,0.72f)));
		// Walls and roof
		objRoot.addChild(m_j3d.createBox(new Vector3d(1,0.1f,0), new Vector3d(0.05,0.7f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 0.9f),"resources/images/376.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(-1,0.1f,0), new Vector3d(0.05,0.7f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 0.9f),"resources/images/376.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.1f,-2.0), new Vector3d(1,0.7f,0.05),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/992.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.84f,0), new Vector3d(1.05,0.04f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/376.jpg"));
		
	
		
		//objRoot.addChild(m_j3d.createBox(new Vector3d(0,-0.25, -.1),new Vector3d(3,.01,1),new Vector3d(0,0,0),new Color3f(0f, 1f, 0f),"resources/images/table.jpg"));
		//objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.4, -.6),new Vector3d(3,.9,.1),new Vector3d(0f, 0f,0f), new Color3f(0.5f,0.6f,0.72f)));
			
		float rad = (float)Math.PI/180;
		Transform3D t = new Transform3D();
		t.setScale(0.4);
		
		TransformGroup tg = new TransformGroup();
		t = new Transform3D();
		t.rotX(rad*10);
		t.setScale(new Vector3d(.5f,.05f,.5f));        
		t.setTranslation(new Vector3d(.3,.3,0));
		tg.setTransform(t);
		   
		freeBody=new RetainingWallsBody1();
		
	    
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
        
        ViewingPlatform vp = univ.getViewingPlatform();
	    TransformGroup steerTG = vp.getViewPlatformTransform();
	    Transform3D t3d = new Transform3D();
	    steerTG.getTransform(t3d);
	    Vector3d s = new Vector3d();
	    Vector3f currPos=new Vector3f();
	    t3d.get(currPos); 
	    t3d.lookAt( new Point3d(0,0.5, 2.95), new Point3d(0,0,0), new Vector3d(0,1,0));
	    t3d.invert();
	    steerTG.setTransform(t3d);

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
	    
	   	    
	     //Block Center
	    switchGroup1 = new Switch( Switch.CHILD_MASK );
	    switchGroup1.setCapability( Switch.ALLOW_SWITCH_WRITE );
	    objtrans.addChild(switchGroup1);
	   
	    
	    float left_end =-0.39f;
	    int count=0;
	    int i=3;
	  
	    	
	    	switchGroup1.addChild(m_j3d.createtrep(new Vector3d(-0.3,-0.1, 0.1),new Vector3d(.32,.7,.4),new Vector3d(0,0,0),2f,0f,0f,new Color3f(1f, 1f, 1f),"resources/images/tile3.jpg","roof1",hm));
	    	switchGroup1.addChild(m_j3d.createtrep(new Vector3d(0.095,-0.1, 0.1),new Vector3d(.81,.7,.4),new Vector3d(0,0,0),0f,0f,0f,new Color3f(1f, 1f, 1f),"resources/images/soil.jpg","roof1",hm));
	   
	   
	   
	    	switchGroup1.addChild(m_j3d.createtrep(new Vector3d(-0.3,-0.1, 0.1),new Vector3d(.32,.7,.4),new Vector3d(0,0,0),2f,0f,0f,new Color3f(1f, 1f, 1f),"resources/images/tile3.jpg","roof1",hm));
	    	
	    	switchGroup1.addChild(m_j3d.createtrep(new Vector3d(0.095,-0.1, 0.1),new Vector3d(.81,.7,.4),new Vector3d(0,0,0),0f,0.4f,0f,new Color3f(1f, 1f, 1f),"resources/images/soil.jpg","roof1",hm));
	    
	   
	    	switchGroup1.addChild(m_j3d.createtrep(new Vector3d(-0.3,-0.1, 0.1),new Vector3d(.32,.7,.4),new Vector3d(0,0,0),2f,0f,2f,new Color3f(1f, 1f, 1f),"resources/images/tile3.jpg","roof1",hm));
	    	switchGroup1.addChild(m_j3d.createtrep(new Vector3d(0.095,-0.1, 0.1),new Vector3d(.81,.7,.4),new Vector3d(0,0,0),-0.35f,0f,0f,new Color3f(1f, 1f, 1f),"resources/images/soil.jpg","roof1",hm));
	    
	  
	   
	   
	    	switchGroup1.addChild(m_j3d.createtrep(new Vector3d(-0.3,-0.1, 0.1),new Vector3d(.32,.7,.4),new Vector3d(0,0,0),2f,0f,2f,new Color3f(1f, 1f, 1f),"resources/images/tile3.jpg","roof1",hm));
	    	switchGroup1.addChild(m_j3d.createtrep(new Vector3d(0.095,-0.1, 0.1),new Vector3d(.81,.7,.4),new Vector3d(0,0,0),-0.35f,0.4f,0f,new Color3f(1f, 1f, 1f),"resources/images/soil.jpg","roof1",hm));
	    	
	    
	    	
	    	java.util.BitSet visibleNodes = new java.util.BitSet( switchGroup1.numChildren() );
		 	   visibleNodes.set( 0); 
		 	  visibleNodes.set( 1);
		 	
		 	   
		 	  switchGroup1.setChildMask( visibleNodes );
	// objtrans.addChild(m_j3d.createtrep(new Vector3d(-0.065,-0.1, 0.1),new Vector3d(.4,.2,.4),new Vector3d(0,0,0),0f,-0.4f,new Color3f(1f, 1f, 1f),"resources/images/soil2.jpg","roof1",hm));
	    
	    return objtrans;
	}
    
     
     
   
     
     
    
    /**
     * Creates new form FreeVibration
     */
    public RetainingWalls1(Container container) {
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
    	RetainingWalls1 mainPanel;

        public void init() {
            setLayout(new BorderLayout());
            mainPanel = new RetainingWalls1(this);
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
            setTitle("Retaining Walls");
            getContentPane().add(new RetainingWalls1(this), BorderLayout.CENTER);
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
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">                          
    private void initComponents() {
        
        
//      new GridLayout(2, 1)
        setLayout(new java.awt.BorderLayout());
        
        bottomPanel = new javax.swing.JPanel(); 	// input from user at bottom
        simulationPanel = new javax.swing.JPanel(); // 3D rendering at center
        topPanel= new javax.swing.JPanel();    		// Pause, resume, Next
        rightPanel = new javax.swing.JPanel();    	// Graph and Input and Output Parameter
        createInputOutputPanel= new javax.swing.JPanel(); 
         
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
        
                            

        
    }// </editor-fold>                        

    private void topPanel() {
            
        java.awt.GridBagConstraints gridBagConstraints;
            
        javax.swing.JPanel guiPanel = new javax.swing.JPanel(); // Pause, resume at top
        guiPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);  
                
//        javax.swing.JButton pauseButton = new javax.swing.JButton();  
//        javax.swing.JButton startButton = new javax.swing.JButton(); 
        reStartButton = new javax.swing.JButton("Re-Start");
        ImageIcon icon = m_j3d.createImageIcon("resources/icons/restart.png"); 
        reStartButton.setIcon(icon);
        startButton = new javax.swing.JButton("Start");
        icon = m_j3d.createImageIcon("resources/icons/start.png"); 
        startButton.setIcon(icon);
        nextButton = new javax.swing.JButton("Next");
        icon = m_j3d.createImageIcon("resources/icons/next.png");        
        nextButton.setIcon(icon);
//        ImageIcon icon = m_j3d.createImageIcon("resources/images/show_graph.png");        
//        startButton.setIcon(icon);
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
                
//                outputGraph.clearGraphValue();
//                inputGraph.clearGraphValue();
                
                valChange = true;
                startSimulation(evt);
                univ.getCanvas().repaint();
               
                
//            	reStartButton.setEnabled(false);
//                //startButton.setEnabled(true);
//                startButton.setText("Start");
//                startStop = false;
//                timer.stop();
//                outputGraph.clearGraphValue();
//                inputGraph.clearGraphValue();
//                
                valChange = true;
                
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
        
       
//        javax.swing.JButton btn= new javax.swing.JButton("Full View Graph");
//        guiPanel.add(btn, gridBagConstraints);
//        icon = m_j3d.createImageIcon("resources/icons/graph_window.png");        
//        btn.setIcon(icon);
//        btn.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {                
// 
//            	HorizontalGraph graph[] ={outputGraph,outputGraph2,inputGraph};
//             	int max[]={1000,100,100};
//             	int magX[]={2,2,2};
//             	int magY[]={2,2,2};
//            	
//            	JFrame frame = new JFrame("Full View Graph");
//            	//GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
//                //Add contents to the window.
//            	
//                //frame.add(p);
//            	frame.setExtendedState(frame.getExtendedState() | frame.MAXIMIZED_BOTH);
//
//                //frame.setMaximizedBounds(e.getMaximumWindowBounds());
//                //frame.setSize(e.getMaximumWindowBounds().width, e.getMaximumWindowBounds().height);
//                
//                //Display the window.
//                frame.pack();
//                frame.setVisible(true);
//                //frame.setResizable(false);
//                
//                fullViewGraph =new FullViewGraph(graph,max,magX,magY,frame.getWidth()-20, frame.getHeight());
//                 
//                frame.add(fullViewGraph);
//                System.out.println("w " + frame.getWidth() + " h " + frame.getHeight());
//                
//                
//            }
//          });
        
      
      guiPanel.add(reStartButton, gridBagConstraints);
      guiPanel.add(startButton, gridBagConstraints);
      guiPanel.add(nextButton, gridBagConstraints);
        
//      javax.swing.JButton btn= new javax.swing.JButton("Manual"); 
//      icon = m_j3d.createImageIcon("resources/icons/manual.png");        
//      btn.setIcon(icon);
//      //startButton.setPreferredSize(new Dimension(100,30));
//        guiPanel.add(btn, gridBagConstraints);
//        btn.setVisible(false);
//        btn.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {                
//            	
//                HelpWindow.createAndShowGUI("forcedVib");
//            }
//          });       
        
        
    }
    
    private void buildConstraints(GridBagConstraints gbc,int x, int y, int w, int h, int wx, int wy, int fill, int anchor){

    	gbc.gridx = y; // start cell in a row
    	gbc.gridy = x; // start cell in a column
    	gbc.gridwidth = w; // how many column does the control occupy in the row
    	gbc.gridheight = h; // how many column does the control occupy in the column
    	gbc.weightx = wx; // relative horizontal size
    	gbc.weighty = wy; // relative vertical size
    	gbc.fill = fill; // the way how the control fills cells
    	gbc.anchor = anchor; // alignment

    	} 
    
    
    private void rightPanel() {
        
                  
     
     initInputControlsField();
    
     
     
 	    Color bk = new Color(219,226,238);
        rightPanel.setLayout(new java.awt.FlowLayout());
      //  rightPanel.setBackground(Color.black);
        rightPanel.setBorder(BorderFactory.createLineBorder(new Color(140,200,240),8));
     //   rightPanel.setBorder(BorderFactory.createLineBorder(new Color(235,233,215),15));
        rightPanel.setPreferredSize(new java.awt.Dimension(350, 600));
        
        JPanel panel = new JPanel();
     //   panel.setBackground(new Color(140,200,240));
        panel.setPreferredSize(new java.awt.Dimension(330, 235));
      
        ImageIcon icon = m_j3d.createImageIcon("resources/CA/RA1.jpg"); 
      
       
        rightIcon = new javax.swing.JButton(" ");
        rightIcon.setIcon(icon);
        panel.add(rightIcon);
        rightPanel.add(panel);
        in3=new JPanel();
        GridBagLayout gbl=new GridBagLayout();
        in3.setLayout(gbl); 
        GridBagConstraints gbc = new GridBagConstraints();
        in3.setPreferredSize(new java.awt.Dimension(330, 365));
        in3.setBackground(bk);
        
        
//    //    in3.setBorder(BorderFactory.createLineBorder(new Color(132,132,255),3));
//      
//        gbc.gridx = 0;       // first column
//        gbc.gridy = 0;       // first row
//        gbc.gridwidth = 1;   // occupies only one column
//        gbc.gridheight = 1;  // occupies only one row 
//        gbc.weightx = 30;    // relative horizontal size - first column
//        gbc.weighty = 10;    // relative vertical size  - first row
//        gbc.fill = GridBagConstraints.NONE;    // stay as small as possible
//                                              // suite for labels       
//        gbc.anchor = GridBagConstraints.CENTER; // center aligning
//        
//        //inform the layout about the control to be added and its constraints:
//      //add the JLabel to the JPanel object

    
        
        
        
        
        JLabel lab=new JLabel(" Height of Wall");
        m_Slider[0] = new JSlider(JSlider.HORIZONTAL,5, 12, 5);
        m_Slider[0].addChangeListener(new ChangeListener() {
		    public void stateChanged(ChangeEvent e) {
            valChange = true;
            int val = ((JSlider) e.getSource()).getValue();  
            fields[0]=val*1.0f;            
                iLabel[0].setText(":: " + fields[0] + " m");       
            repaint();            
         }
         });
        m_Slider[0].setBackground(bk);
        
        
        buildConstraints(gbc, 0, 0, 1, 1, 30, 10, GridBagConstraints.NONE, GridBagConstraints.CENTER );
        gbl.setConstraints(lab, gbc);
        in3.add(lab);
    
        buildConstraints(gbc, 0, 1, 1, 1, 30, 10, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER );
        gbl.setConstraints(m_Slider[0], gbc);
        in3.add(m_Slider[0]);
        
        buildConstraints(gbc, 0, 2, 1, 1, 30, 10, GridBagConstraints.NONE, GridBagConstraints.CENTER );
        gbl.setConstraints(iLabel[0], gbc);
        in3.add(iLabel[0]);
        
                
        lab=new JLabel(" Top Width of Wall");
        m_Slider[1] = new JSlider(JSlider.HORIZONTAL,1, 6, 1);
        m_Slider[1].addChangeListener(new ChangeListener() {
		    public void stateChanged(ChangeEvent e) {
            valChange = true;      
            int val = ((JSlider) e.getSource()).getValue();         
            fields[1]=val*0.5f;            
            iLabel[1].setText(":: " + Float.toString(fields[1]) + " m");           
            repaint();                    
         }
         });
        m_Slider[1].setBackground(bk);
        
        buildConstraints(gbc, 1, 0, 1, 1, 30, 10, GridBagConstraints.NONE, GridBagConstraints.CENTER );
        gbl.setConstraints(lab, gbc);
        in3.add(lab);
    
        buildConstraints(gbc, 1, 1, 1, 1, 30, 10, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER );
        gbl.setConstraints(m_Slider[1], gbc);
        in3.add(m_Slider[1]);
        
        buildConstraints(gbc, 1, 2, 1, 1, 30, 10, GridBagConstraints.NONE, GridBagConstraints.CENTER );
        gbl.setConstraints(iLabel[1], gbc);
        in3.add(iLabel[1]);
//        in3.add(lab);
//        in3.add(m_Slider[1]);
//        in3.add(iLabel[1]);
        
       
        lab=new JLabel(" Bottom Width of Wall");
        m_Slider[2] = new JSlider(JSlider.HORIZONTAL,8, 14, 8);
        m_Slider[2].addChangeListener(new ChangeListener(){
		    public void stateChanged(ChangeEvent e){
            valChange = true;          
            int val = ((JSlider) e.getSource()).getValue();
            fields[2]=val*0.5f;
            
            iLabel[2].setText(":: " + Float.toString(fields[2]) + " m"); 
           
            repaint();          
         }
         });
   
        m_Slider[2].setBackground(bk);
        
        buildConstraints(gbc, 2, 0, 1, 1, 30, 10, GridBagConstraints.NONE, GridBagConstraints.CENTER );
        gbl.setConstraints(lab, gbc);
        in3.add(lab);
    
        buildConstraints(gbc, 2, 1, 1, 1, 30, 10, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER );
        gbl.setConstraints(m_Slider[2], gbc);
        in3.add(m_Slider[2]);
        
        buildConstraints(gbc, 2, 2, 1, 1, 30, 10, GridBagConstraints.NONE, GridBagConstraints.CENTER );
        gbl.setConstraints(iLabel[2], gbc);
        in3.add(iLabel[2]);
//        in3.add(lab);
//        in3.add(m_Slider[2]);
//        in3.add(iLabel[2]);
        
        lab = new JLabel(" Soil Weight");
        soil_wt= new JComboBox();
        soil_wt.addItem("18");         soil_wt.addItem("19");
        soil_wt.addItem("20");       
        soil_wt.addActionListener(new java.awt.event.ActionListener() {
	   	 public void actionPerformed(ActionEvent e) {
	   				 valChange = true;
	   				 JComboBox cb = (JComboBox)e.getSource();
	   				 soil_wt1 = (String)cb.getSelectedItem();
	   				 valChange = true;
	   				repaint();
	   			 }
 			 });
        
      
        buildConstraints(gbc, 3, 0, 1, 1, 30, 10, GridBagConstraints.NONE, GridBagConstraints.CENTER );
        gbl.setConstraints(lab, gbc);
        in3.add(lab);
    
        buildConstraints(gbc, 3, 1, 1, 1, 30, 10, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER );
        gbl.setConstraints(soil_wt, gbc);
        in3.add(soil_wt);
        
        lab=new JLabel("KN/m³");
        buildConstraints(gbc, 3, 2, 1, 1, 30, 10, GridBagConstraints.NONE, GridBagConstraints.CENTER );
        gbl.setConstraints(lab, gbc);
        in3.add(lab);
        
//        lab=new JLabel("       ",JLabel.RIGHT);
//        buildConstraints(gbc, 3, 3, 1, 1, 30, 10, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER );
//        gbl.setConstraints(lab, gbc);
//        in3.add(lab);
        
 //       in3.add(lab);
//        in3.add(soil_wt);
//        lab=new JLabel("KN/m³",JLabel.RIGHT);
//        in3.add(lab);
        
        
        lab = new JLabel("Masonry Weight",JLabel.CENTER);
        mas_wt= new JComboBox();
        mas_wt.addItem("20");         mas_wt.addItem("21");
        mas_wt.addItem("22");         mas_wt.addItem("23");         
        
        mas_wt.addActionListener(new java.awt.event.ActionListener() {
	   	 public void actionPerformed(ActionEvent e) {
	   				 valChange = true;
	   				 JComboBox cb = (JComboBox)e.getSource();
	   				 mas_wt1 = (String)cb.getSelectedItem();
	   				 valChange = true;
	   				repaint();
	   			 }
 			 });
     //   in3.add(lab);
        buildConstraints(gbc, 4, 0, 1, 1, 30, 10, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER );
        gbl.setConstraints(lab, gbc);
        in3.add(lab);
    
        buildConstraints(gbc, 4, 1, 1, 1, 30, 10, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER );
        gbl.setConstraints(mas_wt, gbc);
        in3.add(mas_wt);
        
        lab=new JLabel("KN/m³",JLabel.CENTER);
        buildConstraints(gbc, 4, 2, 1, 1, 30, 10, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER );
        gbl.setConstraints(lab, gbc);
        in3.add(lab);
//        in3.add(mas_wt);
//        lab=new JLabel("KN/m³",JLabel.RIGHT);
//        in3.add(lab);
        
        
        lab = new JLabel(" Angle of Repose");
        ang_res= new JComboBox();
         ang_res.addItem("30"); 
         ang_res.addItem("20");         ang_res.addItem("25");
         ang_res.addItem("35");
        ang_res.addItem("40");         ang_res.addItem("45");
        ang_res.addItem("50");      //  ang_res.addItem("20");
        ang_res.addActionListener(new java.awt.event.ActionListener() {
	   	 public void actionPerformed(ActionEvent e) {
	   				 valChange = true;
	   				 JComboBox cb = (JComboBox)e.getSource();
	   				 ang_res1 = (String)cb.getSelectedItem();
	   				 valChange = true;
	   				repaint();
	   			 }
 			 });
        
     //   in3.add(lab);
        
        buildConstraints(gbc, 5, 0, 1, 1, 30, 10, GridBagConstraints.NONE, GridBagConstraints.CENTER );
        gbl.setConstraints(lab, gbc);
        in3.add(lab);
    
        buildConstraints(gbc, 5, 1, 1, 1, 30, 10, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER );
        gbl.setConstraints(ang_res, gbc);
        in3.add(ang_res);
        
        lab=new JLabel("Degree",JLabel.RIGHT);
        buildConstraints(gbc, 5, 2, 1, 1, 30, 10, GridBagConstraints.NONE, GridBagConstraints.CENTER );
        gbl.setConstraints(lab, gbc);
        in3.add(lab);
//        in3.add(ang_res);
//        lab=new JLabel(" Degree",JLabel.RIGHT);
//        in3.add(lab);
        
        
        lab = new JLabel(" Pressure Type", JLabel.RIGHT);
        prs_tp= new JComboBox();
        prs_tp.addItem("Active");         prs_tp.addItem("Passive");
        prs_tp.addActionListener(new java.awt.event.ActionListener() {
	   	 public void actionPerformed(ActionEvent e) {
	   				 valChange = true;
	   				 JComboBox cb = (JComboBox)e.getSource();
	   				 psr_tp1 = (String)cb.getSelectedItem();
	   				 valChange = true;
	   				repaint();
	   			 }
 			 });
     //   in3.add(lab);
        buildConstraints(gbc, 6, 0, 1, 1, 30, 10, GridBagConstraints.NONE, GridBagConstraints.CENTER );
        gbl.setConstraints(lab, gbc);
        in3.add(lab);
    
        buildConstraints(gbc, 6, 1, 1, 1, 30, 10, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER );
        gbl.setConstraints(prs_tp, gbc);
        in3.add(prs_tp);
        
        lab=new JLabel("",JLabel.RIGHT);
        buildConstraints(gbc, 6, 2, 1, 1, 30, 10, GridBagConstraints.NONE, GridBagConstraints.CENTER );
        gbl.setConstraints(lab, gbc);
        in3.add(lab);
//        in3.add(prs_tp);
//        lab=new JLabel(" ",JLabel.RIGHT);
//        in3.add(lab);
        
        
        
        lab = new JLabel(" Uniform Surcharge");
        uni_sur= new JComboBox();
        uni_sur.addItem("10");         uni_sur.addItem("15");
        uni_sur.addItem("20");         uni_sur.addItem("25");
        uni_sur.addItem("30");       
        uni_sur.addActionListener(new java.awt.event.ActionListener() {
	   	 public void actionPerformed(ActionEvent e) {
	   				 valChange = true;
	   				 JComboBox cb = (JComboBox)e.getSource();
	   				 uni_sur1 = (String)cb.getSelectedItem();
	   				 valChange = true;
	   				repaint();
	   			 }
 			 });
        
     //   in3.add(lab);
        buildConstraints(gbc, 7, 0, 1, 1, 30, 10, GridBagConstraints.NONE, GridBagConstraints.CENTER );
        gbl.setConstraints(lab, gbc);
        in3.add(lab);
    
        buildConstraints(gbc, 7, 1, 1, 1, 30, 10, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER );
        gbl.setConstraints(uni_sur, gbc);
        in3.add(uni_sur);
        
        lab=new JLabel("KN/m³",JLabel.RIGHT);
        buildConstraints(gbc, 7, 2, 1, 1, 30, 10, GridBagConstraints.NONE, GridBagConstraints.CENTER );
        gbl.setConstraints(lab, gbc);
        in3.add(lab);
//        in3.add(uni_sur);
//        lab=new JLabel("KN/m³",JLabel.RIGHT);
//        in3.add(lab);
       
        lab = new JLabel(" Slope Horizantal Level");
        s_hl= new JComboBox();
        s_hl.addItem("1");         s_hl.addItem("2");
        s_hl.addItem("5");         s_hl.addItem("10");
        s_hl.addItem("20");         
        s_hl.addActionListener(new java.awt.event.ActionListener() {
	   	 public void actionPerformed(ActionEvent e) {
	   				 valChange = true;
	   				 JComboBox cb = (JComboBox)e.getSource();
	   				 s_hl1 = (String)cb.getSelectedItem();
	   				 valChange = true;
	   				repaint();
	   			 }
 			 });
      //  in3.add(lab);
        buildConstraints(gbc, 8, 0, 1, 1, 30, 10, GridBagConstraints.NONE, GridBagConstraints.CENTER );
        gbl.setConstraints(lab, gbc);
        in3.add(lab);
    
        buildConstraints(gbc, 8, 1, 1, 1, 30, 10, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER );
        gbl.setConstraints(s_hl, gbc);
        in3.add(s_hl);
        
        lab=new JLabel(" ",JLabel.RIGHT);
        buildConstraints(gbc, 8, 2, 1, 1, 30, 10, GridBagConstraints.NONE, GridBagConstraints.CENTER );
        gbl.setConstraints(lab, gbc);
        in3.add(lab);
//        in3.add(s_hl);
//        lab=new JLabel(" ",JLabel.CENTER);
//        in3.add(lab);
         
        lab = new JLabel(" Slope Vertical Level");
        s_vl= new JComboBox();
        s_vl.addItem("1");         s_vl.addItem("2");
        s_vl.addItem("5");         s_vl.addItem("10");
        s_vl.addItem("20"); 
        s_vl.addActionListener(new java.awt.event.ActionListener() {
	   	 public void actionPerformed(ActionEvent e) {
	   				 valChange = true;
	   				 JComboBox cb = (JComboBox)e.getSource();
	   				 s_vl1 = (String)cb.getSelectedItem();
	   				 valChange = true;
	   				repaint();
	   			 }
 			 });  
     //   in3.add(lab);
        buildConstraints(gbc, 9, 0, 1, 1, 30, 10, GridBagConstraints.NONE, GridBagConstraints.CENTER );
        gbl.setConstraints(lab, gbc);
        in3.add(lab);
    
        buildConstraints(gbc, 9, 1, 1, 2, 30, 10, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
        gbl.setConstraints(s_vl, gbc);
        in3.add(s_vl);
        
        lab=new JLabel("",JLabel.RIGHT);
        buildConstraints(gbc, 9, 2, 1, 1, 30, 10, GridBagConstraints.NONE, GridBagConstraints.CENTER );
        gbl.setConstraints(lab, gbc);
        in3.add(lab);
//        in3.add(s_vl);
//        lab=new JLabel(" ",JLabel.RIGHT);
//        in3.add(lab);
      
        rightPanel.add(in3);        
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
         JLabel lbl = new JLabel("Retaining Walls", JLabel.CENTER);
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
         
     
       
         m_Objective = new JLabel("  ", JLabel.LEFT);
         m_Objective.setFont(new Font("Arial", Font.BOLD, 13));
         m_Objective.setForeground(Color.WHITE);
         guiPanel = new javax.swing.JPanel();
         guiPanel.setBackground(new Color(100,100,100));        
         guiPanel.add(m_Objective);
         btmPanel.add(guiPanel,BorderLayout.NORTH);
         
         
         
         guiPanel = new javax.swing.JPanel(); //          
         guiPanel.setBackground(new Color(235,233,215));
         guiPanel.setLayout(new java.awt.GridBagLayout());
         guiPanel.setBorder(BorderFactory.createLineBorder(new Color(140,200,240),8));
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
//         
//         guiPanel.add(viewButton, gridBagConstraints);
         
         JCheckBox chkbox = new JCheckBox("");
         lbl = new JLabel("Show Output Parameters", JLabel.CENTER);
         //lbl.setFont(new Font("Arial", Font.BOLD, 18));
         ImageIcon icon = m_j3d.createImageIcon("resources/icons/tasklist.png");        
         lbl.setIcon(icon);
         chkbox.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent event){                               
                     boolean bChecked =((JCheckBox)event.getSource()).isSelected();
                     if(bChecked)
                    	 createInputOutputPanel.setVisible(true);
                     else
                    	 createInputOutputPanel.setVisible(false);
                     univ.getCanvas().repaint();
                             // (a) checkbox.isSelected();
                             // (b) ((JCheckBox)event.getSource()).isSelected();
                            
             }
         });

         guiPanel.add(chkbox, gridBagConstraints);
         guiPanel.add(lbl, gridBagConstraints);
         
         chkbox = new JCheckBox("");
         lbl = new JLabel("Show Results", JLabel.CENTER);
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
         guiPanel.setLayout(new java.awt.BorderLayout());
         guiPanel.add(createInputOutputPanel()); 
         btmPanel.add(guiPanel,BorderLayout.SOUTH);
         
        
         
         //btm.add(viewButton, gridBagConstraints);
    }    // added create panel and worked out
    
    
    
private Component createInputOutputPanel(){
    	
	createInputOutputPanel.setPreferredSize(new java.awt.Dimension(1024, 100));
	  
	   Color bk = new Color(219,226,238);
	   createInputOutputPanel.setLayout(new java.awt.GridLayout(1,2));
	   createInputOutputPanel.setBackground(Color.black);
	   createInputOutputPanel.setBorder(BorderFactory.createLineBorder(new Color(235,233,215),8));
     
     in1 = new JPanel(new java.awt.GridLayout(2,2,10,10));
     in1.setBackground(bk);
     createInputOutputPanel.add(in1);
     JLabel lab1=new JLabel("  RESULTS",JLabel.LEFT);
     JLabel lab=new JLabel(" ");
     in1.add(lab1);
     in1.add(lab);
      lab=new JLabel(" Min Pressure (Pmin)");
     outlbl_val[0]=new JLabel("0.0 kN/m²", JLabel.LEFT); // iLabel[0].setForeground(Color.blue);
     in1.add(lab);
     in1.add(outlbl_val[0]);
    
     in2 = new JPanel(new java.awt.GridLayout(2,2,10,10)); 
     in2.setBackground(bk);
     
     lab1=new JLabel(" ",JLabel.LEFT);
     lab=new JLabel(" ");
     in2.add(lab1);
     in2.add(lab);
     
     lab=new JLabel("Max Pressure (Pmax)");
     in2.add(lab);
     outlbl_val[1]=new JLabel("0.0 kN/m²", JLabel.LEFT);;
     in2.add(outlbl_val[1]);
     createInputOutputPanel.add(in2);
    	createInputOutputPanel.setVisible(false);
		return createInputOutputPanel;
        
    }


    
    private void bottomPanel()
    {
    	bottomPanel.setVisible(false); 
           
    }
    
 
 
   
    
    private void initInputControlsField(){
    	
    	
    	iLabel = new JLabel[3];
    	
       	iLabel[0] = new JLabel("5.0 m", JLabel.LEFT); iLabel[0].setForeground(Color.blue);
       	iLabel[1] = new JLabel("1.0 m", JLabel.LEFT); iLabel[1].setForeground(Color.blue);
    	iLabel[2] = new JLabel("4 m", JLabel.LEFT); iLabel[2].setForeground(Color.blue);
       	
       
       	fields = new float[3];
       	fields[0]=5.0f;
       	fields[1]=1.0f;
    	fields[2]=4.0f;      
     

    }


    private void onNextStage()
    {
    	    	
    	valChange = true; // Clear the graph. or Graph will restart on Play    	
   // 	resetOutputParameters(); // Clear the Output Parameters
    	createInputOutputPanel.setVisible(true);
    	enableStage(stage);
    	setInstructionText();
   		 
    }
    private void enableStage(int s){
    	switch(s){
    	
    		
    	case 0:
    		enable(in1,false);	 		enable(in2,false);    		enable(in3,true);
    		s_hl.setEnabled(false);s_vl.setEnabled(false); 
                stagei=1;
            
    		break;
    	
    	case 1: // Home 
    		enable(in1,false);	   		enable(in2,false);    		enable(in3,true);
    	//	s_hl.setEnabled(false);
    		s_hl.setEnabled(false);s_vl.setEnabled(false); 
                stagei=1;
    		break;
    	
    	
    	case 2:
    		enable(in1,false);	   		enable(in2,false);    		enable(in3,true);	
    		   stagei=2;
    		   s_hl.setEnabled(true);s_vl.setEnabled(true); 	
    		break;
    	
    	case 3: // Home 
    		enable(in1,true);    		enable(in2,true);    		enable(in3,true);
    	        stagei=3;
    	        s_hl.setEnabled(true);s_vl.setEnabled(true); 
    		//nextButton.setVisible(false);
    		break;
    	case 4: // Home 
    		enable(in1,true);    		enable(in2,true);    		enable(in3,true);
    	        stagei=3;
    	        s_hl.setEnabled(true);s_vl.setEnabled(true); 
    		nextButton.setVisible(false);
    		break;
    	}
    	
    }
    
    private void setInstructionText()
    {
    	    	
    	switch(stage){
    	case 0: // Home 
    		m_Objective.setText(" ");
    		m_Objective.setForeground(Color.WHITE);
    		    		break;
    	case 1:
    		m_Objective.setText(" ");
    		m_Objective.setForeground(Color.GREEN);
    		    		break;
    	case 2:
    		m_Objective.setText(" ");
    		m_Objective.setForeground(Color.WHITE);
    					break;
    	case 3:
    		m_Objective.setText(" ");
    		m_Objective.setForeground(Color.GREEN);
    		  			break;
    	case 4:
    		m_Objective.setText(" ");
    		m_Objective.setForeground(Color.WHITE);
    					break;

    	}
    		
   		 
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
      
//    	if (!rightPanel.isVisible()){
//    		rightPanel.setVisible(true);
//    		bottomPanel.setVisible(true);
//    	}
    	ImageIcon icon = m_j3d.createImageIcon("resources/icons/stop.png"); 
    	startButton.setIcon(icon);
    	startButton.setText("Stop");
    	enableStage(-1);    	
        reStartButton.setEnabled(false);
        nextButton.setEnabled(false);
    

        if(valChange){

              int slwt=Integer.parseInt(soil_wt1);
              int maswt=Integer.parseInt(mas_wt1);
              int angle=Integer.parseInt(ang_res1);
              System.out.println(ang_res1);
              String pstp=psr_tp1;
              int un = Integer.parseInt(uni_sur1);
                      int shl = Integer.parseInt(s_hl1);
                      int svl = Integer.parseInt(s_vl1);
                     
       //    System.out.println("iam here ");
	        freeBody.Init (fields[0],fields[1],fields[2],slwt,maswt,angle,pstp,un,shl,svl,stage);
	       
        }
        
        timer.start();
        System.out.println("Timer started");     
    }
    
   
    
    // Resume Button Action
    private void timerActionPerformed(java.awt.event.ActionEvent evt)
    {
      int i=0;
    	
//      Formatter fmt;
//
//      fmt = new Formatter();
//
//      fmt.format("%1.4f", freeBody.getPmin());
//    	double min=freeBody.getPmin();
//    	double max=freeBody.getPmax();
    	
      if(String.valueOf(freeBody.getPmin()).length()>4)                                 
        	outlbl_val[i++].setText(String.valueOf(freeBody.getPmin()).substring(0,7) + " kN/m²");
        
        if(String.valueOf(freeBody.getPmax()).length()>4)                                 
          	outlbl_val[i++].setText(String.valueOf(freeBody.getPmax()).substring(0,7) + " kN/m²");
     
        
//        outlbl_val[0].setText(String.valueOf(freeBody.getPmin()) + "kN/m²");
//      //fmt.format("%1.4f", freeBody.getPmax());
//       outlbl_val[1].setText(String.valueOf(freeBody.getPmax()) + "kN/m²"); 
        
   	if(stage==0)
	{
   	     rightIcon.setIcon(m_j3d.createImageIcon("resources/CA/RA1.jpg")); 
		java.util.BitSet visibleNodes = new java.util.BitSet( switchGroup1.numChildren() );
	 	   visibleNodes.set( 0); 
	 	  visibleNodes.set( 1);
	 	
	 	   
	 	  switchGroup1.setChildMask( visibleNodes );
	}
	else if(stage==1)
	{
		 rightIcon.setIcon(m_j3d.createImageIcon("resources/CA/RA2.jpg")); 
		java.util.BitSet visibleNodes = new java.util.BitSet( switchGroup1.numChildren() );
	 	   visibleNodes.set( 2); 
	 	  visibleNodes.set( 3);
	 	
	 	   
	 	  switchGroup1.setChildMask( visibleNodes );
	}
	else if(stage==2)
	{
		 rightIcon.setIcon(m_j3d.createImageIcon("resources/CA/RA3.jpg")); 
	java.util.BitSet visibleNodes = new java.util.BitSet( switchGroup1.numChildren() );
 	   visibleNodes.set( 4); 
 	  visibleNodes.set( 5);
 	
 	   
 	  switchGroup1.setChildMask( visibleNodes );
}
	else if(stage==3)
	{
		 rightIcon.setIcon(m_j3d.createImageIcon("resources/CA/RA4.jpg")); 
	java.util.BitSet visibleNodes = new java.util.BitSet( switchGroup1.numChildren() );
 	   visibleNodes.set( 6); 
 	  visibleNodes.set( 7);
 	
 	   
 	  switchGroup1.setChildMask( visibleNodes );
}
        if(freeBody.isDataCompleted()) {
        	 pauseSimulation();
        	 System.out.println("Data Completed");    
        	return;
        }
           
        
        return;            
    }
    
   
    private void updateSimulationBody(float disp,float dispTMD,float factor)
    {
    	Shape3D shape = (Shape3D)hm.get("block1");
    	//	shape.setGeometry(m_j3d.createBoxGeom(disp,(Vector3f)(hm.get("scale2"))));
        shape.setGeometry(m_j3d.createBoxGeom(disp*factor,new Vector3f(.3f,.4f,.3f)));
        
    	shape = (Shape3D)hm.get("block2");
	//	shape.setGeometry(m_j3d.createBoxGeom(disp,(Vector3f)(hm.get("scale2"))));
    	shape.setGeometry(m_j3d.createBoxGeom(dispTMD*factor,new Vector3f(.3f,.4f,.3f)));
    	
    	TransformGroup tgp = (TransformGroup)hm.get("roof2");
    	Transform3D trans = new Transform3D();
    	tgp.getTransform(trans);
    	trans.setTranslation(new Vector3d(.43+ (dispTMD*factor),0.17, -.1)); 
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
//		outputGraph.setState(0);
//		inputGraph.setState(0);
//		outputGraph2.setState(0);
        //startButton.setEnabled(true);
		       
        valChange = false;
         
		repaint();
    }
    
}

    
   



class RetainingWallsBody1 {
	
	double a;
	double b;
	double h;
	double sw;
	double mw;
	double phi;
	double K;
	double sur;
	double y;
	double lp1,lp2;
	double P,h1,W,x,z,e;
	double a1,W1,W2,W3,W4,d1,d2,d3,d4,Mlp,TM;
	double sv,sh,alp,Cp,Ph,Pv,M,V;
	double Pmin,Pmax;

         public void Init (float fields0,float fields1,float fields2,int slwt,int maswt,int angle,String string,int un,int shl,int svl,int i)
         {
             i=1;
        System.out.println("length = " + fields0 + " breadth =" + fields1 + " depth= " + fields2 + "\n soil wt= " + slwt + "massionary wt= " + maswt + "phi= " + angle + "pressure type" + string + "un " + un + "sh1 " + shl + "sv1 " + svl);
//	      a=fields0;
//        b=fields1;
//        h=fields2;
        
        a=fields1;
        b=fields2;
        h=fields0;
        sw=slwt;
        mw=maswt;
        phi=angle;
        
             String st=string;
	if (st=="Active")
	{
		K= (1-Math.sin(phi*3.141/180))/(1+Math.sin(phi*3.141/180));
	}
	if (st=="Passive")
	{
		K= (1+Math.sin(phi*3.141/180))/(1-Math.sin(phi*3.141/180));
	}
	if (i==1)
	{
		if (y==0)
			sur=10;
	    else
		    sur=0;
		
		    lp1 = sur*K;
		    lp2 = (sur + sw*h)*K;
		    
		    P = (h/2)*(lp1 + lp2);
		    h1 = ((lp2 + 2*lp1)/(lp2 + lp1))*(h/3);
		    W = (h/2)*(a + b)*mw;
		    x = (a*a + a*b + b*b)/(3*(a+b)); //
		    z = x + h1*P/W;
		    e = z - b/2;
	
	}
	if (i==2)
	{
		    sv =1.0;
		    sh =10.0;
		    
		    a1 = sv*h/sh;
		    W1 = 0.5*a1*h*mw;
		    d1 = (2/3.0)*a1;
		    W2 = a*h*mw;
		    d2 = a1 + a/2;
		    W3 = 0.5*(b-a1-a)*h*mw;
		    d3 = a1 + a + (1/3)*(b-a1-a);
		    W4 = 0.5*a1*h*sw;
		    d4 = (1/3)*a1;
		    
		    Mlp = sw*h*h*h*K/6;
		    
		    W = W1+W2+W3+W4;
		    TM = W1*d1 + W2*d2 + W3*d3 + W4*d4 + Mlp;
		    
		    z = TM/W;
		    e = z - b/2;		    
		  
		
	}
	if (i==3)
	{
		    sv =1.0;
		    sh =2.0;
		    
		    alp = Math.atan(sv/sh);
                   
                     
            
		    Cp =Math.cos(alp)*((Math.cos(alp) - Math.sqrt(Math.cos(alp)*Math.cos(alp)- (Math.cos(phi*3.141/180)*Math.cos(phi*3.141/180))))/(Math.cos(alp) + Math.sqrt(Math.cos(alp)*Math.cos(alp)- (Math.cos(phi*3.141/180)*Math.cos(phi*3.141/180)))));
		   
                    P = Cp*sw*h*h/2;
		    Ph = P*Math.cos(alp);
		    Pv = P*Math.sin(alp);
		    W = mw*(a+b)*h/2; 
		    x = (a*a + a*b + b*b)/(3*(a+b));
		    M = (W*x + Ph*h/3);
		    V = (W + Pv);
		    z = M/V;
		    e = z - b/2;
		    W = V;
		
	}
	
		 //   display('change dimensions because e>b/6 ');

if ( e > (b/6))
{
	System.out.println("e = " + e + "b/6 = " + b/6);
                 JOptionPane.showMessageDialog (null, "Change Dimensions Because e>b/6", "Error", JOptionPane.ERROR_MESSAGE);
     
}
else
{
	   Pmax = (W/b)*(1 + 6*e/b);
	    Pmin = (W/b)*(1 - 6*e/b);
	    System.out.println("Pmin :" + Pmin + "Pmax :" + Pmax);
}	
	
         }
         
         public double getPmax()
         {
             return Pmax;
         }
         public double getPmin()
         {
            return Pmin;
         }
public boolean isDataCompleted() {
	// TODO Auto-generated method stub
	return false;
}
}


