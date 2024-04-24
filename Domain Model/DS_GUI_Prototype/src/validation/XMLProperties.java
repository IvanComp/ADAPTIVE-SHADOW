/**
 * Attention: Generated source! Do not modify by hand!
 * Generated by: Merode Code Generator 2.0
 */
package validation;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;

public class XMLProperties {

    final static String OBJECTS = "mxp:metaobject";
    final static String FSMS = "mxp:metafsms";
    final static String EVENTS = "mxp:metamethod";
    final static String METHODS = "mxp:metatransitionmethod";
    final static String GUI_OBJECTS = "mxp:guiobject";
    final static String GUI_STATES = "mxp:guistate";
    final static String GUI_DEPENDENCY = "mxp:guidependency";
    final static String METADEPENDENCIES = "mxp:metadependency";
    final static String CTRL_POINTS = "mxp:edgecontrolpoint";
    final static String CODE_GENERATION_FSM = "codegeneration";
	final static String MODEL_URL = "/src/validation";  
    static String FILE_ADDRESS = "model.mxp";
    
    //find maximum value among the given
    //==================================================================
    public static float max(float[] t) {
        float maximum = t[0];   // start with the first value
        for (int i = 1; i < t.length; i++) {
            if (t[i] > maximum) {
                maximum = t[i]; // new maximum
            }
        }
        return maximum;
    }
    //==================================================================
    
    //find minimum value among the given
    //==================================================================
    public static float min(float[] t) {
        float minimum = t[0];   // start with the first value
		for (int i = 1; i < t.length; i++) {
            if (t[i] < minimum) {
                minimum = t[i]; // new minimum
            }
        }
        return minimum;
    }
    //==================================================================
 
    //find minimum path among the given 2d point and array of 2d points
    //==================================================================
    public static Point2D[] closestPoint(Point2D p1, Point2D[] p2) {
        float minimum = (float) p1.distance(p2[0]);   // start with the first value
		int k = 0;
        for (int i = 1; i < p2.length; i++) {
            if ((float)p1.distance(p2[i]) < minimum) {
                minimum = (float) p1.distance(p2[i]); // new minimum
                k = i;
            }
        }
        Point2D[] closestPoints = new Point2D[2];
        closestPoints[0] = p1;
        closestPoints[1] = p2[k];
        
        return closestPoints;
    }
    //==================================================================
 
  
    //canvas coordinates for EDG based on min, max x,y used to cut image
    //==================================================================
    public static float[] getEDGCanvasCoordinates(){
        float[] canvas = new float[4];
        float x[] = null;
        float y[] = null;

	    Document doc = buildXMLDocument();
	        
	    NodeList listObjects = doc.getElementsByTagName(GUI_OBJECTS);
	    x = new float [listObjects.getLength()];
	    y = new float [listObjects.getLength()];
        
	    for (int i = 0; i < listObjects.getLength(); i++){
           Element el = (Element) listObjects.item(i);
           String elValue = el.getAttribute("location");
           x[i] = Float.parseFloat(elValue.substring(0, elValue.indexOf("!")));
           y[i] = Float.parseFloat(elValue.substring(elValue.indexOf("!") + 1));
        }
	
	    canvas[0] = min(x);
	    canvas[1] = max(x);
	    canvas[2] = min(y);
	    canvas[3] = max(y);
		return canvas;
    }
    //==================================================================
    
