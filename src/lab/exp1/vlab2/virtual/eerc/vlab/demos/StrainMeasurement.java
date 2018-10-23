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
//import java.util.ArrayList;
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

public class StrainMeasurement extends javax.swing.JPanel {
	// Variables declaration - do not modify//GEN-BEGIN:variables
	// ////////////////////////GUI componenet ///////////////////////////
	private javax.swing.JPanel topPanel;

	private javax.swing.JPanel simulationPanel;

	private javax.swing.JPanel bottomPanel;

	private javax.swing.JPanel rightPanel;

	private javax.swing.JPanel in1; // Input panel 1

	private javax.swing.JPanel in2; // Input panel 2

	private javax.swing.JPanel in3; // Input panel 3

	// private javax.swing.JPanel rp1; // Right Input panel 1
	// private javax.swing.JPanel rp2; // Right Input panel 2
	// private javax.swing.JPanel ImagePanel;

	private javax.swing.JButton startButton = null;

	private javax.swing.JButton reStartButton = null;

	private javax.swing.JButton nextButton = null;

	private JComboBox cementGradeList;

	String obj_f;

	private int val = 20;

	private JSlider m_Slider[] = new JSlider[4];

	// private JLabel lbl_damping;
	// private JComboBox type_of_force;
	String objImg;

	private javax.swing.JButton rightIcon = null;

	// private GraphPlotter graphPlotter;
	// //////////////////////////Java3D componenet ///////////////////////////

	private SimpleUniverse univ = null; // Simple Universe Java3D

	private BranchGroup scene = null; // BranchGroup Scene graph

	private Switch switchGroup = null; //

	TransformGroup beam = new TransformGroup(new Transform3D());

	private Switch objSwitch = new Switch();

	private StrainMeasurementBody freeBody = null; // Shape3D

	@SuppressWarnings("unchecked")
	private HashMap hm = new HashMap();

	private J3DShape m_j3d = new J3DShape();

	private double fields[];

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

	// private JComboBox ch;
	// private JComboBox che;
	// private JLabel lbl_k;

	// private String[] units ={" (m) "," (m) "," (mm) "," (Kg/m^3) ",
	// " (m) "," (mm) "," (mm) ","",
	// " (m/s) "," (mm) "," (%) "};

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
		objRoot.addChild(m_j3d.createBox(new Vector3d(0, -0.6, -.1),
				new Vector3d(1.5, .01, 1.5), new Vector3d(0, 0, 0),
				new Color3f(.8f, .8f, .8f), "resources/images/tile.jpg"));
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

//		 objRoot.addChild(m_j3d.createBox(new Vector3d(-0.3,0.0f,0), new
//		 Vector3d(0.05,0.3f,0.3),new Vector3d(0,0,0),new Color3f(1f, 1f,
//		 0.9f),"resources/images/grey13.jpg"));

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

