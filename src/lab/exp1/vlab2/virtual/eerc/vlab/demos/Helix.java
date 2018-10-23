package eerc.vlab.demos;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
//import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
//import java.awt.GridBagConstraints;
//import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.URL;
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
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;

import javax.media.j3d.Shape3D;
import javax.media.j3d.Switch;

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
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.omg.CORBA.Bounds;

import eerc.vlab.common.FullViewGraph;
import eerc.vlab.common.HelpWindow;
import eerc.vlab.common.HorizontalGraph;
import eerc.vlab.common.HorizontalGraphWrapper;
import eerc.vlab.common.ImagePanel;
import eerc.vlab.common.J3DShape;
import eerc.vlab.common.Resources;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;


public class Helix extends javax.swing.JPanel {
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
	
	
	private JComboBox spr_mat;
	private JSlider m_Slider[] = new JSlider[4];
	private JLabel spr_material;
	
	private javax.swing.JButton rightIcon=null;

	//private GraphPlotter         graphPlotter;
	////////////////////////////Java3D componenet ///////////////////////////

	private SimpleUniverse      univ = null;                  // Simple Universe Java3D
	private BranchGroup         scene = null;                 // BranchGroup Scene graph
	TransformGroup objSwitchPos = new TransformGroup(new Transform3D());
	
	private Switch objSwitch = new Switch();
	Appearance appea = new Appearance();
	
	private HelixBody       freeBody =null;               // Shape3D
	private HorizontalGraph		outputGraph =null;
	private HorizontalGraph		outputGraph2 =null;
	private FullViewGraph  		fullViewGraph = new FullViewGraph();
	

	@SuppressWarnings("unchecked")
	private HashMap 			hm = new HashMap();
	private J3DShape 			m_j3d	= new J3DShape();

	private double fields[];
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
	
//	private JComboBox ch;
//	private JComboBox che;
//	private JLabel lbl_k;
	
	
//	private String[] units ={" (m) "," (m) "," (mm) "," (Kg/m^3) ",
//							 " (m) "," (mm) "," (mm) ","",
//							 " (m/s) "," (mm) "," (%) "};
	
	public BranchGroup createSceneGraph() 
	{
		// Create the root of the branch graph
		BranchGroup objRoot = new BranchGroup();
		objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND );
		objRoot.setCapability(Group.ALLOW_CHILDREN_READ);
		objRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
		objRoot.setCapability( BranchGroup.ALLOW_DETACH );
		
		
		
		objRoot.addChild(createVirtualLab());
		
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
		objRoot.addChild(m_j3d.createBox(new Vector3d(1,0.1f,0), new Vector3d(0.05,0.7f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 0.9f),"resources/images/380.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(-1,0.1f,0), new Vector3d(0.05,0.7f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 0.9f),"resources/images/380.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.1f,-2.0), new Vector3d(1,0.7f,0.05),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/377.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.84f,0), new Vector3d(1.05,0.04f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/380.jpg"));
		
	
		
		float rad = (float)Math.PI/180;
		Transform3D t = new Transform3D();
		t.setScale(0.4);
		t.invert();
		TransformGroup tg = new TransformGroup();
		t = new Transform3D();
		t.rotX(rad*10);
		t.setScale(new Vector3d(.5f,.05f,.5f));        
		t.setTranslation(new Vector3d(.3,.3,0));
		tg.setTransform(t);
		freeBody = new HelixBody();    
	    
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
		
		int i;
		TransformGroup spring1 = new TransformGroup();
		TransformGroup spring2 = new TransformGroup();
		TransformGroup spring3 = new TransformGroup();
	    TransformGroup spring4 = new TransformGroup();
		TransformGroup spring5 = new TransformGroup();
		TransformGroup spring6 = new TransformGroup();
		TransformGroup spring7 = new TransformGroup();
		
		
//		TransformGroup springPos[][][][] = new TransformGroup[4][3][6][3];
				
		spring1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		spring1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);		
		
		spring2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		spring2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		spring3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		spring3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		spring4.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		spring4.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		spring5.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		spring5.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		spring6.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		spring6.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		spring7.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		spring7.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
	    Transform3D t = new Transform3D();
        BranchGroup objroot = new BranchGroup();
        