    //canvas coordinates for FSM based on min, max x,y used to cut image
    //due to the absence of label coordinates this function should not be
    //used, instead canvas coordinates are incorporated into the image name
    //==================================================================
    public static float[] getFSMCanvasCoordinates(String fsmObjName){
        float[] canvas = new float[4];
        float x[] = null;
        float y[] = null;

        Document doc = buildXMLDocument();
	    NodeList objects = doc.getElementsByTagName(OBJECTS);
	    NodeList objectFSMs = null;
	    NodeList states = null;
	    
	    //get metafsm subnode of the specified object
	    for (int i = 0; i < objects.getLength(); i++){
	        Element el = (Element) objects.item(i);
	        if (fsmObjName.equals(el.getAttributes().getNamedItem("name").getNodeValue().toUpperCase())){
	        	objectFSMs = el.getElementsByTagName("mxp:metafsm");
	        }
	    }
        //System.out.println(objectFSMs != null ? objectFSMs.toString(): "empty fsms for the specified object");

	    //get subnodes of the node metafsm in the specified object's subnodes
	    //and search for latest user specified fsm in the metafsms of the object
	    for (int k = 0; k < objectFSMs.getLength(); k++){
	        Element el = (Element) objectFSMs.item(k);
        	if("true".equals(el.getAttributes().getNamedItem(CODE_GENERATION_FSM).getNodeValue())
					/*&& lastFSM*/){
        		states = el.getElementsByTagName("mxp:metastate");
			}
	    }
	    
	    Node[] fsmGuiStates = new Node[states.getLength()];
	    for (int k = 0; k < states.getLength(); k++){
	    	Element el = (Element) states.item(k);
	    	int stateId = Integer.valueOf(el.getAttribute("id"));
	    	Node node = getNode(GUI_STATES, stateId, "refid");
	    	fsmGuiStates[k] = node;
	    }
	        
	    x = new float [states.getLength()];
	    y = new float [states.getLength()];
        
	    for (int i = 0; i < fsmGuiStates.length; i++){
           Element el = (Element) fsmGuiStates[i];
           String elValue = el.getAttribute("location");
           x[i] = Float.parseFloat(elValue.substring(0, elValue.indexOf("!")));
           y[i] = Float.parseFloat(elValue.substring(elValue.indexOf("!") + 1));
        }
	
 	    canvas[0] = min(x);
	    canvas[1] = max(x);
 	    canvas[2] = min(y);
 	    canvas[3] = max(y);
 		return canvas;
    }
   //==================================================================

   //returns the id of entity given its name
   //==================================================================  
   public static int getId(String objects, String name){
        int objId = 0;
	    Document doc = buildXMLDocument();
	    
	    String attribute = 
	    	METHODS.equals(objects)? "methodname" : "name";
	    String id = 
	    	METHODS.equals(objects)? "methodid" : "id";
	    
	    NodeList listObjects = doc.getElementsByTagName(objects);
        for (int i = 0; i < listObjects.getLength(); i++){
           String objName = listObjects.item(i).getAttributes().getNamedItem(attribute).getNodeValue();
           if (name.toLowerCase().equals(objName.toLowerCase())){
        	   objId = Integer.valueOf(listObjects.item(i).getAttributes().getNamedItem(id).getNodeValue());
           }
        }
		return objId;
    }
    //==================================================================
 
   
   //returns the entity name given its id
   //==================================================================  
   public static String getObjectName(String id){
	   return getAttributeName(id, OBJECTS, "name");
   }
   //==================================================================
  
   
   //returns the attribute name of entity given its id
   //==================================================================  
   public static String getAttributeName(String id, String objects, String attributeName){
	   String objName = "";
	   String matchObjectId = "";
	   if (OBJECTS.equals(objects)){
		   matchObjectId = "id";
	   } else if (GUI_OBJECTS.equals(objects)){
		   matchObjectId = "refid";
	   } 
	   Document doc = buildXMLDocument();
	   NodeList listObjects = doc.getElementsByTagName(objects);
       for (int i = 0; i < listObjects.getLength(); i++){
    	  NamedNodeMap attributeMap = listObjects.item(i).getAttributes(); 
          if (id.equals(attributeMap.getNamedItem(matchObjectId).getNodeValue())){
        	  objName = attributeMap.getNamedItem(attributeName).getNodeValue();
          }
       }
       return objName;
   }
   //==================================================================
   
 
    //returns the node of xml document given object type, id, and the id property name
    //==================================================================  
    public static Node getNode(String objects, int id, String searchKey){
	    Document doc = buildXMLDocument();
	    Node obj =  null;
	    
	    NodeList listObjects = doc.getElementsByTagName(objects);
        for (int i = 0; i < listObjects.getLength(); i++){
        	if(String.valueOf(id).equals(listObjects.item(i).getAttributes().getNamedItem(searchKey).getNodeValue())){
        		obj = listObjects.item(i);
        	}
        }
		return obj;
    }
    //================================================================== 
	
