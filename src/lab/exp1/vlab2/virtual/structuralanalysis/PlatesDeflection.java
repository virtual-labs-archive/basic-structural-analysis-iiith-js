package structuralanalysis;

//This is for the deflection of Plates
import java.text.*;
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
import javax.media.j3d.LineStripArray;
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
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
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
// imported from common resources
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
public class PlatesDeflection extends javax.swing.JPanel {
	// Variables declaration - do not modify//GEN-BEGIN:variables
	// ////////////////////////GUI component ///////////////////////////
	private javax.swing.JPanel topPanel;
	private javax.swing.JPanel simulationPanel;
	private javax.swing.JPanel bottomPanel;
	private javax.swing.JPanel rightPanel;
//	private javax.swing.JPanel createInputOutputPanel;

	private javax.swing.JPanel in1; // Input panel 1
	private javax.swing.JPanel in2; // Input panel 2
	private javax.swing.JPanel in3; // Input panel 3
	private javax.swing.JPanel in4; // Input panel 4

	private javax.swing.JButton startButton = null;
	private javax.swing.JButton reStartButton = null;
	private javax.swing.JButton nextButton = null;

	String safty_factor = "1", materialGrade;
	int iSubVal = 1;
	private javax.swing.JButton rightIcon = null;

	// private GraphPlotter graphPlotter;
	// //////////////////////////Java3D component ///////////////////////////

	private SimpleUniverse univ = null; // Simple Universe Java3D
	private BranchGroup scene = null; // BranchGroup Scene graph
	private TransformGroup beam = new TransformGroup();
	
	private HorizontalGraph1 outputGraph = null;
	private HorizontalGraph inputGraph = null;

	@SuppressWarnings("unchecked")
	private HashMap hm = new HashMap();
	private J3DShape m_j3d = new J3DShape();

	private double[] fields;
	private JLabel iLabel[];
	private JLabel m_Objective = new JLabel("Objective:");

	private Timer timer = null;
	private Timer m_cameraTimer = null;
	private float m_cameraViews[];
	private int m_cameraEye;
	// Timer for simulation

	private int stage = 0;
	private double stage1=0;
	JComboBox cementGradeList;

	private boolean startStop = false;
	private boolean valChange = true;

	private JSlider m_Slider[] = new JSlider[8];  //No of sliders
	String obj_f;
	private TransformGroup arrow;
	private TransformGroup beam1;
	private TransformGroup[] cubes;
	private TransformGroup Ubeam1;
	private TransformGroup ptPos1;
	private TransformGroup UDLPos1;
	private TransformGroup[] arrows_udl;
	private Point3d[] points;
	private Point3d[] contourpoints;
	private Point3d[] contourpoints_copy;
	private int stripCount[]=new int[484];
	DecimalFormat df = new DecimalFormat("#.##");

	//initialisation
	double[] defl=new double[529];

	private String obj = "Point Load";// ,BOS;
	private String boundary="Simply Supported";
	private double D;
	private double min_deflection,max_deflection;
	final double E=5000*Math.sqrt(30); //Youngs Modulus
	int val = 20;
	JLabel len;
	
	private JComboBox concrete_grade;

	public BranchGroup createSceneGraph() {
		BranchGroup objRoot = new BranchGroup();
		objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		objRoot.setCapability(Group.ALLOW_CHILDREN_READ);
		objRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
		objRoot.setCapability(BranchGroup.ALLOW_DETACH);

		objRoot.addChild(createVirtualLab());
//		objRoot.addChild(createAxes());
	    
		int i,j;
		for(i=-2;i<=2;i++)
		{
			for(j=-2;j<=2;j++)
			{
				objRoot.addChild(m_j3d.createBox(new Vector3d((float)(i),-0.6,(float)(j)),new Vector3d(0.5,.01,0.5),new Vector3d(0,0,0),new Color3f(.8f, .8f, .8f),"resources/images/tile1.jpg"));
			}
		}
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.4,-2.5),new Vector3d(10,10,.01),new Vector3d(0f, 0f,0f), new Color3f(0.5f,0.6f,0.72f)));
		// Walls and roof
		objRoot.addChild(m_j3d.createBox(new Vector3d(1,0.1f,0), new Vector3d(0.05,0.7f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 0.9f),"resources/images/floor.jpg")); //right wall
		objRoot.addChild(m_j3d.createBox(new Vector3d(-1,0.1f,0), new Vector3d(0.05,0.7f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 0.9f),"resources/images/floor.jpg")); //left wall
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.1f,-2.0), new Vector3d(1,0.7f,0.05),new Vector3d(0,0,0),new Color3f(1f, 1f, 1.0f))); //back wall
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