	    TransformGroup objtrans = new TransformGroup(t);
	    objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);	    
	    
	    // Create alpha that continuously cycles with a period
	    
	   /* Alpha alpha1 = new Alpha (-1,
                Alpha.INCREASING_ENABLE + Alpha.DECREASING_ENABLE,
                0,20, 400, 20, 20, 400, 40, 20);*/
	    
                           
        objroot.addChild(objtrans);
        //objtrans.addChild(spring);
        
	    
	    float H;
	    H = 0.36f;
	    final float L0 = 0.36f;  // Don't Change this value
	 // Load and Spring Part :
	    spring1.addChild(m_j3d.createBox(new Vector3d(0.0f,(L0-H)-0.1f,0),new Vector3d(0.05f,0.04f,0.05f), new Vector3d(0.0f,0.0f,0.0f),new Color3f(1.0f,1.0f,1.0f),"resources/images/table.jpg"));
	    spring1.addChild(createHelix(new Vector3d(0,0.14f,0),new Vector3d(1,1,1),new Vector3d(0,0,0),new Color3f(0,0,0),(float)(fields[1]/2),(float)fields[2],(float)fields[0],H));
	    H = 0.352f;
	    spring2.addChild(m_j3d.createBox(new Vector3d(0.0f,(L0-H)-0.1f,0),new Vector3d(0.05f,0.04f,0.05f), new Vector3d(0.0f,0.0f,0.0f),new Color3f(1.0f,1.0f,1.0f),"resources/images/table.jpg"));
	    spring2.addChild(createHelix(new Vector3d(0,0.14f,0),new Vector3d(1,1,1),new Vector3d(0,0,0),new Color3f(0,0,0),(float)(fields[1]/2),(float)fields[2],(float)fields[0],H));
	    H = 0.341f;
	    spring3.addChild(m_j3d.createBox(new Vector3d(0.0f,(L0-H)-0.1f,0),new Vector3d(0.05f,0.04f,0.05f), new Vector3d(0.0f,0.0f,0.0f),new Color3f(1.0f,1.0f,1.0f),"resources/images/table.jpg"));
	    spring3.addChild(createHelix(new Vector3d(0,0.14f,0),new Vector3d(1,1,1),new Vector3d(0,0,0),new Color3f(0,0,0),(float)(fields[1]/2),(float)fields[2],(float)fields[0],H));
	    H = 0.333f;
	    spring4.addChild(m_j3d.createBox(new Vector3d(0.0f,(L0-H)-0.1f,0),new Vector3d(0.05f,0.04f,0.05f), new Vector3d(0.0f,0.0f,0.0f),new Color3f(1.0f,1.0f,1.0f),"resources/images/table.jpg"));
	    spring4.addChild(createHelix(new Vector3d(0,0.14f,0),new Vector3d(1,1,1),new Vector3d(0,0,0),new Color3f(0,0,0),(float)(fields[1]/2),(float)fields[2],(float)fields[0],H));
	    H = 0.33f;
	    spring5.addChild(m_j3d.createBox(new Vector3d(0.0f,(L0-H)-0.1f,0),new Vector3d(0.05f,0.04f,0.05f), new Vector3d(0.0f,0.0f,0.0f),new Color3f(1.0f,1.0f,1.0f),"resources/images/table.jpg"));
	    spring5.addChild(createHelix(new Vector3d(0,0.14f,0),new Vector3d(1,1,1),new Vector3d(0,0,0),new Color3f(0,0,0),(float)(fields[1]/2),(float)fields[2],(float)fields[0],H));
	    H = 0.338f;
	    spring6.addChild(m_j3d.createBox(new Vector3d(0.0f,(L0-H)-0.1f,0),new Vector3d(0.05f,0.04f,0.05f), new Vector3d(0.0f,0.0f,0.0f),new Color3f(1.0f,1.0f,1.0f),"resources/images/table.jpg"));
	    spring6.addChild(createHelix(new Vector3d(0,0.14f,0),new Vector3d(1,1,1),new Vector3d(0,0,0),new Color3f(0,0,0),(float)(fields[1]/2),(float)fields[2],(float)fields[0],H));
	    H = 0.35f;
	    spring7.addChild(m_j3d.createBox(new Vector3d(0.0f,(L0-H)-0.1f,0),new Vector3d(0.05f,0.04f,0.05f), new Vector3d(0.0f,0.0f,0.0f),new Color3f(1.0f,1.0f,1.0f),"resources/images/table.jpg"));
	    spring7.addChild(createHelix(new Vector3d(0,0.14f,0),new Vector3d(1,1,1),new Vector3d(0,0,0),new Color3f(0,0,0),(float)(fields[1]/2),(float)fields[2],(float)fields[0],H));
	    	    
	    	    
	  
	    
	    // Holder
	    objtrans.addChild(m_j3d.createBox(new Vector3d(0.0f,-0.2f,0.0f),new Vector3d(0.4f,0.02f,0.4f), new Vector3d(0.0f,0.0f,0.0f),new Color3f(1.0f,1.0f,1.0f),"resources/images/wood2.jpg"));
	    objtrans.addChild(m_j3d.createCylinder(new Vector3d(0.2,0.1f,0),new Vector3d(0.2,6.5f,0.3),new Vector3d(0,0,0),new Color3f(0.6f,0.6f,0.6f)));
	    objtrans.addChild(m_j3d.createCylinder(new Vector3d(-0.2,0.1f,0),new Vector3d(0.2,6.5f,0.3),new Vector3d(0,0,0),new Color3f(0.6f,0.6f,0.6f)));
	    // Legs
	    objtrans.addChild(m_j3d.createCylinder(new Vector3d(0.35,-0.35f,-0.35),new Vector3d(0.3,3.0f,0.3),new Vector3d(0,0,0),new Color3f(0.4f,0.2f,0.0f)));
	    objtrans.addChild(m_j3d.createCylinder(new Vector3d(-0.35,-0.35f,-0.35),new Vector3d(0.3,3.0f,0.3),new Vector3d(0,0,0),new Color3f(0.4f,0.2f,0.0f)));
	    objtrans.addChild(m_j3d.createCylinder(new Vector3d(-0.35,-0.35f,0.35),new Vector3d(0.3,3.0f,0.3),new Vector3d(0,0,0),new Color3f(0.4f,0.2f,0.0f)));
	    objtrans.addChild(m_j3d.createCylinder(new Vector3d(0.35,-0.35f,0.35),new Vector3d(0.3,3.0f,0.3),new Vector3d(0,0,0),new Color3f(0.4f,0.2f,0.0f)));
	    
	    // 	 Joining the Helix with the Holder	    	   
	    Color3f grey = new Color3f(0.5f,0.5f,0.5f);    
	    
	    LineAttributes la = new LineAttributes();
	    la.setLineWidth(8.0f);
	    ColoringAttributes ca = new ColoringAttributes(grey, ColoringAttributes.SHADE_FLAT);
	    Appearance app = new Appearance();
	    app.setColoringAttributes(ca);
	    app.setLineAttributes(la);
	    Point3f[] coords = new Point3f[2];
	    coords[0] = new Point3f(-0.2f,0.4f,0.0f);
	    coords[1] = new Point3f(0.2f,0.4f,0.0f);
	    LineArray line = new LineArray(2, LineArray.COORDINATES);
	    line.setCoordinates(0, coords);
        Shape3D myShape = new Shape3D(line,app);
        objtrans.addChild(myShape);       
        
        objSwitch = new Switch(Switch.CHILD_MASK);
        objSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
        
     // create switch value interpolator
              
        objroot.addChild(objSwitchPos);        
        objSwitch.addChild(spring1);
        objSwitch.addChild(spring2);
        objSwitch.addChild(spring3);
        objSwitch.addChild(spring4);
        objSwitch.addChild(spring5);
        objSwitch.addChild(spring6);
        objSwitch.addChild(spring7);
        objSwitchPos.addChild(objSwitch);       
        
      //  objroot.addChild(swiInt);
      //  swiInt.setLastChildIndex(6);       
	    return objroot;
	}  
	public Group createHelix(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr, float Radius, float Turns, float wireDia, float H){
    	// Setting the 6 degrees of freedom and Scale
    	
        final float L0 = 0.36f;  // Don't Change this value
        pos.y = pos.y+(L0-H)/2;
    	Transform3D t = new Transform3D();
        float rad = (float)Math.PI/180;
 		if(rot.x != 0)
 			t.rotX(rad*rot.x);
 		else if(rot.y != 0)
 			t.rotY(rad*rot.y); 
 		else if(rot.z != 0)
 			t.rotZ(rad*rot.z);
        t.setScale(scale);        
        t.setTranslation(pos);
        
       // t.setTranslation(vib);
        
        // Creating The Helix
        Radius = Radius/1000;
        wireDia = wireDia/10;
        
        TransformGroup objtrans = new TransformGroup(t);
        objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);       
	   	
	    LineAttributes la = new LineAttributes();
	    
	    la.setLineWidth(wireDia);
	  //  ColoringAttributes ca = new ColoringAttributes(colr, ColoringAttributes.SHADE_FLAT);
	 //   appea.setColoringAttributes(ca);
	    appea.setLineAttributes(la);
	    float i;
	    Point3f[] coords = new Point3f[2];
	    for(i=-Turns*H*200;i<Turns*H*200;i++)
	    {
	    	coords[0] = new Point3f((float)(Radius*Math.sin(i*3.14/(200*H))),i/(400*Turns),(float)(Radius*Math.cos(i*3.14/(200*H))));
	    	coords[1] = new Point3f((float)(Radius*Math.sin((i+1)*3.14/(200*H))),(i+1)/(400*Turns),(float)(Radius*Math.cos((i+1)*3.14/(200*H))));
	    	LineArray line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        Shape3D myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	    }	    
	    coords[0] = new Point3f(-0.002f,H/2,Radius);
	    coords[1] = new Point3f(0,H/2+0.08f,0.0f);
	    LineArray line = new LineArray(2, LineArray.COORDINATES);
	    line.setCoordinates(0, coords);
        Shape3D myShape = new Shape3D(line,appea);
        objtrans.addChild(myShape);
        
        coords[0] = new Point3f(0.002f,-H/2,Radius);
	    coords[1] = new Point3f(0,-H/2-0.02f,0.0f);
	    line = new LineArray(2, LineArray.COORDINATES);
	    line.setCoordinates(0, coords);
        myShape = new Shape3D(line,appea);
        objtrans.addChild(myShape);
        return objtrans;
    } 
     
    
    /**
     * Creates new form FreeVibration
     */
    public Helix(Container container) {
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
		Helix mainPanel;

        public void init() {
            setLayout(new BorderLayout());
            mainPanel = new Helix(this);
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
            setTitle("Helix Responce Body Applet");
            getContentPane().add(new Helix(this), BorderLayout.CENTER);
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
        
        objSwitchPos.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        appea.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        appea.setCapability(Appearance.ALLOW_LINE_ATTRIBUTES_WRITE);
        ColoringAttributes ca = new ColoringAttributes(new Color3f(0,0,0), ColoringAttributes.SHADE_FLAT);
      	appea.setColoringAttributes(ca);
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
        
        timer = new Timer(200,new ActionListener() {
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
        reStartButton.setEnabled(true);
        nextButton.setEnabled(true);
        
        
        
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
            //    outputGraph2.clearGraphValue();
                
                valChange = true;
                startSimulation(evt);
                univ.getCanvas().repaint();
               
                
//            	reStartButton.setEnabled(false);
//                //startButton.setEnabled(true);
//                startButton.setText("Start");
//                startStop = false;
//                timer.stop();
//                outputGraph.clearGraphValue();
//                outputGraph2.clearGraphValue();
//                
//                valChange = true;
                
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

                //frame.setMaximizedBounds(e.getMaximumWindowBounds());
                //frame.setSize(e.getMaximumWindowBounds().width, e.getMaximumWindowBounds().height);
                
                //Display the window.
                frame.pack();
                frame.setVisible(true);
                //frame.setResizable(false);
                
                fullViewGraph =new FullViewGraph(graph,max,magX,magY,frame.getWidth()-20, frame.getHeight());
                frame.add(fullViewGraph);   //Pradeep: added
           //     System.out.println("w " + frame.getWidth() + " h " + frame.getHeight());
                
                
            }
          });
        
      
      guiPanel.add(reStartButton, gridBagConstraints);
      guiPanel.add(startButton, gridBagConstraints);
      guiPanel.add(nextButton, gridBagConstraints);
        
      btn= new javax.swing.JButton("Manual"); 
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
        
    	 rightPanel.setLayout(new java.awt.GridLayout(2,1,0,1));
         
         outputGraph2 = new HorizontalGraph(300,280,"t","u''(t)");
         outputGraph2.setHeading("Input Ground Motion ");
         outputGraph2.setAxisUnit("sec","g");
         outputGraph2.setYAxisColor(Color.GREEN);
         outputGraph2.setYScale(100);
         outputGraph2.fitToYwindow(false);
      
//         rightPanel.setLayout(new java.awt.BorderLayout());
//  
//        JPanel panel = new JPanel();
//        panel.setBackground(new Color(140,200,240));
//      
//        ImageIcon icon = m_j3d.createImageIcon("resources/Data/Helix/stepforce1.jpg"); 
//      
//       
//        rightIcon=new javax.swing.JButton(" ");
//        rightIcon.setIcon(icon);
//        panel.add(rightIcon);
//        
//        panel.setPreferredSize(new Dimension(300,175));
//    //    rightTop.setBackground(new Color(200,200,100));
//        rightPanel.add(panel,BorderLayout.NORTH);
//
//        JPanel rightBottom = new JPanel();
//        rightBottom.setPreferredSize(new Dimension(300, 295));
//  //      rightBottom.setBackground(new Color(100,200,100));
       
        
        
        
        outputGraph = new HorizontalGraph(300,280,"t ","u(t)"); 
        outputGraph.setHeading("Displacement Response ");
        outputGraph.setAxisUnit("sec","m");
        outputGraph.setYAxisColor(Color.BLUE);
        outputGraph.setYScale(500);
        outputGraph.fitToYwindow(true);        
        HorizontalGraphWrapper wrapper = new HorizontalGraphWrapper(outputGraph,1000,2,Color.GRAY);
        
        rightPanel.add(outputGraph);     //added wrapper instead of outputGraph
        
    //    rightPanel.add(outputGraph2);
        
       
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
         JLabel lbl = new JLabel("Helix Responce of SDOF ", JLabel.CENTER);
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
                         //...Perform a task...
                    	 timerActionVerticalCameraMotion(evt);
                     }
                 });
            	 m_cameraTimer.start();
            	 
             }
           });
         
         guiPanel.add(viewButton, gridBagConstraints);
         
         JCheckBox chkbox = new JCheckBox("");
         lbl = new JLabel("Change Input Parameters", JLabel.CENTER);
         //lbl.setFont(new Font("Arial", Font.BOLD, 18));
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
//         guiPanel.add(createInputOutputPanel());
//         btmPanel.add(guiPanel,BorderLayout.SOUTH);
         
        
         

    }
    
    