    //returns master entity names with cardinality constraint of max 1
    //given the dependent name
    //==================================================================
    public static String getMastersWithCardinalityOfMaxOne(String objName) {
	    Document doc = buildXMLDocument();
	    NodeList listObjects = doc.getElementsByTagName(METADEPENDENCIES);
    	int objId = getId(OBJECTS, objName);
    	String masterNames = "";
    	String id = "";
        for (int i = 0; i < listObjects.getLength(); i++){
        	NamedNodeMap attributeMap = listObjects.item(i).getAttributes();
        	if(String.valueOf(objId).equals(attributeMap.getNamedItem("dependent").getNodeValue())){
        		if ("MANDATORY_1".equals(attributeMap.getNamedItem("type").getNodeValue())
        				|| "OPTIONAL_1".equals(attributeMap.getNamedItem("type").getNodeValue())){
        			id = String.valueOf(attributeMap.getNamedItem("master").getNodeValue());
        			masterNames += getObjectName(id).toUpperCase() + ",";      			
        		}
        	}
        }
		return masterNames;
	}
    //================================================================== 

    //returns ctrl points of dependency
    //given the dependency name
    //==================================================================
    public static ArrayList<String> getDependencyCtrlPoints(String dependencyId) {
	    Document doc = buildXMLDocument();
	    NodeList listObjects = doc.getElementsByTagName(GUI_DEPENDENCY);
    	ArrayList<String> coordinates = new ArrayList<String>();
        for (int i = 0; i < listObjects.getLength(); i++){
        	NamedNodeMap attributeMap = listObjects.item(i).getAttributes();
            Element dependency = (Element) listObjects.item(i);
        	if(dependencyId.equals(attributeMap.getNamedItem("refid").getNodeValue())){
                NodeList ctrlPointsList = dependency.getElementsByTagName(CTRL_POINTS);
        		for(int m = 0; m < ctrlPointsList.getLength(); m++){
                    coordinates.add(ctrlPointsList.item(m).getAttributes().getNamedItem("value").getNodeValue());
        		}
        	}
        }
		return coordinates;
	}
    //================================================================== 
    
 
    //returns dependent entity names 
    //==================================================================
    public static String getDependents(String objName) {
	    Document doc = buildXMLDocument();
	    NodeList listObjects = doc.getElementsByTagName(METADEPENDENCIES);
    	int objId = getId(OBJECTS, objName);
    	String dependentNames = "";
    	String id = "";
        for (int i = 0; i < listObjects.getLength(); i++){
        	NamedNodeMap attributeMap = listObjects.item(i).getAttributes();
        	if(String.valueOf(objId).equals(attributeMap.getNamedItem("master").getNodeValue())){
        		id = String.valueOf(attributeMap.getNamedItem("dependent").getNodeValue());
        		dependentNames += getObjectName(id).toUpperCase() + ",";      			
        	}
        }
		return dependentNames;
	}

    //returns dependency ids based on master and dependent name 
    //==================================================================
    public static ArrayList<String> getDependencyIds(String masterName, String depName) {
	    Document doc = buildXMLDocument();
	    NodeList listObjects = doc.getElementsByTagName(METADEPENDENCIES);
    	int mObjId = getId(OBJECTS, masterName);
        int dObjId = getId(OBJECTS, depName);

    	ArrayList<String> dependencyIds = new ArrayList<String>();
        for (int i = 0; i < listObjects.getLength(); i++){
        	NamedNodeMap attributeMap = listObjects.item(i).getAttributes();
        	if(String.valueOf(mObjId).equals(attributeMap.getNamedItem("master").getNodeValue()) &&
            String.valueOf(dObjId).equals(attributeMap.getNamedItem("dependent").getNodeValue())){
        		String dependencyId = String.valueOf(attributeMap.getNamedItem("id").getNodeValue());
        		dependencyIds.add(dependencyId);      			
        	}
        }
		return dependencyIds;
	}
    //================================================================== 
    
