package eerc.vlab.demos;

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
import javax.vecmath.AxisAngle4d;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
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
public class PrincipalStresses extends javax.swing.JPanel {
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
	TransformGroup rotAxis = new TransformGroup(new Transform3D());
	TransformGroup XrotAxis = new TransformGroup(new Transform3D());
	TransformGroup YrotAxis = new TransformGroup(new Transform3D());
	TransformGroup ZrotAxis = new TransformGroup(new Transform3D());
	
	TransformGroup rotCubeFrame = new TransformGroup(new Transform3D());
	TransformGroup XrotCubeFrame = new TransformGroup(new Transform3D());
	TransformGroup YrotCubeFrame = new TransformGroup(new Transform3D());
	TransformGroup ZrotCubeFrame = new TransformGroup(new Transform3D());
	
	double Rx[][] = new double[3][3];
	double Ry[][] = new double[3][3];
	double Rz[][] = new double[3][3];
	
	private PrincipalStressBody freeBody = null; // Shape3D
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
	private JSlider m_Slider[] = new JSlider[20];
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
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.4,-2.5),new Vector3d(10,10,.01),new Vector3d(0f, 0f,0f), new Color3f(0.9f,0.9f,0.95f)));
		// Walls and roof
		//objRoot.addChild(m_j3d.createBox(new Vector3d(1,0.1f,0), new Vector3d(0.05,0.7f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 0.9f),"resources/images/floor.jpg"));
	//	objRoot.addChild(m_j3d.createBox(new Vector3d(-1,0.1f,0), new Vector3d(0.05,0.7f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 0.9f),"resources/images/floor.jpg"));
	//	objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.1f,-2.0), new Vector3d(1,0.7f,0.05),new Vector3d(0,0,0),new Color3f(1f, 1f, 1.0f)));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.84f,0), new Vector3d(1.05,0.04f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/floor.jpg"));
		

		float rad = (float) Math.PI / 180;
		Transform3D t = new Transform3D();
		t.setScale(0.4);

		TransformGroup tg = new TransformGroup();
		t = new Transform3D();
		t.rotX(rad * 10);
		t.setScale(new Vector3d(.5f, .05f, .5f));
		t.setTranslation(new Vector3d(.3, .3, 0));
		tg.setTransform(t);
		freeBody = new PrincipalStressBody();
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
	public static double []calcRoots(double a,double b,double c)
	{
		double []ans = new double[3];
		double q,r;
		Complex temp1 = new Complex(0, 0);
		Complex temp2 = new Complex(0, 0);
		Complex W = new Complex(0, Math.sqrt(3.0)/2.0);
		
		Complex S1 = new Complex(0, 0);
		Complex S2 = new Complex(0, 0);
		Complex Z1 = new Complex(0, 0);
		Complex Z2 = new Complex(0, 0);
		Complex Z3 = new Complex(0, 0);
		q = (b/3.0) - (a*a)/9.0;
		r = (a*b - 3*c)/6.0 - (a*a*a)/27.0;
		temp1 = new Complex(q*q*q + r*r, 0);
		temp2 = temp1.sqrt().plus(new Complex(r, 0));
		S1 = temp2.cubert();
		temp2 = new Complex(r, 0).minus(temp1.sqrt());
		S2 = temp2.cubert();
		Z1 = S1.plus(S2).minus(new Complex(a/3.0, 0));
		
		temp1 = S1.plus(S2).div(new Complex(-0.5, 0));
		temp2 = W.times(S1.minus(S2));
		
		Z2 = temp1.plus(new Complex(-a/3.0, 0)).plus(temp2);
		Z3 = temp1.plus(new Complex(-a/3.0, 0)).minus(temp2);
		ans[0] = Z1.real();
		ans[1] = Z2.real();
		ans[2] = Z3.real();
		return ans;
	}
	public static double [][]matMult(double A[][], double B[][])
	{
		double C[][] = new double[3][3];
		int i,j,k;
		double sum;
		for(i=0;i<3;i++)
		{
			for(j=0;j<3;j++)
			{
				C[i][j] = 0;
			}
		}
		for(i=0;i<3;i++)
		{
			for(j=0;j<3;j++)
			{
				sum=0.0;
				for(k=0;k<3;k++)
				{
					sum = sum + A[i][k]*B[k][j];
				}
				C[i][j] = sum;
			}
		}		
		return C;
	}
	private void destroy() {
		univ.cleanup();
	}

	/**
	 * @return
	 */
	private Group createVirtualLab() {

		 Transform3D t = new Transform3D();
		 Transform3D tr = new Transform3D();
		 tr.setRotation(new AxisAngle4d(0,1,0,6));
		 
		 Transform3D tc = new Transform3D();
		 tc.setTranslation(new Vector3d(0,-0.2,0));
		 tc.setRotation(new AxisAngle4d(0,1,0,-0.2));
		 
		 
		    TransformGroup objtrans = new TransformGroup(t);
		    objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		    
		    TransformGroup frame1 = new TransformGroup(tr);
			TransformGroup frame2 = new TransformGroup(tc);
			TransformGroup frame3 = new TransformGroup(t);
			
			rotAxis = new TransformGroup(t);
			rotCubeFrame = new TransformGroup(t);
			rotAxis.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    rotAxis.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		    rotCubeFrame.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    rotCubeFrame.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		    
		    XrotAxis = new TransformGroup(t);
			XrotCubeFrame = new TransformGroup(t);
			XrotAxis.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    XrotAxis.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		    XrotCubeFrame.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    XrotCubeFrame.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		    YrotAxis = new TransformGroup(t);
			YrotCubeFrame = new TransformGroup(t);
			YrotAxis.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    YrotAxis.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		    YrotCubeFrame.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    YrotCubeFrame.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		    
		    ZrotAxis = new TransformGroup(t);
			ZrotCubeFrame = new TransformGroup(t);
			ZrotAxis.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    ZrotAxis.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		    ZrotCubeFrame.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    ZrotCubeFrame.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		    
		    
		    frame1.addChild(createCubeFrame(new Vector3d(-0.7,0,0),new Vector3d(0.12,0.12,0.12),new Vector3d(0,0,0), new Color3f(0.0f,0.0f,0.0f)));
		    frame1.addChild(createAxis(new Vector3d(-0.7,0,0.12),new Vector3d(0.05,0.05,0.05),new Vector3d(0,0,0), new Color3f(0.4f,0.4f,0.4f),2));
		    frame1.addChild(createAxis(new Vector3d(-0.7,0.12,0.0),new Vector3d(0.05,0.05,0.05),new Vector3d(0,0,0), new Color3f(0.4f,0.4f,0.4f),2));
		    frame1.addChild(createAxis(new Vector3d(-0.58,0.0,0.0),new Vector3d(0.05,0.05,0.05),new Vector3d(0,0,0), new Color3f(0.4f,0.4f,0.4f),2));
		    frame1.addChild(createAxis(new Vector3d(-0.95,-0.25,0.0),new Vector3d(0.3,0.3,0.3),new Vector3d(0,0,0), new Color3f(0.2f,0.2f,0.2f),2));
		    frame1.addChild(m_j3d.createText2D("Y",new Vector3d(-0.95,0.05,0.0),new Vector3d(0.8,0.8,0.8),new Color3f(0f,0f, 0f),28,Font.BOLD ));
		    frame1.addChild(m_j3d.createText2D("X",new Vector3d(-0.65,-0.28,0.0),new Vector3d(0.8,0.8,0.8),new Color3f(0f,0f, 0f),28,Font.BOLD ));
		    frame1.addChild(m_j3d.createText2D("Z",new Vector3d(-0.95,-0.28,0.35),new Vector3d(0.8,0.8,0.8),new Color3f(0f,0f, 0f),28,Font.BOLD ));
		    
		    frame1.addChild(m_j3d.createText2D("Szz",new Vector3d(-0.7,0,0.25),new Vector3d(0.4,0.4,0.4),new Color3f(1f,0.4f, 0f),18,Font.BOLD ));
		    frame1.addChild(m_j3d.createText2D("Szx",new Vector3d(-0.65,-0.02,0.12),new Vector3d(0.4,0.4,0.4),new Color3f(0.2f,0.8f, 0f),18,Font.BOLD ));
		    frame1.addChild(m_j3d.createText2D("Szy",new Vector3d(-0.68,0.04,0.12),new Vector3d(0.4,0.4,0.4),new Color3f(0.2f,0.2f, 0.8f),18,Font.BOLD ));
		    
		    frame1.addChild(m_j3d.createText2D("Sxz",new Vector3d(-0.58,0,0.15),new Vector3d(0.4,0.4,0.4),new Color3f(1f,0.4f, 0f),18,Font.BOLD ));
		    frame1.addChild(m_j3d.createText2D("Sxx",new Vector3d(-0.45,0.0,0.12),new Vector3d(0.4,0.4,0.4),new Color3f(0.2f,0.8f, 0f),18,Font.BOLD ));
		    frame1.addChild(m_j3d.createText2D("Sxy",new Vector3d(-0.53,0.06,0.12),new Vector3d(0.4,0.4,0.4),new Color3f(0.2f,0.2f, 0.8f),18,Font.BOLD ));
		    
		    frame1.addChild(m_j3d.createText2D("Syz",new Vector3d(-0.7,0.12,0.15),new Vector3d(0.4,0.4,0.4),new Color3f(1f,0.4f, 0f),18,Font.BOLD ));
		    frame1.addChild(m_j3d.createText2D("Syx",new Vector3d(-0.65,0.12,0.0),new Vector3d(0.4,0.4,0.4),new Color3f(0.2f,0.8f, 0f),18,Font.BOLD ));
		    frame1.addChild(m_j3d.createText2D("Syy",new Vector3d(-0.7,0.17,0.0),new Vector3d(0.4,0.4,0.4),new Color3f(0.2f,0.2f, 0.8f),18,Font.BOLD ));
		    		    
		    frame2.addChild(createAxis(new Vector3d(0.0,0.0,0.0),new Vector3d(0.3,0.3,0.3),new Vector3d(0,0,0), new Color3f(0.2f,0.2f,0.2f),2));
		    frame2.addChild(m_j3d.createText2D("Y",new Vector3d(0.0,0.28,0.0),new Vector3d(0.8,0.8,0.8),new Color3f(0f,0f, 0f),28,Font.BOLD ));
		    frame2.addChild(m_j3d.createText2D("X",new Vector3d(0.3,-0.05,0.0),new Vector3d(0.8,0.8,0.8),new Color3f(0f,0f, 0f),28,Font.BOLD ));
		    frame2.addChild(m_j3d.createText2D("Z",new Vector3d(-0.03,-0.03,0.4),new Vector3d(0.8,0.8,0.8),new Color3f(0f,0f, 0f),28,Font.BOLD ));
		    rotAxis.addChild(createAxis(new Vector3d(0.0,0.0,0.0),new Vector3d(0.3,0.3,0.3),new Vector3d(0,0,0), new Color3f(0.6f,0.1f,0.1f),2));
		    rotCubeFrame.addChild(createCubeFrame(new Vector3d(0.2,0.2,0),new Vector3d(0.12,0.12,0.12),new Vector3d(0,0,0), new Color3f(0.0f,0.0f,0.0f)));
		    rotCubeFrame.addChild(createAxis(new Vector3d(0.2,0.2,0.12),new Vector3d(0.05,0.05,0.05),new Vector3d(0,0,0), new Color3f(0.4f,0.4f,0.4f),2));
		    rotCubeFrame.addChild(createAxis(new Vector3d(0.2,0.32,0.0),new Vector3d(0.05,0.05,0.05),new Vector3d(0,0,0), new Color3f(0.4f,0.4f,0.4f),2));
		    rotCubeFrame.addChild(createAxis(new Vector3d(0.32,0.2,0.0),new Vector3d(0.05,0.05,0.05),new Vector3d(0,0,0), new Color3f(0.4f,0.4f,0.4f),2));
		    
		    ZrotAxis.addChild(rotAxis);
		    YrotAxis.addChild(ZrotAxis);
		    XrotAxis.addChild(YrotAxis);
		    		    
		    ZrotCubeFrame.addChild(rotCubeFrame);
		    YrotCubeFrame.addChild(ZrotCubeFrame);
		    XrotCubeFrame.addChild(YrotCubeFrame);
		    
		    frame2.addChild(XrotAxis);
		    frame2.addChild(XrotCubeFrame);
		    
		    
		    frame3.addChild(createAxis(new Vector3d(0.7,-0.2,0.0),new Vector3d(0.3,0.3,0.3),new Vector3d(0,-28,0), new Color3f(0.2f,0.2f,0.2f),2));
		    frame3.addChild(m_j3d.createBox(new Vector3d(0.9,0,0),new Vector3d(0.12,0.12,0.12),new Vector3d(0,-28,0),new Color3f(0.7f,0.7f,0.7f),"resources/images/grey13.jpg"));
		    frame3.addChild(createAxis(new Vector3d(0.9,0.0,0.0),new Vector3d(0.24,0.24,0.24),new Vector3d(0,-28,0), new Color3f(0.2f,0.6f,0.2f),2.5f));
		    frame3.addChild(m_j3d.createText2D("SigmaX",new Vector3d(1.05,-0.07,0.0),new Vector3d(0.5,0.5,0.5),new Color3f(0f,0f, 0f),20,Font.BOLD ));
		    frame3.addChild(m_j3d.createText2D("SigmaY",new Vector3d(0.9,0.25,0.0),new Vector3d(0.5,0.5,0.5),new Color3f(0f,0f, 0f),20,Font.BOLD ));
		    frame3.addChild(m_j3d.createText2D("SigmaZ",new Vector3d(0.75,-0.05,0.2),new Vector3d(0.5,0.5,0.5),new Color3f(0f,0f, 0f),20,Font.BOLD ));
		    frame3.addChild(m_j3d.createText2D("Y",new Vector3d(0.7,0.1,0.0),new Vector3d(0.8,0.8,0.8),new Color3f(0f,0f, 0f),28,Font.BOLD ));
		    frame3.addChild(m_j3d.createText2D("X",new Vector3d(1,-0.28,0.0),new Vector3d(0.8,0.8,0.8),new Color3f(0f,0f, 0f),28,Font.BOLD ));
		    frame3.addChild(m_j3d.createText2D("Z",new Vector3d(0.48,-0.22,0.4),new Vector3d(0.8,0.8,0.8),new Color3f(0f,0f, 0f),28,Font.BOLD ));
		   
		    
		    objtrans.addChild(frame1);
		    objtrans.addChild(frame2);
		    objtrans.addChild(frame3);
		    return objtrans;
		    	
		}
	public Group createCubeFrame(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr){
    	
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
	   	
	    LineAttributes la = new LineAttributes();
	    Appearance appea = new Appearance();
	    
	    la.setLineWidth(2);
	    ColoringAttributes ca = new ColoringAttributes(colr, ColoringAttributes.SHADE_FLAT);
	    appea.setColoringAttributes(ca);
	    appea.setLineAttributes(la);
	    float i;
	    Point3f[] coords = new Point3f[2];
	    
	    // ___________________________________________________________	
	    	
	    // #1
	    	coords[0] = new Point3f(1,1,1);
	    	coords[1] = new Point3f(1,1,-1);
	    	LineArray line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        Shape3D myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	       // #2 
	        coords[0] = new Point3f(1,1,1);
	    	coords[1] = new Point3f(1,-1,1);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        //#3
	        coords[0] = new Point3f(1,1,1);
	    	coords[1] = new Point3f(-1,1,1);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        //#4
	        coords[0] = new Point3f(-1,1,-1);
	    	coords[1] = new Point3f(-1,1,1);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        //#5
	        coords[0] = new Point3f(-1,1,-1);
	    	coords[1] = new Point3f(1,1,-1);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        //#6
	        coords[0] = new Point3f(-1,1,-1);
	    	coords[1] = new Point3f(-1,-1,-1);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        //#7
	        coords[0] = new Point3f(-1,-1,1);
	    	coords[1] = new Point3f(-1,-1,-1);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        //#8
	        coords[0] = new Point3f(-1,-1,1);
	    	coords[1] = new Point3f(-1,1,1);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        //#9
	        coords[0] = new Point3f(-1,-1,1);
	    	coords[1] = new Point3f(1,-1,1);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        //#10
	        coords[0] = new Point3f(1,-1,-1);
	    	coords[1] = new Point3f(1,-1,1);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        //#11
	        coords[0] = new Point3f(1,-1,-1);
	    	coords[1] = new Point3f(1,1,-1);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        //#12
	        coords[0] = new Point3f(1,-1,-1);
	    	coords[1] = new Point3f(-1,-1,-1);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        
	   // _____________________________________________________________
	    
	    
	    return objtrans;
    }
	public Group createAxis(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr,float width){
    	
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
	   	
	    LineAttributes la = new LineAttributes();
	    Appearance appea = new Appearance();
	    
	    la.setLineWidth(width);
	    ColoringAttributes ca = new ColoringAttributes(colr, ColoringAttributes.SHADE_FLAT);
	    appea.setColoringAttributes(ca);
	    appea.setLineAttributes(la);
	    float i;
	    Point3f[] coords = new Point3f[2];
	    
	    // ___________________________________________________________	
	    	
	    // #1
	    	coords[0] = new Point3f(0,0,0);
	    	coords[1] = new Point3f(1,0,0);
	    	LineArray line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        Shape3D myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        coords[0] = new Point3f(1,0,0);
	    	coords[1] = new Point3f(0.9f,0.1f,0);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        coords[0] = new Point3f(1,0,0);
	    	coords[1] = new Point3f(0.9f,-0.1f,0);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	       // #2 
	        coords[0] = new Point3f(0,0,0);
	    	coords[1] = new Point3f(0,1,0);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        coords[0] = new Point3f(0,1,0);
	    	coords[1] = new Point3f(0.1f,0.9f,0);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        coords[0] = new Point3f(-0.1f,0.9f,0);
	    	coords[1] = new Point3f(0,1,0);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        
	        //#3
	        coords[0] = new Point3f(0,0,0);
	    	coords[1] = new Point3f(0,0,1);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        coords[0] = new Point3f(0,0.1f,0.9f);
	    	coords[1] = new Point3f(0,0,1);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        coords[0] = new Point3f(0,-0.1f,0.9f);
	    	coords[1] = new Point3f(0,0,1);
	    	line = new LineArray(2, LineArray.COORDINATES);
	        line.setCoordinates(0, coords);
	        myShape = new Shape3D(line,appea);
	        objtrans.addChild(myShape);
	        
	        
	   // _____________________________________________________________
	    
	    
	    return objtrans;
    }


	/**
	 * Creates new form FreeVibration
	 */
	public PrincipalStresses(Container container) {
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
		PrincipalStresses mainPanel;

		public void init() {
			setLayout(new BorderLayout());
			mainPanel = new PrincipalStresses(this);
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
			setTitle("Principle Stresses");
			getContentPane().add(new PrincipalStresses(this), BorderLayout.CENTER);
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

		
		rotAxis.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		rotCubeFrame.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		
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
		add(topPanel, java.awt.BorderLayout.SOUTH);
		add(simulationPanel, java.awt.BorderLayout.NORTH);
		add(bottomPanel, java.awt.BorderLayout.CENTER);
		//add(rightPanel, java.awt.BorderLayout.EAST);

		startStop = false;
		valChange = true;
		stage = 0;

		timer = new Timer(200, new ActionListener() {
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
		//gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);

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

		guiPanel.setBackground(new Color(219, 226, 238));// Color.BLACK
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

		Color bk = new Color(219, 226, 238);
		 
		 	JLabel lab = new JLabel("THETA x", JLabel.CENTER);
		m_Slider[9] = new JSlider(JSlider.HORIZONTAL, 0, 360, 1);
		m_Slider[9].setMajorTickSpacing(60);
        m_Slider[9].setPaintTicks(true);
        m_Slider[9].setPaintLabels(true);

		m_Slider[9].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[9] = val;

				iLabel[9].setText(":" + fields[9] + " Deg");
				updateSimulationBody();
			}
		});		
		
		m_Slider[9].setBackground(bk);
		guiPanel.add(lab);
		guiPanel.add(m_Slider[9]);
		//in1.add(iLabel[9]);

		lab = new JLabel("     THETA y", JLabel.CENTER);
		m_Slider[10] = new JSlider(JSlider.HORIZONTAL, 0, 360, 1);
		m_Slider[10].setMajorTickSpacing(60);
        m_Slider[10].setPaintTicks(true);
        m_Slider[10].setPaintLabels(true);

		m_Slider[10].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[10] = val;

				iLabel[10].setText(":" + fields[10] + " Deg");
				updateSimulationBody();
			}
		});		
		
		m_Slider[10].setBackground(bk);
		guiPanel.add(lab);
		guiPanel.add(m_Slider[10]);
		//in1.add(iLabel[10]);
		
		lab = new JLabel("     THETA z", JLabel.CENTER);
		m_Slider[11] = new JSlider(JSlider.HORIZONTAL, 0, 360, 1);
		 m_Slider[11].setMajorTickSpacing(60);
         m_Slider[11].setPaintTicks(true);
         m_Slider[11].setPaintLabels(true);
		m_Slider[11].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[11] = val;

				iLabel[11].setText(":" + fields[11] + " Deg");
				updateSimulationBody();
			}
		});		
		
		m_Slider[11].setBackground(bk);
		guiPanel.add(lab);
		guiPanel.add(m_Slider[11]);
		//guiPanel.add(iLabel[11]);
		 
		

	//	guiPanel.add(reStartButton, gridBagConstraints);
	//	guiPanel.add(startButton, gridBagConstraints);
	//	guiPanel.add(nextButton, gridBagConstraints);

		
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

		simulationPanel.setPreferredSize(new java.awt.Dimension(1024, 500));
		simulationPanel.setLayout(new java.awt.BorderLayout());

		javax.swing.JPanel guiPanel = new javax.swing.JPanel();
		guiPanel.setBackground(new Color(100, 100, 100));
		JLabel lbl = new JLabel("Principal Stress Experiment", JLabel.CENTER);
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

		//guiPanel.add(chkbox, gridBagConstraints);
		//guiPanel.add(lbl, gridBagConstraints);

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
		bottomPanel.setVisible(true);
		initInputControlsField();

		Color bk = new Color(219, 226, 238);
		bottomPanel.setLayout(new java.awt.GridLayout());