//    private JPanel createInputOutputPanel(){
//    	
//    	JPanel ioparm = new JPanel(new java.awt.GridLayout(1,3));
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
//    	
//    	return  ioparm;
//        
//    }


    private void bottomPanel()
    {
    	   initInputControlsField();
           
    	   Color bk = new Color(219,226,238);
           bottomPanel.setLayout(new java.awt.GridLayout(1,3));
           bottomPanel.setBackground(Color.black);
           bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(235,233,215),8));
           
           in1 = new JPanel(new java.awt.GridLayout(3,3));
           in1.setBackground(bk);
           bottomPanel.add(in1);

           in2 = new JPanel(new java.awt.GridLayout(3,3)); 
           in2.setBackground(bk);

           bottomPanel.add(in2);

           in3 = new JPanel(new java.awt.GridLayout(3,2)); 
           in3.setBackground(bk);
           bottomPanel.add(in3);
           
           JLabel lab_new1 = new JLabel("Geometry :", JLabel.LEFT);
           JLabel lab_new2 = new JLabel(" Helix     ", JLabel.LEFT);
           JLabel lab_new3 = new JLabel("      ", JLabel.LEFT);
           in1.add(lab_new1);
           in1.add(lab_new2);
           in1.add(lab_new3);
        
           
           JLabel lab = new JLabel("Diameter of Wire ", JLabel.CENTER);
           m_Slider[0] = new JSlider(JSlider.HORIZONTAL,10, 30, 20);
           m_Slider[0].setMajorTickSpacing(10);
           m_Slider[0].setPaintTicks(true);
           m_Slider[0].setPaintLabels(true);
           m_Slider[0].addChangeListener(new ChangeListener() {
   		    public void stateChanged(ChangeEvent e) {
               valChange = true;
               int val = ((JSlider) e.getSource()).getValue();
               fields[0]=val;
              
               iLabel[0].setText(":: " + fields[0] + " mm");
              
           //    univ.getCanvas().repaint();
               repaint();            
            }
            });
           m_Slider[0].setBackground(bk);
           in1.add(lab);
           in1.add(m_Slider[0]);
           in1.add(iLabel[0]);
           
           
           lab = new JLabel("Diameter of Spring", JLabel.CENTER);
           m_Slider[1] = new JSlider(JSlider.HORIZONTAL,50, 200, 100);
           m_Slider[1].setMajorTickSpacing(50);
           m_Slider[1].setPaintTicks(true);
           m_Slider[1].setPaintLabels(true);
           m_Slider[1].addChangeListener(new ChangeListener() {
   		    public void stateChanged(ChangeEvent e) {
               valChange = true;
               int val = ((JSlider) e.getSource()).getValue();
               fields[1]=val;
              
               iLabel[1].setText(":: " + fields[1] + " cm");
              // univ.getCanvas().repaint();
               repaint();            
            }
            });
           m_Slider[1].setBackground(bk);
           in1.add(lab);
           in1.add(m_Slider[1]);
           in1.add(iLabel[1]);
       
                     
           lab = new JLabel("Number of Turns ", JLabel.RIGHT);
           
           m_Slider[2] = new JSlider(JSlider.HORIZONTAL,10, 20, 14);
           m_Slider[2].setMajorTickSpacing(2);
           m_Slider[2].setPaintTicks(true);
           m_Slider[2].setPaintLabels(true);
           m_Slider[2].addChangeListener(new ChangeListener() {
                  public void stateChanged(ChangeEvent e) {
               	   valChange = true;
               	   int val = ((JSlider) e.getSource()).getValue();
               	   fields[2]=val;
                   iLabel[2].setText(":: " + fields[2] + " turns");
	            //   univ.getCanvas().repaint();
   	               repaint();               
   	               }
           });
           m_Slider[2].setBackground(bk);
           in2.add(lab);
           in2.add(m_Slider[2]);
           in2.add(iLabel[2]);
           iLabel[2].setForeground(Color.BLUE);
           
         
           
           spr_material = new JLabel("Spring Material", JLabel.RIGHT);
          
           spr_mat = new JComboBox();
           spr_mat.addItem("Stainless Steel");         spr_mat.addItem("Steel Cast");
           spr_mat.addItem("Copper");         spr_mat.addItem("Aluminium");
           spr_mat.addItem("Gold");
       
               
           
           spr_mat.addActionListener(new java.awt.event.ActionListener() {
	   			 public void actionPerformed(ActionEvent e) {
	   				 valChange = true;
	   				 JComboBox cb = (JComboBox)e.getSource();
	   				
	   				 String obj = (String)cb.getSelectedItem();
	   				 float val = getMaterialVal(obj);
	   				 fields[3]=val;		           
	   				// univ.getCanvas().repaint();
	   				 repaint();
	   			 }
 			 });
        
           in2.add(spr_material);
           in2.add(spr_mat);
           in2.add(iLabel[3]);
	        bottomPanel.setVisible(false);
	        
	        lab = new JLabel("Load ", JLabel.RIGHT);
	           
	           m_Slider[3] = new JSlider(JSlider.HORIZONTAL,0, 100, 50);
	           m_Slider[3].addChangeListener(new ChangeListener() {
	                  public void stateChanged(ChangeEvent e) {
	               	   valChange = true;
	               	   int val = ((JSlider) e.getSource()).getValue();
	               	   fields[4]=val;
	           //        iLabel[4].setText(":: " + fields[4]+ " kN");
		             //  univ.getCanvas().repaint();
	   	               repaint();               
	   	               }
	           });
	           m_Slider[3].setBackground(bk);
	           in2.add(lab);
	           in2.add(m_Slider[3]);
	           in2.add(iLabel[4]);
	           iLabel[4].setForeground(Color.BLUE);
	           
	           outlbl_val = new JLabel[3];
	           lab = new JLabel("Defelection (mm) ", JLabel.RIGHT);
	           outlbl_val[0] = new JLabel(" 0 ", JLabel.RIGHT);
	           in3.add(lab);
	           in3.add(outlbl_val[0]);
	           
	           
	           lab = new JLabel("Stiffness (kN/m) ", JLabel.RIGHT);
	           outlbl_val[1] = new JLabel(" 0 ", JLabel.RIGHT);
	           in3.add(lab);
	           in3.add(outlbl_val[1]);
	           
	           lab = new JLabel("Strain Energy (kN-m)", JLabel.RIGHT);
	           outlbl_val[2] = new JLabel(" 0 ", JLabel.RIGHT);
	           in3.add(lab);
	           in3.add(outlbl_val[2]);
	        
            /////////// Enable/Disable function for Input parameters
            enable(in1,false);
            enable(in2,false);
            enable(in3,false);
    }
   
  protected float getMaterialVal(String obj) {
	if (obj.equals("Stainless Steel"))
		return 77.2f;
	else if (obj.equals("Steel Cast"))
		return 78.0f;
	else if (obj.equals("Copper"))
		return 48.0f;
	else if (obj.equals("Aluminium"))
		return 26.0f;
	else if (obj.equals("Gold"))
		return 27.0f;
	return 77.2f;
}