    //returns state object coordinates 
    //==================================================================
    public static int getStateId(String stateName, String fsmObjName) {
    	String stateId = "";
    	//boolean lastFSM = true;
	    NodeList states = null;
	    NodeList objectFSMs = getObjectFsms(fsmObjName);
	    
	    //get subnodes of the node metafsm in the specified object's subnodes
	    //and search for latest user specified fsm in the metafsms of the object
	    for (int k = 0; k < objectFSMs.getLength(); k++){
	        Element el = (Element) objectFSMs.item(k);
        	if("true".equals(el.getAttributes().getNamedItem("codegeneration").getNodeValue())
					/*&& lastFSM*/){
        		states = el.getElementsByTagName("mxp:metastate");
        		for(int m = 0; m < states.getLength(); m++){
        			if (stateName.equals(states.item(m).getAttributes().getNamedItem("name").getNodeValue())){
        				stateId = states.item(m).getAttributes().getNamedItem("id").getNodeValue();
        			}
        		}
				//System.out.println("id: " + stateId);
			}
	    }
		return Integer.valueOf(stateId);
	}
    //================================================================== 
 
 
    //returns NodeList of all fsms for the given object
    //==================================================================
    private static NodeList getObjectFsms(String fsmObjName){
 	    Document doc = buildXMLDocument();
 	    NodeList objectFSMs = null;
	    NodeList objects = doc.getElementsByTagName(OBJECTS);
        //get metafsm subnode of the specified object
        for (int i = 0; i < objects.getLength(); i++){
            Element el = (Element) objects.item(i);
            if (fsmObjName.toUpperCase().equals(el.getAttributes().getNamedItem("name").getNodeValue().toUpperCase())){
            	objectFSMs = el.getElementsByTagName("mxp:metafsm");
            }
        }
        //System.out.println(objectFSMs != null ? objectFSMs.toString(): "empty fsms for the specified object");
        return objectFSMs;
    }
    //==================================================================    

    
    //returns the correct state(s) name(s) given the event and object names 
    //==================================================================
    public static String[] getEventStates(String eventName, String fsmObjName) {
	    NodeList objectFSMs = getObjectFsms(fsmObjName);
	    NodeList objStates = null;
	    NodeList transitions = null;
	    String commaSeparatedStateids = "";
	    String commaSeparatedStateNames = "";
 	    
	    //and search for latest user specified fsm in the metafsms of the object
	    for (int k = 0; k < objectFSMs.getLength(); k++){
	        Element el = (Element) objectFSMs.item(k);
        	if("true".equals(el.getAttributes().getNamedItem(CODE_GENERATION_FSM).getNodeValue())
					/*&& lastFSM*/){
        		objStates = el.getElementsByTagName("mxp:metastate");
        		//get all the transitions
        		transitions = el.getElementsByTagName("mxp:metatransition");
        	    //traverse to find the event name in transitions and collect the sourcestateid-s
        		for(int m = 0; m < transitions.getLength(); m++){
        			NamedNodeMap transAttribs = transitions.item(m).getAttributes();
        			Element transition = (Element) transitions.item(m);
        			NodeList transitionMethods = transition.getElementsByTagName("mxp:metatransitionmethod");
        			
        			for(int x = 0; x < transitionMethods.getLength(); x++){
        				NamedNodeMap transMethodAttribs = transitionMethods.item(x).getAttributes();
        				String methodName = transMethodAttribs.getNamedItem("methodname").getNodeValue();
        				if(eventName.toLowerCase().equals(methodName.toLowerCase())){
        					commaSeparatedStateids += transAttribs.getNamedItem("sourcestateid").getNodeValue() + ",";
        				}
        			}
        		}
			}
	    }
	    if (!"".equals(commaSeparatedStateids)){
	    	String[] StateIds = commaSeparatedStateids.split(",");
	    	for (int i = 0; i < StateIds.length; i++){
	    		String id = StateIds[i];
	    		for (int q = 0; q < objStates.getLength(); q++){
        			if (id.equals(objStates.item(q).getAttributes().getNamedItem("id").getNodeValue())){
        				commaSeparatedStateNames += 
        					"".equals(commaSeparatedStateNames) ? objStates.item(q).getAttributes().getNamedItem("name").getNodeValue()
        							: "," + objStates.item(q).getAttributes().getNamedItem("name").getNodeValue();
        				
        			}
	    		}
	    	}
	    }
    	return "".equals(commaSeparatedStateNames) ? new String [0] 
    			: commaSeparatedStateNames.split(",");
	}
    //================================================================== 