//	    
		bottomPanel.setBackground(Color.black);
		bottomPanel.setPreferredSize(new java.awt.Dimension(1024, 120));
		bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(235,
				233, 215), 8));

		in1 = new JPanel(new java.awt.GridLayout(4,9));
		in1.setBackground(bk);
	
		JLabel lab = new JLabel("Sxx", JLabel.CENTER);
		m_Slider[0] = new JSlider(JSlider.HORIZONTAL, -50, 50, 0);		
		
		m_Slider[0].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[0] = val;
				updateSimulationBody();
				iLabel[0].setText(fields[0]  + " ");

				// univ.getCanvas().repaint();
				//repaint();
			}
		});
		
		m_Slider[0].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[0]);
		in1.add(iLabel[0]);
		
		lab = new JLabel("Sxy", JLabel.CENTER);
		m_Slider[1] = new JSlider(JSlider.HORIZONTAL, -50, 50, 0);
		
		m_Slider[1].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[1] = val;
				updateSimulationBody();
				iLabel[1].setText(fields[1] + " ");
                 m_Slider[3].setValue(val);
				// univ.getCanvas().repaint();
				repaint();
			}
		});
		m_Slider[1].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[1]);
		in1.add(iLabel[1]);

		lab = new JLabel("Sxz", JLabel.CENTER);
		m_Slider[2] = new JSlider(JSlider.HORIZONTAL, -50, 50, 0);
		
		m_Slider[2].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[2] = val;
				updateSimulationBody();
				iLabel[2].setText(fields[2] + " ");
				  m_Slider[6].setValue(val);
				// univ.getCanvas().repaint();
				repaint();
			}
		});		
		
		m_Slider[2].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[2]);
		in1.add(iLabel[2]);
		
		lab = new JLabel("Syx", JLabel.CENTER);
		m_Slider[3] = new JSlider(JSlider.HORIZONTAL, -50, 50, 0);
		
		m_Slider[3].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[3] = val;
				updateSimulationBody();
				iLabel[3].setText(fields[3] + " ");
				  m_Slider[1].setValue(val);
				// univ.getCanvas().repaint();
				repaint();
			}
		});		
		
		m_Slider[3].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[3]);
		in1.add(iLabel[3]);
		
		lab = new JLabel("Syy", JLabel.CENTER);
		m_Slider[4] = new JSlider(JSlider.HORIZONTAL, -50, 50, 0);
		
		m_Slider[4].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[4] = val;
				updateSimulationBody();
				iLabel[4].setText(fields[4] + " ");

				// univ.getCanvas().repaint();
				repaint();
			}
		});		
		
		m_Slider[4].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[4]);
		in1.add(iLabel[4]);

		lab = new JLabel("Syz", JLabel.CENTER);
		m_Slider[5] = new JSlider(JSlider.HORIZONTAL, -50, 50, 0);
		
		m_Slider[5].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[5] = val;
				updateSimulationBody();
				iLabel[5].setText(fields[5] + " ");

				// univ.getCanvas().repaint();
				  m_Slider[7].setValue(val);
				repaint();
			}
		});		
		
		m_Slider[5].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[5]);
		in1.add(iLabel[5]);

		lab = new JLabel("Szx", JLabel.CENTER);
		m_Slider[6] = new JSlider(JSlider.HORIZONTAL, -50, 50, 0);
		
		m_Slider[6].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[6] = val;
				updateSimulationBody();
				iLabel[6].setText(fields[6] + " ");

				// univ.getCanvas().repaint();
				  m_Slider[2].setValue(val);
				repaint();
			}
		});		
		
		m_Slider[6].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[6]);
		in1.add(iLabel[6]);

		lab = new JLabel("Szy", JLabel.CENTER);
		m_Slider[7] = new JSlider(JSlider.HORIZONTAL, -50, 50, 0);
		
		m_Slider[7].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[7] = val;
				updateSimulationBody();
				iLabel[7].setText(fields[7] + " ");

				// univ.getCanvas().repaint();
				  m_Slider[5].setValue(val);
				repaint();
			}
		});		
		
		m_Slider[7].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[7]);
		in1.add(iLabel[7]);
		
		lab = new JLabel("Szz", JLabel.CENTER);
		m_Slider[8] = new JSlider(JSlider.HORIZONTAL, -50, 50, 0);
		
		m_Slider[8].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[8] = val;
				updateSimulationBody();
				iLabel[8].setText(fields[8] + " ");

				// univ.getCanvas().repaint();
				repaint();
			}
		});		
		
		m_Slider[8].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[8]);
		in1.add(iLabel[8]);
		
		lab = new JLabel("THETA x", JLabel.CENTER);
		m_Slider[9] = new JSlider(JSlider.HORIZONTAL, 0, 360, 36);
		
		m_Slider[9].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[9] = val;

				iLabel[9].setText(" = " + fields[9] + " Deg");
				updateSimulationBody();
			}
		});		
		
		m_Slider[9].setBackground(bk);
		in1.add(lab);
		//in1.add(m_Slider[9]);
		in1.add(iLabel[9]);

		lab = new JLabel("THETA y", JLabel.CENTER);
		m_Slider[10] = new JSlider(JSlider.HORIZONTAL, 0, 360, 36);
		
		m_Slider[10].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[10] = val;

				iLabel[10].setText(" = " + fields[10] + " Deg");
				updateSimulationBody();
			}
		});		
		
		m_Slider[10].setBackground(bk);
		in1.add(lab);
		//in1.add(m_Slider[10]);
		in1.add(iLabel[10]);
		
		lab = new JLabel("THETA z", JLabel.CENTER);
		m_Slider[11] = new JSlider(JSlider.HORIZONTAL, 0, 360, 36);
		
		m_Slider[11].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[11] = val;

				iLabel[11].setText(" = " + fields[11] + " Deg");
				updateSimulationBody();
			}
		});		
		
		m_Slider[11].setBackground(bk);
		in1.add(lab);
		//in1.add(m_Slider[11]);
		in1.add(iLabel[11]);

		in2 = new JPanel(new java.awt.GridLayout(4,6));
		in2.setBackground(bk);

		lab = new JLabel("S'xx", JLabel.CENTER);		
		in2.add(lab);		
		in2.add(iLabel[12]);
		
		lab = new JLabel("S'xy", JLabel.CENTER);		
		in2.add(lab);		
		in2.add(iLabel[13]);
		lab = new JLabel("S'xz", JLabel.CENTER);		
		in2.add(lab);		
		in2.add(iLabel[14]);
		lab = new JLabel("S'yx", JLabel.CENTER);		
		in2.add(lab);		
		in2.add(iLabel[15]);
		lab = new JLabel("S'yy", JLabel.CENTER);		
		in2.add(lab);		
		in2.add(iLabel[16]);
		lab = new JLabel("S'yz", JLabel.CENTER);		
		in2.add(lab);		
		in2.add(iLabel[17]);
		lab = new JLabel("S'zx", JLabel.CENTER);		
		in2.add(lab);		
		in2.add(iLabel[18]);
		lab = new JLabel("S'zy", JLabel.CENTER);		
		in2.add(lab);		
		in2.add(iLabel[19]);
		lab = new JLabel("S'zz", JLabel.CENTER);		
		in2.add(lab);		
		in2.add(iLabel[20]);
		lab = new JLabel("Value 1:", JLabel.CENTER);		
		in2.add(lab);		
		in2.add(iLabel[21]);
		lab = new JLabel("Value 2:", JLabel.CENTER);		
		in2.add(lab);		
		in2.add(iLabel[22]);
		lab = new JLabel("Value 3:", JLabel.CENTER);		
		in2.add(lab);		
		in2.add(iLabel[23]);
		
				// ------  #############  -----------------  ######  --------------
		bottomPanel.add(in1);
		bottomPanel.add(in2);
	//	bottomPanel.add(in3);
		bottomPanel.setVisible(true);
	}

	private void initInputControlsField() {

		iLabel = new JLabel[25];
		int i = 0,j = 0;
		for(i=0;i<25;i++)
		{
			iLabel[i] = new JLabel("", JLabel.LEFT);
			iLabel[i].setForeground(Color.blue);
		}
		i = 0;
		fields = new double[20];
		for(i=0;i<20;i++)
		{
			fields[i] = 0.0;
		}
		for(i=0;i<3;i++)
		{
			for(j=0;j<3;j++)
			{
				Rx[i][j] = 0.0;
				Ry[i][j] = 0.0;
				Rz[i][j] = 0.0;
			}
		}
		
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
		//	rotAxis.setTransform(t1);
		}
		updateSimulationBody();
		
		return;
	}

	private void updateSimulationBody(){

		double tx,ty,tz;
		int i,j;
		double [][]R = new double[3][3];
		double [][]Rt = new double[3][3];
		double [][]result = new double[3][3];
		double [][]Sigma = new double[3][3];
		
		
		tx = Math.PI*fields[9]/180;
		ty = Math.PI*fields[10]/180;
		tz = Math.PI*fields[11]/180;
		Transform3D t = new Transform3D();
		t.rotX(tx);
		XrotAxis.setTransform(t);
		XrotCubeFrame.setTransform(t);
		t.rotY(ty);
		YrotAxis.setTransform(t);
		YrotCubeFrame.setTransform(t);
		t.rotZ(tz);
		ZrotAxis.setTransform(t);
		ZrotCubeFrame.setTransform(t);
		
		Rx[0][0] = 1;
		Rx[0][1] = 0;
		Rx[0][2] = 0;
		Rx[1][0] = 0;
		Rx[1][1] = Math.cos(tx);
		Rx[1][2] = -Math.sin(tx);
		Rx[2][0] = 0;
		Rx[2][1] = Math.sin(tx);
		Rx[2][2] = Math.cos(tx);
		
		Ry[0][0] = Math.cos(ty);
		Ry[0][1] = 0;
		Ry[0][2] = Math.sin(ty);
		Ry[1][0] = 0;
		Ry[1][1] = 1;
		Ry[1][2] = 0;
		Ry[2][0] = -Math.sin(ty);
		Ry[2][1] = 0;
		Ry[2][2] = Math.cos(ty);
		
		Rz[0][0] = Math.cos(tz);
		Rz[0][1] = -Math.sin(tz);
		Rz[0][2] = 0;
		Rz[1][0] = Math.sin(tz);
		Rz[1][1] = Math.cos(tz);
		Rz[1][2] = 0;
		Rz[2][0] = 0;
		Rz[2][1] = 0;
		Rz[2][2] = 1;
		
		Sigma[0][0] = fields[0];
		Sigma[0][1] = fields[1];
		Sigma[0][2] = fields[2];
		Sigma[1][0] = fields[3];
		Sigma[1][1] = fields[4];
		Sigma[1][2] = fields[5];
		Sigma[2][0] = fields[6];
		Sigma[2][1] = fields[7];
		Sigma[2][2] = fields[8];
		
		R = matMult(matMult(Rx, Ry),Rz);
		for(i=0;i<3;i++)
		{
			for(j=0;j<3;j++)
			{
				Rt[j][i] = R[i][j];
			}
		}
		result = matMult(matMult(R, Sigma), Rt);
		iLabel[12].setText((float)result[0][0] + " ");
		iLabel[13].setText((float)result[0][1] + " ");
		iLabel[14].setText((float)result[0][2] + " ");
		iLabel[15].setText((float)result[1][0] + " ");
		iLabel[16].setText((float)result[1][1] + " ");
		iLabel[17].setText((float)result[1][2] + " ");
		iLabel[18].setText((float)result[2][0] + " ");
		iLabel[19].setText((float)result[2][1] + " ");
		iLabel[20].setText((float)result[2][2] + " ");
		
		double I1,I2,I3,coff3,coff2,term1,term2;
		double []roots = new double[3];
		I1 = Sigma[0][0] + Sigma[1][1] + Sigma[2][2];
		I2 = Sigma[0][0]*Sigma[1][1] + Sigma[1][1]*Sigma[2][2] + Sigma[0][0]*Sigma[2][2] - Sigma[0][1]*Sigma[0][1] - Sigma[1][2]*Sigma[1][2] - Sigma[2][0]*Sigma[2][0];
		I3 = Sigma[0][0]*Sigma[1][1]*Sigma[2][2] + 2*Sigma[0][1]*Sigma[1][2]*Sigma[2][0] - Sigma[0][0]*Sigma[1][2]*Sigma[1][2] - Sigma[1][1]*Sigma[0][2]*Sigma[0][2] - Sigma[2][2]*Sigma[0][1]*Sigma[0][1];
		System.out.println("I1 = " + I1);
		System.out.println("I2 = " + I2);
		System.out.println("I3 = " + I3);
		
		roots = calcRoots(-I1, I2, -I3);
		iLabel[21].setText((float)roots[0] + " ");
		iLabel[22].setText((float)roots[1] + " ");
		iLabel[23].setText((float)roots[2] + " ");
		
		
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

class PrincipalStressBody {
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
		if (Re > 4000) {
			return "Turbulent Flow";
		}
		if (Re >= 2000 && Re <= 4000) {
			return "Transient Flow";
		}
		return "Flow calculating";
	}
}
class Complex extends Object {