		beam.addChild(m_j3d.createBox(new Vector3d(0.0, 0.0f, 0), new Vector3d(
				0.18, 0.04f, 0.04), new Vector3d(0, 0, 0), new Color3f(1f, 1f,
				0.9f), "resources/images/grey13.jpg"));
		objtrans.addChild(beam);
		freeBody = new StrainMeasurementBody();
//		 objtrans.addChild(m_j3d.createBox(new Vector3d(0.0,-0.2f,0), new
//		 Vector3d(0.24,0.04f,0.04),new Vector3d(0,0,0),new Color3f(1f, 1f,
//		 0.9f),"resources/images/grey13.jpg"));
//		 objtrans.addChild(m_j3d.createBox(new Vector3d(0.0,-0.4f,0), new
//		 Vector3d(0.30,0.04f,0.04),new Vector3d(0,0,0),new Color3f(1f, 1f,
//		 0.9f),"resources/images/grey13.jpg"));
		return objtrans;
	}

	/**
	 * Creates new form FreeVibration
	 */
	public StrainMeasurement(Container container) {
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
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		StrainMeasurement mainPanel;

		public void init() {
			setLayout(new BorderLayout());
			mainPanel = new StrainMeasurement(this);
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
			setTitle("StrainMeasurement Responce Body Applet");
			getContentPane().add(new StrainMeasurement(this),
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

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		// new GridLayout(2, 1)
		beam.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
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

				valChange = true;
				startSimulation(evt);
				univ.getCanvas().repaint();

				// reStartButton.setEnabled(false);
				// //startButton.setEnabled(true);
				// startButton.setText("Start");
				// startStop = false;
				// timer.stop();
				// outputGraph.clearGraphValue();
				// inputGraph.clearGraphValue();
				//                
				// valChange = true;

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

		// javax.swing.JButton btn= new javax.swing.JButton("Full View Graph");
		// guiPanel.add(btn, gridBagConstraints);
		// icon = m_j3d.createImageIcon("resources/icons/graph_window.png");
		// btn.setIcon(icon);
		// btn.addActionListener(new java.awt.event.ActionListener() {
		// @SuppressWarnings("static-access")
		// public void actionPerformed(java.awt.event.ActionEvent evt) {
		// 
		// HorizontalGraph graph[] ={outputGraph};
		// int max[]={1000,100};
		// int magX[]={2,2};
		// int magY[]={2,2};
		//            	
		// JFrame frame = new JFrame("Full View Graph");
		// //GraphicsEnvironment e =
		// GraphicsEnvironment.getLocalGraphicsEnvironment();
		// //Add contents to the window.
		//            	
		// //frame.add(p);
		// frame.setExtendedState(frame.getExtendedState() |
		// frame.MAXIMIZED_BOTH);
		//
		// //frame.setMaximizedBounds(e.getMaximumWindowBounds());
		// //frame.setSize(e.getMaximumWindowBounds().width,
		// e.getMaximumWindowBounds().height);
		//                
		// //Display the window.
		// frame.pack();
		// frame.setVisible(true);
		// //frame.setResizable(false);
		//                
		// fullViewGraph =new
		// FullViewGraph(graph,max,magX,magY,frame.getWidth()-20,
		// frame.getHeight());
		// frame.add(fullViewGraph); //Pradeep: added
		// // System.out.println("w " + frame.getWidth() + " h " +
		// frame.getHeight());
		//                
		//                
		// }
		// });

		guiPanel.add(reStartButton, gridBagConstraints);
		guiPanel.add(startButton, gridBagConstraints);
		guiPanel.add(nextButton, gridBagConstraints);

		javax.swing.JButton btn = new javax.swing.JButton("Manual");
		icon = m_j3d.createImageIcon("resources/icons/manual.png");
		btn.setIcon(icon);
		// startButton.setPreferredSize(new Dimension(100,30));
		// guiPanel.add(btn, gridBagConstraints);
		btn.setVisible(false); // Pradeep: they said to remove Manual

		btn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				HelpWindow.createAndShowGUI("forcedVib");
			}
		});

	}

	private void rightPanel() {

		rightPanel.setLayout(new java.awt.BorderLayout());

		JPanel panel = new JPanel();
		panel.setBackground(new Color(140, 200, 240));

		ImageIcon icon = m_j3d
				.createImageIcon("resources/Data/StrainMeasurement/stepforce1.jpg");

		rightIcon = new javax.swing.JButton(" ");
		rightIcon.setIcon(icon);
		panel.add(rightIcon);

		panel.setPreferredSize(new Dimension(300, 175));
		// rightTop.setBackground(new Color(200,200,100));
		rightPanel.add(panel, BorderLayout.NORTH);

		JPanel rightBottom = new JPanel();
		rightBottom.setPreferredSize(new Dimension(300, 295));
		// rightBottom.setBackground(new Color(100,200,100));

		// outputGraph = new HorizontalGraph(300,280,"t ","u(t)");
		// outputGraph.setHeading("Displacement Response ");
		// outputGraph.setAxisUnit("sec","m");
		// outputGraph.setYAxisColor(Color.BLUE);
		// outputGraph.setYScale(500);
		// outputGraph.fitToYwindow(true);
		// HorizontalGraphWrapper wrapper = new
		// HorizontalGraphWrapper(outputGraph,1000,2,Color.GRAY);
		//        
		// rightBottom.add(wrapper); //added wrapper instead of outputGraph
		//        
		// rightPanel.add(rightBottom,BorderLayout.SOUTH);
		//        

		// Can't call draw graph here as Graphics object is not initialize

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
		JLabel lbl = new JLabel("StrainMeasurement Responce of SDOF ",
				JLabel.CENTER);
		lbl.setFont(new Font("Arial", Font.BOLD, 18));

		lbl.setForeground(Color.orange);
		// lbl.setBackground(Color.BLACK);
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

		m_Objective = new JLabel(
				">: Start the experiment and observe the Displacement Response with respect to Time.",
				JLabel.LEFT);
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
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);

		javax.swing.JButton viewButton = new javax.swing.JButton(
				"Horizontal View");
		ImageIcon icon = m_j3d.createImageIcon("resources/icons/h-view.png");
		viewButton.setIcon(icon);
		viewButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				// 0 ,
				if (m_cameraTimer != null && m_cameraTimer.isRunning()) {
					m_cameraTimer.stop();
				}
				setCameraViews();
				m_cameraTimer = new Timer(200, new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						// ...Perform a task...
						timerActionHorizontalCameraMotion(evt);
					}
				});
				m_cameraTimer.start();
			}
		});

		// guiPanel.add(viewButton, gridBagConstraints);

		viewButton = new javax.swing.JButton("Vertical View");
		icon = m_j3d.createImageIcon("resources/icons/v-view.png");
		viewButton.setIcon(icon);
		viewButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				if (m_cameraTimer != null && m_cameraTimer.isRunning()) {
					m_cameraTimer.stop();
				}
				setCameraViews();
				m_cameraTimer = new Timer(200, new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						// ...Perform a task...
						timerActionVerticalCameraMotion(evt);
					}
				});
				m_cameraTimer.start();

			}
		});

		// guiPanel.add(viewButton, gridBagConstraints);

		JCheckBox chkbox = new JCheckBox("");
		lbl = new JLabel("Change Input Parameters", JLabel.CENTER);
		// lbl.setFont(new Font("Arial", Font.BOLD, 18));
		icon = m_j3d.createImageIcon("resources/icons/tasklist.png");
		lbl.setIcon(icon);
		chkbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				boolean bChecked = ((JCheckBox) event.getSource()).isSelected();
				if (bChecked)
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
		// lbl.setFont(new Font("Arial", Font.BOLD, 18));
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
	//	guiPanel.add(chkbox, gridBagConstraints);
	//	guiPanel.add(lbl, gridBagConstraints);

		btmPanel.add(guiPanel, BorderLayout.CENTER);

		guiPanel = new javax.swing.JPanel(); // 
		guiPanel.setBackground(new Color(130, 169, 193));
		guiPanel.setBorder(BorderFactory.createLineBorder(new Color(235, 233,
				215), 4));
		// guiPanel.add(createInputOutputPanel());
		btmPanel.add(guiPanel, BorderLayout.SOUTH);

	}

	// private JPanel createInputOutputPanel(){
	//    	
	// JPanel ioparm = new JPanel(new java.awt.GridLayout(1,3));
	// //ioparm.setPreferredSize(new java.awt.Dimension(600, 100));
	// ioparm.setBackground(new Color(67,143,205));
	// JPanel parm = new JPanel(new java.awt.GridLayout(4,2,0,10));
	// parm.setBackground(new Color(130,169,193));
	// outlbl_val = new JLabel[4];// new =
	//    	
	// int i=0;
	// JLabel lbl = new JLabel("Input ", JLabel.RIGHT); parm.add(lbl);
	// lbl.setForeground(Color.yellow);
	// lbl = new JLabel("Parameters", JLabel.LEFT); parm.add(lbl);
	// lbl.setForeground(Color.yellow);
	// lbl = new JLabel("Mass", JLabel.LEFT); parm.add(lbl);
	// outlbl_val[i] = new JLabel(getMass() + " Kg", JLabel.LEFT);
	// outlbl_val[i].setForeground(Color.white); parm.add(outlbl_val[i++]);
	// lbl = new JLabel("Stiffness", JLabel.LEFT); parm.add(lbl);
	// outlbl_val[i] = new JLabel((float)getStiff() + " N/m", JLabel.LEFT);
	// outlbl_val[i].setForeground(Color.white); parm.add(outlbl_val[i++]);
	//    	
	// ioparm.add(parm);
	//    	
	//    	
	// parm = new JPanel(new java.awt.GridLayout(4,2,0,10)); //additionally
	// added
	// parm.setBackground(new Color(130,169,193));
	// lbl = new JLabel(" ", JLabel.RIGHT); parm.add(lbl);
	// lbl.setForeground(Color.yellow);
	// lbl = new JLabel(" ", JLabel.RIGHT); parm.add(lbl);
	// lbl.setForeground(Color.yellow);
	//    	
	// lbl = new JLabel(" ", JLabel.RIGHT); parm.add(lbl);
	// lbl = new JLabel(" ", JLabel.RIGHT); parm.add(lbl);
	// lbl = new JLabel(" ", JLabel.RIGHT); parm.add(lbl);
	// lbl = new JLabel(" ", JLabel.RIGHT); parm.add(lbl);
	//    
	// ioparm.add(parm);
	//    	
	//    	
	//    	
	//    	
	// parm = new JPanel(new java.awt.GridLayout(4,2,20,0));
	// parm.setBackground(new Color(130,169,193));
	// lbl = new JLabel("Output ", JLabel.RIGHT); parm.add(lbl);
	// lbl.setForeground(Color.yellow);
	// lbl = new JLabel("Parameters", JLabel.LEFT); parm.add(lbl);
	// lbl.setForeground(Color.yellow);
	//         
	//    	
	//        
	// lbl = new JLabel("Time", JLabel.RIGHT); parm.add(lbl);
	// outlbl_val[i] = new JLabel("t (sec)", JLabel.RIGHT);
	// outlbl_val[i].setForeground(Color.white); parm.add(outlbl_val[i++]); //
	// fmt.format("%.15s", outlbl_val[i++]); parm.add(toString(fmt));
	// lbl = new JLabel("Displacement", JLabel.RIGHT); parm.add(lbl);
	// outlbl_val[i] = new JLabel("d (m)", JLabel.RIGHT);
	// outlbl_val[i].setForeground(Color.white); parm.add(outlbl_val[i++]);
	//
	// ioparm.add(parm);
	//    	
	// return ioparm;
	//        
	// }

	private void bottomPanel() {
		initInputControlsField();

		Color bk = new Color(219, 226, 238);
		bottomPanel.setLayout(new java.awt.GridLayout(1, 3));
		bottomPanel.setBackground(Color.black);
		bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(235,
				233, 215), 8));

		in1 = new JPanel(new java.awt.GridLayout(4, 3));
		in1.setBackground(bk);
		bottomPanel.add(in1);

		in2 = new JPanel(new java.awt.GridLayout(2, 2));
		in2.setBackground(bk);

		bottomPanel.add(in2);

		cementGradeList = new JComboBox();
		cementGradeList.setEditable(true);

		outlbl_val = new JLabel[3];

		in3 = new JPanel(new java.awt.GridLayout(3, 2));
		in3.setBackground(bk);
		bottomPanel.add(in3);

		JLabel lab = new JLabel("Length of Member", JLabel.RIGHT);
		m_Slider[0] = new JSlider(JSlider.HORIZONTAL, 1, 10, 5);
		m_Slider[0].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[0] = val;
				iLabel[0].setText(":: " + fields[0] / 10 + " m");

				univ.getCanvas().repaint();
				// repaint();
			}
		});
		m_Slider[0].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[0]);
		in1.add(iLabel[0]);

		lab = new JLabel("Breadth of Member", JLabel.CENTER);
		m_Slider[1] = new JSlider(JSlider.HORIZONTAL, 1, 3, 2);
		m_Slider[1].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[1] = val;
				iLabel[1].setText(":: " + fields[1] / 10 + " m");
				univ.getCanvas().repaint();

			}
		});
		m_Slider[1].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[1]);
		in1.add(iLabel[1]);

		lab = new JLabel("Depth of member", JLabel.RIGHT);

		m_Slider[2] = new JSlider(JSlider.HORIZONTAL, 12, 20, 12);
		m_Slider[2].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[2] = val;
				iLabel[2].setText(":: " + fields[2] / 10 + " mm");
				univ.getCanvas().repaint();
				// repaint();
			}
		});
		m_Slider[2].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[2]);
		in1.add(iLabel[2]);
		iLabel[2].setForeground(Color.BLUE);

		lab = new JLabel("Load applied", JLabel.RIGHT);

		m_Slider[3] = new JSlider(JSlider.HORIZONTAL, 1, 20, 10);
		m_Slider[3].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[3] = val;

				iLabel[3].setText(":: " + fields[3] * 5 + " kN");
				univ.getCanvas().repaint();
				// repaint();
			}
		});
		m_Slider[3].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[3]);
		in1.add(iLabel[3]);
		iLabel[3].setForeground(Color.BLUE);

		lab = new JLabel("Column Material", JLabel.CENTER);
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
				if (obj_f.equalsIgnoreCase("Concrete")) {

					cementGradeList.addItem("M" + 25);
					cementGradeList.addItem("M" + 20);
					cementGradeList.addItem("M" + 30);
					cementGradeList.addItem("M" + 35);
					cementGradeList.addItem("M" + 40);
					int n = cementGradeList.getItemCount();
					if (n >= 7) {
						cementGradeList.removeItem("Fe" + 415);
						cementGradeList.removeItem("Fe" + 500);
					}
					cementGradeList.removeItem("Fe" + 415);
					repaint();
				}
				if (obj_f.equalsIgnoreCase("Steel")) {
					cementGradeList.addItem("Fe" + 415);
					cementGradeList.addItem("Fe" + 500);
					cementGradeList.removeItem("M" + 20);
					cementGradeList.removeItem("M" + 25);
					cementGradeList.removeItem("M" + 30);
					cementGradeList.removeItem("M" + 35);
					cementGradeList.removeItem("M" + 40);
					int n = cementGradeList.getItemCount();
					if (n >= 2) {
						cementGradeList.removeItem("M" + 20);
						cementGradeList.removeItem("M" + 25);
						cementGradeList.removeItem("M" + 30);
						cementGradeList.removeItem("M" + 35);
						cementGradeList.removeItem("M" + 40);
					}
					System.out.println(n);
					repaint();
				}
				resetOutputParameters();
				repaint();

			}
		});

		in2.add(lab);
		in2.add(Column_material);

		lab = new JLabel("Material Grade", JLabel.CENTER);

		cementGradeList.setEditable(false);
		cementGradeList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				valChange = true;
				String obj = (String) ((JComboBox) e.getSource())
						.getSelectedItem();

				System.out.println(obj);
				StringBuffer obj1 = new StringBuffer(obj);
				if (obj_f.equalsIgnoreCase("Concrete")) {

					obj1 = new StringBuffer(obj.substring(1));
				}
				if (obj_f.equalsIgnoreCase("Steel")) {

					obj1 = new StringBuffer(obj.substring(2));
					System.out.println(obj1);
				}
				val = Integer.parseInt(obj1.toString());
				resetOutputParameters();
				repaint();

			}
		});

		in2.add(lab);
		in2.add(cementGradeList);

		lab = new JLabel("Lateral Strain", JLabel.RIGHT);
		outlbl_val[0] = new JLabel("0 mm");
		in3.add(lab);
		in3.add(outlbl_val[0]);

		lab = new JLabel("Longitudinal Strain", JLabel.RIGHT);
		outlbl_val[1] = new JLabel("0 mm");
		in3.add(lab);
		in3.add(outlbl_val[1]);

		lab = new JLabel("Volumetric Strain", JLabel.RIGHT);
		outlbl_val[2] = new JLabel("0 mm");
		in3.add(lab);
		in3.add(outlbl_val[2]);
		bottomPanel.setVisible(false);

		enable(in1, false);
		enable(in2, false);
		enable(in3, false);
	}

	private void initInputControlsField() {

		iLabel = new JLabel[4];
		int i = 0;
		iLabel[i] = new JLabel("0.5 m", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel("0.2 m", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel("0.2 mm", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel("50 kN", JLabel.LEFT);
		iLabel[i++].setForeground(Color.blue);

		i = 0;
		fields = new double[4];
		fields[0] = 0.5;
		fields[1] = 0.2;
		fields[2] = 0.2;
		fields[3] = 50.0;
	}

	private void onNextStage() {

		valChange = true; // Clear the graph. or Graph will restart on Play
		resetOutputParameters(); // Clear the Output Parameters
		bottomPanel.setVisible(true);
		enableStage(stage);
		setInstructionText();

	}

	private void enableStage(int s) {
		switch (s) {
		case 0: // Home
			enable(in1, true);
			enable(in2, true);
			enable(in3, true);

			break;
		case 1: // Home

			enable(in1, true);
			enable(in2, true);
			enable(in3, true);

			break;
		case 2:

			enable(in1, true);
			enable(in2, true);
			enable(in3, true);
			nextButton.setVisible(true);
			break;
		// case 2:
		//
		// enable(in1,false); enable(in2,false); enable(in3,true);
		// break;
		//			
		// case 3:
		// enable(in1,true); enable(in2,true); enable(in3,true);
		// break;

		}

	}

	private void setInstructionText() {

		valChange = true; // Clear the graph. or Graph will restart on Play
		resetOutputParameters(); // Clear the Output Parameters

		switch (stage) {
		case 0: // Home
			m_Objective
					.setText(">: Run the experiment with selected 'type of force'.");
			m_Objective.setForeground(Color.WHITE);
			break;
		case 1:
			m_Objective
					.setText(">: Observe the effect of time period on maximum response.");
			m_Objective.setForeground(Color.GREEN);
			break;
		// case 2:
		// m_Objective.setText(">: What is resonance and how damping effect the
		// resonance?");
		// m_Objective.setForeground(Color.WHITE);
		// break;
		// case 3:
		// m_Objective.setText(">: Observe how resonance get effect in different
		// cases of Earthquake data input");
		// m_Objective.setForeground(Color.GREEN);
		// break;
		// case 4:
		// m_Objective.setText(">: Change different parameter and observe the
		// effect in time period.");
		// m_Objective.setForeground(Color.WHITE);
		// break;
		}

	}

	private void resetOutputParameters() {
		int i = 0;
		// outlbl_val[i++].setText(getMass() + " Kg");
		// outlbl_val[i++].setText(String.valueOf(getStiff()).substring(0,4)+
		// String.valueOf(getStiff()).substring(String.valueOf(getStiff()).length()-4,String.valueOf(getStiff()).length())+"
		// N/m");
		// i=2;
		// outlbl_val[i++].setText(" t sec");
		// outlbl_val[i++].setText(" d m");

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
		// Vector3d s = new Vector3d();
		Vector3f currPos = new Vector3f();
		t3d.get(currPos);

		// System.out.println("current Pos:" + currPos);
		float y = (float) (float) Math.sin(Math
				.toRadians(m_cameraViews[m_cameraEye]));
		float z = 2.41f - Math.abs(y);// ((float)Math.sin(Math.toRadians(m_cameraViews[m_cameraEye])));
		// default (0, 0, 2.41)
		// System.out.println("x" + x);
		t3d.lookAt(new Point3d(0, y, z), new Point3d(0, 0, 0), new Vector3d(0,
				1, 0));
		t3d.invert();

		// t3d.setTranslation(new Vector3d(0,0,8));
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
		// Vector3d s = new Vector3d();
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

	// Resume Button Action
	private void startSimulation(java.awt.event.ActionEvent evt) {

		// if (!rightPanel.isVisible()){
		// rightPanel.setVisible(true);
		// bottomPanel.setVisible(true);
		// }
		ImageIcon icon = m_j3d.createImageIcon("resources/icons/stop.png");
		startButton.setIcon(icon);
		startButton.setText("Stop");
		enableStage(0); // -1
		reStartButton.setEnabled(false);
		nextButton.setEnabled(false);

		if (valChange) {
			// need to write the code for init
			System.out.println(fields[0]);
			System.out.println(fields[1]);
			System.out.println(fields[2]);
			System.out.println(fields[3]);
			System.out.println(val);
			freeBody.Init(fields[0], fields[1], fields[2], fields[3], val);
			// public void Init(double len,double bre,double dep,double
			// load,double matGd)

		}

		timer.start();
		System.out.println("Timer started");
	}

	private void timerActionPerformed(java.awt.event.ActionEvent evt) {

		// int i=0;

		// updateSimulation();
		double L_ST = freeBody.getLateralStrain();
		// System.out.println("the returned value is :" + L_ST);
		outlbl_val[0].setText(L_ST + " mm");

		double L_ST1 = freeBody.getLongitudinalStrain();
		outlbl_val[1].setText(L_ST1 + " mm");

		double V_ST = freeBody.getVolumetricStrain();
		outlbl_val[2].setText(V_ST + " mm");

		Transform3D t2 = new Transform3D();

		t2.setScale(new Vector3d((1 + freeBody.getUpdate() / 100), 1, 1));
		beam.setTransform(t2);

		freeBody.update();
		if (freeBody.isDataCompleted()) {
			pauseSimulation();
			return;
		}
		return;
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
		valChange = false;
		repaint();
	}
}

class StrainMeasurementBody {

	private double LongitudinalStrain, LateralStrain, VolumetricStrain;

	double Change_depth, Change_breadth;

	private int i = 0;

	public void Init(double len, double bre, double dep, double load,
			double matGd) {
		double sigma = load / (bre * dep);
		if (matGd > 100) {
			LongitudinalStrain = sigma / (5000 * Math.sqrt(matGd));
			LateralStrain = 0.2 * LongitudinalStrain;
		} else {
			LongitudinalStrain = sigma / 200000.0;
			LateralStrain = 0.3 * LongitudinalStrain;
		}
		Change_depth = LongitudinalStrain * dep;
		Change_breadth = LateralStrain * bre;
		VolumetricStrain = LongitudinalStrain - (2 * LateralStrain);

	}

	public double getLongitudinalStrain() {
		return LongitudinalStrain;
	}

	public double getLateralStrain() {
		return LateralStrain;
	}

	public double getVolumetricStrain() {
		return VolumetricStrain;
	}

	public void update() {
		i++;
	}

	public int getUpdate() {
		return i;
	}

	public boolean isDataCompleted() {
		if (i < 200)
			return false;
		return true;
	}

}