//		Canvas3D c = new Canvas3D(config);
	    // Create a Canvas3D using the preferred configuration
	    Canvas3D c = new Canvas3D(config)
	    {
	        public void postRender()
	        {
	            this.getGraphics2D().setColor(Color.blue);
	            this.getGraphics2D().drawString("All Sides Simply Supported",100,100);
	            this.getGraphics2D().flush(false);
	        }
	    };

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
		t3d.invert();   //inverts the transform in the place
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
	private Group createAxes(){
	    BranchGroup axisBG = new BranchGroup();
        final Color3f red   = new Color3f(1.0f,0.0f,0.0f);
        final Color3f green = new Color3f(0.0f, 1.0f, 0.0f);
        final Color3f blue  = new Color3f(0.0f, 0.0f, 1.0f);
		// create line for X axis
	    LineArray axisXLines = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3 );
	    axisBG.addChild(new Shape3D(axisXLines));

	    axisXLines.setCoordinate(0, new Point3f(-1.0f, 0.0f, 0.0f));
	    axisXLines.setCoordinate(1, new Point3f( 1.0f, 0.0f, 0.0f));
	    axisXLines.setColor(0, red);
	    axisXLines.setColor(1, red);
	    //create line for Y axis
	    LineArray axisYLines = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3 );
	    axisBG.addChild(new Shape3D(axisYLines));

	    axisYLines.setCoordinate(0, new Point3f(0.0f, -5.0f, 0.0f));
	    axisYLines.setCoordinate(1, new Point3f( 0.0f, 5.0f, 0.0f));
	    axisYLines.setColor(0, green);
	    axisYLines.setColor(1, green);
	    LineArray axisZLines = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3 );
	    axisBG.addChild(new Shape3D(axisZLines));

	    axisZLines.setCoordinate(0, new Point3f( 0.0f, 0.0f, -1.0f));
	    axisZLines.setCoordinate(1, new Point3f( 0.0f, 0.0f, 1.0f));
	    axisZLines.setColor(0, blue);
	    axisZLines.setColor(1, blue);
	    return axisBG;
	}
	private Group createVirtualLab() {
		
		for(int i=0;i<484;i++)
		{
			stripCount[i]=5;
		}
		beam.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		beam.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		beam.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);

		Transform3D t = new Transform3D();
		TransformGroup objtrans = new TransformGroup(t);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		beam1 = new TransformGroup(t);
		ptPos1 = new TransformGroup(t);
		ptPos1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		ptPos1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
        String side_label;
        side_label="S-S";
		objtrans.addChild(m_j3d.createText2D(side_label,new Vector3d(0.2,0, 0.2),new Vector3d(0.6,0.6,0.6),new Color3f(0.1f,0.3f, 0.6f),16,Font.BOLD ));
		objtrans.addChild(m_j3d.createText2D(side_label,new Vector3d(-0.2,0, 0.2),new Vector3d(0.6,0.6,0.6),new Color3f(0.1f,0.3f, 0.6f),16,Font.BOLD ));
		objtrans.addChild(m_j3d.createText2D(side_label,new Vector3d(0,0,-0.30),new Vector3d(0.6,0.6,0.6),new Color3f(0.1f,0.3f, 0.6f),16,Font.BOLD ));
		objtrans.addChild(m_j3d.createText2D(side_label,new Vector3d(0,0,0.30),new Vector3d(0.6,0.6,0.6),new Color3f(0.1f,0.3f, 0.6f),16,Font.BOLD ));

		beam1.addChild(createBeam(new Vector3d(0,0,0),new Vector3d(1,1,1),new Vector3d(0,0,0),0,0.44));
		
		arrow=(TransformGroup)createArrow(new Vector3d(0,0.02,0),new Vector3d(0.02,0.02,1), new Vector3d(0,0,180),new Color3f(0.1f,0.2f,0.3f));
		arrow.setCapability(Group.ALLOW_CHILDREN_READ);
		arrow.setCapability(Group.ALLOW_CHILDREN_WRITE);
		arrows_udl = new TransformGroup[5];
		for(int i=0;i<5;i++)
		{
			arrows_udl[i] = (TransformGroup) createArrow(new Vector3d(3.0,0.1,0),new Vector3d(0.02,0.02,1), new Vector3d(0,0,180),new Color3f(0.1f,0.2f,0.3f));
			ptPos1.addChild(arrows_udl[i]);
		}
		ptPos1.addChild(arrow);