    private double x,y;
    
    /**
        Constructs the complex number z = u + i*v
        @param u Real part
        @param v Imaginary part
    */
    public Complex(double u,double v) {
        x=u;
        y=v;
    }
    
    /**
        Real part of this Complex number 
        (the x-coordinate in rectangular coordinates).
        @return Re[z] where z is this Complex number.
    */
    public double real() {
        return x;
    }
    
    /**
        Imaginary part of this Complex number 
        (the y-coordinate in rectangular coordinates).
        @return Im[z] where z is this Complex number.
    */
    public double imag() {
        return y;
    }
    
    /**
        Modulus of this Complex number
        (the distance from the origin in polar coordinates).
        @return |z| where z is this Complex number.
    */
    public double mod() {
        if (x!=0 || y!=0) {
            return Math.sqrt(x*x+y*y);
        } else {
            return 0d;
        }
    }
    
    /**
        Argument of this Complex number 
        (the angle in radians with the x-axis in polar coordinates).
        @return arg(z) where z is this Complex number.
    */
    public double arg() {
        return Math.atan2(y,x);
       /* if (a >= 0)
        {
        	return a;
        }
        else
        {
        	return 2*Math.PI + a;
        } */
    }
    
    /**
        Complex conjugate of this Complex number
        (the conjugate of x+i*y is x-i*y).
        @return z-bar where z is this Complex number.
    */
    public Complex conj() {
        return new Complex(x,-y);
    }
    
