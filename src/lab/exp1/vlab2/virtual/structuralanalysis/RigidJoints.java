package structuralanalysis;



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

import java.util.ArrayList;
import java.util.HashMap;


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
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;


import eerc.vlab.common.FullViewGraph;
import eerc.vlab.common.HelpWindow;
import eerc.vlab.common.HorizontalGraph;

import eerc.vlab.common.J3DShape;

import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

@SuppressWarnings("serial")
public class RigidJoints extends javax.swing.JPanel {
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
	

	//private GraphPlotter         graphPlotter;
	////////////////////////////Java3D componenet ///////////////////////////

	private SimpleUniverse      univ = null;                  // Simple Universe Java3D
	private BranchGroup         scene = null;                 // BranchGroup Scene graph
	public BranchGroup branchGroupRoot = new BranchGroup();
	public BranchGroup branchGroupDetachable = new BranchGroup();
	
	TransformGroup lapJoint = new TransformGroup();
	TransformGroup buttJoint = new TransformGroup();
	TransformGroup blackBolt = new TransformGroup();
	TransformGroup turnedBolt = new TransformGroup();
	TransformGroup hsfgBolt = new TransformGroup();
	TransformGroup Pin = new TransformGroup();	

	
	TransformGroup bolts = new TransformGroup();
	TransformGroup bolts2 = new TransformGroup();
	TransformGroup spbolts2 = new TransformGroup();
	TransformGroup plate1 = new TransformGroup();
	TransformGroup plate2 = new TransformGroup();
	TransformGroup plate3 = new TransformGroup();
	TransformGroup plate4 = new TransformGroup();	
	TransformGroup objSwitchPos = new TransformGroup(new Transform3D());
	
	TransformGroup Buttbolts = new TransformGroup();
	TransformGroup Buttbolts2 = new TransformGroup();
	TransformGroup Buttspbolts2 = new TransformGroup();
	TransformGroup ButtplateA1 = new TransformGroup();
	TransformGroup ButtplateA2 = new TransformGroup();
	TransformGroup ButtplateA3 = new TransformGroup();
	TransformGroup ButtplateB1 = new TransformGroup();
	TransformGroup ButtplateB2 = new TransformGroup();
	TransformGroup ButtplateB3 = new TransformGroup();	
	
	TransformGroup blackBoltbolt = new TransformGroup();
	TransformGroup blackBoltnut = new TransformGroup();
	TransformGroup blackBoltboltRot1 = new TransformGroup();
	TransformGroup blackBoltboltRot2 = new TransformGroup();
	TransformGroup blackBoltboltRot3 = new TransformGroup();
	TransformGroup blackBoltnutRot1 = new TransformGroup();
	TransformGroup blackBoltnutRot2 = new TransformGroup();
	TransformGroup blackBoltnutRot3 = new TransformGroup();
	
	TransformGroup HSFGBoltbolt = new TransformGroup();
	TransformGroup HSFGBoltnut = new TransformGroup();
	TransformGroup HSFGBoltboltRot = new TransformGroup();
	TransformGroup HSFGBoltnutRot = new TransformGroup();
	
	Appearance appea = new Appearance();	
	
	private HorizontalGraph		outputGraph =null;

	private FullViewGraph  		fullViewGraph = new FullViewGraph();
	
	//@SuppressWarnings("unchecked")
	private HashMap 			hm = new HashMap();
	private J3DShape 			m_j3d	= new J3DShape();

	private double fields[];
	int SelectedRadioButton = -1;
	static final int LAP_JOINT = 0;
	static final int BUTT_JOINT = 1;
	static final int BLACK_BOLT = 2;
	static final int TURNED_BOLT = 3;
	static final int HSFG_BOLT = 4;
	static final int PIN_CONNECTION = 5;
	
	private JLabel iLabel[];
	private JLabel m_Objective= new JLabel("Objective:");
	
	private Timer timer=null;
	private Timer m_cameraTimer=null; 
	private float m_cameraViews[];
	private int m_cameraEye;
	// Timer for simulation    
	
	private int stage = 0;	
	
	private boolean startStop = false;
	private boolean valChange = false;
	
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
		objRoot.addChild(m_j3d.createBox(new Vector3d(1,0.1f,0), new Vector3d(0.05,0.7f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 0.9f),"resources/images/376.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(-1,0.1f,0), new Vector3d(0.05,0.7f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 0.9f),"resources/images/376.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.1f,-2.0), new Vector3d(1,0.7f,0.05),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/992.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.84f,0), new Vector3d(1.05,0.04f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/376.jpg"));
		
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
    public Group createBolt(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr){
    	// Setting the 6 degrees of freedom and Scale
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
        
        Appearance appea = new Appearance();	    
	    ColoringAttributes ca = new ColoringAttributes(new Color3f(colr.x,colr.y*0.8f,colr.z*0.8f), ColoringAttributes.SHADE_GOURAUD);
	    appea.setColoringAttributes(ca);
           
        TransformGroup objtrans = new TransformGroup(t);
        objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);       
	  
	    objtrans.addChild(m_j3d.createCylinder(new Vector3d(0,scale.y/12,0),new Vector3d(0.2,1,0.2), new Vector3d(0,0,0), colr));
	    objtrans.addChild(new Sphere((float)scale.y/12,appea));
	    
	    return objtrans;
    }
    public Group createBlackBolt(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr){
    	// Setting the 6 degrees of freedom and Scale
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
        
        Appearance appea = new Appearance();	    
	    ColoringAttributes ca = new ColoringAttributes(new Color3f(colr.x,colr.y*0.8f,colr.z*0.8f), ColoringAttributes.SHADE_GOURAUD);
	    appea.setColoringAttributes(ca);
	    LineAttributes la = new LineAttributes();
	    
	    la.setLineWidth(1.5f);
	  
	    appea.setLineAttributes(la);   
        TransformGroup objtrans = new TransformGroup(t);
        objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);       
	  
