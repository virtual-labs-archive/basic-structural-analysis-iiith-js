package com.vlab.geo;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Formatter;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Group;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Texture;
import javax.media.j3d.TransparencyAttributes;

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
import java.util.*;

import org.omg.CORBA.Bounds;

import eerc.vlab.common.FullViewGraph;
import eerc.vlab.common.HelpWindow;
import eerc.vlab.common.HorizontalGraph;
import eerc.vlab.common.HorizontalGraphWrapper;
import eerc.vlab.common.ImagePanel;
import eerc.vlab.common.J3DShape;
import eerc.vlab.common.Resources;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;


public class compaction extends javax.swing.JPanel {
	//  Variables declaration - do not modify//GEN-BEGIN:variables
	//////////////////////////GUI componenet ///////////////////////////
	private javax.swing.JPanel topPanel;
	private javax.swing.JPanel simulationPanel;
	private javax.swing.JPanel bottomPanel;
	private javax.swing.JPanel rightPanel;
	  
	private javax.swing.JPanel in1;			// Input panel 1
	private javax.swing.JPanel in2;			// Input panel 2
	private javax.swing.JPanel in3;			// Input panel 3
	
	
	private boolean noTriangulate = false;
	private boolean noStripify = false;
	private double creaseAngle = 60.0;
	
	
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
	TransformGroup sievePos= new TransformGroup(new Transform3D());
	TransformGroup cyl0 = new TransformGroup();
	TransformGroup cyl1 = new TransformGroup();
	TransformGroup cyl2 = new TransformGroup();
	TransformGroup cyl3 = new TransformGroup();
    TransformGroup cyl4 = new TransformGroup();
    TransformGroup cyl5 = new TransformGroup();
    TransformGroup cyl6 = new TransformGroup();
    
    TransformGroup hammer1 = new TransformGroup();
    TransformGroup hammer2 = new TransformGroup();
    TransformGroup hammer3 = new TransformGroup();
    TransformGroup hammer4 = new TransformGroup();
	
    TransformGroup plank = new TransformGroup();
    TransformGroup body = new TransformGroup();
    TransformGroup Sand = new TransformGroup();
    TransformGroup cap = new TransformGroup();
    
	
    private Switch objSwitch = new Switch();
	Appearance appea = new Appearance();
	
//	private HelixBody       freeBody =null;               // Shape3D
	private HorizontalGraph		outputGraph =null;
	private HorizontalGraph		outputGraph2 =null;
	private FullViewGraph  		fullViewGraph = new FullViewGraph();
	

