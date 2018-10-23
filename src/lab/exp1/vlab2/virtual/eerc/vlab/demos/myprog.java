package eerc.vlab.demos;





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
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.URL;
import java.util.ArrayList;
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
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;

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

import org.omg.CORBA.Bounds;

import eerc.vlab.common.FullViewGraph;
import eerc.vlab.common.HelpWindow;
import eerc.vlab.common.HorizontalGraph;
import eerc.vlab.common.HorizontalGraphWrapper;
import eerc.vlab.common.ImagePanel;
import eerc.vlab.common.J3DShape;
import eerc.vlab.common.Resources;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;


public class myprog {
	
	private javax.swing.JPanel simulationPanel;
	
public myprog()

{
	simulationPanel = new javax.swing.JPanel(); // 3D rendering at center
	J3DShape 	m_j3d	= new J3DShape();
   SimpleUniverse universe = new SimpleUniverse();

   BranchGroup group1 = new BranchGroup();
   group1.setCapability(Group.ALLOW_CHILDREN_EXTEND );
   group1.setCapability(Group.ALLOW_CHILDREN_READ);
   group1.setCapability(Group.ALLOW_CHILDREN_WRITE);
   group1.setCapability( BranchGroup.ALLOW_DETACH );

   simulationPanel.setPreferredSize(new java.awt.Dimension(1024, 600));
   simulationPanel.setLayout(new java.awt.BorderLayout());
//	 Floor
	int i,j;
	for(i=-4;i<=4;i++)
	{
		for(j=-4;j<=4;j++)
		{
			group1.addChild(m_j3d.createBox(new Vector3d((float)(i),-0.6,(float)(j)),new Vector3d(0.5,.01,0.5),new Vector3d(0,0,0),new Color3f(.8f,.8f, .8f),"resources/images/tile.jpg"));
		}
	}
//	objRoot.addChild(m_j3d.createBox(new Vector3d(0,-0.6, -.1),new Vector3d(1.5,.01,1.5),new Vector3d(0,0,0),new Color3f(.8f, .8f, .8f),"resources/images/tile.jpg"));
	group1.addChild(m_j3d.createBox(new Vector3d(0,0.4,-1.5),new Vector3d(4,3,-1.55),new Vector3d(0f, 0f,0f), new Color3f(0.5f,0.6f,0.72f),"resources/images/brick.jpg"));
	
    
   
   /*Color3f brown = new Color3f(0.8f,0f,0.2f);
   ColoringAttributes ca = new ColoringAttributes(brown, ColoringAttributes.SHADE_FLAT);
   Appearance app = new Appearance();
   app.setColoringAttributes(ca);*/
   
   
     // Holder
//   group1.addChild(m_j3d.createBox(new Vector3d(0.0f,-0.2f,0.0f),new Vector3d(0.4f,0.02f,0.4f), new Vector3d(0.0f,0.0f,0.0f),new Color3f(1.0f,1.0f,1.0f),"resources/images/wood2.jpg"));
//   group1.addChild(m_j3d.createCylinder(new Vector3d(.8,-0.1f,0),new Vector3d(0.2,8.5f,0.4),new Vector3d(0,0f,0),new Color3f(0.2f,0.2f,0.2f)));
//   group1.addChild(m_j3d.createCylinder(new Vector3d(-.8,-0.1f,0),new Vector3d(0.2,8.5f,0.4),new Vector3d(0,0f,0),new Color3f(0.2f,0.2f,0.2f)));
//   // Legs
//   group1.addChild(m_j3d.createCylinder(new Vector3d(0.35,-0.35f,-0.35),new Vector3d(0.3,3.0f,0.3),new Vector3d(0,0,0),new Color3f(0.4f,0.2f,0.0f)));
//   group1.addChild(m_j3d.createCylinder(new Vector3d(-0.35,-0.35f,-0.35),new Vector3d(0.3,3.0f,0.3),new Vector3d(0,0,0),new Color3f(0.4f,0.2f,0.0f)));
//   group1.addChild(m_j3d.createCylinder(new Vector3d(-0.35,-0.35f,0.35),new Vector3d(0.3,3.0f,0.3),new Vector3d(0,0,0),new Color3f(0.4f,0.2f,0.0f)));
//   group1.addChild(m_j3d.createCylinder(new Vector3d(0.35,-0.35f,0.35),new Vector3d(0.3,3.0f,0.3),new Vector3d(0,0,0),new Color3f(0.4f,0.2f,0.0f)));
//   group1.addChild(m_j3d.createBox(new Vector3d(0.0f,-.15f,0.0f),new Vector3d(0.25f,0.03f,0.1f), new Vector3d(0f,0f,0f),new Color3f(0f,0f,0.6f)));
   Color3f grey = new Color3f(0.1f,0.1f,0.1f);    
  
   //Hooks
   LineAttributes la = new LineAttributes();
   la.setLineWidth(4.0f);
   ColoringAttributes ca = new ColoringAttributes(grey, ColoringAttributes.SHADE_FLAT);
   Appearance app = new Appearance();
   app.setColoringAttributes(ca);
   app.setLineAttributes(la);
   Point3f[] coords = new Point3f[2];
   coords[0] = new Point3f(0.25f,-.15f,0f);
   coords[1] = new Point3f(0.30f,-.15f,0f);
   LineArray line = new LineArray(2, LineArray.COORDINATES);
   line.setCoordinates(0, coords);
   Shape3D myShape = new Shape3D(line,app);
   
   group1.addChild(myShape); 
   Point3f[] coord = new Point3f[2];
   coord[0] = new Point3f(-0.25f,-.15f,0f);
   coord[1] = new Point3f(-0.30f,-.15f,0f);
   LineArray line1 = new LineArray(2, LineArray.COORDINATES);
   line1.setCoordinates(0, coord);
   Shape3D myShape1 = new Shape3D(line1,app);
   group1.addChild(myShape1);
   
   
   Point3f[] cords1 = new Point3f[2];
   int k;
   for(k=10;k<=450;k++)
   {
	   cords1[0] = new Point3f((float)(.04*Math.cos(k*3.14/360)),(float)(.04*Math.sin(k*3.14/360)),0f);
	   cords1[1] = new Point3f((float)(.04*Math.cos((k+1)*3.14/360)),(float)(.04*Math.sin((k+1)*3.14/360)),0f);
   	
   	   LineArray line3 = new LineArray(2, LineArray.COORDINATES);
       line3.setCoordinates(0, cords1);
       Shape3D myShape3 = new Shape3D(line3,app);
       TransformGroup tg = new TransformGroup();

       Transform3D transform1 = new Transform3D();

       Vector3f vector1 = new Vector3f( -.35f, -.16f, -0.1f);

       transform1.setTranslation(vector1);

       tg.setTransform(transform1);

       tg.addChild(myShape3);
       
       group1.addChild(tg);

   }

   Point3f[] cords2 = new Point3f[2];
   for(k=-170;k<=250;k++)
   {
	   cords2[0] = new Point3f((float)(.04*Math.sin(k*3.14/360)),(float)(.04*Math.cos(k*3.14/360)),0f);
	   cords2[1] = new Point3f((float)(.04*Math.sin((k+1)*3.14/360)),(float)(.04*Math.cos((k+1)*3.14/360)),0f);
   	
   	   LineArray line3 = new LineArray(2, LineArray.COORDINATES);
       line3.setCoordinates(0, cords2);
       Shape3D myShape3 = new Shape3D(line3,app);
       TransformGroup tg = new TransformGroup();

       Transform3D transform1 = new Transform3D();

       Vector3f vector1 = new Vector3f( .35f, -.16f, -0.1f);

       transform1.setTranslation(vector1);

       tg.setTransform(transform1);

       tg.addChild(myShape3);
       
       group1.addChild(tg);

   }
   Color3f brown = new Color3f(0.45f,0.2f,0f);
   LineAttributes laa = new LineAttributes();
   laa.setLineWidth(6.5f);
   ColoringAttributes caa = new ColoringAttributes(brown, ColoringAttributes.SHADE_FLAT);
   Appearance appe = new Appearance();
   appe.setColoringAttributes(caa);
   appe.setLineAttributes(laa);

   Point3f[] cords3 = new Point3f[2];
   for(k=0;k<=720;k++)
   {
	   cords3[0] = new Point3f((float)(.02*Math.cos(k*3.14/360)),.0009f,(float)(.02*Math.sin(k*3.14/360)));
	   cords3[1] = new Point3f((float)(.02*Math.cos((k+1)*3.14/360)),.0009f,(float)(.02*Math.sin((k+1)*3.14/360)));
   	
   	   LineArray line4 = new LineArray(2, LineArray.COORDINATES);
       line4.setCoordinates(0, cords3);
       Shape3D myShape4 = new Shape3D(line4,app);
       TransformGroup tg = new TransformGroup();

       Transform3D transform1 = new Transform3D();

       Vector3f vector1 = new Vector3f( -.39f, -.16f, -0.1f);

       transform1.setTranslation(vector1);

       tg.setTransform(transform1);

       tg.addChild(myShape4);
       
       group1.addChild(tg);

   }

   Point3f[] cords4 = new Point3f[2];
   for(k=0;k<=720;k++)
   {
	   cords4[0] = new Point3f((float)(.02*Math.cos(k*3.14/360)),.0009f,(float)(.02*Math.sin(k*3.14/360)));
	   cords4[1] = new Point3f((float)(.02*Math.cos((k+1)*3.14/360)),.0009f,(float)(.02*Math.sin((k+1)*3.14/360)));
   	
   	   LineArray line4 = new LineArray(2, LineArray.COORDINATES);
       line4.setCoordinates(0, cords4);
       Shape3D myShape4 = new Shape3D(line4,app);
       TransformGroup tg = new TransformGroup();

       Transform3D transform1 = new Transform3D();

       Vector3f vector1 = new Vector3f( .39f, -.16f, -0.1f);

       transform1.setTranslation(vector1);

       tg.setTransform(transform1);

       tg.addChild(myShape4);
       
       group1.addChild(tg);

   }
   Point3f[] coord5 = new Point3f[2];
   coord5[0] = new Point3f(-0.39f,-.153f,0f);
   coord5[1] = new Point3f(-0.82f,-.153f,0f);
   LineArray line5 = new LineArray(2, LineArray.COORDINATES);
   line5.setCoordinates(0, coord5);
   Shape3D myShape5 = new Shape3D(line5,appe);
   group1.addChild(myShape5);
   
   Point3f[] coord6 = new Point3f[2];
   coord6[0] = new Point3f(0.39f,-.153f,0f);
   coord6[1] = new Point3f(0.82f,-.153f,0f);
   LineArray line6 = new LineArray(2, LineArray.COORDINATES);
   line6.setCoordinates(0, coord6);
   Shape3D myShape6 = new Shape3D(line6,appe);
   group1.addChild(myShape6);
   
   
   //group1.addChild(m_j3d.createCylinder(new Vector3d(-.58f,-.15f,0f),new Vector3d(0.1f,4f,0.3f),new Vector3d(0,0,90),new Color3f(0.4f,0.2f,0f)));
   
   Color3f light1Color = new Color3f(.1f, 1.4f, .1f); // green light
   BoundingSphere bounds =
   new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
   Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
   DirectionalLight light1
   = new DirectionalLight(light1Color, light1Direction);

   light1.setInfluencingBounds(bounds);
   //group1.addChild(light1);
   universe.getViewingPlatform().setNominalViewingTransform();

   
   universe.addBranchGraph(group1);

}

public static void main( String[] args ) {

   new myprog();

}
}