    /**
        Addition of Complex numbers (doesn't change this Complex number).
        <br>(x+i*y) + (s+i*t) = (x+s)+i*(y+t).
        @param w is the number to add.
        @return z+w where z is this Complex number.
    */
    public Complex plus(Complex w) {
        return new Complex(x+w.real(),y+w.imag());
    }
    
    /**
        Subtraction of Complex numbers (doesn't change this Complex number).
        <br>(x+i*y) - (s+i*t) = (x-s)+i*(y-t).
        @param w is the number to subtract.
        @return z-w where z is this Complex number.
    */
    public Complex minus(Complex w) {
        return new Complex(x-w.real(),y-w.imag());
    }
    
    /**
        Complex multiplication (doesn't change this Complex number).
        @param w is the number to multiply by.
        @return z*w where z is this Complex number.
    */
    public Complex times(Complex w) {
        return new Complex(x*w.real()-y*w.imag(),x*w.imag()+y*w.real());
    }
    
    /**
        Division of Complex numbers (doesn't change this Complex number).
        <br>(x+i*y)/(s+i*t) = ((x*s+y*t) + i*(y*s-y*t)) / (s^2+t^2)
        @param w is the number to divide by
        @return new Complex number z/w where z is this Complex number  
    */
    public Complex div(Complex w) {
        double den=Math.pow(w.mod(),2);
        return new Complex((x*w.real()+y*w.imag())/den,(y*w.real()-x*w.imag())/den);
    }
    