	@SuppressWarnings("unchecked")
	private HashMap 			hm = new HashMap();
	private eerc.vlab.common.J3DShape 			m_j3d	= new J3DShape();

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
	private int cnt = 0;
	private int state=0;
	private float x=0;
	private int st=0;
	int ctr=0;
	float i=0;
	
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
		// objRoot.addChild(m_j3d.createBox(new Vector3d(0,-0.6, -.1),new Vector3d(1.5,.01,1.5),new Vector3d(0,0,0),new Color3f(.8f, .8f, .8f),"resources/images/tile.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.4,-2.5),new Vector3d(10,10,.01),new Vector3d(0f, 0f,0f), new Color3f(0.5f,0.6f,0.72f)));
		// Walls and roof
		objRoot.addChild(m_j3d.createBox(new Vector3d(1,0.1f,0), new Vector3d(0.05,0.7f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 0.9f),"resources/images/floor.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(-1,0.1f,0), new Vector3d(0.05,0.7f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 0.9f),"resources/images/floor.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.1f,-2.0), new Vector3d(1,0.7f,0.05),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/tile69.jpg"));
		objRoot.addChild(m_j3d.createBox(new Vector3d(0,0.84f,0), new Vector3d(1.05,0.04f,2),new Vector3d(0,0,0),new Color3f(1f, 1f, 1f),"resources/images/floor.jpg"));

	
		
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
		  
		Color3f light1Color = new Color3f(1f, 1f, 1f);
		
	    BoundingSphere bounds =  new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
	
	    Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
	
	    DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
	
	    light1.setInfluencingBounds(bounds);    
	    objRoot.addChild(light1);
	
	    AmbientLight ambientLight =  new AmbientLight(new Color3f(.3f,.3f,.3f));
	
	    ambientLight.setInfluencingBounds(bounds);
	
	    objRoot.addChild(ambientLight);
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
        
        //ViewingPlatform viewingPlatform = univ.getViewingPlatform();
        setLight();
        
        univ.getViewingPlatform().setNominalViewingTransform();

        
        ViewingPlatform vp = univ.getViewingPlatform();
   	    TransformGroup steerTG = vp.getViewPlatformTransform();
   	    Transform3D t3d = new Transform3D();
   	    steerTG.getTransform(t3d);
   	    Vector3d s = new Vector3d();
   	    Vector3f currPos=new Vector3f();
   	    t3d.get(currPos); 
   	    
   	   // System.out.println("current Pos:" + currPos);
   	  
   	    
   	 t3d.lookAt( new Point3d(0, .2, 4 ), new Point3d(0,0,0), new Vector3d(0,1,0));
   	    t3d.invert();
   	    
   	    //t3d.setTranslation(new Vector3d(0,0,8));
   	    steerTG.setTransform(t3d);
        
        
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

    
    public Group createBox(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr) {
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
        TransparencyAttributes ta = new TransparencyAttributes();
        ta.setTransparencyMode (TransparencyAttributes.BLENDED);
        ta.setTransparency (0.7f);
        app.setTransparencyAttributes (ta);
        
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(colr);
        app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        app.setColoringAttributes(ca);
        
        objtrans.addChild(new Box(4.0f, 4.0f, 4.0f,app));
        
        return objtrans;
    }     
    
    
    public Group createWireBox(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr) {
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
        
        
        PolygonAttributes polyAttr = new PolygonAttributes();
        polyAttr.setPolygonMode(PolygonAttributes.POLYGON_LINE);
       // polyAttr.setPolygonMode(PolygonAttributes.POLYGON_POINT);
        polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
        app.setPolygonAttributes(polyAttr);
        
        
        /*LineAttributes LineAttr = new LineAttributes();
        LineAttr.setLinePattern(LineAttributes.PATTERN_SOLID);
       // polyAttr.setPolygonMode(PolygonAttributes.POLYGON_POINT);
        //polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
        app.setLineAttributes(LineAttr);*/
        
        //ta.setTransparencyMode (TransparencyAttributes.BLENDED);
        //ta.setTransparency (0.7f);
        //app.setTransparencyAttributes (ta);
        
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(colr);
        app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        app.setColoringAttributes(ca);
        
        objtrans.addChild(new Box(1.0f, 1.0f, 1.0f,app));
        
        return objtrans;
    }
    public Group loadObjectFile(String objfile,Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr,int var) {
    	
    	
	   	 //objScale.addChild(objTrans);

	   	 int flags = ObjectFile.RESIZE;
	   	 if (!noTriangulate) flags |= ObjectFile.TRIANGULATE;
	   	 if (!noStripify) flags |= ObjectFile.STRIPIFY;
	   	 
	   	 ObjectFile f = new ObjectFile(flags,
	   			 (float)(creaseAngle * Math.PI / 180.0));
	   	 Scene s = null;
	   	 URL filename = Resources.getResource(objfile);
	   	 //pendulum
	   	 try {
	   		  s = f.load(filename);
	   		  
	   		  
	   		  }	catch (FileNotFoundException e) {
	   			  System.err.println(e);
	   			  System.exit(1);
	   		} catch (ParsingErrorException e) {
	   			System.err.println(e);
	   			System.exit(1);
	   			}catch (IncorrectFormatException e) {
	   				System.err.println(e);
	   				System.exit(1);
	   				}
	   			
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
	   	        
	   	        TransformGroup objTrans = new TransformGroup(t);
	   	    	objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	   	    	objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	   	        
	   			BranchGroup bg = s.getSceneGroup();
	   			BoundingBox boundingBox = new BoundingBox(s.getSceneGroup().getBounds());
	   			Point3d lower = new Point3d();
	   			boundingBox.getLower(lower); 
	   			
	   			
	   			
	   			
	   			Appearance app = new Appearance();
	   	        TransparencyAttributes ta = new TransparencyAttributes();
	   	        ta.setTransparencyMode (TransparencyAttributes.BLENDED);
	   	        
	   	        if(var==0)
	   	        	ta.setTransparency (0.3f);
	   	        else
	   	        	ta.setTransparency(.5f);
	   	        app.setTransparencyAttributes (ta);
	   			
	   	        
	   	        ColoringAttributes ca = new ColoringAttributes(colr,0);
	   	        //ca.setColor(colr);
	   	        app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
	   	        app.setColoringAttributes(ca);
	   	        
//	   	        Color3f objColor = m_j3d.getColor3f(184,134,11);
	   	        Color3f objColor = m_j3d.getColor3f(160,82,45);
	   			Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
	   			app.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
	   			app.setMaterial(new Material(colr, black, objColor, black, 80.0f));
	   			Shape3D newShape = (Shape3D) bg.getChild(0);
	   			newShape.setAppearance(app);
//	   			bg.removeChild(0);
//	   			Appearance app = new Appearance();

	//
//	   			
	//
//	   			objTrans.addChild(newShape);
	   			
//	   			Map<String, Shape3D> nameMap = s.getNamedObjects();   
//	   			   
//	   			for (String name : nameMap.keySet()) {  
//	   			        System.out.printf("Name: %s\n", name);   
//	   			} 
	   			//bg.addChild(trans);
	   			//objTrans.addChild(bg);
	   			objTrans.addChild(bg);
	       

	        return objTrans;
 }

    public Group createTextureCylinder(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr,String texfile) {
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
        app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        app.setColoringAttributes(ca);
        //objtrans.addChild(new Cylinder(0.1f,0.1f,app));


         if(texfile!=null){
         	Texture tex = new TextureLoader(Resources.getResource(texfile),TextureLoader.BY_REFERENCE | TextureLoader.Y_UP,this).getTexture();
         	app.setTexture(tex);
     	    TextureAttributes texAttr = new TextureAttributes();
     	    texAttr.setTextureMode(TextureAttributes.MODULATE);
     	    app.setTextureAttributes(texAttr);
         }



 	    objtrans.addChild(new  Cylinder(0.1f, 0.1f,Cylinder.GENERATE_TEXTURE_COORDS | Cylinder.GENERATE_TEXTURE_COORDS_Y_UP, app));

 	        return objtrans;
    }
    
    
	private Group createVirtualLab() {	
		
		int i;
		
	    TransformGroup box =new TransformGroup();
	    TransformGroup table=new TransformGroup();
	    
	    
	    TransformGroup obj1=new TransformGroup();
	    TransformGroup obj2=new TransformGroup();
	    TransformGroup obj3=new TransformGroup();
	    TransformGroup obj4=new TransformGroup();
	  //  TransformGroup text=new TransformGroup();
	    
	    Transform3D grain4 =new Transform3D();
	    Transform3D grain3 =new Transform3D();
	    Transform3D grain2 =new Transform3D();
	    Transform3D grain1 =new Transform3D();
	    Transform3D grain0 =new Transform3D();
	    
	    Transform3D part4 =new Transform3D();
	    Transform3D part3 =new Transform3D();
	    Transform3D part2 =new Transform3D();
	    Transform3D part1 =new Transform3D();
	   // Transform3D zscl =new Transform3D();
	    
	 //   grain4.setTranslation(new Vector3d(0,.395+.055,0));
	 //   grain3.setTranslation(new Vector3d(0,.26+.055,0));
	 //   grain2.setTranslation(new Vector3d(0,.16+.055,0));
	 //   grain1.setTranslation(new Vector3d(0,.06+.055,0));
	 //   grain0.setTranslation(new Vector3d(0,-.075+.055,0));
	 //   
	   // part4.setTranslation(new Vector3d(-0.13,0.22,.8));
	    part4.setScale(new Vector3d(.5,1,.5));
	    part3.setScale(new Vector3d(0,0,0));
	//    part2.setTranslation(new Vector3d(.001f,.35f,.92f));
//	    part1.setTranslation(new Vector3d(0.0f,.2f,1f));

	   // grain3.setScale(new Vector3d(0,0,0));
	    //grain2.setScale(new Vector3d(0,0,0));
	    //grain1.setScale(new Vector3d(0,0,0));
	    //grain0.setScale(new Vector3d(0,0,0));

	    
	    
				
		box.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		box.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		obj1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		obj1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		obj2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		obj2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		obj3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		obj3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		obj4.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		obj4.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
	    Transform3D t = new Transform3D();
        BranchGroup objroot = new BranchGroup();
        
	    TransformGroup objtrans = new TransformGroup(t);
	    objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);	    
	    
	   
                           
        //objroot.addChild(objtrans);
        
	    float h;
	    h = 0.055f;
	    //final float L0 = 0.36f;
	   
	 
	    obj3.addChild(loadObjectFile("resources/geometry/Mycontainer.obj",new Vector3d(0.0f,-.3f-.085,1f),new Vector3d(0.3,.21,0.3),new Vector3d(0,0,0), m_j3d.getColor3f(139,117,0),0));
	    
	    //hammer
	    
	    obj3.addChild(loadObjectFile("resources/geometry/Mycontainer.obj",new Vector3d(0.0f,.2f+.05,1f),new Vector3d(0.3,.08,0.3),new Vector3d(0,0,0), m_j3d.getColor3f(139,117,0),0));

	    //support
	    
	    obj3.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0.13,-0.32-.09,1), new Vector3d(.1,4.7,.1),new Vector3d(0,0,0),  m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),30.0f));
        obj3.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(-0.13,-0.32-.09,1), new Vector3d(.1,4.7,.1),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.3f,0.3f,0.3f),30.0f));
        
        //support Hammer
        obj3.addChild(m_j3d.createCylinder(new Vector3d(.001f,.35f+.05,.92f),new Vector3d(.4,1.1,.4),new Vector3d(0,0,0), m_j3d.getColor3f(139,117,0)));
        
        
        obj3.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0.13,0.22+.05,.8), new Vector3d(.1,1.8,.1),new Vector3d(0,0,0),  m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),30.0f));
        obj3.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(-0.13,0.22+.05,.8), new Vector3d(.1,1.8,.1),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.3f,0.3f,0.3f),30.0f));
        
        
        plank.addChild(loadObjectFile("resources/geometry/Mycontainer.obj",new Vector3d(0.0f,.05,1f),new Vector3d(0.4,.06,0.4),new Vector3d(0,0,0), m_j3d.getColor3f(139,117,0),0));
        plank.addChild(m_j3d.createCylinder(new Vector3d(.001f,-0.01,.92f),new Vector3d(1.33*1.3,.095,2.35*1.33),new Vector3d(0,0,0), m_j3d.getColor3f(218,165,32)));

       
        
        
        
        obj2.addChild(loadObjectFile("resources/geometry/Mycontainer.obj",new Vector3d(0.0f,.35+.2,1f),new Vector3d(0.3,.08,0.3),new Vector3d(0,0,0), m_j3d.getColor3f(139,117,0),0));
        obj2.addChild(m_j3d.createCylinder(new Vector3d(.001f,.38+.3,.92f),new Vector3d(.4,1.1,.4),new Vector3d(0,0,0), m_j3d.getColor3f(139,117,0)));
        obj2.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0.11,0.25+.3,1), new Vector3d(.1,1.7,.1),new Vector3d(0,0,0),  m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),30.0f));
        obj2.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(-0.11,0.25+.3,1), new Vector3d(.1,1.7,.1),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.3f,0.3f,0.3f),30.0f));
        
        body.addChild(loadObjectFile("resources/geometry/Mycontainer.obj",new Vector3d(0.0f,.2f-.15,1f),new Vector3d(0.3,.2,0.3),new Vector3d(0,0,0), m_j3d.getColor3f(139,117,0),1));
        body.addChild(m_j3d.createCylinder(new Vector3d(.001f,-0.08f-.075,.92f),new Vector3d(1.33,.095,2.35),new Vector3d(0,0,0), m_j3d.getColor3f(218,165,32)));
        body.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0.11,.25-h-.15,1), new Vector3d(.1,4.2,.1),new Vector3d(0,0,0),  m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),30.0f));
        body.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(-0.11,.25-h-.15,1), new Vector3d(.1,4.2,.1),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.3f,0.3f,0.3f),30.0f));
        body.addChild(m_j3d.createCylinder(new Vector3d(.135f,.22,.92f),new Vector3d(.1,.6,.1),new Vector3d(0,0,90), m_j3d.getColor3f(184,184,184)));
        body.addChild(m_j3d.createCylinder(new Vector3d(-.135f,.22,.92f),new Vector3d(.1,.6,.1),new Vector3d(0,0,90), m_j3d.getColor3f(184,184,184)));

        cap.addChild(loadObjectFile("resources/geometry/Mycontainer.obj",new Vector3d(0.0f,.4f-.05,1f),new Vector3d(0.3,.06,0.3),new Vector3d(0,0,0), m_j3d.getColor3f(139,117,0),1));
        cap.addChild(m_j3d.createCylinder(new Vector3d(.001f,-0.08f-.075+.46,.92f),new Vector3d(1.33,.095,2.35),new Vector3d(0,0,0), m_j3d.getColor3f(218,165,32)));
        cap.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0.11,.45-h-.04,1), new Vector3d(.1,1.3,.1),new Vector3d(0,0,0),  m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),30.0f));
        cap.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(-0.11,.45-h-.04,1), new Vector3d(.1,1.3,.1),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.3f,0.3f,0.3f),30.0f));
        cap.addChild(m_j3d.createCylinder(new Vector3d(.135f,.32,.92f),new Vector3d(.1,1,.1),new Vector3d(0,0,0), m_j3d.getColor3f(184,184,184)));
        cap.addChild(m_j3d.createCylinder(new Vector3d(-.135f,.32,.92f),new Vector3d(.1,1,.1),new Vector3d(0,0,0), m_j3d.getColor3f(184,184,184)));

        
        sievePos.addChild(obj2);
        
        
        Sand.addChild(loadObjectFile("resources/geometry/Mycontainer.obj",new Vector3d(0.0f,-.3f-.085+.2,1f),new Vector3d(0.3,.21,0.3),new Vector3d(0,0,0), m_j3d.getColor3f(139,117,0),1));
        Sand.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(0.13,-0.32-.09+.22,1), new Vector3d(.1,4.2,.1),new Vector3d(0,0,0),  m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),m_j3d.getColor3f(139,129,76),30.0f));
        Sand.addChild(m_j3d.createCylinderWithMatProp(new Vector3d(-0.13,-0.32-.09+.22,1), new Vector3d(.1,4.2,.1),new Vector3d(0,0,0),  m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),m_j3d.getColor3f(80,91,196),new Color3f(0.3f,0.3f,0.3f),30.0f));
        Sand.addChild(createTextureCylinder(new Vector3d(.001f,-.27,.92f),new Vector3d(1.25*1.01,2.4,1.95*1.01),new Vector3d(0,0,0), m_j3d.getColor3f(238,223,204),"resources/images/sand.jpg"));
        Sand.setTransform(grain0);
        
        
        cyl6.addChild(createTextureCylinder(new Vector3d(.001f,.655-.37,.92f),new Vector3d(1.2,.7,1.95),new Vector3d(0,0,0), m_j3d.getColor3f(238,223,204),"resources/images/sand.jpg"));
        cyl5.addChild(createTextureCylinder(new Vector3d(.001f,.580-.37,.92f),new Vector3d(1.2,.7,1.95),new Vector3d(0,0,0), m_j3d.getColor3f(238,223,204),"resources/images/sand.jpg"));
        cyl4.addChild(createTextureCylinder(new Vector3d(.001f,.450-.31,.92f),new Vector3d(1.2,.7,1.95),new Vector3d(0,0,0), m_j3d.getColor3f(238,223,204),"resources/images/sand.jpg"));
        cyl3.addChild(createTextureCylinder(new Vector3d(.001f,.315-.24,.92f),new Vector3d(1.2,.7,1.95),new Vector3d(0,0,0), m_j3d.getColor3f(238,223,204),"resources/images/sand.jpg"));
        cyl2.addChild(createTextureCylinder(new Vector3d(.001f,.215-.2,.92f),new Vector3d(1.2,.7,1.95),new Vector3d(0,0,0), m_j3d.getColor3f(238,223,204),"resources/images/sand.jpg"));
        cyl1.addChild(createTextureCylinder(new Vector3d(.001f,.115-.17,.92f),new Vector3d(1.2,.7,1.95),new Vector3d(0,0,0), m_j3d.getColor3f(238,223,204),"resources/images/sand.jpg"));
        cyl0.addChild(createTextureCylinder(new Vector3d(.001f,-.12,.92f),new Vector3d(1.2,.7,1.95),new Vector3d(0,0,0), m_j3d.getColor3f(238,223,204),"resources/images/sand.jpg"));
        
        
    /*    cyl4.setTransform(grain4);
        cyl3.setTransform(grain3);
        cyl2.setTransform(grain2);
        cyl1.setTransform(grain1);
        cyl0.setTransform(grain0);*/
        obj2.setTransform(part4);
        sievePos.setTransform(part3);
        cyl0.setTransform(part3);
        cyl1.setTransform(part3);
        cyl2.setTransform(part3);
        cyl3.setTransform(part3);
        cyl4.setTransform(part3);
        cyl5.setTransform(part3);
        cyl6.setTransform(part3);
        
        
        
      //table
	    table.addChild(m_j3d.createBox(new Vector3d(0.0f,-0.2f,1),new Vector3d(0.4f,0.02f,0.4f), new Vector3d(0.0f,0.0f,0.0f),new Color3f(1.0f,1.0f,1.0f),"resources/images/wood2.jpg"));
	    table.addChild(m_j3d.createCylinder(new Vector3d(0.35,-0.35f,1-0.35),new Vector3d(0.3,3.0f,0.3),new Vector3d(0,0,0),new Color3f(0.4f,0.2f,0.0f)));
	    table.addChild(m_j3d.createCylinder(new Vector3d(-0.35,-0.35f,1-0.35),new Vector3d(0.3,3.0f,0.3),new Vector3d(0,0,0),new Color3f(0.4f,0.2f,0.0f)));
	    table.addChild(m_j3d.createCylinder(new Vector3d(-0.35,-0.35f,1+0.35),new Vector3d(0.3,3.0f,0.3),new Vector3d(0,0,0),new Color3f(0.4f,0.2f,0.0f)));
	    table.addChild(m_j3d.createCylinder(new Vector3d(0.35,-0.35f,1+0.35),new Vector3d(0.3,3.0f,0.3),new Vector3d(0,0,0),new Color3f(0.4f,0.2f,0.0f)));
	    
	    
	    objroot.addChild(table);
        
        
     // create switch value interpolator
       // if(stage==-1)
        //objroot.addChild(table);      
        
        //objroot.addChild(objtrans);      

        
      /*  objroot.addChild(objSwitchPos);       
        objSwitch.insertChild(obj3, 0);
        objSwitch.insertChild(sievePos, 1);
        //objSwitch.insertChild(sievePos, 2);
        //objSwitch.insertChild(obj4,3);
        
        objSwitchPos.addChild(objSwitch);    */  
        
        
        
       // objroot.addChild(obj3);
        //objroot.addChild(plank);
        objroot.addChild(body);
        objroot.addChild(cap);

        objroot.addChild(cyl0);
        objroot.addChild(cyl1);
        objroot.addChild(cyl2);
        objroot.addChild(cyl3);
        objroot.addChild(cyl4);
        objroot.addChild(cyl5);
        objroot.addChild(cyl6);

        objroot.addChild(sievePos);
        //objroot.addChild(Sand);
        //objroot.addChild(swiInt);
        //swiInt.setLastChildIndex(6);       
	    return objroot;
	}  
    
    /**
     * Creates new form FreeVibration
     */
    public compaction(Container container) {
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
		compaction mainPanel;

        public void init() {
            setLayout(new BorderLayout());
            mainPanel = new compaction(this);
            add(mainPanel, BorderLayout.CENTER);
                        
        }

        public void destroy() {
      //      mainPanel.destroy();
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
            setTitle("Compaction Test Applet");
            getContentPane().add(new compaction(this), BorderLayout.CENTER);
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
    	cyl0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		cyl0.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    	
    	cyl1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		cyl1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);		
		
		cyl2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		cyl2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		cyl3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		cyl3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		cyl4.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		cyl4.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		cyl5.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		cyl5.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		cyl6.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		cyl6.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		hammer1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		hammer1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		hammer2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		hammer2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		hammer3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		hammer3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		hammer4.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		hammer4.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		plank.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		plank.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		body.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		body.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		Sand.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Sand.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		cap.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		cap.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        
    	sievePos.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
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
        
        timer = new Timer(20,new ActionListener() {
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
            	stage=0;
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
            	cnt++;
            	if(cnt==2)
            		stage=0;
            	if(cnt==2)
              	{
              		nextButton.setEnabled(false);
              	}
            	else
            		nextButton.setEnabled(true);            	
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
        
        rightPanel.add(outputGraph2);
        
       
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
         JLabel lbl = new JLabel("Compaction Test ", JLabel.CENTER);
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
            	 m_cameraTimer = new Timer(800,new ActionListener() {
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
    	//bottomPanel.setVisible(true);
    	enableStage(st);
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
    		break;
    		
    	case 3:

    		enable(in1,true);	 enable(in2,true);		 enable(in3,true);	
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
	      //freeBody.Init((int)fields[0],(int)fields[1],(int)fields[2],fields[3],fields[4]);
	      
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
     	
    	
        
    	Transform3D sieve = new Transform3D();
    	Transform3D sand4 = new Transform3D();
    	Transform3D sand3 = new Transform3D();
    	Transform3D sand2 = new Transform3D();
    	Transform3D sand1 = new Transform3D();
    	Transform3D sand0 = new Transform3D();
    	sand1.setScale(new Vector3d(1,1,1));
    	
    	Transform3D part4 = new Transform3D();
    	Transform3D part3 = new Transform3D();
    	Transform3D part2 = new Transform3D();
    	Transform3D part1 = new Transform3D();
    	
    	
    	
	    if(cnt ==1)
	    {
	    	cyl0.setTransform(sand1);
	    	if(ctr>=6)
	    	{
	    		if(ctr%6==0&&stage==0)
	    			i=(float) (i+.07);
	    		cyl1.setTransform(sand1);
	    	}
	    	if(ctr>=12)
	    		cyl2.setTransform(sand1);
	    	if(ctr>=18)
	    		cyl3.setTransform(sand1);
	    	if(ctr>=24)
	    		cyl4.setTransform(sand1);
	    	if(ctr>=30)
	    		cyl5.setTransform(sand1);
	    	if(ctr>=36)
	    		cyl6.setTransform(sand1);
	    	stage++;
	    	stage=stage%70;
	    	if(stage==0)
	    	{
	    		//cnt=1;
	    		if(cnt>0)
	    			ctr++;
	    		
	    	}
	    	
	    	if(stage<35)
	    	{
	    		
	    		if(ctr>40)
	    			part1.setTranslation(new Vector3d(0,0,0));
	    		else
	    		{
	    			if(ctr%2==0)
	    				part1.setTranslation(new Vector3d(-.04,-.57+i,0));
	    			else
	    				part1.setTranslation(new Vector3d(.04,-.57+i,0));
	    		}

	    	}
	    	if(stage>35)
	    	{
	    		part1.setTranslation(new Vector3d(0,0,0));
	    	}
	    	sievePos.setTransform(part1);
	        //plank.setTransform(part2);
	        //body.setTransform(part3);
	    }
	    if(cnt==2)
	    {
	    	part1.setScale(new Vector3d(0,0,0));
	    	part2.setScale(new Vector3d(0,0,0));
	    	part3.setScale(new Vector3d(0,0,0));
	    	part4.setScale(new Vector3d(1,1,1));
	    	sievePos.setTransform(part1);
	        plank.setTransform(part2);
	        body.setTransform(part3);
	        cyl0.setTransform(part3);
	        cyl1.setTransform(part3);
	        cyl2.setTransform(part3);
	        cyl3.setTransform(part3);
	        cyl4.setTransform(part3);
	        cyl5.setTransform(part3);
	        cyl6.setTransform(part4);

	    }
	    else
	    {
	    //	part1.setScale(new Vector3d(0,0,0));
	    //	sievePos.setTransform(part1);
	    	part2.setTranslation(new Vector3d(0,-.05,0));
	    	cap.setTransform(part2);
	    	part1.setScale(new Vector3d(1,1,1));
	    	sievePos.setTransform(part1);
	    }
	   //i=-.4
       
    	//if(stage>10)
    	
	/*	sieve.setTranslation(new Vector3d(x / 10, x / 50, 0));
		if (cnt == 2) 
		{
			System.out.println("cho");
			if(stage>150)
			{
				x=0;
				cnt=3;
			}
			sievePos.setTransform(sieve);
			
			
			if (state == 0 && x > -.10) {
				System.out.println("value of x is" + x);
				x = (x - (float) 0.010f);
				if (x < -.10)
					state = 1;
			}
			if (state == 1 && x < .10f) {
				System.out.println("value of x is" + x);
				x = (x + (float) .01f);
				if (x >= .1f)
					state = 0;
			}

			if (stage < 130 && stage > 0) {
				// sand.setTranslation(new Vector3d(0,-(stage/1000),0));
				// sand.setTranslation(new Vector3d(0,0,0));
				sand4.setTranslation(new Vector3d(0,.372 + .055 - (float) (stage / 800), 0));
				sand4.setScale(new Vector3d(1,1.0 / (1 + (float) (stage / 30.0)), 1));
				cyl4.setTransform(sand4);

				// cyl3.setTransform(sand1);
			}

			if (stage < 85 && stage > 10) {
				sand3.setTranslation(new Vector3d(0,.265 + .055 + (float) (stage / 550), 0));
				sand3.setScale(new Vector3d(1, (float) (stage / 3.0), 1));
				cyl3.setTransform(sand3);
			}

			if (stage < 120 && stage > 30) {
				sand2.setTranslation(new Vector3d(0,.16 + .055 + (float) (stage / 550), 0));
				sand2.setScale(new Vector3d(1, (float) (stage / 3), 1));
				cyl2.setTransform(sand2);
			}

			if (stage < 120 && stage > 50) {
				sand1.setTranslation(new Vector3d(0,.06 + .055 + (float) (stage / 550), 0));
				sand1.setScale(new Vector3d(1, (float) (stage / 3), 1));
				cyl1.setTransform(sand1);
			}

			if (stage < 130 && stage > 70) {
				sand0.setTranslation(new Vector3d(0, -.06 + .055+ (float) (stage / 550), 0));
				sand0.setScale(new Vector3d(1, (float) (stage / 3), 1));
				cyl0.setTransform(sand0);
			}
		}*/
      
        if(rightPanel.isVisible())
        {
        	outputGraph.drawGraph();
        	outputGraph2.drawGraph();
       
        }
                
       
        
        
               	
        java.util.BitSet visibleNodes = new java.util.BitSet( objSwitch.numChildren() );
        visibleNodes.set(stage);
        objSwitch.setChildMask(visibleNodes);
        if(stage==1)
        	objSwitch.setWhichChild(0);
        if(stage==2)
        	objSwitch.setWhichChild(1);
        if(stage==3)
        	objSwitch.setWhichChild(2);
        if(stage==0)
        	objSwitch.setWhichChild(3);

     

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

//*class HelixBody
//{
 // Inputs from the keyboard 
 // diameter of wire(5-30) mm, diameter of spring(50-200 mm), number of turns(5-30), 
 // spring Material(steel, copper, aluminium..) double value
 // load 0 to 100kN
//	 private int dia_of_wire,radius_of_spring,number_of_turns;// dia of wire=12,radius=75,number of turns =10
//	 private double G;    //=80.0;//spring_material_value or G or Modulus_rigidity
//	 private double load;  //=0.450;
//	 double strain_energy_stored,main_defelection,stiffness_of_spring ;
//	 ArrayList<Double>  defelection=new ArrayList<Double>();
//	 int inc;
//
//  public void Init(int dia,int rad_spr,int turns, double matval, double inp_load )
//  {
//	  defelection.clear();
//	  dia_of_wire=dia;radius_of_spring=rad_spr;number_of_turns=turns;
//	  G=matval;
//	  load=inp_load;
//	  
//	  double maximum_shear_stress,toque,length_of_rod;
//		
//		maximum_shear_stress=load*radius_of_spring*16/(Math.PI*Math.pow(dia_of_wire, 3));
//		length_of_rod=2*Math.PI*(radius_of_spring)*number_of_turns;
//		for (double changeLoad=0;changeLoad<100;changeLoad=changeLoad+1.0)
//		{
//			  strain_energy_stored=32*changeLoad*changeLoad*Math.pow((radius_of_spring),3)*number_of_turns/(G*Math.pow(dia_of_wire,4));
//				defelection.add(64*changeLoad*Math.pow((radius_of_spring),3)*number_of_turns/(G*Math.pow(dia_of_wire,4)));
//				stiffness_of_spring=changeLoad/main_defelection;
//		}
//            strain_energy_stored=32*load*load*Math.pow((radius_of_spring),3)*number_of_turns/(G*Math.pow(dia_of_wire,4));
//			main_defelection=64*load*Math.pow((radius_of_spring),3)*number_of_turns/(G*Math.pow(dia_of_wire,4));
//			stiffness_of_spring=load/main_defelection;
//		//	for (double d=) have to ask the load range
//		//	System.out.println(" defelection = " + defelection + "maximum_shear_stress " + maximum_shear_stress + " stiffness_of_spring = " + stiffness_of_spring);
//  }
// public double getStrainEnergy()
// {
//	 return strain_energy_stored;
// }
// public double getStiffness()
// {
//	 return stiffness_of_spring;
// }
// public double getMainDefelection()
// {
//	 return main_defelection;
// }
// public double getDefelection()
// {
//	
//	 return defelection.get(inc);
// }
// public void update()
// {
// 	inc++;		    	
// }
//boolean isDataCompleted()
//	{
//		int index=inc;
//	
//		if(index >=  defelection.size())
//			return true;
//		
//		return false;
//	}
// }ng = " + stiffness_of_spring);
//  }
// public double getStrainEnergy()
// {
//	 return strain_energy_stored;
// }
// public double getStiffness()
// {
//	 return stiffness_of_spring;
// }
// public double getMainDefelection()
// {
//	 return main_defelection;
// }
// public 
