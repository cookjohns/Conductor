import processing.core.*;
import de.voidplus.leapmotion.*;

public class GUISketch extends PApplet {
	LeapMotion leap;
	LeapMotion factoryLeap;
	PImage img24 = loadImage("good24.png");
	PImage img34 = loadImage("good34.png");
	PImage img44 = loadImage("good44.png");
	PImage img = img24;
	
	Boolean hasBaton;
	Tool globalTool;
	
	com.leapmotion.leap.Controller controller;
	com.leapmotion.leap.Listener   listener;
	
	public GUISketch(Tool inputTool) {
		this.globalTool = inputTool;
	}
	
	public void changeBackground(int input) {
		if (input == 24) img = img24;
		else if (input == 34) img = img34;
		else img = img44;
	}
		
	@Override
	public void setup() {
		size(600, 550);  // w, h
		//background(255);
		img.resize(600, 550);
		background(img);
		leap = new LeapMotion(this);
		controller = new com.leapmotion.leap.Controller();
		listener = new com.leapmotion.leap.Listener();
		controller.addListener(listener);
	}
	
	@Override
	public void draw() {
		//background(255);
//		img.resize(800, 550);
//		background(img);
//		int fps = leap.getFrameRate();
		hasBaton = null;
		
        if (img == null) {
            background(255);
    } else {
            image(img,0,0, width, height);
    }
		com.leapmotion.leap.Frame frame = controller.frame();
		// ========= HANDS =========
		if (!frame.hands().isEmpty()) {
		for(Hand hand : leap.getHands()) {
			//hasBaton = hand.hasTools();
			
	        int     hand_id          = hand.getId();
	        PVector hand_position    = hand.getPosition();
	        PVector hand_stabilized  = hand.getStabilizedPosition();
	        PVector hand_direction   = hand.getDirection();
	        PVector hand_dynamics    = hand.getDynamics();
	        float   hand_roll        = hand.getRoll();
	        float   hand_pitch       = hand.getPitch();
	        float   hand_yaw         = hand.getYaw();
	        boolean hand_is_left     = hand.isLeft();
	        boolean hand_is_right    = hand.isRight();
	        float   hand_grab        = hand.getGrabStrength();
	        float   hand_pinch       = hand.getPinchStrength();
	        float   hand_time        = hand.getTimeVisible();
	        PVector sphere_position  = hand.getSpherePosition();
	        float   sphere_radius    = hand.getSphereRadius();
	        
	        hand.draw();
	        hand.drawSphere();
	        
	     // ========= FINGERS =========

	        for(Finger finger : hand.getOutstrechtedFingers()){
	        					// was hand.getFingers()
	
	            // ----- BASICS -----
	
	            int     finger_id         = finger.getId();
	            PVector finger_position   = finger.getPosition();
	            PVector finger_stabilized = finger.getStabilizedPosition();
	            PVector finger_velocity   = finger.getVelocity();
	            PVector finger_direction  = finger.getDirection();
	            float   finger_time       = finger.getTimeVisible();
	            
	            // ========= ARM =========

	            if(hand.hasArm()){
	                Arm     arm               = hand.getArm();
	                float   arm_width         = arm.getWidth();
	                PVector arm_wrist_pos     = arm.getWristPosition();
	                PVector arm_elbow_pos     = arm.getElbowPosition();
	            }


	            // ----- SPECIFIC FINGER -----

	            switch(finger.getType()){
	                case 0:
	                    // System.out.println("thumb");
	                    break;
	                case 1:
	                    // System.out.println("index");
	                    break;
	                case 2:
	                    // System.out.println("middle");
	                    break;
	                case 3:
	                    // System.out.println("ring");
	                    break;
	                case 4:
	                    // System.out.println("pinky");
	                    break;
	            }


	            // ----- SPECIFIC BONE -----

	            Bone    bone_distal       = finger.getDistalBone();
	            // or                       finger.get("distal");
	            // or                       finger.getBone(0);

	            Bone    bone_intermediate = finger.getIntermediateBone();
	            // or                       finger.get("intermediate");
	            // or                       finger.getBone(1);

	            Bone    bone_proximal     = finger.getProximalBone();
	            // or                       finger.get("proximal");
	            // or                       finger.getBone(2);

	            Bone    bone_metacarpal   = finger.getMetacarpalBone();
	            // or                       finger.get("metacarpal");
	            // or                       finger.getBone(3);


	            // ----- DRAWING -----

	             finger.draw(20); // = drawLines()+drawJoints()
	             finger.drawLines();
	             finger.drawJoints();
		}
	        // ========= TOOLS =========
	        if (this.globalTool != null) System.out.println("GREAT SUCCESS!!!");
	        //globalTool.draw();
			for(Tool tool : hand.getTools()){
	            int     tool_id           = tool.getId();
	            PVector tool_position     = tool.getPosition();
	            PVector tool_stabilized   = tool.getStabilizedPosition();
	            PVector tool_velocity     = tool.getVelocity();
	            PVector tool_direction    = tool.getDirection();
	            float   tool_time         = tool.getTimeVisible();
	            
//	        Tool tool = null;
//	        if (hand.countTools() > 0) {
//	        	tool = hand.getTools().get(0);
//	        	hasBaton = true;
//	        }
//	            tool.draw();
			}
		}
		} // if frame has no hands
		else {
		//com.leapmotion.leap.Frame frame = controller.frame();
			if (this.globalTool != null) {
				System.out.println("GREAT SUCCESS!!!");
				globalTool.draw();
			}
			if (frame.tools().count() > 0) {
				hasBaton = true;
			}
			else { 
				hasBaton = false;
				//System.out.print(hasBaton);
			}
		}
	}
	
	public void onFrame() {
		hasBaton = true;
	}
	
}