    /**
        Complex exponential (doesn't change this Complex number).
        @return exp(z) where z is this Complex number.
    */
    public Complex exp() {
        return new Complex(Math.exp(x)*Math.cos(y),Math.exp(x)*Math.sin(y));
    }
    
    /**
        Principal branch of the Complex logarithm of this Complex number.
        (doesn't change this Complex number).
        The principal branch is the branch with -pi < arg <= pi.
        @return log(z) where z is this Complex number.
    */
    public Complex log() {
        return new Complex(Math.log(this.mod()),this.arg());
    }
    public Complex cube() {
        double r=Math.pow(this.mod(),3);
        double theta=this.arg()*3;
        return new Complex(r*Math.cos(theta),r*Math.sin(theta));
    }
    /**
        Complex square root (doesn't change this complex number).
        Computes the principal branch of the square root, which 
        is the value with 0 <= arg < pi.
        @return sqrt(z) where z is this Complex number.
    */
    public Complex sqrt() {
        double r=Math.sqrt(this.mod());
        double theta=this.arg()/2;
        return new Complex(r*Math.cos(theta),r*Math.sin(theta));
    }
    
    public Complex cubert() {
        double r=Math.pow(this.mod(),1.0/3.0);
        double theta=this.arg();
        return new Complex(r*Math.cos(theta),r*Math.sin(theta));
    }
    
