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
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;



import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import eerc.vlab.common.FullViewGraph;
import eerc.vlab.common.HelpWindow;
import eerc.vlab.common.HorizontalGraph;
import eerc.vlab.common.J3DShape;
import eerc.vlab.common.Resources;
/**
 * Simple Java 3D program that can be run as an application or as an applet.
 */
@SuppressWarnings({ "serial", "unused" })
public class ColumnAnalysis extends javax.swing.JPanel {
	//  Variables declaration - do not modify//GEN-BEGIN:variables
	//////////////////////////GUI componenet ///////////////////////////
	private javax.swing.JPanel topPanel;
	private javax.swing.JPanel simulationPanel;
	private javax.swing.JPanel bottomPanel;
	private javax.swing.JPanel rightPanel;
	  
	private javax.swing.JPanel in1;			// Input panel 1
	private javax.swing.JPanel in2;			// Input panel 2
	private javax.swing.JPanel in3;			// Input panel 3
	
	private javax.swing.JButton startButton=null;
	private javax.swing.JButton reStartButton=null;
	private javax.swing.JButton nextButton=null;

	String objImg,safty_factor="1",materialGrade;
	private javax.swing.JButton rightIcon=null;
	

	//private GraphPlotter         graphPlotter;
	////////////////////////////Java3D componenet ///////////////////////////

	private SimpleUniverse      univ = null;                  // Simple Universe Java3D
	private BranchGroup         scene = null;                 // BranchGroup Scene graph
	private Switch 				switchGroup=null;
	private Switch 				switchGroup1=null;
	private Switch 				switchGroup2=null;
	private Switch 				switchGroup3=null;
	private Switch 				switchGroup4=null;
	private Switch 				switchGroup5=null;
	private Switch 				switchGroup6=null;
	private Switch 				switchGroup7=null;

	


	private ColumnAnalysisBody      freeBody =null;               // Shape3D
	private HorizontalGraph		outputGraph =null;
	private HorizontalGraph		inputGraph =null;
	private FullViewGraph  		fullViewGraph = new FullViewGraph();
	

	@SuppressWarnings("unchecked")
	private HashMap 			hm = new HashMap();
	private J3DShape 			m_j3d	= new J3DShape();

	private double[] fields;
	private JLabel outlbl_val[]=new JLabel[4];
	private JLabel iLabel[];
	private JLabel m_Objective= new JLabel("Objective:");
	
	private Timer timer=null;
	private Timer m_cameraTimer=null; 
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
	private JSlider m_Slider[] = new JSlider[3];
	private JLabel out_lbl[]=new JLabel[5];
	String obj_f;
	 String[] cement = new String[5];
	 String[] cement1 = new String[3];

	private String obj,BOS;
	
	
    int flag=0,val=20;
    JLabel len;

	private JComboBox End_Conditions,Column_Mat,Material_Grade,Fac_Of_Safty;
	
 
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
		

	//	objRoot.addChild(m_j3d.createBox(new Vector3d(0,-0.25, -.1),new Vector3d(3,.01,1),new Vector3d(0,0,0),new Color3f(0f, 1f, 0f),"resources/images/table.jpg"));
	//	objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.4, -.6),new Vector3d(3,.9,.1),new Vector3d(0f, 0f,0f), new Color3f(0.5f,0.6f,0.72f)));
		
		
		float rad = (float)Math.PI/180;
		Transform3D t = new Transform3D();
		t.setScale(0.4);
		
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
        
        
        ViewingPlatform vp = univ.getViewingPlatform();
	    TransformGroup steerTG = vp.getViewPlatformTransform();
	    Transform3D t3d = new Transform3D();
	    steerTG.getTransform(t3d);
	    Vector3d s = new Vector3d();
	    Vector3f currPos=new Vector3f();
	    t3d.get(currPos); 
	    t3d.lookAt( new Point3d(0,0.4, 2.81 ), new Point3d(0,0,0), new Vector3d(0,1,0));
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
	    switchGroup = new Switch( Switch.CHILD_MASK );
	    switchGroup.setCapability( Switch.ALLOW_SWITCH_WRITE );
	    objtrans.addChild(switchGroup);
	    switchGroup1 = new Switch( Switch.CHILD_MASK );
	    switchGroup1.setCapability( Switch.ALLOW_SWITCH_WRITE );
	    objtrans.addChild(switchGroup1);
	    switchGroup2 = new Switch( Switch.CHILD_MASK );
	    switchGroup2.setCapability( Switch.ALLOW_SWITCH_WRITE );
	    objtrans.addChild(switchGroup2);
	    
	    
	    switchGroup3 = new Switch( Switch.CHILD_MASK );
	    switchGroup3.setCapability( Switch.ALLOW_SWITCH_WRITE );
	    objtrans.addChild(switchGroup3);
	    
	    
	    switchGroup4 = new Switch( Switch.CHILD_MASK );
	    switchGroup4.setCapability( Switch.ALLOW_SWITCH_WRITE );
	    objtrans.addChild(switchGroup4);
	    switchGroup5 = new Switch( Switch.CHILD_MASK );
	    switchGroup5.setCapability( Switch.ALLOW_SWITCH_WRITE );
	    objtrans.addChild(switchGroup5);
	    
	    switchGroup6 = new Switch( Switch.CHILD_MASK );
	    switchGroup6.setCapability( Switch.ALLOW_SWITCH_WRITE );
	    objtrans.addChild(switchGroup6);
	    switchGroup7 = new Switch( Switch.CHILD_MASK );
	    switchGroup7.setCapability( Switch.ALLOW_SWITCH_WRITE );
	    objtrans.addChild(switchGroup7);
	    
	    switchGroup1.addChild(m_j3d.createText2D("PINNED",new Vector3d(-0.46,0.259, 0.2),new Vector3d(0.4,0.4,0.4),new Color3f(1f,1f, 0f),16,Font.BOLD ));
	    switchGroup1.addChild(m_j3d.createText2D("PINNED",new Vector3d(-0.46,-0.22, 0.2),new Vector3d(0.4,0.4,0.4),new Color3f(1f,1f, 0f),16,Font.BOLD ));
	    switchGroup1.addChild(m_j3d.createText2D("PINNED",new Vector3d(0.34,-0.22, 0.2),new Vector3d(0.4,0.4,0.4),new Color3f(1f,1f, 0f),16,Font.BOLD ));
	    
	    switchGroup1.addChild(m_j3d.createText2D("FREE",new Vector3d(-0.44,0.259, 0.2),new Vector3d(0.4,0.4,0.4),new Color3f(1f,1f, 0f),16,Font.BOLD ));
	    switchGroup1.addChild(m_j3d.createText2D("FIXED",new Vector3d(-0.44,-0.22, 0.2),new Vector3d(0.4,0.4,0.4),new Color3f(1f,1f, 0f),16,Font.BOLD ));
	    switchGroup1.addChild(m_j3d.createText2D("FIXED",new Vector3d(0.36,-0.22, 0.2),new Vector3d(0.4,0.4,0.4),new Color3f(1f,1f, 0f),16,Font.BOLD ));
	    
	    switchGroup1.addChild(m_j3d.createText2D("PINNED",new Vector3d(-0.46,0.259, 0.2),new Vector3d(0.4,0.4,0.4),new Color3f(1f,1f, 0f),16,Font.BOLD ));
	    switchGroup1.addChild(m_j3d.createText2D("FIXED",new Vector3d(-0.46,-0.22, 0.2),new Vector3d(0.4,0.4,0.4),new Color3f(1f,1f, 0f),16,Font.BOLD ));
	    switchGroup1.addChild(m_j3d.createText2D("FIXED",new Vector3d(0.34,-0.22, 0.2),new Vector3d(0.4,0.4,0.4),new Color3f(1f,1f, 0f),16,Font.BOLD ));
	    