    //returns the correct state(s) name(s) given an object name 
    //==================================================================
    public static ArrayList<String> getStateNames(String objectName) {
	    NodeList objectFSMs = getObjectFsms(objectName);
        ArrayList<String> stateNames = new ArrayList<String>();
	    //and search for latest user specified fsm in the metafsms of the object
	    for (int k = 0; k < objectFSMs.getLength(); k++){
	        Element el = (Element) objectFSMs.item(k);
        	if("true".equals(el.getAttributes().getNamedItem(CODE_GENERATION_FSM).getNodeValue())){
                NodeList objStates = el.getElementsByTagName("mxp:metastate");
        		for(int m = 0; m < objStates.getLength(); m++){
        			NamedNodeMap state = objStates.item(m).getAttributes();
                    String stateName = state.getNamedItem("name").getNodeValue();
        			stateNames.add(stateName);
        		}
			}
	    }
    	return stateNames;
	}

    //================================================================== 

    //returns the correct state name given a state id 
    //==================================================================
    public static String getStateNameById(String stateID) {
       Document doc = buildXMLDocument();
	    NodeList objects = doc.getElementsByTagName(OBJECTS);
        //get metafsm subnode of the specified object
        for (int i = 0; i < objects.getLength(); i++){
            NamedNodeMap el = objects.item(i).getAttributes();

            String objectName = el.getNamedItem("name").getNodeValue();
            NodeList objectFSMs = getObjectFsms(objectName);

            for (int k = 0; k < objectFSMs.getLength(); k++){
                Element fsm = (Element) objectFSMs.item(k);
                NodeList objStates = fsm.getElementsByTagName("mxp:metastate");
                for(int m = 0; m < objStates.getLength(); m++){
                    NamedNodeMap state = objStates.item(m).getAttributes();
                    String currStateID = state.getNamedItem("id").getNodeValue();
                    String stateName = state.getNamedItem("name").getNodeValue();
                    if (currStateID.equals(stateID)){
                        return stateName;
                    }
                }
            }
        }
    	return null;
	}


    //================================================================== 

    //returns the correct state type given a state id 
    //==================================================================
    public static String getStateType(String stateID) {
        Document doc = buildXMLDocument();
	    NodeList objects = doc.getElementsByTagName(OBJECTS);
        //get metafsm subnode of the specified object
        for (int i = 0; i < objects.getLength(); i++){
            NamedNodeMap el = objects.item(i).getAttributes();
            String objectName = el.getNamedItem("name").getNodeValue();
            NodeList objectFSMs = getObjectFsms(objectName);

            for (int k = 0; k < objectFSMs.getLength(); k++){
                Element fsm = (Element) objectFSMs.item(k);
                NodeList objStates = fsm.getElementsByTagName("mxp:metastate");
                for(int m = 0; m < objStates.getLength(); m++){
                    NamedNodeMap state = objStates.item(m).getAttributes();
                    String currStateID = state.getNamedItem("id").getNodeValue();
                    String stateType = state.getNamedItem("type").getNodeValue();
                    if (currStateID.equals(stateID)){
                        return stateType;
                    }
                }
            }
        }
    	return null;
	}


    //returns the correct state(s) id(s) given an object name 
    //==================================================================
    public static ArrayList<String> getStateIDs(String objectName) {
	    NodeList objectFSMs = getObjectFsms(objectName);
        ArrayList<String> stateIDs = new ArrayList<String>();
	    //and search for latest user specified fsm in the metafsms of the object
	    for (int k = 0; k < objectFSMs.getLength(); k++){
	        Element el = (Element) objectFSMs.item(k);
        	if("true".equals(el.getAttributes().getNamedItem(CODE_GENERATION_FSM).getNodeValue())){
                NodeList objStates = el.getElementsByTagName("mxp:metastate");
        		for(int m = 0; m < objStates.getLength(); m++){
        			NamedNodeMap state = objStates.item(m).getAttributes();
                    String stateID = state.getNamedItem("id").getNodeValue();
        			stateIDs.add(stateID);
        		}
			}
	    }
    	return stateIDs;
	}
    //================================================================== 