private void initInputControlsField(){
    	
    	
    	iLabel = new JLabel[5];
       	int i=0;
       	iLabel[i] = new JLabel(" cm ", JLabel.LEFT); iLabel[i++].setForeground(Color.blue);
       	iLabel[i] = new JLabel(" mm ", JLabel.LEFT); iLabel[i++].setForeground(Color.blue);
    	iLabel[i] = new JLabel("", JLabel.LEFT); iLabel[i++].setForeground(Color.blue);
       	iLabel[i] = new JLabel("77.2 kN(m^3)", JLabel.LEFT); iLabel[i++].setForeground(Color.blue);
    	iLabel[i] = new JLabel("50 kN", JLabel.LEFT); iLabel[i++].setForeground(Color.blue);
       
       	i=0;
       	fields = new double[5];
       	fields[0]=20.0;
       	fields[1]=100.0;
    	fields[2]=14;
       	fields[3]=77.2;
    	fields[4]=50.0;
      
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
    		enable(in1,true);	 enable(in2,true);		 enable(in3,true);	
			
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
	 /*   float y = (float)(float)Math.sin(Math.toRadians(m_cameraViews[m_cameraEye]));
	    float z = 2.41f - Math.abs(y);//((float)Math.sin(Math.toRadians(m_cameraViews[m_cameraEye])));
	    // default (0, 0, 2.41)
	   // System.out.println("x" + x);
	    t3d.lookAt( new Point3d(0, y,z), new Point3d(0,0,0), new Vector3d(0,1,0));
	    t3d.invert();*/
	    float z = (float)5*(float)Math.sin(Math.toRadians(m_cameraViews[m_cameraEye]));
	    if(z<0) z=-z;
	    t3d.lookAt( new Point3d(0, 0,-z-2), new Point3d(0,0,-20), new Vector3d(0,1,0));
	    
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
    	enableStage(0);   // -1 	
        reStartButton.setEnabled(true);
        nextButton.setEnabled(true);
        outputGraph.setState(1);
        outputGraph2.setState(1);
        
        if(valChange){
        	
        //	System.out.println("Value Changed");
	      freeBody.Init((int)fields[0],(int)fields[1],(int)fields[2],fields[3],fields[4]);
	      
	      outputGraph.clearGraphValue();
	      outputGraph2.clearGraphValue();
	      float scaleXZ = (float)(fields[1])/100.0f; 
	        Vector3d scaleVec = new Vector3d(scaleXZ,1,scaleXZ);
	        Transform3D scaleT = new Transform3D();
	        scaleT.setScale(scaleVec);
	        objSwitchPos.setTransform(scaleT);
	        
	        LineAttributes la = new LineAttributes();
	        la.setLineWidth((float)fields[0]/10);
	        appea.setLineAttributes(la);	        	      	
        }
        
         timer.start();
        System.out.println("Timer started");
    }
    
   
    
    // Resume Button Action
    private void timerActionPerformed(java.awt.event.ActionEvent evt)
    {
      
    	    	
    	    	
    	float  def= (float)(freeBody.getDefelection()) ;
    	float stiff=(float)(freeBody.getStiffness());
    	float StrainEnergy=(float) (freeBody.getStrainEnergy());
    //	System.out.println(def);
//        float  disp = (float)(freeBody.getDisplacement1()) ; 
       
        int i=0;
        outlbl_val[i++].setText(String.valueOf(def)+ " mm");
        outlbl_val[i++].setText(String.valueOf(stiff)+ " KN/m");
        outlbl_val[i++].setText(String.valueOf(StrainEnergy)+ " KN-m");
//        if(String.valueOf(getStiff()).length()>4)                                 
//        outlbl_val[i++].setText(String.valueOf(getStiff()).substring(0,4)+ String.valueOf(getStiff()).substring(String.valueOf(getStiff()).length()-4,String.valueOf(getStiff()).length())+" N/m");
//        outlbl_val[i++].setText(String.valueOf(getStiff()) + " N/m");
//        
//        
//        Formatter fmt = new Formatter();
//        /////////// Text
////        i=2;
//        outlbl_val[i++].setText(fmt.format("%.2f", Double.parseDouble(String.valueOf(time))) + " sec");
//       if(String.valueOf(disp).length()>4)                                  //This has to be edited
//        	outlbl_val[i++].setText(String.valueOf(disp).substring(0,4)+ String.valueOf(disp).substring(String.valueOf(disp).length()-4,String.valueOf(disp).length())+" m");
//      

        
        ////////////// Graph /////////
        
//        outputGraph.setCurrentValue(time,disp); 
       outputGraph.addGraphValue(def/500000); // * 700
//        
        
  
        
        if(rightPanel.isVisible())
        {
        	outputGraph.drawGraph();
        	outputGraph2.drawGraph();
       
        }
                
//        fullViewGraph.updateGraph(new float[]{disp});
//        fullViewGraph.drawGraph();
        
//        float  mx_disp = (float)outputGraph.getAbsMaximumY();

        
        freeBody.update();        
        
        if(freeBody.isDataCompleted()) {
        	pauseSimulation();
        	return;
        }
        java.util.BitSet mask = new java.util.BitSet(objSwitch.numChildren());        
        objSwitch.setChildMask(mask);        
        stage++;
        stage = stage%7;
        
               	
        java.util.BitSet visibleNodes = new java.util.BitSet( objSwitch.numChildren() );
        visibleNodes.set(stage);
        objSwitch.setChildMask(visibleNodes);        
              
        return;            
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
//		outputGraph2.setState(0);
        //startButton.setEnabled(true);
		       
        valChange = false;
         
		repaint();
    }
 
    
}///////////////// Defination COmplete

    

   

