package structuralanalysis;

//This is for beams with defelection 1

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
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
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleArray;
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
import javax.swing.border.EmptyBorder;
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
import eerc.vlab.common.HorizontalGraph1;
import eerc.vlab.common.J3DShape;
import eerc.vlab.common.Resources;

/**
* Simple Java 3D program that can be run as an application or as an applet.
*/
@SuppressWarnings( { "serial", "unused" })
public class PlasticHinge extends javax.swing.JPanel {
	// Variables declaration - do not modify//GEN-BEGIN:variables
	// ////////////////////////GUI componenet ///////////////////////////
	private javax.swing.JPanel topPanel;
	private javax.swing.JPanel simulationPanel;
	private javax.swing.JPanel bottomPanel;
	
	private javax.swing.JPanel createInputOutputPanel;

	private javax.swing.JPanel in1; // Input panel 1
	private javax.swing.JPanel in2; // Input panel 2
	private javax.swing.JPanel in3; // Input panel 3

	private javax.swing.JButton startButton = null;
	private javax.swing.JButton reStartButton = null;
	private javax.swing.JButton nextButton = null;

	String safty_factor = "1", materialGrade;
	int iSubVal = 1;
	private javax.swing.JButton rightIcon = null;

	// private GraphPlotter graphPlotter;
	// //////////////////////////Java3D componenet ///////////////////////////

	private SimpleUniverse univ = null; // Simple Universe Java3D
	private BranchGroup scene = null; // BranchGroup Scene graph
	TransformGroup mainFrame = new TransformGroup();
	TransformGroup frame1 = new TransformGroup();
	TransformGroup frame2 = new TransformGroup();
	TransformGroup frame3 = new TransformGroup();
	TransformGroup frame4 = new TransformGroup();
	TransformGroup frame5 = new TransformGroup();
	TransformGroup frame6 = new TransformGroup();
	Switch objSwitch = new Switch();
	
	private BeamBody freeBody = null; // Shape3D
	private HorizontalGraph1 outputGraph = null;
	private HorizontalGraph inputGraph = null;
	private FullViewGraph fullViewGraph = new FullViewGraph();

	@SuppressWarnings("unchecked")
	private HashMap hm = new HashMap();
	private J3DShape m_j3d = new J3DShape();

	private double[] fields;
	private JLabel outlbl_val[] = new JLabel[4];
	private JLabel iLabel[];
	private JLabel m_Objective = new JLabel("Objective:");

	private Timer timer = null;
	private Timer m_cameraTimer = null;
	private float m_cameraViews[];
	private int m_cameraEye;
	// Timer for simulation

	private int stage = 0;
	JComboBox cementGradeList;

	private boolean startStop = false;
	private boolean valChange = true;

	private JComboBox ch;
	private JComboBox che;
	private JLabel lbl_k;
	private JSlider m_Slider[] = new JSlider[6];
	private JLabel out_lbl[] = new JLabel[3];
	String obj_f="M15";
	String[] cement = new String[5];
	String[] cement1 = new String[3];

	public String obj = "Point Load";// ,BOS;

	int flag = 0, val = 20;
	JLabel len;

	private JComboBox End_Conditions, Column_Mat, Material_Grade, Fac_Of_Safty;

	public BranchGroup createSceneGraph() {
		BranchGroup objRoot = new BranchGroup();
		objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		objRoot.setCapability(Group.ALLOW_CHILDREN_READ);
		objRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
		objRoot.setCapability(BranchGroup.ALLOW_DETACH);

		objRoot.addChild(createVirtualLab());

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
		
	//	objRoot.addChild(m_j3d.createBox(new Vector3d(-0.3,0.0f,0), new Vector3d(0.05,0.3f,0.3),new Vector3d(0,0,0),new Color3f(1f, 1f, 0.9f),"resources/images/grey13.jpg"));

		
		float rad = (float) Math.PI / 180;
		Transform3D t = new Transform3D();
		t.setScale(0.4);

		TransformGroup tg = new TransformGroup();
		t = new Transform3D();
		t.rotX(rad * 10);
		t.setScale(new Vector3d(.5f, .05f, .5f));
		t.setTranslation(new Vector3d(.3, .3, 0));
		tg.setTransform(t);

		return objRoot;
	}

	private Canvas3D createUniverse(Container container) {
		GraphicsDevice graphicsDevice;
		if (container.getGraphicsConfiguration() != null) {
			graphicsDevice = container.getGraphicsConfiguration().getDevice();
		} else {
			graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getDefaultScreenDevice();
		}
		GraphicsConfigTemplate3D template = new GraphicsConfigTemplate3D();
		GraphicsConfiguration config = graphicsDevice
				.getBestConfiguration(template);

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
		Vector3f currPos = new Vector3f();
		t3d.get(currPos);
		t3d.lookAt(new Point3d(0, 0.4, 2.81), new Point3d(0, 0, 0),
				new Vector3d(0, 1, 0));
		t3d.invert();
		steerTG.setTransform(t3d);

		return c;
	}

	private void setLight() {
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				100.0);
		PlatformGeometry pg = new PlatformGeometry();

