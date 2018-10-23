package com.vlab.fm;


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
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

//import eerc.vlab.common.FullViewGraph;
//import eerc.vlab.common.HelpWindow;
import eerc.vlab.common.HorizontalGraph;
import eerc.vlab.common.J3DShape;
import eerc.vlab.common.Resources;

/**
 * Simple Java 3D program that can be run as an application or as an applet.
 */
@SuppressWarnings( { "serial", "unused" })
public class Bernoullis extends javax.swing.JPanel {
	// Variables declaration - do not modify//GEN-BEGIN:variables
	// ////////////////////////GUI componenet ///////////////////////////
	private javax.swing.JPanel topPanel;
	private javax.swing.JPanel simulationPanel;
	private javax.swing.JPanel bottomPanel;
	private javax.swing.JPanel rightPanel;

	private javax.swing.JPanel in1; // Input panel 1
	private javax.swing.JPanel in2; // Input panel 2
	private javax.swing.JPanel in3; // Input panel 3

	private javax.swing.JButton startButton = null;
	private javax.swing.JButton reStartButton = null;
	private javax.swing.JButton nextButton = null;

	String objImg, safty_factor = "1", materialGrade;
	private javax.swing.JButton rightIcon = null;

	// private GraphPlotter graphPlotter;
	// //////////////////////////Java3D componenet ///////////////////////////

	private SimpleUniverse univ = null; // Simple Universe Java3D
	private BranchGroup scene = null; // BranchGroup Scene graph
	TransformGroup inkc3Pos = new TransformGroup(new Transform3D());
	TransformGroup ink2c3Pos = new TransformGroup(new Transform3D());
	TransformGroup ink3c3Pos = new TransformGroup(new Transform3D());
	TransformGroup inkc2Pos = new TransformGroup(new Transform3D());
	TransformGroup ink2c2Pos = new TransformGroup(new Transform3D());
	TransformGroup ink3c2Pos = new TransformGroup(new Transform3D());
	TransformGroup inkc1Pos = new TransformGroup(new Transform3D());
	TransformGroup ink2c1Pos = new TransformGroup(new Transform3D());
	TransformGroup ink3c1Pos = new TransformGroup(new Transform3D());
	TransformGroup ink4c1Pos = new TransformGroup(new Transform3D());
	
	
	TransformGroup inkcappi1Pos = new TransformGroup(new Transform3D());
	TransformGroup inkcappi2Pos = new TransformGroup(new Transform3D());
	TransformGroup inkcappi3Pos = new TransformGroup(new Transform3D());
	TransformGroup inkcappi4Pos = new TransformGroup(new Transform3D());
	
	private BernoullisBody freeBody = null; // Shape3D
	private HorizontalGraph outputGraph = null;
	private HorizontalGraph inputGraph = null;
	// private FullViewGraph fullViewGraph = new FullViewGraph();

	@SuppressWarnings("unchecked")
	private HashMap hm = new HashMap();
	private J3DShape m_j3d = new J3DShape();

	private double[] fields;
	private JLabel outlbl_val[];
	private JLabel iLabel[];
	private JLabel m_Objective = new JLabel("Objective:");

	private Timer timer = null;
	private Timer m_cameraTimer = null;
	private float m_cameraViews[];
	private int m_cameraEye;
	// Timer for simulation

	private int stage = 0;

	private boolean startStop = false;
	private boolean valChange = true;

	private JComboBox ch;
	private JComboBox che;
	private JLabel lbl_k;
	private JSlider m_Slider[] = new JSlider[5];
	private JLabel out_lbl[] = new JLabel[2];

	int flag = 0, val = 20;