    //returns the correct method name(s) given an object name 
    //==================================================================
    public static ArrayList<String> getMethodNames(String objectName) {
	    NodeList objectFSMs = getObjectFsms(objectName);
        ArrayList<String> methodNames = new ArrayList<String>();
        ArrayList<String> methodIds = new ArrayList<String>();

	    //first find all event ids for this object type
	    for (int k = 0; k < objectFSMs.getLength(); k++){
	        Element el = (Element) objectFSMs.item(k);
        	if("true".equals(el.getAttributes().getNamedItem(CODE_GENERATION_FSM).getNodeValue())){
                NodeList objectMethods = el.getElementsByTagName("mxp:metatransitionmethod");
        		for(int m = 0; m < objectMethods.getLength(); m++){
        			NamedNodeMap method = objectMethods.item(m).getAttributes();
                    String methodId = method.getNamedItem("methodid").getNodeValue();
        			methodIds.add(methodId);
        		}
			}
	    }

        //now find all corresponding method ids
        Document doc = buildXMLDocument();
        NodeList methods = doc.getElementsByTagName("mxp:metamethod");
        for (int k = 0; k < methods.getLength(); k++){
            Element el = (Element) methods.item(k);
            NamedNodeMap method = el.getAttributes();
            String methodId = method.getNamedItem("id").getNodeValue();
            if (methodIds.contains(methodId)) {
                String methodName = method.getNamedItem("name").getNodeValue();
                methodNames.add(methodName);
            }
        }
    	return methodNames;
	}
    //================================================================== 
    
    
    //returns entity x,y coordinates given its name
    //==================================================================
    public static float[] getObjectCoordinates(String name){
        float[] coordinates = new float[2];
        int objId = getId(OBJECTS, name);
        Node el = getNode(GUI_OBJECTS, objId, "refid");
        String location = el.getAttributes().getNamedItem("location").getNodeValue();
        coordinates[0] = Float.parseFloat(location.substring(0, location.indexOf("!")));
        coordinates[1] = Float.parseFloat(location.substring(location.indexOf("!") + 1));
		return coordinates;
    }
    //==================================================================
    
    //returns state x,y coordinates given its id
    //==================================================================
    public static float[] getStateCoordinates(String stateName, String fsmObjName){
        float[] coordinates = new float[2];
        int stateId = getStateId(stateName, fsmObjName);
        Node el = getNode(GUI_STATES, stateId, "refid");
        String location = el.getAttributes().getNamedItem("location").getNodeValue();
        coordinates[0] = Float.parseFloat(location.substring(0, location.indexOf("!")));
        coordinates[1] = Float.parseFloat(location.substring(location.indexOf("!") + 1));
		return coordinates;
    }
    //==================================================================

    //returns entity width, height given its name
    //==================================================================
    public static float[] getObjectSize(String name){
        float[] size = new float[2];
        int objId = getId(OBJECTS, name);
        Node el = getNode(GUI_OBJECTS, objId, "refid");
        String sizeVal = el.getAttributes().getNamedItem("size").getNodeValue();
        size[0] = Float.parseFloat(sizeVal.substring(0, sizeVal.indexOf("!")));
        size[1] = Float.parseFloat(sizeVal.substring(sizeVal.indexOf("!") + 1));
		return size;
    }
    //==================================================================
    
    
    //returns state width, height given its name
    //==================================================================
    public static float[] getStateSize(String stateName, String fsmObjName){
        float[] size = new float[2];
        int objId = getStateId(stateName, fsmObjName);
        Node el = getNode(GUI_STATES, objId, "refid");
        String sizeVal = el.getAttributes().getNamedItem("size").getNodeValue();
        size[0] = Float.parseFloat(sizeVal.substring(0, sizeVal.indexOf("!")));
        size[1] = Float.parseFloat(sizeVal.substring(sizeVal.indexOf("!") + 1));
		return size;
    }
    //==================================================================
    