		Color3f ambientColor = new Color3f(0.1f, 0.1f, 0.1f);
		AmbientLight ambientLightNode = new AmbientLight(ambientColor);
		ambientLightNode.setInfluencingBounds(bounds);
		pg.addChild(ambientLightNode);

		Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
		Vector3f light1Direction = new Vector3f(1.0f, 1.0f, 1.0f);
		Color3f light2Color = new Color3f(1.0f, 1.0f, 1.0f);
		Vector3f light2Direction = new Vector3f(-1.0f, -1.0f, -1.0f);

		DirectionalLight light1 = new DirectionalLight(light1Color,
				light1Direction);
		light1.setInfluencingBounds(bounds);
		pg.addChild(light1);

		DirectionalLight light2 = new DirectionalLight(light2Color,
				light2Direction);
		light2.setInfluencingBounds(bounds);
		pg.addChild(light2);

		ViewingPlatform viewingPlatform = univ.getViewingPlatform();
		viewingPlatform.setPlatformGeometry(pg);

	}

	private void destroy() {
		univ.cleanup();
	}

	private Group createVirtualLab() {

		Transform3D t = new Transform3D();
		// t.setTranslation(new Vector3d(0,.1,.0));

		TransformGroup objtrans = new TransformGroup(t);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
				
		TransformGroup beam1 = new TransformGroup(t);
		beam1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		beam1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		TransformGroup bars1 = new TransformGroup(t);
		bars1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		bars1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		TransformGroup beam2 = new TransformGroup(t);
		beam2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		beam2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		TransformGroup bars2 = new TransformGroup(t);
		bars2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		bars2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		TransformGroup beam3 = new TransformGroup(t);
		beam3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		beam3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		TransformGroup bars3 = new TransformGroup(t);
		bars3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		bars3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		TransformGroup beam4 = new TransformGroup(t);
		beam4.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		beam4.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		TransformGroup bars4 = new TransformGroup(t);
		bars4.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		bars4.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		TransformGroup beam5 = new TransformGroup(t);
		beam5.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		beam5.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		TransformGroup bars5 = new TransformGroup(t);
		bars3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		bars3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		TransformGroup beam6 = new TransformGroup(t);
		beam3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		beam3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		TransformGroup bars6 = new TransformGroup(t);
		bars3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		bars3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		objSwitch = new Switch(Switch.CHILD_MASK);
      objSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
		
		// FRAME 1:
		beam1.addChild(createBeam(new Vector3d(0,0.3,0),new Vector3d(1,0.6,1), new Vector3d(0,0,0), 0, 0.6));
		beam1.addChild(createArrow(new Vector3d(0,0.42,0), new Vector3d(0.03,0.03,1.2),new Vector3d(0,0,180), new Color3f(0.4f,0.3f,0.1f)));
		
		bars1.addChild(m_j3d.createBox(new Vector3d(-0.28,0,0),new Vector3d(0.02,0.3,0.02), new Vector3d(0,0,0), new Color3f(0.4f,0.3f,0.1f),"resources/images/grey13.jpg"));
		bars1.addChild(m_j3d.createBox(new Vector3d(0.28,0,0),new Vector3d(0.02,0.3,0.02), new Vector3d(0,0,0), new Color3f(0.4f,0.3f,0.1f),"resources/images/grey13.jpg"));
		bars1.addChild(createArrow(new Vector3d(-0.4,0.3,0), new Vector3d(0.02,0.02,1),new Vector3d(0,0,-90), new Color3f(0.3f,0.3f,0.4f)));
		
		// FRAME 2:
		beam2.addChild(createBeam(new Vector3d(0.025,0.3,0),new Vector3d(1,0.6,1), new Vector3d(0,0,0), 0.3, 0.6));
		beam2.addChild(createArrow(new Vector3d(0.025,0.39,0), new Vector3d(0.03,0.03,1.2),new Vector3d(0,0,180), new Color3f(0.4f,0.3f,0.1f)));
		bars2.addChild(m_j3d.createBox(new Vector3d(-0.28,0,0),new Vector3d(0.02,0.3,0.02), new Vector3d(0,0,-5), new Color3f(0.4f,0.3f,0.1f),"resources/images/grey13.jpg"));
		bars2.addChild(m_j3d.createBox(new Vector3d(0.28,0,0),new Vector3d(0.02,0.3,0.02), new Vector3d(0,0,-5), new Color3f(0.4f,0.3f,0.1f),"resources/images/grey13.jpg"));
		bars2.addChild(createArrow(new Vector3d(-0.36,0.3,0), new Vector3d(0.02,0.02,1),new Vector3d(0,0,-90), new Color3f(0.3f,0.3f,0.4f)));
		
		// FRAME 3:
		beam3.addChild(createBeam(new Vector3d(0.05,0.29,0),new Vector3d(1,0.6,1), new Vector3d(0,0,0), 0.6, 0.6));
		beam3.addChild(createArrow(new Vector3d(0.05,0.37,0), new Vector3d(0.03,0.03,1.2),new Vector3d(0,0,180), new Color3f(0.4f,0.3f,0.1f)));
		bars3.addChild(m_j3d.createBox(new Vector3d(-0.28,0,0),new Vector3d(0.02,0.3,0.02), new Vector3d(0,0,-10), new Color3f(0.4f,0.3f,0.1f),"resources/images/grey13.jpg"));
		bars3.addChild(m_j3d.createBox(new Vector3d(0.28,0,0),new Vector3d(0.02,0.3,0.02), new Vector3d(0,0,-10), new Color3f(0.4f,0.3f,0.1f),"resources/images/grey13.jpg"));
		bars3.addChild(createArrow(new Vector3d(-0.32,0.3,0), new Vector3d(0.02,0.02,1),new Vector3d(0,0,-90), new Color3f(0.3f,0.3f,0.4f)));
		
		// FRAME 4:
		beam4.addChild(createBeam(new Vector3d(0.075,0.27,0),new Vector3d(1,0.6,1), new Vector3d(0,0,0), 0.9, 0.6));
		beam4.addChild(createArrow(new Vector3d(0.075,0.34,0), new Vector3d(0.03,0.03,1.2),new Vector3d(0,0,180), new Color3f(0.4f,0.3f,0.1f)));
		bars4.addChild(m_j3d.createBox(new Vector3d(-0.28,0,0),new Vector3d(0.02,0.3,0.02), new Vector3d(0,0,-15), new Color3f(0.4f,0.3f,0.1f),"resources/images/grey13.jpg"));
		bars4.addChild(m_j3d.createBox(new Vector3d(0.28,0,0),new Vector3d(0.02,0.3,0.02), new Vector3d(0,0,-15), new Color3f(0.4f,0.3f,0.1f),"resources/images/grey13.jpg"));
		bars4.addChild(createArrow(new Vector3d(-0.28,0.3,0), new Vector3d(0.02,0.02,1),new Vector3d(0,0,-90), new Color3f(0.3f,0.3f,0.4f)));
		
		// FRAME 5:
		beam5.addChild(createBeam(new Vector3d(0.1,0.25,0),new Vector3d(1,0.6,1), new Vector3d(0,0,0), 1.2, 0.6));
		beam5.addChild(createArrow(new Vector3d(0.1,0.31,0), new Vector3d(0.03,0.03,1.2),new Vector3d(0,0,180), new Color3f(0.4f,0.3f,0.1f)));
		bars5.addChild(m_j3d.createBox(new Vector3d(-0.28,0,0),new Vector3d(0.02,0.3,0.02), new Vector3d(0,0,-20), new Color3f(0.4f,0.3f,0.1f),"resources/images/grey13.jpg"));
		bars5.addChild(m_j3d.createBox(new Vector3d(0.28,0,0),new Vector3d(0.02,0.3,0.02), new Vector3d(0,0,-20), new Color3f(0.4f,0.3f,0.1f),"resources/images/grey13.jpg"));
		bars5.addChild(createArrow(new Vector3d(-0.24,0.3,0), new Vector3d(0.02,0.02,1),new Vector3d(0,0,-90), new Color3f(0.3f,0.3f,0.4f)));
		
		// FRAME 6:
		beam6.addChild(m_j3d.createBox(new Vector3d(-0.03,0.23,0),new Vector3d(0.15,0.03,0.03),new Vector3d(0,0,-25), new Color3f(1,0.75f,0.4f),"resources/images/grey13.jpg"));
		beam6.addChild(m_j3d.createBox(new Vector3d(0.25,0.235,0),new Vector3d(0.18,0.03,0.03),new Vector3d(0,0,22), new Color3f(1,0.75f,0.4f),"resources/images/grey13.jpg"));
		beam6.addChild(createArrow(new Vector3d(0.1,0.28,0), new Vector3d(0.035,0.035,1.4),new Vector3d(0,0,180), new Color3f(0.4f,0.3f,0.1f)));
		bars6.addChild(m_j3d.createBox(new Vector3d(-0.28,0,0),new Vector3d(0.02,0.3,0.02), new Vector3d(0,0,-25), new Color3f(0.4f,0.3f,0.1f),"resources/images/grey13.jpg"));
		bars6.addChild(m_j3d.createBox(new Vector3d(0.28,0,0),new Vector3d(0.02,0.3,0.02), new Vector3d(0,0,-25), new Color3f(0.4f,0.3f,0.1f),"resources/images/grey13.jpg"));
		bars6.addChild(createArrow(new Vector3d(-0.2,0.3,0), new Vector3d(0.02,0.02,1),new Vector3d(0,0,-90), new Color3f(0.3f,0.3f,0.4f)));
		
		
		//objtrans.addChild(frame1);
		frame1.addChild(bars1);
		frame1.addChild(beam1);
		//objtrans.addChild(frame2);
		frame2.addChild(bars2);
		frame2.addChild(beam2);
	//	objtrans.addChild(frame3);
		frame3.addChild(bars3);
		frame3.addChild(beam3);
//		objtrans.addChild(frame4);
		frame4.addChild(bars4);
		frame4.addChild(beam4);
	//	objtrans.addChild(frame5);
		frame5.addChild(bars5);
		frame5.addChild(beam5);
	//	objtrans.addChild(frame6);
		frame6.addChild(bars6);
		frame6.addChild(beam6);
		
		objSwitch.addChild(frame1);
		objSwitch.addChild(frame2);
		objSwitch.addChild(frame3);
		objSwitch.addChild(frame4);
		objSwitch.addChild(frame5);
		objSwitch.addChild(frame6);
		mainFrame.addChild(objSwitch);
		objtrans.addChild(mainFrame);
		return objtrans;
	}
	public Group createBeam(Vector3d pos,Vector3d scale,Vector3d rot, double extent, double length){
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
      
      double X;
      for(X = -length/2; X <= length/2; X += 0.02)
      {
      	
      	objtrans.addChild(m_j3d.createBox(new Vector3d(X,extent*X*X,0), new Vector3d(0.02,0.045,0.045),new Vector3d(0,0,Math.atan(extent*2*X)*180.0/Math.PI), new Color3f(1,0.75f,0.4f),"resources/images/grey13.jpg"));
      	
      }
      
      return objtrans;
  }
	public Group createArrow(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr){
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
      
     // t.setTranslation(vib);
      
      TransformGroup objtrans = new TransformGroup(t);
      objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
      objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);       
	   	
	    LineAttributes la = new LineAttributes();
	    Appearance appea = new Appearance();
	    la.setLineWidth(3);
	    ColoringAttributes ca = new ColoringAttributes(colr, ColoringAttributes.SHADE_FLAT);
	    appea.setColoringAttributes(ca);
	    
	    appea.setLineAttributes(la);
	    float i;
	    Point3f[] coords = new Point3f[2];
	    coords[0] = new Point3f(-1,0,0);
	    coords[1] = new Point3f(0,1,0);
	    LineArray line = new LineArray(2, LineArray.COORDINATES);
	    line.setCoordinates(0, coords);
	    Shape3D myShape = new Shape3D(line,appea);
	    objtrans.addChild(myShape);
	    	    
	    coords[0] = new Point3f(1,0,0);
	    coords[1] = new Point3f(0,1,0);
	    line = new LineArray(2, LineArray.COORDINATES);
	    line.setCoordinates(0, coords);
	    myShape = new Shape3D(line,appea);
	    objtrans.addChild(myShape);
	    
	    coords[0] = new Point3f(0,1,0);
	    coords[1] = new Point3f(0,-1,0);
	    line = new LineArray(2, LineArray.COORDINATES);
	    line.setCoordinates(0, coords);
	    myShape = new Shape3D(line,appea);
	    objtrans.addChild(myShape);
	    
	    return objtrans;
  }

	/**
	 * Creates new form FreeVibration
	 */
	public PlasticHinge(Container container) {
		// Initialize the GUI components
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		initComponents();

		centerPanel(container);
		// Create Canvas3D and SimpleUniverse; add canvas to drawing panel

		// scene.addChild(bgleg);
	}

	// ----------------------------------------------------------------

	// Applet framework

	public static class MyApplet extends JApplet {
		PlasticHinge mainPanel;

		public void init() {
			setLayout(new BorderLayout());
			mainPanel = new PlasticHinge(this);
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
			setTitle("Plastic Hinge");
			getContentPane().add(new PlasticHinge(this),
					BorderLayout.CENTER);
			pack();
		}
	}

	// Create a form with the specified labels, tooltips, and sizes.
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MyFrame().setVisible(true);
			}
		});
	}

	private void initComponents() {
		
		mainFrame.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		mainFrame.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		frame1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		frame1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		frame2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		frame2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		frame3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		frame3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		frame4.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		frame4.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		frame5.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		frame5.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		frame6.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		frame6.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		setLayout(new java.awt.BorderLayout());

		bottomPanel = new javax.swing.JPanel(); // input from user at bottom
		simulationPanel = new javax.swing.JPanel(); // 3D rendering at center
		topPanel = new javax.swing.JPanel(); // Pause, resume, Next
		//rightPanel = new javax.swing.JPanel(); // Graph and Input and Output
												// Parameter
		createInputOutputPanel = new javax.swing.JPanel();

		topPanel();
		bottomPanel();
	//	rightPanel();

		// Set Alignment
		// add(guiPanel, java.awt.BorderLayout.NORTH);
		add(topPanel, java.awt.BorderLayout.NORTH);
		add(simulationPanel, java.awt.BorderLayout.CENTER);
		add(bottomPanel, java.awt.BorderLayout.SOUTH);
	//	add(rightPanel, java.awt.BorderLayout.EAST);

		startStop = false;
		valChange = true;
		stage = 0;

		timer = new Timer(800, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// ...Perform a task...
				timerActionPerformed(evt);
				timer.setInitialDelay(0);
			}
		});

	}// </editor-fold>//GEN-END:initComponents

	private void topPanel() {

		java.awt.GridBagConstraints gridBagConstraints;

		javax.swing.JPanel guiPanel = new javax.swing.JPanel(); // Pause, resume
																// at top
		guiPanel.setLayout(new java.awt.GridBagLayout());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);

		// javax.swing.JButton pauseButton = new javax.swing.JButton();
		// javax.swing.JButton startButton = new javax.swing.JButton();
		reStartButton = new javax.swing.JButton("Re-Start");
		ImageIcon icon = m_j3d.createImageIcon("resources/icons/restart.png");
		reStartButton.setIcon(icon);
		startButton = new javax.swing.JButton("Start");
		icon = m_j3d.createImageIcon("resources/icons/start.png");
		startButton.setIcon(icon);
		nextButton = new javax.swing.JButton("Next");
		icon = m_j3d.createImageIcon("resources/icons/next.png");
		nextButton.setIcon(icon);
		// ImageIcon icon =
		// m_j3d.createImageIcon("resources/images/show_graph.png");
		// startButton.setIcon(icon);
		// startButton.setPreferredSize(new Dimension(100,30));

		// reStartButton.setText("Re-Start");
		reStartButton.setEnabled(false);
		nextButton.setEnabled(false);

		guiPanel.setBackground(new Color(67, 143, 205));// Color.BLACK
		topPanel.setLayout(new java.awt.BorderLayout());
		topPanel.add(guiPanel, java.awt.BorderLayout.NORTH);

		startButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// Toggle
				startStop = !startStop;

				if (startStop)
					startSimulation(evt);
				else
					pauseSimulation();
				univ.getCanvas().repaint();
			}
		});

		reStartButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				reStartButton.setEnabled(false);
				// startButton.setEnabled(true);
				startButton.setText("Start");
				startStop = !startStop;
				// startStop = false;
				//                
				// outputGraph.clearGraphValue();
				// inputGraph.clearGraphValue();
				//                
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

		guiPanel.add(reStartButton, gridBagConstraints);
		guiPanel.add(startButton, gridBagConstraints);
		guiPanel.add(nextButton, gridBagConstraints);

		javax.swing.JButton btn = new javax.swing.JButton("Manual");
		icon = m_j3d.createImageIcon("resources/icons/manual.png");
		btn.setIcon(icon);
		// startButton.setPreferredSize(new Dimension(100,30));
		// guiPanel.add(btn, gridBagConstraints);

		btn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				HelpWindow.createAndShowGUI("forcedVib");
			}
		});

	}
 
	private static void enable(Container root, boolean enable) {
		Component children[] = root.getComponents();
		for (int i = 0; i < children.length; i++)
			children[i].setEnabled(enable);
	}

	private void centerPanel(Container container) {

		simulationPanel.setPreferredSize(new java.awt.Dimension(1024, 600));
		simulationPanel.setLayout(new java.awt.BorderLayout());

		javax.swing.JPanel guiPanel = new javax.swing.JPanel();
		guiPanel.setBackground(new Color(100, 100, 100));
		JLabel lbl = new JLabel("Plastic Hinge", JLabel.CENTER);
		lbl.setFont(new Font("Arial", Font.BOLD, 18));

		lbl.setForeground(Color.orange);

		guiPanel.add(lbl);
		simulationPanel.add(guiPanel, BorderLayout.NORTH);

		Canvas3D c = createUniverse(container);
		simulationPanel.add(c, BorderLayout.CENTER);

		JPanel btmPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
		simulationPanel.add(btmPanel, BorderLayout.SOUTH);

		guiPanel = new javax.swing.JPanel();
		guiPanel.setBackground(new Color(100, 100, 100));
		simulationPanel.add(guiPanel, BorderLayout.EAST);

		guiPanel = new javax.swing.JPanel();
		guiPanel.setBackground(new Color(100, 100, 100));
		simulationPanel.add(guiPanel, BorderLayout.WEST);

		// Create the content branch and add it to the universe
		scene = createSceneGraph();
		univ.addBranchGraph(scene);

		m_Objective = new JLabel(" ", JLabel.LEFT);
		m_Objective.setFont(new Font("Arial", Font.BOLD, 13));
		m_Objective.setForeground(Color.WHITE);
		guiPanel = new javax.swing.JPanel();
		guiPanel.setBackground(new Color(100, 100, 100));
		guiPanel.add(m_Objective);
		btmPanel.add(guiPanel, BorderLayout.NORTH);

		guiPanel = new javax.swing.JPanel(); //          
		guiPanel.setBackground(new Color(235, 233, 215));
		guiPanel.setLayout(new java.awt.GridBagLayout());
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		guiPanel.setBorder(BorderFactory.createLineBorder(new Color(140, 200,
				240), 8));
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);

		  

		JCheckBox chkbox = new JCheckBox("");
		lbl = new JLabel("Change Input Parameters", JLabel.CENTER);
		ImageIcon icon = m_j3d.createImageIcon("resources/icons/tasklist.png");
		lbl.setIcon(icon);
		chkbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				boolean bChecked = ((JCheckBox) event.getSource()).isSelected();
				if (bChecked)
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
			public void actionPerformed(ActionEvent event) {
				boolean bChecked = ((JCheckBox) event.getSource()).isSelected();
				if (bChecked)
				/*	rightPanel.setVisible(true);
				else
					rightPanel.setVisible(false); */
				univ.getCanvas().repaint();

			}
		});
		//guiPanel.add(chkbox, gridBagConstraints);
		//guiPanel.add(lbl, gridBagConstraints);

		btmPanel.add(guiPanel, BorderLayout.CENTER);

		guiPanel = new javax.swing.JPanel(); // 
		guiPanel.setBackground(new Color(130, 169, 193));
		guiPanel.setBorder(BorderFactory.createLineBorder(new Color(235, 233,
				215), 4));
		// guiPanel.add(createInputOutputPanel());
		// btmPanel.add(guiPanel,BorderLayout.SOUTH);

	}

	 

	private void bottomPanel() {
		initInputControlsField();

		Color bk = new Color(219, 226, 238);
		bottomPanel.setLayout(new java.awt.GridLayout(1, 3));
		bottomPanel.setBackground(Color.black);
		bottomPanel.setPreferredSize(new java.awt.Dimension(1024, 120));
		bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(235,
				233, 215), 8));

		in1 = new JPanel(new java.awt.GridLayout(4, 3, 10, 10));
		in1.setBackground(bk);
		bottomPanel.add(in1);

		in2 = new JPanel(new java.awt.GridLayout(3, 3, 10, 10));
		in2.setBackground(bk);
		bottomPanel.add(in2);

		in3 = new JPanel(new java.awt.GridLayout(3, 2, 10, 10));
		in3.setBackground(bk);
		bottomPanel.add(in3);

		out_lbl[0] = new JLabel("Elastic");
		out_lbl[1] = new JLabel("NULL");		
		out_lbl[2] = new JLabel("0");
		cementGradeList = new JComboBox();
		cementGradeList.setEditable(true);

		cementGradeList.addItem("Point Load");
		cementGradeList.addItem("UDL");
		cementGradeList.addItem("UDL + Point Load");
		// cementGradeList.addItem("Decreasing UVL");
		// cementGradeList.addItem("Increasing UVL");
		
		JLabel lab,lab1;

		lab = new JLabel("Length", JLabel.RIGHT);
		m_Slider[0] = new JSlider(JSlider.HORIZONTAL, 6, 10, 6);
		m_Slider[0].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[0] = val;

				iLabel[0].setText(":: " + fields[0] + " m");
				
				Transform3D t = new Transform3D();
				double X,Z;
				X = fields[0]/6.0;				
				Z = fields[1]/250.0;
				t.setScale(new Vector3d(X,1,Z));
				mainFrame.setTransform(t);
				
				repaint();
				// univ.getCanvas().repaint();

			}
		});
		m_Slider[0].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[0]);
		in1.add(iLabel[0]);

		lab = new JLabel("Column  Breadth", JLabel.RIGHT);
		m_Slider[1] = new JSlider(JSlider.HORIZONTAL, 250, 500, 250);
		m_Slider[1].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[1] = val;

				iLabel[1].setText(":: " + fields[1] + " cm");
				
				Transform3D t = new Transform3D();
				double X,Z;
				X = fields[0]/6.0;				
				Z = fields[1]/250.0;
				t.setScale(new Vector3d(X,1,Z));
				mainFrame.setTransform(t);
								
				// univ.getCanvas().repaint();

			}
		});
		m_Slider[1].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[1]);
		in1.add(iLabel[1]);

		lab = new JLabel("Bending Force", JLabel.RIGHT);
		m_Slider[2] = new JSlider(JSlider.HORIZONTAL, 10, 100, 50);
		m_Slider[2].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[2] = val;

				iLabel[2].setText(":: " + fields[2] + " kN");
				repaint();
				// univ.getCanvas().repaint();

			}
		});
		m_Slider[2].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[2]);
		in1.add(iLabel[2]);

		lab = new JLabel("Shear Force", JLabel.CENTER);

		m_Slider[3] = new JSlider(JSlider.HORIZONTAL, 5, 10, 5);
		m_Slider[3].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[5] = val;

				iLabel[5].setText(":: " + fields[5] + " kN");

				// univ.getCanvas().repaint();

			}
		});
		m_Slider[3].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[3]);
		in1.add(iLabel[5]);

		lab = new JLabel("Material", JLabel.CENTER);
		JComboBox Column_material = new JComboBox();
		// Column_material.addItem("-");
		Column_material.addItem("Concrete");
		Column_material.addItem("Steel");
		Column_material.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				valChange = true;

				JComboBox cb = (JComboBox) e.getSource();
				obj_f = (String) cb.getSelectedItem();
				valChange = true;

				// cementGradeList.setEditable(false);
				resetOutputParameters();
				repaint();

			}
		});

		in2.add(lab);
		in2.add(Column_material);
		in2.add(iLabel[3]);

		lab = new JLabel("Material Grade", JLabel.CENTER);
		JComboBox material_Grade = new JComboBox();
		// Column_material.addItem("-");
		material_Grade.addItem("M15");
		material_Grade.addItem("M20");
		material_Grade.addItem("M25");
		// material_Grade.addItem("M30");
		material_Grade.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				valChange = true;

				JComboBox cb = (JComboBox) e.getSource();
				obj_f = (String) cb.getSelectedItem();
				valChange = true;

				// cementGradeList.setEditable(false);
				resetOutputParameters();
				repaint();

			}
		});

		in2.add(lab);
		in2.add(material_Grade);
		lab1=new JLabel("", JLabel.RIGHT);
		in2.add(lab1);

		lab = new JLabel("Type of Loading", JLabel.CENTER);

		cementGradeList.setEditable(false);
		cementGradeList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				valChange = true;
				obj = (String) ((JComboBox) e.getSource()).getSelectedItem();
				System.out.println(obj);
				StringBuffer obj1 = new StringBuffer(obj);
			//	iSubVal = getiSubVal(obj);
				resetOutputParameters();
				repaint();
			}
		});

	//	in2.add(lab);
	//	in2.add(cementGradeList);
		in2.add(iLabel[4]);

		lab = new JLabel("Loading Range", JLabel.LEFT);
		in3.add(lab);
		in3.add(out_lbl[0]);
		lab = new JLabel("Breaking Load", JLabel.LEFT);
		in3.add(lab);
		in3.add(out_lbl[1]);
		lab = new JLabel("Max Defelection", JLabel.LEFT);
		len = new JLabel("0 m", JLabel.LEFT);
		in3.add(lab);
		in3.add(out_lbl[2]);

		bottomPanel.setVisible(false);
	}
	double getDeflection(double W, double l, double b, double Y){
		double del=0;
		del = W*l*l*l/(4.0*b*Y);
		return del;
	}
	double getBreakingLoad(double b, double Y, double l){
		double W = 0;
		W = 4*b*Y/(l*l*l);
		return W;
	}
 
	private void initInputControlsField() {

		iLabel = new JLabel[9];
		int i = 0;
		iLabel[i] = new JLabel("6 m", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel("250 cm", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel("50 kN", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel(" ", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel(" ", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel("5 kN", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel("5 kN", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel("5 kN", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel(" ", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);

		i = 0;
		fields = new double[9];
		fields[0] = 6.0;
		fields[1] = 250.0;
		fields[2] = 50.0;
		fields[3] = 50.0;
		fields[4] = 50.0;
		fields[5] = 50.0;
		fields[6] = 5.0;
		fields[7] = 5.0;
		fields[8] = 250.0;

	}

	private void onNextStage() {
		System.out.println("You are tring to see the stage" + stage);
		valChange = true;
		resetOutputParameters();
		bottomPanel.setVisible(true);
		enableStage(stage);
		setInstructionText();

	}

	private void enableStage(int s) {
		switch (s) {
		case 0: // Home
			enable(in1, false);
			enable(in2, false);
			enable(in3, false);

			break;

		case 1:

			enable(in1, true);
			enable(in2, true);
			enable(in3, true);

			break;

		case 2:

			enable(in1, true);
			enable(in2, true);
			enable(in3, true);

			break;

		case 3:

			enable(in1, true);
			enable(in2, true);
			enable(in3, true);

			break;

		case 4:

			enable(in1, true);
			enable(in2, true);
			enable(in3, true);
			nextButton.setVisible(false);
			break;

		}

	}

	private void setInstructionText() {

		valChange = true;
		resetOutputParameters();

		switch (stage) {
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
			m_Objective.setForeground(Color.GREEN);
			break;
		case 3:
			m_Objective.setText(" ");
			m_Objective.setForeground(Color.GREEN);
			break;
		case 4:
			m_Objective.setText(" ");
			m_Objective.setForeground(Color.GREEN);
			break;

		}

	}

	private void resetOutputParameters() {
		int i = 2;

		// outlbl_val[i++].setText(" 0 sec");
		// outlbl_val[i++].setText(" 0 (m/s)");

	}

	private void setCameraViews() {
		m_cameraViews = new float[360];
		int i = 0;
		for (i = 0; i < 90; i++)
			m_cameraViews[i] = i;
		for (int j = 0; j < 90; j++, i++)
			m_cameraViews[i] = (90 - j);
		for (int j = 0; j < 90; j++, i++)
			m_cameraViews[i] = -j;
		for (int j = 0; j < 90; j++, i++)
			m_cameraViews[i] = -(90 - j);

		m_cameraEye = 0;

	}

	private void timerActionVerticalCameraMotion(java.awt.event.ActionEvent evt) {
		ViewingPlatform vp = univ.getViewingPlatform();
		TransformGroup steerTG = vp.getViewPlatformTransform();
		Transform3D t3d = new Transform3D();
		steerTG.getTransform(t3d);

		Vector3f currPos = new Vector3f();        
		t3d.get(currPos);

		float y = (float) (float) Math.sin(Math
				.toRadians(m_cameraViews[m_cameraEye]));
		float z = 2.41f - Math.abs(y);

		t3d.lookAt(new Point3d(0, y, z), new Point3d(0, 0, 0), new Vector3d(0,
				1, 0));
		t3d.invert();

		steerTG.setTransform(t3d);
		m_cameraEye++;
		if (m_cameraEye == 180) {
			m_cameraTimer.stop();
			m_cameraEye = 0;
		}
	}

	private void timerActionHorizontalCameraMotion(
			java.awt.event.ActionEvent evt) {
		ViewingPlatform vp = univ.getViewingPlatform();
		TransformGroup steerTG = vp.getViewPlatformTransform();
		Transform3D t3d = new Transform3D();
		steerTG.getTransform(t3d);

		Vector3f currPos = new Vector3f();
		t3d.get(currPos);

		float x = (float) (float) Math.sin(Math
				.toRadians(m_cameraViews[m_cameraEye]));
		float z = 2.41f - Math.abs(x);

		t3d.lookAt(new Point3d(x, 0, z), new Point3d(0, 0, 0), new Vector3d(0,
				1, 0));
		t3d.invert();

		steerTG.setTransform(t3d);
		m_cameraEye++;
		if (m_cameraEye == 360) {
			m_cameraTimer.stop();
			m_cameraEye = 0;
		}
	}

	private void startSimulation(java.awt.event.ActionEvent evt) {
		ImageIcon icon = m_j3d.createImageIcon("resources/icons/stop.png");
		startButton.setIcon(icon);
		startButton.setText("Stop");
		enableStage(0);
		reStartButton.setEnabled(false);
		nextButton.setEnabled(false);

		/*outputGraph.setState(1);
		inputGraph.setState(1)*/

		if (valChange) {
			System.out.println(obj);
			System.out.println(fields[0]);
			System.out.println(fields[1]);
			System.out.println(fields[2]);
			System.out.println(fields[5]);
			System.out.println(fields[6]);
			System.out.println(fields[7]);

			int small = 0; // = getiSubVal(obj);
			System.out.println(small);

		/*freeBody.init(fields[0], fields[1], fields[2], 1, small, fields[5],
					fields[6], fields[7]); */

		//	inputGraph.clearGraphValue();
		//	outputGraph.clearGraphValue();

		}

		timer.start();
		System.out.println("Timer started");
	}

	private void timerActionPerformed(java.awt.event.ActionEvent evt) {

		 
		
		double def,bLoad;
		//System.out.println(obj_f);
		
		if(obj_f.equalsIgnoreCase("M20")){
			def = getDeflection(fields[2], fields[0], fields[1], 20);		
			bLoad = getBreakingLoad(fields[1], 20, fields[0]);
		}
		else if(obj_f.equalsIgnoreCase("M25")){
			def = getDeflection(fields[2], fields[0], fields[1], 25);		
			bLoad = getBreakingLoad(fields[1], 25, fields[0]);
		}
		else {
			def = getDeflection(fields[2], fields[0], fields[1], 15);		
			bLoad = getBreakingLoad(fields[1], 15, fields[0]);
		}
		
		out_lbl[2].setText((int)(def*100) + " cm");
		out_lbl[1].setText((int)(bLoad+1) + " kN");
		
		
		if(def < 1.0)
		{
			stage = stage%5;
			out_lbl[0].setText("Elastic");
		}
		else
		{
			stage = stage%6;
			out_lbl[0].setText("Plastic");
		}
		java.util.BitSet visibleNodes = new java.util.BitSet( objSwitch.numChildren() );
      visibleNodes.set(stage);
      objSwitch.setChildMask(visibleNodes);
      stage++;        
		return;
	}

	private void updateSimulationBody(double disp) {

		float rad = (float) Math.PI / 180;
		Transform3D trans = new Transform3D();
		TransformGroup tgp = (TransformGroup) hm.get("cylinder");
		tgp.getTransform(trans);
		Vector3d s = new Vector3d();
		trans.getScale(s);
		float val = (float) disp * 200;
		trans.rotZ(rad * val);
		trans.setScale(s);
		trans.setTranslation(new Vector3d(0, -0.21, -0.1));
		tgp.setTransform(trans);

	}

	private void pauseSimulation() {
		timer.stop();
		ImageIcon icon = m_j3d.createImageIcon("resources/icons/start.png");
		startButton.setIcon(icon);
		startButton.setText("Start");
		reStartButton.setEnabled(true);
		// bottomPanel.setVisible(true);
		nextButton.setEnabled(true);

	//	rightPanel.setVisible(true);
		enableStage(stage);
	//	outputGraph.setState(0);
	//	inputGraph.setState(0);
		// startButton.setEnabled(true);
		stage = 0;
		valChange = false;

		repaint();
	}

	public void update(float addy) {

		Vector3d s = new Vector3d();
		// Get Scale

		TransformGroup objtrans = (TransformGroup) hm.get("target1");
		Transform3D trans = new Transform3D();
		objtrans.getTransform(trans);
		trans.getScale(s);
		trans.setScale(s);
		trans.setTranslation(new Vector3d(0.4, 0.272 - addy, 0.1));

		objtrans.setTransform(trans);

	}

	public void update1(float addy) {

		Vector3d s = new Vector3d();
		// Get Scale

		TransformGroup objtrans = (TransformGroup) hm.get("target2");
		Transform3D trans = new Transform3D();
		objtrans.getTransform(trans);
		trans.getScale(s);
		trans.setScale(s);
		trans.setTranslation(new Vector3d(0.4, 0.272 - addy, 0.1));

		objtrans.setTransform(trans);

	}

}