	    objtrans.addChild(m_j3d.createCylinder(new Vector3d(0,0,0),new Vector3d(1,5,1), new Vector3d(0,0,0), new Color3f(0.4f,0.4f,0.4f),"resources/images/grey13.jpg", hm));
	    objtrans.addChild(m_j3d.createBox(new Vector3d(0,0.25f,0), new Vector3d(0.2,0.02,0.2), new Vector3d(0,0,0), colr,"resources/images/grey13.jpg"));	    
	    float i;
	    float Radius = 0.102f;
	    float Turns = 20;
	    float H = 0.4f;
	    Point3f[] coords = new Point3f[2];
	    for(i=-10*200;i<10*200;i++)
	    {
	    	coords[0] = new Point3f((float)(Radius*Math.sin(i*3.14/(200*H))),i/(400*Turns),(float)(Radius*Math.cos(i*3.14/(200*H))));
	    	coords[1] = new Point3f((float)(Radius*Math.sin((i+1)*3.14/(200*H))),(i+1)/(400*Turns),(float)(Radius*Math.cos((i+1)*3.14/(200*H))));
	    	LineArray line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        Shape3D myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	    }	    
	    return objtrans;
    }
    public Group createTurnedBolt(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr){
    	// Setting the 6 degrees of freedom and Scale
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
                   
        TransformGroup objtrans = new TransformGroup(t);
        objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);       
	  
        float i;
        for(i=-72;i<=72;i++)
        {
        	Transform3D transval = new Transform3D();
        	Transform3D rotval = new Transform3D();
        	transval.setTranslation(new Vector3d(0,i/100,0));
        	rotval.rotY(i*10*Math.PI/180);
        	TransformGroup part = new TransformGroup(transval);
        	TransformGroup rotpart = new TransformGroup(rotval);
        	
        	
        	rotpart.addChild(m_j3d.createBox(new Vector3d(),new Vector3d(1,0.02,0.1), new Vector3d(0,0,0), colr,"resources/images/grey13.jpg"));
        	part.addChild(rotpart);
        	objtrans.addChild(part);
        	
        }
        objtrans.addChild(m_j3d.createCylinder(new Vector3d(0,72.0f/75,0),new Vector3d(10,10,10), new Vector3d(0,0,0), colr,"resources/images/grey13.jpg", hm));
	    return objtrans;
    }
    public Group createNut(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr){
    	// Setting the 6 degrees of freedom and Scale
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
      
        TransformGroup objtrans = new TransformGroup(t);
        objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);       
	  
        objtrans.addChild(m_j3d.createBox(new Vector3d(0,0.25f,0), new Vector3d(0.2,0.02,0.2), new Vector3d(0,0,0), colr,"resources/images/grey13.jpg"));	    
        objtrans.addChild(m_j3d.createCylinder(new Vector3d(0,0.25f,0), new Vector3d(1,0.401,1), new Vector3d(0,0,0), new Color3f(1,1,1)));	    
  	   
	    return objtrans;
    }
        
	private Group createVirtualLab() {	
		
		
		Transform3D tor = new Transform3D();
		
		//bolts = new TransformGroup();
		bolts.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		bolts.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		bolts2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		bolts2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		spbolts2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		spbolts2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
				
		plate1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		plate1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		plate2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		plate2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		plate3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		plate3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		plate4.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		plate4.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);	
		
		ButtplateA1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		ButtplateA1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);	
		
		ButtplateA2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		ButtplateA2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);	
		
		ButtplateA3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		ButtplateA3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);	
		
		ButtplateB1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		ButtplateB1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		ButtplateB2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);	
		ButtplateB2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);	
		
		ButtplateB3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		ButtplateB3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		Buttbolts.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);	
		Buttbolts.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Buttspbolts2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);	
		Buttspbolts2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		blackBolt.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		blackBolt.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		blackBoltbolt.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		blackBoltbolt.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		blackBoltnut.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		blackBoltnut.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		blackBoltboltRot1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		blackBoltboltRot1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		blackBoltboltRot2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		blackBoltboltRot2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		blackBoltboltRot3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		blackBoltboltRot3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		blackBoltnutRot1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		blackBoltnutRot1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		blackBoltnutRot2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		blackBoltnutRot2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	
		blackBoltnutRot3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		blackBoltnutRot3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		hsfgBolt.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		hsfgBolt.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
				
		HSFGBoltnut.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		HSFGBoltnut.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		HSFGBoltbolt.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		HSFGBoltbolt.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		HSFGBoltnutRot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		HSFGBoltnutRot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		HSFGBoltboltRot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		HSFGBoltboltRot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		turnedBolt.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		turnedBolt.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
				
		//	TransformGroup lj2 = new TransformGroup();
		
		BranchGroup objroot = new BranchGroup();
      
		// LAP JOINT :
       
	    bolts.addChild(createBolt(new Vector3d(0.28,0.15,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts.addChild(createBolt(new Vector3d(0.28,0.1,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts.addChild(createBolt(new Vector3d(0.28,0.05,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts.addChild(createBolt(new Vector3d(0.28,0.0,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts.addChild(createBolt(new Vector3d(0.28,-0.05,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts.addChild(createBolt(new Vector3d(0.28,-0.1,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts.addChild(createBolt(new Vector3d(0.28,-0.15,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts.addChild(createBolt(new Vector3d(0.32,0.15,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts.addChild(createBolt(new Vector3d(0.32,0.1,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts.addChild(createBolt(new Vector3d(0.32,0.05,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts.addChild(createBolt(new Vector3d(0.32,0.0,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts.addChild(createBolt(new Vector3d(0.32,-0.05,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts.addChild(createBolt(new Vector3d(0.32,-0.1,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts.addChild(createBolt(new Vector3d(0.32,-0.15,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	  
	    tor.setTranslation(new Vector3d(0,0.1,0));
	    tor.rotX(-Math.PI/2);
	    bolts2.addChild(createBolt(new Vector3d(-0.32,0.15,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts2.addChild(createBolt(new Vector3d(-0.32,0.1,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts2.addChild(createBolt(new Vector3d(-0.32,0.05,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts2.addChild(createBolt(new Vector3d(-0.32,0,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts2.addChild(createBolt(new Vector3d(-0.32,-0.05,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts2.addChild(createBolt(new Vector3d(-0.32,-0.1,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts2.addChild(createBolt(new Vector3d(-0.32,-0.15,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts2.addChild(createBolt(new Vector3d(-0.28,0.15,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts2.addChild(createBolt(new Vector3d(-0.28,0.1,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts2.addChild(createBolt(new Vector3d(-0.28,0.05,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts2.addChild(createBolt(new Vector3d(-0.28,0.0,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts2.addChild(createBolt(new Vector3d(-0.28,-0.05,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts2.addChild(createBolt(new Vector3d(-0.28,-0.1,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    bolts2.addChild(createBolt(new Vector3d(-0.28,-0.15,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    
	    bolts2.setTransform(tor);
	    
	    plate1.addChild(m_j3d.createBox(new Vector3d(0.22,0,0.06), new Vector3d(0.15,0.2,0.02), new Vector3d(0,0,0), new Color3f(0.9f,0.8f,0.4f),"resources/images/grey13.jpg"));
        plate2.addChild(m_j3d.createBox(new Vector3d(0.38,0,0.0), new Vector3d(0.15,0.22,0.02), new Vector3d(0,0,0), new Color3f(0.9f,0.7f,0.4f),"resources/images/grey13.jpg"));
        plate3.addChild(m_j3d.createBox(new Vector3d(-0.36,-0.02,-0.04), new Vector3d(0.15,0.22,0.01), new Vector3d(90,0,0), new Color3f(0.9f,0.7f,0.4f),"resources/images/grey13.jpg"));
        plate4.addChild(m_j3d.createBox(new Vector3d(-0.2,-0.01,0.0), new Vector3d(0.15,0.2,0.01), new Vector3d(90,0,0), new Color3f(0.9f,0.8f,0.4f),"resources/images/grey13.jpg"));
        
        spbolts2.addChild(bolts2);	                 
        lapJoint.addChild(bolts);
        lapJoint.addChild(spbolts2);
        lapJoint.addChild(plate1);
        lapJoint.addChild(plate2);
        lapJoint.addChild(plate3);
        lapJoint.addChild(plate4);
        
        // BUTT JOINT :
        
        Buttbolts.addChild(createBolt(new Vector3d(0.27,0.15,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
        Buttbolts.addChild(createBolt(new Vector3d(0.27,0.1,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
        Buttbolts.addChild(createBolt(new Vector3d(0.27,0.05,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
        Buttbolts.addChild(createBolt(new Vector3d(0.27,0.0,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
        Buttbolts.addChild(createBolt(new Vector3d(0.27,-0.05,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
        Buttbolts.addChild(createBolt(new Vector3d(0.27,-0.1,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
        Buttbolts.addChild(createBolt(new Vector3d(0.27,-0.15,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
        Buttbolts.addChild(createBolt(new Vector3d(0.33,0.15,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
        Buttbolts.addChild(createBolt(new Vector3d(0.33,0.1,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
        Buttbolts.addChild(createBolt(new Vector3d(0.33,0.05,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
        Buttbolts.addChild(createBolt(new Vector3d(0.33,0.0,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
        Buttbolts.addChild(createBolt(new Vector3d(0.33,-0.05,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
        Buttbolts.addChild(createBolt(new Vector3d(0.33,-0.1,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
        Buttbolts.addChild(createBolt(new Vector3d(0.33,-0.15,0.1), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	  
	  //  tor.setTranslation(new Vector3d(0,0.1,0));
	  //  tor.rotX(-Math.PI/2);
	    Buttbolts2.addChild(createBolt(new Vector3d(-0.33,0.15,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    Buttbolts2.addChild(createBolt(new Vector3d(-0.33,0.1,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    Buttbolts2.addChild(createBolt(new Vector3d(-0.33,0.05,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    Buttbolts2.addChild(createBolt(new Vector3d(-0.33,0,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    Buttbolts2.addChild(createBolt(new Vector3d(-0.33,-0.05,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    Buttbolts2.addChild(createBolt(new Vector3d(-0.33,-0.1,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    Buttbolts2.addChild(createBolt(new Vector3d(-0.33,-0.15,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    Buttbolts2.addChild(createBolt(new Vector3d(-0.27,0.15,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    Buttbolts2.addChild(createBolt(new Vector3d(-0.27,0.1,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    Buttbolts2.addChild(createBolt(new Vector3d(-0.27,0.05,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    Buttbolts2.addChild(createBolt(new Vector3d(-0.27,0.0,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    Buttbolts2.addChild(createBolt(new Vector3d(-0.27,-0.05,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    Buttbolts2.addChild(createBolt(new Vector3d(-0.27,-0.1,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    Buttbolts2.addChild(createBolt(new Vector3d(-0.27,-0.15,0), new Vector3d(0.4,0.4,0.4), new Vector3d(-90,0,0), new Color3f(0.3f,0.3f,0.2f)));
	    
	    Buttbolts2.setTransform(tor);    
	    
	    ButtplateA1.addChild(m_j3d.createBox(new Vector3d(0.19,0,0.06), new Vector3d(0.1,0.2,0.02), new Vector3d(0,0,0), new Color3f(0.9f,0.8f,0.4f),"resources/images/grey13.jpg"));
	    ButtplateA2.addChild(m_j3d.createBox(new Vector3d(0.3,0,0.0), new Vector3d(0.15,0.22,0.02), new Vector3d(0,0,0), new Color3f(0.9f,0.7f,0.4f),"resources/images/grey13.jpg"));
	    ButtplateA3.addChild(m_j3d.createBox(new Vector3d(0.41,0,0.06), new Vector3d(0.1,0.2,0.02), new Vector3d(0,0,0), new Color3f(0.86f,0.81f,0.41f),"resources/images/grey13.jpg"));
	    ButtplateB1.addChild(m_j3d.createBox(new Vector3d(-0.3,-0.03,-0.02), new Vector3d(0.15,0.24,0.01), new Vector3d(90,0,0), new Color3f(0.9f,0.7f,0.4f),"resources/images/grey13.jpg"));
	    ButtplateB2.addChild(m_j3d.createBox(new Vector3d(-0.19,-0.01,0.0), new Vector3d(0.1,0.2,0.01), new Vector3d(90,0,0), new Color3f(0.9f,0.8f,0.4f),"resources/images/grey13.jpg"));
	    ButtplateB3.addChild(m_j3d.createBox(new Vector3d(-0.41,-0.01,0.0), new Vector3d(0.1,0.2,0.01), new Vector3d(90,0,0), new Color3f(0.86f,0.81f,0.41f),"resources/images/grey13.jpg"));
        
	    
	    Buttspbolts2.addChild(Buttbolts2);	       
        buttJoint.addChild(Buttbolts);
        buttJoint.addChild(Buttspbolts2);        
        buttJoint.addChild(ButtplateA1);
        buttJoint.addChild(ButtplateA2);
        buttJoint.addChild(ButtplateA3);
        buttJoint.addChild(ButtplateB1);
        buttJoint.addChild(ButtplateB2);
        buttJoint.addChild(ButtplateB3);
        
        // BLACK BOLT:
        
        Transform3D n = new Transform3D();
        Transform3D p = new Transform3D();
        p.setTranslation(new Vector3d(0,0,0.2));
        n.setTranslation(new Vector3d(0,0,-0.2));
        
        TransformGroup nZb = new TransformGroup(n);
        TransformGroup pZb = new TransformGroup(p);
        TransformGroup nZn = new TransformGroup(n);
        TransformGroup pZn = new TransformGroup(p);
        
        nZb.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        nZb.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        pZb.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        pZb.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        nZn.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        nZn.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        pZn.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        pZn.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        blackBolt.addChild(m_j3d.createBox(new Vector3d(0.12,-0.11,0),new Vector3d(0.25,0.01,0.3),new Vector3d(0,0,0), new Color3f(0.9f,0.8f,0.4f),"resources/images/grey13.jpg"));
        blackBolt.addChild(m_j3d.createBox(new Vector3d(-0.12,-0.13,0),new Vector3d(0.25,0.01,0.3),new Vector3d(0,0,0), new Color3f(0.9f,0.7f,0.4f),"resources/images/grey13.jpg"));
        blackBoltboltRot1.addChild(createBlackBolt(new Vector3d(0,-0.15,0),new Vector3d(0.3,0.3,0.3), new Vector3d(0,0,0), new Color3f(0.1f,0.1f,0.1f)));
        blackBoltboltRot2.addChild(createBlackBolt(new Vector3d(0,-0.15,0),new Vector3d(0.3,0.3,0.3), new Vector3d(0,0,0), new Color3f(0.1f,0.1f,0.1f)));
        blackBoltboltRot3.addChild(createBlackBolt(new Vector3d(0,-0.15,0),new Vector3d(0.3,0.3,0.3), new Vector3d(0,0,0), new Color3f(0.1f,0.1f,0.1f)));
        blackBoltnutRot1.addChild(createNut(new Vector3d(0,-0.22,0),new Vector3d(0.3,0.3,0.3), new Vector3d(0,0,0), new Color3f(0.4f,0.4f,0.4f)));
        blackBoltnutRot2.addChild(createNut(new Vector3d(0,-0.22,0),new Vector3d(0.3,0.3,0.3), new Vector3d(0,0,0), new Color3f(0.4f,0.4f,0.4f)));
        blackBoltnutRot3.addChild(createNut(new Vector3d(0,-0.22,-0),new Vector3d(0.3,0.3,0.3), new Vector3d(0,0,0), new Color3f(0.4f,0.4f,0.4f)));
        
        blackBoltbolt.addChild(nZb);
        blackBoltbolt.addChild(pZb);
        blackBoltnut.addChild(nZn);
        blackBoltnut.addChild(pZn);
        blackBoltbolt.addChild(blackBoltboltRot1);
        pZb.addChild(blackBoltboltRot2);
        nZb.addChild(blackBoltboltRot3);
        blackBoltnut.addChild(blackBoltnutRot1);
        pZn.addChild(blackBoltnutRot2);
        nZn.addChild(blackBoltnutRot3);
        blackBolt.addChild(blackBoltbolt);
        blackBolt.addChild(blackBoltnut);
        
        // HSFG BOLT:
        
        hsfgBolt.addChild(m_j3d.createBox(new Vector3d(0.12,-0.1,0),new Vector3d(0.25,0.02,0.3),new Vector3d(0,0,0), new Color3f(0.9f,0.8f,0.4f),"resources/images/grey13.jpg"));
        hsfgBolt.addChild(m_j3d.createBox(new Vector3d(-0.12,-0.14,0),new Vector3d(0.25,0.02,0.3),new Vector3d(0,0,0), new Color3f(0.9f,0.7f,0.4f),"resources/images/grey13.jpg"));
        HSFGBoltboltRot.addChild(createBlackBolt(new Vector3d(0,-0.15,0),new Vector3d(0.5,0.5,0.5), new Vector3d(0,0,0), new Color3f(0.2f,0.2f,0.0f)));
        //blackBoltboltRot2.addChild(createBlackBolt(new Vector3d(0,-0.15,0),new Vector3d(0.3,0.3,0.3), new Vector3d(0,0,0), new Color3f(0.1f,0.1f,0.1f)));
        //blackBoltboltRot3.addChild(createBlackBolt(new Vector3d(0,-0.15,0),new Vector3d(0.3,0.3,0.3), new Vector3d(0,0,0), new Color3f(0.1f,0.1f,0.1f)));
        HSFGBoltnutRot.addChild(createNut(new Vector3d(0,-0.22,0),new Vector3d(0.3,0.3,0.3), new Vector3d(0,0,0), new Color3f(0.4f,0.4f,0.4f)));
        
        HSFGBoltbolt.addChild(HSFGBoltboltRot);
        HSFGBoltnut.addChild(HSFGBoltnutRot);
        hsfgBolt.addChild(HSFGBoltbolt);
        hsfgBolt.addChild(HSFGBoltnut);
        // Turned Bolt
        
        turnedBolt.addChild(createTurnedBolt(new Vector3d(0,0,0), new Vector3d(0.075,0.15,0.075), new Vector3d(0,0,0), new Color3f(0.3f,0.3f,0.3f)));
        turnedBolt.addChild(m_j3d.createBox(new Vector3d(0,0.2,0), new Vector3d(0.2,0.02,0.2),new Vector3d(0,0,0), new Color3f(0.2f,0.15f,0.1f),"resources/images/grey13.jpg"));
        // PIN CONNECTION :
     //   Pin.addChild(m_j3d.createBox(new Vector3d(0,0,0), new Vector3d(0.3,0.2,0.01),new Vector3d(0,0,0), new Color3f(0.9f,0.9f,0.9f),"resources/images/pin.jpg"));
        
        // FINALLY :
        
	   	//branchGroupDetachable.addChild(lapJoint);
        //branchGroupDetachable.addChild(buttJoint);
        //branchGroupDetachable.addChild(blackBolt);
        //branchGroupDetachable.addChild(hsfgBolt);
        //branchGroupDetachable.addChild(Pin);
        branchGroupDetachable.addChild(turnedBolt);
        
	    branchGroupRoot.addChild(branchGroupDetachable);    
	    
	    objroot.addChild(branchGroupRoot);
	    
	    return objroot;
	}  
	
     
    
    /**
     * Creates new form FreeVibration
     */
    public RigidJoints(Container container) {
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
		RigidJoints mainPanel;

        public void init() {
            setLayout(new BorderLayout());
            mainPanel = new RigidJoints(this);
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
            setTitle("Rigid Joints");
            getContentPane().add(new RigidJoints(this), BorderLayout.CENTER);
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
        
    	branchGroupRoot.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
    	branchGroupRoot.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
    	branchGroupRoot.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
    	branchGroupRoot.setCapability(BranchGroup.ENABLE_PICK_REPORTING);
    	
    	branchGroupDetachable.setCapability(BranchGroup.ALLOW_DETACH);
    	branchGroupDetachable.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
    	branchGroupDetachable.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE); 
    	branchGroupDetachable.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
    	
//      new GridLayout(2, 1)
        setLayout(new java.awt.BorderLayout());
        
        bottomPanel = new javax.swing.JPanel(); 	// input from user at bottom
        simulationPanel = new javax.swing.JPanel(); // 3D rendering at center
        topPanel= new javax.swing.JPanel();    		// Pause, resume, Next
        rightPanel = new javax.swing.JPanel();    	// Graph and Input and Output Parameter
                
         
        topPanel();
        bottomPanel();
      //  rightPanel();
        
//      Set Alignment
        //add(guiPanel, java.awt.BorderLayout.NORTH);
        add(topPanel, java.awt.BorderLayout.NORTH);
        add(simulationPanel, java.awt.BorderLayout.CENTER);
        add(bottomPanel, java.awt.BorderLayout.SOUTH);
        add(rightPanel, java.awt.BorderLayout.EAST); 
        
        startStop = false;
    	valChange = true;    
        
        timer = new Timer(100,new ActionListener() {
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
            	
              //  univ.getCanvas().repaint();
            }
          });
        
        
        reStartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	reStartButton.setEnabled(false);
            	stage=0;
                //startButton.setEnabled(true);
                startButton.setText("Start");
                startStop = !startStop;
               // startStop = false;     
                
                valChange = true;
                startSimulation(evt);
                       
            }
          });
        
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {                
            	stage++;
            	nextButton.setEnabled(false);            	
            	onNextStage();
            //	univ.getCanvas().repaint();
            }
          });
        
       
        javax.swing.JButton btn= new javax.swing.JButton("Full View Graph");
      
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
                frame.add(fullViewGraph);   
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
  

	private static void enable(Container root, boolean enable) {
	   return;
    }
    
    private void centerPanel(Container container){
    	
    	 simulationPanel.setPreferredSize(new java.awt.Dimension(1024, 600));
         simulationPanel.setLayout(new java.awt.BorderLayout());
        
         javax.swing.JPanel guiPanel = new javax.swing.JPanel();
         guiPanel.setBackground(new Color(100,100,100));
         JLabel lbl = new JLabel("Types of Rigid Joints", JLabel.CENTER);
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
           
         
         m_Objective = new JLabel("V Labs Experiment", JLabel.LEFT);
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
            	 m_cameraTimer = new Timer(100,new ActionListener() {
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
            	 m_cameraTimer = new Timer(100,new ActionListener() {
                     public void actionPerformed(ActionEvent evt) {
                         //...Perform a task...
                    	 timerActionVerticalCameraMotion(evt);
                     }
                 });
            	 m_cameraTimer.start();
            	 
             }
           });
         
         guiPanel.add(viewButton, gridBagConstraints);
         
      
         
         btmPanel.add(guiPanel,BorderLayout.CENTER);
                 
         guiPanel = new javax.swing.JPanel(); // 
         guiPanel.setBackground(new Color(130,169,193));
         guiPanel.setBorder(BorderFactory.createLineBorder(new Color(235,233,215),4));
       
        
         

    }  
    



    private void bottomPanel()
    {
    	   initInputControlsField();
           
    	   Color bk = new Color(219,226,238);
           bottomPanel.setLayout(new java.awt.GridLayout(1,3));
           bottomPanel.setBackground(Color.black);
           bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(235,233,215),8));
           
           in1 = new JPanel(new java.awt.GridLayout(0,1));
           in1.setBackground(bk);
           bottomPanel.add(in1);

           in2 = new JPanel(new java.awt.GridLayout(0,1)); 
           in2.setBackground(bk);

           bottomPanel.add(in2);

           in3 = new JPanel(new java.awt.GridLayout(0,1)); 
           in3.setBackground(bk);
           bottomPanel.add(in3);
           
           JLabel lab_new1 = new JLabel("RIVETED JOINTS", JLabel.CENTER);

           JRadioButton radio1 = new JRadioButton("Lap Joints");
           JRadioButton radio2 = new JRadioButton("Butt Joints");
           radio1.setBackground(bk);
           radio2.setBackground(bk);
                      
           JLabel lbl2 = new JLabel("TYPES OF BOLTS", JLabel.CENTER);

           JRadioButton radio3 = new JRadioButton("Black Bolts");
           JRadioButton radio4 = new JRadioButton("Turned & Fitted Bolt");
           JRadioButton radio5 = new JRadioButton("HSFG Bolt");
           radio3.setBackground(bk);
           radio4.setBackground(bk);
           radio5.setBackground(bk);
           
           
           //JRadioButton radio6 = new JRadioButton("Pin Connections");
         //  radio6.setBackground(bk);
           
        // Group the radio buttons.
           ButtonGroup btgroup = new ButtonGroup();
           btgroup.add(radio1);
           btgroup.add(radio2);
           btgroup.add(radio3);
           btgroup.add(radio4);
           btgroup.add(radio5);
        //   btgroup.add(radio6);
           
        // Register an action listener for the radio buttons.
           
           radio1.addActionListener(new ActionListener() {
			
		//	@Override
			public void actionPerformed(ActionEvent arg0) {
				stage = 0;
				if (SelectedRadioButton != LAP_JOINT)
   					valChange = true;
   				else
   					valChange = false;
				SelectedRadioButton = LAP_JOINT;
				System.out.println("Lap Joint selected");
			}
		});
           
           radio2.addActionListener(new ActionListener() {
   			
   			public void actionPerformed(ActionEvent arg0) {
   				stage = 0;
   				if (SelectedRadioButton != BUTT_JOINT)
   					valChange = true;
   				else
   					valChange = false;
   				SelectedRadioButton = BUTT_JOINT;
   				System.out.println("BUTT Joint selected");
   			}
   		});
           radio3.addActionListener(new ActionListener() {
      			
      			public void actionPerformed(ActionEvent arg0) {
      				stage = 0;
      				if (SelectedRadioButton != BLACK_BOLT)
      					valChange = true;
      				else
      					valChange = false;
      				SelectedRadioButton = BLACK_BOLT;
      				System.out.println("Black Bolt selected");
      			}
      		});
           radio4.addActionListener(new ActionListener() {
     			
     			public void actionPerformed(ActionEvent arg0) {
     				stage = 0;
     				if (SelectedRadioButton != TURNED_BOLT)
     					valChange = true;
     				else
     					valChange = false;
     				SelectedRadioButton = TURNED_BOLT;
     				System.out.println("Turned and Twisted Bolt selected");
     			}
     		});
           radio5.addActionListener(new ActionListener() {
     			
     			public void actionPerformed(ActionEvent arg0) {
     				stage = 0;
     				if (SelectedRadioButton != HSFG_BOLT)
     					valChange = true;
     				else
     					valChange = false;
     				SelectedRadioButton = HSFG_BOLT;
     				System.out.println("HSFG Bolt selected");
     			}
     		});
          /* radio6.addActionListener(new ActionListener() {
    			
    			public void actionPerformed(ActionEvent arg0) {
    				stage = 0;
    				if (SelectedRadioButton != PIN_CONNECTION)
    					valChange = true;
    				else
    					valChange = false;
    				SelectedRadioButton = PIN_CONNECTION;
    				System.out.println("Pin Connection selected");
    			}
    		});
           */
           in1.add(lab_new1);
           in1.add(radio1);
           in1.add(radio2);
           
           in2.add(lbl2);
           in2.add(radio3);
           in2.add(radio4);
           in2.add(radio5);
           
        //   in3.add(radio6);           
    
            /////////// Enable/Disable function for Input parameters
            enable(in1,true);
            enable(in2,true);
            enable(in3,true);
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
       	iLabel[i] = new JLabel(" ", JLabel.LEFT); iLabel[i++].setForeground(Color.blue);
       	iLabel[i] = new JLabel(" ", JLabel.LEFT); iLabel[i++].setForeground(Color.blue);
    	iLabel[i] = new JLabel(" ", JLabel.LEFT); iLabel[i++].setForeground(Color.blue);
       	iLabel[i] = new JLabel(" ", JLabel.LEFT); iLabel[i++].setForeground(Color.blue);
    	iLabel[i] = new JLabel(" ", JLabel.LEFT); iLabel[i++].setForeground(Color.blue);
       
       	i=0;
       	fields = new double[5];
       	fields[0]=0.0;
       	fields[1]=0.0;
    	fields[2]=0;
       	fields[3]=0;
    	fields[4]=0.0;
      
    }
    
     
    private void onNextStage()
    {
    	    	
    	valChange = true; // Clear the graph. or Graph will restart on Play    	
    	resetOutputParameters(); // Clear the Output Parameters
    //	bottomPanel.setVisible(true);
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
    		m_Objective.setText("V Labs Experiment");
    		m_Objective.setForeground(Color.WHITE);
    					break;
    	case 1:
    		m_Objective.setText("V Labs Experiment");
    		m_Objective.setForeground(Color.WHITE);
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
    		bottomPanel.setVisible(true);
//    	}
    	ImageIcon icon = m_j3d.createImageIcon("resources/icons/stop.png"); 
    	startButton.setIcon(icon);
    	startButton.setText("Stop");
    	enableStage(0);   // -1 	
        reStartButton.setEnabled(true);
        nextButton.setEnabled(true);        
        
        
         timer.start();
        System.out.println("Timer started");
    }
    
   
    
    // Resume Button Action
    private void timerActionPerformed(java.awt.event.ActionEvent evt)
    {      
        //univ.cleanup();
        stage++;
        if(stage<=50){
        Double Z;
        
        Z = (double)stage%51;
	    
	    Z = 50 - Z;
	     
	    Z = (Z/50.0);
        
        
    	
    	if(SelectedRadioButton == LAP_JOINT)
    	{
    	
    		
    		Transform3D t = new Transform3D();
    		Transform3D t1 = new Transform3D();
    		Transform3D t2 = new Transform3D();
    		Transform3D t3 = new Transform3D();
    		Transform3D t4 = new Transform3D();
    		Transform3D t5 = new Transform3D();
    		t.setTranslation(new Vector3d(0,0,Z*0.5));
    		t1.setTranslation(new Vector3d(0,Z*0.4,0));
    		t2.setTranslation(new Vector3d(0,-Z*0.4,0));
    		
    		t3.setTranslation(new Vector3d(0,Z*0.2,0));
    		
    		t5.setTranslation(new Vector3d(Z*0.2,0,0));
    		t4.setTranslation(new Vector3d(-Z*0.2,0,0));
    		bolts.setTransform(t);		
    		
    			
    		plate1.setTransform(t1);
    		plate2.setTransform(t2); 
    		plate3.setTransform(t4);
    		plate4.setTransform(t5);
	          
    		spbolts2.setTransform(t3);
    		if(valChange)
    		{
    			branchGroupDetachable.detach();
    			branchGroupDetachable.removeAllChildren();
    			branchGroupDetachable.addChild(lapJoint);
    			branchGroupRoot.addChild(branchGroupDetachable);
    		}
    	}
    	else if(SelectedRadioButton == BUTT_JOINT)
    	{
    	
    		
    		Transform3D t = new Transform3D();
    		Transform3D A1 = new Transform3D();
    		//Transform3D A2 = new Transform3D();
    		Transform3D A3 = new Transform3D();
    		Transform3D t3 = new Transform3D();
    		//Transform3D B1 = new Transform3D();
    		Transform3D B2 = new Transform3D();
    		Transform3D B3 = new Transform3D();
    		
    		t.setTranslation(new Vector3d(0,0,Z*0.5));
    		A1.setTranslation(new Vector3d(0,Z*0.2,0));
    		A3.setTranslation(new Vector3d(0,-Z*0.2,0));
    		
    		t3.setTranslation(new Vector3d(0,Z*0.2,0));
    		
    		B2.setTranslation(new Vector3d(Z*0.2,0,0));
    		B3.setTranslation(new Vector3d(-Z*0.2,0,0));
    		Buttbolts.setTransform(t);		
    		    			
    		ButtplateA1.setTransform(A1);
    		ButtplateA3.setTransform(A3);
    		
    		ButtplateB2.setTransform(B2);
    		ButtplateB3.setTransform(B3);
	          
    		Buttspbolts2.setTransform(t3);
    		if(valChange)
    		{
    			branchGroupDetachable.detach();
    			branchGroupDetachable.removeAllChildren();
    			branchGroupDetachable.addChild(buttJoint);
    			branchGroupRoot.addChild(branchGroupDetachable);
    		}
    	}
    	
    	else if(SelectedRadioButton == BLACK_BOLT)
    	{
    		Transform3D Rot = new Transform3D();    		
    		Transform3D BoltTrans = new Transform3D();    		
    		Transform3D NutTrans = new Transform3D();
    		
    		BoltTrans.setTranslation(new Vector3d(0,Z*0.2,0));
    		NutTrans.setTranslation(new Vector3d(0,-Z*0.2,0));
    		Rot.rotY(Z*8);
    		
    		blackBoltbolt.setTransform(BoltTrans);
    		blackBoltnut.setTransform(NutTrans);
    		blackBoltboltRot1.setTransform(Rot);
    		blackBoltboltRot2.setTransform(Rot);
    		blackBoltboltRot3.setTransform(Rot);
    		blackBoltnutRot1.setTransform(Rot);
    		blackBoltnutRot2.setTransform(Rot);
    		blackBoltnutRot3.setTransform(Rot);
    		
    		if(valChange)
    		{
    			branchGroupDetachable.detach();
    			branchGroupDetachable.removeAllChildren();
    			branchGroupDetachable.addChild(blackBolt);
    			branchGroupRoot.addChild(branchGroupDetachable);
    		}
    	}
    	else if(SelectedRadioButton == HSFG_BOLT)
    	{
    		Transform3D Rot = new Transform3D();    		
    		Transform3D BoltTrans = new Transform3D();    		
    		Transform3D NutTrans = new Transform3D();
    		
    		BoltTrans.setTranslation(new Vector3d(0,Z*0.3,0));
    		NutTrans.setTranslation(new Vector3d(0,-Z*0.2,0));
    		Rot.rotY(Z*8);
    		
    		HSFGBoltbolt.setTransform(BoltTrans);
    		HSFGBoltnut.setTransform(NutTrans);
    		HSFGBoltboltRot.setTransform(Rot);
    		HSFGBoltnutRot.setTransform(Rot);
    		
    		if(valChange)
    		{
    			branchGroupDetachable.detach();
    			branchGroupDetachable.removeAllChildren();
    			branchGroupDetachable.addChild(hsfgBolt);
    			branchGroupRoot.addChild(branchGroupDetachable);
    		}
    	}
    	else if(SelectedRadioButton == TURNED_BOLT)
    	{
    		Transform3D rot = new Transform3D();
    		rot.rotY(Z*10);
    		turnedBolt.setTransform(rot);
    		if(stage > 49)
    		{
    			stage = 0;
    		}
    		if(valChange)
    		{
    			branchGroupDetachable.detach();
    			branchGroupDetachable.removeAllChildren();
    			branchGroupDetachable.addChild(turnedBolt);
    			branchGroupRoot.addChild(branchGroupDetachable);
    		}
    		
    	}
    	else if(SelectedRadioButton == PIN_CONNECTION)
    	{
    		if(valChange)
    		{
    			branchGroupDetachable.detach();
    			branchGroupDetachable.removeAllChildren();
    			branchGroupDetachable.addChild(Pin);
    			branchGroupRoot.addChild(branchGroupDetachable);
    		}
    		
    	}
        }
        valChange = false;
        return;            
    }
    
    private void updateSimulationBody(final double disp){
    	
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
		       
        valChange = false;
         
		repaint();
    }
 
    
}///////////////// Defination COmplete


