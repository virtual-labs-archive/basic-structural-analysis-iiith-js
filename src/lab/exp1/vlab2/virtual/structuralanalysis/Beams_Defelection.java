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
public class Beams_Defelection extends javax.swing.JPanel {
	// Variables declaration - do not modify//GEN-BEGIN:variables
	// ////////////////////////GUI componenet ///////////////////////////
	private javax.swing.JPanel topPanel;

	private javax.swing.JPanel simulationPanel;

	private javax.swing.JPanel bottomPanel;

	private javax.swing.JPanel rightPanel;

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

	private javax.swing.JButton rightIcon1 = null;

	private javax.swing.JButton rightIcon2 = null;

	// private GraphPlotter graphPlotter;
	// //////////////////////////Java3D componenet ///////////////////////////

	private SimpleUniverse univ = null; // Simple Universe Java3D

	private BranchGroup scene = null; // BranchGroup Scene graph

	TransformGroup beam = new TransformGroup(new Transform3D());

	private Switch objSwitch = new Switch();

	private Switch objSwitchArrPoint = new Switch();

	private Switch objSwitchArrUDL = new Switch();

	private Switch objSwitchArrUnP = new Switch();

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

	String obj_f;

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

		int i, j;
		for (i = -2; i <= 2; i++) {
			for (j = -2; j <= 2; j++) {
				objRoot.addChild(m_j3d.createBox(new Vector3d((float) (i),
						-0.6, (float) (j)), new Vector3d(0.5, .01, 0.5),
						new Vector3d(0, 0, 0), new Color3f(.8f, .8f, .8f),
						"resources/images/tile.jpg"));
			}
		}
		// objRoot.addChild(m_j3d.createBox(new Vector3d(0,-0.6, -.1),new
		// Vector3d(1.5,.01,1.5),new Vector3d(0,0,0),new Color3f(.8f, .8f,
		// .8f),"resources/images/tile.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0, 0.4, -2.5),
				new Vector3d(10, 10, .01), new Vector3d(0f, 0f, 0f),
				new Color3f(0.5f, 0.6f, 0.72f)));
		// Walls and roof
		objRoot.addChild(m_j3d.createBox(new Vector3d(1, 0.1f, 0),
				new Vector3d(0.05, 0.7f, 2), new Vector3d(0, 0, 0),
				new Color3f(1f, 1f, 0.9f), "resources/images/floor.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(-1, 0.1f, 0),
				new Vector3d(0.05, 0.7f, 2), new Vector3d(0, 0, 0),
				new Color3f(1f, 1f, 0.9f), "resources/images/floor.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0, 0.1f, -2.0),
				new Vector3d(1, 0.7f, 0.05), new Vector3d(0, 0, 0),
				new Color3f(1f, 1f, 1.0f)));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0, 0.84f, 0),
				new Vector3d(1.05, 0.04f, 2), new Vector3d(0, 0, 0),
				new Color3f(1f, 1f, 1f), "resources/images/floor.jpg"));

		objRoot.addChild(m_j3d.createBox(new Vector3d(-0.3, 0.0f, 0),
				new Vector3d(0.05, 0.3f, 0.3), new Vector3d(0, 0, 0),
				new Color3f(1f, 1f, 0.9f), "resources/images/grey13.jpg"));

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

		// beam.addChild(createBeam(new Vector3d(-0.22,0,0),new
		// Vector3d(1,1,1),new Vector3d(0,0,0), 0, 0.24));

		// beam.addChild(m_j3d.createBox(new Vector3d(0,0,0), new
		// Vector3d(0.24,0.025,0.025),new Vector3d(0,0,0),new
		// Color3f(1,1,1),"resources/images/floor.jpg"));
		objtrans.addChild(m_j3d.createText2D("FIXED", new Vector3d(-0.2, 0.08,
				0.2), new Vector3d(0.6, 0.6, 0.6), new Color3f(0.3f, 1f, 0.3f),
				16, Font.BOLD));
		objtrans.addChild(m_j3d.createText2D("FREE", new Vector3d(0.25, 0.08,
				0.2), new Vector3d(0.6, 0.6, 0.6), new Color3f(0.3f, 1f, 0.3f),
				16, Font.BOLD));

		TransformGroup defBeam1 = new TransformGroup(t);
		TransformGroup defBeam2 = new TransformGroup(t);
		TransformGroup defBeam3 = new TransformGroup(t);
		TransformGroup defBeam4 = new TransformGroup(t);
		TransformGroup defBeam5 = new TransformGroup(t);
		TransformGroup defBeam6 = new TransformGroup(t);
		TransformGroup defBeam7 = new TransformGroup(t);

		TransformGroup singArr1 = new TransformGroup(t);
		TransformGroup singArr2 = new TransformGroup(t);
		TransformGroup singArr3 = new TransformGroup(t);
		TransformGroup singArr4 = new TransformGroup(t);
		TransformGroup singArr5 = new TransformGroup(t);
		TransformGroup singArr6 = new TransformGroup(t);
		TransformGroup singArr7 = new TransformGroup(t);

		TransformGroup UDLArr1 = new TransformGroup(t);
		TransformGroup UDLArr2 = new TransformGroup(t);
		TransformGroup UDLArr3 = new TransformGroup(t);
		TransformGroup UDLArr4 = new TransformGroup(t);
		TransformGroup UDLArr5 = new TransformGroup(t);
		TransformGroup UDLArr6 = new TransformGroup(t);

		TransformGroup UnPArr1 = new TransformGroup(t);
		TransformGroup UnPArr2 = new TransformGroup(t);
		TransformGroup UnPArr3 = new TransformGroup(t);
		TransformGroup UnPArr4 = new TransformGroup(t);
		TransformGroup UnPArr5 = new TransformGroup(t);
		TransformGroup UnPArr6 = new TransformGroup(t);
		TransformGroup UnPArr7 = new TransformGroup(t);

		objSwitch = new Switch(Switch.CHILD_MASK);
		objSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
		objSwitchArrPoint = new Switch(Switch.CHILD_MASK);
		objSwitchArrPoint.setCapability(Switch.ALLOW_SWITCH_WRITE);

		objSwitchArrUDL = new Switch(Switch.CHILD_MASK);
		objSwitchArrUDL.setCapability(Switch.ALLOW_SWITCH_WRITE);
		objSwitchArrUnP = new Switch(Switch.CHILD_MASK);
		objSwitchArrUnP.setCapability(Switch.ALLOW_SWITCH_WRITE);

		defBeam1.addChild(createBeam(new Vector3d(-0.22, 0, 0), new Vector3d(1,
				1, 1), new Vector3d(0, 0, 0), 0, 0.24));
		singArr1.addChild(createArrow(new Vector3d(0.23, 0.06, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.6f, 0.5f, 0.1f)));
		defBeam2.addChild(createBeam(new Vector3d(-0.22, 0, 0), new Vector3d(1,
				1, 1), new Vector3d(0, 0, 0), 5, 0.24));
		singArr2.addChild(createArrow(new Vector3d(0.23, 0.05, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.6f, 0.5f, 0.1f)));
		defBeam3.addChild(createBeam(new Vector3d(-0.22, 0, 0), new Vector3d(1,
				1, 1), new Vector3d(0, 0, 0), 10, 0.24));
		singArr3.addChild(createArrow(new Vector3d(0.23, 0.04, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.6f, 0.5f, 0.1f)));
		defBeam4.addChild(createBeam(new Vector3d(-0.22, 0, 0), new Vector3d(1,
				1, 1), new Vector3d(0, 0, 0), 14, 0.24));
		singArr4.addChild(createArrow(new Vector3d(0.23, 0.03, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.6f, 0.5f, 0.1f)));
		defBeam5.addChild(createBeam(new Vector3d(-0.22, 0, 0), new Vector3d(1,
				1, 1), new Vector3d(0, 0, 0), 17, 0.24));
		singArr5.addChild(createArrow(new Vector3d(0.23, 0.02, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.6f, 0.5f, 0.1f)));
		defBeam6.addChild(createBeam(new Vector3d(-0.22, 0, 0), new Vector3d(1,
				1, 1), new Vector3d(0, 0, 0), 20, 0.24));
		defBeam7.addChild(createBeam(new Vector3d(-0.22, 0, 0), new Vector3d(1,
				1, 1), new Vector3d(0, 0, 0), 23, 0.24));

		UDLArr1.addChild(createArrow(new Vector3d(0.22, 0.06, 0), new Vector3d(
				0.02, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.1f,
				0.2f, 0.5f)));
		UDLArr1.addChild(createArrow(new Vector3d(0.13, 0.06, 0), new Vector3d(
				0.02, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.1f,
				0.2f, 0.5f)));
		UDLArr1.addChild(createArrow(new Vector3d(0.04, 0.06, 0), new Vector3d(
				0.02, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.1f,
				0.2f, 0.5f)));
		UDLArr1.addChild(createArrow(new Vector3d(-0.05, 0.06, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));

		UDLArr2.addChild(createArrow(new Vector3d(0.22, 0.05, 0), new Vector3d(
				0.02, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.1f,
				0.2f, 0.5f)));
		UDLArr2.addChild(createArrow(new Vector3d(0.13, 0.051, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UDLArr2.addChild(createArrow(new Vector3d(0.04, 0.052, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UDLArr2.addChild(createArrow(new Vector3d(-0.05, 0.053, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));

		UDLArr3.addChild(createArrow(new Vector3d(0.22, 0.04, 0), new Vector3d(
				0.02, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.1f,
				0.2f, 0.5f)));
		UDLArr3.addChild(createArrow(new Vector3d(0.13, 0.0412, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UDLArr3.addChild(createArrow(new Vector3d(0.04, 0.0424, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UDLArr3.addChild(createArrow(new Vector3d(-0.05, 0.0436, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));

		UDLArr4.addChild(createArrow(new Vector3d(0.22, 0.03, 0), new Vector3d(
				0.02, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.1f,
				0.2f, 0.5f)));
		UDLArr4.addChild(createArrow(new Vector3d(0.13, 0.036, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UDLArr4.addChild(createArrow(new Vector3d(0.04, 0.038, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UDLArr4.addChild(createArrow(new Vector3d(-0.05, 0.0436, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));

		UDLArr5.addChild(createArrow(new Vector3d(0.22, 0.02, 0), new Vector3d(
				0.02, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.1f,
				0.2f, 0.5f)));
		UDLArr5.addChild(createArrow(new Vector3d(0.13, 0.03, 0), new Vector3d(
				0.02, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.1f,
				0.2f, 0.5f)));
		UDLArr5.addChild(createArrow(new Vector3d(0.04, 0.032, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UDLArr5.addChild(createArrow(new Vector3d(-0.05, 0.0436, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));

		UnPArr1.addChild(createArrow(new Vector3d(0.22, 0.06, 0), new Vector3d(
				0.02, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.1f,
				0.2f, 0.5f)));
		UnPArr1.addChild(createArrow(new Vector3d(0.13, 0.06, 0), new Vector3d(
				0.02, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.1f,
				0.2f, 0.5f)));
		UnPArr1.addChild(createArrow(new Vector3d(0.04, 0.06, 0), new Vector3d(
				0.02, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.1f,
				0.2f, 0.5f)));
		UnPArr1.addChild(createArrow(new Vector3d(-0.05, 0.06, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UnPArr1.addChild(createArrow(new Vector3d(0.22, 0.11, 0), new Vector3d(
				0.03, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.4f,
				0.2f, 0.2f)));

		UnPArr2.addChild(createArrow(new Vector3d(0.22, 0.05, 0), new Vector3d(
				0.02, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.1f,
				0.2f, 0.5f)));
		UnPArr2.addChild(createArrow(new Vector3d(0.13, 0.051, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UnPArr2.addChild(createArrow(new Vector3d(0.04, 0.052, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UnPArr2.addChild(createArrow(new Vector3d(-0.05, 0.053, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UnPArr2.addChild(createArrow(new Vector3d(0.22, 0.1, 0), new Vector3d(
				0.03, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.4f,
				0.2f, 0.2f)));

		UnPArr3.addChild(createArrow(new Vector3d(0.22, 0.04, 0), new Vector3d(
				0.02, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.1f,
				0.2f, 0.5f)));
		UnPArr3.addChild(createArrow(new Vector3d(0.13, 0.0412, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UnPArr3.addChild(createArrow(new Vector3d(0.04, 0.0424, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UnPArr3.addChild(createArrow(new Vector3d(-0.05, 0.0436, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UnPArr3.addChild(createArrow(new Vector3d(0.22, 0.09, 0), new Vector3d(
				0.03, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.4f,
				0.2f, 0.2f)));

		UnPArr4.addChild(createArrow(new Vector3d(0.22, 0.03, 0), new Vector3d(
				0.02, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.1f,
				0.2f, 0.5f)));
		UnPArr4.addChild(createArrow(new Vector3d(0.13, 0.036, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UnPArr4.addChild(createArrow(new Vector3d(0.04, 0.038, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UnPArr4.addChild(createArrow(new Vector3d(-0.05, 0.0436, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UnPArr4.addChild(createArrow(new Vector3d(0.22, 0.08, 0), new Vector3d(
				0.03, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.4f,
				0.2f, 0.2f)));

		UnPArr5.addChild(createArrow(new Vector3d(0.22, 0.02, 0), new Vector3d(
				0.02, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.1f,
				0.2f, 0.5f)));
		UnPArr5.addChild(createArrow(new Vector3d(0.13, 0.03, 0), new Vector3d(
				0.02, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.1f,
				0.2f, 0.5f)));
		UnPArr5.addChild(createArrow(new Vector3d(0.04, 0.032, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UnPArr5.addChild(createArrow(new Vector3d(-0.05, 0.0436, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UnPArr5.addChild(createArrow(new Vector3d(0.22, 0.07, 0), new Vector3d(
				0.03, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.4f,
				0.2f, 0.2f)));

		UnPArr6.addChild(createArrow(new Vector3d(0.22, 0.01, 0), new Vector3d(
				0.02, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.1f,
				0.2f, 0.5f)));
		UnPArr6.addChild(createArrow(new Vector3d(0.13, 0.03, 0), new Vector3d(
				0.02, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.1f,
				0.2f, 0.5f)));
		UnPArr6.addChild(createArrow(new Vector3d(0.04, 0.032, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UnPArr6.addChild(createArrow(new Vector3d(-0.05, 0.0436, 0),
				new Vector3d(0.02, 0.02, 1), new Vector3d(0, 0, 180),
				new Color3f(0.1f, 0.2f, 0.5f)));
		UnPArr6.addChild(createArrow(new Vector3d(0.22, 0.06, 0), new Vector3d(
				0.03, 0.02, 1), new Vector3d(0, 0, 180), new Color3f(0.4f,
				0.2f, 0.2f)));

		// beam.addChild(UnPArr1);

		objSwitch.addChild(defBeam1);
		objSwitch.addChild(defBeam2);
		objSwitch.addChild(defBeam3);
		objSwitch.addChild(defBeam4);
		objSwitch.addChild(defBeam5);
		objSwitch.addChild(defBeam6);
		objSwitch.addChild(defBeam7);

		objSwitchArrPoint.addChild(singArr1);
		objSwitchArrPoint.addChild(singArr2);
		objSwitchArrPoint.addChild(singArr3);
		objSwitchArrPoint.addChild(singArr4);
		objSwitchArrPoint.addChild(singArr5);
		objSwitchArrPoint.addChild(singArr6);
		objSwitchArrPoint.addChild(singArr7);

		objSwitchArrUDL.addChild(UDLArr1);
		objSwitchArrUDL.addChild(UDLArr2);
		objSwitchArrUDL.addChild(UDLArr3);
		objSwitchArrUDL.addChild(UDLArr4);
		objSwitchArrUDL.addChild(UDLArr5);
		objSwitchArrUDL.addChild(UDLArr6);

		objSwitchArrUnP.addChild(UnPArr1);
		objSwitchArrUnP.addChild(UnPArr2);
		objSwitchArrUnP.addChild(UnPArr3);
		objSwitchArrUnP.addChild(UnPArr4);
		objSwitchArrUnP.addChild(UnPArr5);
		objSwitchArrUnP.addChild(UnPArr6);

		objtrans.addChild(beam);
		beam.addChild(objSwitch);
		beam.addChild(objSwitchArrPoint);
		beam.addChild(objSwitchArrUDL);
		beam.addChild(objSwitchArrUnP);

		return objtrans;
	}

	public Group createBeam(Vector3d pos, Vector3d scale, Vector3d rot,
			double theta, double length) {
		// Setting the 6 degrees of freedom and Scale

		Transform3D t = new Transform3D();
		float rad = (float) Math.PI / 180;
		if (rot.x != 0)
			t.rotX(rad * rot.x);
		else if (rot.y != 0)
			t.rotY(rad * rot.y);
		else if (rot.z != 0)
			t.rotZ(rad * rot.z);
		t.setScale(scale);
		t.setTranslation(pos);

		TransformGroup objtrans = new TransformGroup(t);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		double ang = theta / 10;
		theta = theta * 2.0 * Math.PI / 3600.0;
		int i, j;
		double X, Y;
		X = 0.0;
		Y = 0.0;
		for (i = 0; i < 10; i++) {
			objtrans.addChild(m_j3d.createBox(new Vector3d(2 * X, 2 * Y, 0),
					new Vector3d(length / 10, 0.025, 0.025), new Vector3d(0, 0,
							-ang * (i + 1)), new Color3f(1, 1, 1),
					"resources/images/floor.jpg"));
			X += Math.cos(i * theta) * length / 10;
			Y -= Math.sin(i * theta) * length / 10;
		}

		return objtrans;
	}

	public Group createArrow(Vector3d pos, Vector3d scale, Vector3d rot,
			Color3f colr) {
		// Setting the 6 degrees of freedom and Scale
		Transform3D t = new Transform3D();
		float rad = (float) Math.PI / 180;
		if (rot.x != 0)
			t.rotX(rad * rot.x);
		else if (rot.y != 0)
			t.rotY(rad * rot.y);
		else if (rot.z != 0)
			t.rotZ(rad * rot.z);
		t.setScale(scale);
		t.setTranslation(pos);

		// t.setTranslation(vib);

		TransformGroup objtrans = new TransformGroup(t);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		LineAttributes la = new LineAttributes();
		Appearance appea = new Appearance();
		la.setLineWidth(3);
		ColoringAttributes ca = new ColoringAttributes(colr,
				ColoringAttributes.SHADE_FLAT);
		appea.setColoringAttributes(ca);

		appea.setLineAttributes(la);
		float i;
		Point3f[] coords = new Point3f[2];
		coords[0] = new Point3f(-1, 0, 0);
		coords[1] = new Point3f(0, 1, 0);
		LineArray line = new LineArray(2, LineArray.COORDINATES);
		line.setCoordinates(0, coords);
		Shape3D myShape = new Shape3D(line, appea);
		objtrans.addChild(myShape);

		coords[0] = new Point3f(1, 0, 0);
		coords[1] = new Point3f(0, 1, 0);
		line = new LineArray(2, LineArray.COORDINATES);
		line.setCoordinates(0, coords);
		myShape = new Shape3D(line, appea);
		objtrans.addChild(myShape);

		coords[0] = new Point3f(0, 1, 0);
		coords[1] = new Point3f(0, -1, 0);
		line = new LineArray(2, LineArray.COORDINATES);
		line.setCoordinates(0, coords);
		myShape = new Shape3D(line, appea);
		objtrans.addChild(myShape);

		return objtrans;
	}

	/**
	 * Creates new form FreeVibration
	 */
	public Beams_Defelection(Container container) {
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
		Beams_Defelection mainPanel;

		public void init() {
			setLayout(new BorderLayout());
			mainPanel = new Beams_Defelection(this);
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
			setTitle("Analysis of Beams");
			getContentPane().add(new Beams_Defelection(this),
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

		beam.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		setLayout(new java.awt.BorderLayout());

		bottomPanel = new javax.swing.JPanel(); // input from user at bottom
		simulationPanel = new javax.swing.JPanel(); // 3D rendering at center
		topPanel = new javax.swing.JPanel(); // Pause, resume, Next
		rightPanel = new javax.swing.JPanel(); // Graph and Input and Output
		// Parameter
		createInputOutputPanel = new javax.swing.JPanel();

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

		timer = new Timer(400, new ActionListener() {
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

	private void rightPanel() {

		rightPanel.setLayout(new java.awt.GridLayout(2, 1, 0, 1));
		rightPanel.setPreferredSize(new java.awt.Dimension(300, 600));
		rightPanel.setBorder(BorderFactory.createLineBorder(new Color(140, 200,
				240), 8));
		JPanel panel = new JPanel();
		panel.setBackground(new Color(140, 200, 240));

		panel.setBorder(BorderFactory.createLineBorder(
				new Color(132, 132, 255), 4));
		panel.setBorder(new EmptyBorder(10, 10, 0, 0));

		ImageIcon icon = m_j3d.createImageIcon("resources/C_A123/1.jpg");
		rightIcon = new javax.swing.JButton(" ");
		rightIcon.setIcon(icon);
		//panel.add(rightIcon);
		rightPanel.add(rightIcon);

		ImageIcon icon2 = m_j3d.createImageIcon("resources/SFD&BMD/3.jpg");
		rightIcon1 = new javax.swing.JButton(" ");
		rightIcon1.setIcon(icon2);
	//	panel.add(rightIcon1);
		rightPanel.add(rightIcon1);

//		ImageIcon icon3 = m_j3d.createImageIcon("resources/C_A123/1.jpg");
//		rightIcon2 = new javax.swing.JButton(" ");
//		rightIcon2.setIcon(icon3);
//	//	panel.add(rightIcon2);
//		rightPanel.add(rightIcon2);

		// outputGraph = new HorizontalGraph1(300, 150, "t", "u''(t)");
		// outputGraph.setHeading("Graph 1");
		// outputGraph.setAxisUnit("sec", "g");
		// outputGraph.setYAxisColor(new Color(0.0f, 0.54f, 0.27f));// Color.
		// // DARK_GRAY
		// // );
		// outputGraph.setYScale(10);
		// outputGraph.fitToYwindow(true);
		// rightPanel.add(outputGraph);
		//
		// inputGraph = new HorizontalGraph(300, 150, "t", "u''(t)");
		// inputGraph.setHeading("Graph 2");
		// inputGraph.setAxisUnit("sec", "g");
		// inputGraph.setYAxisColor(new Color(0.0f, 0.54f,
		// 0.27f));//Color.DARK_GRAY
		// // );
		// inputGraph.setYScale(10);
		// inputGraph.fitToYwindow(true);
		// rightPanel.add(inputGraph);

		rightPanel.setVisible(false);

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
		JLabel lbl = new JLabel("Analysis of Beams (Cantilever)", JLabel.CENTER);
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

		// javax.swing.JButton viewButton= new
		// javax.swing.JButton("Horizontal View");
		// ImageIcon icon = m_j3d.createImageIcon("resources/icons/h-view.png");
		// viewButton.setIcon(icon);
		// viewButton.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(java.awt.event.ActionEvent evt) {
		//             	
		// // 0 ,
		// if(m_cameraTimer!=null && m_cameraTimer.isRunning()){
		// m_cameraTimer.stop();}
		// setCameraViews();
		// m_cameraTimer = new Timer(200,new ActionListener() {
		// public void actionPerformed(ActionEvent evt) {
		// //...Perform a task...
		// timerActionHorizontalCameraMotion(evt);
		// }
		// });
		// m_cameraTimer.start();
		// }
		// });
		//
		//         
		// guiPanel.add(viewButton, gridBagConstraints);
		//          
		// viewButton= new javax.swing.JButton("Vertical View");
		// icon = m_j3d.createImageIcon("resources/icons/v-view.png");
		// viewButton.setIcon(icon);
		// viewButton.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(java.awt.event.ActionEvent evt) {
		//             	
		// if(m_cameraTimer!=null && m_cameraTimer.isRunning()){
		// m_cameraTimer.stop();}
		// setCameraViews();
		// m_cameraTimer = new Timer(200,new ActionListener() {
		// public void actionPerformed(ActionEvent evt) {
		// timerActionVerticalCameraMotion(evt);
		// }
		// });
		// m_cameraTimer.start();
		//            	 
		// }
		// });
		//         
		// guiPanel.add(viewButton, gridBagConstraints);

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
					rightPanel.setVisible(true);
				else
					rightPanel.setVisible(false);
				univ.getCanvas().repaint();

			}
		});
		guiPanel.add(chkbox, gridBagConstraints);
		guiPanel.add(lbl, gridBagConstraints);

		btmPanel.add(guiPanel, BorderLayout.CENTER);

		guiPanel = new javax.swing.JPanel(); // 
		guiPanel.setBackground(new Color(130, 169, 193));
		guiPanel.setBorder(BorderFactory.createLineBorder(new Color(235, 233,
				215), 4));
		// guiPanel.add(createInputOutputPanel());
		// btmPanel.add(guiPanel,BorderLayout.SOUTH);

	}

	// private String getImg() {
	// if(objImg=="Both Ends Pinned")
	// {
	// String str="resources/CA/1.jpg";
	// rightIcon.setIcon(m_j3d.createImageIcon(str));
	// return str;
	//				
	// }
	// else if(objImg=="One End Fixed Other End Free")
	// {
	// String str="resources/CA/2.jpg";
	// rightIcon.setIcon(m_j3d.createImageIcon(str));
	// return str;
	// }
	// else if(objImg=="Both ends Fixed")
	// {
	// String str="resources/CA/3.jpg";
	// rightIcon.setIcon(m_j3d.createImageIcon(str));
	// return str;
	//				
	// }
	// else if(objImg=="One end Fixed Other End Pinned")
	// {
	// String str="resources/CA/4.jpg";
	// rightIcon.setIcon(m_j3d.createImageIcon(str));
	// return str;
	//				
	// }
	// else
	// return "resources/CA/1.jpg";
	// }
	//    
	// private Component createInputOutputPanel(){
	//    	
	//	 
	//        
	// }

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

		out_lbl[0] = new JLabel("0");
		out_lbl[1] = new JLabel("0");
		out_lbl[2] = new JLabel("0");
		cementGradeList = new JComboBox();
		cementGradeList.setEditable(true);

		cementGradeList.addItem("Point Load");
		cementGradeList.addItem("UDL");
		cementGradeList.addItem("UDL + Point Load");
		// cementGradeList.addItem("Decreasing UVL");
		// cementGradeList.addItem("Increasing UVL");

		JLabel lab, lab1;

		lab = new JLabel("Length", JLabel.RIGHT);
		m_Slider[0] = new JSlider(JSlider.HORIZONTAL, 1, 3, 2);
		m_Slider[0].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[0] = val;

				iLabel[0].setText(":: " + fields[0] + " m");
				// getEL();
				repaint();
				// univ.getCanvas().repaint();

			}
		});
		m_Slider[0].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[0]);
		in1.add(iLabel[0]);

		lab = new JLabel("Column    Breadth", JLabel.RIGHT);
		m_Slider[1] = new JSlider(JSlider.HORIZONTAL, 250, 500, 250);
		m_Slider[1].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[1] = val;

				iLabel[1].setText(":: " + fields[1] + " mm");

				// univ.getCanvas().repaint();

			}
		});
		m_Slider[1].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[1]);
		in1.add(iLabel[1]);

		lab = new JLabel("Depth", JLabel.RIGHT);
		m_Slider[2] = new JSlider(JSlider.HORIZONTAL, 250, 500, 250);
		m_Slider[2].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[2] = val;

				iLabel[2].setText(":: " + fields[2] + " mm");
				repaint();
				// univ.getCanvas().repaint();

			}
		});
		m_Slider[2].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[2]);
		in1.add(iLabel[2]);

		lab = new JLabel("Weight Acting", JLabel.CENTER);

		m_Slider[3] = new JSlider(JSlider.HORIZONTAL, 5, 10, 5);
		m_Slider[3].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[5] = val;

				iLabel[5].setText(":: " + fields[5] + " m");

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
		lab1 = new JLabel("", JLabel.RIGHT);
		in2.add(lab1);

		lab = new JLabel("Type of Loading", JLabel.CENTER);

		cementGradeList.setEditable(false);
		cementGradeList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				valChange = true;
				obj = (String) ((JComboBox) e.getSource()).getSelectedItem();
				System.out.println(obj);
				StringBuffer obj1 = new StringBuffer(obj);
				iSubVal = getiSubVal(obj);
				resetOutputParameters();
				repaint();
			}
		});

		in2.add(lab);
		in2.add(cementGradeList);
		in2.add(iLabel[4]);

		lab = new JLabel("Shear Force", JLabel.LEFT);
		in3.add(lab);
		in3.add(out_lbl[0]);
		lab = new JLabel("Bending Movement", JLabel.LEFT);
		in3.add(lab);
		in3.add(out_lbl[1]);
		lab = new JLabel("Max Defelection", JLabel.LEFT);
		len = new JLabel("0 m", JLabel.LEFT);
		in3.add(lab);
		in3.add(out_lbl[2]);

		bottomPanel.setVisible(false);
	}

	private int getiSubVal(String obj) {

		if (obj == "Point Load" && stage == 0 || stage == 1) {
			String str = "resources/C_A123/1.jpg";
			String str2="resources/SFD&BMD/1.jpg";
			rightIcon.setIcon(m_j3d.createImageIcon(str));
			return 1;
		}

		if (obj == "UDL" && stage == 0 || stage == 1) {
			String str = "resources/C_A123/2.jpg";
			rightIcon.setIcon(m_j3d.createImageIcon(str));
			return 2;
		}
		if (obj == "UDL + Point Load" && stage == 0 || stage == 1) {
			String str = "resources/C_A123/3.jpg";
			rightIcon.setIcon(m_j3d.createImageIcon(str));
			return 3;
		}
		if (obj == "Decreasing UVL" && stage == 0 || stage == 1) {
			String str = "resources/C_A123/4.jpg";
			rightIcon.setIcon(m_j3d.createImageIcon(str));
			return 4;
		}
		if (obj == "Increasing UVL" && stage == 0 || stage == 1) {
			String str = "resources/C_A123/4.jpg";
			rightIcon.setIcon(m_j3d.createImageIcon(str));
			return 5;
		}

		return 1;
	}

	private void initInputControlsField() {

		iLabel = new JLabel[9];
		int i = 0;
		iLabel[i] = new JLabel("2 m", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel("250 mm", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel("250 mm", JLabel.LEFT);
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
		fields[0] = 2.0;
		fields[1] = 250.0;
		fields[2] = 250.0;
		fields[3] = 250.0;
		fields[4] = 250.0;
		fields[5] = 250.0;
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

		/*
		 * outputGraph.setState(1); inputGraph.setState(1)
		 */

		if (valChange) {
			System.out.println(obj);
			System.out.println(fields[0]);
			System.out.println(fields[1]);
			System.out.println(fields[2]);
			System.out.println(fields[5]);
			System.out.println(fields[6]);
			System.out.println(fields[7]);

			int small = getiSubVal(obj);
			System.out.println(small);

			/*
			 * freeBody.init(fields[0], fields[1], fields[2], 1, small,
			 * fields[5], fields[6], fields[7]);
			 */

		}

		timer.start();
		System.out.println("Timer started");
	}

	private void timerActionPerformed(java.awt.event.ActionEvent evt) {

		/*
		 * float def = (float) (freeBody.bendMnt());
		 * out_lbl[0].setText(String.valueOf(def));
		 * 
		 * float sf = (float) (freeBody.shearForce());
		 * 
		 * freeBody.update();
		 * 
		 * outputGraph.setCurrentValue(def, 0); outputGraph.addGraphValue(def /
		 * 1000000, sf / 1000000);
		 * 
		 * inputGraph.setCurrentValue(sf, 0); inputGraph.addGraphValue(sf /
		 * 1000000);
		 * 
		 * if (rightPanel.isVisible()) { outputGraph.drawGraph();
		 * inputGraph.drawGraph(); }
		 * 
		 * if (freeBody.isDataCompleted()) { timer.stop();
		 * System.out.println("timer stopped"); }
		 */
		// System.out.println("Loading : " + obj);
		stage++;
		stage = stage % 7;

		Transform3D t1 = new Transform3D();

		// System.out.println("Lenght : "+fields[0] + "Breadth : "+fields[1] +
		// "Depth : "+ fields[2]);
		t1.setScale(new Vector3d((fields[0]*3) / 6, fields[1] / 250,
				fields[2] / 250));
		t1.setTranslation(new Vector3d(0.24 * ((( fields[0]*3) / 6) - 1), 0, 0));
		beam.setTransform(t1);

		java.util.BitSet visibleNodes = new java.util.BitSet(objSwitch
				.numChildren());
		visibleNodes.set(stage);
		objSwitch.setChildMask(visibleNodes);
		java.util.BitSet pointMask = new java.util.BitSet(objSwitchArrPoint
				.numChildren());

		java.util.BitSet UDLMask = new java.util.BitSet(objSwitchArrUDL
				.numChildren());

		java.util.BitSet UnPMask = new java.util.BitSet(objSwitchArrUnP
				.numChildren());

		if (obj.equals("Point Load")) {
			pointMask.set(stage);
			objSwitchArrPoint.setChildMask(pointMask);
			UDLMask.set(7);
			objSwitchArrUDL.setChildMask(UDLMask);
			UnPMask.set(7);
			objSwitchArrUnP.setChildMask(UnPMask);

		} else if (obj.equals("UDL")) {
			pointMask.set(7);
			objSwitchArrPoint.setChildMask(pointMask);
			UDLMask.set(stage);
			objSwitchArrUDL.setChildMask(UDLMask);
			UnPMask.set(7);
			objSwitchArrUnP.setChildMask(UnPMask);
		} else {
			pointMask.set(7);
			objSwitchArrPoint.setChildMask(pointMask);
			UDLMask.set(7);
			objSwitchArrUDL.setChildMask(UDLMask);
			UnPMask.set(stage);
			objSwitchArrUnP.setChildMask(UnPMask);

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
		// bottomPanel.setVisible(true);
		nextButton.setEnabled(true);

		rightPanel.setVisible(true);
		enableStage(stage);
		//outputGraph.setState(0);
	//	inputGraph.setState(0);
		// startButton.setEnabled(true);

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

class BeamBody {
	double length;

	private ArrayList<Double> SF = new ArrayList<Double>();

	private ArrayList<Double> BM = new ArrayList<Double>();

	private ArrayList<Double> moment = new ArrayList<Double>();

	double m1, m2;

	// double SF=0.0,BM=0.0;
	int val;

	// public static void main(String args[])
	// {
	// BeamBody b1=new BeamBody();
	// b1.init(5.0,2.0,2.0,2,3,10.5,2.0,20.0);
	// }

	public void init(double d, double bre, double dep, int jMain, int iSub,
			double W, double f, double W1) {
		System.out.println("jmain= " + jMain);
		System.out.println("isub== " + iSub);
		length = d * 1000;
		System.out.println("length== " + length);
		SF.clear();
		BM.clear();
		moment.clear();

		switch (jMain) {
		case 1:
			switch (iSub) {
			case 1:
				for (double i = 0; i < length; i = i + 50.0)// = 0:0.05:L
				{
					SF.add(W);
					BM.add(-W * (length - i));
					// System.out.println("SF = " + SF + "BM = " + BM );
				}
				break;
			case 2:
				for (double i = 0; i < length; i = i + 50.0)// = 0:0.05:L
				{
					SF.add(W * (length - i));
					BM.add(-W * (length - i) * (length - i) / 2);
					// System.out.println("SF = " + SF + "BM = " + BM );
				}
				break;
			case 3:
				for (double i = 0; i < length; i = i + 50.0) {
					SF.add(W * (length - i) + W1);
					BM.add(-(W * (length - i) * (length - i) / 2 + W1
							* (length - i)));
				}
				break;
			case 4:
				for (double i = 0; i < length; i = i + 50.0) {
					SF.add(W * (length - i) * (length - i) / (2 * length));
					BM.add(-W * (length - i) * (length - i) * (length - i)
							/ (6 * length));
				}
				break;
			case 5:
				for (double i = 0; i < length; i = i + 50.0) {
					SF.add(W * length / 2 + W1 * (i * i) / (2 * length));
					BM.add(W * length * i / 2 - W * i * i * i / (6 * length)
							- W * length * length / 3); // W*L*i/2 - W*i^3/(6*L)
					// - W*L^2/3;
					// System.out.println("SF = " + SF + "BM = " + BM );
				}
				break;

			default:
				break;
			}
			break;
		case 2:
			switch (iSub) {
			case 1:
				for (double i = 0; i < f; i = i + 50.0) {
					SF.add(W * (length - f) / length);
					BM.add(W * (length - f) * i / length);
					// System.out.println("SF = " + SF + "BM = " + BM );
				}
				for (double i = f; i < length; i = i + 50.0) {
					SF.add(-W * f / length);
					BM.add(W * f * (length - i) / length);
					// System.out.println("SF = " + SF + "BM = " + BM );
				}
				break;
			case 2:
				for (double i = 0; i < length; i = i + 50.0) {
					SF.add(W * length / 2 - W * i);
					BM.add(W * (length - i) * i / 2);
					// System.out.println("SF = " + SF + "BM = " + BM );
				}
				break;
			case 3:
				for (double i = 0; i < length / 2; i = i + 50.0) {
					SF.add(W * length / 4 - W * i * i / length);
					BM.add(W * length * i / 4 - W * i * i * i / (3 * length));

					// System.out.println("SF = " + SF + "BM = " + BM );
				}
				for (double i = length; i > length / 2; i = i - 50.0) {
					SF.add(W * length / 4 - W * i * i / length);
					BM.add(W * length * i / 4 - W * i * i * i / (3 * length));

					// System.out.println("SF = " + SF + "BM = " + BM );
				}

				break;
			case 4:
				for (double i = 0; i < length; i = i + 50.0) {
					SF.add(W * length / 6 - W * i * i / (2 * length));
					BM.add(W * length * i / 6 - W * i * i * i / (6 * length));
					// System.out.println("SF = " + SF + "BM = " + BM );
				}
				break;

			default:
				break;
			}
			break;
		case 3:
			switch (iSub) {
			case 1:
				for (double i = 0; i < f; i = i + 50.0) {
					SF.add(W * (length - f) / length);
					BM.add(W * (length - f) * i / length);
					// System.out.println("SF = " + SF + "BM = " + BM );
				}
				for (double i = f; i < length; i = i + 50.0) {
					SF.add(-W * f / length);
					BM.add(W * f * (length - i) / length);
					// System.out.println("SF = " + SF + "BM = " + BM );
				}
				m1 = W * f * (length - f) * (length - f) / Math.pow(length, 2);
				m2 = W * f * f * (length - f) / Math.pow(length, 2);
				for (double i = 0; i < length; i = i + 50.0) {
					moment.add(m1 + (m2 - m1) / length * i);
				}

				break;
			case 2:
				for (double i = 0; i < length; i = i + 50.0) {
					SF.add(W * length / 2 - W * i);
					BM.add(W * (length - i) * i / 2);
					// System.out.println("SF = " + SF + "BM = " + BM );
				}
				for (double i = 0; i < length; i = i + 50.0) {
					moment.add(W * length * length / 12); // = W*L^2/12;
				}
				break;
			case 3:
				for (double i = 0; i < length / 2; i = i + 50.0) {
					SF.add(W * length / 4 - W * i * i / length);
					BM.add(W * length * i / 4 - W * i * i * i / (3 * length));

					// System.out.println("SF = " + SF + "BM = " + BM );
				}
				for (double i = length; i > length / 2; i = i - 50.0) {
					SF.add(W * length / 4 - W * i * i / length);
					BM.add(W * length * i / 4 - W * i * i * i / (3 * length));

					// System.out.println("SF = " + SF + "BM = " + BM );
				}
				m1 = 5 * W * Math.pow(length, 2) / 96;
				m2 = m1;

				for (double i = 0; i < length; i = i + 50.0) {
					moment.add(m1 + (m2 - m1) / length * i); // = W*L^2/12;
				}

				break;
			case 4:
				for (double i = 0; i < length; i = i + 50.0) {
					SF.add(W * length / 6 - W * i * i / (2 * length));
					BM.add(W * length * i / 6 - W * i * i * i / (6 * length));
					// System.out.println("SF = " + SF + "BM = " + BM );
				}

				m1 = W * Math.pow(length, 2) / 20;
				m2 = W * Math.pow(length, 2) / 30;
				for (double i = 0; i < length; i = i + 50.0) {
					moment.add(m1 + (m2 - m1) / length * i);

				}
				break;

			default:
				break;
			}
			break;
		default:
			break;
		}
		System.out.println("printing size " + BM.size());

	}

	public void update() {
		val += 1;

	}

	public double shearForce() {
		return SF.get(val);
	}

	public double bendMnt() {
		return BM.get(val);
	}

	public double getMoment() {
		return moment.get(val);
	}

	boolean isDataCompleted() {
		int index = val;
		if (index < SF.size()) {
			return false;
		}
		return true;
	}

}