    //returns the shortest path coordinates given two object names
    //==================================================================
    public static Point2D[] getShortestPath(String dependentObjName,
    		String masterObjName, float minX, float minY){
        //object coordinates
    	Point2D[] dpnd = getShapeCoordinates(dependentObjName, minX, minY);
        //master object coordinates
    	Point2D[] ms = getShapeCoordinates(masterObjName, minX, minY);
        
        Object[]closestPairs = {closestPoint(dpnd[0], ms), 
        		closestPoint(dpnd[1], ms),
        		closestPoint(dpnd[2], ms),
        		closestPoint(dpnd[3], ms)};

        return shortDistance(closestPairs);
    }
    //==================================================================
 
    //returns the shortest path coordinates given two object names
    //==================================================================
    public static Point2D[] getShapeCoordinates(String objName, float minX, float minY){
        float x = 0, y = 0, h = 0, w = 0;
        float topx, bottomx, topy, bottomy, leftx, rightx, lefty, righty; 
        String location, size;
       
        int obj = getId(OBJECTS, objName);
        Node el = getNode(GUI_OBJECTS, obj, "refid");
        
        location = el.getAttributes().getNamedItem("location").getNodeValue();
        x = Float.parseFloat(location.substring(0, location.indexOf("!")));
        y = Float.parseFloat(location.substring(location.indexOf("!") + 1));
		//float minX = getEDGCanvasCoordinates()[0];  
		//float minY = getEDGCanvasCoordinates()[2];
        
        x = x - minX;
        y = y - minY;
        
        size = el.getAttributes().getNamedItem("size").getNodeValue();
        w = Float.parseFloat(size.substring(0, size.indexOf("!")));
        h = Float.parseFloat(size.substring(size.indexOf("!") + 1));
        
        //System.out.println("x : " + x + " y : " + y + " h : " + h + " w : " + w);
        
        //object coordinates
        topx = x + w/2;
        topy = y;
        Point2D p1 = new Point2D.Float(topx, topy);
        bottomx = topx;
        bottomy = y + h;
        Point2D p2 = new Point2D.Float(bottomx, bottomy);
        rightx = x + w;
        righty = y + h/2;
        Point2D p3 = new Point2D.Float(rightx, righty);
        leftx = x;
        lefty = y + h/2;
        Point2D p4 = new Point2D.Float(leftx, lefty);
        Point2D[] shapePoints = { p1, p2, p3, p4 };

 		return shapePoints;
    }
    //==================================================================

    
    //returns the shortest distance coordinates given 2d point and
    //array of any shape 2d points
    //==================================================================
    public static Point2D[] shortDistance(Object[]closestPairs){
        Point2D[] ar = (Point2D[]) closestPairs[0];
        float minimum = (float) ar[0].distance(ar[1]);   
    	for (int i = 1; i < closestPairs.length; i++) {
            Point2D[] arSwap = (Point2D[]) closestPairs[i];
            if ((float)arSwap[0].distance(arSwap[1]) < minimum) {
            	minimum = (float)arSwap[0].distance(arSwap[1]);
            	ar = arSwap;
            }
        }
		return ar;
    }
    
    public static ArrayList<String> getObjectNames(){
        ArrayList<String> objectNames = new ArrayList<String>();
        Document doc = buildXMLDocument();
        NodeList objects = doc.getElementsByTagName(OBJECTS);
        for (int i = 0; i < objects.getLength(); i++){
           Element el = (Element) objects.item(i);
           String elValue = el.getAttribute("name");
           objectNames.add(elValue);
        }
        return objectNames;
    }

    public static ArrayList<String> getObjectNamesWithNoMaster(){
        ArrayList<String> objectNames = new ArrayList<String>();
        Document doc = buildXMLDocument();
        NodeList objects = doc.getElementsByTagName(OBJECTS);
        for (int i = 0; i < objects.getLength(); i++){
            Element el = (Element) objects.item(i);
            String objName = el.getAttribute("name");
            boolean hasMaster = false;
            for (int j = 0; j < objects.getLength(); j++){
                Element el2 = (Element) objects.item(j);
                String objName2 = el2.getAttribute("name");
                if (Arrays.asList(XMLProperties.getDependents(objName2).split(",")).contains(objName)){
                    hasMaster = true;
                }
           }
           if (!hasMaster) {
                objectNames.add(objName);
           }
        }
        return objectNames;
    }