	public BranchGroup createSceneGraph() {
		// Create the root of the branch graph
		BranchGroup objRoot = new BranchGroup();
		objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		objRoot.setCapability(Group.ALLOW_CHILDREN_READ);
		objRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
		objRoot.setCapability(BranchGroup.ALLOW_DETACH);

		objRoot.addChild(createVirtualLab());
		
		/*objRoot.addChild(m_j3d.createBox(new Vector3d(0, -0.5, -.1),
				new Vector3d(3, .01, 1), new Vector3d(0, 0, 0), new Color3f(0f,
						1f, 0f), "resources/images/table.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0, 0.4, -.6),
				new Vector3d(3, .9, .1), new Vector3d(0f, 0f, 0f), new Color3f(
						0.5f, 0.6f, 0.72f))); */
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
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.1f,-2.0), new Vector3d(1,0.7f,0.05),new Vector3d(0,0,0),new Color3f(1f, 1f, 1.0f),"resources/images/382.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.84f,0), new Vector3d(1.05,0.04f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/376.jpg"));
		

		float rad = (float) Math.PI / 180;
		Transform3D t = new Transform3D();
		t.setScale(0.4);

		TransformGroup tg = new TransformGroup();
		t = new Transform3D();
		t.rotX(rad * 10);
		t.setScale(new Vector3d(.5f, .05f, .5f));
		t.setTranslation(new Vector3d(.3, .3, 0));
		tg.setTransform(t);
		freeBody = new BernoullisBody();
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

	/**
	 * @return
	 */
	private Group createVirtualLab() {

		 Transform3D t = new Transform3D();
	     //t.setTranslation(new Vector3d(0,.1,.0));
	                 
		    TransformGroup objtrans = new TransformGroup(t);
		    objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		    
		    Transform3D t1 = new Transform3D();
		    TransformGroup pump = new TransformGroup(t1);
		    TransformGroup cylinder1 = new TransformGroup(t1);
		    TransformGroup cylinder2 = new TransformGroup(t1);
		    TransformGroup cylinder3 = new TransformGroup(t1);
		    TransformGroup cappi1 = new TransformGroup(t1);
		    TransformGroup cappi2 = new TransformGroup(t1);
		    TransformGroup cappi3 = new TransformGroup(t1);
		    TransformGroup cappi4 = new TransformGroup(t1);
		    TransformGroup backboard = new TransformGroup(t1);
		    TransformGroup bucket = new TransformGroup(t1);
		    
		    Transform3D inkroot1 = new Transform3D();
		    inkroot1.setTranslation(new Vector3d(-0.7,-0.01f,0.01));
		    TransformGroup inkc3 = new TransformGroup(inkroot1);
		    
		    Transform3D inkroot2 = new Transform3D();
		    inkroot2.setTranslation(new Vector3d(-0.32f,-0.01f,0.0));
		    TransformGroup ink2c3 = new TransformGroup(inkroot2);
		   
		    Transform3D inkroot3 = new Transform3D();
		    inkroot3.setTranslation(new Vector3d(-0.325,-0.31f,0));
		    TransformGroup ink3c3 = new TransformGroup(inkroot3);
		   
		    Transform3D inkroot4 = new Transform3D();
		    inkroot4.setTranslation(new Vector3d(-0.294,-0.315f,0.0));
		    TransformGroup inkc2 = new TransformGroup(inkroot4);
		   
		    Transform3D inkroot5 = new Transform3D();
		    inkroot5.setTranslation(new Vector3d(-0.095,-0.315f,0.0));
		    TransformGroup ink2c2 = new TransformGroup(inkroot5);
		   
		    Transform3D inkroot6 = new Transform3D();
		    inkroot6.setTranslation(new Vector3d(0.1,-0.315f,0.0));
		    TransformGroup ink3c2 = new TransformGroup(inkroot6);
		   	
		    Transform3D inkroot7 = new Transform3D();
		    inkroot7.setTranslation(new Vector3d(0.3,-0.315f,0.0));
		    TransformGroup inkc1 = new TransformGroup(inkroot7);
		    
		    Transform3D inkroot8 = new Transform3D();
		    inkroot8.setTranslation(new Vector3d(0.438,-0.315f,0.0));
		    TransformGroup ink2c1 = new TransformGroup(inkroot8);
		    
		    Transform3D inkroot9 = new Transform3D();
		    inkroot9.setTranslation(new Vector3d(-0.131f,-0.3f,0.0));
		    TransformGroup inkcappi1 = new TransformGroup(inkroot9);
		    
		    Transform3D inkroot10 = new Transform3D();
		    inkroot10.setTranslation(new Vector3d(-0.001f,-0.3f,0));
		    TransformGroup inkcappi2 = new TransformGroup(inkroot10);
		    
		    Transform3D inkroot11 = new Transform3D();
		    inkroot11.setTranslation(new Vector3d(0.129f,-0.3f,0.0));
		    TransformGroup inkcappi3 = new TransformGroup(inkroot11);
		    
		    Transform3D inkroot12 = new Transform3D();
		    inkroot12.setTranslation(new Vector3d(0.259f,-0.3f,0.0));
		    TransformGroup inkcappi4 = new TransformGroup(inkroot12);
		    
		    Transform3D inkroot13 = new Transform3D();
		    inkroot13.setTranslation(new Vector3d(0.435,0.0f,0.0));
		    TransformGroup ink3c1 = new TransformGroup(inkroot13);
		    
		    Transform3D inkroot14 = new Transform3D();
		    inkroot14.setTranslation(new Vector3d(0.572,0.0f,0.0));
		    TransformGroup ink4c1 = new TransformGroup(inkroot14);
		    
		    		    
		    pump.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    cylinder1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    cylinder2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    cylinder3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    cappi1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    cappi2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    cappi3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    cappi4.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    backboard.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    bucket.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    inkc3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    ink2c3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    ink3c3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    inkc2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    ink2c2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    ink3c2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    inkc1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    ink2c1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    ink3c1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    ink4c1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			   
		    
		    inkcappi1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    inkcappi2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    inkcappi3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    inkcappi4.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			         
		    
		    pump.addChild(m_j3d.createBox(new Vector3d(-0.45f,-0f,0.05f),new Vector3d(0.1f,0.04f,0.001f), new Vector3d(0.0f,5.0f,0.0f),new Color3f(1f, 1f, 1f),"resources/images/motor.jpg"));
		    pump.addChild(m_j3d.createCylinder(new Vector3d(-0.45,-0.2,0),new Vector3d(0.2,3,0.2),new Vector3d(0,0,0), new Color3f(0.4f,0.4f,0.4f)));
		    cylinder1.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(0.2,-0.315f,0), new Vector3d(0.13,5.0,0.15),new Vector3d(0,0,90),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.2f,0.2f,0.2f),30.0f));
		    cylinder1.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(0.438,-0.15f,0), new Vector3d(0.13,3.2,0.15),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.2f,0.2f,0.2f),30.0f));
		    cylinder1.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(0.5,0.0f,0), new Vector3d(0.13,1.5,0.15),new Vector3d(0,0,90),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.2f,0.2f,0.2f),30.0f));
		    cylinder1.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(0.572,-0.05f,0), new Vector3d(0.13,1.15,0.15),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.2f,0.2f,0.2f),30.0f));
		    
		    cylinder2.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(-0.2,-0.315f,0), new Vector3d(0.3,2.0,0.3),new Vector3d(0,0,90),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.2f,0.2f,0.2f),30.0f));
		    cylinder2.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(-0.0,-0.315f,0), new Vector3d(0.25,2.0,0.25),new Vector3d(0,0,90),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.2f,0.2f,0.2f),30.0f));
		    cylinder2.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(0.2,-0.315f,0), new Vector3d(0.2,2.0,0.2),new Vector3d(0,0,90),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.2f,0.2f,0.2f),30.0f));
		    cylinder3.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(-0.6,-0.01f,0), new Vector3d(0.13,2.0,0.13),new Vector3d(0,0,90),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.2f,0.2f,0.2f),30.0f));
		    cylinder3.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(-0.32f,-0.158f,0), new Vector3d(.13,3.1,.13),new Vector3d(0,0,-1),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.3f,0.3f,0.3f),30.0f));
		    cylinder3.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(-0.396,-0.01f,0), new Vector3d(0.13,1.8,0.13),new Vector3d(0,0,90),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.2f,0.2f,0.2f),30.0f));
		    cylinder3.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(-0.3,-0.31f,0), new Vector3d(0.13,0.65,0.13),new Vector3d(0,0,90),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.2f,0.2f,0.2f),30.0f));
		    
		    
		    
		    backboard.addChild(m_j3d.createBox(new Vector3d(0.08,-0.04,-0.05), new Vector3d(0.3,0.3,0.01), new Vector3d(0,0,0),new Color3f(1,1,1),"resources/images/board.jpg"));
		    bucket.addChild(m_j3d.createCylinder(new Vector3d(0.6f,-0.35f,0),new Vector3d(1f,0.1f,1),new Vector3d(0.,-0,0),new Color3f(0.3f,0.3f,0.3f)));
		    bucket.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(0.6f,-0.288f,0), new Vector3d(1,1,1),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.3f,0.3f,0.3f),30.0f));
		    bucket.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(0.6f,-0.18f,0), new Vector3d(1.1,1.2,1.1),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.3f,0.3f,0.3f),30.0f));
		    bucket.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(0.6f,-0.131f,0), new Vector3d(1.15,0.2,1.15),new Vector3d(0,0,0),  m_j3d.getColor3f(80,80,80),m_j3d.getColor3f(80,80,80),m_j3d.getColor3f(80,80,80),m_j3d.getColor3f(90,91,90),new Color3f(0.3f,0.3f,0.3f),30.0f));
			   
		    
		    cappi1.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(-0.13f,-0.05f,0), new Vector3d(.1,4.9,.1),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.3f,0.3f,0.3f),30.0f));
		    cappi2.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(0.0f,-0.05f,0), new Vector3d(.1,4.9,.1),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.3f,0.3f,0.3f),30.0f));
		    cappi3.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(0.13f,-0.05f,0), new Vector3d(.1,4.9,.1),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.3f,0.3f,0.3f),30.0f));
		    cappi4.addChild(m_j3d.createCylinderWithMatProp( new Vector3d(0.26f,-0.05f,0), new Vector3d(.1,4.9,.1),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.3f,0.3f,0.3f),30.0f));
			
		    inkc3Pos.addChild(m_j3d.createCylinder(new Vector3d(0,0,0),new Vector3d(0.12,0.1,0.12),new Vector3d(0,0,90),new Color3f(1,0,0)));
		    ink2c3Pos.addChild(m_j3d.createCylinder(new Vector3d(0,0,0),new Vector3d(0.11,0.1,0.11),new Vector3d(0,0,0),new Color3f(1,0,0)));
		    ink3c3Pos.addChild(m_j3d.createCylinder(new Vector3d(0,0,0),new Vector3d(0.11,0.1,0.11),new Vector3d(0,0,90),new Color3f(1,0,0)));
		    inkc2Pos.addChild(m_j3d.createCylinder(new Vector3d(0,0,0), new Vector3d(0.28,0.1,0.28),new Vector3d(0,0,90),new Color3f(1,0,0)));
		    ink2c2Pos.addChild(m_j3d.createCylinder(new Vector3d(0,0,0), new Vector3d(0.23,0.1,0.23),new Vector3d(0,0,90),new Color3f(1,0,0)));
		    ink3c2Pos.addChild(m_j3d.createCylinder(new Vector3d(0,0,0), new Vector3d(0.18,0.1,0.18),new Vector3d(0,0,90),new Color3f(1,0,0)));
		    inkc1Pos.addChild(m_j3d.createCylinder(new Vector3d(0,0,0), new Vector3d(0.12,0.1,0.12),new Vector3d(0,0,90),new Color3f(1,0,0)));
		    ink2c1Pos.addChild(m_j3d.createCylinder(new Vector3d(0,0,0), new Vector3d(0.1,0.1,0.1),new Vector3d(0,0,0),new Color3f(1,0,0)));
		    ink3c1Pos.addChild(m_j3d.createCylinder(new Vector3d(0,0,0), new Vector3d(0.1,0.1,0.1),new Vector3d(0,0,90),new Color3f(1,0,0)));
		    ink4c1Pos.addChild(m_j3d.createCylinder(new Vector3d(0,0,0), new Vector3d(0.1,0.1,0.1),new Vector3d(0,0,0),new Color3f(1,0,0)));
		    
		    
		    inkcappi1Pos.addChild(m_j3d.createCylinder(new Vector3d(0,0,0), new Vector3d(0.1,0.1,0.1),new Vector3d(0,0,0),new Color3f(1,0,0)));
		    inkcappi2Pos.addChild(m_j3d.createCylinder(new Vector3d(0,0,0), new Vector3d(0.1,0.1,0.1),new Vector3d(0,0,0),new Color3f(1,0,0)));
		    inkcappi3Pos.addChild(m_j3d.createCylinder(new Vector3d(0,0,0), new Vector3d(0.1,0.1,0.1),new Vector3d(0,0,0),new Color3f(1,0,0)));
		    inkcappi4Pos.addChild(m_j3d.createCylinder(new Vector3d(0,0,0), new Vector3d(0.1,0.1,0.1),new Vector3d(0,0,0),new Color3f(1,0,0)));
			      	   
		    
		    inkc3.addChild(inkc3Pos);
		    ink2c3.addChild(ink2c3Pos);
		    ink3c3.addChild(ink3c3Pos);
		    inkc2.addChild(inkc2Pos);
		    ink2c2.addChild(ink2c2Pos);
		    ink3c2.addChild(ink3c2Pos);
		    inkc1.addChild(inkc1Pos);
		    ink2c1.addChild(ink2c1Pos);
		    ink3c1.addChild(ink3c1Pos);
		    ink4c1.addChild(ink4c1Pos);
		    
		    
		    inkcappi1.addChild(inkcappi1Pos);
		    inkcappi2.addChild(inkcappi2Pos);
		    inkcappi3.addChild(inkcappi3Pos);
		    inkcappi4.addChild(inkcappi4Pos);
		    
			   
		    objtrans.addChild(pump);
		    objtrans.addChild(cylinder1);
		    objtrans.addChild(cylinder2);
		    objtrans.addChild(cylinder3);
		    objtrans.addChild(backboard);

		    objtrans.addChild(bucket);
		    objtrans.addChild(cappi1);
		    objtrans.addChild(cappi2);
		    objtrans.addChild(cappi3);
		    objtrans.addChild(cappi4);
		    
		    objtrans.addChild(inkc3);
		    objtrans.addChild(ink2c3);
		    objtrans.addChild(ink3c3);
		    objtrans.addChild(inkc2);
		    objtrans.addChild(ink2c2);
		    objtrans.addChild(ink3c2);
		    objtrans.addChild(inkc1);
		    objtrans.addChild(ink2c1);
		    objtrans.addChild(ink3c1);
		    objtrans.addChild(ink4c1);
		    
		    
		    objtrans.addChild(inkcappi1);
		    objtrans.addChild(inkcappi2);
		    objtrans.addChild(inkcappi3);
		    objtrans.addChild(inkcappi4);
			    
		    return objtrans;
		    	
		}


	/**
	 * Creates new form FreeVibration
	 */
	public Bernoullis(Container container) {
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
		Bernoullis mainPanel;

		public void init() {
			setLayout(new BorderLayout());
			mainPanel = new Bernoullis(this);
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
			setTitle("Bernoulli's Experiment");
			getContentPane().add(new Bernoullis(this), BorderLayout.CENTER);
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

		
		inkc3Pos.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		ink2c3Pos.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		ink3c3Pos.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		inkc2Pos.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		ink2c2Pos.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		ink3c2Pos.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		inkc1Pos.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		ink2c1Pos.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		ink3c1Pos.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		ink4c1Pos.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		inkcappi1Pos.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		inkcappi2Pos.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		inkcappi3Pos.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		inkcappi4Pos.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		setLayout(new java.awt.BorderLayout());

		bottomPanel = new javax.swing.JPanel(); // input from user at bottom
		simulationPanel = new javax.swing.JPanel(); // 3D rendering at center
		topPanel = new javax.swing.JPanel(); // Pause, resume, Next
		rightPanel = new javax.swing.JPanel(); // Graph and Input and Output
		// Parameter

		topPanel();
		bottomPanel();
		rightPanel();

		// Set Alignment
		// add(guiPanel, java.awt.BorderLayout.NORTH);
		add(topPanel, java.awt.BorderLayout.NORTH);
		add(simulationPanel, java.awt.BorderLayout.CENTER);
		add(bottomPanel, java.awt.BorderLayout.SOUTH);
		add(rightPanel, java.awt.BorderLayout.EAST);

		startStop = false;
		valChange = true;
		stage = 0;

		timer = new Timer(50, new ActionListener() {
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

		
	}

	private void rightPanel() {

		rightPanel.setLayout(new java.awt.BorderLayout());

		JPanel panel = new JPanel();
		panel.setBackground(new Color(140, 200, 240));
		panel.setBorder(BorderFactory.createLineBorder(
				new Color(132, 132, 255), 4));
		panel.setBorder(new EmptyBorder(10, 10, 0, 0));

		rightPanel.setVisible(false);

	}

	private static void enable(Container root, boolean enable) {
		//Component children[] = root.getComponents();
		//for (int i = 0; i < children.length; i++)
		//	children[i].setEnabled(enable);
		return;
	}

	private void centerPanel(Container container) {

		simulationPanel.setPreferredSize(new java.awt.Dimension(1024, 600));
		simulationPanel.setLayout(new java.awt.BorderLayout());

		javax.swing.JPanel guiPanel = new javax.swing.JPanel();
		guiPanel.setBackground(new Color(100, 100, 100));
		JLabel lbl = new JLabel("Bernoulli's Experiment", JLabel.CENTER);
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
		lbl = new JLabel("Show Results", JLabel.CENTER);

		icon = m_j3d.createImageIcon("resources/icons/show_graph.png");
		lbl.setIcon(icon);
		chkbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				boolean bChecked = ((JCheckBox) event.getSource()).isSelected();
				if (bChecked)
					rightPanel.setVisible(true);
				else
					rightPanel.setVisible(false);
				univ.getCanvas().repaint();

			}
		});
		// guiPanel.add(chkbox, gridBagConstraints);
		// guiPanel.add(lbl, gridBagConstraints);

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
		bottomPanel.setLayout(new java.awt.GridLayout());

//	    
		bottomPanel.setBackground(Color.black);
		bottomPanel.setPreferredSize(new java.awt.Dimension(1024, 120));
		bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(235,
				233, 215), 8));

		in1 = new JPanel(new java.awt.GridLayout(5, 3, 10, 10));
		in1.setBackground(bk);
	
		JLabel lab = new JLabel("Temp of Water ", JLabel.CENTER);
		m_Slider[0] = new JSlider(JSlider.HORIZONTAL, 1, 10, 3);
		// m_Slider[0].setMajorTickSpacing(10);
		// m_Slider[0].setPaintTicks(true);
		// m_Slider[0].setPaintLabels(true);
		m_Slider[0].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[0] = val*10;

				iLabel[0].setText(":: " + fields[0]  + " C");

				// univ.getCanvas().repaint();
				repaint();
			}
		});
		m_Slider[0].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[0]);
		in1.add(iLabel[0]);

		lab = new JLabel("Dia of Measuring Tank ", JLabel.CENTER);
		m_Slider[1] = new JSlider(JSlider.HORIZONTAL, 1, 10, 3);
		// m_Slider[1].setMajorTickSpacing(10);
		// m_Slider[1].setPaintTicks(true);
		// m_Slider[1].setPaintLabels(true);
		m_Slider[1].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[1] = val;

				iLabel[1].setText(":: " + fields[1] + " C");

				// univ.getCanvas().repaint();
				repaint();
			}
		});
		m_Slider[1].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[1]);
		in1.add(iLabel[1]);

		lab = new JLabel("Collected Water Level ", JLabel.CENTER);
		m_Slider[2] = new JSlider(JSlider.HORIZONTAL, 1, 10, 3);
		// m_Slider[2].setMajorTickSpacing(10);
		// m_Slider[2].setPaintTicks(true);
		// m_Slider[2].setPaintLabels(true);
		m_Slider[2].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[2] = val;

				iLabel[2].setText(":: " + fields[2] + " cm");

				// univ.getCanvas().repaint();
				repaint();
			}
		});
		m_Slider[2].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[2]);
		in1.add(iLabel[2]);

	//	in2 = new JPanel(new java.awt.GridLayout(2, 2, 1, 1));
	//	in2.setBackground(bk);
		

		lab = new JLabel("Time Taken to Collect Water ", JLabel.CENTER);
		m_Slider[3] = new JSlider(JSlider.HORIZONTAL, 30, 180, 60);

		m_Slider[3].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[3] = val;

				iLabel[3].setText(":: " + fields[3] + " sec");

				// univ.getCanvas().repaint();
				repaint();
			}
		});
		m_Slider[3].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[3]);
		in1.add(iLabel[3]);

		lab = new JLabel("Dia of Pipe", JLabel.CENTER);
		m_Slider[4] = new JSlider(JSlider.HORIZONTAL, 1, 5, 2);
		// m_Slider[2].setMajorTickSpacing(10);
		// m_Slider[2].setPaintTicks(true);
		// m_Slider[2].setPaintLabels(true);
		m_Slider[4].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[4] = val;

				iLabel[4].setText(":: " + fields[4] + " cm");

				// univ.getCanvas().repaint();
				repaint();
			}
		});
		m_Slider[4].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[4]);
		in1.add(iLabel[4]);

		in3 = new JPanel(new java.awt.GridLayout(2, 2, 5, 5));
		in3.setBackground(bk);
		

		outlbl_val = new JLabel[2];
		lab = new JLabel("Regime of Flow ", JLabel.RIGHT);
		out_lbl[0] = new JLabel(" Flow Type ", JLabel.RIGHT);
		in3.add(lab);
		in3.add(out_lbl[0]);

		lab = new JLabel("Reynolds Number ", JLabel.RIGHT);
		out_lbl[1] = new JLabel("  ", JLabel.RIGHT);
		in3.add(lab);
		in3.add(out_lbl[1]);
		bottomPanel.add(in1);
	//	bottomPanel.add(in2,d);
		bottomPanel.add(in3);
		bottomPanel.setVisible(false);
	}

	private void initInputControlsField() {

		iLabel = new JLabel[5];
		int i = 0;
		iLabel[i] = new JLabel("30 C", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel(" 3 cm", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel(" 15 cm", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel(" 60 sec", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel(" 2 cm", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);

		i = 0;
		fields = new double[5];
		fields[0] = 30.0;
		fields[1] = 3.0;
		fields[2] = 15.0;
		fields[3] = 60.0;
		fields[4] = 2.0;

	}

	private void onNextStage() {

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

	private void startSimulation(java.awt.event.ActionEvent evt)
	{
		ImageIcon icon = m_j3d.createImageIcon("resources/icons/stop.png");
		startButton.setIcon(icon);
		startButton.setText("Stop");
		enableStage(0);
		reStartButton.setEnabled(false);
		nextButton.setEnabled(false);
		if (valChange) {
		//	System.out.println(fields[0]);
		//	System.out.println(fields[1]);
		//	System.out.println(fields[2]);
		//	System.out.println(fields[3]);
		//	System.out.println(fields[4]);
			 freeBody.init(fields[0], fields[1], fields[2], fields[3], fields[4]);
			//freeBody.init(30, 3, 15, 60, 2);
		}

		timer.start();
		
		System.out.println("Timer started");
	}

	private void timerActionPerformed(java.awt.event.ActionEvent evt) {
		
		stage ++;
		Transform3D t1 = new Transform3D();
		if(stage<16)
		{
			
			t1.setScale(new Vector3d((float)stage*10/4,1,1));
			t1.setTranslation(new Vector3d((float)stage/80,0,0));
			inkc3Pos.setTransform(t1);
		}
		else if (stage < 28)
		{
			t1.setScale(new Vector3d(1,(float)(stage-15)*10/4,1));
			t1.setTranslation(new Vector3d(0,0.01f-(float)(stage-15)/80,0.01));
			ink2c3Pos.setTransform(t1);
		}
		else if(stage < 29)
		{
			t1.setScale(new Vector3d((float)(stage-27)*10/4,1,1));
			t1.setTranslation(new Vector3d((float)(stage-27)/80,0,0.01));
			ink3c3Pos.setTransform(t1);
		}
		else if(stage < 67)
		{
			t1.setScale(new Vector3d((float)(stage-28)/2,1,1));
			t1.setTranslation(new Vector3d((float)(stage-28)/400,0,0.01));
			inkc2Pos.setTransform(t1);
			
		
		}
		else if(stage < 91)
		{
			t1.setScale(new Vector3d((float)(stage-65)*3/4,1,1));
			t1.setTranslation(new Vector3d((float)(stage-65)*3/800,0,0.01));
			ink2c2Pos.setTransform(t1);
			

			t1.setScale(new Vector3d(1,(float)(stage-68)*10/4.5,1));
			t1.setTranslation(new Vector3d(0,(float)(stage-68)/90,0.01));
			inkcappi1Pos.setTransform(t1);
		}
		else if(stage <110)
		{
			t1.setScale(new Vector3d((float)(stage-90),1,1));
			t1.setTranslation(new Vector3d((float)(stage-90)/200,0,0.01));
			ink3c2Pos.setTransform(t1);
			
			t1.setScale(new Vector3d(1,(float)(stage-90)*10/4.5,1));
			t1.setTranslation(new Vector3d(0,(float)(stage-90)/90,0.01));
			inkcappi2Pos.setTransform(t1);
			
		}
		else if(stage < 116)
		{
			t1.setScale(new Vector3d((float)(stage-109)*10/4.2,1,1));
			t1.setTranslation(new Vector3d((float)(stage-109)/85,0,0.01));
			inkc1Pos.setTransform(t1);			
		}
		else if(stage < 129)
		{
			t1.setScale(new Vector3d(1,(float)(stage-115)*10/4,1));
			t1.setTranslation(new Vector3d(0,(float)(stage-115)/80,0.01));
			ink2c1Pos.setTransform(t1);
			
			
			t1.setScale(new Vector3d(1,(float)(stage-115)*10/3.5,1));
			t1.setTranslation(new Vector3d(0,(float)(stage-115)/70,0.01));
			inkcappi3Pos.setTransform(t1);
			
			inkcappi4Pos.setTransform(t1);
		}
		else if(stage < 134)
		{
			t1.setScale(new Vector3d((float)(stage-128)*10/3.8,1,1));
			t1.setTranslation(new Vector3d((float)(stage-128)/76,0,0.01));
			ink3c1Pos.setTransform(t1);
		}
		else if(stage < 140)
		{
			t1.setScale(new Vector3d(1,(float)(stage-133)*10/3.8,1));
			t1.setTranslation(new Vector3d(0,-(float)(stage-133)/76,0.01));
			ink4c1Pos.setTransform(t1);
		}
		
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
		nextButton.setEnabled(true);
		// rightPanel.setVisible(true);
		enableStage(stage);
		// outputGraph.setState(0);
		// inputGraph.setState(0);
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

	public void cool() {
		try {
			Thread.sleep(250); // do nothing for 1000 miliseconds (1 second)
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Group createTriangle(Point3d point1, Point3d point2, Point3d point3,
			Vector3d scale, Vector3d rot, Color3f color) {
		Transform3D t = new Transform3D();
		float rad = (float) Math.PI / 180;
		if (rot.x != 0)
			t.rotX(rad * rot.x);
		else if (rot.y != 0)

			t.rotY(rad * rot.y);
		else if (rot.z != 0)
			t.rotZ(rad * rot.z);
		t.setScale(scale);
		TransformGroup objtrans = new TransformGroup(t);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		Shape3D shape = new Shape3D();
		TriangleArray tri = new TriangleArray(3, TriangleArray.COORDINATES
				| TriangleArray.COLOR_3);
		tri.setCoordinate(0, point1);
		tri.setCoordinate(1, point2);
		tri.setCoordinate(2, point3);
		tri.setColor(0, color);
		tri.setColor(1, color);
		tri.setColor(2, color);
		shape.setGeometry(tri);
		objtrans.addChild(shape);
		return objtrans;

	}

}

class BernoullisBody {
	double ReynoldsNumber;
	double Re;
	double Q, V;

	public void init(double temp, double dia_ofTank, double cwl, double tttc,
			double dia_pipe) {
		double mew = 0.0;
		if (temp == 0) {
			mew = 1.787;
		}
		if (temp == 10) {
			mew = 1.307;
		}
		if (temp == 20) {
			mew = 1.004;
		}
		if (temp == 30) {
			mew = 0.801;
		}
		if (temp == 40) {
			mew = 0.658;
		}
		if (temp == 50) {
			mew = 0.553;
		}
		if (temp == 60) {
			mew = 0.475;
		}
		if (temp == 70) {
			mew = 0.413;
		}
		if (temp == 80) {
			mew = 0.365;
		}
		if (temp == 90) {
			mew = 0.326;
		}
		if (temp == 100) {
			mew = 0.294;
		}
		Q = (Math.PI * (dia_ofTank * dia_ofTank) * cwl) / (4 * tttc);
		V = (4 * Q) / (Math.PI * Math.pow(dia_pipe, 2));
		Re = V * dia_pipe / mew;
	}

	public double getRe() {
		return Re;
	}

	public String getTypeOfFlow() {
		if (Re < 2000) {
			return "Laminar Flow";
		}
		if (Re < 4000) {
			return "Turbulent Flow";
		}
		if (Re >= 2000 && Re <= 4000) {
			return "Transient Flow";
		}
		return "Flow calculating";
	}
}