	    switchGroup1.addChild(m_j3d.createText2D("FIXED",new Vector3d(-0.46,0.259, 0.2),new Vector3d(0.4,0.4,0.4),new Color3f(1f,1f, 0f),16,Font.BOLD ));
	    switchGroup1.addChild(m_j3d.createText2D("FIXED",new Vector3d(-0.46,-0.22, 0.2),new Vector3d(0.4,0.4,0.4),new Color3f(1f,1f, 0f),16,Font.BOLD ));
	    switchGroup1.addChild(m_j3d.createText2D("FIXED",new Vector3d(0.34,-0.22, 0.2),new Vector3d(0.4,0.4,0.4),new Color3f(1f,1f, 0f),16,Font.BOLD ));
	    switchGroup1.addChild(m_j3d.createText2D("FREE",new Vector3d(0.34,0.259, 0.2),new Vector3d(0.4,0.4,0.4),new Color3f(0f,0f, 1f),16,Font.BOLD ));
	    
	    
	    
	    
	    java.util.BitSet visibleNodes = new java.util.BitSet( switchGroup1.numChildren() );
	 	   visibleNodes.set( 0); 
	 	  visibleNodes.set( 1);
	 	
	 	   
	 	  switchGroup1.setChildMask( visibleNodes ); // Text top bottom and bottom text

		    TransformGroup Middle = new TransformGroup(t);  //for middle in Second
		    Middle.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    Middle.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    
		    
		   
		    