     public static ArrayList<String> getObjectNamesWithNoDependents(){
        ArrayList<String> objectNames = new ArrayList<String>();
        Document doc = buildXMLDocument();
        NodeList objects = doc.getElementsByTagName(OBJECTS);
        for (int i = 0; i < objects.getLength(); i++){
           Element el = (Element) objects.item(i);
           String objName = el.getAttribute("name");
           if (XMLProperties.getDependents(objName).isEmpty()){
                objectNames.add(objName);
           }
        }
        return objectNames;
    }
    public static ArrayList<ArrayList<String>> getAllEDGPathsObject(String topObjName) {
        ArrayList<String> objNames = getObjectNamesWithNoDependents();
        ArrayList<String> botObjNames = new ArrayList<String>();
        for (String objName : objNames) {
            botObjNames.add(objName.toLowerCase());
        }
        return getAllEDGPathsObject(topObjName.toLowerCase(), botObjNames);
    }
    public static ArrayList<ArrayList<String>> getAllEDGPathsObject(String topObjName, ArrayList<String> botObjNames) {
        ArrayList<String> subPath = new ArrayList<String>();
        subPath.add(topObjName);
        return getAllEDGPathsObject(topObjName, botObjNames, subPath);
    }
    enum EXAM {
        CONTINUE,
        ABANDON,
        ACCEPT
      }
      
    private static EXAM examine(ArrayList<String> subPath, ArrayList<String> botObjNames){
        String lastEl = subPath.get(subPath.size() - 1);
        if (botObjNames.contains(lastEl)){
            return EXAM.ACCEPT;
        }
        return EXAM.CONTINUE;
    }

    private static ArrayList<ArrayList<String>> extend(ArrayList<String> subPath){
        String lastEl = subPath.get(subPath.size() - 1);
        String dependents = getDependents(lastEl);
        ArrayList<ArrayList<String>> partialSolutions = new ArrayList<ArrayList<String>>();
        for (String dependent : dependents.split(",")){
            ArrayList<String> cloned_list = new ArrayList<String>(subPath);
            cloned_list.add(dependent.toLowerCase());
            partialSolutions.add(cloned_list);
        }
        return partialSolutions;
    }

    public static ArrayList<ArrayList<String>> getAllEDGPathsObject(String topObjName, ArrayList<String> botObjNames, ArrayList<String> subPath) {
        EXAM exam = examine(subPath, botObjNames);
        if (exam.equals(EXAM.ACCEPT)) {
            ArrayList<ArrayList<String>> solution = new ArrayList<ArrayList<String>>();
            solution.add(subPath);
            return solution;
        }
        else {
            ArrayList<ArrayList<String>> solution = new ArrayList<ArrayList<String>>();
            for (ArrayList<String> partialSolution : extend(subPath)) {
                ArrayList<ArrayList<String>> recSolution = getAllEDGPathsObject(topObjName, botObjNames, partialSolution);
                if (!recSolution.isEmpty()){
                    for (ArrayList<String> sol : recSolution){
                        solution.add(sol);
                    }
                }
            }
            return solution;
        }
    }

    //builds the XML document
    //================================================================== 
    public static Document buildXMLDocument(){
		String userDir = System.getProperty("user.dir");
	    String fileSeparator= System.getProperty("file.separator") ;
	    //userDir = userDir.replace(fileSeparator,"/");
	    String mxpLocation = (userDir + MODEL_URL).replace(fileSeparator,"/"); 
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        Document doc = null;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.parse(new File(mxpLocation + "/" + FILE_ADDRESS));
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //normalize text representation
        doc.getDocumentElement().normalize();
        return doc;
    }
    //================================================================== 

    public static void main(String args[]) {
    	Document doc =  buildXMLDocument();

        System.out.println("==============================");
        System.out.println("Root element of the doc is " + doc.getDocumentElement().getNodeName());

        System.out.println("==============================");
        NodeList listClass = doc.getElementsByTagName(OBJECTS);
        int totalClass = listClass.getLength();
        System.out.println("Total Objects : " + totalClass);
      
    }
}