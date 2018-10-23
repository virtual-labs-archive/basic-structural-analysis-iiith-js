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
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
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

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import eerc.vlab.common.CommonGraph;
import eerc.vlab.common.FullViewGraph;
import eerc.vlab.common.HelpWindow;
import eerc.vlab.common.HorizontalGraph;
import eerc.vlab.common.HorizontalGraph1;
import eerc.vlab.common.J3DShape;
import eerc.vlab.common.Resources;

@SuppressWarnings( { "serial", "unused" })
public class Beams_Defelection2 extends javax.swing.JPanel {
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
	//private javax.swing.JButton reStartButton = null;
	//private javax.swing.JButton nextButton = null;

	
	String safty_factor = "1", materialGrade;
	int iSubVal = 1;
	private javax.swing.JButton rightIcon = null;

	// private GraphPlotter graphPlotter;
	// //////////////////////////Java3D componenet ///////////////////////////

	private SimpleUniverse univ = null; // Simple Universe Java3D
	private BranchGroup scene = null; // BranchGroup Scene graph
	TransformGroup beam = new TransformGroup(new Transform3D());
	TransformGroup support1 = new TransformGroup(new Transform3D());
	TransformGroup support2 = new TransformGroup(new Transform3D());
	private Switch objSwitch = new Switch();

	private BeamBody freeBody = null; // Shape3D
	private CommonGraph ShearGraph = null;
	private CommonGraph MomentGraph = null;
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

	private double stage = 0;
	JComboBox cementGradeList;

	private boolean startStop = false;
	private boolean valChange = true;

	private JComboBox ch;
	private JComboBox che;
	private JLabel lbl_k;
	private JSlider m_Slider[] = new JSlider[7];
	private JLabel out_lbl[] = new JLabel[3];
	String boundary = "Fixed";
	String[] cement = new String[5];
	String[] cement1 = new String[3];

	private String loading = "Point Load";// ,BOS;

	int flag = 0, val = 20;
	JLabel len;

	private JComboBox End_Conditions, Column_Mat, Material_Grade, Fac_Of_Safty;
	private TransformGroup arrow;
	private TransformGroup beam1;
	private TransformGroup[] cubes;
	private TransformGroup Ubeam1;
	private TransformGroup ptPos1;
	private TransformGroup UDLPos1;
	private TransformGroup[] arrows_udl;
	private int flag_panel=1;
	private ImageIcon icon;
	private Dimension dimension;

	public BranchGroup createSceneGraph() {
		BranchGroup objRoot = new BranchGroup();
		objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		objRoot.setCapability(Group.ALLOW_CHILDREN_READ);
		objRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
		objRoot.setCapability(BranchGroup.ALLOW_DETACH);

		objRoot.addChild(createVirtualLab());

		int i,j;
		for(i=-2;i<=2;i++)
		{
			for(j=-2;j<=2;j++)
			{
				objRoot.addChild(m_j3d.createBox(new Vector3d((float)(i),-0.6,(float)(j)),new Vector3d(0.5,.01,0.5),new Vector3d(0,0,0),new Color3f(.8f, .8f, .8f),"resources/images/tile.jpg"));
			}
		}
		//	objRoot.addChild(m_j3d.createBox(new Vector3d(0,-0.6, -.1),new Vector3d(1.5,.01,1.5),new Vector3d(0,0,0),new Color3f(.8f, .8f, .8f),"resources/images/tile.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.4,-2.5),new Vector3d(10,10,.01),new Vector3d(0f, 0f,0f), new Color3f(0.5f,0.6f,0.72f)));
		// Walls and roof
		objRoot.addChild(m_j3d.createBox(new Vector3d(1,0.1f,0), new Vector3d(0.05,0.7f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 0.9f),"resources/images/floor.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(-1,0.1f,0), new Vector3d(0.05,0.7f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 0.9f),"resources/images/floor.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.1f,-2.0), new Vector3d(1,0.7f,0.05),new Vector3d(0,0,0),new Color3f(1f, 1f, 1.0f)));
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

	private void destroy() 
	{
		univ.cleanup();
	}

	private Group createVirtualLab() 
	{
		Transform3D t = new Transform3D();
		TransformGroup objtrans = new TransformGroup(t);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		beam1 = new TransformGroup(t);
		ptPos1 = new TransformGroup(t);

		objSwitch = new Switch(Switch.CHILD_MASK);
		objSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);

		//objtrans.addChild(m_j3d.createText2D("FIXED",new Vector3d(-0.35,0.1, 0.2),new Vector3d(0.6,0.6,0.6),new Color3f(0.1f,0.3f, 0.6f),16,Font.BOLD ));
		//objtrans.addChild(m_j3d.createText2D("FREE",new Vector3d(0.2,0.1, 0.2),new Vector3d(0.6,0.6,0.6),new Color3f(0.1f,0.3f, 0.6f),16,Font.BOLD ));
		support1.addChild(m_j3d.createBox(new Vector3d(-0.22,0,0), new Vector3d(0.05,0.3f,0.2),new Vector3d(0,0,0),new Color3f(1f, 1f, 0.9f),"resources/images/grey13.jpg"));
		support2.addChild(m_j3d.createBox(new Vector3d(0.22,0,0), new Vector3d(0.05,0.3f,0.2),new Vector3d(0,0,0),new Color3f(1f, 1f, 0.9f),"resources/images/grey13.jpg"));