//		ptPos1.addChild(beam1);
		beam.addChild(ptPos1);
		calculateDeflections(fields[0],fields[1]);
		beam.addChild(createContour());
		objtrans.addChild(beam);
		hm.put("contourbg",beam);
		return objtrans;
	}
	private Group createContour()
	{
		int i;
		BranchGroup objtrans=new BranchGroup();
		objtrans.setCapability(BranchGroup.ALLOW_DETACH);
		objtrans.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		objtrans.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		objtrans.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        LineStripArray lineArray = new LineStripArray(2420, LineArray.COORDINATES, stripCount);
        lineArray.setCoordinates(0, contourpoints);
        Appearance blueColorAppearance = new Appearance();
        ColoringAttributes blueColoring = new ColoringAttributes();
        blueColoring.setColor(0.0f, 0.0f, 1.0f);
        blueColorAppearance.setColoringAttributes(blueColoring);
        LineAttributes lineAttrib = new LineAttributes();
        lineAttrib.setLineWidth(2.0f);
        blueColorAppearance.setLineAttributes(lineAttrib);
        objtrans.addChild(new Shape3D(lineArray, blueColorAppearance));
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
               
        cubes=new TransformGroup[529];
        TransformGroup objtrans = new TransformGroup(t);
        objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		objtrans.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		objtrans.setCapability(BranchGroup.ALLOW_DETACH);
        
        double X,Y;
        int k=0;
        for(X = -0.22; X <= 0.22; X += 0.02)
        {
        	for(Y = -0.22; Y <= 0.22; Y += 0.02)
        	{
        		cubes[k] = new TransformGroup();
    			cubes[k].setCapability(Group.ALLOW_CHILDREN_READ);
    			cubes[k].setCapability(Group.ALLOW_CHILDREN_WRITE);
    			cubes[k] =(TransformGroup) m_j3d.createBox(new Vector3d(X,0,Y), new Vector3d(0.02,0.045,0.045),new Vector3d(0,0,Math.atan(extent*2*X)*180.0/Math.PI), new Color3f(1,1,1),"resources/images/floor.jpg");
    			objtrans.addChild(cubes[k]);
        		k++;
        	}
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
	    coords[1] = new Point3f(0,-5,0);
	    line = new LineArray(2, LineArray.COORDINATES);
	    line.setCoordinates(0, coords);
	    myShape = new Shape3D(line,appea);
	    objtrans.addChild(myShape);
	    
	    return objtrans;
    }
	public void calculateDeflections(double length,double breadth)
	{
		double i,j;
		double x,y;
		int k=0,l=0,m=0,n=0;
		int nod_max;
		double min=Math.pow(10,8);
		double max=0;
		double l_x=length/22.0;
		double l_y=breadth/22.0;
		points=new Point3d[529];
        for(i=0,m=0;m<23;i+=l_x,m++)
        {
    		x=Double.parseDouble(df.format((i*0.44/length)-0.22));
        	for(j=0,n=0;n<23;j+=l_y,n++)
        	{
        		y=Double.parseDouble(df.format((j*0.44/breadth)-0.22));
        		defl[k]=getDeflection(i,j);
        		if(defl[k]<min && defl[k]!=0)
        			min=defl[k];
        		if(defl[k]>max)
        			max=defl[k];        			
        		points[k]=new Point3d(x,-defl[k],y);
        		k++;
        	}
        }
        min_deflection=min;
        max_deflection=max;
        if(max_deflection<Math.pow(Math.E,-1))
        	nod_max=0;
        else
        	nod_max=(int)Math.log10(max_deflection)+1; //no of digits in maximum
//        System.out.println("Min_deflection:"+min_deflection+" Max_deflection:"+max_deflection+" No_of_digits:"+nod_max);
//        System.out.println("Divx"+l_x+" Div_y"+l_y);
        l=0;
        k=0;
        contourpoints=new Point3d[2420];
		contourpoints_copy=new Point3d[5420];
        double temp;
        for(m=0,i=0;m<23;m++,i+=l_x)
        {
        	for(j=0,n=0;n<23;n++,j+=l_y)
        	{
        		temp=defl[k]*Math.pow(10,-1*(nod_max));
        		points[k].setY(-temp);
        //		System.out.print("X:"+points[k].getX()+" ActualX:"+df.format(i)+" ");
        //		System.out.print("Deflection:"+points[k].getY()+ " ActualY:"+defl[k]);
        //		System.out.println(" Z:"+points[k].getZ()+ " ActualZ:"+df.format(j));
        		k++;
        	}
        }
        k=0;
        for(i=-22;i<22;i+=2)
        {
        	for(j=-22;j<22;j+=2)
        	{
        		contourpoints[l]=points[k];
        		l=l+1;
        		contourpoints[l]=points[k+23];
        		l=l+1;
        		contourpoints[l]=points[k+24];
        		l=l+1;
        		contourpoints[l]=points[k+1];
        		l=l+1;
        		contourpoints[l]=points[k];
        		l=l+1;
        		k++;
        	}
        }
        for(l=0;l<2420;l++)
        	contourpoints_copy[l]=new Point3d(contourpoints[l].getX(),contourpoints[l].getY(),contourpoints[l].getZ());
	}
	/**
	 * Creates new form FreeVibration
	 */
	public PlatesDeflection(Container container) {
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
		PlatesDeflection mainPanel;

		public void init() {
			setLayout(new BorderLayout());
			mainPanel = new PlatesDeflection(this);
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
			setTitle("Analysis of Plates");
			getContentPane().add(new PlatesDeflection(this),
					BorderLayout.CENTER);
			pack();
		}
	}

	// Create a form with the specified labels, tool tips, and sizes.
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

		topPanel();
		bottomPanel();
		rightPanel();

		// Set Alignment
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
		//disabling the sliders
		m_Slider[4].setEnabled(false);
	}// </editor-fold>//GEN-END:initComponents

	private void topPanel() {

		java.awt.GridBagConstraints gridBagConstraints;

		javax.swing.JPanel guiPanel = new javax.swing.JPanel(); // Pause, resume
																// at top
		guiPanel.setLayout(new java.awt.GridBagLayout());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);

		reStartButton = new javax.swing.JButton("Re-Start");
		ImageIcon icon = m_j3d.createImageIcon("resources/icons/restart1.png");
		reStartButton.setIcon(icon);
		startButton = new javax.swing.JButton("Start");
		icon = m_j3d.createImageIcon("resources/icons/start.png");
		startButton.setIcon(icon);
		nextButton = new javax.swing.JButton("Next");
		icon = m_j3d.createImageIcon("resources/icons/next.png");
		nextButton.setIcon(icon);

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
			//  startButton.setEnabled(true);
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

		rightPanel.setLayout(new java.awt.GridLayout(3, 1, 0, 1));
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
		panel.add(rightIcon);
		rightPanel.add(panel);

		outputGraph = new HorizontalGraph1(300, 150, "t", "u''(t)");
		outputGraph.setHeading("Graph 1");
		outputGraph.setAxisUnit("sec", "g");
		outputGraph.setYAxisColor(new Color(0.0f, 0.54f, 0.27f));// Color.
																	// DARK_GRAY
																	// );
		outputGraph.setYScale(10);
		outputGraph.fitToYwindow(true);
		rightPanel.add(outputGraph);

		inputGraph = new HorizontalGraph(300, 150, "t", "u''(t)");
		inputGraph.setHeading("Graph 2");
		inputGraph.setAxisUnit("sec", "g");
		inputGraph.setYAxisColor(new Color(0.0f, 0.54f, 0.27f));//Color.DARK_GRAY
																// );
		inputGraph.setYScale(10);
		inputGraph.fitToYwindow(true);
		rightPanel.add(inputGraph);

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
		JLabel lbl = new JLabel("Analysis of Plates", JLabel.CENTER);
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

		// Bottom Part of Simulation Panel (Adding Buttons of Show Graph and Change Input Parameters)
		guiPanel = new javax.swing.JPanel(); //          
		guiPanel.setBackground(new Color(235, 233, 215));
		guiPanel.setLayout(new java.awt.GridBagLayout());
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		guiPanel.setBorder(BorderFactory.createLineBorder(new Color(140, 200,
				240), 8));
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		// Start Camera Views
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
	    // End Camera Views
	    
		JCheckBox chkbox = new JCheckBox("");
		lbl = new JLabel("Change Input Parameters", JLabel.CENTER);
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
//		guiPanel.add(chkbox, gridBagConstraints);
//		guiPanel.add(lbl, gridBagConstraints);

		btmPanel.add(guiPanel, BorderLayout.CENTER);

		 

	}

	

	private void bottomPanel() {
		initInputControlsField();

		Color bk = new Color(219, 226, 238);
		bottomPanel.setLayout(new java.awt.GridLayout(1, 4));
		bottomPanel.setBackground(Color.black);
		bottomPanel.setPreferredSize(new java.awt.Dimension(1024, 120));
		bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(235,233, 215), 8));
		in1 = new JPanel(new java.awt.GridLayout(3, 3, 5, 10));
		in1.setBackground(bk);
		bottomPanel.add(in1);

		in2 = new JPanel(new java.awt.GridLayout(2, 3, 5, 20));
		in2.setBackground(bk);
		bottomPanel.add(in2);

		in3 = new JPanel(new java.awt.GridLayout(2, 3, 5, 20));
		in3.setBackground(bk);
		bottomPanel.add(in3);
		
		in4 = new JPanel(new java.awt.GridLayout(4,3,10,5)); //rows cols horizontal vertical
		in4.setBackground(bk);
		bottomPanel.add(in4);
		
		JLabel lab,lab1;

		lab = new JLabel("Length", JLabel.LEFT);
		
		m_Slider[6] = new JSlider(JSlider.HORIZONTAL, 0, (int)(fields[0]*10), 10);

		m_Slider[0] = new JSlider(JSlider.HORIZONTAL, 10, 40,10);
		m_Slider[0].setMajorTickSpacing(30);
		m_Slider[0].setMinorTickSpacing(10);
		m_Slider[0].setPaintTicks(true);
		m_Slider[0].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[0] = val/10.0;
				m_Slider[6].setMaximum((int)(fields[0]*10));  
				iLabel[0].setText(":: " + fields[0] + " m");
				repaint();
			}
		});
		m_Slider[0].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[0]);
		in1.add(iLabel[0]);

		lab = new JLabel("Breadth", JLabel.LEFT);
	
		m_Slider[7] = new JSlider(JSlider.HORIZONTAL, 0, (int)(fields[1]*10), 10);

		m_Slider[1] = new JSlider(JSlider.HORIZONTAL, 10, 40, 10);
		m_Slider[1].setMajorTickSpacing(30);
		m_Slider[1].setMinorTickSpacing(10);
		m_Slider[1].setPaintTicks(true);
		m_Slider[1].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[1] = val/10.0;
				m_Slider[7].setMaximum((int)(fields[1]*10));  
				iLabel[1].setText(":: " + fields[1] + " m");
				// univ.getCanvas().repaint();

			}
		});
		m_Slider[1].setBackground(bk);
		in1.add(lab);
		in1.add(m_Slider[1]);
		in1.add(iLabel[1]);

		lab = new JLabel("Thickness", JLabel.LEFT);
		m_Slider[2] = new JSlider(JSlider.HORIZONTAL,100, 500, 100);
		m_Slider[2].setMajorTickSpacing(400);
		m_Slider[2].setMinorTickSpacing(100);
		m_Slider[2].setPaintTicks(true);
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


		lab = new JLabel("Concrete Grade", JLabel.LEFT);
		JComboBox concrete_grade = new JComboBox();
		concrete_grade.setAlignmentX(RIGHT_ALIGNMENT);
		concrete_grade.addItem("M15");
		concrete_grade.addItem("M20");
		concrete_grade.addItem("M25");
		concrete_grade.addItem("M30");
		concrete_grade.addItem("M35");
		concrete_grade.addItem("M40");
		concrete_grade.addItem("M45");
		concrete_grade.addItem("M50");
		concrete_grade.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				valChange = true;

				JComboBox cb = (JComboBox) e.getSource();
				obj_f = (String) cb.getSelectedItem();
				valChange = true;
				resetOutputParameters();
				repaint();

			}
		});

		in2.add(lab);
		in2.add(concrete_grade);
		lab1=new JLabel("", JLabel.RIGHT);
		in2.add(lab1);

		lab = new JLabel("Poisson Ratio", JLabel.LEFT);
		m_Slider[3] = new JSlider(JSlider.HORIZONTAL, 1, 6, 6);
		m_Slider[3].setMajorTickSpacing(5);
		m_Slider[3].setMinorTickSpacing(1);
		m_Slider[3].setPaintTicks(true);
		m_Slider[3].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[3] = val*0.05;
				iLabel[3].setText(":: " + df.format(fields[3]));

				// univ.getCanvas().repaint();

			}
		});
		m_Slider[3].setBackground(bk);
		in2.add(lab);
		in2.add(m_Slider[3]);
		in2.add(iLabel[3]);

		lab=new JLabel("Boundary",JLabel.LEFT);
		JComboBox Boundary_type = new JComboBox();
		Boundary_type.addItem("Fixed");
		Boundary_type.addItem("Simply Supported");
		Boundary_type.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				valChange = true;
				JComboBox cb = (JComboBox) e.getSource();
				boundary = (String) cb.getSelectedItem();
				valChange = true;
				resetOutputParameters();
				repaint();
			}
		});
		in3.add(lab);
		in3.add(Boundary_type);
		lab1=new JLabel(" ", JLabel.LEFT);
		in3.add(lab1);
		
		lab = new JLabel("Load",JLabel.LEFT);
		JComboBox Loading_type = new JComboBox();
		Loading_type.addItem("Point Load");
		Loading_type.addItem("UDL");
		Loading_type.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				valChange = true;
				JComboBox cb = (JComboBox) e.getSource();
				obj = (String) cb.getSelectedItem();
				System.out.println("Loading Type is:"+obj);
				valChange = true;
				resetOutputParameters();
				repaint();

			}
		});
		in3.add(lab);
		in3.add(Loading_type);
		lab1=new JLabel(" ", JLabel.LEFT);
		in3.add(lab1);

		lab = new JLabel("UDL (KN)", JLabel.LEFT);
		m_Slider[4] = new JSlider(JSlider.HORIZONTAL, 1, 50, 5);
		m_Slider[4].setMajorTickSpacing(49);
		m_Slider[4].setMinorTickSpacing(10);
		m_Slider[4].setPaintTicks(true);
		m_Slider[4].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[4] = val;

				iLabel[4].setText(":: " + fields[4] + "KN");
				repaint();
			}
		});
		m_Slider[4].setBackground(bk);
		in4.add(lab);
		in4.add(m_Slider[4]);
		in4.add(iLabel[4]);

		lab = new JLabel("POINT LOAD (KN)", JLabel.LEFT);
		m_Slider[5] = new JSlider(JSlider.HORIZONTAL, 1, 100, 5);
		m_Slider[5].setMajorTickSpacing(99);
		m_Slider[5].setMinorTickSpacing(25);
		m_Slider[5].setPaintTicks(true);
		m_Slider[5].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				int val = ((JSlider) e.getSource()).getValue();
				fields[5] = val;

				iLabel[5].setText(":: " + fields[5] + "KN");
				repaint();
			}
		});
		m_Slider[5].setBackground(bk);
		in4.add(lab);
		in4.add(m_Slider[5]);
		in4.add(iLabel[5]);

		lab = new JLabel("X ", JLabel.LEFT);
		//Slider is defined before slider[0]
		m_Slider[6].setMajorTickSpacing((int)(fields[0]*10));
		m_Slider[6].setMinorTickSpacing(10);
		m_Slider[6].setPaintTicks(true);
		m_Slider[6].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				((JSlider) e.getSource()).setMaximum((int)fields[0]*10);
				int val = ((JSlider) e.getSource()).getValue();
				fields[6] = val/10.0;
				iLabel[6].setText(":: " + fields[6] + " m");
				repaint();
			}
		});
		m_Slider[6].setBackground(bk);
		in4.add(lab);
		in4.add(m_Slider[6]);
		in4.add(iLabel[6]);
		
		lab = new JLabel("Y ", JLabel.LEFT);
		//Slider is defined before slider[1]
		m_Slider[7].setMajorTickSpacing((int)fields[1]*10);
		m_Slider[7].setMinorTickSpacing(10);
		m_Slider[7].setPaintTicks(true);
		m_Slider[7].addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				valChange = true;
				((JSlider) e.getSource()).setMaximum((int)fields[1]*10);
				int val = ((JSlider) e.getSource()).getValue();
				fields[7] = val/10.0;

				iLabel[7].setText(":: " + fields[7] + " m");
				repaint();
			}
		});
		m_Slider[7].setBackground(bk);
		in4.add(lab);
		in4.add(m_Slider[7]);
		in4.add(iLabel[7]);
		
		bottomPanel.setVisible(false);
	}

	private int getiSubVal(String obj) {

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
	}

	private void initInputControlsField() {

		iLabel = new JLabel[9];
		int i = 0;
		iLabel[i] = new JLabel("1 m", JLabel.LEFT);  //0
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel("1 m", JLabel.LEFT); //1
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel("100 mm", JLabel.LEFT); //2
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel("0.3", JLabel.LEFT); //3
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel("5 KN", JLabel.LEFT); //4
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel("5 KN", JLabel.LEFT); //5
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel("1 m", JLabel.LEFT); //6
		iLabel[i++].setForeground(Color.blue);
		iLabel[i] = new JLabel("1 m", JLabel.LEFT); //7
		iLabel[i++].setForeground(Color.blue);

		i = 0;
		fields = new double[8];
		fields[0] = 1.0;
		fields[1] = 1.0;
		fields[2] = 100.0;
		fields[3] = 0.3;
		fields[4] = 5.0;
		fields[5] = 5.0;
		fields[6] = 1.0;
		fields[7] = 1.0;
		D = E*0.001/(12*(1-fields[3]));
		System.out.println("D:"+D);
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
			enable(in4,false);
			break;

		case 1:

			enable(in1, true);
			enable(in2, true);
			enable(in3, true);
			enable(in4, true);
			break;

		case 2:

			enable(in1, true);
			enable(in2, true);
			enable(in3, true);
			enable(in4, true);
			break;

		case 3:

			enable(in1, true);
			enable(in2, true);
			enable(in3, true);
			enable(in4, true);
			break;

		case 4:

			enable(in1, true);
			enable(in2, true);
			enable(in3, true);
			enable(in4, true);
			nextButton.setVisible(false);
			break;

		}
		resetOutputParameters();
	}

	private void setInstructionText() {

		valChange = true;
		resetOutputParameters();

		switch (stage) {
		case 0: 
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
		if(in4.isEnabled())
		{
			if(obj.equals("Point Load"))
			{
				m_Slider[4].setEnabled(false);
				m_Slider[5].setEnabled(true);
				m_Slider[6].setEnabled(true);
				m_Slider[7].setEnabled(true);
			}
			else if(obj.equals("UDL"))
			{
				m_Slider[4].setEnabled(true);
				m_Slider[5].setEnabled(false);
				m_Slider[6].setEnabled(false);
				m_Slider[7].setEnabled(false);
			}
		}
		else
		{
			m_Slider[4].setEnabled(false);
			m_Slider[5].setEnabled(false);
			m_Slider[6].setEnabled(false);
			m_Slider[7].setEnabled(false);
		}

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

//		set states of input graph and output graph
/*		outputGraph.setState(1);
		inputGraph.setState(1);		*/

		if (valChange) {
			System.out.println(obj);
			System.out.println("Length"+fields[0]);
			System.out.println("Breadth"+fields[1]);
			System.out.println("Thickness"+fields[2]);
			
			if(obj.equals("Point Load"))
				System.out.println("Force:"+fields[4]);
			else
				System.out.println("Force:"+fields[4]);
			
			System.out.println("Force"+fields[5]);
			System.out.println("a"+fields[6]);
			System.out.println("b"+fields[7]);

			int small = getiSubVal(obj);
			System.out.println(small);


			inputGraph.clearGraphValue();
			outputGraph.clearGraphValue();	
			D=E* Math.pow(fields[2]/1000, 3) / (12 * (1-fields[3]));
			System.out.println("D:"+D);
		}
		calculateDeflections(fields[0],fields[1]);
		timer.start();
		System.out.println("Timer started");
	}

	private void timerActionPerformed(java.awt.event.ActionEvent evt){		
		stage=(stage+1)%10;
		int i;
		double rad = (double)Math.PI/180;
		Transform3D t1;
		//Scale the beam 
		Transform3D t = new Transform3D();
		//t.setScale(new Vector3d(fields[0]/4,fields[2]/400,fields[1]/4));
		t.setScale(new Vector3d(fields[0]/4,1,fields[1]/4));
		beam.setTransform(t);
		
		Vector3d scale=new Vector3d(0.02,0.02,1);
		Vector3d rot = new Vector3d(0,0,180);
		Vector3d pos;
		
		double X,Y;
		double deflection;
		int k;
		if(boundary.equals("Simply Supported"))
		{
			if(obj.equals("Point Load"))
			{
				//Translating Arrows
				t1=new Transform3D();
				pos = new Vector3d(fields[6]*0.44/fields[0] - 0.22 ,0.02-stage*0.005, -fields[7]*0.44/fields[1] + 0.22);
				t1.rotZ(rad*rot.z);
				t1.setScale(scale);        
				t1.setTranslation(pos);
				arrow.setTransform(t1);

				//Translating Uniform load arrows out of visibility
				for(i=0;i<5;i++)
				{
					t = new Transform3D();
					pos = new Vector3d(4.0,0.02-stage*0.005,-fields[7]*0.44/fields[1] + 0.22);	
					t.rotZ(rad*rot.z);
					t.setScale(scale);        
					t.setTranslation(pos);
					arrows_udl[i].setTransform(t);
				}
				//Adding new branch
				k=0;
				for(X=-0.22;X<=0.22;X+=0.02)
				{
					for(Y = -0.22; Y <= 0.22; Y += 0.02)
					{
						t=new Transform3D();
						t.setTranslation(new Vector3d(X,-defl[k]*(stage/9),Y));
						t.setScale(new Vector3d(0.02,0.045,0.045));
						cubes[k].setTransform(t);
						k++;
					}
				}
				//updating body when timer occurs;
		        for(i=0;i<2420;i++)
		        {
		        	contourpoints[i].setY(contourpoints_copy[i].getY()*(stage/9.0));
		        }    
		        updateSimulationBody();
			}
			else
			{
				//Translating Arrows
				t1=new Transform3D();
				pos = new Vector3d(4.0 ,0.02-stage*0.005, -fields[7]*0.44/fields[1] + 0.22);
				t1.rotZ(rad*rot.z);
				t1.setScale(scale);        
				t1.setTranslation(pos);
				arrow.setTransform(t1);

				//Translating Uniform load arrows out of visibility
				for(i=0;i<5;i++)
				{
					t = new Transform3D();
					pos = new Vector3d((i-2)*0.1,0.02-stage*0.005, -fields[7]*0.44/fields[1] + 0.22);	
					t.rotZ(rad*rot.z);
					t.setScale(scale);        
					t.setTranslation(pos);
					arrows_udl[i].setTransform(t);
				}
				//Translating Beam
			    k=0;
				for(X=-0.22;X<=0.22;X+=0.02)
				{
					for(Y = -0.22; Y <= 0.22; Y += 0.02)
					{
						t=new Transform3D();
						t.setTranslation(new Vector3d(X,-defl[k]*(stage/9.0),Y));
						t.setScale(new Vector3d(0.02,0.045,0.045));
						cubes[k].setTransform(t);
						k++;
					}
				}
				//updating body when timer occurs;
		        for(i=0;i<2420;i++)
		        {
		        	contourpoints[i].setY(contourpoints_copy[i].getY()*(stage/9.0));
		        }    
		        updateSimulationBody();
			}
		}

	 
		return;
	}

	private void updateSimulationBody(){
		float rad = (float) Math.PI / 180;
		Transform3D trans = new Transform3D();
		TransformGroup bg = (TransformGroup) hm.get("contourbg");
		bg.removeChild(1);
		bg.addChild(createContour());
		hm.put("contourbg", bg);
	}

	private void pauseSimulation() {
		System.out.println("Pause Pressed!!!");
		timer.stop();
		ImageIcon icon = m_j3d.createImageIcon("resources/icons/start.png");
		startButton.setIcon(icon);
		startButton.setText("Start");
		reStartButton.setEnabled(true);
		bottomPanel.setVisible(true);
		nextButton.setEnabled(true);

		rightPanel.setVisible(true);
		enableStage(3);
		outputGraph.setState(0);
		inputGraph.setState(0);
		// startButton.setEnabled(true);

		valChange = false;

		repaint();
	}
	private double getDeflection(double x,double y)
	{
		double Lx,Ly,E,p,a,b,def;
		Lx=fields[0];    //length
		Ly=fields[1];  	 //breadth
		a=fields[6];     // X coordinate of Point Load
		b=fields[7];     // Y coordinate of Point Load
		int m,n;
		float temp=0;
		if(obj.equals("Point Load"))
		{
			p=fields[5]*1000;
			def=(4 * p) /(Math.PI*Math.PI*Math.PI*Math.PI * D * Lx * Ly);
			if(x==3.16 && y==0.18)
				System.out.println("Const:"+def);
			for(m=1;m<=5;m+=2)					
				for(n=1;n<=5;n+=2)
					temp+=(Math.sin(m*Math.PI*x/Lx) * Math.sin(n*Math.PI*y/Ly) * Math.sin(m*Math.PI*a/Lx) * Math.sin(n*Math.PI*b/Ly) ) / (  Math.pow(((m*m)/(Lx*Lx) + (n*n)/(Ly * Ly)),2));
			if(x==3.16 && y==0.18)
				System.out.println("Temp:"+temp);
			def=def*temp;		
		}
		else
		{
			p=fields[4]*1000;
			def=(16.0 * p) / (Math.pow(Math.PI,6) * D) ;
			for(m=1;m<=5;m+=2)					
				for(n=1;n<=5;n+=2)
					temp+=(Math.sin(m*Math.PI*x/Lx) * Math.sin(n*Math.PI*y/Ly)) / ( m*n*Math.pow(((m*m)/(Lx*Lx) + (n*n)/(Ly * Ly)),2));
			def=def*temp;
		}
		return def;
	}
}
