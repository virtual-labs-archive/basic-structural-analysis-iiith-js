package eerc.vlab.common;

import java.awt.Font;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Group;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.ImageIcon;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.image.TextureLoader;

public class J3DShape extends javax.swing.JPanel{

	private boolean spin = false;
	private boolean noTriangulate = false;
	private boolean noStripify = false;
	private double creaseAngle = 60.0;
	
	/** Returns an ImageIcon, or null if the path was invalid. */
    public  ImageIcon createImageIcon(String path) {
        URL filename = Resources.getResource(path);
        if (filename != null) {
            return new ImageIcon(filename);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    
	public Color3f getColor3f(int r,int g,int b){
		return new Color3f(r/255.0f,g/255.0f,b/255.0f);
	}
	
	@SuppressWarnings("restriction")
	public Group loadObjectFile(String objfile,Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr) {
    	
    	
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
//	   			Shape3D newShape = (Shape3D) bg.getChild(0);
//	   			bg.removeChild(0);
//	   			Appearance app = new Appearance();
//	   			Color3f objColor = new Color3f(0.7f, 0.2f, 0.8f);
//	   			Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
//	   			app.setMaterial(new Material(objColor, black, objColor, black, 80.0f));
	//
//	   			newShape.setAppearance(app);
	//
//	   			objTrans.addChild(newShape);
	   			
//	   			Map<String, Shape3D> nameMap = s.getNamedObjects();   
//	   			   
//	   			for (String name : nameMap.keySet()) {  
//	   			        System.out.printf("Name: %s\n", name);   
//	   			} 
	   			//bg.addChild(trans);
	   			//objTrans.addChild(bg);
	   			objTrans.addChild(s.getSceneGroup());
	       

	        return objTrans;
   }
	
	public TransformGroup createPendulum(Vector3d pos, Vector3d scale) {
    	Transform3D ts = new Transform3D();
    	ts = new Transform3D();
        ts.setScale(scale);
	    ts.setTranslation(pos);
	    
    	TransformGroup objtrans = new TransformGroup(ts);
    	objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); 
    	objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        
        // Rectangle
    	Shape3D shape = createRectangleShape(new Color3f(1f, 0f, 0f),null);
        //ts=new Transform3D();
	    ts.setScale(.01);
	    ts.setTranslation(new Vector3d(0,0, 0));
	    TransformGroup tg = new TransformGroup(ts);
	    tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); 
	    tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ); 	    
	    tg.addChild(shape);
	    objtrans.addChild(tg);
        
//      -------------Bob---------//   
	    
        Appearance app = new Appearance();
        ColoringAttributes ca = new ColoringAttributes();
        //ca.setColor(new Color3f(.5f,.1f,.5f));
        app.setCapability( Appearance.ALLOW_MATERIAL_READ |  Appearance.ALLOW_MATERIAL_WRITE);
        //app.setColoringAttributes(ca);
        Color3f r1= new Color3f(.5f,.5f,0.5f);
        Color3f b1 = new Color3f(0.3f,.5f,0.6f);       
        app.setMaterial(new Material(r1, b1, r1, b1, .9f));
        
        
	    
	    //ts=new Transform3D();
	    ts.setScale(.7);
	    ts.setTranslation(new Vector3d(0,0.4,-.1));
	    tg = new TransformGroup(ts);
	    tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); 
	    tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ); 
	    
	    Sphere sp= new Sphere(.025f,app);
	    tg.addChild(sp);
	    
	    objtrans.addChild(tg);

	    
	    
	    //-----------Rod-----------//
	   
	    objtrans.addChild(createLine(new Point3d(0.0,.1,0), new Point3d(0.0,-.1,0),
	    		new Vector3d(0,0.2,-.1),new Vector3d(1,2,1), new Color3f(.0f,.0f,0.0f)));
	    
//        app = new Appearance();
//        ca = new ColoringAttributes();
//        ca.setColor(new Color3f(.5f,.5f,.5f));
//        app.setCapability(app.ALLOW_COLORING_ATTRIBUTES_WRITE);
//        app.setColoringAttributes(ca);
//
//        Color3f red = new Color3f(0.5f,.5f,0.5f);
//        Color3f black = new Color3f(0f,0f,0f);
//        app.setMaterial(new Material(red, black, red, black, 1.0f));
//
//        ts=new Transform3D();
//        ts.setScale(new Vector3d(.1,.5,1));
//        ts.setTranslation(new Vector3d(0,0.2,-.1));
//	    tg = new TransformGroup(ts);
//	    tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); 
//	    tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ); 
//	    
//	    Cylinder cl= new Cylinder(.008f,1f,app);
//	    tg.addChild(cl);
	    
//	    objtrans.addChild(tg);
	    
	    return objtrans;
		
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
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(colr);
        app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        app.setColoringAttributes(ca);
        
        objtrans.addChild(new Box(1.0f, 1.0f, 1.0f,app));
        
        return objtrans;
    }
   
    //default cylinder of radius of 1.0 and height of 2.0.
    public Group createCylinder(float r, float h,Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr,String id,HashMap hmap) {
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
        objtrans.addChild(new Cylinder(r,h,app));
        
        if(id!=null)
 	    	 hmap.put(id, objtrans);
        
        return objtrans;
    }
    public Group createTextureCylinder(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr,String texfile,String id,HashMap hmap) {
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

 	    if(id!=null)
 	    	 hmap.put(id, objtrans);
 	    


        return objtrans;
    }

    
    public Group createCylinder(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr) {
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
       objtrans.addChild(new Cylinder(0.1f,0.1f,app));
      
       
       return objtrans;
   }
   
    public Group createCylinder(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr,String id,HashMap hmap) {
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
        objtrans.addChild(new Cylinder(0.1f,0.1f,app));
        
        if(id!=null)
 	    	 hmap.put(id, objtrans);
        
        return objtrans;
    }
    