	    TransformGroup objtrans2 = new TransformGroup(t);
	    objtrans2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    objtrans2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    TransformGroup objtrans3 = new TransformGroup(t);
	    objtrans3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    objtrans3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    TransformGroup objtrans4 = new TransformGroup(t);
	    objtrans4.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    objtrans4.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    TransformGroup objtrans5 = new TransformGroup(t);
	    objtrans5.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    objtrans5.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    TransformGroup objtrans6 = new TransformGroup(t);
	    objtrans6.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    objtrans6.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    TransformGroup objtrans7 = new TransformGroup(t);
	    objtrans6.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    objtrans6.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	   
	    TransformGroup obj[] =  new TransformGroup[20];
	    for(int i=0;i<20;i++)
	    {
	    	  obj[i]= new TransformGroup(t);
	    	 obj[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	 	    obj[i].setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	 	    if(i<=6)
	 	    {
	 	    	switchGroup5.addChild(obj[i]);
	 	    	
	 	    }
	 	    if(i>=8 && i<=13)
	 	    {
	 	    	switchGroup6.addChild(obj[i]);
	 	    	
	 	    }
	 	    if(i>13 )
	 	    	switchGroup7.addChild(obj[i]);
	 	    	
	    	
	    }
	    
	    
	    Transform3D t3 = new Transform3D();
       t3.setTranslation(new Vector3d(0.4,0.272, 0.1));
	    
	    TransformGroup objtrans1 = new TransformGroup(t3);
	    objtrans1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    objtrans1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    
	    switchGroup.addChild(objtrans7);
	    switchGroup.addChild(objtrans6);
	    switchGroup.addChild(objtrans2);
	    switchGroup.addChild(objtrans3);
	    switchGroup.addChild(objtrans4);
	    switchGroup.addChild(objtrans5);
	
	    
	   	    
	     //Block Center
	    float left_end =-0.39f;
	    int count=0;
	    //First 
	    objtrans.addChild(m_j3d.createBox(new Vector3d(-0.4,-0.2, 0.1),new Vector3d(.1,.021,.1),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/grey4.jpg"));
	   
	    for(float i=0; i <0.4; i+=0.01)
	    {
	    	count++;
	    objtrans.addChild(m_j3d.createBox(new Vector3d(-0.4,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
	    Middle.addChild(m_j3d.createBox(new Vector3d(0.4,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
	    }
	    switchGroup2.addChild(Middle); //middle one 
	    
	    
	    
	
	    //objtrans.addChild( m_j3d.createTextureBox(new Vector3d(-0.4,0.3, 0.13),new Vector3d(.2,.03,.2),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/stone.jpg","roof1",hm));
	    objtrans.addChild(m_j3d.createBox(new Vector3d(-0.4,0.272, 0.1),new Vector3d(.1,.021,.1),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/grey4.jpg"));
	    
	   //objtrans.addChild(m_j3d.createTextureBox(new Vector3d(0,0, -.07),new Vector3d(.45,.04,.02),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/tile3.jpg","roof1",hm));
	    
	    switchGroup4.addChild(m_j3d.createBox(new Vector3d(0.4,-0.2, 0.1),new Vector3d(.1,.021,.1),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/grey4.jpg"));
	   
	    
	    objtrans1.addChild(m_j3d.createBox(new Vector3d(0,0, 0),new Vector3d(.1,.021,.1),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/grey4.jpg"));
	    objtrans1.addChild(m_j3d.createText2D("PINNED",new Vector3d(0.34-0.4,0.259-0.272, 0.22-0.1),new Vector3d(0.4,0.4,0.4),new Color3f(0f,0f, 1f),16,Font.BOLD ));
	    objtrans1.addChild(m_j3d.createLine1(new Point3d(0,0.3-0.272, 0),new Point3d(0,0.35-0.272,0),new Vector3d(0,0,0),new Vector3d(1,1,1),new Color3f(1f,1f,0),2));
	    objtrans1.addChild(createTriangle(new Point3d(0,0.29-0.272, 0),new Point3d(0.41-0.4,0.305-0.272, 0),new Point3d(0.39-0.4,0.305-0.272,0),new Vector3d(1,1,1),new Vector3d(0,0,0),new Color3f(1f,1f,0)));
	    hm.put("target1",objtrans1 );
	    
	    switchGroup3.addChild(objtrans1);
	    
	    
	    TransformGroup objtrans12 = new TransformGroup(t3);
	    objtrans12.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    objtrans12.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    objtrans12.addChild(m_j3d.createBox(new Vector3d(0,0, 0),new Vector3d(.1,.021,.1),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/grey4.jpg"));
	    objtrans12.addChild(m_j3d.createText2D("FIXED",new Vector3d(0.34-0.4,0.259-0.272, 0.22-0.1),new Vector3d(0.4,0.4,0.4),new Color3f(1f,0f, 1f),16,Font.BOLD ));
	    objtrans12.addChild(m_j3d.createLine1(new Point3d(0,0.29-0.272, 0),new Point3d(0,0.35-0.272,0),new Vector3d(0,0,0),new Vector3d(1,1,1),new Color3f(1f,1f,0),4));
	    objtrans12.addChild(createTriangle(new Point3d(0,0.29-0.272, 0),new Point3d(0.41-0.4,0.305-0.272, 0),new Point3d(0.39-0.4,0.305-0.272,0),new Vector3d(1,1,1),new Vector3d(0,0,0),new Color3f(1f,1f,0)));
	    switchGroup3.addChild(objtrans12);
	    hm.put("target2",objtrans12 );
	    
	    
	    
	   
	  
	    
	 	
	 	
	    
	
	    float radius =0.1f;
	   float x1_center =0.24f;
	   float y1_center=0.4f;
	   
	   
	    
	    
		 
	    for(float i=0; i <0.4; i+=0.01)
	    {
	    	count++;
	    	freeBody = new ColumnAnalysisBody();
	    	freeBody.init(6, 250, 250, 20,2, 1);
	    	double x=0;
	    	
	    	 
	    	 count++;
		    	
		    	
		    	int flag=0;
		    	
		       if(i<0.37)
		       {
		    	   x =freeBody.getY(1,200,i*6000/0.37);
		    	   x=x/4000;
		    	   objtrans5.addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		       }
		       if(i<0.375)
		       {
		    		 x =freeBody.getY(1,200,i*6000/0.375);
		    		 x=x/5000;
		    		 objtrans4.addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		    		 
		       }
		       if(i<0.38)
		       {
		    		 x =freeBody.getY(1,200,i*6000/0.38);
			    	 x=x/6000;
		    		 objtrans3.addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		       }
		       if(i<0.385)
		       {
		    		 x =freeBody.getY(1,200,i*6000/0.385);
			    	 x=x/7000;
		    		 objtrans2.addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		       }
		       if(i<0.39)
		       {
		    	   x =freeBody.getY(1,200,i*6000/0.39);
			    	 x=x/8000;
		    		 objtrans6.addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		    	   
		       }
		       if(i<0.395)
		       {
		    	   
		    	   x =freeBody.getY(1,200,i*6000/0.39);
			    	 x=x/9000;
		    		 objtrans7.addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		       }
		    		 
		    		 
		    		 
		    	
		    	
		    	
		    		//switchGroup.addChild(m_j3d.createTextureBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		    	
		    	
		    	
		    		x =freeBody.getY(2,200,i*6000/0.4);
		    		obj[0].addChild(m_j3d.createBox(new Vector3d(0.4+i*i*0.2,-0.16+i, .13),new Vector3d(.02,.005,.04),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		    		obj[1].addChild(m_j3d.createBox(new Vector3d(0.4+i*i*0.3,-0.16+i, .13),new Vector3d(.02,.005,.04),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		    		obj[2].addChild(m_j3d.createBox(new Vector3d(0.4+i*i*0.4,-0.16+i, .13),new Vector3d(.02,.005,.04),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		    		obj[3].addChild(m_j3d.createBox(new Vector3d(0.4+i*i*0.5,-0.16+i, .13),new Vector3d(.02,.005,.04),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		    		obj[4].addChild(m_j3d.createBox(new Vector3d(0.4+i*i*0.7,-0.16+i, .13),new Vector3d(.02,.005,.04),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		    		obj[5].addChild(m_j3d.createBox(new Vector3d(0.4+i*i*0.8,-0.16+i, .13),new Vector3d(.02,.005,.04),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		    		obj[6].addChild(m_j3d.createBox(new Vector3d(0.4+i*i*1.1,-0.16+i, .13),new Vector3d(.02,.005,.04),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		    		
		    		
		    		
		    	
		    	
		    	
		    		x =freeBody.getY(3,200,i*6000/0.4);
		    		x=x/2000;
		    		if(i<0.01)
			    	{
			    		x=0;
			    		obj[8].addChild(m_j3d.createBox(new Vector3d(0.404,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
			    		obj[9].addChild(m_j3d.createBox(new Vector3d(0.408,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
			    		obj[10].addChild(m_j3d.createBox(new Vector3d(0.408,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
			    		obj[11].addChild(m_j3d.createBox(new Vector3d(0.408,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
			    		obj[12].addChild(m_j3d.createBox(new Vector3d(0.411,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
			    		obj[13].addChild(m_j3d.createBox(new Vector3d(0.411,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
			    		
			    		x =freeBody.getY(4,200,i*6000/0.4);
			    	     x=x/2000;
		 	              obj[19].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 	               
		 	             x =freeBody.getY(4,200,i*6000/0.4);
			    	     x=x/2500;
		 	              obj[18].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 	             x =freeBody.getY(4,200,i*6000/0.4);
			    	     x=x/3000;
		 	              obj[17].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 	             x =freeBody.getY(4,200,i*6000/0.4);
			    	     x=x/4000;
		 	              obj[16].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 	             x =freeBody.getY(4,200,i*6000/0.4);
			    	     x=x/5000;
		 	              obj[15].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 	             x =freeBody.getY(4,200,i*6000/0.4);
			    	     x=x/5500;
		 	              obj[14].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 	              
			    		
			    		
			    		
			    	}
		    		else if(i>0.018)
		    		{

		 		       if(i<0.37)
		 		       {
		 		    	   x =freeBody.getY(3,200,i*6000/0.37);
		 		    	   x=x/2000;
		 		    	   obj[13].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 		    	   
		 		    	   
		 	                x =freeBody.getY(4,200,i*6000/0.37);
		 	                x=x/2000;
		 	               obj[19].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 	               
		 		       }
		 		       if(i<0.375)
		 		       {
		 		    		 x =freeBody.getY(3,200,i*6000/0.375);
		 		    		 x=x/2500;
		 		    		 obj[12].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 		    		x =freeBody.getY(4,200,i*6000/0.375);
		 		    		 x=x/2500;
		 		    		 obj[18].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 		       }
		 		       if(i<0.38)
		 		       {
		 		    		 x =freeBody.getY(3,200,i*6000/0.38);
		 			    	 x=x/3000;
		 		    		 obj[11].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 		       
		 		    		 
		 		    		 x =freeBody.getY(4,200,i*6000/0.38);
		 			    	 x=x/3000;
		 		    		 obj[17].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 		    		 
		 		       }
		 		       if(i<0.385)
		 		       {
		 		    		 x =freeBody.getY(3,200,i*6000/0.385);
		 			    	 x=x/4000;
		 		    		 obj[10].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 		    		 
		 		    		 x =freeBody.getY(4,200,i*6000/0.385);
		 			    	 x=x/4000;
		 		    		 obj[16].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 		       }
		 		       if(i<0.39)
		 		       {
		 		    	   x =freeBody.getY(3,200,i*6000/0.39);
		 			    	 x=x/5000;
		 		    		 obj[9].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 		    	   
		 		    		x =freeBody.getY(4,200,i*6000/0.39);
		 			    	 x=x/5000;
		 		    		 obj[15].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 		       }
		 		       if(i<0.395)
		 		       {
		 		    	   
		 		    	   x =freeBody.getY(3,200,i*6000/0.39);
		 			    	 x=x/5500;
		 		    		 obj[8].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 		    		 
		 		    		 x =freeBody.getY(3,200,i*6000/0.39);
		 			    	 x=x/5500;
		 		    		 obj[14].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 		       
		 		       }
		    			
		    		}
		    		else
		    		{
		    			x =freeBody.getY(4,200,i*6000/0.37);
			    	     x=x/2000;
		 	              obj[19].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 	               
		 	             x =freeBody.getY(4,200,i*6000/0.375);
			    	     x=x/2500;
		 	              obj[18].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 	             x =freeBody.getY(4,200,i*6000/0.38);
			    	     x=x/3000;
		 	              obj[17].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 	             x =freeBody.getY(4,200,i*6000/0.385);
			    	     x=x/4000;
		 	              obj[16].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 	             x =freeBody.getY(4,200,i*6000/0.39);
			    	     x=x/5000;
		 	              obj[15].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 	             x =freeBody.getY(4,200,i*6000/0.395);
			    	     x=x/5500;
		 	              obj[14].addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		 	              
		    			
		    		}
		    		//objtrans4.addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		    	
		    	
		    		x =freeBody.getY(4,200,i*6000/0.4);
		    		x=x/2000;
		    		if(i<0.01)
			    	{
			    		x=0;
			    		//objtrans5.addChild(m_j3d.createBox(new Vector3d(0.416-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
			    	}
		    		
		    		else if(i>0.018)
		    		x=1;
		    			//objtrans5.addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		    		else if(i>0.37)
		    		{
		    			x=0;
		    			//objtrans5.addChild(m_j3d.createBox(new Vector3d(0.41-x,-0.16+i, .13),new Vector3d(.02,.005,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
		    		}
		    		
		    		
		    	
		    	
	
	    	 //x=x/4000; x=1
	    //	x=x/200000; //2
	    	//x=x/1000; x=3
	    	x=x/2000;//--> 3
	    	
	    	if(i<0.01)
	    	{
	    		x=0;
	    	}
	    	
	    	
	 	 
	 	 
	 	   
	   // objtrans.addChild(m_j3d.createBox(new Vector3d(0.4+i*i,-0.16+i, .13),new Vector3d(.03,.02,.04),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
	    	//objtrans.addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.02,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
	    	//
	    	//objtrans.addChild(m_j3d.createBox(new Vector3d(0.4-x,-0.16+i, .13),new Vector3d(.02,.02,.02),new Vector3d(0,0,0),new Color3f(0f, 0f, 1f)));
	    	
	    }
	    return objtrans;   
	}
     
   
     
     
    
    /**
     * Creates new form FreeVibration
     */
    public ColumnAnalysis(Container container) {
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
    	ColumnAnalysis mainPanel;

        public void init() {
            setLayout(new BorderLayout());
            mainPanel = new ColumnAnalysis(this);
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
            setTitle("Buckling of Columns");
            getContentPane().add(new ColumnAnalysis(this), BorderLayout.CENTER);
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
    

    private void initComponents() {
        
        

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
    	stage =0;
        
        timer = new Timer(400,new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //...Perform a task...
            	timerActionPerformed(evt);
            	timer.setInitialDelay(0);
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
//                
//                outputGraph.clearGraphValue();
//                inputGraph.clearGraphValue();
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
        
       
//        javax.swing.JButton btn= new javax.swing.JButton("Full View Graph");
//        guiPanel.add(btn, gridBagConstraints);
//        icon = m_j3d.createImageIcon("resources/icons/graph_window.png");        
//        btn.setIcon(icon);
//        btn.addActionListener(new java.awt.event.ActionListener() {
//            @SuppressWarnings("static-access")
//			public void actionPerformed(java.awt.event.ActionEvent evt) {                
// 
//            	HorizontalGraph graph[] ={outputGraph};
//             	int max[]={1000,100};
//             	int magX[]={2,2};
//             	int magY[]={2,2};
//            	
//            	JFrame frame = new JFrame("Full View Graph");
//            	//GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
//                //Add contents to the window.
//            	
//                //frame.add(p);
//            	frame.setExtendedState(frame.getExtendedState() | frame.MAXIMIZED_BOTH);
//
//                
//                frame.pack();
//                frame.setVisible(true);
//              
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
        
      javax.swing.JButton btn= new javax.swing.JButton("Manual"); 
      icon = m_j3d.createImageIcon("resources/icons/manual.png");        
      btn.setIcon(icon);
      //startButton.setPreferredSize(new Dimension(100,30));
     //   guiPanel.add(btn, gridBagConstraints);
        
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {                
            	
                HelpWindow.createAndShowGUI("forcedVib");
            }
          });

        
        
        
    }
    private void rightPanel() {
        
        
        rightPanel.setLayout(new java.awt.BorderLayout());
  
        JPanel panel = new JPanel();
        panel.setBackground(new Color(140,200,240));
        panel.setBorder(BorderFactory.createLineBorder(new Color(132,132,255),4));
        panel.setBorder(new EmptyBorder(10, 10, 0, 0));
      
        ImageIcon icon = m_j3d.createImageIcon("resources/CA/1.jpg"); 
      
       
        rightIcon=new javax.swing.JButton(" ");
        rightIcon.setIcon(icon);
        panel.add(rightIcon);
        
        panel.setPreferredSize(new Dimension(300,225));
    //    rightTop.setBackground(new Color(200,200,100));
        rightPanel.add(panel,BorderLayout.NORTH);

        JPanel rightBottom = new JPanel();
        rightBottom.setPreferredSize(new Dimension(300, 285));
        
  //      rightBottom.setBorder(new EmptyBorder(10, 10, 10, 10));
  //      rightBottom.setBackground(new Color(100,200,100));
        rightBottom = new JPanel(new java.awt.GridLayout(5,2));
        rightBottom.setBorder(BorderFactory.createLineBorder(new Color(140,200,240),8));
        
        JLabel lab1=new JLabel("RESULTS",JLabel.CENTER); 
        lab1.setFont(new Font("Arial", Font.BOLD, 17));
        lab1.setForeground(Color.blue);
        JLabel lab=new JLabel(" ",JLabel.CENTER);  
        rightBottom.add(lab1);
        rightBottom.add(lab);
        
        
        lab=new JLabel("Euler's Critical Load",JLabel.RIGHT);        
        outlbl_val[0]=new JLabel("0", JLabel.CENTER);;
        rightBottom.add(lab);
        rightBottom.add(outlbl_val[0]);
        
        lab=new JLabel("Rankine's Critical Load",JLabel.RIGHT);        
        outlbl_val[1]=new JLabel("0", JLabel.CENTER);
        rightBottom.add(lab);
        rightBottom.add(outlbl_val[1]);
        
        lab=new JLabel("Safe Axial Load",JLabel.RIGHT);        
        outlbl_val[2]=new JLabel("0", JLabel.CENTER);
        rightBottom.add(lab);
        rightBottom.add(outlbl_val[2]);
        
       
        
        lab=new JLabel("Slenderness Ratio",JLabel.RIGHT);        
        outlbl_val[3]=new JLabel("0", JLabel.CENTER);
        rightBottom.add(lab);
        rightBottom.add(outlbl_val[3]);
        
       
        
        
       
        rightPanel.add(rightBottom,BorderLayout.CENTER);
        
                
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
         JLabel lbl = new JLabel("Buckling of Columns", JLabel.CENTER);
         lbl.setFont(new Font("Arial", Font.BOLD, 18));

         lbl.setForeground(Color.orange);
        
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
         
     
    
         
         m_Objective = new JLabel(" ", JLabel.LEFT);
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
         guiPanel.setBorder(BorderFactory.createLineBorder(new Color(140,200,240),8));
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
//                                	 timerActionVerticalCameraMotion(evt);
//                     }
//                 });
//            	 m_cameraTimer.start();
//            	 
//             }
//           });
//         
//         guiPanel.add(viewButton, gridBagConstraints);
         
         JCheckBox chkbox = new JCheckBox("");
         lbl = new JLabel("Change Input Parameters", JLabel.CENTER);
         ImageIcon icon = m_j3d.createImageIcon("resources/icons/tasklist.png");        
         lbl.setIcon(icon);
         chkbox.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent event){                               
                     boolean bChecked =((JCheckBox)event.getSource()).isSelected();
                     if(bChecked)
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
      //   guiPanel.add(createInputOutputPanel());
       //  btmPanel.add(guiPanel,BorderLayout.SOUTH);
         
        

    }
    private String getImg() {
    	if(objImg=="Both Ends Pinned")
			{
    		    String str="resources/CA/1.jpg";
    		    rightIcon.setIcon(m_j3d.createImageIcon(str)); 
				return str;
				
			}
			else if(objImg=="One End Fixed Other End Free")
			{
				String str="resources/CA/2.jpg";
				rightIcon.setIcon(m_j3d.createImageIcon(str)); 
				return str;
			}
			else if(objImg=="Both ends Fixed")
			{
				String str="resources/CA/3.jpg";
				rightIcon.setIcon(m_j3d.createImageIcon(str)); 
				return str;
				
			}
			else if(objImg=="One end Fixed Other End Pinned")
			{
				String str="resources/CA/4.jpg";
				rightIcon.setIcon(m_j3d.createImageIcon(str));
				return str;
				
			}			
			else 
				return "resources/CA/1.jpg";
	}
    
    

    private void bottomPanel()
    {
    	   initInputControlsField();
    	  
    	   Color bk = new Color(219,226,238);
           bottomPanel.setLayout(new java.awt.GridLayout(1,3));
           bottomPanel.setBackground(Color.black);
           bottomPanel.setPreferredSize(new java.awt.Dimension(1024, 120));
           bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(235,233,215),8));
           
           in1 = new JPanel(new java.awt.GridLayout(3,3,10,10));
           in1.setBackground(bk);
           bottomPanel.add(in1);

           in2 = new JPanel(new java.awt.GridLayout(3,2,10,10)); 
           in2.setBackground(bk);
           bottomPanel.add(in2);

           in3 = new JPanel(new java.awt.GridLayout(2,2,10,47)); 
           in3.setBackground(bk);
           bottomPanel.add(in3);
         
           cementGradeList = new JComboBox();   
           cementGradeList.setEditable(true);

           JLabel lab;
           cement[0]="M" + 25;
		   cement[1]="M" + 20;
		   cement[2]="M" + 30;
		   cement[3]="M" + 35;
		   cement[4]="M" + 40;
		//   cement[5]="M" + 25;
		         
		         
//		   cement1[0]="Fe" + 200;
//		   cement1[1]="Fe" + 415;
//		   cement1[2]="Fe" + 500;
		       
	//	   cementGradeList=new JComboBox(cement);

           
           lab=new JLabel("Length", JLabel.RIGHT);
           m_Slider[0] = new JSlider(JSlider.HORIZONTAL,6, 10, 6);
           m_Slider[0].addChangeListener(new ChangeListener() {
   		    public void stateChanged(ChangeEvent e) {
               valChange = true;
               int val = ((JSlider) e.getSource()).getValue();
               fields[0]=val;
         
               iLabel[0].setText(":: " + fields[0] + " m");
               getEL();
               repaint();
             //  univ.getCanvas().repaint();
           
            }
            });
           m_Slider[0].setBackground(bk);
           in1.add(lab);
           in1.add(m_Slider[0]);
           in1.add(iLabel[0]);
          
           
           lab=new JLabel("Column    Breadth", JLabel.RIGHT);
           m_Slider[1] = new JSlider(JSlider.HORIZONTAL,5, 10, 5);
           m_Slider[1].addChangeListener(new ChangeListener() {
   		    public void stateChanged(ChangeEvent e) {
               valChange = true;
               int val = ((JSlider) e.getSource()).getValue();
               fields[1]=val;
         
               iLabel[1].setText(":: " + fields[1]*50 + " m");
               
               //univ.getCanvas().repaint();
           
            }
            });
           m_Slider[1].setBackground(bk);
           in1.add(lab);
           in1.add(m_Slider[1]);
           in1.add(iLabel[1]);
           
           
           lab=new JLabel("Depth", JLabel.RIGHT);
           m_Slider[2] = new JSlider(JSlider.HORIZONTAL,5, 10, 5);
           m_Slider[2].addChangeListener(new ChangeListener() {
   		    public void stateChanged(ChangeEvent e) {
               valChange = true;
               int val = ((JSlider) e.getSource()).getValue();
               fields[2]=val;
         
               iLabel[2].setText(":: " + fields[2]*50 + " m");
                repaint();
            //   univ.getCanvas().repaint();
           
            }
            });
           m_Slider[2].setBackground(bk);
           in1.add(lab);
           in1.add(m_Slider[2]);
           in1.add(iLabel[2]);
           
           
         
           lab=new JLabel("Column Material",JLabel.CENTER);
              JComboBox Column_material = new JComboBox();
         //     Column_material.addItem("-");
              Column_material.addItem("Concrete");
              Column_material.addItem("Steel");
              Column_material.addActionListener(new java.awt.event.ActionListener() {
 	   			 public void actionPerformed(ActionEvent e) {
 	   				 valChange = true;
 	   				
 	   			      
 	   				 JComboBox cb = (JComboBox)e.getSource();
 	   				 obj_f = (String)cb.getSelectedItem();
 	   				 valChange = true;
 	   			   	if (obj_f.equalsIgnoreCase("Concrete"))
	 	     		    {
	 	     	  
	 	     	            cementGradeList.addItem("M" + 25); 
	 	     	        	cementGradeList.addItem("M" + 20); 
	 	     	        	cementGradeList.addItem("M" + 30); 
	 	     	  	     	cementGradeList.addItem("M" + 35);  
	 	     	         	cementGradeList.addItem("M" + 40);  
	 	     	  	    int n=cementGradeList.getItemCount();
	 	     	  	    if (n>5)
	 	     	  	    {
	 	     	  	        cementGradeList.removeItem("Fe" + 415); 	
	 	     	  	    }
	     	              
	 	     	     
	 	     	     	cementGradeList.removeItem("Fe" + 415);
	     	    
	 	     	  	repaint();
	 	     		   }
	 	   		     	if (obj_f.equalsIgnoreCase("Steel"))
		     		    {
		     
		     
		     	      	cementGradeList.addItem("Fe" + 415); 
		     	      	cementGradeList.removeItem("M" + 20);
		     	    	cementGradeList.removeItem("M" + 25);
		     	    	cementGradeList.removeItem("M" + 30);
		     		    cementGradeList.removeItem("M" + 35);
		     		    cementGradeList.removeItem("M" + 40);
		     		
		     	      	int n=cementGradeList.getItemCount();
		     	         if(n>1)
		     	         {
		     	        	cementGradeList.removeItem("M" + 20);
			     	    	cementGradeList.removeItem("M" + 25);
			     	    	cementGradeList.removeItem("M" + 30);
			     		    cementGradeList.removeItem("M" + 35);
			     		    cementGradeList.removeItem("M" + 40);		     	        	 
		     	         }
		     	       //   System.out.println(n);
		     	         repaint();
		     		   }
	 	   		 //   cementGradeList.setEditable(false);
 	   				 resetOutputParameters();
 	   				repaint();
 	   			
 	   			 }
    			 });
              
              in2.add(lab);
              in2.add(Column_material);
           
           lab = new JLabel("Material Grade", JLabel.CENTER);         
    
           cementGradeList.setEditable(false);
                
      //   System.out.println(cementGradeList);
            cementGradeList.addActionListener(new java.awt.event.ActionListener() {
	   			 public void actionPerformed(ActionEvent e) {
	   				 valChange = true;
	   			   				 String obj =(String)((JComboBox)e.getSource()).getSelectedItem();
	   		    	
	   			   			
	   			  //  System.out.println(obj);
	   			    StringBuffer obj1 = new StringBuffer(obj);
	   			    if(obj_f.equalsIgnoreCase("Concrete")) 
	   				 {
	   			    	
	   				    obj1 = new StringBuffer(obj.substring(1));	   			
	   				 }
	   			    if(obj_f.equalsIgnoreCase("Steel")) 
   				    {
	   			
   				       obj1 = new StringBuffer(obj.substring(2));	   			
   				    }
	   			//    System.out.println(obj1);
	   			    
	   			  //  val=Integer.parseInt(obj);
	   				 val = Integer.parseInt(obj1.toString());	   				   				 
	   				 resetOutputParameters();
	   				repaint();
	   				 // univ.getCanvas().repaint();
	   			 }
   			 });
         
            in2.add(lab);
            in2.add(cementGradeList);
          
           lab = new JLabel("End Conditions", JLabel.CENTER);
           End_Conditions = new JComboBox();
           End_Conditions.addItem("Both Ends Pinned");            
           End_Conditions.addItem("One End Fixed Other End Free");	      
           End_Conditions.addItem("Both ends Fixed");
           End_Conditions.addItem("One end Fixed Other End Pinned");
 
           End_Conditions.addActionListener(new java.awt.event.ActionListener() {
	   			 public void actionPerformed(ActionEvent e) {
	   				 valChange = true;
	   				 JComboBox cb = (JComboBox)e.getSource();
	   				 objImg = (String)cb.getSelectedItem();
	   			 	 valChange = true;
	   			 	 getImg();
	   			 	repaint();
	   				// univ.getCanvas().repaint();
	   			
	   			 }
			 });
         in2.add(lab);       
         in2.add(End_Conditions);  
         
         
         JLabel lab1;
         lab = new JLabel("    Effective Length", JLabel.LEFT);
         lab1=new JLabel("    ",JLabel.RIGHT);
         len=new JLabel("0 m",JLabel.LEFT);   
         in3.add(lab1);
         in3.add(lab);
         in3.add(len);
        
         
        
         lab=new JLabel("   Factor of safety",JLabel.LEFT);
         lab1=new JLabel("    ",JLabel.RIGHT);
         Fac_Of_Safty=new JComboBox();
         Fac_Of_Safty.addItem("1");
         Fac_Of_Safty.addItem("2");
         Fac_Of_Safty.addItem("3");
         Fac_Of_Safty.addItem("4");
         Fac_Of_Safty.addItem("5");
         Fac_Of_Safty.addActionListener(new java.awt.event.ActionListener() {
   			 public void actionPerformed(ActionEvent e) {
   				 valChange = true;
   				 JComboBox cb = (JComboBox)e.getSource();
   				 safty_factor=(String)cb.getSelectedItem();
   			 	 valChange = true;
   			 	 repaint();
   			 }
		 });
        
         in3.add(lab1);
         in3.add(lab);
         in3.add(Fac_Of_Safty);
       
       
         
         bottomPanel.setVisible(false);
    }
    
//    public void getCem(String s)
//    {
//    	if(s.equals("Steel"))
//    	{
//    		cementGradeList.removeAllItems();
//    		     cement1[0]="M" + 200;
//				 cement1[1]="M" + 415;
//			     cement1[2]="M" + 500;
//			    		
//    	}
//    	if(s.equals("Concrete"))
//    	{
//    		cementGradeList.removeAllItems();
//    		     cement[0]="M" + 20;
//				 cement[1]="M" + 25;
//			     cement[2]="M" + 30;
//		         cement[3]="M" + 35;
//		         cement[4]="M" + 40;
//    		
//    	}
//    }
    private void getEL()
	{
    	int i=getEndConditions();
    	double  Yval=0;
    	
    	if (i==1)
    		Yval=fields[0];
    	if(i==2) 
    		Yval=fields[0]*2;
    	
    	if (i==3)
    		Yval=fields[0]/2;
    	if(i==4) 
    		Yval=fields[0]/Math.sqrt(2.0);
    	
    	len.setText(String.valueOf(Yval) + " m");
		
    	
    //	return 6000.0;
	}
    private int getEndConditions(){
 	   String obj =  (String) End_Conditions.getSelectedItem(); 
 	   if(obj=="Both Ends Pinned")
 		   return 1;
 	   if(obj=="One End Fixed Other End Free")
		   return 2;
 	  if(obj=="Both ends Fixed")
		   return 4;
 	 if(obj=="One end Fixed Other End Pinned")
		   return 3;
 	   
         return 1;
     }

 
    private void initInputControlsField(){
    	
    	
    	iLabel = new JLabel[3];
       	int i=0;
       	iLabel[i] = new JLabel("6 m", JLabel.LEFT); iLabel[i++].setForeground(Color.blue);
       	iLabel[i] = new JLabel("250 mm", JLabel.LEFT); iLabel[i++].setForeground(Color.blue);
    	iLabel[i] = new JLabel("250 mm", JLabel.LEFT); iLabel[i++].setForeground(Color.blue);
       	
       	i=0;
       	fields = new double[3];
       	fields[0]=6.0;
       	fields[1]=250.0;
       	fields[2]=250.0;
    	
       
       	
    }
    private void onNextStage()
    {
    	    	
    	valChange = true;     	
    	resetOutputParameters(); 
    	bottomPanel.setVisible(true);
    	enableStage(stage);
    	setInstructionText();
   		 
    }
    private void enableStage(int s){
    	switch(s){
    	case 0: // Home     		
    		enable(in1,false);	 enable(in2,false);		 enable(in3,false);	
			
    		break;
    	
    	case 1:

    		enable(in1,true);	 enable(in2,true);		 enable(in3,true);	
    
    		break;
    		
    	case 2:

    		enable(in1,true);	 enable(in2,true);		 enable(in3,true);	
    
    		break;
    		
    	case 3:

    		enable(in1,true);	 enable(in2,true);		 enable(in3,true);	
    
    		break;
    		
    	case 4:

    		enable(in1,true);	 enable(in2,true);		 enable(in3,true);	
            nextButton.setVisible(false);
    		break;

    	}
    	
    }
    
    private void setInstructionText()
    {
    	    	
    	valChange = true;   	
    	resetOutputParameters(); 
    	
    	
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
    
    private void resetOutputParameters()
    {
    	int i=2;
       
//        outlbl_val[i++].setText(" 0 sec");
//        outlbl_val[i++].setText(" 0 (m/s)");
      

        
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
	  
	    Vector3f currPos=new Vector3f();
	    t3d.get(currPos); 
	   
	
	    float y = (float)(float)Math.sin(Math.toRadians(m_cameraViews[m_cameraEye]));
	    float z = 2.41f - Math.abs(y);
	   
	    t3d.lookAt( new Point3d(0, y,z), new Point3d(0,0,0), new Vector3d(0,1,0));
	    t3d.invert();
	    
	    
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
	  
	    Vector3f currPos=new Vector3f();
	    t3d.get(currPos);
	    
	
	    float x = (float)(float)Math.sin(Math.toRadians(m_cameraViews[m_cameraEye]));
	    float z = 2.41f - Math.abs(x);
	
	    t3d.lookAt( new Point3d(x, 0,z), new Point3d(0,0,0), new Vector3d(0,1,0));
	    t3d.invert();
	    
	  
	    steerTG.setTransform(t3d);
	    m_cameraEye++;
	    if(m_cameraEye == 360){
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
    
      

        
        if(valChange){
	        int  EC = getEndConditions();
	        int SF=Integer.parseInt(safty_factor);
	       
	        freeBody.init(fields[0], fields[1], fields[2], val,SF, EC);
	        // init(double len,double bre,double dep,double matGd,double safFac,int endC)
	   
        }
        
        timer.start();
        System.out.println("Timer started");     
    }
    
   
    
 
    private void timerActionPerformed(java.awt.event.ActionEvent evt)
    {
    	
    	  int i=0; 
          
          if(String.valueOf(freeBody.getCriticalLoad()).length()>4)                                 
          	outlbl_val[i++].setText(String.valueOf(freeBody.getCriticalLoad()).substring(0,7) + " kN");
          
          if(String.valueOf(freeBody.getRankeesCriticalLoad()).length()>4)                                 
            	outlbl_val[i++].setText(String.valueOf(freeBody.getRankeesCriticalLoad()).substring(0,7) + " kN");
          
          if(String.valueOf(freeBody.getAxialLoad()).length()>4)                                 
            	outlbl_val[i++].setText(String.valueOf(freeBody.getAxialLoad()).substring(0,7) + " kN");
        

          
          if(String.valueOf(freeBody.getSlenderNessLoad()).length()>4)                                 
          	outlbl_val[i++].setText(String.valueOf(freeBody.getSlenderNessLoad()).substring(0,7) + " ");
          
          len.setText(String.valueOf(freeBody.getLength()));
          
          
    	 java.util.BitSet visibleNodes = new java.util.BitSet( switchGroup1.numChildren() );
 	 //   System.out.println(stage);
 		visibleNodes.set( stage*3);
 		visibleNodes.set( stage*3 +1);
 		visibleNodes.set( stage*3+2);
 		if(stage==1)
 			visibleNodes.set(12);
 		switchGroup1.setChildMask( visibleNodes );
 		
 		visibleNodes = new java.util.BitSet( switchGroup2.numChildren() );
 		visibleNodes.set( 0);
 		switchGroup2.setChildMask( visibleNodes );
 		
 		
 		visibleNodes = new java.util.BitSet( switchGroup4.numChildren() );
 		visibleNodes.set( 0);
 		switchGroup4.setChildMask( visibleNodes );
 		
 		visibleNodes = new java.util.BitSet( switchGroup3.numChildren() );
 		
 		if(stage==0 || stage==2)
 			visibleNodes.set( 0);
 		else if(stage==3)
 			visibleNodes.set( 1);
 		switchGroup3.setChildMask( visibleNodes );
 		cool();
 		float z=0.005f;
 		if(stage==0)
 		{
 			cool();
 			
 			visibleNodes = new java.util.BitSet( switchGroup2.numChildren() );
     	    switchGroup2.setChildMask( visibleNodes );
              update(z);
 			visibleNodes = new java.util.BitSet( switchGroup.numChildren() );
 			visibleNodes.set( 0);
 			switchGroup.setChildMask( visibleNodes );
 			cool();
 			update(2*z);
 			visibleNodes = new java.util.BitSet( switchGroup.numChildren() );
 			visibleNodes.set( 1);
 			switchGroup.setChildMask( visibleNodes );
 			cool();
 			update(3*z);
 			visibleNodes = new java.util.BitSet( switchGroup.numChildren() );
 			visibleNodes.set( 2);
 			switchGroup.setChildMask( visibleNodes );
 			cool();
 			update(4*z);
 			visibleNodes = new java.util.BitSet( switchGroup.numChildren() );
 			visibleNodes.set( 3);
 			switchGroup.setChildMask( visibleNodes );
 			cool();
 			update(5*z);
 			visibleNodes = new java.util.BitSet( switchGroup.numChildren() );
 			visibleNodes.set( 4);
 			switchGroup.setChildMask( visibleNodes );
 			cool();
 			update(6*z);
 			visibleNodes = new java.util.BitSet( switchGroup.numChildren() );
 			visibleNodes.set( 5);
 			switchGroup.setChildMask( visibleNodes );
 			cool();
 			update(7*z);
 			visibleNodes = new java.util.BitSet( switchGroup.numChildren() );
 			visibleNodes.set( 6);
 			switchGroup.setChildMask( visibleNodes );
 			
 			update(0f);
 			
 			visibleNodes = new java.util.BitSet( switchGroup.numChildren() );
 			switchGroup.setChildMask( visibleNodes );
 		}
 		if(stage==1)
 		{
 			cool();
 			
 			visibleNodes = new java.util.BitSet( switchGroup2.numChildren() );
     	    switchGroup2.setChildMask( visibleNodes );
              
 			visibleNodes = new java.util.BitSet( switchGroup5.numChildren() );
 			visibleNodes.set( 0);
 			switchGroup5.setChildMask( visibleNodes );
 			cool();
 		
 			visibleNodes = new java.util.BitSet( switchGroup5.numChildren() );
 			visibleNodes.set( 1);
 			switchGroup5.setChildMask( visibleNodes );
 			cool();
 		
 			visibleNodes = new java.util.BitSet( switchGroup5.numChildren() );
 			visibleNodes.set( 2);
 			switchGroup5.setChildMask( visibleNodes );
 			cool();
 			
 			visibleNodes = new java.util.BitSet( switchGroup5.numChildren() );
 			visibleNodes.set( 3);
 			switchGroup5.setChildMask( visibleNodes );
 			cool();
 		
 			visibleNodes = new java.util.BitSet( switchGroup5.numChildren() );
 			visibleNodes.set( 4);
 			switchGroup5.setChildMask( visibleNodes );
 			cool();
 		
 			visibleNodes = new java.util.BitSet( switchGroup5.numChildren() );
 			visibleNodes.set( 5);
 			switchGroup5.setChildMask( visibleNodes );
 			cool();
 		
 			visibleNodes = new java.util.BitSet( switchGroup5.numChildren() );
 			visibleNodes.set( 6);
 			switchGroup5.setChildMask( visibleNodes );
 			cool();
 		
 			visibleNodes = new java.util.BitSet( switchGroup5.numChildren() );
 			switchGroup5.setChildMask( visibleNodes );
 		}
 		
 		if(stage==2)
 		{
 			cool();
 			
 			visibleNodes = new java.util.BitSet( switchGroup2.numChildren() );
     	    switchGroup2.setChildMask( visibleNodes );
              update(z);
 			visibleNodes = new java.util.BitSet( switchGroup6.numChildren() );
 			visibleNodes.set( 0);
 			switchGroup6.setChildMask( visibleNodes );
 			cool();
 			update(2*z);
 			visibleNodes = new java.util.BitSet( switchGroup6.numChildren() );
 			visibleNodes.set( 1);
 			switchGroup6.setChildMask( visibleNodes );
 			cool();
 			update(3*z);
 			visibleNodes = new java.util.BitSet( switchGroup6.numChildren() );
 			visibleNodes.set( 2);
 			switchGroup6.setChildMask( visibleNodes );
 			cool();
 			update(4*z);
 			visibleNodes = new java.util.BitSet( switchGroup6.numChildren() );
 			visibleNodes.set( 3);
 			switchGroup6.setChildMask( visibleNodes );
 			cool();
 			update(5*z);
 			visibleNodes = new java.util.BitSet( switchGroup6.numChildren() );
 			visibleNodes.set( 4);
 			switchGroup6.setChildMask( visibleNodes );
 			cool();
 			update(6*z);
 			visibleNodes = new java.util.BitSet( switchGroup6.numChildren() );
 			visibleNodes.set( 5);
 			switchGroup6.setChildMask( visibleNodes );
 			cool();
 			update(7*z);
 			visibleNodes = new java.util.BitSet( switchGroup6.numChildren() );
 			visibleNodes.set( 6);
 			switchGroup6.setChildMask( visibleNodes );
 			cool();
 			update(0f);
 			visibleNodes = new java.util.BitSet( switchGroup6.numChildren() );
 			switchGroup6.setChildMask( visibleNodes );
 		}
 			
 		if(stage==3)
 		{
 			cool();
 			
 			visibleNodes = new java.util.BitSet( switchGroup2.numChildren() );
     	    switchGroup2.setChildMask( visibleNodes );
              update1(z);
 			visibleNodes = new java.util.BitSet( switchGroup7.numChildren() );
 			visibleNodes.set( 0);
 			switchGroup7.setChildMask( visibleNodes );
 			cool();
 			update1(2*z);
 			visibleNodes = new java.util.BitSet( switchGroup7.numChildren() );
 			visibleNodes.set( 1);
 			switchGroup7.setChildMask( visibleNodes );
 			cool();
 			update1(3*z);
 			visibleNodes = new java.util.BitSet( switchGroup7.numChildren() );
 			visibleNodes.set( 2);
 			switchGroup7.setChildMask( visibleNodes );
 			cool();
 			update1(4*z);
 			visibleNodes = new java.util.BitSet( switchGroup7.numChildren() );
 			visibleNodes.set( 3);
 			switchGroup7.setChildMask( visibleNodes );
 			cool();
 			update1(5*z);
 			visibleNodes = new java.util.BitSet( switchGroup7.numChildren() );
 			visibleNodes.set( 4);
 			switchGroup7.setChildMask( visibleNodes );
 			cool();
 			update1(6*z);
 			visibleNodes = new java.util.BitSet( switchGroup7.numChildren() );
 			visibleNodes.set( 5);
 			switchGroup7.setChildMask( visibleNodes );
 			cool();
 			update1(7*z);
 			visibleNodes = new java.util.BitSet( switchGroup7.numChildren() );
 			visibleNodes.set( 6);
 			switchGroup7.setChildMask( visibleNodes );
 			
 			update1(0f);
 			visibleNodes = new java.util.BitSet( switchGroup7.numChildren() );
 			switchGroup7.setChildMask( visibleNodes );
 		}
 	
       
      
//        outlbl_val[i++].setText(String.valueOf(freeBody.getCriticalLoad()) + " kN");
//        outlbl_val[i++].setText(String.valueOf(freeBody.getRankeesCriticalLoad()) + " kN");
//        outlbl_val[i++].setText(String.valueOf(freeBody.getAxialLoad()) + " kN");
//        outlbl_val[i++].setText(String.valueOf(freeBody.getSlenderNessLoad()) );
        return;            
    }
    
    private void updateSimulationBody(double disp)
    {
    	
    	  float rad = (float)Math.PI/180;
    	  Transform3D trans = new Transform3D();
    	  TransformGroup tgp = (TransformGroup)hm.get("cylinder");
    	  tgp.getTransform(trans);
    	  Vector3d s = new Vector3d();
		  trans.getScale(s );
		  float val = (float)disp*200;
		  trans.rotZ(rad*val);
		  trans.setScale(s);
		  trans.setTranslation(new Vector3d(0,-0.21,-0.1));
		  tgp.setTransform(trans);	  
		  
		  
    }
//    private void test()
//    {
//    	
//    }
    
    
    private void pauseSimulation()
    {
    	
		timer.stop();
		java.util.BitSet visibleNodes = new java.util.BitSet( switchGroup4.numChildren() );
		switchGroup4.setChildMask( visibleNodes );
		
		 visibleNodes = new java.util.BitSet( switchGroup2.numChildren() );
		 switchGroup2.setChildMask( visibleNodes );
		
		visibleNodes = new java.util.BitSet( switchGroup1.numChildren() );
		visibleNodes.set(stage*3);
		visibleNodes.set(stage*3+1);
		switchGroup1.setChildMask( visibleNodes );
		visibleNodes = new java.util.BitSet( switchGroup3.numChildren() );
		switchGroup3.setChildMask( visibleNodes );
		
		
		
		ImageIcon icon = m_j3d.createImageIcon("resources/icons/start.png"); 
    	startButton.setIcon(icon);
    	startButton.setText("Start");
    	reStartButton.setEnabled(true);
        nextButton.setEnabled(true);
    	//rightPanel.setVisible(true);
		enableStage(stage);
	//	outputGraph.setState(0);
	//	inputGraph.setState(0);
        valChange = false;	         
		repaint();
    }
        public void update(float addy)
    {

    	Vector3d s = new Vector3d();
  	  // Get Scale
  	  	
  	   TransformGroup objtrans=  (TransformGroup)hm.get("target1");
  	 Transform3D trans = new Transform3D();
  	 objtrans.getTransform(trans);
  	 trans.getScale(s );	
  	 trans.setScale(s);
     trans.setTranslation(new Vector3d(0.4,0.272-addy, 0.1));
     
  	 objtrans.setTransform(trans);
    	
    }
    public void update1(float addy)
    {

    	Vector3d s = new Vector3d();
  	  // Get Scale
  	  	
  	   TransformGroup objtrans=  (TransformGroup)hm.get("target2");
  	 Transform3D trans = new Transform3D();
  	 objtrans.getTransform(trans);
  	 trans.getScale(s );	
  	 trans.setScale(s);
     trans.setTranslation(new Vector3d(0.4,0.272-addy, 0.1));
     
  	 objtrans.setTransform(trans);
    	
    }
    public void cool()
    {
    	try
    	{
    		Thread.sleep(250); // do nothing for 1000 miliseconds (1 second)
    	}
    	catch(InterruptedException e)
    	{
    		e.printStackTrace();
    	} 
    }

    
    public Group createTriangle(Point3d point1,Point3d point2,Point3d point3,Vector3d scale,Vector3d rot,Color3f color)
    {
    	Transform3D t = new Transform3D();
        float rad = (float)Math.PI/180;
    		if(rot.x != 0)
    			t.rotX(rad*rot.x);
    		else if(rot.y != 0)
    			
    			t.rotY(rad*rot.y); 
    		else if(rot.z != 0)
    			t.rotZ(rad*rot.z);
        t.setScale(scale);    
        TransformGroup objtrans = new TransformGroup(t);
        objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        Shape3D shape = new Shape3D();
        TriangleArray tri = new TriangleArray(3, TriangleArray.COORDINATES|TriangleArray.COLOR_3);
        tri.setCoordinate(0, point1);
        tri.setCoordinate(1, point2);
        tri.setCoordinate(2,point3) ;
        tri.setColor(0, color);
        tri.setColor(1, color);
        tri.setColor(2,color );
        shape.setGeometry(tri);
        objtrans.addChild(shape); 
        return objtrans;
       
    }

    
}	    
   


class ColumnAnalysisBody  {
	double length,Elength,breadth,depth,matGrade,saftyFac; //Inputs
	String matChar,endCond;                        // Inputs
	double E,A,I1,I2,I,K,alp;                      //Initial Calculations
	double Pe=0.0,Pr=0.0,Sl=0.0,SR=0.0;
	double Y=0.0;
	ColumnAnalysisBody()
	{
		Elength=0.0;
		length=6000.0;
		breadth=250;
		depth=250;
		matGrade=20;
		saftyFac=2;
		
	}
	
	void init(double len,double bre,double dep,double matGd,double safFac,int endC)
	{
	 //  	System.out.println("length :" + len + " Breadth " + bre + "depth " + dep + "matgd " + matGd + "safty Factor" + safFac + "end c " + endC );
          length=len*1000;
        
          breadth=bre;
          depth=dep;
          matGrade=matGd;
          saftyFac=safFac;
          if (matGrade > 100.0)
        	  E=200000;         // Es = 200000;
          else
          E=5000*Math.sqrt(matGrade);
         
          A=breadth*depth;
          I1=(breadth*depth*depth*depth)/12;
          I2=(depth*breadth*breadth*breadth)/12;
          I=min(I1,I2);
          K=Math.sqrt(I/A);
          alp=matGrade/(Math.PI*Math.PI*E);
          if(endC==1)
        	  Elength=1*length;
          else if (endC==2)
        	  Elength=2*length;
          else if(endC==3)
        	  Elength=length/2;
          else
        	  Elength=length/Math.sqrt(2); 
          
          Pe=Math.PI*Math.PI*E*I/(Elength*Elength)/1000;
          Pr=matGrade*A/(1+alp*(Elength/K)*(Elength/K))/1000;
          Sl=min(Pe,Pr)/saftyFac;
          SR=Elength/K;       
          
	}


	private double min(double i12, double i22) 
	{
		if (i12>i22)
			return i22;
		else
			return i12;
		
	}
	public double getY(int endC,int W,double x)
	{
		if(endC==1)
		{
			Y=(((W*length*x*x*x)/12)-((W*x*x*x*x)/24)-((W*length*length*length*x)/24))/(E*I);
		    return Y;
		}
		if(endC==2)
		{
			Y=(((-W*(length-x)*(length-x)*(length-x)*(length-x))/24)-((W*length*length*length*x)/6)+(W*length*length*length*length)/24)/(E*I);
			return Y;
		}
		if(endC==3) 
		{
			Y=(W*length*x*x*x/12.0-W*x*x*x*x/24.0-W*length*length*x*x/32.0-W*length*length*length*x/96.0)/(E*I);
			
		//	System.out.println(Y);
			return Y;
		}
		if(endC==4) 
		{
			Y=(W*length*x*x*x/16.0-W*x*x*x*x/24.0-W*length*length*length*x/48.0)/(E*I);
			
			return Y;
		}
		return Y;
	}
	public double getLength()
	{
		return Elength/1000;
	}
//	public double getY()
//	{
//		return Y;
//	}
	public double getCriticalLoad()
	{
		return Pe;
	}
	public double getRankeesCriticalLoad()
	{
		return Pr;
	}
	public double getAxialLoad()
	{
		return Sl;
	}
	public double getSlenderNessLoad()
	{
		return SR;
	}
	

	 
}