    // Real cosh function (used to compute complex trig functions)
    private double cosh(double theta) {
        return (Math.exp(theta)+Math.exp(-theta))/2;
    }
    
    // Real sinh function (used to compute complex trig functions)
    private double sinh(double theta) {
        return (Math.exp(theta)-Math.exp(-theta))/2;
    }
    
    /**
        Sine of this Complex number (doesn't change this Complex number).
        <br>sin(z) = (exp(i*z)-exp(-i*z))/(2*i).
        @return sin(z) where z is this Complex number.
    */
    public Complex sin() {
        return new Complex(cosh(y)*Math.sin(x),sinh(y)*Math.cos(x));
    }
    
    /**
        Cosine of this Complex number (doesn't change this Complex number).
        <br>cos(z) = (exp(i*z)+exp(-i*z))/ 2.
        @return cos(z) where z is this Complex number.
    */
    public Complex cos() {
        return new Complex(cosh(y)*Math.cos(x),-sinh(y)*Math.sin(x));
    }
    
    /**
        Hyperbolic sine of this Complex number 
        (doesn't change this Complex number).
        <br>sinh(z) = (exp(z)-exp(-z))/2.
        @return sinh(z) where z is this Complex number.
    */
    public Complex sinh() {
        return new Complex(sinh(x)*Math.cos(y),cosh(x)*Math.sin(y));
    }
    