//   The Material object defines the appearance of an object under illumination. If the Material object in an Appearance object is null, lighting is disabled for all nodes that use that Appearance object.
//
//   The properties that can be set for a Material object are:
//
//       * Ambient color - the ambient RGB color reflected off the surface of the material. The range of values is 0.0 to 1.0. The default ambient color is (0.2, 0.2, 0.2).
//
//       * Diffuse color - the RGB color of the material when illuminated. The range of values is 0.0 to 1.0. The default diffuse color is (1.0, 1.0, 1.0).
//
//       * Specular color - the RGB specular color of the material (highlights). The range of values is 0.0 to 1.0. The default specular color is (1.0, 1.0, 1.0).
//
//       * Emissive color - the RGB color of the light the material emits, if any. The range of values is 0.0 to 1.0. The default emissive color is (0.0, 0.0, 0.0).
//
//       * Shininess - the material's shininess, in the range [1.0, 128.0] with 1.0 being not shiny and 128.0 being very shiny. Values outside this range are clamped. The default value for the material's shininess is 64.
//
//       * Color target - the material color target for per-vertex colors, one of: AMBIENT, EMISSIVE, DIFFUSE, SPECULAR, or AMBIENT_AND_DIFFUSE. The default target is DIFFUSE.
       
    public Group createCylinderWithMatProp(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr,Color3f ambientColor, Color3f emissiveColor, Color3f diffuseColor, Color3f specularColor, float shininess) {
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
//       Material(Color3f ambientColor, Color3f emissiveColor, Color3f diffuseColor, Color3f specularColor, float shininess) 
       app.setMaterial(new Material(ambientColor,emissiveColor,diffuseColor,specularColor,shininess));
       objtrans.addChild(new Cylinder(0.1f,0.1f,app));
      
       
       return objtrans;
   }
       
       
   public Group createCone(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr,String id,HashMap hmap) {
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
        objtrans.addChild(new Cone(0.05f, 0.1f,app)); 
        
        if(id!=null)
 	    	 hmap.put(id, objtrans);
        
        return objtrans;
    }
    
    public Group createConeWithMatProp(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr,Color3f ambientColor, Color3f emissiveColor, Color3f diffuseColor, Color3f specularColor, float shininess) {
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
//        Material(Color3f ambientColor, Color3f emissiveColor, Color3f diffuseColor, Color3f specularColor, float shininess) 
        app.setMaterial(new Material(ambientColor,emissiveColor,diffuseColor,specularColor,shininess));
        objtrans.addChild(new Cone(0.1f,0.1f,app));
       
        
        return objtrans;
    }
    
   
    
    public Group createBox(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr,String texfile) {
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
        
        if(texfile!=null){
        	Texture tex = new TextureLoader(Resources.getResource(texfile),TextureLoader.BY_REFERENCE | TextureLoader.Y_UP,this).getTexture();
        	app.setTexture(tex);
    	    TextureAttributes texAttr = new TextureAttributes();
    	    texAttr.setTextureMode(TextureAttributes.MODULATE);
    	    app.setTextureAttributes(texAttr);
        }
	    
	    
	    objtrans.addChild(new Box(1.0f, 1.0f, 1.0f,Box.GENERATE_TEXTURE_COORDS | Box.GENERATE_TEXTURE_COORDS_Y_UP, app));
        
	    
        return objtrans;
    }
    
    public Group createTextureBox(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr,String texfile,String id,HashMap hmap) {
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
        
        if(texfile!=null){
        	Texture tex = new TextureLoader(Resources.getResource(texfile),TextureLoader.BY_REFERENCE | TextureLoader.Y_UP,this).getTexture();
        	app.setTexture(tex);
    	    TextureAttributes texAttr = new TextureAttributes();
    	    texAttr.setTextureMode(TextureAttributes.MODULATE);
    	    app.setTextureAttributes(texAttr);
        }
    
  
	   
	    objtrans.addChild(new  Box(1.0f, 1.0f, 1.0f,Box.GENERATE_TEXTURE_COORDS | Box.GENERATE_TEXTURE_COORDS_Y_UP, app));
        
	    if(id!=null)
	    	 hmap.put(id, objtrans);
	    
        return objtrans;
    }
    
    public Group createText2D(String str,Vector3d pos,Vector3d scale,Color3f colr,int font,int type) {
   	 
    	 Transform3D ts = new Transform3D();
    	 Text2D text2d  = new Text2D(str, colr,	"Times New Roman", font, type);
         text2d.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
  		 text2d.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
  		//hm.put("hdr_txt", text2d);
  		 ts = new Transform3D();
  		 ts.setScale(scale);
  	     ts.setTranslation(pos);
  	     TransformGroup objtrans = new TransformGroup(ts);
  	     objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  	     objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
  	    
  	    
  	     Float f=text2d.getRectangleScaleFactor();
  	     text2d.setRectangleScaleFactor(f);
  	     objtrans.addChild(text2d);  	     
  	     
         return objtrans;
    }
    
    public Group createFont3D(String str,Vector3d pos,Vector3d scale,Color3f colr, String key) {
    	 
    	 TransformGroup objtrans = new TransformGroup();
    	 Transform3D ts = new Transform3D();
    	 ts.setTranslation(pos);
    	 ts.setScale(scale);
     	
     	 objtrans.setTransform(ts);
     	
    	 Appearance textAppear = new Appearance();
         ColoringAttributes textColor = new ColoringAttributes();
         textColor.setColor(colr);
         textAppear.setColoringAttributes(textColor);
        
         Font3D font3D = new Font3D(new Font("Times New Roman", Font.PLAIN, 1),  new FontExtrusion());
         Text3D textGeom = new Text3D(font3D, str);
         textGeom.setAlignment(Text3D.ALIGN_FIRST);
         textGeom.setCapability(Text3D.ALLOW_STRING_WRITE);
         textGeom.setCapability(Text3D.ALLOW_STRING_READ);
        
         Shape3D textShape = new Shape3D();
         textShape.setGeometry(textGeom);
         textShape.setAppearance(textAppear);
                         
     	 objtrans.addChild(textShape);
         return objtrans;
    }
      
    public Geometry createLegGeom(Point3d p1, Point3d p2,Color3f colr){
    	   	
		    int size=2;
		    
		    LineArray xline =new LineArray(size,LineArray.COORDINATES | LineArray.COLOR_3);
		    
		    for(int i=0; i< size; i++)    	xline.setColor(i,colr);
		    
		    
		    Point3d line_verts[] = new Point3d[size];
		    
		    line_verts[0] = p1;
		    line_verts[1] = p2;
		    
		    xline.setCoordinates(0, line_verts);
		 		    
		    return xline;
	    
	 
    }
    
    public Group createTextureCube(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr,String texfile,String id,HashMap hmap){
	   	
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
     
 	     Appearance app = new Appearance();
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(colr);
      //app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        app.setColoringAttributes(ca);
      
        if(texfile!=null){
        	Texture tex = new TextureLoader(Resources.getResource(texfile),TextureLoader.BY_REFERENCE | TextureLoader.Y_UP,this).getTexture();
        	app.setTexture(tex);
    	    TextureAttributes texAttr = new TextureAttributes();
    	    texAttr.setTextureMode(TextureAttributes.MODULATE);
    	    app.setTextureAttributes(texAttr);
        }
 	    
 	     Shape3D shape = new Shape3D(createBoxGeom(0),app);
 	     shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE | Shape3D.ALLOW_GEOMETRY_READ);
 	    
 	     if(id!=null)
 	    	 hmap.put(id, shape);
 	     objtrans.addChild(shape);		 
     
 	    
 	     return objtrans;

    }   
     
    public Group createRectangle(Vector3d pos,Vector3d scale,Color3f colr,String texfile) { //,
         // Create a transform group node to scale and position the object.
     	
//         QuadArray polygon1 = new QuadArray (4,QuadArray.COORDINATES | GeometryArray.TEXTURE_COORDINATE_2);
//     	polygon1.setCoordinate (0, new Point3f (0f, 0f, 0f));
//     	polygon1.setCoordinate (1, new Point3f (2f, 0f, 0f));
//     	polygon1.setCoordinate (2, new Point3f (2f, 3f, 0f));
//     	polygon1.setCoordinate (3, new Point3f (0f, 3f, 0f));
 //
//       // Then setup the texture coordinates (0,0), (1,0), (1,1), (0,1):
 //
// 	    polygon1.setTextureCoordinate (0, new Point2f(0.0f,0.0f));
// 	    polygon1.setTextureCoordinate (1, new Point2f(1.0f,0.0f)); 
//         polygon1.setTextureCoordinate (2, new Point2f(1.0f,1.0f));
//         polygon1.setTextureCoordinate (3, new Point2f(0.0f,1.0f));
     	//Anti-clockwise
     	ArrayList<Point3f> vrtx = new ArrayList<Point3f> ();
 		vrtx.add(new Point3f(-1.0f, 1.0f, 0.0f));
 		vrtx.add(new Point3f(-1.0f, -1.0f, 0.0f));
 		vrtx.add(new Point3f(1.0f, -1.0f, 0.0f));
 		vrtx.add(new Point3f(1.0f, 1.0f, 0.0f));
 		
         QuadArray plane = new QuadArray(4, GeometryArray.COORDINATES   | GeometryArray.TEXTURE_COORDINATE_2);
         for(int i=0; i< vrtx.size(); i++)
         	plane.setCoordinate(i, vrtx.get(i));
 	   
 	    // Texture co-ordinate
 	    TexCoord2f q = new TexCoord2f();
 	    q.set(0.0f, 1.0f);
 	    plane.setTextureCoordinate(0, 0, q);
 	    q.set(0.0f, 0.0f);
 	    plane.setTextureCoordinate(0, 1, q);
 	    q.set(1.0f, 0.0f);
 	    plane.setTextureCoordinate(0, 2, q);
 	    q.set(1.0f, 1.0f);
 	    plane.setTextureCoordinate(0, 3, q);
     		   
         Transform3D t = new Transform3D();
         //t.set(scale, pos);
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
         
         if(texfile != null){
         	Texture tex = new TextureLoader(Resources.getResource(texfile),TextureLoader.BY_REFERENCE | TextureLoader.Y_UP,this).getTexture();
     	    app.setTexture(tex);
     	    TextureAttributes texAttr = new TextureAttributes();
     	    texAttr.setTextureMode(TextureAttributes.MODULATE);
     	    app.setTextureAttributes(texAttr);
         }
         
 	    
 	    Shape3D shape = new Shape3D(plane,app);
 	    shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE | Shape3D.ALLOW_GEOMETRY_READ);
 	    
 	    objtrans.addChild(shape);
         
         return objtrans;
    }
    
    public Geometry createRectangleGeom(float disp){
	   	
//    	Anti-clockwise
        QuadArray plane = new QuadArray(4, GeometryArray.COORDINATES   | GeometryArray.TEXTURE_COORDINATE_2);
        
       // for(int i=0; i< vrtx.size(); i++)
        	//plane.setCoordinate(i, vrtx.get(i));
        Point3f p = new Point3f();
	    p.set(-1.0f + disp, 1.0f, 0.0f);
	    plane.setCoordinate(0, p);
	    
	    p.set(-1.0f, -1.0f, 0.0f);
	    plane.setCoordinate(1, p);
	    
	    p.set( 1.0f, -1.0f, 0.0f);
	    plane.setCoordinate(2, p);
	    
	    p.set( 1.0f + disp, 1.0f, 0.0f);
	    plane.setCoordinate(3, p);
	   
	    // Texture co-ordinate
	    TexCoord2f q = new TexCoord2f();
	    q.set(0.0f, 1.0f);
	    plane.setTextureCoordinate(0, 0, q);
	    q.set(0.0f, 0.0f);
	    plane.setTextureCoordinate(0, 1, q);
	    q.set(1.0f, 0.0f);
	    plane.setTextureCoordinate(0, 2, q);
	    q.set(1.0f, 1.0f);
	    plane.setTextureCoordinate(0, 3, q);
	 		    
	    return plane;
 
    }
    
    public Group createLine(Point3d p1, Point3d p2,Vector3d pos, Vector3d scale,Color3f colr) { //,
        // Create a transform group node to scale and position the object.
    	
    	//Anti-clockwise
  	    int size=2;
		    
		    LineArray line =new LineArray(size,LineArray.COORDINATES | LineArray.COLOR_3);
		    
		    for(int i=0; i< size; i++)    	line.setColor(i,colr);
		    
		    
		    Point3d line_verts[] = new Point3d[size];
		    
		    line_verts[0] = p1;
		    line_verts[1] = p2;
		    
		    line.setCoordinates(0, line_verts);
		    
		    Appearance app = new Appearance();
		    LineAttributes la = new LineAttributes();
		    la.setLineWidth(1);
		    app.setLineAttributes(la);
		 		    
		    Shape3D shape = new Shape3D(line,app);
		    
		    Transform3D ts = new Transform3D();
	   	    TransformGroup objTrans = new TransformGroup();
	   	    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	   	    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    	ts.setScale(scale);
	    	ts.setTranslation(pos);
	    	objTrans.setTransform(ts);
	    	objTrans.addChild(shape);
	     
		    return objTrans;
    }
    public Group createLine1(Point3d p1, Point3d p2,Vector3d pos, Vector3d scale,Color3f colr,int width) { //,
        // Create a transform group node to scale and position the object.
    	
    	//Anti-clockwise
  	    int size=2;
		    
		    LineArray line =new LineArray(size,LineArray.COORDINATES | LineArray.COLOR_3);
		    
		    for(int i=0; i< size; i++)    	line.setColor(i,colr);
		    
		    
		    Point3d line_verts[] = new Point3d[size];
		    
		    line_verts[0] = p1;
		    line_verts[1] = p2;
		    
		    line.setCoordinates(0, line_verts);
		    
		    Appearance app = new Appearance();
		    LineAttributes la = new LineAttributes();
		    la.setLineWidth(width);
		    app.setLineAttributes(la);
		 		    
		    Shape3D shape = new Shape3D(line,app);
		    
		    Transform3D ts = new Transform3D();
	   	    TransformGroup objTrans = new TransformGroup();
	   	    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	   	    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    	ts.setScale(scale);
	    	ts.setTranslation(pos);
	    	objTrans.setTransform(ts);
	    	objTrans.addChild(shape);
	     
		    return objTrans;
    }
    
    public Shape3D createRectangleShape(Color3f colr,String texfile) { //,
        // Create a transform group node to scale and position the object.
    	
    	//Anti-clockwise
        QuadArray plane = new QuadArray(4, GeometryArray.COORDINATES   | GeometryArray.TEXTURE_COORDINATE_2);
	    Point3f p = new Point3f();
	    p.set(-1.0f, 1.0f, 0.0f);
	    plane.setCoordinate(0, p);
	    
	    p.set(-1.0f, -1.0f, 0.0f);
	    plane.setCoordinate(1, p);
	    
	    p.set( 1.0f, -1.0f, 0.0f);
	    plane.setCoordinate(2, p);
	    
	    p.set( 1.0f, 1.0f, 0.0f);
	    plane.setCoordinate(3, p);
	   
	    // Texture co-ordinate
	    TexCoord2f q = new TexCoord2f();
	    q.set(0.0f, 1.0f);
	    plane.setTextureCoordinate(0, 0, q);
	    q.set(0.0f, 0.0f);
	    plane.setTextureCoordinate(0, 1, q);
	    q.set(1.0f, 0.0f);
	    plane.setTextureCoordinate(0, 2, q);
	    q.set(1.0f, 1.0f);
	    plane.setTextureCoordinate(0, 3, q);
	    
        // Create a new ColoringAttributes object for the shape's
        // appearance and make it writable at runtime.
        Appearance app = new Appearance();
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(colr);
        //app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        app.setColoringAttributes(ca);
        
        if(texfile !=null){
	        Texture tex = new TextureLoader(Resources.getResource(texfile),TextureLoader.BY_REFERENCE | TextureLoader.Y_UP,this).getTexture();
		    app.setTexture(tex);
		    TextureAttributes texAttr = new TextureAttributes();
		    texAttr.setTextureMode(TextureAttributes.MODULATE);
		    app.setTextureAttributes(texAttr);
        }
	    
	    Shape3D shape = new Shape3D(plane,app);
	    shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE | Shape3D.ALLOW_GEOMETRY_READ);
	    return shape;
    }
    
     
    // Not in use
   public Group createCuboid(Vector3d pos,Vector3d scale,Vector3d rot,Color3f colr,String texfile){
	   	
 	     Transform3D t = new Transform3D();
 	     float rad = (float)Math.PI/180;
 	     if(rot.x != 0)
			t.rotX(rad*rot.x);
		 else if(rot.y != 0)
			t.rotY(rad*rot.y);
		 else if(rot.z != 0)
			t.rotZ(rad*rot.z);
        
 	     t.setTranslation(pos);
     
         TransformGroup objtrans = new TransformGroup(t);
         objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
         objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
     
 	     Appearance app = new Appearance();
         ColoringAttributes ca = new ColoringAttributes();
         ca.setColor(colr);
      //app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
         app.setColoringAttributes(ca);
      
         if(texfile !=null){
 	        Texture tex = new TextureLoader(Resources.getResource(texfile),TextureLoader.BY_REFERENCE | TextureLoader.Y_UP,this).getTexture();
 		    app.setTexture(tex);
 		    TextureAttributes texAttr = new TextureAttributes();
 		    texAttr.setTextureMode(TextureAttributes.MODULATE);
 		    app.setTextureAttributes(texAttr);
         }
	    
	     Shape3D shape = new Shape3D(createBoxGeom(0),app);
	     shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE | Shape3D.ALLOW_GEOMETRY_READ);
	    	     
	     objtrans.addChild(shape);		      
	    
	     return objtrans;

  }
   
   public Group createCuboid(Vector3d pos,Vector3f dim,Vector3d rot,Color3f colr,String texfile,String id,HashMap hmap){
	   	
	   Transform3D t = new Transform3D();
	   float rad = (float)Math.PI/180;
	     if(rot.x != 0)
			t.rotX(rad*rot.x);
		 else if(rot.y != 0)
			t.rotY(rad*rot.y);
		 else if(rot.z != 0)
			t.rotZ(rad*rot.z); 	     
	   t.setTranslation(pos);
   
       TransformGroup objtrans = new TransformGroup(t);
       objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
       objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
   
	    Appearance app = new Appearance();
       ColoringAttributes ca = new ColoringAttributes();
       ca.setColor(colr);
    //app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
       app.setColoringAttributes(ca);
    
       if(texfile !=null){
	        Texture tex = new TextureLoader(Resources.getResource(texfile),TextureLoader.BY_REFERENCE | TextureLoader.Y_UP,this).getTexture();
		    app.setTexture(tex);
		    TextureAttributes texAttr = new TextureAttributes();
		    texAttr.setTextureMode(TextureAttributes.MODULATE);
		    app.setTextureAttributes(texAttr);
       }
	    
      Shape3D shape = new Shape3D(createBoxGeom(0,dim),app);
      shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE | Shape3D.ALLOW_GEOMETRY_READ);
    
     
      objtrans.addChild(shape);		
      
      if(id!=null)
	    	 hmap.put(id, shape);
	    
      return objtrans;

  }
   
  public Group createCuboidOnBase(Vector3d pos,Vector3d dim,Vector3d rot,Color3f colr,String texfile,String id,HashMap hmap){
	   	
	   Transform3D t = new Transform3D();
	   float rad = (float)Math.PI/180;
	     if(rot.x != 0)
			t.rotX(rad*rot.x);
		 else if(rot.y != 0)
			t.rotY(rad*rot.y);
		 else if(rot.z != 0)
			t.rotZ(rad*rot.z); 	     
	   t.setTranslation(pos);
   
       TransformGroup objtrans = new TransformGroup(t);
       objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
       objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
   
	    Appearance app = new Appearance();
       ColoringAttributes ca = new ColoringAttributes();
       ca.setColor(colr);
    //app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
       app.setColoringAttributes(ca);
    
       if(texfile !=null){
	        Texture tex = new TextureLoader(Resources.getResource(texfile),TextureLoader.BY_REFERENCE | TextureLoader.Y_UP,this).getTexture();
		    app.setTexture(tex);
		    TextureAttributes texAttr = new TextureAttributes();
		    texAttr.setTextureMode(TextureAttributes.MODULATE);
		    app.setTextureAttributes(texAttr);
       }
	    
      Shape3D shape = new Shape3D(createBoxGeomOnBase(dim,0),app);
      shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE | Shape3D.ALLOW_GEOMETRY_READ);
    
     
      objtrans.addChild(shape);		
      
      if(id!=null)
	    	 hmap.put(id, objtrans);
	    
      return objtrans;

  }
  public Group createtrep(Vector3d pos,Vector3d scale ,Vector3d rot,float xleftdisp,float ydisp,float xrightdisp,Color3f colr,String texfile,String id,HashMap hmap){
	   	
	   Transform3D t = new Transform3D();
	   float rad = (float)Math.PI/180;
	     if(rot.x != 0)
			t.rotX(rad*rot.x);
		 else if(rot.y != 0)
			t.rotY(rad*rot.y);
		 else if(rot.z != 0)
			t.rotZ(rad*rot.z); 	     
	   t.setTranslation(pos);
	   t.setScale(scale);
  
      TransformGroup objtrans = new TransformGroup(t);
      objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
      objtrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
  
	    Appearance app = new Appearance();
      ColoringAttributes ca = new ColoringAttributes();
      ca.setColor(colr);
   //app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
      app.setColoringAttributes(ca);
   
      if(texfile !=null){
	        Texture tex = new TextureLoader(Resources.getResource(texfile),TextureLoader.BY_REFERENCE | TextureLoader.Y_UP,this).getTexture();
		    app.setTexture(tex);
		    TextureAttributes texAttr = new TextureAttributes();
		    texAttr.setTextureMode(TextureAttributes.MODULATE);
		    app.setTextureAttributes(texAttr);
      }
	    
     Shape3D shape = new Shape3D(creatrepGeom(xleftdisp,ydisp,xrightdisp),app);
     shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE | Shape3D.ALLOW_GEOMETRY_READ);
   
    
     objtrans.addChild(shape);		
     
     if(id!=null)
	    	 hmap.put(id, objtrans);
	    
     return objtrans;

 }
 
  
   
  public Geometry createBoxGeom(float disp){
      
       double max = 0.5;
       double min = -max;
 
       QuadArray box = new QuadArray(24, QuadArray.COORDINATES| QuadArray.TEXTURE_COORDINATE_2);
 //
       Point3d verts[] = new Point3d[24];
 //
       int i=0;
     // front face
       verts[i++] = new Point3d(max, min, max);
       verts[i++] = new Point3d(max + disp, max, max);
       verts[i++] = new Point3d(min + disp, max, max);
       verts[i++] = new Point3d(min, min, max);
//     // back face
       verts[i++] = new Point3d(min, min, min);
       verts[i++] = new Point3d(min + disp, max, min);
       verts[i++] = new Point3d(max+ disp, max, min);
       verts[i++] = new Point3d(max, min, min);
     // right face
       verts[i++] = new Point3d(max, min, min);
       verts[i++] = new Point3d(max + disp, max, min);
       verts[i++] = new Point3d(max + disp, max, max);
       verts[i++] = new Point3d(max , min, max);
     // left face
       verts[i++] = new Point3d(min, min, max);
       verts[i++] = new Point3d(min + disp, max, max);
       verts[i++] = new Point3d(min + disp, max, min);
       verts[i++] = new Point3d(min , min, min);
     // top face
       verts[i++] = new Point3d(max + disp, max, max);
       verts[i++] = new Point3d(max, max, min);
       verts[i++] = new Point3d(min + disp, max, min);
       verts[i++] = new Point3d(min, max, max);
     // bottom face
       verts[i++] = new Point3d(min  + disp, min, max);
       verts[i++] = new Point3d(min, min, min);
       verts[i++] = new Point3d(max + disp, min, min);
       verts[i++] = new Point3d(max, min, max);
 //
        box.setCoordinates(0, verts);
        i=0;	   
	    // Texture co-ordinate
	    TexCoord2f q = new TexCoord2f();
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
//	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
//	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
//	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);	 		 
       
	    
	    return box;

   	}
 public Geometry creatrepGeom( float leftxdisp,float ydisp,float rightxdisp)
 {
	 double max = 0.5;
     double min = -max;
     float disp= leftxdisp;

     QuadArray box = new QuadArray(24, QuadArray.COORDINATES| QuadArray.TEXTURE_COORDINATE_2);
//
     Point3d verts[] = new Point3d[24];
//
     int i=0;
   // front face
     verts[i++] = new Point3d(max+rightxdisp, min, max);
     verts[i++] = new Point3d(max , max+ydisp, max);
     verts[i++] = new Point3d(min , max, max);
     verts[i++] = new Point3d(min-disp, min, max);
//   // back face
     verts[i++] = new Point3d(min-disp, min, min);
     verts[i++] = new Point3d(min , max, min);
     verts[i++] = new Point3d(max, max+ydisp, min);
     verts[i++] = new Point3d(max+rightxdisp, min, min);
   // right face
     verts[i++] = new Point3d(max+rightxdisp, min, min);
     verts[i++] = new Point3d(max , max+ydisp, min);
     verts[i++] = new Point3d(max , max+ydisp, max);
     verts[i++] = new Point3d(max+rightxdisp , min, max);
   // left face
     verts[i++] = new Point3d(min-disp, min, max);
     verts[i++] = new Point3d(min , max, max);
     verts[i++] = new Point3d(min , max, min);
     verts[i++] = new Point3d(min-disp , min, min);
   // top face
     verts[i++] = new Point3d(max , max+ydisp, max);
     verts[i++] = new Point3d(max, max+ydisp, min);
     verts[i++] = new Point3d(min , max, min);
     verts[i++] = new Point3d(min, max, max);
   // bottom face
     verts[i++] = new Point3d(min  - disp, min, max);
     verts[i++] = new Point3d(min-disp, min, min);
     verts[i++] = new Point3d(max+rightxdisp , min, min);
     verts[i++] = new Point3d(max+rightxdisp, min, max);
//
      box.setCoordinates(0, verts);
      i=0;	   
	    // Texture co-ordinate
	    TexCoord2f q = new TexCoord2f();
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
//	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
//	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
//	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);	 		 
     
	    
	    return box;

 }
  
  public Geometry createBoxGeom(float disp,Vector3f dim){
      
      // double max = 0.5;
       //double min = -max;
       double x = dim.x*0.5;
       double y = dim.y*0.5;
       double z = dim.z*0.5;
       double nx = -x;
       double ny = -y;
       double nz = -z;
       
 
       QuadArray box = new QuadArray(24, QuadArray.COORDINATES| QuadArray.TEXTURE_COORDINATE_2);
 //
       Point3d verts[] = new Point3d[24];
 //
       int i=0;
     // front face
       verts[i++] = new Point3d(x, ny, z);
       verts[i++] = new Point3d(x + disp, y, z);
       verts[i++] = new Point3d(nx + disp, y, z);
       verts[i++] = new Point3d(nx, ny, z);
     // back face
       verts[i++] = new Point3d(nx, ny, nz);
       verts[i++] = new Point3d(nx + disp, y, nz);
       verts[i++] = new Point3d(x+ disp, y, nz);
       verts[i++] = new Point3d(x, ny, nz);
     // right face
       verts[i++] = new Point3d(x, ny, nz);
       verts[i++] = new Point3d(x + disp, y, nz);
       verts[i++] = new Point3d(x + disp, y, z);
       verts[i++] = new Point3d(x , ny, z);
     // left face
       verts[i++] = new Point3d(nx, ny, z);
       verts[i++] = new Point3d(nx + disp, y, z);
       verts[i++] = new Point3d(nx + disp, y, nz);
       verts[i++] = new Point3d(nx , ny, nz);
     // top face
       verts[i++] = new Point3d(x + disp, y, z);
       verts[i++] = new Point3d(x, y, nz);
       verts[i++] = new Point3d(nx + disp, y, nz);
       verts[i++] = new Point3d(nx, y, z);
     // bottom face
       verts[i++] = new Point3d(nx  + disp, ny, z);
       verts[i++] = new Point3d(nx, ny, nz);
       verts[i++] = new Point3d(x + disp, ny, nz);
       verts[i++] = new Point3d(x, ny, z);
 //
        box.setCoordinates(0, verts);
        i=0;
	   
	    // Texture co-ordinate
	    TexCoord2f q = new TexCoord2f();
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
//	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
//	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
//	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);	 		 
       
	    
	    return box;

   	}