		beam1.addChild(createBeam(new Vector3d(0,0,0),new Vector3d(1,1,1),new Vector3d(0,0,0),0,0.44));

		arrow = createArrow(new Vector3d(0.0,0.1,0),new Vector3d(0.02,0.02,1), new Vector3d(0,0,180),new Color3f(0.1f,0.2f,0.3f));
		arrow.setCapability(Group.ALLOW_CHILDREN_READ);
		arrow.setCapability(Group.ALLOW_CHILDREN_WRITE);

		arrows_udl = new TransformGroup[5];
		for(int i=0;i<5;i++)
		{
			arrows_udl[i] = createArrow(new Vector3d(3.0,0.1,0),new Vector3d(0.02,0.02,1), new Vector3d(0,0,180),new Color3f(0.1f,0.2f,0.3f));
			ptPos1.addChild(arrows_udl[i]);
		}

		ptPos1.addChild(beam1);
		ptPos1.addChild(arrow);

		objSwitch.addChild(ptPos1);
		beam.addChild(objSwitch);

		objtrans.addChild(support1);
		objtrans.addChild(support2);
		objtrans.addChild(beam);

		//making the beam visible.
		java.util.BitSet mask1 = new java.util.BitSet(objSwitch.numChildren());        
		objSwitch.setChildMask(mask1);
		java.util.BitSet visibleNodes1 = new java.util.BitSet( objSwitch.numChildren() );
		visibleNodes1.set(0);
		objSwitch.setChildMask(visibleNodes1);
		return objtrans;
	}

	public Group createBeam(Vector3d pos,Vector3d scale,Vector3d rot, double extent, double length)
	{
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

		cubes = new TransformGroup[880];       
		TransformGroup objtrans = new TransformGroup(t);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		double X;
		int j=0;
		for(X = -length/2; X <= length/2; X += 0.0005,j++)
		{
			cubes[j] = new TransformGroup();
			cubes[j].setCapability(Group.ALLOW_CHILDREN_READ);
			cubes[j].setCapability(Group.ALLOW_CHILDREN_WRITE);
			cubes[j] = createBox(new Vector3d(X,0,0), new Vector3d(0.02,0.045,0.045),new Vector3d(0,0,Math.atan(extent*2*X)*180.0/Math.PI), new Color3f(1,1,1),"resources/images/floor.jpg");
			objtrans.addChild(cubes[j]);
		}
		//System.out.println(j+"");
		return objtrans;
	}
	public TransformGroup createBox(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr,String texfile) {
		// Create a transform group node to scale and position the object.
		//new Point3d(0.0, 0.0, 0.0)
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

		// Create a simple shape leaf node and add it to the scene graph
		//Shape3D shape = new Box(1.0, 1.0, 1.0);       

		// Create a new ColoringAttributes object for the shape's
		// appearance and make it writable at runtime.
		Appearance app = new Appearance();
		ColoringAttributes ca = new ColoringAttributes();
		ca.setColor(colr);
		//app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		app.setColoringAttributes(ca);

		if(texfile!=null)
		{
			Texture tex = new TextureLoader(Resources.getResource(texfile),TextureLoader.BY_REFERENCE | TextureLoader.Y_UP,this).getTexture();
			app.setTexture(tex);
			TextureAttributes texAttr = new TextureAttributes();
			texAttr.setTextureMode(TextureAttributes.MODULATE);
			app.setTextureAttributes(texAttr);
		}
		objtrans.addChild(new Box(1.0f, 1.0f, 1.0f,Box.GENERATE_TEXTURE_COORDS | Box.GENERATE_TEXTURE_COORDS_Y_UP, app));
		return objtrans;
	}

	public TransformGroup createArrow(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr){
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
	public Beams_Defelection2(Container container) {
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
		Beams_Defelection2 mainPanel;

		public void init() {
			setLayout(new BorderLayout());
			mainPanel = new Beams_Defelection2(this);
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
			getContentPane().add(new Beams_Defelection2(this),
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
		support1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		support2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

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
		//s=0;
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
		//reStartButton = new javax.swing.JButton("Re-Start");
		//ImageIcon icon = m_j3d.createImageIcon("resources/icons/restart.png");
		//reStartButton.setIcon(icon);
		startButton = new javax.swing.JButton("Start");
		ImageIcon icon = m_j3d.createImageIcon("resources/icons/start.png");
		startButton.setIcon(icon);
		/*nextButton = new javax.swing.JButton("Next");
		icon = m_j3d.createImageIcon("resources/icons/next.png");
		nextButton.setIcon(icon);*/

		//reStartButton.setEnabled(false);
		//nextButton.setEnabled(false);

		guiPanel.setBackground(new Color(67, 143, 205));// Color.BLACK
		topPanel.setLayout(new java.awt.BorderLayout());
		topPanel.add(guiPanel, java.awt.BorderLayout.NORTH);

		startButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// Toggle
				startStop = !startStop;

				if (startStop)
				{
					startSimulation(evt);
				}
				else
					pauseSimulation();
				univ.getCanvas().repaint();
			}
		});

		/*reStartButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				reStartButton.setEnabled(false);
				// startButton.setEnabled(true);
				startButton.setText("Start");
				startStop = !startStop;
				//resetOutputParameters();
				// startStop = false;
				//                
				// ShearGraph.clearGraphValue();
				// MomentGraph.clearGraphValue();
				//                
				valChange = true;
				startSimulation(evt);
				univ.getCanvas().repaint();

			}
		});*/

		/*nextButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				s++;
				nextButton.setEnabled(false);
				//resetOutputParameters();
				onNextStage();
				univ.getCanvas().repaint();
			}
		});*/

		//		guiPanel.add(reStartButton, gridBagConstraints);
		guiPanel.add(startButton, gridBagConstraints);
		//		guiPanel.add(nextButton, gridBagConstraints);

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

		rightPanel.setLayout(new java.awt.GridLayout(3, 1, 0, 1));
		rightPanel.setPreferredSize(new java.awt.Dimension(300, 600));
		rightPanel.setBorder(BorderFactory.createLineBorder(new Color(140, 200, 240), 8));
		JPanel panel = new JPanel();
		panel.setBackground(new Color(140, 200, 240));

		panel.setBorder(BorderFactory.createLineBorder(
				new Color(132, 132, 255), 4));
		panel.setBorder(new EmptyBorder(10, 10, 0, 0));

		rightIcon = new javax.swing.JButton(" ");
		rightIcon.setBackground(new Color(1.0f,1.0f,1.0f));
		//dimension = new Dimension(200,120);
		//rightIcon.setPreferredSize(dimension);
		icon = m_j3d.createImageIcon("resources/Beams/Fixed_point.PNG");
		rightIcon.setIcon(icon);
		panel.add(rightIcon);
		rightPanel.add(panel);


		ShearGraph = new CommonGraph(270,150, "x", "V");
		ShearGraph.setHeading("Shear");
		ShearGraph.setAxisUnit("dm", "V");
		ShearGraph.setYAxisColor(new Color(0.0f, 0.54f, 0.27f));// Color.
		// DARK_GRAY
		// );
		ShearGraph.setYScale(0.5f);
		//ShearGraph.setXscale(25f);
		ShearGraph.fitToYwindow(true);
		rightPanel.add(ShearGraph);

		MomentGraph = new CommonGraph(270, 150, "x", "M");
		MomentGraph.setHeading("Moment");
		MomentGraph.setAxisUnit("dm", "M");
		MomentGraph.setYAxisColor(new Color(0.0f, 0.54f, 0.27f));//Color.DARK_GRAY
		// );
		MomentGraph.setYScale(1);
		//MomentGraph.setXscale(25f);
		MomentGraph.fitToYwindow(true);
		rightPanel.add(MomentGraph);
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
		JLabel lbl = new JLabel("Analysis of Beams", JLabel.CENTER);
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
				{
					bottomPanel.setVisible(true);
					enableStage(1);
					//resetOutputParameters();
				}
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

	private void bottomPanel() 
	{

		initInputControlsField();

		Color bk = new Color(219, 226, 238);
		bottomPanel.setLayout(new java.awt.GridLayout(1, 3));
		bottomPanel.setBackground(Color.black);
		//bottomPanel.setPreferredSize(new java.awt.Dimension(1024, 120));
		bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(235,233, 215), 8));

		in1 = new JPanel(new java.awt.GridLayout(3, 3, 10, 10));
		in1.setBackground(bk);
		bottomPanel.add(in1);

		in2 = new JPanel(new java.awt.GridLayout(3, 3, 10, 10));
		in2.setBackground(bk);
		bottomPanel.add(in2);

		in3 = new JPanel(new java.awt.GridLayout(3, 3, 10, 10));
		in3.setBackground(bk);
		bottomPanel.add(in3);

		JLabel lab,lab1;
		JLabel dot1,dot2;

		m_Slider[6] = new JSlider(JSlider.HORIZONTAL,0,(int) fields[0],1);

		lab = new JLabel("Length(dm)", JLabel.RIGHT);
		m_Slider[0] = new JSlider(JSlider.HORIZONTAL, 10,40,25);
		m_Slider[0].setMajorTickSpacing(30);
		m_Slider[0].setMinorTickSpacing(10);
		m_Slider[0].setPaintTicks(true);
		m_Slider[0].setPaintLabels(true);
		m_Slider[0].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[0] = val;
				m_Slider[6].setMaximum((int) fields[0]);  
				iLabel[0].setText(":: " + fields[0] + "dm");
				// getEL();
				repaint();
				// univ.getCanvas().repaint();

			}
		});
		m_Slider[0].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[0]);
		in1.add(iLabel[0]);

		lab = new JLabel("Breadth(mm)", JLabel.RIGHT);
		m_Slider[1] = new JSlider(JSlider.HORIZONTAL,100,500,300);
		m_Slider[1].setMajorTickSpacing(400);
		m_Slider[1].setMinorTickSpacing(100);
		m_Slider[1].setPaintTicks(true);
		m_Slider[1].setPaintLabels(true);
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

		lab = new JLabel("Depth(mm)", JLabel.RIGHT);
		m_Slider[2] = new JSlider(JSlider.HORIZONTAL,100,500,300);
		m_Slider[2].setMajorTickSpacing(400);
		m_Slider[2].setMinorTickSpacing(100);
		m_Slider[2].setPaintTicks(true);
		m_Slider[2].setPaintLabels(true);
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

		lab = new JLabel("Boundary", JLabel.CENTER);
		JComboBox Boundary_type = new JComboBox();
		// Column_material.addItem("-");
		//boundary;
		Boundary_type.addItem("Fixed");
		Boundary_type.addItem("Cantilever");
		Boundary_type.addItem("Supported");
		Boundary_type.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				valChange = true;
				JComboBox cb = (JComboBox) e.getSource();
				boundary = (String) cb.getSelectedItem();
				valChange = true;
				//cementGradeList.setEditable(false);
				//resetSliders();
				update_image();
				repaint();

			}
		});
		JLabel dot3 = new JLabel(" ",JLabel.LEFT); 
		in2.add(lab);
		in2.add(Boundary_type);
		in2.add(dot3);

		lab = new JLabel("Concrete Grade",JLabel.LEFT);
		m_Slider[3] = new JSlider(JSlider.HORIZONTAL,20,50,20);
		m_Slider[3].setMajorTickSpacing(30);
		m_Slider[3].setMinorTickSpacing(10);
		m_Slider[3].setPaintTicks(true);
		m_Slider[3].setPaintLabels(true);
		m_Slider[3].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[3] = val;
				iLabel[3].setText(":: " + fields[3]);
				repaint();
			}
		});
		m_Slider[3].setBackground(bk);
		in2.add(lab);
		in2.add(m_Slider[3]);
		in2.add(iLabel[3]);

		lab = new JLabel("Loading Type",JLabel.LEFT);
		JComboBox Loading_type = new JComboBox();
		Loading_type.addItem("Point Load");
		Loading_type.addItem("UDL");
		Loading_type.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				valChange = true;
				JComboBox cb = (JComboBox) e.getSource();
				loading = (String) cb.getSelectedItem();
				valChange = true;
				//cementGradeList.setEditable(false);
				resetSliders();
				update_image();
				repaint();

			}
		});
		dot1 = new JLabel(" ",JLabel.LEFT);
		in2.add(lab);
		in2.add(Loading_type);
		in2.add(dot1);

		lab = new JLabel("Force(KN)",JLabel.LEFT);
		m_Slider[4] = new JSlider(JSlider.HORIZONTAL,1,50,5);
		m_Slider[4].setMajorTickSpacing(49);
		m_Slider[4].setMinorTickSpacing(5);
		m_Slider[4].setPaintTicks(true);
		m_Slider[4].setPaintLabels(true);
		m_Slider[4].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[4] = val;
				iLabel[4].setText(":: " + fields[4]+" KN");
				repaint();
				// univ.getCanvas().repaint();

			}
		});
		m_Slider[4].setBackground(bk);
		in3.add(lab);
		in3.add(m_Slider[4]);
		in3.add(iLabel[4]);

		lab = new JLabel("Force(KN)",JLabel.LEFT);
		m_Slider[5] = new JSlider(JSlider.HORIZONTAL,1,100,5);
		m_Slider[5].setMajorTickSpacing(99);
		m_Slider[5].setMinorTickSpacing(10);
		m_Slider[5].setPaintTicks(true);
		m_Slider[5].setPaintLabels(true);
		m_Slider[5].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[5] = val;
				iLabel[5].setText(":: " + fields[5]+" KN");
				repaint();
				// univ.getCanvas().repaint();

			}
		});
		m_Slider[5].setBackground(bk);
		in3.add(lab);
		in3.add(m_Slider[5]);
		in3.add(iLabel[5]);

		lab = new JLabel("Point(dm)",JLabel.LEFT);
		//the slider is defined at the beginning of the function.
		m_Slider[6].setMajorTickSpacing((int) fields[0]);
		m_Slider[6].setPaintTicks(true);
		m_Slider[6].setPaintLabels(true);
		m_Slider[6].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[6] = val;
				iLabel[6].setText(":: " + fields[6]+" dm");
				repaint();
				// univ.getCanvas().repaint();

			}
		});
		m_Slider[6].setBackground(bk);
		in3.add(lab);
		in3.add(m_Slider[6]);
		in3.add(iLabel[6]);
		bottomPanel.setVisible(false);
	}

	/*private int getiSubVal(String obj) {

		if (obj == "Point Load" && stage == 0 || stage == 1) {
			String str = "resources/C_A123/1.jpg";
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
	}*/

	private void initInputControlsField() {

		iLabel = new JLabel[7];
		int i = 0;
		iLabel[i] = new JLabel(":: 25 dm", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel(":: 300 mm", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel(":: 300 mm", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel(":: 20", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel(":: 5 KN", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel(":: 5 kN", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel(":: 1 dm", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);

		i = 0;
		fields = new double[7];
		fields[0] = 25.0;
		fields[1] = 300.0;
		fields[2] = 300.0;
		fields[3] = 20.0;
		fields[4] = 5.0;
		fields[5] = 5.0;
		fields[6] = 1.0;
	}

	/*private void onNextStage() {
		System.out.println("You are tring to see the stage" + s);
		valChange = true;
		//resetOutputParameters();
		bottomPanel.setVisible(true);
		enableStage((int)s);
		setInstructionText();
	}*/

	private void enableStage(int s) {

		switch (s) 
		{
		case 0: // Home
			enable(in1, false);
			enable(in2, false);
			enable(in3, false);
			break;

		case 1:

			enable(in1, true);
			enable(in2, true);
			enable(in3, true);
			resetSliders();
			break;

			/*case 2:

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
			//nextButton.setVisible(false);
			break;*/

		}
		//System.out.println(in3.isEnabled()+" "+s);

	}

	/*private void setInstructionText() {

		valChange = true;
		resetOutputParameters();

		switch ((int)s) {
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

	}*/

	/*private void resetOutputParameters() 
	{
		//int i = 2;
		//System.out.println("Entered");
		//if(flag_panel==1)
		//{
		//}
		//else
		/*{
			m_Slider[4].setEnabled(false);
			m_Slider[5].setEnabled(false);
			m_Slider[6].setEnabled(false);
		}
   }*/
	private void resetSliders()
	{
		if(loading.equals("Point Load"))
		{
			m_Slider[4].setEnabled(false);
			m_Slider[5].setEnabled(true);
			m_Slider[6].setEnabled(true);
		}
		else if(loading.equals("UDL"))
		{
			m_Slider[4].setEnabled(true);
			m_Slider[5].setEnabled(false);
			m_Slider[6].setEnabled(false);
		}
	}

	/*	private void setCameraViews() {
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

	}*/

	/*private void timerActionVerticalCameraMotion(java.awt.event.ActionEvent evt) {
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
	}*/

	/*private void timerActionHorizontalCameraMotion(
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
	}*/

	private void startSimulation(java.awt.event.ActionEvent evt) 
	{
		ImageIcon icon = m_j3d.createImageIcon("resources/icons/stop.png");
		startButton.setIcon(icon);
		startButton.setText("Stop");
		enableStage(0);
		//reStartButton.setEnabled(false);
		//		nextButton.setEnabled(false);

		/*ShearGraph.setState(1);
		MomentGraph.setState(1)*/
		draw_graphs();

		/*if (valChange) 
		{

		}*/
		timer.start();

		//System.out.println("Timer started");
	}
	private void update_image()
	{
		if(boundary.equals("Cantilever"))
		{
			if(loading.equals("Point Load"))
			{	
				//setting the scale
				icon = m_j3d.createImageIcon("resources/Beams/Cantilever_point.PNG");
				rightIcon.setIcon(icon);
			}
			else
			{
				icon = m_j3d.createImageIcon("resources/Beams/Cantilever_udl.PNG");
				rightIcon.setIcon(icon);
			}
		}
		else if(boundary.equals("Supported"))
		{
			if(loading.equals("Point Load"))
			{	
				//setting the scale
				icon = m_j3d.createImageIcon("resources/Beams/Supported_point.PNG");
				rightIcon.setIcon(icon);
			}
			else
			{
				icon = m_j3d.createImageIcon("resources/Beams/Supported_udl.PNG");
				rightIcon.setIcon(icon);
			}
		}
		else 
		{
			if(loading.equals("Point Load"))
			{	
				//setting the scale
				icon = m_j3d.createImageIcon("resources/Beams/Fixed_point.PNG");
				rightIcon.setIcon(icon);
			}
			else
			{
				icon = m_j3d.createImageIcon("resources/Beams/Fixed_udl.PNG");
				rightIcon.setIcon(icon);
			}
		}

	}
	private void draw_graphs() 
	{
		double l = fields[0];
		//double br = fields[1]/1000;
		//double d = fields[2]/1000;
		double w = fields[4];
		double a = fields[6];
		double b = l-a;
		double p = fields[5];
		double x;
		//System.out.println(l+" "+a);
		//double ht = rightPanel.getHeight();
		//dimension = new Dimension(270,150);
		//rightIcon.setPreferredSize(dimension);

		ShearGraph.clearGraphValue();
		MomentGraph.clearGraphValue();
		if(boundary.equals("Cantilever"))
		{
			if(loading.equals("Point Load"))
			{	
				//setting the scale
				//icon = m_j3d.createImageIcon("resources/Beams/Cantilever_point.PNG");
				//rightIcon.setIcon(icon);
				//rightPanel.getPreferredSize();

				ShearGraph.setYScale((float) (35/p));				
				MomentGraph.setYScale((float) (67/ (p*(l-a)) ));
				ShearGraph.setXscale((float) (140/l));
				MomentGraph.setXscale((float) (140/l));
				for(x=0;x<=l;x+=0.05)
				{
					if(x>=a)
					{
						ShearGraph.addGraphValue(x,p);
						MomentGraph.addGraphValue(x,p*(x-a));
					}
				}
			}
			else
			{
				//icon = m_j3d.createImageIcon("resources/Beams/Cantilever_udl.PNG");
				//rightIcon.setIcon(icon);
				ShearGraph.setYScale( (float) (400/(50*l)));
				MomentGraph.setYScale( (float) (134/(w*l*l)) );
				ShearGraph.setXscale((float) (140/l));
				MomentGraph.setXscale((float) (140/l));
				for(x=0;x<=l;x+=0.05)
				{      				
					ShearGraph.addGraphValue(x,w*x);
					MomentGraph.addGraphValue(x,(w*x*x)/2);
				}
			}
		}
		else if(boundary.equals("Supported"))
		{
			if(loading.equals("Point Load"))
			{	
				//setting the scale
				//icon = m_j3d.createImageIcon("resources/Beams/Supported_point.PNG");
				//rightIcon.setIcon(icon);
				ShearGraph.setYScale(0.5f);
				MomentGraph.setYScale((float) (67*l/ (p*a*b)) );
				for(x=0;x<=l;x+=0.05)
				{
					if(x<b)
					{
						MomentGraph.addGraphValue(x,(p*b*x)/l);
					}
					else if(x==b)
					{
						MomentGraph.addGraphValue(x,(p*b*a)/l);
					}
					else
					{
						MomentGraph.addGraphValue(x, (p*a*(l-x))/l );	
					}
					if(x<a)
					{
						ShearGraph.addGraphValue(x,(p*a)/l);
					}
					else
					{
						ShearGraph.addGraphValue(x,(p*b)/l);
					}
				}
			}
			else
			{
				//icon = m_j3d.createImageIcon("resources/Beams/Supported_udl.PNG");
				//rightIcon.setIcon(icon);
				ShearGraph.setYScale( (float) (134/(w*l)));
				MomentGraph.setYScale( (float) (536/(w*l*l)) );
				for(x=0;x<=l;x+=0.05)
				{      				
					ShearGraph.addGraphValue(x,w*(l/2-x));
					MomentGraph.addGraphValue(x,(w*x*(l-x))/2);
				}
			}
		}
		else 
		{
			if(loading.equals("Point Load"))
			{	
				//setting the scale
				//icon = m_j3d.createImageIcon("resources/Beams/Fixed_point.PNG");
				//rightIcon.setIcon(icon);
				double R1 = (p*b*b*(3*a+b))/l;
				double R2 = (p*a*a*(3*b+a))/l;
				double max = R1;
				if(R2>R1)
					max = R2;
				ShearGraph.setYScale((float) (67/(max)));
				MomentGraph.setYScale((float) (67/ (p*l)) );
				for(x=0;x<=l;x+=0.05)
				{
					if(x<a)
					{
						MomentGraph.addGraphValue(x,(R1*x)- ( (p*a*a*b*b) / (l*l) ));
					}
					else if(x==a)
					{
						MomentGraph.addGraphValue(x, (2*p*a*a*b*b) / (l*l*l) );
					}
					else
					{
						MomentGraph.addGraphValue(x,(R2* (l-x) )- ( (p*a*a*b*b) / (l*l) ));	
					}
					if(x<a)
					{
						ShearGraph.addGraphValue(x,R1);
					}
					else
					{
						ShearGraph.addGraphValue(x,R2);
					}
				}
			}
			else
			{
				//icon = m_j3d.createImageIcon("resources/Beams/Fixed_udl.PNG");
				//rightIcon.setIcon(icon);
				ShearGraph.setYScale( (float) (134/(w*l)));
				MomentGraph.setYScale( (float) ( (67*12) / (w*l*l) ) );
				for(x=0;x<=l;x+=0.05)
				{      				
					ShearGraph.addGraphValue(x,w * (l/2-x) );
					MomentGraph.addGraphValue(x,( w * (6*l*x-l*l-6*x*x) )/12);
				}
			}
		}
		ShearGraph.drawGraph();
		MomentGraph.drawGraph();
	}
	private void timerActionPerformed(java.awt.event.ActionEvent evt) 
	{
		stage++;
		stage = stage%10;

		Vector3d scale = new Vector3d(0.02,0.02,1);
		Vector3d rot = new Vector3d(0,0,180);
		Vector3d pos;
		Transform3D t = new Transform3D();

		float rad = (float)Math.PI/180;

		Transform3D t1 = new Transform3D();
		t1.setScale(new Vector3d((fields[0]+10)/35,(fields[1]+700)/1000,(fields[2]+200)/500));
		beam.setTransform(t1);
		double length = fields[0];
		double point;
		double force;

		/*ShearGraph.clearGraphValue();
		for(double x=0;x<67;x+=0.1)
		{
			ShearGraph.setYScale(1f);
			ShearGraph.setXscale(1f);
			ShearGraph.addGraphValue((stage/10)+1,x);
			ShearGraph.drawGraph();
		}*/
		double l=0.44;
		if(boundary.equals("Cantilever"))
		{
			t1 = new Transform3D();
			t1.setTranslation(new Vector3d(-0.30*(((fields[0])/25)-1),10,0));
			support1.setTransform(t1);
			t1 = new Transform3D();
			t1.setTranslation(new Vector3d(0.30*(((fields[0]+10)/35)-1),0,0));
			support2.setTransform(t1);

			if(loading.equals("Point Load"))
			{			
				//Translating Arrow.
				point = fields[6];
				force = fields[5]/150;

				if(length>=35)
				{
					point*=31.0/35;
				}
				else if(length>=30)
				{
					point*=5.0/6;
				}
				else if(length>=25)
				{
					point*=0.84;
				}
				else if(length>=20)
				{
					point*=3.0/4;
				}
				else if(length>=15)
				{
					point*=0.7;
				}
				else if(length>=10)
				{
					point*=3.0/5;
				}
				//System.out.println("point = "+point);
				pos = new Vector3d(((point)*0.44/(length))-0.22,(9-(stage)/(3))*((force+900)/1000)/100,0);
				t.rotZ(rad*rot.z);
				t.setScale(scale);        
				t.setTranslation(pos);
				arrow.setTransform(t);

				double a = (point*0.44)/length;
				double b = 0.44-a;

				//For setting other arrows out of visibility.
				for(int i=0;i<5;i++)
				{
					t = new Transform3D();
					pos = new Vector3d(3.0,(9.3-(stage)/(3))/100,0);
					t.rotZ(rad*rot.z);
					t.setScale(scale);        
					t.setTranslation(pos);
					arrows_udl[i].setTransform(t);
				}

				//Translating beam.
				double j = -0.22;

				for(int i=0;i<880;i++,j+=0.0005)
				{
					l=0.44;
					double x = l+j-0.22;
					if(x>a)
					{
						double z = l-x;
						t1.setTranslation(new Vector3d(j,-((stage/10))*(force*z*z*(3*b-l+x)),0));
					}
					else
					{
						t1.setTranslation(new Vector3d(j,-((stage/10))*(force*b*b*(0.44*3-3*x-b)),0));
					}
					t1.setScale(new Vector3d(0.02,0.045,0.045));
					cubes[i].setTransform(t1);
				}
			}
			else
			{			

				//Taking the pointload arrow out of visibility.
				pos = new Vector3d(3,(9-(stage)/(3))/100,0);
				t.rotZ(rad*rot.z);
				t.setScale(scale);        
				t.setTranslation(pos);
				arrow.setTransform(t);

				//Translating.
				for(int i=0;i<5;i++)
				{
					t = new Transform3D();
					pos = new Vector3d((i-2)*0.1,(9.3-(stage)/(3))/100,0);
					t.rotZ(rad*rot.z);
					t.setScale(scale);        
					t.setTranslation(pos);
					arrows_udl[i].setTransform(t);
				}
				//Translating beam.
				double j = -0.22;
				force = fields[4]/10;
				for(int i=0;i<880;i++,j+=0.0005)
				{
					 l=0.44;
					double x = l+j-0.22;
					t1.setTranslation(new Vector3d(j,-((stage/50)*(force/3))*((x*x*x*x-4*l*l*l*x+3*l*l*l*l)),0));
					t1.setScale(new Vector3d(0.02,0.045,0.045));
					cubes[i].setTransform(t1);
				}
			}
		}
		else if(boundary.equals("Supported"))
		{
			t1 = new Transform3D();
			t1.setTranslation(new Vector3d(-0.30*(((fields[0]+16)/35)-1),-(0.33+fields[1]/9000),0));
			support1.setTransform(t1);
			t1 = new Transform3D();
			t1.setTranslation(new Vector3d(0.30*(((fields[0]+16)/35)-1),-(0.33+fields[1]/9000),0));
			support2.setTransform(t1);

			if(loading.equals("Point Load"))
			{			
				//Translating Arrow.
				point = fields[6];
				double a = (point*0.44)/length;
				double b = 0.44-a;

				pos = new Vector3d((point*0.44/length)-0.22,(9-(stage)/(3))/100,0);
				t.rotZ(rad*rot.z);
				t.setScale(scale);        
				t.setTranslation(pos);
				arrow.setTransform(t);

				//For setting other arrows out of visibility.
				for(int i=0;i<5;i++)
				{
					t = new Transform3D();
					pos = new Vector3d(3.0,(9.3-(stage)/(3))/100,0);
					t.rotZ(rad*rot.z);
					t.setScale(scale);        
					t.setTranslation(pos);
					arrows_udl[i].setTransform(t);
				}

				//Translating beam.
				double j = -0.22;
				force = fields[5]/10;
				for(int i=0;i<880;i++,j+=0.0005)
				{
					//double l=0.44;
					double x = l+j-0.22;
					if(x>a)
					{
						x=0.44-x;
						a = b - a + ( b = a );
					}
					t1.setTranslation(new Vector3d(j,-((stage/10))*(force*b*x*(0.44*0.44-b*b-x*x)),0));
					t1.setScale(new Vector3d(0.02,0.045,0.045));
					cubes[i].setTransform(t1);
				}
			}
			else
			{			

				//Taking the pointload arrow out of visibility.
				pos = new Vector3d(3,(9-(stage)/(3))/100,0);
				t.rotZ(rad*rot.z);
				t.setScale(scale);        
				t.setTranslation(pos);
				arrow.setTransform(t);
				force = fields[4];

				//Translating.
				for(int i=0;i<5;i++)
				{
					t = new Transform3D();
					pos = new Vector3d((i-2)*0.1,(9.3-(stage)/(3))/100,0);
					t.rotZ(rad*rot.z);
					t.setScale(scale);        
					t.setTranslation(pos);
					arrows_udl[i].setTransform(t);
				}
				//Translating beam.
				double j = -0.22;
				for(int i=0;i<880;i++,j+=0.0005)
				{
					//double l=0.44;
					double x = l+j-0.22;
					t1.setTranslation(new Vector3d(j,-((stage)*(force/130)*x)*((l*l*l-2*l*x*x+x*x*x)),0));
					t1.setScale(new Vector3d(0.02,0.045,0.045));
					cubes[i].setTransform(t1);
				}
			}
		}
		else if(boundary.equals("Fixed"))
		{
			t1 = new Transform3D();
			t1.setTranslation(new Vector3d(-0.30*(((fields[0]+17)/35)-1),0,0));
			support1.setTransform(t1);
			t1 = new Transform3D();
			t1.setTranslation(new Vector3d(0.30*(((fields[0]+17)/35)-1),0,0));
			support2.setTransform(t1);

			if(loading.equals("Point Load"))
			{			
				//Translating Arrow.
				point = fields[6];

				if(length>=35)
				{
					//point*=31.0/35;
				}
				else if(length>=30)
				{
					point*=28.0/30;
					point++;
				}
				else if(length>=25)
				{
					point*=0.84;
					point+=2;
				}
				else if(length>=20)
				{
					point*=4.0/5;
					point+=2;
				}
				else if(length>=15)
				{
					point*=11.0/15;
					point+=2;
				}
				else if(length>=10)
				{
					point*=3.0/5;
					point+=2;
				}
				pos = new Vector3d((point*0.44/length)-0.22,(9-(stage)/(3))/100,0);
				t.rotZ(rad*rot.z);
				t.setScale(scale);        
				t.setTranslation(pos);
				arrow.setTransform(t);

				double a = (point*0.44)/length;
				double b = 0.44-a;
				//For setting other arrows out of visibility.
				for(int i=0;i<5;i++)
				{
					t = new Transform3D();
					pos = new Vector3d(3.0,(9.3-(stage)/(3))/100,0);
					t.rotZ(rad*rot.z);
					t.setScale(scale);        
					t.setTranslation(pos);
					arrows_udl[i].setTransform(t);
				}

				//Translating beam.
				double j = -0.22;
				force = fields[5]/10;
				for(int i=0;i<880;i++,j+=0.0005)
				{
					//double l=0.44;
					double x = l+j-0.22;
					if(x>a)
					{
						x=0.44-x;
						a = b - a + ( b = a );
					}
					t1.setTranslation(new Vector3d(j,-(stage)*(force*b*b*x*x*(3*a*l-3*a*x-b*x)),0));
					t1.setScale(new Vector3d(0.02,0.045,0.045));
					cubes[i].setTransform(t1);
				}
			}
			else
			{			
				//Taking the pointload arrow out of visibility.
				pos = new Vector3d(3,(9-(stage)/(3))/100,0);
				t.rotZ(rad*rot.z);
				t.setScale(scale);        
				t.setTranslation(pos);
				arrow.setTransform(t);
				force = fields[4];

				//Translating.
				for(int i=0;i<5;i++)
				{
					t = new Transform3D();
					pos = new Vector3d((i-2)*0.1,(9.3-(stage)/(3))/100,0);
					t.rotZ(rad*rot.z);
					t.setScale(scale);        
					t.setTranslation(pos);
					arrows_udl[i].setTransform(t);
				}
				//Translating beam.
				double j = -0.22;
				for(int i=0;i<880;i++,j+=0.0005)
				{
					//double l=0.44;
					double x = l+j-0.22;
					if(x>0.22)
					{
						x = l - x;
					}
					double  y = l - x;
					//System.out.println(x+" "+y);
					t1.setTranslation(new Vector3d(j,-((stage)*(force/50)*x*x)*(y*y),0));
					t1.setScale(new Vector3d(0.02,0.045,0.045));
					cubes[i].setTransform(t1);
				}
			}
		}
		return;
	}
	/*private void updateSimulationBody(double disp) {

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

	}*/

	private void pauseSimulation() {
		timer.stop();
		ImageIcon icon = m_j3d.createImageIcon("resources/icons/start.png");
		startButton.setIcon(icon);
		startButton.setText("Start");
		//reStartButton.setEnabled(true);
		// bottomPanel.setVisible(true);
		// nextButton.setEnabled(true);

		//rightPanel.setVisible(true);
		enableStage(1);
		//ShearGraph.setState(0);
		//MomentGraph.setState(0);
		// startButton.setEnabled(true);

		valChange = false;
		repaint();
	}
	/*public void update(float addy) 
	{
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
	public void update1(float addy)
	{
		Vector3d s = new Vector3d();
		// Get Scale
		TransformGroup objtrans = (TransformGroup) hm.get("target2");
		Transform3D trans = new Transform3D();
		objtrans.getTransform(trans);
		trans.getScale(s);
		trans.setScale(s);
		trans.setTranslation(new Vector3d(0.4, 0.272 - addy, 0.1));
		objtrans.setTransform(trans);
	}*/
}
/*class BeamBody2 {
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

}*/