    /**
        Hyperbolic cosine of this Complex number 
        (doesn't change this Complex number).
        <br>cosh(z) = (exp(z) + exp(-z)) / 2.
        @return cosh(z) where z is this Complex number.
    */
    public Complex cosh() {
        return new Complex(cosh(x)*Math.cos(y),sinh(x)*Math.sin(y));
    }
    
    /**
        Tangent of this Complex number (doesn't change this Complex number).
        <br>tan(z) = sin(z)/cos(z).
        @return tan(z) where z is this Complex number.
    */
    public Complex tan() {
        return (this.sin()).div(this.cos());
    }
    
    /**
        Negative of this complex number (chs stands for change sign). 
        This produces a new Complex number and doesn't change 
        this Complex number.
        <br>-(x+i*y) = -x-i*y.
        @return -z where z is this Complex number.
    */
    public Complex chs() {
        return new Complex(-x,-y);
    }
    
    /**
        String representation of this Complex number.
        @return x+i*y, x-i*y, x, or i*y as appropriate.
    */
    public String toString() {
        if (x!=0 && y>0) {
            return x+" + "+y+"i";
        }
        if (x!=0 && y<0) {
            return x+" - "+(-y)+"i";
        }
        if (y==0) {
            return String.valueOf(x);
        }
        if (x==0) {
            return y+"i";
        }
        // shouldn't get here (unless Inf or NaN)
        return x+" + i*"+y;
        
    }       
}