public Geometry createBoxGeomInXY(float dispx,float dispz,Vector3f dim){
      
      // double max = 0.5;
       //double min = -max;
       double x = dim.x*0.5;
       double y = dim.y*0.5;
       double z = dim.z*0.5;
       double nx = -x;
       double ny = -y;
       double nz = -z;
       
 
       QuadArray box = new QuadArray(24, QuadArray.COORDINATES| QuadArray.TEXTURE_COORDINATE_2);
 //
       Point3d verts[] = new Point3d[24];
 //
       int i=0;
     // front face
       verts[i++] = new Point3d(x, ny, z);
       verts[i++] = new Point3d(x + dispx, y, z+dispz);
       verts[i++] = new Point3d(nx + dispx, y, z+dispz);
       verts[i++] = new Point3d(nx, ny, z);
     // back face
       verts[i++] = new Point3d(nx, ny, nz);
       verts[i++] = new Point3d(nx + dispx, y, nz+dispz);
       verts[i++] = new Point3d(x+ dispx, y, nz+dispz);
       verts[i++] = new Point3d(x, ny, nz);
     // right face
       verts[i++] = new Point3d(x, ny, nz);
       verts[i++] = new Point3d(x + dispx, y, nz+dispz);
       verts[i++] = new Point3d(x + dispx, y, z+dispz);
       verts[i++] = new Point3d(x , ny, z);
     // left face
       verts[i++] = new Point3d(nx, ny, z);
       verts[i++] = new Point3d(nx + dispx, y, z+dispz);
       verts[i++] = new Point3d(nx + dispx, y, nz+dispz);
       verts[i++] = new Point3d(nx , ny, nz);
     // top face
       verts[i++] = new Point3d(x + dispx, y, z+dispz);
       verts[i++] = new Point3d(x, y, nz);
       verts[i++] = new Point3d(nx + dispx, y, nz+dispz);
       verts[i++] = new Point3d(nx, y, z);
     // bottom face
       verts[i++] = new Point3d(nx  + dispx, ny, z+dispz);
       verts[i++] = new Point3d(nx, ny, nz);
       verts[i++] = new Point3d(x + dispx, ny, nz+dispz);
       verts[i++] = new Point3d(x, ny, z);
 //
        box.setCoordinates(0, verts);
        i=0;
	   
	    // Texture co-ordinate
	    TexCoord2f q = new TexCoord2f();
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
//	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
//	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
//	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);	 		 
       
	    
	    return box;

   	}
  /*
//Return one of the 8 corner points.  The points are numbered as follows:
  //
//              (m,x,m)                        (x,x,m)
//                ------------------------------ 
//               /|  6                        /| 7
//              / |                          / |
//             /  |                         /  |
//            /   |                        /   |
//           /    |                       /    |
//          /     |                      /     |
//         /      |                     /      |
//        /       |                    /       |
//       /        |                   /        |
/(m,x,m)/  3      |         (x,x,x)  /         |
//     /----------------------------/   2      |
//     |          |                 |          |
//     |          |                 |          |      +Y
//     | (m,m,m)) |  5              |          | 8
//     |          |-----------------|----------|      |
//     |         /                  |         /(x,m,m)|
//     |        /                   |        /        |       -Z
//     |       /                    |       /         |
//     |      /                     |      /          |     /
//     |     /                      |     /           |    /
//     |    /                       |    /            |   /
//     |   /                        |   /             |  /
//     |  /                         |  /              | /
//     | /                          | /               |/
//     |/                           |/                ----------------- +X
//     ------------------------------
//    (m,m,x)   4                    (x,m,x) 1
  //
  */
  public Geometry createBoxGeom(float top,float botm){
    
     double max = 0.5;
     double min = -max;

     QuadArray box = new QuadArray(24, QuadArray.COORDINATES| QuadArray.TEXTURE_COORDINATE_2);
//
     Point3d verts[] = new Point3d[24];
//
     int i=0;
   // front face
     verts[i++] = new Point3d(max + botm, min, max);
     verts[i++] = new Point3d(max + top, max, max);
     verts[i++] = new Point3d(min + top, max, max);
     verts[i++] = new Point3d(min +botm, min, max);
//   // back face
     verts[i++] = new Point3d(min + botm, min, min);
     verts[i++] = new Point3d(min + top, max, min);
     verts[i++] = new Point3d(max + top, max, min);
     verts[i++] = new Point3d(max + botm, min, min);
   // right face
     verts[i++] = new Point3d(max + botm, min, min);
     verts[i++] = new Point3d(max + top, max, min);
     verts[i++] = new Point3d(max + top, max, max);
     verts[i++] = new Point3d(max + botm, min, max);
   // left face
     verts[i++] = new Point3d(min + botm, min, max);
     verts[i++] = new Point3d(min + top, max, max);
     verts[i++] = new Point3d(min + top, max, min);
     verts[i++] = new Point3d(min + botm, min, min);
   // top face
     verts[i++] = new Point3d(max + top, max, max);
     verts[i++] = new Point3d(max + botm, max, min);
     verts[i++] = new Point3d(min + top, max, min);
     verts[i++] = new Point3d(min + botm, max, max);
   // bottom face
     verts[i++] = new Point3d(min + top, min, max);
     verts[i++] = new Point3d(min + botm, min, min);
     verts[i++] = new Point3d(max + top, min, min);
     verts[i++] = new Point3d(max + botm, min, max);
//
      box.setCoordinates(0, verts);
      i=0;
	   
	    // Texture co-ordinate
	    TexCoord2f q = new TexCoord2f();
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
//	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
//	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);
//	    
	    q.set(0.0f, 1.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(0.0f, 0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,0.0f);
	    box.setTextureCoordinate(0, i++, q);
	    q.set(1.0f,1.0f);
	    box.setTextureCoordinate(0, i++, q);	 		 
     
	    
	    return box;

 	}
  
  public Geometry createBoxGeom(Vector3d dim,float top,float botm){
	    
	  double x = dim.x*0.5;
      double y = dim.y*0.5;
      double z = dim.z*0.5;
      double nx = -x;
      double ny = -y;
      double nz = -z;

	     QuadArray box = new QuadArray(24, QuadArray.COORDINATES| QuadArray.TEXTURE_COORDINATE_2);
	//
	     Point3d verts[] = new Point3d[24];
	//
	     int i=0;
	   // front face
	     verts[i++] = new Point3d(x + botm, ny, z);
	     verts[i++] = new Point3d(x + top, y, z);
	     verts[i++] = new Point3d(nx + top, y, z);
	     verts[i++] = new Point3d(nx +botm, ny, z);
//	   // back face
	     verts[i++] = new Point3d(nx + botm, ny, nz);
	     verts[i++] = new Point3d(nx + top, y, nz);
	     verts[i++] = new Point3d(x + top, y, nz);
	     verts[i++] = new Point3d(x + botm, ny, nz);
	   // right face
	     verts[i++] = new Point3d(x + botm, ny, nz);
	     verts[i++] = new Point3d(x + top, y, nz);
	     verts[i++] = new Point3d(x + top, y, z);
	     verts[i++] = new Point3d(x + botm, ny, z);
	   // left face
	     verts[i++] = new Point3d(nx + botm, ny, z);
	     verts[i++] = new Point3d(nx + top, y, z);
	     verts[i++] = new Point3d(nx + top, y, nz);
	     verts[i++] = new Point3d(nx + botm, ny, nz);
	   // top face
	     verts[i++] = new Point3d(x + top, y, z);
	     verts[i++] = new Point3d(x + botm, y, nz);
	     verts[i++] = new Point3d(nx + top, y, nz);
	     verts[i++] = new Point3d(nx + botm, y, z);
	   // bottom face
	     verts[i++] = new Point3d(nx + top, ny, z);
	     verts[i++] = new Point3d(nx + botm, ny, nz);
	     verts[i++] = new Point3d(x + top, ny, nz);
	     verts[i++] = new Point3d(x + botm, ny, z);
	//
	      box.setCoordinates(0, verts);
	      i=0;
		   
		    // Texture co-ordinate
		    TexCoord2f q = new TexCoord2f();
		    q.set(0.0f, 1.0f);
		    box.setTextureCoordinate(0, i++, q);
		    q.set(0.0f, 0.0f);
		    box.setTextureCoordinate(0, i++, q);
		    q.set(1.0f,0.0f);
		    box.setTextureCoordinate(0, i++, q);
		    q.set(1.0f,1.0f);
		    box.setTextureCoordinate(0, i++, q);
		    
		    q.set(0.0f, 1.0f);
		    box.setTextureCoordinate(0, i++, q);
		    q.set(0.0f, 0.0f);
		    box.setTextureCoordinate(0, i++, q);
		    q.set(1.0f,0.0f);
		    box.setTextureCoordinate(0, i++, q);
		    q.set(1.0f,1.0f);
		    box.setTextureCoordinate(0, i++, q);
		    
		    q.set(0.0f, 1.0f);
		    box.setTextureCoordinate(0, i++, q);
		    q.set(0.0f, 0.0f);
		    box.setTextureCoordinate(0, i++, q);
		    q.set(1.0f,0.0f);
		    box.setTextureCoordinate(0, i++, q);
		    q.set(1.0f,1.0f);
		    box.setTextureCoordinate(0, i++, q);
//		    
		    q.set(0.0f, 1.0f);
		    box.setTextureCoordinate(0, i++, q);
		    q.set(0.0f, 0.0f);
		    box.setTextureCoordinate(0, i++, q);
		    q.set(1.0f,0.0f);
		    box.setTextureCoordinate(0, i++, q);
		    q.set(1.0f,1.0f);
		    box.setTextureCoordinate(0, i++, q);
//		    
		    q.set(0.0f, 1.0f);
		    box.setTextureCoordinate(0, i++, q);
		    q.set(0.0f, 0.0f);
		    box.setTextureCoordinate(0, i++, q);
		    q.set(1.0f,0.0f);
		    box.setTextureCoordinate(0, i++, q);
		    q.set(1.0f,1.0f);
		    box.setTextureCoordinate(0, i++, q);
//		    
		    q.set(0.0f, 1.0f);
		    box.setTextureCoordinate(0, i++, q);
		    q.set(0.0f, 0.0f);
		    box.setTextureCoordinate(0, i++, q);
		    q.set(1.0f,0.0f);
		    box.setTextureCoordinate(0, i++, q);
		    q.set(1.0f,1.0f);
		    box.setTextureCoordinate(0, i++, q);	 		 
	     
		    
		    return box;

	 }
  
  	public Geometry createBoxGeomOnBase(Vector3d dim,float disp){
	    
//  	 double max = 0.5;
        //double min = -max;
        double x = dim.x*0.5;
        double y = dim.y;
        double z = dim.z*0.5;
        double nx = -x;
        double ny = 0;
        double nz = -z;
        
  
        QuadArray box = new QuadArray(24, QuadArray.COORDINATES| QuadArray.TEXTURE_COORDINATE_2);
  //
        Point3d verts[] = new Point3d[24];
  //
        int i=0;
      // front face
        verts[i++] = new Point3d(x, ny, z);
        verts[i++] = new Point3d(x + disp, y, z);
        verts[i++] = new Point3d(nx + disp, y, z);
        verts[i++] = new Point3d(nx, ny, z);
      // back face
        verts[i++] = new Point3d(nx, ny, nz);
        verts[i++] = new Point3d(nx + disp, y, nz);
        verts[i++] = new Point3d(x+ disp, y, nz);
        verts[i++] = new Point3d(x, ny, nz);
      // right face
        verts[i++] = new Point3d(x, ny, nz);
        verts[i++] = new Point3d(x + disp, y, nz);
        verts[i++] = new Point3d(x + disp, y, z);
        verts[i++] = new Point3d(x , ny, z);
      // left face
        verts[i++] = new Point3d(nx, ny, z);
        verts[i++] = new Point3d(nx + disp, y, z);
        verts[i++] = new Point3d(nx + disp, y, nz);
        verts[i++] = new Point3d(nx , ny, nz);
      // top face
        verts[i++] = new Point3d(x + disp, y, z);
        verts[i++] = new Point3d(x, y, nz);
        verts[i++] = new Point3d(nx + disp, y, nz);
        verts[i++] = new Point3d(nx, y, z);
      // bottom face
        verts[i++] = new Point3d(nx  + disp, ny, z);
        verts[i++] = new Point3d(nx, ny, nz);
        verts[i++] = new Point3d(x + disp, ny, nz);
        verts[i++] = new Point3d(x, ny, z);
  //
         box.setCoordinates(0, verts);
         i=0;
 	   
 	    // Texture co-ordinate
 	    TexCoord2f q = new TexCoord2f();
 	    q.set(0.0f, 1.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    q.set(0.0f, 0.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    q.set(1.0f,0.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    q.set(1.0f,1.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    
 	    q.set(0.0f, 1.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    q.set(0.0f, 0.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    q.set(1.0f,0.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    q.set(1.0f,1.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    
 	    q.set(0.0f, 1.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    q.set(0.0f, 0.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    q.set(1.0f,0.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    q.set(1.0f,1.0f);
 	    box.setTextureCoordinate(0, i++, q);
// 	    
 	    q.set(0.0f, 1.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    q.set(0.0f, 0.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    q.set(1.0f,0.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    q.set(1.0f,1.0f);
 	    box.setTextureCoordinate(0, i++, q);
// 	    
 	    q.set(0.0f, 1.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    q.set(0.0f, 0.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    q.set(1.0f,0.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    q.set(1.0f,1.0f);
 	    box.setTextureCoordinate(0, i++, q);
// 	    
 	    q.set(0.0f, 1.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    q.set(0.0f, 0.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    q.set(1.0f,0.0f);
 	    box.setTextureCoordinate(0, i++, q);
 	    q.set(1.0f,1.0f);
 	    box.setTextureCoordinate(0, i++, q);	 		 
        
 	    
 	    return box;

	 	}
  
  
	
	
	
	
}