// Force Body Motion Object

class HelixBody
{
 // Inputs from the keyboard 
 // diameter of wire(5-30) mm, diameter of spring(50-200 mm), number of turns(5-30), 
 // spring Material(steel, copper, aluminium..) double value
 // load 0 to 100kN
	 private int dia_of_wire,radius_of_spring,number_of_turns;// dia of wire=12,radius=75,number of turns =10
	 private double G;    //=80.0;//spring_material_value or G or Modulus_rigidity
	 private double load;  //=0.450;
	 double strain_energy_stored,main_defelection,stiffness_of_spring ;
	 ArrayList<Double>  defelection=new ArrayList<Double>();
	 int inc;

  public void Init(int dia,int rad_spr,int turns, double matval, double inp_load )
  {
	  defelection.clear();
	  dia_of_wire=dia;radius_of_spring=rad_spr;number_of_turns=turns;
	  G=matval;
	  load=inp_load;
	  
	  double maximum_shear_stress,toque,length_of_rod;
		
		maximum_shear_stress=load*radius_of_spring*16/(Math.PI*Math.pow(dia_of_wire, 3));
		length_of_rod=2*Math.PI*(radius_of_spring)*number_of_turns;
		for (double changeLoad=0;changeLoad<100;changeLoad=changeLoad+1.0)
		{
			  strain_energy_stored=32*changeLoad*changeLoad*Math.pow((radius_of_spring),3)*number_of_turns/(G*Math.pow(dia_of_wire,4));
				defelection.add(64*changeLoad*Math.pow((radius_of_spring),3)*number_of_turns/(G*Math.pow(dia_of_wire,4)));
				stiffness_of_spring=changeLoad/main_defelection;
		}
            strain_energy_stored=32*load*load*Math.pow((radius_of_spring),3)*number_of_turns/(G*Math.pow(dia_of_wire,4));
			main_defelection=64*load*Math.pow((radius_of_spring),3)*number_of_turns/(G*Math.pow(dia_of_wire,4));
			stiffness_of_spring=load/main_defelection;
		//	for (double d=) have to ask the load range
		//	System.out.println(" defelection = " + defelection + "maximum_shear_stress " + maximum_shear_stress + " stiffness_of_spring = " + stiffness_of_spring);
  }
 public double getStrainEnergy()
 {
	 return strain_energy_stored;
 }
 public double getStiffness()
 {
	 return stiffness_of_spring;
 }
 public double getMainDefelection()
 {
	 return main_defelection;
 }
 public double getDefelection()
 {
	
	 return defelection.get(inc);
 }
 public void update()
 {
 	inc++;		    	
 }
boolean isDataCompleted()
	{
		int index=inc;
	
		if(index >=  defelection.size())
			return true;
		
		return false;
	}
 }
  


